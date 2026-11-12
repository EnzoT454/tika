import argparse
import hashlib
import json
import os
from pathlib import Path
import subprocess
import tarfile
import xml.etree.ElementTree as ET

p=argparse.ArgumentParser();p.add_argument('id');p.add_argument('--attempt',default='1');a=p.parse_args()
root=Path('/Volumes/TOSHIBA_EXT/ift3913/tika-tache3')
build=Path('/private/tmp/tache3-injections/build')
dest=root/'tache3-rapports/vulnerabilites'/a.id
log=dest/f'build-attempt-{a.attempt}.log'
assert not log.exists()
before={str(p.relative_to(build)):hashlib.sha256(p.read_bytes()).hexdigest() for p in (build/'tika-core/src').rglob('*.java')}
env=dict(os.environ,JAVA_HOME='/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home')
cmd=['./mvnw','-B','-ntp','-pl','tika-core','-am','clean','install']
with log.open('w') as stream:
    result=subprocess.run(cmd,cwd=build,env=env,stdout=stream,stderr=subprocess.STDOUT,timeout=600)
counts={}
for module in ('tika-parent','tika-annotation-processor','tika-core'):
    stats={k:0 for k in ('tests','failures','errors','skipped')}
    reports=list((build/module/'target/surefire-reports').glob('TEST-*.xml'))
    for report in reports:
        node=ET.parse(report).getroot()
        for key in stats: stats[key]+=int(node.get(key,0))
    check=build/module/'target/checkstyle-result.xml'
    counts[module]={'surefire':stats,'surefire_files':len(reports),'checkstyle_violations':len(ET.parse(check).getroot().findall('.//error')) if check.exists() else None}
changed=[name for name,h in before.items() if hashlib.sha256((build/name).read_bytes()).hexdigest()!=h]
with tarfile.open(dest/f'build-reports-{a.attempt}.tar.gz','w:gz') as archive:
    for module in counts:
        for name in ('surefire-reports','checkstyle-result.xml','site/jacoco'):
            f=build/module/'target'/name
            if f.exists(): archive.add(f,arcname=str(f.relative_to(build)))
record={'command':cmd,'exit_code':result.returncode,'modules':counts,'sources_changed_by_build':changed}
(dest/f'build-results-{a.attempt}.json').write_text(json.dumps(record,indent=2)+'\n')
print(json.dumps(record),flush=True)
if result.returncode: print('\n'.join(log.read_text().splitlines()[-15:]))
raise SystemExit(result.returncode)
