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
// 3 methode(s) de test desactivee(s) (@Disabled) car leur oracle est faux : elles echouent sur le code non mute.

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;

public class EndianUtils_getShortBE_17_1_Test {

    @Disabled("ChatUniTest : oracle faux - expected: <513> but was: <258>")
    @Test
    public void testGetShortBE() throws Exception {
        // Test data
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int offset = 0;
        short expected = 0x0201;
        // Get the private method using reflection
        Method method = EndianUtils.class.getDeclaredMethod("getShortBE", byte[].class, int.class);
        method.setAccessible(true);
        // Invoke the private method
        short result = (short) method.invoke(null, data, offset);
        // Verify the result
        assertEquals(expected, result);
    }

    @Disabled("ChatUniTest : oracle faux - Unexpected exception type thrown, expected: <java.lang.IndexOutOfBoundsException> but was: <java.lang.reflect.InvocationTargetException>")
    @Test
    public void testGetShortBEWithInvalidOffset() throws Exception {
        // Test data
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int offset = 10;
        short expected = 0;
        // Get the private method using reflection
        Method method = EndianUtils.class.getDeclaredMethod("getShortBE", byte[].class, int.class);
        method.setAccessible(true);
        // Invoke the private method and verify that it throws an exception
        Executable executable = () -> method.invoke(null, data, offset);
        assertThrows(IndexOutOfBoundsException.class, executable);
    }

    @Disabled("ChatUniTest : oracle faux - Unexpected exception type thrown, expected: <java.lang.NullPointerException> but was: <java.lang.reflect.InvocationTargetException>")
    @Test
    public void testGetShortBEWithNullData() throws Exception {
        // Test data
        byte[] data = null;
        int offset = 0;
        short expected = 0;
        // Get the private method using reflection
        Method method = EndianUtils.class.getDeclaredMethod("getShortBE", byte[].class, int.class);
        method.setAccessible(true);
        // Invoke the private method and verify that it throws an exception
        Executable executable = () -> method.invoke(null, data, offset);
        assertThrows(NullPointerException.class, executable);
    }
}
