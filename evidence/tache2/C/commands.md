# Reproduction de l'état C

Depuis la racine du dépôt original, préparer une copie temporaire sans les
fichiers AppleDouble. Les tests IA corrigés et les deux classes de tests manuels
sont inclus automatiquement par `prepare-local-build.py`.

```bash
build_dir=$(python3 evidence/tache2/prepare-local-build.py)
cd "$build_dir"
./mvnw -B -pl tika-core -am clean install
./mvnw -B -pl tika-core -Pmutation org.pitest:pitest-maven:mutationCoverage
```

L'état C conserve les paramètres PIT de A et B : les deux classes de production,
les mutateurs `DEFAULTS`, deux threads, les mêmes délais et `withHistory=false`.
