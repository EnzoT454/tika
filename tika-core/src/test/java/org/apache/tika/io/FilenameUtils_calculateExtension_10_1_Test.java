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

// Test genere par ChatUniTest 2.1.1 (qwen2.5-coder:7b via Ollama), derniere tentative, non compilable tel quel.
// Corrections manuelles (4) :
//   - new MimeType("...") n'existe pas (le constructeur prend un MediaType) : remplace par new MimeType(MediaType.parse("...")) (3 lignes)
//   - montage Mockito inutile (@Mock MimeTypes + @InjectMocks sur un champ statique, stubs jamais utilises -> UnnecessaryStubbingException
//     en mode strict) remplace par une simple instance ; @ExtendWith(MockitoExtension.class) retire

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypeException;

public class FilenameUtils_calculateExtension_10_1_Test {

    private final FilenameUtils filenameUtils = new FilenameUtils();


    @Test
    public void testCalculateExtension_withContentType_png() throws MimeTypeException {
        Metadata metadata = new Metadata();
        metadata.set(Metadata.CONTENT_TYPE, "image/png");
        String defaultValue = "default.ext";
        String result = filenameUtils.calculateExtension(metadata, defaultValue);
        assertEquals(".png", result);
    }


    @Test
    public void testCalculateExtension_withContentType_pdf() throws MimeTypeException {
        Metadata metadata = new Metadata();
        metadata.set(Metadata.CONTENT_TYPE, "application/pdf");
        String defaultValue = "default.ext";
        String result = filenameUtils.calculateExtension(metadata, defaultValue);
        assertEquals(".pdf", result);
    }


    @Test
    public void testCalculateExtension_withContentType_ocr_png() throws MimeTypeException {
        Metadata metadata = new Metadata();
        metadata.set(Metadata.CONTENT_TYPE, "image/ocr-png");
        String defaultValue = "default.ext";
        String result = filenameUtils.calculateExtension(metadata, defaultValue);
        assertEquals(".png", result);
    }

    @Test
    public void testCalculateExtension_withNoContentType() throws MimeTypeException {
        Metadata metadata = new Metadata();
        String defaultValue = "default.ext";
        String result = filenameUtils.calculateExtension(metadata, defaultValue);
        assertEquals(defaultValue, result);
    }


}
