package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;

public class EndianUtils_getUShortBE_19_0_Test {

    @Test
    public void testGetUShortBE() {
        byte[] data = new byte[] { 0x00, 0x01 };
        int offset = 0;
        int expected = 256;
        int result = EndianUtils.getUShortBE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUShortBEWithOffset() {
        byte[] data = new byte[] { 0x01, 0x00, 0x00, 0x01 };
        int offset = 1;
        int expected = 256;
        int result = EndianUtils.getUShortBE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUShortBEWithNegativeOffset() {
        byte[] data = new byte[] { 0x00, 0x01 };
        int offset = -1;
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            EndianUtils.getUShortBE(data, offset);
        });
        assertEquals("Index -1 out of bounds for length 2", exception.getMessage());
    }

    @Test
    public void testGetUShortBEWithTooLargeOffset() {
        byte[] data = new byte[] { 0x00, 0x01 };
        int offset = 2;
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            EndianUtils.getUShortBE(data, offset);
        });
        assertEquals("Index 2 out of bounds for length 2", exception.getMessage());
    }
}
