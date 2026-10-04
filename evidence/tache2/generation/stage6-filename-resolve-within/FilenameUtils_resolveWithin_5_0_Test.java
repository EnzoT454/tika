package org.apache.tika.io;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.utils.StringUtils;

public class FilenameUtils_resolveWithin_5_0_Test {

    private Path dir;

    private Path name;

    @BeforeEach
    public void setUp() {
        dir = Paths.get("/path/to/directory");
        name = Paths.get("file.txt");
    }

    @Test
    public void testResolveWithinWithinSameDirectory() throws IOException {
        Path resolved = FilenameUtils.resolveWithin(dir, name.toString());
        assertEquals(dir.resolve(name), resolved);
    }

    @Test
    public void testResolveWithinOutsideDirectory() throws IOException {
        Path outsideDir = Paths.get("/path/to/outside/directory");
        Path resolved = FilenameUtils.resolveWithin(outsideDir, name.toString());
        assertEquals(outsideDir.resolve(name), resolved);
    }

    @Test
    public void testResolveWithinThrowsIOException() throws IOException {
        Path outsideDir = Paths.get("/path/to/outside/directory");
        Path resolved = FilenameUtils.resolveWithin(outsideDir, name.toString());
        assertEquals(outsideDir.resolve(name), resolved);
    }
}
