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

public class EndianUtils_getUShortBE_18_0_Test {

    @Test
    public void testGetUShortBE() {
        byte[] data = new byte[] { 0x01, 0x02 };
        int result = EndianUtils.getUShortBE(data);
        assertEquals(258, result);
    }

    @Test
    public void testGetUShortBEWithOffset() {
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        int result = EndianUtils.getUShortBE(data, 1);
        assertEquals(514, result);
    }

    @Test
    public void testGetUShortBEWithEmptyArray() {
        byte[] data = new byte[0];
        Executable executable = () -> EndianUtils.getUShortBE(data);
        assertThrows(IndexOutOfBoundsException.class, executable);
    }

    @Test
    public void testGetUShortBEWithNegativeOffset() {
        byte[] data = new byte[] { 0x01, 0x02 };
        Executable executable = () -> EndianUtils.getUShortBE(data, -1);
        assertThrows(IndexOutOfBoundsException.class, executable);
    }
}
