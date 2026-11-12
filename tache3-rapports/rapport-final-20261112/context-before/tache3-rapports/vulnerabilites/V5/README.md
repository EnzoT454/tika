# V5 — Repli forcé vers AES/ECB (CWE-327)

`tika-core/.../parser/CryptoParser.java`, méthode `parse`, déchiffre le document avant de le transmettre à un autre analyseur. Ce fichier est choisi parce que l'algorithme y est utilisé réellement pour des contenus chiffrés, et non pour un identifiant sans rôle de sécurité.

Avant, le mode et le fournisseur demandés au constructeur étaient respectés. Maintenant toute transformation commençant par `AES` est remplacée par `Cipher.getInstance("AES")`. Avec SunJCE, cela choisit ECB avec padding, même si l'appelant demande AES/GCM. ECB n'authentifie pas le contenu et permet notamment la substitution de blocs issus d'autres messages sous la même clé.

La condition importante est que les paramètres de contexte permettent l'initialisation ECB. Avec des paramètres GCM fournis, l'initialisation peut échouer au lieu d'accepter le document. Cette injection concerne une API de **déchiffrement** : elle ne démontre ni récupération de clé ni fuite automatique du texte d'un document déjà chiffré correctement. La régression à vérifier est l'acceptation d'un contenu ECB dans un chemin censé exiger GCM. Un producteur ou une source de blocs ECB sous la même clé est une condition d'attaque supplémentaire.

La règle WARNING `use-of-default-aes` ajoute une alerte nouvelle ; cumul V1–V5 : 22 alertes. Le build complet réussit avec 964 tests core et 8 processeur réussis, deux désactivations initiales et zéro violation Checkstyle. Aucun contrôle n'est désactivé et le build ne change pas les sources.

Correction : respecter la transformation complète et le fournisseur demandés, refuser tout repli non explicite et privilégier un mode authentifié adapté au protocole. Référence : [transformations et mode ECB par défaut dans JCA](https://docs.oracle.com/en/java/javase/25/security/java-cryptography-architecture-jca-reference-guide.html).

Preuves : patch, sources exactes, JSON Semgrep/comparaisons et `build-results-1.json`. Configuration inchangée ; modification locale non commitée sur la base `860a2631d9e78311387207ed9380275b2a151781`, sans run GitHub d'injection.

## Consolidation documentaire du 12 novembre

Les anciennes traces indiquent l’acceptation du contenu ECB alors que GCM est demandé, dans le contexte précis du scénario ; aucune récupération de clé n’est démontrée. Les archives sources, XML et comptes Semgrep ont été recoupés sans nouvelle sonde. Voir [le bilan des sept types](../consolidation-20261112/README.md) et ses limites. Aucun run GitHub propre à cette injection n’est encore enregistré.
