package org.apache.tika.io;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.apache.tika.exception.TikaException;

@ExtendWith(MockitoExtension.class)
public class EndianUtils_getShortLE_12_0_Test {

    @InjectMocks
    private EndianUtils endianUtils;

    @Mock
    private InputStream inputStream;

    @Test
    public void testGetShortLE() throws IOException {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        when(inputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(data.length, -1);
        short result = endianUtils.getShortLE(data);
        assertEquals(258, result);
    }
}
