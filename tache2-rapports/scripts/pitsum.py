import sys, xml.etree.ElementTree as ET, collections
path = sys.argv[1]
t = ET.parse(path)
by_class = collections.defaultdict(collections.Counter)
by_method = collections.defaultdict(collections.Counter)
by_mutator = collections.defaultdict(collections.Counter)
details = collections.defaultdict(list)
for m in t.getroot().findall("mutation"):
    cls = m.find("mutatedClass").text.split(".")[-1]
    meth = m.find("mutatedMethod").text
    st = m.get("status")
    mut = m.find("mutator").text.split(".")[-1]
    line = m.find("lineNumber").text
    desc = m.find("description").text
    kt = m.find("killingTest").text if m.find("killingTest") is not None else ""
    by_class[cls][st] += 1
    by_method[(cls, meth)][st] += 1
    by_mutator[(cls, mut)][st] += 1
    details[cls].append((int(line), meth, mut, st, desc, kt or ""))
def score(c):
    tot = sum(c.values()); k = c.get("KILLED",0) + c.get("TIMED_OUT",0)
    cov = tot - c.get("NO_COVERAGE",0)
    return tot, k, cov
print("== par classe ==")
for cls, c in sorted(by_class.items()):
    tot,k,cov = score(c)
    print("%-15s total=%3d killed=%3d survived=%3d no_cov=%3d timed_out=%d  score=%.0f%%  strength=%.0f%%" % (cls, tot, k, c.get("SURVIVED",0), c.get("NO_COVERAGE",0), c.get("TIMED_OUT",0), 100.0*k/tot, (100.0*k/cov) if cov else 0))
if "--methods" in sys.argv:
    print("\n== par methode (mutants non tues) ==")
    for (cls, meth), c in sorted(by_method.items()):
        tot,k,cov = score(c)
        if k < tot:
            print("%-15s %-32s total=%3d killed=%3d survived=%3d no_cov=%3d" % (cls, meth, tot, k, c.get("SURVIVED",0), c.get("NO_COVERAGE",0)))
if "--mutators" in sys.argv:
    print("\n== par mutateur ==")
    for (cls, mut), c in sorted(by_mutator.items()):
        tot,k,cov = score(c)
        print("%-15s %-40s total=%3d killed=%3d survived=%3d no_cov=%3d" % (cls, mut, tot, k, c.get("SURVIVED",0), c.get("NO_COVERAGE",0)))
if "--details" in sys.argv:
    which = sys.argv[sys.argv.index("--details")+1]
    print("\n== details", which, "==")
    for row in sorted(details[which]):
        print("L%-4d %-28s %-38s %-12s %s" % row[:5])
