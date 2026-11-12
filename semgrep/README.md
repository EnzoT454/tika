# Scan de référence — tâche 3

Depuis la racine du worktree complet, avec Python 3.11 :

```bash
python3 -m venv /private/tmp/tika-semgrep-venv
/private/tmp/tika-semgrep-venv/bin/python -m pip install -r semgrep/requirements.txt
/private/tmp/tika-semgrep-venv/bin/python semgrep/scan.py --output /private/tmp/semgrep-result.json
```

Semgrep CE est fixé à 1.185.0. `rules.lock.json` identifie la révision et les empreintes des 82 règles Java générales de sécurité, y compris les règles d'audit. Elles sont téléchargées dans un cache temporaire et vérifiées avant utilisation ; aucun compte Semgrep n'est requis. Les métriques et la vérification distante de version sont désactivées.

Les règles upstream sont sous Semgrep Rules License v1.0 : elles ne sont pas redistribuées dans ce dépôt. Leur notice est conservée dans le cache local. Source et conditions : https://github.com/semgrep/semgrep-rules et https://semgrep.dev/legal/rules-license/.

`scope.json` couvre uniquement le Java de production des cinq modules autorisés. Tests, archives, fichiers de construction et AppleDouble sont hors périmètre. Le script ignore les suppressions `nosemgrep` et ne dépend pas de `.gitignore` ; il vérifie que chaque source Java attendue a été analysée, sans erreur ni règle ignorée. L'analyse CE ne garantit pas le suivi de données entre fichiers ou méthodes.

Ce script produit un **scan complet**. Une analyse complète peut terminer avec le code 0 malgré des alertes. Le contrôle bloquant est assuré séparément par `gate.py`.

Le rapport initial et ses limites sont conservés dans `tache3-rapports/semgrep-avant/`. Les règles et le périmètre doivent rester identiques lors de la comparaison après injections.

## Contrôle différentiel

Depuis un dépôt avec les deux commits disponibles, utiliser des SHA complets :

```bash
/private/tmp/tache3-semgrep-venv/bin/python semgrep/gate.py \
  --base SHA_REFERENCE --head SHA_ANALYSE \
  --output-dir /private/tmp/comparaison-neuve
```

Le répertoire de sortie doit être neuf. Le script analyse les **sources suivies des commits**, pas les modifications locales non commitées. Il extrait les sources Java dans deux dossiers temporaires sans toucher au worktree ni exécuter les scripts du commit de référence. Les deux scans complets utilisent la configuration du contrôleur courant, y compris lorsque la référence est antérieure à l'ajout de Semgrep. Une modification future du périmètre ou des règles impose une nouvelle référence académique comparable.

Chaque alerte est identifiée par sa règle, son chemin et le SHA-256 du fragment source signalé (positions en octets du JSON). Les occurrences identiques sont comptées : un doublon supplémentaire reste une nouveauté. Les numéros de ligne ne participent pas à l'identité. L'empreinte JSON de CE vaut `requires login` dans la version retenue ; elle n'est donc pas utilisée. Ce mécanisme local est explicite et ne prétend pas reproduire le suivi sémantique de la plateforme Semgrep.

Codes de sortie : **0** sans nouveauté, **1** avec nouveautés, **2** en cas d'erreur technique (référence absente, configuration invalide, moteur en échec, analyse incomplète). Les alertes préexistantes restent visibles. Une suppression n'annule pas un ajout différent. Les JSON `before.json`, `after.json`, `diff.json`, les manifestes SHA-256, les journaux et `summary.json`/`summary.md` sont conservés même en cas d'échec ; si le premier scan échoue, le rapport après peut être absent.

