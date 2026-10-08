# Étape 3 — sélection et corrections des tests issus de ChatUniTest

Branche : `tache2-final`, base Saidana `619ff284`. Travail local, sans génération
supplémentaire, sans modification de production et sans exécution PIT.
Java OpenJDK 17.0.18, wrapper Maven 3.9.12, JUnit 6.1.3.

## Provenance

- `before/` : les 49 fichiers intégrés Saidana avant ce tri, copiés sans retouche.
  Ils incluent les corrections et annotations de rejet de l'expérience Saidana.
  Les véritables sorties brutes restent dans `tache2-rapports/03-chatunitest/`.
- `decisions.json` : inventaire des 193 méthodes, décision, justification,
  corps avant/après et empreinte du corps initial. Une méthode corrigée peut
  contenir plusieurs corrections ; 27 méthodes corrigées ne signifie pas
  27 lignes ni 27 interventions logiques.
- `decisions.md` : les 59 décisions de correction ou rejet, lisibles.
- `accepted-sources/` et `source-manifest.json` : 40 classes réellement testées.
- `curate.py` : script de sélection utilisé, avec les ajouts de déclarations
  d'exceptions constatés à la compilation. Ne pas le relancer dans ce dépôt :
  il refuse d'écraser les archives existantes. Son rejeu exige une copie vierge
  de l'état initial des tests Saidana.

## Validation

Dans la copie interne `/private/tmp/tika-final-step2-wbf90_na`, les anciennes
sources IA ont été remplacées par celles de la sélection. Un nettoyage Maven
a supprimé les classes compilées obsolètes des fichiers désormais rejetés.

```bash
./mvnw -B -pl tika-core clean resources:resources compiler:compile \
  resources:testResources compiler:testCompile surefire:test \
  '-Dtest=EndianUtils*_Test,MediaType*_Test,FilenameUtils*_Test'
```

Cette première compilation a révélé la déclaration manquante de
`BufferUnderrunException` dans `testReadIntBEWithNegativeInput`.
Après correction, une deuxième tentative a révélé la même nécessité dans
`testReadShortLEWithNegativeBytes`. Les deux échecs sont archivés, et ces
modifications sont incluses dans le registre humain.

La commande finale réussie, sans nouveau changement de production :

```bash
./mvnw -B -pl tika-core compiler:testCompile surefire:test \
  '-Dtest=EndianUtils*_Test,MediaType*_Test,FilenameUtils*_Test'
```

**161 tests, zéro échec, erreur ou désactivation** : 76 `EndianUtils`,
39 `MediaType`, 46 `FilenameUtils`. Les noms des classes et les effectifs
Surefire ont été comparés aux décisions de sélection ; les sources du dépôt
sont identiques aux copies effectivement compilées.

Puis la suite complète déjà compilée est exécutée :

```bash
./mvnw -B -pl tika-core surefire:test
```

**966 cas, 964 réussis, deux désactivés, zéro échec et erreur.**
Comptage : 749 cas originaux + 161 cas IA retenus + 56 cas manuels.
Les rapports XML sont copiés dans `surefire-reports/` et les journaux sont
conservés. Les deux désactivations appartiennent aux tests originaux ; aucune
annotation de désactivation ne reste dans les sources IA acceptées.

Ces commandes appellent des objectifs directs : elles valident la compilation
et Surefire, pas la construction complète avec les contrôles du cycle Maven.
Le blocage Checkstyle de l'étape 2 reste à résoudre à l'étape 4.
Les scores A/B/C historiques restent inchangés ; les nouvelles mesures sont
réservées à l'étape 5.
