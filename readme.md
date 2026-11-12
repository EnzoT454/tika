# IFT3913 — Tâche 3 : bloquer les nouvelles alertes Semgrep

Aqel Hamza (**EnzoT454**) et Saidana Mohamed (**FakeTMG**).

Notre workflow GitHub Actions échoue lorsqu'une modification ajoute une alerte Semgrep par rapport à une référence Git. La démonstration finale donne **17 → 24 résultats : 8 identités ajoutées, 1 supprimée et 16 conservées**, pour sept types distincts documentés. Le rickroll rétro se déclenche après un échec JUnit et conserve le build rouge. Cette branche contient volontairement les modifications vulnérables demandées pour le cours.

## 1. Périmètre et référence

Nous avons conservé [notre fork Tika](https://github.com/EnzoT454/tika/tree/tache3) et isolé la tâche sur `tache3`. Les sept modifications sont dans `tika-core`, au [commit 5d37388](https://github.com/EnzoT454/tika/commit/5d37388b0cbf1baeb19c04a2e1c0ed633753765c). La référence avant injections est [860a263](https://github.com/EnzoT454/tika/commit/860a2631d9e78311387207ed9380275b2a151781) ; le run final analyse [c20318f](https://github.com/EnzoT454/tika/commit/c20318f65f5fabb3a59be08f8cb8529ea06ca6b8).

L'environnement utilise Java 17, le wrapper Maven 3.9.12 et **Semgrep CE 1.185.0, moteur OSS**. Nous avons fixé 82 règles Java générales de sécurité à une révision et des empreintes précises, afin qu'un changement de règles ne soit pas confondu avec une nouvelle faiblesse du code. Le [verrou](semgrep/rules.lock.json) et le [périmètre](semgrep/scope.json) permettent de reproduire ce choix. Les règles sont téléchargées et vérifiées, sans redistribuer leur contenu.

Le scan couvre **682 fichiers Java de production** de core, serialization, OCR, PDF et Microsoft. Tests, archives et sorties générées sont exclus. Les 17 alertes initiales sont conservées dans le [rapport avant](tache3-rapports/semgrep-avant/semgrep.json) : ce sont des signalements à examiner, pas 17 vulnérabilités exploitables confirmées.

## 2. Fonctionnement du workflow

Le [workflow tâche 3](.github/workflows/tache3-ift3913.yml) ajoute un job de sécurité préalable à la construction core, sans remplacer les workflows historiques. Il récupère l'historique Git et choisit la référence selon l'événement : état `before` pour un push, ancêtre commun avec la cible pour une PR, SHA explicite pour un lancement manuel. Un push de plusieurs commits est ainsi comparé depuis son état précédent complet. Le checkout d'une PR analyse sa tête.

[`gate.py`](semgrep/gate.py) extrait les deux états et lance deux scans complets avec la même configuration. Une identité combine règle, chemin et empreinte du fragment signalé ; les occurrences sont comptées et les numéros de ligne sont ignorés. Cela évite de confondre un déplacement de lignes avec un ajout, ou de manquer une nouveauté lorsque le total reste identique.

Le contrôleur retourne **0 sans nouveauté, 1 avec nouveautés, 2 pour une erreur technique**. Un scan Semgrep terminé avec code 0 ne suffit donc pas à déclarer le contrôle réussi. Les erreurs, scans incomplets et références absentes ne sont pas masqués. Les rapports sont conservés avec `always()`, sans `continue-on-error` ; la construction dépend du succès de la sécurité.

Cette identité reste locale et syntaxique : un renommage ou une reformulation peut apparaître nouveau, et un remplacement strictement identique dans le même fichier à nombre constant peut être considéré conservé. Elle ne remplace pas une analyse sémantique du contexte.

## 3. Les sept modifications

**V1 — Commande shell, CWE-78.** Nous avons choisi [ExternalParser.java](tika-core/src/main/java/org/apache/tika/parser/external/ExternalParser.java), car `parse` exécute déjà un programme externe avec le chemin du document. La liste d'arguments est maintenant jointe et transmise à `sh -c` sans échappement. Un chemin conservé depuis l'utilisateur peut injecter des métacaractères shell. La règle `command-injection-process-builder` signale le nouveau fragment. Elle signalait déjà l'ancien appel sans shell : une identité est ajoutée et une supprimée, avec un total inchangé. [Fiche et preuves V1](tache3-rapports/vulnerabilites/V1/README.md).

**V2 — XXE, CWE-611.** Le fallback `getDocumentBuilder(ParseContext)` de [XMLReaderUtils.java](tika-core/src/main/java/org/apache/tika/utils/XMLReaderUtils.java) fournit un lecteur DOM existant. Sa nouvelle fabrique accepte les DOCTYPE et l'expansion d'entités, sans résolveur restrictif. Un XML non fiable peut demander une ressource accessible au processus, selon les réglages JAXP. Le pool global reste distinct. Deux nouvelles règles détectent ce même type : `documentbuilderfactory-disallow-doctype-decl-false` et `documentbuilderfactory-disallow-doctype-decl-missing`. [Fiche V2](tache3-rapports/vulnerabilites/V2/README.md).

**V3 — Désérialisation, CWE-502.** Dans [ParseContext.java](tika-core/src/main/java/org/apache/tika/parser/ParseContext.java), l'entrée de configuration brute accepte désormais `java-serialized:` suivi de Base64. `ObjectInputStream.readObject()` est appelé sans filtre local avant la conversion vers `JsonConfig`. Un callback peut donc s'exécuter avant cette conversion. Le risque suppose une configuration non fiable et des classes appropriées disponibles. La règle `object-deserialization` ajoute une alerte ; la sonde locale démontre un callback contrôlé, pas une chaîne arbitraire d'exécution de commandes. [Fiche V3](tache3-rapports/vulnerabilites/V3/README.md).

**V4 — Nom d'hôte TLS, CWE-295.** [TikaInputStream.java](tika-core/src/main/java/org/apache/tika/io/TikaInputStream.java) ouvre réellement la connexion de lecture d'un document distant. Nous lui attachons un `HostnameVerifier` retournant toujours true : le certificat peut correspondre à un autre nom. La chaîne de confiance reste vérifiée ; tous les certificats autosignés ne sont pas acceptés. `insecure-hostname-verifier` ajoute une alerte. La vérification locale utilise une connexion simulée. [Fiche V4](tache3-rapports/vulnerabilites/V4/README.md).

**V5 — AES/ECB, CWE-327.** [CryptoParser.java](tika-core/src/main/java/org/apache/tika/parser/CryptoParser.java) déchiffre de vrais contenus, ce qui rend le choix cryptographique pertinent. Toute transformation AES demandée est remplacée par `Cipher.getInstance("AES")`, soit ECB par défaut avec SunJCE. `use-of-default-aes` ajoute une alerte. La sonde accepte du contenu ECB alors que GCM est demandé, dans un contexte sans paramètres incompatibles. Cela ne démontre ni récupération de clé ni fuite automatique d'un document correctement chiffré. [Fiche V5](tache3-rapports/vulnerabilites/V5/README.md).

**V6 — Motif contrôlé et risque ReDoS, CWE-1333.** [RegexCaptureParser.java](tika-core/src/main/java/org/apache/tika/parser/RegexCaptureParser.java) applique normalement des motifs de configuration aux documents. La première ligne `regex:MOTIF` permet maintenant au document de fournir un motif Java sans restriction, appliqué aux lignes suivantes. `java-pattern-from-string-parameter` ajoute une alerte **INFO**. La détection et la compilation sont confirmées, mais **le comportement ReDoS n'est pas démontré**. Le marqueur de la sonde est rejeté par les métadonnées réservées ; son absence ne prouve pas l'absence d'évaluation. Cette limite est conservée. [Fiche V6](tache3-rapports/vulnerabilites/V6/README.md).

**V7 — Permissions excessives, CWE-276.** [TemporaryResources.java](tika-core/src/main/java/org/apache/tika/io/TemporaryResources.java) crée les fichiers qui peuvent contenir les documents mis en cache. Après création, leurs droits POSIX deviennent `rw-rw-rw-`. D'autres comptes peuvent lire ou modifier le contenu si les répertoires parents et ACL le permettent. `overly-permissive-file-permission` ajoute une alerte ; la sonde confirme les bits de permissions, sans accès entre comptes. [Fiche V7](tache3-rapports/vulnerabilites/V7/README.md).

## 4. Comparaison et validations

Les [rapports avant/après et associations](tache3-rapports/comparaisons/etape-06-20261112/README.md) expliquent les huit ajouts : deux pour V2, un pour chacune des six autres modifications. L'ancienne identité V1 disparaît par remplacement, pas par correction. Le run GitHub final retrouve exactement ces résultats et bloque la construction avec le code 1.

| Cas GitHub | Résultat observé | Run |
|---|---|---|
| Référence sans injections | Sécurité et tests réussis, aucun rickroll | [43276322587](https://github.com/EnzoT454/tika/actions/runs/43276322587) |
| Échec JUnit volontaire | Rickroll exécuté, build toujours échoué | [43276898487](https://github.com/EnzoT454/tika/actions/runs/43276898487) |
| Erreur de compilation | Build échoué, rickroll ignoré | [43277596512](https://github.com/EnzoT454/tika/actions/runs/43277596512) |
| Retrait du test temporaire | Retour au vert, aucun rickroll | [43278025406](https://github.com/EnzoT454/tika/actions/runs/43278025406) |
| Sept modifications présentes | 17 → 24, code sécurité 1, construction ignorée | [43278937793](https://github.com/EnzoT454/tika/actions/runs/43278937793) |

Les sept états ont aussi été construits localement : chacun donne **964 tests core et 8 tests processeur réussis**, deux désactivations préexistantes et zéro violation Checkstyle. Quatorze cas locaux vérifient notamment déplacements de lignes, total constant, push multi-commits et erreurs techniques. La [matrice consolidée](tache3-rapports/rapport-final-20261112/validation-matrix.md) distingue ces preuves de la limite V6. Les JSON, XML, journaux, SHA et empreintes sont conservés localement pour ne pas dépendre de la durée de rétention des artefacts GitHub.

## 5. Rickroll réutilisable

L'[action composite locale](.github/actions/rickroll/action.yml) appelle `ift3913-rickroll.sh` : six poses ASCII, message rétro et lien vidéo dans les logs et le résumé. Il s'agit d'images textuelles successives, sans lecture vidéo automatique ni téléchargement.

Le [détecteur Surefire](.github/scripts/tache3-test-status.py) pose un marqueur avant Maven puis compte les nouveaux cas `failure`/`error`, en vérifiant les compteurs XML. Les rapports anciens sont ignorés. Cela distingue un test échoué d'une erreur de compilation ou de téléchargement. L'action est appelée avec `uses` après cet échec confirmé ; elle retourne 0 pour son affichage, tandis que l'étape Maven reste rouge. Neuf cas ciblés du détecteur ont été validés localement. Les tests de démonstration ont été retirés de la branche de test ; ils restent archivés hors de la suite normale.

## 6. Reproduction, travail commun et assistance IA

Les commandes de reproduction figurent dans [semgrep/README.md](semgrep/README.md). Avec Python 3.11, la comparaison complète peut être relancée depuis un clone disposant des deux commits :

```bash
python3 -m venv /private/tmp/tika-semgrep-repro
/private/tmp/tika-semgrep-repro/bin/python -m pip install -r semgrep/requirements.txt
/private/tmp/tika-semgrep-repro/bin/python semgrep/gate.py --base 860a2631d9e78311387207ed9380275b2a151781 --head c20318f65f5fabb3a59be08f8cb8529ea06ca6b8 --output-dir /private/tmp/tika-semgrep-comparaison-neuve
```

Le dossier de sortie doit être neuf ; le code attendu est 1. L'analyse ne couvre pas toutes les dépendances ni tous les chemins d'exécution de Tika. La protection de branche nécessaire pour interdire une fusion n'a pas été vérifiée.

**Codex a contribué à la conception et à l'implémentation du workflow, des modifications pédagogiques et des scripts, aux vérifications locales et à la rédaction.** Les validations GitHub ont été lancées par le binôme ; les limites et résultats négatifs sont déclarés plutôt que présentés comme des réussites.

Le [rapport de tâche 2 est conservé](tache3-rapports/rapport-final-20261112/readme-tache2-avant.md), avec sa [version Git immuable](https://github.com/EnzoT454/tika/blob/f7bc2acbe7fa48895077fef999d002c548a88569/readme.md). La présentation originale d'Apache Tika reste dans [README-tika-original.md](README-tika-original.md).
