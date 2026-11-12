# Guide de reprise — Apache Tika, tâche 3 IFT3913

Ce fichier s'applique à ce worktree et à ses sous-dossiers. Il permet à une nouvelle session Codex de reprendre le travail sans confondre préparation, intentions et résultats. Les instructions explicites de l'utilisateur priment.

Dernière mise à jour : **12 novembre 2026**, heure de Montréal.

## 1. Règles de travail et autorisations

- Communiquer en français, simplement, en séparant les faits vérifiés, les hypothèses et le travail restant.
- Travailler une étape à la fois. Ne pas lancer toute la tâche 3 lorsqu'une seule étape est autorisée.
- Avant toute modification, vérifier la branche et `git status --short`. Sur le disque externe, cette commande peut être lente : utiliser un périmètre ciblé si nécessaire et ne pas annoncer un état propre sans résultat.
- Préserver les modifications existantes et le travail de tâche 2. Ne pas effectuer de nettoyage destructif.
- Rester en local, sans commit, push, PR ni publication tant que l'utilisateur ne les demande pas. Les futures validations GitHub nécessiteront une instruction correspondante.
- Ne pas utiliser de sous-agents sans demande explicite.
- Les sept modifications locales sont présentes, V6 conservée comme limite non bloquante. L'utilisateur a ensuite choisi le rickroll animé façon terminal rétro et autorisé sa réalisation locale (étape 7, section 16). Les tests contrôlés utilisent une copie de référence, sans modifier les sources Java du worktree. Aucun commit, push ou publication autorisé. Ne pas envoyer les injections au projet Apache.
- Chercher avec `rg` / `rg --files`, en excluant les fichiers macOS avec `-g '!**/._*'`.
- Utiliser les mécanismes d'autorisation de l'environnement lorsque nécessaires. Le dossier de ce worktree peut être en dehors des racines d'écriture d'une session lancée depuis l'ancien dossier.
- **Après chaque étape importante, actualiser ce fichier** : changements effectués, décisions et raisons, validations et preuves, limites, reste à faire et prochaine étape. Ne pas transformer une intention en résultat acquis.

## 2. Dépôt, branche et binôme

| Élément | Valeur |
|---|---|
| Fork conservé | https://github.com/EnzoT454/tika |
| Branche de tâche 3 | `tache3` |
| Worktree de tâche 3 | `/Volumes/TOSHIBA_EXT/ift3913/tika-tache3` |
| Base de départ | `62ff2e9b180817f4b38108c4e90ffa05348c5a4d` |
| Message du commit | `Update contributor name in readme.md` |
| Ancien worktree préservé | `/Volumes/TOSHIBA_EXT/ift3913/tika`, branche `tache2-final` |
| Binôme | Aqel Hamza — `EnzoT454` ; Saidana Mohamed — `FakeTMG` |

Le worktree `tache3` est créé depuis le commit identifié, sans copier les modifications locales non commitées de `tache2-final`. Les worktrees partagent les objets et références Git, mais ont des fichiers et index distincts. Aucun nouveau fork n'est nécessaire selon les consignes consultées.

La création du worktree a terminé avec succès : 9 682 fichiers déployés et code de sortie Git 0. La branche et le dossier ont été ouverts par l'utilisateur.

## 3. Comprendre Tika et le périmètre

Tika est un projet Java/Maven qui détecte les formats des documents et extrait texte et métadonnées. `tika-core` fournit les interfaces et utilitaires communs ; `tika-parsers` contient les analyseurs spécialisés. Le `pom.xml` racine agrège les modules ; `tika-parent/pom.xml` centralise versions et contrôles.

Les modules autorisés par la page du cours sont `tika-core`, `tika-serialization`, `tika-parser-ocr-module`, `tika-parser-pdf-module` et `tika-parser-microsoft-module`. Commencer par `tika-core` ; élargir si nécessaire pour des vulnérabilités pertinentes. La limite de trois classes de tâche 2 n'est pas explicitement répétée pour tâche 3.

Les résultats historiques de tâche 2 ne prouvent pas une validation de tâche 3. Le `readme.md` hérité décrit encore la tâche 2 ; préserver ce travail et ne pas présenter son contenu comme un rapport tâche 3.

## 4. Demande académique et barème

Objectif : adapter GitHub Actions afin de bloquer une version introduisant une nouvelle vulnérabilité détectée par Semgrep, puis le démontrer avec sept injections de types distincts dans Tika.

| Critère | Poids | Attente |
|---|---:|---|
| Fonctionnement GA | 20 % | Échec du contrôle/build lorsqu'une nouvelle vulnérabilité est détectée. |
| Documentation GA | 20 % | Conception, implémentation et validation expliquées et justifiées. |
| Vulnérabilités | 30 % | Sept vulnérabilités de types différents introduites dans le code. |
| Documentation des vulnérabilités | 20 % | Fichier choisi, type, mécanisme vulnérable et résultat Semgrep justifiés pour chacune. |
| Humour | 10 % | Action réutilisable de rickroll invoquée quand un test échoue. |

Comparer les rapports avant/après afin d'identifier les nouveautés. Sept alertes ne signifient pas nécessairement sept types. Une alerte statique seule ne prouve pas le caractère exploitable : expliquer entrée contrôlable, opération sensible, protection absente et conditions.

**Énoncé corrigé fourni le 12 novembre : le rickroll vise explicitement le cas d'étude, donc Tika.** Cela confirme la clarification antérieure de l'utilisateur et le plan retenu ; aucune étape technique n'est ajoutée. Rust reste facultatif. Le guide `tache3.md` a été synchronisé.

## 5. Décisions prises

