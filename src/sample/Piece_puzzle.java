package sample;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.effect.Light;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.ArrayList;
import java.util.Random;

public class Piece_puzzle {

    Group ma_Forme_de_piece;

    ArrayList<Circle> points_du_cadre; // 8 necessaire , 4 angle et 4 creux/dents
    ArrayList<CubicCurveTo> ligne; // ligne formant les contour de la piece.
    ArrayList<Circle> point_de_controle; // liste permettant d'avoir acces aux 16 points de controle par piece
    ArrayList<MoveTo> ma_liste_de_move_to;

    int nb_creux;
    int nb_dents;

    double taille_piece_longueur; // piece en forme de rectangle , obligé d'avoir une dimension ;
    double taille_piece_hauteur; // piece en forme de rectangle , obligé d'avoir une dimension ;

    //§!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // MEMO
    //§!!!!!!!!!!!!!!!!!!!!!!!!!
    /*
    ---------------> longueur ( en pixel )
    |
    |
    |
    |
    v
  hauteur ( en pixel )

     */
    Piece_puzzle(int nb_creux, int nb_dents, double longueur, double hauteur) {
        this.nb_creux = nb_creux;
        this.nb_dents = nb_dents;
        this.taille_piece_hauteur = hauteur;
        this.taille_piece_longueur = longueur;

        points_du_cadre = new ArrayList<>();
        ligne = new ArrayList<>();
        point_de_controle = new ArrayList<>();
        ma_liste_de_move_to = new ArrayList<>();


        int nb_restant_de_creux_a_faire = nb_creux;
        int nb_restant_de_dents_a_faire  = nb_dents;

        double decalage_pour_creux_dents = 0 ;

        // creation des points du cadre
        for (int i = 0; i < 24; i++) {

            int random = new Random().nextInt((int) 100 ) ; // facteur de decalage des creux et dents , + ou - .
            if (  random % 4 == 3 ) decalage_pour_creux_dents = -50 ;
            if ( random % 4 == 2 ) decalage_pour_creux_dents = +50 ;

            points_du_cadre.add(new Circle(6, Color.RED)); // se place en 0,0

            if ( i== 0 ) {
                points_du_cadre.get(i).setLayoutX(100);
                points_du_cadre.get(i).setLayoutY(100);
            }

            if (i >= 1 && i <= 6) // correspondt aux 3 point du haut de la piece
            {
                points_du_cadre.get(i).setLayoutX(longueur / 6 + points_du_cadre.get(i - 1).getLayoutX());
                points_du_cadre.get(i).setLayoutY(points_du_cadre.get(i - 1).getLayoutY());
            }
            if (i >= 7 && i <= 12)// correspont aux 2 point a droite
            {
                points_du_cadre.get(i).setLayoutX(points_du_cadre.get(i - 1).getLayoutX());
                points_du_cadre.get(i).setLayoutY(longueur / 6 + points_du_cadre.get(i - 1).getLayoutY());
            }
            if (i >= 13 && i <= 19)// correspont aux 2 point a droite
            {
                points_du_cadre.get(i).setLayoutY(points_du_cadre.get(i - 1).getLayoutY());
                points_du_cadre.get(i).setLayoutX(points_du_cadre.get(i - 1).getLayoutX() - longueur / 6);
            }
            if (i >= 19)// correspont aux 2 point a droite
            {
                points_du_cadre.get(i).setLayoutX(points_du_cadre.get(i - 1).getLayoutX());
                points_du_cadre.get(i).setLayoutY(points_du_cadre.get(i - 1).getLayoutY() - longueur / 6);
            }
        }

        for (int i = 0; i < 48; i+=2) {
            Circle mon_clercle = new Circle(10, Color.BLUE);
            mon_clercle.setLayoutX(points_du_cadre.get(i/2).getLayoutX()+20);
            mon_clercle.setLayoutY(points_du_cadre.get(i/2).getLayoutY()+20);

            point_de_controle.add(mon_clercle);
            mon_clercle = new Circle(10, Color.GREEN);
            mon_clercle.setLayoutX(points_du_cadre.get(i/2).getLayoutX()+60);
            mon_clercle.setLayoutY(points_du_cadre.get(i/2).getLayoutY()+60);

            point_de_controle.add(mon_clercle);
        }

        // creation des lignes
        int j = 0;
        for (int i = 0; i < 24; i++) {
            j = i + 1; // va permettre de relier le dernier point au premier
            if (j % 24 == 0) j = 0; // place le dernier point relié au premier

            CubicCurveTo maligne = new CubicCurveTo();
            maligne.xProperty().bind(points_du_cadre.get(j).layoutXProperty());
            maligne.yProperty().bind(points_du_cadre.get(j).layoutYProperty());

            maligne.controlX1Property().bind(point_de_controle.get(i*2).layoutXProperty());
            maligne.controlY1Property().bind(point_de_controle.get(i*2).layoutYProperty());

            maligne.controlX2Property().bind(point_de_controle.get(i*2+1).layoutXProperty());
            maligne.controlY2Property().bind(point_de_controle.get(i*2+1).layoutYProperty());


            MoveTo moveTo = new MoveTo();
            moveTo.xProperty().bind(points_du_cadre.get(i).layoutXProperty());
            moveTo.yProperty().bind(points_du_cadre.get(i).layoutYProperty());


            ma_liste_de_move_to.add(moveTo);
            ligne.add(maligne);

        }


        for (CubicCurveTo l : ligne) {
                System.out.println("CubicCurveto cree : " + l);

        }

        points_du_cadre.forEach(a -> a.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                a.setLayoutX(me.getSceneX());
                a.setLayoutY(me.getSceneY());
            }
        }));
        point_de_controle.forEach(a -> a.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                a.setLayoutX(me.getSceneX());
                a.setLayoutY(me.getSceneY());
            }
        }));



        /*
        Rectangle test_rec = new Rectangle();
        test_rec.setX(points_du_cadre.get(0).getLayoutX());
        test_rec.setY(points_du_cadre.get(0).getLayoutY());

        test_rec.setHeight(hauteur-50);
        test_rec.setWidth(longueur-50);

        ma_Forme_de_piece = Shape.union(test_rec, new Rectangle());

         */

    }

}
