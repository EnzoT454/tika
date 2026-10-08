#!/usr/bin/env python3
"""Relier chaque nouvelle détection IA aux sources et oracles exacts."""
from collections import Counter
import hashlib
import json
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[3]
OUT = Path(__file__).resolve().parent
PREVIOUS = OUT.parent/'etape-05'

# Commentaires humains : les sources exactes restent la preuve des données/oracles.
DATA = {
 'testGetIntBEWithOffset': 'Tableau 01…08, offset 4 ; valeur BE 0x05060708.',
 'testGetIntLE': 'Octets 01 02 03 04 ; valeur LE 0x04030201 (appel direct ou réflexion).',
 'testGetLongLE': 'Octets 12 34 56 78 9A BC DE F0, offset 0 ; valeur LE 0xF0DEBC9A78563412L.',
 'testGetShortLE': 'Octets 01 02, offset 0 ; short LE 0x0201.',
 'testGetUByte': 'Premier octet 01, offset 0 ; valeur non signée 1.',
 'testGetUIntBEWithLargeNumber': 'Octets 00 FF FF FF ; entier non signé BE 0x00FFFFFFL.',
 'testGetUIntBEWithNegativeNumber': 'Octets FF FF FF FF ; entier non signé 4294967295L.',
 'testGetUIntLEWithOffset': 'Tableau 00 00 00 00 00 00 01 00, offset 4 ; valeur LE 0x00010000L.',
 'testGetUShortBE': 'Octets 01 02 ; entier BE non signé 258.',
 'testGetUShortLE': 'Octets 00 01 ; entier LE non signé 256.',
 'testGetUShortLEWithOffset': 'Tableau 01…08, offset 2 ; entier LE 0x0403.',
 'testReadIntBEWithNegativeInput': 'Flux FF 00 00 00 ; int signé BE 0xFF000000.',
 'testReadIntBEWithPartialInput': 'Mockito renvoie 01, 02, -1, 0 ; BufferUnderrunException obligatoire.',
 'testReadIntBEWithValidInput': 'Flux 01 02 03 04 ; int BE 0x01020304.',
 'testReadIntLEValidInput': 'Flux 12 34 56 78 ; int LE 0x78563412.',
 'testReadLongBE': 'Flux 01…08 → 0x0102030405060708L ; FF FE FD FC FB FA F9 F8 → 0xFFFEFDFCFBFAF9F8L ; quatre octets → underrun.',
 'testReadLongLE': 'Mockito renvoie 12 34 56 78 9A BC DE F0 ; 0xF0DEBC9A78563412L et huit lectures.',
 'testReadLongLE_WithBufferUnderrunException': 'Mockito renvoie sept octets valides puis -1 ; underrun et huit lectures.',
 'testReadShortBE': 'Mockito renvoie 12 34 puis -1 ; short BE 0x1234 sur les deux premières lectures.',
 'testReadUShortBEWithEndOfStream': 'Mockito renvoie 12 puis -1 ; underrun attendu.',
 'testReadShortLEWithBufferUnderrun': 'Flux contenant seulement 12 ; underrun attendu.',
 'testReadShortLEWithNegativeBytes': 'Flux FF FE ; short LE signé 0xFEFF.',
 'testReadUE7_MultipleBytes': 'Flux 81 00 ; (1 << 7) + 0 = 128L.',
 'testReadUShortBE': 'Flux 12 34 ; entier BE non signé 0x1234.',
 'testUbyteToIntMax': 'Conversion du byte 127 → entier 127 ; le flux local créé est inutilisé.',
 'testGetSanitizedEmbeddedFileName_withPathContainingColonAndColonAndColonAndBackslash': 'RESOURCE_NAME_KEY seul = C:::/path/to/file.txt ; nom attendu file.txt.',
 'testGetSanitizedEmbeddedFilePath_withReservedCharacters': 'EMBEDDED_RESOURCE_PATH = invalid:filename, autres propriétés null ; défaut test, maxLength 100 ; résultat non nul sans deux-points.',
 'testResolveWithin': 'Répertoire /tmp, enfant test.txt ; Path attendu /tmp/test.txt. Dépend de l’absence du fichier au moment de la mesure.',
 'testEqualsWithNull': 'Fixture MediaType avec paramètre charset ; equals(null) doit être false.',
 'testImageMethod': 'image/png sans puis avec charset=UTF-8 ; image("") doit renvoyer null.',
 'testVideoMethod': 'video(mp4) doit égaler new MediaType("video", "mp4", emptyMap).',
}


