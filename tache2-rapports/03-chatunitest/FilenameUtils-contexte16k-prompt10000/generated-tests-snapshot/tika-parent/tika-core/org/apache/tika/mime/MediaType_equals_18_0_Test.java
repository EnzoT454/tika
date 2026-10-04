package org.apache.tika.mime;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.apache.tika.mime.MediaType;
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
public class MediaType_equals_18_0_Test {

    private MediaType mediaType;

    @BeforeEach
    public void setUp() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("charset", "UTF-8");
        mediaType = new MediaType("text/plain", "utf-8", parameters);
    }

    @Test
    public void testEquals() {
        MediaType mediaType2 = new MediaType("text/plain", "utf-8", Collections.singletonMap("charset", "UTF-8"));
        assertTrue(mediaType.equals(mediaType2));
        MediaType mediaType3 = new MediaType("text/plain", "utf-8", Collections.singletonMap("charset", "UTF-16"));
        assertFalse(mediaType.equals(mediaType3));
        MediaType mediaType4 = new MediaType("text/plain", "utf-8", Collections.emptyMap());
        assertFalse(mediaType.equals(mediaType4));
        assertFalse(mediaType.equals(new Object()));
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(mediaType.equals(null));
    }
}
