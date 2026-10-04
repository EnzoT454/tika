package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

public class EndianUtils_getUIntBE_27_0_Test {

    @Test
    public void testGetUIntBE() throws IOException, TikaException {
        byte[] data = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01 };
        int offset = 0;
        long result = EndianUtils.getUIntBE(data, offset);
        assertEquals(1L, result);
    }

    @Test
    public void testGetUIntBEWithNegativeNumber() throws IOException, TikaException {
        byte[] data = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
        int offset = 0;
        long result = EndianUtils.getUIntBE(data, offset);
        assertEquals(4294967295L, result);
    }

    @Test
    public void testGetUIntBEWithZero() throws IOException, TikaException {
        byte[] data = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
        int offset = 0;
        long result = EndianUtils.getUIntBE(data, offset);
        assertEquals(0L, result);
    }

    @Test
    public void testGetUIntBEWithLargeNumber() throws IOException, TikaException {
        byte[] data = { (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
        int offset = 0;
        long result = EndianUtils.getUIntBE(data, offset);
        assertEquals(4294967295L, result);
    }

    @Test
    public void testGetUIntBEWithOffset() throws IOException, TikaException {
        byte[] data = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
        int offset = 4;
        long result = EndianUtils.getUIntBE(data, offset);
        assertEquals(1L, result);
    }
}
