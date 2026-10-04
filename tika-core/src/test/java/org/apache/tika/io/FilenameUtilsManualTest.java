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
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.apache.tika.metadata.TikaCoreProperties;

/**
 * IFT3913, tache 2 : tests ecrits a la main pour les mutants de {@link FilenameUtils} qui
 * survivaient aux tests originaux et aux tests generes par ChatUniTest (methodes privees
 * getEmbeddedPath/getEmbeddedName/getPrefixLength/lookupExtension, bornes de longueur,
 * branche "chemins existants" de resolveWithin).
 * Chaque test documente son intention, le choix des donnees et l'oracle (readme-tache-2.md, section 9.3).
 */
public class FilenameUtilsManualTest {

    private static final String DEFAULT_EXT = ".bin";

    private static Metadata metadata(Object... propertyValuePairs) {
        Metadata m = new Metadata();
        for (int i = 0; i < propertyValuePairs.length; i += 2) {
            m.set((Property) propertyValuePairs[i], (String) propertyValuePairs[i + 1]);
        }
        return m;
    }

    private static String fileName(Metadata m, int maxLength) {
        return FilenameUtils.getSanitizedEmbeddedFileName(m, DEFAULT_EXT, maxLength);
    }

    private static String filePath(Metadata m, int maxLength) {
        return FilenameUtils.getSanitizedEmbeddedFilePath(m, DEFAULT_EXT, maxLength);
    }

    // ------------------------------------------------------------------
    // 1. Ordre de repli des proprietes de metadonnees (methodes privees
    //    getEmbeddedName et getEmbeddedPath). Mutants vises : NEGATE_CONDITIONALS
    //    sur chaque "if (!isBlank(path))" et EMPTY_RETURNS sur chaque "return path".
    //    Donnees : une seule propriete renseignee a la fois, avec une valeur qui
    //    identifie la propriete (le nom de fichier contient le numero de rang),
    //    puis deux proprietes en conflit pour verifier la priorite.
    //    Oracle : l'ordre code en dur dans FilenameUtils (nom : RESOURCE_NAME_KEY,
    //    INTERNAL_PATH, EMBEDDED_RELATIONSHIP_ID, EMBEDDED_RESOURCE_PATH,
    //    ORIGINAL_RESOURCE_NAME ; chemin : EMBEDDED_RESOURCE_PATH, INTERNAL_PATH,
    //    RESOURCE_NAME_KEY, EMBEDDED_RELATIONSHIP_ID, ORIGINAL_RESOURCE_NAME).
    // ------------------------------------------------------------------

    @Test
    public void embeddedName_eachPropertyAloneIsUsed() {
        assertEquals("n1.txt", fileName(metadata(TikaCoreProperties.RESOURCE_NAME_KEY, "n1.txt"), 50));
        assertEquals("n2.txt", fileName(metadata(TikaCoreProperties.INTERNAL_PATH, "dir/n2.txt"), 50));
        assertEquals("n3.txt", fileName(metadata(TikaCoreProperties.EMBEDDED_RELATIONSHIP_ID, "n3.txt"), 50));
        assertEquals("n4.txt", fileName(metadata(TikaCoreProperties.EMBEDDED_RESOURCE_PATH, "/a/n4.txt"), 50));
        assertEquals("n5.txt", fileName(metadata(TikaCoreProperties.ORIGINAL_RESOURCE_NAME, "n5.txt"), 50));
        assertNull(fileName(new Metadata(), 50));
    }

    @Test
    public void embeddedName_resourceNameWinsOverOtherProperties() {
        Metadata m = metadata(TikaCoreProperties.EMBEDDED_RESOURCE_PATH, "/a/path.txt",
                TikaCoreProperties.INTERNAL_PATH, "internal.txt",
                TikaCoreProperties.RESOURCE_NAME_KEY, "name.txt",
                TikaCoreProperties.ORIGINAL_RESOURCE_NAME, "original.txt");
        assertEquals("name.txt", fileName(m, 50));
        // sans RESOURCE_NAME_KEY, INTERNAL_PATH passe avant EMBEDDED_RESOURCE_PATH
        Metadata m2 = metadata(TikaCoreProperties.EMBEDDED_RESOURCE_PATH, "/a/path.txt",
                TikaCoreProperties.INTERNAL_PATH, "internal.txt");
        assertEquals("internal.txt", fileName(m2, 50));
    }

