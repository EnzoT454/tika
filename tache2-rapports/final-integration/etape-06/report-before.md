0# IFT3913 – Tâche 2 : augmentation de la suite de tests de Tika avec ChatUniTest et pitest

| Nom complet | Identifiant GitHub |
|---|---|
|  Mohamed Saidana | FakeTMG |
| Aqel Hamza | EnzoT454 |

Ce dépôt est un fork de [umontreal-diro/tika](https://github.com/umontreal-diro/tika) (Apache Tika 4.0.0-SNAPSHOT,
commit `a2d75c2`). Le README original de Tika a été déplacé dans [README-tika-original.md](README-tika-original.md).
Toute la documentation de la tâche 2 se trouve dans ce fichier unique.

## Sommaire

1. [Résumé des résultats](#1-résumé-des-résultats)
2. [Contexte et environnement](#2-contexte-et-environnement)
3. [Choix des classes à tester](#3-choix-des-classes-à-tester)
4. [Installation de ChatUniTest dans le pipeline Maven](#4-installation-de-chatunitest-dans-le-pipeline-maven)
5. [Tests générés par ChatUniTest](#5-tests-générés-par-chatunitest)
6. [Analyse critique des tests générés](#6-analyse-critique-des-tests-générés)
7. [Analyse de mutation avec pitest](#7-analyse-de-mutation-avec-pitest)
8. [Mutants détectés par les tests générés](#8-mutants-détectés-par-les-tests-générés)
9. [Tests supplémentaires écrits à la main](#9-tests-supplémentaires-écrits-à-la-main)
10. [Exécution dans GitHub Actions](#10-exécution-dans-github-actions)
11. [Reproduire les résultats](#11-reproduire-les-résultats)

## 1. Résumé des résultats

**État de l'intégration locale `tache2-final` au 8 octobre 2026.** Les scores et
comptages Saidana ci-dessous restent ceux de l'expérience historique. Depuis le
tri de l'étape 3, la suite finale contient 161 tests issus de ChatUniTest retenus
(134 conservés sans changement d'assertions, 27 corrigés humainement) et 56 tests
manuels. Les 32 candidats rejetés sont archivés avec leurs motifs. La suite
`tika-core` exécutée par Surefire compte 966 cas : 964 réussis, deux désactivations
préexistantes, aucun échec ni erreur. Aucun nouveau test retenu n'est désactivé.
La construction complète ciblée `tika-core` et ses dépendances réussit à
l’étape 4, avec les contrôles applicables activés et huit tests supplémentaires
du processeur d’annotations réussis. Les mesures comparables de l’étape 5 sont terminées : **A = 185/406
(45,57 %), B = 317/406 (78,08 %), C = 396/406 (97,54 %)**. Le score
compte uniquement `KILLED` ; C conserve séparément sept `SURVIVED`, deux
`NO_COVERAGE` et un `TIMED_OUT`. Les tableaux historiques ci-dessous ne sont
pas les mesures finales ; celles-ci sont présentées en section 7.5.

Trois classes de `tika-core` ont été traitées : `EndianUtils`, `MediaType` et `FilenameUtils`. ChatUniTest 2.1.1,
branché sur un modèle Qwen2.5-Coder-7B exécuté localement avec Ollama, a généré des tests pour les méthodes
publiques de ces classes ; chaque test exporté a été ré-exécuté par nos soins, les méthodes à oracle faux ont été
désactivées (`@Disabled` avec le motif), les tests non compilables ont été corrigés à la main avec le minimum de
changements, puis des tests manuels ont été écrits pour les mutants survivants.

| | Tests originaux | + tests ChatUniTest | + tests manuels |
|---|---|---|---|
| Mutants tués / 406 (pitest, 3 classes) | 159 (**39 %**) | 269 (**66 %**) | 393 (**97 %**) |
| `EndianUtils` (206 mutants) | 38 (18 %) | 129 (63 %) | 204 (99 %) |
| `MediaType` (85 mutants) | 48 (56 %) | 65 (76 %) | 84 (99 %) |
| `FilenameUtils` (115 mutants) | 73 (63 %) | 75 (65 %) | 105 (91 %) |
| Couverture de lignes des 3 classes (JaCoCo) | 309/451 (69 %) | | 447/451 (99 %) |
| Méthodes de test | 24 | + 193 générées (dont 58 désactivées) | + 55 manuelles |

Les 13 mutants restants sont tous équivalents (code mort, `return null` remplacé par `null`, variable incrémentée
puis jamais relue, etc.) et sont justifiés un par un aux sections 8 et 9. La suite complète de `tika-core` passe :
`Tests run: 997, Failures: 0, Errors: 0, Skipped: 60` (749 tests originaux dont 2 ignorés par Tika, 248 ajoutés).

Ce que la génération a donné, en bref :

- **Taux de réussite brut de ChatUniTest** : 36 méthodes exportées sur 49 soumises (73 %, dont une seulement après
  une seconde configuration avec un contexte doublé) ; mais 21 des 70 méthodes
  et constructeurs des trois classes n'ont jamais été soumis au modèle (filtre de ChatUniTest sur les constructeurs,
  les méthodes privées et les noms `get*`/`set*`/`is*`), dont des méthodes publiques importantes comme
  `MediaType.set(...)` et `MediaType.getBaseType()`.
- **Fiabilité des tests exportés** : sur 157 méthodes de test exportées « avec succès », 46 (29 %) échouent sur le
  code non muté (oracle faux). ChatUniTest 2.1.1 les exporte quand même, parce qu'il ne traite pas les échecs
  d'assertion comme des erreurs par défaut.
- **Corrections manuelles** pour rendre compilables les 14 méthodes sans test : 11 fichiers `EndianUtils` (15 lignes,
  une seule cause : la classe imbriquée `EndianUtils.BufferUnderrunException` absente du contexte), 1 fichier
  `MediaType` (2 lignes, API inventée), 1 fichier `FilenameUtils` (4 lignes, API inventée et Mockito inutile) ; une
  méthode (`getSanitizedEmbeddedFileName`) n'a obtenu un test qu'à la seconde configuration (contexte 16k, budgets de
  jetons doublés), après 18 appels au total.
- **Apport en mutation** : les tests générés tuent 110 mutants de plus que les tests originaux, presque tous dans
  `EndianUtils` (méthodes jamais testées) ; sur `FilenameUtils`, déjà bien testée, ils n'en tuent que deux. Les
  tests manuels, écrits en lisant le rapport pitest, en tuent 124 de plus en 55 méthodes.

## 2. Contexte et environnement

| Élément | Valeur |
|---|---|
| Projet | Apache Tika 4.0.0-SNAPSHOT, module `tika-core` (fork `umontreal-diro/tika`, commit `a2d75c2`) |
| Build | Maven 3.9.9 (wrapper `mvnw` 3.9.12 dans le CI) ; JDK 17 (Temurin 17.0.20) pour la génération, les tests et pitest, JDK 17 dans GitHub Actions ; la couverture de référence de la section 3.2 a été mesurée avec le JDK 21 avant le passage au JDK 17 (voir 4.2) |
| Framework de test | JUnit Jupiter 6.1.3 (déjà utilisé par Tika) |
| Couverture | JaCoCo 0.8.15 (déjà configuré dans `tika-parent/pom.xml`) |
| Génération de tests | [ChatUniTest](https://github.com/ZJU-ACES-ISE/ChatUniTest) via le plugin Maven `io.github.zju-aces-ise:chatunitest-maven-plugin:2.1.1` |
| Modèle de langage | `qwen2.5-coder:7b` (Qwen2.5-Coder-7B-Instruct, quantification Q4_K_M, 4,7 Go), exécuté localement avec [Ollama](https://ollama.com) 0.35.0 |
| Analyse de mutation | [pitest](https://pitest.org) `pitest-maven:1.30.0` + `pitest-junit5-plugin:1.2.3` |
| Machine | Portable Windows 11, Intel Core i5-11400H (6 cœurs), 16 Go de RAM, NVIDIA GeForce RTX 2050 (4 Go de VRAM) |

Le modèle ne tient pas entièrement dans les 4 Go de VRAM : Ollama en charge environ 45 % sur le GPU et le reste sur le CPU.
Vitesse mesurée : environ 11 jetons/s en génération et 120 jetons/s pour le traitement du prompt, soit 1 à 2 minutes
par appel au modèle.

## 3. Choix des classes à tester

### 3.1 Méthode de sélection

Nous avons cherché des classes de `tika-core` qui (a) ont déjà une classe de test dédiée, (b) ne sont pas couvertes
à 100 % et (c) ont des mutants vivants. Pour cela :

1. nous avons exécuté **toute** la suite de tests de `tika-core` (749 tests) avec JaCoCo
   (`mvn -pl tika-core test`), afin de tenir compte de la couverture indirecte apportée par les autres tests du module ;
2. nous avons exécuté pitest sur les classes candidates avec **uniquement leur classe de test dédiée**
   (`EndianUtilsTest`, `MediaTypeTest`, `FilenameUtilsTest`), pour mesurer le score de mutation de référence.

Nous avons retenu trois classes utilitaires, sans état, dont le comportement est facile à spécifier par un oracle
exact (ce qui rend la comparaison des oracles IA / humains pertinente) :

| Classe | Test original | Rôle |
|---|---|---|
| `org.apache.tika.io.EndianUtils` (483 lignes, 32 méthodes) | `EndianUtilsTest` (4 tests) | Lecture d'entiers little/big/middle-endian dans des flux et des tableaux d'octets |
| `org.apache.tika.mime.MediaType` (432 lignes, 28 méthodes) | `MediaTypeTest` (10 tests) | Représentation immuable d'un type MIME (`type/sous-type; param=valeur`), analyse et normalisation |
| `org.apache.tika.io.FilenameUtils` (419 lignes, 14 méthodes) | `FilenameUtilsTest` (10 tests) | Normalisation et assainissement de noms et de chemins de fichiers embarqués |

### 3.2 Couverture de référence (JaCoCo, suite complète de `tika-core`)

Rapport brut : [tache2-rapports/01-jacoco-tests-originaux/jacoco.csv](tache2-rapports/01-jacoco-tests-originaux/jacoco.csv).

| Classe | Lignes | Branches | Méthodes | Instructions |
|---|---|---|---|---|
| `EndianUtils` | 31/121 (26 %) | 10/28 (36 %) | 4/32 (12 %) | 158/685 (23 %) |
| `MediaType` | 125/155 (81 %) | 67/90 (74 %) | 22/28 (79 %) | 554/698 (79 %) |
| `FilenameUtils` | 153/175 (87 %) | 82/110 (75 %) | 13/14 (93 %) | 790/871 (91 %) |

Méthodes non couvertes ou partiellement couvertes :

- **`EndianUtils`** : seules `readUE7`, `readUIntLE`, `readUIntBE` et `readIntME` sont exercées par `EndianUtilsTest`
  (et partiellement : la branche `BufferUnderrunException` de `readUIntBE` n'est pas atteinte, deux branches de `readUE7`
  non plus). Les 27 autres méthodes ne sont **jamais appelées** par aucun test de `tika-core` : `readShortLE/BE`,
  `readUShortLE/BE`, `readIntLE/BE`, `readLongLE/BE`, `getShortLE/BE`, `getUShortLE/BE`, `getIntLE/BE`, `getUIntLE/BE`,
  `getLongLE`, `ubyteToInt`, `getUByte` (avec et sans décalage).
- **`MediaType`** : `audio()`, `video()`, `set(MediaType...)`, `set(String...)`, `MediaType(MediaType, String, String)` et
  `MediaType(MediaType, Charset)` ne sont jamais appelés ; `parse` (chaîne sans `/`, forme « charset d'abord »),
  `isSimpleName`, `union`, `equals` (argument qui n'est pas un `MediaType`) et le constructeur privé `MediaType(String, int)`
  ne sont que partiellement couverts.
- **`FilenameUtils`** : toutes les méthodes publiques sont appelées, mais 28 branches ne le sont pas : ordre de repli de
  `getEmbeddedPath` / `getEmbeddedName` (`INTERNAL_PATH`, `EMBEDDED_RELATIONSHIP_ID`, `ORIGINAL_RESOURCE_NAME`),
  `getPrefixLength` (préfixe Windows `C:` seul), `resolveWithin` (branche « chemins existants / liens symboliques »),
  `calculateExtension` et `lookupExtension` (type MIME inconnu ou sans extension), `getName` (chemin se terminant par `.`).

### 3.3 Mutants vivants avec les tests originaux (pitest)

Rapport brut : [tache2-rapports/02-pitest-tests-originaux/index.html](tache2-rapports/02-pitest-tests-originaux/index.html)
(`mutations.xml` dans le même dossier).

| Classe | Mutants | Tués | Survivants | Sans couverture | Score de mutation | Force des tests |
|---|---|---|---|---|---|---|
| `EndianUtils` | 206 | 38 | 14 | 154 | **18 %** | 73 % |
| `MediaType` | 85 | 48 | 19 | 18 | **56 %** | 72 % |
| `FilenameUtils` | 115 | 73 | 20 | 22 | **63 %** | 78 % |
| **Total** | **406** | **159** | **53** | **194** | **39 %** | 75 % |

(« Score de mutation » = tués / total ; « force des tests » = tués / mutants couverts, telle que définie par pitest.)

Les trois classes ont donc à la fois des méthodes non couvertes et des mutants vivants (53 survivants dans du code couvert,
194 mutants dans du code jamais exécuté), ce qui en fait de bons candidats pour la génération de tests.

## 4. Installation de ChatUniTest dans le pipeline Maven

Toutes les modifications sont dans [tika-core/pom.xml](tika-core/pom.xml) (recherchez `IFT3913`).

### 4.1 Plugin Maven ChatUniTest

```xml
<plugin>
  <groupId>io.github.zju-aces-ise</groupId>
  <artifactId>chatunitest-maven-plugin</artifactId>
  <version>2.1.1</version>
  <configuration>
    <apiKeys>ollama</apiKeys>                      <!-- Ollama n'exige pas de clé -->
    <model>codeqwen:v1.5-chat</model>              <!-- alias Ollama vers qwen2.5-coder:7b, voir 4.3 -->
    <url>http://localhost:11434/v1/chat/completions</url>
    <maxPromptTokens>${chatunitest.maxPromptTokens}</maxPromptTokens>      <!-- 5500 par défaut (contexte Ollama 8192) -->
    <maxResponseTokens>${chatunitest.maxResponseTokens}</maxResponseTokens><!-- 1024 par défaut ; surchargeables avec -D -->
    <minErrorTokens>500</minErrorTokens>
    <testNumber>2</testNumber>                     <!-- 2 tentatives par méthode (5 par défaut) -->
    <maxRounds>3</maxRounds>                       <!-- 3 tours de réparation (5 par défaut) -->
    <thread>false</thread>                         <!-- un seul GPU : une requête à la fois -->
    <stopWhenSuccess>true</stopWhenSuccess>
    <tmpOutput>${project.build.directory}/chatunitest-info</tmpOutput>
    <testOutput>${project.basedir}/chatunitest</testOutput>
  </configuration>
</plugin>
```

et la dépendance de test exigée par ChatUniTest (`chatunitest-starter` apporte JUnit 5, Mockito, ByteBuddy) :

```xml
<dependency>
  <groupId>io.github.ZJU-ACES-ISE</groupId>
  <artifactId>chatunitest-starter</artifactId>
  <version>1.5.0</version>
  <type>pom</type>
  <scope>test</scope>
  <exclusions>
    <exclusion><groupId>org.junit.platform</groupId><artifactId>junit-platform-runner</artifactId></exclusion>
    <exclusion><groupId>org.junit.vintage</groupId><artifactId>junit-vintage-engine</artifactId></exclusion>
  </exclusions>
</dependency>
```

### 4.2 Problèmes rencontrés et solutions

| Problème | Cause | Solution |
|---|---|---|
| Après l'ajout de `chatunitest-starter`, Surefire affichait `Tests run: 0` pour tout `tika-core` | Le starter apporte `junit-platform-runner` (JUnit 4). En sa présence, Surefire choisit le fournisseur JUnit 4 et ignore les tests JUnit 5/6 | Exclusion de `junit-platform-runner` et `junit-vintage-engine` (voir ci-dessus) ; la suite complète (749 tests) s'exécute de nouveau |
| `No Model with name qwen2.5-coder:7b` | ChatUniTest 2.1.1 n'accepte qu'une liste fermée de noms de modèles (`gpt-3.5-turbo`, `gpt-4o`, `gpt-4o-mini`, `code-llama`, `codeqwen:v1.5-chat`, …) et envoie ce nom tel quel à l'API | Création d'un alias Ollama : `ollama cp qwen2.5-coder:7b codeqwen:v1.5-chat`. Le nom accepté par ChatUniTest pointe ainsi vers le modèle réellement utilisé |
| Prompts tronqués | Fenêtre de contexte par défaut d'Ollama = 4096 jetons, alors que ChatUniTest suppose 16 385 jetons pour `codeqwen` | Ollama lancé avec `OLLAMA_CONTEXT_LENGTH=8192` et `maxPromptTokens` ramené à 5500 |
| pitest ne trouvait pas de lanceur JUnit | `pitest-junit5-plugin` exige `junit-platform-launcher` sur le classpath de test | Ajout de `org.junit.platform:junit-platform-launcher` en portée `test` (version gérée par `junit-bom` 6.1.3) |
| Chaque message d'erreur renvoyé au modèle commençait par deux avertissements javac sans rapport (`Annotation processing is enabled…`, `Supported source version 'RELEASE_17' … less than -source '21'`) | ChatUniTest compile les tests avec le JDK qui exécute Maven (ici le JDK 21) et transmet tous les diagnostics, avertissements compris, au prompt de réparation ; le processeur d'annotations de Tika cible Java 17 | Installation d'un JDK 17 (`winget install EclipseAdoptium.Temurin.17.JDK`) et génération relancée avec `JAVA_HOME` pointant dessus : les messages ne contiennent plus que les vraies erreurs, et les tests générés sont garantis compilables par le CI (JDK 17) |
| Le portable est passé en veille après 20 minutes d'inactivité pendant la génération (2 h perdues) | Réglage d'alimentation par défaut de Windows | Processus temporaire `SetThreadExecutionState(ES_SYSTEM_REQUIRED)` pendant la génération |
| `mvn test` sur `MediaType*` : un mutant de `isSimpleName` survivait alors que le test manuel le visait | pitest réutilise le même JVM pour plusieurs mutants ; le cache statique `SIMPLE_TYPES` de `MediaType` gardait la chaîne de test d'une exécution précédente | Le test utilise une chaîne unique à chaque exécution (voir 9.2) |

### 4.3 Modèle local avec Ollama

```bash
winget install Ollama.Ollama                       # Ollama 0.35.0
ollama pull qwen2.5-coder:7b                        # 4,7 Go, Q4_K_M
ollama cp qwen2.5-coder:7b codeqwen:v1.5-chat       # alias accepté par ChatUniTest
OLLAMA_CONTEXT_LENGTH=8192 OLLAMA_KEEP_ALIVE=30m ollama serve
```

Ollama expose une API compatible OpenAI sur `http://localhost:11434/v1/chat/completions`, que ChatUniTest utilise
sans modification de son code.

### 4.4 Commandes de génération

```bash
# depuis la racine du dépôt ; test-compile garantit la résolution des dépendances de test
mvn -pl tika-core test-compile chatunitest:class -DselectClass=org.apache.tika.io.EndianUtils \
    -Dcheckstyle.skip -Drat.skip -Dforbiddenapis.skip -Dossindex.skip
mvn -pl tika-core test-compile chatunitest:class -DselectClass=org.apache.tika.mime.MediaType ...
mvn -pl tika-core test-compile chatunitest:class -DselectClass=org.apache.tika.io.FilenameUtils ...
```

## 5. Tests générés par ChatUniTest

### 5.1 Déroulement commun

- Commande : `mvn -pl tika-core test-compile chatunitest:class -DselectClass=<classe>` (JDK 17, paramètres de la
  section 4 : 2 tentatives par méthode, 3 tours de réparation, arrêt au premier succès).
- Pour chaque méthode, ChatUniTest construit un prompt contenant la signature de la classe, le code de la méthode
  focale, les signatures des autres méthodes et le code des méthodes dépendantes (voir
  `tache2-rapports/03-chatunitest/<classe>/history*/.../records.json` pour tous les prompts et réponses, jeton par jeton).
  Le modèle répond par une classe de test complète ; ChatUniTest la compile (`javax.tools`), l'exécute avec le lanceur
  JUnit Platform, et renvoie les erreurs au modèle pour réparation.
- **Où sont les tests ?** ChatUniTest écrit les tests acceptés dans `tika-core/chatunitest/tika-parent/tika-core/<paquet>/`
  (un fichier `<Classe>_<méthode>_<n°méthode>_<n°tentative>_Test.java` par méthode, plus une suite `<Classe>_Suite.java`),
  et les traces (prompts, réponses, erreurs de compilation et d'exécution) dans `tika-core/target/chatunitest-info/`.
  Nous avons archivé ces deux sorties, brutes et non modifiées, dans
  [tache2-rapports/03-chatunitest/](tache2-rapports/03-chatunitest/) (`generated-tests-snapshot/`, `error-message/`,
  `history*/`, `stats.md`). Les tests finalement intégrés au projet sont dans
  `tika-core/src/test/java/org/apache/tika/<paquet>/<Classe>_*_Test.java`, chacun avec un en-tête qui indique s'il a
  été modifié et quelles méthodes ont été désactivées.
- **Un point important sur ce que ChatUniTest appelle un succès.** Dans sa version 2.1.1, lorsque le test généré
  compile et que ses seuls échecs d'exécution sont des `AssertionFailedError`, ChatUniTest l'exporte quand même et
  affiche `compile and execute successfully` (code de `AbstractRunner` : les erreurs d'assertion ne déclenchent la
  réparation que si l'option `prune` est activée ; elle est désactivée par défaut). Le journal le montre sans ambiguïté :

  ```
  [         4 tests found           ]
  [         2 tests successful      ]
  [         2 tests failed          ]
  [INFO] Test for method < readUE7 > compile and execute successfully round 1
  ```

  Nous avons donc ré-exécuté nous-mêmes **tous** les tests exportés (lanceur JUnit Platform 6.1.3, même classpath que
  Surefire) pour établir leur résultat réel ; c'est ce résultat qui est rapporté ci-dessous. Les méthodes de test qui
  échouent sur le code non muté ont un oracle faux : nous les avons conservées dans le fichier mais désactivées avec
  `@Disabled("ChatUniTest : oracle faux - <message d'échec>")`, car Surefire (critère « exécution ») et pitest (qui
  refuse une suite rouge) exigent une suite verte.
- La suite `<Classe>_Suite.java` générée utilise `@RunWith(JUnitPlatform.class)` et `junit-platform-runner`
  (JUnit 4), supprimés dans JUnit 6 : elle n'a pas été intégrée.

### 5.2 `EndianUtils` (31 méthodes)

Statistiques complètes par méthode : [tache2-rapports/03-chatunitest/EndianUtils/stats.md](tache2-rapports/03-chatunitest/EndianUtils/stats.md).

| Indicateur | Valeur |
|---|---|
| Méthodes traitées | 31 (toutes les méthodes publiques, surcharges comprises) |
| Appels au modèle | 95 (64 389 jetons de prompt, 38 229 jetons de réponse), environ 1 h 20 de calcul |
| Méthodes avec un test exporté par ChatUniTest | **20 / 31 (65 %)** |
| … dont compilé du premier coup (tour 0) | 17 |
| … dont après un tour de réparation | 2 (`readUE7`, `getUByte` : casts `(byte)` manquants, « possible lossy conversion from int to byte ») |
| … dont après une 2e tentative | 2 (`getShortBE(byte[], int)` : 1re tentative rejetée pour erreurs d'exécution autres que des assertions ; `ubyteToInt` : 4 tours d'erreurs de cast) |
| Méthodes sans test après 2 tentatives × 3 tours | **11 / 31 (35 %)** : les 11 méthodes `read*(InputStream)` qui lèvent `BufferUnderrunException` |
| Appels « perdus » sur ces 11 méthodes | 66 (69 % des appels) |

**Pourquoi les 11 méthodes `read*` ont-elles toutes échoué ?** `BufferUnderrunException` est une classe **imbriquée**
(`EndianUtils.BufferUnderrunException`). Le contexte que ChatUniTest donne au modèle (signature de la classe focale,
code de la méthode, signatures des autres méthodes) ne mentionne jamais les classes imbriquées. Le modèle a donc
deviné un import : `org.apache.tika.exception.BufferUnderrunException` ou `org.apache.tika.io.BufferUnderrunException`,
et aucun des 66 tours de réparation n'a produit `EndianUtils.BufferUnderrunException`, bien que le message javac
(`cannot find symbol: class BufferUnderrunException`) ait été renvoyé à chaque tour. Les fichiers d'erreur
(`error-message/`) montrent aussi deux autres erreurs occasionnelles : un import `InvocationTargetException` manquant
et l'emploi de `ReflectionTestUtils` (classe de Spring, absente du projet).

**Corrections manuelles.** Pour ne pas perdre ces 11 tests, nous avons repris le code de la dernière tentative de
chacun et appliqué le minimum de corrections pour qu'il compile (**15 lignes corrigées pour 11 fichiers**) :

| Correction | Fichiers | Lignes |
|---|---|---|
| Import erroné remplacé par `import org.apache.tika.io.EndianUtils.BufferUnderrunException;` (ou import ajouté) | 11 | 11 |
| Exception vérifiée `BufferUnderrunException` ajoutée à la clause `throws` d'une méthode de test | `readIntBE`, `readUIntLE` | 2 |
| Variable réaffectée puis capturée par une lambda (non effectivement finale) | `readLongBE` | 1 |
| Appel via `ReflectionTestUtils.invokeMethod` (Spring) remplacé par un appel direct | `readUIntBE` | 2 (dont 1 comptée avec l'import) |

Chaque fichier corrigé porte en en-tête la liste exacte de ses corrections.

**Résultat réel à l'exécution** (après intégration, `mvn -pl tika-core test -Dtest='EndianUtils*'`) :

| | Fichiers | Méthodes de test | Réussissent | Échouent (oracle faux, désactivées) |
|---|---|---|---|---|
| Tests exportés tels quels par ChatUniTest | 20 | 71 | 40 | **31 (44 %)** |
| Tests corrigés à la main (11 `read*`) | 11 | 26 | 19 | 7 (27 %) |
| **Total généré** | **31** | **97** | **59 (61 %)** | **38 (39 %)** |

Autrement dit : **aucune** des 11 méthodes `read*` n'a obtenu un test qui compile sans intervention ; sur les 20 tests
qui compilaient, moins d'un sur deux s'exécute sans échec. La suite finale (4 tests originaux + 97 générés dont 38
désactivés) est verte : `Tests run: 101, Failures: 0, Errors: 0, Skipped: 38`.

Couverture de `EndianUtils` après ajout des tests générés (JaCoCo, tests `EndianUtils*` seulement) : lignes
**26 % → 84 %** (102/121), branches **36 % → 86 %** (24/28), méthodes **12 % → 75 %** (24/32). Les lignes encore non
couvertes sont celles dont tous les tests générés ont été désactivés (`readLongBE` entièrement, la ligne de calcul
de `readLongLE`, et les surcharges à une ligne `getShortLE(byte[])`, `getUShortLE(byte[])`, `getShortBE(byte[])`,
`getShortBE(byte[], int)`, `getIntBE(byte[])`, `getUIntLE(byte[])`, `getUIntBE(byte[])`).

### 5.3 `MediaType` (27 méthodes, 11 soumises au modèle)

Statistiques : [tache2-rapports/03-chatunitest/MediaType/stats.md](tache2-rapports/03-chatunitest/MediaType/stats.md).

**ChatUniTest n'a soumis que 11 des 27 méthodes au modèle.** Son filtre (`Counter.filter` dans `chatunitest-core`)
écarte d'office : les constructeurs (6 ici), les méthodes non publiques (`isSimpleName`, `parseParameters`, `unquote`,
`union`), toute méthode `get*()` sans paramètre (`getBaseType`, `getType`, `getSubtype`, `getParameters`) et toute
méthode dont le nom **commence par `set`** : les deux fabriques `set(MediaType...)` et `set(String...)` sont donc
prises pour des accesseurs et ignorées (faux positif du filtre). Le journal affiche `Skip method: …` pour chacune.
Or une bonne partie des mutants vivants de la référence (section 3.3) se trouve précisément dans `getBaseType`,
`getType`, `getSubtype`, `union`, `set` et `isSimpleName`.

| Indicateur | Valeur |
|---|---|
| Méthodes soumises au modèle | 11 / 27 (`application`, `audio`, `image`, `text`, `video`, `parse`, `hasParameters`, `toString`, `equals`, `hashCode`, `compareTo`) |
| Appels au modèle | 28 (44 587 jetons de prompt, 11 757 de réponse), environ 25 minutes |
| Méthodes avec un test exporté | **10 / 11** (6 au tour 0, 4 après une 2e tentative : `audio`, `text`, `toString`, `hashCode`) |
| Méthode sans test après 2 tentatives | `image` : la 1re tentative appelle une surcharge **inexistante** `MediaType.image(String, Map)` (API hallucinée, répétée aux 3 tours), la 2e tente d'invoquer par réflexion une méthode `verifyStatic` qui n'existe pas |
| Corrections manuelles (`image`, 1re tentative reprise) | 2 : `MediaType.image("png", params)` → `new MediaType(MediaType.image("png"), params)` |

**Résultat réel à l'exécution** (`mvn -pl tika-core test -Dtest='MediaType*'`) :

| | Fichiers | Méthodes de test | Réussissent | Échouent (désactivées) |
|---|---|---|---|---|
| Tests exportés tels quels | 10 | 39 | 33 | 6 (15 %) |
| Test corrigé à la main (`image`) | 1 | 5 | 4 | 1 |
| **Total généré** | **11** | **44** | **37 (84 %)** | **7 (16 %)** |

Le taux d'oracles faux est bien plus bas que pour `EndianUtils` (16 % contre 39 %) : les valeurs attendues sont ici
des chaînes (`"text/html"`, `"image/png; charset=UTF-8"`) directement lisibles dans le code de `MediaType`, alors que
`EndianUtils` demandait des calculs sur les bits. Parmi les 7 échecs, 5 sont de vrais oracles faux, 1 est une
mauvaise utilisation de Mockito (`hashCode` : stubs `@BeforeEach` jamais utilisés, `UnnecessaryStubbingException` en
mode strict) et 1 est discutable (`text(null)`, voir 6.2).

Suite `MediaType*` après intégration : `Tests run: 53, Failures: 0, Errors: 0, Skipped: 7` (10 originaux + 44 générés).
Couverture de `MediaType` (tests `MediaType*`) : lignes 81 % → 81 % (126/156, mesure pitest), car les tests générés
exercent surtout des méthodes déjà couvertes ; les lignes manquantes sont celles de `set`, `union` et de la branche
« charset d'abord » de `parse`, jamais soumises au modèle ou jamais exercées.

### 5.4 `FilenameUtils` (12 méthodes, 7 soumises au modèle)

Statistiques : [tache2-rapports/03-chatunitest/FilenameUtils/stats.md](tache2-rapports/03-chatunitest/FilenameUtils/stats.md).
Les 5 méthodes privées (`getPrefixLength`, `removeProtocol`, `getEmbeddedPath`, `getEmbeddedName`, `lookupExtension`)
sont écartées par le filtre de ChatUniTest.

| Indicateur | Valeur |
|---|---|
| Méthodes soumises au modèle | 7 / 12 (`normalize`, `getName`, `getSuffixFromPath`, `getSanitizedEmbeddedFileName`, `getSanitizedEmbeddedFilePath`, `resolveWithin`, `calculateExtension`) |
| Appels au modèle | 18 (36 001 jetons de prompt, 14 135 de réponse), environ 30 minutes |
| Méthodes avec un test exporté | **5 / 7** (4 au tour 0 ; `getName` après un tour de réparation d'erreur d'exécution) |
| `getSanitizedEmbeddedFileName` (6 appels, aucun test) | tentative 0 : erreur d'exécution Mockito (`PotentialStubbingProblem` : la classe concrète `Metadata` est mockée en mode strict), puis **`Exceed max prompt tokens … Skipped`** (le prompt de réparation, code + erreurs + méthode de 60 lignes, dépasse les 5500 jetons autorisés), puis constante inventée `TikaCoreProperties.EMBEDDED_NAME` ; tentative 1 : 3 tours de `MissingMethodInvocationException` (`when()` appliqué à un objet qui n'est pas un mock) |
| `calculateExtension` (6 appels, aucun test) | tentative 0 : accesseurs inventés `getMIME_TYPES()`, `getEmbeddedDocumentUtil()`, puis `ReflectionTestUtils` (Spring) ; tentative 1 : `new MimeType("image/png")` alors que le constructeur prend un `MediaType` (3 tours identiques) |
| Corrections manuelles (`calculateExtension`, tentative 1 reprise) | 4 : constructeur `MimeType(MediaType.parse(…))` sur 3 lignes, et retrait du montage `@Mock MimeTypes` / `@InjectMocks` (le champ `MIME_TYPES` est statique : le mock n'est jamais injecté, et ses stubs inutilisés font échouer chaque test en mode strict) |

**Résultat réel à l'exécution** (`mvn -pl tika-core test -Dtest='FilenameUtils*'`) :

| | Fichiers | Méthodes de test | Réussissent | Échouent (désactivées) |
|---|---|---|---|---|
| Tests exportés tels quels | 5 | 25 | 16 | 9 (36 %), dont 2 dépendent de la plateforme |
| Test corrigé à la main (`calculateExtension`) | 1 | 5 | 1 | 4 |
| **Total généré (configuration 8k)** | **6** | **30** | **17 (57 %)** | **13 (43 %)** |
| Test de la seconde configuration (16k / 10000 / 2048, voir ci-dessous) | 1 | 22 | 22 | 0 |
| **Total généré** | **7** | **52** | **39 (75 %)** | **13 (25 %)** |

Deux des trois échecs de `resolveWithin_5_0` ne sont pas des oracles faux mais des oracles **dépendants de la
plateforme** : les messages attendus contiennent `/etc/passwd` et `/tmp` avec le séparateur `/`, alors que sous
Windows `Path.normalize()` produit `\etc\passwd`. Ces deux tests passeraient sous Linux (donc dans GitHub Actions) ;
nous les avons annotés `@DisabledOnOs(WINDOWS)` plutôt que `@Disabled`, afin qu'ils s'exécutent dans le CI.

Suite `FilenameUtils*` après intégration : `Tests run: 62, Failures: 0, Errors: 0, Skipped: 13` (10 originaux +
52 générés). Couverture de `FilenameUtils` (tests `FilenameUtils*`, mesure pitest) : 87 % → 88 % de lignes (154/175).

**Seconde configuration pour la méthode sans test.** Comme `getSanitizedEmbeddedFileName` avait buté sur la limite de
prompt, nous l'avons relancée seule (`mvn … chatunitest:method -DselectMethod=FilenameUtils#getSanitizedEmbeddedFileName`)
avec Ollama relancé en `OLLAMA_CONTEXT_LENGTH=16384` :

- *Essai 1* (contexte 16k, mais budget ChatUniTest inchangé à 5500/1024 jetons : la valeur du `pom.xml` l'emportait
  sur `-DmaxPromptTokens`) : 6 appels, aucun test exporté. Les trois réponses de la 1re tentative sont **tronquées à
  1024 jetons** (`responseToken = 1024`) ; la 2e tentative échoue sur un import manquant, puis Mockito, puis de nouveau
  `Exceed max prompt tokens`. Le seul code qui compile (tentative 0, tour 2) a **12 méthodes de test sur 12 en échec**
  (9 attendent `test.txt.txt` pour un fichier `test.txt`, 2 mockent `Metadata` avec des stubs non appelés, 1 passe
  `null` et attend une valeur) : nous ne l'avons pas intégré ; son code est archivé dans
  [03-chatunitest/FilenameUtils-contexte16k/](tache2-rapports/03-chatunitest/FilenameUtils-contexte16k/).
- *Essai 2* (contexte 16k, `-Dchatunitest.maxPromptTokens=10000 -Dchatunitest.maxResponseTokens=2048`, après avoir
  rendu ces deux paramètres surchargeables dans le `pom.xml`) : 4 appels. La 1re tentative échoue trois fois sur la
  même erreur Mockito (`PotentialStubbingProblem` : mocks stricts de `Metadata` avec des stubs non appelés) ; la 2e
  tentative produit au tour 0 un test de **22 méthodes qui compilent et passent toutes**
  ([`FilenameUtils_getSanitizedEmbeddedFileName_3_1_Test`](tika-core/src/test/java/org/apache/tika/io/FilenameUtils_getSanitizedEmbeddedFileName_3_1_Test.java),
  intégré tel quel). La réponse fait exactement 2048 jetons (le nouveau plafond) et le test est très répétitif : huit
  variantes de `C:\path`, `C:/path`, `C::/path` qui donnent toutes `file.txt`. Le budget de jetons était donc bien le
  facteur limitant pour cette méthode, mais le gain (une méthode déjà couverte à 94 % par les tests originaux, un
  seul mutant supplémentaire tué, voir 8.3) reste faible.

Le même essai relancé par erreur sur `getSanitizedEmbeddedFilePath` (déjà réussie en 8k) a produit un second test au
tour 0, non intégré (doublon).

### 5.5 Tri des tests IA pour l'intégration finale — étape 3

Les sections 5.1 à 5.4 décrivent la génération et l'intégration historiques
Saidana. Le 8 octobre 2026, les 193 méthodes intégrées ont été inventoriées avant
modification. Leurs fichiers à cet instant sont conservés séparément des sorties
brutes ChatUniTest, qui restent intactes dans `tache2-rapports/03-chatunitest/`.

| Classe | Conservées sans changement d'assertions | Corrigées humainement | Rejetées | Retenues et exécutées |
|---|---:|---:|---:|---:|
| `EndianUtils` | 59 | 17 | 21 | 76 |
| `MediaType` | 37 | 2 | 5 | 39 |
| `FilenameUtils` | 38 | 8 | 6 | 46 |
| **Total** | **134** | **27** | **32** | **161** |

Les corrections sont déduites des contrats, des entrées et des tests originaux,
avant la validation : poids des octets selon l'ordre endian, octets négatifs lus
comme des valeurs 0–255 par `ByteArrayInputStream`, arrêt UE7 sans continuation,
extensions MIME avec point et extraction du dernier segment sans assainissement
implicite. Par exemple, `12 34 56 78 9A BC DE F0` donne
`0xF0DEBC9A78563412L` en little-endian ; `image/png` donne `.png` ;
`getName("/home/user/documents/../report.pdf")` donne `report.pdf`.
Les entrées IA sont conservées, sauf la portabilisation du test de traversée
`resolveWithin` : `@TempDir` remplace `/tmp` et le message exact est construit
avec des objets `Path`, sans dépendre du séparateur Windows ou Unix.

Deux tests changent de nom pour décrire leur comportement réel : le prétendu
maximum UE7 vérifie en fait l'arrêt sur le premier octet `0x7F`, et
`invalid/type` est un type MIME syntaxiquement valide. Deux méthodes corrigées
nécessitent aussi la déclaration de `BufferUnderrunException` après passage à
un appel direct ; les tentatives de compilation échouées sont conservées.
Les deux corrections de `getUShortLE` retirent les blocs `catch` qui masquaient
les exceptions ; elles ne remplacent pas une assertion précise par une assertion
plus faible.

Les rejets sont explicites : onze attentes de `NullPointerException` sur un
`byte` primitif, candidats avec réflexion et erreurs multiples déjà couverts par
des tests directs, montages Mockito sans rapport avec la méthode, attentes
ambiguës hors du contrat, doublon de traversée et méthode au corps vide. Ces
candidats sont retirés de `src/test/java`, conservés dans les preuves et ne sont
plus présentés comme des tests acceptés désactivés.

Les **161 cas IA retenus passent**, sans échec, erreur ni désactivation, puis
l'ensemble de `tika-core` passe avec **966 cas, dont 964 réussis et deux déjà
désactivés par Tika**. Les objectifs Maven directs utilisés ne valident pas le
cycle complet ni Checkstyle ; l'harmonisation de l'étape 4 reste nécessaire.
La mesure PIT n'est pas relancée ici : les gains historiques ne sont pas
réattribués à cette sélection corrigée.

Le [registre individuel](tache2-rapports/final-integration/etape-03/decisions.md)
et son JSON contiennent les motifs et les méthodes avant/après. Les
[commandes et preuves](tache2-rapports/final-integration/etape-03/commands.md)
incluent les XML Surefire, sources testées et empreintes.

### 5.6 Harmonisation Maven et PIT — étape 4

La construction `./mvnw -B -pl tika-core -am clean install` réussit dans une
copie sur le disque interne : **966 cas dans tika-core, 964 réussis, deux
désactivations préexistantes, aucun échec ni erreur**, et **8/8 tests réussis**
dans le processeur d’annotations. Checkstyle ne signale aucune erreur dans ces
deux modules. Les contrôles applicables du cycle Maven sont conservés, sans
`-Pfast` ni `-DskipTests`. Les imports explicites et le formatage Spotless des
tests IA ne changent ni leurs données ni leurs assertions. Les deux tentatives
intermédiaires échouées restent archivées.

ChatUniTest 2.1.1 est isolé dans le profil `chatunitest`. Le starter 1.5.0 est
retiré ; les tests acceptés utilisent JUnit 6.1.3 et Mockito 5.23.0 avec les
versions du parent. Le paramètre de génération séquentielle est corrigé en
`enableMultithreading=false`. Aucun modèle n’est exécuté pendant cette étape.

Le profil `mutation` fixe PIT **1.30.0**, son connecteur JUnit **1.2.3**, les
classes `EndianUtils`, `MediaType`, `FilenameUtils`, et le périmètre de tests
`org.apache.tika.*`. Onze opérateurs sont explicités, avec deux threads,
`timeoutFactor=1.25`, `timeoutConstant=4000`, sans historique. La même
configuration et les mêmes tests originaux devront servir aux trois états,
avec une sélection physique des sources IA et manuelles puis une construction
propre avant chaque mesure. Les chiffres historiques du rapport ne sont pas
ceux de cette configuration finale.

Les [commandes et paramètres archivés](tache2-rapports/final-integration/etape-04/commands.md)
comprennent le POM effectif, l’arbre des dépendances, les XML Surefire, Checkstyle
et JaCoCo. Aucun nouveau PIT n’est exécuté ici. L’étape 5 mesurera A/B/C avec
`./mvnw -B -pl tika-core -Pmutation org.pitest:pitest-maven:mutationCoverage`.

## 6. Analyse critique des tests générés

### 6.1 `EndianUtils` : oracles de l'IA contre oracles écrits à la main

Les tests originaux de `EndianUtilsTest` (4 tests, écrits par les développeurs de Tika) choisissent des données
dont la valeur attendue est **justifiée** : `readUE7` avec `{0x84, 0x1e}` → 542 (exemple du format), `readIntME` avec
`{0x0b, 0x0a, 0x0d, 0x0c}` → `0x0a0b0c0d` en citant la page de référence sur l'endianness, et un cas d'underrun avec
3 octets au lieu de 4. Ils sont peu nombreux mais chaque oracle est exact et spécifique.

Les 97 méthodes de test générées ont la **bonne intention** dans la grande majorité des cas (valeur nominale,
décalage, underrun, valeurs limites 0x00/0xFF, octets « négatifs »), mais l'oracle est faux dans 39 % des cas.
Catégories observées (exemples tirés des fichiers intégrés, les méthodes citées sont celles désactivées) :

1. **Confusion little-endian / big-endian** (la plus fréquente). `getShortLE(new byte[]{0x01, 0x02})` : attendu 258
   (`0x0102`), obtenu 513 (`0x0201`) ; `getUShortBE_19_0` attend 256 pour `{0x00, 0x01}` ; `getUIntLE_24_0` attend 1
   pour `{0x01, 0x00, 0x00, 0x00}`… alors que `getIntLE_20_0` fait exactement le bon calcul (`0x04030201`). Le modèle
   connaît le concept mais se trompe une fois sur deux dans son application.
2. **Mauvaise compréhension de `InputStream.read()`**. Le modèle croit que des octets négatifs provoquent un
   `BufferUnderrunException` (`readShortLE_0_1#testReadShortLEWithNegativeBytes` avec `{-1, -2}`,
   `readIntLE_6_1`, `readIntBE_7_1`), alors que `read()` renvoie 255 et 254. Il attend aussi une exception après
   avoir simulé « 0x12, 0x34, puis -1 » pour une lecture de **deux** octets (`readShortBE_1_1#testReadShortBE`).
3. **Erreurs d'arithmétique multi-octets**. `readLongLE_9_1` attend `0xFEDCBA9876543210` pour les octets
   `12 34 56 78 9A BC DE F0` (valeur correcte : `0xF0DEBC9A78563412`) ; `readIntME_8_1` attend `0x12345678` pour
   `12 34 56 78` (middle-endian : `0x34127856`) ; `readLongBE_10_1` attend -2 pour `FF FE FD … F8`.
4. **Tests absurdes**. `ubyteToInt_29_1` contient 16 tests dont 11 attendent une `NullPointerException` en
   passant… un `byte` primitif (`testUbyteToIntNull`, `testUbyteToIntEmpty`, `testUbyteToIntLarge`, …, tous avec le
   même appel `ubyteToInt((byte) 0)`). `readUE7_11_0#testReadUE7_MaxValue` attend `Long.MAX_VALUE` pour six octets
   `0x7F`, alors que le premier octet sans bit de continuation termine la lecture (résultat 127).
5. **Réflexion inutile et oracle d'exception incohérent**. `getIntBE_23_0` et `getShortBE_17_1` appellent une
   méthode **publique** par réflexion (`getPrivateMethod(...).invoke(...)`) puis attendent
   `ArrayIndexOutOfBoundsException` ; la réflexion enveloppe l'exception dans `InvocationTargetException`, donc le
   test échoue même si l'intention (tableau vide, décalage négatif) est bonne.
6. **Vérifications peu spécifiques ou redondantes**. Plusieurs fichiers testent la surcharge voisine plutôt que la
   méthode focale (`readShortBE_1_1` teste surtout `readUShortBE`), ou répètent le même cas sous des noms différents.
   À l'inverse, nous n'avons trouvé aucun oracle tautologique (valeur attendue recalculée à partir de l'entrée).

Points positifs : lorsque l'oracle est juste, les tests générés sont **plus spécifiques** que ce qu'un développeur
pressé écrirait : octets tous distincts (`01 02 03 04 05 06 07 08`) qui vérifient chaque position, cas 0x00 et 0xFF,
vérification signée/non signée (`getUIntBE_27_0#testGetUIntBEWithNegativeNumber`), underrun à chaque longueur.
Ce sont précisément ces tests qui tuent les mutants arithmétiques (section 8.1).

Bilan pour cette classe : l'IA produit rapidement un **squelette de cas de test pertinent** (quoi tester, avec quelles
données), mais l'**oracle doit être vérifié un par un par un humain** ; un test généré accepté sans relecture a plus
d'une chance sur trois d'être faux, et ChatUniTest 2.1.1 ne filtre pas ces faux oracles par défaut.

### 6.2 `MediaType` : oracles de l'IA contre oracles écrits à la main

Les 10 tests originaux de `MediaTypeTest` sont des tests de **spécification** : chaque oracle est une chaîne
canonique justifiée par la RFC 2045 ou par un ticket Tika cité en commentaire (TIKA-121, TIKA-349 : `;;`, charset
entre guillemets, « charset d'abord »). Ils se concentrent sur la normalisation (`"text/plain; charset=UTF-8"` pour
des entrées en majuscules, avec espaces, avec guillemets).

Les 44 méthodes générées :

1. **Oracles corrects et spécifiques (majoritaires).** `parse_7_0` couvre 14 cas (null, chaîne vide, espaces,
   paramètres multiples, guillemets, `;;`, `==`) avec des valeurs attendues exactes ; `equals_18_0` vérifie l'égalité,
   la différence sur un paramètre, la différence avec/sans paramètre, `null` et un `Object` quelconque : c'est plus
   complet que le test original, qui ne teste pas `equals` du tout.
2. **Oracles faux par hypothèse erronée sur le contrat.** `compareTo_20_0` attend `-1`/`1` alors que
   `String.compareTo` renvoie la différence des caractères (`-14` pour `json` vs `xml`) ; le contrat de `Comparable`
   ne garantit que le signe. `parse_7_0#testParseInvalidType` suppose que `"invalid/type"` est rejeté (il est
   syntaxiquement valide). `audio_1_1` attend `IllegalArgumentException` pour le sous-type `"invalid"`.
3. **Oracle « raisonnable » mais contraire à l'implémentation.** `text_3_1` attend `null` pour `MediaType.text(null)` ;
   Tika renvoie en réalité le type `text/null`, parce que `text()` concatène sans vérifier. L'IA a ici encodé une
   attente défendable que le code ne respecte pas : ce test échoue, mais il met le doigt sur un comportement douteux
   de Tika (aucune validation de l'argument des fabriques `text()`, `image()`, …). C'est le seul cas, sur les trois
   classes, où un échec de test généré nous paraît révéler un défaut potentiel du code plutôt que du test.
4. **Hors sujet.** `application_0_0` ne teste jamais `application()` : ses quatre tests portent sur `getType`,
   `getSubtype`, `getBaseType`, `getParameters` (ce qui, par hasard, est utile puisque ChatUniTest avait écarté ces
   accesseurs).
5. **Redondance.** `hasParameters_15_0` compte 13 tests pour une méthode d'une ligne ; 11 d'entre eux construisent
   une map non vide de différentes façons et vérifient `true`. Lisible, mais sans valeur ajoutée après le 2e cas.
6. **Mauvais usage des outils.** Réflexion pour appeler des méthodes publiques (`audio_1_1` : `invoke(mediaType, null)`
   déclenche `IllegalArgumentException` du mécanisme de réflexion, pas de la méthode testée) ; mocks Mockito inutiles
   (`hashCode_19_1`, `image_2_1` : `@Mock MediaType` jamais utilisé, qui fait échouer le test en mode strict) ;
   API inventée (`image(String, Map)`).
7. **Données peu plausibles.** `equals_18_0` construit `new MediaType("text/plain", "utf-8", …)` : un type contenant
   une barre oblique et un sous-type qui est un charset. Le test passe, mais ne ressemble à aucun type MIME réel.

Comparés aux tests manuels, les tests générés pour cette classe sont donc **plus larges** (plus de méthodes, plus de
cas limites) mais **moins sûrs** : chaque oracle a dû être vérifié, et les méthodes les plus intéressantes pour la
mutation (`getBaseType`, `union`, `set`, `isSimpleName`) n'ont pas été traitées par l'outil.

### 6.3 `FilenameUtils` : oracles de l'IA contre oracles écrits à la main

`FilenameUtilsTest` (10 tests originaux) est le test manuel le plus riche des trois : `testEmbeddedFileNames` et
`testEmbeddedFilePaths` alignent chacun une trentaine de paires entrée/sortie choisies pour des **attaques réelles**
(`../../my_ppt.ppt`, `C:\a/b/c/..the quick brown fox.xlsx`, `https://tika.apache.org/...`, noms de 80 caractères),
avec l'oracle exact du nom assaini. Les développeurs connaissent les cas pathologiques (zip slip, préfixes Windows,
protocoles) et les testent directement.

Les 30 méthodes générées :

1. **Oracles exacts quand le code est lisible.** `normalize_0_0` énumère chaque caractère réservé (`* : < > | " '`)
   avec son code `%XX` correct : les constantes sont dans la classe. `getSanitizedEmbeddedFilePath_4_0` (7 tests,
   6 justes) couvre `null`, blanc, chemin long, caractères réservés, protocole, avec des oracles corrects.
2. **Oracles faux par ignorance du contrat réel.** `getName_1_0` suppose que `getName("…/../report.pdf")` renvoie
   `""` (la méthode ne regarde que le dernier segment : `report.pdf`), que `getName("…/report?pdf")` normalise le `?`
   (c'est `normalize` qui le fait) et que `getName("…/.abcde")` renvoie `""` (confusion avec la règle d'extension de
   `getSuffixFromPath`). `getSuffixFromPath_2_0` suppose qu'une extension de quatre lettres (`.txtx`) est refusée :
   la règle du code est « moins de 6 caractères point compris ». `calculateExtension` attend `"png"` sans le point.
   Dans tous ces cas l'intention est bonne, l'oracle est une **supposition** sur une convention plutôt qu'une
   lecture du code.
3. **Oracles dépendants de l'environnement.** `resolveWithin_5_0` construit ses attentes avec `/tmp` et `/etc/passwd`
   et compare le **message d'exception** mot pour mot : fragile (le message n'est pas une spécification) et
   non portable (séparateurs Windows). Un test écrit à la main vérifierait le type d'exception et l'appartenance au
   répertoire, pas la phrase.
4. **Mockito à contre-emploi.** La classe `Metadata` est concrète et triviale à instancier ; le modèle la mocke
   systématiquement (`when(metadata.get(TikaCoreProperties.X)).thenReturn(...)` pour les cinq propriétés, à chaque
   test), ce qui alourdit les tests, les couple à l'ordre de repli interne de `getEmbeddedPath`, et provoque les
   échecs `PotentialStubbingProblem` / `MissingMethodInvocationException` qui ont fait échouer `getSanitizedEmbeddedFileName`.
   De même `@InjectMocks` sur une classe utilitaire statique (`calculateExtension`).
5. **API hallucinée.** `TikaCoreProperties.EMBEDDED_NAME`, `getMIME_TYPES()`, `getEmbeddedDocumentUtil()`,
   `new MimeType(String)` : le modèle complète des noms plausibles sans vérifier qu'ils existent ; comme pour
   `BufferUnderrunException`, le contexte fourni par ChatUniTest ne contient pas la signature des classes
   dépendantes utilisées (ici `Metadata`, `MimeType`).
6. **Limite de contexte.** C'est la seule classe où un tour a été abandonné faute de place dans le prompt
   (`Exceed max prompt tokens`) : avec un contexte de 8k jetons, les méthodes de 50 lignes et plus ne laissent pas
   assez de place pour le code du test fautif **et** le message d'erreur lors de la réparation.

Au total, pour cette classe, les tests générés ajoutent peu par rapport aux tests manuels : les méthodes publiques
étaient déjà bien couvertes (87 % de lignes) et les mutants vivants se trouvaient dans les méthodes privées et dans
des bornes de longueur, que l'IA n'a pas visées.

## 7. Analyse de mutation avec pitest

### 7.1 Configuration

```xml
<plugin>
  <groupId>org.pitest</groupId>
  <artifactId>pitest-maven</artifactId>
  <version>1.30.0</version>
  <dependencies>
    <dependency>
      <groupId>org.pitest</groupId>
      <artifactId>pitest-junit5-plugin</artifactId>
      <version>1.2.3</version>
    </dependency>
  </dependencies>
  <configuration>
    <targetClasses>
      <param>org.apache.tika.io.EndianUtils</param>
      <param>org.apache.tika.mime.MediaType</param>
      <param>org.apache.tika.io.FilenameUtils</param>
    </targetClasses>
    <targetTests>
      <param>org.apache.tika.io.EndianUtils*</param>
      <param>org.apache.tika.mime.MediaType*</param>
      <param>org.apache.tika.io.FilenameUtils*</param>
    </targetTests>
    <outputFormats><param>HTML</param><param>XML</param></outputFormats>
    <timestampedReports>false</timestampedReports>
    <exportLineCoverage>true</exportLineCoverage>
  </configuration>
</plugin>
```

Les mutateurs sont ceux du groupe `DEFAULTS` de pitest (CONDITIONALS_BOUNDARY, INCREMENTS, INVERT_NEGS, MATH,
NEGATE_CONDITIONALS, VOID_METHOD_CALLS, EMPTY_RETURNS, FALSE_RETURNS, TRUE_RETURNS, NULL_RETURNS, PRIMITIVE_RETURNS).

### 7.2 Score de mutation avec les tests originaux

```bash
mvn -pl tika-core org.pitest:pitest-maven:mutationCoverage \
    -DtargetTests=org.apache.tika.io.EndianUtilsTest,org.apache.tika.mime.MediaTypeTest,org.apache.tika.io.FilenameUtilsTest
```

Résultat (détails en [3.3](#33-mutants-vivants-avec-les-tests-originaux-pitest)) : 406 mutants, 159 tués, score global de
**39 %** (`EndianUtils` 18 %, `MediaType` 56 %, `FilenameUtils` 63 %).

### 7.3 Score de mutation avec les tests originaux + générés

Même commande, en limitant pitest à la classe et à ses tests (`-DtargetClasses=... -DtargetTests=org.apache.tika.io.EndianUtils*`),
après intégration des tests générés (méthodes à oracle faux désactivées, voir section 5).

| Classe | Mutants | Tués avant → après | Survivants (couverts) | Sans couverture | Score avant → après | Force des tests | Rapport |
|---|---|---|---|---|---|---|---|
| `EndianUtils` | 206 | 38 → **129** | 14 → 31 | 154 → 46 | **18 % → 63 %** | 73 % → 81 % | [05-pitest-EndianUtils-tests-generes](tache2-rapports/05-pitest-EndianUtils-tests-generes/index.html) |
| `MediaType` | 85 | 48 → **65** | 19 → 4 | 18 → 16 | **56 % → 76 %** | 72 % → 94 % | [08-pitest-MediaType-tests-generes](tache2-rapports/08-pitest-MediaType-tests-generes/index.html) |
| `FilenameUtils` | 115 | 73 → **75** | 20 → 18 | 22 → 22 | **63 % → 65 %** | 78 % → 81 % | [10-pitest-FilenameUtils-tests-generes](tache2-rapports/10-pitest-FilenameUtils-tests-generes/index.html) |
| **Total** | **406** | **159 → 269** | 53 → 53 | 194 → 84 | **39 % → 66 %** | | |

Note : la référence de la section 3.3 a été calculée en une seule exécution avec les trois classes de test originales ;
les exécutions par classe ci-dessus ne retiennent que les tests de la classe (`-DtargetTests=…MediaType*`). Un mutant
de `MediaType.hashCode` était tué dans la référence par `FilenameUtilsTest` (effet croisé via `MimeTypes`) et n'est
plus couvert ici ; il est traité par un test manuel (section 9.2). Le tableau global final de la section 1 est
calculé en une seule exécution avec toutes les classes de test.

Les tests générés **ne détectent pas tous les mutants** : pour `EndianUtils`, 77 mutants restent vivants (31 dans du
code couvert, 46 dans du code que les tests actifs n'exécutent pas) ; pour `MediaType`, 20 (4 couverts, 16 non
couverts, presque tous dans les méthodes que ChatUniTest a sautées) ; pour `FilenameUtils`, 40 (18 couverts, 22 non
couverts). La section 8 explique mutant par mutant ce qui
est détecté et pourquoi ; la section 9 décrit les tests manuels ajoutés pour les survivants.

### 7.4 Score de mutation avec les tests originaux + générés + manuels

| Classe | Mutants | Tués (originaux → + générés → + manuels) | Survivants | Score final | Couverture de lignes (pitest) | Rapport |
|---|---|---|---|---|---|---|
| `EndianUtils` | 206 | 38 → 129 → **204** | 2 (équivalents, voir 9.1) | **99 %** | 121/121 (100 %) | [07-pitest-EndianUtils-tests-manuels](tache2-rapports/07-pitest-EndianUtils-tests-manuels/index.html) |
| `MediaType` | 85 | 48 → 65 → **84** | 1 (équivalent, voir 9.2) | **99 %** | 156/156 (100 %) | [09-pitest-MediaType-tests-manuels](tache2-rapports/09-pitest-MediaType-tests-manuels/index.html) |
| `FilenameUtils` | 115 | 73 → 75 → **105** | 10 (équivalents, voir 9.3) | **91 %** | 171/175 (98 %) | [11-pitest-FilenameUtils-tests-manuels](tache2-rapports/11-pitest-FilenameUtils-tests-manuels/index.html) |
| **Total (une seule exécution, configuration par défaut du pom)** | **406** | **159 → 269 → 393** | 13 | **97 %** | 448/452 (99 %) | [12-pitest-final-toutes-classes](tache2-rapports/12-pitest-final-toutes-classes/index.html) |

Suites finales : `EndianUtils*` → `Tests run: 129, Failures: 0, Errors: 0, Skipped: 38` (4 originaux, 97 générés dont
38 désactivés, 28 manuels) ; `MediaType*` → `Tests run: 65, Failures: 0, Errors: 0, Skipped: 7` (10 originaux,
44 générés dont 7 désactivés, 12 manuels) ; `FilenameUtils*` → `Tests run: 77, Failures: 0, Errors: 0, Skipped: 13`
(10 originaux, 52 générés dont 13 désactivés, 15 manuels).

### 7.5 Mesures comparables de tache2-final — étape 5

Les trois états sont reconstruits dans des copies indépendantes, avec les mêmes
sources de production, POM, paramètres PIT et tests originaux. Les empreintes
confirment aussi l'identité du bytecode des classes cibles ; les XML contiennent
exactement le même ensemble de **406 mutants**. Les sources ne changent pas
pendant les constructions. Les anciennes expériences des sections 7.1 à 7.4
restent conservées à titre historique et ne représentent pas cette intégration.

| État | Tests ajoutés aux originaux | KILLED | TIMED_OUT | SURVIVED | NO_COVERAGE | Score KILLED/406 |
|---|---|---:|---:|---:|---:|---:|
| A | Aucun | 185 | 1 | 36 | 184 | 45,57 % |
| B | 161 cas IA retenus, corrections humaines tracées | 317 | 1 | 56 | 32 | 78,08 % |
| C | B + 56 cas manuels des deux travaux retenus | 396 | 1 | 7 | 2 | 97,54 % |

Le score global utilise les sommes des effectifs, sans assimiler un timeout à
une détection par assertion. Les journaux PIT affichent un compteur « Killed »
qui inclut ce timeout ; cela explique l'écart d'un mutant avec notre score.
Le nombre de survivants augmente de A à B parce que certains mutants auparavant
non couverts deviennent exécutés sans être détectés ; aucune détection acquise
n'est perdue. A vers B ajoute **132 nouveaux KILLED**, tous attribués par PIT à
une classe IA retenue ; B vers C en ajoute **79**, tous attribués à une classe
manuelle. Les identités et les tests tueurs sont archivés individuellement.

| Classe | Total | KILLED A | KILLED B | KILLED C | Score C |
|---|---:|---:|---:|---:|---:|
| EndianUtils | 206 | 38 | 163 | 204 | 99,03 % |
| MediaType | 85 | 68 | 72 | 84 | 98,82 % |
| FilenameUtils | 115 | 79 | 82 | 108 | 93,91 % |

Les trois cycles `clean install` et les trois exécutions PIT réussissent, avec
les contrôles applicables activés et zéro erreur Checkstyle. Surefire confirme
**749/910/966 cas core** dans A/B/C, dont **747/908/964 réussis** et les deux
mêmes désactivations préexistantes. Les 161 cas IA et les 56 cas manuels passent
sans désactivation. Chaque état exécute aussi huit tests réussis du processeur
d'annotations.

Les [commandes du protocole](tache2-rapports/final-integration/etape-05/commands.md)
et le [tableau détaillé](tache2-rapports/final-integration/etape-05/comparison.md)
renvoient aux trois archives séparées : sources exactes, manifestes SHA-256,
XML Surefire/Checkstyle/JaCoCo/PIT et journaux. Le lien détaillé entre chaque
nouveau mutant tué, l'entrée et l'assertion, ainsi que l'analyse des sept
survivants, deux non couverts et du timeout, restent l'objet de l'étape 6.
Aucune équivalence de mutant n'est déduite de ces seuls scores.

## 8. Mutants détectés par les tests générés

La comparaison mutant par mutant entre le rapport de référence et le rapport avec tests générés est produite par un
script (`pitdiff.py`, dans `tache2-rapports/`) et conservée dans
[05-pitest-EndianUtils-tests-generes/diff-vs-tests-originaux.txt](tache2-rapports/05-pitest-EndianUtils-tests-generes/diff-vs-tests-originaux.txt) :
pour chaque mutant, statut avant, statut après et test tueur.

### 8.1 `EndianUtils` : 91 mutants nouvellement tués, 31 survivants, 46 non couverts

Répartition des 129 mutants tués par mutateur : MATH 87, PRIMITIVE_RETURNS 22, NEGATE_CONDITIONALS 13,
INCREMENTS 6, CONDITIONALS_BOUNDARY 1.

**Pourquoi ces mutants sont détectés.**

- **`MATH` sur les lignes d'assemblage** (`(ch4 << 24) + (ch3 << 16) + (ch2 << 8) + ch1`, `b1 << 8`, `& 0xFF`, …) :
  pitest remplace un décalage à gauche par un décalage à droite, une addition par une soustraction, un ET par un OU.
  Ils sont tués par tout test qui compare le résultat à une **valeur exacte calculée sur des octets distincts** :
  avec `{0x01, 0x02, 0x03, 0x04}` → `0x04030201`, chaque octet occupe une position de bits différente, donc
  changer n'importe quel décalage ou n'importe quelle addition change le résultat. Exemple : les 11 mutants de
  `getIntLE(byte[], int)` sont tous tués par le seul test `getIntLE_20_0#testGetIntLE`. À l'inverse, un test
  avec des octets identiques (`{0, 0, 0, 0}` ou `{0xFF, 0xFF, 0xFF, 0xFF}`) ne distingue pas `<< 24` de `<< 16`.
- **`PRIMITIVE_RETURNS`** (la méthode renvoie 0) : tué dès qu'un test attend une valeur non nulle, ce que font les
  tests nominaux générés (`readUShortLE_2_1#testReadUShortLE` attend `0x3412`).
- **`NEGATE_CONDITIONALS`** sur `if ((ch1 | ch2 | …) < 0)` : le mutant lève `BufferUnderrunException` sur des
  données valides et ne la lève plus sur un flux tronqué. Il est tué soit par le test nominal (exception inattendue),
  soit par le test d'underrun (`readUShortLE_2_1#testReadUShortLEWithBufferUnderrun` avec un seul octet).
- **`INCREMENTS`** (`data[i++]` → `data[i--]`) dans `getIntLE`/`getIntBE` : le parcours du tableau repart en arrière
  et lit d'autres octets ; tué par les tests avec décalage et octets distincts (`getIntBE_22_0#testGetIntBEWithOffset`).
- **`MATH` « OR → AND » dans la condition d'underrun** : seul le **dernier** OU de l'expression est tué par les tests
  d'underrun générés, car ils tronquent toujours le flux sur le dernier octet (`thenReturn(0x12, 0x34, 0x56, -1)`) ;
  `((ch1 | ch2 | ch3) & ch4)` vaut alors `ch1|ch2|ch3 ≥ 0` et l'exception disparaît. Les OU précédents survivent (voir
  ci-dessous).

**Pourquoi 31 mutants couverts survivent.**

| Mutants | Ligne(s) | Raison | Détectable ? |
|---|---|---|---|
| 8 × `CONDITIONALS_BOUNDARY` `(… ) < 0` → `<= 0` | 64, 73, 92, 111, 130, 149, 168, 191 | Le mutant ne diffère que si l'OU des octets vaut **exactement 0**, c'est-à-dire si tous les octets lus sont `0x00`. Aucun test généré ne lit un flux entièrement nul. | Oui, avec des octets tous nuls (section 9) |
| 17 × `MATH` `\|` → `&` dans la condition d'underrun | 92 (2), 111 (3), 130 (3), 149 (1), 168 (2), 191 (6) | Pour qu'un ET diffère d'un OU il faut un `-1` **avant** le dernier octet et des octets valides après. Les tests générés tronquent le flux à la fin, ou le vident entièrement (`thenReturn(-1)` : tous les termes valent -1 et le ET reste négatif). | Oui, avec un flux qui renvoie -1 puis des données (section 9) |
| 3 × `readUE7` (bornes `>= 0`, `< max`, et `read++` → `read--`) | 235, 246 | Les tests générés actifs (`{0x7F, 0x00}`, `{0x80}`) ne terminent jamais la lecture sur un octet `0x00`, et n'atteignent jamais la limite de 6 octets. | Oui (section 9) |
| 2 × `INCREMENTS` sur le dernier `data[i++]` de `getIntLE` / `getIntBE` | 362, 388 | La variable `i` n'est plus lue après ce dernier accès : `i++` ou `i--` ne change rien. **Mutant équivalent.** | Non |
| 1 × `CONDITIONALS_BOUNDARY` `read++ < max` | 235 | compté dans la ligne `readUE7` ci-dessus | |

**Pourquoi 46 mutants ne sont pas couverts.** Ils sont dans du code que seuls des tests **désactivés** (oracle faux)
exécutaient : la ligne de calcul de `readLongLE` (15 mutants, le seul test nominal `testReadLongLE` attendait une
mauvaise valeur), tout `readLongBE` (24 mutants, l'unique test généré regroupait trois cas dans une méthode dont
l'oracle du deuxième cas était faux), et les 7 surcharges à une ligne `get*(byte[])` dont le test généré a été
désactivé ou qui n'étaient appelées que par réflexion sur l'autre surcharge (7 `PRIMITIVE_RETURNS`).

### 8.2 `MediaType` : 18 mutants nouvellement tués, 4 survivants, 16 non couverts

Comparaison : [08-pitest-MediaType-tests-generes/diff-vs-tests-originaux.txt](tache2-rapports/08-pitest-MediaType-tests-generes/diff-vs-tests-originaux.txt).

**Pourquoi ces 18 mutants sont détectés** (le test tueur est celui que pitest a enregistré) :

- `image` et `video` : `NULL_RETURNS` sur la fabrique → tués par les tests nominaux `image_2_0#testImageMethod`
  (`assertEquals("image/png", …toString())` : un `null` provoque une `NullPointerException`) et `video_4_0`.
- `parse` ligne 277 (`NEGATE_CONDITIONALS` sur `TYPE_PATTERN.matcher(string).matches()`) : tué par
  `image_2_0#testImageMethod`, qui construit `new MediaType(MediaType.image("png"), params)` ; `getBaseType()`
  passe par `parse("image/png; charset=UTF-8")`, forme avec paramètres qui emprunte la branche regex.
- `isSimpleName` (4 mutants : deux `NEGATE_CONDITIONALS`, une borne, un `TRUE_RETURNS`) : tués indirectement par
  `parse_7_0#testParseParametersWithEmptyValues` et `image_2_0` ; en forçant `isSimpleName` à répondre « simple »
  pour un nom contenant `;` ou `=`, le mutant fait prendre le chemin « cache » à une chaîne avec paramètres, qui
  n'est alors plus analysée : `toString()` et `getParameters()` ne correspondent plus aux valeurs attendues.
- `union` lignes 349-350 (`NEGATE_CONDITIONALS` sur `a.isEmpty()`, `EMPTY_RETURNS`) : tués par
  `image_2_0#testImageMethod` qui ajoute une map non vide à un type **sans** paramètre et vérifie
  `getParameters().get("charset")`.
- `getType` / `getSubtype` (`EMPTY_RETURNS`, `MATH` sur `slash + 1`) : tués par les assertions exactes
  `assertEquals("application", getType())`, `assertEquals("plain", getSubtype())` des tests `application_0_0` et
  `parse_7_0#testParseSimpleType`.
- `hasParameters` (`NEGATE_CONDITIONALS`, `TRUE_RETURNS`) : tués par `hasParameters_15_0#testHasParametersWithEmptyParameters`
  (le cas `false`, que le test original ne vérifiait jamais).
- `equals` (3 mutants + 1 non couvert avant) : `testEqualsWithNull` tue la négation de `instanceof` (le mutant tente
  `(MediaType) null` puis `that.string` → `NullPointerException`) et le `return true` de la branche `else` ;
  `testEquals` tue `return true`/`return false` sur la comparaison des chaînes grâce aux trois paires
  égal / paramètre différent / sans paramètre.

**Pourquoi 4 mutants couverts survivent et 16 ne sont pas couverts.**

| Mutants | Ligne(s) | Raison | Détectable ? |
|---|---|---|---|
| `parse` `NULL_RETURNS` | 257 | La ligne est `return null;` (chaîne sans `/`) : pitest remplace `null` par `null`. **Mutant équivalent.** | Non |
| `isSimpleName` borne et négation | 288 | Les tests actifs n'utilisent aucun nom contenant un caractère **à la borne** des intervalles (`0`, `9`, `a`, `z`) ni ne vérifient l'effet observable de `isSimpleName` (mise en cache de l'instance). | Oui (9.2) |
| `getBaseType` négation | 367 | Les tests actifs appellent `getBaseType()` seulement sur des types **sans** paramètre et ne comparent que `getType()` : avec la condition inversée, `parse("application/json")` renvoie un objet égal, donc indétectable ainsi. | Oui, avec un type à paramètres et `assertSame` (9.2) |
| `application`, `audio`, `text` `NULL_RETURNS` | 181, 185, 193 | Non couverts : le test `application_0_0` ne teste pas `application()`, les tests `audio_1_1` et `text_3_1` sont désactivés (oracle faux). | Oui (9.2) |
| `set(MediaType...)`, `set(String...)` (4 mutants) | 211-234 | Non couverts : méthodes **sautées par ChatUniTest** (nom commençant par `set`). | Oui (9.2) |
| `union` lignes 351-357 (5 mutants) | 351-357 | Non couverts : la branche « deux maps non vides » n'est jamais exercée ; le constructeur `MediaType(MediaType, Map)` est sauté (constructeur). | Oui (9.2) |
| `parse` « charset d'abord » `NULL_RETURNS` | 278 | Non couvert : aucun test généré n'essaie la forme `charset=…; type/sous-type` documentée dans la javadoc. | Oui (9.2) |
| `getBaseType` `NULL_RETURNS` | 370 | Non couvert : jamais appelé avec paramètres. | Oui (9.2) |
| `hashCode`, `compareTo` `PRIMITIVE_RETURNS` | 425, 429 | Non couverts : les tests générés `hashCode_19_1` et `compareTo_20_0` sont désactivés. | Oui (9.2) |

### 8.3 `FilenameUtils` : 2 mutants nouvellement tués, 18 survivants, 22 non couverts

Comparaison : [10-pitest-FilenameUtils-tests-generes/diff-vs-tests-originaux.txt](tache2-rapports/10-pitest-FilenameUtils-tests-generes/diff-vs-tests-originaux.txt).

Les tests générés n'apportent **que deux mutants** de plus que les tests originaux : la négation de la première
condition de `getEmbeddedPath` (`if (!isBlank(EMBEDDED_RESOURCE_PATH))`, ligne 344), tuée par
`getSanitizedEmbeddedFilePath_4_0#testGetSanitizedEmbeddedFilePath_withLongPath`, et celle de la première condition
de `getEmbeddedName` (ligne 365), tuée par le test de la seconde configuration
`getSanitizedEmbeddedFileName_3_1#testGetSanitizedEmbeddedFileName_withPathContainingProtocol`. Ces tests mockent
`Metadata` et ne renseignent **qu'une** propriété ; quand la condition est inversée, la méthode saute cette propriété,
retombe sur les autres (toutes `null`) et renvoie `null` au lieu du nom attendu. Les tests originaux, eux,
renseignent toujours `RESOURCE_NAME_KEY` **et** `EMBEDDED_RESOURCE_PATH` avec la même valeur : inverser l'une des
conditions fait simplement passer à l'autre propriété, qui a la même valeur, et le mutant survit. C'est un exemple
où un oracle plus « étroit » (une seule propriété) détecte ce que l'oracle manuel laisse passer.

Pourquoi le reste survit :

| Mutants | Lignes | Raison | Détectable ? |
|---|---|---|---|
| 6 × négation et 8 × `EMPTY_RETURNS` dans `getEmbeddedPath` / `getEmbeddedName` | 348-380 | Méthodes **privées**, non soumises au modèle ; les tests originaux renseignent deux propriétés avec la même valeur (voir ci-dessus) ; aucun test ne renseigne `INTERNAL_PATH`, `EMBEDDED_RELATIONSHIP_ID` ou `ORIGINAL_RESOURCE_NAME` seuls. | Oui (9.3) |
| 4 × `CONDITIONALS_BOUNDARY` sur `> maxLength` | 189, 264, 265 | Il faut un nom (ou un chemin) dont la longueur vaut **exactement** `maxLength` ; les tests utilisent 50 avec des noms nettement plus courts ou plus longs. | Oui (9.3) |
| `CONDITIONALS_BOUNDARY` `n.length() - i < 6` | 134 | Aucun test n'utilise une extension de exactement 6 caractères point compris (`.abcde`). | Oui (9.3) |
| `EMPTY_RETURNS` sur les gardes `return null` | 174, 232, 250, 268 | Branches jamais atteintes : nom réduit à des espaces avant l'extension, chemin `.`, dernier segment réduit à une extension, chemin trop long mais nom assez court. | Oui (9.3) |
| 4 × négation dans `getPrefixLength` et `EMPTY_RETURNS` `return ".bin"`, `return null` de `lookupExtension` | 323, 402, 416 | Branche « `X:` » atteinte seulement par des noms de 1 ou 2 caractères que `commons-io` ne reconnaît pas ; aucun test ne fournit un type MIME inconnu ou invalide (tous utilisent `.docx`/`.xlsx`). | Oui (9.3) |
| 3 × négation dans `resolveWithin` | 305, 308 | Les tests (originaux et générés) n'utilisent que des chemins **inexistants** (`base/isa`, `/tmp/...`) : la seconde vérification (`Files.exists`, `toRealPath`) n'est jamais exécutée. | Oui pour 2 d'entre eux (9.3) |
| `CONDITIONALS_BOUNDARY` `prefixLength > 0` → `>= 0` | 156, 215, 320 | Quand `prefixLength` vaut 0, `substring(0)` ne change rien et `return 0` équivaut à continuer. **Équivalents.** | Non |
| Négation de `prefixLength > 0` (nom de fichier) | 156 | Ne pas retirer le préfixe (`C:\`, `~/`, `//serveur/partage/`) ne change pas le **nom** : `:` et `\` sont ensuite remplacés par `/` et `getName` garde le dernier segment. Équivalent pour toute entrée valide (voir la note sur `~` en 9.3). | Non |
| `EMPTY_RETURNS` sur `return null` ligne 185 et 259 | 185, 259 | Garde inatteignable : un `namePart` non vide ne peut pas devenir vide après `replaceAll` (les remplacements produisent `_`) et `trim`. Code mort, **équivalents.** | Non |
| 2 × `CONDITIONALS_BOUNDARY` et `PRIMITIVE_RETURNS` dans `getPrefixLength` | 323, 324 | `return 2` n'est jamais exécuté : toute chaîne `X:` est déjà reconnue par `commons-io` ; rendre la condition plus stricte ne change rien. **Équivalents** (code mort). | Non |
| Négation de `Files.exists(normalizedDir)` | 305 | Ne diffère que si le chemin résolu existe sans que le répertoire existe (impossible) ou en présence de liens symboliques sortant du répertoire, que nous ne pouvons pas créer de façon portable dans un test. Considéré **équivalent** dans notre environnement. | Non |

## 9. Tests supplémentaires écrits à la main

Chaque test manuel vise un ou plusieurs mutants survivants identifiés à la section 8. Les fichiers portent le
suffixe `ManualTest` et chaque méthode y est commentée (intention, données, oracle). Les rapports pitest « après
tests manuels » contiennent la comparaison mutant par mutant avec le rapport « tests générés »
(`diff-vs-tests-generes.txt`), qui indique le test tueur de chaque mutant.

### 9.1 `EndianUtils` : [EndianUtilsManualTest.java](tika-core/src/test/java/org/apache/tika/io/EndianUtilsManualTest.java) (28 tests)

Résultat : les 29 mutants survivants détectables sont tués, les 46 mutants non couverts sont couverts et tués ;
il reste exactement les 2 mutants équivalents. Score : 63 % → **99 %** (204/206).

| Test(s) | Intention (comportement testé) | Motivation des données | Oracle (comment la valeur attendue est déterminée) | Mutants tués |
|---|---|---|---|---|
| `readUShortLE_allZeroBytes_returnsZero`, `readUShortBE_…`, `readUIntLE_…`, `readUIntBE_…`, `readIntLE_…`, `readIntBE_…`, `readIntME_…`, `readLongLE_…`, `readLongBE_allZeroBytes_returnsZero` (9 tests) | Un flux qui contient exactement le nombre d'octets requis, tous à `0x00`, est lu sans erreur et vaut 0. | C'est le seul cas où `(ch1 \| ch2 \| …)` vaut exactement 0 : il sépare la condition d'underrun `< 0` de sa mutation `<= 0`. Aucun test existant n'utilisait des octets tous nuls. | Des octets tous nuls représentent la valeur 0 dans n'importe quel ordre d'octets ; comme le flux n'est pas tronqué, aucune exception ne doit être levée. | 9 × `CONDITIONALS_BOUNDARY` (lignes 64, 73, 92, 111, 130, 149, 168, 191, 217) |
| `readUIntLE_eofOnFirstByteThenData_throwsUnderrun`, `readUIntBE_…`, `readIntLE_…`, `readIntBE_…`, `readIntME_…`, `readLongLE_…`, `readLongBE_eofOnFirstByteThenData_throwsUnderrun` (7 tests) | Si **n'importe quelle** lecture renvoie -1, même la première, la méthode signale un underrun, quels que soient les octets suivants. | Un flux maison (`sequence(-1, 1, 2, 3, …)`) renvoie -1 puis des octets valides. Avec `ch1 = -1`, chaque OU remplacé par un ET rend l'expression positive, ce que les tests générés (qui tronquent toujours la fin du flux) ne pouvaient pas détecter. Ce scénario est réaliste : un `FileInputStream` sur un fichier en cours d'écriture renvoie -1 puis de nouvelles données. | Le contrat de la méthode (javadoc : « if the stream cannot provide enough bytes ») impose `BufferUnderrunException`. | 17 × `MATH` « OR → AND » (lignes 92, 111, 130, 149, 168, 191) + 7 mutants de la ligne 217 (`readLongBE`, non couverte avant) |
| `readLongLE_distinctBytes_assemblesLittleEndian`, `readLongBE_distinctBytes_assemblesBigEndian` | Assemblage correct des 8 octets d'un `long` dans les deux ordres, y compris avec le bit de signe à 1. | Octets tous distincts `01…08` : chaque position de bits (0 à 56) est observable, donc tout décalage ou toute addition mutée change le résultat ; `F0 DE … 12` / `12 34 … F0` vérifie le cast `(long)` des octets de poids fort (sans lui, le résultat serait tronqué) ; `FF × 8` vérifie la valeur -1. | Composition explicite : LE → `0x0807060504030201`, BE → `0x0102030405060708`, et la même valeur `0x123456789ABCDEF0` lue dans les deux ordres. | 15 `MATH`/`PRIMITIVE_RETURNS` de la ligne 195 et 16 de la ligne 221 (non couvertes avant) |
| `readLongBE_sevenBytesOnly_throwsUnderrun` | Underrun sur le dernier des 8 octets. | 7 octets valides puis fin de flux : seul `ch8` vaut -1. | Exception attendue par contrat. | 1 × `MATH` (dernier OU de la ligne 217) |
| `readUE7_continuationThenZeroByte_returns128` | Lecture d'une valeur sur deux octets dont le dernier est `0x00`. | `{0x81, 0x00}` : premier octet avec bit de continuation (valeur 1), second octet exactement 0. Un octet final nul distingue `i >= 0` de `i > 0` dans la condition de boucle et `i < 0` de `i <= 0` dans le test d'erreur. | `(1 << 7) + 0 = 128`, par la définition du format (7 bits utiles par octet, poids fort d'abord). | 2 × `CONDITIONALS_BOUNDARY` (lignes 235 et 246) |
| `readUE7_stopsAfterSixContinuationBytes` | La lecture s'arrête après 6 octets (`max = 6`) même si le bit de continuation est encore à 1. | 6 octets `0x81` suivis d'un 7e octet `0x01` : un 7e tour de boucle ajouterait `0x01` et changerait la valeur. | `v` vaut `((((((1 << 7 \| 1) << 7 \| 1) …` = 34 630 287 489, calculé dans le test par la même récurrence et vérifié par une constante ; le 7e octet est consommé mais ignoré d'après le code. | `INCREMENTS` `read++` → `read--` et `CONDITIONALS_BOUNDARY` `< max` → `<= max` (ligne 235) |
| `getShortLE_singleArgOverload_readsFromOffsetZero`, `getUShortLE_…`, `getShortBE_…`, `getIntBE_…`, `getUIntLE_singleArgOverload_isUnsigned`, `getUIntBE_singleArgOverload_isUnsigned`, `getShortBE_withOffset_readsSignedBigEndian` (7 tests) | Les surcharges à un argument lisent à partir de l'indice 0 ; contraste signé / non signé. | Deux octets différents (`0x12`, `0x34`) pour distinguer LE et BE et obtenir une valeur non nulle ; `0xFF…` pour vérifier que `getShort*` renvoie -1 (signé) et `getUShort*`/`getUInt*` 65535 / 4 294 967 295 (non signé) ; `0x8000` pour le bit de signe d'un `short`. | Valeurs hexadécimales composées à la main selon l'ordre des octets ; -1 et 65535 sont les deux interprétations de `0xFFFF`. | 7 × `PRIMITIVE_RETURNS` (lignes 259, 280, 303, 314, 373, 399, 421) |

**Mutants équivalents (non tués, non tuables).** `getIntLE(byte[], int)` ligne 362 et `getIntBE(byte[], int)` ligne 388 :
`int b3 = data[i++] & 0xFF;` est la dernière utilisation de `i`. Remplacer `i++` par `i--` modifie une variable
qui n'est plus jamais lue ; le comportement observable est strictement identique, aucun test ne peut distinguer
le mutant de l'original.

### 9.2 `MediaType` : [MediaTypeManualTest.java](tika-core/src/test/java/org/apache/tika/mime/MediaTypeManualTest.java) (12 tests)

Résultat : 19 des 20 mutants restants sont tués ; il reste le mutant équivalent de `parse` ligne 257. Score :
76 % → **99 %** (84/85), couverture de lignes 100 %.

| Test | Intention (comportement testé) | Motivation des données | Oracle | Mutants tués |
|---|---|---|---|---|
| `factories_buildCanonicalTypeFromSubtype` | Les cinq fabriques `application/audio/text/image/video(subtype)` construisent `type/sous-type`, et renvoient l'instance mise en cache par `parse`. | Un sous-type usuel par fabrique (`json`, `mpeg`, `csv`, `png`, `mp4`), choisis différents pour qu'une fabrique qui en appellerait une autre soit détectée. | Chaîne canonique attendue lisible dans le code (`"application/" + type`) ; `assertSame` avec `parse("text/csv")` car les types simples sont mis en cache. | 3 × `NULL_RETURNS` (`application`, `audio`, `text`) |
| `setOfMediaTypes_ignoresNullAndDuplicates_andIsUnmodifiable` | `set(MediaType...)` ignore `null`, dédoublonne et renvoie un ensemble non modifiable. | `TEXT_PLAIN, null, TEXT_HTML, TEXT_PLAIN` : un `null` (branche `if (type != null)`), un doublon (sémantique d'ensemble) et deux types distincts (taille observable, 2). | Taille 2 et contenu exact d'après le code ; `UnsupportedOperationException` d'après la javadoc (« unmodifiable set »). | `NEGATE_CONDITIONALS` et `EMPTY_RETURNS` de `set(MediaType...)` |
| `setOfStrings_parsesEachString_andSkipsUnparsable` | `set(String...)` analyse chaque chaîne et ignore celles qui ne sont pas des types MIME. | `"text/plain", "not a media type", "text/html", "text/plain"` : une chaîne sans `/` (→ `parse` renvoie `null`, branche `if (mt != null)`), un doublon ; plus le cas sans argument. | Taille 2, contenu, non modifiable, ensemble vide pour aucun argument. | `NEGATE_CONDITIONALS` et `EMPTY_RETURNS` de `set(String...)` |
| `parse_charsetBeforeType_isAccepted` | Forme « charset d'abord » des serveurs web défaillants (TIKA-350), annoncée par la javadoc de `parse`. | `"charset=UTF-8; text/plain"` : ne correspond pas à `TYPE_PATTERN`, donc seule la branche `CHARSET_FIRST_PATTERN` peut l'accepter. | Forme canonique `text/plain; charset=UTF-8` (paramètres après le type, triés). | `NULL_RETURNS` ligne 278 |
| `parse_simpleNameWithBoundaryCharacters_isCached` | Un nom composé uniquement de caractères « simples », y compris ceux aux **bornes** des intervalles (`0`, `9`, `a`, `z`) et les spéciaux admis (`- + . _`), est reconnu par `isSimpleName` et mis en cache. | `"a0z9/x-0.9_a+z" + nombre unique` : chaque borne et chaque caractère spécial apparaît ; le suffixe unique (nanosecondes) évite qu'une exécution précédente dans le même JVM (pitest réutilise le JVM entre mutants, et `SIMPLE_TYPES` est statique) ait déjà mis la chaîne en cache. | L'effet observable de `isSimpleName` est l'identité d'instance (`assertSame` entre deux `parse` successifs), optimisation documentée par le champ `SIMPLE_TYPES` ; un mutant qui rejette une borne fait passer la chaîne par l'expression régulière, qui crée une instance à chaque appel. | `CONDITIONALS_BOUNDARY` et `NEGATE_CONDITIONALS` ligne 288 |
| `parse_nonSimpleName_isNotCached_butStillParsed` | Contre-épreuve : un nom non simple (majuscules) est analysé par l'expression régulière, normalisé, mais pas mis en cache ; un sous-type vide est rejeté. | `"Text/Plain"` deux fois ; `"text/"`. | `equals(TEXT_PLAIN)` mais `assertNotSame` ; `assertNull` pour `"text/"` (`VALID_CHARS` exige au moins un caractère). | (confirme l'oracle du test précédent) |
| `constructorWithParameters_mergesBothMaps_newValuesWin` | `union(a, b)` avec deux maps non vides : union des clés, la valeur ajoutée l'emporte. | `a = {charset=UTF-8, x=1}`, `b = {charset=ISO-8859-1, format=flowed}` : une clé propre à chaque côté (détecte la suppression de chaque `putAll`) et une clé en conflit (détecte l'ordre des `putAll`). | Valeurs attendues déduites de `putAll(a)` puis `putAll(b)` ; chaîne canonique triée par nom de paramètre. | `NEGATE_CONDITIONALS` 351, `EMPTY_RETURNS` 352 et 357, 2 × `VOID_METHOD_CALLS` 355-356 |
| `constructorWithEmptyMap_keepsBaseParameters` | `union(a, b)` avec `b` vide renvoie `a` tel quel. | Type de base avec deux paramètres, map vide ajoutée. | Égalité avec le type de base et paramètres inchangés. | `EMPTY_RETURNS` ligne 352 (`return a`) |
| `constructorWithCharset_addsCharsetParameterToBaseType` | Les constructeurs de commodité `(MediaType, Charset)` et `(MediaType, name, value)` ajoutent un paramètre. | `UTF_8` et `charset=ISO-8859-1` sur des types sans paramètre. | Chaîne canonique `text/plain; charset=UTF-8`. | (couverture des constructeurs sautés par ChatUniTest) |
| `getBaseType_stripsParameters_andReturnsSelfWhenNone` | `getBaseType` retire les paramètres et renvoie `this` quand il n'y en a pas. | Un type avec `charset`, puis un type construit sans paramètre. | Javadoc (`text/plain` pour `text/plain; charset=utf-8`), `hasParameters()` faux, et `assertSame` pour la branche `return this`. | `NEGATE_CONDITIONALS` 367, `NULL_RETURNS` 370 |
| `hashCode_isConsistentWithEquals_andWithCanonicalString` | `hashCode` est cohérent avec `equals` et dérive de la chaîne canonique. | `new MediaType("text","plain")` et `parse("TEXT/PLAIN")` (égaux après normalisation) ; `TEXT_PLAIN` contre `TEXT_HTML`. | Contrat `Object` (égaux ⇒ même hash) ; égalité avec `"text/plain".hashCode()` d'après le code ; hash différents pour deux types différents (vrai ici, ce qui distingue le mutant « renvoie 0 »). | `PRIMITIVE_RETURNS` 425 |
| `compareTo_followsCanonicalStringOrder` | Ordre total cohérent avec les chaînes canoniques ; seul le signe est garanti. | `application/json`, `application/xml`, `application/json; charset=UTF-8` : un préfixe strict (`json` < `json; …`) et deux sous-types. | `< 0`, `> 0`, `== 0` sur le même type ; ordre d'un `TreeSet` égal à l'ordre lexicographique des chaînes. | `PRIMITIVE_RETURNS` 429 |

**Mutant équivalent (non tuable).** `parse` ligne 257 : l'instruction est `return null;` (chaîne sans `/`) et pitest
y applique « replaced return value with null », ce qui ne change rien.

### 9.3 `FilenameUtils` : [FilenameUtilsManualTest.java](tika-core/src/test/java/org/apache/tika/io/FilenameUtilsManualTest.java) (15 tests)

Résultat : les 30 mutants restants jugés détectables sont tous tués ; il reste exactement les 10 mutants classés
équivalents en 8.3. Score : 65 % → **91 %** (105/115), couverture de lignes 98 % (171/175 ; les lignes manquantes sont les deux `return null`
inatteignables, le `return 2` mort de `getPrefixLength` et le `throw` de la vérification par liens symboliques).

| Test | Intention (comportement testé) | Motivation des données | Oracle | Mutants tués |
|---|---|---|---|---|
| `embeddedName_eachPropertyAloneIsUsed` | Chacune des cinq propriétés de métadonnées suffit, seule, à produire un nom (ordre de repli de `getEmbeddedName`). | Une seule propriété renseignée par assertion, avec un nom qui porte son rang (`n1.txt` … `n5.txt`) ; un chemin pour `INTERNAL_PATH` et `EMBEDDED_RESOURCE_PATH` afin de vérifier que seul le dernier segment est gardé ; métadonnées vides → `null`. | Le nom attendu est celui de la propriété renseignée, assaini (dernier segment) ; `null` si rien n'est renseigné (javadoc). | 3 × négation (369, 373, 377) et 4 × `EMPTY_RETURNS` (370, 374, 378, 380) |
| `embeddedName_resourceNameWinsOverOtherProperties` | Priorité `RESOURCE_NAME_KEY` > `INTERNAL_PATH` > … > `ORIGINAL_RESOURCE_NAME`. | Plusieurs propriétés renseignées avec des **valeurs différentes**, contrairement aux tests originaux qui donnent la même valeur à deux propriétés (ce qui rendait les négations indétectables). | Ordre lu dans le code de `getEmbeddedName`. | (confirme 365-377) |
| `embeddedPath_eachPropertyAloneIsUsed`, `embeddedPath_resourcePathWinsOverOtherProperties` | Même chose pour `getEmbeddedPath` (ordre `EMBEDDED_RESOURCE_PATH` > `INTERNAL_PATH` > `RESOURCE_NAME_KEY` > `EMBEDDED_RELATIONSHIP_ID` > `ORIGINAL_RESOURCE_NAME`), en conservant le chemin relatif (`a/p1.txt`). | Idem ; `/a/p1.txt` vérifie aussi la suppression du `/` initial. | Idem. | 3 × négation (348, 352, 356), 4 × `EMPTY_RETURNS` (349, 353, 357, 359) |
| `fileName_exactlyMaxLength_isNotTruncated` | Le nom dont la partie sans extension fait **exactement** `maxLength` n'est pas tronqué ; un caractère de plus l'est. | `abcdefghij.txt` (10) avec `maxLength = 10`, puis 11 caractères. | Code : `namePart.length() > maxLength` ; forme tronquée `substring(0, maxLength - ext - 3) + "..." + ext` = `abc....txt`. | `CONDITIONALS_BOUNDARY` 189 |
| `filePath_exactlyMaxLength_keepsRelativePath` | Un chemin relatif dont la longueur totale vaut exactement `maxLength` est conservé entier ; un de moins et seul le nom est gardé. | `a/b/x.txt` (9 caractères) avec `maxLength` 9 puis 8. | Code lignes 261-270. | `CONDITIONALS_BOUNDARY` 264, `EMPTY_RETURNS` 268 |
| `filePath_tooLongPath_nameExactlyMaxLength_isNotTruncated` | Chemin trop long mais nom exactement à la limite : nom entier sans chemin ; nom d'un caractère de plus : tronqué. | `dir/abcdefgh.txt` avec `maxLength = 8` (nom = 8), puis nom de 9. | Code ligne 265-266 : `a....txt` pour la troncature. | `CONDITIONALS_BOUNDARY` 265 |
| `fileName_blankNamePartBeforeExtension_returnsNull` | Un nom réduit à des espaces devant l'extension est rejeté. | `"   .txt"` : l'extension est valide mais la partie nom est blanche. | `null` (garde ligne 173-174), jamais `""`. | `EMPTY_RETURNS` 174 |
| `filePath_dotOnly_returnsNull` | Le chemin `.` ne produit aucun nom. | `"."` : `getName` renvoie `""` pour `.`. | `null` (garde 231-232). | `EMPTY_RETURNS` 232 |
| `filePath_lastSegmentIsOnlyAnExtension_returnsNull` | Un dernier segment réduit à une extension (`dir/.txt`) ne produit aucun nom. | `dir/.txt` : l'extension vaut tout le nom mais pas tout le chemin, donc la garde de la ligne 239 ne s'applique pas et c'est celle de la ligne 249 qui joue. | `null`. | `EMPTY_RETURNS` 250 |
| `degenerateShortNames_areKeptAsNames` | Des noms d'un ou deux caractères (`A`, `AB`, `1:`, `[:`) ne sont pas pris pour des préfixes de lecteur et donnent un nom plus l'extension par défaut. | Chaque nom fait basculer **une** condition de `getPrefixLength` ligne 323 : `A` (longueur ≠ 2 → le mutant appelle `charAt(1)` et lève une exception), `AB` (deuxième caractère ≠ `:`), `1:` (premier caractère < `A`), `[:` (premier caractère > `Z`). `commons-io` renvoie 0 ou -1 pour tous. | `X.bin` : nom conservé, `:` remplacé par `/` puis retiré en fin de chemin, extension par défaut faute de type MIME. | 4 × négation ligne 323 |
| `suffix_fiveCharactersAccepted_sixRejected` | Une extension de 5 caractères point compris est acceptée, 6 refusée. | `.abcd` et `.abcde`. | Javadoc (« 5 or less ») et code (`< 6`). | `CONDITIONALS_BOUNDARY` 134 |
| `calculateExtension_unknownOrInvalidMimeType_fallsBackToBin` | Type MIME inconnu (sans extension enregistrée) ou syntaxiquement invalide → `.bin` ; absent → valeur par défaut ; connu → son extension. | `application/x-ift3913-unknown-type`, `ceci n'est pas un type mime`, aucun type, `application/pdf`. | Javadoc de `calculateExtension` et code : `lookupExtension` renvoie `null` → `.bin`. | `EMPTY_RETURNS` 402 et 416 |
| `resolveWithin_existingChild_isReturned` | Avec des chemins qui **existent** (répertoire temporaire JUnit), la vérification par chemins réels accepte un enfant direct et un enfant imbriqué. | `@TempDir` + fichiers créés : seule façon d'exécuter les lignes 305-312. | Le chemin résolu est renvoyé, égal à `dir.resolve(...)`. | Négation ligne 308 |
| `resolveWithin_missingChildInExistingDir_isReturned` | Un nom inexistant dans un répertoire existant est résolu sans erreur. | Répertoire temporaire, enfant absent : `Files.exists(resolved)` est faux. Le mutant entre dans la branche et `toRealPath()` lève `NoSuchFileException`. | Chemin résolu renvoyé. | Négation de `Files.exists(resolved)` ligne 305 |

**Intégration locale sur `tache2-final` — étape 2.** Le tableau et les scores ci-dessus
décrivent encore l'expérience Saidana à 15 tests manuels. Un seizième test,
`resolveWithinRejectsExistingSymbolicLinkOutsideDirectory`, est repris de
`tache2-hamza` (commit `d3b8e3f`). Il crée deux répertoires frères `inside` et
`outside`, puis un lien `inside/link` vers `outside`. Le chemin lexical reste dans
`inside`, tandis que son chemin réel en sort : l'oracle est donc `IOException`,
conformément à la protection contre les liens symboliques dans `resolveWithin`.
Les quatre autres tests manuels Hamza ne sont pas dupliqués : les replis
`INTERNAL_PATH`, le MIME inconnu et la conservation d'un nom court sont déjà
exercés dans la suite Saidana, qui teste aussi la borne exacte du nom sans extension.

Les **16 tests manuels réussissent, sans échec, erreur ni désactivation**, dans une
copie sur le disque interne. La construction avec les contrôles habituels reste
bloquée par 202 erreurs Checkstyle dans les tests hérités ; la vérification réussie
appelle séparément les objectifs de ressources, de compilation et Surefire.
Elle ne constitue pas une construction complète réussie. Les preuves et commandes
sont dans [le compte rendu de l'étape 2](tache2-rapports/final-integration/etape-02/commands.md).
Aucun nouveau score PIT n'est revendiqué à cette étape ; les mesures communes A/B/C
et la révision des équivalences sont prévues aux étapes 5 et 6.

**Mutants équivalents (10, non tuables) — conclusion historique Saidana à réviser.** Voir le tableau de la section 8.3 : bornes `prefixLength > 0` → `>= 0`
(156, 215, 320), négation de `prefixLength > 0` pour le nom (156), gardes inatteignables (185, 259), code mort
de `getPrefixLength` (323 ×2, 324) et négation de `Files.exists(normalizedDir)` (305).

**Observation faite en écrivant ces tests.** `getPrefixLength("~")` renvoie 2 (comportement de `commons-io` pour le
préfixe « home ») alors que la chaîne n'a qu'un caractère : `getSanitizedEmbeddedFileName` avec un nom de ressource
`"~"` lève une `StringIndexOutOfBoundsException` sur `path.substring(2)`. Nous ne l'avons pas testé (ce serait
figer un bogue), mais c'est un cas que l'équipe Tika pourrait vouloir corriger.

## 10. Exécution dans GitHub Actions

Le workflow [.github/workflows/tache2-ift3913.yml](.github/workflows/tache2-ift3913.yml) s'exécute à chaque `push`,
à chaque `pull_request` et manuellement (`workflow_dispatch`). Il :

1. installe le JDK 17 (version de référence de Tika) ;
2. compile `tika-core` et ses dépendances (`./mvnw -pl tika-core -am install -DskipTests`) ;
3. exécute les tests des trois classes ciblées, c'est-à-dire les tests originaux, les tests générés par ChatUniTest et
   les tests manuels (`-Dtest='EndianUtils*,MediaType*,FilenameUtils*'`) ; JaCoCo produit la couverture au passage ;
4. lance l'analyse de mutation pitest sur les trois classes (`org.pitest:pitest-maven:mutationCoverage`) ;
5. publie les rapports Surefire, JaCoCo et pitest comme artefact `rapports-tache2`, et résume le score de mutation
   dans la page du job.

<!-- À COMPLÉTER après le premier push : lien vers une exécution réussie, onglet Actions du dépôt -->

Les tests générés ne respectent pas les règles Checkstyle de Tika (ordre des imports, lignes trop longues) ; comme
Checkstyle est configuré en phase `validate` avec `failOnViolation`, il est désactivé dans ce workflow
(`-Dcheckstyle.skip`), de même que les vérifications `rat`, `forbiddenapis` et `ossindex` qui n'ont pas de rapport
avec la tâche.

## 11. Reproduire les résultats

```bash
git clone <ce dépôt> && cd tika            # JDK 17 et Maven 3.9 (ou ./mvnw)
# 1. compiler le module et ses parents
mvn -pl tika-core -am install -DskipTests -Dossindex.skip -Drat.skip -Dcheckstyle.skip -Dforbiddenapis.skip
# 2. couverture (toute la suite tika-core, tests originaux + générés + manuels)
mvn -pl tika-core test -Dcheckstyle.skip -Drat.skip -Dforbiddenapis.skip -Dossindex.skip
#    -> tika-core/target/site/jacoco/index.html
# 3. score de mutation avec les tests originaux seulement (référence)
mvn -pl tika-core org.pitest:pitest-maven:mutationCoverage \
    -DtargetTests=org.apache.tika.io.EndianUtilsTest,org.apache.tika.mime.MediaTypeTest,org.apache.tika.io.FilenameUtilsTest
# 4. score de mutation avec les tests originaux + générés (sans les tests manuels)
mvn -pl tika-core org.pitest:pitest-maven:mutationCoverage \
    -DexcludedTestClasses=org.apache.tika.io.EndianUtilsManualTest,org.apache.tika.mime.MediaTypeManualTest,org.apache.tika.io.FilenameUtilsManualTest
# 5. score de mutation avec tous les tests (configuration par défaut du pom)
mvn -pl tika-core org.pitest:pitest-maven:mutationCoverage
#    -> tika-core/target/pit-reports/index.html
# 6. (facultatif) regénérer des tests avec ChatUniTest : Ollama + alias, puis les commandes de la section 4.4
```

Les rapports obtenus lors de la rédaction de ce document sont conservés dans [tache2-rapports/](tache2-rapports/)
(un dossier numéroté par étape, décrit dans [tache2-rapports/README.md](tache2-rapports/README.md)), avec les
scripts d'analyse utilisés (`tache2-rapports/scripts/`).
