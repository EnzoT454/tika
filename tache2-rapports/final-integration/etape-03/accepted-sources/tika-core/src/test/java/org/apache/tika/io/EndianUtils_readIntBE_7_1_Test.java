/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tika.io;

// Test genere par ChatUniTest 2.1.1 (qwen2.5-coder:7b via Ollama), derniere tentative, non compilable tel quel.
// Corrections manuelles (2) :
//   - import errone 'import org.apache.tika.io.BufferUnderrunException;' remplace par 'import org.apache.tika.io.EndianUtils.BufferUnderrunException;'
//   - exception verifiee BufferUnderrunException ajoutee a la clause throws de testReadIntBEWithValidInput

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.apache.tika.io.EndianUtils.BufferUnderrunException;

@ExtendWith(MockitoExtension.class)
public class EndianUtils_readIntBE_7_1_Test {

    @Test
    public void testReadIntBEWithValidInput() throws IOException, BufferUnderrunException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04 });
        int result = EndianUtils.readIntBE(inputStream);
        assertEquals(0x01020304, result);
    }


    @Test
    public void testReadIntBEWithNegativeInput() throws IOException, BufferUnderrunException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { -1, 0, 0, 0 });
        assertEquals(0xFF000000, EndianUtils.readIntBE(inputStream));
    }

    @Test
    public void testReadIntBEWithPartialInput() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        when(inputStream.read()).thenReturn(0x01, 0x02, -1, 0);
        assertThrows(BufferUnderrunException.class, () -> EndianUtils.readIntBE(inputStream));
    }
}