def masked(source):
    pattern = r'//[^\n]*|/\*.*?\*/|"(?:\\.|[^"\\])*"|\x27(?:\\.|[^\x27\\])*\x27'
    return re.sub(pattern, lambda m: ''.join('\n' if c=='\n' else ' ' for c in m[0]), source, flags=re.S)


def method(source, name):
    clean = masked(source)
    match = re.search(r'\bvoid\s+'+re.escape(name)+r'\s*\(', clean)
    assert match, name
    start = clean.index('{', match.end()); end = start+1; depth = 1
    while depth:
        depth += (clean[end]=='{') - (clean[end]=='}'); end += 1
    line = source.count('\n', 0, match.start())+1
    return source[match.start():end], line


def assertions(body):
    clean = masked(body); found = []
    for match in re.finditer(r'\b(?:assert\w+|verify)\s*\(', clean):
        end = match.end(); depth = 1
        while depth:
            depth += (clean[end]=='(') - (clean[end]==')'); end += 1
        found.append(body[match.start():end])
    return found


def mechanism(m):
    desc = m['description']; name = m['mutatedClass'].split('.')[-1]
    if name == 'FilenameUtils':
        return {'getEmbeddedName':'La négation ignore la seule propriété renseignée ; null remplace file.txt et assertEquals échoue.',
                'getEmbeddedPath':'La négation ignore la seule propriété renseignée ; null viole assertNotNull, avant assertFalse sur les deux-points.',
                'resolveWithin':'La négation de Files.exists(resolved) force toRealPath sur un enfant absent ; une IOException inattendue empêche assertEquals(Path, Path).'}[m['mutatedMethod']]
    if name == 'MediaType':
        if m['mutatedMethod']=='equals': return 'Le retour false de la branche non-MediaType devient true ; assertFalse(equals(null)) échoue.'
        if m['mutatedMethod']=='video': return 'La fabrique renvoie null ; assertEquals(expectedMediaType, actualMediaType) échoue.'
        if m['mutatedMethod']=='union': return 'Le type de base a une map vide et la map ajoutée contient charset=UTF-8 ; la condition inversée perd le paramètre. La chaîne attendue avec charset et son assertion de valeur distinguent le mutant.'
        if m['mutatedMethod']=='parse': return 'La négation est sur CHARSET_FIRST_PATTERN.matches(), pas TYPE_PATTERN. image("") aboutit à une tentative group() sans match : exception inattendue avant assertNull(invalidMediaType).'
    if 'return with 0' in desc: return 'La valeur attendue est non nulle ; assertEquals distingue le retour forcé à zéro.'
    if 'Shift Left' in desc: return 'Les octets non nuls occupent des positions déterminées ; le décalage droite change la valeur vérifiée par assertEquals.'
    if 'addition with subtraction' in desc:
        if m['mutatedMethod']=='getLongLE': return 'La borne initiale offset+LONG_SIZE-1 devient offset-LONG_SIZE-1 ; la boucle est sautée à offset 0, résultat 0 au lieu de la valeur longue attendue.'
        return 'La soustraction des contributions non nulles change la valeur endian attendue par assertEquals.'
    if 'AND with OR' in desc: return 'Le masque de l’octet ou de l’entier non signé devient un OU qui force des bits à 1 ; la valeur exacte attendue par assertEquals change.'
    if 'OR with AND' in desc:
        if m['mutatedMethod']=='getLongLE': return 'L’accumulation commence à zéro ; le ET empêche de composer les octets et contredit la valeur longue attendue.'
        return 'Le -1 signalant la fin prématurée est masqué par un ET avec des octets valides ; l’exception exigée par assertThrows disparaît.'
    if desc=='Changed increment from 1 to -1': return 'Le parcours d’indices change : mauvaise valeur ou accès hors bornes. assertEquals échoue ou le test lève une exception inattendue (enveloppée par invoke si réflexion).'
    if 'integer subtraction with addition' in desc: return 'La borne initiale de getLongLE passe de offset+7 à offset+9 ; le tableau de huit octets est dépassé avant assertEquals.'
    if desc=='negated conditional':
        if m['mutatedMethod']=='getLongLE': return 'j>=offset devient j<offset ; la boucle est sautée à offset 0, résultat 0 au lieu de la valeur longue attendue.'
        if 'WithBufferUnderrunException' in m['killingTest']: return 'La garde d’underrun est inversée sur un flux tronqué ; l’exception obligatoire disparaît et assertThrows échoue.'
        return 'La garde d’underrun est inversée sur un flux complet ; BufferUnderrunException inattendue au lieu de la valeur attendue par assertEquals.'
    if desc=='changed conditional boundary':
        if m['mutatedMethod']=='getLongLE': return 'La boucle j>=offset devient j>offset : l’octet à offset 0 est omis ; assertEquals sur la valeur longue échoue.'
        if m['lineNumber']=='235': return 'Le dernier octet 00 est exclu par i>0 au lieu de i>=0 ; le résultat vaut 1 au lieu de 128 et assertEquals échoue.'
        if m['lineNumber']=='246': return 'L’octet final 00 est considéré comme une fin prématurée par i<=0 ; exception inattendue avant assertEquals(128L, result).'
    raise AssertionError(m)


