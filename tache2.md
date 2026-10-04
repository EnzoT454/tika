# Tâche 2

## Étapes

Voici l’ordre à suivre pour réaliser la tâche 2. Le [plan détaillé](#plan-detaille), les consignes et les comptes rendus figurent plus bas dans ce document.

1. **Préparer le dépôt et l’environnement**

   - Vérifier votre fork GitHub et l’inscription du binôme.
   - Noter le commit initial et les versions Java/Maven.
   - Construire et exécuter les tests existants :

     ```bash
     ./mvnw -B -pl tika-core -am clean install
     ```

2. **Mesurer la couverture initiale**

   - Consulter `tika-core/target/site/jacoco/index.html`.
   - Repérer des classes déjà testées, avec des lignes ou branches non couvertes.
   - Examiner notamment `FilenameUtils`, `LookaheadInputStream` et `TailStream` : ce sont des candidates, pas encore des choix validés.

3. **Installer PIT et valider la compatibilité**

   - Ajouter le plugin Maven PIT et son connecteur JUnit.
   - Vérifier qu’il découvre et exécute réellement les tests du dépôt.
   - Ce contrôle est nécessaire avant les expériences, car cette copie utilise JUnit 6.

4. **Choisir définitivement une à trois classes**

   - Exécuter PIT sur les candidates avec les tests originaux.
   - Retenir celles qui ont une couverture incomplète et des mutants survivants.
   - Privilégier une ou deux classes bien analysées.
   - Sauvegarder les rapports : ils constituent l’état A — tests originaux.

5. **Configurer ChatUniTest avec un modèle local**

   - Installer Ollama et un modèle ouvert adapté à votre machine.
   - Intégrer ChatUniTest dans Maven.
   - Configurer son accès au modèle local.
   - Valider une première génération sur une méthode avant de traiter toutes les classes retenues.

6. **Générer et conserver les tests bruts**

   - Enregistrer la commande, le modèle, les paramètres et les journaux.
   - Conserver les fichiers générés avant toute modification.
   - Compter les tests qui compilent et réussissent sans correction humaine.
   - Documenter chaque correction nécessaire, séparément des réparations automatiques de ChatUniTest.

7. **Analyser les oracles des tests générés**

   - Expliquer ce que chaque test vérifie et pourquoi le résultat attendu est correct.
   - Comparer ses assertions à celles des tests originaux.
   - Identifier les vérifications précises, triviales, redondantes ou fragiles.
   - Rédiger cette analyse pendant le travail : elle représente 20 % du barème.

8. **Mesurer l’apport des tests IA**

   - Exécuter les tests originaux avec les tests générés retenus.
   - Relancer JaCoCo et PIT : état B.
   - Garder le même code de production et les mêmes paramètres PIT qu’en A.
   - Pour chaque mutant nouvellement tué, expliquer quel test le détecte, avec quelles données et quelle assertion.

9. **Ajouter les tests manuels nécessaires**

   - Examiner les mutants encore survivants ou non couverts.
   - Écrire des tests ciblés pour les détecter.
   - Documenter pour chacun : nom, intention, choix des données et justification de l’oracle.
   - Relancer les analyses : état C — suite complète.
   - Expliquer les éventuels mutants restants.

10. **Faire réussir les tests dans GitHub Actions**

    - Configurer une action qui exécute tous les nouveaux tests.
    - Vérifier leur présence dans les rapports, pas seulement le statut vert.
    - Conserver le lien vers l’exécution réussie sur le commit final.

11. **Finaliser le rapport et remettre**

    - Regrouper toute la documentation dans un unique `readme.md` à la racine.
    - Inclure les résultats A/B/C, les corrections, l’analyse des oracles et mutants, les commandes et la déclaration d’utilisation de l’IA.
    - Ajouter les liens du dépôt et du README dans le fichier du binôme.
    - Vérifier la fenêtre de remise indiquée dans le plan avant de créer la nouvelle PR.

---

## Consignes du cours

La tâche 2 se fait en binômes. Quand un binôme est formé, il fait une 'pull request' sur ce répertoire pour ajouter un sous-répertoire de la forme 'NOM1_NOM2/'. Ce répertoire inclut un fichier readme.md dans lequel les étudiants indiquent leur nom et prénom, suivant le format documenté ici: [github/PULL_REQUEST_TEMPLATE/tache2-readme.md](https://github.com/umontreal-diro/IFT3913/blob/2026/.github/PULL_REQUEST_TEMPLATE/tache2-readme.md).

## Instructions pour la tâche 2

Pour la tâche 2, chaque binôme doit accomplir les étapes suivantes:

- sélectionner entre une et 3 classes du [cas d'étude](https://github.com/umontreal-diro/IFT3913/blob/2026/README.md/#cas-d%C3%A9tude)  qui ont déjà des tests, mais qui ne couvrent pas 100% du code
- installer et utiliser [ChatUniTest](https://github.com/ZJU-ACES-ISE/ChatUniTest) avec un modèle de langage ouvert exécuté localement (ex. via Ollama) pour générer automatiquement des tests supplémentaires sur les classes sélectionnées.
- Documenter le résultat de cette génération : où sont les tests générés ? les tests générés compilent-ils et s'exécutent-ils sans intervention manuelle ? sinon, combien de corrections ont été nécessaires ?  
- Comparer qualitativement les oracles produits par l'IA à ceux écrits à la main (pertinence, spécificité, ou au contraire vérifications triviales). 
- ajouter [pitest](https://pitest.org/) au projet
- exécuter une analyse de mutation sur les classes sélectionnées
- calculer le score de mutation avec les tests originaux pour les classes sélectionnées
- calculer le score de mutation avec les nouveaux tests et déterminer si les tests générés détectent tous les mutants. Expliquez quels mutants sont détectés et pourquoi. 
- s’il y a des mutants non détectés, ajouter manuellement des tests. Documenter précisément chaque cas de test: nom du test, intention du test (quel comportement est testé), motivation des données de test choisies, explication de l'oracle (comment déterminer le comportement attendu)

## Critères d'évaluation de la tâche 2

| critère | description |
|-------------------------------------------- | ----|
| classes à tester (10%) | justifier que les classes et méthodes choisies pour la génération de test ne sont pas déjà couvertes et ont des mutants vivants  |
| IA et test (10%) |  ChatUniTest est installé dans le pipeline Maven | 
| tests générés (10%)	| Générer des tests avec ChatUniTest | 
| documentation tests	(20%)	| les tests générés sont expliqués et critiqués| 
| mutation (15%) | exécuter pitest sur les classes testées, avec tests originaux puis avec les tests générés | 
| documentation mutants (15%) 	| les mutants détectés par les tests générés sont documentés et la raison pour la détection est expliquée| 
| test supplémentaires  (10%) | les tests écrits à la main sont clairement documentés   |
| exécution (10%)	| tous les nouveaux tests s'exécutent avec succès dans la Github action| 
| Format de documentation | toute la documentation doit être dans un fichier readme.md unique, à la racine du référentiel Github de votre tâche |



## Instructions pour rendre la tâche 2

Une fois la tâche accomplie, les étudiants font une nouvelle 'pull request' sur le sous-répertoire de leur binôme et ajoutent les informations suivantes dans le fichier readme:
- un lien vers le référentiel (repository) Github qui inclut leur tâche
- un lien vers la page README.md qui documente la tâche (readme uniquement, pas de docx, google docs ou autre)

Le format pour la PR est documenté ici: [.github/PULL_REQUEST_TEMPLATE/tache2-readme.md](https://github.com/umontreal-diro/IFT3913/blob/2026/.github/PULL_REQUEST_TEMPLATE/tache2-readme.md).

La date limite pour la seconde 'pull request' est indiquée sur la [page principale](https://github.com/umontreal-diro/IFT3913/blob/2026/README.md/#evaluation-ift3913---a25) du cours.

---

<a id="plan-detaille"></a>

## Plan détaillé de réalisation — recommandations pour couvrir tout le barème

Les consignes ci-dessus sont celles du cours. Les sections suivantes proposent une méthode de travail ; elles ne constituent pas des exigences supplémentaires de l'enseignant ni une garantie de note.

**Statut : étapes 1 à 9 de la liste en tête du document réalisées en local (les comptes rendus initiaux regroupent certains travaux). PIT est intégré et les états A, B et C sont archivés avec les mêmes 148 mutants : le score passe de 88 / 148 (59,46 %) à 90 / 148 (60,81 %), puis à 111 / 148 (75,00 %). `LookaheadInputStream` atteint 100 % des lignes et branches ; `FilenameUtils` atteint 163 / 175 lignes et 93 / 110 branches. `TailStream` est écartée car entièrement couverte. Les tests IA tuent deux mutants en B et les neuf tests manuels tuent 21 mutants supplémentaires en C ; 27 survivent et 10 ne sont toujours pas couverts. Les comptes rendus successifs ci-dessous conservent la progression et les décisions prises à chaque étape.**

Ce fichier est un document préparatoire demandé pour organiser le travail. Pour la remise, réunir toute l'analyse, les résultats et les instructions de reproduction dans **un seul `readme.md` à la racine du dépôt de la tâche**. Le correcteur ne doit pas devoir consulter `tache2.md` pour trouver une justification. Consolider ce plan dans le rapport final et retirer le document préparatoire de la version remise s'il ferait doublon. Les journaux et rapports bruts peuvent servir de pièces justificatives liées depuis ce README.

### 1. Stratégie de pointage et preuves attendues

Commencer par **une classe de `tika-core`**, puis en retenir une deuxième seulement si elle apporte des comportements complémentaires et reste maîtrisable. Une à trois classes sont autorisées : le barème ne donne aucun bonus explicite pour trois classes. Privilégier des entrées déterministes, des oracles précis et des mutants explicables.

La documentation des tests générés, des mutants et des tests manuels représente **45 %** du barème. La rédiger pendant les expériences, avec les preuves disponibles, plutôt qu'après toutes les corrections.

| Critère | Points | Preuves à présenter dans le README final |
|---|---:|---|
| Classes à tester | 10 | Tests préexistants, couverture initiale par classe et méthode, branches manquantes, mutants survivants identifiés. |
| IA et test | 10 | Configuration ChatUniTest dans Maven, versions, commande exécutée et preuve de connexion au modèle local. |
| Tests générés | 10 | Sources produites, provenance, journal de génération, nombre de tests et résultats avant correction humaine. |
| Documentation des tests | 20 | Explication et critique des oracles générés, comparaison concrète avec les tests originaux, registre des corrections. |
| Mutation | 15 | Rapports PIT comparables avant/après, commandes, effectifs et scores par classe et au total. |
| Documentation des mutants | 15 | Correspondance mutant → test → entrée discriminante → assertion qui détecte le changement. |
| Tests supplémentaires | 10 | Pour chaque test manuel : nom, intention, justification des données et de l'oracle, mutants concernés. |
| Exécution | 10 | GitHub Action verte au commit remis, avec preuve que tous les nouveaux tests ont réellement été exécutés. |
| Format | Obligatoire | Rapport autonome dans le `readme.md` unique à la racine, liens de remise valides. |

### 2. Préparer le dépôt et lever les incompatibilités tôt

1. Vérifier que le dépôt de travail est bien le fork du cas d'étude du cours ; noter son URL, sa branche et son commit de départ.
2. Vérifier l'inscription du binôme dans le dépôt du cours. La copie locale contient `tache2/AQEL_SAIDANA/readme.md` avec Aqel Hamza et Saidana Mohamed ; cela ne prouve pas que la PR d'inscription est fusionnée.
3. Relever le système, l'architecture, la RAM, Java, Maven et, pour la génération, les versions d'Ollama et du modèle.
4. Construire et tester `tika-core` avec ses dépendances. Conserver les sorties, les nombres de tests et les éventuels échecs préexistants.
5. Faire immédiatement un essai minimal de PIT puis de ChatUniTest sur une candidate. Résoudre les incompatibilités avant d'engager plusieurs jours de génération.

Commandes initiales proposées, depuis la racine :

```bash
git rev-parse HEAD
java -version
./mvnw -version
./mvnw -B -pl tika-core -am clean install
```

Constats dans cette copie : `tika-parent/pom.xml` déclare Tika `4.0.0-SNAPSHOT`, une cible Java 17, JUnit `6.1.3` et JaCoCo `0.8.15`. JaCoCo est déjà configuré : vérifier le rapport produit avant d'ajouter une configuration redondante. Examiner `tika-core/target/site/jacoco/` et `tika-core/target/surefire-reports/`.

**Point à valider :** la compatibilité effective de ChatUniTest, de ses dépendances de test et du connecteur PIT/JUnit avec JUnit 6. Inspecter l'arbre des dépendances et vérifier la découverte réelle des tests. Ne pas remplacer globalement la version de JUnit pour masquer un conflit. Si une adaptation est nécessaire, la documenter, la figer et refaire toutes les mesures avec le même environnement.

**Fin d'étape :** compilation réussie, tests existants exécutés, rapports accessibles et essai des deux outils concluant. Ne pas utiliser `-Pfast` pour les preuves : ce profil saute notamment les tests.

### 3. Choisir les classes à partir des mesures — 10 %

Le README du cours autorise `tika-core`, `tika-serialization`, `tika-parser-ocr-module`, `tika-parser-pdf-module` et `tika-parser-microsoft-module`. `tika-core` constitue un bon point de départ pour limiter les outils externes et les gros fichiers de test.

Candidates dont la classe et les tests existent dans cette copie :

| Classe dans `org.apache.tika.io` | Tests existants | Intérêt à explorer, sous réserve des mesures |
|---|---|---|
| `FilenameUtils` | `FilenameUtilsTest` | Normalisation, caractères réservés, extraction de noms et cas limites de chaînes. |
| `LookaheadInputStream` | `LookaheadInputStreamTest` | Limites de lecture, fin de flux et comportement du flux sous-jacent. |
| `TailStream` | `TailStreamTest` | Derniers octets conservés, limites du tampon, lectures et restauration d'état. |

1. Lire les contrats et les tests existants de chaque candidate.
2. Mesurer les lignes **et** branches couvertes avec les tests originaux.
3. Repérer les méthodes et conditions partiellement couvertes.
4. Exécuter un premier PIT ciblé avec les tests originaux.
5. Retenir une ou deux classes avec couverture incomplète **et mutants `SURVIVED` démontrés**. Une classe avec seulement des mutants `NO_COVERAGE` fournit une justification moins directe du critère « mutants vivants » ; distinguer les statuts.
6. Écarter une candidate déjà entièrement couverte, sans survivants ou dont l'environnement rend l'expérience trop difficile à reproduire.

Tableau à remplir sans inventer de chiffres :

| Classe / méthode | Tests originaux | Lignes couvertes / total | Branches couvertes / total | Mutants survivants | Justification du choix |
|---|---|---|---|---|---|
| À mesurer | À relever | À mesurer | À mesurer | Identifiants à relever | À rédiger après mesures |

**Fin d'étape :** choix définitif de 1 à 3 classes, justifié par les rapports et des liens vers le code et les tests.

### 4. Définir une expérience reproductible et établir la référence — 15 %

Configurer PIT dans le POM du module ciblé, idéalement dans un profil explicite `mutation`. Fixer les versions de `pitest-maven` et du connecteur JUnit après l'essai de compatibilité. Le connecteur est une dépendance du plugin PIT. La [documentation Maven de PIT](https://pitest.org/quickstart/maven/) et celle du [connecteur JUnit](https://github.com/pitest/pitest-junit5-plugin) servent de références ; ne pas considérer les versions du texte joint comme déjà validées sur ce dépôt.

Définir les classes cibles, les tests inclus, les opérateurs de mutation, les exclusions justifiées, les limites de temps et le nombre de processus. Demander des rapports HTML et XML. Les mêmes réglages et le même code de production doivent être utilisés pour chaque état :

| État | Tests exécutés | But |
|---|---|---|
| A — référence | Tous les tests originaux du périmètre | Établir couverture et mutation initiales. |
| B — ajout IA | A + tests ChatUniTest acceptés, corrections humaines tracées | Mesurer le gain attribuable aux tests issus de la génération. |
| C — complément manuel | B + nouveaux tests manuels | Montrer l'effet des compléments sur les mutants restants. |

Une exécution des tests IA seuls peut aider à attribuer une détection ambiguë, mais ne remplace pas A/B/C. Si des corrections humaines changent les assertions, distinguer leur contribution de celle des tests exécutables sans retouche ; une mesure B0 de ces derniers est utile lorsque possible.

Commandes proposées **après création et validation du profil `mutation`**, et installation des dépendances du réacteur :

```bash
./mvnw -B -pl tika-core clean test
./mvnw -B -pl tika-core -Pmutation org.pitest:pitest-maven:mutationCoverage
```

Le profil n'existe pas encore : ces commandes sont un objectif de configuration. Adapter le périmètre si une classe d'un autre module est retenue. Ne pas lancer la mutation sur tous les modules avec `-am` sans en maîtriser l'effet.

Conserver chaque état par commit ou sélection explicite des sources de test. Éviter que des `.class` obsolètes rendent les tests de B visibles dans A : nettoyer la compilation avant chaque mesure. Archiver les résultats **avant** le nettoyage suivant, hors de `target/`, par exemple dans `evidence/tache2/A/`, `B/`, `C/`. Éviter des noms commençant par `_`, maintenant ignorés dans ce dépôt.

Chaque archive doit contenir le commit, la commande, les versions, le périmètre exact, les rapports JaCoCo/PIT/Surefire et le journal utile. Préserver une trace des paramètres et désactiver la réutilisation d'historique PIT pour les mesures comparatives, ou expliquer précisément son usage.

### 5. Intégrer ChatUniTest et générer avec un modèle local — 20 %

1. Ajouter le plugin `io.github.zju-aces-ise:chatunitest-maven-plugin` dans un profil Maven `chatunitest` du module ciblé. Fixer une version validée.
2. Examiner la dépendance `chatunitest-starter` indiquée dans la documentation ; vérifier son effet sur JUnit avant intégration.
3. Choisir un modèle ouvert compatible avec la machine. Noter son nom exact, sa version ou son empreinte, sa licence, sa quantification et ses paramètres.
4. Démarrer le modèle **localement** via Ollama et vérifier une requête minimale. Conserver la preuve du modèle chargé et de l'URL locale ; ne pas employer un modèle distant ou une variante cloud.
5. Configurer `model`, `url`, la valeur technique `apiKeys` si requise par le client, la classe ciblée, le dossier de sortie et les limites de génération/réparation.
6. Faire une génération pilote sur une méthode, puis lancer les classes retenues avec le même protocole. Fixer à l'avance le nombre de tentatives et consigner aussi les échecs.

Le [plugin ChatUniTest](https://github.com/ZJU-ACES-ISE/chatunitest-maven-plugin) documente les paramètres `url`, `model`, `selectClass`, `selectMethod`, `testOutput`, `testNumber` et `maxRounds`, ainsi que les objectifs `class` et `method`. Pour Ollama, vérifier l'endpoint local `http://localhost:11434/v1/chat/completions` dans sa [documentation de compatibilité](https://docs.ollama.com/api/openai-compatibility). Ce raccordement est à tester sur la version choisie.

Exemple de commande à adapter, après configuration du profil ; `FilenameUtils` reste ici une candidate :

```bash
./mvnw -pl tika-core -Pchatunitest chatunitest:class \
  -DselectClass=org.apache.tika.io.FilenameUtils
```

Séparer les productions brutes, les réparations automatiques de ChatUniTest, les corrections humaines et les tests manuels ajoutés ensuite. Archiver immédiatement les sorties et journaux bruts avant toute copie ou retouche. Placer les tests finalement retenus dans les sources de test Maven, avec des noms détectés par Surefire, par exemple `FilenameUtilsGeneratedTest` et `FilenameUtilsManualTest`. Tracer les renommages.

Tableau de génération à remplir pour chaque tentative :

| Essai | Classe / méthode | Modèle et paramètres | Fichiers bruts | Méthodes générées | Compilation avant retouche | Tests passants avant retouche | Réparations automatiques | Corrections humaines |
|---|---|---|---|---:|---|---|---:|---:|
| À renseigner | | | | | | | | |

Définir l'unité de comptage : compter séparément fichiers, méthodes de test et cas exécutés pour les tests paramétrés. Pour les corrections humaines, compter chaque intervention logique et indiquer aussi combien de fichiers et de tests sont concernés.

| Correction | Test | Problème observé | Modification | Catégorie | Impact sur l'oracle |
|---|---|---|---|---|---|
| C01… | Nom exact | Message ou comportement | Diff ou description précise | Import / API / compilation / assertion / stabilité | Inchangé ou justification du changement |

**Fin d'étape :** génération réellement réalisée via Maven et le modèle local, provenance conservée, compilation et exécution avant/après retouches connues. Ne pas supprimer silencieusement les tests échoués ni remplacer les valeurs attendues uniquement pour faire passer les tests.

### 6. Expliquer et critiquer les oracles — 20 %

Pour chaque test généré retenu, indiquer le comportement, les entrées, l'assertion, la source du résultat attendu et sa valeur ajoutée. Comparer avec un test original pertinent quand il existe ; relever aussi l'absence de cas comparable. Pour les rejets, donner la raison.

| Test généré | Comportement et données | Oracle et justification | Test original comparable | Apport ou faiblesse | Décision |
|---|---|---|---|---|---|
| À renseigner | Cas nominal, limite, erreur… | Valeur exacte, exception, état… | Nom et assertion | Pertinent, redondant, trivial, fragile… | Conservé / corrigé / rejeté |

Questions à traiter : l'assertion vérifie-t-elle une valeur précise ou seulement une non-nullité ? Le résultat attendu découle-t-il du contrat ? Le test distingue-t-il un comportement erroné plausible ? Le cas limite est-il réellement atteint ? Une exception trop générale masque-t-elle une erreur ? L'oracle reproduit-il l'algorithme testé ou dépend-il de l'heure, de la locale ou du système ?

Exemple pédagogique, à ne pas présenter comme un résultat mesuré : pour `FilenameUtils.normalize`, vérifier la chaîne exacte `why%3F.zip` sur l'entrée `why?.zip` est plus spécifique que vérifier une sortie non nulle. Le résultat attendu est illustré dans la Javadoc. Cela ne prouve pas que ce cas est absent des tests existants.

### 7. Mesurer B et expliquer les mutants nouvellement détectés — 30 % avec l'analyse PIT

1. Exécuter B avec exactement le même code de production et les mêmes paramètres PIT que A.
2. Comparer les mutants par classe, signature de méthode, opérateur et index/localisation de mutation ; le numéro de ligne seul ne suffit pas.
3. Identifier ceux passés de `SURVIVED` ou `NO_COVERAGE` à `KILLED`.
4. Relever le test tueur dans le XML PIT. Si l'attribution est ambiguë, isoler le test concerné dans une exécution supplémentaire.
5. Expliquer la chaîne causale : transformation → donnée discriminante → comportement modifié → assertion en échec.

| Mutant | Classe / méthode / emplacement | Transformation PIT | Statut A | Statut B | Test détecteur | Pourquoi l'entrée et l'oracle le détectent |
|---|---|---|---|---|---|---|
| M01… | À relever | À relever | À relever | À relever | Nom exact | Explication spécifique |

Présenter tous les mutants nouvellement tués, en regroupant seulement les cas réellement similaires sans perdre leurs identifiants. Une simple hausse du score ou une capture du résumé PIT ne suffit pas à expliquer les détections.

Calculer et publier les effectifs par statut. Présenter explicitement **`100 × KILLED / total des mutants générés`** comme taux de mutants tués, puis recopier séparément la métrique affichée par PIT si sa convention de détection diffère. Garder `SURVIVED`, `NO_COVERAGE`, `TIMED_OUT` et les erreurs séparés ; ne pas présenter un timeout comme une assertion qui détecte un défaut. Si le total est nul, indiquer « non applicable ».

Ne pas retirer discrètement les mutants équivalents du dénominateur. Si un score ajusté est utile, le présenter en supplément, avec les exclusions justifiées. Le score global se calcule à partir des sommes des effectifs, et non de la moyenne simple des pourcentages par classe. Exprimer les gains B−A et C−B en **points de pourcentage**.

### 8. Ajouter les compléments manuels et mesurer C — 10 %

Examiner chaque mutant restant après B. Chercher une entrée qui distingue le programme original du mutant, puis formuler un oracle issu du contrat. Prioriser les bornes, les entrées vides, les différences de contenu exact, les séquences d'opérations et les exceptions documentées.

Pour **chaque test manuel**, remplir :

| Nom du test | Mutants visés | Intention | Données choisies et motivation | Oracle et justification | Résultat après ajout |
|---|---|---|---|---|---|
| Nom Java exact | M… | Comportement précis | Pourquoi ces valeurs distinguent les cas | Résultat attendu tiré du contrat | Statuts PIT / succès du test |

Vérifier que le test passe sur le code original et que PIT confirme la détection attendue. Ne pas modifier le code de production pour faciliter le score. Si un véritable bogue est découvert, le documenter séparément et garder l'expérience comparable.

Pour un mutant déclaré équivalent, expliquer pourquoi aucune entrée du domaine valide ne distingue les comportements. Un manque de temps ou de compréhension n'est pas une preuve d'équivalence. Documenter honnêtement les survivants non résolus. Si l'IA tue déjà tous les mutants, le démontrer et expliquer pourquoi aucun complément manuel n'est nécessaire selon la condition des consignes.

Tableau final, une ligne par classe et par état, puis un total pondéré :

| Classe | État | Tests exécutés | Couverture lignes / branches | Total mutants | KILLED | SURVIVED | NO_COVERAGE | Autres statuts | Taux tués | Score PIT |
|---|---|---:|---|---:|---:|---:|---:|---|---|---|
| À renseigner | A / B / C | | | | | | | | | |

### 9. Assurer l'exécution dans GitHub Actions — 10 %

Créer un workflow dédié, par exemple `.github/workflows/tache2-tests.yml`, déclenché sur les PR, les pushes de la branche de travail et manuellement. Les workflows actuels visent notamment `main` : vérifier que la branche réellement remise est couverte.

Le workflow doit récupérer le dépôt, préparer le JDK validé, utiliser le wrapper Maven et exécuter les tests du module avec ses dépendances. Commande de départ : `./mvnw -B -pl tika-core -am clean verify`. Préserver les contrôles de format et de licence applicables aux nouveaux fichiers.

Publier les rapports Surefire et JaCoCo comme artefacts, y compris en cas d'échec. Vérifier les noms et le nombre de nouveaux tests exécutés, l'absence d'exclusions accidentelles et les tests ignorés. Ne pas utiliser `continue-on-error`, `-DskipTests` ou `-Pfast` pour obtenir artificiellement une action verte.

Versionner les tests acceptés pour que la CI les exécute sans dépendre du modèle local. La génération reste un profil Maven reproductible ; les consignes ne demandent pas explicitement de régénérer à chaque push. Un job PIT manuel ou dédié est utile pour reproduire les résultats si son coût reste raisonnable.

**Fin d'étape :** action verte sur le commit de remise, lien permanent vers l'exécution, rapports prouvant l'exécution de tous les nouveaux tests. Une réussite sur un ancien commit ne suffit pas.

### 10. Construire le README final et remettre

Plan du rapport unique à la racine :

1. Binôme, objectif, dépôt et commit de référence.
2. Environnement et commandes de reproduction.
3. Classes choisies : tests existants, couverture et survivants initiaux.
4. Configuration Maven de ChatUniTest et modèle local.
5. Génération : fichiers, résultats bruts, réparations automatiques et corrections humaines.
6. Analyse critique des oracles, comparée aux tests originaux.
7. Protocole PIT et résultats A/B/C, par classe et au total.
8. Analyse des mutants détectés par les tests IA.
9. Tests manuels, justification des oracles et survivants finaux.
10. GitHub Actions et liens vers les preuves.
11. Limites de l'expérience et déclaration de l'utilisation de l'IA, y compris l'aide à la préparation de ce plan.

Le dépôt contient déjà `README.md`. Au moment de la consolidation, éviter deux fichiers distingués seulement par la casse sur macOS : effectuer si nécessaire un renommage Git en deux étapes et préserver l'attribution et les informations utiles du projet. Ne pas confondre ce rapport technique avec le petit fichier du binôme dans le dépôt du cours.

Dans `tache2/AQEL_SAIDANA/readme.md` du dépôt **du cours**, conserver les noms et les libellés, puis ajouter uniquement les deux URL demandées, directement après les deux-points :

```text
- Lien vers le répertoire GitHub : https://github.com/COMPTE/DEPOT
- Lien vers le README du répertoire : https://github.com/COMPTE/DEPOT/blob/BRANCHE/readme.md
```

D'après la copie locale de la branche 2026 du cours, l'échéance est le **13 octobre 2026 à 17 h, heure de Montréal**. Le workflow de remise contrôle la **date de création de la PR** et accepte les liens à partir du **9 octobre 2026 à 00 h, heure de Montréal**. Créer une nouvelle PR de remise dans cette fenêtre ; compléter une PR créée avant cette date ne satisfait pas ce contrôle. Revalider ces règles sur la branche officielle avant soumission : l'accès web au README général n'a pas abouti pendant cette préparation.

### 11. Calendrier et répartition proposés

| Période | Travail | Responsable proposé | Résultat attendu |
|---|---|---|---|
| 26–28 septembre | État initial, compatibilité des outils, présélection | Hamza ; relecture Mohamed | Build reproductible, PIT et génération pilotes. |
| 29–30 septembre | Couverture, mutation A, choix définitif | Mohamed ; relecture Hamza | Classes admissibles et archive A. |
| 1–3 octobre | Génération locale, conservation du brut, corrections tracées | Hamza ; relecture Mohamed | Tests IA exécutables et registre complet. |
| 4–5 octobre | Analyse des oracles, mutation B, attribution des détections | Mohamed ; relecture Hamza | Archive B et tableaux explicatifs. |
| 6–7 octobre | Tests manuels, mutation C, CI finale | Les deux | Archive C et tests verts. |
| 8–9 octobre | Consolidation du README et audit du barème | Les deux | Rapport autonome et vérifiable. |
| 9–12 octobre | Nouvelle PR de remise et vérification des liens | Les deux | Remise valide avant l'échéance. |
| 13 octobre avant 17 h | Marge de correction seulement | Les deux | Aucun résultat essentiel laissé à la dernière minute. |

Répartition indicative à adapter au binôme. La personne qui n'a pas produit une analyse vérifie ses oracles et la correspondance entre chiffres et rapports. Si le temps manque, réduire le nombre de classes dans la limite autorisée plutôt que sacrifier la traçabilité ou la documentation.

### 12. Liste de contrôle avant remise

- [ ] 1 à 3 classes autorisées, tests préexistants, couverture incomplète et mutants survivants prouvés.
- [ ] ChatUniTest intégré à Maven et génération attestée avec un modèle ouvert local.
- [ ] Versions, paramètres, matériel, commandes et chemins de sortie documentés.
- [ ] Productions brutes conservées ; réparations automatiques et corrections humaines distinguées et comptées.
- [ ] Tous les tests générés retenus sont expliqués et leurs oracles critiqués.
- [ ] Comparaison explicite avec les tests originaux écrits à la main.
- [ ] Mesures A/B/C comparables, mêmes sources de production et même configuration PIT.
- [ ] Rapports et effectifs cohérents ; statuts, dénominateurs et gains expliqués.
- [ ] Chaque mutant nouvellement détecté est relié à un test et à une assertion justifiée.
- [ ] Chaque test manuel possède les quatre éléments exigés ; survivants finaux analysés.
- [ ] Tous les nouveaux tests s'exécutent avec succès dans GitHub Actions au commit remis.
- [ ] Rapport final autonome dans le seul `readme.md` de remise à la racine.
- [ ] Usage de l'IA déclaré, liens accessibles et nouvelle PR créée dans la fenêtre autorisée.

### Références de préparation

- [Consignes de la tâche 2](https://github.com/umontreal-diro/IFT3913/blob/2026/tache2/readme.md), recoupées avec la copie locale et les instructions fournies.
- [README du cours : cas d'étude, échéances et usage de l'IA](https://github.com/umontreal-diro/IFT3913/blob/2026/README.md), consulté dans la copie locale.
- [Modèle de remise](https://github.com/umontreal-diro/IFT3913/blob/2026/.github/PULL_REQUEST_TEMPLATE/tache2-readme.md).
- [Validation des remises](https://github.com/umontreal-diro/IFT3913/blob/2026/.github/workflows/validate-tache2-readme.yml), consultée dans la copie locale.
- [Plugin Maven ChatUniTest](https://github.com/ZJU-ACES-ISE/chatunitest-maven-plugin).
- [API compatible proposée par Ollama](https://docs.ollama.com/api/openai-compatibility).
- [PIT avec Maven](https://pitest.org/quickstart/maven/) et [connecteur JUnit](https://github.com/pitest/pitest-junit5-plugin).
- Configuration locale inspectée : `tika-parent/pom.xml`, `.mvn/maven.config`, `.github/workflows/main-jdk17-build.yml` et sources des classes candidates.

## Réalisation de l'étape 1 — état initial local

Le binôme est déjà constitué ; seuls les liens de remise restent à ajouter ultérieurement. Aucun push, commit ou changement du fichier du binôme n'a été effectué pendant cette étape.

| Élément | Valeur constatée |
|---|---|
| Branche locale créée | `tache2-tests` |
| Commit de départ | `a2d75c2c10563b261148e9bafe4046832b33c1a6` |
| Dépôt distant configuré | `https://github.com/EnzoT454/tika.git` |
| Java | OpenJDK Homebrew 17.0.18, compilateur 17.0.18 |
| Maven | 3.9.12, via `mvnw` |
| Système | macOS 26.6.2, aarch64 ; locale `fr_CA`, UTF-8 |
| Résultat Maven | `BUILD SUCCESS`, code de sortie 0 |

### Difficulté liée au disque externe et solution

La première exécution dans le dépôt a échoué dans Checkstyle : un fichier auxiliaire macOS `._KebabCaseConverter.java` était traité comme du Java. Une deuxième tentative avec `-Dcheckstyle.excludes=**/._*` a dépassé ce contrôle, mais Surefire a ensuite essayé de charger `org.apache.tika.annotation.._KebabCaseConverterTest`. Ces échecs précèdent l'exécution des tests de `tika-core` ; ce ne sont pas des assertions échouées du projet.

La solution a été de construire une copie temporaire sur le disque interne, sans les fichiers `._*`. Elle contient les fichiers racine suivis, `.mvn`, les trois modules nécessaires (`tika-parent`, `tika-annotation-processor`, `tika-core`) et les POM des autres modules pour la découverte du réacteur. Les 613 fichiers copiés sont inventoriés avec leur SHA-256. Aucun fichier source copié n'a changé pendant la compilation ; aucun code de production ni test original n'a été modifié dans le dépôt.

Commande réussie dans la copie locale :

```bash
./mvnw -B -pl tika-core -am clean install
```

Cette exécution n'utilise ni exclusion Checkstyle supplémentaire, ni `-Pfast`, ni `-DskipTests`. Les contrôles configurés par le projet restent inchangés. Le journal indique notamment que l'audit OSS Index est déjà désactivé par la configuration du projet ; cette exécution ne constitue pas un audit de sécurité.

Pour recréer une copie équivalente à partir des fichiers suivis de la version courante, depuis la racine :

```bash
build_dir=$(python3 evidence/tache2/prepare-local-build.py)
cd "$build_dir"
./mvnw -B -pl tika-core -am clean install
```

Le script copie les fichiers suivis dans leur état de travail courant, sans leurs attributs macOS. Il est destiné à cette référence initiale ; avant les étapes suivantes, intégrer explicitement les nouveaux tests ou configurations non suivis à la copie. Ne pas supposer qu'un fichier non suivi y est présent. Le chemin utilisé pour cette mesure est conservé dans `evidence/tache2/initial/staging-directory.txt`.

### Résultats vérifiés

| Module | Cas comptabilisés par Surefire | Réussis | Échecs | Erreurs | Ignorés |
|---|---:|---:|---:|---:|---:|
| `tika-core` | 749 | 747 | 0 | 0 | 2 |
| `tika-annotation-processor` | 8 | 8 | 0 | 0 | 0 |

Les deux tests ignorés dans la suite d'origine sont `TikaInputStreamTest.reproduceRandomizedTestFailure` (reproduction manuelle d'un échec à partir d'une graine) et `CustomErrorHandlerTest.testUndeclaredEntityXML` (désactivé avec la mention « TODO -- rework without xerces »). Aucun test n'a été désactivé pour cette étape. Failsafe ne trouve pas de tests d'intégration à exécuter dans ce périmètre.

Couverture globale initiale de `tika-core`, issue des compteurs JaCoCo : **5 462 / 10 716 lignes** et **2 105 / 4 244 branches**. Ces valeurs portent sur le module entier ; elles ne justifient pas encore le choix d'une classe pour la tâche 2.

Preuves conservées hors de `target/` :

- [Rapport JaCoCo initial de tika-core](evidence/tache2/initial/jacoco/index.html).
- [Résultats structurés : tests, exclusions d'origine et couverture](evidence/tache2/initial/results.json).
- [Journal de la compilation réussie](evidence/tache2/initial/build.log).
- [Manifest des sources et empreintes SHA-256](evidence/tache2/initial/source-manifest.json).
- Rapports XML et texte Surefire dans `evidence/tache2/initial/surefire-reports/`, rapports du processeur d'annotations dans les dossiers préfixés `annotation-processor-`.
- Journaux des deux tentatives initiales, commandes, versions, état Git initial et patch des modifications locales dans `evidence/tache2/initial/`.

L'étape 1 est terminée. La prochaine étape consiste à examiner les couvertures par classe et méthode, puis à confirmer les candidates avec PIT. Les preuves sont locales et n'ont pas été publiées.

## Réalisation de l'étape 2 — analyse de couverture et présélection

L'analyse réutilise le rapport XML JaCoCo de l'étape 1, sans relancer ni modifier les tests. Les empreintes SHA-256 des trois classes examinées et de leurs tests correspondent au manifeste initial. Les compteurs décrivent la couverture obtenue par **l'ensemble des tests originaux de `tika-core`**, pas uniquement les tests portant le nom de la classe.

### Résultat de la présélection

| Priorité | Classe | Tests directs exécutés avec succès | Lignes couvertes | Branches couvertes | Décision |
|---|---|---:|---|---|---|
| 1 | `org.apache.tika.io.LookaheadInputStream` | 6 | 32 / 40 — 80,00 % | 14 / 16 — 87,50 % | Présélectionnée : lecture par tableau entièrement non couverte, classe petite et déterministe. |
| 2 | `org.apache.tika.io.FilenameUtils` | 10 | 154 / 175 — 88,00 % | 86 / 110 — 78,18 % | Présélectionnée : lacunes dans plusieurs comportements utiles, plus de branches à analyser. |
| — | `org.apache.tika.io.TailStream` | 9 | 61 / 61 — 100 % | 22 / 22 — 100 % | Écartée : ne satisfait pas le critère de couverture initiale incomplète. |

Ces classes appartiennent à `tika-core`, module autorisé par le cours. L'existence de tests préalables et la couverture incomplète sont démontrées pour les deux premières. **Aucun mutant survivant n'est encore démontré.** Une couverture de 100 % ne prouve pas l'absence de mutants survivants ; l'exclusion de `TailStream` repose sur le critère de couverture de la consigne.

### Candidate prioritaire : LookaheadInputStream

Sources : [classe](tika-core/src/main/java/org/apache/tika/io/LookaheadInputStream.java), [tests originaux](tika-core/src/test/java/org/apache/tika/io/LookaheadInputStreamTest.java), [couverture détaillée](evidence/tache2/initial/jacoco/org.apache.tika.io/LookaheadInputStream.html).

La classe permet de consulter un nombre borné d'octets d'un flux, avec restauration de la position du flux sous-jacent. Les tests existants vérifient les flux nul et vide, la limite de lecture, une limite nulle, `mark/reset` et `skip`. Ils utilisent la lecture d'un octet à la fois, avec des valeurs attendues précises et une vérification de la restauration du flux après fermeture.

| Méthode | Lignes couvertes | Branches couvertes | Lacune constatée |
|---|---|---|---|
| `read(byte[], int, int)` | 0 / 7 | 0 / 2 | Aucune exécution de la surcharge, lignes exécutables 103–108 et 110. |
| `markSupported()` | 0 / 1 | Sans branche | Valeur retournée ligne 129 jamais vérifiée ni exécutée. |
| Les 8 autres méthodes, constructeur inclus | 32 / 32 | 14 / 14 | Déjà exécutées ; la qualité de détection reste à mesurer avec PIT. |

Axes pour la génération future, sans ajout de tests à ce stade :

- Lecture dans un tableau avec un décalage non nul : vérifier le nombre d'octets lus, leur valeur et la conservation des cases hors de la zone écrite.
- Demande de lecture plus grande que les données disponibles dans la fenêtre : vérifier le nombre effectivement lu et le respect de la limite.
- Lectures successives jusqu'à la fin de la fenêtre : vérifier l'avancement de position et le résultat de fin de flux pour une demande de longueur positive.
- Séquence lecture par tableau puis `mark/reset` et fermeture : vérifier la cohérence de l'état et la restauration du flux sous-jacent.
- Vérification explicite de `markSupported()` ; ce test simple complète la couverture mais ne doit pas constituer l'essentiel de l'analyse.

Les oracles devront s'appuyer sur le contrat et sur des données connues, sans recopier le calcul interne de la méthode. Pour les arguments invalides ou les lectures de longueur nulle, examiner le contrat avant d'adopter le comportement courant comme résultat attendu. Si un bogue apparaît, le distinguer de l'expérience d'amélioration des tests.

**Point de décision PIT :** les mutations de la surcharge jamais exécutée peuvent être `NO_COVERAGE`. Cela ne démontre pas à lui seul des mutants `SURVIVED` dans cette classe. Examiner également les méthodes déjà exécutées avant de confirmer cette candidate.

### Deuxième candidate : FilenameUtils

Sources : [classe](tika-core/src/main/java/org/apache/tika/io/FilenameUtils.java), [tests originaux](tika-core/src/test/java/org/apache/tika/io/FilenameUtilsTest.java), [couverture détaillée](evidence/tache2/initial/jacoco/org.apache.tika.io/FilenameUtils.html).

La classe traite les noms de fichiers, les chemins incorporés et les extensions. Les dix tests directs existants comportent de nombreuses assertions de chaînes exactes ; dix méthodes de test ne signifient donc pas seulement dix entrées testées. `testResolveWithin` utilise des chemins construits avec `Paths.get` sans créer les fichiers correspondants, ce qui laisse le traitement des chemins existants non exécuté dans la mesure initiale.

| Méthode | Lignes couvertes | Branches couvertes | Lacune et axe à explorer |
|---|---|---|---|
| `resolveWithin(Path, String)` | 6 / 10 | 3 / 8 | Conversion en chemins réels et contrôle associé non exécutés, lignes 306–309 ; utiliser des fichiers temporaires existants et, si pertinent, des liens symboliques. |
| `getSanitizedEmbeddedFileName(...)` | 30 / 32 | 15 / 18 | Retours anticipés non exécutés et combinaison de guillemets partiellement couverte. |
| `getSanitizedEmbeddedFilePath(...)` | 41 / 45 | 20 / 24 | Retours anticipés et chemin trop long avec nom final assez court ; retour ligne 268 non exécuté. |
| `getEmbeddedPath(Metadata)` — privée | 10 / 13 | 5 / 8 | Plusieurs métadonnées de repli ne produisent jamais le résultat dans la suite initiale. |
| `getEmbeddedName(Metadata)` — privée | 10 / 13 | 5 / 8 | Même lacune pour les sources de noms de repli. |
| `calculateExtension(...)` | 7 / 8 | 3 / 4 | Retour `.bin` ligne 402 non exécuté lorsque la recherche d'extension ne fournit rien. |
| `lookupExtension(String)` — privée | 5 / 8 | 1 / 2 | Absence d'extension et traitement d'un type MIME invalide à explorer via l'API publique. |
| `getName(String)` | 9 / 9 | 7 / 8 | Condition sur `.` et `..` partiellement couverte ; toutes les lignes exécutées ne signifient pas toutes les branches testées. |
| `getPrefixLength(String)` — privée | 6 / 6 | 7 / 10 | Condition composée sur le préfixe de chemin partiellement couverte. |

Priorités proposées pour limiter la complexité :

1. Sources de métadonnées de repli : construire des métadonnées où les sources prioritaires sont absentes, puis vérifier le nom ou chemin exact par les méthodes publiques. Les helpers des tests originaux renseignent habituellement `RESOURCE_NAME_KEY` et `EMBEDDED_RESOURCE_PATH` ensemble, ce qui laisse d'autres cas de sélection à explorer.
2. Réduction d'un chemin trop long lorsque le nom final tient dans la limite : vérifier le résultat exact et la conservation de l'extension.
3. Résolution de chemins réellement présents, à l'aide de répertoires temporaires JUnit ; étendre aux liens symboliques seulement si l'environnement local et la CI permettent une exécution fiable.
4. Extension de repli : clarifier le contrat et ses limites avant de fixer l'oracle, car la Javadoc de `calculateExtension` et le retour final `.bin` méritent d'être distingués selon le cas.

Les méthodes privées seront exercées par l'API publique, sans ajouter de visibilité ni utiliser la réflexion uniquement pour augmenter la couverture. Certains retours défensifs peuvent être difficiles ou impossibles à atteindre après les transformations précédentes : leur atteignabilité reste à vérifier. Ne pas promettre 100 % de couverture ni déclarer leurs futurs mutants équivalents sans démonstration.

`normalize` est déjà couverte à 9 / 9 lignes et 8 / 8 branches ; `getSuffixFromPath` à 7 / 7 lignes et 6 / 6 branches. Elles ne justifient donc pas, à elles seules, le choix fondé sur un manque de couverture. Le constructeur implicite de `FilenameUtils` représente une ligne non couverte ; instancier cette classe utilitaire pour gagner cette ligne apporterait peu à l'objectif de détection.

### Preuves et reproduction de l'analyse

```bash
python3 evidence/tache2/analyze-initial-coverage.py
```

Le script vérifie les empreintes des classes et tests examinés, lit les compteurs du rapport XML initial et produit :

- [Inventaire de couverture des classes du module](evidence/tache2/selection/class-coverage.csv), incluant les classes internes ; une valeur vide de couverture de branches signifie qu'aucune branche n'est comptabilisée.
- [Détail des candidates](evidence/tache2/selection/candidates.json) : compteurs par méthode avec signature JVM, lignes non couvertes ou partielles, noms des tests originaux, résultats Surefire et décision provisoire.

L'empreinte du XML JaCoCo et le commit initial figurent dans les données pour relier la présélection à la mesure exacte. Aucun test supplémentaire, changement de code Java ou de POM n'a été réalisé pendant cette étape ; aucun push n'a été effectué.

**Prochaine étape :** intégrer PIT et vérifier sa compatibilité avec JUnit 6, puis mesurer les mutants des deux classes présélectionnées avec les tests originaux. Confirmer uniquement les candidates qui satisfont aussi le critère des mutants survivants ; revenir à l'inventaire si nécessaire.

## Réalisation des étapes 3 et 4 — intégration PIT, mutation initiale A et choix définitif

### Configuration mise en place et compatibilité vérifiée

Le profil **`mutation`** est ajouté dans [tika-core/pom.xml](tika-core/pom.xml). Il active `org.pitest:pitest-maven:1.25.9` avec `org.pitest:pitest-junit5-plugin:1.2.3`, conformément aux versions fournies pour la tâche. Hors de ce profil, la compilation habituelle n'exécute pas PIT.

La compatibilité a été vérifiée par une exécution réussie, et non seulement par la résolution des dépendances : le journal PIT annonce le support JUnit, charge automatiquement `junit-platform-launcher:6.1.3`, calcule la couverture et termine l'analyse avec `BUILD SUCCESS`. Aucune modification des versions JUnit ou des tests n'a été nécessaire. Cette validation concerne cette configuration et ce périmètre précis.

| Paramètre figé pour A/B/C | Valeur |
|---|---|
| Classes mutées | `org.apache.tika.io.LookaheadInputStream`, `org.apache.tika.io.FilenameUtils` |
| Tests admissibles | `org.apache.tika.*` : tous les tests correspondants du module, pas seulement les deux classes de tests directs |
| Opérateurs | Groupe `DEFAULTS` de PIT 1.25.9 |
| Processus de travail | `threads = 2` |
| Délais | `timeoutFactor = 1.25`, `timeoutConstant = 4000 ms` |
| Historique incrémental | `withHistory = false` |
| Échec si aucun mutant | `failWhenNoMutations = true` |
| Rapports | HTML, XML et export de couverture par ligne |
| Exclusions ajoutées de classes, méthodes ou tests | Aucune ; filtres standards de PIT conservés |

Le groupe d'opérateurs est fixé par le numéro de version et le nom `DEFAULTS`. Le journal consigne les opérateurs ayant effectivement produit des mutants. Ne pas remplacer ce groupe ou mettre à jour PIT entre A, B et C sans refaire la référence A.

Références de configuration consultées : [PIT pour Maven](https://pitest.org/quickstart/maven/) et [connecteur JUnit](https://github.com/pitest/pitest-junit5-plugin). Les résultats ci-dessous proviennent des exécutions locales archivées.

### Commandes exécutées et reproduction

Une nouvelle copie temporaire locale a été créée avec le POM modifié, pour éviter les fichiers auxiliaires macOS du disque externe. Dans cette copie :

```bash
./mvnw -B -pl tika-core -am clean install
./mvnw -B -pl tika-core -Pmutation org.pitest:pitest-maven:mutationCoverage
```

Les deux commandes ont terminé avec un code de sortie 0. La première retrouve 749 cas Surefire pour `tika-core`, dont 747 réussis et les deux cas déjà désactivés, sans échec ni erreur. Le réacteur construit aussi le processeur d'annotations et exécute ses tests. Les empreintes des fichiers Java correspondent à l'état initial ; seuls la configuration Maven et les documents de travail ont évolué.

Pour reproduire sur cette machine, depuis la racine du dépôt :

```bash
build_dir=$(python3 evidence/tache2/prepare-local-build.py)
cd "$build_dir"
./mvnw -B -pl tika-core -am clean install
./mvnw -B -pl tika-core -Pmutation org.pitest:pitest-maven:mutationCoverage
```

Sur un système de fichiers qui ne produit pas de `._*`, les deux commandes Maven peuvent être exécutées directement depuis la racine du dépôt. Le rapport produit est dans `tika-core/target/pit-reports/`. L'archive de cette expérience est dans `evidence/tache2/A/` du dépôt original. La reproduction via le script doit toujours inclure tous les tests nouveaux lorsqu'ils seront ajoutés ; le script actuel ne copie que les fichiers suivis.

### Scores de mutation initiaux

| Classe | Total | KILLED | SURVIVED | NO_COVERAGE | Autres statuts | Score : KILLED / total |
|---|---:|---:|---:|---:|---:|---:|
| `LookaheadInputStream` | 33 | 19 | 5 | 9 | 0 | 57,58 % |
| `FilenameUtils` | 115 | 69 | 30 | 16 | 0 | 60,00 % |
| **Total** | **148** | **88** | **35** | **25** | **0** | **59,46 %** |

Le score global est `100 × 88 / 148`, sans retrait des mutants non couverts et sans moyenne simple des scores des classes. Aucun timeout, mutant non viable, erreur d'exécution ou erreur mémoire n'est recensé. Le nombre de mutants indiqués comme détectés dans le XML correspond ici exactement aux mutants `KILLED`.

Le résumé PIT arrondit le score global à 59 %. Il affiche aussi une force des tests de 72 %, calculée sur les seuls mutants couverts (`88 / (148 − 25)`, soit 71,54 %) : cette métrique ne remplace pas le score sur tous les mutants. Sa couverture de lignes des classes ciblées est 186 / 215, soit 86,51 %, cohérente avec les compteurs JaCoCo des deux classes réunies.

Le journal PIT annonce 378 éléments examinés et 311 exécutions de tests pendant la mutation. Ces compteurs propres à PIT ne sont pas le nombre de cas Surefire : PIT sélectionne les tests pertinents pour chaque mutant et peut les réexécuter. Le journal complet et les tests tueurs du XML constituent les preuves d'exécution.

### Justification du choix définitif et exemples de survivants

Les deux classes retenues ont des tests préexistants réussis, une couverture initiale incomplète et de vrais statuts **`SURVIVED`**, distincts de `NO_COVERAGE`. Le critère de sélection est donc maintenant étayé pour chacune.

Les identifiants ci-dessous sont calculés à partir de la classe, de la signature de méthode, de l'opérateur et des index de mutation PIT. Ils permettent de retrouver le même mutant entre les états si le code de production et les paramètres restent identiques.

| Identifiant | Emplacement | Mutation et statut A | Interprétation initiale / piste pour la suite |
|---|---|---|---|
| `4454644d58518a71` | `LookaheadInputStream`, constructeur, ligne 68 | Suppression de `stream.mark(n)` ; `SURVIVED` | Les tests directs utilisent des `ByteArrayInputStream` initialement au début. Explorer une position initiale non nulle pour rendre observable l'absence de marque. |
| `2860eff98480611b` | `LookaheadInputStream.fill`, ligne 82 | Soustraction remplacée par addition dans la longueur demandée ; `SURVIVED` | Lorsque le tampon est encore vide, les deux calculs donnent la même valeur. Explorer un flux qui remplit le tampon en plusieurs lectures courtes. |
| `c98f2b1125885598` | `LookaheadInputStream.fill`, ligne 86 | Suppression de l'appel à `close()` en fin de flux ; `SURVIVED` | Examiner la restauration immédiate du flux sous-jacent lorsque la fin est rencontrée, avant une fermeture explicite par le test. |
| `1694949b679ac2b9` | `FilenameUtils.getEmbeddedName`, ligne 369 | Condition de sélection d'une métadonnée de repli modifiée ; `SURVIVED` | Exercer la source `INTERNAL_PATH` par la méthode publique avec la source prioritaire absente. |
| `9f319e59c9b7bcd3` | `FilenameUtils.resolveWithin`, ligne 305 | Condition de vérification des chemins existants modifiée ; `SURVIVED` | Les tests directs créent des objets `Path` sans préparer les fichiers ; vérifier les comportements avec un répertoire temporaire réel. |
| `e8b83566eb240ba8` | `FilenameUtils.calculateExtension`, ligne 393 | Condition sur l'absence de type MIME modifiée ; `SURVIVED` | Les tests directs emploient `.bin` comme valeur de repli, ce qui peut masquer une différence ; examiner un repli distinct et justifié par le contrat. |

Ces pistes sont des hypothèses issues du code et des tests existants ; aucun nouveau test n'a encore été écrit pour les valider. Le statut survivant ne prouve pas qu'un mutant est tuable : les 35 cas devront être analysés, notamment les changements de bornes et contrôles défensifs potentiellement équivalents.

### Archives disponibles et contrôles

- [Rapport PIT HTML de l'état A](evidence/tache2/A/pit-reports/index.html).
- [Rapport XML PIT original](evidence/tache2/A/pit-reports/mutations.xml).
- [Résumé par classe et global](evidence/tache2/A/mutation-summary.json).
- [Inventaire des 148 mutants en CSV](evidence/tache2/A/mutants.csv) et [JSON](evidence/tache2/A/mutants.json), avec statuts, transformations et tests tueurs disponibles.
- [Journal PIT](evidence/tache2/A/pit.log) et [journal de compilation et tests](evidence/tache2/A/build.log).
- Copie du POM, patch de configuration, commandes, code de sortie, manifeste SHA-256 et vérification des sources dans `evidence/tache2/A/`.
- Rapports Surefire, compteurs JaCoCo XML/CSV et données d'exécution JaCoCo archivés pour A.

Pour recalculer les tableaux depuis l'archive, sans relancer PIT :

```bash
python3 evidence/tache2/summarize-mutations.py A
```

Pour ouvrir le rapport depuis n'importe quel dossier de cette machine :

```bash
open /Volumes/TOSHIBA_EXT/ift3913/tika/evidence/tache2/A/pit-reports/index.html
```

La configuration est prête pour les comparaisons futures. L'étape suivante sera l'installation et la génération avec ChatUniTest et un modèle ouvert local. Aucun push n'a été effectué ; ni le code de production, ni les tests originaux, ni le fichier du binôme n'ont été modifiés.

## Réalisation de l'étape 5 — ChatUniTest, modèle local et pilote

**Étape terminée le 27 septembre 2026.** Le pilote démontre le fonctionnement de la génération locale ; il ne remplace pas la génération étendue ni l'analyse des oracles et de la mutation B.

### Configuration finale validée

| Élément | Valeur |
|---|---|
| Plugin Maven | `io.github.zju-aces-ise:chatunitest-maven-plugin:2.1.1` |
| Profil | `chatunitest` dans `tika-core/pom.xml` |
| Modèle réellement exécuté | `qwen2.5-coder:3b`, 3,1 milliards de paramètres, `Q4_K_M`, environ 1,9 Go |
| Licence du modèle | Qwen Research License, texte archivé ; ne pas l'assimiler à Apache 2.0 |
| Alias technique | `codeqwen:v1.5-chat`, mêmes poids et même empreinte que le modèle réel |
| Serveur | Ollama local, `http://127.0.0.1:11434/v1/chat/completions` |
| Paramètres | `testNumber=1`, `maxRounds=3`, température 0,2, contexte maximal du prompt 6 000 tokens, réponse maximale 1 024 tokens |
| Exécution | Séquentielle (`enableMultithreading=false`), validation active (`noExecution=false`), fusion désactivée (`enableMerge=false`) |
| Dépendance spécifique | `mockito-junit-jupiter`, version définie par le parent, portée `test`, uniquement dans le profil `chatunitest` |
| Prompts adaptés | `tika-core/src/test/chatunitest-prompts/` : génération, consigne système et réparation |

Ollama et le modèle étaient déjà installés. Aucun nouveau modèle n'a été téléchargé. Le modèle original n'a pas été remplacé : un alias a été ajouté car ChatUniTest 2.1.1 refuse les noms absents de son registre interne. Le fichier `model-alias.json` prouve l'identité des empreintes. La valeur `ollama-local` du champ `apiKeys` est un paramètre technique ; les requêtes visent le serveur local.

Les noms des paramètres ont été vérifiés dans le descripteur du plugin téléchargé : dans le POM, `enableMultithreading` et `enableMerge` correspondent aux propriétés CLI `thread` et `merge`. Le paramètre `prune`, mentionné dans une documentation consultée, n'est pas reconnu par cette version et a été retiré. Le fichier `plugin-descriptor.xml` est conservé.

### Essais conservés et ajustements

Le pilote vise `org.apache.tika.io.LookaheadInputStream#markSupported`, une méthode jamais exécutée dans le rapport initial. Son comportement simple permet de vérifier la chaîne technique avant les cas plus complexes.

| Essai | Résultat | Réponses du modèle | Tours de réparation automatiques | Ajustement humain de configuration ensuite |
|---|---|---:|---:|---|
| 1 | Arrêt du plugin : nom `qwen2.5-coder:3b` non accepté. | 0 | 0 | Création de l'alias Ollama ; correction des paramètres du plugin. |
| 2 | Trois versions échouent à la compilation : imports Mockito absents du module. | 3 | 2 | Ajout de Mockito dans le seul profil de génération. |
| 3 | Trois versions échouent à la compilation : appels inventés à `ReflectionUtils`. | 3 | 2 | Prompts adaptés pour demander un test court, l'API publique et aucun utilitaire inventé. |
| 4 | Compilation et exécution réussies au tour 0 : une méthode `@Test`. | 1 | 0 | Aucune retouche du Java généré. |

**Corrections manuelles des fichiers Java générés : 0.** Les ajustements concernent l'environnement, le POM et les prompts, pas une correction cachée du test. Les réparations automatiques des essais 2 et 3 ont été conservées, même si elles n'ont pas abouti. Le premier essai de génération contenait aussi une assertion incorrecte sur `markSupported()` après fermeture ; elle constitue un exemple de faiblesse à discuter dans l'analyse qualitative future.

Attention : ChatUniTest a affiché `BUILD SUCCESS` pour les essais 2 et 3 malgré les échecs de compilation de tous leurs tests. La réussite du pilote est établie par le message explicite de compilation/exécution, puis par le rapport Surefire indépendant, et non par le seul code de sortie Maven.

Les prompts adaptés demandent un ou deux tests focalisés, des objets Java ordinaires et des assertions déduites du comportement fourni. Ils ne contiennent ni test Java préécrit ni valeur de résultat propre à `markSupported()`. Le modèle reste l'auteur du test ; ChatUniTest peut effectuer ses transformations automatiques, notamment ajouter des imports.

### Test produit et validation indépendante

Fichier archivé : [LookaheadInputStream_markSupported_6_0_Test.java](evidence/tache2/generation/pilot-attempt-4/LookaheadInputStream_markSupported_6_0_Test.java).

Il construit un `LookaheadInputStream` et vérifie `assertTrue(lookaheadInputStream.markSupported())`. Cet oracle correspond à la capacité annoncée par cette méthode ; le test est utile pour valider l'installation mais reste simple et insuffisant pour couvrir les comportements de lecture et les survivants identifiés.

ChatUniTest a exécuté **1 test, réussi, aucun échec, aucun test ignoré**. Le même fichier, copié sans modification dans les sources de test de la copie temporaire, a ensuite été compilé par Maven et exécuté avec Surefire 3.5.6 et **JUnit 6.1.3** : **1 test réussi, 0 échec, 0 erreur, 0 ignoré**. Son empreinte a été comparée avant et après la copie.

La vérification indépendante appelle directement `compiler:testCompile` et `surefire:test` pour tester le Java brut. Elle ne constitue pas une validation complète du style, des licences ou de GitHub Actions. Le fichier comporte notamment des imports ajoutés inutiles : son intégration propre dans la suite du dépôt et les corrections de format éventuelles sont tracées à l'étape 6.

Le test reste dans les preuves et la copie temporaire : **aucun nouveau test n'est encore intégré à `tika-core/src/test/java/` du dépôt original**. L'état A et les sources de production sont inchangés ; aucune mesure B n'a été effectuée.

### Commandes de reproduction

Préparer l'alias une seule fois, après avoir vérifié les modèles existants :

```bash
ollama list
ollama cp qwen2.5-coder:3b codeqwen:v1.5-chat
```

Depuis la racine du dépôt, préparer une copie locale et générer le pilote :

```bash
build_dir=$(python3 evidence/tache2/prepare-local-build.py)
cd "$build_dir"
./mvnw -B -pl tika-core -am clean install
./mvnw -B -pl tika-core -Pchatunitest test-compile \
  io.github.zju-aces-ise:chatunitest-maven-plugin:2.1.1:method \
  '-DselectMethod=org.apache.tika.io.LookaheadInputStream#markSupported'
```

Depuis cette copie, copier le test obtenu puis vérifier son exécution indépendante :

```bash
cp tika-core/target/chatunitest-tests/tika-parent/tika-core/org/apache/tika/io/LookaheadInputStream_markSupported_6_0_Test.java \
  tika-core/src/test/java/org/apache/tika/io/
./mvnw -B -pl tika-core -Pchatunitest compiler:testCompile surefire:test \
  -Dtest=LookaheadInputStream_markSupported_6_0_Test
```

Le nom du test et le sous-chemin sont ceux observés dans cette expérience ; les vérifier si le code ou le plugin change. La génération n'est pas garantie identique à chaque exécution. Archiver les résultats avant tout `clean`.

Le script `prepare-local-build.py` a été amélioré pendant cette étape : il inclut désormais les fichiers **non suivis et non ignorés** des modules nécessaires, notamment les nouveaux prompts et les futurs tests. Les mises en garde des comptes rendus précédents décrivaient son ancienne version. Une copie de vérification a confirmé la présence des trois fichiers de prompt.

### Preuves et suite

- [Résumé structuré du pilote](evidence/tache2/generation/pilot-summary.json).
- [Journal de génération réussie](evidence/tache2/generation/pilot-attempt-4.log).
- [Journal de validation JUnit 6](evidence/tache2/generation/pilot-junit6.log).
- [Rapport Surefire XML du pilote](evidence/tache2/generation/TEST-org.apache.tika.io.LookaheadInputStream_markSupported_6_0_Test.xml).
- [Prompts, réponse du modèle et historique du tour réussi](evidence/tache2/generation/pilot-attempt-4/records.json).
- Configurations de chaque essai, journaux des échecs, versions brutes et archives de contexte dans `evidence/tache2/generation/`.

La génération étendue de l'étape 6 est consignée ci-dessous. Aucun push n'a été effectué.

## Réalisation de l'étape 6 — génération étendue et conservation du brut

**Étape terminée le 27 septembre 2026.** Les fichiers produits par le modèle, ses réparations automatiques, les commandes, les journaux et les validations indépendantes ont été archivés avant toute correction humaine. Les essais sont ciblés sur des méthodes liées aux lacunes de couverture ou aux mutants survivants de l'état A.

| Cible | Production ChatUniTest | Validation indépendante | Décision à ce stade |
|---|---|---|---|
| `LookaheadInputStream#read` | Aucune réponse : la sélection d'une surcharge est restée bloquée au chargement des informations de classe ; l'exécution a été arrêtée après 1 min 14 s, avant toute requête au modèle. | Sans objet. | Limitation du sélecteur du plugin consignée ; ne pas compter comme test généré. |
| `LookaheadInputStream#skip` | Trois versions brutes, soit la réponse initiale et deux réparations automatiques. | Les trois échouent à la compilation. | Conserver les échecs ; aucune correction humaine n'est appliquée à ce stade. |
| `FilenameUtils#calculateExtension` | Un fichier avec deux méthodes `@Test`. | Compile ; 1 test réussi et 1 échec : l'IA attend `"png"` au lieu de `".png"`. | Correction humaine nécessaire : modifier cette seule valeur attendue, puis relancer le test. |
| `FilenameUtils#resolveWithin` | Un fichier avec trois méthodes `@Test`, obtenu au tour 0. | Compile et réussit : 3 tests, 0 échec, 0 erreur, 0 ignoré. | Conserver le fichier brut ; l'utilité et les oracles seront examinés à l'étape 7. |

Le pilote de l'étape 5 reste une sortie utile : `LookaheadInputStream#markSupported` contient un test qui réussit sans retouche Java. En incluant ce pilote, **deux fichiers bruts réussissent sans correction humaine**, **un fichier compile mais échoue par un oracle incorrect**, et **trois versions brutes échouent à la compilation**. Les trois versions de `skip` ne sont pas trois tests distincts : elles correspondent aux tours 0, 1 et 2 d'une même tentative, que ChatUniTest a tenté de réparer automatiquement.

### Corrections et séparation des états

**Nombre de corrections humaines effectuées sur les fichiers Java générés : 0.** Le test de `calculateExtension` requiert une correction identifiée, mais elle n'a pas encore été appliquée : le fichier brut et l'échec sont donc reproductibles. Les échecs de `skip` proviennent d'accès au champ privé `position` aux tours 0 et 1, puis d'une tentative de réflexion qui omet les exceptions vérifiées au tour 2. Ils montrent que la réparation automatique n'a pas respecté la contrainte de l'API publique malgré les prompts adaptés.

Les fichiers ne sont pas ajoutés à `tika-core/src/test/java/` du dépôt original. Ils ont été copiés seulement dans une copie temporaire interne pour les compiler et les exécuter. Le code de production, la suite originale et l'état A restent donc inchangés. Les tests corrigés ou retenus devront être intégrés explicitement à l'étape suivante, avec leur historique conservé.

### Preuves et reproduction

- [Journal et versions ayant échoué pour `skip`](evidence/tache2/generation/stage6-lookahead-skip/).
- [Journal du blocage de la surcharge `read`](evidence/tache2/generation/stage6-lookahead-read/stage6-lookahead-read.log) et la tentative de capture de pile, restée vide parce que la JVM ne répondait pas à l'attachement.
- [Fichier brut et échec Surefire de `calculateExtension`](evidence/tache2/generation/stage6-filename-calculate-extension/).
- [Fichier brut, historique ChatUniTest et rapport Surefire réussi de `resolveWithin`](evidence/tache2/generation/stage6-filename-resolve-within/).

Pour reproduire une tentative, préparer d'abord la copie locale décrite à l'étape 5, puis remplacer la cible :

```bash
./mvnw -B -pl tika-core -Pchatunitest test-compile \
  io.github.zju-aces-ise:chatunitest-maven-plugin:2.1.1:method \
  '-DselectMethod=org.apache.tika.io.FilenameUtils#resolveWithin'
```

L'analyse des oracles de l'étape 7 est consignée ci-dessous.

## Réalisation de l'étape 7 — analyse qualitative des oracles IA

**Étape terminée le 27 septembre 2026.** Cette analyse compare les sorties brutes de ChatUniTest aux tests écrits à la main et aux mutants survivants de l'état A. Une assertion qui réussit n'est pas automatiquement utile : elle doit distinguer un comportement erroné plausible et son résultat attendu doit découler du contrat, sans reproduire inutilement l'implémentation.

| Sortie IA | Analyse de l'oracle | Comparaison avec les tests originaux et PIT A | Décision pour B |
|---|---|---|---|
| `LookaheadInputStream#markSupported` | `assertTrue(markSupported())` est correct : la méthode retourne explicitement `true`. L'oracle est précis, mais vérifie une constante et les imports Mockito ainsi que le faux flux sont inutiles. | Aucun test original n'appelle cette méthode ; elle ajoute une ligne de couverture. Elle ne cible cependant aucun des survivants A recensés dans le constructeur ou `fill()`. | Retenir seulement comme test de couverture, après nettoyage de style ; ne pas le présenter comme une amélioration forte de détection. |
| `FilenameUtils#calculateExtension`, type `image/png` | L'assertion doit être `assertEquals(".png", result)`, car `lookupExtension` retourne l'extension avec son point initial. `"png"` est donc un oracle faux, confirmé par Surefire. | Les tests originaux vérifient déjà des extensions avec leur point (`.pdf`) et exercent indirectement le type MIME `docx`, mais pas ce contrat direct avec PNG. | Retenir après une correction humaine de la valeur attendue et le nettoyage du Java généré. |
| `FilenameUtils#calculateExtension`, type MIME absent | `assertEquals("default.bin", result)` correspond exactement à la branche `mime == null`, qui retourne la valeur fournie sans transformation. Le choix d'un défaut différent de `.bin` rend la différence observable. | Cette entrée est plus discriminante que les appels originaux employant `.bin` comme défaut : elle peut détecter le survivant A `e8b83566eb240ba8`, qui modifie le traitement de l'absence de type MIME. | Retenir après le même nettoyage du fichier généré. |
| `FilenameUtils#resolveWithin` | Les trois assertions comparent un chemin enfant ordinaire à `dir.resolve(name)`. La troisième se nomme « ThrowsIOException » mais ne fournit aucun `..` et ne vérifie aucune exception. Elles n'atteignent ni normalisation significative, ni sortie du répertoire, ni protection contre les liens symboliques. | `FilenameUtilsTest.testResolveWithin` couvre déjà les enfants simples et imbriqués, le répertoire lui-même, trois traversées `..` et le cas du préfixe `a/bc` contre `a/b`. Il est donc plus spécifique que la sortie IA. | Écarter : redondant et partiellement mal nommé. Il ne justifie pas une nouvelle mesure B. |
| `LookaheadInputStream#skip`, tours 0 à 2 | Les valeurs `skip(2) == 2`, `available() == 3`, `skip(6) == 5` et `available() == 0` sont observables et cohérentes avec le flux de cinq octets. En revanche, l'accès direct à `position` est interdit ; la réparation finale utilise la réflexion, contraire au protocole, et oublie des exceptions vérifiées. | Le test original vérifie déjà `skip(1)`, la lecture de l'octet suivant, la limite et la restauration du flux. Les cas IA ajoutent la demande supérieure à la fenêtre, mais pas la lecture par tableau non couverte ni les conditions de remplissage court associées au survivant A dans `fill()`. | Ne pas intégrer tel quel. Une version corrigée pourrait conserver les seules assertions publiques et ajouter une lecture observable ; ce serait une correction humaine à documenter. |
| `LookaheadInputStream#read(byte[], int, int)` | Aucun oracle n'a été produit : le plugin est bloqué lors de la sélection de cette surcharge. | Cette surcharge reste la lacune de couverture la plus intéressante de la classe. | À traiter par un test manuel à l'étape 9 si la génération ne peut pas être relancée avec une sélection non ambiguë. |

### Bilan des oracles

Les sorties IA montrent trois qualités différentes. Le test `markSupported` est correct mais presque trivial. Le test `calculateExtension` contient une erreur factuelle facilement visible, mais sa seconde assertion utilise des données de test bien choisies et peut tuer un mutant survivant. Les tests `resolveWithin` passent sans apporter de comportement nouveau ; leur réussite est donc insuffisante comme argument de qualité. Les réponses sur `skip` montrent également que la réparation automatique peut dégrader le test en introduisant la réflexion après un accès privé refusé.

Les tests manuels existants sont globalement plus spécifiques : ils vérifient les données de sortie, les exceptions et la restauration du flux, tandis que les sorties IA emploient souvent des chemins ordinaires ou répètent des assertions accessibles directement. L'étape B conservera uniquement les candidats IA utiles après corrections tracées ; les données qui servent à viser la lecture par tableau et les survivants restants seront séparées comme tests manuels à l'étape 9.

### Corrections prévues et traçabilité

| Fichier brut | Correction humaine nécessaire avant intégration | Motif |
|---|---|---|
| `FilenameUtils_calculateExtension_10_0_Test.java` | Remplacer `"png"` par `".png"`, supprimer les imports inutilisés, appliquer le format et adopter un nom conforme à Surefire. | Rendre l'oracle exact et faire passer Checkstyle. |
| `LookaheadInputStream_markSupported_6_0_Test.java` | Supprimer les imports inutilisés et le faux flux inutile, appliquer le format et adopter un nom conforme à Surefire. | Le comportement testé est valide, mais le fichier brut n'est pas intégrable tel quel. |
| `LookaheadInputStream_skip_4_0_Test.java` | Repartir de la dernière intention valide, supprimer les accès à `position` et la réflexion, puis ajouter une observation publique de la lecture. | Les trois versions brutes ne compilent pas et ne respectent pas l'API publique. |

À l'issue de l'étape 7, ces corrections n'étaient pas encore appliquées : l'état A et les fichiers bruts demeurent intacts. L'étape 8, documentée ci-dessous, intègre les seuls candidats décidés ci-dessus dans une copie de travail, exécute B avec exactement les paramètres PIT de A et attribue les mutants nouvellement tués aux assertions correspondantes.

## Réalisation de l'étape 8 — état B, couverture et mutation avec les tests IA retenus

**Étape terminée le 27 septembre 2026.** Deux sorties IA ont été intégrées dans des classes de test distinctes, après corrections explicites. La copie temporaire de construction contient ainsi les tests originaux et les trois nouveaux cas issus de ChatUniTest. Le code de production, les classes PIT, les opérateurs, les délais et les paramètres JVM sont identiques à l'état A.

### Corrections appliquées avant B

| Fichier brut | Fichier intégré | Corrections humaines | Conservation de l'origine IA |
|---|---|---|---|
| `LookaheadInputStream_markSupported_6_0_Test.java` | `LookaheadInputStreamGeneratedTest.java` | Suppression des imports inutilisés et du faux flux ; remplacement par le constructeur public avec flux `null` ; ajout de l'en-tête et du format du projet. L'assertion `true` est inchangée. | Le brut et sa réponse sont archivés dans `generation/pilot-attempt-4/`. |
| `FilenameUtils_calculateExtension_10_0_Test.java` | `FilenameUtilsGeneratedTest.java` | Correction sémantique de `"png"` vers `".png"`, suppression des imports et exceptions inutiles, puis en-tête, format et noms conformes au projet. Les deux scénarios générés sont conservés. | Le brut, son empreinte et l'échec Surefire sont archivés dans `generation/stage6-filename-calculate-extension/`. |

Il y a donc **une correction sémantique d'oracle** et **deux opérations de mise en forme/intégration de fichiers**. Les assertions brutes, les entrées et l'intention du modèle restent visibles dans les archives ; aucune sortie n'est réattribuée à un test écrit manuellement.

### Exécution et couverture JaCoCo

La commande `./mvnw -B -pl tika-core -am clean install`, exécutée dans la copie temporaire, réussit avec **752 tests, 0 échec, 0 erreur et 2 tests désactivés**. Elle inclut les trois nouveaux cas : un dans `LookaheadInputStreamGeneratedTest` et deux dans `FilenameUtilsGeneratedTest`.

| Classe | JaCoCo A : lignes | JaCoCo B : lignes | JaCoCo A : branches | JaCoCo B : branches | Interprétation |
|---|---:|---:|---:|---:|---|
| `LookaheadInputStream` | 32 / 40 | 33 / 40 | 14 / 16 | 14 / 16 | `markSupported()` est maintenant exécutée ; aucune nouvelle branche. |
| `FilenameUtils` | 154 / 175 | 154 / 175 | 86 / 110 | 86 / 110 | `calculateExtension()` était déjà atteinte indirectement ; B en renforce l'oracle sans augmenter les compteurs. |

Le rapport complet est dans [B/jacoco/index.html](evidence/tache2/B/jacoco/index.html). La différence entre couverture et mutation est visible ici : le second test `calculateExtension` ne couvre pas une nouvelle ligne, mais rend observable un comportement que les tests initiaux ne distinguaient pas.

### Analyse PIT B et comparaison avec A

| Classe | A : KILLED / SURVIVED / NO_COVERAGE | B : KILLED / SURVIVED / NO_COVERAGE | Score A | Score B |
|---|---:|---:|---:|---:|
| `LookaheadInputStream` | 19 / 5 / 9 | 20 / 5 / 8 | 57,58 % | 60,61 % |
| `FilenameUtils` | 69 / 30 / 16 | 70 / 29 / 16 | 60,00 % | 60,87 % |
| **Total** | **88 / 35 / 25** | **90 / 34 / 24** | **59,46 %** | **60,81 %** |

Les identités des 148 mutants sont les mêmes dans A et B. Le script [compare-mutations.py](evidence/tache2/compare-mutations.py) compare les rapports XML et trouve exactement deux changements, détaillés dans [B/mutation-delta.json](evidence/tache2/B/mutation-delta.json).

| Mutant nouvellement tué | Test IA tueur | Pourquoi l'assertion le détecte |
|---|---|---|
| `LookaheadInputStream.markSupported`, ligne 129 : retour booléen remplacé par `false` ; statut A `NO_COVERAGE` → B `KILLED` | `LookaheadInputStreamGeneratedTest.markSupportedIsTrue` | L'assertion attend explicitement `true`. La mutation rend l'appel `false`, donc le test échoue. C'est un gain de couverture, mais un oracle simple. |
| `FilenameUtils.calculateExtension`, ligne 393 : condition `mime == null` modifiée ; statut A `SURVIVED` → B `KILLED` | `FilenameUtilsGeneratedTest.calculateExtensionReturnsProvidedDefaultWithoutMimeType` | Avec un `Metadata` sans type et un défaut distinct de `.bin`, le contrat impose `default.bin`. Le mutant emprunte l'autre comportement, ce qui ne retourne pas cette valeur ; l'assertion distingue donc les deux cas. |

Les tests IA **ne détectent pas tous les mutants** : B laisse 34 `SURVIVED` et 24 `NO_COVERAGE`. Le test PNG corrigé est valide, mais PIT n'attribue aucun nouveau mutant à cette assertion dans cette configuration ; il ne faut pas lui attribuer un gain non mesuré. Les survivants restants seront traités par des tests manuels ciblés à l'étape 9.

### Preuves et reproduction

- [Commandes, sources exactes et manifeste de B](evidence/tache2/B/commands.md).
- [Rapport PIT B](evidence/tache2/B/pit-reports/index.html), [résumé PIT B](evidence/tache2/B/mutation-summary.json) et [diff A/B](evidence/tache2/B/mutation-delta.json).
- [Rapports Surefire B](evidence/tache2/B/surefire-reports/) et [journal de construction](evidence/tache2/B/B-build.log).

L'état C et les tests manuels de l'étape 9 sont documentés ci-dessous.

## Réalisation de l'étape 9 — tests manuels ciblés et état C

**Étape terminée le 27 septembre 2026.** Neuf tests manuels ont été ajoutés dans [LookaheadInputStreamManualTest.java](tika-core/src/test/java/org/apache/tika/io/LookaheadInputStreamManualTest.java) et [FilenameUtilsManualTest.java](tika-core/src/test/java/org/apache/tika/io/FilenameUtilsManualTest.java). Ils proviennent de l'analyse des survivants et des zones `NO_COVERAGE` de B ; ils ne sont pas attribués à ChatUniTest.

### Cas de test manuels

| Test | Intention | Données choisies | Oracle et motivation |
|---|---|---|---|
| `arrayReadHonorsOffsetLengthAndEndOfLookahead` | Exercer la surcharge `read(byte[], off, len)` non couverte en B. | Flux `a,b,c`, fenêtre de deux octets, tableau de quatre octets rempli de `x`, lecture à l'offset 1. | Vérifie le nombre 2, chaque case modifiée et non modifiée, puis `-1` après la fenêtre. Les valeurs rendent observables `fill`, la borne, `arraycopy`, l'offset et le retour. |
| `arrayReadReturnsOnlyBytesRemainingAfterAByteRead` | Distinguer la longueur disponible après une lecture unitaire. | Flux `a,b,c`, fenêtre 2 ; lire `a`, puis demander deux octets dans un tableau. | Le résultat doit être 1 et le premier élément doit être `b`. Avec l'addition mutée à la ligne 105, la méthode tente de copier trop d'octets. |
| `closeRestoresThePositionAtConstruction` | Vérifier que le constructeur marque la position courante du flux, pas seulement sa position initiale. | Flux `x,a,b`, consommation préalable de `x`, puis fenêtre 2. | Après fermeture, la lecture du flux source doit redonner `a`. Sans `mark(n)` ou avec la condition constructeur mutée, le flux revient à `x` ou échoue. |
| `shortReadsFillIncrementallyAndRestoreAtEndOfStream` | Exercer plusieurs remplissages courts et la fermeture automatique à la fin. | `ByteArrayInputStream` sur `a,b,c` qui ne livre qu'un octet par lecture de tableau ; fenêtre 4. | Les lectures doivent retourner `a`, `b`, `c`, puis `-1`, et le flux source doit revenir à `a`. Cela rend la restauration après EOF observable. |
| `embeddedFilenameUsesInternalPathWhenResourceNameIsMissing` | Vérifier le premier repli de `getEmbeddedName`. | Métadonnées avec seulement `INTERNAL_PATH=folder/internal.txt`. | Le nom nettoyé doit être `internal.txt`. L'absence volontaire de `RESOURCE_NAME_KEY` force la branche de repli. |
| `embeddedPathUsesInternalPathWhenResourcePathIsMissing` | Vérifier le premier repli de `getEmbeddedPath`. | Même métadonnée `INTERNAL_PATH=folder/internal.txt`. | Le chemin nettoyé doit être `folder/internal.txt`, ce qui distingue le chemin complet du seul nom. |
| `filenameAtMaximumLengthIsNotTruncated` | Tester la borne stricte de longueur. | Nom `abc.txt` de longueur exacte 7 et `maxLength=7`. | Le résultat reste `abc.txt`. Une comparaison `>=` au lieu de `>` produirait une troncature invalide. |
| `calculateExtensionUsesBinForAnUnknownMimeType` | Vérifier la valeur de repli après une recherche MIME sans extension connue. | Type inventé `application/x-tika-unknown-type` et défaut `.fallback`. | Le contrat retourne `.bin`, pas le défaut ni une chaîne vide. Ceci atteint aussi `lookupExtension`. |
| `resolveWithinRejectsExistingSymbolicLinkOutsideDirectory` | Vérifier la défense contre la traversée d'un lien symbolique existant. | Répertoire temporaire contenant `inside/link` pointant vers un répertoire frère `outside`. | `resolveWithin(inside, "link")` doit lever `IOException` car les chemins réels sortent de `inside`. Le test exerce le bloc que les simples objets `Path` ne peuvent pas atteindre. |

### Résultats C

La construction complète réussit avec **761 tests, 0 échec, 0 erreur et 2 tests désactivés**. Les neuf cas manuels sont exécutés par Surefire, puis PIT est relancé avec les paramètres inchangés.

| Classe | JaCoCo B : lignes / branches | JaCoCo C : lignes / branches | PIT B | PIT C |
|---|---:|---:|---:|---:|
| `LookaheadInputStream` | 33 / 40 ; 14 / 16 | 40 / 40 ; 16 / 16 | 20 / 33 — 60,61 % | 31 / 33 — 93,94 % |
| `FilenameUtils` | 154 / 175 ; 86 / 110 | 163 / 175 ; 93 / 110 | 70 / 115 — 60,87 % | 80 / 115 — 69,57 % |
| **Total** | — | — | **90 / 148 — 60,81 %** | **111 / 148 — 75,00 %** |

Le diff [B/C](evidence/tache2/C/mutation-delta.json) contient **21 mutants nouvellement tués**. Les deux tests de lecture par tableau tuent les huit mutants jusque-là non couverts de cette surcharge ; le cas après lecture unitaire tue le dernier de ces huit. Les tests de restauration du flux tuent trois mutants de `LookaheadInputStream`, et les tests de repli de métadonnées, de longueur, de MIME inconnu et de lien symbolique tuent dix mutants de `FilenameUtils`, dont plusieurs survivants de B.

### Mutants restants

L'état C laisse **27 `SURVIVED`** et **10 `NO_COVERAGE`**. Les deux survivants de `LookaheadInputStream` sont dans `fill` : la borne `available() == 0` et la longueur demandée lors d'un remplissage. Les tests manuels montrent déjà le scénario de lectures courtes, mais PIT ne distingue pas ces deux modifications avec ce flux ; ils restent à analyser comme potentiellement équivalents ou à cibler par un flux artificiel plus contraignant.

Les 35 mutants restants de `FilenameUtils` sont concentrés dans les replis plus profonds de `getEmbeddedName` et `getEmbeddedPath`, les cas limites de nettoyage de noms et chemins, et certaines bornes. Ils ne sont pas déclarés équivalents : le rapport C les conserve explicitement pour une analyse ultérieure. Le choix de s'arrêter ici privilégie des tests dont l'oracle découle directement du contrat, plutôt que des assertions spéculatives destinées seulement à augmenter le score.

### Preuves et reproduction

- [Commandes et configuration de C](evidence/tache2/C/commands.md).
- [Rapport PIT C](evidence/tache2/C/pit-reports/index.html), [résumé](evidence/tache2/C/mutation-summary.json) et [diff B/C](evidence/tache2/C/mutation-delta.json).
- [Rapport JaCoCo C](evidence/tache2/C/jacoco/index.html) et [résultats Surefire](evidence/tache2/C/surefire-reports/).

La prochaine étape est **l'étape 10 : vérifier l'exécution dans GitHub Actions**, puis consolider le rapport unique de remise à l'étape 11.

## Préparation de l'étape 10 — workflow GitHub Actions

**Préparation locale réalisée le 3 octobre 2026 ; exécution GitHub non réalisée.**
Le workflow [.github/workflows/tache2-tests.yml](.github/workflows/tache2-tests.yml)
se déclenche sur les pushes vers `main` et `tache2-tests`, sur les PR ciblant ces
branches et manuellement. Il utilise Ubuntu, Java Temurin 17 et le wrapper Maven :

```bash
./mvnw -B -pl tika-core -am clean verify
```

La commande conserve les contrôles Maven configurés ; aucun `-Pfast`,
`-DskipTests` ou `continue-on-error` n'est ajouté. Le journal est conservé avec
`tee` et `pipefail`, afin qu'un échec Maven fasse bien échouer le job.
Le workflow exécute les tests acceptés sans lancer ChatUniTest, Ollama ou PIT.

Le script [.github/scripts/check-tache2-tests.py](.github/scripts/check-tache2-tests.py)
examine les quatre XML Surefire des nouveaux tests. Il exige les noms exacts,
une exécution unique de chaque cas, les compteurs attendus (1, 2, 4 et 5) et
aucun échec, erreur ou test désactivé. JUnit inclut `(Path)` dans le nom du cas
utilisant `@TempDir` ; cette signature est prise en compte. Le résultat apparaît
dans le résumé du job. La vérification et l'archivage sont exécutés même si Maven
échoue ; ils ne masquent pas son échec.

Les artefacts contiennent le journal, les rapports Surefire de `tika-core` et du
processeur d'annotations, le rapport JaCoCo et le résultat Checkstyle disponible.
Leur nom contient le SHA du commit et leur conservation est fixée à 30 jours.
Archiver les preuves utiles avant leur expiration.

### Validation locale et travail restant

Le YAML a été analysé localement, et le vérificateur a accepté les douze cas des
rapports C archivés. Des copies temporaires altérées ont confirmé le rejet d'un
rapport absent, d'un cas désactivé, échoué, manquant ou dupliqué. Cette validation
ne constitue pas une exécution de GitHub Actions et ne remplace pas un build Linux.

Commande de contrôle sans relancer les tests :

```bash
python3 .github/scripts/check-tache2-tests.py evidence/tache2/C/surefire-reports
```

Après autorisation de publication, versionner les nouveaux tests, le POM, le
workflow et son script, puis pousser vers le fork. Vérifier l'exécution dans
l'onglet Actions sur le commit final, télécharger les rapports et ajouter le lien
permanent au rapport de remise. Le déclenchement manuel depuis l'interface GitHub
nécessite que le workflow soit aussi présent sur la branche par défaut ; le push
vers `tache2-tests` fournit le déclenchement prévu pour la branche de travail.
Aucun commit, push ou changement du dépôt du cours n'a été réalisé ici.
