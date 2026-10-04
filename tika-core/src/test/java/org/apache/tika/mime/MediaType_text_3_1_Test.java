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
package org.apache.tika.mime;

// Test genere par ChatUniTest 2.1.1 (qwen2.5-coder:7b via Ollama).
// Exporte tel quel par ChatUniTest (compilait sans intervention).
// 1 methode(s) de test desactivee(s) (@Disabled) car leur oracle est faux : elles echouent sur le code non mute.

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

public class MediaType_text_3_1_Test {

    @Disabled("ChatUniTest : oracle faux - expected: <null> but was: <text/null>")
    @Test
    public void testTextMethod() {
        // Test with a valid subtype
        MediaType mediaType = MediaType.text("html");
        assertNotNull(mediaType);
        assertEquals("text/html", mediaType.toString());
        // Test with a null subtype
        mediaType = MediaType.text(null);
        assertNull(mediaType);
        // Test with an empty subtype
        mediaType = MediaType.text("");
        assertEquals("text/plain", mediaType.toString());
        // Test with a subtype containing special characters
        mediaType = MediaType.text("text/html; charset=UTF-8");
        assertNotNull(mediaType);
        assertEquals("text/html; charset=UTF-8", mediaType.toString());
    }
}
