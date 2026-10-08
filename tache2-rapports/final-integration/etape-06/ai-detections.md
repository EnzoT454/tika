# 132 nouvelles détections IA de A vers B

PIT fournit le test tueur et le statut. Le mécanisme ci-dessous est une analyse des sources,
pas une trace d’exécution fournie par PIT. Une exception inattendue peut détecter le mutant avant l’assertion.
Les sources complètes, fixtures et assertions exactes sont archivées ; les corrections humaines restent déclarées.

| ID (préfixe) | Mutant : classe, méthode, ligne, index | Transition | Test / entrée-oracle | Mécanisme |
|---|---|---|---|---|
| fff16a2939f1 | EndianUtils.getIntBE L385 index 9 : Changed increment from 1 to -1 | NO_COVERAGE → KILLED | EndianUtils_getUIntBE_27_0_Test#testGetUIntBEWithLargeNumber ; Octets 00 FF FF FF ; entier non signé BE 0x00FFFFFFL. | Le parcours d’indices change : mauvaise valeur ou accès hors bornes. assertEquals échoue ou le test lève une exception inattendue (enveloppée par invoke si réflexion). |
| dbb6c88a8c88 | EndianUtils.getIntBE L386 index 18 : Changed increment from 1 to -1 | NO_COVERAGE → KILLED | EndianUtils_getUIntBE_27_0_Test#testGetUIntBEWithLargeNumber ; Octets 00 FF FF FF ; entier non signé BE 0x00FFFFFFL. | Le parcours d’indices change : mauvaise valeur ou accès hors bornes. assertEquals échoue ou le test lève une exception inattendue (enveloppée par invoke si réflexion). |
| 88e530c117a3 | EndianUtils.getIntBE L387 index 27 : Changed increment from 1 to -1 | NO_COVERAGE → KILLED | EndianUtils_getIntBE_22_0_Test#testGetIntBEWithOffset ; Tableau 01…08, offset 4 ; valeur BE 0x05060708. | Le parcours d’indices change : mauvaise valeur ou accès hors bornes. assertEquals échoue ou le test lève une exception inattendue (enveloppée par invoke si réflexion). |
| df49dab32c90 | EndianUtils.getIntBE L385 index 12 : Replaced bitwise AND with OR | NO_COVERAGE → KILLED | EndianUtils_getUIntBE_27_0_Test#testGetUIntBEWithLargeNumber ; Octets 00 FF FF FF ; entier non signé BE 0x00FFFFFFL. | Le masque de l’octet ou de l’entier non signé devient un OU qui force des bits à 1 ; la valeur exacte attendue par assertEquals change. |
| 1a03b49a9ae0 | EndianUtils.getIntBE L386 index 21 : Replaced bitwise AND with OR | NO_COVERAGE → KILLED | EndianUtils_getUIntBE_27_0_Test#testGetUIntBEWithLargeNumber ; Octets 00 FF FF FF ; entier non signé BE 0x00FFFFFFL. | Le masque de l’octet ou de l’entier non signé devient un OU qui force des bits à 1 ; la valeur exacte attendue par assertEquals change. |
| 20e6c6f2b531 | EndianUtils.getIntBE L387 index 30 : Replaced bitwise AND with OR | NO_COVERAGE → KILLED | EndianUtils_getUIntBE_27_0_Test#testGetUIntBEWithLargeNumber ; Octets 00 FF FF FF ; entier non signé BE 0x00FFFFFFL. | Le masque de l’octet ou de l’entier non signé devient un OU qui force des bits à 1 ; la valeur exacte attendue par assertEquals change. |
| 590573f30f9a | EndianUtils.getIntBE L388 index 39 : Replaced bitwise AND with OR | NO_COVERAGE → KILLED | EndianUtils_getUIntBE_27_0_Test#testGetUIntBEWithLargeNumber ; Octets 00 FF FF FF ; entier non signé BE 0x00FFFFFFL. | Le masque de l’octet ou de l’entier non signé devient un OU qui force des bits à 1 ; la valeur exacte attendue par assertEquals change. |
| c68ce0f75e43 | EndianUtils.getIntBE L389 index 45 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_getUIntBE_27_0_Test#testGetUIntBEWithNegativeNumber ; Octets FF FF FF FF ; entier non signé 4294967295L. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| 132aaa40d5e7 | EndianUtils.getIntBE L389 index 48 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_getUIntBE_27_0_Test#testGetUIntBEWithLargeNumber ; Octets 00 FF FF FF ; entier non signé BE 0x00FFFFFFL. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| 2e4acdc81038 | EndianUtils.getIntBE L389 index 49 : Replaced integer addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_getUIntBE_27_0_Test#testGetUIntBEWithLargeNumber ; Octets 00 FF FF FF ; entier non signé BE 0x00FFFFFFL. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 8ae9a744fbdc | EndianUtils.getIntBE L389 index 52 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_getUIntBE_27_0_Test#testGetUIntBEWithLargeNumber ; Octets 00 FF FF FF ; entier non signé BE 0x00FFFFFFL. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| c6b3ce4b32e6 | EndianUtils.getIntBE L389 index 53 : Replaced integer addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_getUIntBE_27_0_Test#testGetUIntBEWithLargeNumber ; Octets 00 FF FF FF ; entier non signé BE 0x00FFFFFFL. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 210e09561341 | EndianUtils.getIntBE L389 index 55 : Replaced integer addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_getUIntBE_27_0_Test#testGetUIntBEWithLargeNumber ; Octets 00 FF FF FF ; entier non signé BE 0x00FFFFFFL. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 93d1e923bc17 | EndianUtils.getIntBE L389 index 56 : replaced int return with 0 for org/apache/tika/io/EndianUtils::getIntBE | NO_COVERAGE → KILLED | EndianUtils_getUIntBE_27_0_Test#testGetUIntBEWithLargeNumber ; Octets 00 FF FF FF ; entier non signé BE 0x00FFFFFFL. | La valeur attendue est non nulle ; assertEquals distingue le retour forcé à zéro. |
| 1490421021da | EndianUtils.getIntLE L347 index 6 : replaced int return with 0 for org/apache/tika/io/EndianUtils::getIntLE | NO_COVERAGE → KILLED | EndianUtils_getIntLE_20_0_Test#testGetIntLE ; Octets 01 02 03 04 ; valeur LE 0x04030201 (appel direct ou réflexion). | La valeur attendue est non nulle ; assertEquals distingue le retour forcé à zéro. |
| 35e4a7b2dcfe | EndianUtils.getIntLE L359 index 9 : Changed increment from 1 to -1 | NO_COVERAGE → KILLED | EndianUtils_getIntLE_21_0_Test#testGetIntLE ; Octets 01 02 03 04 ; valeur LE 0x04030201 (appel direct ou réflexion). | Le parcours d’indices change : mauvaise valeur ou accès hors bornes. assertEquals échoue ou le test lève une exception inattendue (enveloppée par invoke si réflexion). |
| 0ec3ed1b8cf3 | EndianUtils.getIntLE L360 index 18 : Changed increment from 1 to -1 | NO_COVERAGE → KILLED | EndianUtils_getIntLE_21_0_Test#testGetIntLE ; Octets 01 02 03 04 ; valeur LE 0x04030201 (appel direct ou réflexion). | Le parcours d’indices change : mauvaise valeur ou accès hors bornes. assertEquals échoue ou le test lève une exception inattendue (enveloppée par invoke si réflexion). |
| 7f99faee3f75 | EndianUtils.getIntLE L361 index 27 : Changed increment from 1 to -1 | NO_COVERAGE → KILLED | EndianUtils_getIntLE_21_0_Test#testGetIntLE ; Octets 01 02 03 04 ; valeur LE 0x04030201 (appel direct ou réflexion). | Le parcours d’indices change : mauvaise valeur ou accès hors bornes. assertEquals échoue ou le test lève une exception inattendue (enveloppée par invoke si réflexion). |
| 4e533aec9e7b | EndianUtils.getIntLE L359 index 12 : Replaced bitwise AND with OR | NO_COVERAGE → KILLED | EndianUtils_getIntLE_21_0_Test#testGetIntLE ; Octets 01 02 03 04 ; valeur LE 0x04030201 (appel direct ou réflexion). | Le masque de l’octet ou de l’entier non signé devient un OU qui force des bits à 1 ; la valeur exacte attendue par assertEquals change. |
| 29ed34ba4e35 | EndianUtils.getIntLE L360 index 21 : Replaced bitwise AND with OR | NO_COVERAGE → KILLED | EndianUtils_getIntLE_21_0_Test#testGetIntLE ; Octets 01 02 03 04 ; valeur LE 0x04030201 (appel direct ou réflexion). | Le masque de l’octet ou de l’entier non signé devient un OU qui force des bits à 1 ; la valeur exacte attendue par assertEquals change. |
| c30cae725945 | EndianUtils.getIntLE L361 index 30 : Replaced bitwise AND with OR | NO_COVERAGE → KILLED | EndianUtils_getIntLE_21_0_Test#testGetIntLE ; Octets 01 02 03 04 ; valeur LE 0x04030201 (appel direct ou réflexion). | Le masque de l’octet ou de l’entier non signé devient un OU qui force des bits à 1 ; la valeur exacte attendue par assertEquals change. |
| f212854b42ea | EndianUtils.getIntLE L362 index 39 : Replaced bitwise AND with OR | NO_COVERAGE → KILLED | EndianUtils_getIntLE_21_0_Test#testGetIntLE ; Octets 01 02 03 04 ; valeur LE 0x04030201 (appel direct ou réflexion). | Le masque de l’octet ou de l’entier non signé devient un OU qui force des bits à 1 ; la valeur exacte attendue par assertEquals change. |
| e6a3a8d8f81b | EndianUtils.getIntLE L363 index 45 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_getIntLE_21_0_Test#testGetIntLE ; Octets 01 02 03 04 ; valeur LE 0x04030201 (appel direct ou réflexion). | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| f207f16c97cf | EndianUtils.getIntLE L363 index 48 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_getIntLE_21_0_Test#testGetIntLE ; Octets 01 02 03 04 ; valeur LE 0x04030201 (appel direct ou réflexion). | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| 8be33ef0998a | EndianUtils.getIntLE L363 index 49 : Replaced integer addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_getIntLE_21_0_Test#testGetIntLE ; Octets 01 02 03 04 ; valeur LE 0x04030201 (appel direct ou réflexion). | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 0c8da0339d73 | EndianUtils.getIntLE L363 index 52 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_getIntLE_21_0_Test#testGetIntLE ; Octets 01 02 03 04 ; valeur LE 0x04030201 (appel direct ou réflexion). | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| b8c8116af463 | EndianUtils.getIntLE L363 index 53 : Replaced integer addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_getIntLE_21_0_Test#testGetIntLE ; Octets 01 02 03 04 ; valeur LE 0x04030201 (appel direct ou réflexion). | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 084a78b535c8 | EndianUtils.getIntLE L363 index 55 : Replaced integer addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_getIntLE_21_0_Test#testGetIntLE ; Octets 01 02 03 04 ; valeur LE 0x04030201 (appel direct ou réflexion). | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 0bb56be5b4e8 | EndianUtils.getIntLE L363 index 56 : replaced int return with 0 for org/apache/tika/io/EndianUtils::getIntLE | NO_COVERAGE → KILLED | EndianUtils_getIntLE_21_0_Test#testGetIntLE ; Octets 01 02 03 04 ; valeur LE 0x04030201 (appel direct ou réflexion). | La valeur attendue est non nulle ; assertEquals distingue le retour forcé à zéro. |
| d656d212137a | EndianUtils.getLongLE L446 index 17 : changed conditional boundary | NO_COVERAGE → KILLED | EndianUtils_getLongLE_28_0_Test#testGetLongLE ; Octets 12 34 56 78 9A BC DE F0, offset 0 ; valeur LE 0xF0DEBC9A78563412L. | La boucle j>=offset devient j>offset : l’octet à offset 0 est omis ; assertEquals sur la valeur longue échoue. |
| 7677ac356e89 | EndianUtils.getLongLE L446 index 9 : Replaced integer addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_getLongLE_28_0_Test#testGetLongLE ; Octets 12 34 56 78 9A BC DE F0, offset 0 ; valeur LE 0xF0DEBC9A78563412L. | La borne initiale offset+LONG_SIZE-1 devient offset-LONG_SIZE-1 ; la boucle est sautée à offset 0, résultat 0 au lieu de la valeur longue attendue. |
| 85d762562218 | EndianUtils.getLongLE L446 index 11 : Replaced integer subtraction with addition | NO_COVERAGE → KILLED | EndianUtils_getLongLE_28_0_Test#testGetLongLE ; Octets 12 34 56 78 9A BC DE F0, offset 0 ; valeur LE 0xF0DEBC9A78563412L. | La borne initiale de getLongLE passe de offset+7 à offset+9 ; le tableau de huit octets est dépassé avant assertEquals. |
| f9745308e66f | EndianUtils.getLongLE L447 index 22 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_getLongLE_28_0_Test#testGetLongLE ; Octets 12 34 56 78 9A BC DE F0, offset 0 ; valeur LE 0xF0DEBC9A78563412L. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| 4524ede9af87 | EndianUtils.getLongLE L448 index 31 : Replaced bitwise AND with OR | NO_COVERAGE → KILLED | EndianUtils_getLongLE_28_0_Test#testGetLongLE ; Octets 12 34 56 78 9A BC DE F0, offset 0 ; valeur LE 0xF0DEBC9A78563412L. | Le masque de l’octet ou de l’entier non signé devient un OU qui force des bits à 1 ; la valeur exacte attendue par assertEquals change. |
| 84940f3ade0b | EndianUtils.getLongLE L448 index 33 : Replaced bitwise OR with AND | NO_COVERAGE → KILLED | EndianUtils_getLongLE_28_0_Test#testGetLongLE ; Octets 12 34 56 78 9A BC DE F0, offset 0 ; valeur LE 0xF0DEBC9A78563412L. | L’accumulation commence à zéro ; le ET empêche de composer les octets et contredit la valeur longue attendue. |
| 9a7cb4a5df68 | EndianUtils.getLongLE L446 index 17 : negated conditional | NO_COVERAGE → KILLED | EndianUtils_getLongLE_28_0_Test#testGetLongLE ; Octets 12 34 56 78 9A BC DE F0, offset 0 ; valeur LE 0xF0DEBC9A78563412L. | j>=offset devient j<offset ; la boucle est sautée à offset 0, résultat 0 au lieu de la valeur longue attendue. |
| 2987bb8b329c | EndianUtils.getLongLE L450 index 43 : replaced long return with 0 for org/apache/tika/io/EndianUtils::getLongLE | NO_COVERAGE → KILLED | EndianUtils_getLongLE_28_0_Test#testGetLongLE ; Octets 12 34 56 78 9A BC DE F0, offset 0 ; valeur LE 0xF0DEBC9A78563412L. | La valeur attendue est non nulle ; assertEquals distingue le retour forcé à zéro. |
| 7989685a8e51 | EndianUtils.getShortLE L270 index 7 : replaced short return with 0 for org/apache/tika/io/EndianUtils::getShortLE | NO_COVERAGE → KILLED | EndianUtils_getShortLE_13_0_Test#testGetShortLE ; Octets 01 02, offset 0 ; short LE 0x0201. | La valeur attendue est non nulle ; assertEquals distingue le retour forcé à zéro. |
| 0adf3e308a83 | EndianUtils.getUByte L472 index 7 : Replaced bitwise AND with OR | NO_COVERAGE → KILLED | EndianUtils_getUByte_30_0_Test#testGetUByte ; Premier octet 01, offset 0 ; valeur non signée 1. | Le masque de l’octet ou de l’entier non signé devient un OU qui force des bits à 1 ; la valeur exacte attendue par assertEquals change. |
| 723c124a4951 | EndianUtils.getUByte L472 index 9 : replaced short return with 0 for org/apache/tika/io/EndianUtils::getUByte | NO_COVERAGE → KILLED | EndianUtils_getUByte_30_0_Test#testGetUByte ; Premier octet 01, offset 0 ; valeur non signée 1. | La valeur attendue est non nulle ; assertEquals distingue le retour forcé à zéro. |
| 547f84e6e37b | EndianUtils.getUIntBE L433 index 12 : Replaced bitwise AND with OR | NO_COVERAGE → KILLED | EndianUtils_getUIntBE_27_0_Test#testGetUIntBEWithLargeNumber ; Octets 00 FF FF FF ; entier non signé BE 0x00FFFFFFL. | Le masque de l’octet ou de l’entier non signé devient un OU qui force des bits à 1 ; la valeur exacte attendue par assertEquals change. |
| b54e93bf9146 | EndianUtils.getUIntBE L433 index 13 : replaced long return with 0 for org/apache/tika/io/EndianUtils::getUIntBE | NO_COVERAGE → KILLED | EndianUtils_getUIntBE_27_0_Test#testGetUIntBEWithLargeNumber ; Octets 00 FF FF FF ; entier non signé BE 0x00FFFFFFL. | La valeur attendue est non nulle ; assertEquals distingue le retour forcé à zéro. |
| b353acff7923 | EndianUtils.getUIntLE L411 index 12 : Replaced bitwise AND with OR | NO_COVERAGE → KILLED | EndianUtils_getUIntLE_24_0_Test#testGetUIntLEWithOffset ; Tableau 00 00 00 00 00 00 01 00, offset 4 ; valeur LE 0x00010000L. | Le masque de l’octet ou de l’entier non signé devient un OU qui force des bits à 1 ; la valeur exacte attendue par assertEquals change. |
| 7e4f30f0ef69 | EndianUtils.getUIntLE L411 index 13 : replaced long return with 0 for org/apache/tika/io/EndianUtils::getUIntLE | NO_COVERAGE → KILLED | EndianUtils_getUIntLE_24_0_Test#testGetUIntLEWithOffset ; Tableau 00 00 00 00 00 00 01 00, offset 4 ; valeur LE 0x00010000L. | La valeur attendue est non nulle ; assertEquals distingue le retour forcé à zéro. |
| 62ffabd78edb | EndianUtils.getUShortBE L324 index 6 : replaced int return with 0 for org/apache/tika/io/EndianUtils::getUShortBE | NO_COVERAGE → KILLED | EndianUtils_getUShortBE_18_0_Test#testGetUShortBE ; Octets 01 02 ; entier BE non signé 258. | La valeur attendue est non nulle ; assertEquals distingue le retour forcé à zéro. |
| 7e3cd45b207a | EndianUtils.getUShortBE L335 index 7 : Replaced bitwise AND with OR | NO_COVERAGE → KILLED | EndianUtils_getUShortBE_18_0_Test#testGetUShortBE ; Octets 01 02 ; entier BE non signé 258. | Le masque de l’octet ou de l’entier non signé devient un OU qui force des bits à 1 ; la valeur exacte attendue par assertEquals change. |
| d7c2ad9dde69 | EndianUtils.getUShortBE L336 index 14 : Replaced integer addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_getUShortBE_18_0_Test#testGetUShortBE ; Octets 01 02 ; entier BE non signé 258. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 2672bab90283 | EndianUtils.getUShortBE L336 index 17 : Replaced bitwise AND with OR | NO_COVERAGE → KILLED | EndianUtils_getUShortBE_18_0_Test#testGetUShortBE ; Octets 01 02 ; entier BE non signé 258. | Le masque de l’octet ou de l’entier non signé devient un OU qui force des bits à 1 ; la valeur exacte attendue par assertEquals change. |
| 5ef1a300eb5e | EndianUtils.getUShortBE L337 index 23 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_getUShortBE_18_0_Test#testGetUShortBE ; Octets 01 02 ; entier BE non signé 258. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| 0b691a6abd2f | EndianUtils.getUShortBE L337 index 25 : Replaced integer addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_getUShortBE_18_0_Test#testGetUShortBE ; Octets 01 02 ; entier BE non signé 258. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 9d385329c540 | EndianUtils.getUShortBE L337 index 26 : replaced int return with 0 for org/apache/tika/io/EndianUtils::getUShortBE | NO_COVERAGE → KILLED | EndianUtils_getUShortBE_18_0_Test#testGetUShortBE ; Octets 01 02 ; entier BE non signé 258. | La valeur attendue est non nulle ; assertEquals distingue le retour forcé à zéro. |
| 252f898c36c0 | EndianUtils.getUShortLE L280 index 6 : replaced int return with 0 for org/apache/tika/io/EndianUtils::getUShortLE | NO_COVERAGE → KILLED | EndianUtils_getUShortLE_14_0_Test#testGetUShortLE ; Octets 00 01 ; entier LE non signé 256. | La valeur attendue est non nulle ; assertEquals distingue le retour forcé à zéro. |
| 4cbf55500162 | EndianUtils.getUShortLE L291 index 7 : Replaced bitwise AND with OR | NO_COVERAGE → KILLED | EndianUtils_getUShortLE_15_0_Test#testGetUShortLEWithOffset ; Tableau 01…08, offset 2 ; entier LE 0x0403. | Le masque de l’octet ou de l’entier non signé devient un OU qui force des bits à 1 ; la valeur exacte attendue par assertEquals change. |
| 4e5b4e913161 | EndianUtils.getUShortLE L292 index 14 : Replaced integer addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_getUShortLE_15_0_Test#testGetUShortLEWithOffset ; Tableau 01…08, offset 2 ; entier LE 0x0403. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 3be675ca1dac | EndianUtils.getUShortLE L292 index 17 : Replaced bitwise AND with OR | NO_COVERAGE → KILLED | EndianUtils_getUShortLE_15_0_Test#testGetUShortLEWithOffset ; Tableau 01…08, offset 2 ; entier LE 0x0403. | Le masque de l’octet ou de l’entier non signé devient un OU qui force des bits à 1 ; la valeur exacte attendue par assertEquals change. |
| e2cfc774a17d | EndianUtils.getUShortLE L293 index 23 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_getUShortLE_15_0_Test#testGetUShortLEWithOffset ; Tableau 01…08, offset 2 ; entier LE 0x0403. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| 9fd5cca52314 | EndianUtils.getUShortLE L293 index 25 : Replaced integer addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_getUShortLE_15_0_Test#testGetUShortLEWithOffset ; Tableau 01…08, offset 2 ; entier LE 0x0403. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 480bb59ca415 | EndianUtils.getUShortLE L293 index 26 : replaced int return with 0 for org/apache/tika/io/EndianUtils::getUShortLE | NO_COVERAGE → KILLED | EndianUtils_getUShortLE_15_0_Test#testGetUShortLEWithOffset ; Tableau 01…08, offset 2 ; entier LE 0x0403. | La valeur attendue est non nulle ; assertEquals distingue le retour forcé à zéro. |
| bca2a36c12d3 | EndianUtils.readIntBE L149 index 27 : Replaced bitwise OR with AND | NO_COVERAGE → KILLED | EndianUtils_readIntBE_7_1_Test#testReadIntBEWithPartialInput ; Mockito renvoie 01, 02, -1, 0 ; BufferUnderrunException obligatoire. | Le -1 signalant la fin prématurée est masqué par un ET avec des octets valides ; l’exception exigée par assertThrows disparaît. |
| 3e61cc567903 | EndianUtils.readIntBE L149 index 29 : Replaced bitwise OR with AND | NO_COVERAGE → KILLED | EndianUtils_readIntBE_7_1_Test#testReadIntBEWithPartialInput ; Mockito renvoie 01, 02, -1, 0 ; BufferUnderrunException obligatoire. | Le -1 signalant la fin prématurée est masqué par un ET avec des octets valides ; l’exception exigée par assertThrows disparaît. |
| 42d388ec04ef | EndianUtils.readIntBE L152 index 42 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_readIntBE_7_1_Test#testReadIntBEWithNegativeInput ; Flux FF 00 00 00 ; int signé BE 0xFF000000. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| a24ee275217f | EndianUtils.readIntBE L152 index 45 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_readIntBE_7_1_Test#testReadIntBEWithValidInput ; Flux 01 02 03 04 ; int BE 0x01020304. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| 614849ce2513 | EndianUtils.readIntBE L152 index 46 : Replaced integer addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_readIntBE_7_1_Test#testReadIntBEWithValidInput ; Flux 01 02 03 04 ; int BE 0x01020304. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| f525af4e54ba | EndianUtils.readIntBE L152 index 49 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_readIntBE_7_1_Test#testReadIntBEWithValidInput ; Flux 01 02 03 04 ; int BE 0x01020304. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| a2fb84f40d88 | EndianUtils.readIntBE L152 index 50 : Replaced integer addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_readIntBE_7_1_Test#testReadIntBEWithValidInput ; Flux 01 02 03 04 ; int BE 0x01020304. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 84b2b04d1385 | EndianUtils.readIntBE L152 index 52 : Replaced integer addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_readIntBE_7_1_Test#testReadIntBEWithValidInput ; Flux 01 02 03 04 ; int BE 0x01020304. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| bf8a50afb19b | EndianUtils.readIntBE L149 index 30 : negated conditional | NO_COVERAGE → KILLED | EndianUtils_readIntBE_7_1_Test#testReadIntBEWithNegativeInput ; Flux FF 00 00 00 ; int signé BE 0xFF000000. | La garde d’underrun est inversée sur un flux complet ; BufferUnderrunException inattendue au lieu de la valeur attendue par assertEquals. |
| 5913149a827d | EndianUtils.readIntBE L152 index 53 : replaced int return with 0 for org/apache/tika/io/EndianUtils::readIntBE | NO_COVERAGE → KILLED | EndianUtils_readIntBE_7_1_Test#testReadIntBEWithNegativeInput ; Flux FF 00 00 00 ; int signé BE 0xFF000000. | La valeur attendue est non nulle ; assertEquals distingue le retour forcé à zéro. |
| 9384d3ef0af6 | EndianUtils.readIntLE L133 index 42 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_readIntLE_6_1_Test#testReadIntLEValidInput ; Flux 12 34 56 78 ; int LE 0x78563412. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| 409124b2ff09 | EndianUtils.readIntLE L133 index 45 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_readIntLE_6_1_Test#testReadIntLEValidInput ; Flux 12 34 56 78 ; int LE 0x78563412. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| 918ead6d2ca7 | EndianUtils.readIntLE L133 index 46 : Replaced integer addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_readIntLE_6_1_Test#testReadIntLEValidInput ; Flux 12 34 56 78 ; int LE 0x78563412. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 67b7ad6b4e8c | EndianUtils.readIntLE L133 index 49 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_readIntLE_6_1_Test#testReadIntLEValidInput ; Flux 12 34 56 78 ; int LE 0x78563412. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| c6a9e0362be6 | EndianUtils.readIntLE L133 index 50 : Replaced integer addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_readIntLE_6_1_Test#testReadIntLEValidInput ; Flux 12 34 56 78 ; int LE 0x78563412. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 0eec7f861bf5 | EndianUtils.readIntLE L133 index 52 : Replaced integer addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_readIntLE_6_1_Test#testReadIntLEValidInput ; Flux 12 34 56 78 ; int LE 0x78563412. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 11a5098e8f41 | EndianUtils.readIntLE L130 index 30 : negated conditional | NO_COVERAGE → KILLED | EndianUtils_readIntLE_6_1_Test#testReadIntLEValidInput ; Flux 12 34 56 78 ; int LE 0x78563412. | La garde d’underrun est inversée sur un flux complet ; BufferUnderrunException inattendue au lieu de la valeur attendue par assertEquals. |
| d5514a77879c | EndianUtils.readIntLE L133 index 53 : replaced int return with 0 for org/apache/tika/io/EndianUtils::readIntLE | NO_COVERAGE → KILLED | EndianUtils_readIntLE_6_1_Test#testReadIntLEValidInput ; Flux 12 34 56 78 ; int LE 0x78563412. | La valeur attendue est non nulle ; assertEquals distingue le retour forcé à zéro. |
| 2302b5ecac86 | EndianUtils.readLongBE L221 index 71 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_readLongBE_10_1_Test#testReadLongBE ; Flux 01…08 → 0x0102030405060708L ; FF FE FD FC FB FA F9 F8 → 0xFFFEFDFCFBFAF9F8L ; quatre octets → underrun. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| 2546758af977 | EndianUtils.readLongBE L221 index 75 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_readLongBE_10_1_Test#testReadLongBE ; Flux 01…08 → 0x0102030405060708L ; FF FE FD FC FB FA F9 F8 → 0xFFFEFDFCFBFAF9F8L ; quatre octets → underrun. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| f35f3d8efd60 | EndianUtils.readLongBE L221 index 76 : Replaced long addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_readLongBE_10_1_Test#testReadLongBE ; Flux 01…08 → 0x0102030405060708L ; FF FE FD FC FB FA F9 F8 → 0xFFFEFDFCFBFAF9F8L ; quatre octets → underrun. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 9d31d011428e | EndianUtils.readLongBE L221 index 80 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_readLongBE_10_1_Test#testReadLongBE ; Flux 01…08 → 0x0102030405060708L ; FF FE FD FC FB FA F9 F8 → 0xFFFEFDFCFBFAF9F8L ; quatre octets → underrun. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| e97ee6280b9e | EndianUtils.readLongBE L221 index 81 : Replaced long addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_readLongBE_10_1_Test#testReadLongBE ; Flux 01…08 → 0x0102030405060708L ; FF FE FD FC FB FA F9 F8 → 0xFFFEFDFCFBFAF9F8L ; quatre octets → underrun. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| c92404ad5a8c | EndianUtils.readLongBE L221 index 85 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_readLongBE_10_1_Test#testReadLongBE ; Flux 01…08 → 0x0102030405060708L ; FF FE FD FC FB FA F9 F8 → 0xFFFEFDFCFBFAF9F8L ; quatre octets → underrun. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| b302dc73d92e | EndianUtils.readLongBE L221 index 86 : Replaced long addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_readLongBE_10_1_Test#testReadLongBE ; Flux 01…08 → 0x0102030405060708L ; FF FE FD FC FB FA F9 F8 → 0xFFFEFDFCFBFAF9F8L ; quatre octets → underrun. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 7a291aa7cf1d | EndianUtils.readLongBE L221 index 90 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_readLongBE_10_1_Test#testReadLongBE ; Flux 01…08 → 0x0102030405060708L ; FF FE FD FC FB FA F9 F8 → 0xFFFEFDFCFBFAF9F8L ; quatre octets → underrun. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| 0bbd35cf6508 | EndianUtils.readLongBE L221 index 91 : Replaced long addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_readLongBE_10_1_Test#testReadLongBE ; Flux 01…08 → 0x0102030405060708L ; FF FE FD FC FB FA F9 F8 → 0xFFFEFDFCFBFAF9F8L ; quatre octets → underrun. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| ecca8f54d279 | EndianUtils.readLongBE L221 index 94 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_readLongBE_10_1_Test#testReadLongBE ; Flux 01…08 → 0x0102030405060708L ; FF FE FD FC FB FA F9 F8 → 0xFFFEFDFCFBFAF9F8L ; quatre octets → underrun. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| 00ca9e131be3 | EndianUtils.readLongBE L221 index 96 : Replaced long addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_readLongBE_10_1_Test#testReadLongBE ; Flux 01…08 → 0x0102030405060708L ; FF FE FD FC FB FA F9 F8 → 0xFFFEFDFCFBFAF9F8L ; quatre octets → underrun. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| be911dd2bf93 | EndianUtils.readLongBE L221 index 99 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_readLongBE_10_1_Test#testReadLongBE ; Flux 01…08 → 0x0102030405060708L ; FF FE FD FC FB FA F9 F8 → 0xFFFEFDFCFBFAF9F8L ; quatre octets → underrun. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| ec06b34e01f4 | EndianUtils.readLongBE L221 index 101 : Replaced long addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_readLongBE_10_1_Test#testReadLongBE ; Flux 01…08 → 0x0102030405060708L ; FF FE FD FC FB FA F9 F8 → 0xFFFEFDFCFBFAF9F8L ; quatre octets → underrun. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| b114e79ad645 | EndianUtils.readLongBE L221 index 104 : Replaced long addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_readLongBE_10_1_Test#testReadLongBE ; Flux 01…08 → 0x0102030405060708L ; FF FE FD FC FB FA F9 F8 → 0xFFFEFDFCFBFAF9F8L ; quatre octets → underrun. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 9d8a4f8516eb | EndianUtils.readLongBE L217 index 58 : negated conditional | NO_COVERAGE → KILLED | EndianUtils_readLongBE_10_1_Test#testReadLongBE ; Flux 01…08 → 0x0102030405060708L ; FF FE FD FC FB FA F9 F8 → 0xFFFEFDFCFBFAF9F8L ; quatre octets → underrun. | La garde d’underrun est inversée sur un flux complet ; BufferUnderrunException inattendue au lieu de la valeur attendue par assertEquals. |
| abe1841f10ae | EndianUtils.readLongBE L221 index 105 : replaced long return with 0 for org/apache/tika/io/EndianUtils::readLongBE | NO_COVERAGE → KILLED | EndianUtils_readLongBE_10_1_Test#testReadLongBE ; Flux 01…08 → 0x0102030405060708L ; FF FE FD FC FB FA F9 F8 → 0xFFFEFDFCFBFAF9F8L ; quatre octets → underrun. | La valeur attendue est non nulle ; assertEquals distingue le retour forcé à zéro. |
| 3c816a900bc0 | EndianUtils.readLongLE L191 index 57 : Replaced bitwise OR with AND | NO_COVERAGE → KILLED | EndianUtils_readLongLE_9_1_Test#testReadLongLE_WithBufferUnderrunException ; Mockito renvoie sept octets valides puis -1 ; underrun et huit lectures. | Le -1 signalant la fin prématurée est masqué par un ET avec des octets valides ; l’exception exigée par assertThrows disparaît. |
| 00c3f3539c88 | EndianUtils.readLongLE L195 index 71 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_readLongLE_9_1_Test#testReadLongLE ; Mockito renvoie 12 34 56 78 9A BC DE F0 ; 0xF0DEBC9A78563412L et huit lectures. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| 0891b5d77200 | EndianUtils.readLongLE L195 index 75 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_readLongLE_9_1_Test#testReadLongLE ; Mockito renvoie 12 34 56 78 9A BC DE F0 ; 0xF0DEBC9A78563412L et huit lectures. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| 222c95245c1e | EndianUtils.readLongLE L195 index 76 : Replaced long addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_readLongLE_9_1_Test#testReadLongLE ; Mockito renvoie 12 34 56 78 9A BC DE F0 ; 0xF0DEBC9A78563412L et huit lectures. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| a46e5c1f0f23 | EndianUtils.readLongLE L195 index 80 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_readLongLE_9_1_Test#testReadLongLE ; Mockito renvoie 12 34 56 78 9A BC DE F0 ; 0xF0DEBC9A78563412L et huit lectures. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| 77b9c2661602 | EndianUtils.readLongLE L195 index 81 : Replaced long addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_readLongLE_9_1_Test#testReadLongLE ; Mockito renvoie 12 34 56 78 9A BC DE F0 ; 0xF0DEBC9A78563412L et huit lectures. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| a0483e171f51 | EndianUtils.readLongLE L195 index 85 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_readLongLE_9_1_Test#testReadLongLE ; Mockito renvoie 12 34 56 78 9A BC DE F0 ; 0xF0DEBC9A78563412L et huit lectures. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| 147585f1041c | EndianUtils.readLongLE L195 index 86 : Replaced long addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_readLongLE_9_1_Test#testReadLongLE ; Mockito renvoie 12 34 56 78 9A BC DE F0 ; 0xF0DEBC9A78563412L et huit lectures. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 7a642214c83e | EndianUtils.readLongLE L195 index 90 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_readLongLE_9_1_Test#testReadLongLE ; Mockito renvoie 12 34 56 78 9A BC DE F0 ; 0xF0DEBC9A78563412L et huit lectures. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| 3b1a5e2bf42f | EndianUtils.readLongLE L195 index 91 : Replaced long addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_readLongLE_9_1_Test#testReadLongLE ; Mockito renvoie 12 34 56 78 9A BC DE F0 ; 0xF0DEBC9A78563412L et huit lectures. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| a79bc5597244 | EndianUtils.readLongLE L195 index 94 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_readLongLE_9_1_Test#testReadLongLE ; Mockito renvoie 12 34 56 78 9A BC DE F0 ; 0xF0DEBC9A78563412L et huit lectures. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| 23d3a2b8262e | EndianUtils.readLongLE L195 index 96 : Replaced long addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_readLongLE_9_1_Test#testReadLongLE ; Mockito renvoie 12 34 56 78 9A BC DE F0 ; 0xF0DEBC9A78563412L et huit lectures. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 2e7d765eba19 | EndianUtils.readLongLE L195 index 99 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_readLongLE_9_1_Test#testReadLongLE ; Mockito renvoie 12 34 56 78 9A BC DE F0 ; 0xF0DEBC9A78563412L et huit lectures. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| 96653596177d | EndianUtils.readLongLE L195 index 101 : Replaced long addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_readLongLE_9_1_Test#testReadLongLE ; Mockito renvoie 12 34 56 78 9A BC DE F0 ; 0xF0DEBC9A78563412L et huit lectures. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 3a1283b95d20 | EndianUtils.readLongLE L195 index 104 : Replaced long addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_readLongLE_9_1_Test#testReadLongLE ; Mockito renvoie 12 34 56 78 9A BC DE F0 ; 0xF0DEBC9A78563412L et huit lectures. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 7c48575daea1 | EndianUtils.readLongLE L191 index 58 : negated conditional | NO_COVERAGE → KILLED | EndianUtils_readLongLE_9_1_Test#testReadLongLE_WithBufferUnderrunException ; Mockito renvoie sept octets valides puis -1 ; underrun et huit lectures. | La garde d’underrun est inversée sur un flux tronqué ; l’exception obligatoire disparaît et assertThrows échoue. |
| 6bdbb3c60b45 | EndianUtils.readLongLE L195 index 105 : replaced long return with 0 for org/apache/tika/io/EndianUtils::readLongLE | NO_COVERAGE → KILLED | EndianUtils_readLongLE_9_1_Test#testReadLongLE ; Mockito renvoie 12 34 56 78 9A BC DE F0 ; 0xF0DEBC9A78563412L et huit lectures. | La valeur attendue est non nulle ; assertEquals distingue le retour forcé à zéro. |
| 89712987de08 | EndianUtils.readShortBE L58 index 6 : replaced short return with 0 for org/apache/tika/io/EndianUtils::readShortBE | NO_COVERAGE → KILLED | EndianUtils_readShortBE_1_1_Test#testReadShortBE ; Mockito renvoie 12 34 puis -1 ; short BE 0x1234 sur les deux premières lectures. | La valeur attendue est non nulle ; assertEquals distingue le retour forcé à zéro. |
| b12d6d3eb402 | EndianUtils.readShortLE L45 index 6 : replaced short return with 0 for org/apache/tika/io/EndianUtils::readShortLE | NO_COVERAGE → KILLED | EndianUtils_readShortLE_0_1_Test#testReadShortLEWithNegativeBytes ; Flux FF FE ; short LE signé 0xFEFF. | La valeur attendue est non nulle ; assertEquals distingue le retour forcé à zéro. |
| 239a0a1acda1 | EndianUtils.readUE7 L235 index 21 : changed conditional boundary | SURVIVED → KILLED | EndianUtils_readUE7_11_0_Test#testReadUE7_MultipleBytes ; Flux 81 00 ; (1 << 7) + 0 = 128L. | Le dernier octet 00 est exclu par i>0 au lieu de i>=0 ; le résultat vaut 1 au lieu de 128 et assertEquals échoue. |
| f002c03f883e | EndianUtils.readUE7 L246 index 64 : changed conditional boundary | SURVIVED → KILLED | EndianUtils_readUE7_11_0_Test#testReadUE7_MultipleBytes ; Flux 81 00 ; (1 << 7) + 0 = 128L. | L’octet final 00 est considéré comme une fin prématurée par i<=0 ; exception inattendue avant assertEquals(128L, result). |
| 6dc58b1ed92e | EndianUtils.readUShortBE L73 index 15 : Replaced bitwise OR with AND | NO_COVERAGE → KILLED | EndianUtils_readShortBE_1_1_Test#testReadUShortBEWithEndOfStream ; Mockito renvoie 12 puis -1 ; underrun attendu. | Le -1 signalant la fin prématurée est masqué par un ET avec des octets valides ; l’exception exigée par assertThrows disparaît. |
| debc5656a490 | EndianUtils.readUShortBE L76 index 28 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_readUShortBE_3_1_Test#testReadUShortBE ; Flux 12 34 ; entier BE non signé 0x1234. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| 5f69009fe37c | EndianUtils.readUShortBE L76 index 30 : Replaced integer addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_readUShortBE_3_1_Test#testReadUShortBE ; Flux 12 34 ; entier BE non signé 0x1234. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 62650f045782 | EndianUtils.readUShortBE L73 index 16 : negated conditional | NO_COVERAGE → KILLED | EndianUtils_readUShortBE_3_1_Test#testReadUShortBE ; Flux 12 34 ; entier BE non signé 0x1234. | La garde d’underrun est inversée sur un flux complet ; BufferUnderrunException inattendue au lieu de la valeur attendue par assertEquals. |
| 0c294268c952 | EndianUtils.readUShortBE L76 index 31 : replaced int return with 0 for org/apache/tika/io/EndianUtils::readUShortBE | NO_COVERAGE → KILLED | EndianUtils_readUShortBE_3_1_Test#testReadUShortBE ; Flux 12 34 ; entier BE non signé 0x1234. | La valeur attendue est non nulle ; assertEquals distingue le retour forcé à zéro. |
| 0b64b7ae3112 | EndianUtils.readUShortLE L64 index 15 : Replaced bitwise OR with AND | NO_COVERAGE → KILLED | EndianUtils_readShortLE_0_1_Test#testReadShortLEWithBufferUnderrun ; Flux contenant seulement 12 ; underrun attendu. | Le -1 signalant la fin prématurée est masqué par un ET avec des octets valides ; l’exception exigée par assertThrows disparaît. |
| 5b81287237b1 | EndianUtils.readUShortLE L67 index 28 : Replaced Shift Left with Shift Right | NO_COVERAGE → KILLED | EndianUtils_readShortLE_0_1_Test#testReadShortLEWithNegativeBytes ; Flux FF FE ; short LE signé 0xFEFF. | Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals. |
| 1a50d152ad9f | EndianUtils.readUShortLE L67 index 30 : Replaced integer addition with subtraction | NO_COVERAGE → KILLED | EndianUtils_readShortLE_0_1_Test#testReadShortLEWithNegativeBytes ; Flux FF FE ; short LE signé 0xFEFF. | La soustraction des contributions non nulles change la valeur endian attendue par assertEquals. |
| 0c4a0af820f7 | EndianUtils.readUShortLE L64 index 16 : negated conditional | NO_COVERAGE → KILLED | EndianUtils_readShortLE_0_1_Test#testReadShortLEWithNegativeBytes ; Flux FF FE ; short LE signé 0xFEFF. | La garde d’underrun est inversée sur un flux complet ; BufferUnderrunException inattendue au lieu de la valeur attendue par assertEquals. |
| 5f3f7b3a0bc7 | EndianUtils.readUShortLE L67 index 31 : replaced int return with 0 for org/apache/tika/io/EndianUtils::readUShortLE | NO_COVERAGE → KILLED | EndianUtils_readShortLE_0_1_Test#testReadShortLEWithNegativeBytes ; Flux FF FE ; short LE signé 0xFEFF. | La valeur attendue est non nulle ; assertEquals distingue le retour forcé à zéro. |
| 22c70c68144d | EndianUtils.ubyteToInt L461 index 5 : Replaced bitwise AND with OR | NO_COVERAGE → KILLED | EndianUtils_ubyteToInt_29_1_Test#testUbyteToIntMax ; Conversion du byte 127 → entier 127 ; le flux local créé est inutilisé. | Le masque de l’octet ou de l’entier non signé devient un OU qui force des bits à 1 ; la valeur exacte attendue par assertEquals change. |
| 9620d1c4bafa | EndianUtils.ubyteToInt L461 index 6 : replaced int return with 0 for org/apache/tika/io/EndianUtils::ubyteToInt | NO_COVERAGE → KILLED | EndianUtils_ubyteToInt_29_1_Test#testUbyteToIntMax ; Conversion du byte 127 → entier 127 ; le flux local créé est inutilisé. | La valeur attendue est non nulle ; assertEquals distingue le retour forcé à zéro. |
| f7f267628212 | FilenameUtils.getEmbeddedName L365 index 11 : negated conditional | SURVIVED → KILLED | FilenameUtils_getSanitizedEmbeddedFileName_3_1_Test#testGetSanitizedEmbeddedFileName_withPathContainingColonAndColonAndColonAndBackslash ; RESOURCE_NAME_KEY seul = C:::/path/to/file.txt ; nom attendu file.txt. | La négation ignore la seule propriété renseignée ; null remplace file.txt et assertEquals échoue. |
| 10cd2f8403ee | FilenameUtils.getEmbeddedPath L344 index 11 : negated conditional | SURVIVED → KILLED | FilenameUtils_getSanitizedEmbeddedFilePath_4_0_Test#testGetSanitizedEmbeddedFilePath_withReservedCharacters ; EMBEDDED_RESOURCE_PATH = invalid:filename, autres propriétés null ; défaut test, maxLength 100 ; résultat non nul sans deux-points. | La négation ignore la seule propriété renseignée ; null viole assertNotNull, avant assertFalse sur les deux-points. |
| 6e7e439e0625 | FilenameUtils.resolveWithin L305 index 38 : negated conditional | SURVIVED → KILLED | FilenameUtils_resolveWithin_5_0_Test#testResolveWithin ; Répertoire /tmp, enfant test.txt ; Path attendu /tmp/test.txt. Dépend de l’absence du fichier au moment de la mesure. | La négation de Files.exists(resolved) force toRealPath sur un enfant absent ; une IOException inattendue empêche assertEquals(Path, Path). |
| 83edf77a2c4a | MediaType.equals L420 index 23 : replaced boolean return with true for org/apache/tika/mime/MediaType::equals | NO_COVERAGE → KILLED | MediaType_equals_18_0_Test#testEqualsWithNull ; Fixture MediaType avec paramètre charset ; equals(null) doit être false. | Le retour false de la branche non-MediaType devient true ; assertFalse(equals(null)) échoue. |
| 2c38d3acd00a | MediaType.parse L277 index 154 : negated conditional | NO_COVERAGE → KILLED | MediaType_image_2_0_Test#testImageMethod ; image/png sans puis avec charset=UTF-8 ; image("") doit renvoyer null. | La négation est sur CHARSET_FIRST_PATTERN.matches(), pas TYPE_PATTERN. image("") aboutit à une tentative group() sans match : exception inattendue avant assertNull(invalidMediaType). |
| 833352172e60 | MediaType.union L349 index 5 : negated conditional | SURVIVED → KILLED | MediaType_image_2_0_Test#testImageMethod ; image/png sans puis avec charset=UTF-8 ; image("") doit renvoyer null. | Le type de base a une map vide et la map ajoutée contient charset=UTF-8 ; la condition inversée perd le paramètre. La chaîne attendue avec charset et son assertion de valeur distinguent le mutant. |
| cadb8f7f4117 | MediaType.video L197 index 6 : replaced return value with null for org/apache/tika/mime/MediaType::video | NO_COVERAGE → KILLED | MediaType_video_4_0_Test#testVideoMethod ; video(mp4) doit égaler new MediaType("video", "mp4", emptyMap). | La fabrique renvoie null ; assertEquals(expectedMediaType, actualMediaType) échoue. |

