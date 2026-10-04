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

public class FilenameUtils_getSanitizedEmbeddedFilePath_4_0_Test {

    private Metadata metadata;

    private FilenameUtils filenameUtils;

    @BeforeEach
    public void setUp() {
        metadata = mock(Metadata.class);
        filenameUtils = new FilenameUtils();
    }

    @Test
    public void testGetSanitizedEmbeddedFilePath_EmbeddedPathNull() throws IOException {
        when(metadata.get(TikaCoreProperties.EMBEDDED_RESOURCE_PATH)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.INTERNAL_PATH)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.EMBEDDED_RELATIONSHIP_ID)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.ORIGINAL_RESOURCE_NAME)).thenReturn(null);
        String result = filenameUtils.getSanitizedEmbeddedFilePath(metadata, "default", 100);
        assertNull(result);
    }

    @Test
    public void testGetSanitizedEmbeddedFilePath_EmbeddedPathBlank() throws IOException {
        when(metadata.get(TikaCoreProperties.EMBEDDED_RESOURCE_PATH)).thenReturn("");
        when(metadata.get(TikaCoreProperties.INTERNAL_PATH)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.EMBEDDED_RELATIONSHIP_ID)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.ORIGINAL_RESOURCE_NAME)).thenReturn(null);
        String result = filenameUtils.getSanitizedEmbeddedFilePath(metadata, "default", 100);
        assertNull(result);
    }

    @Test
    public void testGetSanitizedEmbeddedFilePath_EmbeddedPathProtocol() throws IOException {
        when(metadata.get(TikaCoreProperties.EMBEDDED_RESOURCE_PATH)).thenReturn("http://example.com/path");
        when(metadata.get(TikaCoreProperties.INTERNAL_PATH)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.EMBEDDED_RELATIONSHIP_ID)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.ORIGINAL_RESOURCE_NAME)).thenReturn(null);
        String result = filenameUtils.getSanitizedEmbeddedFilePath(metadata, "default", 100);
        assertEquals("example.com/path", result);
    }

    @Test
    public void testGetSanitizedEmbeddedFilePath_EmbeddedPathProtocolAndDefaultExtension() throws IOException {
        when(metadata.get(TikaCoreProperties.EMBEDDED_RESOURCE_PATH)).thenReturn("http://example.com/path");
        when(metadata.get(TikaCoreProperties.INTERNAL_PATH)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.EMBEDDED_RELATIONSHIP_ID)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.ORIGINAL_RESOURCE_NAME)).thenReturn(null);
        String result = filenameUtils.getSanitizedEmbeddedFilePath(metadata, "txt", 100);
        assertEquals("example.com/path.txt", result);
    }

    @Test
    public void testGetSanitizedEmbeddedFilePath_EmbeddedPathProtocolAndMaxLength() throws IOException {
        when(metadata.get(TikaCoreProperties.EMBEDDED_RESOURCE_PATH)).thenReturn("http://example.com/path/to/very/long/file/name");
        when(metadata.get(TikaCoreProperties.INTERNAL_PATH)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.EMBEDDED_RELATIONSHIP_ID)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.ORIGINAL_RESOURCE_NAME)).thenReturn(null);
        String result = filenameUtils.getSanitizedEmbeddedFilePath(metadata, "txt", 10);
        assertEquals("httpexamplecompath_to_very_long_file_name.txt", result);
    }
}
