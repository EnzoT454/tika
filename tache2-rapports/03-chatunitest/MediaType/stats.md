| # | Méthode | Tentatives | Appels LLM | Tours en échec de compilation | Exportée | Réussite à | Erreurs de compilation (symbole) |
|---|---|---|---|---|---|---|---|
| 0 | `application(String)` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 1 | `audio(String)` | 2 | 4 | 3 | oui | attempt1, tour 0 | class Method (2); method verifyStatic(java.lang.Class<org.apache.tika.mime.Med (1) |
| 10 | `unquote(String)` | 0 | 0 | 0 | non | — |  |
| 11 | `union(Map, Map)` | 0 | 0 | 0 | non | — |  |
| 12 | `getBaseType()` | 0 | 0 | 0 | non | — |  |
| 13 | `getType()` | 0 | 0 | 0 | non | — |  |
| 14 | `getSubtype()` | 0 | 0 | 0 | non | — |  |
| 15 | `hasParameters()` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 16 | `getParameters()` | 0 | 0 | 0 | non | — |  |
| 17 | `toString()` | 2 | 4 | 3 | oui | attempt1, tour 0 | incompatible types: java.lang.reflect.Constructor<org.apache (2); method newInstance(java.lang.String,java.lang.String) (2); class Constructor (1) |
| 18 | `equals(Object)` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 19 | `hashCode()` | 2 | 4 | 3 | oui | attempt1, tour 0 | reference to MediaType is ambiguous (3) |
| 2 | `image(String)` | 2 | 6 | 6 | non | — | method image in class org.apache.tika.mime.MediaType cannot  (4); method verifyStatic(java.lang.Class<org.apache.tika.mime.Med (1); class Method (1) |
| 20 | `compareTo(MediaType)` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 21 | `MediaType(String, String, Map)` | 0 | 0 | 0 | non | — |  |
| 22 | `MediaType(String, String)` | 0 | 0 | 0 | non | — |  |
| 23 | `MediaType(String, int)` | 0 | 0 | 0 | non | — |  |
| 24 | `MediaType(MediaType, Map)` | 0 | 0 | 0 | non | — |  |
| 25 | `MediaType(MediaType, String, String)` | 0 | 0 | 0 | non | — |  |
| 26 | `MediaType(MediaType, Charset)` | 0 | 0 | 0 | non | — |  |
| 3 | `text(String)` | 2 | 4 | 3 | oui | attempt1, tour 0 | method text in class org.apache.tika.mime.MediaType cannot b (9) |
| 4 | `video(String)` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 5 | `set(MediaType[])` | 0 | 0 | 0 | non | — |  |
| 6 | `set(String[])` | 0 | 0 | 0 | non | — |  |
| 7 | `parse(String)` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 8 | `isSimpleName(String)` | 0 | 0 | 0 | non | — |  |
| 9 | `parseParameters(String)` | 0 | 0 | 0 | non | — |  |

Total : 27 méthodes, 28 appels au modèle, 10 méthodes avec un test exporté (37 %), 44587 jetons de prompt, 11757 jetons de réponse.
