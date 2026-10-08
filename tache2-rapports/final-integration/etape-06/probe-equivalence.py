#!/usr/bin/env python3
"""Contre-exemples locaux isolés : ne modifie ni production ni suites A/B/C."""
import hashlib
import json
from pathlib import Path
import shutil
import subprocess
import tempfile

ROOT = Path(__file__).resolve().parents[3]
OUT = Path(__file__).resolve().parent


def main():
    states = json.loads((OUT.parent/'etape-05/states.json').read_text())
    build = Path(states['C'])
    dependencies = build/'tika-core/target/tika-core-4.0.0-SNAPSHOT-test-jar-with-dependencies.jar'
    classes = build/'tika-core/target/classes'
    source_path = ROOT/'tika-core/src/main/java/org/apache/tika/io/FilenameUtils.java'
    source = source_path.read_text()
    work = Path(tempfile.mkdtemp(prefix='tika-equivalence-', dir='/private/tmp'))
    probe = '''import org.apache.tika.io.FilenameUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
public class EquivalenceProbe {
    public static void main(String[] args) throws Exception {
        System.out.println("OS\\t" + System.getProperty("os.name"));
        java.lang.reflect.Method prefix = FilenameUtils.class.getDeclaredMethod("getPrefixLength", String.class);
        prefix.setAccessible(true);
        for (String input : new String[]{"~", "~alice", "A:", "Z:", "C:", "A", "AB"}) {
            Metadata metadata = new Metadata();
            metadata.set(TikaCoreProperties.RESOURCE_NAME_KEY, input);
            System.out.println(input + "\\tprefix\\t" + prefix.invoke(null, input));
            System.out.println(input + "\\tcommons_prefix\\t" + org.apache.commons.io.FilenameUtils.getPrefixLength(input));
            for (String operation : new String[]{"name", "path"}) {
                try {
                    String result = operation.equals("name")
                        ? FilenameUtils.getSanitizedEmbeddedFileName(metadata, ".bin", 100)
                        : FilenameUtils.getSanitizedEmbeddedFilePath(metadata, ".bin", 100);
                    System.out.println(input + "\\t" + operation + "\\tRETURN:" + result);
                } catch (Throwable error) {
                    System.out.println(input + "\\t" + operation + "\\tTHROW:" + error.getClass().getName());
                }
            }
        }
    }
}
'''
    (OUT/'EquivalenceProbe.java.txt').write_text(probe)
    (work/'EquivalenceProbe.java').write_text(probe)
    command = ['javac', '-cp', str(dependencies), str(work/'EquivalenceProbe.java')]
    logs = [{'command': command, 'result': subprocess.run(command, text=True, capture_output=True, check=True).stdout}]
    variants = {
        'original': None,
        'name_prefix_negated_L156': ('if (prefixLength > 0) {', 'if (prefixLength <= 0) {'),
        'prefix_lower_boundary_L323': ("path.charAt(0) >= 'A'", "path.charAt(0) > 'A'"),
        'prefix_upper_boundary_L323': ("path.charAt(0) <= 'Z'", "path.charAt(0) < 'Z'"),
    }
    results = {}
    for variant, replacement in variants.items():
        directory = work/variant
        directory.mkdir()
        if replacement:
            changed = source.replace(*replacement, 1)
            java = directory/'FilenameUtils.java'
            java.write_text(changed)
            shutil.copyfile(java, OUT/(variant+'.java.txt'))
            command = ['javac', '-cp', str(dependencies), '-d', str(directory), str(java)]
            result = subprocess.run(command, text=True, capture_output=True, check=True)
            logs.append({'command': command, 'stdout': result.stdout, 'stderr': result.stderr})
        command = ['java', '-cp', ':'.join(map(str,[directory,work,classes,dependencies])), 'EquivalenceProbe']
        result = subprocess.run(command, text=True, capture_output=True, check=True)
        logs.append({'command': command, 'stdout': result.stdout, 'stderr': result.stderr})
        results[variant] = result.stdout
    assert '~\tname\tTHROW:java.lang.StringIndexOutOfBoundsException' in results['original']
    assert '~\tname\tRETURN:~.bin' in results['name_prefix_negated_L156']
    for bound, letter in [('lower','A'), ('upper','Z')]:
        assert letter+':\tpath\tRETURN:null' in results['original']
        assert letter+':\tpath\tRETURN:'+letter+'.bin' in results[f'prefix_{bound}_boundary_L323']
    (OUT/'probe-results.json').write_text(json.dumps({
        'workspace':str(work), 'source_sha256':hashlib.sha256(source_path.read_bytes()).hexdigest(),
        'dependency_jar':str(dependencies), 'variants':results,
        'commands':logs, 'asserted_counterexamples':3,
        'integrated_into_test_suite':False, 'pit_rerun':False},indent=2)+'\n')
    shutil.copyfile('/tmp/tika-final-commons-filename-bytecode.txt', OUT/'commons-io-2.22.0-filename-bytecode.txt')
    print(json.dumps(results,indent=2))


if __name__ == '__main__':
    main()
