#!/usr/bin/env python3
"""Validation locale réelle, dans un dépôt Git jetable ; aucune modification de Tika."""
import argparse
import importlib.util
import json
from pathlib import Path
import shutil
import subprocess
import sys
import tempfile


HERE = Path(__file__).resolve().parent
spec = importlib.util.spec_from_file_location('gate', HERE / 'gate.py')
gate = importlib.util.module_from_spec(spec)
spec.loader.exec_module(gate)


def run(command, cwd=None):
    return subprocess.check_output(command, cwd=cwd, stderr=subprocess.STDOUT).decode().strip()


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output-dir', type=Path, required=True)
    parser.add_argument('--rules-dir', type=Path, required=True)
    args = parser.parse_args()
    output = args.output_dir.resolve()
    output.mkdir(parents=True, exist_ok=False)
    results = []
    with tempfile.TemporaryDirectory(prefix='tika-gate-validation-') as temp:
        repo = Path(temp) / 'repo'
        repo.mkdir()
        source_repo = HERE.parent
        source_sha = run(['git', '-C', str(source_repo), 'rev-parse', 'HEAD'])
        targets = json.loads((HERE / 'scope.json').read_text())['targets']
        manifest = gate.snapshot(source_repo, source_sha, repo, targets)
        gate.write_json(output / 'source-manifest.json', manifest)
        run(['git', 'init', '-b', 'validation'], repo)

        def commit(message):
            run(['git', 'add', '.'], repo)
            run(['git', '-c', 'user.name=Validation locale', '-c', 'user.email=validation@example.invalid',
                 '-c', 'commit.gpgsign=false', 'commit', '-m', message], repo)
            return run(['git', 'rev-parse', 'HEAD'], repo)

        def check(name, expected, base=None, head=None, event=None, event_name=None,
                  expected_added=None, expected_removed=None, script=None):
            command = [sys.executable, str(script or HERE / 'gate.py'), '--repo', str(repo),
                       '--output-dir', str(output / name), '--rules-dir', str(args.rules_dir)]
            if event is not None:
                path = output / (name + '-event.json')
                gate.write_json(path, event)
                command.extend(['--event-json', str(path), '--event-name', event_name])
            else:
                command.extend(['--base', base, '--head', head])
            result = subprocess.run(command, capture_output=True, text=True)
            (output / (name + '.log')).write_text(result.stdout + result.stderr)
            summary = json.loads((output / name / 'summary.json').read_text())
            assert result.returncode == expected, (name, result.returncode, summary)
            assert summary['exit_code'] == expected
            if expected_added is not None:
                assert summary['added'] == expected_added, (name, summary)
            if expected_removed is not None:
                assert summary['removed'] == expected_removed, (name, summary)
            results.append({'scenario': name, 'expected_exit_code': expected,
                            'actual_exit_code': result.returncode, 'summary': summary})
            gate.write_json(output / 'results.json', {'source_commit': source_sha,
                            'source_files': len(manifest), 'scenarios': results})
            print(name + ' : OK (' + str(result.returncode) + ')', flush=True)
            return summary

        baseline = commit('Sources initiales de validation')
        check('same', 0, baseline, baseline, expected_added=0)
        (repo / 'note.txt').write_text('Changement sans effet sur le Java.\n')
        benign = commit('Changement benin')
        check('benign', 0, baseline, benign, expected_added=0)
        loader = repo / 'tika-core/src/main/java/org/apache/tika/config/ServiceLoader.java'
        loader.write_bytes(b'// Decalage de lignes de validation.\n\n' + loader.read_bytes())
        shifted = commit('Deplacement des lignes')
        check('line-shift', 0, baseline, shifted, expected_added=0)
        fixture = repo / 'tika-core/src/main/java/org/apache/tika/GateValidation.java'
        fixture.write_text('''package org.apache.tika;
class GateValidation {
    void execute(String input) throws java.io.IOException {
        new ProcessBuilder(input).start();
    }
}
''')
        vulnerable = commit('Alerte temporaire jamais executee')
        summary = check('new-finding', 1, baseline, vulnerable, expected_added=1)
        assert summary['before_count'] == 17 and summary['after_count'] == 18
        fixture.write_text(fixture.read_text().replace('    void execute(', '''    void duplicate(String input) throws java.io.IOException {
        new ProcessBuilder(input).start();
    }
    void execute('''))
        duplicate = commit('Deuxieme occurrence identique')
        check('duplicate-occurrence', 1, vulnerable, duplicate, expected_added=1)
        # Revenir au contenu à une seule occurrence, sans reset destructif.
        fixture.write_bytes(gate.git(repo, 'show', vulnerable + ':' + str(fixture.relative_to(repo))))
        report = json.loads((output / 'new-finding' / 'before.json').read_text())
        socket = next(f for f in report['results'] if f['check_id'] == 'unencrypted-socket')
        removed_path = repo / socket['path']
        removed_data = removed_path.read_bytes()
        removed_path.unlink()
        equal = commit('Suppression ancienne alerte et ajout nouveau a total constant')
        summary = check('equal-count', 1, baseline, equal, expected_added=1, expected_removed=1)
        assert summary['before_count'] == summary['after_count'] == 17
        removed_path.write_bytes(removed_data)
        fixture.unlink()
        corrected = commit('Correction de la modification temporaire')
        check('correction', 0, vulnerable, corrected, expected_added=0, expected_removed=1)
        check('push-multiple-commits', 1,
              event={'before': baseline, 'after': duplicate, 'deleted': False},
              event_name='push', expected_added=2)
        # La cible PR a avance avec un changement benin apres divergence.
        target_tree = run(['git', 'rev-parse', benign + '^{tree}'], repo)
        target = run(['git', '-c', 'user.name=Validation locale',
                      '-c', 'user.email=validation@example.invalid', '-c', 'commit.gpgsign=false',
                      'commit-tree', target_tree, '-p', baseline, '-m', 'Cible divergente'], repo)
        run(['git', 'update-ref', 'refs/heads/pr-target', target], repo)
        summary = check('pull-request', 1,
                        event={'pull_request': {'base': {'sha': target}, 'head': {'sha': vulnerable}}},
                        event_name='pull_request', expected_added=1)
        assert summary['base'] == baseline and summary['head'] == vulnerable
        check('missing-reference', 2, 'f' * 40, corrected)
        check('first-push', 2, event={'before': '0' * 40, 'after': corrected}, event_name='push')
        # Le declenchement manuel exige une reference explicite.
        check('manual', 0, event={'inputs': {'baseline': baseline}},
              event_name='workflow_dispatch', expected_added=0)
        invalid = Path(temp) / 'invalid-config'
        invalid.mkdir()
        for name in ('gate.py', 'scan.py', 'scope.json', 'rules.lock.json', 'requirements.txt'):
            shutil.copy2(HERE / name, invalid / name)
        (invalid / 'scope.json').write_text('{configuration invalide')
        check('invalid-configuration', 2, baseline, corrected, script=invalid / 'gate.py')
        # Un scan qui produit des erreurs de syntaxe ne doit pas passer.
        fixture.write_text('package org.apache.tika; class GateValidation { void x( { !!!\n')
        broken = commit('Source syntaxiquement invalide')
        check('incomplete-scan', 2, baseline, broken)
        run(['git', 'bundle', 'create', str(output / 'fixtures.bundle'), '--all'], repo)
    return 0


if __name__ == '__main__':
    sys.exit(main())
