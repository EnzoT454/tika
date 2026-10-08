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
import org.mockito.*;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.EndianUtils.BufferUnderrunException;

public class EndianUtils_readIntME_8_1_Test {

    @Disabled("ChatUniTest : oracle faux - expected: <305419896> but was: <873625686>")
    @Test
    public void testReadIntME() throws IOException, TikaException {
        // Test case 1: Normal case
        byte[] data1 = { 0x12, 0x34, 0x56, 0x78 };
        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(data1);
        int result1 = EndianUtils.readIntME(inputStream1);
        assertEquals(0x12345678, result1);
        // Test case 2: End of stream
        byte[] data2 = {};
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(data2);
        assertThrows(BufferUnderrunException.class, () -> EndianUtils.readIntME(inputStream2));
        // Test case 3: Negative values
        byte[] data3 = { (byte) 0xFF, (byte) 0xFE, (byte) 0xFD, (byte) 0xFC };
        ByteArrayInputStream inputStream3 = new ByteArrayInputStream(data3);
        assertThrows(BufferUnderrunException.class, () -> EndianUtils.readIntME(inputStream3));
    }
}
