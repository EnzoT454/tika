package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;

public class EndianUtils_getUShortLE_15_0_Test {

    @Test
    public void testGetUShortLE() {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int offset = 0;
        int expected = 0x0201;
        int result = EndianUtils.getUShortLE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUShortLEWithOffset() {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int offset = 2;
        int expected = 0x0403;
        int result = EndianUtils.getUShortLE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUShortLEWithLargeOffset() {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int offset = 6;
        int expected = 0x0807;
        int result = EndianUtils.getUShortLE(data, offset);
        assertEquals(expected, result);
    }
}
