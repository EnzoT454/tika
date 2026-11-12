#!/usr/bin/env python3
"""Comparer deux instantanés Git : 0 succès, 1 nouveautés, 2 erreur technique."""
import argparse
from collections import defaultdict
import hashlib
import io
import json
import os
from pathlib import Path, PurePosixPath
import re
import subprocess
import sys
import tarfile
import tempfile


CONFIG = Path(__file__).resolve().parent


def git(repo, *args):
    result = subprocess.run(['git', '-C', str(repo), *args], capture_output=True)
    if result.returncode:
        raise RuntimeError(result.stderr.decode(errors='replace').strip())
    return result.stdout


def commit(repo, value):
    # Les références externes sont des SHA, jamais des options Git.
    if not isinstance(value, str) or not re.fullmatch(r'[0-9a-fA-F]{40}', value) or set(value) == {'0'}:
        raise ValueError('Référence absente ou SHA invalide : ' + str(value))
    return git(repo, 'rev-parse', '--verify', value + '^{commit}').decode().strip()


def references(repo, base=None, head=None, event=None, event_name=None):
    if event is None:
        if base is None:
            raise ValueError('--base est obligatoire hors événement GitHub')
        return commit(repo, base), commit(repo, head or git(repo, 'rev-parse', 'HEAD').decode().strip())
    if base or head:
        raise ValueError('Ne pas combiner événement et références explicites')
    if event_name == 'pull_request':
        head = commit(repo, event['pull_request']['head']['sha'])
        target = commit(repo, event['pull_request']['base']['sha'])
        base = git(repo, 'merge-base', target, head).decode().strip()
    elif event_name == 'push':
        if event.get('deleted'):
            raise ValueError('Suppression de branche : aucun état à analyser')
        base, head = event.get('before'), event.get('after')
    elif event_name == 'workflow_dispatch':
        base = event.get('inputs', {}).get('baseline')
        head = git(repo, 'rev-parse', 'HEAD').decode().strip()
    else:
        raise ValueError('Événement non pris en charge : ' + str(event_name))
    return commit(repo, base), commit(repo, head)


def snapshot(repo, sha, destination, targets):
    """Lire seulement les Java suivis des cibles, sans exécuter le code du commit."""
    archive = git(repo, 'archive', '--format=tar', sha, '--', *targets)
    manifest = {}
    with tarfile.open(fileobj=io.BytesIO(archive)) as stream:
        for member in stream:
            path = PurePosixPath(member.name)
            if path.is_absolute() or '..' in path.parts:
                raise ValueError('Chemin non sûr dans instantané')
            if any(part.startswith('._') for part in path.parts) or path.suffix != '.java':
                continue
            if not member.isfile():
                raise ValueError('Source Java non régulière : ' + str(path))
            data = stream.extractfile(member).read()
            target = destination / str(path)
            target.parent.mkdir(parents=True, exist_ok=True)
            target.write_bytes(data)
            manifest[str(path)] = hashlib.sha256(data).hexdigest()
    return manifest


def index_findings(report, root):
    groups = defaultdict(list)
    if report.get('errors') or report.get('skipped_rules'):
        raise ValueError('Rapport incomplet')
    for finding in report['results']:
        path = PurePosixPath(finding['path'])
        if path.is_absolute() or '..' in path.parts:
            raise ValueError('Chemin invalide dans résultat')
        data = (root / str(path)).read_bytes()
        start, end = finding['start']['offset'], finding['end']['offset']
        if not (0 <= start < end <= len(data)):
            raise ValueError('Positions invalides dans résultat')
        # Pas de numéro de ligne ni de normalisation des chaînes littérales.
        digest = hashlib.sha256(data[start:end].strip()).hexdigest()
        key = (finding['check_id'], str(path), digest)
        groups[key].append(finding)
    return groups


def compare(before, after, before_root, after_root):
    old, new = index_findings(before, before_root), index_findings(after, after_root)
    added, removed, retained = [], [], 0
    for key in sorted(old.keys() | new.keys()):
        shared = min(len(old[key]), len(new[key]))
        retained += shared
        added.extend(new[key][shared:])
        removed.extend(old[key][shared:])
    return {'added': added, 'removed': removed, 'retained': retained,
            'before_count': len(before['results']), 'after_count': len(after['results'])}


