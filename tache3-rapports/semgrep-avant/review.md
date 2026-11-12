# Lecture initiale des 17 alertes

Les emplacements, messages et CWE sont dans `findings.json` et le JSON brut. Cet examen contextualise les résultats ; ce n'est pas un audit complet d'exploitabilité.

- Sept `unsafe-reflection` : ServiceLoader, XMLReaderUtils (deux), DataElement, StreamObject, PDFParserConfig et ConfigLoader. Il faut établir qui contrôle le nom de classe et les restrictions applicables. ServiceLoader vérifie notamment le type attendu ; XMLReaderUtils charge le gestionnaire de sécurité Xerces. Le motif ne prouve pas une injection exploitable.
- Sept `command-injection-process-builder` : FileCommandDetector, ExternalParser, LibPstParser (deux), TesseractOCRParser (deux) et PopplerRenderer. La règle d'audit signale des arguments dynamiques. Les exemples examinés passent un tableau ou une liste à ProcessBuilder ; cela ne prouve pas une interprétation par un shell. Examiner l'origine des arguments, l'exécutable et son interprétation avant de conclure.
- Une `saxparserfactory-disallow-doctype-decl-missing`, MimeTypesReader L223 : absence d'interdiction explicite des DOCTYPE dans la fabrique locale, qui active toutefois FEATURE_SECURE_PROCESSING. Vérifier l'origine de la configuration MIME et les protections de résolution avant de déclarer une XXE exploitable.
- Une `xmlinputfactory-possible-xxe`, XMLReaderUtils L307 : protections via `tryToSetStaxProperty`, dont SUPPORT_DTD=false et IS_SUPPORTING_EXTERNAL_ENTITIES=false, plus un résolveur ignorant. La règle ne reconnaît pas ce traitement auxiliaire ; le code illustre une limite de détection. Ce constat n'est ni une preuve d'exploitation ni une garantie universelle si une propriété échoue.
- Une `unencrypted-socket`, NetworkParser L68 : la branche telnet utilise une socket non chiffrée. Le comportement signalé est présent ; son risque dépend de l'usage, des données et du réseau.

Toutes ces alertes restent dans la référence, quelle que soit leur interprétation. Les introductions futures doivent être distinguées par identité et contexte, sans modifier les rapports bruts ni soustraire arbitrairement les résultats bruyants.