1. Conserver le fork actuel et préserver `tache2-final` ; isoler la tâche 3 sur sa branche et son worktree.
2. Utiliser une copie de construction sur disque interne, excluant les AppleDouble `._*`. L'utilisateur a explicitement approuvé ce choix.
3. Semgrep CE 1.185.0 et 82 règles Java générales de sécurité figés ; révision upstream `a5ad1714f00c9f7fde980cab0ea1d6dc6d414fd2`. Périmètre : Java de production des cinq modules autorisés. Voir la section Semgrep.
4. Distinguer la référence initiale figée du rapport et la référence utilisée par chaque événement CI. Comparer les résultats, pas uniquement leurs comptes ; tester les déplacements de lignes et les pushs de plusieurs commits.
5. Conserver les alertes préexistantes dans le rapport initial sans les attribuer aux injections. Une erreur technique d'analyse ne doit pas être annoncée comme une détection réussie.
6. Préserver le statut d'échec tout en conservant les rapports. Ne pas masquer les échecs avec `continue-on-error` ou des scripts qui perdent le code de sortie.
7. Invoquer le rickroll après un échec de test de Tika, sans masquer celui-ci. Distinguer cet événement d'un échec Semgrep, de compilation ou de téléchargement.
8. Prévoir un rapport propre à tâche 3 avec liens vers commits/runs précis. Le guide de travail et la petite fiche de remise du cours ne remplacent pas ce rapport.

## 6. État vérifié de la copie de construction

Copie de référence préparée : **`/private/tmp/tika-final-build-yy2wo_ao`**.

- Script existant utilisé : `tache2-rapports/final-integration/etape-04/prepare-local-build.py` depuis le worktree tâche 3.
- Il copie les fichiers suivis dans leur état courant et les non suivis non ignorés, avec un filtre de construction : fichiers racine, `.mvn`, `tika-core`, `tika-parent`, `tika-annotation-processor` et POM de découverte du réacteur.
- 658 fichiers dans `source-manifest.json`, dont `tache3.md`.
- Empreintes SHA-256 vérifiées ; aucun fichier `._*` ; wrapper `mvnw` exécutable ; POM requis présents.
- Résumé : `/private/tmp/tika-final-build-yy2wo_ao/preparation-summary.json`.
- Une préparation ciblée intermédiaire existe aussi dans `/private/tmp/tika-final-build-eb5kj0lg`. Les deux manifestes ont été comparés et sont identiques. Utiliser `yy2wo_ao` comme référence pour la prochaine construction.
- L'inventaire global sur disque externe étant lent, un adaptateur temporaire `/private/tmp/prepare-tache3-build.py` a aussi été essayé. Il conserve tous les fichiers suivis et limite la recherche des non suivis aux modules de construction et à `tache3.md`. Il n'est pas nécessaire à l'utilisation de la copie finale.
- **Construction initiale et tests validés le 10 novembre 2026** : voir la section suivante et `tache3-rapports/environnement/`. Le scan initial Semgrep est maintenant validé (section Semgrep) ; aucune mesure PIT de tâche 3 exécutée.

Cette copie n'est pas un clone Git complet ni une copie de tous les modules : elle sert à construire `tika-core`. Elle n'inclut pas automatiquement les scripts CI sous `.github/`, et ne doit pas être utilisée comme périmètre prétendument complet d'un scan Semgrep de tout Tika.

Les dossiers sous `/private/tmp` peuvent disparaître. Vérifier leur présence à la reprise. Les manifestes décrivent l'instant de préparation ; ce nouvel `AGENTS.md` et toute modification ultérieure n'y sont pas automatiquement inclus. Rafraîchir la copie si les sources/configurations nécessaires changent. Archiver les preuves utiles dans `tache3-rapports/` avant nettoyage.

## 7. Prochaine étape et travail restant

**Les étapes 6 (comparaison) et 7 (rickroll rétro) sont terminées localement**, avec réserves conservées. Comparaison : `tache3-rapports/comparaisons/etape-06-20261112/`, 17 → 24 alertes, 8 ajouts, 1 suppression, 16 conservés. Rickroll : `tache3-rapports/rickroll/etape-07-20261112/`, trois cas Maven réels et neuf cas de détection validés. L'action n'a pas encore été exécutée sur un runner GitHub. V6 reste une détection statique avec observation comportementale incomplète. Les sept modifications restent non commitées sur la base `860a2631d9e78311387207ed9380275b2a151781`. **Prochaine étape après instruction : validation GitHub (étape 8), puis rapport final.** Aucun commit/push/publication autorisé actuellement.

Validation initiale du 10 novembre 2026 :

- Java Homebrew 17.0.18 ; wrapper Maven 3.9.12 ; Tika 4.0.0-SNAPSHOT.
- `./mvnw -B -ntp -pl tika-core -am clean install` : `BUILD SUCCESS`, code 0, durée Maven 27,871 s, fin à 12 h 07 min 26 s EST.
- Core : 966 cas, 964 réussis, zéro échec/erreur, deux désactivations préexistantes (`TikaInputStreamTest.reproduceRandomizedTestFailure`, `CustomErrorHandlerTest.testUndeclaredEntityXML`).
- Processeur d'annotations : 8/8 réussis. Checkstyle : zéro violation dans parent, processeur et core. Aucun contrôle désactivé.
- Les 658 empreintes du manifeste sont inchangées après construction, notamment après Spotless.
- La première tentative a été bloquée par le sandbox lors de l'installation du POM parent dans `~/.m2`, avant les tests core. Relance autorisée avec accès au cache ; les deux journaux sont conservés, sans confondre cet échec technique et une erreur des sources.
- Preuves durables : `tache3-rapports/environnement/` : `results.json`, `reference.json`, versions Java/Maven, journaux des deux tentatives, `commands.md`, manifestes et empreintes. `sources.tar.gz` contient les 658 fichiers exacts de construction ; `reports.tar.gz` contient Surefire, Checkstyle et JaCoCo bruts.
- Cette validation de construction porte sur tika-core et ses dépendances seulement. Le scan Semgrep, réalisé ensuite, est décrit ci-dessous. Aucun run GitHub ; le `readme.md` hérité reste inchangé.
- `AGENTS.md` est ignoré par `.gitignore` (ligne 32 au moment de la vérification). Il assure la reprise locale mais ne sera pas suivi par un `git add` ordinaire ; décider explicitement de son suivi si une publication de ce guide est demandée.


Commande exécutée depuis la copie :

```bash
java -version
./mvnw -version
./mvnw -B -ntp -pl tika-core -am clean install
```

