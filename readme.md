# IFT3913 — Tâche 3 : bloquer les nouvelles alertes Semgrep

Aqel Hamza (**EnzoT454**) et Saidana Mohamed (**FakeTMG**).

Nous avons ajouté un contrôle Semgrep avant la construction de Tika. Il bloque les nouvelles alertes et conserve les rapports pour expliquer l'échec. Notre version contient huit modifications pédagogiques : le scan passe de **17 à 25 alertes**. Nous avons aussi vérifié qu'un test JUnit échoué déclenche le rickroll sans masquer l'échec. 

## 1. Périmètre et référence

Nous avons conservé [notre fork Tika](https://github.com/EnzoT454/tika/tree/tache3) et isolé la tâche sur `tache3`. Les huit modifications sont dans `tika-core` : V1 à V7 au [commit 5d37388](https://github.com/EnzoT454/tika/commit/5d37388b0cbf1baeb19c04a2e1c0ed633753765c), puis V8 au [commit aa5d406](https://github.com/EnzoT454/tika/commit/aa5d406bbded2dd73aecbc9c03fbb9d6f22da7a4). La référence avant injections est [860a263](https://github.com/EnzoT454/tika/commit/860a2631d9e78311387207ed9380275b2a151781). Le [workflow](.github/workflows/tache3-ift3913.yml), la [comparaison avant/après](#4-comparaison-et-validations) et le [run V8](https://github.com/EnzoT454/tika/actions/runs/43729952161) donnent les principaux points d'entrée pour vérifier les résultats.

L'environnement utilise Java 17, le wrapper Maven 3.9.12 et **Semgrep CE 1.185.0, moteur OSS**. Nous avons fixé 82 règles Java générales de sécurité à une révision et des empreintes précises, afin qu'un changement de règles ne soit pas confondu avec une nouvelle faiblesse du code. Le [verrou](semgrep/rules.lock.json) et le [périmètre](semgrep/scope.json) permettent de reproduire ce choix. Les règles sont téléchargées et vérifiées, sans redistribuer leur contenu.

Le scan couvre **682 fichiers Java de production** de core, serialization, OCR, PDF et Microsoft. Tests, archives et sorties générées sont exclus. Les 17 alertes initiales sont conservées dans le [rapport avant](tache3-rapports/semgrep-avant/semgrep.json) : ce sont des signalements à examiner, pas 17 vulnérabilités exploitables confirmées.

## 2. Fonctionnement du workflow

Le [workflow tâche 3](.github/workflows/tache3-ift3913.yml) ajoute un job de sécurité préalable à la construction core, sans remplacer les workflows historiques. Il récupère l'historique Git et choisit la référence selon l'événement : état `before` pour un push, ancêtre commun avec la cible pour une PR, SHA explicite pour un lancement manuel. Un push de plusieurs commits est ainsi comparé depuis son état précédent complet. Le checkout d'une PR analyse sa tête.

[`gate.py`](semgrep/gate.py) extrait les deux états et lance deux scans complets avec la même configuration. Nous comparons les alertes elles-mêmes, car leur nombre ne suffit pas. Avec V1, une ancienne alerte est remplacée par une nouvelle : le total reste identique, alors que le passage à `sh -c` introduit une injection shell. Chaque identité combine la règle, le chemin et l'empreinte du fragment signalé ; les occurrences sont comptées et les numéros de ligne sont ignorés.

Le contrôleur retourne **0 sans nouveauté, 1 avec nouveautés, 2 pour une erreur technique**. Un scan Semgrep terminé avec code 0 ne suffit donc pas à déclarer le contrôle réussi. Les erreurs, scans incomplets et références absentes ne sont pas masqués. Les rapports sont conservés avec `always()`, sans `continue-on-error` ; la construction dépend du succès de la sécurité.

Cette identité reste locale et syntaxique : un renommage ou une reformulation peut apparaître nouveau, et un remplacement strictement identique dans le même fichier à nombre constant peut être considéré conservé. Elle ne remplace pas une analyse sémantique du contexte.

## 3. Les modifications (V1 à V7, puis V8)

**V1 — Commande shell, CWE-78.** Nous avons choisi [ExternalParser.java](tika-core/src/main/java/org/apache/tika/parser/external/ExternalParser.java), car `parse` exécute déjà un programme externe avec le chemin du document. La liste d'arguments est maintenant jointe et transmise à `sh -c` sans échappement. Un chemin conservé depuis l'utilisateur peut injecter des métacaractères shell. La règle `command-injection-process-builder` signale le nouveau fragment. Elle signalait déjà l'ancien appel sans shell : une identité est ajoutée et une supprimée, avec un total inchangé. [Fiche et preuves V1](tache3-rapports/vulnerabilites/V1/README.md).

**V2 — XXE, CWE-611.** Le fallback `getDocumentBuilder(ParseContext)` de [XMLReaderUtils.java](tika-core/src/main/java/org/apache/tika/utils/XMLReaderUtils.java) fournit un lecteur DOM existant. Sa nouvelle fabrique accepte les DOCTYPE et l'expansion d'entités, sans résolveur restrictif. Un XML non fiable peut demander une ressource accessible au processus, selon les réglages JAXP. Le pool global reste distinct. Deux nouvelles règles détectent ce même type : `documentbuilderfactory-disallow-doctype-decl-false` et `documentbuilderfactory-disallow-doctype-decl-missing`. [Fiche V2](tache3-rapports/vulnerabilites/V2/README.md).

**V3 — Désérialisation, CWE-502.** Dans [ParseContext.java](tika-core/src/main/java/org/apache/tika/parser/ParseContext.java), l'entrée de configuration brute accepte désormais `java-serialized:` suivi de Base64. `ObjectInputStream.readObject()` est appelé sans filtre local avant la conversion vers `JsonConfig`. Un callback peut donc s'exécuter avant cette conversion. Le risque suppose une configuration non fiable et des classes appropriées disponibles. La règle `object-deserialization` ajoute une alerte ; la sonde locale démontre un callback contrôlé, pas une chaîne arbitraire d'exécution de commandes. [Fiche V3](tache3-rapports/vulnerabilites/V3/README.md).

**V4 — Nom d'hôte TLS, CWE-295.** [TikaInputStream.java](tika-core/src/main/java/org/apache/tika/io/TikaInputStream.java) ouvre réellement la connexion de lecture d'un document distant. Nous lui attachons un `HostnameVerifier` retournant toujours true : le certificat peut correspondre à un autre nom. La chaîne de confiance reste vérifiée ; tous les certificats autosignés ne sont pas acceptés. `insecure-hostname-verifier` ajoute une alerte. La vérification locale utilise une connexion simulée. [Fiche V4](tache3-rapports/vulnerabilites/V4/README.md).

**V5 — AES/ECB, CWE-327.** [CryptoParser.java](tika-core/src/main/java/org/apache/tika/parser/CryptoParser.java) déchiffre de vrais contenus, ce qui rend le choix cryptographique pertinent. Toute transformation AES demandée est remplacée par `Cipher.getInstance("AES")`, soit ECB par défaut avec SunJCE. `use-of-default-aes` ajoute une alerte. La sonde accepte du contenu ECB alors que GCM est demandé, dans un contexte sans paramètres incompatibles. Cela ne démontre ni récupération de clé ni fuite automatique d'un document correctement chiffré. [Fiche V5](tache3-rapports/vulnerabilites/V5/README.md).

**V6 — Motif contrôlé et risque ReDoS, CWE-1333.** [RegexCaptureParser.java](tika-core/src/main/java/org/apache/tika/parser/RegexCaptureParser.java) applique normalement des motifs de configuration aux documents. La première ligne `regex:MOTIF` permet maintenant au document de fournir lui-même une expression régulière, appliquée aux lignes suivantes. Un motif coûteux peut mobiliser les ressources du parser. `java-pattern-from-string-parameter` ajoute une alerte **INFO**, mais notre vérification locale **ne démontre pas de ReDoS**. Nous conservons ce cas comme un risque identifié, avec une validation comportementale incomplète ; le diagnostic est détaillé dans la [fiche V6](tache3-rapports/vulnerabilites/V6/README.md).

**V7 — Permissions excessives, CWE-276.** [TemporaryResources.java](tika-core/src/main/java/org/apache/tika/io/TemporaryResources.java) crée les fichiers qui peuvent contenir les documents mis en cache. Après création, leurs droits POSIX deviennent `rw-rw-rw-`. D'autres comptes peuvent lire ou modifier le contenu si les répertoires parents et ACL le permettent. `overly-permissive-file-permission` ajoute une alerte ; la sonde confirme les bits de permissions, sans accès entre comptes. [Fiche V7](tache3-rapports/vulnerabilites/V7/README.md).

**V8 — Empreinte faible MD5, CWE-328.** Nous avons ajouté un huitième cas, le repli silencieux vers MD5, pour compléter la démonstration avec une faiblesse distincte détectée par une règle absente du rapport initial. [InputStreamDigester.java](tika-core/src/main/java/org/apache/tika/digest/InputStreamDigester.java) calcule les empreintes des documents, qui peuvent servir à vérifier leur intégrité ou à repérer des doublons. Un algorithme indisponible levait une exception ; il est maintenant remplacé silencieusement par `MessageDigest.getInstance("MD5")`, qui n'est pas résistant aux collisions. L'appelant reçoit une empreinte MD5 sans avertissement. Le risque dépend de la confiance accordée à cette empreinte. `use-of-md5` ajoute une alerte WARNING ; aucune collision n'a été fabriquée. [Fiche et rapports V8](tache3-rapports/vulnerabilites/V8/README.md).

## 4. Comparaison et validations

Le résultat final est **17 → 25 alertes**. Les [rapports avant/après de V1 à V7](tache3-rapports/comparaisons/etape-06-20261112/README.md) expliquent le premier passage de 17 à 24 : huit identités ajoutées, une supprimée et seize conservées. V2 produit deux alertes pour un seul type ; l'ancienne identité V1 disparaît par remplacement, pas par correction.

V8 ajoute ensuite une seule alerte, `use-of-md5` dans `InputStreamDigester.java`. Le [run 43729952161](https://github.com/EnzoT454/tika/actions/runs/43729952161) compare `5edd9c9` à `aa5d406` : **24 → 25**, un ajout, aucune suppression et 24 alertes conservées. Le contrôle retourne 1 et la construction est ignorée. Les [rapports Semgrep bruts et le journal GitHub](tache3-rapports/vulnerabilites/V8/github-43729952161/) sont archivés avec leurs empreintes.

| Cas GitHub | Résultat observé | Run |
|---|---|---|
| Référence sans injections | Sécurité et tests réussis, aucun rickroll | [43276322587](https://github.com/EnzoT454/tika/actions/runs/43276322587) |
| Échec JUnit volontaire | Rickroll exécuté, build toujours échoué | [43276898487](https://github.com/EnzoT454/tika/actions/runs/43276898487) |
| Erreur de compilation | Build échoué, rickroll ignoré | [43277596512](https://github.com/EnzoT454/tika/actions/runs/43277596512) |
| Retrait du test temporaire | Retour au vert, aucun rickroll | [43278025406](https://github.com/EnzoT454/tika/actions/runs/43278025406) |
| Sept modifications présentes | 17 → 24, code sécurité 1, construction ignorée | [43278937793](https://github.com/EnzoT454/tika/actions/runs/43278937793) |
| Ajout de V8 | 24 → 25, un ajout MD5, code sécurité 1, construction ignorée | [43729952161](https://github.com/EnzoT454/tika/actions/runs/43729952161) |

Les sept états ont aussi été construits localement : chacun donne **964 tests core et 8 tests processeur réussis**, deux désactivations préexistantes et zéro violation Checkstyle. 14 cas locaux vérifient notamment déplacements de lignes, total constant, push multi-commits et erreurs techniques. La [matrice consolidée](tache3-rapports/rapport-final-20261112/validation-matrix.md) distingue ces preuves de la limite V6. Les JSON, XML, journaux, SHA et empreintes sont conservés localement pour ne pas dépendre de la durée de rétention des artefacts GitHub.

Pour V8, la fiche consigne aussi un build local réussi, avec 966 cas core dont deux désactivés. Le journal Maven et les XML de ce build ne sont pas archivés ; ce résultat ne dispose donc pas du même niveau de preuve que les sept builds précédents. Le run GitHub V8 confirme le blocage Semgrep, sans exécuter Maven.

## 5. Rickroll réutilisable

L'[action composite locale](.github/actions/rickroll/action.yml) appelle `ift3913-rickroll.sh` : six poses ASCII, message rétro et lien vidéo dans les logs et le résumé. Il s'agit d'images textuelles successives, sans lecture vidéo automatique ni téléchargement.

Le [détecteur Surefire](.github/scripts/tache3-test-status.py) pose un marqueur avant Maven puis compte les nouveaux cas `failure`/`error`, en vérifiant les compteurs XML. Les rapports anciens sont ignorés. Cela distingue un test échoué d'une erreur de compilation ou de téléchargement. L'action est appelée avec `uses` après cet échec confirmé ; elle retourne 0 pour son affichage, tandis que l'étape Maven reste rouge. Neuf cas ciblés du détecteur ont été validés localement. Les tests de démonstration ont été retirés de la branche de test ; ils restent archivés hors de la suite normale.

## 6. Reproduction, travail commun et assistance IA

Les commandes de reproduction figurent dans [semgrep/README.md](semgrep/README.md). Avec Python 3.11, la comparaison complète peut être relancée depuis un clone disposant des deux commits :

```bash
python3 -m venv /private/tmp/tika-semgrep-repro
/private/tmp/tika-semgrep-repro/bin/python -m pip install -r semgrep/requirements.txt
/private/tmp/tika-semgrep-repro/bin/python semgrep/gate.py --base 860a2631d9e78311387207ed9380275b2a151781 --head aa5d406bbded2dd73aecbc9c03fbb9d6f22da7a4 --output-dir /private/tmp/tika-semgrep-comparaison-neuve
```

Cette commande vise l'état complet V1 à V8 : 17 → 25 alertes, avec un code attendu de 1. Le dossier de sortie doit être neuf. Pour reproduire séparément le run d'ajout de V8, utiliser `5edd9c9da85d30c2c63ddde265023313855690d8` comme base et le même SHA de tête : 24 → 25 alertes, code attendu 1. Ces commandes sont prévues pour Linux ou macOS.

Nous avons réalisé le travail ensemble et vérifié les résultats au fur et à mesure. **Codex a contribué à la conception et à l'implémentation du workflow, des modifications pédagogiques et des scripts, aux vérifications locales et à la rédaction.** Nous avons lancé les runs GitHub Actions, vérifié les résultats et analysé les alertes et les journaux. Les limites rencontrées sont indiquées dans le rapport et les fiches.

La présentation originale d'Apache Tika reste dans [README-tika-original.md](README-tika-original.md).
