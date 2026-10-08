#!/usr/bin/env python3
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""Verifier les nouveaux cas acceptes et toute la suite core.

Adaptation du verificateur Hamza avec un inventaire fige independant de
l'execution a verifier : 161 cas IA et 56 manuels dans 43 classes.
"""
import argparse
from collections import Counter
import json
import os
from pathlib import Path
import sys
import xml.etree.ElementTree as ET


def check(directory):
    expected = json.loads(Path(__file__).with_name('tache2-expected-tests.json').read_text())
    counts = Counter()
    for definition in expected['classes'].values():
        counts[definition['role']] += len(definition['cases'])
    if dict(counts) != expected['counts'] or counts != {'ai': 161, 'manual': 56}:
        raise ValueError('Inventaire des 217 cas incoherent')
    suites = {}
    totals = Counter()
    skipped = Counter()
    for report in sorted(directory.glob('TEST-*.xml')):
        suite = ET.parse(report).getroot()
        name = suite.get('name')
        if suite.tag != 'testsuite' or not name or name in suites:
            raise ValueError(f'Suite incorrecte ou dupliquee : {report}')
        cases = suite.findall('testcase')
        actual = Counter(tests=len(cases), failures=0, errors=0, skipped=0)
        for case in cases:
            if case.get('classname') != name or not case.get('name'):
                raise ValueError(f'Identite de cas incorrecte : {report}')
            for status, tag in [('failures', 'failure'), ('errors', 'error'), ('skipped', 'skipped')]:
                if case.find(tag) is not None:
                    actual[status] += 1
                    if tag == 'skipped':
                        skipped[(name, case.get('name'))] += 1
        for key in ['tests', 'failures', 'errors', 'skipped']:
            if int(suite.attrib[key]) != actual[key]:
                raise ValueError(f'Compteur {key} incoherent : {report}')
        if actual['failures'] or actual['errors']:
            raise ValueError(f'Test echoue : {report}')
        suites[name] = cases
        totals.update(actual)
    rows = []
    for name, definition in expected['classes'].items():
        if name not in suites:
            raise ValueError(f'Rapport attendu absent : {name}')
        cases = suites[name]
        if Counter(c.get('name') for c in cases) != Counter(definition['cases']):
            raise ValueError(f'Cas absent, inattendu ou duplique : {name}')
        if any(c.find('skipped') is not None for c in cases):
            raise ValueError(f'Nouveau cas desactive : {name}')
        rows.append(f"| {name.split('.')[-1]} | {definition['role']} | {len(cases)} | Reussis |")
    allowed = Counter((name, case) for name, cases in expected['allowed_skipped'].items() for case in cases)
    if skipped != allowed:
        raise ValueError(f'Desactivations differentes de la reference : {dict(skipped)}')
    if totals['tests'] != expected['core_tests']:
        raise ValueError(f"Nombre total core : {totals['tests']}, attendu {expected['core_tests']}")
    summary = '\n'.join([
        '### Verification des tests de la tache 2', '',
        '161 cas IA et 56 cas manuels reussis, aucun nouveau cas desactive.',
        'Suite core : 966 cas, 964 reussis, deux desactivations preexistantes.', '',
        '| Classe | Origine | Cas | Resultat |', '|---|---|---:|---|', *rows, ''])
    print(summary)
    if os.environ.get('GITHUB_STEP_SUMMARY'):
        with open(os.environ['GITHUB_STEP_SUMMARY'], 'a', encoding='utf-8') as output:
            output.write(summary+'\n')
    return {'core': dict(totals), 'accepted': dict(counts), 'classes': len(rows)}


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('reports', nargs='?', type=Path, default=Path('tika-core/target/surefire-reports'))
    try:
        check(parser.parse_args().reports)
    except (OSError, ET.ParseError, ValueError, KeyError) as error:
        print(f'Verification echouee : {error}', file=sys.stderr)
        sys.exit(1)
