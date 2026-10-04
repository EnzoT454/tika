"""Compare two PIT archives created by summarize-mutations.py.

Usage: python3 evidence/tache2/compare-mutations.py A B
Writes mutation-delta.json and mutation-delta.csv in the second archive.
"""

import csv
import json
from pathlib import Path
import sys


root = Path(__file__).resolve().parent
before_dir = root / sys.argv[1]
after_dir = root / sys.argv[2]
before = {row["id"]: row for row in json.loads((before_dir / "mutants.json").read_text())}
after = {row["id"]: row for row in json.loads((after_dir / "mutants.json").read_text())}

if set(before) != set(after):
    raise ValueError("PIT mutation identities differ between archives")

changed = []
for identity in sorted(before):
    old, new = before[identity], after[identity]
    if old["status"] == new["status"]:
        continue
    changed.append({
        "id": identity,
        "class": new["class"],
        "method": new["method"],
        "descriptor": new["descriptor"],
        "line": new["line"],
        "mutator": new["mutator"],
        "description": new["description"],
        "before_status": old["status"],
        "after_status": new["status"],
        "before_killing_test": old["killing_test"],
        "after_killing_test": new["killing_test"],
    })

summary = {
    "before": sys.argv[1],
    "after": sys.argv[2],
    "mutation_count": len(before),
    "changed_count": len(changed),
    "newly_killed": [row for row in changed if row["after_status"] == "KILLED"],
    "changes": changed,
}
(after_dir / "mutation-delta.json").write_text(json.dumps(summary, indent=2) + "\n")
if changed:
    with (after_dir / "mutation-delta.csv").open("w", newline="") as handle:
        writer = csv.DictWriter(handle, fieldnames=list(changed[0]))
        writer.writeheader()
        writer.writerows(changed)
print(json.dumps(summary, indent=2))
