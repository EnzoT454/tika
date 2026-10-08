#!/usr/bin/env python3
"""Verifier le YAML, les commandes shell et les refus du controleur Surefire."""
import copy
import json
from pathlib import Path
import shutil
import subprocess
import tempfile
import xml.etree.ElementTree as ET

import yaml

ROOT = Path(__file__).resolve().parents[3]
OUT = Path(__file__).resolve().parent


def main():
    workflow = yaml.safe_load((ROOT/'.github/workflows/tache2-ift3913.yml').read_text())
    assert set(workflow['on']) == {'push', 'pull_request', 'workflow_dispatch'}
    assert workflow['on']['push']['branches'] == ['main', 'tache2-final']
    assert workflow['on']['pull_request']['branches'] == ['main', 'tache2-final']
    assert workflow['permissions'] == {'contents': 'read'}
    job = workflow['jobs']['tests-et-mutation']
    assert job['runs-on'] == 'ubuntu-latest'
    runs = [s['run'] for s in job['steps'] if 'run' in s]
    for run in runs:
        subprocess.run(['bash', '-n'], input=run, text=True, check=True)
        assert not any(x in run for x in ['-DskipTests', '-Pfast', '-Dcheckstyle.skip',
            '-Drat.skip', '-Dforbiddenapis.skip', '-Dossindex.skip', '-Dtest='])
    assert any('-am clean install' in run for run in runs)
    assert any('-Pmutation org.pitest:pitest-maven:mutationCoverage' in run for run in runs)
    assert all('continue-on-error' not in step for step in job['steps'])
    assert all('set -euo pipefail' in run for run in runs if './mvnw' in run)
    reports = OUT.parent/'etape-05/C/tika-core/surefire-reports'
    work = Path(tempfile.mkdtemp(prefix='tika-ci-verifier-', dir='/private/tmp'))
    shutil.copytree(reports, work/'reports')
    inventory = json.loads((ROOT/'.github/scripts/tache2-expected-tests.json').read_text())
    selected = next(iter(inventory['classes']))
    report = work/'reports'/('TEST-'+selected+'.xml')
    original = report.read_bytes()
    script = ROOT/'.github/scripts/check-tache2-tests.py'
    results = []
    def run_case(name, expected_exit):
        result = subprocess.run(['python3', str(script), str(work/'reports')],
                                text=True, capture_output=True)
        assert result.returncode == expected_exit, (name, result.returncode, result.stderr)
        results.append({'scenario': name, 'expected_exit': expected_exit,
                        'exit_code': result.returncode, 'stderr': result.stderr})
    run_case('all_217_accepted_cases_pass', 0)
    for scenario in ['missing_report', 'missing_case', 'renamed_case', 'duplicated_case',
                     'skipped_case', 'failed_case', 'tampered_suite_counter']:
        report.write_bytes(original)
        suite = ET.fromstring(original)
        case = suite.find('testcase')
        if scenario == 'missing_report':
            report.unlink()
        else:
            if scenario == 'missing_case':
                suite.remove(case); suite.set('tests', str(int(suite.get('tests'))-1))
            elif scenario == 'renamed_case':
                case.set('name', 'unexpectedCase')
            elif scenario == 'duplicated_case':
                suite.append(copy.deepcopy(case)); suite.set('tests', str(int(suite.get('tests'))+1))
            elif scenario == 'skipped_case':
                ET.SubElement(case, 'skipped'); suite.set('skipped', '1')
            elif scenario == 'failed_case':
                ET.SubElement(case, 'failure'); suite.set('failures', '1')
            elif scenario == 'tampered_suite_counter':
                suite.set('tests', '0')
            ET.ElementTree(suite).write(report, encoding='utf-8', xml_declaration=True)
        run_case(scenario, 1)
    report.write_bytes(original)
    (OUT/'verifier-validation.json').write_text(json.dumps({
        'yaml_parsed': True, 'shell_syntax_valid': True,
        'no_quality_or_test_skip_flags': True, 'scenarios': results,
        'reports_are_temporary_copies': True}, indent=2)+'\n')
    print('YAML et shell valides ; 217 cas acceptes verifies ; sept anomalies rejetees.')


if __name__ == '__main__':
    main()
