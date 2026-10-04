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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

/**
 * IFT3913, tache 2 : tests ecrits a la main pour les mutants de {@link MediaType} qui
 * survivaient aux tests originaux et aux tests generes par ChatUniTest (qui a ignore les
 * methodes set(...), getBaseType(), union(...) et les constructeurs).
 * Chaque test documente son intention, le choix des donnees et l'oracle (README.md, section 9.2).
 */
public class MediaTypeManualTest {

    // ------------------------------------------------------------------
    // 1. Fabriques application() / audio() / text() : jamais appelees par un
    //    test actif. Mutant vise : NULL_RETURNS (la fabrique renvoie null).
    //    Oracle : la fabrique concatene le type principal et le sous-type ;
    //    la forme canonique est "type/sous-type" en minuscules.
    // ------------------------------------------------------------------

    @Test
    public void factories_buildCanonicalTypeFromSubtype() {
        assertEquals("application/json", MediaType.application("json").toString());
        assertEquals("audio/mpeg", MediaType.audio("mpeg").toString());
        assertEquals("text/csv", MediaType.text("csv").toString());
        assertEquals("image/png", MediaType.image("png").toString());
        assertEquals("video/mp4", MediaType.video("mp4").toString());
        // meme instance que parse() : les types simples sont mis en cache
        assertSame(MediaType.parse("text/csv"), MediaType.text("csv"));
    }

    // ------------------------------------------------------------------
    // 2. set(MediaType...) et set(String...) : ignores par ChatUniTest (nom
    //    commencant par "set"). Mutants vises : NEGATE_CONDITIONALS sur
    //    "if (type != null)" / "if (mt != null)" et EMPTY_RETURNS (ensemble vide).
    //    Donnees : un doublon (pour verifier la semantique d'ensemble), un null
    //    et une chaine non analysable (ignores d'apres le code), des types
    //    distincts (pour que la taille soit observable).
    //    Oracle : taille = nombre de types distincts valides, contenu exact,
    //    ensemble non modifiable (javadoc : "unmodifiable set").
    // ------------------------------------------------------------------

    @Test
    public void setOfMediaTypes_ignoresNullAndDuplicates_andIsUnmodifiable() {
        MediaType plain = MediaType.TEXT_PLAIN;
        MediaType html = MediaType.TEXT_HTML;
        Set<MediaType> set = MediaType.set(plain, null, html, plain);
        assertEquals(2, set.size());
        assertTrue(set.contains(plain));
        assertTrue(set.contains(html));
        assertThrows(UnsupportedOperationException.class, () -> set.add(MediaType.APPLICATION_XML));
    }

    @Test
    public void setOfStrings_parsesEachString_andSkipsUnparsable() {
        Set<MediaType> set = MediaType.set("text/plain", "not a media type", "text/html", "text/plain");
        assertEquals(2, set.size());
        assertTrue(set.contains(MediaType.TEXT_PLAIN));
        assertTrue(set.contains(MediaType.TEXT_HTML));
        assertThrows(UnsupportedOperationException.class, () -> set.add(MediaType.APPLICATION_XML));
        assertTrue(MediaType.set(new String[0]).isEmpty());
    }

    // ------------------------------------------------------------------
    // 3. parse() avec le charset en premier (TIKA-350, serveurs web casses).
    //    Mutant vise : NULL_RETURNS sur le "return new MediaType(...)" de la
    //    branche CHARSET_FIRST_PATTERN, jamais executee par les autres tests.
    //    Oracle : la javadoc de parse() annonce explicitement la forme
    //    "charset=xxx; type/subtype" ; le resultat canonique met les
    //    parametres apres le type.
    // ------------------------------------------------------------------

    @Test
    public void parse_charsetBeforeType_isAccepted() {
        MediaType type = MediaType.parse("charset=UTF-8; text/plain");
        assertEquals("text/plain; charset=UTF-8", type.toString());
        assertEquals("text", type.getType());
        assertEquals("plain", type.getSubtype());
        assertEquals("UTF-8", type.getParameters().get("charset"));
    }

    // ------------------------------------------------------------------
    // 4. isSimpleName() : caracteres aux bornes des intervalles autorises
    //    ('0'..'9', 'a'..'z') et caracteres speciaux admis ('-', '+', '.', '_').
    //    Mutants vises : CONDITIONALS_BOUNDARY et NEGATE_CONDITIONALS ligne 288.
    //    Un nom simple est mis en cache par parse() (meme instance a chaque
    //    appel, optimisation documentee par le champ SIMPLE_TYPES) ; un nom
    //    juge "non simple" passe par l'expression reguliere et produit une
    //    nouvelle instance a chaque appel. L'identite d'instance rend donc
    //    la decision de isSimpleName observable sans acceder a la methode privee.
    // ------------------------------------------------------------------

    @Test
    public void parse_simpleNameWithBoundaryCharacters_isCached() {
        // Chaine unique a chaque execution : le cache SIMPLE_TYPES est statique et pitest
        // reutilise le meme JVM pour plusieurs mutants ; une chaine deja mise en cache par
        // une execution precedente ne passerait plus par isSimpleName().
        String s = "a0z9/x-0.9_a+z" + Math.abs(System.nanoTime() % 1_000_000_000L);
        assertSame(MediaType.parse(s), MediaType.parse(s));
        assertEquals(s, MediaType.parse(s).toString());
    }