def write_json(path, value):
    path.write_text(json.dumps(value, indent=2, ensure_ascii=False) + '\n')


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--repo', type=Path, default=CONFIG.parent)
    parser.add_argument('--base')
    parser.add_argument('--head')
    parser.add_argument('--event-json', type=Path)
    parser.add_argument('--event-name')
    parser.add_argument('--output-dir', type=Path, required=True)
    parser.add_argument('--rules-dir', type=Path)
    args = parser.parse_args()
    output = args.output_dir.resolve()
    # Un répertoire neuf empêche de présenter un ancien rapport comme résultat courant.
    try:
        output.mkdir(parents=True, exist_ok=False)
    except OSError as error:
        print('Erreur technique : répertoire de sortie neuf requis : ' + str(error), file=sys.stderr)
        return 2
    summary = {'status': 'technical_error', 'exit_code': 2}
    try:
        event = json.loads(args.event_json.read_text()) if args.event_json else None
        base, head = references(args.repo, args.base, args.head, event, args.event_name)
        summary.update(base=base, head=head, event=args.event_name or 'local')
        targets = json.loads((CONFIG / 'scope.json').read_text())['targets']
        summary['configuration_sha256'] = {
            name: hashlib.sha256((CONFIG / name).read_bytes()).hexdigest()
            for name in ('scan.py', 'gate.py', 'scope.json', 'rules.lock.json', 'requirements.txt')}
        with tempfile.TemporaryDirectory(prefix='tika-security-') as temp:
            reports, roots = {}, {}
            for label, sha in (('before', base), ('after', head)):
                root = Path(temp) / label
                root.mkdir()
                roots[label] = root
                manifest = snapshot(args.repo, sha, root, targets)
                write_json(output / (label + '-manifest.json'), manifest)
                command = [sys.executable, str(CONFIG / 'scan.py'), '--source-root', str(root),
                           '--output', str(output / (label + '.json'))]
                if args.rules_dir:
                    command.extend(['--rules-dir', str(args.rules_dir.resolve())])
                env = dict(os.environ)
                env.update(SEMGREP_LOG_FILE=str(output / (label + '-engine.log')),
                           SEMGREP_SETTINGS_FILE=str(Path(temp) / 'settings.yml'))
                with (output / (label + '.log')).open('w') as log:
                    result = subprocess.run(command, env=env, stdout=log, stderr=subprocess.STDOUT)
                if result.returncode:
                    raise RuntimeError('Scan ' + label + ' en échec : code ' + str(result.returncode))
                reports[label] = json.loads((output / (label + '.json')).read_text())
            diff = compare(reports['before'], reports['after'], roots['before'], roots['after'])
            write_json(output / 'diff.json', diff)
            code = 1 if diff['added'] else 0
            summary.update(status='new_findings' if code else 'passed', exit_code=code,
                           added=len(diff['added']), removed=len(diff['removed']),
                           retained=diff['retained'], before_count=diff['before_count'],
                           after_count=diff['after_count'])
    except Exception as error:
        summary['error'] = str(error)
    write_json(output / 'summary.json', summary)
    lines = ['Contrôle Semgrep : ' + summary['status'],
             'Référence : ' + summary.get('base', '?'),
             'Version : ' + summary.get('head', '?')]
    if 'added' in summary:
        lines.append(f"Alertes : {summary['before_count']} → {summary['after_count']} ; "
                     f"{summary['added']} nouvelles, {summary['removed']} supprimées.")
        for finding in diff['added']:
            lines.append(f"- {finding['check_id']} : {finding['path']}:{finding['start']['line']}")
    if 'error' in summary:
        lines.append('Erreur technique : ' + summary['error'])
    message = '\n'.join(lines) + '\n'
    (output / 'summary.md').write_text(message)
    if os.environ.get('GITHUB_STEP_SUMMARY'):
        with open(os.environ['GITHUB_STEP_SUMMARY'], 'a') as stream:
            stream.write(message)
    print(message, end='')
    return summary['exit_code']


if __name__ == '__main__':
    sys.exit(main())