    @Test
    public void embeddedPath_eachPropertyAloneIsUsed() {
        assertEquals("a/p1.txt", filePath(metadata(TikaCoreProperties.EMBEDDED_RESOURCE_PATH, "/a/p1.txt"), 50));
        assertEquals("b/p2.txt", filePath(metadata(TikaCoreProperties.INTERNAL_PATH, "b/p2.txt"), 50));
        assertEquals("p3.txt", filePath(metadata(TikaCoreProperties.RESOURCE_NAME_KEY, "p3.txt"), 50));
        assertEquals("p4.txt", filePath(metadata(TikaCoreProperties.EMBEDDED_RELATIONSHIP_ID, "p4.txt"), 50));
        assertEquals("p5.txt", filePath(metadata(TikaCoreProperties.ORIGINAL_RESOURCE_NAME, "p5.txt"), 50));
        assertNull(filePath(new Metadata(), 50));
    }

    @Test
    public void embeddedPath_resourcePathWinsOverOtherProperties() {
        Metadata m = metadata(TikaCoreProperties.RESOURCE_NAME_KEY, "name.txt",
                TikaCoreProperties.INTERNAL_PATH, "internal/i.txt",
                TikaCoreProperties.EMBEDDED_RESOURCE_PATH, "/x/y/path.txt");
        assertEquals("x/y/path.txt", filePath(m, 50));
        Metadata m2 = metadata(TikaCoreProperties.RESOURCE_NAME_KEY, "name.txt",
                TikaCoreProperties.EMBEDDED_RELATIONSHIP_ID, "rId7");
        assertEquals("name.txt", filePath(m2, 50));
    }

    // ------------------------------------------------------------------
    // 2. Bornes de longueur : le cas "exactement maxLength" separe "> maxLength"
    //    de sa mutation ">= maxLength" (CONDITIONALS_BOUNDARY lignes 189, 264, 265)
    //    et couvre le "return namePart + extension" de la ligne 268.
    //    Oracle : lu dans le code ; un nom qui atteint exactement la limite est
    //    renvoye entier, un nom qui la depasse est tronque a
    //    maxLength - extension - 3 caracteres suivis de "..." et de l'extension.
    // ------------------------------------------------------------------

    @Test
    public void fileName_exactlyMaxLength_isNotTruncated() {
        // namePart = 10 caracteres, maxLength = 10 : la limite porte sur le nom sans extension
        assertEquals("abcdefghij.txt",
                fileName(metadata(TikaCoreProperties.RESOURCE_NAME_KEY, "abcdefghij.txt"), 10));
        // un caractere de plus : tronque a 10 - 4 - 3 = 3 caracteres + "..." + ".txt"
        assertEquals("abc....txt",
                fileName(metadata(TikaCoreProperties.RESOURCE_NAME_KEY, "abcdefghijk.txt"), 10));
    }

    @Test
    public void filePath_exactlyMaxLength_keepsRelativePath() {
        // "a/b/x.txt" fait 9 caracteres : avec maxLength = 9 le chemin relatif est conserve
        assertEquals("a/b/x.txt",
                filePath(metadata(TikaCoreProperties.EMBEDDED_RESOURCE_PATH, "a/b/x.txt"), 9));
        // avec maxLength = 8 le chemin est trop long mais le nom (1 caractere) ne l'est pas :
        // seul le nom est renvoye
        assertEquals("x.txt",
                filePath(metadata(TikaCoreProperties.EMBEDDED_RESOURCE_PATH, "a/b/x.txt"), 8));
    }

    @Test
    public void filePath_tooLongPath_nameExactlyMaxLength_isNotTruncated() {
        // namePart "abcdefgh" = 8 = maxLength : renvoye entier (sans le chemin relatif "dir")
        assertEquals("abcdefgh.txt",
                filePath(metadata(TikaCoreProperties.EMBEDDED_RESOURCE_PATH, "dir/abcdefgh.txt"), 8));
        // namePart de 9 caracteres : tronque a 8 - 4 - 3 = 1 caractere
        assertEquals("a....txt",
                filePath(metadata(TikaCoreProperties.EMBEDDED_RESOURCE_PATH, "dir/abcdefghi.txt"), 8));
    }

    // ------------------------------------------------------------------
    // 3. Retours null des branches de garde (EMPTY_RETURNS "" lignes 174, 232, 250).
    //    Donnees construites pour atteindre chaque garde : nom reduit a des espaces
    //    avant l'extension, chemin "." (nom vide apres nettoyage), chemin dont le
    //    dernier segment est seulement une extension.
    //    Oracle : la javadoc et le code renvoient null quand aucun nom utilisable
    //    ne peut etre produit ; "" serait un nom de fichier invalide.
    // ------------------------------------------------------------------

    @Test
    public void fileName_blankNamePartBeforeExtension_returnsNull() {
        assertNull(fileName(metadata(TikaCoreProperties.RESOURCE_NAME_KEY, "   .txt"), 50));
    }

    @Test
    public void filePath_dotOnly_returnsNull() {
        assertNull(filePath(metadata(TikaCoreProperties.EMBEDDED_RESOURCE_PATH, "."), 50));
    }

