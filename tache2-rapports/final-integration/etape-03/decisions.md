# Registre des decisions — etape 3

Les sources avant tri (deja integrees/corrigees chez Saidana) sont dans `before/`. Elles ne remplacent pas les sorties originales ChatUniTest de `tache2-rapports/03-chatunitest/`.

| Fichier / methode | Decision | Justification |
|---|---|---|
| `EndianUtils_getIntBE_23_0_Test.java#testGetIntBE` | rejected | Reflexion inutile, erreurs de valeurs ou dexceptions enveloppees ; couverture assuree par autres tests directs. |
| `EndianUtils_getIntBE_23_0_Test.java#testGetIntBEWithOffset` | rejected | Reflexion inutile, erreurs de valeurs ou dexceptions enveloppees ; couverture assuree par autres tests directs. |
| `EndianUtils_getIntBE_23_0_Test.java#testGetIntBEWithEmptyArray` | rejected | Reflexion inutile, erreurs de valeurs ou dexceptions enveloppees ; couverture assuree par autres tests directs. |
| `EndianUtils_getIntBE_23_0_Test.java#testGetIntBEWithNegativeOffset` | rejected | Reflexion inutile, erreurs de valeurs ou dexceptions enveloppees ; couverture assuree par autres tests directs. |
| `EndianUtils_getShortBE_17_1_Test.java#testGetShortBE` | rejected | Reflexion inutile, erreurs de valeurs ou dexceptions enveloppees ; couverture assuree par autres tests directs. |
| `EndianUtils_getShortBE_17_1_Test.java#testGetShortBEWithInvalidOffset` | rejected | Reflexion inutile, erreurs de valeurs ou dexceptions enveloppees ; couverture assuree par autres tests directs. |
| `EndianUtils_getShortBE_17_1_Test.java#testGetShortBEWithNullData` | rejected | Reflexion inutile, erreurs de valeurs ou dexceptions enveloppees ; couverture assuree par autres tests directs. |
| `EndianUtils_getShortLE_12_0_Test.java#testGetShortLE` | rejected | Montage de flux sans rapport avec une lecture de tableau ; cas nominal deja couvert par autres tests directs. |
| `EndianUtils_getUIntBE_26_0_Test.java#testGetUIntBE` | rejected | Montage de flux sans rapport avec une lecture de tableau ; cas nominal deja couvert par autres tests directs. |
| `EndianUtils_getUIntBE_27_0_Test.java#testGetUIntBEWithLargeNumber` | corrected | BE non signe : 00 FF FF FF = 16777215. |
| `EndianUtils_getUIntBE_27_0_Test.java#testGetUIntBEWithOffset` | corrected | Les quatre octets a offset 4 sont nuls. |
| `EndianUtils_getUIntLE_24_0_Test.java#testGetUIntLE` | corrected | Les quatre premiers octets sont nuls ; les octets suivants ne sont pas lus. |
| `EndianUtils_getUIntLE_24_0_Test.java#testGetUIntLEWithOffset` | corrected | LE a offset 4 : 00 00 01 00 = 65536. |
| `EndianUtils_getUShortBE_18_0_Test.java#testGetUShortBEWithOffset` | corrected | BE a offset 1 : 02 03 = 515. |
| `EndianUtils_getUShortBE_19_0_Test.java#testGetUShortBE` | corrected | BE : les deux octets lus sont 00 01 ou 00 00. |
| `EndianUtils_getUShortBE_19_0_Test.java#testGetUShortBEWithOffset` | corrected | BE : les deux octets lus sont 00 01 ou 00 00. |
| `EndianUtils_getUShortLE_14_0_Test.java#testGetUShortLE` | corrected | LE : octet de poids faible en premier ; retirer aussi le catch qui masque les exceptions. |
| `EndianUtils_getUShortLE_14_0_Test.java#testGetUShortLEWithOffset` | corrected | LE : octet de poids faible en premier ; retirer aussi le catch qui masque les exceptions. |
| `EndianUtils_readIntBE_7_1_Test.java#testReadIntBEWithNegativeInput` | corrected | ByteArrayInputStream.read renvoie 255 pour le byte -1 ; quatre octets disponibles, entier BE signe. Ajouter BufferUnderrunException a throws apres passage a un appel direct. |
| `EndianUtils_readIntLE_6_1_Test.java#testReadIntLENegativeInput` | corrected | Quatre octets FF donnent -1 en entier signe ; ils ne representent pas EOF. |
| `EndianUtils_readIntME_8_1_Test.java#testReadIntME` | rejected | Plusieurs oracles faux dans une seule methode (ordre middle-endian et confusion byte negatif/EOF). Cas nominaux et underrun couverts par les tests originaux et manuels ; candidat conserve hors suite. |
| `EndianUtils_readLongBE_10_1_Test.java#testReadLongBE` | corrected | Assemblage BE des huit octets FF FE FD FC FB FA F9 F8, sans changer les donnees. |
| `EndianUtils_readLongLE_9_1_Test.java#testReadLongLE` | corrected | Assemblage LE des huit octets 12 34 56 78 9A BC DE F0. |
| `EndianUtils_readShortBE_1_1_Test.java#testReadShortBE` | corrected | Deux octets suffisent pour un short BE ; le -1 apres les deux octets nest pas lu. |
| `EndianUtils_readShortLE_0_1_Test.java#testReadShortLEWithNegativeBytes` | corrected | Bytes FF FE disponibles : short LE signe -257, sans underrun. Declaration de BufferUnderrunException necessaire pour lappel direct. |
| `EndianUtils_readUE7_11_0_Test.java#testReadUE7_MultipleBytes` | corrected | Format UE7 : (1 << 7) + 0 = 128. |
| `EndianUtils_readUE7_11_0_Test.java#testReadUE7_MaxValue` | corrected | Premier 7F sans bit de continuation : arret apres le premier octet. Renommer le cas trompeur. |
| `EndianUtils_ubyteToInt_29_1_Test.java#testUbyteToIntNull` | rejected | byte primitif jamais null : exception attendue impossible, cas repetitifs sans intention valide. |
| `EndianUtils_ubyteToInt_29_1_Test.java#testUbyteToIntEmpty` | rejected | byte primitif jamais null : exception attendue impossible, cas repetitifs sans intention valide. |
| `EndianUtils_ubyteToInt_29_1_Test.java#testUbyteToIntInvalid` | rejected | byte primitif jamais null : exception attendue impossible, cas repetitifs sans intention valide. |
| `EndianUtils_ubyteToInt_29_1_Test.java#testUbyteToIntLarge` | rejected | byte primitif jamais null : exception attendue impossible, cas repetitifs sans intention valide. |
| `EndianUtils_ubyteToInt_29_1_Test.java#testUbyteToIntSmall` | rejected | byte primitif jamais null : exception attendue impossible, cas repetitifs sans intention valide. |
| `EndianUtils_ubyteToInt_29_1_Test.java#testUbyteToIntBoundary` | rejected | byte primitif jamais null : exception attendue impossible, cas repetitifs sans intention valide. |
| `EndianUtils_ubyteToInt_29_1_Test.java#testUbyteToIntBoundaryNegative` | rejected | byte primitif jamais null : exception attendue impossible, cas repetitifs sans intention valide. |
| `EndianUtils_ubyteToInt_29_1_Test.java#testUbyteToIntBoundaryPositive` | rejected | byte primitif jamais null : exception attendue impossible, cas repetitifs sans intention valide. |
| `EndianUtils_ubyteToInt_29_1_Test.java#testUbyteToIntBoundaryNegativePositive` | rejected | byte primitif jamais null : exception attendue impossible, cas repetitifs sans intention valide. |
| `EndianUtils_ubyteToInt_29_1_Test.java#testUbyteToIntBoundaryPositiveNegative` | rejected | byte primitif jamais null : exception attendue impossible, cas repetitifs sans intention valide. |
| `EndianUtils_ubyteToInt_29_1_Test.java#testUbyteToIntBoundaryNegativePositiveNegative` | rejected | byte primitif jamais null : exception attendue impossible, cas repetitifs sans intention valide. |
| `FilenameUtils_calculateExtension_10_1_Test.java#testCalculateExtension_withContentType_png` | corrected | Le registre MIME renvoie une extension avec son point, comme les tests originaux. |
| `FilenameUtils_calculateExtension_10_1_Test.java#testCalculateExtension_withContentType_pdf` | corrected | Le registre MIME renvoie une extension avec son point, comme les tests originaux. |
| `FilenameUtils_calculateExtension_10_1_Test.java#testCalculateExtension_withContentType_ocr_png` | corrected | Le registre MIME renvoie une extension avec son point, comme les tests originaux. |
| `FilenameUtils_calculateExtension_10_1_Test.java#testCalculateExtension_withNullMetadata` | rejected | Candidat rejete : oracle non justifie sur le domaine valide ; ne pas figer un comportement douteux ou garder un test artificiel. |
| `FilenameUtils_getName_1_0_Test.java#testGetNameWithParentDirectory` | corrected | getName garde le dernier segment ; il ne normalise ni ? ni une extension seule. |
| `FilenameUtils_getName_1_0_Test.java#testGetNameWithCurrentDirectory` | corrected | getName garde le dernier segment ; il ne normalise ni ? ni une extension seule. |
| `FilenameUtils_getName_1_0_Test.java#testGetNameWithReservedCharacters` | corrected | getName garde le dernier segment ; il ne normalise ni ? ni une extension seule. |
| `FilenameUtils_getName_1_0_Test.java#testGetNameWithASCIINumeric` | corrected | getName garde le dernier segment ; il ne normalise ni ? ni une extension seule. |
| `FilenameUtils_getSanitizedEmbeddedFilePath_4_0_Test.java#testGetSanitizedEmbeddedFilePath_withInvalidPath` | rejected | invalid/path est un chemin relatif valide, test redondant avec withValidPath ; le mot invalid ne prouve aucune invalidite. |
| `FilenameUtils_getSanitizedEmbeddedFilePath_4_0_Test.java#testGetSanitizedEmbeddedFilePath_withProtocol` | rejected | Corps vide : aucun comportement ni oracle ; ne pas compter comme test utile. |
| `FilenameUtils_getSuffixFromPath_2_0_Test.java#testGetSuffixFromPath` | rejected | Methode multi-scenarios avec plusieurs oracles faux ; cas de suffixes deja couverts par les tests originaux et manuels. |
| `FilenameUtils_resolveWithin_5_0_Test.java#testResolveWithinOutside` | corrected | Meme intention de rejet de .. ; TempDir et message exact construit avec Path pour les separateurs du systeme. |
| `FilenameUtils_resolveWithin_5_0_Test.java#testResolveWithinSymlinkOutside` | rejected | Doublon exact du cas Outside ; aucun lien symbolique nest cree. |
| `FilenameUtils_resolveWithin_5_0_Test.java#testResolveWithinReservedCharacters` | rejected | Oracle depend de la plateforme et attend une sortie du repertoire pour un nom lexical interieur. |
| `MediaType_audio_1_1_Test.java#testAudioMethod` | rejected | Plusieurs attentes dexceptions ou de normalisation ne decoulent pas du contrat ; reparation substantielle, fabriques couvertes manuellement. |
| `MediaType_compareTo_20_0_Test.java#testCompareTo` | rejected | compareTo garantit le signe, pas -1/1 ; les attentes pour le prefixe avec parametres sont inversees. Cas correct deja couvert par compareTo_followsCanonicalStringOrder. |
| `MediaType_hashCode_19_1_Test.java#testHashCode` | rejected | Montage Mockito inutilise ; oracle valable mais cas deja couvert par le test manuel hashCode. |
| `MediaType_image_2_0_Test.java#testGetBaseType` | corrected | getBaseType retire les parametres, pas le sous-type. |
| `MediaType_parse_7_0_Test.java#testParseInvalidType` | corrected | Le type est syntaxiquement valide ; parse ne verifie pas un registre de noms. Renommer le cas. |
| `MediaType_parse_7_0_Test.java#testParseParametersWithEscapedQuotes` | rejected | Syntaxe de guillemets echappes et attente ambigues ; conserver lechec pour analyse plutot que figer un comportement non garanti. |
| `MediaType_text_3_1_Test.java#testTextMethod` | rejected | Plusieurs attentes dexceptions ou de normalisation ne decoulent pas du contrat ; reparation substantielle, fabriques couvertes manuellement. |
