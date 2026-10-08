# Étape 5 — mesures comparables A/B/C

Exécutions locales du 8 octobre 2026 sur `tache2-final`, sans push. Les résultats
de cette intégration sont distincts des expériences historiques Hamza et Saidana.

## Protocole

Trois copies indépendantes sur le disque interne sont préparées par
`prepare-states.py`, à partir de l'état courant du dépôt. Le script utilise le
préparateur de l'étape 4, exclut AppleDouble, puis retire uniquement les sources
IA et manuelles qui ne font pas partie de l'état choisi. Il conserve les mêmes
749 cas originaux et toutes leurs ressources dans les trois états.

| État | Sources ajoutées aux originales | Cas core attendus |
|---|---|---:|
| A | Aucune | 749 |
| B | 40 classes IA, 161 cas retenus après le tri documenté | 910 |
| C | B + 3 classes manuelles, 56 cas, dont le lien symbolique Hamza | 966 |

Les 32 candidats IA rejetés restent dans les archives de génération et de
l'étape 3, sans être compilés. Les 27 corrections humaines restent déclarées.

PIT 1.30.0, connecteur JUnit 1.2.3, onze opérateurs explicites, cibles
`EndianUtils`, `MediaType`, `FilenameUtils`, tests `org.apache.tika.*`, deux
threads, `timeoutFactor=1.25`, `timeoutConstant=4000`, aucun historique : le POM
est identique dans les trois copies. Les paramètres détaillés sont dans
[`../etape-04/commands.md`](../etape-04/commands.md).

## Commandes exécutées

Depuis la racine du dépôt original :

```bash
python3 tache2-rapports/final-integration/etape-05/prepare-states.py
```

Dans chaque copie indiquée par `states.json`, successivement A, B puis C :

```bash
./mvnw -B -pl tika-core -am clean install
./mvnw -B -pl tika-core -Pmutation org.pitest:pitest-maven:mutationCoverage
```

Chaque état est compilé proprement avant PIT. Les contrôles applicables sont
conservés, sans `-Pfast`, `-DskipTests`, désactivation des nouveaux tests ou
sélection limitée aux tests directs des classes. Aucun code de production
n'est modifié. Les exécutions Maven/PIT sont séquentielles ; seul l'archivage
indépendant des rapports peut se faire pendant la construction suivante.

Après chaque mesure, depuis le dépôt original :

```bash
python3 tache2-rapports/final-integration/etape-05/archive-state.py A
python3 tache2-rapports/final-integration/etape-05/archive-state.py B
python3 tache2-rapports/final-integration/etape-05/archive-state.py C
python3 tache2-rapports/final-integration/etape-05/compare-states.py
```

Les scripts refusent les incohérences : évolution des sources pendant la
construction, nombres de cas inattendus, tests IA/manuels ignorés, erreurs
Checkstyle, différences entre sources communes, bytecode cible ou univers
de mutants. `environment.json` conserve Java, Maven, branche et commit de base ;
le commit seul ne représente pas les modifications locales, d'où les archives
de sources et manifestes.

## Preuves et interprétation

Chaque dossier A/B/C conserve séparément :

- `sources.tar.gz` et `source-manifest.json` : fichiers exacts avant construction,
  avec empreintes SHA-256, identiques après construction ;
- `selection.json` : fichiers acceptés/exclus et copie de construction ;
- `build.log`, `pit.log` : journaux des deux commandes ;
- XML Surefire et Checkstyle des modules core et annotation-processor ;
- `jacoco/` et `pit-reports/` : rapports complets, dont les XML bruts ;
- `results.json` et `mutants.json` : effectifs, couverture, empreintes du bytecode
  cible et inventaire des mutants avec identifiants et tests tueurs.

Le score utilisé est **`100 × KILLED / total`**. `TIMED_OUT`, `SURVIVED`,
`NO_COVERAGE` et les éventuels autres statuts sont comptés séparément. PIT
inclut les timeouts dans son compteur agrégé « Killed » affiché dans le journal ;
ce compteur n'est donc pas celui de notre score. Le score global est calculé
sur les sommes des effectifs, sans moyenne des pourcentages par classe.

[`comparison.md`](comparison.md) présente les résultats, et `comparison.json`
conserve chaque transition de statut. Une hausse de `SURVIVED` peut provenir
de mutants auparavant `NO_COVERAGE` qui sont désormais exécutés. Le nombre
de nouvelles détections se calcule mutant par mutant, en séparant les pertes.

Les mutants toujours non détectés et le lien précis entre chaque nouvelle
détection, l'entrée et l'assertion seront expliqués à l'étape 6. Aucun nouveau
classement d'équivalence n'est établi pendant cette mesure.

Pour reproduire, extraire l'archive d'un état dans un dossier neuf sur le disque
interne, vérifier son manifeste et lancer ses deux commandes Maven. Archiver
les nouveaux rapports dans un dossier distinct afin de préserver ces mesures.

## Résultats vérifiés

Les six commandes Maven ont terminé avec un code de sortie 0 et `BUILD SUCCESS`.
A/B/C comptent respectivement 185/317/396 `KILLED` sur 406, soit
45,57 % / 78,08 % / 97,54 %. Le timeout est séparé dans les trois états.
Les comparaisons confirment 132 nouvelles détections par les classes IA puis
79 par les classes manuelles, sans perte de `KILLED`. Les bytecodes cibles et
les identités de mutants sont identiques dans les trois états.
