# Rapports de la tâche 2 (IFT3913)

Toute la documentation est dans le [README.md](../README.md) à la racine ; ce dossier contient uniquement les
artefacts bruts produits pendant le travail, dans l'ordre des étapes.

| Dossier | Contenu |
|---|---|
| `01-jacoco-tests-originaux/` | Couverture JaCoCo (`jacoco.csv`) de toute la suite `tika-core` originale (749 tests). |
| `02-pitest-tests-originaux/` | Rapport pitest de référence : trois classes, tests originaux dédiés seulement (`index.html`, `mutations.xml`). |
| `03-chatunitest/<Classe>/` | Sorties brutes de ChatUniTest : `generated-tests-snapshot/` (tests exportés, non modifiés), `error-message/` (code et erreurs de chaque tour en échec), `history*/` (prompts, réponses et jetons de chaque appel au modèle), `stats.md` (synthèse par méthode). `FilenameUtils-contexte16k/` : seconde configuration (contexte 16k) pour les deux méthodes trop longues. |
| `04-jacoco-tests-generes/` | Couverture JaCoCo par classe après ajout des tests générés (tests `<Classe>*` seulement). |
| `05-pitest-EndianUtils-tests-generes/` | pitest `EndianUtils`, tests originaux + générés ; `diff-vs-tests-originaux.txt` = comparaison mutant par mutant avec `02`. |
| `06-jacoco-tests-manuels/` | Couverture JaCoCo par classe après ajout des tests manuels. |
| `07-pitest-EndianUtils-tests-manuels/` | pitest `EndianUtils`, tous les tests ; `diff-vs-tests-generes.txt` = comparaison avec `05`. |
| `08-pitest-MediaType-tests-generes/`, `09-pitest-MediaType-tests-manuels/` | Idem pour `MediaType`. |
| `10-pitest-FilenameUtils-tests-generes/`, `11-pitest-FilenameUtils-tests-manuels/` | Idem pour `FilenameUtils`. |
| `12-pitest-final-toutes-classes/` | pitest final : trois classes, tous les tests, en une seule exécution (configuration par défaut du pom). |
| `13-jacoco-final/` | Couverture JaCoCo finale de toute la suite `tika-core`. |
| `scripts/` | `pitsum.py` (résumé d'un `mutations.xml`), `pitdiff.py` (comparaison de deux rapports, test tueur par mutant), `cutstats.py` (statistiques ChatUniTest), `integrate.py` (copie des tests générés avec `@Disabled` sur les oracles faux), `RunTests.java` (lanceur JUnit Platform autonome utilisé pour ré-exécuter les tests exportés). |
| `PR-depot-du-cours-readme.md` | Gabarit du `readme.md` à déposer dans `tache2/NOM1_NOM2/` du dépôt du cours. |

Dans les traces de `03-chatunitest/`, le chemin local absolu du poste de travail a été remplacé par `<depot>` ;
le reste est inchangé.
