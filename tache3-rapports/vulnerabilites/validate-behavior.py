#!/usr/bin/env python3
"""Vérifications bornées avant/après, sur les classes des copies Maven internes."""
import argparse
import json
from pathlib import Path
import subprocess
import tempfile
import xml.etree.ElementTree as ET

p = argparse.ArgumentParser(description=__doc__)
p.add_argument('--before', type=Path, required=True)
p.add_argument('--after', type=Path, required=True)
p.add_argument('--java-home', type=Path, required=True)
p.add_argument('--output', type=Path, required=True)
p.add_argument('--only-v6', action='store_true', help='Ne lancer que trois cas V6 courts et sans réseau')
a = p.parse_args()
a.output.mkdir(parents=True, exist_ok=False)
source = Path(__file__).resolve().parent / 'InjectionProbe.java'
xml = next((a.after / 'tika-core/target/surefire-reports').glob('TEST-*.xml'))
classpath = ET.parse(xml).find("properties/property[@name='java.class.path']").get('value')
results = {}
with tempfile.TemporaryDirectory(prefix='tika-bounded-probes-') as temporary:
    work = Path(temporary)
    command = [str(a.java_home / 'bin/javac'), '-cp', classpath, '-d', str(work), str(source)]
    compile_result = subprocess.run(command, capture_output=True, text=True, timeout=60)
    (a.output / 'javac.log').write_text(compile_result.stdout + compile_result.stderr)
    assert compile_result.returncode == 0
    for label, build in (('before', a.before), ('after', a.after)):
        cp = classpath.replace(str(a.after), str(build))
        command = [str(a.java_home / 'bin/java'), '-cp', str(work) + ':' + cp,
                   'InjectionProbe', str(work / label)]
        if a.only_v6:
            command.append('--v6-only')
        result = subprocess.run(command, capture_output=True, text=True, timeout=30)
        (a.output / (label + '.log')).write_text(result.stdout + result.stderr)
        assert result.returncode == 0, (label, result.stdout, result.stderr)
        selected = (6,) if a.only_v6 else range(1, 8)
        values = dict(line.split('=', 1) for line in result.stdout.splitlines()
                      if line.startswith(tuple('V' + str(i) + '=' for i in selected)))
        results[label] = values
        (a.output / 'results.json').write_text(json.dumps(results, indent=2) + '\n')
        assert values == {'V' + str(i): 'false' if label == 'before' else 'true' for i in selected}, (label, values)
        if a.only_v6:
            diagnostics = dict(line.split('=', 1) for line in result.stdout.splitlines()
                               if line.startswith('V6_VALIDATION_'))
            assert diagnostics == {'V6_VALIDATION_LF_COUNT': '2', 'V6_VALIDATION_LINE_COUNT': '2',
                                   'V6_VALIDATION_NEGATIVE': 'false', 'V6_VALIDATION_LITERAL': 'false'}, diagnostics
print(json.dumps(results), flush=True)
