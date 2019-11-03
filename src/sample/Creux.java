package sample;

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
/** faire Creux () , la juste pour faire piece pour l'instant*/
    public Creux() {
        super(false);

    }
    public Creux(Creux c) {
        super(false);
        //this.liste_cercle = c.liste_cercle;
        //this.liste_cercle_controle = d.getListe_cercle_controle();
        copie_Coordonnee(c.liste_cercle,c.liste_cercle_controle);
        cercle_Vers_Courbe();
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

    public Creux(Dents d) {
        super(false);
        //this.liste_cercle = d.liste_cercle;
        //this.liste_cercle_controle = d.getListe_cercle_controle();
        inversion_Hauteur(d.liste_cercle,d.liste_cercle_controle);
        cercle_Vers_Courbe();

    }
    //cree des courbes a partir des 2 listes de cercles
    private void cercle_Vers_Courbe() {
        for (int i = 0; i < this.liste_cubicCurveTo.size() ; i++) {

            //this.liste_cubicCurveTo.get(i).setX(this.liste_cercle.get(i).getLayoutX());
            //this.liste_cubicCurveTo.get(i).setY(this.liste_cercle.get(i).getLayoutY());
            this.liste_cubicCurveTo.get(i).xProperty().bind(this.liste_cercle.get(i).layoutXProperty());
            this.liste_cubicCurveTo.get(i).yProperty().bind(this.liste_cercle.get(i).layoutYProperty());

            this.liste_cubicCurveTo.get(i).controlX1Property().bind(this.liste_cercle_controle.get(2*i+1).layoutXProperty() );
            this.liste_cubicCurveTo.get(i).controlY1Property().bind(this.liste_cercle_controle.get(2*i+1).layoutYProperty());

            this.liste_cubicCurveTo.get(i).controlX2Property().bind(this.liste_cercle_controle.get(2*i).layoutXProperty());
            this.liste_cubicCurveTo.get(i).controlY2Property().bind(this.liste_cercle_controle.get(2*i).layoutYProperty());

            //this.liste_Moveto.get(i).setX(this.liste_cercle.get(i+1).getLayoutX());
            //this.liste_Moveto.get(i).setY(this.liste_cercle.get(i+1).getLayoutY());
            this.liste_Moveto.get(i).xProperty().bind(this.liste_cercle.get(i+1).layoutXProperty());
            this.liste_Moveto.get(i).yProperty().bind(this.liste_cercle.get(i+1).layoutYProperty());

            notre_path.getElements().add(this.liste_Moveto.get(i));
            notre_path.getElements().add(this.liste_cubicCurveTo.get(i));
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
        }
        for (int i = 0; i < liste_cercle_controle.size() ; i++) {
            Circle c = this.liste_cercle_controle.get(i);
            //on addittionne 2 fois la difference entre le y de c0 et le y de i
            // pour effectuer une symetrie verticale de ci avec l'axe c0;c6
            c.setLayoutY(liste_controleurs.get(i).getLayoutY()+2*(liste_cercle.get(0).getLayoutY()-liste_controleurs.get(i).getLayoutY()));
            //on affecte la psition en x sans la changer
            c.setLayoutX(liste_controleurs.get(i).getLayoutX());
        }
    }


}

