# V1 — Injection de commandes (CWE-78)

`tika-core/.../parser/external/ExternalParser.java`, méthode `parse`, prépare les arguments d'un programme externe. Le fichier est choisi parce qu'il remplace déjà `${INPUT_FILE}` par le chemin réel du document puis exécute ce programme.

Avant, `ProcessBuilder` recevait une liste d'arguments : les caractères du chemin n'étaient pas interprétés par un shell. La modification joint la liste avec des espaces et la transmet à `sh -c`, sans citer ni échapper les arguments. Si l'application conserve un chemin fourni par un utilisateur, ses métacaractères peuvent donc modifier la commande exécutée. Une configuration de commande de confiance ne protège plus un nom de fichier hostile. Ce scénario suppose un shell POSIX et un chemin conservé par `TikaInputStream.get(Path)` ; une copie sous un nom temporaire imposé par le serveur réduit cette entrée particulière.

La règle `command-injection-process-builder` (ERROR) était déjà présente à cet emplacement avant modification, sans démontrer une injection shell. Le nouveau fragment est une identité ajoutée et l'ancien une identité supprimée : **17 → 17** au total. C'est une régression réelle du mécanisme d'exécution, et non une simple dix-huitième alerte. `incremental.json` et `cumulative.json` montrent l'ajout et la suppression.

Le build complet core et dépendances réussit : 964 tests core réussis, deux désactivations initiales, 8 tests processeur réussis, zéro violation Checkstyle. La tentative 1 est un refus d'écriture du cache Maven ; la tentative 2 est la validation réussie. Aucun contrôle n'a été désactivé et Spotless n'a pas changé les sources.

Correction : conserver la liste d'arguments et éviter le shell. Le signalement statique doit être interprété avec cette différence de contexte ; la règle seule ne prouvait pas l'exploitation avant la modification.

Preuves : `change.patch`, `semgrep.json`, `scan.log`, `source-manifest.json`, `sources.tar.gz`, `results.json`, `build-results-2.json` et les journaux/rapports associés. Moteur CE 1.185.0 et 82 règles inchangés. Modification locale non commitée, base `860a2631d9e78311387207ed9380275b2a151781` ; aucun run GitHub des injections à ce stade.

## Consolidation documentaire du 12 novembre

Les anciennes traces indiquent un marqueur de commande local positif ; cette observation est distincte de la seule alerte statique. Les archives sources, XML et comptes Semgrep ont été recoupés sans nouvelle sonde. Voir [le bilan des sept types](../consolidation-20261112/README.md) et ses limites. Aucun run GitHub propre à cette injection n’est encore enregistré.

## Actualisation GitHub après les scans locaux

Ces sources ont été introduites dans le [commit 5d37388](https://github.com/EnzoT454/tika/commit/5d37388b0cbf1baeb19c04a2e1c0ed633753765c) et les nouveautés ont été confirmées dans le [run final 43278937793](https://github.com/EnzoT454/tika/actions/runs/43278937793), sur c20318f. Les mentions « non commitée » et « aucun run » ci-dessus décrivent les premiers scans locaux. Voir [la consolidation finale](../../rapport-final-20261112/README.md).
