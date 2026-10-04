package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import java.util.Arrays;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;

public class EndianUtils_getIntBE_22_0_Test {

    @Test
    public void testGetIntBE() {
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        int result = EndianUtils.getIntBE(data, 0);
        assertEquals(0x01020304, result);
    }

    @Test
    public void testGetIntBEWithOffset() {
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int result = EndianUtils.getIntBE(data, 4);
        assertEquals(0x05060708, result);
    }

    @Test
    public void testGetIntBEWithEmptyArray() {
        byte[] data = new byte[0];
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> EndianUtils.getIntBE(data, 0));
    }

    @Test
    public void testGetIntBEWithOffsetExceedsArrayLength() {
        byte[] data = new byte[4];
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> EndianUtils.getIntBE(data, 5));
    }
}
