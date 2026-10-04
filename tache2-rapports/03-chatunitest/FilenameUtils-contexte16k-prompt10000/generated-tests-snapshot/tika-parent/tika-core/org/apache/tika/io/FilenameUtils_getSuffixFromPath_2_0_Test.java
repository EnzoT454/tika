package org.apache.tika.io;

import org.apache.tika.io.FilenameUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.tika.extractor.EmbeddedDocumentUtil;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.utils.StringUtils;
import java.lang.reflect.Method;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

public class FilenameUtils_getSuffixFromPath_2_0_Test {

    @BeforeAll
    public static void setUp() throws Exception {
        // Initialize any required dependencies or setup
    }

    @Test
    public void testGetSuffixFromPath() throws Exception {
        Method method = FilenameUtils.class.getDeclaredMethod("getSuffixFromPath", String.class);
        method.setAccessible(true);
        // Test case 1: Path with valid suffix
        String path1 = "example.txt";
        String result1 = (String) method.invoke(null, path1);
        assertEquals(".txt", result1);
        // Test case 2: Path with invalid suffix
        String path2 = "example.txtx";
        String result2 = (String) method.invoke(null, path2);
        assertEquals("", result2);
        // Test case 3: Path with no suffix
        String path3 = "example";
        String result3 = (String) method.invoke(null, path3);
        assertEquals("", result3);
        // Test case 4: Path with ASCII numeric suffix
        String path4 = ".example";
        String result4 = (String) method.invoke(null, path4);
        assertEquals(".example", result4);
        // Test case 5: Path with long suffix
        String path5 = "example.abcdefg";
        String result5 = (String) method.invoke(null, path5);
        assertEquals("", result5);
        // Test case 6: Path with protocol
        String path6 = "http://example.com/file.txt";
        String result6 = (String) method.invoke(null, path6);
        assertEquals(".txt", result6);
        // Test case 7: Path with reserved characters
        String path7 = "example?file.txt";
        String result7 = (String) method.invoke(null, path7);
        assertEquals(".txt", result7);
    }
}
