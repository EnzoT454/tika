Contrôle Semgrep : new_findings
Référence : 860a2631d9e78311387207ed9380275b2a151781
Version : c20318f65f5fabb3a59be08f8cb8529ea06ca6b8
Alertes : 17 → 24 ; 8 nouvelles, 1 supprimées.
- command-injection-process-builder : tika-core/src/main/java/org/apache/tika/parser/external/ExternalParser.java:192
- documentbuilderfactory-disallow-doctype-decl-false : tika-core/src/main/java/org/apache/tika/utils/XMLReaderUtils.java:1270
- documentbuilderfactory-disallow-doctype-decl-missing : tika-core/src/main/java/org/apache/tika/utils/XMLReaderUtils.java:1271
- insecure-hostname-verifier : tika-core/src/main/java/org/apache/tika/io/TikaInputStream.java:224
- java-pattern-from-string-parameter : tika-core/src/main/java/org/apache/tika/parser/RegexCaptureParser.java:138
- object-deserialization : tika-core/src/main/java/org/apache/tika/parser/ParseContext.java:149
- overly-permissive-file-permission : tika-core/src/main/java/org/apache/tika/io/TemporaryResources.java:94
- use-of-default-aes : tika-core/src/main/java/org/apache/tika/parser/CryptoParser.java:78
