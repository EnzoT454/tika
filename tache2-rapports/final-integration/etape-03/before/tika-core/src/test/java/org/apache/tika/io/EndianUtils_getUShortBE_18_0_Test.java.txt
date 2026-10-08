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

// Test genere par ChatUniTest 2.1.1 (qwen2.5-coder:7b via Ollama).
// Exporte tel quel par ChatUniTest (compilait sans intervention).
// 1 methode(s) de test desactivee(s) (@Disabled) car leur oracle est faux : elles echouent sur le code non mute.

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;

public class EndianUtils_getUShortBE_18_0_Test {

    @Test
    public void testGetUShortBE() {
        byte[] data = new byte[] { 0x01, 0x02 };
        int result = EndianUtils.getUShortBE(data);
        assertEquals(258, result);
    }

    @Disabled("ChatUniTest : oracle faux - expected: <514> but was: <515>")
    @Test
    public void testGetUShortBEWithOffset() {
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        int result = EndianUtils.getUShortBE(data, 1);
        assertEquals(514, result);
    }

    @Test
    public void testGetUShortBEWithEmptyArray() {
        byte[] data = new byte[0];
        Executable executable = () -> EndianUtils.getUShortBE(data);
        assertThrows(IndexOutOfBoundsException.class, executable);
    }

    @Test
    public void testGetUShortBEWithNegativeOffset() {
        byte[] data = new byte[] { 0x01, 0x02 };
        Executable executable = () -> EndianUtils.getUShortBE(data, -1);
        assertThrows(IndexOutOfBoundsException.class, executable);
    }
}