## Oracles exacts des 32 tests tueurs

### EndianUtils_getIntBE_22_0_Test#testGetIntBEWithOffset

1 nouveaux mutants ; décision : `accepted_unchanged`.

Tableau 01…08, offset 4 ; valeur BE 0x05060708.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_getIntBE_22_0_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testGetIntBEWithOffset() {
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int result = EndianUtils.getIntBE(data, 4);
        assertEquals(0x05060708, result);
    }
```

### EndianUtils_getIntLE_20_0_Test#testGetIntLE

1 nouveaux mutants ; décision : `accepted_unchanged`.

Octets 01 02 03 04 ; valeur LE 0x04030201 (appel direct ou réflexion).

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_getIntLE_20_0_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testGetIntLE() throws IOException, TikaException {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int result = EndianUtils.getIntLE(data);
        assertEquals(0x04030201, result);
    }
```

### EndianUtils_getIntLE_21_0_Test#testGetIntLE

14 nouveaux mutants ; décision : `accepted_unchanged`.

Octets 01 02 03 04 ; valeur LE 0x04030201 (appel direct ou réflexion).

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_getIntLE_21_0_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testGetIntLE() throws Exception {
        EndianUtils endianness = new EndianUtils();
        Method method = endianness.getClass().getDeclaredMethod("getIntLE", byte[].class, int.class);
        method.setAccessible(true);
        byte[] data = new byte[4];
        data[0] = (byte) 0x01;
        data[1] = (byte) 0x02;
        data[2] = (byte) 0x03;
        data[3] = (byte) 0x04;
        int result = (int) method.invoke(endianness, data, 0);
        assertEquals(0x04030201, result);
    }
