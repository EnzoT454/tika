# V2 — Entités XML externes (CWE-611)

`tika-core/.../utils/XMLReaderUtils.java`, méthode `getDocumentBuilder(ParseContext)`, fournit un parseur DOM lorsqu'aucun parseur n'est fourni par l'appelant. Ce point est choisi pour modifier une API existante de lecture XML tout en préservant le pool global utilisé par `buildDOM`.

Avant, le fallback utilisait la fabrique sécurisée et un résolveur ignorant les entités externes. Maintenant il construit une fabrique indépendante, active l'expansion des entités et les DOCTYPE, désactive le traitement sécurisé et n'installe pas de résolveur restrictif. Un XML non fiable traité par le DocumentBuilder ainsi obtenu peut demander une ressource externe avec les droits du processus. Cela dépend du fournisseur XML et des propriétés JAXP de la JVM ; les appels utilisant un builder de contexte fourni ou le pool global ne passent pas par ce fallback.

Le scan cumulatif passe de 17 à 19 alertes après V1 et V2. V2 ajoute **deux alertes pour un seul type XXE** : `documentbuilderfactory-disallow-doctype-decl-false` et `documentbuilderfactory-disallow-doctype-decl-missing`. Elles sont nouvelles par identité et emplacement, sans suppression des 17 signalements initiaux hors remplacement V1. Voir les deux JSON de comparaison.

Le build complet de cette version réussit : 964 tests core et 8 processeur réussis, deux désactivations initiales, zéro violation Checkstyle et aucune modification automatique des sources. Cela ne signifie pas que la suite existante couvre ce fallback : les tests DOM du pool global restent protégés.

Un premier essai, abandonné, modifiait le parseur DOM global et donc tout le pool. Son build a été interrompu pendant les tests XML ; aucune réussite n'est attribuée à cet essai. Il est conservé séparément dans `../V2-attempt-1/`. La version retenue ici limite l'injection au fallback du contexte.

Correction : réutiliser `XMLReaderUtils.getDocumentBuilder()` sécurisé ou interdire l'accès aux ressources externes et installer un résolveur adapté. Référence : [propriétés XMLConstants de Java 17](https://docs.oracle.com/en/java/javase/17/docs/api/java.xml/javax/xml/XMLConstants.html).

Preuves : patch, sources exactes, manifeste, JSON Semgrep, comparaisons et `build-results-1.json`. Configuration Semgrep inchangée, modification locale non commitée sur la base `860a2631d9e78311387207ed9380275b2a151781` ; aucun run GitHub propre à V2.

## Consolidation documentaire du 12 novembre

Les anciennes traces indiquent la lecture du marqueur local par le fallback DOM ; aucune ressource externe hostile n’est nécessaire pour cette observation. Les archives sources, XML et comptes Semgrep ont été recoupés sans nouvelle sonde. Voir [le bilan des sept types](../consolidation-20261112/README.md) et ses limites. Aucun run GitHub propre à cette injection n’est encore enregistré.

## Actualisation GitHub après les scans locaux

Ces sources ont été introduites dans le [commit 5d37388](https://github.com/EnzoT454/tika/commit/5d37388b0cbf1baeb19c04a2e1c0ed633753765c) et les nouveautés ont été confirmées dans le [run final 43278937793](https://github.com/EnzoT454/tika/actions/runs/43278937793), sur c20318f. Les mentions « non commitée » et « aucun run » ci-dessus décrivent les premiers scans locaux. Voir [la consolidation finale](../../rapport-final-20261112/README.md).
