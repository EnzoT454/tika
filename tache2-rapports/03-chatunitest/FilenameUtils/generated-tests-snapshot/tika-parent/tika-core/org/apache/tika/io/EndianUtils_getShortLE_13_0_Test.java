package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import org.junit.jupiter.api.function.Executable;
import java.io.IOException;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;

public class EndianUtils_getShortLE_13_0_Test {

    @Test
    public void testGetShortLE() throws IOException {
        byte[] data = { 0x01, 0x02 };
        int offset = 0;
        short expected = 0x0201;
        short result = EndianUtils.getShortLE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetShortLEWithNegativeOffset() {
        byte[] data = { 0x01, 0x02 };
        int offset = -1;
        Executable executable = () -> EndianUtils.getShortLE(data, offset);
        assertThrows(IndexOutOfBoundsException.class, executable);
    }

    @Test
    public void testGetShortLEWithOffsetExceedingArrayLength() {
        byte[] data = { 0x01, 0x02 };
        int offset = 2;
        Executable executable = () -> EndianUtils.getShortLE(data, offset);
        assertThrows(IndexOutOfBoundsException.class, executable);
    }

    @Test
    public void testGetShortLEWithEmptyArray() {
        byte[] data = {};
        int offset = 0;
        Executable executable = () -> EndianUtils.getShortLE(data, offset);
        assertThrows(IndexOutOfBoundsException.class, executable);
    }
}
