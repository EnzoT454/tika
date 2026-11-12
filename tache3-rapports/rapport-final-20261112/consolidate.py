#!/usr/bin/env python3
"""Consolider les preuves GitHub déjà téléchargées ; aucun appel réseau ou Java."""
from collections import Counter
import hashlib
import json
from pathlib import Path
import shutil
import subprocess
import xml.etree.ElementTree as ET

OUT = Path(__file__).resolve().parent
ROOT = OUT.parents[1]


def sha(raw):
    return hashlib.sha256(raw).hexdigest()


def write(name, value):
    p = OUT/name
    assert not p.exists(), str(p)
    p.write_text(json.dumps(value, indent=2, ensure_ascii=False)+'\n')


context_paths=['readme.md','AGENTS.md','tache3.md','.github/actions/rickroll/README.md',
               *[f'tache3-rapports/vulnerabilites/V{i}/README.md' for i in range(1,8)]]
for name in context_paths:
    target=OUT/'context-before'/name
    assert not target.exists()
    target.parent.mkdir(parents=True,exist_ok=True)
    shutil.copyfile(ROOT/name,target)
shutil.copyfile(ROOT/'readme.md',OUT/'readme-tache2-avant.md')
old_manifest=ROOT/'tache3-rapports/rickroll/etape-07-20261112/checksums.json'
expected=json.loads(old_manifest.read_text())
mismatches=[n for n,h in expected.items() if sha((ROOT/n).read_bytes())!=h]
write('prior-integrity.json',{'files_checked':len(expected),'mismatches':mismatches})
assert not mismatches,mismatches

cases={43276322587:'tests-reussis',43276898487:'echec-junit',
       43277596512:'echec-compilation',43278025406:'correction'}
records=[]
for run_id,label in cases.items():
    folder=ROOT/'tache3-rapports/rickroll/github'/str(run_id)
    run=json.loads((folder/'run.json').read_text())
    jobs={j['name']:j for j in run['jobs']}
    assert jobs['securite']['conclusion']=='success'
    semgrep=json.loads((folder/f'semgrep-{run_id}-1/summary.json').read_text())
    assert semgrep['exit_code']==0 and semgrep['added']==0
    state=json.loads((folder/f'construction-{run_id}-1/tache3-test-state/results.json').read_text())
    rick=next(s for s in jobs['construction']['steps'] if s['name']=='Rickroll apres un cas JUnit echoue')
    log=(folder/'run.log').read_text()
    counts=dict(tests=0,failures=0,errors=0,skipped=0)
    xmls=[p for p in folder.glob('construction-*/**/TEST-*.xml') if not p.name.startswith('._')]
    for p in xmls:
        suite=ET.parse(p).getroot()
        for key in counts:counts[key]+=int(suite.get(key,0))
    assert counts==state['totals']
    if label=='echec-junit':
        assert state['has_failures'] and counts['failures']==1 and counts['errors']==0
        assert rick['conclusion']=='success' and log.count('[FRAME ')==6
        assert 'YOU GOT RICKROLLED!' in log and 'Process completed with exit code 1' in log
        assert jobs['construction']['conclusion']==run['conclusion']=='failure'
    elif label=='echec-compilation':
        assert not state['has_failures'] and counts['tests']==8
        assert 'incompatible types' in log
        assert rick['conclusion']=='skipped' and '[FRAME ' not in log
        assert jobs['construction']['conclusion']==run['conclusion']=='failure'
    else:
        assert not state['has_failures'] and counts==dict(tests=974,failures=0,errors=0,skipped=2)
        assert rick['conclusion']=='skipped' and '[FRAME ' not in log
        assert jobs['construction']['conclusion']==run['conclusion']=='success'
    artifacts_step=next(s for s in jobs['construction']['steps'] if s['name']=='Conserver les resultats de construction')
    assert artifacts_step['conclusion']=='success'
    records.append({'case':label,'run_id':run_id,'url':run['url'],'head_sha':run['headSha'],
                    'conclusion':run['conclusion'],'security_conclusion':jobs['securite']['conclusion'],
                    'rickroll_step':rick['conclusion'],'frames':log.count('[FRAME '),
                    'surefire':counts,'status':state['status'],
                    'evidence':str(folder.relative_to(ROOT))})
write('rickroll-results.json',records)

