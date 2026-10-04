package org.apache.tika.mime;

import org.apache.tika.mime.MediaType;
import org.junit.jupiter.api.function.Executable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MediaType_hasParameters_15_0_Test {

    @Test
    public void testHasParametersWithNoParameters() {
        MediaType mediaType = new MediaType("text", "plain");
        assertFalse(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithParameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("charset", "UTF-8");
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertTrue(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithEmptyParameters() {
        Map<String, String> parameters = Collections.emptyMap();
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertFalse(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithOneParameter() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("one", "value");
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertTrue(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithMultipleParameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("one", "value1");
        parameters.put("two", "value2");
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertTrue(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithEmptyParameterName() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("", "value");
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertTrue(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithEmptyParameterValue() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("one", "");
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertTrue(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithWhitespaceParameterName() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(" one ", "value");
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertTrue(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithWhitespaceParameterValue() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("one", " value ");
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertTrue(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithSpecialParameterName() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("one!", "value");
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertTrue(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithSpecialParameterValue() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("one", "value!");
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertTrue(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithEmptyStringParameterName() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("", "value");
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertTrue(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithEmptyStringParameterValue() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("one", "");
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertTrue(mediaType.hasParameters());
    }
}
