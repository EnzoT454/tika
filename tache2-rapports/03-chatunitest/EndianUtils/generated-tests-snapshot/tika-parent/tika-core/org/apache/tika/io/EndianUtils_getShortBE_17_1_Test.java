package org.apache.tika.io;

import org.junit.jupiter.api.function.Executable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.apache.tika.exception.TikaException;

public class EndianUtils_getShortBE_17_1_Test {

    @Test
    public void testGetShortBE() throws Exception {
        // Test data
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int offset = 0;
        short expected = 0x0201;
        // Get the private method using reflection
        Method method = EndianUtils.class.getDeclaredMethod("getShortBE", byte[].class, int.class);
        method.setAccessible(true);
        // Invoke the private method
        short result = (short) method.invoke(null, data, offset);
        // Verify the result
        assertEquals(expected, result);
    }

    @Test
    public void testGetShortBEWithInvalidOffset() throws Exception {
        // Test data
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int offset = 10;
        short expected = 0;
        // Get the private method using reflection
        Method method = EndianUtils.class.getDeclaredMethod("getShortBE", byte[].class, int.class);
        method.setAccessible(true);
        // Invoke the private method and verify that it throws an exception
        Executable executable = () -> method.invoke(null, data, offset);
        assertThrows(IndexOutOfBoundsException.class, executable);
    }

    @Test
    public void testGetShortBEWithNullData() throws Exception {
        // Test data
        byte[] data = null;
        int offset = 0;
        short expected = 0;
        // Get the private method using reflection
        Method method = EndianUtils.class.getDeclaredMethod("getShortBE", byte[].class, int.class);
        method.setAccessible(true);
        // Invoke the private method and verify that it throws an exception
        Executable executable = () -> method.invoke(null, data, offset);
        assertThrows(NullPointerException.class, executable);
    }
}
