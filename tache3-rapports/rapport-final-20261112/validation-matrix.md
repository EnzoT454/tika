# Matrice finale de validation

Les lignes regroupent des essais antérieurs et des synthèses qui se recoupent. Les 26 lignes réussies ne sont donc pas 26 nouvelles exécutions indépendantes. Cette consolidation ne lance aucun test.

| Scénario | État vérifié | Observation et périmètre | Preuve |
|---|---|---|---|
| same | Réussie | Code 0. validation locale antérieure du contrôleur sur historique synthétique | [preuve](../comparaisons/etape-06-20261112/evidence/local-ci-results.json) |
| benign | Réussie | Code 0. validation locale antérieure du contrôleur sur historique synthétique | [preuve](../comparaisons/etape-06-20261112/evidence/local-ci-results.json) |
| line-shift | Réussie | Code 0. validation locale antérieure du contrôleur sur historique synthétique | [preuve](../comparaisons/etape-06-20261112/evidence/local-ci-results.json) |
| new-finding | Réussie | Code 1. validation locale antérieure du contrôleur sur historique synthétique | [preuve](../comparaisons/etape-06-20261112/evidence/local-ci-results.json) |
| duplicate-occurrence | Réussie | Code 1. validation locale antérieure du contrôleur sur historique synthétique | [preuve](../comparaisons/etape-06-20261112/evidence/local-ci-results.json) |
| equal-count | Réussie | Code 1. validation locale antérieure du contrôleur sur historique synthétique | [preuve](../comparaisons/etape-06-20261112/evidence/local-ci-results.json) |
| correction | Réussie | Code 0. validation locale antérieure du contrôleur sur historique synthétique | [preuve](../comparaisons/etape-06-20261112/evidence/local-ci-results.json) |
| push-multiple-commits | Réussie | Code 1. validation locale antérieure du contrôleur sur historique synthétique | [preuve](../comparaisons/etape-06-20261112/evidence/local-ci-results.json) |
| pull-request | Réussie | Code 1. validation locale antérieure du contrôleur sur historique synthétique | [preuve](../comparaisons/etape-06-20261112/evidence/local-ci-results.json) |
| missing-reference | Réussie | Code 2. validation locale antérieure du contrôleur sur historique synthétique | [preuve](../comparaisons/etape-06-20261112/evidence/local-ci-results.json) |
| first-push | Réussie | Code 2. validation locale antérieure du contrôleur sur historique synthétique | [preuve](../comparaisons/etape-06-20261112/evidence/local-ci-results.json) |
| manual | Réussie | Code 0. validation locale antérieure du contrôleur sur historique synthétique | [preuve](../comparaisons/etape-06-20261112/evidence/local-ci-results.json) |
| invalid-configuration | Réussie | Code 2. validation locale antérieure du contrôleur sur historique synthétique | [preuve](../comparaisons/etape-06-20261112/evidence/local-ci-results.json) |
| incomplete-scan | Réussie | Code 2. validation locale antérieure du contrôleur sur historique synthétique | [preuve](../comparaisons/etape-06-20261112/evidence/local-ci-results.json) |
| github-43251450652 | Réussie | démonstration CI antérieure ; exemple temporaire, pas les sept injections | [run GitHub](https://github.com/EnzoT454/tika/actions/runs/43251450652) |
| github-43251595954 | Réussie | démonstration CI antérieure ; exemple temporaire, pas les sept injections | [run GitHub](https://github.com/EnzoT454/tika/actions/runs/43251595954) |
| github-43252710325 | Réussie | démonstration CI antérieure ; exemple temporaire, pas les sept injections | [run GitHub](https://github.com/EnzoT454/tika/actions/runs/43252710325) |
| github-43253590194 | Réussie | démonstration CI antérieure ; exemple temporaire, pas les sept injections | [run GitHub](https://github.com/EnzoT454/tika/actions/runs/43253590194) |
| github-43254989974 | Réussie | démonstration CI antérieure ; exemple temporaire, pas les sept injections | [run GitHub](https://github.com/EnzoT454/tika/actions/runs/43254989974) |
| sept-types-detection-statique | Réussie | 682 sources finales, sept CWE, huit identités ajoutées | [preuve](vulnerability-provenance.json) |
| sept-etats-build-tests | Réussie | sept builds archivés, 964 core + 8 processeur, deux désactivations initiales, zéro violation | [preuve](../vulnerabilites/consolidation-20261112/matrix.json) |
| six-observations-bornees | Réussie | anciennes sondes V1–V5/V7 ; limites de portée conservées | [preuve](../comparaisons/etape-06-20261112/evidence/archived-behavior.json) |
| v6-comportement | Incomplète | détection INFO et build confirmés ; marqueur réservé rejeté, pas de démonstration ReDoS | [preuve](../comparaisons/etape-06-20261112/evidence/v6-only-after.log) |
| sept-injections-blocage-integre | Réussie | Code 1. Semgrep code 1, 17 → 24, +8/−1, construction ignorée | [run GitHub](https://github.com/EnzoT454/tika/actions/runs/43278937793) |
| rickroll-apres-tests-reussis | Réussie | Aucun rickroll et job réussi ; retour au vert également confirmé | [run GitHub](https://github.com/EnzoT454/tika/actions/runs/43276322587) |
| rickroll-apres-test-junit-echoue | Réussie | Un cas JUnit échoué ; six poses, action réussie et job en échec | [run GitHub](https://github.com/EnzoT454/tika/actions/runs/43276898487) |
| compilation-echouee-sans-echec-junit | Réussie | Erreur javac, rickroll ignoré, job échoué | [run GitHub](https://github.com/EnzoT454/tika/actions/runs/43277596512) |

**Réserve V6 :** signalement INFO confirmé localement et à distance, compilation réussie ; ReDoS non démontré et marqueur de la sonde refusé par les métadonnées réservées. Aucun résultat positif n’est déduit de cette absence de marqueur.

Le blocage des sept modifications et les quatre cas du rickroll sont maintenant confirmés dans les artefacts GitHub téléchargés. Les preuves de l’étape 6 restent intactes et décrivent leur état antérieur.
