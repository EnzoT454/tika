"""Compare deux mutations.xml de pitest : mutants nouvellement tues / toujours vivants, par classe et par test tueur."""
import sys, xml.etree.ElementTree as ET, collections
def load(p):
    d = {}
    for m in ET.parse(p).getroot().findall("mutation"):
        key = (m.find("mutatedClass").text, m.find("mutatedMethod").text, m.find("lineNumber").text,
               m.find("mutator").text.split(".")[-1], ",".join((i.text or "") for i in m.findall("indexes/index")) or (m.find("index").text if m.find("index") is not None else ""), m.find("description").text)
        kt = m.find("killingTest"); kt = kt.text if kt is not None and kt.text else ""
        d[key] = (m.get("status"), kt)
    return d
a, b = load(sys.argv[1]), load(sys.argv[2])
dead = {"KILLED", "TIMED_OUT", "MEMORY_ERROR", "RUN_ERROR"}
newly = collections.defaultdict(list); still = collections.defaultdict(list); lost = []
for k, (sb, ktb) in b.items():
    sa = a.get(k, ("ABSENT",""))[0]
    cls = k[0].split(".")[-1]
    if sb in dead and sa not in dead:
        newly[cls].append((k, sa, sb, ktb))
    elif sb not in dead:
        still[cls].append((k, sa, sb))
    if sa in dead and sb not in dead:
        lost.append((k, sa, sb))
print("== mutants nouvellement tues (avant -> apres) ==")
for cls in sorted(newly):
    print("--", cls, ":", len(newly[cls]))
    by_test = collections.Counter()
    for k, sa, sb, kt in sorted(newly[cls], key=lambda x: (int(x[0][2]), x[0][1])):
        t = kt.split("/[method:")[-1].rstrip("]") if kt else "?"
        tc = kt.split("[class:")[-1].split("]")[0].split(".")[-1] if kt else "?"
        by_test[tc + "#" + t] += 1
        print("   L%-4s %-28s %-34s %-12s -> %-9s tue par %s#%s" % (k[2], k[1], k[3], sa, sb, tc, t))
    print("   par test tueur:", dict(by_test))
print("\n== mutants toujours vivants apres ==")
for cls in sorted(still):
    print("--", cls, ":", len(still[cls]))
    for k, sa, sb in sorted(still[cls], key=lambda x: (int(x[0][2]), x[0][1])):
        print("   L%-4s %-28s %-34s %-12s  %s" % (k[2], k[1], k[3], sb, k[5][:70]))
if lost:
    print("\n== ATTENTION : mutants tues avant mais plus apres ==")
    for k, sa, sb in lost: print("  ", k[:4], sa, "->", sb)