    @Test
    public void filePath_lastSegmentIsOnlyAnExtension_returnsNull() {
        assertNull(filePath(metadata(TikaCoreProperties.EMBEDDED_RESOURCE_PATH, "dir/.txt"), 50));
    }

    // ------------------------------------------------------------------
    // 4. getPrefixLength : la branche maison "X:" n'est atteinte que pour des
    //    chaines de deux caracteres que commons-io ne reconnait pas comme prefixe.
    //    Mutants vises : NEGATE_CONDITIONALS sur chacune des quatre conditions.
    //    Donnees : noms degeneres d'un ou deux caracteres ("A", "AB", "1:", "[:")
    //    qui font basculer exactement une condition ; le mutant renvoie alors un
    //    prefixe de 2 et vide le chemin (resultat null) ou leve une exception
    //    (charAt(1) sur "A").
    //    Oracle : ces noms ne sont pas des prefixes de lecteur, ils doivent etre
    //    conserves (plus l'extension par defaut) ; ':' est remplace par '/' puis
    //    elimine en fin de chemin.
    // ------------------------------------------------------------------

    @Test
    public void degenerateShortNames_areKeptAsNames() {
        assertEquals("A.bin", fileName(metadata(TikaCoreProperties.RESOURCE_NAME_KEY, "A"), 50));
        assertEquals("AB.bin", filePath(metadata(TikaCoreProperties.EMBEDDED_RESOURCE_PATH, "AB"), 50));
        assertEquals("1.bin", filePath(metadata(TikaCoreProperties.EMBEDDED_RESOURCE_PATH, "1:"), 50));
        assertEquals("[.bin", filePath(metadata(TikaCoreProperties.EMBEDDED_RESOURCE_PATH, "[:"), 50));
    }

    // ------------------------------------------------------------------
    // 5. getSuffixFromPath : borne "moins de 6 caracteres point compris"
    //    (CONDITIONALS_BOUNDARY ligne 134).
    //    Oracle : javadoc ("an extension length be 5 or less") et code (< 6).
    // ------------------------------------------------------------------

    @Test
    public void suffix_fiveCharactersAccepted_sixRejected() {
        assertEquals(".abcd", FilenameUtils.getSuffixFromPath("file.abcd"));
        assertEquals("", FilenameUtils.getSuffixFromPath("file.abcde"));
    }

    // ------------------------------------------------------------------
    // 6. calculateExtension / lookupExtension avec un type MIME inconnu ou invalide
    //    (EMPTY_RETURNS lignes 402 et 416, jamais couvertes).
    //    Oracle : javadoc de calculateExtension ("on parse exception or null value,
    //    return the default value") et code : type inconnu sans extension -> ".bin".
    // ------------------------------------------------------------------

    @Test
    public void calculateExtension_unknownOrInvalidMimeType_fallsBackToBin() {
        Metadata unknown = metadata(Metadata.CONTENT_TYPE, "application/x-ift3913-unknown-type");
        assertEquals(".bin", FilenameUtils.calculateExtension(unknown, ".dflt"));
        Metadata invalid = metadata(Metadata.CONTENT_TYPE, "ceci n'est pas un type mime");
        assertEquals(".bin", FilenameUtils.calculateExtension(invalid, ".dflt"));
        assertEquals(".dflt", FilenameUtils.calculateExtension(new Metadata(), ".dflt"));
        Metadata known = metadata(Metadata.CONTENT_TYPE, "application/pdf");
        assertEquals(".pdf", FilenameUtils.calculateExtension(known, ".dflt"));
    }

    // ------------------------------------------------------------------
    // 7. resolveWithin avec des chemins qui existent reellement : la seconde
    //    verification (chemins reels, ligne 305-308) n'etait jamais executee.
    //    Mutants vises : NEGATE_CONDITIONALS sur Files.exists(resolved) et sur
    //    realResolved.startsWith(realDir).
    //    Oracle : un fichier existant dans le repertoire est renvoye tel quel ;
    //    un nom inexistant aussi (la verification des chemins reels ne s'applique
    //    pas, et le mutant tenterait toRealPath() sur un fichier absent ->
    //    NoSuchFileException).
    // ------------------------------------------------------------------

    @Test
    public void resolveWithin_existingChild_isReturned(@TempDir Path dir) throws IOException {
        Path child = Files.createFile(dir.resolve("child.txt"));
        assertEquals(child, FilenameUtils.resolveWithin(dir, "child.txt"));
        Files.createDirectories(dir.resolve("sub"));
        Path nested = Files.createFile(dir.resolve("sub").resolve("nested.txt"));
        assertEquals(nested, FilenameUtils.resolveWithin(dir, "sub/nested.txt"));
    }

    @Test
    public void resolveWithin_missingChildInExistingDir_isReturned(@TempDir Path dir) throws IOException {
        assertEquals(dir.resolve("absent.txt"), FilenameUtils.resolveWithin(dir, "absent.txt"));
    }
}
