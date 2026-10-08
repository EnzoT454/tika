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

// Test genere par ChatUniTest 2.1.1 (qwen2.5-coder:7b via Ollama), tentative 0 (la tentative 1 etait inexploitable), non compilable tel quel.
// Corrections manuelles (2) :
//   - appel a une surcharge inexistante MediaType.image(String, Map) (API hallucinee) remplace par new MediaType(MediaType.image("png"), params) (2 occurrences)

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class MediaType_image_2_0_Test {

    @Test
    public void testImageMethod() {
        // Test with a valid image type
        MediaType mediaType = MediaType.image("png");
        assertEquals("image/png", mediaType.toString());
        assertEquals("image", mediaType.getType());
        assertEquals("png", mediaType.getSubtype());
        assertTrue(mediaType.getParameters().isEmpty());
        // Test with a valid image type with parameters
        Map<String, String> params = new HashMap<>();
        params.put("charset", "UTF-8");
        MediaType mediaTypeWithParams = new MediaType(MediaType.image("png"), params);
        assertEquals("image/png; charset=UTF-8", mediaTypeWithParams.toString());
        assertEquals("image", mediaTypeWithParams.getType());
        assertEquals("png", mediaTypeWithParams.getSubtype());
        assertEquals("UTF-8", mediaTypeWithParams.getParameters().get("charset"));
        // Test with an invalid image type
        MediaType invalidMediaType = MediaType.image("");
        assertNull(invalidMediaType);
    }

    @Test
    public void testConstructor() {
        // Test with a valid type, subtype, and parameters
        Map<String, String> params = new HashMap<>();
        params.put("charset", "UTF-8");
        MediaType mediaType = new MediaType("image", "png", params);
        assertEquals("image/png; charset=UTF-8", mediaType.toString());
        assertEquals("image", mediaType.getType());
        assertEquals("png", mediaType.getSubtype());
        assertEquals("UTF-8", mediaType.getParameters().get("charset"));
        // Test with a valid type and subtype
        MediaType mediaTypeWithoutParams = new MediaType("image", "png");
        assertEquals("image/png", mediaTypeWithoutParams.toString());
        assertEquals("image", mediaTypeWithoutParams.getType());
        assertEquals("png", mediaTypeWithoutParams.getSubtype());
        assertTrue(mediaTypeWithoutParams.getParameters().isEmpty());
    }


    @Test
    public void testGetBaseType() {
        MediaType mediaType = MediaType.parse("image/png");
        assertEquals(MediaType.parse("image/png"), mediaType.getBaseType());
    }

    @Test
    public void testGetParameters() {
        Map<String, String> params = new HashMap<>();
        params.put("charset", "UTF-8");
        MediaType mediaType = new MediaType("image", "png", params);
        assertEquals(params, mediaType.getParameters());
    }

    @Test
    public void testImageMethodWithParameters() throws Exception {
        // Test with a valid image type and parameters
        Map<String, String> params = new HashMap<>();
        params.put("charset", "UTF-8");
        MediaType mediaTypeWithParams = new MediaType(MediaType.image("png"), params);
        assertEquals("image/png; charset=UTF-8", mediaTypeWithParams.toString());
        assertEquals("image", mediaTypeWithParams.getType());
        assertEquals("png", mediaTypeWithParams.getSubtype());
        assertEquals("UTF-8", mediaTypeWithParams.getParameters().get("charset"));
    }
}
