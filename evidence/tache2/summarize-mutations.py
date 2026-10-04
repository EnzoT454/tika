"""Summarize an archived PIT XML report, preserving stable mutation identities.

Usage: python3 evidence/tache2/summarize-mutations.py A
"""

from collections import Counter, defaultdict
import csv
import hashlib
import json
from pathlib import Path
import sys
import xml.etree.ElementTree as ET


directory = Path(__file__).resolve().parent / sys.argv[1]
reports = list((directory / "pit-reports").rglob("mutations.xml"))
if len(reports) != 1:
    raise ValueError("Expected exactly one mutations.xml report")
report = reports[0]
tree = ET.parse(report).getroot()
rows = []
counts = defaultdict(Counter)
for mutation in tree.findall("mutation"):
    row = {
        key: mutation.findtext(tag, "")
        for key, tag in [
            ("class", "mutatedClass"), ("method", "mutatedMethod"),
            ("descriptor", "methodDescription"), ("line", "lineNumber"),
            ("mutator", "mutator"), ("description", "description"),
            ("killing_test", "killingTest"),
        ]
    }
    row["indexes"] = ",".join(node.text or "" for node in mutation.findall("indexes/index"))
    row["blocks"] = ",".join(node.text or "" for node in mutation.findall("blocks/block"))
    row["status"] = mutation.get("status")
    row["detected"] = mutation.get("detected") == "true"
    identity = [row[key] for key in ("class", "method", "descriptor", "mutator", "indexes")]
    row["id"] = hashlib.sha256(json.dumps(identity).encode()).hexdigest()[:16]
    rows.append(row)
    counts[row["class"]][row["status"]] += 1
if len({row["id"] for row in rows}) != len(rows):
    raise ValueError("Mutation identities are not unique")


def metrics(group):
    statuses = Counter(row["status"] for row in group)
    total = len(group)
    detected = sum(row["detected"] for row in group)
    return {
        "total": total, "statuses": dict(sorted(statuses.items())),
        "killed_pct": round(100 * statuses["KILLED"] / total, 2) if total else None,
        "pit_detected": detected,
        "pit_detected_pct": round(100 * detected / total, 2) if total else None,
    }


summary = {
    "report_sha256": hashlib.sha256(report.read_bytes()).hexdigest(),
    "identity_fields": ["class", "method", "descriptor", "mutator", "indexes"],
    "classes": {name: metrics([row for row in rows if row["class"] == name])
                for name in sorted(counts)},
    "total": metrics(rows),
}
(directory / "mutation-summary.json").write_text(json.dumps(summary, indent=2) + "\n")
(directory / "mutants.json").write_text(json.dumps(rows, indent=2) + "\n")
if rows:
    with (directory / "mutants.csv").open("w", newline="") as handle:
        writer = csv.DictWriter(handle, fieldnames=list(rows[0]))
        writer.writeheader()
        writer.writerows(rows)
print(json.dumps(summary, indent=2))
