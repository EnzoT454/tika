# Étape 4 — Maven, PIT et contrôles de construction

Travail local du 8 octobre 2026 sur `tache2-final`. Aucun push, aucune nouvelle
génération ChatUniTest et aucune mesure PIT pendant cette étape.

## Configuration retenue

| Élément | Configuration finale |
|---|---|
| Tests ordinaires | JUnit 6.1.3 ; Mockito Jupiter/Core 5.23.0, versions du parent |
| ChatUniTest | Plugin 2.1.1, uniquement dans le profil `chatunitest` |
| Starter ChatUniTest | Retiré des dépendances ordinaires |
| Génération | `enableMultithreading=false`, `enableMerge=false`, `noExecution=false` |
| PIT | 1.30.0, profil `mutation` ; connecteur JUnit 1.2.3 |
| Classes mutées | `org.apache.tika.io.EndianUtils`, `org.apache.tika.mime.MediaType`, `org.apache.tika.io.FilenameUtils` |
| Tests PIT | `org.apache.tika.*`, même périmètre dans A/B/C |
| Exécution PIT | 2 threads, facteur de délai 1.25, constante 4000 ms, historique désactivé |
| JVM PIT | `-Xmx2g`, `-Djava.awt.headless=true` |
| Rapports PIT | HTML/XML, dossier sans horodatage, couverture des lignes exportée |

Les onze opérateurs sont figés explicitement : `CONDITIONALS_BOUNDARY`,
`INCREMENTS`, `INVERT_NEGS`, `MATH`, `NEGATE_CONDITIONALS`, `VOID_METHOD_CALLS`,
`EMPTY_RETURNS`, `FALSE_RETURNS`, `TRUE_RETURNS`, `NULL_RETURNS`,
`PRIMITIVE_RETURNS`. Ne pas redéfinir les cibles ou opérateurs entre A/B/C.
Le nombre total de mutants sera celui des nouveaux XML, pas une valeur reprise
automatiquement d'une expérience antérieure.

L'alias Ollama et les budgets de génération sont conservés dans le profil ;
l'existence du POM effectif ne prouve ni le chargement ni l'exécution d'un modèle.
Le paramètre ancien `thread` est remplacé par `enableMultithreading`.

## Construction et validations effectuées

Copie de construction utilisée : `/private/tmp/tika-final-step2-wbf90_na`,
actualisée avec les sources retenues et le nouveau POM. Java Homebrew 17.0.18,
wrapper Maven 3.9.12. Commandes exécutées dans cette copie :

```bash
./mvnw -B -pl tika-core -am clean install
./mvnw -B -pl tika-core -Pchatunitest,mutation help:effective-pom \
  -Doutput=/tmp/tika-final-step4-effective-pom.xml
./mvnw -B -pl tika-core dependency:tree \
  '-Dincludes=org.junit.jupiter:*,org.junit.platform:*,org.junit.vintage:*,org.mockito:*,io.github.ZJU-ACES-ISE:*'
```

Les trois commandes réussissent. Le cycle complet conserve les contrôles
applicables, sans `-Pfast`, `-DskipTests` ou objectifs directs contournant le
cycle. Checkstyle : zéro erreur dans core et le processeur d'annotations.
Les réglages hérités du projet, dont la désactivation préexistante d'OSS Index,
ne constituent pas une preuve d'audit de sécurité.

| Module | Cas | Réussis | Échecs | Erreurs | Désactivés |
|---|---:|---:|---:|---:|---:|
| tika-core | 966 | 964 | 0 | 0 | 2 préexistants |
| tika-annotation-processor | 8 | 8 | 0 | 0 | 0 |

Les imports génériques des tests IA sont développés, les imports inutilisés
retirés, les commentaires longs répartis, puis Spotless applique le format du
projet. Une comparaison des tokens hors imports/commentaires confirme que les
40 classes IA gardent les mêmes données et assertions qu'à l'étape 3. Les sources
formatées effectivement compilées sont recopiées dans le dépôt ; les sources
de production sont identiques à celles de la copie construite.

Les échecs intermédiaires restent disponibles : `attempt-1-build.log` (huit
imports inutilisés), `attempt-2-build.log` (deux types JUnit à importer après
suppression du wildcard). Le journal final est `build.log`.

Les preuves comprennent `pom-before.xml`, `pom-final.xml`,
`effective-pom-profiles.xml`, `profiles.log`, `dependencies.log`,
`source-manifest.json`, `results.json`, les XML Surefire et Checkstyle des deux
modules et le rapport JaCoCo complet de core. Le POM final diffère du POM
construit uniquement par l'ajout du profil dans un commentaire d'exemple.

## Reproduction et étape suivante

Depuis la racine, le script fourni copie les fichiers suivis dans leur état
courant et les fichiers non suivis/non ignorés des modules nécessaires, exclut
les `._*`, inclut les POM de découverte et écrit un manifeste SHA-256 :

```bash
build_dir=$(python3 tache2-rapports/final-integration/etape-04/prepare-local-build.py)
cd "$build_dir"
./mvnw -B -pl tika-core -am clean install
```

Les fichiers ignorés et les preuves ne deviennent pas automatiquement des
sources. Vérifier le manifeste avant une mesure et archiver les résultats dans
le dépôt original avant de supprimer la copie.

À l'étape 5, préparer trois copies/sélections de sources séparées : A (tous les
tests originaux), B (A + 161 cas IA acceptés), C (B + 56 cas manuels). Garder le
même code de production, POM et périmètre de tests originaux. Construire chaque
état proprement, puis exécuter dans sa copie :

```bash
./mvnw -B -pl tika-core -Pmutation org.pitest:pitest-maven:mutationCoverage
```

Cette dernière commande n'a pas été exécutée à l'étape 4. Les trois états
doivent avoir leurs sources exactes, journaux et XML distincts ; les scores
historiques restent séparés.

Le script de préparation a été vérifié dans `/private/tmp/tika-final-build-a3870ao0` : 658 fichiers, aucun AppleDouble, POM et sources Java core identiques au dépôt. Cette nouvelle copie ne fait pas l’objet d’une seconde construction Maven.