Limites de l'identité : un renommage de fichier ou une reformulation du fragment signalé peut produire une nouveauté même sans nouvelle faiblesse. Un remplacement par un fragment strictement identique dans le même fichier, à nombre d'occurrences constant, est considéré comme conservé ; ce mécanisme ne suit pas l'identité d'une méthode ou le contexte de données au-delà du fragment signalé. Les JSON bruts et la revue de chaque injection restent nécessaires. Les garanties d'analyse sont celles du moteur OSS et des 82 règles retenues.

## GitHub Actions

`.github/workflows/tache3-ift3913.yml` reprend la construction `tika-core` de tâche 2 et lui ajoute un job de sécurité préalable. Le workflow tâche 2 et les workflows Apache sont préservés. Les événements push/PR ciblent `main` et `tache3`, sans filtre de chemins qui pourrait éviter le contrôle.

- **Push** : `before` → `after` de l'événement complet, y compris plusieurs commits et les pushs forcés si les objets sont accessibles.
- **PR** : ancêtre commun de `pull_request.base.sha` et `pull_request.head.sha` → SHA de tête de la PR. Le checkout utilise cette tête, pas le commit de fusion synthétique de GitHub.
- **Manuel** : entrée obligatoire `baseline` (SHA complet) → HEAD sélectionné pour le run.
- **Premier push** (SHA précédent nul), objet absent ou absence d'ancêtre commun : erreur technique explicite. Aucun repli vers `HEAD^` ou vers un succès sans analyse. Utiliser un run manuel avec une référence connue pour une première démonstration.

Le checkout récupère l'historique complet ; seules les permissions `contents: read` sont demandées, sans jeton Semgrep. Les étapes ne masquent pas les échecs. La construction dépend du succès de la sécurité ; les artefacts de sécurité sont téléversés avec `always()`. La construction conserve `pipefail` et ses rapports. Le rickroll est intégré et validé sur GitHub, avec détection des cas JUnit échoués distincte des erreurs techniques.

Les champs d'événement sont lus depuis `GITHUB_EVENT_PATH` par Python ; ils ne sont pas interpolés dans du code shell. Références : [événements GitHub Actions](https://docs.github.com/en/actions/reference/workflows-and-actions/events-that-trigger-workflows) et [checkout d'une tête de PR](https://github.com/actions/checkout#checkout-pull-request-head-commit-instead-of-merge-commit).

## Validation locale

`validate_gate.py` crée un dépôt Git **temporaire** à partir des sources Java du HEAD réel. Il y crée un historique de test et des alertes synthétiques jamais exécutées, puis invoque le vrai contrôleur avec le vrai moteur et les 82 règles. Il ne crée aucun commit dans le dépôt de travail.

```bash
/private/tmp/tache3-semgrep-venv/bin/python semgrep/validate_gate.py \
  --output-dir /private/tmp/validation-neuve \
  --rules-dir /private/tmp/tache3-semgrep-rule-cache
```

Le cache doit être présent ; `scan.py` vérifie ses empreintes ou télécharge les règles. Le script couvre même version, changement bénin, déplacement de lignes, nouveauté, doublon, suppression+ajout à total identique, correction, push de plusieurs commits, PR divergente, référence absente, premier push, déclenchement manuel, configuration invalide et source Java malformée. Les rapports et résultats sont archivés dans `tache3-rapports/validation-ci/`. Le bundle Git de validation contient seulement le dépôt de test et permet de retrouver ses SHA ; les règles upstream n'y figurent pas.

La démonstration GitHub initiale du 12 novembre est archivée dans [validation-ci/github](../tache3-rapports/validation-ci/github/README.md). La [consolidation finale](../tache3-rapports/rapport-final-20261112/README.md) ajoute la validation des sept modifications : [run 43278937793](https://github.com/EnzoT454/tika/actions/runs/43278937793), code 1, 17 → 24, huit ajouts, une suppression et seize résultats conservés. Les quatre cas du rickroll sont également confirmés à distance. La limite comportementale V6 et l'absence de vérification des protections de branche restent déclarées.

<!-- Validation CI : changement documentaire sans effet sur le scan. -->
