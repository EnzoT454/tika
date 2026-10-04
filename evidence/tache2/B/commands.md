# Reproduction de l'état B

Depuis la racine du dépôt original, préparer une copie temporaire sans les
fichiers AppleDouble, puis exécuter les mêmes paramètres Maven et PIT que
l'état A :

```bash
build_dir=$(python3 evidence/tache2/prepare-local-build.py)
cd "$build_dir"
./mvnw -B -pl tika-core -am clean install
./mvnw -B -pl tika-core -Pmutation org.pitest:pitest-maven:mutationCoverage
```

La copie utilisée par cette expérience contenait les deux sources de test
`LookaheadInputStreamGeneratedTest.java` et `FilenameUtilsGeneratedTest.java`.
Elles sont également archivées dans ce dossier, avec le manifeste des sources,
les rapports JaCoCo, PIT et Surefire.