```

### EndianUtils_getLongLE_28_0_Test#testGetLongLE

8 nouveaux mutants ; décision : `accepted_unchanged`.

Octets 12 34 56 78 9A BC DE F0, offset 0 ; valeur LE 0xF0DEBC9A78563412L.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_getLongLE_28_0_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testGetLongLE() throws Exception {
        EndianUtils endianUtils = new EndianUtils();
        byte[] data = new byte[] { (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x9A, (byte) 0xBC, (byte) 0xDE, (byte) 0xF0 };
        int offset = 0;
        Method method = EndianUtils.class.getDeclaredMethod("getLongLE", byte[].class, int.class);
        method.setAccessible(true);
        long result = (long) method.invoke(endianUtils, data, offset);
        assertEquals(0xF0DEBC9A78563412L, result);
    }
```

### EndianUtils_getShortLE_13_0_Test#testGetShortLE

1 nouveaux mutants ; décision : `accepted_unchanged`.

Octets 01 02, offset 0 ; short LE 0x0201.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_getShortLE_13_0_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testGetShortLE() throws IOException {
        byte[] data = { 0x01, 0x02 };
        int offset = 0;
        short expected = 0x0201;
        short result = EndianUtils.getShortLE(data, offset);
        assertEquals(expected, result);
    }
