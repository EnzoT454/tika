# Étape 7 — Rickroll animé façon terminal rétro

Implémentation locale terminée le 12 novembre 2026. L'action composite `.github/actions/rickroll` invoque `ift3913-rickroll.sh` avec `uses`. Elle affiche six poses ASCII, le lien YouTube et un résumé Markdown. Les images textuelles restent successives dans les logs ; aucune lecture vidéo ou requête réseau n'est lancée par le script.

Le workflow conserve Semgrep comme préalable. Il démarre un marqueur temporel avant Maven, puis examine les nouveaux XML Surefire via `.github/scripts/tache3-test-status.py`, même après un échec du build. Le rickroll dépend d'un `testcase` contenant `failure` ou `error` ; les compteurs sont contrôlés et les anciens rapports exclus. Une erreur technique du détecteur échoue explicitement et ne produit pas de signal JUnit fictif.

L'action retourne 0 lorsqu'elle affiche correctement les six poses. L'étape Maven reste échouée et aucun `continue-on-error` n'est introduit. La ligne « EXIT 1 (Maven) » décrit l'échec original ; elle n'est pas le code de sortie du dessin. L'annulation du job ne doit pas déclencher l'action.

## Trois cas réels sur une copie isolée

La référence de construction initiale de 658 fichiers a été reconstruite dans un dossier temporaire, sans les sept injections. Ses empreintes ont été vérifiées. Les mêmes commandes de build et d'évaluation que dans le YAML ont ensuite été exécutées ; le point d'entrée de l'action composite a été appelé lorsque son indicateur était vrai. Aucun runner GitHub n'est simulé comme s'il avait été réellement utilisé.

| Cas | Observation Surefire | Code Maven | Rickroll | Code de build conservé |
|---|---|---:|---|---:|
| Tests réussis | 974 cas, 0 échec/erreur, 2 désactivations initiales | 0 | Non | 0 |
| Test JUnit volontairement échoué | 975 cas, 1 échec contrôlé, 0 erreur, 2 désactivations initiales | 1 | Oui : 6 poses et résumé | 1 |
| Erreur de compilation de test | 8 tests processeur réussis ; aucun test core exécuté | 1 | Non | 1 |

Le dernier cas contient une erreur de type Java syntaxiquement valide (`String` assignée à `int`). Le journal confirme `incompatible types` à la compilation ; il ne s'agit pas d'une assertion JUnit échouée. Les deux fixtures `RickrollDemoTest.java` sont conservées sous `runs/` pour la preuve, en dehors de la suite normale. Le dossier temporaire de build a été supprimé automatiquement ; aucune fixture n'a été ajoutée aux sources du worktree.

Les neuf cas ciblés du détecteur réussissent aussi : assertion échouée, exception de test, succès, test désactivé, ancien rapport rouge, absence de rapport après échec technique, réussite annoncée sans rapport, XML malformé et compteur incohérent. Les trois derniers sont correctement refusés comme erreurs techniques. Les cas de rapports sont synthétiques ; les trois builds du tableau utilisent réellement Maven et JUnit.

## Preuves et reproduction

`runs/results.json` et les dossiers de cas conservent les codes, résultats du détecteur, XML/checkstyle/JaCoCo bruts dans `reports.tar.gz`, journaux Maven, sorties GitHub simulées localement, résumé et `rickroll.log` pour l'échec JUnit. `preview.log` montre l'aperçu sans échec fictif. `configuration.tar.gz` fige le workflow, l'action, le détecteur et le validateur ; `baseline-sources.tar.gz` fige la référence sûre.

La première tentative, bloquée par le sandbox pendant l'installation du POM parent dans `~/.m2`, est conservée dans `attempt-1-sandbox/`. Ce blocage n'est pas annoncé comme un échec de test. La relance autorisée a donné les trois résultats ci-dessus.

Commande exécutée depuis le worktree :

```bash
/private/tmp/tache3-semgrep-venv/bin/python tache3-rapports/rickroll/validate.py \
  --output /private/tmp/tache3-rickroll-validation-final
```

Le validateur exige un dossier de sortie neuf ; il utilise Java 17, Maven wrapper et les dépendances du cache local. Il ne désactive aucun contrôle du projet. Les versions de guides antérieures à cette étape sont conservées dans `context-before/`. Le manifeste de l'étape 6 a été vérifié avant mise à jour des guides ; leurs anciennes empreintes restent vérifiables sur ces copies.

`verification.json`, `checksums.json` et `SHA256SUMS` conservent les contrôles et empreintes. Depuis la racine :

```bash
shasum -a 256 -c tache3-rapports/rickroll/etape-07-20261112/SHA256SUMS
```

## Limites et prochaine étape

La syntaxe Bash/Python/YAML et la dépendance sécurité sont vérifiées. Les commandes sont validées sur macOS avec un environnement de type runner local ; **le moteur d'expressions GitHub, l'invocation distante via `uses` et le téléversement des nouveaux artefacts ne sont pas encore exécutés sur GitHub**. Les publications et runs distants demandent une instruction correspondante.

Une branche contenant les sept injections sera arrêtée avant la construction par Semgrep. La future démonstration du rickroll doit utiliser un état de référence sans injections et une modification de test contrôlée, en conservant le contrôle Semgrep actif. Aucun affaiblissement du workflow n'est inclus.

Les 682 sources Java de production du worktree sont inchangées par rapport au manifeste V7, et les limites de V6 restent conservées. Aucun commit, push, PR ou publication pendant cette étape. Prochaine étape : validation GitHub de l'action et des sept modifications après autorisation, puis rapport final concis dans `readme.md`.
