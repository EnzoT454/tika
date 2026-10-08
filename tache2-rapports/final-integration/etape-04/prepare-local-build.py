#!/usr/bin/env python3
"""Copier les sources courantes sur le disque interne, sans AppleDouble."""
import hashlib
import json
from pathlib import Path
import shutil
import subprocess
import tempfile


def main():
    root = Path(__file__).resolve().parents[3]
    result = subprocess.run(
        ["git", "ls-files", "-z", "--cached", "--others", "--exclude-standard"],
        cwd=root, capture_output=True, check=True,
    )
    build = Path(tempfile.mkdtemp(prefix="tika-final-build-", dir="/private/tmp"))
    modules = {"tika-core", "tika-parent", "tika-annotation-processor", ".mvn"}
    manifest = {}
    for name in sorted(set(result.stdout.decode().split("\0")) - {""}):
        relative = Path(name)
        if any(part.startswith("._") for part in relative.parts):
            continue
        if not (len(relative.parts) == 1 or relative.parts[0] in modules
                or relative.name == "pom.xml"):
            continue
        source = root / relative
        # Les fichiers suivis supprimés dans l'état courant ne sont pas copiés.
        if not source.is_file():
            continue
        target = build / relative
        target.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(source, target)
        manifest[name] = hashlib.sha256(target.read_bytes()).hexdigest()
    (build / "mvnw").chmod(0o755)
    (build / "source-manifest.json").write_text(
        json.dumps(manifest, indent=2, ensure_ascii=False) + "\n"
    )
    print(build)


if __name__ == "__main__":
    main()
