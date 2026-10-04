package org.apache.tika.io;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.lang.reflect.Method;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;

@ExtendWith(MockitoExtension.class)
public class EndianUtils_getLongLE_28_0_Test {

    @Test
    public void testGetLongLE() throws Exception {
        EndianUtils endianUtils = new EndianUtils();
        byte[] data = new byte[] { (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x9A, (byte) 0xBC, (byte) 0xDE, (byte) 0xF0 };
        int offset = 0;
        Method method = EndianUtils.class.getDeclaredMethod("getLongLE", byte[].class, int.class);
        method.setAccessible(true);
        long result = (long) method.invoke(endianUtils, data, offset);
        assertEquals(0xF0DEBC9A78563412L, result);
    }
}
