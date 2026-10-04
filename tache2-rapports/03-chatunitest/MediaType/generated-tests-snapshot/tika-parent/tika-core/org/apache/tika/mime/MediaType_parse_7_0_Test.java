package org.apache.tika.mime;

import org.apache.tika.mime.MediaType;
import org.junit.jupiter.api.function.Executable;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
