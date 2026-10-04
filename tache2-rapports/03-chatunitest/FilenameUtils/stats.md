| # | Méthode | Tentatives | Appels LLM | Tours en échec de compilation | Exportée | Réussite à | Erreurs de compilation (symbole) |
|---|---|---|---|---|---|---|---|
| 0 | `normalize(String)` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 1 | `getName(String)` | 1 | 2 | 0 | oui | attempt0, tour 1 |  |
| 10 | `calculateExtension(Metadata, String)` | 2 | 6 | 6 | non | — | incompatible types: java.lang.String cannot be converted to  (8); Some messages have been simplified; recompile with -Xdiags:v (3); method getMIME_TYPES() (2) |
| 11 | `lookupExtension(String)` | 0 | 0 | 0 | non | — |  |
| 2 | `getSuffixFromPath(String)` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 3 | `getSanitizedEmbeddedFileName(Metadata, String, int)` | 2 | 6 | 1 | non | — | variable EMBEDDED_NAME (9) |
| 4 | `getSanitizedEmbeddedFilePath(Metadata, String, int)` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 5 | `resolveWithin(Path, String)` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 6 | `getPrefixLength(String)` | 0 | 0 | 0 | non | — |  |
| 7 | `removeProtocol(String)` | 0 | 0 | 0 | non | — |  |
| 8 | `getEmbeddedPath(Metadata)` | 0 | 0 | 0 | non | — |  |
| 9 | `getEmbeddedName(Metadata)` | 0 | 0 | 0 | non | — |  |

Total : 12 méthodes, 18 appels au modèle, 5 méthodes avec un test exporté (42 %), 36001 jetons de prompt, 14135 jetons de réponse.
