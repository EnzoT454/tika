# V7 — Fichiers temporaires accessibles aux autres utilisateurs (CWE-276)

`tika-core/.../io/TemporaryResources.java`, méthode `createTempFile(String)`, crée les fichiers utilisés notamment pour mettre en cache les documents d'entrée. Elle est choisie parce qu'un changement de permissions atteint de vrais contenus traités par Tika, et pas un fichier témoin inutilisé.

La création sécurisée est maintenant suivie de `Files.setPosixFilePermissions(..., "rw-rw-rw-")`. Cela donne la lecture et l'écriture au groupe et aux autres utilisateurs locaux. Dans un répertoire temporaire traversable par d'autres comptes, le nom du fichier suffit pour lire ou modifier un document entre son écriture et son analyse. L'atomicité de la création initiale et le sticky bit de `/tmp` n'empêchent pas ces accès au fichier existant.

Le risque dépend des permissions des répertoires parents et des ACL : un répertoire privé 0700 peut encore empêcher l'accès. La modification ne s'exécute que sur un système de fichiers POSIX ; elle ne prétend pas modifier les ACL Windows. La preuve bornée contrôle les bits du fichier sur des données fictives, sans ouvrir de document d'un autre utilisateur.

La règle WARNING `overly-permissive-file-permission` ajoute une identité absente avant l'étape. Avec les sept injections, **24 résultats** sont présents : 8 identités ajoutées, une ancienne remplacée par V1, 16 conservées. Les deux alertes XXE correspondent à un seul type ; les sept CWE retenus sont distincts.

Le build complet final réussit : 964 tests core et 8 processeur réussis, deux désactivations préexistantes, zéro violation Checkstyle. Aucun contrôle désactivé ni source modifiée par Spotless. Cette réussite ne rend évidemment pas les permissions appropriées : la suite existante ne vérifie pas ce cas de sécurité.

Correction : conserver les permissions restrictives de `Files.createTempFile` et utiliser un répertoire privé ; ne pas élargir les droits après création. Preuves : patch, sources exactes, manifestes, rapports Semgrep/comparaisons et `build-results-1.json`. Modification locale non commitée, base `860a2631d9e78311387207ed9380275b2a151781`, aucun run GitHub des sept injections.

## Consolidation documentaire du 12 novembre

Les anciennes traces indiquent les bits POSIX de lecture et écriture pour les autres comptes ; aucun accès croisé entre utilisateurs n’est démontré. Les archives sources, XML et comptes Semgrep ont été recoupés sans nouvelle sonde. Voir [le bilan des sept types](../consolidation-20261112/README.md) et ses limites. Aucun run GitHub propre à cette injection n’est encore enregistré.