Conserver le journal complet et examiner Surefire/Checkstyle. Ne pas contourner les contrôles avec `-Pfast`, `-DskipTests` ou des désactivations. Spotless peut appliquer des modifications ; vérifier les sources après construction. Relever les nombres réellement observés au lieu de recopier les chiffres historiques de tâche 2.

Étapes restantes :

1. Terminé : environnement, référence et validation initiale archivés dans `tache3-rapports/environnement/`.
2. Terminé : Semgrep CE 1.185.0, 82 règles et périmètre figés ; 682 fichiers analysés.
3. Terminé : rapport avant injections archivé dans `tache3-rapports/semgrep-avant/`.
4. Terminé : workflow et contrôle différentiel, 14 scénarios locaux puis démonstration GitHub positive/négative/correction ; preuves dans `tache3-rapports/validation-ci/` et `github/`.
5. Sept modifications et fiches présentes : scans et sept builds validés. Réserve comportementale V6 maintenue et acceptée par l'utilisateur pour poursuivre ; ne pas annoncer de ReDoS démontré.
6. Terminé : rapports avant/après réutilisés et archivés, comparaison exacte des identités, association des huit ajouts aux sept V, matrice avec réussites/limites/non-exécutions et empreintes. Voir section 15.
7. Terminé localement : action composite de rickroll rétro, déclenchement Surefire ciblé et échec Maven préservé. Trois cas réels et neuf cas du détecteur validés ; invocation GitHub via `uses` encore à confirmer à distance.
8. Terminé pour la démonstration initiale : runs GitHub positifs/négatifs et correction archivés. Les runs des sept injections et du rickroll restent à obtenir dans leurs étapes respectives.
9. Consolider le rapport final, répartition du travail et déclaration d'assistance IA.
10. Préparer et effectuer la remise uniquement après instruction.

Les sept familles initialement proposées ont été remplacées/confirmées par les types réellement documentés : commande shell (CWE-78), XXE (611), désérialisation (502), nom d'hôte TLS (295), AES/ECB (327), motif contrôlé/ReDoS potentiel (1333), permissions POSIX excessives (276). Les sept couples fichier/règle et leurs résultats figurent dans la consolidation. V6 reste une validation comportementale incomplète ; les 82 règles initiales sont conservées.

## État Semgrep — scan initial terminé le 10 novembre 2026

- Environnement Python isolé : `/private/tmp/tache3-semgrep-venv`, Python 3.11.15, Semgrep Community Edition **1.185.0**, moteur OSS, sans compte ni Pro.
- Configuration durable : `semgrep/requirements.txt`, `scope.json`, `rules.lock.json`, `scan.py` et `README.md`. Le script vérifie version, empreintes et complétude du scan.
- **82 règles Java générales**, sous `java/lang/security` du dépôt officiel, révision `a5ad1714f00c9f7fde980cab0ea1d6dc6d414fd2`. Règles d'audit incluses, identifiants uniques, réécriture d'identifiants désactivée. Les règles propres aux autres langages/frameworks ne sont pas retenues.
- La licence upstream est Semgrep Rules License v1.0, pas Apache/MIT. Ne pas publier leurs contenus : le dépôt conserve uniquement scripts, révision, inventaire et empreintes ; le script télécharge les règles dans un cache temporaire et conserve leur notice. Les règles téléchargées restent hors dépôt.
- **682 fichiers Java de production** : core, serialization, OCR, PDF et Microsoft. Les répertoires exacts sont dans `semgrep/scope.json`. Tests, archives, ressources de configuration et sorties générées hors périmètre ; `._*` exclus explicitement. Ce scan n'est pas une analyse de tout Tika ni de ses dépendances.
- Copie source de scan : `/private/tmp/tache3-semgrep-before`, distincte de la copie de construction. Les sources Java sont identiques aux fichiers courants ; diff Git des cinq répertoires de production vide, manifeste archivé, sources inchangées après scan.
- Résultat : **17 alertes préexistantes**, 8 ERROR et 9 WARNING, zéro erreur et zéro règle ignorée ; 682/682 fichiers analysés. Sept `unsafe-reflection`, sept `command-injection-process-builder`, une SAX/DOCTYPE, une StAX/XXE et une socket non chiffrée.
- Ce sont des signalements, pas 17 vulnérabilités confirmées. Première lecture dans `review.md` : ProcessBuilder avec arguments ne prouve pas une injection shell ; StAX a des protections via une méthode auxiliaire non reconnue par la règle. Les 17 résultats restent dans la référence sans suppression.
- Options : strict, métriques et vérification distante de version désactivées, suppressions nosemgrep désactivées, indépendance de `.gitignore`, deux processus, timeout 30 s par règle/fichier, taille de fichier non limitée. Le script refuse erreurs/règles ignorées/fichiers manquants.
- Reproduction depuis un cache vide validée : mêmes 17 identités. L'aide CLI et le moteur ont été vérifiés ; les règles sont téléchargées à SHA figé et vérifiées par SHA-256.
- Particularités : installation réseau autorisée après blocage du sandbox ; magasin CA certifi explicite sans désactiver TLS ; scan relancé avec autorisation après refus d'écriture dans `~/.semgrep/semgrep.log`. Les traces utiles sont archivées.
- Preuves durables : `tache3-rapports/semgrep-avant/` : rapport brut `semgrep.json`, `results.json`, `findings.json`, `review.md`, `commands.md`, logs, versions/dépendances, manifeste, archives des sources et de la configuration, empreintes. Aucun contenu des règles upstream dans ces archives.
- **Lors de ce scan initial, le contrôle différentiel n'était pas encore implémenté.** Le scan complet termine avec code 0 malgré les 17 résultats affichés « blocking ». Le contrôle bloquant est maintenant assuré séparément par `semgrep/gate.py` (validation du 12 novembre). Aucun workflow modifié/exécuté, aucune injection, aucun commit ni push pendant l'étape du scan initial.

Commande depuis le worktree complet, avec l'environnement installé :

```bash
/private/tmp/tache3-semgrep-venv/bin/python semgrep/scan.py --output /private/tmp/semgrep-result.json
```

Les outils et caches de `/private/tmp` peuvent disparaître : les recréer selon `semgrep/README.md`. Conserver le moteur, les règles et le périmètre pour la comparaison après injections. Une future extension justifiée de ce périmètre impose de refaire une référence comparable, pas de mélanger les résultats.

