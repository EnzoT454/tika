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

import java.lang.reflect.Method;

import org.junit.jupiter.api.*;
import org.mockito.*;

public class FilenameUtils_getSuffixFromPath_2_0_Test {

    @BeforeAll
    public static void setUp() throws Exception {
        // Initialize any required dependencies or setup
    }

    @Disabled("ChatUniTest : oracle faux - expected: <> but was: <.txtx>")
    @Test
    public void testGetSuffixFromPath() throws Exception {
        Method method = FilenameUtils.class.getDeclaredMethod("getSuffixFromPath", String.class);
        method.setAccessible(true);
        // Test case 1: Path with valid suffix
        String path1 = "example.txt";
        String result1 = (String) method.invoke(null, path1);
        assertEquals(".txt", result1);
        // Test case 2: Path with invalid suffix
        String path2 = "example.txtx";
        String result2 = (String) method.invoke(null, path2);
        assertEquals("", result2);
        // Test case 3: Path with no suffix
        String path3 = "example";
        String result3 = (String) method.invoke(null, path3);
        assertEquals("", result3);
        // Test case 4: Path with ASCII numeric suffix
        String path4 = ".example";
        String result4 = (String) method.invoke(null, path4);
        assertEquals(".example", result4);
        // Test case 5: Path with long suffix
        String path5 = "example.abcdefg";
        String result5 = (String) method.invoke(null, path5);
        assertEquals("", result5);
        // Test case 6: Path with protocol
        String path6 = "http://example.com/file.txt";
        String result6 = (String) method.invoke(null, path6);
        assertEquals(".txt", result6);
        // Test case 7: Path with reserved characters
        String path7 = "example?file.txt";
        String result7 = (String) method.invoke(null, path7);
        assertEquals(".txt", result7);
    }
}
