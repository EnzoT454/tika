#!/usr/bin/env bash
set -euo pipefail

preview=false
if [[ "${1:-}" == '--preview' && "$#" == 1 ]]; then
  preview=true
elif [[ "$#" != 0 ]]; then
  printf 'Usage: bash ift3913-rickroll.sh [--preview]\n' >&2
  exit 2
fi
delay="${RICKROLL_FRAME_DELAY:-0.2}"
case "$delay" in
  0|0.1|0.2|0.3|0.5) ;;
  *) printf 'Frame delay must be 0, 0.1, 0.2, 0.3 or 0.5 seconds.\n' >&2; exit 2 ;;
esac

if [[ "$preview" == true ]]; then
  printf '[SYSTEM] Preview only — no test failure simulated.\n'
else
  printf '[SYSTEM] JUnit failure detected!\n'
fi
printf '[SYSTEM] Initiating surprise...\n\n'
printf '================================\n'
printf '       ★ RICKROLL MODE ★\n'
printf '================================\n'

# Append frames rather than clearing the screen: every pose remains in CI logs.
frames=(
'    \o/
     |
    / \'
'     o/
    /|
    / \'
'    _o_
     |
    / \'
'    \o
     |\
    / \'
'     o/
    /|
    / >'
'    \o/
     |
    < \'
)
for index in "${!frames[@]}"; do
  printf '\n[FRAME %s/%s]\n%s\n' "$((index + 1))" "${#frames[@]}" "${frames[$index]}"
  sleep "$delay"
done
printf '\nYOU GOT RICKROLLED!\n'
printf '▶ Rickroll: https://www.youtube.com/watch?v=dQw4w9WgXcQ\n'
if [[ "$preview" == false ]]; then
  printf '✖ TEST FAILURE — EXIT 1 (Maven)\n'
  printf '[SYSTEM] Original build failure remains unchanged.\n'
fi

if [[ -n "${GITHUB_STEP_SUMMARY:-}" ]]; then
  cat >> "$GITHUB_STEP_SUMMARY" <<'SUMMARY'

### ★ RICKROLL MODE ★

```text
    \o/
     |
    / \
```

**YOU GOT RICKROLLED!**

▶ [Rickroll](https://www.youtube.com/watch?v=dQw4w9WgXcQ)

Les poses successives sont conservees dans les logs. Le statut du build initial est preserve.
SUMMARY
fi

# This action succeeds at displaying the joke. The Maven step remains failed.
exit 0
