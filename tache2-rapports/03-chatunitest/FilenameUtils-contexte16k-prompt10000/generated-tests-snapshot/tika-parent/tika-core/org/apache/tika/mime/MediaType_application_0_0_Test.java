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

public class MediaType_application_0_0_Test {

    @Test
    public void testGetBaseType() {
        MediaType mediaType = new MediaType("application", "json");
        assertEquals("application", mediaType.getBaseType().getType());
    }

    @Test
    public void testGetType() {
        MediaType mediaType = new MediaType("application", "json");
        assertEquals("application", mediaType.getType());
    }

    @Test
    public void testGetSubtype() {
        MediaType mediaType = new MediaType("application", "json");
        assertEquals("json", mediaType.getSubtype());
    }

    @Test
    public void testGetParameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("charset", "UTF-8");
        MediaType mediaType = new MediaType("text", "html", parameters);
        assertEquals("UTF-8", mediaType.getParameters().get("charset"));
    }
}
