# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements. See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License. You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""Require every accepted task 2 test to be executed successfully by Surefire."""

import argparse
from collections import Counter
import os
from pathlib import Path
import sys
import xml.etree.ElementTree as ET


EXPECTED = {
    "LookaheadInputStreamGeneratedTest": {"markSupportedIsTrue"},
    "FilenameUtilsGeneratedTest": {
        "calculateExtensionUsesMimeTypeExtension",
        "calculateExtensionReturnsProvidedDefaultWithoutMimeType",
    },
    "LookaheadInputStreamManualTest": {
        "arrayReadHonorsOffsetLengthAndEndOfLookahead",
        "arrayReadReturnsOnlyBytesRemainingAfterAByteRead",
        "closeRestoresThePositionAtConstruction",
        "shortReadsFillIncrementallyAndRestoreAtEndOfStream",
    },
    "FilenameUtilsManualTest": {
        "embeddedFilenameUsesInternalPathWhenResourceNameIsMissing",
        "embeddedPathUsesInternalPathWhenResourcePathIsMissing",
        "filenameAtMaximumLengthIsNotTruncated",
        "calculateExtensionUsesBinForAnUnknownMimeType",
        "resolveWithinRejectsExistingSymbolicLinkOutsideDirectory(Path)",
    },
}


def check_reports(directory):
    errors = []
    rows = []
    for short_name, names in EXPECTED.items():
        class_name = "org.apache.tika.io." + short_name
        report = directory / ("TEST-" + class_name + ".xml")
        try:
            suite = ET.parse(report).getroot()
            if suite.tag != "testsuite" or suite.get("name") != class_name:
                raise ValueError("Nom de suite incorrect")
            cases = suite.findall("testcase")
            actual = Counter(case.get("name") for case in cases)
            if actual != Counter(names):
                raise ValueError("Cas absents, inattendus ou dupliqués : " + str(actual))
            for key, expected in (("tests", len(names)), ("failures", 0),
                                  ("errors", 0), ("skipped", 0)):
                if int(suite.attrib[key]) != expected:
                    raise ValueError(f"Compteur {key} incorrect : {suite.attrib[key]}")
            for case in cases:
                if case.get("classname") != class_name:
                    raise ValueError("Classe de cas incorrecte")
                if any(case.find(tag) is not None for tag in ("failure", "error", "skipped")):
                    raise ValueError("Cas échoué ou désactivé : " + case.get("name", ""))
            rows.append(f"| {short_name} | {len(cases)} | Réussis |")
        except (OSError, ET.ParseError, ValueError, KeyError) as error:
            errors.append(f"{report}: {error}")
            rows.append(f"| {short_name} | — | ÉCHEC |")
    summary = "\n".join([
        "### Vérification des nouveaux tests de la tâche 2", "",
        "| Classe | Cas | Résultat |", "|---|---:|---|", *rows, "",
        "12 nouveaux cas réussis, aucun désactivé." if not errors
        else "Vérification échouée : consulter les erreurs et les rapports.",
    ]) + "\n"
    print(summary)
    if os.environ.get("GITHUB_STEP_SUMMARY"):
        with open(os.environ["GITHUB_STEP_SUMMARY"], "a", encoding="utf-8") as output:
            output.write(summary)
    for error in errors:
        print(error, file=sys.stderr)
    return 1 if errors else 0


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("reports", nargs="?", type=Path,
                        default=Path("tika-core/target/surefire-reports"))
    sys.exit(check_reports(parser.parse_args().reports))
