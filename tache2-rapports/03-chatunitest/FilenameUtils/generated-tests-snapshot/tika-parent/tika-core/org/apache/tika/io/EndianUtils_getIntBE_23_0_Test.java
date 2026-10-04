package org.apache.tika.io;

import org.junit.jupiter.api.function.Executable;
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

public class EndianUtils_getIntBE_23_0_Test {

    @Test
    public void testGetIntBE() throws Exception {
        EndianUtils endianUtils = new EndianUtils();
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x02 };
        int result = (int) getPrivateMethod("getIntBE", EndianUtils.class, byte[].class, int.class).invoke(endianUtils, data, 0);
        assertEquals(65536, result);
    }

    @Test
    public void testGetIntBEWithOffset() throws Exception {
        EndianUtils endianUtils = new EndianUtils();
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x02 };
        int result = (int) getPrivateMethod("getIntBE", EndianUtils.class, byte[].class, int.class).invoke(endianUtils, data, 4);
        assertEquals(1, result);
    }

    @Test
    public void testGetIntBEWithEmptyArray() throws Exception {
        EndianUtils endianUtils = new EndianUtils();
        byte[] data = new byte[] {};
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            getPrivateMethod("getIntBE", EndianUtils.class, byte[].class, int.class).invoke(endianUtils, data, 0);
        });
    }

    @Test
    public void testGetIntBEWithNegativeOffset() throws Exception {
        EndianUtils endianUtils = new EndianUtils();
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x02 };
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            getPrivateMethod("getIntBE", EndianUtils.class, byte[].class, int.class).invoke(endianUtils, data, -1);
        });
    }

    private Method getPrivateMethod(String methodName, Class<?> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
        Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method;
    }
}