def main():
    changes = json.loads((PREVIOUS/'comparison.json').read_text())['transitions']['A_to_B']['newly_killed']
    decisions = json.loads((OUT.parent/'etape-03/decisions.json').read_text())
    provenance = {(Path(d['file']).stem,d['method']):d for d in decisions}
    manifest = json.loads((PREVIOUS/'B/source-manifest.json').read_text())
    records = []; cases = {}
    snapshots = OUT/'ai-killing-sources'; snapshots.mkdir(exist_ok=True)
    for m in changes:
        full = re.search(r'\[class:([^]]+)\]', m['killingTest'])[1]
        name = re.search(r'\[method:([^](]+)', m['killingTest'])[1]
        simple = full.split('.')[-1]; relative = 'tika-core/src/test/java/'+full.replace('.','/')+'.java'
        source = (ROOT/relative).read_text()
        assert hashlib.sha256((ROOT/relative).read_bytes()).hexdigest() == manifest[relative]
        (snapshots/(simple+'.java.txt')).write_text(source)
        body, line = method(source,name); oracle = assertions(body); assert oracle
        origin = provenance[(simple,name)]
        first_test = re.search(r'@Test\b',source)
        fixture = source[:first_test.start()] if first_test else ''
        key = simple+'#'+name
        case = {'test':key,'source':relative,'line':line,'data_and_expected_result':DATA[name],
                'method_source':body,'assertions':oracle,'fixture_context':fixture,
                'integration_decision':origin['decision'],'correction_reason':origin['reason']}
        cases[key] = case
        production = 'tika-core/src/main/java/'+m['mutatedClass'].replace('.','/')+'.java'
        record = dict(m, test_case=key, test_source=relative, test_line=line,
                      data_and_expected_result=DATA[name], assertions=oracle,
                      integration_decision=origin['decision'],
                      production_source=production,
                      production_line=(ROOT/production).read_text().splitlines()[int(m['lineNumber'])-1].strip(),
                      detection_mechanism_inferred_from_source=mechanism(m))
        records.append(record)
    assert len(records)==132 and len(cases)==32
    (OUT/'ai-detections.json').write_text(json.dumps(records,indent=2,ensure_ascii=False)+'\n')
    (OUT/'ai-test-oracles.json').write_text(json.dumps(list(cases.values()),indent=2,ensure_ascii=False)+'\n')
    lines = ['# 132 nouvelles détections IA de A vers B', '',
             'PIT fournit le test tueur et le statut. Le mécanisme ci-dessous est une analyse des sources,',
             'pas une trace d’exécution fournie par PIT. Une exception inattendue peut détecter le mutant avant l’assertion.',
             'Les sources complètes, fixtures et assertions exactes sont archivées ; les corrections humaines restent déclarées.', '',
             '| ID (préfixe) | Mutant : classe, méthode, ligne, index | Transition | Test / entrée-oracle | Mécanisme |',
             '|---|---|---|---|---|']
    def safe(text): return text.replace('|','\\|').replace('\n',' ')
    for m in records:
        lines.append('| '+ ' | '.join(map(safe,[m['id'][:12],
            m['mutatedClass'].split('.')[-1]+'.'+m['mutatedMethod']+' L'+m['lineNumber']+' index '+','.join(m['indexes'])+' : '+m['description'],
            m['previous_status']+' → KILLED', m['test_case']+' ; '+m['data_and_expected_result'],
            m['detection_mechanism_inferred_from_source']]))+' |')
    lines += ['', '## Oracles exacts des 32 tests tueurs', '']
    count = Counter(m['test_case'] for m in records)
    for key, case in sorted(cases.items()):
        lines += ['### '+key, '', f"{count[key]} nouveaux mutants ; décision : `{case['integration_decision']}`.",
                  '',case['data_and_expected_result'], '',
                  f"[Source](../../../../{case['source']}) ; fixture complète dans `ai-killing-sources/`.", '',
                  '```java',case['method_source'],'```','']
    (OUT/'ai-detections.md').write_text('\n'.join(lines))
    print('132 mutants liés à 32 tests, données, assertions, sources et corrections humaines.')


if __name__ == '__main__':
    main()
