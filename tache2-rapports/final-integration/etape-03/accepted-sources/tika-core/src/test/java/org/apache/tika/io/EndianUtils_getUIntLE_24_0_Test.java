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

import java.io.IOException;

import org.junit.jupiter.api.*;
import org.mockito.*;

public class EndianUtils_getUIntLE_24_0_Test {


    @Test
    public void testGetUIntLE() throws IOException {
        byte[] data = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01 };
        long result = EndianUtils.getUIntLE(data);
        assertEquals(0L, result);
    }


    @Test
    public void testGetUIntLEWithOffset() throws IOException {
        byte[] data = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00 };
        long result = EndianUtils.getUIntLE(data, 4);
        assertEquals(0x00010000L, result);
    }
}
