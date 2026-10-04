"""Integre les tests generes dans src/test/java en desactivant (@Disabled) les methodes dont l'oracle est faux.
usage: integrate.py <pkg_dir_dest> <results.txt>... -- <src.java>..."""
import sys, re, io, os, collections
args = sys.argv[1:]; dest = args[0]; sep = args.index("--"); results = args[1:sep]; files = args[sep+1:]
fails = collections.defaultdict(list)  # class -> [(display, message)]
cur = None
for r in results:
    for ln in io.open(r, encoding="utf-8", errors="replace"):
        m = re.match(r"^(EndianUtils\S+|MediaType\S+|FilenameUtils\S+)\s+found=", ln)
        if m: cur = m.group(1); continue
        m = re.match(r"^\s+FAIL (.+?) : (?:org\.opentest4j\.\w+|java\.lang\.\w+Error|[\w.]+Exception)?:?\s*(.*)$", ln.rstrip())
        if m and cur: fails[cur].append((m.group(1).strip(), m.group(2).strip()))
os.makedirs(dest, exist_ok=True)
summary = []
for f in files:
    name = os.path.basename(f)[:-5]
    src = io.open(f, encoding="utf-8").read()
    n_dis = 0
    for display, msg in fails.get(name, []):
        meth = display[:-2] if display.endswith("()") else None
        reason = "ChatUniTest : oracle faux - " + msg.replace('"', "'")[:150]
        if meth:
            pat = re.compile(r"(\n([ \t]*)(?:@[A-Za-z]+(?:\([^)]*\))?[ \t]*\n[ \t]*)*?)([ \t]*)public void " + re.escape(meth) + r"\(")
        else:  # @DisplayName("...") : chercher la methode qui suit l'annotation
            pat = re.compile(r"(\n([ \t]*)@DisplayName\(" + re.escape('"' + display + '"') + r"\)[ \t]*\n)([ \t]*)public void \w+\(")
        m = pat.search(src)
        if not m:
            # fallback: @Test juste avant 'public void meth('
            idx = src.find("public void %s(" % meth) if meth else -1
            if idx < 0: print("!! methode introuvable", name, display); continue
            line_start = src.rfind("\n", 0, idx) + 1
            indent = re.match(r"[ \t]*", src[line_start:]).group(0)
            src = src[:line_start] + indent + '@Disabled("%s")\n' % reason + src[line_start:]
        else:
            # inserer @Disabled avant la premiere annotation du bloc (apres le \n initial)
            start = m.start(1) + 1
            indent = re.match(r"[ \t]*", src[start:]).group(0)
            src = src[:start] + indent + '@Disabled("%s")\n' % reason + src[start:]
        n_dis += 1
    if n_dis and "import org.junit.jupiter.api.*;" not in src and "import org.junit.jupiter.api.Disabled;" not in src:
        src = re.sub(r"(package [\w.]+;\n)", r"\1\nimport org.junit.jupiter.api.Disabled;\n", src, count=1)
    header = ("// Test genere par ChatUniTest 2.1.1 (qwen2.5-coder:7b via Ollama).\n"
              "// %s\n" % ("Exporte tel quel par ChatUniTest (compilait sans intervention)." if "Corrections manuelles" not in src else "Voir les corrections manuelles ci-dessous.") +
              ("// %d methode(s) de test desactivee(s) (@Disabled) car leur oracle est faux : elles echouent sur le code non mute.\n" % n_dis if n_dis else "// Toutes les methodes de test reussissent sur le code non mute.\n"))
    if "// Test genere par ChatUniTest" in src:
        src = src.replace("// Test genere par ChatUniTest 2.1.1 (qwen2.5-coder:7b via Ollama), derniere tentative, non compilable tel quel.\n",
                          "// Test genere par ChatUniTest 2.1.1 (qwen2.5-coder:7b via Ollama), derniere tentative, non compilable tel quel.\n" +
                          ("// %d methode(s) de test desactivee(s) (@Disabled) car leur oracle est faux : elles echouent sur le code non mute.\n" % n_dis if n_dis else "// Toutes les methodes de test reussissent sur le code non mute.\n"), 1)
    else:
        src = re.sub(r"(package [\w.]+;\n)", lambda mm: mm.group(1) + "\n" + header, src, count=1)
    io.open(os.path.join(dest, name + ".java"), "w", encoding="utf-8", newline="\n").write(src)
    summary.append((name, n_dis, len(fails.get(name, []))))
for n, d, t in summary: print("%-40s desactivees=%d/%d" % (n, d, t))
print("TOTAL fichiers=%d methodes desactivees=%d" % (len(summary), sum(d for _, d, _ in summary)))
