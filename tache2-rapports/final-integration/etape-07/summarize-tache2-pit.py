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

"""Resumer PIT avec KILLED/total et les timeouts separes."""
import argparse
from collections import Counter, defaultdict
import os
from pathlib import Path
import sys
import xml.etree.ElementTree as ET

TARGETS = {'org.apache.tika.io.EndianUtils', 'org.apache.tika.io.FilenameUtils',
           'org.apache.tika.mime.MediaType'}


def summarize(path):
    classes = defaultdict(Counter)
    for mutant in ET.parse(path).getroot().findall('mutation'):
        classes[mutant.findtext('mutatedClass')][mutant.attrib['status']] += 1
    if set(classes) != TARGETS:
        raise ValueError(f'Classes PIT inattendues ou absentes : {sorted(classes)}')
    total = Counter()
    for counts in classes.values():
        total.update(counts)
    lines = ['### Mutation : statuts separes', '',
             'Score = 100 x KILLED / total ; les timeouts sont comptes separement.', '',
             '| Classe | Total | KILLED | TIMED_OUT | SURVIVED | NO_COVERAGE | Autres | Score |',
             '|---|---:|---:|---:|---:|---:|---:|---:|---:|']
    for name, counts in list(sorted(classes.items())) + [('Total', total)]:
        size = sum(counts.values())
        other = sum(v for k,v in counts.items() if k not in ['KILLED','TIMED_OUT','SURVIVED','NO_COVERAGE'])
        lines.append(f"| {name.split('.')[-1]} | {size} | {counts['KILLED']} | {counts['TIMED_OUT']} | "
                     f"{counts['SURVIVED']} | {counts['NO_COVERAGE']} | {other} | {100*counts['KILLED']/size:.2f} % |")
    summary = '\n'.join(lines)+'\n'
    print(summary)
    if os.environ.get('GITHUB_STEP_SUMMARY'):
        with open(os.environ['GITHUB_STEP_SUMMARY'], 'a', encoding='utf-8') as output:
            output.write(summary)


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('mutations', nargs='?', type=Path, default=Path('tika-core/target/pit-reports/mutations.xml'))
    try:
        summarize(parser.parse_args().mutations)
    except (OSError, ET.ParseError, ValueError, KeyError) as error:
        print(f'Resume PIT impossible : {error}', file=sys.stderr)
        sys.exit(1)
