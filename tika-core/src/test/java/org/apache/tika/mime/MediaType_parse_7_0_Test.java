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
package org.apache.tika.mime;

// Test genere par ChatUniTest 2.1.1 (qwen2.5-coder:7b via Ollama).
// Exporte tel quel par ChatUniTest (compilait sans intervention).
// 2 methode(s) de test desactivee(s) (@Disabled) car leur oracle est faux : elles echouent sur le code non mute.

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

public class MediaType_parse_7_0_Test {

    @Test
    public void testParseNull() {
        assertNull(MediaType.parse(null));
    }

    @Test
    public void testParseSimpleType() {
        MediaType mediaType = MediaType.parse("text/plain");
        assertNotNull(mediaType);
        assertEquals("text", mediaType.getType());
        assertEquals("plain", mediaType.getSubtype());
        assertTrue(mediaType.getParameters().isEmpty());
    }

    @Test
    public void testParseComplexType() {
        MediaType mediaType = MediaType.parse("image/png; charset=utf-8");
        assertNotNull(mediaType);
        assertEquals("image", mediaType.getType());
        assertEquals("png", mediaType.getSubtype());
        assertEquals("utf-8", mediaType.getParameters().get("charset"));
    }

    @Disabled("ChatUniTest : oracle faux - expected: <null> but was: <invalid/type>")
    @Test
    public void testParseInvalidType() {
        assertNull(MediaType.parse("invalid/type"));
    }

    @Test
    public void testParseEmptyString() {
        assertNull(MediaType.parse(""));
    }

    @Test
    public void testParseWhitespace() {
        assertNull(MediaType.parse(" "));
    }

    @Test
    public void testParseSimpleTypeWithParameters() {
        MediaType mediaType = MediaType.parse("text/plain; param=value");
        assertNotNull(mediaType);
        assertEquals("text", mediaType.getType());
        assertEquals("plain", mediaType.getSubtype());
        assertEquals("value", mediaType.getParameters().get("param"));
    }

    @Test
    public void testParseComplexTypeWithParameters() {
        MediaType mediaType = MediaType.parse("image/png; charset=utf-8; param=value");
        assertNotNull(mediaType);
        assertEquals("image", mediaType.getType());
        assertEquals("png", mediaType.getSubtype());
        assertEquals("utf-8", mediaType.getParameters().get("charset"));
        assertEquals("value", mediaType.getParameters().get("param"));
    }

    @Test
    public void testParseParametersWithQuotes() {
        MediaType mediaType = MediaType.parse("image/png; charset=\"utf-8\"; param=\"value with spaces\"");
        assertNotNull(mediaType);
        assertEquals("image", mediaType.getType());
        assertEquals("png", mediaType.getSubtype());
        assertEquals("utf-8", mediaType.getParameters().get("charset"));
        assertEquals("value with spaces", mediaType.getParameters().get("param"));
    }

    @Disabled("ChatUniTest : oracle faux - attendait la valeur dequotee utf-8 pour un charset entoure de guillemets echappes ; unquote ne retire pas les guillemets echappes")
    @Test
    public void testParseParametersWithEscapedQuotes() {
        MediaType mediaType = MediaType.parse("image/png; charset=\\\"utf-8\\\"; param=\\\"value with spaces\\\"");
        assertNotNull(mediaType);
        assertEquals("image", mediaType.getType());
        assertEquals("png", mediaType.getSubtype());
        assertEquals("\"utf-8\"", mediaType.getParameters().get("charset"));
        assertEquals("\"value with spaces\"", mediaType.getParameters().get("param"));
    }

    @Test
    public void testParseParametersWithEmptyValues() {
        MediaType mediaType = MediaType.parse("image/png; charset=; param=");
        assertNotNull(mediaType);
        assertEquals("image", mediaType.getType());
        assertEquals("png", mediaType.getSubtype());
        assertEquals("", mediaType.getParameters().get("charset"));
        assertEquals("", mediaType.getParameters().get("param"));
    }

    @Test
    public void testParseParametersWithLeadingTrailingSpaces() {
        MediaType mediaType = MediaType.parse("image/png; charset = utf-8 ; param = value");
        assertNotNull(mediaType);
        assertEquals("image", mediaType.getType());
        assertEquals("png", mediaType.getSubtype());
        assertEquals("utf-8", mediaType.getParameters().get("charset"));
        assertEquals("value", mediaType.getParameters().get("param"));
    }

    @Test
    public void testParseParametersWithMultipleSemicolons() {
        MediaType mediaType = MediaType.parse("image/png; charset=utf-8;; param=value");
        assertNotNull(mediaType);
        assertEquals("image", mediaType.getType());
        assertEquals("png", mediaType.getSubtype());
        assertEquals("utf-8", mediaType.getParameters().get("charset"));
        assertEquals("value", mediaType.getParameters().get("param"));
    }

    @Test
    public void testParseParametersWithMultipleEquals() {
        MediaType mediaType = MediaType.parse("image/png; charset=utf-8=; param=value=");
        assertNotNull(mediaType);
        assertEquals("image", mediaType.getType());
        assertEquals("png", mediaType.getSubtype());
        assertEquals("utf-8=", mediaType.getParameters().get("charset"));
        assertEquals("value=", mediaType.getParameters().get("param"));
    }
}
