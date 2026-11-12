#!/usr/bin/env python3
"""Valider localement le rickroll sur une copie de reference, sans injection."""
import argparse
import hashlib
import importlib.util
import json
import os
from pathlib import Path
import shutil
import subprocess
import sys
import tarfile
import tempfile
import time
import xml.etree.ElementTree as ET

from ruamel.yaml import YAML


ROOT = Path(__file__).resolve().parents[2]
DETECTOR = ROOT / '.github/scripts/tache3-test-status.py'
ACTION = ROOT / '.github/actions/rickroll'
sys.dont_write_bytecode = True
spec = importlib.util.spec_from_file_location('test_status', DETECTOR)
status = importlib.util.module_from_spec(spec)
spec.loader.exec_module(status)


def write(path, value):
    path.write_text(json.dumps(value, indent=2, ensure_ascii=False) + '\n')


def detector_cases(output):
    cases = []
    with tempfile.TemporaryDirectory(prefix='rickroll-detector-') as temp:
        base = Path(temp)
        for name, outcome, kind, stale, error in [
            ('assertion-failed','failure','failure',False,False),
            ('test-error','failure','error',False,False),
            ('passed','success','passed',False,False),
            ('skipped','success','skipped',False,False),
            ('stale-failure','failure','failure',True,False),
            ('compile-no-report','failure','none',False,False),
            ('success-no-report','success','none',False,True),
            ('malformed-report','failure','malformed',False,True),
            ('counter-mismatch','failure','counter',False,True),
        ]:
            root = base/name
            root.mkdir()
            for module in status.MODULES:
                folder = root/module/'target/surefire-reports'
                folder.mkdir(parents=True)
                if kind == 'none':
                    continue
                if kind == 'malformed':
                    (folder/'TEST-fixture.xml').write_text('<broken')
                    continue
                suite = ET.Element('testsuite', tests='1', failures=str(int(kind=='failure' or kind=='counter')),
                                   errors=str(int(kind=='error')), skipped=str(int(kind=='skipped')))
                case = ET.SubElement(suite,'testcase',name='fixture',classname='Fixture')
                if kind in ('failure','error','skipped'):
                    ET.SubElement(case,kind)
                ET.ElementTree(suite).write(folder/'TEST-fixture.xml')
            try:
                result = status.evaluate(root, time.time_ns()+1000000 if stale else 1, outcome)
                assert not error, name
                assert result['has_failures'] == (kind in ('failure','error') and not stale), name
                cases.append({'case':name,'validated':True,'result':result})
            except (ET.ParseError, ValueError) as exception:
                assert error, (name,exception)
                cases.append({'case':name,'validated':True,'expected_technical_error':str(exception)})
    write(output/'detector-cases.json',cases)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output',type=Path,required=True)
    args = parser.parse_args()
    output = args.output.resolve()
    output.mkdir(parents=True,exist_ok=False)
    detector_cases(output)
    yaml = YAML(typ='safe')
    workflow = yaml.load((ROOT/'.github/workflows/tache3-ift3913.yml').read_text())
    action = yaml.load((ACTION/'action.yml').read_text())
    steps = workflow['jobs']['construction']['steps']
    build_step = next(s for s in steps if s.get('id')=='build')
    detector_step = next(s for s in steps if s.get('id')=='tests')
    rick_step = next(s for s in steps if s.get('uses')=='./.github/actions/rickroll')
    assert workflow['jobs']['construction']['needs']=='securite'
    assert not any(s.get('continue-on-error') for job in workflow['jobs'].values() for s in job['steps'])
    assert 'always()' in rick_step['if'] and "steps.tests.outputs.has_failures == 'true'" in rick_step['if']
    assert '!cancelled()' in rick_step['if'] and 'always()' in detector_step['if']
    records = []
    with tempfile.TemporaryDirectory(prefix='tache3-rickroll-build-') as temp:
        build = Path(temp)
        baseline = ROOT/'tache3-rapports/environnement/sources.tar.gz'
        with tarfile.open(baseline) as archive:
            for member in archive:
                path = Path(member.name)
                assert not path.is_absolute() and '..' not in path.parts
                if member.isfile() and not any(p.startswith('._') for p in path.parts):
                    target = build/path
                    target.parent.mkdir(parents=True,exist_ok=True)
                    target.write_bytes(archive.extractfile(member).read())
        manifest = json.loads((ROOT/'tache3-rapports/environnement/source-manifest.json').read_text())
        assert all(hashlib.sha256((build/name).read_bytes()).hexdigest()==h for name,h in manifest.items())
        write(output/'baseline-manifest.json',manifest)
        (build/'mvnw').chmod(0o755)
        script = build/'.github/scripts/tache3-test-status.py'
        script.parent.mkdir(parents=True)
        shutil.copyfile(DETECTOR,script)
        fixture = build/'tika-core/src/test/java/org/apache/tika/RickrollDemoTest.java'
        header = (build/'tika-core/src/main/java/org/apache/tika/Tika.java').read_text().split('package ',1)[0]
        for label,expected_code,expected_rick in [('passed',0,False),('junit-failed',1,True),('compile-failed',1,False)]:
            dest = output/label
            dest.mkdir()
            if label != 'passed':
                if label == 'junit-failed':
                    body = 'org.junit.jupiter.api.Assertions.fail("IFT3913 controlled JUnit failure");'
                else:
                    body = 'int count = "IFT3913 controlled compiler error";\n        org.junit.jupiter.api.Assertions.assertEquals(0, count);'
                fixture.write_text(header + '''package org.apache.tika;

import org.junit.jupiter.api.Test;

public class RickrollDemoTest {
    @Test
    void demonstration() {
        '''+body+'''
    }
}
''')
                shutil.copyfile(fixture,dest/'RickrollDemoTest.java')
            env = dict(os.environ,JAVA_HOME='/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home',
                       GITHUB_OUTPUT=str(dest/'github-output.txt'),GITHUB_STEP_SUMMARY=str(dest/'github-summary.md'),
                       ACTION_DIR=str(ACTION),RICKROLL_FRAME_DELAY='0')
            with (dest/'build-runner.log').open('w') as log:
                result = subprocess.run(['bash','-e','-o','pipefail','-c',build_step['run']],cwd=build,
                                        env=env,stdout=log,stderr=subprocess.STDOUT,timeout=600)
            assert result.returncode==expected_code,(label,result.returncode)
            env['BUILD_OUTCOME']='success' if result.returncode==0 else 'failure'
            detect = subprocess.run(['bash','-e','-o','pipefail','-c',detector_step['run']],cwd=build,
                                    env=env,capture_output=True,text=True,timeout=30)
            (dest/'detector.log').write_text(detect.stdout+detect.stderr)
            assert detect.returncode==0,(label,detect.stdout,detect.stderr)
            state = json.loads((build/'tache3-test-state/results.json').read_text())
            assert state['has_failures']==expected_rick,(label,state)
            action_code = None
            if state['has_failures']:
                animation = subprocess.run(['bash','-e','-o','pipefail','-c',action['runs']['steps'][0]['run']],
                                           cwd=build,env=env,capture_output=True,text=True,timeout=10)
                (dest/'rickroll.log').write_text(animation.stdout+animation.stderr)
                action_code=animation.returncode
                assert action_code==0 and animation.stdout.count('[FRAME ')==6
                assert 'YOU GOT RICKROLLED!' in animation.stdout
                assert 'youtube.com/watch?v=dQw4w9WgXcQ' in animation.stdout
                assert (dest/'github-summary.md').is_file()
            else:
                assert not (dest/'github-summary.md').exists()
            final_code = result.returncode or detect.returncode or (action_code or 0)
            assert final_code==expected_code
            shutil.copyfile(build/'tache3-build.log',dest/'maven.log')
            shutil.copytree(build/'tache3-test-state',dest/'test-state')
            with tarfile.open(dest/'reports.tar.gz','w:gz') as archive:
                for module in ('tika-parent',*status.MODULES):
                    for name in ('surefire-reports','checkstyle-result.xml','site/jacoco'):
                        path = build/module/'target'/name
                        if path.exists():
                            archive.add(path,arcname=str(path.relative_to(build)))
            if label == 'compile-failed':
                assert 'incompatible types' in (dest/'maven.log').read_text()
                assert not any('tika-core/' in p for p in state['reports'])
            record = {'case':label,'maven_exit_code':result.returncode,'detector_exit_code':detect.returncode,
                      'rickroll_invoked':state['has_failures'],'action_exit_code':action_code,
                      'preserved_build_exit_code':final_code,'state':state,
                      'github_runner_executed':False}
            write(dest/'results.json',record)
            records.append(record)
            write(output/'results.json',records)
            print(json.dumps(record),flush=True)
    return 0


if __name__=='__main__':
    sys.exit(main())
