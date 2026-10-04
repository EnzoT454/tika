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
// 11 methode(s) de test desactivee(s) (@Disabled) car leur oracle est faux : elles echouent sur le code non mute.

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.*;
import org.mockito.*;

public class EndianUtils_ubyteToInt_29_1_Test {

    @Test
    public void testUbyteToInt() throws IOException {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        int result = EndianUtils.ubyteToInt((byte) 0xFF);
        assertEquals(255, result);
    }

    @Test
    public void testUbyteToIntNegative() throws IOException {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        int result = EndianUtils.ubyteToInt((byte) -1);
        assertEquals(255, result);
    }

    @Test
    public void testUbyteToIntZero() throws IOException {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        int result = EndianUtils.ubyteToInt((byte) 0);
        assertEquals(0, result);
    }

    @Test
    public void testUbyteToIntMax() throws IOException {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        int result = EndianUtils.ubyteToInt((byte) 127);
        assertEquals(127, result);
    }

    @Test
    public void testUbyteToIntMin() throws IOException {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        int result = EndianUtils.ubyteToInt((byte) -128);
        assertEquals(128, result);
    }

    @Disabled("ChatUniTest : oracle faux - Expected java.lang.NullPointerException to be thrown, but nothing was thrown.")
    @Test
    public void testUbyteToIntNull() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            EndianUtils.ubyteToInt((byte) 0);
        });
    }

    @Disabled("ChatUniTest : oracle faux - Expected java.lang.NullPointerException to be thrown, but nothing was thrown.")
    @Test
    public void testUbyteToIntEmpty() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            EndianUtils.ubyteToInt((byte) 0);
        });
    }

    @Disabled("ChatUniTest : oracle faux - Expected java.lang.NullPointerException to be thrown, but nothing was thrown.")
    @Test
    public void testUbyteToIntInvalid() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            EndianUtils.ubyteToInt((byte) 256);
        });
    }

    @Disabled("ChatUniTest : oracle faux - Expected java.lang.NullPointerException to be thrown, but nothing was thrown.")
    @Test
    public void testUbyteToIntLarge() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            EndianUtils.ubyteToInt((byte) 1024);
        });
    }

    @Disabled("ChatUniTest : oracle faux - Expected java.lang.NullPointerException to be thrown, but nothing was thrown.")
    @Test
    public void testUbyteToIntSmall() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            EndianUtils.ubyteToInt((byte) -1024);
        });
    }

    @Disabled("ChatUniTest : oracle faux - Expected java.lang.NullPointerException to be thrown, but nothing was thrown.")
    @Test
    public void testUbyteToIntBoundary() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            EndianUtils.ubyteToInt((byte) 128);
        });
    }

    @Disabled("ChatUniTest : oracle faux - Expected java.lang.NullPointerException to be thrown, but nothing was thrown.")
    @Test
    public void testUbyteToIntBoundaryNegative() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            EndianUtils.ubyteToInt((byte) -128);
        });
    }

    @Disabled("ChatUniTest : oracle faux - Expected java.lang.NullPointerException to be thrown, but nothing was thrown.")
    @Test
    public void testUbyteToIntBoundaryPositive() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            EndianUtils.ubyteToInt((byte) 127);
        });
    }

    @Disabled("ChatUniTest : oracle faux - Expected java.lang.NullPointerException to be thrown, but nothing was thrown.")
    @Test
    public void testUbyteToIntBoundaryNegativePositive() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            EndianUtils.ubyteToInt((byte) -127);
        });
    }

    @Disabled("ChatUniTest : oracle faux - Expected java.lang.NullPointerException to be thrown, but nothing was thrown.")
    @Test
    public void testUbyteToIntBoundaryPositiveNegative() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            EndianUtils.ubyteToInt((byte) 126);
        });
    }

    @Disabled("ChatUniTest : oracle faux - Expected java.lang.NullPointerException to be thrown, but nothing was thrown.")
    @Test
    public void testUbyteToIntBoundaryNegativePositiveNegative() throws IOException {
        assertThrows(NullPointerException.class, () -> {
            EndianUtils.ubyteToInt((byte) -126);
        });
    }
}
