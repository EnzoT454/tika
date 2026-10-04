package org.apache.tika.io;

import org.junit.jupiter.api.function.Executable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.apache.tika.extractor.EmbeddedDocumentUtil;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.utils.StringUtils;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class FilenameUtils_getName_1_0_Test {

    @Test
    public void testGetNameWithNullPath() throws Exception {
        String path = null;
        String result = invokePrivateMethod(FilenameUtils.class, "getName", path);
        assertEquals(StringUtils.EMPTY, result);
    }

    @Test
    public void testGetNameWithEmptyPath() throws Exception {
        String path = "";
        String result = invokePrivateMethod(FilenameUtils.class, "getName", path);
        assertEquals(StringUtils.EMPTY, result);
    }

    @Test
    public void testGetNameWithUnixPath() throws Exception {
        String path = "/home/user/documents/report.pdf";
        String result = invokePrivateMethod(FilenameUtils.class, "getName", path);
        assertEquals("report.pdf", result);
    }

    @Test
    public void testGetNameWithWindowsPath() throws Exception {
        String path = "C:\\Users\\user\\Documents\\report.pdf";
        String result = invokePrivateMethod(FilenameUtils.class, "getName", path);
        assertEquals("report.pdf", result);
    }

    @Test
    public void testGetNameWithColonPath() throws Exception {
        String path = "C:somefilename";
        String result = invokePrivateMethod(FilenameUtils.class, "getName", path);
        assertEquals("somefilename", result);
    }

    @Test
    public void testGetNameWithParentDirectory() throws Exception {
        String path = "/home/user/documents/../report.pdf";
        String result = invokePrivateMethod(FilenameUtils.class, "getName", path);
        assertEquals(StringUtils.EMPTY, result);
    }

    @Test
    public void testGetNameWithCurrentDirectory() throws Exception {
        String path = "/home/user/documents/./report.pdf";
        String result = invokePrivateMethod(FilenameUtils.class, "getName", path);
        assertEquals(StringUtils.EMPTY, result);
    }

    @Test
    public void testGetNameWithReservedCharacters() throws Exception {
        String path = "/home/user/documents/report?pdf";
        String result = invokePrivateMethod(FilenameUtils.class, "getName", path);
        assertEquals("report.pdf", result);
    }

    @Test
    public void testGetNameWithASCIINumeric() throws Exception {
        String path = "/home/user/documents/.abcde";
        String result = invokePrivateMethod(FilenameUtils.class, "getName", path);
        assertEquals(StringUtils.EMPTY, result);
    }

    @Test
    public void testGetNameWithMultipleDelimiters() throws Exception {
        String path = "/home/user/documents/report.pdf?name=example";
        String result = invokePrivateMethod(FilenameUtils.class, "getName", path);
        assertEquals("report.pdf?name=example", result);
    }

    @Test
    public void testGetNameWithMacintoshDelimiter() throws Exception {
        String path = "/home/user/documents:report.pdf";
        String result = invokePrivateMethod(FilenameUtils.class, "getName", path);
        assertEquals("report.pdf", result);
    }

    private static String invokePrivateMethod(Class<?> clazz, String methodName, Object... args) throws Exception {
        java.lang.reflect.Method method = clazz.getDeclaredMethod(methodName, String.class);
        method.setAccessible(true);
        return (String) method.invoke(null, args);
    }
}
