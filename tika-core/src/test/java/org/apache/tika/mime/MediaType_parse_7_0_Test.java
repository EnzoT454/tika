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
// Compilait avant integration ; selection/corrections humaines tracees dans etape-03.

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


public class MediaType_parse_7_0_Test {

    @Test
    public void testParseNull() {
        assertNull(MediaType.parse(null));
    }

    @Test
    public void testParseSimpleType() {
        MediaType mediaType = MediaType.parse("text/plain");
        assertNotNull(mediaType);
        assertEquals("text", mediaType.getType());
        assertEquals("plain", mediaType.getSubtype());
        assertTrue(mediaType.getParameters().isEmpty());
    }

    @Test
    public void testParseComplexType() {
        MediaType mediaType = MediaType.parse("image/png; charset=utf-8");
        assertNotNull(mediaType);
        assertEquals("image", mediaType.getType());
        assertEquals("png", mediaType.getSubtype());
        assertEquals("utf-8", mediaType.getParameters().get("charset"));
    }


    @Test
    public void testParseSyntacticallyValidUnregisteredType() {
        assertEquals("invalid/type", MediaType.parse("invalid/type").toString());
    }

    @Test
    public void testParseEmptyString() {
        assertNull(MediaType.parse(""));
    }

    @Test
    public void testParseWhitespace() {
        assertNull(MediaType.parse(" "));
    }

    @Test
    public void testParseSimpleTypeWithParameters() {
        MediaType mediaType = MediaType.parse("text/plain; param=value");
        assertNotNull(mediaType);
        assertEquals("text", mediaType.getType());
        assertEquals("plain", mediaType.getSubtype());
        assertEquals("value", mediaType.getParameters().get("param"));
    }

    @Test
    public void testParseComplexTypeWithParameters() {
        MediaType mediaType = MediaType.parse("image/png; charset=utf-8; param=value");
        assertNotNull(mediaType);
        assertEquals("image", mediaType.getType());
        assertEquals("png", mediaType.getSubtype());
        assertEquals("utf-8", mediaType.getParameters().get("charset"));
        assertEquals("value", mediaType.getParameters().get("param"));
    }

    @Test
    public void testParseParametersWithQuotes() {
        MediaType mediaType = MediaType.parse("image/png; charset=\"utf-8\"; param=\"value with spaces\"");
        assertNotNull(mediaType);
        assertEquals("image", mediaType.getType());
        assertEquals("png", mediaType.getSubtype());
        assertEquals("utf-8", mediaType.getParameters().get("charset"));
        assertEquals("value with spaces", mediaType.getParameters().get("param"));
    }



    @Test
    public void testParseParametersWithEmptyValues() {
        MediaType mediaType = MediaType.parse("image/png; charset=; param=");
        assertNotNull(mediaType);
        assertEquals("image", mediaType.getType());
        assertEquals("png", mediaType.getSubtype());
        assertEquals("", mediaType.getParameters().get("charset"));
        assertEquals("", mediaType.getParameters().get("param"));
    }

    @Test
    public void testParseParametersWithLeadingTrailingSpaces() {
        MediaType mediaType = MediaType.parse("image/png; charset = utf-8 ; param = value");
        assertNotNull(mediaType);
        assertEquals("image", mediaType.getType());
        assertEquals("png", mediaType.getSubtype());
        assertEquals("utf-8", mediaType.getParameters().get("charset"));
        assertEquals("value", mediaType.getParameters().get("param"));
    }

    @Test
    public void testParseParametersWithMultipleSemicolons() {
        MediaType mediaType = MediaType.parse("image/png; charset=utf-8;; param=value");
        assertNotNull(mediaType);
        assertEquals("image", mediaType.getType());
        assertEquals("png", mediaType.getSubtype());
        assertEquals("utf-8", mediaType.getParameters().get("charset"));
        assertEquals("value", mediaType.getParameters().get("param"));
    }

    @Test
    public void testParseParametersWithMultipleEquals() {
        MediaType mediaType = MediaType.parse("image/png; charset=utf-8=; param=value=");
        assertNotNull(mediaType);
        assertEquals("image", mediaType.getType());
        assertEquals("png", mediaType.getSubtype());
        assertEquals("utf-8=", mediaType.getParameters().get("charset"));
        assertEquals("value=", mediaType.getParameters().get("param"));
    }
}
