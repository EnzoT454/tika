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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;

/**
 * Manual tests derived from surviving and uncovered PIT mutants.
 */
public class FilenameUtilsManualTest {

    @Test
    public void embeddedFilenameUsesInternalPathWhenResourceNameIsMissing() {
        Metadata metadata = new Metadata();
        metadata.set(TikaCoreProperties.INTERNAL_PATH, "folder/internal.txt");

        assertEquals("internal.txt", FilenameUtils.getSanitizedEmbeddedFileName(metadata, ".bin", 50));
    }

    @Test
    public void embeddedPathUsesInternalPathWhenResourcePathIsMissing() {
        Metadata metadata = new Metadata();
        metadata.set(TikaCoreProperties.INTERNAL_PATH, "folder/internal.txt");

        assertEquals("folder/internal.txt",
                FilenameUtils.getSanitizedEmbeddedFilePath(metadata, ".bin", 50));
    }

    @Test
    public void filenameAtMaximumLengthIsNotTruncated() {
        Metadata metadata = new Metadata();
        metadata.set(TikaCoreProperties.RESOURCE_NAME_KEY, "abc.txt");

        assertEquals("abc.txt", FilenameUtils.getSanitizedEmbeddedFileName(metadata, ".bin", 7));
    }

    @Test
    public void calculateExtensionUsesBinForAnUnknownMimeType() {
        Metadata metadata = new Metadata();
        metadata.set(Metadata.CONTENT_TYPE, "application/x-tika-unknown-type");

        assertEquals(".bin", FilenameUtils.calculateExtension(metadata, ".fallback"));
    }

    @Test
    public void resolveWithinRejectsExistingSymbolicLinkOutsideDirectory(@TempDir Path temporary)
            throws IOException {
        Path directory = Files.createDirectory(temporary.resolve("inside"));
        Path outside = Files.createDirectory(temporary.resolve("outside"));
        Files.createSymbolicLink(directory.resolve("link"), outside);

        assertThrows(IOException.class, () -> FilenameUtils.resolveWithin(directory, "link"));
    }
}
