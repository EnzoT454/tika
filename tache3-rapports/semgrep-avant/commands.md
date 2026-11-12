# Semgrep avant injections — 10 novembre 2026

Base Git : `62ff2e9b180817f4b38108c4e90ffa05348c5a4d`, branche `tache3`. Aucun changement Java de production n'a été introduit. Les 682 sources courantes ont été copiées sur disque interne et inventoriées ; les chemins relatifs ont été conservés. Le diff Git des cinq répertoires de production est vide.

## Reproduire depuis le worktree complet

```bash
python3 -m venv /private/tmp/tika-semgrep-venv
/private/tmp/tika-semgrep-venv/bin/python -m pip install -r semgrep/requirements.txt
/private/tmp/tika-semgrep-venv/bin/python semgrep/scan.py --output /private/tmp/semgrep-result.json
```

Environnement utilisé : Python 3.11.15, `/private/tmp/tache3-semgrep-venv`, sources dans `/private/tmp/tache3-semgrep-before`. La commande exacte du moteur figure en première ligne de `scan.log`. Code de sortie final : 0. Les dépendances résolues sur macOS sont dans `python-packages.txt` ; ce relevé ne garantit pas des versions de dépendances identiques sur tous les systèmes.

## Choix

Semgrep CE 1.185.0, sans compte ni moteur Pro. Les 82 règles Java générales de sécurité du sous-arbre `java/lang/security` sont fixées à la révision `a5ad1714f00c9f7fde980cab0ea1d6dc6d414fd2`. Les règles d'audit sont incluses pour voir les opérations sensibles ; les règles propres à d'autres langages ou aux frameworks web ne sont pas retenues. Les identifiants sont uniques et leur réécriture est désactivée. Le verrou conserve les empreintes SHA-256 de l'archive et de chaque fichier. Les règles sous Semgrep Rules License v1.0 ne sont pas redistribuées : le script les télécharge et conserve leur notice localement.

Le périmètre est le Java de production de core, serialization, OCR, PDF et Microsoft. Les cibles explicites excluent tests, archives et sorties de construction. Les AppleDouble sont explicitement exclus. `--no-git-ignore` évite une dépendance au statut Git de la copie ; `--disable-nosem` évite les suppressions locales d'alertes. Le script exige que tous les fichiers attendus soient analysés, sans erreur ni règle ignorée. Moteur OSS, deux processus, timeout de 30 secondes par règle/fichier, pas de limite de taille de fichier ; métriques et vérification distante de version désactivées.

## Résultat et limites

17 alertes préexistantes : 8 ERROR et 9 WARNING ; 682 fichiers analysés, zéro erreur et zéro règle ignorée. Les 82 règles ont été exécutées. Une reproduction depuis un cache vide retrouve les mêmes 17 identités (règle, chemin, lignes). Les sources de la copie sont inchangées. Aucune alerte n'a été supprimée et aucun code de production n'a été corrigé.

Ces résultats ne démontrent pas 17 vulnérabilités exploitables. Voir `review.md`. CE ne fournit pas les garanties d'analyse interprocédurale/interfichier d'un moteur plus avancé. Ce scan ne couvre pas tout Tika, les ressources XML/configurations ni les dépendances vulnérables. Une absence d'alerte ne prouve pas l'absence de faiblesse.

Ce scan complet n'utilise pas `--error` : le code 0 signifie que l'analyse s'est terminée, malgré les 17 résultats signalés « blocking » dans le résumé. Le contrôle différentiel et ses codes de sortie restent à implémenter à l'étape suivante. Aucun workflow GitHub n'est modifié ou exécuté ici.

## Environnement et preuves

Le réseau du sandbox a d'abord empêché les téléchargements ; l'installation a été effectuée avec l'accès autorisé. Semgrep a rencontré un problème de magasin de certificats macOS ; le script utilise certifi si aucun magasin CA explicite n'est fourni, sans désactiver TLS. Un scan a ensuite été bloqué lors de l'écriture dans `~/.semgrep/semgrep.log` ; sa trace est conservée et la relance autorisée réussit. Ces erreurs techniques ne sont pas des résultats de sécurité.

`sources.tar.gz` conserve les sources scannées avec LICENSE.txt/NOTICE.txt Tika. `configuration.tar.gz` conserve scripts et verrou, sans contenus de règles. `semgrep.json` est le rapport brut ; `results.json` et `findings.json` sont dérivés. `checksums.json` vérifie les preuves. Les archives et le manifeste décrivent l'état exact, au-delà du seul SHA de base.
