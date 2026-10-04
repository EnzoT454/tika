package org.apache.tika.io;

import org.apache.tika.io.EndianUtils;
import java.lang.reflect.Method;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;

public class EndianUtils_getIntLE_21_0_Test {

    @Test
    public void testGetIntLE() throws Exception {
        EndianUtils endianness = new EndianUtils();
        Method method = endianness.getClass().getDeclaredMethod("getIntLE", byte[].class, int.class);
        method.setAccessible(true);
        byte[] data = new byte[4];
        data[0] = (byte) 0x01;
        data[1] = (byte) 0x02;
        data[2] = (byte) 0x03;
        data[3] = (byte) 0x04;
        int result = (int) method.invoke(endianness, data, 0);
        assertEquals(0x04030201, result);
    }
}
