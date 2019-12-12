package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;


/**
 * Finir Creux( Creux c)
 * Faire Creux() -->avc LP ?
 * finir de gerer la construction d'une piece en fonctione de la liste de bordure passee en parametre
 *
 * */


public class Creux extends Forme_Bordure {
    private Shape c ;
    //pas de coord x et y ici pcq CREUX est instancié via une Dents
    private final static Boolean est_plat = false;
    public Creux() {// ici on genere notre propre "dents"
        super(est_plat);
        Dents d = new Dents();
        new Creux(d);
        ajout_couleur_cercle();
    }
    public Creux(Creux c) {
        super(est_plat);
        copie_Coordonnee(c.liste_cercle,c.liste_cercle_controle);
        cercle_Vers_Courbe();//utile que pour les tests de desssinner la bordure
        ajout_couleur_cercle();
    }
    private void copie_Coordonnee(ArrayList<Circle> liste_cercle,ArrayList<Circle> liste_controleurs) {
        for (int i = 0; i < liste_cercle.size(); i++) {
            Circle c = this.liste_cercle.get(i);
            c.setLayoutY(liste_cercle.get(i).getLayoutY());
            c.setLayoutX(liste_cercle.get(i).getLayoutX());
        }
        for (int i = 0; i < liste_cercle_controle.size() ; i++) {
            Circle c = this.liste_cercle_controle.get(i);
            c.setLayoutY(liste_cercle.get(i).getLayoutY());
            c.setLayoutX(liste_controleurs.get(i).getLayoutX());
        }
    }

    // creer un creux a partir des cercle d'une dents de la piece voisine sans aucune operation desssus
    public Creux(ArrayList<Circle> liste1, ArrayList<Circle> liste2) {
        super(est_plat);
        this.liste_cercle = liste1;
        this.liste_cercle_controle = liste2;
        ajout_couleur_cercle();
    }// pas besoin de faire des transformation pour le placer dans la piece

    public Creux(Dents d, int cote) {
        super(est_plat);
        if (cote == Piece.HAUT || cote == Piece.BAS) {
            inversion_Hauteur(d.liste_cercle,d.liste_cercle_controle);

        }else{
            inversion_Hauteur2(d.liste_cercle,d.liste_cercle_controle);
        }
        ajout_couleur_cercle();
    }
    public Creux(Dents d) {
        super(est_plat);
        this.setAngle1(d.getAngle1());
        this.setAngle2(d.getAngle2());
        inversion_Hauteur(d.liste_cercle,d.liste_cercle_controle);
        ajout_couleur_cercle();
    }
    private void ajout_couleur_cercle() {
       /* this.liste_cercle.get(0).setFill(Color.GOLD);
        this.liste_cercle.get(1).setFill(Color.ORANGE);
        this.liste_cercle.get(2).setFill(Color.RED);
        this.liste_cercle.get(3).setFill(Color.PINK);
        this.liste_cercle.get(4).setFill(Color.PURPLE);
        this.liste_cercle.get(5).setFill(Color.DARKBLUE);
        this.liste_cercle.get(6).setFill(Color.GOLD);
        */
        this.liste_cercle.get(0).setFill(Color.RED);
        this.liste_cercle.get(1).setFill(Color.RED);
        this.liste_cercle.get(2).setFill(Color.RED);
        this.liste_cercle.get(3).setFill(Color.GREEN);
        this.liste_cercle.get(4).setFill(Color.GREEN);
        this.liste_cercle.get(5).setFill(Color.BLUE);
        this.liste_cercle.get(6).setFill(Color.BLUE);
    }
    //cree des courbes a partir des 2 listes de cercles
    private void cercle_Vers_Courbe() {
        for (int i = 0; i < this.liste_cubicCurveTo.size() ; i++) {

            this.liste_cubicCurveTo.get(i).xProperty().bind(this.liste_cercle.get(i).layoutXProperty());
            this.liste_cubicCurveTo.get(i).yProperty().bind(this.liste_cercle.get(i).layoutYProperty());

            this.liste_cubicCurveTo.get(i).controlX1Property().bind(this.liste_cercle_controle.get(2*i+1).layoutXProperty() );
            this.liste_cubicCurveTo.get(i).controlY1Property().bind(this.liste_cercle_controle.get(2*i+1).layoutYProperty());

            this.liste_cubicCurveTo.get(i).controlX2Property().bind(this.liste_cercle_controle.get(2*i).layoutXProperty());
            this.liste_cubicCurveTo.get(i).controlY2Property().bind(this.liste_cercle_controle.get(2*i).layoutYProperty());

            this.liste_Moveto.get(i).xProperty().bind(this.liste_cercle.get(i+1).layoutXProperty());
            this.liste_Moveto.get(i).yProperty().bind(this.liste_cercle.get(i+1).layoutYProperty());

            notre_path.getElements().add(this.liste_Moveto.get(i));
            notre_path.getElements().add(this.liste_cubicCurveTo.get(i));
        }
    }

