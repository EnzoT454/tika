package org.apache.tika.io;

import org.apache.tika.metadata.Metadata;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.tika.extractor.EmbeddedDocumentUtil;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.utils.StringUtils;

public class FilenameUtils_calculateExtension_10_0_Test {

    @Test
    public void testCalculateExtensionWithMimeType() throws IOException, MimeTypeException {
        Metadata metadata = new Metadata();
        metadata.set(Metadata.CONTENT_TYPE, "image/png");
        String result = FilenameUtils.calculateExtension(metadata, "default.bin");
        assertEquals("png", result);
    }

    @Test
    public void testCalculateExtensionWithNullMimeType() throws IOException, MimeTypeException {
        Metadata metadata = new Metadata();
        String result = FilenameUtils.calculateExtension(metadata, "default.bin");
        assertEquals("default.bin", result);
    }
}
