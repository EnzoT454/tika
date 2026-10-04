"""Copy the baseline build inputs to a local filesystem without macOS sidecars.

Run from anywhere: python3 evidence/tache2/prepare-local-build.py
Prints the temporary directory; does not run Maven or modify the source tree.
"""

import hashlib
import json
from pathlib import Path
import shutil
import subprocess
import tempfile


root = Path(__file__).resolve().parents[2]
stage = Path(tempfile.mkdtemp(prefix="tika-tache2-initial-", dir="/private/tmp"))
tracked = subprocess.check_output(
    ["git", "ls-files", "-z"], cwd=root
).decode().split("\0")
modules = {"tika-core", "tika-parent", "tika-annotation-processor", ".mvn"}
# Include new tests and generation configuration before they are committed.
# Git ignores still exclude targets, macOS sidecars and other local artifacts.
untracked = subprocess.check_output(
    ["git", "ls-files", "--others", "--exclude-standard", "-z", "--", *sorted(modules)],
    cwd=root,
).decode().split("\0")
manifest = []
for item in sorted(set(tracked + untracked)):
    if not item:
        continue
    relative = Path(item)
    if any(part.startswith("._") for part in relative.parts):
        continue
    if (len(relative.parts) != 1
            and relative.parts[0] not in modules
            and relative.name != "pom.xml"):
        continue
    source = root / relative
    if not source.is_file():
        continue
    destination = stage / relative
    destination.parent.mkdir(parents=True, exist_ok=True)
    shutil.copyfile(source, destination)
    manifest.append({
        "path": item,
        "sha256": hashlib.sha256(source.read_bytes()).hexdigest(),
    })
(stage / "mvnw").chmod(0o755)
(stage / "source-manifest.json").write_text(json.dumps(manifest, indent=2) + "\n")
print(stage)