    private void inversion_Hauteur2(ArrayList<Circle> liste_cercle,ArrayList<Circle> liste_controleurs) {
        for (int i = 0; i < liste_cercle.size(); i++) {
            Circle c = this.liste_cercle.get(i);
            //on addittionne 2 fois la difference entre le y de c0 et le y de i
            // pour effectuer une symetrie verticale de ci avec l'axe c0;c6
            c.setLayoutX(liste_cercle.get(i).getLayoutX() + 2 * (liste_cercle.get(0).getLayoutX() - liste_cercle.get(i).getLayoutX()));
            //on affecte la psition en x sans la changer
            c.setLayoutY(liste_cercle.get(i).getLayoutY());
        }
        for (int i = 0; i < liste_cercle_controle.size() ; i++) {
            Circle c = this.liste_cercle_controle.get(i);
            //on addittionne 2 fois la difference entre le y de c0 et le y de i
            // pour effectuer une symetrie verticale de ci avec l'axe c0;c6
            c.setLayoutX(liste_controleurs.get(i).getLayoutX() + 2 * (liste_cercle.get(0).getLayoutX() - liste_controleurs.get(i).getLayoutX()));
            //on affecte la psition en x sans la changer
            c.setLayoutY(liste_controleurs.get(i).getLayoutY());
        }
    }

    // on inverse la hauteur de chaque point par rapport a
    // la hauteur des points cercle 0 et 6 de liste_cercle
    // == symetrie verticale par rapport a l'axe c0;c6
    private void inversion_Hauteur(ArrayList<Circle> liste_cercle,ArrayList<Circle> liste_controleurs) {
        for (int i = 0; i < liste_cercle.size(); i++) {
            Circle c = this.liste_cercle.get(i);
            //on addittionne 2 fois la difference entre le y de c0 et le y de i
            // pour effectuer une symetrie verticale de ci avec l'axe c0;c6
            c.setLayoutY(liste_cercle.get(i).getLayoutY() + 2 * (liste_cercle.get(0).getLayoutY() - liste_cercle.get(i).getLayoutY()));
            //on affecte la psition en x sans la changer
            c.setLayoutX(liste_cercle.get(i).getLayoutX());
            this.liste_cercle.get(i).setLayoutX(c.getLayoutX());
            this.liste_cercle.get(i).setLayoutY(c.getLayoutY());
        }
        for (int i = 0; i < liste_cercle_controle.size() ; i++) {
            Circle c = this.liste_cercle_controle.get(i);
            //on addittionne 2 fois la difference entre le y de c0 et le y de i
            // pour effectuer une symetrie verticale de ci avec l'axe c0;c6
            c.setLayoutY(liste_controleurs.get(i).getLayoutY() + 2 * (liste_cercle.get(0).getLayoutY() - liste_controleurs.get(i).getLayoutY()));
            //on affecte la psition en x sans la changer
            c.setLayoutX(liste_controleurs.get(i).getLayoutX());
            this.liste_cercle_controle.get(i).setLayoutX(c.getLayoutX());
            this.liste_cercle_controle.get(i).setLayoutY(c.getLayoutY());
        }
    }


}

