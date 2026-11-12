#!/usr/bin/env python3
"""Identifier les cas JUnit echoues du build courant ; aucune lecture des anciens rapports."""
import argparse
import json
import os
from pathlib import Path
import sys
import time
import xml.etree.ElementTree as ET


MODULES = ('tika-annotation-processor', 'tika-core')


def evaluate(root, started_ns, outcome):
    totals = dict(tests=0, failures=0, errors=0, skipped=0)
    reports, stale, modules = [], [], set()
    for module in MODULES:
        for path in sorted((root / module / 'target/surefire-reports').glob('TEST-*.xml')):
            if path.name.startswith('._'):
                continue
            name = str(path.relative_to(root))
            if path.stat().st_mtime_ns < started_ns:
                stale.append(name)
                continue
            suite = ET.parse(path).getroot()
            if suite.tag != 'testsuite':
                raise ValueError('Racine Surefire incorrecte : ' + name)
            cases = suite.findall('testcase')
            counts = dict(tests=len(cases), failures=0, errors=0, skipped=0)
            for case in cases:
                states = [tag for tag in ('failure', 'error', 'skipped') if case.find(tag) is not None]
                if len(states) > 1:
                    raise ValueError('Statuts JUnit incompatibles : ' + name)
                for tag in states:
                    counts[{'failure':'failures', 'error':'errors', 'skipped':'skipped'}[tag]] += 1
            for key, value in counts.items():
                if int(suite.attrib[key]) != value:
                    raise ValueError('Compteur Surefire incoherent : ' + name)
                totals[key] += value
            reports.append(name)
            modules.add(module)
    failed = totals['failures'] + totals['errors'] > 0
    if outcome == 'success' and (modules != set(MODULES) or failed or totals['tests'] == 0):
        raise ValueError('Build annonce reussi mais rapports absents ou tests echoues')
    return {'status':'test_failure' if failed else ('passed' if outcome == 'success' else 'build_failure_without_failed_test'),
            'build_outcome':outcome, 'has_failures':failed, 'totals':totals,
            'reports':reports, 'stale_reports_ignored':stale}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--root', type=Path, default=Path.cwd())
    parser.add_argument('--start', action='store_true')
    parser.add_argument('--build-outcome', choices=('success', 'failure'))
    args = parser.parse_args()
    root = args.root.resolve()
    state = root / 'tache3-test-state'
    state.mkdir(exist_ok=True)
    marker = state / 'started.json'
    if args.start:
        marker.write_text(json.dumps({'started_ns':time.time_ns()}) + '\n')
        return 0
    result = {'status':'technical_error', 'has_failures':False}
    code = 0
    try:
        if not args.build_outcome:
            raise ValueError('--build-outcome obligatoire')
        started_ns = json.loads(marker.read_text())['started_ns']
        result = evaluate(root, started_ns, args.build_outcome)
    except (OSError, ET.ParseError, ValueError, KeyError, TypeError) as error:
        result['error'] = str(error)
        code = 2
    (state / 'results.json').write_text(json.dumps(result, indent=2) + '\n')
    if os.environ.get('GITHUB_OUTPUT'):
        with open(os.environ['GITHUB_OUTPUT'], 'a') as stream:
            stream.write('has_failures=' + str(result['has_failures']).lower() + '\n')
    print(json.dumps(result))
    return code


if __name__ == '__main__':
    sys.exit(main())
