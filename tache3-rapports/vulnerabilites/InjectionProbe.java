/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermission;
import java.security.Key;
import java.security.cert.Certificate;
import java.util.*;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

import org.apache.tika.config.JsonConfig;
import org.apache.tika.io.TemporaryResources;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.ExternalProcess;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.*;
import org.apache.tika.parser.external.ExternalParser;
import org.apache.tika.parser.external.ExternalParserConfig;
import org.apache.tika.sax.ToTextContentHandler;
import org.apache.tika.utils.XMLReaderUtils;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

/** Bounded local probes; only dummy data and a synthetic HTTPS connection. */
public class InjectionProbe {
    private static boolean callback;

    public static class SerializedConfig implements JsonConfig, Serializable {
        private static final long serialVersionUID = 1L;
        public String json() { return "{}"; }
        private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
            input.defaultReadObject();
            callback = true;
        }
    }

    private static class FakeHttps extends HttpsURLConnection {
        FakeHttps(URL url) { super(url); }
        public String getCipherSuite() { return "TEST_ONLY"; }
        public Certificate[] getLocalCertificates() { return new Certificate[0]; }
        public Certificate[] getServerCertificates() { return new Certificate[0]; }
        public void disconnect() { }
        public boolean usingProxy() { return false; }
        public void connect() { }
        public String getContentType() { return "text/plain"; }
        public int getContentLength() { return 4; }
        public InputStream getInputStream() { return new ByteArrayInputStream(new byte[4]); }
    }

    public static void main(String[] args) throws Exception {
        if (args.length > 1 && "--v6-only".equals(args[1])) {
            probeV6();
            return;
        }
        Path dir = Path.of(args[0]);
        Files.createDirectories(dir);

        Path input = dir.resolve("input;printf V1_MARKER;#");
        Files.writeString(input, "dummy");
        ExternalParserConfig config = new ExternalParserConfig();
        config.setCommandLine(List.of("printf", "%s", "${INPUT_FILE}"));
        config.setReturnStdout(true);
        Metadata output = new Metadata();
        try (TikaInputStream stream = TikaInputStream.get(input)) {
            new ExternalParser(config).parse(stream, new DefaultHandler(), output, new ParseContext());
        }
        System.out.println("V1=" + output.get(ExternalProcess.STD_OUT).endsWith("V1_MARKER"));

        Path entity = dir.resolve("entity.txt");
        Files.writeString(entity, "V2_MARKER");
        String xml = "<!DOCTYPE root [<!ENTITY item SYSTEM '" + entity.toUri() + "'>]><root>&item;</root>";
        String text = XMLReaderUtils.getDocumentBuilder(new ParseContext())
                .parse(new InputSource(new StringReader(xml))).getDocumentElement().getTextContent();
        System.out.println("V2=" + text.equals("V2_MARKER"));

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream serialized = new ObjectOutputStream(bytes)) {
            serialized.writeObject(new SerializedConfig());
        }
        callback = false;
        new ParseContext().setJsonConfig("probe", "java-serialized:" + Base64.getEncoder().encodeToString(bytes.toByteArray()));
        System.out.println("V3=" + callback);

        FakeHttps[] connection = new FakeHttps[1];
        URL url = new URL(null, "https://expected.invalid/document", new URLStreamHandler() {
            protected URLConnection openConnection(URL target) {
                connection[0] = new FakeHttps(target);
                return connection[0];
            }
        });
        try (TikaInputStream ignored = TikaInputStream.get(url)) { }
        boolean accepts;
        try { accepts = connection[0].getHostnameVerifier().verify("wrong.invalid", null); }
        catch (RuntimeException e) { accepts = false; }
        System.out.println("V4=" + accepts);

        // The zero key protects no real data; it only builds a reproducible fixture.
        Key key = new SecretKeySpec(new byte[16], "AES");
        Cipher encrypt = Cipher.getInstance("AES/ECB/PKCS5Padding");
        encrypt.init(Cipher.ENCRYPT_MODE, key);
        byte[] ciphertext = encrypt.doFinal("V5_MARKER".getBytes(StandardCharsets.UTF_8));
        ParseContext context = new ParseContext();
        context.set(Key.class, key);
        context.set(org.apache.tika.parser.Parser.class, new AbstractParser() {
            public Set<MediaType> getSupportedTypes(ParseContext c) { return Set.of(MediaType.OCTET_STREAM); }
            public void parse(TikaInputStream stream, org.xml.sax.ContentHandler handler, Metadata m, ParseContext c)
                    throws IOException, SAXException {
                char[] chars = new String(stream.readAllBytes(), StandardCharsets.UTF_8).toCharArray();
                handler.characters(chars, 0, chars.length);
            }
        });
        boolean downgraded = false;
        ToTextContentHandler handler = new ToTextContentHandler();
        try (TikaInputStream stream = TikaInputStream.get(ciphertext)) {
            new CryptoParser("AES/GCM/NoPadding", Set.of(MediaType.OCTET_STREAM)) { }
                    .parse(stream, handler, new Metadata(), context);
            downgraded = handler.toString().contains("V5_MARKER");
        } catch (org.apache.tika.exception.TikaException expected) { }
        System.out.println("V5=" + downgraded);

        probeV6();

        try (TemporaryResources resources = new TemporaryResources()) {
            resources.setTemporaryFileDirectory(dir);
            Path temp = resources.createTempFile();
            Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(temp);
            System.out.println("V7=" + (permissions.contains(PosixFilePermission.OTHERS_READ)
                    && permissions.contains(PosixFilePermission.OTHERS_WRITE)));
        }
        Files.delete(input);
        Files.delete(entity);
    }

    private static boolean inlineMatch(String document) throws Exception {
        Metadata result = new Metadata();
        try (TikaInputStream stream = TikaInputStream.get(document.getBytes(StandardCharsets.UTF_8))) {
            new RegexCaptureParser().parse(stream, new DefaultHandler(), result, new ParseContext());
        }
        return "true".equals(result.get("X-TIKA:INLINE_MATCH"));
    }

    private static void probeV6() throws Exception {
        // Linear patterns and four data characters only; no backtracking stress test.
        String document = "regex:^a+$\naaaa\n";
        System.out.println("V6_VALIDATION_LF_COUNT=" + document.chars().filter(c -> c == 10).count());
        System.out.println("V6_VALIDATION_LINE_COUNT=" + document.lines().count());
        System.out.println("V6=" + inlineMatch(document));
        System.out.println("V6_VALIDATION_NEGATIVE=" + inlineMatch("regex:^b+$\naaaa\n"));
        System.out.println("V6_VALIDATION_LITERAL=" + inlineMatch("regex:^a+$\\naaaa\\n"));
    }
}
