package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import org.apache.tika.exception.TikaException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class EndianUtils_readUE7_11_0_Test {

    @Test
    public void testReadUE7() throws IOException, TikaException {
        EndianUtils endianUtils = new EndianUtils();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x7F, 0x00 });
        long result = endianUtils.readUE7(inputStream);
        assertEquals(127, result);
    }

    @Test
    public void testReadUE7_MultipleBytes() throws IOException, TikaException {
        EndianUtils endianUtils = new EndianUtils();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x81, 0x00 });
        long result = endianUtils.readUE7(inputStream);
        assertEquals(129, result);
    }

    @Test
    public void testReadUE7_MaxValue() throws IOException, TikaException {
        EndianUtils endianUtils = new EndianUtils();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7F });
        long result = endianUtils.readUE7(inputStream);
        assertEquals(0x7FFFFFFFFFFFFFFFL, result);
    }

    @Test
    public void testReadUE7_BufferUnderun() throws IOException, TikaException {
        EndianUtils endianUtils = new EndianUtils();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x80 });
        assertThrows(IOException.class, () -> endianUtils.readUE7(inputStream));
    }
}
