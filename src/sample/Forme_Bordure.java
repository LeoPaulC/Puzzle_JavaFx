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
    final static double DEFAULT_COORD_X = 0;
    final static double DEFAULT_COORD_Y = 0;

    private final static int NB_CERCLE_BORDURE = 7;
    private final static int NB_CERCLE_CONTROLE_BORDURE = (NB_CERCLE_BORDURE-1)*2;
    static double TAILLE_COTE_PIECE_HAUTEUR = 50;
    static double TAILLE_COTE_PIECE_LONGUEUR = 50;
    static double MIN_TAILLE = 20 ; // taille minimale entre

    private double longueur_appendice;
    private double hauteur_appendice;


    // angle de rotation des cercles de controle pour deformation de cadre (locale)
    private double angle1 = 0.0;
    private double angle2 = 0.0;

    ArrayList<Circle> liste_cercle ;
    ArrayList<Circle> liste_cercle_controle ;
    ArrayList<CubicCurveTo> liste_cubicCurveTo ;
    ArrayList<MoveTo> liste_Moveto ;
    Path notre_path ;
    LineTo lineTo ;
    Boolean est_plat;


    Forme_Bordure(Boolean est_plat) {
        //si on ne precise pas les coord alors on les mets a 0 par defaut
        this.est_plat = est_plat;
        if (!est_plat) { // coté avec creux et ou dents
            // Création de notre liste de cercle
            liste_cercle = new ArrayList<>();
            for (int i = 0; i < NB_CERCLE_BORDURE; i++) { // 7 cercles
                liste_cercle.add(new Circle(10, Color.RED));
            }
            // Création de notre liste de cercle de controle
            liste_cercle_controle = new ArrayList<>();
            for (int i = 0; i < NB_CERCLE_CONTROLE_BORDURE; i += 2) { // 12 points de controles
                liste_cercle_controle.add(new Circle(10, Color.BLUE)); // premier point de controle en bleu
                liste_cercle_controle.add(new Circle(10, Color.GREEN)); // deuxieme en vert
            }
            // creation de notre liste de cubicCurveTo
            liste_cubicCurveTo = new ArrayList<>();
            for (int i = 0; i < NB_CERCLE_BORDURE - 1; i++) { // 6 Curves
                liste_cubicCurveTo.add(new CubicCurveTo());
            }
            liste_Moveto = new ArrayList<>();
            for (int i = 0; i < NB_CERCLE_BORDURE - 1; i++) { // 6 MoveTo pour aller avec les 6 Curves
                liste_Moveto.add(new MoveTo());
            }
            notre_path = new Path();
        } else {
            liste_cercle = new ArrayList<>();
            liste_cercle.add(new Circle(10, Color.PINK));
            liste_cercle.add(new Circle(10, Color.GREEN));
            lineTo = new LineTo();
        }
    }



    public static double getTailleCotePieceHauteur() {
        return TAILLE_COTE_PIECE_HAUTEUR;
    }

    public static void setTailleCotePieceHauteur(double tailleCotePieceHauteur) {
        TAILLE_COTE_PIECE_HAUTEUR = tailleCotePieceHauteur;
    }

    public static double getTailleCotePieceLongueur() {
        return TAILLE_COTE_PIECE_LONGUEUR;
    }

    public static void setTailleCotePieceLongueur(double tailleCotePieceLongueur) {
        TAILLE_COTE_PIECE_LONGUEUR = tailleCotePieceLongueur;
    }

    public static double getMinTaille() {
        return MIN_TAILLE;
    }

    public static void setMinTaille(double minTaille) {
        MIN_TAILLE = minTaille;
    }

    public ArrayList<Circle> getListe_cercle() {
        return liste_cercle;
    }

    public void setListe_cercle(ArrayList<Circle> liste_cercle) {
        this.liste_cercle = liste_cercle;
    }

    public ArrayList<Circle> getListe_cercle_controle() {
        return liste_cercle_controle;
    }

    public void setListe_cercle_controle(ArrayList<Circle> liste_cercle_controle) {
        this.liste_cercle_controle = liste_cercle_controle;
    }

    public ArrayList<CubicCurveTo> getListe_cubicCurveTo() {
        return liste_cubicCurveTo;
    }

    public void setListe_cubicCurveTo(ArrayList<CubicCurveTo> liste_cubicCurveTo) {
        this.liste_cubicCurveTo = liste_cubicCurveTo;
    }

    public double getAngle1() {
        return angle1;
    }

    public void setAngle1(double angle1) {
        this.angle1 = angle1;
    }

    public double getAngle2() {
        return angle2;
    }

    public void setAngle2(double angle2) {
        this.angle2 = angle2;
    }
    public ArrayList<MoveTo> getListe_Moveto() {
        return liste_Moveto;
    }

    public void setListe_Moveto(ArrayList<MoveTo> liste_Moveto) {
        this.liste_Moveto = liste_Moveto;
    }

    public LineTo getListe_plate() {
        return lineTo;
    }

    public void setListe_plate(LineTo liste_plate) {
        this.lineTo = liste_plate;
    }

    public Boolean getEst_plat() {
        return est_plat;
    }
    public void setEst_plat(Boolean est_plat) {
        this.est_plat = est_plat;
    }

    public Path getNotre_path() {
        return notre_path;
    }

    public void setNotre_path(Path notre_path) {
        this.notre_path = notre_path;
    }

    public static int getNbCercleBordure() {
        return NB_CERCLE_BORDURE;
    }

    public static int getNbCercleControleBordure() {
        return NB_CERCLE_CONTROLE_BORDURE;
    }


}