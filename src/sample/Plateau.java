package sample;

public class Plateau {
    private int nb_ligne;
    private int nb_colonne;
    private Piece tab[][];

    public Plateau(int ligne,int col ) {
        nb_ligne = ligne;
        nb_colonne = col;
        tab = new Piece[nb_ligne][nb_colonne];
    }

    // rempli le tableau de piece
    private void create_plateau() {
        for (int i = 0; i < nb_ligne; i++) {
            for (int j = 0; j < nb_colonne; j++) {

            }
        }
    }
}
