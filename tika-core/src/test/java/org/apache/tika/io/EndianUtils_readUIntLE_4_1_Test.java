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
// Corrections manuelles (2) :
//   - import errone 'import org.apache.tika.exception.BufferUnderrunException;' remplace par 'import org.apache.tika.io.EndianUtils.BufferUnderrunException;'

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.apache.tika.io.EndianUtils.BufferUnderrunException;

@ExtendWith(MockitoExtension.class)
public class EndianUtils_readUIntLE_4_1_Test {

    @Mock
    private InputStream inputStream;

    private EndianUtils endianUtils;

    @BeforeEach
    public void setUp() {
        endianUtils = new EndianUtils();
    }

    @Test
    public void testReadUIntLEWithValidInput() throws IOException, BufferUnderrunException {
        when(inputStream.read()).thenReturn(0x12, 0x34, 0x56, 0x78);
        long result = endianUtils.readUIntLE(inputStream);
        assertEquals(0x78563412L, result);
    }

    @Test
    public void testReadUIntLEWithBufferUnderrun() throws IOException {
        when(inputStream.read()).thenReturn(0x12, 0x34, 0x56, -1);
        assertThrows(BufferUnderrunException.class, () -> {
            endianUtils.readUIntLE(inputStream);
        });
    }

    @Test
    public void testReadUIntLEWithNegativeInput() throws IOException, BufferUnderrunException {
        when(inputStream.read()).thenReturn(0x12, 0x34, 0x56, 0x80);
        long result = endianUtils.readUIntLE(inputStream);
        assertEquals(0x80563412L, result);
    }
}
