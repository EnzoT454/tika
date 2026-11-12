#!/usr/bin/env python3
"""Exécuter le scan initial avec moteur, règles et périmètre figés."""
import argparse
import hashlib
import io
import json
import os
from pathlib import Path
import subprocess
import sys
import tarfile
import tempfile
import urllib.request


def sha256(data):
    return hashlib.sha256(data).hexdigest()


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', required=True, type=Path)
    parser.add_argument('--rules-dir', type=Path)
    parser.add_argument('--source-root', type=Path,
                        help='Copie des sources à analyser ; configuration de ce script conservée')
    args = parser.parse_args()
    config_root = Path(__file__).resolve().parent
    root = (args.source_root or config_root.parent).resolve()
    lock = json.loads((config_root / 'rules.lock.json').read_text())
    scope = json.loads((config_root / 'scope.json').read_text())
    # Respecter une configuration CA explicite ; sinon utiliser certifi.
    import certifi
    env = dict(os.environ)
    env['SEMGREP_ENABLE_VERSION_CHECK'] = '0'
    env.setdefault('SSL_CERT_FILE', certifi.where())
    env.setdefault('REQUESTS_CA_BUNDLE', certifi.where())
    cache = args.rules_dir or Path(tempfile.gettempdir()) / ('tika-semgrep-' + lock['upstream_commit']) / 'rules'
    missing = [n for n, h in lock['files'].items()
               if not (cache / n).is_file() or sha256((cache / n).read_bytes()) != h]
    if missing:
        url = 'https://codeload.github.com/semgrep/semgrep-rules/tar.gz/' + lock['upstream_commit']
        with urllib.request.urlopen(url, timeout=60) as response:
            raw = response.read()
        if sha256(raw) != lock['archive_sha256']:
            raise RuntimeError('Empreinte incorrecte pour l’archive des règles')
        with tarfile.open(fileobj=io.BytesIO(raw), mode='r:gz') as archive:
            members = {'/'.join(m.name.split('/')[1:]): m
                       for m in archive.getmembers() if m.isfile()}
            for name, expected in lock['files'].items():
                data = archive.extractfile(members[name]).read()
                if sha256(data) != expected:
                    raise RuntimeError('Empreinte incorrecte : ' + name)
                target = cache / name
                target.parent.mkdir(parents=True, exist_ok=True)
                target.write_bytes(data)
            # Conserver la notice upstream avec les copies locales.
            license_data = archive.extractfile(members['LICENSE']).read()
            (cache / 'LICENSE').write_bytes(license_data)
    actual_yaml = {str(p.relative_to(cache)) for p in cache.rglob('*')
                   if p.is_file() and p.suffix in ('.yaml', '.yml')}
    if actual_yaml != set(lock['files']):
        raise RuntimeError('Le cache contient un ensemble de règles différent du verrou')
    targets = scope['targets']
    if any(not (root / target).is_dir() for target in targets):
        raise RuntimeError('Un répertoire du périmètre est absent')
    if any(not list((root / target).rglob('*.java')) for target in targets):
        raise RuntimeError('Un module du périmètre ne contient aucun fichier Java')
    executable = str(Path(sys.executable).parent / 'semgrep')
    version = subprocess.run([executable, '--version'], env=env, capture_output=True,
                             text=True, timeout=60)
    if version.returncode or version.stdout.strip() != lock['semgrep_version']:
        raise RuntimeError('Version du moteur incorrecte : ' + version.stdout + version.stderr)
    output = args.output.resolve()
    output.parent.mkdir(parents=True, exist_ok=True)
    command = [executable, 'scan', '--config', str(cache), '--oss-only',
               '--no-rewrite-rule-ids', '--strict', '--metrics', 'off',
               '--disable-version-check', '--disable-nosem', '--no-git-ignore',
               '--include', '*.java', '--exclude', '**/._*',
               '--jobs', '2', '--timeout', '30',
               '--max-target-bytes', '0', '--json', '--output', str(output), *targets]
    print(json.dumps({'cwd': str(root), 'command': command}, ensure_ascii=False), flush=True)
    result = subprocess.run(command, cwd=root, env=env, timeout=900)
    if result.returncode == 0:
        report = json.loads(output.read_text())
        expected = {str(p.relative_to(root)) for target in targets
                    for p in (root / target).rglob('*.java')
                    if not any(part.startswith('._') for part in p.relative_to(root).parts)}
        scanned = set(report.get('paths', {}).get('scanned', []))
        if report.get('errors') or report.get('skipped_rules') or scanned != expected:
            raise RuntimeError('Analyse incomplète : erreurs, règles ignorées ou fichiers manquants')
    return result.returncode


if __name__ == '__main__':
    sys.exit(main())
