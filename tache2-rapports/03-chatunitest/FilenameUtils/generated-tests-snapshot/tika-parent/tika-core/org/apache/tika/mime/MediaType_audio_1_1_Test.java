package org.apache.tika.mime;

import org.apache.tika.mime.MediaType;
import org.junit.jupiter.api.function.Executable;
import java.lang.reflect.Method;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MediaType_audio_1_1_Test {

    private Method audioMethod;

    private MediaType mediaType;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        audioMethod = MediaType.class.getDeclaredMethod("audio", String.class);
        audioMethod.setAccessible(true);
        mediaType = new MediaType("audio", "mpeg", new HashMap<>());
    }

    @Test
    public void testAudioMethod() throws Exception {
        // Test with valid audio subtype
        MediaType result = (MediaType) audioMethod.invoke(mediaType, "mpeg");
        assertEquals("audio/mpeg", result.toString());
        // Test with null subtype
        Executable executable = () -> audioMethod.invoke(mediaType, null);
        assertThrows(NullPointerException.class, executable);
        // Test with empty subtype
        executable = () -> audioMethod.invoke(mediaType, "");
        assertThrows(IllegalArgumentException.class, executable);
        // Test with invalid subtype
        executable = () -> audioMethod.invoke(mediaType, "invalid");
        assertThrows(IllegalArgumentException.class, executable);
    }
}