## 8. Validation et documentation attendues

La matrice doit couvrir : même version/référence, changement bénin, nouvelle vulnérabilité, sept types présents, correction, déplacement de lignes, suppression+ajout à compte identique, push de plusieurs commits, référence absente/configuration invalide, tests réussis, test volontairement échoué et erreur de compilation.

Pour chaque vulnérabilité, conserver : fichier/méthode/commit, rôle initial, modification, entrée et effet dangereux, justification, identifiant de règle et CWE si pertinent, résultats avant/après, preuve du blocage et limites. Une correction éventuelle est une expérience distincte ; ne pas écraser les preuves de la version volontairement vulnérable.

Archiver les JSON bruts, règles, versions, commandes, SHA, journaux, rapports de tests et liens de runs. Les captures seules sont insuffisantes ; les artefacts distants peuvent expirer. Le statut rouge attendu doit être attribué à Semgrep et non à une panne étrangère.

## Consignes de rédaction du rapport — décision utilisateur

- **Le rapport soumis pour la tâche 3 sera `readme.md` à la racine de cette branche.** `tache3.md` est le plan détaillé et `AGENTS.md` le guide de reprise ; ils ne remplacent pas le rapport évalué.
- Faire nettement plus court que le rapport de tâche 2 : phrases simples, vocabulaire naturel, ton de binôme et informations concrètes. Éviter les formulations mécaniques, le remplissage et les répétitions.
- Privilégier de courts paragraphes qui relient le choix, sa raison et le résultat observé. Expliquer le raisonnement nécessaire pour justifier les réponses, sans développer un cours théorique.
- Éviter les longues tables et les inventaires dans le rapport. Utiliser un petit tableau seulement s'il facilite réellement une comparaison.
- Pour chaque vulnérabilité, conserver les éléments essentiels : fichier et raison du choix, mécanisme vulnérable, règle/résultat Semgrep et preuve de nouveauté. Relier les détails techniques et les journaux aux archives plutôt que les recopier.
- Documenter le fonctionnement et la validation du workflow ainsi que le rickroll avec la même concision. Rester précis sur les références, les résultats et les limites utiles.
- Un style naturel ne dispense pas de la déclaration d'assistance IA prévue par les consignes du cours ; la garder courte et factuelle. Ne pas inventer de résultats, d'actions humaines ou de provenance.
- Le rapport hérité de tâche 2 n'est pas réécrit à ce stade. Lors de la consolidation autorisée, préserver son historique et rédiger le rapport tâche 3 à partir des résultats réellement acquis.

## 9. Sources et remise

