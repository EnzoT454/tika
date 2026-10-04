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

public class FilenameUtils_normalize_0_0_Test {

    @Test
    public void testNormalize() {
        FilenameUtils fu = new FilenameUtils();
        String result = fu.normalize("example*file.txt");
        assertEquals("example%2Afile.txt", result);
        result = fu.normalize("example:file.txt");
        assertEquals("example%3Afile.txt", result);
        result = fu.normalize("example<file.txt");
        assertEquals("example%3Cfile.txt", result);
        result = fu.normalize("example>file.txt");
        assertEquals("example%3Efile.txt", result);
        result = fu.normalize("example|file.txt");
        assertEquals("example%7Cfile.txt", result);
        result = fu.normalize("example\"file.txt");
        assertEquals("example%22file.txt", result);
        result = fu.normalize("example'file.txt");
        assertEquals("example%27file.txt", result);
        result = fu.normalize("example.txt");
        assertEquals("example.txt", result);
        result = fu.normalize(".example.txt");
        assertEquals(".example.txt", result);
        result = fu.normalize(".example123.txt");
        assertEquals(".example123.txt", result);
        result = fu.normalize(".example1234.txt");
        assertEquals(".example1234.txt", result);
        result = fu.normalize(".example12345.txt");
        assertEquals(".example12345.txt", result);
        result = fu.normalize(".example123456.txt");
        assertEquals(".example123456.txt", result);
        result = fu.normalize(".example1234567.txt");
        assertEquals(".example1234567.txt", result);
        result = fu.normalize(".example12345678.txt");
        assertEquals(".example12345678.txt", result);
        result = fu.normalize(".example123456789.txt");
        assertEquals(".example123456789.txt", result);
        result = fu.normalize(".example1234567890.txt");
        assertEquals(".example1234567890.txt", result);
        result = fu.normalize(".example12345678901.txt");
        assertEquals(".example12345678901.txt", result);
        result = fu.normalize(".example123456789012.txt");
        assertEquals(".example123456789012.txt", result);
        result = fu.normalize(".example1234567890123.txt");
        assertEquals(".example1234567890123.txt", result);
        result = fu.normalize(".example12345678901234.txt");
        assertEquals(".example12345678901234.txt", result);
        result = fu.normalize(".example123456789012345.txt");
        assertEquals(".example123456789012345.txt", result);
        result = fu.normalize(".example1234567890123456.txt");
        assertEquals(".example1234567890123456.txt", result);
        result = fu.normalize(".example12345678901234567.txt");
        assertEquals(".example12345678901234567.txt", result);
        result = fu.normalize(".example123456789012345678.txt");
        assertEquals(".example123456789012345678.txt", result);
        result = fu.normalize(".example1234567890123456789.txt");
        assertEquals(".example1234567890123456789.txt", result);
        result = fu.normalize(".example12345678901234567890.txt");
    }
}
