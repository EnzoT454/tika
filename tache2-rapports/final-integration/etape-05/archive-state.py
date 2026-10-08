#!/usr/bin/env python3
"""Archiver les rapports bruts et calculer un score KILLED/total uniquement."""
from collections import Counter, defaultdict
import hashlib
import json
from pathlib import Path
import shutil
import sys
import xml.etree.ElementTree as ET

EVIDENCE = Path(__file__).resolve().parent


def main(state):
    out = EVIDENCE / state
    selection = json.loads((out/'selection.json').read_text())
    build = Path(selection['build_directory'])
    manifest = json.loads((out/'source-manifest.json').read_text())
    changes = [name for name, digest in manifest.items()
               if not (build/name).is_file()
               or hashlib.sha256((build/name).read_bytes()).hexdigest() != digest]
    assert not changes, f'Sources modifiées pendant la construction : {changes}'
    totals = {}
    categories = defaultdict(Counter)
    ai_classes = {Path(p).stem for p in selection['accepted_ai_files']}
    manual_classes = {Path(p).stem for p in selection['manual_files']}
    for module in ['tika-core', 'tika-annotation-processor']:
        target = build/module/'target'
        dest = out/module
        dest.mkdir(exist_ok=True)
        reports = dest/'surefire-reports'
        reports.mkdir(exist_ok=True)
        counts = Counter()
        for path in sorted((target/'surefire-reports').glob('TEST-*.xml')):
            shutil.copyfile(path, reports/path.name)
            root = ET.parse(path).getroot()
            c = Counter({k: int(root.attrib.get(k, 0))
                         for k in ['tests', 'failures', 'errors', 'skipped']})
            counts.update(c)
            if module == 'tika-core':
                simple = root.attrib['name'].split('.')[-1]
                category = 'ai' if simple in ai_classes else 'manual' if simple in manual_classes else 'original'
                categories[category].update(c)
        assert counts['failures'] == counts['errors'] == 0
        checkstyle = target/'checkstyle-result.xml'
        shutil.copyfile(checkstyle, dest/checkstyle.name)
        assert not ET.parse(checkstyle).getroot().findall('.//error')
        totals[module] = dict(counts, passed=counts['tests']-counts['skipped'], checkstyle_errors=0)
    assert totals['tika-core']['tests'] == selection['expected_core_cases']
    assert categories['original']['tests'] == 749
    for category, expected in [('ai', 0 if state == 'A' else 161), ('manual', 56 if state == 'C' else 0)]:
        assert categories[category]['tests'] == expected
        assert categories[category]['skipped'] == 0
    shutil.copytree(build/'tika-core/target/site/jacoco', out/'jacoco', dirs_exist_ok=True)
    shutil.copytree(build/'tika-core/target/pit-reports', out/'pit-reports', dirs_exist_ok=True)
    records = []
    statuses = Counter()
    by_class = defaultdict(Counter)
    for mutant in ET.parse(out/'pit-reports/mutations.xml').getroot().findall('mutation'):
        fields = {tag: mutant.findtext(tag, default='') for tag in [
            'sourceFile', 'mutatedClass', 'mutatedMethod', 'methodDescription',
            'lineNumber', 'mutator', 'description', 'killingTest']}
        fields['indexes'] = [node.text for node in mutant.findall('indexes/index')]
        fields['blocks'] = [node.text for node in mutant.findall('blocks/block')]
        fields['status'] = mutant.attrib['status']
        fields['detected'] = mutant.attrib.get('detected')
        fields['numberOfTestsRun'] = mutant.attrib.get('numberOfTestsRun')
        identity = {k: fields[k] for k in ['mutatedClass', 'mutatedMethod',
                    'methodDescription', 'lineNumber', 'mutator', 'indexes', 'blocks', 'description']}
        fields['id'] = hashlib.sha256(json.dumps(identity, sort_keys=True).encode()).hexdigest()
        records.append(fields)
        statuses[fields['status']] += 1
        by_class[fields['mutatedClass']][fields['status']] += 1
    assert len({r['id'] for r in records}) == len(records)
    def summarize(counts):
        total = sum(counts.values())
        return {'total': total, 'statuses': dict(counts),
                'score_killed_only': 100*counts['KILLED']/total}
    summary = {'state': state, 'tests': totals, 'test_categories': {k: dict(v) for k,v in categories.items()},
               'sources_unchanged_during_build': True, 'mutation': summarize(statuses),
               'by_class': {k: summarize(v) for k,v in by_class.items()}}
    classes = build/'tika-core/target/classes'
    summary['target_bytecode_sha256'] = {
        str(path.relative_to(classes)): hashlib.sha256(path.read_bytes()).hexdigest()
        for pattern in ['org/apache/tika/io/EndianUtils*.class',
                        'org/apache/tika/io/FilenameUtils*.class',
                        'org/apache/tika/mime/MediaType*.class']
        for path in sorted(classes.glob(pattern))}
    jacoco_classes = {}
    for cls in ET.parse(out/'jacoco/jacoco.xml').getroot().findall('package/class'):
        name = cls.attrib['name'].replace('/', '.')
        if name in by_class:
            jacoco_classes[name] = {c.attrib['type']: {
                'covered': int(c.attrib['covered']), 'missed': int(c.attrib['missed'])}
                for c in cls.findall('counter')}
    summary['jacoco_classes'] = jacoco_classes
    (out/'mutants.json').write_text(json.dumps(records, indent=2)+'\n')
    (out/'results.json').write_text(json.dumps(summary, indent=2)+'\n')
    print(json.dumps(summary, indent=2))


if __name__ == '__main__':
    main(sys.argv[1])
