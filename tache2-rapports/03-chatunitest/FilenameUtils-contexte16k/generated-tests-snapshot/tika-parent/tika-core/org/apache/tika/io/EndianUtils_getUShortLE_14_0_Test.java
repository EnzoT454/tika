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

public class EndianUtils_getUShortLE_14_0_Test {

    @Test
    public void testGetUShortLE() {
        try {
            byte[] data = { 0x00, 0x01 };
            int result = EndianUtils.getUShortLE(data);
            assertEquals(1, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetUShortLEWithOffset() {
        try {
            byte[] data = { 0x00, 0x01, 0x02, 0x03 };
            int result = EndianUtils.getUShortLE(data, 1);
            assertEquals(258, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetUShortLEWithNegativeOffset() {
        try {
            byte[] data = { 0x00, 0x01, 0x02, 0x03 };
            assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getUShortLE(data, -1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetUShortLEWithOffsetExceedingArrayLength() {
        try {
            byte[] data = { 0x00, 0x01, 0x02, 0x03 };
            assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getUShortLE(data, 3));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
