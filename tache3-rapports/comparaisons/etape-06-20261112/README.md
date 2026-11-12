# Étape 6 — Comparaison finale Semgrep

**Comparaison terminée le 12 novembre 2026**, à partir des rapports archivés, sans nouvelle analyse Java. Le passage à l'étape suivante est accepté en conservant la limite comportementale de V6. Cette conclusion ne transforme pas les validations incomplètes ou non exécutées en réussites.

## Résultat avant/après

| Mesure | Résultat |
|---|---:|
| Rapport initial | 17 |
| Rapport final V1–V7 | 24 |
| Identités ajoutées | 8 |
| Identité supprimée | 1 |
| Identités conservées | 16 |
| Types / CWE distincts documentés | 7 |

Les deux rapports portent sur 682 fichiers avec Semgrep CE 1.185.0 et les 82 règles verrouillées. Leurs sources archivées ont été contrôlées contre les manifestes. La comparaison utilise la règle, le chemin et le SHA-256 du fragment signalé, avec multiplicité et sans numéro de ligne. Les huit ajouts et la suppression recalculés correspondent **exactement** aux objets du rapport cumulatif V7 déjà archivé.

La suppression est l'ancien fragment `ProcessBuilder(thisCommandLine)` de V1, remplacé par le fragment shell vulnérable. Elle ne représente pas une correction. V1 conserve ainsi 17 résultats à sa première étape, malgré une régression. V2 produit deux résultats pour un seul type XXE.

## Association des huit nouveautés

Les chemins sont relatifs à `tika-core/src/main/java/org/apache/tika/`.

| V | CWE | Fichier | Règle(s) nouvelle(s) | Ajouts |
|---|---|---|---|---:|
| V1 | 78 | `parser/external/ExternalParser.java` | `command-injection-process-builder` | 1 |
| V2 | 611 | `utils/XMLReaderUtils.java` | `documentbuilderfactory-disallow-doctype-decl-false` ; `documentbuilderfactory-disallow-doctype-decl-missing` | 2 |
| V3 | 502 | `parser/ParseContext.java` | `object-deserialization` | 1 |
| V4 | 295 | `io/TikaInputStream.java` | `insecure-hostname-verifier` | 1 |
| V5 | 327 | `parser/CryptoParser.java` | `use-of-default-aes` | 1 |
| V6 | 1333 | `parser/RegexCaptureParser.java` | `java-pattern-from-string-parameter` | 1 |
| V7 | 276 | `io/TemporaryResources.java` | `overly-permissive-file-permission` | 1 |

`associations.json` donne les chemins complets, lignes, sévérités et empreintes. `added.json`, `removed.json` et `retained.json` conservent chaque signalement brut associé à son identité ; les 16 conservés sont appariés avant/après. Aucun ajout n'est laissé sans association. V6 est INFO et n'est pas exclue de la comparaison.

## État des validations

La [matrice détaillée](validation-matrix.md) distingue **réussie**, **incomplète** et **non exécutée**, ainsi que le périmètre de chaque preuve. Ses lignes regroupent des essais et des synthèses qui se recoupent ; leur nombre n'est pas un nombre de nouvelles expériences indépendantes.

Les 14 scénarios locaux du contrôleur et les cinq runs GitHub antérieurs sont réutilisés avec leurs références exactes. Les runs distants démontrent le contrôleur sur l'exemple temporaire, pas sur les sept modifications actuelles. Les sept builds successifs déjà archivés réussissent chacun : 964 tests core + 8 processeur, deux désactivations préexistantes et zéro violation Checkstyle.

Les rapports statiques et les comparaisons confirment la nouveauté des signalements. Une alerte ne prouve pas à elle seule une exploitation : les hypothèses des fiches restent applicables, notamment chemin contrôlable pour V1, configuration et classes disponibles pour V3, contexte cryptographique pour V5, répertoires/ACL pour V7.

## Réserve conservée pour V6

La détection statique, sa nouveauté et la compilation sont confirmées. **Aucun comportement ReDoS ni épuisement de ressources n'est démontré.** Les traces de la sonde restent négatives, y compris lorsque deux LF sont confirmés. Le marqueur `X-TIKA:INLINE_MATCH` est refusé par la protection des métadonnées réservées : son absence ne prouve pas que le motif n'a pas été évalué. Cette limite de l'observation est conservée sans modification de code ni nouvelle sonde.

V6 reste donc une modification associée à un risque potentiel CWE-1333, signalée par une règle INFO d'audit. Sa justification repose sur le chemin statique document → motif Java sans restriction, avec une incertitude comportementale explicitement déclarée. Cette réserve n'empêche pas la clôture de **la comparaison documentaire**, conformément à la décision utilisateur.

## Preuves et intégrité

Le dossier conserve les JSON bruts avant/après, leurs manifestes et archives sources, la configuration du contrôleur (sans règles upstream), les associations et la matrice. `evidence/` fixe les résultats locaux/CI et les traces comportementales utilisés. Aucun test, build, scan, commit, push ou accès GitHub n'a été lancé pour cette étape.

Le précédent manifeste de consolidation a été vérifié avant mise à jour des guides. Les versions précédentes de `AGENTS.md` et `tache3.md` sont conservées dans `context-before/` ; leurs modifications documentaires ultérieures rendent normalement leurs anciens chemins de travail différents. Le manifeste précédent est préservé et le nouveau `checksums.json`/`SHA256SUMS` couvre l'état de cette étape.

`compare.py` conserve la recette de production des rapports, qui refuse d'écraser des sorties et suppose le contexte antérieur. `verify.py` permet de contrôler les résultats figés sans scanner ni exécuter Java :

```bash
python3 tache3-rapports/comparaisons/etape-06-20261112/verify.py
shasum -a 256 -c tache3-rapports/comparaisons/etape-06-20261112/SHA256SUMS
```

Le résultat attendu du contrôleur complet serait le code 1 en présence des huit nouveautés ; **aucun nouveau code de sortie intégré ni run GitHub des sept états n'est revendiqué**. Ces validations distantes restent à obtenir après autorisation de publication.

**Prochaine étape : l'action réutilisable de rickroll**, appelée après un échec de test Tika, avec statut d'échec préservé. Elle n'est pas implémentée ici. Le rapport final `readme.md` reste inchangé.
