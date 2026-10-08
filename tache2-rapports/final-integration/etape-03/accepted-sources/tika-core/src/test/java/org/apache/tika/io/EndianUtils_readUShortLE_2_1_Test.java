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
//   - import errone 'import org.apache.tika.exception.BufferUnderrunException;' remplace par 'import org.apache.tika.io.EndianUtils.BufferUnderrunException;'

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.EndianUtils.BufferUnderrunException;

public class EndianUtils_readUShortLE_2_1_Test {

    @Test
    public void testReadUShortLE() throws IOException, TikaException {
        EndianUtils endianness = new EndianUtils();
        byte[] data = new byte[] { (byte) 0x12, (byte) 0x34 };
        InputStream inputStream = new ByteArrayInputStream(data);
        int result = endianness.readUShortLE(inputStream);
        assertEquals(0x3412, result);
    }

    @Test
    public void testReadUShortLEWithBufferUnderrun() throws IOException, TikaException {
        EndianUtils endianness = new EndianUtils();
        byte[] data = new byte[] { (byte) 0x12 };
        InputStream inputStream = new ByteArrayInputStream(data);
        Executable executable = () -> endianness.readUShortLE(inputStream);
        assertThrows(BufferUnderrunException.class, executable);
    }
}