```

### EndianUtils_getUByte_30_0_Test#testGetUByte

2 nouveaux mutants ; décision : `accepted_unchanged`.

Premier octet 01, offset 0 ; valeur non signée 1.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_getUByte_30_0_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testGetUByte() throws IOException {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int offset = 0;
        short result = EndianUtils.getUByte(data, offset);
        assertEquals(1, result);
    }
```

### EndianUtils_getUIntBE_27_0_Test#testGetUIntBEWithLargeNumber

14 nouveaux mutants ; décision : `corrected`.

Octets 00 FF FF FF ; entier non signé BE 0x00FFFFFFL.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_getUIntBE_27_0_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testGetUIntBEWithLargeNumber() throws IOException, TikaException {
        byte[] data = { (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
        int offset = 0;
        long result = EndianUtils.getUIntBE(data, offset);
        assertEquals(0x00FFFFFFL, result);
    }
```

### EndianUtils_getUIntBE_27_0_Test#testGetUIntBEWithNegativeNumber

1 nouveaux mutants ; décision : `accepted_unchanged`.

Octets FF FF FF FF ; entier non signé 4294967295L.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_getUIntBE_27_0_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testGetUIntBEWithNegativeNumber() throws IOException, TikaException {
        byte[] data = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
        int offset = 0;
        long result = EndianUtils.getUIntBE(data, offset);
        assertEquals(4294967295L, result);
    }
```

### EndianUtils_getUIntLE_24_0_Test#testGetUIntLEWithOffset

2 nouveaux mutants ; décision : `corrected`.

Tableau 00 00 00 00 00 00 01 00, offset 4 ; valeur LE 0x00010000L.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_getUIntLE_24_0_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testGetUIntLEWithOffset() throws IOException {
        byte[] data = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00 };
        long result = EndianUtils.getUIntLE(data, 4);
        assertEquals(0x00010000L, result);
    }
```

### EndianUtils_getUShortBE_18_0_Test#testGetUShortBE

7 nouveaux mutants ; décision : `accepted_unchanged`.

Octets 01 02 ; entier BE non signé 258.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_getUShortBE_18_0_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testGetUShortBE() {
        byte[] data = new byte[] { 0x01, 0x02 };
        int result = EndianUtils.getUShortBE(data);
        assertEquals(258, result);
    }
```

### EndianUtils_getUShortLE_14_0_Test#testGetUShortLE

1 nouveaux mutants ; décision : `corrected`.

Octets 00 01 ; entier LE non signé 256.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_getUShortLE_14_0_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testGetUShortLE() {
            byte[] data = { 0x00, 0x01 };
            int result = EndianUtils.getUShortLE(data);
            assertEquals(0x0100, result);

    }
```

### EndianUtils_getUShortLE_15_0_Test#testGetUShortLEWithOffset

6 nouveaux mutants ; décision : `accepted_unchanged`.

Tableau 01…08, offset 2 ; entier LE 0x0403.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_getUShortLE_15_0_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testGetUShortLEWithOffset() {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        int offset = 2;
        int expected = 0x0403;
        int result = EndianUtils.getUShortLE(data, offset);
        assertEquals(expected, result);
    }
```

### EndianUtils_readIntBE_7_1_Test#testReadIntBEWithNegativeInput

3 nouveaux mutants ; décision : `corrected`.

Flux FF 00 00 00 ; int signé BE 0xFF000000.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_readIntBE_7_1_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testReadIntBEWithNegativeInput() throws IOException, BufferUnderrunException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { -1, 0, 0, 0 });
        assertEquals(0xFF000000, EndianUtils.readIntBE(inputStream));
    }
