package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;

public class Forme_Bordure extends Shape {

    static final int NB_CERCLE_PAR_BORDURE = 7 ;
    static int TAILLE_COTE_PIECE_HAUTEUR = 200;
    static int TAILLE_COTE_PIECE_LONGUEUR = 200;

    ArrayList<Circle> liste_cercle ;
    ArrayList<Circle> liste_cercle_controle ;
    ArrayList<CubicCurveTo> liste_cubicCurveTo ;
    ArrayList<MoveTo> liste_Moveto ;
    LineTo liste_plate ;

    Boolean est_plat ;

    Path notre_path ;

    Forme_Bordure(Boolean est_plat){
        if ( !est_plat ) { // coté avec creux et ou dents
            // Création de notre liste de cercle
            liste_cercle = new ArrayList<>();
            for (int i = 0; i < NB_CERCLE_PAR_BORDURE; i++) { // 7 cercles
                liste_cercle.add(new Circle(10, Color.RED));
            }
            // Création de notre liste de cercle de controle
            liste_cercle_controle = new ArrayList<>();
            for (int i = 0; i < (NB_CERCLE_PAR_BORDURE - 1) * 2; i += 2) { // 12 points de controles
                liste_cercle.add(new Circle(10, Color.BLUE)); // premier point de controle en bleu
                liste_cercle.add(new Circle(10, Color.GREEN)); // deuxieme en vert
            }
            // creation de notre liste de cubicCurveTo
            liste_cubicCurveTo = new ArrayList<>();
            for (int i = 0; i < NB_CERCLE_PAR_BORDURE - 1; i++) { // 6 Curves
                liste_cubicCurveTo.add(new CubicCurveTo());
            }
            liste_Moveto = new ArrayList<>();
            for (int i = 0; i < NB_CERCLE_PAR_BORDURE - 1; i++) { // 6 MoveTo pour aller avec les 6 Curves
                liste_Moveto.add(new MoveTo());
            }
            notre_path = new Path();
        }
        else {
            liste_cercle = new ArrayList<>();
            liste_cercle.add( new Circle(10,Color.GOLD) ) ;
            liste_cercle.add(new Circle(10 ,Color.GOLD) ) ;
            liste_plate = new LineTo();
        }
    }
}
