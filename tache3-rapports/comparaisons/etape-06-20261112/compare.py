#!/usr/bin/env python3
"""Comparer les preuves archivées, sans scanner ni exécuter de code Java."""
from collections import Counter, defaultdict
import hashlib
import json
from pathlib import Path
import shutil
import tarfile

OUT = Path(__file__).resolve().parent
ROOT = OUT.parents[2]
VULNS = ROOT / 'tache3-rapports/vulnerabilites'


def sha(data):
    return hashlib.sha256(data).hexdigest()


def write(name, value):
    p = OUT / name
    if p.exists():
        raise RuntimeError('Sortie déjà présente : ' + str(p))
    p.write_text(json.dumps(value, indent=2, ensure_ascii=False) + '\n')


def copy(source, name):
    p = OUT / name
    if p.exists():
        raise RuntimeError('Ne pas écraser : ' + str(p))
    p.parent.mkdir(parents=True, exist_ok=True)
    shutil.copyfile(source, p)


old_manifest = json.loads((VULNS/'consolidation-20261112/checksums.json').read_text())
old_mismatch = [name for name,h in old_manifest.items() if sha((ROOT/name).read_bytes()) != h]
assert not old_mismatch, old_mismatch
write('prior-integrity.json', {'checked_files':len(old_manifest), 'mismatches':old_mismatch,
                              'time_scope':'before updating guides for step 6'})
for name in ('AGENTS.md', 'tache3.md'):
    copy(ROOT/name, 'context-before/'+name)

initial_dir = ROOT / 'tache3-rapports/semgrep-avant'
inputs = {}
for label, folder, report in (('before', initial_dir, 'semgrep.json'), ('after', VULNS/'V7', 'semgrep.json')):
    copy(folder/report, label+'-semgrep.json')
    copy(folder/'source-manifest.json', label+'-manifest.json')
    copy(folder/'sources.tar.gz', label+'-sources.tar.gz')
    expected = json.loads((folder/'source-manifest.json').read_text())
    with tarfile.open(folder/'sources.tar.gz') as archive:
        data = {m.name:archive.extractfile(m).read() for m in archive if m.isfile() and m.name.endswith('.java')}
    assert {name:sha(raw) for name,raw in data.items()} == expected
    raw_report = json.loads((folder/report).read_text())
    assert raw_report['version']=='1.185.0' and not raw_report.get('errors') and not raw_report.get('skipped_rules')
    assert set(raw_report['paths']['scanned'])==set(expected)
    groups = defaultdict(list)
    for finding in raw_report['results']:
        raw = data[finding['path']]
        start,end = finding['start']['offset'],finding['end']['offset']
        assert 0 <= start < end <= len(raw)
        key = (finding['check_id'], finding['path'], sha(raw[start:end].strip()))
        groups[key].append(finding)
    inputs[label]={'groups':groups,'report':raw_report,'manifest':expected}

added,removed,retained = [],[],[]
old,new = inputs['before']['groups'],inputs['after']['groups']
for key in sorted(old.keys() | new.keys()):
    shared = min(len(old[key]),len(new[key]))
    identity = {'rule':key[0],'path':key[1],'matched_source_sha256':key[2]}
    for a,b in zip(old[key][:shared],new[key][:shared]):
        retained.append({'identity':identity,'before':a,'after':b})
    added.extend({'identity':identity,'finding':f} for f in new[key][shared:])
    removed.extend({'identity':identity,'finding':f} for f in old[key][shared:])
assert (len(added),len(removed),len(retained)) == (8,1,16)
existing = json.loads((VULNS/'V7/cumulative.json').read_text())
assert [r['finding'] for r in added]==existing['added']
assert [r['finding'] for r in removed]==existing['removed']
assert existing['retained']==16 and existing['before_count']==17 and existing['after_count']==24

registry = json.loads((VULNS/'consolidation-20261112/matrix.json').read_text())
associations = []
for entry in added:
    f = entry['finding']
    matches = [r for r in registry if r['file']==f['path'] and f['check_id'] in r['rules']]
    assert len(matches)==1
    entry['vulnerability']=matches[0]['id']
for row in registry:
    items = [a for a in added if a['vulnerability']==row['id']]
    assert len(items)==(2 if row['id']=='V2' else 1)
    associations.append({'id':row['id'],'file':row['file'],'cwe':row['cwe'],
                         'findings':[{'rule':a['finding']['check_id'],
                                      'line':a['finding']['start']['line'],
                                      'severity':a['finding']['extra']['severity'],
                                      'identity':a['identity']} for a in items],
                         'behavior_status':'incomplete' if row['id']=='V6' else 'archived_bounded_observation',
                         'github_run':row['github_run']})
