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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;


public class MediaType_hasParameters_15_0_Test {

    @Test
    public void testHasParametersWithNoParameters() {
        MediaType mediaType = new MediaType("text", "plain");
        assertFalse(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithParameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("charset", "UTF-8");
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertTrue(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithEmptyParameters() {
        Map<String, String> parameters = Collections.emptyMap();
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertFalse(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithOneParameter() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("one", "value");
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertTrue(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithMultipleParameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("one", "value1");
        parameters.put("two", "value2");
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertTrue(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithEmptyParameterName() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("", "value");
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertTrue(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithEmptyParameterValue() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("one", "");
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertTrue(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithWhitespaceParameterName() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(" one ", "value");
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertTrue(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithWhitespaceParameterValue() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("one", " value ");
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertTrue(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithSpecialParameterName() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("one!", "value");
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertTrue(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithSpecialParameterValue() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("one", "value!");
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertTrue(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithEmptyStringParameterName() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("", "value");
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertTrue(mediaType.hasParameters());
    }

    @Test
    public void testHasParametersWithEmptyStringParameterValue() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("one", "");
        MediaType mediaType = new MediaType("text", "plain", parameters);
        assertTrue(mediaType.hasParameters());
    }
}
