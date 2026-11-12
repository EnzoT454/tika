# Rickroll animé façon terminal rétro

Action composite originale pour la tâche 3 IFT3913. Elle affiche six poses ASCII successives, « YOU GOT RICKROLLED! » et le lien de la vidéo dans les logs, puis ajoute un aperçu au résumé du job. Aucun téléchargement, son, lecture automatique ni appel réseau n'est effectué. Les logs GitHub conservent les images textuelles successives ; ils ne produisent pas une animation graphique fluide.

Prévisualisation locale :

```bash
bash .github/actions/rickroll/ift3913-rickroll.sh --preview
```

Dans le workflow, l'action est réutilisée via :

```yaml
- name: Rickroll apres un cas JUnit echoue
  if: ${{ always() && !cancelled() && steps.tests.outputs.has_failures == 'true' }}
  uses: ./.github/actions/rickroll
```

Le déclenchement appartient au workflow. `.github/scripts/tache3-test-status.py` enregistre le début du build puis relit les XML Surefire des deux modules testés. Il compte les éléments `failure`/`error` de vrais `testcase`, vérifie les compteurs des suites et ignore les rapports antérieurs au marqueur temporel. Les erreurs de compilation, téléchargements interrompus et anciens rapports rouges ne constituent pas des échecs JUnit prouvés. Un rapport invalide ou une réussite annoncée sans rapports attendus est une erreur technique explicite.

Les étapes d'évaluation et d'artefacts s'exécutent aussi après l'échec Maven. L'action retourne 0 lorsqu'elle a correctement affiché l'humour ; **l'étape Maven déjà échouée et le job restent rouges**, sans `continue-on-error`. La mention « EXIT 1 (Maven) » concerne le build ayant échoué, pas le code de sortie du dessin. Une annulation ne déclenche pas l'action.

Le délai par pose est de 0,2 seconde par défaut, limité à une petite liste entre 0 et 0,5 seconde. `RICKROLL_FRAME_DELAY=0` permet les vérifications automatisées sans attente. L'invocation via `bash` ne dépend pas du bit exécutable du fichier.

L'intégration conserve le job Semgrep comme préalable de construction. Une branche contenant les sept injections sera donc arrêtée avant les tests ; la démonstration du rickroll utilise une copie de référence sans injections, sans assouplir le contrôle de sécurité du workflow.

Les preuves locales et leurs limites sont conservées dans `tache3-rapports/rickroll/`. Elles ne constituent pas un run GitHub Actions ; les conditions, sorties et téléchargements d'actions devront être confirmés à distance après autorisation de publication.

Références : [actions composites](https://docs.github.com/en/actions/tutorials/create-actions/create-a-composite-action) et [conditions de statut GitHub Actions](https://docs.github.com/en/actions/reference/workflows-and-actions/expressions). Implémentation personnelle avec assistance de Codex ; aucun code de `random-rickroll` ni implémentation Rust repris.
