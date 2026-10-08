#!/usr/bin/env python3
"""Comparer des identités de mutants stables, sans assimiler les timeouts aux tués."""
from collections import Counter
import json
from pathlib import Path

EVIDENCE = Path(__file__).resolve().parent


def main():
    results = {s: json.loads((EVIDENCE/s/'results.json').read_text()) for s in 'ABC'}
    mutants = {s: {m['id']: m for m in json.loads((EVIDENCE/s/'mutants.json').read_text())}
               for s in 'ABC'}
    assert mutants['A'].keys() == mutants['B'].keys() == mutants['C'].keys()
    assert results['A']['target_bytecode_sha256'] == results['B']['target_bytecode_sha256'] == results['C']['target_bytecode_sha256']
    manifests = {s: json.loads((EVIDENCE/s/'source-manifest.json').read_text()) for s in 'ABC'}
    assert all(all(manifests[s].get(p) == digest for p, digest in manifests['A'].items())
               for s in 'BC')
    comparison = {'same_mutant_universe': True, 'same_common_sources': True, 'same_target_bytecode': True,
                  'states': results, 'transitions': {}}
    for before, after in [('A', 'B'), ('B', 'C'), ('A', 'C')]:
        transitions = Counter()
        newly_killed, lost_kills, changed = [], [], []
        for identity, old in mutants[before].items():
            new = mutants[after][identity]
            transitions[f"{old['status']} -> {new['status']}"] += 1
            entry = dict(new, previous_status=old['status'], previous_killing_test=old['killingTest'])
            if old['status'] != new['status']:
                changed.append(entry)
            if old['status'] != 'KILLED' and new['status'] == 'KILLED':
                newly_killed.append(entry)
            if old['status'] == 'KILLED' and new['status'] != 'KILLED':
                lost_kills.append(entry)
        comparison['transitions'][f'{before}_to_{after}'] = {
            'matrix': dict(transitions), 'newly_killed_count': len(newly_killed),
            'lost_kills_count': len(lost_kills), 'newly_killed': newly_killed,
            'lost_kills': lost_kills, 'status_changes': changed}
    (EVIDENCE/'comparison.json').write_text(json.dumps(comparison, indent=2)+'\n')
    lines = ['# Mesures finales comparables A/B/C', '',
             'Score = `100 × KILLED / total`. `TIMED_OUT` reste séparé.', '',
             '| État | Classe | Total | KILLED | TIMED_OUT | SURVIVED | NO_COVERAGE | Autres | Score |',
             '|---|---|---:|---:|---:|---:|---:|---:|---:|']
    for state, result in results.items():
        for name, summary in list(sorted(result['by_class'].items())) + [('Total', result['mutation'])]:
            c = summary['statuses']
            other = sum(v for k,v in c.items() if k not in ['KILLED','TIMED_OUT','SURVIVED','NO_COVERAGE'])
            lines.append(f"| {state} | {name.split('.')[-1]} | {summary['total']} | "
                         f"{c.get('KILLED',0)} | {c.get('TIMED_OUT',0)} | {c.get('SURVIVED',0)} | "
                         f"{c.get('NO_COVERAGE',0)} | {other} | {summary['score_killed_only']:.2f} % |")
    lines += ['', 'Les trois états ont exactement les mêmes identités de mutants et les mêmes sources communes.',
              'Les sources et configurations exactes sont archivées séparément dans A, B et C.', '']
    for transition, data in comparison['transitions'].items():
        lines.append(f"- {transition} : {data['newly_killed_count']} nouveaux `KILLED`, "
                     f"{data['lost_kills_count']} pertes de `KILLED`.")
    lines += ['', 'Les transitions, identifiants et tests tueurs sont dans `comparison.json`.',
              'Le lien entre chaque mutant, les données et l’assertion sera analysé à l’étape 6.', '']
    (EVIDENCE/'comparison.md').write_text('\n'.join(lines))
    print('\n'.join(lines))


if __name__ == '__main__':
    main()
