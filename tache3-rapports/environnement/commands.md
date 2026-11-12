# Validation initiale — 10 novembre 2026

Base : `62ff2e9b180817f4b38108c4e90ffa05348c5a4d`, branche `tache3`.
Sources : worktree `/Volumes/TOSHIBA_EXT/ift3913/tika-tache3` ; copie interne `/private/tmp/tika-final-build-yy2wo_ao`.

```bash
# Depuis le worktree : préparation sans fichiers AppleDouble
python3 tache2-rapports/final-integration/etape-04/prepare-local-build.py
# Dans le dossier interne retourné
java -version
./mvnw -version
./mvnw -B -ntp -pl tika-core -am clean install
```

La tentative 01 a échoué avant les tests core : le sandbox interdisait l'installation du POM parent dans `~/.m2`. La tentative 02, relancée avec l'accès autorisé au cache, réussit sans modifier la commande ni désactiver de contrôle. Le journal complet de chacune est conservé.

Résultat : 966 cas core, 964 réussis et deux désactivations préexistantes ; huit tests du processeur réussis ; zéro échec, zéro erreur et zéro violation Checkstyle dans les trois modules. La durée Maven de la tentative réussie est de 27,871 s. Aucun des 658 fichiers du manifeste n'a changé pendant la construction, y compris après Spotless.

Les désactivations sont `TikaInputStreamTest.reproduceRandomizedTestFailure` et `CustomErrorHandlerTest.testUndeclaredEntityXML`.

`results.json` est dérivé des XML Surefire et Checkstyle ; `reports.tar.gz` conserve ces rapports bruts et JaCoCo. `sources.tar.gz` conserve les 658 fichiers de la copie avant toute injection ; `source-manifest.json` fournit leurs empreintes SHA-256. La copie couvre la construction de tika-core, pas tout le dépôt ni ses vérificateurs CI. `readme.md` dans l'archive est encore le rapport hérité de tâche 2.

Cette validation ne lance ni Semgrep ni PIT et ne constitue pas un run GitHub Actions. Le SHA ne décrit pas les fichiers non commitées : les sources archivées et le manifeste sont la référence exacte de cette construction.
