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

public class EndianUtils_getUIntBE_26_0_Test {

    @Test
    public void testGetUIntBE() throws IOException, TikaException {
        // Create a mock InputStream
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(4);
        // Create a byte array with test data
        byte[] testData = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01 };
        // Call the getUIntBE method
        long result = EndianUtils.getUIntBE(testData);
        // Verify the result
        assertEquals(1L, result);
    }

    @Test
    public void testGetUIntBEWithOffset() throws IOException, TikaException {
        // Create a mock InputStream
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(4);
        // Create a byte array with test data
        byte[] testData = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01 };
        // Call the getUIntBE method with an offset
        long result = EndianUtils.getUIntBE(testData, 4);
        // Verify the result
        assertEquals(1L, result);
    }
}
