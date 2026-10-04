| # | Méthode | Tentatives | Appels LLM | Tours en échec de compilation | Exportée | Réussite à | Erreurs de compilation (symbole) |
|---|---|---|---|---|---|---|---|
| 0 | `readShortLE(InputStream)` | 2 | 6 | 6 | non | — | class BufferUnderrunException (7); class InvocationTargetException (3) |
| 1 | `readShortBE(InputStream)` | 2 | 6 | 6 | non | — | class BufferUnderrunException (24); class InputStream (3) |
| 10 | `readLongBE(InputStream)` | 2 | 6 | 6 | non | — | class BufferUnderrunException (6); unreported exception org.apache.tika.io.EndianUtils.BufferUn (1) |
| 11 | `readUE7(InputStream)` | 1 | 2 | 1 | oui | attempt0, tour 1 | incompatible types: possible lossy conversion from int to by (2) |
| 12 | `getShortLE(byte[])` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 13 | `getShortLE(byte[], int)` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 14 | `getUShortLE(byte[])` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 15 | `getUShortLE(byte[], int)` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 16 | `getShortBE(byte[])` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 17 | `getShortBE(byte[], int)` | 2 | 4 | 0 | oui | attempt1, tour 0 |  |
| 18 | `getUShortBE(byte[])` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 19 | `getUShortBE(byte[], int)` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 2 | `readUShortLE(InputStream)` | 2 | 6 | 6 | non | — | class BufferUnderrunException (6) |
| 20 | `getIntLE(byte[])` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 21 | `getIntLE(byte[], int)` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 22 | `getIntBE(byte[])` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 23 | `getIntBE(byte[], int)` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 24 | `getUIntLE(byte[])` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 25 | `getUIntLE(byte[], int)` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 26 | `getUIntBE(byte[])` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 27 | `getUIntBE(byte[], int)` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 28 | `getLongLE(byte[], int)` | 1 | 1 | 0 | oui | attempt0, tour 0 |  |
| 29 | `ubyteToInt(byte)` | 2 | 5 | 4 | oui | attempt1, tour 1 | incompatible types: possible lossy conversion from int to by (31); incompatible types: <nulltype> cannot be converted to byte (1); Some messages have been simplified; recompile with -Xdiags:v (1) |
| 3 | `readUShortBE(InputStream)` | 2 | 6 | 6 | non | — | class BufferUnderrunException (6) |
| 30 | `getUByte(byte[], int)` | 1 | 2 | 1 | oui | attempt0, tour 1 | incompatible types: possible lossy conversion from int to by (1) |
| 4 | `readUIntLE(InputStream)` | 2 | 6 | 6 | non | — | class BufferUnderrunException (6) |
| 5 | `readUIntBE(InputStream)` | 2 | 6 | 6 | non | — | class BufferUnderrunException (9); variable ReflectionTestUtils (6) |
| 6 | `readIntLE(InputStream)` | 2 | 6 | 6 | non | — | class BufferUnderrunException (6) |
| 7 | `readIntBE(InputStream)` | 2 | 6 | 6 | non | — | class BufferUnderrunException (9) |
| 8 | `readIntME(InputStream)` | 2 | 6 | 6 | non | — | class BufferUnderrunException (7) |
| 9 | `readLongLE(InputStream)` | 2 | 6 | 6 | non | — | class BufferUnderrunException (6) |

Total : 31 méthodes, 95 appels au modèle, 20 méthodes avec un test exporté (65 %), 64389 jetons de prompt, 38229 jetons de réponse.
