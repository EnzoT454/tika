# Consolidation finale et rapport de tâche 3

Vérification du 12 novembre 2026, à partir des artefacts téléchargés par l'utilisateur. Aucun nouveau run, test, build, scan, appel GitHub, commit ou publication pendant cette consolidation. Les sources Java et les protections existantes restent inchangées.

## Cinq runs recoupés

| Cas | Run / SHA de tête | Conclusion vérifiée |
|---|---|---|
| Rickroll : tests réussis | [43276322587](https://github.com/EnzoT454/tika/actions/runs/43276322587) / `99ef66be2baff901cecd20358cee73349b0db42a` | Sécurité et construction réussies ; rickroll ignoré. |
| Rickroll : échec JUnit | [43276898487](https://github.com/EnzoT454/tika/actions/runs/43276898487) / `3c6fc2d2e19ad5f229fe7b7494f3f5baf59e3cf7` | Un cas échoué, six poses, action réussie et job en échec. |
| Rickroll : erreur de compilation | [43277596512](https://github.com/EnzoT454/tika/actions/runs/43277596512) / `1073e389f96192a5e8abfab19624430dd32da141` | Erreur javac, aucun échec JUnit core ; rickroll ignoré et build rouge. |
| Rickroll : correction | [43278025406](https://github.com/EnzoT454/tika/actions/runs/43278025406) / `f91d03f5f46ed9f8706ff66b53c06039408fc94d` | Retour au vert ; rickroll ignoré. |
| Sept modifications | [43278937793](https://github.com/EnzoT454/tika/actions/runs/43278937793) / `c20318f65f5fabb3a59be08f8cb8529ea06ca6b8` | Semgrep code 1, 17 → 24, +8/−1, 16 conservées ; construction ignorée. |

Les quatre premiers runs sont des PR sur une référence sans injections, permettant d'atteindre les tests sans désactiver Semgrep. Les réussites comptent chacune 974 cas (966 core + 8 processeur), zéro échec/erreur et deux désactivations initiales. L'échec JUnit compte 975 cas et une assertion échouée supplémentaire. L'erreur de compilation ne laisse que les huit tests processeur exécutés. Les XML, états du détecteur et étapes GitHub sont cohérents ; les étapes de conservation d'artefacts réussissent, y compris après échec.

Pour le dernier run, les manifestes avant/après correspondent exactement aux sources de la comparaison locale. Les identifiants, chemins et positions des signalements concordent ; les huit ajouts correspondent à V1–V7. Le commit d'introduction commun est `5d37388b0cbf1baeb19c04a2e1c0ed633753765c`, dont les sept fichiers ont été comparés aux sources actuelles. La référence CI est `860a2631d9e78311387207ed9380275b2a151781`.

`rickroll-results.json`, `semgrep-results.json` et `vulnerability-provenance.json` donnent les données recoupées. La [matrice finale](validation-matrix.md) remplace l'état de couverture antérieur sans l'écraser : les quatre cas auparavant non exécutés sont désormais confirmés, et V6 reste incomplète sur le plan comportemental.

## Limites conservées

V6 est détectée statiquement, y compris sur GitHub, mais aucun épuisement de ressources n'a été démontré. L'indicateur `X-TIKA:INLINE_MATCH` est rejeté par les métadonnées réservées ; l'observation négative n'est pas interprétée comme une absence d'évaluation. Les autres sondes gardent leur portée limitée : callback fictif, HTTPS simulé, contexte cryptographique précis et bits POSIX plutôt qu'accès entre comptes. Aucun résultat d'exploitation externe n'est revendiqué.

Le statut GitHub rouge des sept modifications prouve le blocage du workflow, sans valider une protection de branche obligatoire. Cette dernière n'a pas été consultée. Aucun nouvel état de PR n'a été interrogé à distance ; la présence ou l'absence du test temporaire est vérifiée dans les arbres Git locaux des commits de correction et de tâche 3.

## Rapport et historique

Le rapport soumis est maintenant `readme.md` à la racine. Sa version de tâche 2 est préservée octet pour octet dans `readme-tache2-avant.md`, avec son SHA-256 dans `verification.json`. Ses liens relatifs étaient destinés à la racine ; la [version Git immuable](https://github.com/EnzoT454/tika/blob/f7bc2acbe7fa48895077fef999d002c548a88569/readme.md) conserve ce contexte. `context-before/` fige les guides et fiches avant actualisation.

La répartition indiquée par l'utilisateur est un travail en commun, en même temps, avec validation au fur et à mesure. La déclaration d'assistance IA est conservée dans le rapport, sans attribuer artificiellement des tâches individuelles ou présenter le code comme entièrement écrit sans assistance.

## Intégrité et reproduction de l'audit

Les répertoires originaux téléchargés restent sous `tache3-rapports/rickroll/github/` et `tache3-rapports/validation-ci/github/43278937793/`. `github-evidence.tar.gz` en conserve une copie regroupée sans AppleDouble. Les fichiers sont des artefacts déjà extraits par `gh run download` ; cette archive est un regroupement local, pas le ZIP original signé par GitHub.

`consolidate.py` relit JSON, XML, journaux et objets Git locaux, sans Java ni réseau. Il refuse d'écraser ses sorties. Le manifeste antérieur a été vérifié avant les changements de documentation. Les anciennes versions des documents modifiés sont conservées pour vérifier les anciennes empreintes ; les anciens manifestes ne sont pas réécrits.

`checksums.json` et `SHA256SUMS` fixent les fichiers actuels, les preuves et les versions de contexte. Depuis la racine :

```bash
shasum -a 256 -c tache3-rapports/rapport-final-20261112/SHA256SUMS
```

Les empreintes garantissent la cohérence locale de l'état conservé, sans remplacer une signature externe de provenance. Toute évolution ultérieure des guides ou du rapport nécessite un nouveau manifeste daté.

**Reste à faire :** relecture du rapport, publication après instruction, puis préparation et remise dans le dépôt du cours après autorisation. Le rapport rédigé localement n'est pas encore publié par cette consolidation.
