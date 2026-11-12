#!/usr/bin/env python3
"""Vérifier la comparaison figée et ses empreintes, sans exécuter Tika."""
from collections import Counter
import hashlib
import json
from pathlib import Path
import tarfile

OUT = Path(__file__).resolve().parent
ROOT = OUT.parents[2]


def sha(raw):
    return hashlib.sha256(raw).hexdigest()


def identities(findings, data):
    return Counter((f['check_id'],f['path'],sha(data[f['path']][f['start']['offset']:f['end']['offset']].strip()))
                   for f in findings)


data, indexes = {}, {}
for label in ('before','after'):
    with tarfile.open(OUT/(label+'-sources.tar.gz')) as archive:
        data[label]={m.name:archive.extractfile(m).read() for m in archive if m.isfile() and m.name.endswith('.java')}
    manifest=json.loads((OUT/(label+'-manifest.json')).read_text())
    assert {n:sha(b) for n,b in data[label].items()}==manifest
    report=json.loads((OUT/(label+'-semgrep.json')).read_text())
    assert not report.get('errors') and not report.get('skipped_rules')
    assert len(manifest)==682 and set(report['paths']['scanned'])==set(manifest)
    indexes[label]=identities(report['results'],data[label])
added=json.loads((OUT/'added.json').read_text())
removed=json.loads((OUT/'removed.json').read_text())
retained=json.loads((OUT/'retained.json').read_text())
assert identities([r['finding'] for r in added],data['after'])==indexes['after']-indexes['before']
assert identities([r['finding'] for r in removed],data['before'])==indexes['before']-indexes['after']
assert identities([r['before'] for r in retained],data['before'])==indexes['before'] & indexes['after']
assert identities([r['after'] for r in retained],data['after'])==indexes['before'] & indexes['after']
assert (len(added),len(removed),len(retained))==(8,1,16)
assert Counter(r['vulnerability'] for r in added)==Counter({'V1':1,'V2':2,'V3':1,'V4':1,'V5':1,'V6':1,'V7':1})
hashes=json.loads((OUT/'checksums.json').read_text())
assert all(sha((ROOT/name).read_bytes())==h for name,h in hashes.items())
print(json.dumps({'comparison':'passed','before':17,'after':24,'added':8,'removed':1,'retained':16,
                  'sha256_files_verified':len(hashes),'java_executed':False}))