```

### EndianUtils_readIntBE_7_1_Test#testReadIntBEWithPartialInput

2 nouveaux mutants ; décision : `accepted_unchanged`.

Mockito renvoie 01, 02, -1, 0 ; BufferUnderrunException obligatoire.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_readIntBE_7_1_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testReadIntBEWithPartialInput() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        when(inputStream.read()).thenReturn(0x01, 0x02, -1, 0);
        assertThrows(BufferUnderrunException.class, () -> EndianUtils.readIntBE(inputStream));
    }
```

### EndianUtils_readIntBE_7_1_Test#testReadIntBEWithValidInput

5 nouveaux mutants ; décision : `accepted_unchanged`.

Flux 01 02 03 04 ; int BE 0x01020304.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_readIntBE_7_1_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testReadIntBEWithValidInput() throws IOException, BufferUnderrunException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04 });
        int result = EndianUtils.readIntBE(inputStream);
        assertEquals(0x01020304, result);
    }
```

### EndianUtils_readIntLE_6_1_Test#testReadIntLEValidInput

8 nouveaux mutants ; décision : `accepted_unchanged`.

Flux 12 34 56 78 ; int LE 0x78563412.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_readIntLE_6_1_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testReadIntLEValidInput() throws IOException, TikaException {
        byte[] input = new byte[] { 0x12, 0x34, 0x56, 0x78 };
        InputStream inputStream = new ByteArrayInputStream(input);
        int result = endianUtils.readIntLE(inputStream);
        Assertions.assertEquals(0x78563412, result);
    }
```

