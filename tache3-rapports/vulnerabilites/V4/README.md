# V4 — Nom d'hôte TLS non vérifié (CWE-295)

`tika-core/.../io/TikaInputStream.java`, méthode `get(URL, Metadata)`, ouvre la connexion utilisée pour lire un document distant. Ce point existant est choisi parce que le vérificateur est effectivement attaché à la connexion avant son utilisation, plutôt qu'ajouté comme une classe sans appel.

La modification installe, pour HTTPS, un `HostnameVerifier` dont `verify` retourne toujours true. Une connexion peut donc accepter un certificat de confiance qui correspond à un autre nom d'hôte. Le nom demandé ne protège plus l'identité du serveur ; une interception réseau ou une redirection DNS vers un serveur possédant un certificat valide pour son propre nom peut exploiter cette absence de vérification.

**La chaîne de certificats reste vérifiée.** Cette injection n'accepte pas automatiquement tout certificat autosigné et n'est pas une désactivation générale de TLS. Aucun changement n'est appliqué aux valeurs globales de la JVM. Les URI locales et les connexions HTTP suivent leur chemin préexistant.

La règle WARNING `insecure-hostname-verifier` ajoute une identité absente de la référence ; cumul V1–V4 : 21 alertes. La construction complète réussit avec 964 tests core et 8 processeur réussis, deux désactivations initiales, zéro violation Checkstyle et sources inchangées par le build.

Correction : conserver la vérification standard du nom d'hôte ou employer une vérification qui compare réellement le nom attendu au certificat. Référence : [configuration par connexion de HttpsURLConnection](https://docs.oracle.com/en/java/javase/26/docs/api/java.base/javax/net/ssl/HttpsURLConnection.html).

Preuves : patch, manifeste, sources, JSON brut/comparaisons et `build-results-1.json`. CE 1.185.0 et règles inchangés. Base `860a2631d9e78311387207ed9380275b2a151781` ; modification locale non commitée, sans run GitHub propre à V4.

## Consolidation documentaire du 12 novembre

Les anciennes traces indiquent un vérificateur acceptant le nom dans une connexion simulée ; aucun handshake réel n’est validé. Les archives sources, XML et comptes Semgrep ont été recoupés sans nouvelle sonde. Voir [le bilan des sept types](../consolidation-20261112/README.md) et ses limites. Aucun run GitHub propre à cette injection n’est encore enregistré.

## Actualisation GitHub après les scans locaux

Ces sources ont été introduites dans le [commit 5d37388](https://github.com/EnzoT454/tika/commit/5d37388b0cbf1baeb19c04a2e1c0ed633753765c) et les nouveautés ont été confirmées dans le [run final 43278937793](https://github.com/EnzoT454/tika/actions/runs/43278937793), sur c20318f. Les mentions « non commitée » et « aucun run » ci-dessus décrivent les premiers scans locaux. Voir [la consolidation finale](../../rapport-final-20261112/README.md).
