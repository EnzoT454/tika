package org.apache.tika.mime;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.tika.mime.MediaType;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashSet;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MediaType_text_3_1_Test {

    @Test
    public void testTextMethod() {
        // Test with a valid subtype
        MediaType mediaType = MediaType.text("html");
        assertNotNull(mediaType);
        assertEquals("text/html", mediaType.toString());
        // Test with a null subtype
        mediaType = MediaType.text(null);
        assertNull(mediaType);
        // Test with an empty subtype
        mediaType = MediaType.text("");
        assertEquals("text/plain", mediaType.toString());
        // Test with a subtype containing special characters
        mediaType = MediaType.text("text/html; charset=UTF-8");
        assertNotNull(mediaType);
        assertEquals("text/html; charset=UTF-8", mediaType.toString());
    }
}