    @Test
    public void parse_nonSimpleName_isNotCached_butStillParsed() {
        // une majuscule n'est pas un caractere "simple" : analyse par expression reguliere,
        // normalisation en minuscules et nouvelle instance a chaque appel
        MediaType first = MediaType.parse("Text/Plain");
        MediaType second = MediaType.parse("Text/Plain");
        assertEquals(MediaType.TEXT_PLAIN, first);
        assertNotSame(first, second);
        // un sous-type vide n'est pas un nom simple et n'est pas accepte par l'expression reguliere
        assertNull(MediaType.parse("text/"));
    }

    // ------------------------------------------------------------------
    // 5. union() via le constructeur MediaType(MediaType, Map) : les deux maps
    //    sont non vides. Mutants vises : NEGATE_CONDITIONALS "b.isEmpty()",
    //    EMPTY_RETURNS et suppression des deux appels putAll (lignes 351-357).
    //    Donnees : un parametre present seulement dans le type de base (x),
    //    un seulement dans l'ajout (format) et un present des deux cotes
    //    (charset) avec des valeurs differentes.
    //    Oracle : union des cles ; en cas de conflit, la valeur ajoutee gagne
    //    (putAll(a) puis putAll(b)) ; les parametres sont tries par nom dans
    //    la forme canonique.
    // ------------------------------------------------------------------

    @Test
    public void constructorWithParameters_mergesBothMaps_newValuesWin() {
        MediaType base = MediaType.parse("text/plain; charset=UTF-8; x=1");
        Map<String, String> extra = new HashMap<>();
        extra.put("charset", "ISO-8859-1");
        extra.put("format", "flowed");
        MediaType merged = new MediaType(base, extra);
        assertEquals("ISO-8859-1", merged.getParameters().get("charset"));
        assertEquals("1", merged.getParameters().get("x"));
        assertEquals("flowed", merged.getParameters().get("format"));
        assertEquals("text/plain; charset=ISO-8859-1; format=flowed; x=1", merged.toString());
    }

    @Test
    public void constructorWithEmptyMap_keepsBaseParameters() {
        // union(a, b) avec b vide : les parametres du type de base sont conserves tels quels
        MediaType base = MediaType.parse("text/plain; charset=UTF-8; x=1");
        MediaType same = new MediaType(base, java.util.Collections.<String, String>emptyMap());
        assertEquals(base, same);
        assertEquals("UTF-8", same.getParameters().get("charset"));
        assertEquals("1", same.getParameters().get("x"));
    }

    @Test
    public void constructorWithCharset_addsCharsetParameterToBaseType() {
        MediaType withCharset = new MediaType(MediaType.TEXT_PLAIN, java.nio.charset.StandardCharsets.UTF_8);
        assertEquals("text/plain; charset=UTF-8", withCharset.toString());
        MediaType withName = new MediaType(MediaType.TEXT_HTML, "charset", "ISO-8859-1");
        assertEquals("text/html; charset=ISO-8859-1", withName.toString());
    }

    // ------------------------------------------------------------------
    // 6. getBaseType(). Mutants vises : NEGATE_CONDITIONALS "parameters.isEmpty()"
    //    et NULL_RETURNS. Oracle : javadoc ("text/plain" pour
    //    "text/plain; charset=utf-8") ; sans parametre, la methode renvoie
    //    l'objet lui-meme ("return this"), ce que assertSame verifie.
    // ------------------------------------------------------------------

    @Test
    public void getBaseType_stripsParameters_andReturnsSelfWhenNone() {
        MediaType withParams = MediaType.parse("text/plain; charset=utf-8");
        assertEquals(MediaType.TEXT_PLAIN, withParams.getBaseType());
        assertFalse(withParams.getBaseType().hasParameters());
        MediaType plain = new MediaType("text", "plain");
        assertSame(plain, plain.getBaseType());
    }

    // ------------------------------------------------------------------
    // 7. hashCode() et compareTo(). Mutants vises : PRIMITIVE_RETURNS (0).
    //    Oracle hashCode : coherent avec equals (deux types egaux ont le meme
    //    hash) et, comme il delegue a la chaine canonique, egal a
    //    string.hashCode() ; deux types differents ont ici des hash differents.
    //    Oracle compareTo : seul le signe est garanti par Comparable ; l'ordre
    //    est celui des chaines canoniques, verifie aussi par un TreeSet.
    // ------------------------------------------------------------------

    @Test
    public void hashCode_isConsistentWithEquals_andWithCanonicalString() {
        MediaType a = new MediaType("text", "plain");
        MediaType b = MediaType.parse("TEXT/PLAIN");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals("text/plain".hashCode(), a.hashCode());
        assertNotEquals(MediaType.TEXT_PLAIN.hashCode(), MediaType.TEXT_HTML.hashCode());
    }

    @Test
    public void compareTo_followsCanonicalStringOrder() {
        MediaType json = MediaType.application("json");
        MediaType xml = MediaType.application("xml");
        MediaType jsonUtf8 = MediaType.parse("application/json; charset=UTF-8");
        assertTrue(json.compareTo(xml) < 0);
        assertTrue(xml.compareTo(json) > 0);
        assertEquals(0, json.compareTo(MediaType.parse("application/json")));
        assertTrue(json.compareTo(jsonUtf8) < 0); // "application/json" est un prefixe de "application/json; ..."
        TreeSet<MediaType> sorted = new TreeSet<>(Arrays.asList(xml, jsonUtf8, json));
        assertEquals(Arrays.asList(json, jsonUtf8, xml), new java.util.ArrayList<>(sorted));
    }
}
