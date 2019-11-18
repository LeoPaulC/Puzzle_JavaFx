package sample;

public class Bordure_Plate extends Forme_Bordure {
    double posX = DEFAULT_COORD_X;
    double posY = DEFAULT_COORD_Y;
    private final static Boolean est_plat = true;


    public Bordure_Plate(int indice, double posX, double posY) {
        super(est_plat);
        //System.out.println("je suis dans bordure plate ind x y ");
        this.posX = posX;
        this.posY = posY;
        fill_liste_cercle(indice);
    }
    Bordure_Plate(int indice) {// indice correspondant a HAUT DROITE BAS GAUCHE
        super(est_plat);
        fill_liste_cercle(indice);
    }
    //rempli la liste de cercle pour le lineTo
    private void fill_liste_cercle(int indice) {
        System.out.println("val indice : "+indice);
        if (indice > Piece.GAUCHE) {
            System.out.println("indice de bordure plate INCORRECTE");
            return ;
        }
        if (indice == Piece.HAUT ) {

            this.liste_cercle.get(0).setLayoutX(posX);
            this.liste_cercle.get(0).setLayoutY(posY);

            this.liste_cercle.get(1).setLayoutX(posX + TAILLE_COTE_PIECE_LONGUEUR);
            this.liste_cercle.get(1).setLayoutY(posY); //meme ligne que cercle 0

        } else if (indice == Piece.BAS) {

            this.liste_cercle.get(0).setLayoutX(posX + TAILLE_COTE_PIECE_LONGUEUR);
            this.liste_cercle.get(0).setLayoutY(posY + TAILLE_COTE_PIECE_HAUTEUR);

            this.liste_cercle.get(1).setLayoutX(posX);
            this.liste_cercle.get(1).setLayoutY(posY + TAILLE_COTE_PIECE_HAUTEUR);

        } else if (indice == Piece.DROITE) {

            this.liste_cercle.get(0).setLayoutX(posX + TAILLE_COTE_PIECE_LONGUEUR);
            this.liste_cercle.get(0).setLayoutY(posY);

            this.liste_cercle.get(1).setLayoutX(posX + TAILLE_COTE_PIECE_LONGUEUR);
            this.liste_cercle.get(1).setLayoutY(posY + TAILLE_COTE_PIECE_HAUTEUR);

        }else{

            this.liste_cercle.get(0).setLayoutX(posX);
            this.liste_cercle.get(0).setLayoutY(posY + TAILLE_COTE_PIECE_HAUTEUR);

            this.liste_cercle.get(1).setLayoutX(posX);
            this.liste_cercle.get(1).setLayoutY(posY);
        }
    }
}
