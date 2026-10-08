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
// Corrections manuelles (1) :
//   - import errone 'import org.apache.tika.io.BufferUnderrunException;' remplace par 'import org.apache.tika.io.EndianUtils.BufferUnderrunException;'

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.apache.tika.io.EndianUtils.BufferUnderrunException;

@ExtendWith(MockitoExtension.class)
public class EndianUtils_readShortBE_1_1_Test {

    @Mock
    private InputStream inputStream;


    @Test
    public void testReadShortBE() throws IOException, BufferUnderrunException {
        // Arrange
        // Simulate reading 0x1234 and then end of stream
        when(inputStream.read()).thenReturn(0x12, 0x34, -1);
        EndianUtils endianUtils = new EndianUtils();
        // Act and Assert
        assertEquals((short) 0x1234, endianUtils.readShortBE(inputStream));
    }

    @Test
    public void testReadShortBEWithValidData() throws IOException, BufferUnderrunException {
        // Arrange
        // Simulate reading 0x1234
        when(inputStream.read()).thenReturn(0x12, 0x34);
        EndianUtils endianUtils = new EndianUtils();
        // Act
        short result = endianUtils.readShortBE(inputStream);
        // Assert
        assertEquals(0x1234, result);
    }

    @Test
    public void testReadUShortBE() throws IOException, BufferUnderrunException {
        // Arrange
        // Simulate reading 0x1234
        when(inputStream.read()).thenReturn(0x12, 0x34);
        EndianUtils endianUtils = new EndianUtils();
        // Act
        int result = endianUtils.readUShortBE(inputStream);
        // Assert
        assertEquals(0x1234, result);
    }

    @Test
    public void testReadUShortBEWithEndOfStream() throws IOException, BufferUnderrunException {
        // Arrange
        // Simulate reading 0x12 and then end of stream
        when(inputStream.read()).thenReturn(0x12, -1);
        EndianUtils endianUtils = new EndianUtils();
        // Act and Assert
        assertThrows(BufferUnderrunException.class, () -> {
            endianUtils.readUShortBE(inputStream);
        });
    }
}
