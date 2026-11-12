# IFT3913 — Tâche 2 : améliorer les tests d’Apache Tika

| Membre du binôme | Identifiant GitHub |
|---|---|
| Aqel Hamza | EnzoT454 |
| Saidana Mohamed | FakeTMG |

Nous avons travaillé sur trois classes de `tika-core` : `EndianUtils`, `MediaType` et `FilenameUtils`. L’objectif était de compléter leurs tests avec ChatUniTest et un modèle exécuté localement, puis de vérifier avec PIT si ces nouveaux tests détectaient davantage de défauts.

La version finale de [notre fork Tika](https://github.com/EnzoT454/tika) réunit les tests de Mohamed, un complément de Hamza et un protocole commun de mesure. **Le score passe de 185 à 396 mutants tués sur 406, soit de 45,57 % à 97,54 %.** La suite finale comporte 161 tests issus de ChatUniTest et 56 tests manuels. Tous ces nouveaux tests réussissent et aucun n’est désactivé. Ces chiffres proviennent de nouvelles exécutions comparables, réalisées le 8 octobre 2026 sur `tache2-final`.

Ce rapport décrit la version intégrée. Les anciennes expériences restent archivées, mais leurs scores ne sont pas utilisés comme résultats de cette version. La présentation et l’attribution originales d’Apache Tika sont conservées dans [README-tika-original.md](README-tika-original.md), avec les fichiers `LICENSE` et `NOTICE` du projet.

## 1. Organisation du travail

Nous avons d’abord travaillé sur deux branches, puis regroupé les éléments utiles dans `tache2-final`, créée à partir de `tache2-saidana`. Le tableau décrit la provenance des contributions ; la phase d’intégration a aussi bénéficié de l’assistance de Codex, détaillée à la fin du rapport.

| Contribution | Travail réalisé |
|---|---|
| Mohamed — `tache2-saidana` | Étude des trois classes retenues, génération avec ChatUniTest et Qwen 7B, archivage des tentatives et premières corrections de compilation, 55 tests manuels et première version du rapport. |
| Hamza — `tache2-hamza` | Expériences sur `FilenameUtils` et `LookaheadInputStream`, protocole de reproduction, vérification des tests en CI et test de rejet d’un lien symbolique qui sort du répertoire autorisé. |
| Intégration sur `tache2-final` | Sélection des tests complémentaires, tri des candidats IA et corrections d’oracles, harmonisation Maven/PIT, nouvelles mesures A/B/C, revue des mutants, adaptation du vérificateur CI et consolidation du rapport. |

Nous avons gardé les trois classes de Mohamed pour respecter la limite de trois classes. `LookaheadInputStream` reste donc dans les expériences historiques de Hamza. Parmi ses tests de `FilenameUtils`, le cas du lien symbolique apportait un comportement absent ; les autres cas examinés recoupaient les tests déjà présents et n’ont pas été ajoutés.

## 2. Pourquoi ces classes ?

Tika identifie les formats de documents et extrait leur texte et leurs métadonnées. Les classes choisies sont des utilitaires communs du module autorisé `tika-core`. Elles avaient déjà des tests, mais la suite originale laissait des méthodes, branches et mutants non détectés.

| Classe | Fonction et lacunes constatées | Lignes A | Branches A | Mutation A |
|---|---|---:|---:|---:|
| `EndianUtils` | Lire des nombres depuis des octets ou des flux, dans plusieurs ordres d’octets. Beaucoup de lectures et surcharges n’étaient pas exercées. | 31/121 | 10/28 | 38/206 — 18,45 % |
| `MediaType` | Construire, analyser et comparer les types MIME. Lacunes dans les paramètres, fabriques et règles de mise en cache. | 125/155 | 67/90 | 68/85 — 80,00 % |
| `FilenameUtils` | Assainir les noms et chemins, choisir une extension et résoudre un fichier dans un répertoire. Lacunes dans les métadonnées de repli, les limites de longueur et les chemins réels. | 154/175 | 86/110 | 79/115 — 68,70 % |

La couverture JaCoCo indique ce qui est exécuté. PIT vérifie si les tests remarquent une modification du comportement. Nous avons utilisé les deux : une ligne couverte peut encore contenir un mutant survivant. Les compteurs ci-dessus viennent de la [mesure A finale](tache2-rapports/final-integration/etape-05/A/results.json), avec toute la suite originale du module.

## 3. Environnement et génération locale

La génération principale de Mohamed a été réalisée sous Windows 11, avec 16 Go de RAM et une RTX 2050 de 4 Go. Le modèle était **Qwen2.5-Coder 7B Instruct, quantifié Q4_K_M**, exécuté avec Ollama. L’alias `codeqwen:v1.5-chat` permettait de le sélectionner dans la liste fermée du plugin ; il ne désigne pas un second modèle. Les pilotes de Hamza utilisaient un Qwen 3B sur une machine de 8 Go, mais les tests IA de la suite finale proviennent du travail 7B de Mohamed.

| Élément | Configuration finale des mesures |
|---|---|
| Java / Maven | OpenJDK 17.0.18 / wrapper Maven 3.9.12 |
| Tests | JUnit 6.1.3, Mockito 5.23.0 |
| Couverture / mutation | JaCoCo 0.8.15, PIT 1.30.0, connecteur JUnit 1.2.3 |
| Génération | ChatUniTest 2.1.1 dans le profil Maven `chatunitest` |
| API du modèle | Ollama local, `http://127.0.0.1:11434/v1/chat/completions` |

La configuration se trouve dans [tika-core/pom.xml](tika-core/pom.xml). ChatUniTest est isolé dans son profil et PIT dans `mutation`. Le paramètre `ollama-local` sert de valeur technique pour l’API locale, sans clé distante. La génération est séquentielle (`enableMultithreading=false`), sans fusion automatique, avec validation d’exécution activée, deux tentatives et au plus trois tours de réparation par tentative. Les budgets sont de 5 500 jetons de prompt et 1 024 de réponse ; une reprise de `FilenameUtils` utilise un contexte de 16 384 avec des budgets de 10 000/2 048.

Les méthodes simples, privées et certains constructeurs étaient exclus par le filtrage du plugin. Les premières campagnes ont exporté des tests pour 20 méthodes d’`EndianUtils`, 10 de `MediaType` et 5 de `FilenameUtils`. Il s’agit de méthodes de production ciblées, pas de nombres de cas JUnit. Les reprises et les corrections ont ensuite fourni les candidats intégrés. Les tableaux d’appels, prompts, réponses et erreurs sont conservés dans [les archives de génération](tache2-rapports/03-chatunitest/).

Les échecs ont été instructifs : le modèle omettait l’import de l’exception imbriquée `BufferUnderrunException`, inventait des signatures comme `image(String, Map)` et des métadonnées inexistantes. Un contexte plus grand n’a pas garanti le succès : une première tentative a encore échoué avant la reprise retenue. Un `BUILD SUCCESS` du plugin ne suffit donc pas ; nous avons vérifié les fichiers exportés et leur exécution effective par Surefire.

## 4. Ce que nous avons accepté, corrigé ou rejeté

Un oracle est la règle qui dit si le résultat du test est correct. Nous avons confronté les assertions générées au contrat des méthodes, aux tests originaux et, lorsque nécessaire, au comportement documenté par le code. Les tests originaux donnaient une référence, mais ne couvraient pas toujours les cas limites proposés par le modèle.

Au début de l’intégration, nous avions **193 candidats JUnit dans 49 classes**. Le tri a donné :

| Classe | Conservés sans modification d’assertion à cette étape | Corrigés à cette étape | Rejetés | Retenus |
|---|---:|---:|---:|---:|
| `EndianUtils` | 59 | 17 | 21 | 76 |
| `MediaType` | 37 | 2 | 5 | 39 |
| `FilenameUtils` | 38 | 8 | 6 | 46 |
| **Total** | **134** | **27** | **32** | **161** |

« Conservés sans modification » signifie sans changement d’assertion **pendant ce tri**. Certains fichiers avaient déjà reçu des corrections de compilation dans le travail de Saidana : les 134 cas ne sont donc pas présentés comme 134 réussites brutes du modèle. Les corrections hors ChatUniTest, y compris celles assistées par Codex, sont distinguées des réparations automatiques du plugin.

| Exemple | Problème de l’oracle ou du candidat | Décision et justification |
|---|---|---|
| `getUIntBE`, octets `00 FF FF FF` | Valeur numérique attendue incorrecte. | Attendre `0x00FFFFFFL` : assemblage big endian de quatre octets, résultat non signé. |
| `getLongLE`, octets `12 34 56 78 9A BC DE F0` | Mauvaise constante attendue. | Attendre `0xF0DEBC9A78563412L`, calculée selon l’ordre little endian. |
| `calculateExtension` | Attentes incompatibles avec le registre MIME et appels d’API inventés. | Utiliser les API existantes et distinguer extension enregistrée, type inconnu et valeur par défaut. |
| Chemins et messages d’exception | Résultat dépendant du système ou attente incorrecte. | Construire les chemins avec `Paths` et justifier l’exception exacte, sans assouplir l’assertion pour la rendre verte. |
| Valeur `null` pour un paramètre `byte` primitif | Cas impossible à appeler en Java. | Rejeter le candidat ; aucune exception applicative ne peut être testée avec cette entrée. |
| Réflexion, mocks sans rapport avec le comportement, doublons ou méthode vide | Test fragile, redondant ou sans oracle utile. | Conserver la sortie dans les preuves, l’exclure de la suite acceptée. |

Les sorties brutes n’ont pas été écrasées. Le [registre de décisions](tache2-rapports/final-integration/etape-03/decisions.md) et sa version JSON donnent, méthode par méthode, l’état avant/après et la raison du choix. Les sources avant tri et les 40 classes acceptées sont archivées séparément. Les 32 candidats rejetés restent analysables ; ils ne sont pas simplement désactivés dans la suite finale.

## 5. Trois mesures réellement comparables

Nous avons refait trois constructions propres dans des copies indépendantes. Le code de production, les POM, les ressources et les **749 cas originaux** sont identiques. Nous avons conservé toute la suite originale de `tika-core`, plutôt que seulement les trois classes de tests directs : d’autres tests du module peuvent aussi détecter leurs mutants.

| État | Composition | Cas comptabilisés | Réussis | Déjà désactivés | Échecs / erreurs |
|---|---|---:|---:|---:|---:|
| A | Tests originaux | 749 | 747 | 2 | 0 / 0 |
| B | A + 161 tests issus de ChatUniTest acceptés | 910 | 908 | 2 | 0 / 0 |
| C | B + 56 tests manuels | 966 | 964 | 2 | 0 / 0 |

Les deux désactivations appartiennent à la suite originale (`TikaInputStreamTest.reproduceRandomizedTestFailure` et `CustomErrorHandlerTest.testUndeclaredEntityXML`). Aucun nouveau test n’est ignoré. Les huit tests du processeur d’annotations réussissent aussi dans chaque état. Les constructions complètes ont réussi avec les contrôles applicables du projet, dont Checkstyle et Spotless ; Checkstyle rapporte zéro erreur.

PIT cible exactement les trois classes avec `targetTests=org.apache.tika.*`. Nous avons figé deux threads, `timeoutFactor=1.25`, `timeoutConstant=4000 ms`, sans historique, et les onze opérateurs suivants : `CONDITIONALS_BOUNDARY`, `INCREMENTS`, `INVERT_NEGS`, `MATH`, `NEGATE_CONDITIONALS`, `VOID_METHOD_CALLS`, `EMPTY_RETURNS`, `FALSE_RETURNS`, `TRUE_RETURNS`, `NULL_RETURNS`, `PRIMITIVE_RETURNS`.

Les manifestes et empreintes confirment le même bytecode cible et les **mêmes 406 identités de mutants** dans A, B et C. Chaque état conserve ses sources exactes, journaux, XML Surefire, JaCoCo et PIT. Le [protocole de reproduction](tache2-rapports/final-integration/etape-05/commands.md) explique cette vérification.

### Résultats de mutation

Nous calculons le score avec **`100 × KILLED / total`**, puis le score global avec les sommes des effectifs. Les timeouts sont présentés séparément : le compteur agrégé de PIT peut les inclure parmi les mutants détectés, ce qui explique un chiffre différent dans son affichage.

| Classe | Mutants | A : KILLED / score | B : KILLED / score | C : KILLED / score |
|---|---:|---:|---:|---:|
| `EndianUtils` | 206 | 38 — 18,45 % | 163 — 79,13 % | 204 — 99,03 % |
| `FilenameUtils` | 115 | 79 — 68,70 % | 82 — 71,30 % | 108 — 93,91 % |
| `MediaType` | 85 | 68 — 80,00 % | 72 — 84,71 % | 84 — 98,82 % |
| **Total** | **406** | **185 — 45,57 %** | **317 — 78,08 %** | **396 — 97,54 %** |

| Statut brut | A | B | C |
|---|---:|---:|---:|
| `KILLED` | 185 | 317 | 396 |
| `SURVIVED` | 36 | 56 | 7 |
| `NO_COVERAGE` | 184 | 32 | 2 |
| `TIMED_OUT` | 1 | 1 | 1 |

B tue **132 mutants supplémentaires** et C en tue **79 de plus**, sans perte de mutants tués entre états. La hausse des survivants de A à B ne traduit pas une régression : certains mutants non couverts deviennent exécutés sans être encore détectés. Les transitions exactes sont dans [comparison.json](tache2-rapports/final-integration/etape-05/comparison.json).

### Couverture finale

| Classe | Lignes C | Branches C |
|---|---:|---:|
| `EndianUtils` | 121/121 — 100 % | 28/28 — 100 % |
| `MediaType` | 155/155 — 100 % | 84/90 — 93,33 % |
| `FilenameUtils` | 173/175 — 98,86 % | 106/110 — 96,36 % |

La couverture ne suffit toujours pas à expliquer tous les survivants. Par exemple, les deux incréments finaux d’`EndianUtils` sont exécutés, mais leur valeur n’est plus utilisée ensuite.

## 6. Apport et limites des tests générés

Les 132 nouvelles détections de B se répartissent en **125 pour `EndianUtils`, 3 pour `FilenameUtils` et 4 pour `MediaType`**. PIT les attribue à 32 cas JUnit. Parmi eux, 23 cas conservés sans changement d’assertion pendant le tri tuent 73 mutants ; neuf cas corrigés en tuent 59. Le gain de B est donc celui des tests issus de ChatUniTest **après sélection et corrections**, pas celui du modèle seul.

Pour les lectures d’octets, les valeurs distinctes et les offsets rendent visibles les changements de décalage, d’addition, de masque et d’indice. Pour les métadonnées, une seule propriété renseignée force un chemin de repli qui pouvait être masqué par les tests originaux. Les assertions d’égalité et d’exception deviennent ainsi plus discriminantes.

Deux limites restent visibles. Le candidat `resolveWithin` utilisant `/tmp/test.txt` dépend de l’état du disque et peut tuer un mutant par une exception inattendue ; les tests manuels utilisent ensuite `@TempDir` pour contrôler ce contexte. Dans `MediaType_image_2_0_Test`, `image("")` tue un mutant de `CHARSET_FIRST_PATTERN` par une exception avant l’assertion attendue. Il faut distinguer ce mécanisme d’une assertion ayant directement échoué.

L’annexe A donne les entrées, oracles et nombres de nouvelles détections pour les 32 cas. Le [relevé détaillé des 132 mutants](tache2-rapports/final-integration/etape-06/ai-detections.md) et son JSON relient chaque identité, opérateur, ligne, test et assertion. PIT fournit le test tueur ; le mécanisme au niveau de l’assertion est une analyse du code, pas une trace d’échec fournie pour chaque mutant.

## 7. Tests manuels : compléter les lacunes restantes

Nous avons retenu **28 cas pour `EndianUtils`, 12 pour `MediaType` et 16 pour `FilenameUtils`**. Ils ciblent surtout les limites que les candidats générés n’avaient pas isolées : octets tous nuls, fin de flux à une position particulière, constructeurs et ensembles MIME, priorités entre métadonnées, longueurs exactes et vrais fichiers sur disque.

Le complément de Hamza crée un répertoire autorisé et un répertoire extérieur, puis un lien symbolique du premier vers le second. `assertThrows(IOException.class, ...)` vérifie que la résolution refuse de sortir du répertoire par ce lien. Il tue le mutant supprimant une partie de la vérification des chemins réels, auparavant présenté à tort comme équivalent.

Ces 56 cas expliquent les 79 nouvelles détections de C. L’annexe B précise leurs intentions, données et oracles ; les sources sont [EndianUtilsManualTest.java](tika-core/src/test/java/org/apache/tika/io/EndianUtilsManualTest.java), [MediaTypeManualTest.java](tika-core/src/test/java/org/apache/tika/mime/MediaTypeManualTest.java) et [FilenameUtilsManualTest.java](tika-core/src/test/java/org/apache/tika/io/FilenameUtilsManualTest.java).

## 8. Mutants restants et limites de l’analyse

Le score final laisse dix mutants hors `KILLED`. Nous avons revu les anciennes déclarations d’équivalence au lieu de considérer automatiquement chaque survivant comme impossible à tuer.

| Mutants restants | Statut | Analyse |
|---|---|---|
| `EndianUtils.getIntLE` L362 et `getIntBE` L388, incrément final | 2 `SURVIVED` | Équivalence justifiée : l’indice local est modifié après le dernier accès et n’est plus lu. |
| `FilenameUtils` L156/L215, `> 0` remplacé par `>= 0` | 2 `SURVIVED` | Le seul cas ajouté exécute `substring(0)`, qui conserve le contenu. |
| `FilenameUtils` L185/L259, garde sur un nom blanc | 2 `NO_COVERAGE` | Gardes inatteignables dans le domaine d’entrées du code figé : un nom blanc a déjà été rejeté et les transformations intermédiaires ne suppriment pas son dernier caractère non blanc. Statuts bruts conservés. |
| `FilenameUtils.getPrefixLength` L323, bornes `A` et `Z` | 2 `SURVIVED` | Détectables sur macOS avec `A:` et `Z:` : original → `null`, variante → `A.bin` ou `Z.bin`. Le résultat de Commons IO dépend du système ; ce ne sont pas des équivalences universelles. |
| `FilenameUtils` L156, négation du retrait de préfixe | 1 `SURVIVED` | Détectable avec `~` ou `~alice` : exception dans l’original, nom dans la variante. Cela caractérise un comportement problématique préexistant ; nous n’avons pas modifié la production ni ajouté ce cas dans C. |
| `MediaType.parseParameters` L305, `> 0` remplacé par `>= 0` | 1 `TIMED_OUT` | Boucle infinie quand la chaîne restante est vide. Détection par délai PIT, distincte d’un test qui termine en échec. |

Les sondes et justifications sont archivées dans [remaining-mutants.md](tache2-rapports/final-integration/etape-06/remaining-mutants.md). Les variantes expérimentales étaient isolées ; elles n’ont pas changé le code de production de A/B/C. Les anciennes équivalences concernant le lien symbolique et certains replis de préfixe ont été retirées lorsqu’un test ou un contre-exemple les contredisait.

**Une divergence reste non résolue.** `MediaType.parse`, L257/index 44, est `KILLED` dans les trois suites complètes et dans une répétition de C, mais `SURVIVED` dans une exécution ciblée. L’export isolé montre un remplacement `null` par `null` et le test isolé réussit. L’ordre des tests ou un état partagé constituent des hypothèses, sans cause démontrée. Nous conservons les statuts mesurés et signalons cette limite ; ce mutant n’est pas compté parmi les 132 gains attribués aux tests IA.

## 9. Validation et GitHub Actions

Le [workflow final](.github/workflows/tache2-ift3913.yml) cible `main` et `tache2-final`. Il construit les modules nécessaires avec Java 17, exécute les tests et les contrôles, vérifie l’inventaire des nouveaux cas, lance PIT puis conserve les rapports comme artefacts.

Le [vérificateur des tests](.github/scripts/check-tache2-tests.py), adapté du travail de Hamza, exige les **217 nouveaux cas attendus dans 43 classes**, avec leurs noms exacts. Il refuse les rapports manquants, doublons, cas renommés, tests ignorés, échecs et compteurs incohérents. Sept anomalies simulées ont toutes été refusées. Le [résumé PIT](.github/scripts/summarize-tache2-pit.py) lit les XML et sépare les statuts.

Les quatre commandes du workflow ont été exécutées localement avec succès : 964 tests core réussis, deux désactivations originales, huit tests du processeur réussis, zéro erreur Checkstyle et 396/406 mutants tués. Les preuves sont dans [etape-07](tache2-rapports/final-integration/etape-07/). Aucun nouveau test n’est masqué par `continue-on-error`, `-Pfast` ou `-DskipTests`.

**Statut distant : l’exécution GitHub Actions de `tache2-final` reste à obtenir et son lien reste à ajouter.** Les validations distantes des branches précédentes, dont [le run Saidana](https://github.com/EnzoT454/tika/actions/runs/37239989235), concernent leurs propres versions. Elles ne prouvent pas l’exécution de cette intégration. La publication et les liens de remise seront ajoutés après autorisation.

## 10. Reproduire et consulter les preuves

Dans une copie sur disque interne, depuis la racine et avec Java 17 :

```bash
./mvnw -B -ntp -pl tika-core -am clean install
python3 .github/scripts/check-tache2-tests.py
./mvnw -B -ntp -pl tika-core -Pmutation org.pitest:pitest-maven:mutationCoverage
python3 .github/scripts/summarize-tache2-pit.py
```

Sur le disque externe macOS, les fichiers AppleDouble `._*` peuvent perturber les outils malgré `.gitignore`. Le [préparateur de copie](tache2-rapports/final-integration/etape-04/prepare-local-build.py) les exclut sans désactiver les contrôles ; il faut ajouter les quatre fichiers CI indiqués dans le compte rendu de l’étape 7 pour exécuter les vérificateurs depuis cette copie.

Pour refaire A/B/C, utiliser les archives exactes ou [prepare-states.py](tache2-rapports/final-integration/etape-05/prepare-states.py), puis suivre le protocole de l’étape 5. Conserver les nouveaux résultats à part pour ne pas remplacer les mesures présentées ici. Le commit de base seul ne contient pas toutes les modifications locales : les archives et manifestes sont nécessaires à la reproduction.

| Preuves | Contenu |
|---|---|
| [Génération originale](tache2-rapports/03-chatunitest/) | Prompts, réponses, reprises et erreurs du modèle. |
| [Tri des candidats](tache2-rapports/final-integration/etape-03/) | 193 décisions, sources avant/après et corrections. |
| [Configuration](tache2-rapports/final-integration/etape-04/) | POM effectif, dépendances, formatage et construction. |
| [A](tache2-rapports/final-integration/etape-05/A/), [B](tache2-rapports/final-integration/etape-05/B/), [C](tache2-rapports/final-integration/etape-05/C/) | Sources exactes, journaux, tests, JaCoCo, PIT et identités des mutants. |
| [Analyse des mutants](tache2-rapports/final-integration/etape-06/) | Entrées, oracles, nouveaux mutants tués, survivants et sondes. |
| [Validation CI locale](tache2-rapports/final-integration/etape-07/) | Commandes du workflow, vérifications positives/négatives et rapports. |

## 11. Déclaration d’usage de l’IA

ChatUniTest et Qwen ont produit les candidats de tests via Ollama local. Leurs réparations automatiques, les corrections effectuées ensuite et les tests ajoutés hors génération sont distingués dans les archives. Le modèle n’a pas produit seul la suite finale acceptée.

Codex (OpenAI) a aussi été utilisé pour assister l’intégration : comparaison des branches, sélection et correction des tests, configuration Maven/PIT, scripts de reproduction et de vérification, analyse des mutants et sondes, puis rédaction de ce rapport.

## Annexe A — Oracles des cas IA apportant de nouvelles détections

Les noms ci-dessous identifient `classe#méthode`. La colonne « gains » compte les mutants nouvellement tués de A à B, attribués à ce cas par PIT. « Corrigé » concerne le tri de l’intégration, et non toute l’histoire du fichier. Les sources et assertions exactes figurent aussi dans [ai-test-oracles.json](tache2-rapports/final-integration/etape-06/ai-test-oracles.json).

| Cas JUnit | Entrée et oracle | Gains | Tri |
|---|---|---:|---|
| `EndianUtils_getIntBE_22_0_Test#testGetIntBEWithOffset` | Tableau 01…08, offset 4 ; valeur BE 0x05060708. | 1 | Conservé |
| `EndianUtils_getIntLE_20_0_Test#testGetIntLE` | Octets 01 02 03 04 ; valeur LE 0x04030201 (appel direct ou réflexion). | 1 | Conservé |
| `EndianUtils_getIntLE_21_0_Test#testGetIntLE` | Octets 01 02 03 04 ; valeur LE 0x04030201 (appel direct ou réflexion). | 14 | Conservé |
| `EndianUtils_getLongLE_28_0_Test#testGetLongLE` | Octets 12 34 56 78 9A BC DE F0, offset 0 ; valeur LE 0xF0DEBC9A78563412L. | 8 | Conservé |
| `EndianUtils_getShortLE_13_0_Test#testGetShortLE` | Octets 01 02, offset 0 ; short LE 0x0201. | 1 | Conservé |
| `EndianUtils_getUByte_30_0_Test#testGetUByte` | Premier octet 01, offset 0 ; valeur non signée 1. | 2 | Conservé |
| `EndianUtils_getUIntBE_27_0_Test#testGetUIntBEWithLargeNumber` | Octets 00 FF FF FF ; entier non signé BE 0x00FFFFFFL. | 14 | Corrigé |
| `EndianUtils_getUIntBE_27_0_Test#testGetUIntBEWithNegativeNumber` | Octets FF FF FF FF ; entier non signé 4294967295L. | 1 | Conservé |
| `EndianUtils_getUIntLE_24_0_Test#testGetUIntLEWithOffset` | Tableau 00 00 00 00 00 00 01 00, offset 4 ; valeur LE 0x00010000L. | 2 | Corrigé |
| `EndianUtils_getUShortBE_18_0_Test#testGetUShortBE` | Octets 01 02 ; entier BE non signé 258. | 7 | Conservé |
| `EndianUtils_getUShortLE_14_0_Test#testGetUShortLE` | Octets 00 01 ; entier LE non signé 256. | 1 | Corrigé |
| `EndianUtils_getUShortLE_15_0_Test#testGetUShortLEWithOffset` | Tableau 01…08, offset 2 ; entier LE 0x0403. | 6 | Conservé |
| `EndianUtils_readIntBE_7_1_Test#testReadIntBEWithNegativeInput` | Flux FF 00 00 00 ; int signé BE 0xFF000000. | 3 | Corrigé |
| `EndianUtils_readIntBE_7_1_Test#testReadIntBEWithPartialInput` | Mockito renvoie 01, 02, -1, 0 ; BufferUnderrunException obligatoire. | 2 | Conservé |
| `EndianUtils_readIntBE_7_1_Test#testReadIntBEWithValidInput` | Flux 01 02 03 04 ; int BE 0x01020304. | 5 | Conservé |
| `EndianUtils_readIntLE_6_1_Test#testReadIntLEValidInput` | Flux 12 34 56 78 ; int LE 0x78563412. | 8 | Conservé |
| `EndianUtils_readLongBE_10_1_Test#testReadLongBE` | Flux 01…08 → 0x0102030405060708L ; FF FE FD FC FB FA F9 F8 → 0xFFFEFDFCFBFAF9F8L ; quatre octets → underrun. | 16 | Corrigé |
| `EndianUtils_readLongLE_9_1_Test#testReadLongLE` | Mockito renvoie 12 34 56 78 9A BC DE F0 ; 0xF0DEBC9A78563412L et huit lectures. | 15 | Corrigé |
| `EndianUtils_readLongLE_9_1_Test#testReadLongLE_WithBufferUnderrunException` | Mockito renvoie sept octets valides puis -1 ; underrun et huit lectures. | 2 | Conservé |
| `EndianUtils_readShortBE_1_1_Test#testReadShortBE` | Mockito renvoie 12 34 puis -1 ; short BE 0x1234 sur les deux premières lectures. | 1 | Corrigé |
| `EndianUtils_readShortBE_1_1_Test#testReadUShortBEWithEndOfStream` | Mockito renvoie 12 puis -1 ; underrun attendu. | 1 | Conservé |
| `EndianUtils_readShortLE_0_1_Test#testReadShortLEWithBufferUnderrun` | Flux contenant seulement 12 ; underrun attendu. | 1 | Conservé |
| `EndianUtils_readShortLE_0_1_Test#testReadShortLEWithNegativeBytes` | Flux FF FE ; short LE signé 0xFEFF. | 5 | Corrigé |
| `EndianUtils_readUE7_11_0_Test#testReadUE7_MultipleBytes` | Flux 81 00 ; (1 << 7) + 0 = 128L. | 2 | Corrigé |
| `EndianUtils_readUShortBE_3_1_Test#testReadUShortBE` | Flux 12 34 ; entier BE non signé 0x1234. | 4 | Conservé |
| `EndianUtils_ubyteToInt_29_1_Test#testUbyteToIntMax` | Conversion du byte 127 → entier 127 ; le flux local créé est inutilisé. | 2 | Conservé |
| `FilenameUtils_getSanitizedEmbeddedFileName_3_1_Test#testGetSanitizedEmbeddedFileName_withPathContainingColonAndColonAndColonAndBackslash` | RESOURCE_NAME_KEY seul = C:::/path/to/file.txt ; nom attendu file.txt. | 1 | Conservé |
| `FilenameUtils_getSanitizedEmbeddedFilePath_4_0_Test#testGetSanitizedEmbeddedFilePath_withReservedCharacters` | EMBEDDED_RESOURCE_PATH = invalid:filename, autres propriétés null ; défaut test, maxLength 100 ; résultat non nul sans deux-points. | 1 | Conservé |
| `FilenameUtils_resolveWithin_5_0_Test#testResolveWithin` | Répertoire /tmp, enfant test.txt ; Path attendu /tmp/test.txt. Dépend de l’absence du fichier au moment de la mesure. | 1 | Conservé |
| `MediaType_equals_18_0_Test#testEqualsWithNull` | Fixture MediaType avec paramètre charset ; equals(null) doit être false. | 1 | Conservé |
| `MediaType_image_2_0_Test#testImageMethod` | image/png sans puis avec charset=UTF-8 ; image("") doit renvoyer null. | 2 | Conservé |
| `MediaType_video_4_0_Test#testVideoMethod` | video(mp4) doit égaler new MediaType("video", "mp4", emptyMap). | 1 | Conservé |

## Annexe B — Intentions et oracles des tests manuels

Les tableaux regroupent les cas qui partagent la même intention. Les valeurs attendues sont calculées à partir de l’ordre des octets, du contrat de l’API ou d’un comportement précis du code figé. Lorsqu’un test caractérise une implémentation, nous ne présentons pas cet oracle comme une règle universelle.

### EndianUtils — 28 cas

| Test(s) | Intention (comportement testé) | Motivation des données | Oracle (comment la valeur attendue est déterminée) |
|---|---|---|---|
| `readUShortLE_allZeroBytes_returnsZero`, `readUShortBE_…`, `readUIntLE_…`, `readUIntBE_…`, `readIntLE_…`, `readIntBE_…`, `readIntME_…`, `readLongLE_…`, `readLongBE_allZeroBytes_returnsZero` (9 tests) | Un flux qui contient exactement le nombre d'octets requis, tous à `0x00`, est lu sans erreur et vaut 0. | C'est le seul cas où `(ch1 \| ch2 \| …)` vaut exactement 0 : il sépare la condition d'underrun `< 0` de sa mutation `<= 0`. Aucun test existant n'utilisait des octets tous nuls. | Des octets tous nuls représentent la valeur 0 dans n'importe quel ordre d'octets ; comme le flux n'est pas tronqué, aucune exception ne doit être levée. |
| `readUIntLE_eofOnFirstByteThenData_throwsUnderrun`, `readUIntBE_…`, `readIntLE_…`, `readIntBE_…`, `readIntME_…`, `readLongLE_…`, `readLongBE_eofOnFirstByteThenData_throwsUnderrun` (7 tests) | Si **n'importe quelle** lecture renvoie -1, même la première, la méthode signale un underrun, quels que soient les octets suivants. | Un flux maison (`sequence(-1, 1, 2, 3, …)`) renvoie -1 puis des octets valides. Avec `ch1 = -1`, chaque OU remplacé par un ET rend l'expression positive, ce que les tests générés (qui tronquent toujours la fin du flux) ne pouvaient pas détecter. Ce flux contrôlé isole la position de l’erreur ; il ne représente pas tous les comportements d’un flux ordinaire. | Le contrat de la méthode (javadoc : « if the stream cannot provide enough bytes ») impose `BufferUnderrunException`. |
| `readLongLE_distinctBytes_assemblesLittleEndian`, `readLongBE_distinctBytes_assemblesBigEndian` | Assemblage correct des 8 octets d'un `long` dans les deux ordres, y compris avec le bit de signe à 1. | Octets tous distincts `01…08` : chaque position de bits (0 à 56) est observable, donc tout décalage ou toute addition mutée change le résultat ; `F0 DE … 12` / `12 34 … F0` vérifie le cast `(long)` des octets de poids fort (sans lui, le résultat serait tronqué) ; `FF × 8` vérifie la valeur -1. | Composition explicite : LE → `0x0807060504030201`, BE → `0x0102030405060708`, et la même valeur `0x123456789ABCDEF0` lue dans les deux ordres. |
| `readLongBE_sevenBytesOnly_throwsUnderrun` | Underrun sur le dernier des 8 octets. | 7 octets valides puis fin de flux : seul `ch8` vaut -1. | Exception attendue par contrat. |
| `readUE7_continuationThenZeroByte_returns128` | Lecture d'une valeur sur deux octets dont le dernier est `0x00`. | `{0x81, 0x00}` : premier octet avec bit de continuation (valeur 1), second octet exactement 0. Un octet final nul distingue `i >= 0` de `i > 0` dans la condition de boucle et `i < 0` de `i <= 0` dans le test d'erreur. | `(1 << 7) + 0 = 128`, par la définition du format (7 bits utiles par octet, poids fort d'abord). |
| `readUE7_stopsAfterSixContinuationBytes` | La lecture s'arrête après 6 octets (`max = 6`) même si le bit de continuation est encore à 1. | 6 octets `0x81` suivis d'un 7e octet `0x01` : un 7e tour de boucle ajouterait `0x01` et changerait la valeur. | `v` vaut `((((((1 << 7 \| 1) << 7 \| 1) …` = 34 630 287 489, calculé dans le test par la même récurrence et vérifié par une constante ; le 7e octet est consommé mais ignoré d'après le code. |
| `getShortLE_singleArgOverload_readsFromOffsetZero`, `getUShortLE_…`, `getShortBE_…`, `getIntBE_…`, `getUIntLE_singleArgOverload_isUnsigned`, `getUIntBE_singleArgOverload_isUnsigned`, `getShortBE_withOffset_readsSignedBigEndian` (7 tests) | Les surcharges à un argument lisent à partir de l'indice 0 ; contraste signé / non signé. | Deux octets différents (`0x12`, `0x34`) pour distinguer LE et BE et obtenir une valeur non nulle ; `0xFF…` pour vérifier que `getShort*` renvoie -1 (signé) et `getUShort*`/`getUInt*` 65535 / 4 294 967 295 (non signé) ; `0x8000` pour le bit de signe d'un `short`. | Valeurs hexadécimales composées à la main selon l'ordre des octets ; -1 et 65535 sont les deux interprétations de `0xFFFF`. |

### MediaType — 12 cas

| Test | Intention (comportement testé) | Motivation des données | Oracle |
|---|---|---|---|
| `factories_buildCanonicalTypeFromSubtype` | Les cinq fabriques `application/audio/text/image/video(subtype)` construisent `type/sous-type`, et renvoient l'instance mise en cache par `parse`. | Un sous-type usuel par fabrique (`json`, `mpeg`, `csv`, `png`, `mp4`), choisis différents pour qu'une fabrique qui en appellerait une autre soit détectée. | Chaîne canonique attendue lisible dans le code (`"application/" + type`) ; `assertSame` avec `parse("text/csv")` car les types simples sont mis en cache. |
| `setOfMediaTypes_ignoresNullAndDuplicates_andIsUnmodifiable` | `set(MediaType...)` ignore `null`, dédoublonne et renvoie un ensemble non modifiable. | `TEXT_PLAIN, null, TEXT_HTML, TEXT_PLAIN` : un `null` (branche `if (type != null)`), un doublon (sémantique d'ensemble) et deux types distincts (taille observable, 2). | Taille 2 et contenu exact d'après le code ; `UnsupportedOperationException` d'après la javadoc (« unmodifiable set »). |
| `setOfStrings_parsesEachString_andSkipsUnparsable` | `set(String...)` analyse chaque chaîne et ignore celles qui ne sont pas des types MIME. | `"text/plain", "not a media type", "text/html", "text/plain"` : une chaîne sans `/` (→ `parse` renvoie `null`, branche `if (mt != null)`), un doublon ; plus le cas sans argument. | Taille 2, contenu, non modifiable, ensemble vide pour aucun argument. |
| `parse_charsetBeforeType_isAccepted` | Forme « charset d'abord » des serveurs web défaillants (TIKA-350), annoncée par la javadoc de `parse`. | `"charset=UTF-8; text/plain"` : ne correspond pas à `TYPE_PATTERN`, donc seule la branche `CHARSET_FIRST_PATTERN` peut l'accepter. | Forme canonique `text/plain; charset=UTF-8` (paramètres après le type, triés). |
| `parse_simpleNameWithBoundaryCharacters_isCached` | Un nom composé uniquement de caractères « simples », y compris ceux aux **bornes** des intervalles (`0`, `9`, `a`, `z`) et les spéciaux admis (`- + . _`), est reconnu par `isSimpleName` et mis en cache. | `"a0z9/x-0.9_a+z" + nombre unique` : chaque borne et chaque caractère spécial apparaît ; le suffixe unique (nanosecondes) évite qu'une exécution précédente dans le même JVM (pitest réutilise le JVM entre mutants, et `SIMPLE_TYPES` est statique) ait déjà mis la chaîne en cache. | L'effet observable de `isSimpleName` est l'identité d'instance (`assertSame` entre deux `parse` successifs), optimisation documentée par le champ `SIMPLE_TYPES` ; un mutant qui rejette une borne fait passer la chaîne par l'expression régulière, qui crée une instance à chaque appel. |
| `parse_nonSimpleName_isNotCached_butStillParsed` | Contre-épreuve : un nom non simple (majuscules) est analysé par l'expression régulière, normalisé, mais pas mis en cache ; un sous-type vide est rejeté. | `"Text/Plain"` deux fois ; `"text/"`. | `equals(TEXT_PLAIN)` mais `assertNotSame` ; `assertNull` pour `"text/"` (`VALID_CHARS` exige au moins un caractère). |
| `constructorWithParameters_mergesBothMaps_newValuesWin` | `union(a, b)` avec deux maps non vides : union des clés, la valeur ajoutée l'emporte. | `a = {charset=UTF-8, x=1}`, `b = {charset=ISO-8859-1, format=flowed}` : une clé propre à chaque côté (détecte la suppression de chaque `putAll`) et une clé en conflit (détecte l'ordre des `putAll`). | Valeurs attendues déduites de `putAll(a)` puis `putAll(b)` ; chaîne canonique triée par nom de paramètre. |
| `constructorWithEmptyMap_keepsBaseParameters` | `union(a, b)` avec `b` vide renvoie `a` tel quel. | Type de base avec deux paramètres, map vide ajoutée. | Égalité avec le type de base et paramètres inchangés. |
| `constructorWithCharset_addsCharsetParameterToBaseType` | Les constructeurs de commodité `(MediaType, Charset)` et `(MediaType, name, value)` ajoutent un paramètre. | `UTF_8` et `charset=ISO-8859-1` sur des types sans paramètre. | Chaîne canonique `text/plain; charset=UTF-8`. |
| `getBaseType_stripsParameters_andReturnsSelfWhenNone` | `getBaseType` retire les paramètres et renvoie `this` quand il n'y en a pas. | Un type avec `charset`, puis un type construit sans paramètre. | Javadoc (`text/plain` pour `text/plain; charset=utf-8`), `hasParameters()` faux, et `assertSame` pour la branche `return this`. |
| `hashCode_isConsistentWithEquals_andWithCanonicalString` | `hashCode` est cohérent avec `equals` et dérive de la chaîne canonique. | `new MediaType("text","plain")` et `parse("TEXT/PLAIN")` (égaux après normalisation) ; `TEXT_PLAIN` contre `TEXT_HTML`. | Contrat `Object` (égaux ⇒ même hash) ; égalité avec `"text/plain".hashCode()` d'après le code ; hash différents pour deux types différents (vrai ici, ce qui distingue le mutant « renvoie 0 »). |
| `compareTo_followsCanonicalStringOrder` | Ordre total cohérent avec les chaînes canoniques ; seul le signe est garanti. | `application/json`, `application/xml`, `application/json; charset=UTF-8` : un préfixe strict (`json` < `json; …`) et deux sous-types. | `< 0`, `> 0`, `== 0` sur le même type ; ordre d'un `TreeSet` égal à l'ordre lexicographique des chaînes. |

### FilenameUtils — 16 cas

| Test | Intention (comportement testé) | Motivation des données | Oracle |
|---|---|---|---|
| `embeddedName_eachPropertyAloneIsUsed` | Chacune des cinq propriétés de métadonnées suffit, seule, à produire un nom (ordre de repli de `getEmbeddedName`). | Une seule propriété renseignée par assertion, avec un nom qui porte son rang (`n1.txt` … `n5.txt`) ; un chemin pour `INTERNAL_PATH` et `EMBEDDED_RESOURCE_PATH` afin de vérifier que seul le dernier segment est gardé ; métadonnées vides → `null`. | Le nom attendu est celui de la propriété renseignée, assaini (dernier segment) ; `null` si rien n'est renseigné (javadoc). |
| `embeddedName_resourceNameWinsOverOtherProperties` | Priorité `RESOURCE_NAME_KEY` > `INTERNAL_PATH` > … > `ORIGINAL_RESOURCE_NAME`. | Plusieurs propriétés renseignées avec des **valeurs différentes**, contrairement aux tests originaux qui donnent la même valeur à deux propriétés (ce qui rendait les négations indétectables). | Ordre lu dans le code de `getEmbeddedName`. |
| `embeddedPath_eachPropertyAloneIsUsed`, `embeddedPath_resourcePathWinsOverOtherProperties` | Même chose pour `getEmbeddedPath` (ordre `EMBEDDED_RESOURCE_PATH` > `INTERNAL_PATH` > `RESOURCE_NAME_KEY` > `EMBEDDED_RELATIONSHIP_ID` > `ORIGINAL_RESOURCE_NAME`), en conservant le chemin relatif (`a/p1.txt`). | Idem ; `/a/p1.txt` vérifie aussi la suppression du `/` initial. | Idem. |
| `fileName_exactlyMaxLength_isNotTruncated` | Le nom dont la partie sans extension fait **exactement** `maxLength` n'est pas tronqué ; un caractère de plus l'est. | `abcdefghij.txt` (10) avec `maxLength = 10`, puis 11 caractères. | Code : `namePart.length() > maxLength` ; forme tronquée `substring(0, maxLength - ext - 3) + "..." + ext` = `abc....txt`. |
| `filePath_exactlyMaxLength_keepsRelativePath` | Un chemin relatif dont la longueur totale vaut exactement `maxLength` est conservé entier ; un de moins et seul le nom est gardé. | `a/b/x.txt` (9 caractères) avec `maxLength` 9 puis 8. | Code lignes 261-270. |
| `filePath_tooLongPath_nameExactlyMaxLength_isNotTruncated` | Chemin trop long mais nom exactement à la limite : nom entier sans chemin ; nom d'un caractère de plus : tronqué. | `dir/abcdefgh.txt` avec `maxLength = 8` (nom = 8), puis nom de 9. | Code ligne 265-266 : `a....txt` pour la troncature. |
| `fileName_blankNamePartBeforeExtension_returnsNull` | Un nom réduit à des espaces devant l'extension est rejeté. | `"   .txt"` : l'extension est valide mais la partie nom est blanche. | `null` (garde ligne 173-174), jamais `""`. |
| `filePath_dotOnly_returnsNull` | Le chemin `.` ne produit aucun nom. | `"."` : `getName` renvoie `""` pour `.`. | `null` (garde 231-232). |
| `filePath_lastSegmentIsOnlyAnExtension_returnsNull` | Un dernier segment réduit à une extension (`dir/.txt`) ne produit aucun nom. | `dir/.txt` : l'extension vaut tout le nom mais pas tout le chemin, donc la garde de la ligne 239 ne s'applique pas et c'est celle de la ligne 249 qui joue. | `null`. |
| `degenerateShortNames_areKeptAsNames` | Des noms d'un ou deux caractères (`A`, `AB`, `1:`, `[:`) ne sont pas pris pour des préfixes de lecteur et donnent un nom plus l'extension par défaut. | Chaque nom fait basculer **une** condition de `getPrefixLength` ligne 323 : `A` (longueur ≠ 2 → le mutant appelle `charAt(1)` et lève une exception), `AB` (deuxième caractère ≠ `:`), `1:` (premier caractère < `A`), `[:` (premier caractère > `Z`). `commons-io` renvoie 0 ou -1 pour tous. | `X.bin` : nom conservé, `:` remplacé par `/` puis retiré en fin de chemin, extension par défaut faute de type MIME. |
| `suffix_fiveCharactersAccepted_sixRejected` | Une extension de 5 caractères point compris est acceptée, 6 refusée. | `.abcd` et `.abcde`. | Javadoc (« 5 or less ») et code (`< 6`). |
| `calculateExtension_unknownOrInvalidMimeType_fallsBackToBin` | Type MIME inconnu (sans extension enregistrée) ou syntaxiquement invalide → `.bin` ; absent → valeur par défaut ; connu → son extension. | `application/x-ift3913-unknown-type`, `ceci n'est pas un type mime`, aucun type, `application/pdf`. | Javadoc de `calculateExtension` et code : `lookupExtension` renvoie `null` → `.bin`. |
| `resolveWithin_existingChild_isReturned` | Avec des chemins qui **existent** (répertoire temporaire JUnit), la vérification par chemins réels accepte un enfant direct et un enfant imbriqué. | `@TempDir` + fichiers créés : seule façon d'exécuter les lignes 305-312. | Le chemin résolu est renvoyé, égal à `dir.resolve(...)`. |
| `resolveWithin_missingChildInExistingDir_isReturned` | Un nom inexistant dans un répertoire existant est résolu sans erreur. | Répertoire temporaire, enfant absent : `Files.exists(resolved)` est faux. Le mutant entre dans la branche et `toRealPath()` lève `NoSuchFileException`. | Chemin résolu renvoyé. |
| `resolveWithinRejectsExistingSymbolicLinkOutsideDirectory` | Refuser une sortie par lien symbolique. | Deux répertoires temporaires distincts, lien créé dans le répertoire autorisé vers l’extérieur. | `IOException` attendue : le chemin réel doit rester dans le répertoire autorisé. |
