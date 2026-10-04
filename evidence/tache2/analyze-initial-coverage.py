"""Extract class and candidate-method coverage from the archived baseline.

Usage: python3 evidence/tache2/analyze-initial-coverage.py
No build or source modification is performed.
"""

import csv
import hashlib
import json
from pathlib import Path
import xml.etree.ElementTree as ET


root = Path(__file__).resolve().parents[2]
baseline = root / "evidence/tache2/initial"
output = root / "evidence/tache2/selection"
output.mkdir(exist_ok=True)
report = baseline / "jacoco/jacoco.xml"
tree = ET.parse(report).getroot()
names = ("LookaheadInputStream", "FilenameUtils", "TailStream")
manifest = {
    item["path"]: item["sha256"]
    for item in json.loads((baseline / "source-manifest.json").read_text())
}


def counters(element):
    return {
        counter.get("type"): {
            "covered": int(counter.get("covered")),
            "missed": int(counter.get("missed")),
        }
        for counter in element.findall("counter")
    }


def ratio(counts, kind):
    counter = counts.get(kind, {"covered": 0, "missed": 0})
    covered, missed = counter["covered"], counter["missed"]
    total = covered + missed
    return [covered, missed, total, round(100 * covered / total, 2) if total else ""]


rows = []
candidates = []
for package in tree.findall("package"):
    for cls in package.findall("class"):
        counts = counters(cls)
        rows.append([cls.get("name").replace("/", ".")]
                    + ratio(counts, "LINE") + ratio(counts, "BRANCH"))
        short_name = cls.get("name").rsplit("/", 1)[-1]
        if package.get("name") != "org/apache/tika/io" or short_name not in names:
            continue
        source = "tika-core/src/main/java/" + cls.get("name") + ".java"
        test = "tika-core/src/test/java/" + cls.get("name") + "Test.java"
        for path in (source, test):
            actual = hashlib.sha256((root / path).read_bytes()).hexdigest()
            if actual != manifest[path]:
                raise ValueError("Source differs from baseline: " + path)
        source_lines = (root / source).read_text().splitlines()
        source_node = package.find("sourcefile[@name='" + cls.get("sourcefilename") + "']")
        gaps = []
        for line in source_node.findall("line"):
            if int(line.get("mi")) or int(line.get("mb")):
                values = {key: int(value) for key, value in line.attrib.items()}
                values["source"] = source_lines[values["nr"] - 1].strip()
                gaps.append(values)
        test_xml = baseline / "surefire-reports" / (
            "TEST-" + cls.get("name").replace("/", ".") + "Test.xml"
        )
        suite = ET.parse(test_xml).getroot()
        candidates.append({
            "class": cls.get("name").replace("/", "."),
            "source": source,
            "test_source": test,
            "counters": counts,
            "methods": [
                {"name": method.get("name"), "descriptor": method.get("desc"),
                 "line": int(method.get("line")), "counters": counters(method)}
                for method in cls.findall("method")
            ],
            "uncovered_or_partial_source_lines": gaps,
            "original_test_cases": [case.get("name") for case in suite.findall("testcase")],
            "surefire": {key: int(suite.get(key, "0"))
                         for key in ("tests", "failures", "errors", "skipped")},
            "decision": "excluded_full_coverage" if short_name == "TailStream"
                        else "shortlisted_pending_pit",
        })

with (output / "class-coverage.csv").open("w", newline="") as handle:
    writer = csv.writer(handle)
    writer.writerow(["class", "lines_covered", "lines_missed", "lines_total", "lines_pct",
                     "branches_covered", "branches_missed", "branches_total", "branches_pct"])
    writer.writerows(sorted(rows))

result = {
    "baseline_commit": (baseline / "commit.txt").read_text().strip(),
    "report_sha256": hashlib.sha256(report.read_bytes()).hexdigest(),
    "coverage_scope": "All original tika-core tests in the archived baseline",
    "pit_run": False,
    "candidates": candidates,
}
(output / "candidates.json").write_text(json.dumps(result, indent=2, ensure_ascii=False) + "\n")
for candidate in sorted(candidates, key=lambda item: names.index(item["class"].rsplit(".", 1)[-1])):
    print(candidate["class"], "lines:", ratio(candidate["counters"], "LINE"),
          "branches:", ratio(candidate["counters"], "BRANCH"), candidate["decision"])
