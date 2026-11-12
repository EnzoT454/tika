# IFT3913 — Tâche 3 : plan de réalisation et critères de réussite

Ce document est le guide de préparation de la tâche 3. Il décrit les exigences, les choix recommandés, les preuves à conserver et les étapes à réaliser. **Il ne constitue pas une preuve de réalisation** : aucune injection, mesure Semgrep ou validation GitHub n'est acquise par sa seule présence. Le rapport final devra distinguer les résultats vérifiés des intentions.

## 1. Binôme, dépôt et organisation

| Membre | GitHub |
|---|---|
| Aqel Hamza | EnzoT454 |
| Saidana Mohamed | FakeTMG |

- Fork conservé : https://github.com/EnzoT454/tika
- Branche de la tâche : `tache3`.
- Base choisie : `62ff2e9b180817f4b38108c4e90ffa05348c5a4d`, message `Update contributor name in readme.md`.
- Lien de référence : https://github.com/EnzoT454/tika/commit/62ff2e9b180817f4b38108c4e90ffa05348c5a4d (accessible en ligne si ce commit a été publié).
- Dossier de travail séparé : `/Volumes/TOSHIBA_EXT/ift3913/tika-tache3`.
- Dossier historique préservé : `/Volumes/TOSHIBA_EXT/ift3913/tika`, branche `tache2-final`.

Les deux dossiers sont des worktrees du même dépôt : ils partagent les objets Git et les références, mais ont leurs propres fichiers et index. La nouvelle branche part du commit indiqué, pas des modifications locales non commitées de l'ancien dossier. Ne pas copier automatiquement ces modifications dans la tâche 3.

Le nouveau dossier reste sur le disque externe : cette séparation n'élimine pas les fichiers AppleDouble `._*`. Pour les constructions, préparer si nécessaire une copie sur disque interne, sans désactiver les contrôles du projet. Les preuves durables doivent être rapatriées dans le dossier de la tâche.

Le travail initial reste local. La création du guide et de la branche n'autorise ni push, ni PR, ni publication. Les validations distantes seront nécessaires à la fin, après instruction correspondante. Ne pas envoyer les vulnérabilités pédagogiques au projet Apache.

## 2. Sources officielles et échéance