### EndianUtils_readLongBE_10_1_Test#testReadLongBE

16 nouveaux mutants ; décision : `corrected`.

Flux 01…08 → 0x0102030405060708L ; FF FE FD FC FB FA F9 F8 → 0xFFFEFDFCFBFAF9F8L ; quatre octets → underrun.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_readLongBE_10_1_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testReadLongBE() throws Exception {
        // Test with valid input
        byte[] input = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        InputStream inputStream = new ByteArrayInputStream(input);
        long result = EndianUtils.readLongBE(inputStream);
        assertEquals(0x0102030405060708L, result);
        // Test with negative input
        input = new byte[] { (byte) 0xFF, (byte) 0xFE, (byte) 0xFD, (byte) 0xFC, (byte) 0xFB, (byte) 0xFA, (byte) 0xF9, (byte) 0xF8 };
        inputStream = new ByteArrayInputStream(input);
        result = EndianUtils.readLongBE(inputStream);
        assertEquals(0xFFFEFDFCFBFAF9F8L, result);
        // Test with buffer underrun
        InputStream shortStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04 });
        Executable executable = () -> EndianUtils.readLongBE(shortStream);
        assertThrows(BufferUnderrunException.class, executable);
    }
```

### EndianUtils_readLongLE_9_1_Test#testReadLongLE

15 nouveaux mutants ; décision : `corrected`.

Mockito renvoie 12 34 56 78 9A BC DE F0 ; 0xF0DEBC9A78563412L et huit lectures.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_readLongLE_9_1_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testReadLongLE() throws IOException, TikaException {
        // Mock the InputStream to return specific values
        when(inputStream.read()).thenReturn(0x12, 0x34, 0x56, 0x78, 0x9A, 0xBC, 0xDE, 0xF0);
        // Call the method under test
        long result = EndianUtils.readLongLE(inputStream);
        // Verify the result
        assertEquals(0xF0DEBC9A78563412L, result);
        // Verify that read() was called exactly 8 times
        verify(inputStream, times(8)).read();
    }
```

