# Preuves GitHub Actions — 12 novembre 2026

Les cinq runs de démonstration sont archivés ici avec leurs métadonnées, journaux complets et huit artefacts. Les ZIP d'origine sont conservés et leurs contenus extraits sous `artifacts/`. Leur intégrité CRC et leur digest SHA-256 GitHub, lorsqu'il est fourni, ont été vérifiés. `checksums.json` couvre les fichiers locaux, hors fichiers auxiliaires macOS.

| Cas | Run | Résultat vérifié |
|---|---|---|
| Premier push | [43251450652](https://github.com/EnzoT454/tika/actions/runs/43251450652) | Erreur technique 2 : SHA précédent nul ; construction ignorée. |
| Manuel, référence explicite | [43251595954](https://github.com/EnzoT454/tika/actions/runs/43251595954) | 17 → 17, aucun ajout ; sécurité et construction réussies. |
| Push documentaire bénin | [43252710325](https://github.com/EnzoT454/tika/actions/runs/43252710325) | 17 → 17, aucun ajout ; sécurité et construction réussies. |
| PR avec alerte temporaire | [43253590194](https://github.com/EnzoT454/tika/actions/runs/43253590194) | 17 → 18, un ajout ; code 1 et construction ignorée. |
| PR corrigée | [43254989974](https://github.com/EnzoT454/tika/actions/runs/43254989974) | 17 → 17 par rapport à la base PR ; code 0 et construction réussie. |

Le run rouge signale `command-injection-process-builder` dans `tika-core/src/main/java/org/apache/tika/GateValidation.java:4`, introduit par [96318db](https://github.com/EnzoT454/tika/commit/96318db1eada7b025fefeb1be2b8d9a59565e345). Ce code de test n'est pas exécuté et ne compte pas parmi les sept injections définitives. La [correction e8384e4](https://github.com/EnzoT454/tika/commit/e8384e424ad8cc8fe4b43a78464fa71b056c0bdb) supprime le fichier. Les deux patchs sont archivés. Le run corrigé compare toujours la tête à la base de la PR ; il ne s'agit pas d'un rapport natif comparant le commit vulnérable au commit corrigé.

Dans les trois builds réussis, les XML Surefire donnent 966 cas core, dont 964 réussis et deux désactivations préexistantes, ainsi que 8/8 tests du processeur réussis. Zéro échec et zéro erreur. Les contrôles de configuration et les empreintes des 682 sources Java concordent avec la référence initiale. `results.json` rassemble les SHA réellement comparés, les conclusions des jobs et les comptes extraits des rapports ; `verification.json` résume les contrôles.

La [PR nº 1](https://github.com/EnzoT454/tika/pull/1) a été fermée **sans fusion** après récupération des preuves. `pr-before-close.json` et `pr-after-close.json` conservent les états vérifiés. Les branches de démonstration locale et distante sont conservées pour la traçabilité. Le worktree est revenu sur `tache3`, au commit [b6f2723](https://github.com/EnzoT454/tika/commit/b6f2723a3cdafdec790872041cd2285c36443ff8).

## Récupération effectuée

GitHub CLI a été utilisé avec `--repo EnzoT454/tika` : `gh run view ID --json …`, `gh run view ID --log`, `gh api repos/EnzoT454/tika/actions/runs/ID/artifacts` puis `gh api repos/EnzoT454/tika/actions/artifacts/ARTIFACT_ID/zip`. Les téléchargements ont été effectués sans rejouer les workflows. Les métadonnées de chaque artefact conservent son nom, son identifiant et son éventuelle date d'expiration. Les copies locales restent disponibles après expiration distante.

Le réseau du sandbox a d'abord refusé l'accès à GitHub ; la récupération a réussi après autorisation de l'environnement. Le retour de branche a également nécessité l'accès autorisé à l'index Git partagé, situé dans l'ancien worktree. Les messages locaux `non-monotonic index` sur `._pack-…idx` persistent ; aucun nettoyage de `.git` n'a été effectué.

Les annotations de dépréciation Node/actions et de migration `ubuntu-latest` sont présentes dans les journaux. Aucune version d'action ni aucun runner n'a été modifié au titre de cet archivage. Les preuves confirment le comportement observé de ces runs, sans garantir celui de futures versions du runner. La protection de branche, les sept injections définitives et le rickroll ne sont pas validés par cette démonstration.

Aucun nouveau commit ni push n'a été créé pendant l'archivage ; ces fichiers et les guides actualisés restent locaux en attente d'une instruction de publication. Le rapport final `readme.md` est inchangé.
