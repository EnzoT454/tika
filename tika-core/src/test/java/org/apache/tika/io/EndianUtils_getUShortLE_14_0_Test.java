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
// 2 methode(s) de test desactivee(s) (@Disabled) car leur oracle est faux : elles echouent sur le code non mute.

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

public class EndianUtils_getUShortLE_14_0_Test {

    @Disabled("ChatUniTest : oracle faux - expected: <1> but was: <256>")
    @Test
    public void testGetUShortLE() {
        try {
            byte[] data = { 0x00, 0x01 };
            int result = EndianUtils.getUShortLE(data);
            assertEquals(1, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Disabled("ChatUniTest : oracle faux - expected: <258> but was: <513>")
    @Test
    public void testGetUShortLEWithOffset() {
        try {
            byte[] data = { 0x00, 0x01, 0x02, 0x03 };
            int result = EndianUtils.getUShortLE(data, 1);
            assertEquals(258, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetUShortLEWithNegativeOffset() {
        try {
            byte[] data = { 0x00, 0x01, 0x02, 0x03 };
            assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getUShortLE(data, -1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetUShortLEWithOffsetExceedingArrayLength() {
        try {
            byte[] data = { 0x00, 0x01, 0x02, 0x03 };
            assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getUShortLE(data, 3));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
