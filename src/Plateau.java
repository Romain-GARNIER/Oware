public class Plateau {

    private int nbrTrou = 12;
    private int[] plateau = new int[nbrTrou];

    public Plateau(int[] plateau) {
        this.plateau = plateau;
        for (int i = 0; i < nbrTrou; i++) {
            plateau[i] = 4;
        }
    }

    void semer(int numeroTrou) {
        int nbrGraineASemer = plateau[numeroTrou];
        for (int i = 0; i < nbrGraineASemer; i++) {
            int nextTrou = numeroTrou + i;
            if (plateau[nextTrou] <= 2 && plateau[nextTrou] >= 1) {
                ramasserGraine(nextTrou);
            } else {
                plateau[nextTrou] += 1;
            }
        }
    }

    void ramasserGraine(int nextTrou) {
            Joueur.addGraines(plateau[nextTrou] + 1);
            plateau[nextTrou] = 0;
        }

    }


