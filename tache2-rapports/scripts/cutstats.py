"""Statistiques de generation ChatUniTest a partir de history*/class*/methodMapping.json + attempt*/records.json.
usage: python cutstats.py <dossier contenant history*> [--md]"""
import sys, json, glob, os, collections
root = sys.argv[1]; md = "--md" in sys.argv
rows = []
for hist in sorted(glob.glob(os.path.join(root, "history*"))):
    for cls in sorted(glob.glob(os.path.join(hist, "class*"))):
        mm = os.path.join(cls, "methodMapping.json")
        if not os.path.exists(mm): continue
        mapping = json.load(open(mm, encoding="utf-8"))
        for mkey, sig in mapping.items():
            sig = sig if isinstance(sig, str) else (sig.get("signature") or sig.get("methodName") or json.dumps(sig))
            attempts = sorted(glob.glob(os.path.join(cls, mkey, "attempt*", "records.json")))
            calls = 0; compile_err = 0; exec_err = 0; exported = False; last_round = None; ptok = 0; rtok = 0; errs = collections.Counter()
            for a in attempts:
                recs = json.load(open(a, encoding="utf-8"))
                for r in recs:
                    calls += 1; ptok += r.get("promptToken", 0); rtok += r.get("responseToken", 0)
                    if r.get("hasError"):
                        et = (r.get("errorMsg") or {}).get("errorType", "?")
                        if et == "COMPILE_ERROR":
                            compile_err += 1
                            for e in (r["errorMsg"].get("errorMessage") or []):
                                s = e.split("symbol:")[-1].strip().split("\n")[0] if "symbol:" in e else e.split(" : ")[-1].split("\n")[0]
                                errs[s.strip()[:60]] += 1
                        else: exec_err += 1
                    else:
                        exported = True; last_round = (os.path.basename(os.path.dirname(a)), r.get("round"))
            # verification croisee avec les fichiers reellement exportes (un tour "skipped" est enregistre sans erreur)
            snap = glob.glob(os.path.join(root, "generated-tests-snapshot", "**", "*_%s_%s_*_Test.java" % (sig.split("(")[0], mkey.replace("method", ""))), recursive=True)
            if exported and not snap:
                exported = False; last_round = None
            rows.append((mkey, sig, len(attempts), calls, compile_err, exec_err, exported, last_round, ptok, rtok, errs))
tot_calls = sum(r[3] for r in rows); tot_exp = sum(1 for r in rows if r[6])
if md:
    print("| # | Méthode | Tentatives | Appels LLM | Tours en échec de compilation | Exportée | Réussite à | Erreurs de compilation (symbole) |")
    print("|---|---|---|---|---|---|---|---|")
    for r in rows:
        succ = ("%s, tour %s" % r[7]) if r[7] else "—"
        errs = "; ".join("%s (%d)" % (k, v) for k, v in r[10].most_common(3))
        print("| %s | `%s` | %d | %d | %d | %s | %s | %s |" % (r[0].replace("method",""), r[1], r[2], r[3], r[4], "oui" if r[6] else "non", succ, errs))
    print("\nTotal : %d méthodes, %d appels au modèle, %d méthodes avec un test exporté (%.0f %%), %d jetons de prompt, %d jetons de réponse." % (len(rows), tot_calls, tot_exp, 100.0*tot_exp/len(rows) if rows else 0, sum(r[8] for r in rows), sum(r[9] for r in rows)))
else:
    for r in rows:
        print("%-9s %-45s att=%d calls=%2d cerr=%2d xerr=%d exported=%-5s at=%s errs=%s" % (r[0], r[1][:45], r[2], r[3], r[4], r[5], r[6], r[7], dict(r[10].most_common(2))))
    print("TOTAL methods=%d calls=%d exported=%d" % (len(rows), tot_calls, tot_exp))
