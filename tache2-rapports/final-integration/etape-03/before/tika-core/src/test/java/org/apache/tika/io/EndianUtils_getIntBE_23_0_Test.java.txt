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
// 4 methode(s) de test desactivee(s) (@Disabled) car leur oracle est faux : elles echouent sur le code non mute.

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.*;
import org.mockito.*;

public class EndianUtils_getIntBE_23_0_Test {

    @Disabled("ChatUniTest : oracle faux - expected: <65536> but was: <1>")
    @Test
    public void testGetIntBE() throws Exception {
        EndianUtils endianUtils = new EndianUtils();
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x02 };
        int result = (int) getPrivateMethod("getIntBE", EndianUtils.class, byte[].class, int.class).invoke(endianUtils, data, 0);
        assertEquals(65536, result);
    }

    @Disabled("ChatUniTest : oracle faux - expected: <1> but was: <2>")
    @Test
    public void testGetIntBEWithOffset() throws Exception {
        EndianUtils endianUtils = new EndianUtils();
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x02 };
        int result = (int) getPrivateMethod("getIntBE", EndianUtils.class, byte[].class, int.class).invoke(endianUtils, data, 4);
        assertEquals(1, result);
    }

    @Disabled("ChatUniTest : oracle faux - Unexpected exception type thrown, expected: <java.lang.ArrayIndexOutOfBoundsException> but was: <java.lang.reflect.InvocationTargetException>")
    @Test
    public void testGetIntBEWithEmptyArray() throws Exception {
        EndianUtils endianUtils = new EndianUtils();
        byte[] data = new byte[] {};
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            getPrivateMethod("getIntBE", EndianUtils.class, byte[].class, int.class).invoke(endianUtils, data, 0);
        });
    }

    @Disabled("ChatUniTest : oracle faux - Unexpected exception type thrown, expected: <java.lang.ArrayIndexOutOfBoundsException> but was: <java.lang.reflect.InvocationTargetException>")
    @Test
    public void testGetIntBEWithNegativeOffset() throws Exception {
        EndianUtils endianUtils = new EndianUtils();
        byte[] data = new byte[] { 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x02 };
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            getPrivateMethod("getIntBE", EndianUtils.class, byte[].class, int.class).invoke(endianUtils, data, -1);
        });
    }

    private Method getPrivateMethod(String methodName, Class<?> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
        Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method;
    }
}
