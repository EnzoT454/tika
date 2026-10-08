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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


public class FilenameUtils_normalize_0_0_Test {

    @Test
    public void testNormalize() {
        FilenameUtils fu = new FilenameUtils();
        String result = fu.normalize("example*file.txt");
        assertEquals("example%2Afile.txt", result);
        result = fu.normalize("example:file.txt");
        assertEquals("example%3Afile.txt", result);
        result = fu.normalize("example<file.txt");
        assertEquals("example%3Cfile.txt", result);
        result = fu.normalize("example>file.txt");
        assertEquals("example%3Efile.txt", result);
        result = fu.normalize("example|file.txt");
        assertEquals("example%7Cfile.txt", result);
        result = fu.normalize("example\"file.txt");
        assertEquals("example%22file.txt", result);
        result = fu.normalize("example'file.txt");
        assertEquals("example%27file.txt", result);
        result = fu.normalize("example.txt");
        assertEquals("example.txt", result);
        result = fu.normalize(".example.txt");
        assertEquals(".example.txt", result);
        result = fu.normalize(".example123.txt");
        assertEquals(".example123.txt", result);
        result = fu.normalize(".example1234.txt");
        assertEquals(".example1234.txt", result);
        result = fu.normalize(".example12345.txt");
        assertEquals(".example12345.txt", result);
        result = fu.normalize(".example123456.txt");
        assertEquals(".example123456.txt", result);
        result = fu.normalize(".example1234567.txt");
        assertEquals(".example1234567.txt", result);
        result = fu.normalize(".example12345678.txt");
        assertEquals(".example12345678.txt", result);
        result = fu.normalize(".example123456789.txt");
        assertEquals(".example123456789.txt", result);
        result = fu.normalize(".example1234567890.txt");
        assertEquals(".example1234567890.txt", result);
        result = fu.normalize(".example12345678901.txt");
        assertEquals(".example12345678901.txt", result);
        result = fu.normalize(".example123456789012.txt");
        assertEquals(".example123456789012.txt", result);
        result = fu.normalize(".example1234567890123.txt");
        assertEquals(".example1234567890123.txt", result);
        result = fu.normalize(".example12345678901234.txt");
        assertEquals(".example12345678901234.txt", result);
        result = fu.normalize(".example123456789012345.txt");
        assertEquals(".example123456789012345.txt", result);
        result = fu.normalize(".example1234567890123456.txt");
        assertEquals(".example1234567890123456.txt", result);
        result = fu.normalize(".example12345678901234567.txt");
        assertEquals(".example12345678901234567.txt", result);
        result = fu.normalize(".example123456789012345678.txt");
        assertEquals(".example123456789012345678.txt", result);
        result = fu.normalize(".example1234567890123456789.txt");
        assertEquals(".example1234567890123456789.txt", result);
        result = fu.normalize(".example12345678901234567890.txt");
    }
}
