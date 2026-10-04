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

import org.junit.jupiter.api.*;
import org.mockito.*;

import org.apache.tika.utils.StringUtils;

public class FilenameUtils_getName_1_0_Test {

    @Test
    public void testGetNameWithNullPath() throws Exception {
        String path = null;
        String result = invokePrivateMethod(FilenameUtils.class, "getName", path);
        assertEquals(StringUtils.EMPTY, result);
    }

    @Test
    public void testGetNameWithEmptyPath() throws Exception {
        String path = "";
        String result = invokePrivateMethod(FilenameUtils.class, "getName", path);
        assertEquals(StringUtils.EMPTY, result);
    }

    @Test
    public void testGetNameWithUnixPath() throws Exception {
        String path = "/home/user/documents/report.pdf";
        String result = invokePrivateMethod(FilenameUtils.class, "getName", path);
        assertEquals("report.pdf", result);
    }

    @Test
    public void testGetNameWithWindowsPath() throws Exception {
        String path = "C:\\Users\\user\\Documents\\report.pdf";
        String result = invokePrivateMethod(FilenameUtils.class, "getName", path);
        assertEquals("report.pdf", result);
    }

    @Test
    public void testGetNameWithColonPath() throws Exception {
        String path = "C:somefilename";
        String result = invokePrivateMethod(FilenameUtils.class, "getName", path);
        assertEquals("somefilename", result);
    }

    @Disabled("ChatUniTest : oracle faux - expected: <> but was: <report.pdf>")
    @Test
    public void testGetNameWithParentDirectory() throws Exception {
        String path = "/home/user/documents/../report.pdf";
        String result = invokePrivateMethod(FilenameUtils.class, "getName", path);
        assertEquals(StringUtils.EMPTY, result);
    }

    @Disabled("ChatUniTest : oracle faux - expected: <> but was: <report.pdf>")
    @Test
    public void testGetNameWithCurrentDirectory() throws Exception {
        String path = "/home/user/documents/./report.pdf";
        String result = invokePrivateMethod(FilenameUtils.class, "getName", path);
        assertEquals(StringUtils.EMPTY, result);
    }

    @Disabled("ChatUniTest : oracle faux - expected: <report.pdf> but was: <report?pdf>")
    @Test
    public void testGetNameWithReservedCharacters() throws Exception {
        String path = "/home/user/documents/report?pdf";
        String result = invokePrivateMethod(FilenameUtils.class, "getName", path);
        assertEquals("report.pdf", result);
    }

    @Disabled("ChatUniTest : oracle faux - expected: <> but was: <.abcde>")
    @Test
    public void testGetNameWithASCIINumeric() throws Exception {
        String path = "/home/user/documents/.abcde";
        String result = invokePrivateMethod(FilenameUtils.class, "getName", path);
        assertEquals(StringUtils.EMPTY, result);
    }

    @Test
    public void testGetNameWithMultipleDelimiters() throws Exception {
        String path = "/home/user/documents/report.pdf?name=example";
        String result = invokePrivateMethod(FilenameUtils.class, "getName", path);
        assertEquals("report.pdf?name=example", result);
    }

    @Test
    public void testGetNameWithMacintoshDelimiter() throws Exception {
        String path = "/home/user/documents:report.pdf";
        String result = invokePrivateMethod(FilenameUtils.class, "getName", path);
        assertEquals("report.pdf", result);
    }

    private static String invokePrivateMethod(Class<?> clazz, String methodName, Object... args) throws Exception {
        java.lang.reflect.Method method = clazz.getDeclaredMethod(methodName, String.class);
        method.setAccessible(true);
        return (String) method.invoke(null, args);
    }
}
