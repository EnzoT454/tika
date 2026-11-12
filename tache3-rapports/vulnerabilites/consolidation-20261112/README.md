# Bilan documentaire de l'étape 5 — sept modifications pédagogiques

Consolidation du 12 novembre 2026, à partir des preuves locales existantes. Aucun scan, build, scénario Java, commit ou accès externe n'a été lancé pour cette consolidation. Le code de production et les sondes Java sont préservés. La branche est `tache3`, HEAD `860a2631d9e78311387207ed9380275b2a151781` ; les sept modifications restent non commitées.

## Correspondance des sept fiches avec Semgrep

Les chemins du tableau sont relatifs à `tika-core/src/main/java/org/apache/tika/`. Le cumul correspond aux scans successifs V1, puis V1–V2, jusqu'à V1–V7. Les ajouts/suppressions comparent chaque état au précédent.

| Fiche | CWE | Règle(s) Semgrep | Fichier et ligne signalée | Ajouts / suppressions | Cumul |
|---|---|---|---|---|---|
| [V1](../V1/README.md) | 78 | `command-injection-process-builder` | `parser/external/ExternalParser.java:192` | 1 / 1 | 17 |
| [V2](../V2/README.md) | 611 | `documentbuilderfactory-disallow-doctype-decl-false` ; `documentbuilderfactory-disallow-doctype-decl-missing` | `utils/XMLReaderUtils.java:1270,1271` | 2 / 0 | 19 |
| [V3](../V3/README.md) | 502 | `object-deserialization` | `parser/ParseContext.java:149` | 1 / 0 | 20 |
| [V4](../V4/README.md) | 295 | `insecure-hostname-verifier` | `io/TikaInputStream.java:224` | 1 / 0 | 21 |
| [V5](../V5/README.md) | 327 | `use-of-default-aes` | `parser/CryptoParser.java:78` | 1 / 0 | 22 |
| [V6](../V6/README.md) | 1333 | `java-pattern-from-string-parameter` | `parser/RegexCaptureParser.java:138` | 1 / 0 | 23 |
| [V7](../V7/README.md) | 276 | `overly-permissive-file-permission` | `io/TemporaryResources.java:94` | 1 / 0 | 24 |

Les sept CWE correspondent à des mécanismes distincts. V2 produit deux alertes pour un seul type. V1 remplace un ancien signalement ProcessBuilder par un nouveau fragment utilisant réellement un shell ; le nombre total seul ne permet pas de voir cette régression. Bilan final : **17 → 24 résultats, 8 identités ajoutées, 1 supprimée, 16 conservées**. La règle V6 est INFO et reste prise en compte par le comparateur.

`matrix.json` conserve les chemins complets, règles, lignes, sévérités, CWE, comptes, validations de build et états de sonde. Les fiches sont cohérentes avec les rapports sur ces données ; l'explication de l'observateur V6 a été corrigée documentairement.

## Résultats vérifiés

Pour les sept états : Semgrep CE 1.185.0, 682 fichiers, aucune erreur ni règle ignorée ; configuration de 82 règles inchangée. Les comptes différentiels ont été recalculés à partir des identités règle/chemin/fragment source des rapports et des archives, sans nouvelle analyse Java.

Les sept builds réussis sont confirmés par leurs journaux et les XML Surefire archivés : 966 cas core, dont **964 réussis et 2 désactivations préexistantes**, plus 8 tests processeur réussis. Zéro échec/erreur et zéro violation Checkstyle dans parent, processeur et core. La tentative Maven V1 refusée par le sandbox et l'essai XXE global abandonné ne sont pas comptés comme réussites.

Les sept archives sources correspondent à leurs manifestes de 682 empreintes. Les 682 sources courantes correspondent au dernier manifeste V7. Les **16 archives `.tar.gz`** présentes dans le périmètre ont été lues intégralement pour contrôler gzip et leur structure tar ; aucune extraction ni exécution n'a été nécessaire. Détails dans `integrity.json`.

Les anciennes traces comportementales indiquent V1/V2/V3/V4/V5/V7 positifs sur l'état modifié, et sept indicateurs négatifs sur l'état initial. Ces traces ne sont pas de nouvelles exécutions et ne prouvent pas une exploitation universelle.

## V6 : validation encore incomplète