- Guide détaillé local : [tache3.md](tache3.md).
- [Consignes officielles tâche 3](https://github.com/umontreal-diro/IFT3913/blob/2026/tache3/readme.md).
- [Calendrier et cas d'étude](https://github.com/umontreal-diro/IFT3913/blob/2026/README.md).
- [Modèle de fiche de remise](https://github.com/umontreal-diro/IFT3913/blob/2026/.github/PULL_REQUEST_TEMPLATE/tache3-readme.md).
- [Référence CLI Semgrep](https://semgrep.dev/docs/cli-reference).

Échéance relevée lors de la consultation du 10 novembre : **17 novembre 2026, 17 h EST, Montréal**. Revalider avant remise. La PR du dépôt du cours contient un dossier `tache3/NOM1_NOM2/` avec les noms/identifiants du binôme et les liens du fork et du rapport. Elle ne contient pas à elle seule toute la documentation technique.

## 10. Journal de session — 10 novembre 2026

- Lecture du contexte de tâche 2 et du rapport final ; consultation des consignes officielles tâche 3 et explication du barème.
- Décision de conserver le fork, création de `tache3` depuis le SHA indiqué et worktree séparé ; copie Git terminée avec succès.
- Création de `tache3.md`, guide détaillé des étapes, critères, validations et preuves. Aucun travail d'injection commencé.
- Explications théoriques fournies à l'utilisateur : analyse statique, familles de vulnérabilités, CI, références Git, validation et rapport.
- Confirmation utilisateur : GraphHopper désigne Tika pour le rickroll.
- Approbation puis préparation de la copie interne ; 658 fichiers vérifiés, sans AppleDouble ; aucune construction encore exécutée.
- Création de ce guide à la demande de l'utilisateur pour assurer la continuité entre sessions. À tenir à jour après chaque étape importante.

- Consigne utilisateur ajoutée : rapport soumis dans `readme.md`, plus court que tâche 2, précis, naturel, centré sur les justifications et sans longues tables.

- Étape de validation initiale réalisée sur instruction : construction complète réussie, 964 cas core et 8 processeur réussis, deux désactivations initiales, zéro violation Checkstyle ; sources inchangées. Preuves archivées dans `tache3-rapports/environnement/`. Prochaine étape : Semgrep et rapport avant, après instruction.

- Étape Semgrep réalisée sur instruction : CE 1.185.0, 82 règles générales verrouillées, 682 sources Java des cinq modules, 17 alertes préexistantes, aucune erreur ni règle ignorée. Reproduction depuis cache vide réussie. Preuves dans `tache3-rapports/semgrep-avant/` ; guides synchronisés. Prochaine étape : workflow différentiel après instruction.

## 11. Journal de session — 12 novembre 2026

- Lecture des guides et explication de l'état du dépôt. L'énoncé corrigé fourni par l'utilisateur remplace GraphHopper par le cas d'étude : aucun changement au plan technique. Rapport final confirmé dans `readme.md` ; ancienne formulation indécise de `tache3.md` corrigée.
- Sur instruction « réalise-la », implémentation de l'étape de contrôle différentiel uniquement. Branche réelle `tache3`, HEAD inchangé `62ff2e9b180817f4b38108c4e90ffa05348c5a4d`. État ciblé avant modification : `semgrep/`, `tache3-rapports/`, `tache3.md` déjà non suivis ; travail existant préservé.
- Nouveau `semgrep/gate.py` : extrait deux instantanés Git de sources Java et utilise deux scans complets identiques, sans exécuter les scripts du commit de référence. `scan.py` accepte `--source-root`. Configuration du contrôleur courant appliquée aux deux états, même si le commit de référence ne contient pas Semgrep.
- Références : push `before` → `after` (pas `HEAD^`) ; PR ancêtre commun de base et tête → tête ; manuel SHA complet obligatoire. Premier push avec SHA nul ou objet introuvable : erreur technique explicite. Historique complet demandé au checkout.
- Identité locale : règle + chemin + SHA-256 du fragment signalé, avec multiplicité et sans numéro de ligne. Le JSON CE indique `requires login` pour l'empreinte native. Limites : renommage/reformulation possiblement nouveau ; remplacement identique dans un même fichier à nombre constant considéré conservé ; aucun suivi sémantique du contexte hors fragment. Voir `semgrep/README.md`.
- Codes du contrôleur : 0 sans nouveauté, 1 avec nouveautés, 2 en cas d'erreur technique. Répertoire de sortie neuf requis. JSON avant/après, diff, résumés, manifestes et journaux conservés. Une erreur d'analyse n'est jamais attribuée à une détection réussie.
- Nouveau `.github/workflows/tache3-ift3913.yml` : adaptation du build core de tâche 2 avec job sécurité préalable ; workflows historiques préservés. Push/PR sur main et tache3, déclenchement manuel, permissions lecture seule, conservation des artefacts avec `always()`, aucun `continue-on-error`, construction dépendante de sécurité et `pipefail` préservé. Aucun rickroll à ce stade.
- Validation réelle avec `semgrep/validate_gate.py`, sans mocks : **14 scénarios réussis**, CE 1.185.0 et 82 règles. Même code, bénin, décalage de lignes, nouveauté (17 → 18), occurrence identique supplémentaire (18 → 19), suppression+ajout (17 → 17 mais échec), correction (18 → 17), push multi-commits, PR divergente, référence absente, premier push, manuel, configuration invalide et source malformée. Les derniers cas donnent une erreur technique 2.
- Tous les commits et exemples Java de validation ont été créés dans un dépôt **temporaire**, jamais dans ce worktree. Aucune commande dangereuse du Java de test exécutée. Le bundle de ce dépôt est archivé pour retrouver ses SHA. Il ne constitue pas les sept injections définitives.
- Les **682 empreintes** sont identiques au manifeste du scan initial et aux fichiers courants. YAML lu avec ruamel.yaml, structure et dépendances vérifiées ; syntaxe Python vérifiée. Pas de nouvelle construction Maven puisque les sources réelles sont inchangées. Aucun actionlint ni run GitHub exécuté.
- Première tentative de validation interrompue avant scan au démarrage du contrôle de version. Désactivation explicite de la vérification distante pour `--version`, ajout de délais maximums ; relance complète réussie. Les traces de la tentative interrompue restent séparées dans les archives. Le cache existant de règles a été vérifié, sans téléchargement supplémentaire.
- Limite d'inventaire : le diff global rencontre une ressource `droste.zip` illisible dans les tests du module pkg ; ne pas annoncer le dépôt globalement propre. Les contrôles ciblés réussissent. Ne pas modifier cette ressource au titre de cette étape.
- Preuves : `tache3-rapports/validation-ci/commands.md`, `results.json`, `verification.json`, manifestes et empreintes ; `runs.tar.gz` contient rapports bruts, journaux, événements synthétiques et `fixtures.bundle` ; `configuration.tar.gz` conserve scripts et workflow exacts, sans contenus des règles upstream. Dossier temporaire de travail : `/private/tmp/tache3-gate-validation-final` (non durable).
- **Reste à faire** : sept injections et fiches après instruction ; comparaison académique après ; rickroll et validation tests/compilation ; runs GitHub après autorisation ; rapport final concis dans `readme.md`, remise après instruction. La validation locale ne prouve pas le téléchargement des actions, l'installation Ubuntu, le téléversement des artefacts ni le statut distant. Aucun commit, push, PR ou publication dans le dépôt réel pendant cette étape.

## 12. Validation GitHub et archivage — 12 novembre 2026

- L'utilisateur a ensuite publié la préparation (`20a91e9686a67dc248839fd078c531b4b71358c7`), le changement bénin (`b6f2723a3cdafdec790872041cd2285c36443ff8`) et une branche `validation-ci-tache3` dans son fork. Il a créé la PR nº 1 vers `tache3`, ajouté l'exemple temporaire (`96318db1eada7b025fefeb1be2b8d9a59565e345`), puis l'a retiré (`e8384e424ad8cc8fe4b43a78464fa71b056c0bdb`). Ces actions utilisateur sont postérieures à l'étape locale décrite en section 11.
- Sur instruction d'archiver les preuves avant les injections : récupération via GitHub CLI des métadonnées des **5 runs**, de leurs journaux complets et des **8 artefacts ZIP**, puis extraction et vérification d'intégrité. SHA-256 GitHub comparé lorsqu'il est fourni ; CRC des ZIP vérifié. Aucun nouveau run lancé.
- Runs vérifiés : `43251450652` premier push, erreur technique 2 pour référence nulle et construction ignorée ; `43251595954` manuel, succès 17 → 17 ; `43252710325` push bénin, succès 17 → 17 ; `43253590194` PR avec exemple, code 1 et 17 → 18, construction ignorée ; `43254989974` PR corrigée, succès 17 → 17 par rapport à la base PR.
- L'alerte temporaire est `command-injection-process-builder`, `GateValidation.java:4`. Elle ne compte pas parmi les sept injections définitives. Les patchs d'ajout et de suppression sont archivés. Les configurations utilisées par les scans correspondent aux fichiers courants ; les 682 SHA-256 Java courants correspondent au manifeste initial. Le fichier temporaire est absent.
- Les XML Surefire des trois builds réussis confirment chacun 966 cas core (964 réussis, 2 désactivations préexistantes) et 8 tests processeur réussis ; zéro échec/erreur. Les journaux et artefacts confirment le téléchargement des actions, l'installation Ubuntu et le téléversement effectif des rapports pour ces runs.
- Après vérification des preuves : PR https://github.com/EnzoT454/tika/pull/1 **fermée sans fusion**, état CLOSED et `mergedAt: null` vérifiés. Branches de démonstration conservées. Retour du worktree sur `tache3`, HEAD `b6f2723a3cdafdec790872041cd2285c36443ff8`, sans suppression de branches ni nettoyage de `.git`.
- Autorisations de l'environnement utilisées pour accès réseau GitHub et écriture de l'index partagé lors du changement de branche. Les messages `non-monotonic index` sur un AppleDouble `._pack-…idx` restent présents et n'ont pas empêché ces opérations. Ne pas annoncer un état Git global propre ; les nouveaux guides/preuves restent locaux.
- Preuves durables : `tache3-rapports/validation-ci/github/README.md`, `runs.json`, `results.json`, `verification.json`, états PR avant/après, patchs, répertoires par run et `checksums.json`. Aucune règle upstream téléchargée n'a été ajoutée. Les preuves locales antérieures restent intactes.
- Guides actualisés : `AGENTS.md`, `tache3.md`, `semgrep/README.md`. Aucun commit ni push effectué par l'agent pour cet archivage ; aucune injection définitive ni modification de code de production. `readme.md` reste le rapport hérité, à consolider ultérieurement.
- Reste à faire après instruction : sept types et fiches, comparaison académique après, rickroll et scénarios de tests associés, runs correspondants, rapport final et remise. Les annotations de dépréciation des actions/runner sont archivées ; aucune migration n'est incluse dans cette étape.

## 13. Injections locales — travail en cours, 12 novembre 2026

- Étape autorisée par « réalise next step ». Base réelle au début : branche `tache3`, HEAD `860a2631d9e78311387207ed9380275b2a151781` ; statut ciblé sans modification listée avant injection. Ce commit est postérieur à l'archivage précédent, sans commit créé par l'agent pour cette étape.
- Copie isolée préparée dans `/private/tmp/tache3-injections` : 682 sources identiques au manifeste initial ; copie de construction de 658 fichiers. Règles, version et périmètre conservés.
- **V1 réalisée et validée** : `ExternalParser.parse` joint les arguments puis les transmet à `sh -c` sans échappement. Les chemins `${INPUT_FILE}` contrôlables deviennent une entrée shell. Règle `command-injection-process-builder` : une identité ajoutée et une ancienne supprimée, total inchangé à 17. Preuves dans `tache3-rapports/vulnerabilites/V1/` (JSON brut, diff incrémental/cumulé, patch, manifeste et archive source).
- V1 : build complet `./mvnw -B -ntp -pl tika-core -am clean install` réussi : 966 cas core, zéro échec/erreur, deux désactivations ; 8 processeur réussis ; zéro violation Checkstyle et aucune modification des sources par Spotless. Première tentative bloquée au cache Maven, relance autorisée réussie ; deux traces archivées séparément.
- **V2 réalisée et validée** : `XMLReaderUtils.getDocumentBuilder(ParseContext)` utilise une fabrique sans protections contre les entités externes lorsque le contexte n'en fournit pas. Deux nouvelles alertes DOM/XXE pour un seul type, total cumulatif 19. Build complet réussi avec mêmes comptes de tests et zéro violation Checkstyle. Un premier essai touchant le pool global a été interrompu pendant les tests XML, puis remplacé ; toutes ses traces sont conservées séparément dans `V2-attempt-1/`. Le pool global sécurisé est préservé dans la version retenue.
- **V3 réalisée et validée** : `ParseContext.setJsonConfig(String, String)` accepte maintenant `java-serialized:` suivi d'un objet Base64 et appelle `ObjectInputStream.readObject` avant conversion en `JsonConfig`, sans filtre local. Une nouvelle alerte `object-deserialization`, total 20 ; build complet réussi avec 964 core + 8 processeur réussis, deux désactivations et zéro violation. Une preuve comportementale contrôlée reste à compléter.
- **V4 réalisée et validée** : `TikaInputStream.get(URL, Metadata)` attache aux connexions HTTPS un HostnameVerifier retournant true. Une alerte `insecure-hostname-verifier`, total 21 ; build complet réussi (964 core + 8 processeur, deux désactivations, zéro violation). La chaîne de confiance TLS reste vérifiée ; la faiblesse concerne uniquement le nom d'hôte.
- **V5 réalisée et validée statiquement/build** : `CryptoParser.parse` ignore les modes demandés pour AES et emploie `Cipher.getInstance("AES")` (ECB par défaut SunJCE). Nouvelle alerte `use-of-default-aes`, total 22 ; build complet réussi avec comptes identiques, zéro violation. La preuve comportementale du downgrade reste à compléter.
- **V6 réalisée et validée statiquement/build** : `RegexCaptureParser.parse` accepte une première ligne `regex:` provenant du document, compilée sans restriction puis appliquée aux lignes suivantes. La configuration auparavant choisie par l'appelant peut ainsi être remplacée/complétée par les données du document. Alerte INFO `java-pattern-from-string-parameter`, total 23 ; build complet réussi (964 core + 8 processeur, deux désactivations, zéro violation).
- **V7 réalisée et validée statiquement/build** : `TemporaryResources.createTempFile(String)` attribue `rw-rw-rw-` aux fichiers temporaires POSIX. Nouvelle alerte `overly-permissive-file-permission`, total final **24 résultats : 8 identités ajoutées, 1 ancienne remplacée par V1, 16 conservées**. Les deux alertes XXE ne comptent que pour un type. Les 7 CWE sont 78, 611, 502, 295, 327, 1333 et 276.
- Les **7 états successifs** passent le build complet core + dépendances : 964 tests core et 8 processeur réussis, deux désactivations initiales, zéro violation Checkstyle et sources inchangées par Spotless. Chaque dossier V1 à V7 conserve un scan complet des 682 sources, comparaisons incrémentale/cumulative via `gate.compare`, patch, manifeste, archive source, journaux et rapports de build. Les sept fiches README sont rédigées, mais leur synthèse/validation finale reste à terminer.
- Vérifications comportementales contrôlées : anciennes traces dans `behavior/`, sept indicateurs false sur les classes initiales, V1/V2/V3/V4/V5/V7 true après injections et V6 false. **Le diagnostic initial attribuant certainement cet échec aux doubles antislashs était incorrect/trop catégorique.** La tentative V6 seule confirme deux LF et deux lignes, mais V6 reste false. Analyse statique ultérieure : `Metadata.set(String,String)` rejette le marqueur réservé `X-TIKA:INLINE_MATCH`. L'observateur est donc inadéquat ; ni l'absence d'évaluation du motif ni une validation comportementale réussie ne sont établies. Voir section 14 et consolidation.
- `behavior-attempt-1/` conserve le premier échec de compilation du programme de vérification (ContentHandler ambigu, corrigé en qualifiant le type SAX). `behavior/` contient actuellement la seconde tentative et ses résultats partiels ; ne pas les écraser lors de la relance. Aucun épuisement CPU volontaire ni requête réseau externe dans ces sondes : petit fichier local, marqueurs, connexion HTTPS simulée, chaîne regex courte.
- **État après consolidation** : synthèse/tableau, fiches et intégrité des archives vérifiés et manifestes produits. Reste l'observation V6 fiable, sans contourner la protection Metadata ; aucune nouvelle sonde ni modification Java autorisée par la dernière demande. Les comparaisons locales existent, mais aucun run GitHub des sept injections. Ne pas créer de commit, même temporaire, au titre de la demande documentaire actuelle. Ne pas cocher la clôture globale de l'étape 5.
- Aucun nouveau commit, push ni run GitHub. Les candidats SSRF/traversée de chemins sont remplacés par ReDoS et permissions excessives car les règles générales verrouillées ne couvrent pas directement les premiers dans les classes visées. Les scripts de capture et build utilisés sont encore dans `/private/tmp/tache3-injection-checkpoint.py` et `/private/tmp/tache3-injection-build.py` ; en conserver une copie durable lors de la finalisation. La copie de construction courante est `/private/tmp/tache3-injections/build` ; les classes initiales utilisées pour comparaison sont dans `/private/tmp/tika-final-build-yy2wo_ao`.

## 14. Consolidation documentaire — 12 novembre 2026

- Dernière demande : uniquement relire les preuves, vérifier les sept fiches, préparer le tableau et les empreintes ; aucune modification Java ni sonde pendant cette consolidation. La demande précédente de validation V6 a produit une tentative bornée distincte, qui ne doit pas être confondue avec ce travail documentaire.
- Dossier durable : `tache3-rapports/vulnerabilites/consolidation-20261112/`. `README.md` donne le bilan et le tableau des sept CWE/règles/fichiers/résultats ; `matrix.json` conserve les détails ; `integrity.json` conserve les contrôles ; `context.json` fixe les empreintes des Java examinés et confirme le verrou de 82 règles identique à l'archive initiale.
- Audit sans exécution Tika : recomptage des résultats et identités Semgrep depuis les JSON et fragments archivés, correspondance avec les fiches, lecture des XML Surefire/Checkstyle et journaux des sept builds réussis. Résultats : 682 fichiers par scan, aucune erreur/règle ignorée, sept CWE distincts ; total final 24, avec 8 ajouts, 1 suppression, 16 conservés. Pour chaque build : 964 core + 8 processeur réussis, deux désactivations initiales, zéro violation.
- Intégrité : sept archives sources conformes à leurs 682 empreintes respectives ; 682 sources courantes conformes au manifeste V7. Les 16 `.tar.gz` (y compris essais conservés) sont lisibles et ont passé la lecture gzip intégrale. `checksums.json` et `SHA256SUMS` fixent les fichiers de preuve et le contexte documentaire, avec exclusions explicites AppleDouble/caches et des manifestes eux-mêmes. L'outil `audit.py` et les anciens outils Python de capture/build sont conservés, sans exécution de ces derniers.
- **V6 : rectification factuelle.** La tentative déjà archivée sous `V6/validation-finale-20261112/` confirme deux LF et deux lignes, mais indique V6=false. L'explication certaine par des sauts de ligne mal encodés est donc abandonnée. La lecture de `Metadata.set` → `blockReservedKeyWrite` → `ReservedNamespaces.isTikaNative` confirme que `X-TIKA:INLINE_MATCH` est un marqueur réservé dont l'écriture String est refusée. Le motif est évalué avant cette écriture dans le code ; l'absence du marqueur ne démontre pas l'absence d'évaluation. Ce constat est statique, pas une nouvelle sonde instrumentée réussie.
- Les six observations positives anciennes restent archivées avec leurs limites : commande locale, entité locale, callback fictif, connexion HTTPS simulée, acceptation ECB dans le contexte du scénario, bits POSIX. Aucun épuisement de ressources, handshake hostile réel, récupération de clé ni accès entre comptes n'est démontré.
- Fiches V1–V7, `tache3.md` et ce guide actualisés. Les anciens rapports et tentatives n'ont pas été écrasés. Aucun commit, push, publication, nouveau scan ou nouveau build pour cette consolidation ; aucun changement de production ni de sonde Java.
- **Étape 5 non clôturée globalement** : reste une observation V6 fiable, bornée et indépendante du marqueur réservé, après instruction autorisant cette vérification. Les comparaisons locales existent ; aucun run GitHub des sept états n'est enregistré dans les preuves. Le rapport final et les runs distants restent des étapes séparées. Ne pas recréer les sept modifications.

## 15. Étape 6 — comparaison finale et matrice terminées, 12 novembre 2026

- Décision utilisateur ultérieure à la section 14 : poursuivre le projet en conservant V6 comme détection statique confirmée avec une limite comportementale, sans refaire les injections ni chercher une exploitation. Cette réserve n'est plus bloquante pour l'étape suivante ; elle n'est pas déclarée résolue.
- Nouveau dossier `tache3-rapports/comparaisons/etape-06-20261112/`, sans écrasement des preuves antérieures. `before-semgrep.json`/`after-semgrep.json`, manifestes et archives sources figent les deux états. Configuration de comparaison conservée, sans règles upstream. Le script documentaire `compare.py` ne lance ni scan ni Java.
- Comparaison recalculée sur les identités règle/chemin/SHA-256 du fragment, avec occurrences : **17 initiales, 24 finales, 8 ajoutées, 1 supprimée, 16 conservées**. Les objets ajoutés/supprimés correspondent exactement au cumulatif V7 existant, pas seulement à ses comptes. Les deux scans archivés couvrent les mêmes 682 fichiers, CE 1.185.0, 82 règles verrouillées, sans erreur/règle ignorée.
- `added.json`, `removed.json`, `retained.json` et `associations.json` relient les huit ajouts à V1–V7 et aux sept CWE. Deux règles pour V2/XXE ; l'ancienne identité ProcessBuilder remplacée par V1 est une suppression d'identité, pas une correction de sécurité. L'INFO V6 reste incluse.
- `validation-matrix.json` et `.md` distinguent les 14 essais locaux antérieurs, les 5 runs GitHub de démonstration et les synthèses de l'état final. Cas réussis dans leur périmètre, V6 incomplète, blocage intégré des sept états et trois scénarios rickroll/tests-compilation non exécutés. Les anciens runs rouges attendus ne sont pas confondus avec des validations échouées ou une nouvelle vulnérabilité.
- **Pas de code 1 intégré revendiqué pour les sept injections actuelles** : la comparaison contient bien des nouveautés et 1 est le résultat attendu du contrôleur, mais aucun nouveau gate complet ni run GitHub de ces sept états n'a été exécuté. Les anciens runs de l'exemple temporaire restent les preuves distantes disponibles.
- Intégrité : manifeste de consolidation précédent vérifié avant mise à jour des guides ; leurs versions antérieures sont conservées dans `context-before/`. Les preuves CI locales et GitHub sont recoupées avec leurs empreintes. Nouveau manifeste `checksums.json`/`SHA256SUMS` pour cette étape ; les anciens manifestes ne sont pas réécrits. Les anciens chemins des guides peuvent naturellement différer après cette mise à jour, leurs anciennes empreintes correspondent aux copies figées.
- `README.md` résume résultats, liens et limites ; `summary.json` marque la comparaison terminée avec réserves documentées. `verify.py` vérifie les identités et empreintes en lecture seule, sans exécuter Tika. `readme.md` à la racine reste inchangé pour la future rédaction finale.
- Aucun nouveau scan, build, sonde, commit (même temporaire), push, PR, accès GitHub ou publication pendant cette étape. Les sources Java et fichiers Semgrep de production restent inchangés. Prochaine étape proposée : **7 — action réutilisable de rickroll**, uniquement après instruction, conditionnée à un échec de test et sans masquer celui-ci.

## 16. Étape 7 — rickroll rétro réalisé et validé localement, 12 novembre 2026

- L'utilisateur choisit le danseur ASCII animé dans un terminal rétro. Création de `.github/actions/rickroll/action.yml`, `ift3913-rickroll.sh` et README : six poses, lien fixe YouTube, résumé Markdown, mode `--preview`, délais limités. Aucun téléchargement, navigateur ou lecture automatique. Implémentation personnelle avec assistance IA, sans code de random-rickroll repris.
- `.github/scripts/tache3-test-status.py` pose un marqueur avant Maven et examine les XML Surefire récents de core/processeur. Vrais éléments `testcase/failure` ou `testcase/error`, compteurs cohérents ; rapports anciens ignorés. XML invalide, compteurs incohérents ou réussite sans rapports attendus → erreur technique 2, pas un test échoué inventé.
- Workflow tâche 3 : étape Maven identifiée `build`, `pipefail` conservé ; évaluation après succès/échec avec `always()` ; action locale appelée via `uses` si `has_failures == 'true'` et job non annulé. Conservation de l'état du détecteur dans les artefacts. Aucun `continue-on-error`, aucune désactivation de tests, job Semgrep toujours préalable. L'action retourne 0 pour son affichage, l'échec de l'étape Maven reste acquis.
- Validation réelle sur une copie neuve de l'archive initiale de 658 fichiers, sans les sept injections. Même commande Maven que le workflow. Réussite : 974 cas (966 core + 8 processeur), zéro échec/erreur, deux désactivations initiales, rickroll absent et code 0. Échec JUnit contrôlé : 975 cas, un échec, deux désactivations ; six poses et résumé, action 0, code Maven 1 préservé. Erreur de type à la compilation de test : 8 tests processeur réussis, aucun test core, pas de rickroll, code 1 préservé. Les fixtures ne sont jamais ajoutées aux sources du worktree.
- Neuf cas du détecteur validés : assertion, exception de test, succès, désactivation, ancien rapport rouge, absence de rapport après panne, succès sans rapport, XML malformé, compteurs incohérents. Syntaxes Bash/Python/YAML et dépendance sécurité vérifiées. Prévisualisation locale avec six poses réussie.
- Première exécution bloquée par l'installation du POM parent dans `~/.m2` ; échec technique archivé séparément. Relance autorisée avec cache Maven réussie. Aucun symptôme du sandbox présenté comme un échec JUnit.
- Preuves : `tache3-rapports/rickroll/etape-07-20261112/` (README, runs, neuf cas, fixtures, journaux, XML/checkstyle/JaCoCo en archives, aperçu, configuration figée, sources de référence, vérification et empreintes). Validateur durable : `tache3-rapports/rickroll/validate.py`. Les guides antérieurs sont figés dans `context-before/` et le manifeste étape 6 a été vérifié avant actualisation.
- Les **682 sources Java de production restent identiques au manifeste V7** ; aucune nouvelle injection ni sonde de vulnérabilité. Les tests JUnit de démonstration ne servent qu'à l'action humoristique. Les anciens résultats et limites V6 restent inchangés.
- **Limite : aucun runner GitHub utilisé pour l'étape 7.** Les mêmes commandes et point d'entrée composite ont été exercés localement, mais le moteur d'expressions GitHub, `uses` distant et le téléversement des nouveaux artefacts restent à confirmer après autorisation. Avec les sept injections, Semgrep bloque avant les tests ; démontrer le rickroll sur une branche/état de référence, sans affaiblir la sécurité.
- Aucun commit, push, PR ni publication pendant cette étape. Prochaine étape après instruction : 8, validations GitHub du rickroll et des sept modifications, puis consolidation du rapport final `readme.md`.
