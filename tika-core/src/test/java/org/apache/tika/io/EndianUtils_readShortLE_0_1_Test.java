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
// 1 methode(s) de test desactivee(s) (@Disabled) car leur oracle est faux : elles echouent sur le code non mute.
// Corrections manuelles (1) :
//   - import errone 'import org.apache.tika.exception.BufferUnderrunException;' remplace par 'import org.apache.tika.io.EndianUtils.BufferUnderrunException;'

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.EndianUtils.BufferUnderrunException;

@ExtendWith(MockitoExtension.class)
public class EndianUtils_readShortLE_0_1_Test {

    @Test
    public void testReadShortLE() throws IOException, TikaException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 0x12, 0x34 });
        short result = EndianUtils.readShortLE(inputStream);
        assertEquals(0x3412, result);
    }

    @Test
    public void testReadShortLEWithBufferUnderrun() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 0x12 });
        assertThrows(BufferUnderrunException.class, () -> EndianUtils.readShortLE(inputStream));
    }

    @Disabled("ChatUniTest : oracle faux - Expected org.apache.tika.io.EndianUtils.BufferUnderrunException to be thrown, but nothing was thrown.")
    @Test
    public void testReadShortLEWithNegativeBytes() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { -1, -2 });
        assertThrows(BufferUnderrunException.class, () -> EndianUtils.readShortLE(inputStream));
    }
}
