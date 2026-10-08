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

// Test genere par ChatUniTest 2.1.1 (qwen2.5-coder:7b via Ollama).
// Compilait avant integration ; selection/corrections humaines tracees dans etape-03.

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FilenameUtils_resolveWithin_5_0_Test {

    @InjectMocks
    private FilenameUtils filenameUtils;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testResolveWithin() throws IOException {
        Path dir = Paths.get("/tmp");
        Path resolved = filenameUtils.resolveWithin(dir, "test.txt");
        assertEquals(Paths.get("/tmp/test.txt"), resolved);
    }


    @Test
    public void testResolveWithinOutside(@org.junit.jupiter.api.io.TempDir Path temporary) throws IOException {
        Path dir = temporary.resolve("inside");
        IOException exception = assertThrows(IOException.class, () -> {
            filenameUtils.resolveWithin(dir, "../outside.txt");
        });
        assertEquals("'../outside.txt' resolves to '" + temporary.resolve("outside.txt") + "', which is outside of '" + dir + "'", exception.getMessage());
    }

    @Test
    public void testResolveWithinSymlink() throws IOException {
        Path dir = Paths.get("/tmp");
        Path resolved = filenameUtils.resolveWithin(dir, "symlink");
        assertEquals(Paths.get("/tmp/symlink"), resolved);
    }




}
