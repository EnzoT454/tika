package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import org.junit.jupiter.api.function.Executable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class EndianUtils_ubyteToInt_29_1_Test {

    @Test
    public void testUbyteToInt() throws IOException {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        int result = EndianUtils.ubyteToInt((byte) 0xFF);
        assertEquals(255, result);
    }

    @Test
    public void testUbyteToIntNegative() throws IOException {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        int result = EndianUtils.ubyteToInt((byte) -1);
        assertEquals(255, result);
    }

    @Test
    public void testUbyteToIntZero() throws IOException {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        int result = EndianUtils.ubyteToInt((byte) 0);
        assertEquals(0, result);
    }

    @Test
    public void testUbyteToIntMax() throws IOException {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        int result = EndianUtils.ubyteToInt((byte) 127);
        assertEquals(127, result);
    }

    @Test
    public void testUbyteToIntMin() throws IOException {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        int result = EndianUtils.ubyteToInt((byte) -128);
        assertEquals(128, result);
    }

    @Test
    public void testUbyteToIntNull() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            EndianUtils.ubyteToInt((byte) 0);
        });
    }

    @Test
    public void testUbyteToIntEmpty() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            EndianUtils.ubyteToInt((byte) 0);
        });
    }

    @Test
    public void testUbyteToIntInvalid() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            EndianUtils.ubyteToInt((byte) 256);
        });
    }

    @Test
    public void testUbyteToIntLarge() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            EndianUtils.ubyteToInt((byte) 1024);
        });
    }

    @Test
    public void testUbyteToIntSmall() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            EndianUtils.ubyteToInt((byte) -1024);
        });
    }

    @Test
    public void testUbyteToIntBoundary() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            EndianUtils.ubyteToInt((byte) 128);
        });
    }

    @Test
    public void testUbyteToIntBoundaryNegative() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            EndianUtils.ubyteToInt((byte) -128);
        });
    }

    @Test
    public void testUbyteToIntBoundaryPositive() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            EndianUtils.ubyteToInt((byte) 127);
        });
    }

    @Test
    public void testUbyteToIntBoundaryNegativePositive() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            EndianUtils.ubyteToInt((byte) -127);
        });
    }

    @Test
    public void testUbyteToIntBoundaryPositiveNegative() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            EndianUtils.ubyteToInt((byte) 126);
        });
    }

    @Test
    public void testUbyteToIntBoundaryNegativePositiveNegative() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            EndianUtils.ubyteToInt((byte) -126);
        });
    }
}
