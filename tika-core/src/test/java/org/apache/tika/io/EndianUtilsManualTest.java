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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import org.apache.tika.io.EndianUtils.BufferUnderrunException;

/**
 * IFT3913, tache 2 : tests ecrits a la main pour les mutants de {@link EndianUtils} qui
 * survivaient aux tests originaux et aux tests generes par ChatUniTest.
 * Chaque test documente son intention, le choix des donnees et l'oracle (voir aussi README.md, section 9).
 */
public class EndianUtilsManualTest {

    /** Flux qui renvoie une sequence fixe de valeurs de read(), y compris -1 au milieu. */
    private static InputStream sequence(final int... values) {
        return new InputStream() {
            private int i = 0;

            @Override
            public int read() {
                return i < values.length ? values[i++] : -1;
            }
        };
    }

    private static InputStream bytes(int... values) {
        byte[] b = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            b[i] = (byte) values[i];
        }
        return new ByteArrayInputStream(b);
    }

    // ------------------------------------------------------------------
    // 1. Entrees entierement nulles : (ch1 | ch2 | ...) vaut exactement 0.
    //    Mutant vise : CONDITIONALS_BOUNDARY  "< 0" -> "<= 0"  (le mutant leve
    //    BufferUnderrunException alors que les octets sont tous presents).
    //    Oracle : des octets tous a zero representent la valeur 0, quelle que
    //    soit l'endianness ; aucun underrun puisque le flux contient le nombre
    //    d'octets requis.
    // ------------------------------------------------------------------

    @Test
    public void readUShortLE_allZeroBytes_returnsZero() throws Exception {
        assertEquals(0, EndianUtils.readUShortLE(bytes(0, 0)));
    }

    @Test
    public void readUShortBE_allZeroBytes_returnsZero() throws Exception {
        assertEquals(0, EndianUtils.readUShortBE(bytes(0, 0)));
    }

    @Test
    public void readUIntLE_allZeroBytes_returnsZero() throws Exception {
        assertEquals(0L, EndianUtils.readUIntLE(bytes(0, 0, 0, 0)));
    }

    @Test
    public void readUIntBE_allZeroBytes_returnsZero() throws Exception {
        assertEquals(0L, EndianUtils.readUIntBE(bytes(0, 0, 0, 0)));
    }

    @Test
    public void readIntLE_allZeroBytes_returnsZero() throws Exception {
        assertEquals(0, EndianUtils.readIntLE(bytes(0, 0, 0, 0)));
    }

    @Test
    public void readIntBE_allZeroBytes_returnsZero() throws Exception {
        assertEquals(0, EndianUtils.readIntBE(bytes(0, 0, 0, 0)));
    }

    @Test
    public void readIntME_allZeroBytes_returnsZero() throws Exception {
        assertEquals(0, EndianUtils.readIntME(bytes(0, 0, 0, 0)));
    }

    @Test
    public void readLongLE_allZeroBytes_returnsZero() throws Exception {
        assertEquals(0L, EndianUtils.readLongLE(bytes(0, 0, 0, 0, 0, 0, 0, 0)));
    }

    @Test
    public void readLongBE_allZeroBytes_returnsZero() throws Exception {
        assertEquals(0L, EndianUtils.readLongBE(bytes(0, 0, 0, 0, 0, 0, 0, 0)));
    }

    // ------------------------------------------------------------------
    // 2. Fin de flux signalee sur le PREMIER octet puis donnees valides.
    //    Mutants vises : MATH  "|" -> "&" dans (ch1 | ch2 | ... ) < 0.
    //    Avec ch1 = -1 et les autres octets >= 0, chaque OR remplace par un AND
    //    rend l'expression positive : le mutant renvoie une valeur au lieu de
    //    lever l'exception. Un flux qui renvoie -1 puis des donnees est realiste
    //    (fichier en cours d'ecriture lu avec FileInputStream) ; la specification
    //    de la methode est que TOUTE lecture a -1 signale un underrun.
    //    Oracle : BufferUnderrunException attendue.
    // ------------------------------------------------------------------

    @Test
    public void readUIntLE_eofOnFirstByteThenData_throwsUnderrun() {
        assertThrows(BufferUnderrunException.class,
                () -> EndianUtils.readUIntLE(sequence(-1, 1, 2, 3)));
    }

    @Test
    public void readUIntBE_eofOnFirstByteThenData_throwsUnderrun() {
        assertThrows(BufferUnderrunException.class,
                () -> EndianUtils.readUIntBE(sequence(-1, 1, 2, 3)));
    }

    @Test
    public void readIntLE_eofOnFirstByteThenData_throwsUnderrun() {
        assertThrows(BufferUnderrunException.class,
                () -> EndianUtils.readIntLE(sequence(-1, 1, 2, 3)));
    }

    @Test
    public void readIntBE_eofOnFirstByteThenData_throwsUnderrun() {
        assertThrows(BufferUnderrunException.class,
                () -> EndianUtils.readIntBE(sequence(-1, 1, 2, 3)));
    }

    @Test
    public void readIntME_eofOnFirstByteThenData_throwsUnderrun() {
        assertThrows(BufferUnderrunException.class,
                () -> EndianUtils.readIntME(sequence(-1, 1, 2, 3)));
    }

    @Test
    public void readLongLE_eofOnFirstByteThenData_throwsUnderrun() {
        assertThrows(BufferUnderrunException.class,
                () -> EndianUtils.readLongLE(sequence(-1, 1, 2, 3, 4, 5, 6, 7)));
    }

    @Test
    public void readLongBE_eofOnFirstByteThenData_throwsUnderrun() {
        assertThrows(BufferUnderrunException.class,
                () -> EndianUtils.readLongBE(sequence(-1, 1, 2, 3, 4, 5, 6, 7)));
    }

    // ------------------------------------------------------------------
    // 3. Lecture de long : octets tous distincts, pour que chaque position
    //    (decalage de 0 a 56 bits) et chaque addition soit observable.
    //    Mutants vises : MATH (shift <-> shift, + -> -) et PRIMITIVE_RETURNS sur
    //    les lignes de calcul de readLongLE / readLongBE (non couvertes avant).
    //    Oracle : composition explicite 0x0807060504030201 (LE) et
    //    0x0102030405060708 (BE) ; puis une valeur dont l'octet de poids fort a
    //    le bit de signe a 1 pour verifier le cast en long (sinon le resultat
    //    serait negatif ou tronque).
    // ------------------------------------------------------------------

    @Test
    public void readLongLE_distinctBytes_assemblesLittleEndian() throws Exception {
        assertEquals(0x0807060504030201L,
                EndianUtils.readLongLE(bytes(0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08)));
        assertEquals(0x123456789ABCDEF0L,
                EndianUtils.readLongLE(bytes(0xF0, 0xDE, 0xBC, 0x9A, 0x78, 0x56, 0x34, 0x12)));
        assertEquals(-1L,
                EndianUtils.readLongLE(bytes(0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF)));
    }

    @Test
    public void readLongBE_distinctBytes_assemblesBigEndian() throws Exception {
        assertEquals(0x0102030405060708L,
                EndianUtils.readLongBE(bytes(0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08)));
        assertEquals(0x123456789ABCDEF0L,
                EndianUtils.readLongBE(bytes(0x12, 0x34, 0x56, 0x78, 0x9A, 0xBC, 0xDE, 0xF0)));
        assertEquals(-1L,
                EndianUtils.readLongBE(bytes(0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF)));
    }

    @Test
    public void readLongBE_sevenBytesOnly_throwsUnderrun() {
        // underrun sur le dernier octet : seule la 8e lecture vaut -1
        assertThrows(BufferUnderrunException.class,
                () -> EndianUtils.readLongBE(bytes(1, 2, 3, 4, 5, 6, 7)));
    }

    // ------------------------------------------------------------------
    // 4. readUE7 : entier a 7 bits par octet, bit de poids fort = continuation.
    // ------------------------------------------------------------------

    /**
     * Octet de continuation suivi de l'octet 0x00 : la valeur est (1 << 7) + 0 = 128 et
     * la lecture se termine normalement sur un octet qui vaut exactement 0.
     * Mutants vises : CONDITIONALS_BOUNDARY "i >= 0" -> "i > 0" (la boucle s'arreterait
     * avant d'ajouter l'octet 0 : resultat 1) et "i < 0" -> "i <= 0" (IOException levee
     * a tort parce que le dernier octet vaut 0).
     */
    @Test
    public void readUE7_continuationThenZeroByte_returns128() throws IOException {
        assertEquals(128L, EndianUtils.readUE7(bytes(0x81, 0x00)));
    }

    /**
     * Sept octets de continuation : la methode est documentee pour s'arreter apres
     * 6 octets (max = 6). Les 6 premiers octets 0x81 valent chacun 1 ; la valeur est donc
     * v = ((((((1 << 7 | 1) << 7 | 1) << 7 | 1) << 7 | 1) << 7 | 1) = 34 630 287 489,
     * le 7e octet (0x01) est consomme par la condition de boucle mais ignore.
     * Mutants vises : INCREMENTS "read++" -> "read--" et CONDITIONALS_BOUNDARY
     * "read++ < max" -> "<= max" : dans les deux cas une 7e iteration ajouterait
     * l'octet 0x01 et donnerait 34 630 287 489 * 128 + 1.
     */
    @Test
    public void readUE7_stopsAfterSixContinuationBytes() throws IOException {
        long expected = 0;
        for (int i = 0; i < 6; i++) {
            expected = (expected << 7) | 1;
        }
        assertEquals(34630287489L, expected); // garde-fou sur le calcul de l'oracle
        assertEquals(expected, EndianUtils.readUE7(bytes(0x81, 0x81, 0x81, 0x81, 0x81, 0x81, 0x01)));
    }

    // ------------------------------------------------------------------
    // 5. Surcharges a une ligne (data) -> (data, 0) jamais appelees par un test
    //    actif. Mutants vises : PRIMITIVE_RETURNS (retour remplace par 0).
    //    Oracle : valeur non nulle dont les deux octets different, pour
    //    distinguer aussi LE de BE, et contraste signe / non signe sur 0xFFFF.
    // ------------------------------------------------------------------

    @Test
    public void getShortLE_singleArgOverload_readsFromOffsetZero() {
        assertEquals((short) 0x1234, EndianUtils.getShortLE(new byte[]{0x34, 0x12}));
        assertEquals((short) -1, EndianUtils.getShortLE(new byte[]{(byte) 0xFF, (byte) 0xFF}));
    }

    @Test
    public void getUShortLE_singleArgOverload_readsFromOffsetZero() {
        assertEquals(0x1234, EndianUtils.getUShortLE(new byte[]{0x34, 0x12}));
        assertEquals(65535, EndianUtils.getUShortLE(new byte[]{(byte) 0xFF, (byte) 0xFF}));
    }

    @Test
    public void getShortBE_singleArgOverload_readsFromOffsetZero() {
        assertEquals((short) 0x1234, EndianUtils.getShortBE(new byte[]{0x12, 0x34}));
        assertEquals((short) -1, EndianUtils.getShortBE(new byte[]{(byte) 0xFF, (byte) 0xFF}));
    }

    @Test
    public void getShortBE_withOffset_readsSignedBigEndian() {
        assertEquals((short) 0x1234, EndianUtils.getShortBE(new byte[]{0x00, 0x12, 0x34}, 1));
        assertEquals((short) 0x8000, EndianUtils.getShortBE(new byte[]{0x00, (byte) 0x80, 0x00}, 1));
    }

    @Test
    public void getIntBE_singleArgOverload_readsFromOffsetZero() {
        assertEquals(0x01020304, EndianUtils.getIntBE(new byte[]{0x01, 0x02, 0x03, 0x04}));
    }

    @Test
    public void getUIntLE_singleArgOverload_isUnsigned() {
        assertEquals(0x04030201L, EndianUtils.getUIntLE(new byte[]{0x01, 0x02, 0x03, 0x04}));
        assertEquals(4294967295L, EndianUtils.getUIntLE(
                new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}));
    }

    @Test
    public void getUIntBE_singleArgOverload_isUnsigned() {
        assertEquals(0x01020304L, EndianUtils.getUIntBE(new byte[]{0x01, 0x02, 0x03, 0x04}));
        assertEquals(4294967295L, EndianUtils.getUIntBE(
                new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}));
    }
}
