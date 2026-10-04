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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * Manual tests derived from surviving and uncovered PIT mutants.
 */
public class LookaheadInputStreamManualTest {

    @Test
    public void arrayReadHonorsOffsetLengthAndEndOfLookahead() throws IOException {
        ByteArrayInputStream source = new ByteArrayInputStream(new byte[]{'a', 'b', 'c'});
        LookaheadInputStream lookahead = new LookaheadInputStream(source, 2);
        byte[] target = new byte[]{'x', 'x', 'x', 'x'};

        assertEquals(2, lookahead.read(target, 1, 3));
        assertEquals('x', target[0]);
        assertEquals('a', target[1]);
        assertEquals('b', target[2]);
        assertEquals('x', target[3]);
        assertEquals(-1, lookahead.read(target, 0, 1));

        lookahead.close();
        assertEquals('a', source.read());
    }

    @Test
    public void arrayReadReturnsOnlyBytesRemainingAfterAByteRead() throws IOException {
        ByteArrayInputStream source = new ByteArrayInputStream(new byte[]{'a', 'b', 'c'});
        LookaheadInputStream lookahead = new LookaheadInputStream(source, 2);
        byte[] target = new byte[2];

        assertEquals('a', lookahead.read());
        assertEquals(1, lookahead.read(target, 0, target.length));
        assertEquals('b', target[0]);
    }

    @Test
    public void closeRestoresThePositionAtConstruction() throws IOException {
        ByteArrayInputStream source = new ByteArrayInputStream(new byte[]{'x', 'a', 'b'});
        assertEquals('x', source.read());
        LookaheadInputStream lookahead = new LookaheadInputStream(source, 2);

        assertEquals('a', lookahead.read());
        lookahead.close();

        assertEquals('a', source.read());
    }

    @Test
    public void shortReadsFillIncrementallyAndRestoreAtEndOfStream() throws IOException {
        ByteArrayInputStream source = new ByteArrayInputStream(new byte[]{'a', 'b', 'c'}) {
            @Override
            public synchronized int read(byte[] bytes, int offset, int length) {
                return super.read(bytes, offset, Math.min(length, 1));
            }
        };
        LookaheadInputStream lookahead = new LookaheadInputStream(source, 4);

        assertEquals('a', lookahead.read());
        assertEquals('b', lookahead.read());
        assertEquals('c', lookahead.read());
        assertEquals(-1, lookahead.read());

        assertEquals('a', source.read());
    }
}
