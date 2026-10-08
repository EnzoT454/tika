# Étape 6 — oracles, mutants et révision des équivalences

Analyse locale du 8 octobre 2026, sur `tache2-final`. Les sources de production,
les tests acceptés et les mesures A/B/C de l'étape 5 sont conservés.

## Détections IA

```bash
python3 tache2-rapports/final-integration/etape-06/link-ai-mutants.py
```

Le script relie **chacun des 132 nouveaux KILLED de A vers B** à l'identifiant
complet du mutant (classe, méthode, descripteur, index, opérateur), au test tueur
PIT, à l'entrée, aux assertions exactes et à la décision de correction humaine.
Les empreintes des sources sont vérifiées contre le manifeste B. Les **32 tests
tueurs** et leurs fixtures sont archivés avec leurs sources complètes.

- `ai-detections.md` : 132 lignes individuelles puis les 32 corps de tests ;
- `ai-detections.json` : détail structuré de chaque mutant et mécanisme ;
- `ai-test-oracles.json` : données, corps, assertions, fixtures et provenance ;
- `ai-killing-sources/` : sources exactes des classes concernées.

PIT fournit un test tueur, pas la ligne exacte de l'assertion défaillante. Les
mécanismes sont explicitement des analyses des sources. Certains mutants sont
détectés par une exception inattendue avant l'assertion. Exemple corrigé :
`MediaType.parse` L277 porte sur `CHARSET_FIRST_PATTERN`, pas `TYPE_PATTERN` ;
avec `image("")`, une condition inversée entraîne un `group()` sans match.

## Mutants encore non KILLED

`remaining-mutants.md` et son JSON décrivent individuellement les **7 SURVIVED,
2 NO_COVERAGE et 1 TIMED_OUT** de C. Conclusions dans le code/dépendances figés,
sur des entrées normales, sans modification réflexive des champs :

- 2 incréments finaux EndianUtils : valeur de l'indice local jamais relue ;
- 2 bornes `prefixLength > 0` : `substring(0)` préserve le contenu ;
- 2 gardes de nom blanc : un nom non blanc est déjà imposé et les remplacements
  ne peuvent pas éliminer son dernier caractère non blanc ; statuts NO_COVERAGE
  conservés, sans les convertir en KILLED ;
- 3 survivants FilenameUtils avec contre-exemples observés ;
- 1 boucle `parseParameters` devenant infinie : timeout, pas équivalence.

## Sondes isolées des trois contre-exemples

```bash
python3 tache2-rapports/final-integration/etape-06/probe-equivalence.py
```

Le script compile sur `/private/tmp` trois variantes qui changent uniquement
les opérateurs concernés, puis les compare au code original. Aucune variante
n'est copiée dans `src/main/java` ou la suite de tests.

| Mutation | Entrée de RESOURCE_NAME_KEY, défaut `.bin`, maxLength 100 | Original | Variante |
|---|---|---|---|
| Borne basse `>= 'A'` → `> 'A'`, L323 index25 | `A:` ; méthode chemin | null | `A.bin` |
| Borne haute `<= 'Z'` → `< 'Z'`, L323 index30 | `Z:` ; méthode chemin | null | `Z.bin` |
| Négation `prefixLength > 0`, L156 index52 | `~` ; méthode nom | StringIndexOutOfBoundsException | `~.bin` |

Commons IO 2.22.0 renvoie 0 pour `A:`/`Z:` sur macOS, donc le repli Tika est
exécuté. Sous un système prenant en charge les lettres de lecteur, Commons IO
peut déjà renvoyer 2 : l'ancien constat Windows ne prouve pas une équivalence
sur toutes les plateformes. Le bytecode de la dépendance est archivé.

Le cas `~alice` confirme aussi la différence liée au préfixe home. Il expose
un bogue préexistant ; un test qui attend son exception serait une
caractérisation de ce bogue, pas une justification du comportement souhaitable.
Ces candidats n'ont pas été intégrés : aucun nouveau score n'est attribué.
`probe-results.json` conserve les commandes javac/java, résultats et empreinte.

## Autres déclarations historiques retirées

La négation de `Files.exists(normalizedDir)` L305 index43 est KILLED en C par
`resolveWithinRejectsExistingSymbolicLinkOutsideDirectory`. Le lien lexical
intérieur mène à un répertoire réel extérieur ; la mutation saute la protection
et viole `assertThrows(IOException.class, ...)`. Ce cas est détectable et exécuté.

La borne `prefixLength > 0` → `>= 0` de **getPrefixLength L320** est différente
des bornes L156/L215 : elle peut empêcher le repli pour une lettre de lecteur
sur macOS. Elle est déjà KILLED en A/B/C par `FilenameUtilsTest.testEmbeddedFilePaths`.
Le retour 2 remplacé par 0, L324, est lui aussi KILLED par ce test original.
Ils ne sont donc pas du code mort général.

## Divergence diagnostique MediaType.parse L257

Le mutant `19cfe3681688…`, index44, NULL_RETURNS, est **KILLED** dans les
mesures complètes A/B/C par `TypeDetectorTest.testDetect`. L'ancienne déclaration
globale « non tuable » n'est pas retenue pour ces mesures. Son export isolé
avec le moteur Gregor 1.30.0, à un puis onze opérateurs, montre néanmoins
`null` remplacé par `null`, et le test original passe dans cette sonde.

Pour vérifier l'écart, les commandes suivantes sont exécutées dans la copie C,
sans changer son code ni écraser les archives de l'étape 5 :

```bash
./mvnw -B -pl tika-core -Pmutation org.pitest:pitest-maven:mutationCoverage \
  -DtargetClasses=org.apache.tika.mime.MediaType \
  -DtargetTests=org.apache.tika.detect.TypeDetectorTest -Dmutators=NULL_RETURNS \
  -DreportsDirectory=/private/tmp/tika-step6-pit-diagnostic -Dverbose=true -Dexport=true

./mvnw -B -pl tika-core -Pmutation org.pitest:pitest-maven:mutationCoverage \
  -DreportsDirectory=/private/tmp/tika-step6-pit-C-repeat -Dverbose=true
```

Les deux commandes réussissent. Le diagnostic ciblé (11 mutants) classe ce
mutant **SURVIVED**. La répétition complète de C retrouve **exactement les mêmes
406 statuts**, dont ce mutant KILLED et le total 396/406. Cette divergence de
contexte est documentée : un effet d'ordre ou d'état partagé est une hypothèse,
pas une cause prouvée. Les compteurs bruts ne sont pas corrigés à la main et
ce mutant n'est pas présenté comme une détection supplémentaire apportée par l'IA.

Le paramètre `export` de la commande ciblée n'a pas produit d'archive du mutant ;
l'export effectif utilise la petite classe `ExportMutation` et le moteur Gregor.
Sources des sondes, commandes, bytecodes original/muté et résultats sont dans
`export-mutation.json` et les fichiers `.java.txt` associés. Les rapports et
journaux PIT diagnostiques sont conservés dans `pit-diagnostic/`,
`pit-C-repeat/`, `pit-diagnostic.log` et `pit-C-repeat.log`.

`validation.json` atteste la stabilité de la répétition C et conserve l'écart.
La suite finale reste à 161 cas IA retenus et 56 manuels ; la prochaine étape
est la validation CI de cette version intégrée, sans publication automatique.