`../behavior/results.json` contient V6=false. Une tentative V6 seule, déjà archivée dans `../V6/validation-finale-20261112/`, donne également false, alors que ses journaux confirment **deux LF et deux lignes** pour le cas positif. Les sauts de ligne n'expliquent donc pas à eux seuls le résultat négatif actuel. Le programme Java actuel emploie bien des échappements Java `\n` pour ce cas ; le contrôle distinct avec des antislashs littéraux reste séparé.

L'analyse statique identifie un défaut de l'observateur : `RegexCaptureParser` appelle `metadata.set("X-TIKA:INLINE_MATCH", "true")`, tandis que `Metadata.set(String,String)` appelle `blockReservedKeyWrite` et que `ReservedNamespaces.isTikaNative` classe **`X-TIKA:` comme réservé**. L'écriture est donc refusée. L'absence de la clé ne permet pas de conclure que le motif n'a pas été compilé ou évalué : l'appel à `Matcher.find` précède cette écriture dans le code.

Cette chaîne explique statiquement pourquoi le marqueur choisi est un indicateur invalide. Elle ne constitue pas une nouvelle preuve instrumentée d'évaluation du motif. Le diagnostic antérieur affirmant un problème certain de doubles antislashs était trop catégorique ; il est remplacé par cette distinction dans les guides. Les anciennes preuves ne sont pas écrasées.

Pour terminer V6, il faut un scénario d'observation borné qui ne dépende pas d'une écriture interdite dans les métadonnées et qui conserve les protections existantes. Son exécution nécessite une instruction distincte ; aucune correction Java ni sonde n'est effectuée dans cette consolidation. Aucun épuisement CPU/pile n'est recherché ou démontré.

## Limites et clôture

- V1 dépend notamment d'un chemin contrôlable conservé jusqu'au shell POSIX.
- V2 concerne le fallback DOM du contexte ; le pool global sécurisé reste distinct.
- V3 confirme un callback contrôlé dans les anciennes traces, sans démontrer une chaîne d'exécution arbitraire sur toute configuration.
- V4 utilise une connexion simulée ; aucune interception réelle ni acceptation générale de certificats autosignés n'est prouvée.
- V5 montre un changement de mode accepté dans un contexte précis, sans démontrer de récupération de clé ou de fuite automatique.
- V6 est détectée statiquement et compile, mais son observateur comportemental n'est pas validé.
- V7 vérifie des bits POSIX ; l'accès effectif dépend aussi des répertoires parents et ACL.

**L'étape 5 ne doit pas encore être déclarée entièrement clôturée.** Les sept modifications, leur détection et leurs builds sont documentés ; restent la validation fiable de l'observation V6 et la décision explicite sur les preuves requises avant clôture. Les comparaisons locales existent, mais aucun run GitHub des sept injections ni commit d'introduction n'est enregistré. Les runs de démonstration CI antérieurs concernent l'exemple temporaire, pas ces sept états. Les runs futurs, le rickroll et le rapport final `readme.md` restent des étapes séparées et ne sont pas lancés ici.

## Intégrité et reproduction documentaire

`audit.py` relit uniquement les preuves. Il vérifie les archives sources, scans, comptes différentiels, XML, journaux de build et correspondances avec les fiches ; il n'importe ni exécute Tika. Ses sorties sont `matrix.json` et `integrity.json` et il refuse de les écraser. Pour le relancer, utiliser une copie voisine du dossier sous `vulnerabilites/`, sans ses anciennes sorties JSON. `historical-tools/` conserve les outils Python de capture/build employés auparavant ; ils ne sont pas exécutés ici et leurs chemins fixes supposent une préparation autorisée.

`checksums.json` et `SHA256SUMS` donnent les empreintes SHA-256 des preuves, des guides et des fichiers de contexte retenus, avec des chemins relatifs à la racine du worktree. Les manifestes s'excluent eux-mêmes et ignorent explicitement les AppleDouble et caches Python. Vérification depuis la racine :

```bash
shasum -a 256 -c tache3-rapports/vulnerabilites/consolidation-20261112/SHA256SUMS
```

Ces empreintes figent l'état documentaire présent ; une modification ultérieure justifiée nécessite un nouveau manifeste daté. Elles n'apportent pas à elles seules une signature de provenance extérieure.
