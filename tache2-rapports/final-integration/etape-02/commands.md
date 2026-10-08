# Étape 2 — compléments manuels FilenameUtils

Étape réalisée localement le 8 octobre 2026, sur `tache2-final`, à partir du
commit Saidana `619ff28473dbbc342c787bd9c0cb2a6e378818c9`.
Source Hamza : `d3b8e3f9264fb7b77d39f2405f57e9955de534a5`, fichier
`tika-core/src/test/java/org/apache/tika/io/FilenameUtilsManualTest.java`.
Le fichier Hamza intégral est conservé dans `FilenameUtilsManualTest-hamza-original.java.txt`.

## Sélection des cinq cas Hamza

| Cas Hamza | Décision | Justification |
|---|---|---|
| `embeddedFilenameUsesInternalPathWhenResourceNameIsMissing` | Non dupliqué | `embeddedName_eachPropertyAloneIsUsed` utilise déjà `INTERNAL_PATH` seul, avec un chemin dont le dernier segment constitue le nom attendu. |
| `embeddedPathUsesInternalPathWhenResourcePathIsMissing` | Non dupliqué | `embeddedPath_eachPropertyAloneIsUsed` vérifie déjà le chemin complet issu de `INTERNAL_PATH` seul. |
| `filenameAtMaximumLengthIsNotTruncated` | Non dupliqué | Le nom `abc.txt` a une partie sans extension de longueur 3, inférieure à `maxLength=7`. Saidana couvre déjà les noms courts et la vraie borne `namePart.length() == maxLength`, ainsi que le dépassement. |
| `calculateExtensionUsesBinForAnUnknownMimeType` | Non dupliqué | `calculateExtension_unknownOrInvalidMimeType_fallsBackToBin` vérifie déjà `.bin` pour un type inconnu avec un défaut distinct. |
| `resolveWithinRejectsExistingSymbolicLinkOutsideDirectory` | Importé | Les deux tests manuels Saidana de résolution acceptent des chemins existants ou absents, mais ne créent aucun lien symbolique extérieur. |

Le test importé conserve le nom, les données et l'assertion Hamza. Il est manuel,
pas généré par ChatUniTest. La création de deux répertoires frères rend la sortie
du répertoire observable après `toRealPath()`, malgré un chemin lexical intérieur.
L'assertion `assertThrows(IOException.class, ...)` provient du comportement de
protection documenté dans le code de `resolveWithin`. Aucune hypothèse
d'équivalence ni hausse de score PIT n'est validée par cette seule exécution.

## Copie de construction

Une copie temporaire de l'état courant des fichiers suivis a été préparée dans
`/private/tmp/tika-final-step2-wbf90_na`. Elle comprend les fichiers racine,
`.mvn`, `tika-parent`, `tika-annotation-processor`, `tika-core` et les POM de
découverte des autres modules. Les fichiers `._*` sont exclus.
Le manifeste avant construction est archivé ; la source effectivement testée,
après le formatage Spotless, est archivée avec son empreinte dans `results.json`.
Seule cette classe de test formatée est reprise dans le dépôt original.

## Commandes et résultats

Dans la copie, avec Java OpenJDK Homebrew 17.0.18 et le wrapper Maven 3.9.12 :

```bash
./mvnw -B -pl tika-core -am clean install \
  -Dtest=FilenameUtilsManualTest -Dsurefire.failIfNoSpecifiedTests=false
```

Le paramètre Surefire permet aux modules parents de ne pas avoir de cas portant
ce nom ; il ne constitue pas une preuve d'exécution de leurs tests. La commande
échoue dans `tika-core` avant Surefire : **202 erreurs Checkstyle**. Aucun
contrôle n'a été désactivé. L'échec est conservé dans `build-with-checks.log`
et `checkstyle-result.xml`. Dans cette copie formatée, `FilenameUtilsManualTest`
ne contient aucune erreur Checkstyle ; les erreurs sont dans les autres tests.

La compilation et l'exécution ciblées suivantes réussissent :

```bash
./mvnw -B -pl tika-core resources:resources compiler:compile \
  resources:testResources compiler:testCompile surefire:test \
  -Dtest=FilenameUtilsManualTest
```

**16 cas, 0 échec, 0 erreur, 0 désactivé.** Le XML Surefire inclut le test
`resolveWithinRejectsExistingSymbolicLinkOutsideDirectory(Path)`.
Cette commande appelle des objectifs directs et ne parcourt pas les contrôles
du cycle complet : son `BUILD SUCCESS` confirme cette exécution ciblée seulement.
Le journal, le XML et la source testée sont archivés ici.

Les corrections des candidats IA, l'harmonisation Maven, les mesures A/B/C,
la CI et la consolidation finale restent des étapes distinctes.