- [Instructions de la tâche 3](https://github.com/umontreal-diro/IFT3913/blob/2026/tache3/readme.md).
- [Page principale, calendrier et cas d'étude](https://github.com/umontreal-diro/IFT3913/blob/2026/README.md).
- [Modèle de remise](https://github.com/umontreal-diro/IFT3913/blob/2026/.github/PULL_REQUEST_TEMPLATE/tache3-readme.md).
- [Référence CLI Semgrep](https://semgrep.dev/docs/cli-reference).
- [Conditions et échecs GitHub Actions](https://docs.github.com/en/actions/reference/workflows-and-actions/expressions).

Consultation des consignes : 10 novembre 2026. Échéance affichée : **mardi 17 novembre 2026 à 17 h EST, heure de Montréal**. Revalider la page officielle avant la remise. La page indique une retenue de 10 % dans les premières 24 heures de retard, 50 % entre 24 et 48 heures et aucune note au-delà.

Le cas d'étude autorise les modules `tika-core`, `tika-serialization`, `tika-parser-ocr-module`, `tika-parser-pdf-module` et `tika-parser-microsoft-module`. Commencer dans `tika-core`, puis élargir dans ce périmètre si les sept types le nécessitent. La limite de trois classes de la tâche 2 n'est pas explicitement répétée dans la tâche 3.

**Énoncé corrigé fourni par l'utilisateur le 12 novembre :** le rickroll vise explicitement le cas d'étude, donc Tika. Cela confirme le plan retenu : une action réutilisable appelée après un échec de test, sans masquer celui-ci. Rust reste un exemple facultatif.

## 3. Exigences et barème

| Critère | Poids | Travail attendu | Preuve recommandée |
|---|---:|---|---|
| GitHub Action | 20 % | Faire échouer le processus de build lorsqu'un commit introduit une nouvelle vulnérabilité détectée par Semgrep. | Runs sans nouveauté réussis et runs avec nouveauté échoués pour la bonne raison. |
| Documentation GA | 20 % | Expliquer les choix de conception, l'implémentation et leur validation. | Workflow commenté, politique de référence Git, matrice de validation, journaux et liens de runs. |
| Vulnérabilités | 30 % | Modifier Tika pour introduire sept vulnérabilités de types différents détectables par Semgrep. | Sept modifications identifiées et résultats associés aux règles. |
| Documentation des vulnérabilités | 20 % | Justifier le fichier, le type, le caractère vulnérable et le résultat Semgrep pour chacune. | Sept fiches précises, liens aux lignes et commits, comparaison avant/après. |
| Humour | 10 % | Invoquer une action réutilisable de rickroll quand un test échoue. | Run avec échec de test déclenchant l'action et contrôle positif sans déclenchement. |

Les preuves proposées renforcent le dossier ; elles ne sont pas toutes des prescriptions textuelles du cours. La documentation représente 40 % de la note. Un workflow rouge seul ne prouve pas que Semgrep a bloqué une nouvelle vulnérabilité.

## 4. Concepts à comprendre

Semgrep analyse statiquement le code selon des règles. Un résultat Semgrep est une alerte à interpréter : il ne prouve pas à lui seul qu'une entrée hostile atteint réellement le code dans toutes les configurations.

Pour chaque injection, expliquer l'entrée contrôlable, son chemin vers une opération dangereuse, la protection absente et les conditions nécessaires. Une méthode inutilisée ajoutée uniquement pour correspondre à un motif constitue une démonstration moins convaincante qu'une modification cohérente d'un chemin existant.

Une nouvelle vulnérabilité est une alerte introduite par rapport à une référence définie. Les alertes préexistantes doivent être visibles dans le rapport initial, sans être attribuées aux modifications du binôme.

Exemple : la référence contient 12 alertes ; un changement bénin en conserve 12 et passe ; un ajout en produit une treizième et échoue. **Comparer seulement les comptes est insuffisant** : une suppression et une introduction peuvent garder le même total. Comparer les identités des résultats avec une méthode résistante aux déplacements de lignes, de préférence le mécanisme différentiel de Semgrep validé sur la version retenue.

Sept types différents signifient sept mécanismes de faiblesse distincts, pas sept occurrences d'une même règle. Utiliser les identifiants de règles et les CWE lorsqu'ils sont disponibles, sans inventer une correspondance.

## 5. Architecture recommandée du workflow

Modifier ou prolonger un workflow existant de Tika, en conservant un diff lisible. Ne pas remplacer silencieusement le workflow historique de la tâche 2. Séparer les responsabilités : analyse Semgrep, construction/tests, conservation des preuves et action humoristique conditionnelle.

Principes recommandés :

- Fixer la version de Semgrep et la révision des règles ; archiver la configuration effective. Un jeu téléchargé dynamiquement peut évoluer entre deux mesures.
- Utiliser le même moteur, les mêmes règles et le même périmètre pour les rapports avant/après.
- Analyser les chemins de production retenus et documenter les exclusions. Éviter les doublons issus des archives, copies et sorties `target/` ; ne pas exclure une injection pour rendre la CI verte.
- Produire un rapport JSON brut et une synthèse lisible ; SARIF est un complément possible.
- Récupérer l'historique Git nécessaire à la comparaison et afficher les SHA utilisés.
- Définir la référence par événement : base/ancêtre commun pour une PR, état précédent pour un push, traitement explicite d'un premier push ou d'une référence absente. Un push de plusieurs commits ne doit pas être réduit arbitrairement à `HEAD^`.
- Distinguer la référence initiale figée du rapport académique et la référence de comparaison utilisée pour chaque événement CI.
- Configurer un véritable code de sortie bloquant. `semgrep scan` seul ne bloque pas par défaut sur les résultats ; vérifier les options de la version retenue.
- Traiter explicitement les erreurs de configuration, références absentes, scans incomplets et timeouts. Ne pas transformer une analyse impossible en réussite silencieuse.
- Conserver les rapports même après un échec, tout en préservant le statut d'échec du contrôle.
- Limiter les permissions à celles nécessaires. Une configuration CE locale peut éviter un compte ou un jeton Semgrep ; confirmer les capacités du mode choisi avant de le retenir.

L'option différentielle exacte et les codes de sortie seront vérifiés avec la version installée. Ce guide ne constitue pas un YAML exécutable validé. Un échec de job rend le workflow en échec ; empêcher une fusion exige en plus une protection de branche, si celle-ci est souhaitée et autorisée.

## 6. Choisir les sept vulnérabilités

Partir des règles Java disponibles, puis chercher des emplacements pertinents dans les modules autorisés. La liste suivante contient des candidats : aucun n'est encore retenu ni détecté.

| Type candidat | Ce qu'il faut démontrer |
|---|---|
| Injection de commandes | Une donnée contrôlable influence une commande exécutée sans protection adaptée. |
| Traversée de chemins | Un chemin contrôlable permet de sortir du répertoire autorisé. |
| XXE | Une configuration XML permet la résolution d'entités externes sur des données non fiables. |
| Désérialisation non sûre | Des données non fiables sont désérialisées sans contrôle adapté des types. |
| SSRF | Une entrée contrôle une destination réseau sans restrictions appropriées. |
| Validation TLS désactivée | Le code accepte un certificat ou un nom d'hôte sans validation correcte. |
| Cryptographie faible | Un mécanisme inadapté est utilisé dans un contexte qui exige une protection cryptographique. |

Remplacer un candidat si la règle n'est pas disponible, si sa détection demande un moteur non retenu ou si l'intégration serait artificielle. Une utilisation de MD5 pour un simple identifiant non sécuritaire n'est pas automatiquement une vulnérabilité cryptographique. L'analyse du contexte compte.

Pour chaque injection : conserver la compilation, éviter les dépendances gratuites, utiliser seulement des données fictives et ne pas exécuter d'exploitation réseau. Les vulnérabilités sont pédagogiques ; leur validation statique et leurs contre-exemples doivent rester contrôlés.

## 7. Étapes de réalisation et points de contrôle

### Étape 1 — État initial et périmètre

Vérifier branche, statut Git, commit, modules et workflows. Identifier les fichiers réellement présents et les éventuelles modifications locales. La clarification GraphHopper → Tika est confirmée. Construire la base et archiver le journal.

Livrable : environnement, SHA de base et preuve de construction. Ne pas annoncer une base validée avant cette vérification.

### Étape 2 — Semgrep et rapport avant

Installer une version précise, choisir les règles et lancer un premier scan. Examiner les erreurs et les fichiers réellement analysés. Archiver le JSON avant, les règles et les commandes. Inventorier les alertes préexistantes.

Livrable : référence de sécurité reproductible.

### Étape 3 — Contrôle différentiel CI

Implémenter le calcul de référence et le blocage. Vérifier localement un changement bénin et une première alerte nouvelle avant de préparer les sept injections. Prévoir les événements PR, push et éventuellement déclenchement manuel.

Livrable : workflow et comparaison validés sur des cas simples. Les runs GitHub restent à obtenir après autorisation de publication.

### Étape 4 — Sept injections documentées

Choisir les sept types avec leurs règles, puis modifier le code une vulnérabilité à la fois. Après chacune : scan, examen du résultat, compilation et contrôles adaptés au module. Préparer une fiche immédiatement. Lorsque les commits seront autorisés, préférer un commit identifiable par injection pour faciliter la revue.

Livrable : sept types démontrés et aucune confusion avec les alertes initiales.

### Étape 5 — Comparaison et validation complète

Scanner les états avant/après avec la même configuration. Produire les résultats ajoutés, conservés et supprimés. Relier chaque ajout à une injection ; expliquer les alertes supplémentaires éventuelles au lieu de forcer artificiellement un total de sept.

Livrable : tableau comparatif et matrice de validation ci-dessous, avec rapports bruts.

### Étape 6 — Action réutilisable de rickroll

Créer une action composite locale ou adapter une action existante en conservant l'attribution et la licence applicables. Une implémentation Rust n'est pas obligatoire. L'invoquer via `uses`, conditionnée à l'échec de l'étape de tests concernée. Un simple `if: failure()` global pourrait réagir à une panne Maven ou Semgrep sans échec de test ; distinguer ces situations.

Prévoir une démonstration visible dans les logs ou le résumé et expliquer le comportement : un runner sans interface graphique ne garantit pas la lecture automatique d'une vidéo. Vérifier que l'humour ne masque pas l'échec initial. Réaliser un échec de test contrôlé, puis retirer ce test de démonstration de la suite normale et conserver son commit/run de preuve.

Livrable : invocation effective après échec de test, absence d'invocation après réussite, statut final toujours en échec dans le cas négatif.

### Étape 7 — Rapport final et preuves distantes

Rédiger le rapport autonome de tâche 3 dans **`readme.md` à la racine**, conformément à la décision utilisateur. Préserver le rapport de la tâche 2 sur sa branche et éviter deux noms qui ne diffèrent que par la casse sur macOS. Le rapport doit être plus court que celui de tâche 2, concret, naturel et centré sur les justifications et résultats. Ce `tache3.md` reste le guide de réalisation.

Après autorisation, publier la branche et effectuer les validations GitHub. Conserver les liens permanents vers les commits, fichiers et runs. Un workflow volontairement rouge après les injections est attendu : expliquer sa cause et fournir aussi des runs de contrôle réussis.

### Étape 8 — Remise dans le dépôt du cours

Après autorisation, préparer une PR sur le dépôt du cours et la branche officielle adéquate. Ajouter sous `tache3/NOM1_NOM2/` le petit `readme.md` conforme au modèle : noms, identifiants GitHub, lien du dépôt et lien du rapport. Ce fichier de liens ne remplace pas le rapport technique du fork. Revalider l'échéance et l'accessibilité des liens avant envoi.

## 8. Matrice de validation à compléter

| Scénario | Résultat attendu | Preuve à enregistrer |
|---|---|---|
| Même code que la référence | Aucune alerte nouvelle, succès du contrôle sécurité. | SHA, JSON, code de sortie et run. |
| Changement bénin | Succès malgré les alertes préexistantes. | Diff et rapport différentiel. |
| Une injection | Échec sécurité, règle et emplacement attendus. | Commit, résultat et run rouge. |
| Sept injections | Sept types distincts détectés, contrôle en échec. | Tableau des sept fiches et rapport après. |
| Correction d'une injection | L'alerte associée disparaît. | Comparaison ; succès seulement si aucune autre nouveauté ne reste. |
| Décalage de lignes sans nouveau défaut | Pas de fausse introduction due au seul décalage. | Avant/après et synthèse. |
| Suppression et ajout avec total identique | La nouvelle alerte est quand même bloquée. | Identités comparées. |
| Push de plusieurs commits | Toutes les nouveautés depuis l'état précédent sont considérées. | SHA de référence et SHA analysé. |
| Référence absente ou configuration invalide | Échec technique explicite. | Journal et code de sortie. |
| Tests réussis | Aucun rickroll. | Journal du workflow. |
| Test volontairement échoué | Action rickroll invoquée, échec conservé. | Rapport Surefire et run. |
| Échec de compilation sans échec de test | Ne pas l'attribuer à un cas de test échoué. | Journal et condition de déclenchement. |

## 9. Modèle de fiche pour chaque vulnérabilité

1. Identifiant V1 à V7, titre et type distinct.
2. Module, fichier, méthode, lignes et commit d'introduction.
3. Rôle du code existant et justification du choix du fichier.
4. Comportement avant et modification introduite.
5. Entrée contrôlable, opération dangereuse, protections absentes et conditions d'exploitation.
6. Règle Semgrep exacte, CWE si disponible, sévérité et message.
7. Résultat avant : absence de cette alerte ; résultat après : détection identifiée dans le JSON.
8. Commande, version du moteur, règles et référence Git utilisées.
9. Preuve du blocage CI et lien du run, une fois obtenu.
10. Correction possible, limites et éventuels faux positifs ou dépendances au contexte.

## 10. Organisation recommandée des preuves et du rapport

Créer progressivement `tache3-rapports/` avec `environnement/`, `avant/`, `apres/`, `comparaisons/`, `validation-ci/` et `vulnerabilites/V1/` à `V7/`. Les noms sont une proposition d'organisation, pas une exigence du cours.

Conserver les JSON bruts, commandes, journaux, versions, règles et SHA. Les captures illustrent les résultats mais ne remplacent pas les données. Éviter de stocker uniquement des artefacts GitHub à durée limitée ; conserver une copie utile dans le dépôt. Ne pas rescanner ces archives comme s'il s'agissait de production.

Plan du rapport final : binôme et périmètre ; environnement ; conception du workflow ; politique de référence et codes de sortie ; validation ; sept fiches ; comparaison avant/après ; action humoristique ; limites ; reproduction ; répartition du travail ; déclaration d'usage de l'IA.

## 11. Priorités, estimation et état de départ

Estimation indicative : 20 à 35 heures cumulées pour le binôme, à adapter après le premier scan. Priorité : obtenir tôt une alerte nouvelle qui fait échouer correctement le contrôle, puis étendre aux sept types. Réserver du temps aux runs distants et à la documentation ; ne pas attendre la fin pour écrire les fiches.

État au moment de la préparation :

- [x] Fork et commit de départ identifiés.
- [x] Branche et worktree confirmés après création.
- [x] Guide de réalisation préparé.
- [x] Construction initiale de la tâche 3 validée.
- [x] Interprétation GraphHopper confirmée.
- [x] Version Semgrep et règles figées.
- [x] Rapport avant archivé.
- [x] Blocage différentiel implémenté et validé localement (14 scénarios, 12 novembre) ; runs GitHub encore à obtenir.
- [ ] Sept vulnérabilités distinctes introduites et documentées.
- [ ] Rapports après et comparaison archivés.
- [ ] Action humoristique validée.
- [ ] Runs GitHub obtenus après autorisation.
- [ ] Rapport final consolidé et usage de l'IA déclaré.
- [ ] Remise effectuée après autorisation.

Ne cocher une étape qu'après examen de ses preuves. Ni une intention, ni un fichier YAML présent, ni un scan qui ne s'est pas terminé ne prouvent sa réussite.

### Validation initiale acquise — 10 novembre 2026

La construction complète de `tika-core` et ses dépendances réussit avec Java 17.0.18 et Maven 3.9.12. Core : 966 cas, 964 réussis et deux désactivations préexistantes, sans échec ni erreur. Les huit tests du processeur réussissent ; Checkstyle ne relève aucune violation. Les 658 fichiers copiés sont inchangés après construction. La première tentative a été bloquée par les permissions du cache Maven ; la relance autorisée réussit sans contournement des contrôles. Preuves : [tache3-rapports/environnement/commands.md](tache3-rapports/environnement/commands.md) et [results.json](tache3-rapports/environnement/results.json). Aucun scan Semgrep exécuté à ce stade.

### Scan initial Semgrep acquis — 10 novembre 2026

Semgrep CE 1.185.0, 82 règles Java générales de sécurité fixées à `a5ad1714f00c9f7fde980cab0ea1d6dc6d414fd2`. Les 682 sources Java de production des cinq modules autorisés ont été analysées sans erreur ni règle ignorée : 17 alertes préexistantes, 8 ERROR et 9 WARNING. Elles ne constituent pas 17 vulnérabilités confirmées. Les tests et archives sont hors périmètre. Les règles sont téléchargées et vérifiées, sans redistribution de leur contenu ; configuration dans [semgrep/README.md](semgrep/README.md). Preuves et limites : [tache3-rapports/semgrep-avant/commands.md](tache3-rapports/semgrep-avant/commands.md). La reproduction depuis un cache vide retrouve les mêmes résultats. Aucun contrôle différentiel, workflow ou injection n'est encore réalisé.

### Contrôle différentiel validé localement — 12 novembre 2026

`semgrep/gate.py` compare deux scans complets identiques d'instantanés Git. Référence push : `before` complet ; PR : ancêtre commun avec la cible ; manuel : SHA explicite. Codes : 0 sans nouveauté, 1 avec nouveautés, 2 pour une erreur technique. La comparaison utilise règle, chemin et fragment signalé avec multiplicité, sans numéro de ligne ; ses limites sont documentées dans [semgrep/README.md](semgrep/README.md).

Le workflow `.github/workflows/tache3-ift3913.yml` reprend la construction core de tâche 2 et y ajoute la sécurité préalable, sans modifier les workflows historiques. Les rapports sont conservés après échec ; aucune erreur n'est masquée. **14 scénarios réels réussissent** dans un dépôt temporaire : même code, bénin, décalage, nouvelle alerte, doublon, total constant, correction, push multi-commits, PR divergente, références absentes, manuel et erreurs de configuration/analyse. Les 682 empreintes initiales et courantes concordent. Preuves : [commands.md](tache3-rapports/validation-ci/commands.md), [results.json](tache3-rapports/validation-ci/results.json) et archives associées.

Aucun commit dans le dépôt réel, aucune injection définitive, aucun nouveau build Maven ni run GitHub. Le téléchargement des actions, l'installation Ubuntu et le téléversement des artefacts restent à valider à distance après autorisation. Prochaine étape proposée : sélectionner puis introduire les sept types distincts, un à un, après instruction. Le rickroll et le rapport final restent à faire.
