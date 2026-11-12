import argparse
import difflib
import hashlib
import importlib.util
import json
import os
from pathlib import Path
import shutil
import subprocess
import sys
import tarfile

ROOT = Path('/Volumes/TOSHIBA_EXT/ift3913/tika-tache3')
TEMP = Path('/private/tmp/tache3-injections')
PROOF = ROOT / 'tache3-rapports/vulnerabilites'
spec = importlib.util.spec_from_file_location('gate', ROOT/'semgrep/gate.py')
gate = importlib.util.module_from_spec(spec)
spec.loader.exec_module(gate)

def manifest(folder):
    scope=json.loads((ROOT/'semgrep/scope.json').read_text())['targets']
    return {str(p.relative_to(folder)):hashlib.sha256(p.read_bytes()).hexdigest()
            for target in scope for p in (folder/target).rglob('*.java')
            if not any(x.startswith('._') for x in p.parts)}

parser=argparse.ArgumentParser()
parser.add_argument('id')
parser.add_argument('--rule')
parser.add_argument('--file')
args=parser.parse_args()
if args.id=='prepare':
    TEMP.mkdir(exist_ok=False)
    old=json.loads((ROOT/'tache3-rapports/semgrep-avant/source-manifest.json').read_text())
    assert old == manifest(ROOT)
    for label in ('initial','current'):
        for name in old:
            target=TEMP/label/name; target.parent.mkdir(parents=True,exist_ok=True)
            shutil.copyfile(ROOT/name,target)
    shutil.copyfile(ROOT/'tache3-rapports/semgrep-avant/semgrep.json',TEMP/'previous.json')
    build=TEMP/'build'
    files=subprocess.check_output(['git','ls-files','-z'],cwd=ROOT).decode().split('\0')
    count=0
    for name in files:
        p=Path(name)
        if not name or any(x.startswith('._') for x in p.parts): continue
        if not (len(p.parts)==1 or p.parts[0] in {'.mvn','tika-parent','tika-core','tika-annotation-processor'} or p.name=='pom.xml'): continue
        target=build/p; target.parent.mkdir(parents=True,exist_ok=True)
        shutil.copyfile(ROOT/p,target); count+=1
    (build/'mvnw').chmod(0o755)
    print(json.dumps({'sources':len(old),'build_files':count,'temp':str(TEMP)}))
    sys.exit(0)

dest=PROOF/args.id
dest.mkdir(parents=True,exist_ok=False)
before=TEMP/('before-'+args.id)
shutil.copytree(TEMP/'current',before)
current=TEMP/'current'
names=manifest(ROOT)
for name in names:
    shutil.copyfile(ROOT/name,current/name)
    if name.startswith('tika-core/'):
        shutil.copyfile(ROOT/name,TEMP/'build'/name)
env=dict(os.environ,SEMGREP_LOG_FILE=str(dest/'engine.log'),
         SEMGREP_SETTINGS_FILE=str(TEMP/'settings.yml'))
cmd=[sys.executable,str(ROOT/'semgrep/scan.py'),'--source-root',str(current),
     '--rules-dir','/private/tmp/tache3-semgrep-rule-cache','--output',str(dest/'semgrep.json')]
with (dest/'scan.log').open('w') as log:
    result=subprocess.run(cmd,env=env,stdout=log,stderr=subprocess.STDOUT)
assert result.returncode==0,(args.id,result.returncode)
report=json.loads((dest/'semgrep.json').read_text())
previous=json.loads((TEMP/'previous.json').read_text())
initial=json.loads((ROOT/'tache3-rapports/semgrep-avant/semgrep.json').read_text())
incremental=gate.compare(previous,report,before,current)
cumulative=gate.compare(initial,report,TEMP/'initial',current)
assert any(f['check_id']==args.rule and f['path']==args.file for f in incremental['added']), (args.id,[(f['check_id'],f['path']) for f in incremental['added']])
for name,data in [('incremental.json',incremental),('cumulative.json',cumulative),('source-manifest.json',names)]:
    (dest/name).write_text(json.dumps(data,indent=2,ensure_ascii=False)+'\n')
source=ROOT/args.file
(dest/'change.patch').write_text(''.join(difflib.unified_diff((TEMP/'initial'/args.file).read_text().splitlines(True),source.read_text().splitlines(True),fromfile='a/'+args.file,tofile='b/'+args.file)))
with tarfile.open(dest/'sources.tar.gz','w:gz') as archive:
    for name in names: archive.add(current/name,arcname=name)
    for name in ('LICENSE.txt','NOTICE.txt'): archive.add(ROOT/name,arcname=name)
record={'id':args.id,'rule':args.rule,'file':args.file,'scan_exit_code':0,
        'scanned':len(report['paths']['scanned']),'findings':len(report['results']),
        'incremental_added':len(incremental['added']),'incremental_removed':len(incremental['removed']),
        'cumulative_added':len(cumulative['added']),'cumulative_removed':len(cumulative['removed']),
        'current_source_sha256':hashlib.sha256(source.read_bytes()).hexdigest(),
        'base_commit':subprocess.check_output(['git','rev-parse','HEAD'],cwd=ROOT).decode().strip(),
        'introduction_commit':None,'github_run':None}
(dest/'results.json').write_text(json.dumps(record,indent=2)+'\n')
shutil.copyfile(dest/'semgrep.json',TEMP/'previous.json')
print(json.dumps(record),flush=True)
