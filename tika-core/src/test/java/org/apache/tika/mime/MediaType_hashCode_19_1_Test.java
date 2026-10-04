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

import java.util.Collections;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MediaType_hashCode_19_1_Test {

    @Mock
    private MediaType mockMediaType;

    @BeforeEach
    public void setUp() {
        when(mockMediaType.getType()).thenReturn("application");
        when(mockMediaType.getSubtype()).thenReturn("octet-stream");
        when(mockMediaType.getParameters()).thenReturn(Collections.emptyMap());
    }

    @Disabled("ChatUniTest : le test echoue par UnnecessaryStubbingException (stubs Mockito @BeforeEach jamais utilises, MockitoExtension en mode strict) ; l'assertion elle-meme est juste")
    @Test
    public void testHashCode() {
        MediaType mediaType = new MediaType("application", "octet-stream");
        int hashCode = mediaType.hashCode();
        assertEquals("application/octet-stream".hashCode(), hashCode);
    }
}
