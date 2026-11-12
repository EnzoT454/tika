# Matrice de validation — étape 6

Cette matrice réutilise exclusivement les preuves archivées. Aucun cas n’a été relancé pendant l’étape 6. « Réussie » signifie que le comportement attendu du contrôle est prouvé dans le périmètre indiqué, y compris lorsqu’un échec technique ou sécurité est attendu.

## Contrôleur : essais locaux antérieurs

Les [14 résultats locaux](evidence/local-ci-results.json) utilisent un historique Git synthétique et le vrai moteur. Les SHA de cet historique ne sont pas des commits d’injection du dépôt de travail.

| Scénario | État | Code observé | Observation |
|---|---|---:|---|
| Même code / même référence | Réussie | 0 | 17 → 17, +0 / −0 |
| Changement bénin | Réussie | 0 | 17 → 17, +0 / −0 |
| Déplacement de lignes | Réussie | 0 | 17 → 17, +0 / −0 |
| Une nouvelle alerte | Réussie | 1 | 17 → 18, +1 / −0 |
| Occurrence identique supplémentaire | Réussie | 1 | 18 → 19, +1 / −0 |
| Suppression + ajout, total identique | Réussie | 1 | 17 → 17, +1 / −1 |
| Correction d’une alerte | Réussie | 0 | 18 → 17, +0 / −1 |
| Push de plusieurs commits | Réussie | 1 | 17 → 19, +2 / −0 |
| PR avec cible divergente | Réussie | 1 | 17 → 18, +1 / −0 |
| Référence introuvable | Réussie | 2 | Erreur technique explicite, aucune détection de vulnérabilité revendiquée |
| Premier push, référence nulle | Réussie | 2 | Erreur technique explicite, aucune détection de vulnérabilité revendiquée |
| Déclenchement manuel | Réussie | 0 | 17 → 17, +0 / −0 |
| Configuration invalide | Réussie | 2 | Erreur technique explicite, aucune détection de vulnérabilité revendiquée |
| Scan incomplet / Java malformé | Réussie | 2 | Erreur technique explicite, aucune détection de vulnérabilité revendiquée |

## Démonstration GitHub antérieure

Les [cinq résultats GitHub](evidence/github-results.json) concernent l’exemple temporaire. Ils ne sont pas des runs des sept modifications actuelles.

| Run | Cas | État de la validation | Sécurité / construction |
|---|---|---|---|
| [43251450652](https://github.com/EnzoT454/tika/actions/runs/43251450652) | Premier push | Réussie | Code 2 / skipped |
| [43251595954](https://github.com/EnzoT454/tika/actions/runs/43251595954) | Manuel avec référence explicite | Réussie | Code 0 / success |
| [43252710325](https://github.com/EnzoT454/tika/actions/runs/43252710325) | Push bénin | Réussie | Code 0 / success |
| [43253590194](https://github.com/EnzoT454/tika/actions/runs/43253590194) | PR avec alerte temporaire | Réussie | Code 1 / skipped |
| [43254989974](https://github.com/EnzoT454/tika/actions/runs/43254989974) | PR corrigée | Réussie | Code 0 / success |

## État final et validations restantes

| Scénario | État | Preuve et limite |
|---|---|---|
| Détection des sept types | Réussie | Huit identités ajoutées, sept CWE ; associations.json et rapports bruts. |
| Compilation et tests des sept états | Réussie | Sept builds archivés : 964 core + 8 processeur réussis, deux désactivations initiales, zéro violation. |
| Observations bornées V1–V5 et V7 | Réussie dans leur périmètre | Anciennes traces positives ; pas une preuve d’exploitation universelle. |
| Observation comportementale V6 | Incomplète | INFO détectée et build réussi ; marqueur réservé rejeté, ReDoS non démontré. |
| Blocage intégré des sept injections | Non exécutée | Les nouveautés sont comparées ; code 1 attendu, aucun code intégré ni run distant enregistré pour ces sept états. |
| Rickroll après tests réussis | Non exécutée | Action non implémentée ; les anciens builds ne valident pas sa condition future. |
| Rickroll après échec JUnit volontaire | Non exécutée | À démontrer lors de l’étape 7 ; préserver le statut d’échec. |
| Erreur de compilation sans échec JUnit | Non exécutée | Cas distinct du scan Semgrep sur Java malformé ; à distinguer lors de l’intégration du rickroll. |

La réserve V6 est conservée conformément à la décision utilisateur et ne bloque pas l’étape 7. La matrice est complète comme état des preuves disponibles ; les lignes incomplètes/non exécutées ne sont pas marquées réussies. Les données structurées sont dans `validation-matrix.json`.
