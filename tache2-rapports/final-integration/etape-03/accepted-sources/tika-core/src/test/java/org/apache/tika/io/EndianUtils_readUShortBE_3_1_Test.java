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
// Toutes les methodes de test reussissent sur le code non mute.
// Corrections manuelles (1) :
//   - import manquant ajoute : import org.apache.tika.io.EndianUtils.BufferUnderrunException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.EndianUtils.BufferUnderrunException;

@ExtendWith(MockitoExtension.class)
public class EndianUtils_readUShortBE_3_1_Test {

    @Test
    public void testReadUShortBE() throws IOException, TikaException {
        // Arrange
        byte[] data = { (byte) 0x12, (byte) 0x34 };
        InputStream inputStream = new ByteArrayInputStream(data);
        EndianUtils endianUtils = new EndianUtils();
        // Act
        int result = endianUtils.readUShortBE(inputStream);
        // Assert
        assertEquals(0x1234, result);
    }

    @Test
    public void testReadUShortBE_WithBufferUnderrun() throws IOException, TikaException {
        // Arrange
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.read()).thenReturn(-1);
        EndianUtils endianUtils = new EndianUtils();
        // Act & Assert
        assertThrows(BufferUnderrunException.class, () -> {
            endianUtils.readUShortBE(inputStream);
        });
    }
}
