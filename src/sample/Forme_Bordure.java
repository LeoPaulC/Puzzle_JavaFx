package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
/**Contraintes
 *   |o
 *  o|_ _ _ _ _ _ _ >x
 *   |
 *   |
 *   |
 *   |
 * y v
 * liste des cercles d'une dent ou d'un creux = {c0, ..., c6} (ceux des courbes)
 *  on place c0 et c6 en fonction de la piece
 *  pour i = 0, ... , 6 , ci:(xi,yi)
 *  pour i = 0 et i = 6 contrainte lié avec la position de la piece etc
 *  donc x0,x6,y0,y6 fixé
 *  min_taille creux/dents
 *  limit_droite = x6-min_taille
 *  limit_gauche = x0+min_taille
 *  pour i = 1
 *      x0 <= x1 < limit_droite , y1 < y0
 *  pour i = 2
 *      x1 <= x2 < limit_droite , y2 < y1
 *  pour i = 3
 *      x0 <= x3 <= x6 , y2 < y3
 *  pour i = 4
 *      x3 <= x4 , y3 < y4
 *  pour i = 5
 *      limit_gauche < x5 <= x4 , y4 < y5
 * */

public class Forme_Bordure extends Shape {

    static final int NB_CERCLE_PAR_BORDURE = 7 ;
    static int TAILLE_COTE_PIECE_HAUTEUR = 200;
    static int TAILLE_COTE_PIECE_LONGUEUR = 200;
    static int MIN_TAILLE = 20 ; // taille minimale entre

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
