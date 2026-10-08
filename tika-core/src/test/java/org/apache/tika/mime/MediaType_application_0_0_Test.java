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
// Toutes les methodes de test reussissent sur le code non mute.

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;


public class MediaType_application_0_0_Test {

    @Test
    public void testGetBaseType() {
        MediaType mediaType = new MediaType("application", "json");
        assertEquals("application", mediaType.getBaseType().getType());
    }

    @Test
    public void testGetType() {
        MediaType mediaType = new MediaType("application", "json");
        assertEquals("application", mediaType.getType());
    }

    @Test
    public void testGetSubtype() {
        MediaType mediaType = new MediaType("application", "json");
        assertEquals("json", mediaType.getSubtype());
    }

    @Test
    public void testGetParameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("charset", "UTF-8");
        MediaType mediaType = new MediaType("text", "html", parameters);
        assertEquals("UTF-8", mediaType.getParameters().get("charset"));
    }
}