### EndianUtils_readLongLE_9_1_Test#testReadLongLE_WithBufferUnderrunException

2 nouveaux mutants ; décision : `accepted_unchanged`.

Mockito renvoie sept octets valides puis -1 ; underrun et huit lectures.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_readLongLE_9_1_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testReadLongLE_WithBufferUnderrunException() throws IOException {
        // Mock the InputStream to return -1 (indicating end of stream)
        when(inputStream.read()).thenReturn(0x12, 0x34, 0x56, 0x78, 0x9A, 0xBC, 0xDE, -1);
        // Call the method under test and assert that it throws BufferUnderrunException
        assertThrows(BufferUnderrunException.class, () -> EndianUtils.readLongLE(inputStream));
        // Verify that read() was called exactly 8 times
        verify(inputStream, times(8)).read();
    }
```

### EndianUtils_readShortBE_1_1_Test#testReadShortBE

1 nouveaux mutants ; décision : `corrected`.

Mockito renvoie 12 34 puis -1 ; short BE 0x1234 sur les deux premières lectures.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_readShortBE_1_1_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testReadShortBE() throws IOException, BufferUnderrunException {
        // Arrange
        // Simulate reading 0x1234 and then end of stream
        when(inputStream.read()).thenReturn(0x12, 0x34, -1);
        EndianUtils endianUtils = new EndianUtils();
        // Act and Assert
        assertEquals((short) 0x1234, endianUtils.readShortBE(inputStream));
    }
```