run_id=43278937793
folder=ROOT/'tache3-rapports/validation-ci/github'/str(run_id)
run=json.loads((folder/'run.json').read_text())
summary=json.loads((folder/f'semgrep-{run_id}-1/summary.json').read_text())
assert summary['status']=='new_findings' and summary['exit_code']==1
assert (summary['before_count'],summary['after_count'],summary['added'],summary['removed'],summary['retained'])==(17,24,8,1,16)
local=ROOT/'tache3-rapports/comparaisons/etape-06-20261112'
for phase in ('before','after'):
    remote_manifest=json.loads((folder/f'semgrep-{run_id}-1/{phase}-manifest.json').read_text())
    assert remote_manifest==json.loads((local/f'{phase}-manifest.json').read_text())
    a=json.loads((folder/f'semgrep-{run_id}-1/{phase}.json').read_text())
    b=json.loads((local/f'{phase}-semgrep.json').read_text())
    key=lambda f:(f['check_id'],f['path'],f['start']['offset'],f['end']['offset'])
    assert Counter(map(key,a['results']))==Counter(map(key,b['results']))
    assert not a.get('errors') and not a.get('skipped_rules') and len(a['paths']['scanned'])==682
    assert a['version']=='1.185.0'
remote_diff=json.loads((folder/f'semgrep-{run_id}-1/diff.json').read_text())
local_add=json.loads((local/'added.json').read_text())
key=lambda f:(f['check_id'],f['path'],f['start']['offset'],f['end']['offset'])
assert Counter(map(key,remote_diff['added']))==Counter(key(a['finding']) for a in local_add)
jobs={j['name']:j for j in run['jobs']}
assert run['conclusion']=='failure' and jobs['securite']['conclusion']=='failure' and jobs['construction']['conclusion']=='skipped'
assert 'Process completed with exit code 1' in (folder/'run.log').read_text()
associations=json.loads((local/'associations.json').read_text())
intro='5d37388b0cbf1baeb19c04a2e1c0ed633753765c'
for row in associations:
    result=subprocess.run(['git','show',intro+':'+row['file']],cwd=ROOT,capture_output=True,check=True)
    current=(ROOT/row['file']).read_bytes()
    assert result.stdout==current
    row.update(introduction_commit=intro,github_run=run_id,github_url=run['url'])
write('vulnerability-provenance.json',associations)
write('semgrep-results.json',{'run_id':run_id,'url':run['url'],'conclusion':run['conclusion'],
                             'summary':summary,'matches_local_source_manifests':True,
                             'matches_local_findings':True,'construction':'skipped'})

matrix=json.loads((local/'validation-matrix.json').read_text())
updates={
'sept-injections-blocage-integre':('reussie','Semgrep code 1, 17 → 24, +8/−1, construction ignorée',run['url']),
'rickroll-apres-tests-reussis':('reussie','Aucun rickroll et job réussi ; retour au vert également confirmé',records[0]['url']),
'rickroll-apres-test-junit-echoue':('reussie','Un cas JUnit échoué ; six poses, action réussie et job en échec',records[1]['url']),
'compilation-echouee-sans-echec-junit':('reussie','Erreur javac, rickroll ignoré, job échoué',records[2]['url'])}
for item in matrix:
    if item['scenario'] in updates:
        state,observation,url=updates[item['scenario']]
        item.update(status=state,scope=observation,url=url)
        item['evidence']='rickroll-results.json' if item['scenario']!='sept-injections-blocage-integre' else 'semgrep-results.json'
        if item['scenario']=='sept-injections-blocage-integre':item['observed_exit_code']=1
for item in matrix:
    evidence=item.get('evidence','')
    if evidence=='associations.json':item['evidence']='vulnerability-provenance.json'
    elif evidence.startswith('evidence/'):item['evidence']='../comparaisons/etape-06-20261112/'+evidence
    elif 'consolidation-20261112/matrix.json' in evidence:item['evidence']='../vulnerabilites/consolidation-20261112/matrix.json'
write('validation-matrix.json',matrix)
write('verification.json',{'github_runs_checked':5,'rickroll_cases_checked':4,'semgrep_final_confirmed':True,
                           'v6_behavior':'incomplete, unchanged','all_source_manifests_match':True,
                           'tests_executed_during_consolidation':False,'network_access':False,
                           'matrix_statuses':dict(Counter(r['status'] for r in matrix)),
                           'task2_report_backup_sha256':sha((OUT/'readme-tache2-avant.md').read_bytes())})
print(json.dumps({'runs_checked':5,'rickroll_cases':4,'semgrep_added':8,'semgrep_removed':1,'semgrep_retained':16,
                  'matrix_statuses':dict(Counter(r['status'] for r in matrix))}))
