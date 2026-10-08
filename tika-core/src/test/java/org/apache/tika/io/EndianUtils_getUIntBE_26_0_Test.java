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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import org.apache.tika.exception.TikaException;

public class EndianUtils_getUIntBE_26_0_Test {



    @Test
    public void testGetUIntBEWithOffset() throws IOException, TikaException {
        // Create a mock InputStream
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(4);
        // Create a byte array with test data
        byte[] testData = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01 };
        // Call the getUIntBE method with an offset
        long result = EndianUtils.getUIntBE(testData, 4);
        // Verify the result
        assertEquals(1L, result);
    }
}
