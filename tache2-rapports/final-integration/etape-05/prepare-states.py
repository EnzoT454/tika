#!/usr/bin/env python3
"""Préparer trois copies indépendantes et figer leurs sources avant mesure."""
import hashlib
import json
from pathlib import Path
import subprocess
import tarfile

ROOT = Path(__file__).resolve().parents[3]
EVIDENCE = Path(__file__).resolve().parent


def hashes(build):
    return {str(p.relative_to(build)): hashlib.sha256(p.read_bytes()).hexdigest()
            for p in sorted(build.rglob('*')) if p.is_file()
            and 'target' not in p.relative_to(build).parts
            and p.name != 'source-manifest.json'}


def main():
    decisions = json.loads((EVIDENCE.parent / 'etape-03/decisions.json').read_text())
    ai = sorted({d['file'] for d in decisions if d['decision'] != 'rejected'})
    manual = [f'tika-core/src/test/java/org/apache/tika/{package}/{name}ManualTest.java'
              for package, name in [('io', 'EndianUtils'), ('mime', 'MediaType'),
                                    ('io', 'FilenameUtils')]]
    assert len(ai) == 40
    states = {}
    for state in ['A', 'B', 'C']:
        out = EVIDENCE / state
        out.mkdir(exist_ok=False)
        build = Path(subprocess.check_output(
            ['python3', str(EVIDENCE.parent / 'etape-04/prepare-local-build.py')],
            cwd=ROOT, text=True).strip())
        excluded = (ai if state == 'A' else []) + (manual if state != 'C' else [])
        for relative in excluded:
            (build / relative).unlink()
        manifest = hashes(build)
        (out / 'source-manifest.json').write_text(json.dumps(manifest, indent=2)+'\n')
        (out / 'selection.json').write_text(json.dumps({
            'state': state, 'build_directory': str(build), 'excluded_sources': excluded,
            'accepted_ai_files': [] if state == 'A' else ai,
            'manual_files': manual if state == 'C' else [],
            'expected_core_cases': {'A': 749, 'B': 910, 'C': 966}[state]
        }, indent=2)+'\n')
        with tarfile.open(out / 'sources.tar.gz', 'w:gz') as archive:
            for relative in manifest:
                archive.add(build / relative, arcname=relative, recursive=False)
        states[state] = str(build)
    manifests = {s: json.loads((EVIDENCE/s/'source-manifest.json').read_text()) for s in states}
    common = {k: v for k, v in manifests['A'].items()
              if k.startswith('tika-core/src/main/') or k.endswith('pom.xml')
              or k.startswith('tika-core/src/test/')}
    assert all(all(manifests[s].get(k) == v for k, v in common.items()) for s in states)
    (EVIDENCE / 'states.json').write_text(json.dumps(states, indent=2)+'\n')
    print(json.dumps(states, indent=2))


if __name__ == '__main__':
    main()
