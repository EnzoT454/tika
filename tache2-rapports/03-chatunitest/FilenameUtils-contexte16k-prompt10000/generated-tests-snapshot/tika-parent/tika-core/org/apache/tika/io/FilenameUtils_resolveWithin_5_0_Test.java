package org.apache.tika.io;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
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

@ExtendWith(MockitoExtension.class)
public class FilenameUtils_resolveWithin_5_0_Test {

    @InjectMocks
    private FilenameUtils filenameUtils;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testResolveWithin() throws IOException {
        Path dir = Paths.get("/tmp");
        Path resolved = filenameUtils.resolveWithin(dir, "test.txt");
        assertEquals(Paths.get("/tmp/test.txt"), resolved);
    }

    @Test
    public void testResolveWithinOutside() throws IOException {
        Path dir = Paths.get("/tmp");
        IOException exception = assertThrows(IOException.class, () -> {
            filenameUtils.resolveWithin(dir, "../etc/passwd");
        });
        assertEquals("'../etc/passwd' resolves to '/etc/passwd', which is outside of '/tmp'", exception.getMessage());
    }

    @Test
    public void testResolveWithinSymlink() throws IOException {
        Path dir = Paths.get("/tmp");
        Path resolved = filenameUtils.resolveWithin(dir, "symlink");
        assertEquals(Paths.get("/tmp/symlink"), resolved);
    }

    @Test
    public void testResolveWithinSymlinkOutside() throws IOException {
        Path dir = Paths.get("/tmp");
        IOException exception = assertThrows(IOException.class, () -> {
            filenameUtils.resolveWithin(dir, "../etc/passwd");
        });
        assertEquals("'../etc/passwd' resolves to '/etc/passwd', which is outside of '/tmp'", exception.getMessage());
    }

    @Test
    public void testResolveWithinReservedCharacters() throws IOException {
        Path dir = Paths.get("/tmp");
        IOException exception = assertThrows(IOException.class, () -> {
            filenameUtils.resolveWithin(dir, "test:txt");
        });
        assertEquals("'test:txt' resolves to '/tmp/test:txt', which is outside of '/tmp'", exception.getMessage());
    }
}
