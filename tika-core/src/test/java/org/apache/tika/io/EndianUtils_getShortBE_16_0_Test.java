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
// Toutes les methodes de test reussissent sur le code non mute.

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;


public class EndianUtils_getShortBE_16_0_Test {

    @Test
    public void testGetShortBEWithEmptyArray() {
        byte[] data = {};
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getShortBE(data));
    }

    @Test
    public void testGetShortBEWithSingleElementArray() {
        byte[] data = { (byte) 0x01 };
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getShortBE(data));
    }

    @Test
    public void testGetShortBEWithNegativeOffset() {
        byte[] data = { (byte) 0x01, (byte) 0x00 };
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getShortBE(data, -1));
    }

    @Test
    public void testGetShortBEWithOffsetGreaterThanArrayLength() {
        byte[] data = { (byte) 0x01, (byte) 0x00 };
        assertThrows(IndexOutOfBoundsException.class, () -> EndianUtils.getShortBE(data, 2));
    }
}
