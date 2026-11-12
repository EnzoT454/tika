# Contrôle différentiel — validation locale du 12 novembre 2026

Branche réelle : `tache3`, HEAD `62ff2e9b180817f4b38108c4e90ffa05348c5a4d`. Aucun commit, push ou run GitHub dans le dépôt de travail. Les commits des résultats appartiennent exclusivement au dépôt temporaire de validation.

## Implémentation et choix

`semgrep/gate.py` extrait deux instantanés Git et appelle `scan.py` pour chacun, avec exactement la même configuration courante. Les scripts de la référence ne sont pas exécutés. Le scan accepte maintenant `--source-root`, désactive également la vérification distante de version pour `--version`, et limite la durée du contrôle de version et du scan.

La comparaison utilise règle, chemin et SHA-256 du fragment signalé, sans numéro de ligne, en conservant le nombre d'occurrences. L'empreinte JSON CE est indisponible sans connexion (`requires login`). Cette comparaison locale ne suit pas les méthodes ou le flux de données au-delà du fragment ; renommages/reformulations peuvent apparaître nouveaux, et remplacer un fragment par un identique dans le même fichier à nombre constant est considéré comme conservé. Voir `semgrep/README.md` pour les limites.

Le workflow tâche 3 reprend le build core du workflow tâche 2 dans un fichier distinct et ajoute la sécurité comme dépendance préalable. Les workflows existants sont préservés. Référence push : `before` complet ; PR : ancêtre commun avec la cible ; manuel : SHA explicite. Une référence absente ou nulle est une erreur technique, sans repli silencieux. Les artefacts sont conservés avec `always()`, sans masquer le code de sortie.

## Commande exécutée

```bash
/private/tmp/tache3-semgrep-venv/bin/python semgrep/validate_gate.py \
  --output-dir /private/tmp/tache3-gate-validation-final \
  --rules-dir /private/tmp/tache3-semgrep-rule-cache
```

Semgrep CE 1.185.0, 82 règles verrouillées, moteur OSS. Chaque cas valide utilise deux scans complets, sans mocks. Les cas d'erreur s'arrêtent dès que le problème est rencontré. Le Java temporaire qui déclenche une alerte n'est jamais exécuté ; il ne constitue pas l'une des sept injections définitives.

## Résultats observés

- Même code, changement bénin et décalage des lignes : code 0, 17 alertes conservées, zéro ajout.
- Une nouvelle alerte : code 1, 17 → 18 alertes, un ajout `command-injection-process-builder`.
- Occurrence supplémentaire du même fragment : code 1, 18 → 19, un ajout.
- Suppression d'une ancienne alerte et ajout d'une autre : code 1, 17 → 17, un ajout et une suppression.
- Correction : code 0, 18 → 17, une suppression et aucun ajout.
- Push de plusieurs commits : code 1, comparaison depuis `before`, deux ajouts.
- PR avec cible divergente : code 1, ancêtre commun correctement choisi, un ajout.
- Référence absente et premier push sans référence : code 2 explicite.
- Déclenchement manuel avec référence explicite : code 0.
- Configuration JSON invalide et scan d'une source malformée : code 2, jamais présentés comme détection de sécurité réussie.

**14 scénarios réussis.** Détails, SHA temporaires et codes réellement observés dans `results.json`. Les 682 empreintes initiales correspondent au rapport Semgrep avant et aux sources courantes. Le YAML a été lu avec ruamel.yaml et sa structure vérifiée (événements, dépendance, permissions, profondeur Git, absence de `continue-on-error`) ; les sources Python ont été analysées syntaxiquement.

Une première tentative a été interrompue avant le premier scan, lors du démarrage sans désactivation explicite de la vérification distante pour `--version`. La commande de version avec `SEMGREP_ENABLE_VERSION_CHECK=0` a réussi ; la relance complète ci-dessus est celle des 14 résultats. Les fichiers de la tentative interrompue sont archivés séparément. Un premier essai de lecture YAML avec PyYAML a échoué car ce paquet n'est pas installé ; la vérification réussie utilise ruamel.yaml déjà présent.

## Archives et limites

`runs.tar.gz` contient JSON bruts, manifestes, journaux, événements synthétiques et `fixtures.bundle` (historique Git jetable permettant de retrouver les SHA). Les notices Tika sont jointes. `configuration.tar.gz` conserve les scripts, workflow, périmètre et verrou exacts, sans contenu des règles upstream. `checksums.json` vérifie les fichiers archivés.

Pour examiner l'historique après extraction : `git clone fixtures.bundle copie-validation` depuis le dossier contenant le bundle. Les SHA sont dans `results.json` ; le contrôleur peut être appelé avec `--repo copie-validation --base SHA --head SHA --output-dir DOSSIER_NEUF`. Le contrôleur ne tient pas compte des modifications non commitées ; le test d'une modification locale exige cette copie Git isolée.

Le diff global a rencontré une ressource `droste.zip` illisible dans les tests du module pkg, extérieur au périmètre ; aucun état global propre n'est affirmé. Les vérifications ciblées et les 682 empreintes réussissent. Les commandes d'inventaire Python excluent les fichiers AppleDouble `._*`.

Aucun build Maven supplémentaire n'a été exécuté : les sources réelles n'ont pas changé, la construction initiale reste la preuve disponible. Aucun actionlint ni runner GitHub n'a été exécuté. Installation Ubuntu, téléchargement des actions/règles, téléversement des artefacts, statut du workflow et éventuelle protection de branche restent à vérifier à distance après autorisation. Le rickroll, les sept injections et le rapport final ne font pas partie de cette étape.
