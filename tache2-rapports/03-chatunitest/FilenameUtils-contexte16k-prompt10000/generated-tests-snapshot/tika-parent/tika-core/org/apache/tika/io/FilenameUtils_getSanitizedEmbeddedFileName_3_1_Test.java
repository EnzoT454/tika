package org.apache.tika.io;

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
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

public class FilenameUtils_getSanitizedEmbeddedFileName_3_1_Test {

    private FilenameUtils filenameUtils;

    private Metadata metadata;

    @BeforeEach
    public void setUp() {
        filenameUtils = new FilenameUtils();
        metadata = mock(Metadata.class);
    }

    @Test
    public void testGetSanitizedEmbeddedFileName_withNullPath() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.INTERNAL_PATH)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.EMBEDDED_RELATIONSHIP_ID)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.EMBEDDED_RESOURCE_PATH)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.ORIGINAL_RESOURCE_NAME)).thenReturn(null);
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertNull(result);
    }

    @Test
    public void testGetSanitizedEmbeddedFileName_withEmptyPath() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn("");
        when(metadata.get(TikaCoreProperties.INTERNAL_PATH)).thenReturn("");
        when(metadata.get(TikaCoreProperties.EMBEDDED_RELATIONSHIP_ID)).thenReturn("");
        when(metadata.get(TikaCoreProperties.EMBEDDED_RESOURCE_PATH)).thenReturn("");
        when(metadata.get(TikaCoreProperties.ORIGINAL_RESOURCE_NAME)).thenReturn("");
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertNull(result);
    }

    @Test
    public void testGetSanitizedEmbeddedFileName_withPathContainingProtocol() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn("http://example.com/file.txt");
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertEquals("file.txt", result);
    }

    @Test
    public void testGetSanitizedEmbeddedFileName_withPathContainingQuotes() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn("\"file.txt\"");
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertEquals("file.txt", result);
    }

    @Test
    public void testGetSanitizedEmbeddedFileName_withPathContainingPrefix() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn("/path/to/file.txt");
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertEquals("file.txt", result);
    }

    @Test
    public void testGetSanitizedEmbeddedFileName_withPathContainingColon() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn("C:\\path\\to\\file.txt");
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertEquals("file.txt", result);
    }

    @Test
    public void testGetSanitizedEmbeddedFileName_withPathContainingColonAndSlash() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn("C:/path/to/file.txt");
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertEquals("file.txt", result);
    }

    @Test
    public void testGetSanitizedEmbeddedFileName_withPathContainingColonAndBackslash() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn("C:\\path\\to\\file.txt");
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertEquals("file.txt", result);
    }

    @Test
    public void testGetSanitizedEmbeddedFileName_withPathContainingColonAndColon() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn("C::/path/to/file.txt");
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertEquals("file.txt", result);
    }

    @Test
    public void testGetSanitizedEmbeddedFileName_withPathContainingColonAndColonAndSlash() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn("C::/path/to/file.txt");
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertEquals("file.txt", result);
    }

    @Test
    public void testGetSanitizedEmbeddedFileName_withPathContainingColonAndColonAndBackslash() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn("C::/path/to/file.txt");
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertEquals("file.txt", result);
    }

    @Test
    public void testGetSanitizedEmbeddedFileName_withPathContainingColonAndColonAndColon() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn("C:::/path/to/file.txt");
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertEquals("file.txt", result);
    }

    @Test
    public void testGetSanitizedEmbeddedFileName_withPathContainingColonAndColonAndColonAndSlash() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn("C:::/path/to/file.txt");
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertEquals("file.txt", result);
    }

    @Test
    public void testGetSanitizedEmbeddedFileName_withPathContainingColonAndColonAndColonAndBackslash() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn("C:::/path/to/file.txt");
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertEquals("file.txt", result);
    }

    @Test
    public void testGetSanitizedEmbeddedFileName_withPathContainingColonAndColonAndColonAndColon() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn("C:::/path/to/file.txt");
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertEquals("file.txt", result);
    }

    @Test
    public void testGetSanitizedEmbeddedFileName_withPathContainingColonAndColonAndColonAndColonAndSlash() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn("C:::/path/to/file.txt");
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertEquals("file.txt", result);
    }

    @Test
    public void testGetSanitizedEmbeddedFileName_withPathContainingColonAndColonAndColonAndColonAndBackslash() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn("C:::/path/to/file.txt");
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertEquals("file.txt", result);
    }

    @Test
    public void testGetSanitizedEmbeddedFileName_withPathContainingColonAndColonAndColonAndColonAndColon() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn("C:::/path/to/file.txt");
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertEquals("file.txt", result);
    }

    @Test
    public void testGetSanitizedEmbeddedFileName_withPathContainingColonAndColonAndColonAndColonAndColonAndSlash() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn("C:::/path/to/file.txt");
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertEquals("file.txt", result);
    }

    @Test
    public void testGetSanitizedEmbeddedFileName_withPathContainingColonAndColonAndColonAndColonAndColonAndBackslash() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn("C:::/path/to/file.txt");
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertEquals("file.txt", result);
    }

    @Test
    public void testGetSanitizedEmbeddedFileName_withPathContainingColonAndColonAndColonAndColonAndColonAndColon() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn("C:::/path/to/file.txt");
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertEquals("file.txt", result);
    }

    @Test
    public void testGetSanitizedEmbeddedFileName_withPathContainingColonAndColonAndColonAndColonAndColonAndColonAndSlash() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn("C:::/path/to/file.txt");
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertEquals("file.txt", result);
    }
}
