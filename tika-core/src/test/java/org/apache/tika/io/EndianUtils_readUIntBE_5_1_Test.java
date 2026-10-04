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
// Corrections manuelles (3) :
//   - appel via ReflectionTestUtils (Spring, absent du projet) remplace par un appel direct a EndianUtils.readUIntBE
//   - appel via ReflectionTestUtils (Spring, absent du projet) remplace par un appel direct a EndianUtils.readUIntBE
//   - import manquant ajoute : import org.apache.tika.io.EndianUtils.BufferUnderrunException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.EndianUtils.BufferUnderrunException;

@ExtendWith(MockitoExtension.class)
public class EndianUtils_readUIntBE_5_1_Test {

    private EndianUtils endianUtils;

    @BeforeEach
    public void setUp() {
        endianUtils = new EndianUtils();
    }

    @Test
    public void testReadUIntBE() throws IOException, TikaException {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        InputStream inputStream = new ByteArrayInputStream(data);
        long result = EndianUtils.readUIntBE(inputStream);
        assertEquals(0x01020304L, result);
    }

    @Test
    public void testReadUIntBEWithBufferUnderrun() throws IOException, TikaException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        Mockito.when(inputStream.read()).thenReturn(-1);
        Executable executable = () -> EndianUtils.readUIntBE(inputStream);
        assertThrows(BufferUnderrunException.class, executable);
    }
}
