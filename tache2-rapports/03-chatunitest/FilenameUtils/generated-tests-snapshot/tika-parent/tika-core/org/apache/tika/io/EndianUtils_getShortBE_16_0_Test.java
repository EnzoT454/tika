package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import java.io.IOException;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;

public class EndianUtils_getShortBE_16_0_Test {

    @Test
    public void testGetShortBEWithEmptyArray() {
        byte[] data = {};
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getShortBE(data));
    }

    @Test
    public void testGetShortBEWithSingleElementArray() {
        byte[] data = { (byte) 0x01 };
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getShortBE(data));
    }

    @Test
    public void testGetShortBEWithNegativeOffset() {
        byte[] data = { (byte) 0x01, (byte) 0x00 };
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getShortBE(data, -1));
    }

    @Test
    public void testGetShortBEWithOffsetGreaterThanArrayLength() {
        byte[] data = { (byte) 0x01, (byte) 0x00 };
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getShortBE(data, 2));
    }
}
