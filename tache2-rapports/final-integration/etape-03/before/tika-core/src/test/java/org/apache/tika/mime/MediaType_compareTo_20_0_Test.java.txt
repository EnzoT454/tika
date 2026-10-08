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
import org.mockito.*;

public class MediaType_compareTo_20_0_Test {

    @Disabled("ChatUniTest : oracle faux - expected: <-1> but was: <-14>")
    @Test
    public void testCompareTo() {
        MediaType mediaType1 = new MediaType("application", "json", Collections.emptyMap());
        MediaType mediaType2 = new MediaType("application", "xml", Collections.emptyMap());
        MediaType mediaType3 = new MediaType("application", "json", Collections.singletonMap("charset", "UTF-8"));
        assertEquals(-1, mediaType1.compareTo(mediaType2));
        assertEquals(1, mediaType2.compareTo(mediaType1));
        assertEquals(0, mediaType1.compareTo(mediaType1));
        assertEquals(1, mediaType1.compareTo(mediaType3));
        assertEquals(-1, mediaType3.compareTo(mediaType1));
    }
}
