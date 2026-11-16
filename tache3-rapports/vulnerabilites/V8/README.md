# V8 — Repli silencieux vers MD5 (CWE-328)

## Pourquoi une huitième modification

Nous avons ajouté le repli silencieux vers MD5 pour compléter la démonstration avec une faiblesse distincte détectée par une règle absente du rapport initial. V1 reste une injection shell introduite dans un chemin existant : le fait que Semgrep signalait déjà l'ancien appel ne l'invalide pas. Le type d'une vulnérabilité et l'identifiant d'une règle déjà présente sont deux notions différentes.

## Fichier et modification

`tika-core/src/main/java/org/apache/tika/digest/InputStreamDigester.java`, méthode `newMessageDigest()`. Cette classe calcule les empreintes des documents analysés (métadonnées `X-TIKA:digest:*`), utilisées pour vérifier l'intégrité d'un fichier ou repérer des doublons. Le choix de l'algorithme y a donc une conséquence réelle.

Avant, un algorithme inconnu du fournisseur JCA levait `IllegalArgumentException`. Maintenant, l'exception `NoSuchAlgorithmException` est interceptée et l'empreinte est calculée avec `MessageDigest.getInstance("MD5")`. Le patch est dans [change.patch](change.patch).

## Pourquoi le code est vulnérable

MD5 n'est plus résistant aux collisions : on sait fabriquer deux fichiers différents de même empreinte. Le repli est silencieux : l'appelant qui demande un algorithme fort mal orthographié ou indisponible (par exemple `SHA3-256` sur un fournisseur qui ne l'offre pas) reçoit une empreinte MD5 sans erreur ni avertissement. Un système qui se fie à cette empreinte pour l'intégrité ou la déduplication peut alors accepter un document substitué.

Limite : le repli ne se déclenche que si l'algorithme demandé est indisponible ; une configuration valide (SHA-256, par exemple) n'est pas touchée. Aucune collision n'a été fabriquée.

## Résultat Semgrep

Semgrep CE 1.185.0, mêmes 82 règles et même périmètre (682 fichiers) que le reste de la tâche, zéro erreur d'analyse :

| État | Alertes | Rapport condensé |
|---|---|---|
| Référence avant injections (860a263) | 17 | [semgrep-reference-860a263.json](semgrep-reference-860a263.json) |
| V1 à V7 (c20318f) | 24 | [semgrep-avant-V8-c20318f.json](semgrep-avant-V8-c20318f.json) |
| V1 à V8 | 25 | [semgrep-apres-V8.json](semgrep-apres-V8.json) |

La seule différence entre les deux derniers rapports est une alerte WARNING `use-of-md5` (CWE-328: Use of Weak Hash) à la ligne 76 de `InputStreamDigester.java`. Cette règle ne produit aucune alerte dans la référence.

Les trois scans ont été lancés en local sous Windows, directement avec la commande `semgrep scan` et les options de `semgrep/scan.py`, car `scan.py` et `gate.py` comparent des chemins avec `/` et ne fonctionnent pas tels quels sous Windows. Les rapports condensés gardent la règle, le fichier, la ligne, la sévérité et le CWE de chaque alerte.

Le [run GitHub 43729952161](https://github.com/EnzoT454/tika/actions/runs/43729952161), déclenché par le push de [aa5d406](https://github.com/EnzoT454/tika/commit/aa5d406bbded2dd73aecbc9c03fbb9d6f22da7a4), confirme ensuite ces résultats sur Ubuntu. La référence de ce push est `5edd9c9da85d30c2c63ddde265023313855690d8`, qui contient déjà V1 à V7 : 24 → 25 alertes, un ajout `use-of-md5`, aucune suppression et 24 conservées. Le statut est `new_findings`, code 1 ; la construction est ignorée et le téléversement des rapports réussit. Les deux JSON bruts ne contiennent aucune erreur d'analyse.

Les [preuves téléchargées](github-43729952161/) comprennent les rapports bruts avant/après, le différentiel, les manifestes, les journaux des scans et le journal du workflow. Le fichier `checksums.json` fixe leurs empreintes SHA-256. Aucun nouveau run n'a été lancé pour cet archivage.

## Build

La validation locale consignée est `mvn -B -ntp -pl tika-core -am clean install` : réussite avec la modification, 966 cas `tika-core` dont 2 désactivés (préexistants), 0 échec, 0 erreur, Checkstyle sans violation. Le journal Maven et les XML de ce build ne sont pas présents dans les archives disponibles ; cette déclaration n'est donc pas recoupée avec des preuves brutes. Le run GitHub ci-dessus bloque avant Maven et ne constitue pas une validation de construction.

## Correction

Rétablir la levée de `IllegalArgumentException` : un algorithme indisponible doit être refusé, jamais remplacé par un algorithme plus faible.
