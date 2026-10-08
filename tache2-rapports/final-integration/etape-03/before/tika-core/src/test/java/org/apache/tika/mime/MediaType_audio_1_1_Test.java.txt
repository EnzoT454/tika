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

import java.lang.reflect.Method;
import java.util.HashMap;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;

public class MediaType_audio_1_1_Test {

    private Method audioMethod;

    private MediaType mediaType;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        audioMethod = MediaType.class.getDeclaredMethod("audio", String.class);
        audioMethod.setAccessible(true);
        mediaType = new MediaType("audio", "mpeg", new HashMap<>());
    }

    @Disabled("ChatUniTest : oracle faux - Unexpected exception type thrown, expected: <java.lang.NullPointerException> but was: <java.lang.IllegalArgumentException>")
    @Test
    public void testAudioMethod() throws Exception {
        // Test with valid audio subtype
        MediaType result = (MediaType) audioMethod.invoke(mediaType, "mpeg");
        assertEquals("audio/mpeg", result.toString());
        // Test with null subtype
        Executable executable = () -> audioMethod.invoke(mediaType, null);
        assertThrows(NullPointerException.class, executable);
        // Test with empty subtype
        executable = () -> audioMethod.invoke(mediaType, "");
        assertThrows(IllegalArgumentException.class, executable);
        // Test with invalid subtype
        executable = () -> audioMethod.invoke(mediaType, "invalid");
        assertThrows(IllegalArgumentException.class, executable);
    }
}
