#!/usr/bin/env python3
"""Consolider les preuves existantes ; aucune compilation ni sonde Java."""
from collections import Counter
import gzip
import hashlib
import json
from pathlib import Path
import tarfile
import xml.etree.ElementTree as ET

OUT = Path(__file__).resolve().parent
PROOF = OUT.parent
ROOT = PROOF.parents[1]


def digest(data):
    return hashlib.sha256(data).hexdigest()


def write(name, value):
    target = OUT / name
    if target.exists():
        raise RuntimeError('Ne pas écraser une preuve : ' + str(target))
    target.write_text(json.dumps(value, indent=2, ensure_ascii=False) + '\n')


def sources(archive, manifest):
    expected = json.loads(manifest.read_text())
    with tarfile.open(archive) as stream:
        data = {m.name: stream.extractfile(m).read() for m in stream
                if m.isfile() and m.name.endswith('.java')}
    assert {n: digest(b) for n, b in data.items()} == expected, str(archive)
    return data


def identities(report, data):
    assert not report.get('errors') and not report.get('skipped_rules')
    assert report['version'] == '1.185.0'
    assert set(report['paths']['scanned']) == set(data)
    result = Counter()
    for f in report['results']:
        raw = data[f['path']]
        start, end = f['start']['offset'], f['end']['offset']
        assert 0 <= start < end <= len(raw)
        result[(f['check_id'], f['path'], digest(raw[start:end].strip()))] += 1
    return result


before_dir = ROOT / 'tache3-rapports/semgrep-avant'
initial_data = sources(before_dir/'sources.tar.gz', before_dir/'source-manifest.json')
initial_report = json.loads((before_dir/'semgrep.json').read_text())
initial = previous = identities(initial_report, initial_data)
matrix = []
for i in range(1, 8):
    folder = PROOF / f'V{i}'
    record = json.loads((folder/'results.json').read_text())
    data = sources(folder/'sources.tar.gz', folder/'source-manifest.json')
    report = json.loads((folder/'semgrep.json').read_text())
    current = identities(report, data)
    incremental = json.loads((folder/'incremental.json').read_text())
    cumulative = json.loads((folder/'cumulative.json').read_text())
    for diff, base in ((incremental, previous), (cumulative, initial)):
        assert len(diff['added']) == sum((current-base).values())
        assert len(diff['removed']) == sum((base-current).values())
        assert diff['retained'] == sum((base & current).values())
    assert digest((ROOT/record['file']).read_bytes()) == record['current_source_sha256']
    assert record['file'] in (folder/'change.patch').read_text()
    attempt = 2 if i == 1 else 1
    build = json.loads((folder/f'build-results-{attempt}.json').read_text())
    assert build['exit_code'] == 0 and not build['sources_changed_by_build']
    assert 'BUILD SUCCESS' in (folder/f'build-attempt-{attempt}.log').read_text()
    counts, checkstyle = {}, {}
    with tarfile.open(folder/f'build-reports-{attempt}.tar.gz') as archive:
        for member in archive:
            if not member.isfile():
                continue
            module = member.name.split('/')[0]
            if '/surefire-reports/TEST-' in member.name and member.name.endswith('.xml'):
                node = ET.parse(archive.extractfile(member)).getroot()
                stats = counts.setdefault(module, {k: 0 for k in ('tests', 'failures', 'errors', 'skipped')})
                for key in stats:
                    stats[key] += int(node.get(key, 0))
            if member.name.endswith('checkstyle-result.xml'):
                checkstyle[module] = len(ET.parse(archive.extractfile(member)).getroot().findall('.//error'))
    for module in ('tika-core', 'tika-annotation-processor'):
        assert counts[module] == build['modules'][module]['surefire']
    assert counts['tika-core'] == {'tests': 966, 'failures': 0, 'errors': 0, 'skipped': 2}
    assert counts['tika-annotation-processor'] == {'tests': 8, 'failures': 0, 'errors': 0, 'skipped': 0}
    assert checkstyle and not any(checkstyle.values())
    findings = incremental['added']
    cwes = sorted({c.split(':', 1)[0] for f in findings for c in f['extra']['metadata']['cwe']})
    fiche = (folder/'README.md').read_text()
    assert record['rule'] in fiche and all(c in fiche for c in cwes)
    matrix.append({'id':f'V{i}', 'cwe':cwes, 'file':record['file'],
                   'rules':sorted({f['check_id'] for f in findings}),
                   'locations':[{'rule':f['check_id'], 'line':f['start']['line'], 'severity':f['extra']['severity']} for f in findings],
                   'incremental_added':len(incremental['added']), 'incremental_removed':len(incremental['removed']),
                   'cumulative_findings':len(report['results']), 'scan_files':len(data),
                   'build_exit_code':0, 'surefire':counts, 'checkstyle':checkstyle,
                   'archived_behavior':json.loads((PROOF/'behavior/results.json').read_text())['after'][f'V{i}'],
                   'github_run':record['github_run'], 'introduction_commit':record['introduction_commit']})
    previous = current
assert len({c for row in matrix for c in row['cwe']}) == 7
final_manifest = json.loads((PROOF/'V7/source-manifest.json').read_text())
assert all(digest((ROOT/name).read_bytes()) == h for name,h in final_manifest.items())
archive_checks = []
for path in sorted(PROOF.rglob('*.tar.gz')):
    if any(p.startswith('._') for p in path.parts):
        continue
    with gzip.open(path, 'rb') as stream:
        while stream.read(1024*1024):
            pass
    with tarfile.open(path) as archive:
        members = archive.getmembers()
    archive_checks.append({'path':str(path.relative_to(ROOT)), 'sha256':digest(path.read_bytes()),
                           'gzip_integrity':'passed', 'tar_members':len(members)})
write('matrix.json', matrix)
write('integrity.json', {'source_archives_match_manifests':7, 'current_production_files_match_final_manifest':len(final_manifest),
                       'successful_builds_cross_checked':7, 'distinct_cwes':7,
                       'final_diff':{'before':17,'after':24,'added':8,'removed':1,'retained':16},
                       'archives':archive_checks, 'probes_executed_during_consolidation':False})
print(json.dumps({'rows':len(matrix),'archives_verified':len(archive_checks),'distinct_cwes':7,'final_findings':24}))