assert len({c for row in associations for c in row['cwe']})==7
removed[0]['related_vulnerability']='V1'
removed[0]['meaning']='ancienne identité remplacée par le fragment shell V1 ; pas une correction de sécurité'
write('added.json',added)
write('removed.json',removed)
write('retained.json',retained)
write('associations.json',associations)

config = {}
for name in ('requirements.txt','rules.lock.json','scope.json','scan.py','gate.py'):
    source = ROOT/'semgrep'/name
    copy(source,'configuration/'+name)
    config[name]=sha(source.read_bytes())
local_source = ROOT/'tache3-rapports/validation-ci/results.json'
remote_source = ROOT/'tache3-rapports/validation-ci/github/results.json'
copy(local_source,'evidence/local-ci-results.json')
copy(remote_source,'evidence/github-results.json')
copy(VULNS/'behavior/results.json','evidence/archived-behavior.json')
copy(VULNS/'V6/validation-finale-20261112/results.json','evidence/v6-only-results.json')
copy(VULNS/'V6/validation-finale-20261112/after.log','evidence/v6-only-after.log')
local = json.loads(local_source.read_text())
remote = json.loads(remote_source.read_text())
matrix = []
for case in local['scenarios']:
    assert case['actual_exit_code']==case['expected_exit_code']
    matrix.append({'scenario':case['scenario'],'status':'reussie','scope':'validation locale antérieure du contrôleur sur historique synthétique',
                   'observed_exit_code':case['actual_exit_code'],'summary':case['summary'],
                   'evidence':'evidence/local-ci-results.json'})
for run in remote['runs']:
    matrix.append({'scenario':'github-'+str(run['databaseId']),'status':'reussie',
                   'scope':'démonstration CI antérieure ; exemple temporaire, pas les sept injections',
                   'observed_conclusion':run['conclusion'],'security':run['security'],'jobs':run['jobs'],
                   'url':run['url'],'evidence':'evidence/github-results.json'})
matrix.extend([
 {'scenario':'sept-types-detection-statique','status':'reussie','scope':'682 sources finales, sept CWE, huit identités ajoutées','evidence':'associations.json'},
 {'scenario':'sept-etats-build-tests','status':'reussie','scope':'sept builds archivés, 964 core + 8 processeur, deux désactivations initiales, zéro violation','evidence':'../../vulnerabilites/consolidation-20261112/matrix.json'},
 {'scenario':'six-observations-bornees','status':'reussie','scope':'anciennes sondes V1–V5/V7 ; limites de portée conservées','evidence':'evidence/archived-behavior.json'},
 {'scenario':'v6-comportement','status':'incomplete','scope':'détection INFO et build confirmés ; marqueur réservé rejeté, pas de démonstration ReDoS','evidence':'evidence/v6-only-after.log'},
 {'scenario':'sept-injections-blocage-integre','status':'non_executee','scope':'comparaison finale avec nouveautés confirmée ; aucun code de sortie du gate complet ni run GitHub enregistré pour ces sept états','expected_exit_code':1,'evidence':'summary.json'},
 {'scenario':'rickroll-apres-tests-reussis','status':'non_executee','scope':'action non implémentée ; anciens builds réussis ne valident pas son futur déclenchement'},
 {'scenario':'rickroll-apres-test-junit-echoue','status':'non_executee','scope':'aucun test JUnit volontairement échoué avec action rickroll'},
 {'scenario':'compilation-echouee-sans-echec-junit','status':'non_executee','scope':'aucune preuve de ce cas Maven ; le scan Semgrep malformé est un cas distinct'}
])
write('validation-matrix.json',matrix)
summary={'step':6,'status':'comparison_complete_with_documented_limits',
         'before_count':17,'after_count':24,'added':8,'removed':1,'retained':16,
         'distinct_cwes':7,'expected_gate_exit_for_new_findings':1,'actual_gate_exit_for_final_state':None,
         'v6_static_detection':'confirmed','v6_behavior':'incomplete',
         'semgrep_version':'1.185.0','locked_rules':82,'java_files_per_scan':682,
         'configuration_sha256':config,'validation_matrix_statuses':dict(Counter(c['status'] for c in matrix)),
         'new_scan_executed':False,'behavior_probe_executed':False,'production_modified':False,
         'publication_performed':False}
write('summary.json',summary)
print(json.dumps(summary,ensure_ascii=False))
