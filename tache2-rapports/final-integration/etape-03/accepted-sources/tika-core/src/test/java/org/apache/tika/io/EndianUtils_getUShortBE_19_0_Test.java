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
// Compilait avant integration ; selection/corrections humaines tracees dans etape-03.

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

public class EndianUtils_getUShortBE_19_0_Test {


    @Test
    public void testGetUShortBE() {
        byte[] data = new byte[] { 0x00, 0x01 };
        int offset = 0;
        int expected = 1;
        int result = EndianUtils.getUShortBE(data, offset);
        assertEquals(expected, result);
    }


    @Test
    public void testGetUShortBEWithOffset() {
        byte[] data = new byte[] { 0x01, 0x00, 0x00, 0x01 };
        int offset = 1;
        int expected = 0;
        int result = EndianUtils.getUShortBE(data, offset);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUShortBEWithNegativeOffset() {
        byte[] data = new byte[] { 0x00, 0x01 };
        int offset = -1;
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            EndianUtils.getUShortBE(data, offset);
        });
        assertEquals("Index -1 out of bounds for length 2", exception.getMessage());
    }

    @Test
    public void testGetUShortBEWithTooLargeOffset() {
        byte[] data = new byte[] { 0x00, 0x01 };
        int offset = 2;
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            EndianUtils.getUShortBE(data, offset);
        });
        assertEquals("Index 2 out of bounds for length 2", exception.getMessage());
    }
}
