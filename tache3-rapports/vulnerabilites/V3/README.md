# V3 — Désérialisation Java non filtrée (CWE-502)

`tika-core/.../parser/ParseContext.java`, surcharge `setJsonConfig(String, String)`, reçoit déjà une configuration brute et la stocke pour les analyseurs. La modification ajoute volontairement un format de configuration binaire `java-serialized:` suivi de Base64, au lieu de traiter systématiquement l'argument comme une chaîne JSON.

Pour ce préfixe, le setter construit un `ObjectInputStream` puis appelle `readObject()` sans filtre de classes ou de taille. La conversion vers `JsonConfig` intervient après lecture : elle ne bloque pas les callbacks exécutés pendant la reconstruction. Le format ajouté est une nouvelle fonctionnalité pédagogique dangereuse, pas une fonctionnalité Tika préexistante ni une méthode sans appel : le chemin d'entrée public existant l'exécute directement.

L'attaque suppose que l'application laisse un utilisateur fournir cette chaîne et que des classes ayant des callbacks dangereux sont disponibles dans son classpath. La configuration est souvent de confiance ; cette condition doit être expliquée. Un filtre global de JVM peut également limiter le risque. L'absence de filtre local n'est pas une preuve automatique d'exécution de commandes sur toute installation.

Semgrep ajoute une alerte WARNING `object-deserialization`, absente avant la modification. Le cumul V1–V3 contient 20 alertes ; les données incrémentales identifient précisément celle-ci. La construction complète réussit, avec 964 tests core et 8 processeur réussis, deux désactivations initiales, zéro violation Checkstyle et sources inchangées par Spotless.

Correction : conserver le format JSON attendu et supprimer la désérialisation native. Si elle est indispensable, imposer une liste de classes et des limites avant toute lecture. Référence : [ObjectInputStream et validation des données non fiables](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/io/ObjectInputStream.html).

Les sources exactes, patch, JSON avant/après, logs et rapports sont dans ce dossier. CE 1.185.0, 82 règles et périmètre inchangés. Base `860a2631d9e78311387207ed9380275b2a151781` ; modification non commitée et aucun run GitHub d'injection à ce stade.

## Consolidation documentaire du 12 novembre

Les anciennes traces indiquent le callback contrôlé de désérialisation ; elles ne démontrent pas une chaîne d’exploitation arbitraire. Les archives sources, XML et comptes Semgrep ont été recoupés sans nouvelle sonde. Voir [le bilan des sept types](../consolidation-20261112/README.md) et ses limites. Aucun run GitHub propre à cette injection n’est encore enregistré.

## Actualisation GitHub après les scans locaux

Ces sources ont été introduites dans le [commit 5d37388](https://github.com/EnzoT454/tika/commit/5d37388b0cbf1baeb19c04a2e1c0ed633753765c) et les nouveautés ont été confirmées dans le [run final 43278937793](https://github.com/EnzoT454/tika/actions/runs/43278937793), sur c20318f. Les mentions « non commitée » et « aucun run » ci-dessus décrivent les premiers scans locaux. Voir [la consolidation finale](../../rapport-final-20261112/README.md).