### EndianUtils_readShortBE_1_1_Test#testReadUShortBEWithEndOfStream

1 nouveaux mutants ; décision : `accepted_unchanged`.

Mockito renvoie 12 puis -1 ; underrun attendu.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_readShortBE_1_1_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testReadUShortBEWithEndOfStream() throws IOException, BufferUnderrunException {
        // Arrange
        // Simulate reading 0x12 and then end of stream
        when(inputStream.read()).thenReturn(0x12, -1);
        EndianUtils endianUtils = new EndianUtils();
        // Act and Assert
        assertThrows(BufferUnderrunException.class, () -> {
            endianUtils.readUShortBE(inputStream);
        });
    }
```

### EndianUtils_readShortLE_0_1_Test#testReadShortLEWithBufferUnderrun

1 nouveaux mutants ; décision : `accepted_unchanged`.

Flux contenant seulement 12 ; underrun attendu.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_readShortLE_0_1_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testReadShortLEWithBufferUnderrun() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 0x12 });
        assertThrows(BufferUnderrunException.class, () -> EndianUtils.readShortLE(inputStream));
    }
```

### EndianUtils_readShortLE_0_1_Test#testReadShortLEWithNegativeBytes

5 nouveaux mutants ; décision : `corrected`.

Flux FF FE ; short LE signé 0xFEFF.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_readShortLE_0_1_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testReadShortLEWithNegativeBytes() throws IOException, BufferUnderrunException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { -1, -2 });
        assertEquals((short) 0xFEFF, EndianUtils.readShortLE(inputStream));
    }
```

### EndianUtils_readUE7_11_0_Test#testReadUE7_MultipleBytes

2 nouveaux mutants ; décision : `corrected`.

Flux 81 00 ; (1 << 7) + 0 = 128L.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_readUE7_11_0_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testReadUE7_MultipleBytes() throws IOException, TikaException {
        EndianUtils endianUtils = new EndianUtils();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x81, 0x00 });
        long result = endianUtils.readUE7(inputStream);
        assertEquals(128L, result);
    }
```

### EndianUtils_readUShortBE_3_1_Test#testReadUShortBE

4 nouveaux mutants ; décision : `accepted_unchanged`.

Flux 12 34 ; entier BE non signé 0x1234.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_readUShortBE_3_1_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testReadUShortBE() throws IOException, TikaException {
        // Arrange
        byte[] data = { (byte) 0x12, (byte) 0x34 };
        InputStream inputStream = new ByteArrayInputStream(data);
        EndianUtils endianUtils = new EndianUtils();
        // Act
        int result = endianUtils.readUShortBE(inputStream);
        // Assert
        assertEquals(0x1234, result);
    }
```

### EndianUtils_ubyteToInt_29_1_Test#testUbyteToIntMax

2 nouveaux mutants ; décision : `accepted_unchanged`.

Conversion du byte 127 → entier 127 ; le flux local créé est inutilisé.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/EndianUtils_ubyteToInt_29_1_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testUbyteToIntMax() throws IOException {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        int result = EndianUtils.ubyteToInt((byte) 127);
        assertEquals(127, result);
    }
```

### FilenameUtils_getSanitizedEmbeddedFileName_3_1_Test#testGetSanitizedEmbeddedFileName_withPathContainingColonAndColonAndColonAndBackslash

1 nouveaux mutants ; décision : `accepted_unchanged`.

RESOURCE_NAME_KEY seul = C:::/path/to/file.txt ; nom attendu file.txt.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/FilenameUtils_getSanitizedEmbeddedFileName_3_1_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testGetSanitizedEmbeddedFileName_withPathContainingColonAndColonAndColonAndBackslash() throws IOException {
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn("C:::/path/to/file.txt");
        String result = filenameUtils.getSanitizedEmbeddedFileName(metadata, "txt", 10);
        assertEquals("file.txt", result);
    }
```

### FilenameUtils_getSanitizedEmbeddedFilePath_4_0_Test#testGetSanitizedEmbeddedFilePath_withReservedCharacters

1 nouveaux mutants ; décision : `accepted_unchanged`.

EMBEDDED_RESOURCE_PATH = invalid:filename, autres propriétés null ; défaut test, maxLength 100 ; résultat non nul sans deux-points.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/FilenameUtils_getSanitizedEmbeddedFilePath_4_0_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testGetSanitizedEmbeddedFilePath_withReservedCharacters() throws IOException {
        when(metadata.get(TikaCoreProperties.EMBEDDED_RESOURCE_PATH)).thenReturn("invalid:filename");
        when(metadata.get(TikaCoreProperties.INTERNAL_PATH)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.EMBEDDED_RELATIONSHIP_ID)).thenReturn(null);
        when(metadata.get(TikaCoreProperties.ORIGINAL_RESOURCE_NAME)).thenReturn(null);
        String result = filenameUtils.getSanitizedEmbeddedFilePath(metadata, defaultExtension, maxLength);
        assertNotNull(result);
        assertFalse(result.contains(":"));
    }
```

### FilenameUtils_resolveWithin_5_0_Test#testResolveWithin

1 nouveaux mutants ; décision : `accepted_unchanged`.

Répertoire /tmp, enfant test.txt ; Path attendu /tmp/test.txt. Dépend de l’absence du fichier au moment de la mesure.

[Source](../../../../tika-core/src/test/java/org/apache/tika/io/FilenameUtils_resolveWithin_5_0_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testResolveWithin() throws IOException {
        Path dir = Paths.get("/tmp");
        Path resolved = filenameUtils.resolveWithin(dir, "test.txt");
        assertEquals(Paths.get("/tmp/test.txt"), resolved);
    }
```

### MediaType_equals_18_0_Test#testEqualsWithNull

1 nouveaux mutants ; décision : `accepted_unchanged`.

Fixture MediaType avec paramètre charset ; equals(null) doit être false.

[Source](../../../../tika-core/src/test/java/org/apache/tika/mime/MediaType_equals_18_0_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testEqualsWithNull() {
        assertFalse(mediaType.equals(null));
    }
```

### MediaType_image_2_0_Test#testImageMethod

2 nouveaux mutants ; décision : `accepted_unchanged`.

image/png sans puis avec charset=UTF-8 ; image("") doit renvoyer null.

[Source](../../../../tika-core/src/test/java/org/apache/tika/mime/MediaType_image_2_0_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testImageMethod() {
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
```

### MediaType_video_4_0_Test#testVideoMethod

1 nouveaux mutants ; décision : `accepted_unchanged`.

video(mp4) doit égaler new MediaType("video", "mp4", emptyMap).

[Source](../../../../tika-core/src/test/java/org/apache/tika/mime/MediaType_video_4_0_Test.java) ; fixture complète dans `ai-killing-sources/`.

```java
void testVideoMethod() {
        // Arrange
        String type = "mp4";
        MediaType expectedMediaType = new MediaType("video", type, Collections.emptyMap());
        // Act
        MediaType actualMediaType = MediaType.video(type);
        // Assert
        assertEquals(expectedMediaType, actualMediaType);
    }
```
