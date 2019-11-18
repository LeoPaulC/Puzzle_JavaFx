package sample;

public class Bordure_Plate extends Forme_Bordure {
    double posX = DEFAULT_COORD_X;
    double posY = DEFAULT_COORD_Y;

    private final static Boolean est_plat = true;
    Bordure_Plate(int indice) {// indice correspondant a HAUT DROITE BAS GAUCHE
        super(est_plat);
        fill_liste_cercle(indice);
    }
    //rempli la liste de cercle pour le lineTo
    private void fill_liste_cercle(int indice) {
        assert (indice != Piece.HAUT);
        //on a que deux cercle a remplir
        //le premier point correspond a celui  des coord de positione de la bordure
        this.liste_cercle.get(0).setCenterX(posX);
        this.liste_cercle.get(0).setCenterY(posY);
        //le second depends de la largeur ou de la longuer de la piece
        if (indice == Piece.HAUT || indice == Piece.BAS) {
            this.liste_cercle.get(1).setCenterX(posX + TAILLE_COTE_PIECE_LONGUEUR);
            this.liste_cercle.get(1).setCenterX(posY); //meme ligne que cercle 0
        }
        else  {
            this.liste_cercle.get(1).setCenterX(posX);//sur la meme "colonne" que cercle 0
            this.liste_cercle.get(1).setCenterX(posY + TAILLE_COTE_PIECE_HAUTEUR); //meme ligne que cercle 1
        }
    }
}
