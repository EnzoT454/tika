package org.apache.tika.io;

import org.apache.tika.io.FilenameUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.utils.StringUtils;
import org.junit.jupiter.api.function.Executable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.apache.tika.extractor.EmbeddedDocumentUtil;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

public class FilenameUtils_getSanitizedEmbeddedFilePath_4_0_Test {

    private FilenameUtils filenameUtils;

    private Metadata metadata;

    private String defaultExtension;

    private int maxLength;

    @BeforeEach
    public void setUp() {
        filenameUtils = new FilenameUtils();
        metadata = mock(Metadata.class);
        defaultExtension = "test";
        maxLength = 100;
    }

    @Test
    public void testGetSanitizedEmbeddedFilePath_withNullPath() throws IOException {
        when(metadata.get(TikaCoreProperties.EMBEDDED_RESOURCE_PATH)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.INTERNAL_PATH)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.EMBEDDED_RELATIONSHIP_ID)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.ORIGINAL_RESOURCE_NAME)).thenReturn(null);
        String result = filenameUtils.getSanitizedEmbeddedFilePath(metadata, defaultExtension, maxLength);
        assertNull(result);
    }

    @Test
    public void testGetSanitizedEmbeddedFilePath_withBlankPath() throws IOException {
        when(metadata.get(TikaCoreProperties.EMBEDDED_RESOURCE_PATH)).thenReturn("  ");
        when(metadata.get(TikaCoreProperties.INTERNAL_PATH)).thenReturn("");
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.EMBEDDED_RELATIONSHIP_ID)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.ORIGINAL_RESOURCE_NAME)).thenReturn(null);
        String result = filenameUtils.getSanitizedEmbeddedFilePath(metadata, defaultExtension, maxLength);
        assertNull(result);
    }

    @Test
    public void testGetSanitizedEmbeddedFilePath_withInvalidPath() throws IOException {
        when(metadata.get(TikaCoreProperties.EMBEDDED_RESOURCE_PATH)).thenReturn("invalid/path");
        when(metadata.get(TikaCoreProperties.INTERNAL_PATH)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.EMBEDDED_RELATIONSHIP_ID)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.ORIGINAL_RESOURCE_NAME)).thenReturn(null);
        String result = filenameUtils.getSanitizedEmbeddedFilePath(metadata, defaultExtension, maxLength);
        assertNull(result);
    }

    @Test
    public void testGetSanitizedEmbeddedFilePath_withValidPath() throws IOException {
        when(metadata.get(TikaCoreProperties.EMBEDDED_RESOURCE_PATH)).thenReturn("valid/path");
        when(metadata.get(TikaCoreProperties.INTERNAL_PATH)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.EMBEDDED_RELATIONSHIP_ID)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.ORIGINAL_RESOURCE_NAME)).thenReturn(null);
        String result = filenameUtils.getSanitizedEmbeddedFilePath(metadata, defaultExtension, maxLength);
        assertNotNull(result);
    }

    @Test
    public void testGetSanitizedEmbeddedFilePath_withLongPath() throws IOException {
        when(metadata.get(TikaCoreProperties.EMBEDDED_RESOURCE_PATH)).thenReturn("a".repeat(1000));
        when(metadata.get(TikaCoreProperties.INTERNAL_PATH)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.EMBEDDED_RELATIONSHIP_ID)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.ORIGINAL_RESOURCE_NAME)).thenReturn(null);
        String result = filenameUtils.getSanitizedEmbeddedFilePath(metadata, defaultExtension, maxLength);
        assertNotNull(result);
        assertTrue(result.length() <= maxLength);
    }

    @Test
    public void testGetSanitizedEmbeddedFilePath_withReservedCharacters() throws IOException {
        when(metadata.get(TikaCoreProperties.EMBEDDED_RESOURCE_PATH)).thenReturn("invalid:filename");
        when(metadata.get(TikaCoreProperties.INTERNAL_PATH)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.EMBEDDED_RELATIONSHIP_ID)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.ORIGINAL_RESOURCE_NAME)).thenReturn(null);
        String result = filenameUtils.getSanitizedEmbeddedFilePath(metadata, defaultExtension, maxLength);
        assertNotNull(result);
        assertFalse(result.contains(":"));
    }

    @Test
    public void testGetSanitizedEmbeddedFilePath_withProtocol() throws IOException {
    }
}
