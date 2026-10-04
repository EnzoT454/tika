package org.apache.tika.mime;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ExtendWith(MockitoExtension.class)
public class MediaType_toString_17_1_Test {

    private MediaType mediaType;

    @BeforeEach
    public void setUp() {
        String type = "application";
        String subtype = "json";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("charset", "UTF-8");
        mediaType = new MediaType(type, subtype, parameters);
    }

    @Test
    public void testToString() {
        String expected = "application/json; charset=UTF-8";
        assertEquals(expected, mediaType.toString());
    }
}
