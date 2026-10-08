# Mesures finales comparables A/B/C

Score = `100 × KILLED / total`. `TIMED_OUT` reste séparé.

| État | Classe | Total | KILLED | TIMED_OUT | SURVIVED | NO_COVERAGE | Autres | Score |
|---|---|---:|---:|---:|---:|---:|---:|---:|
| A | EndianUtils | 206 | 38 | 0 | 14 | 154 | 0 | 18.45 % |
| A | FilenameUtils | 115 | 79 | 0 | 20 | 16 | 0 | 68.70 % |
| A | MediaType | 85 | 68 | 1 | 2 | 14 | 0 | 80.00 % |
| A | Total | 406 | 185 | 1 | 36 | 184 | 0 | 45.57 % |
| B | EndianUtils | 206 | 163 | 0 | 38 | 5 | 0 | 79.13 % |
| B | FilenameUtils | 115 | 82 | 0 | 17 | 16 | 0 | 71.30 % |
| B | MediaType | 85 | 72 | 1 | 1 | 11 | 0 | 84.71 % |
| B | Total | 406 | 317 | 1 | 56 | 32 | 0 | 78.08 % |
| C | EndianUtils | 206 | 204 | 0 | 2 | 0 | 0 | 99.03 % |
| C | FilenameUtils | 115 | 108 | 0 | 5 | 2 | 0 | 93.91 % |
| C | MediaType | 85 | 84 | 1 | 0 | 0 | 0 | 98.82 % |
| C | Total | 406 | 396 | 1 | 7 | 2 | 0 | 97.54 % |

Les trois états ont exactement les mêmes identités de mutants et les mêmes sources communes.
Les sources et configurations exactes sont archivées séparément dans A, B et C.

- A_to_B : 132 nouveaux `KILLED`, 0 pertes de `KILLED`.
- B_to_C : 79 nouveaux `KILLED`, 0 pertes de `KILLED`.
- A_to_C : 211 nouveaux `KILLED`, 0 pertes de `KILLED`.

Les transitions, identifiants et tests tueurs sont dans `comparison.json`.
Le lien entre chaque mutant, les données et l’assertion sera analysé à l’étape 6.
