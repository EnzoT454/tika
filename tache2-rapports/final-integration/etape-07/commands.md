# Étape 7 — workflow final et contrôle des cas exécutés

Préparation et validation locales du 8 octobre 2026, sur `tache2-final`, sans
commit automatique ni push. Aucune exécution GitHub de cette version n'est
revendiquée. Les anciens runs Hamza/Saidana restent des preuves historiques.

## Changements

Le workflow `.github/workflows/tache2-ift3913.yml` est adapté :

- push vers `main`/`tache2-final`, PR dont la base est l'une de ces branches,
  lancement manuel possible ; permissions `contents: read` ;
- Java 17 Temurin sur `ubuntu-latest`, cache Maven ;
- construction complète core et dépendances avec `clean install` ;
- retrait des drapeaux `skipTests`, Checkstyle, RAT, forbidden-apis et OSS Index
  du workflow, aucune sélection `-Dtest` ; contrôles applicables conservés ;
- vérification exacte des 161 cas IA et 56 manuels dans 43 classes ;
- PIT avec le profil final `mutation`, résumé XML KILLED/total et statuts séparés ;
- journaux et rapports conservés même après un échec, pendant 30 jours.

Les pipelines Maven utilisent `set -euo pipefail` : `tee` ne masque pas un
échec. Aucun `continue-on-error`. ChatUniTest/Ollama n'est pas exécuté en CI.
Les propriétés héritées du projet, notamment OSS Index désactivé dans le POM,
ne sont pas modifiées ; retirer son drapeau du workflow n'est pas un audit.

Le vérificateur reprend la méthode Hamza (compteurs, noms, doublons et balises
de résultats), adaptée au périmètre final. L'inventaire
`.github/scripts/tache2-expected-tests.json` est figé depuis les sources et XML
de C ; il n'est pas régénéré par le workflow depuis ses propres résultats.
Il exige 966 cas core, dont les deux seules désactivations préexistantes :
`TikaInputStreamTest.reproduceRandomizedTestFailure` et
`CustomErrorHandlerTest.testUndeclaredEntityXML`.

## Validation locale

Copie de construction : `/private/tmp/tika-final-build-x4r_1f3o`, préparée par
le script de l'étape 4 puis complétée avec les quatre fichiers CI ci-dessus
(workflow, inventaire et deux scripts). AppleDouble exclu. Commandes exactes
exécutées dans cette copie, avec Java Homebrew 17.0.18 et Maven 3.9.12 :

```bash
set -euo pipefail
./mvnw -B -ntp -pl tika-core -am clean install 2>&1 | tee tache2-build.log
python3 .github/scripts/check-tache2-tests.py
./mvnw -B -ntp -pl tika-core -Pmutation org.pitest:pitest-maven:mutationCoverage 2>&1 | tee tache2-pit.log
python3 .github/scripts/summarize-tache2-pit.py
```

Les quatre commandes terminent avec un code 0. Core : **966 cas, 964 réussis,
deux désactivations anciennes, zéro échec/erreur**. Processeur d'annotations :
**8/8 réussis**. Checkstyle : zéro erreur dans les deux modules. Les 217 cas
acceptés sont présents, sous les noms attendus, sans désactivation ni doublon.

PIT retrouve **396 KILLED, 7 SURVIVED, 2 NO_COVERAGE, 1 TIMED_OUT** sur 406,
soit **97,54 %** KILLED/total. Les sources de production, tests et POM sont
identiques à C. Cette validation CI locale est une expérience supplémentaire,
archivée ici ; elle ne remplace aucune archive A/B/C.

Les XML de C ont aussi été vérifiés séparément par les nouveaux scripts.
Le YAML est analysé avec PyYAML 6.0.3 et chaque bloc shell vérifié par `bash -n` :

```bash
python3 tache2-rapports/final-integration/etape-07/validate-ci.py
```

Sept anomalies sont introduites uniquement dans des copies temporaires des XML :
rapport absent, cas absent, renommé, dupliqué, désactivé, échoué et compteur
incohérent. Le vérificateur les refuse toutes avec un code 1. Les vrais rapports
et sources ne sont pas modifiés. Les scénarios et erreurs sont archivés dans
`verifier-validation.json`.

Cette validation ne simule pas les services GitHub, les téléchargements des
actions ou l'environnement Ubuntu ; ce n'est pas une exécution distante.

## Preuves

- `workflow-before.yml`, copies Hamza : configuration précédente et provenance ;
- workflow, scripts et inventaire finaux, empreintes dans `results.json` ;
- `build.log`, `pit.log`, `check-fresh-build.log`, `pit-summary-fresh.md` ;
- Surefire/Checkstyle des deux modules, JaCoCo et PIT complets ;
- `source-manifest.json`, `results.json`, `verifier-validation.json` ;
- `check-archived-C.log`, `pit-summary-C.md` : vérification de la référence C.

Pour reproduire sur macOS, préparer une copie via le script de l'étape 4,
copier les quatre fichiers `.github` dans cette copie en conservant leurs
chemins, puis exécuter les quatre commandes ci-dessus. Les scripts de CI ne
nécessitent que la bibliothèque standard Python ; PyYAML sert uniquement au
contrôle local du workflow.

Le prochain travail autorisé une étape à la fois est la consolidation dans le
seul `readme.md` de remise. Le lien CI final sera ajouté après une publication
et une exécution GitHub autorisées, pas remplacé par un lien d'une ancienne version.
