package org.apache.tika.mime;

import org.apache.tika.mime.MediaType;
import java.nio.charset.Charset;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MediaType_compareTo_20_0_Test {

    @Test
    public void testCompareTo() {
        MediaType mediaType1 = new MediaType("application", "json", Collections.emptyMap());
        MediaType mediaType2 = new MediaType("application", "xml", Collections.emptyMap());
        MediaType mediaType3 = new MediaType("application", "json", Collections.singletonMap("charset", "UTF-8"));
        assertEquals(-1, mediaType1.compareTo(mediaType2));
        assertEquals(1, mediaType2.compareTo(mediaType1));
        assertEquals(0, mediaType1.compareTo(mediaType1));
        assertEquals(1, mediaType1.compareTo(mediaType3));
        assertEquals(-1, mediaType3.compareTo(mediaType1));
    }
}
