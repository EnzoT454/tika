# V8 — Repli silencieux vers MD5 (CWE-328)

## Pourquoi une huitième modification

La V1 (injection de commande) modifie un appel `ProcessBuilder` que la règle `command-injection-process-builder` signalait déjà avant nos changements : cette règle donne 7 alertes dans la référence et 7 après. Le contrôle différentiel la compte comme nouvelle parce que le fragment signalé a changé, mais le type d'alerte existait déjà. V8 ajoute donc un septième type d'alerte **absent de la référence**, pour que sept types soient nouveaux sans ambiguïté (V2 à V8), V1 restant documentée comme aggravation d'une alerte existante.

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

## Build

`mvn -B -ntp -pl tika-core -am clean install` réussit avec la modification : 966 tests `tika-core` exécutés, 0 échec, 0 erreur, 2 désactivés (préexistants), Checkstyle sans violation.

## Correction

Rétablir la levée de `IllegalArgumentException` : un algorithme indisponible doit être refusé, jamais remplacé par un algorithme plus faible.
