package sample;

import javafx.beans.binding.NumberBinding;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import javax.management.monitor.GaugeMonitor;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static sample.Main.plateau;
import static sample.Main.tab_pane;

/**
 * il
 * nous
 * faut
 * une
 * position
 * de
 * depart !!!!!!
 */
public class Piece extends Shape {

    // on calcule toutes les coordonnéées d'une piece par rapport au xC0 et yC0 de la bordure du Haut .

    /**
     * 0 -> haut
     * 1 -> droite
     * 2 -> bas
     * 3 -> gauche
     */
    static final int HAUT = 0;
    static final int DROITE = 1;
    static final int BAS = 2;
    static final int GAUCHE = 3;
    static final int TAB_ANGLE_ROTATION[] = {0, 270, 180, 90};
    static final int TAB_INDICE_PIVOT[] = {0, 0, 0, 0};//on ne prend pas le 6eme pcq bordure plate n'a que 2 cercle
    static final int TAB_COEFFICIENT_TRANSLATION_X[] = {0, 1, 1, 0};
    static final int TAB_COEFFICIENT_TRANSLATION_Y[] = {0, 1, 1, 0};
    static final int TAB_COEFFICIENT_TRANSLATION_X_PLAT[] = {0, 1, 1, 0};
    static final int TAB_COEFFICIENT_TRANSLATION_Y_PLAT[] = {0, 0, 1, 1};
    static final int NOMBRE_COTE = 4;
    static final double MIN_LONGUEUR = 30;
    static final double MIN_HAUTEUR = 30;
    private static final boolean DEFAULT_ISMOVABLE = true;
    private double posX = 0;
    private double posY = 0;
    private static final int DEFAULT_NIVEAU = 1;
    private int niveau = DEFAULT_NIVEAU;

    //utilisé uniquement dans les anciennes methodes morray
    ArrayList<Circle> liste_cercle;
    private int cpt_cercle = 0; //compteur sur la liste de cercle
    ArrayList<Circle> liste_cercle_controle;
    private int cpt_cercle_controle = 0; //compteur sur la liste de cercle de controle
    ArrayList<CubicCurveTo> liste_courbe;
    Forme_Bordure[] tab_bordure;

    Rectangle rectangle;
    Shape forme;
    Path path;
    ArrayList<PathElement> liste_shape;
    double hauteur;
    double longueur;

    private double oldX;
    private double oldY;

    protected Pane panneau;
    protected boolean isMovable = DEFAULT_ISMOVABLE; // nous dit si la piece peut etre bougée ou non
    protected Piece piece_ombre =null;

    protected double translationX;
    protected double translationY;

    public Piece() {

    }

    public Piece(Piece p) {
        this.tab_bordure = new Forme_Bordure[NOMBRE_COTE];
        path = new Path();
        liste_cercle_controle = new ArrayList<Circle>();
        liste_cercle = new ArrayList<Circle>();
        liste_courbe = new ArrayList<CubicCurveTo>();
        liste_shape = new ArrayList<PathElement>();
        fill_tab_bordure(p.tab_bordure);
        copie_listes_cercle(p);
        //fill_liste_cercle();// pas besoin de l'inversion presente dans cette fonction
        create_shapes();
        //forme = Shape.union(path, new Circle(0));
        //forme.layoutXProperty().bindBidirectional(path.layoutXProperty());
        //forme.layoutYProperty().bindBidirectional(path.layoutYProperty());
    }
    public Piece(Piece p, Pane pane) {
        this.tab_bordure = new Forme_Bordure[NOMBRE_COTE];
        path = new Path();
        liste_cercle_controle = new ArrayList<Circle>();
        liste_cercle = new ArrayList<Circle>();
        liste_courbe = new ArrayList<CubicCurveTo>();
        liste_shape = new ArrayList<PathElement>();
        this.panneau = pane;
        fill_tab_bordure(p.tab_bordure);
        copie_listes_cercle(p);
        create_shapes();
        //Ajouter_evenement();
    }
    // renvoie la coordonnee x du point de la piece situé le plus a gauche
    protected double getMinX() {
        double min = tab_bordure[0].liste_cercle.get(0).getLayoutX();
        for (Forme_Bordure bordure : tab_bordure) {
            for (Circle circle : bordure.liste_cercle) {
                if (circle.getLayoutX() < min) {
                    min = circle.getLayoutX();
                }
            }
        }
        return min;
    }
    // renvoie la coordonnee x du point de la piece situé le plus a gauche
    protected double getMaxX() {
        double max = tab_bordure[0].liste_cercle.get(0).getLayoutX();
        for (Forme_Bordure bordure : tab_bordure) {
            for (Circle circle : bordure.liste_cercle) {
                if (circle.getLayoutX() > max) {
                    max = circle.getLayoutX();
                }
            }
        }
        return max;
    }
    // renvoie la coordonnee y du point de la piece situé le plus BAS
    protected double getMinY() {
        double min = tab_bordure[0].liste_cercle.get(0).getLayoutY();
        for (Forme_Bordure bordure : tab_bordure) {
            for (Circle circle : bordure.liste_cercle) {
                if (circle.getLayoutY() < min) {
                    min = circle.getLayoutY();
                }
            }
        }
        return min;
    }
    // renvoie la coordonnee y du point de la piece situé le plus HAUT
    protected double getMaxY() {
        double max = tab_bordure[0].liste_cercle.get(0).getLayoutY();
        for (Forme_Bordure bordure : tab_bordure) {
            for (Circle circle : bordure.liste_cercle) {
                if (circle.getLayoutY() > max) {
                    max = circle.getLayoutY();
                }
            }
        }
        return max;
    }

    protected void MAJ_Forme() {
        forme.setLayoutX(path.getLayoutX());
        forme.setLayoutY(path.getLayoutY());
        //forme = Shape.union(path, new Circle(0));
    }

    private void copie_listes_cercle(Piece p) {
        this.liste_cercle = (ArrayList<Circle>) p.liste_cercle.clone();
        this.liste_cercle_controle = (ArrayList<Circle>) p.liste_cercle_controle.clone();
    }

    private void fill_tab_bordure(Forme_Bordure[] tab_bordure) {
        for (int i = 0; i < NOMBRE_COTE; i++) {
            if (tab_bordure[i].getClass() == Dents.class || tab_bordure[i].getClass() == Creux.class) {
                ArrayList<Circle> liste1 = (ArrayList<Circle>) tab_bordure[i].getListe_cercle().clone();
                ArrayList<Circle> liste2 = (ArrayList<Circle>) tab_bordure[i].getListe_cercle_controle().clone();
                if (tab_bordure[i].getClass() == Creux.class) {
                    this.tab_bordure[i] = new Creux(liste1, liste2);
                } else {
                    this.tab_bordure[i] = new Dents(liste1, liste2);
                }
            } else { // bordure plate
                Bordure_Plate b = (Bordure_Plate)tab_bordure[i];
                this.tab_bordure[i] = new Bordure_Plate(i, b.posX, b.posY, hauteur, longueur);
            }
        }
    }
    //constructeur qui permet de placer la piece dans l'espace
    public Piece(ArrayList<Forme_Bordure> liste_bordure, double x, double y, double hauteur, double longueur) {
        posX = x;
        posY = y;
        this.hauteur = hauteur;
        this.longueur = longueur;
        path = new Path();
        tab_bordure = new Forme_Bordure[NOMBRE_COTE];
        liste_cercle_controle = new ArrayList<Circle>();
        liste_cercle = new ArrayList<Circle>();
        liste_courbe = new ArrayList<CubicCurveTo>();
        liste_shape = new ArrayList<PathElement>();
        fill_tab_bordure(liste_bordure , hauteur, longueur);
        fill_liste_cercle();
        create_shapes();
    }

    //constructeur qui permet de placer la piece dans l'espace
    public Piece(ArrayList<Forme_Bordure> liste_bordure, double x, double y, double hauteur, double longueur, int niveau) {
        posX = x;
        posY = y;
        this.hauteur = hauteur;
        this.longueur = longueur;
        path = new Path();
        tab_bordure = new Forme_Bordure[NOMBRE_COTE];
        liste_cercle_controle = new ArrayList<Circle>();
        liste_cercle = new ArrayList<Circle>();
        liste_courbe = new ArrayList<CubicCurveTo>();
        liste_shape = new ArrayList<PathElement>();
        this.niveau = niveau;
        fill_tab_bordure(liste_bordure, hauteur, longueur);
        fill_liste_cercle();
        create_shapes();
        //Ajouter_evenement();
    }

    //constructeur qui permet de placer la piece dans l'espace
    public Piece(ArrayList<Forme_Bordure> liste_bordure, double x, double y, double hauteur, double longueur, int niveau, Pane pane) {
        posX = x;
        posY = y;
        this.hauteur = hauteur;
        this.longueur = longueur;
        path = new Path();
        tab_bordure = new Forme_Bordure[NOMBRE_COTE];
        liste_cercle_controle = new ArrayList<Circle>();
        liste_cercle = new ArrayList<Circle>();
        liste_courbe = new ArrayList<CubicCurveTo>();
        liste_shape = new ArrayList<PathElement>();
        this.niveau = niveau;
        this.panneau = pane;
        fill_tab_bordure(liste_bordure, hauteur, longueur);
        fill_liste_cercle();
        create_shapes();
        //Ajouter_evenement();
    }

    //prend en parametre une liste de Forme_Bordure précisant les contraintes de
    //chaque cote de la piece dans un puzzle==Plateau
    // si [ null , null , null, null ] alors aucune contrainte
    //si [ dent d, null , creux , Bordure_Plate b]
    // alors Haut s'adapte a d , Droite libre, Bas s'adapte a c et Gauche a b=ligne droite
    public Piece(ArrayList<Forme_Bordure> liste_bordure) {
        this.longueur = MIN_LONGUEUR;
        this.hauteur = MIN_HAUTEUR;
        path = new Path();
        tab_bordure = new Forme_Bordure[NOMBRE_COTE];
        liste_cercle_controle = new ArrayList<Circle>();
        liste_cercle = new ArrayList<Circle>();
        liste_courbe = new ArrayList<CubicCurveTo>();
        liste_shape = new ArrayList<PathElement>();
        Gestion_Placement_Bordure();
        fill_liste_cercle();
        create_shapes();
    }


    /** Gerer la prise en compte des bordure de contraintes !!!! */
    private void fill_tab_bordure(ArrayList<Forme_Bordure> liste_bordure, double hauteur, double longueur) {
        for (int i = 0; i < NOMBRE_COTE; i++) {
            if (liste_bordure.get(i) == null) {
                //si pas de contrainte alors on met une dent
                // ou un creux mais pas de bordure plate --- pcq pas de bord
                tab_bordure[i] = randoms_Bordure(i, hauteur , longueur,liste_bordure);
                gestion_placement(tab_bordure[i],i);
            } else if (liste_bordure.get(i).getClass() == Dents.class) {
                //Main.consumer.accept("listebordure.get(i).class == Dents");
                ArrayList<Circle> liste1 = liste_bordure.get(i).getListe_cercle();
                ArrayList<Circle> liste2 = liste_bordure.get(i).getListe_cercle_controle();
                if (i == BAS || i == HAUT) {
                    Collections.reverse(liste1);
                    Collections.reverse(liste2);
                }
                tab_bordure[i]= new Creux(liste1,liste2); // pas besoin de la placer normalement

            } else if (liste_bordure.get(i).getClass() == Creux.class) {
                //Main.consumer.accept("listebordure.get(i).class == Creux");
                ArrayList<Circle> liste1 = liste_bordure.get(i).getListe_cercle();
                ArrayList<Circle> liste2 = liste_bordure.get(i).getListe_cercle_controle();
                if (i == BAS || i == HAUT) {
                    Collections.reverse(liste1);
                    Collections.reverse(liste2);
                }
                tab_bordure[i]= new Dents(liste1, liste2);
            } else if (liste_bordure.get(i).getClass() == Bordure_Plate.class) {
                tab_bordure[i] = new Bordure_Plate(i, posX ,posY, hauteur, longueur);
                gestion_placement(tab_bordure[i],i);
                //true pcq la bordure est plate
            }
        }
    }


    //creer les shapes ( CubicCurveTo ou Lineto) en fonction des cercles de la liste de cercle
    private void create_shapes() {
        //affichage_coord_liste(this.liste_cercle);
        doMoveTo();// creation du moveTo
        for (int i = 0; i < NOMBRE_COTE; i++) { // creation des shapes correspondant aux bordures
            if (tab_bordure[i].getEst_plat()) { // BOrdure_Plate
                doLineTo_inShapes();// pas de cercle de controle
            }
            else{ //si Creux ou Dents
                doCurve_inShapes();
            }
        }
        doClosePath();
    }

    private void doClosePath() {
        path.getElements().add(new ClosePath());
        path.setFillRule(FillRule.EVEN_ODD);
        path.setFill(new ImagePattern(new Image("file:index.jpeg"), Forme_Bordure.getTailleCotePieceLongueur(), Forme_Bordure.getTailleCotePieceHauteur(), Forme_Bordure.getTailleCotePieceLongueur(), Forme_Bordure.getTailleCotePieceHauteur(), false));
    }
    //cree le moveTo avec le tout premier cercle de la liste de cercle
    private void doMoveTo() {
        //cree les courbe de la piece
        MoveTo moveTo = new MoveTo();
        moveTo.xProperty().bind(this.liste_cercle.get(cpt_cercle).layoutXProperty());
        moveTo.yProperty().bind(this.liste_cercle.get(cpt_cercle++).layoutYProperty());
        this.liste_shape.add(moveTo);
        path.getElements().add(moveTo);
    }

    private void doLineTo_inShapes() {
        LineTo lineTo = new LineTo();
        lineTo.xProperty().bind(this.liste_cercle.get(cpt_cercle).layoutXProperty());
        lineTo.yProperty().bind(this.liste_cercle.get(cpt_cercle++).layoutYProperty());
        this.liste_shape.add(lineTo);
        path.getElements().add(lineTo);
    }
    // creer les shapes a partir des cercles des liste de la piece en fonctione du
    private void doCurve_inShapes() {
        for (int i = 1; i < Forme_Bordure.getNbCercleBordure() && 2 * (i - 1) < Forme_Bordure.getNbCercleControleBordure(); i++) {
            CubicCurveTo c = new CubicCurveTo();
            c.controlX1Property().bind(this.liste_cercle_controle.get(cpt_cercle_controle).layoutXProperty());
            c.controlY1Property().bind(this.liste_cercle_controle.get(cpt_cercle_controle++).layoutYProperty());
            c.controlX2Property().bind(this.liste_cercle_controle.get(cpt_cercle_controle).layoutXProperty());
            c.controlY2Property().bind(this.liste_cercle_controle.get(cpt_cercle_controle++).layoutYProperty());
            c.xProperty().bind(this.liste_cercle.get(cpt_cercle).layoutXProperty());
            c.yProperty().bind(this.liste_cercle.get(cpt_cercle++).layoutYProperty());
            this.liste_shape.add(c);
            path.getElements().add(c);
        }

    }
    //cree les courbe de la piece
    private void create_cubiCurveTo2() {
        //cree les courbe de la piece
        MoveTo moveTo = new MoveTo();
        moveTo.xProperty().bind(this.liste_cercle.get(0).layoutXProperty());
        moveTo.yProperty().bind(this.liste_cercle.get(0).layoutYProperty());
        path.getElements().add(moveTo);
        for (int i = 1; i < this.liste_cercle.size() && 2 * (i - 1) < this.liste_cercle_controle.size(); i++) {
            CubicCurveTo c = new CubicCurveTo();
            c.controlX1Property().bind(this.liste_cercle_controle.get(2 * (i - 1)).layoutXProperty());
            c.controlY1Property().bind(this.liste_cercle_controle.get(2 * (i - 1)).layoutYProperty());
            c.controlX2Property().bind(this.liste_cercle_controle.get(2 * (i - 1) + 1).layoutXProperty());
            c.controlY2Property().bind(this.liste_cercle_controle.get(2 * (i - 1) + 1).layoutYProperty());
            c.xProperty().bind(this.liste_cercle.get(i).layoutXProperty());
            c.yProperty().bind(this.liste_cercle.get(i).layoutYProperty());
            this.liste_courbe.add(c);
            path.getElements().add(c);
        }
        path.getElements().add(new ClosePath());
        path.setFillRule(FillRule.EVEN_ODD);
        path.setFill(new ImagePattern(new Image("file:index.jpeg"), Forme_Bordure.getTailleCotePieceLongueur(), Forme_Bordure.getTailleCotePieceHauteur(), Forme_Bordure.getTailleCotePieceLongueur(), Forme_Bordure.getTailleCotePieceHauteur(), false));

    }

    //met les liste de cercles et cercle_contrle de formeBordure dans celles de piece
    //gere les doublons
    private void setListOnList(Forme_Bordure forme_bordure) {
        ArrayList<Circle> listC = new ArrayList<Circle>(forme_bordure.getListe_cercle());
        //liste cercle
        for (int i = 0; i < listC.size() - 1; i++) { //on ne met pas le dernier point pour eviter les doublons
            this.liste_cercle.add(listC.get(i));
        }
        if (!forme_bordure.getEst_plat()) {
            setListControleOnList(forme_bordure);
        }
    }

    private void setListControleOnList(Forme_Bordure forme_bordure) {
        ArrayList<Circle> listCC = new ArrayList<Circle>(forme_bordure.getListe_cercle_controle());
        //liste cercle control
        for (int i = 0; i < listCC.size(); i++) { // pas de probleme de doublons ici
            this.liste_cercle_controle.add(listCC.get(i));
        }
    }

    // remplis les liste de cercle et cerlce de coontrole de la piece
    private void fill_liste_cercle() {
        for (int i = 0; i < NOMBRE_COTE; i++) {
            if (i == DROITE || i == GAUCHE) {
                if (!tab_bordure[i].getEst_plat()) {
                    Collections.reverse(tab_bordure[i].liste_cercle);
                    Collections.reverse(tab_bordure[i].liste_cercle_controle);
                }
            }
            setListOnList(tab_bordure[i]);
        }
        //dernier point == premier point donc ajoute le tout premier point en plus
        this.liste_cercle.add(this.liste_cercle.get(0));
    }

    private Forme_Bordure randoms_Bordure(int i ,double hauteur , double longueur , ArrayList<Forme_Bordure> liste_bordure) {
        //Main.consumer.accept("dans randoms_bordure");
        Forme_Bordure bordure;
        Random random = new Random();
        int r =random.nextInt(2+ 1); // random entre
        if ( r%2 == 0) {
            if (i == DROITE) {
                double angle = liste_bordure.get(HAUT).getAngle1();
                if (liste_bordure.get(HAUT).getClass() == Dents.class) {
                    bordure = new Dents(i,posX, posY,hauteur,longueur,niveau, bordure_Plate_Possede(liste_bordure),-angle);
                }
                else {
                    bordure = new Dents(i,posX, posY,hauteur,longueur,niveau, bordure_Plate_Possede(liste_bordure),angle);
                }
            } else if (i == BAS) {
                if (tab_bordure[DROITE].getClass() == Creux.class && liste_bordure.get(GAUCHE).getClass() == Creux.class ) {
                    double angle1 = tab_bordure[DROITE].getAngle1();
                    double angle2 = liste_bordure.get(GAUCHE).getAngle1();
                    bordure = new Dents(i,posX, posY,hauteur,longueur,niveau, bordure_Plate_Possede(liste_bordure),angle1,angle2);
                }else if (tab_bordure[DROITE].getClass() == Creux.class && liste_bordure.get(GAUCHE).getClass() == Dents.class) {
                    double angle1 = tab_bordure[DROITE].getAngle1();
                    double angle2 = liste_bordure.get(GAUCHE).getAngle1();
                    bordure = new Dents(i,posX, posY,hauteur,longueur,niveau, bordure_Plate_Possede(liste_bordure),angle1,-angle2);
                } else if (tab_bordure[DROITE].getClass() == Dents.class && liste_bordure.get(GAUCHE).getClass() == Dents.class) {
                    double angle1 = tab_bordure[DROITE].getAngle1();
                    double angle2 = liste_bordure.get(GAUCHE).getAngle1();
                    bordure = new Dents(i,posX, posY,hauteur,longueur,niveau, bordure_Plate_Possede(liste_bordure),-angle1,-angle2);
                }else if( tab_bordure[DROITE].getClass()  == Dents.class && liste_bordure.get(GAUCHE).getClass() == Creux.class){
                    double angle1 = tab_bordure[DROITE].getAngle1();
                    double angle2 = liste_bordure.get(GAUCHE).getAngle1();
                    bordure = new Dents(i, posX, posY, hauteur, longueur, niveau, bordure_Plate_Possede(liste_bordure), -angle1, angle2);
                }
                else if (tab_bordure[DROITE].getClass() == Bordure_Plate.class) {
                    double angle1 = 0.0;
                    double angle2 = liste_bordure.get(GAUCHE).getAngle1();
                    if (liste_bordure.get(GAUCHE).getClass() == Creux.class){ // dents Bordure_plate
                        bordure = new Dents(i, posX, posY, hauteur, longueur, niveau, bordure_Plate_Possede(liste_bordure), angle1, angle2);
                    }
                    else { //GAUCHE == Dents
                        bordure = new Dents(i, posX, posY, hauteur, longueur, niveau, bordure_Plate_Possede(liste_bordure), angle1, -angle2);
                    }
                }
                else if ( liste_bordure.get(GAUCHE).getClass()== Bordure_Plate.class) {
                    double angle1 = tab_bordure[DROITE].getAngle1();
                    double angle2 = 0.0;
                    if ( tab_bordure[DROITE].getClass()== Creux.class){ // dents Bordure_plate
                        bordure = new Dents(i, posX, posY, hauteur, longueur, niveau, bordure_Plate_Possede(liste_bordure), angle1, angle2);
                    }
                    else { //GAUCHE == Dents
                        bordure = new Dents(i, posX, posY, hauteur, longueur, niveau, bordure_Plate_Possede(liste_bordure), -angle1, -angle2);
                    }
                }
                else{ //CAS TERMINAL !!! // faut pas arriver ici chef
                    double angle1 = 0.0;
                    double angle2 = 0.0;
                    bordure = new Dents(i, posX, posY, hauteur, longueur, niveau, bordure_Plate_Possede(liste_bordure), angle1, angle2);
                }
            }else{
                bordure = null; // si on arrive ici c'est pas bien
            }
        }
        else{
            if (i == DROITE) {
                double angle = liste_bordure.get(HAUT).getAngle1();
                if (liste_bordure.get(HAUT).getClass() == Creux.class) {
                    bordure = new Creux(new Dents(i,posX, posY,hauteur,longueur,niveau, bordure_Plate_Possede(liste_bordure),-angle));
                }
                else{
                    bordure = new Creux(new Dents(i,posX, posY,hauteur,longueur,niveau, bordure_Plate_Possede(liste_bordure),angle));
                }
            } else if (i == BAS) {
                if (tab_bordure[DROITE].getClass() == Creux.class && liste_bordure.get(GAUCHE).getClass() == Creux.class ) {
                    double angle1 = tab_bordure[DROITE].getAngle1();
                    double angle2 = liste_bordure.get(GAUCHE).getAngle1();
                    bordure = new Creux(new Dents(i,posX, posY,hauteur,longueur,niveau, bordure_Plate_Possede(liste_bordure),-angle1,-angle2));
                }else if (tab_bordure[DROITE].getClass() == Creux.class && liste_bordure.get(GAUCHE).getClass() == Dents.class) {
                    double angle1 = tab_bordure[DROITE].getAngle1();
                    double angle2 = liste_bordure.get(GAUCHE).getAngle1();
                    bordure = new Creux(new Dents(i,posX, posY,hauteur,longueur,niveau, bordure_Plate_Possede(liste_bordure),-angle1,angle2));
                } else if (tab_bordure[DROITE].getClass() == Dents.class && liste_bordure.get(GAUCHE).getClass() == Dents.class) {
                    double angle1 = tab_bordure[DROITE].getAngle1();
                    double angle2 = liste_bordure.get(GAUCHE).getAngle1();
                    bordure = new Creux(new Dents(i,posX, posY,hauteur,longueur,niveau, bordure_Plate_Possede(liste_bordure),angle1,angle2));
                }else if( tab_bordure[DROITE].getClass()  == Dents.class && liste_bordure.get(GAUCHE).getClass() == Creux.class){
                    double angle1 = tab_bordure[DROITE].getAngle1();
                    double angle2 = liste_bordure.get(GAUCHE).getAngle1();
                    bordure = new Creux(new Dents(i,posX, posY,hauteur,longueur,niveau, bordure_Plate_Possede(liste_bordure),angle1,-angle2));
                }
                else if (tab_bordure[DROITE].getClass() == Bordure_Plate.class) {
                    double angle1 = 0.0;
                    double angle2 = liste_bordure.get(GAUCHE).getAngle1();
                    if (liste_bordure.get(GAUCHE).getClass() == Creux.class){ // dents Bordure_plate
                        bordure = new Creux(new Dents(i, posX, posY, hauteur, longueur, niveau, bordure_Plate_Possede(liste_bordure), angle1, -angle2));
                    }
                    else { //GAUCHE == Dents
                        bordure = new Creux(new Dents(i, posX, posY, hauteur, longueur, niveau, bordure_Plate_Possede(liste_bordure), angle1, angle2));
                    }
                }
                else if ( liste_bordure.get(GAUCHE).getClass()== Bordure_Plate.class) {
                    double angle1 = tab_bordure[DROITE].getAngle1();
                    double angle2 = 0.0;
                    if ( tab_bordure[DROITE].getClass()== Creux.class){ // dents Bordure_plate
                        bordure = new Creux(new Dents(i, posX, posY, hauteur, longueur, niveau, bordure_Plate_Possede(liste_bordure), -angle1, angle2));
                    }
                    else { //GAUCHE == Dents
                        bordure = new Creux(new Dents(i, posX, posY, hauteur, longueur, niveau, bordure_Plate_Possede(liste_bordure), angle1, angle2));
                    }
                }
                else{ // CAS TERMINAL !!!! // faut pas arriver ici chef
                    double angle1 = 0.0;
                    double angle2 = 0.0;
                    bordure = new Creux(new Dents(i, posX, posY, hauteur, longueur, niveau, bordure_Plate_Possede(liste_bordure), angle1, angle2));
                }
            }else{
                bordure = null; // si on arrive ici c'est pas bien
            }
        }
        return bordure;
    }

    // TODO: gerer cette fonction pour quelle renvoie true ssi bordurePlate a droite ou en Bas
    private boolean bordure_Plate_Possede(ArrayList<Forme_Bordure> liste_bordure) {
        boolean res = false;
        /*for (Forme_Bordure bordure : liste_bordure) {
            if ( bordure !=null && bordure.getClass() == Bordure_Plate.class) {
                res = true;
            }
        }*/
        if (liste_bordure.get(BAS) != null && liste_bordure.get(BAS).getClass() == Bordure_Plate.class) {
            res = true;
        }

        return res;
    }



    // s'occupe de placer les Formes_Bordure sur les cotes de la piece
    private void Gestion_Placement_Bordure() {
        for (int i = 0; i < NOMBRE_COTE; i++) {
            placement_Bordure(i);
        }
    }

    // s'occupe de placer une Formes_Bordure sur la piece en fonction du cote
    private void gestion_placement(Forme_Bordure forme_bordure, int cote) {
        if (!forme_bordure.getEst_plat()) {
            int angle = recup_Angle_Rotation(cote);
            Circle pivot = recup_Indice_Pivot(cote);
            rotation(forme_bordure, angle, pivot);
            int coefficient_x_translation = recup_Coefficient_Translation(true,cote, forme_bordure.getEst_plat());
            int coefficient_y_translation = recup_Coefficient_Translation(false,cote, forme_bordure.getEst_plat());
            translation(forme_bordure, coefficient_x_translation, coefficient_y_translation);
        }
    }


    // en fonction du cote on effectue une rotation a partir d'un point de la courbe en position HAUT
    private void placement_Bordure(int position) {
        if (!tab_bordure[position].getEst_plat()) {
            int angle = recup_Angle_Rotation(position);
            Circle pivot = recup_Indice_Pivot(position);
            Forme_Bordure forme_bordure = this.tab_bordure[position];
            rotation(forme_bordure, angle, pivot);
            int coefficient_x_translation = recup_Coefficient_Translation(true,position, tab_bordure[position].getEst_plat());
            int coefficient_y_translation = recup_Coefficient_Translation(false,position, tab_bordure[position].getEst_plat());
            translation(forme_bordure, coefficient_x_translation, coefficient_y_translation);
        }
    }

    // effectue une rotation de chaque cerlce de la Forme_Bordure en fonction du pivot et de l'angle
    private void rotation(Forme_Bordure forme_bordure, int angle, Circle pivot) {
        for (int i = 0; i < forme_bordure.liste_cercle.size(); i++) {
            Point p = calcul_rotation(forme_bordure.liste_cercle.get(i), pivot, angle);
            forme_bordure.liste_cercle.get(i).setLayoutX(p.x);
            forme_bordure.liste_cercle.get(i).setLayoutY(p.y);
        }
        if (!forme_bordure.getEst_plat()) {
            for (int i = 0; i < forme_bordure.liste_cercle_controle.size(); i++) {
                Point p = calcul_rotation(forme_bordure.liste_cercle_controle.get(i), pivot, angle);
                forme_bordure.liste_cercle_controle.get(i).setLayoutX(p.x);
                forme_bordure.liste_cercle_controle.get(i).setLayoutY(p.y);
            }
        }
    }

    //renvoie les nouvelles coordonné de m apres rotation en fonction de o : origine
    protected static Point calcul_rotation(Circle m, Circle o, double angle) {
        double xm, ym, x, y;
        double rot = angle * Math.PI / 180;
        xm = m.getLayoutX() - o.getLayoutX();
        ym = m.getLayoutY() - o.getLayoutY();
        x = xm * Math.cos(rot) + ym * Math.sin(rot) + o.getLayoutX();
        y = xm * Math.sin(rot) + ym * Math.cos(rot) + o.getLayoutY();
        return new Point((int) x, (int) y);
    }

    //effectue une translation verticale et/ou horizontale
    private void translation(Forme_Bordure forme_bordure, int coefficient_X, int coefficient_Y) {
        for (int i = 0; i < forme_bordure.liste_cercle.size(); i++) {
            Point p = calcul_translation(forme_bordure.liste_cercle.get(i), coefficient_X, coefficient_Y);
            forme_bordure.liste_cercle.get(i).setLayoutX(p.x);
            forme_bordure.liste_cercle.get(i).setLayoutY(p.y);
        }
        if (!forme_bordure.getEst_plat()) {
            for (int i = 0; i < forme_bordure.liste_cercle_controle.size(); i++) {
                Point p = calcul_translation(forme_bordure.liste_cercle_controle.get(i), coefficient_X, coefficient_Y);
                forme_bordure.liste_cercle_controle.get(i).setLayoutX(p.x);
                forme_bordure.liste_cercle_controle.get(i).setLayoutY(p.y);
            }
        }
    }

    //renvoie les nouvelles coordonnées du point apres translation en x et en y
    private Point calcul_translation(Circle c, int coef_x, int coef_y) {
        double x, y;/**ne pas utiliser la  hauteur de la bordure mais de la piece MORRAY !!!! */
        x = c.getLayoutX() + (coef_x * longueur);
        y = c.getLayoutY() + (coef_y * hauteur);
        return new Point((int) x, (int) y);
    }

    //renvoie la valeur du coef de translation horizontal x du cote POSITION
    private int recup_Coefficient_Translation(boolean x,int position, boolean plat) {
        int []tab ;
        if (x) {
            tab = TAB_COEFFICIENT_TRANSLATION_X;
        }else{
            tab = TAB_COEFFICIENT_TRANSLATION_Y;
        }
        if (position == DROITE) {
            return tab[DROITE];
        } else if (position == BAS) {
            return tab[BAS];
        } else if (position == GAUCHE) {
            return tab[GAUCHE];
        }
        return tab[HAUT];
    }
    //renvoie l'indice du cercle devant etre utilisé comme pivot lors du placement de la bordure
    private Circle recup_Indice_Pivot(int position) {
        //on recupere la liste des points des courbes de la bordure du haut
        Forme_Bordure forme_bordure = tab_bordure[HAUT];
        ArrayList<Circle> liste = forme_bordure.liste_cercle;
        if (position == DROITE) {
            return liste.get(TAB_INDICE_PIVOT[DROITE]);
        } else if (position == BAS) {
            return liste.get(TAB_INDICE_PIVOT[BAS]);
        } else if (position == GAUCHE) {
            return liste.get(TAB_INDICE_PIVOT[GAUCHE]);
        }
        return liste.get(TAB_INDICE_PIVOT[HAUT]);
    }

    // renvoie l'angle de rotation en fonction du cote choisi
    private int recup_Angle_Rotation(int position) {
        if (position == DROITE) {
            return TAB_ANGLE_ROTATION[DROITE];
        } else if (position == BAS) {
            return TAB_ANGLE_ROTATION[BAS];
        } else if (position == GAUCHE) {
            return TAB_ANGLE_ROTATION[GAUCHE];
        }
        return TAB_ANGLE_ROTATION[HAUT];
    }

    private void affichage_coord_liste(ArrayList<Circle> liste) {
        int cpt = 0;
        System.out.println("voy a affichar les coord de position morray");
        for (Circle circle : liste) {
            System.out.println("indice : "+ cpt++  +" coord X :" + circle.getCenterX() + " coordY : " + circle.getCenterY());
        }
    }

    public void Ajouter_evenement() {
        Piece piece1 = this;
        path.setOnMousePressed((new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //gestion event dans le panneau courant de la piece
                panneau.getChildren().remove(path);
                panneau.getChildren().add(path);
                oldX = mouseEvent.getX();
                oldY = mouseEvent.getY();
                //gestion du Drag and Drop
                if (panneau == tab_pane[1] || panneau == tab_pane[2] || panneau == tab_pane[3] || panneau == tab_pane[4]) {
                    piece_ombre = new Piece(piece1, tab_pane[0] );
                    piece_ombre.path.setStroke(piece1.path.getStroke());
                    piece_ombre.path.setFill(piece1.path.getFill());
                    piece_ombre.path.setLayoutX(piece1.path.getLayoutX() + piece1.panneau.getWidth()*2);
                    piece_ombre.path.setLayoutY(piece1.path.getLayoutY() );
                    tab_pane[0].getChildren().add(piece_ombre.path);
                }
            }
        }));
        path.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (isMovable) { // drag seulement si la piece est deplacable
                    double newX = mouseEvent.getX();
                    double newY = mouseEvent.getY();
                    path.setLayoutX(path.getLayoutX() + newX - oldX);
                    path.setLayoutY(path.getLayoutY() + newY - oldY);
                    piece_ombre.path.setLayoutX((piece_ombre.path.getLayoutX() + newX - oldX));
                    piece_ombre.path.setLayoutY((piece_ombre.path.getLayoutY() + newY - oldY));
                }

            }
        });
    }


    public void Mise_a_jour_point(Dents d) {
        int i = 0;
        ArrayList<MoveTo> newMoveto = new ArrayList<>();
        ArrayList<CubicCurveTo> newControle = new ArrayList<>();
        for (PathElement pe : d.notre_path.getElements()) {
            if (pe.getClass() == MoveTo.class) {
                newMoveto.add((MoveTo) pe);
            }
            if (pe.getClass() == CubicCurveTo.class) {
                newControle.add((CubicCurveTo) pe);
            }

        }
        Recup_elts(d, newMoveto, newControle);
    }

    public void Recup_elts(Dents d, ArrayList<MoveTo> mt, ArrayList<CubicCurveTo> cct) {
        //d.liste_Moveto = mt ;
        //d.liste_cubicCurveTo = cct ;

        //d.liste_cercle
        //d.liste_cercle_controle

        ArrayList<Circle> newCircle = new ArrayList<>();
        ArrayList<Circle> newCircleControle = new ArrayList<>();

        int cpt = 0;
        for (int i = 0; i < cct.size(); i++) {
            // start cubiccurveto
            Circle c = new Circle();
            c.setLayoutX(cct.get(i).getX());
            //System.out.println("coord x : " + c.getLayoutX());
            c.setLayoutY(cct.get(i).getY());
            //System.out.println("coord y : " + c.getLayoutY());
            newCircle.add(c);
            cpt++;
            // controle 1 :
            c = new Circle();
            c.setLayoutX(cct.get(i).getControlX1());
            //System.out.println("coord x : " + c.getLayoutX());
            c.setLayoutY(cct.get(i).getControlY1());
            //System.out.println("coord y : " + c.getLayoutY());
            newCircleControle.add(c);
            cpt++;
            // controle 2 :
            c = new Circle();
            c.setLayoutX(cct.get(i).getControlX2());
            //System.out.println("coord x : " + c.getLayoutX());
            c.setLayoutY(cct.get(i).getControlY2());
            //System.out.println("coord y : " + c.getLayoutY());
            newCircleControle.add(c);
            cpt++;
        }
        Circle c = new Circle();
        c.setLayoutX(mt.get(mt.size() - 1).getX());
        //System.out.println("coord x : " + c.getLayoutX());
        c.setLayoutY(mt.get(mt.size() - 1).getY());
        //System.out.println("coord y : " + c.getLayoutY());
        newCircle.add(c);
        cpt++;
        //System.out.println("mon cpt : " + cpt);

    }

    private void dessineLineto() {
        MoveTo moveTo = new MoveTo();
        moveTo.setX(tab_bordure[0].getListe_cercle().get(0).getCenterX());
        moveTo.setY(tab_bordure[0].getListe_cercle().get(0).getCenterY());
        path.getElements().add(moveTo);
        for (int i = 1; i < NOMBRE_COTE; i++) {
            LineTo lineTo = new LineTo();
            lineTo.setX(tab_bordure[i].getListe_cercle().get(0).getCenterX());
            lineTo.setY(tab_bordure[i].getListe_cercle().get(0).getCenterY());
            path.getElements().add(lineTo);
        }
        LineTo lineTo = new LineTo();
        lineTo.setX(tab_bordure[HAUT].getListe_cercle().get(0).getCenterX());
        lineTo.setY(tab_bordure[HAUT].getListe_cercle().get(0).getCenterY());
        path.getElements().add(lineTo);
        path.getElements().add(new ClosePath());
    }
    public static int getHAUT() {
        return HAUT;
    }

    public static int getDROITE() {
        return DROITE;
    }

    public static int getBAS() {
        return BAS;
    }

    public static int getGAUCHE() {
        return GAUCHE;
    }

    public static int[] getTabAngleRotation() {
        return TAB_ANGLE_ROTATION;
    }

    public static int[] getTabIndicePivot() {
        return TAB_INDICE_PIVOT;
    }

    public static int getNombreCote() {
        return NOMBRE_COTE;
    }

    public Forme_Bordure[] getTab_bordure() {
        return tab_bordure;
    }
    // renvoie la bordure de cote indice = { HAUT | DROITE | BAS | GAUCHE }
    public Forme_Bordure getBordure(int indice) {
        if (indice == DROITE) {
            return tab_bordure[DROITE];
        } else if (indice == BAS) {
            return tab_bordure[BAS];
        } else if (indice == GAUCHE) {
            return tab_bordure[GAUCHE];
        }
        return tab_bordure[HAUT];
    }
    public static double getMinLongueur() {
        return MIN_LONGUEUR;
    }

    public static double getMinHauteur() {
        return MIN_HAUTEUR;
    }

    public void setTab_bordure(Forme_Bordure[] tab_bordure) {
        this.tab_bordure = tab_bordure;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public Shape getForme() {
        return forme;
    }

    public void setForme(Shape forme) {
        this.forme = forme;
    }
    public Pane getPanneau() {
        return panneau;
    }

    public void setPanneau(Pane panneau) {
        this.panneau = panneau;
    }
    public boolean isMovable() {
        return isMovable;
    }

    public void setMovable(boolean movable) {
        isMovable = movable;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getOldX() {
        return oldX;
    }

    public void setOldX(double oldX) {
        this.oldX = oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public void setOldY(double oldY) {
        this.oldY = oldY;
    }

}
/*private void create_cubiCurveTo() {
        init_Liste_Courbe();
        MoveTo moveTo = new MoveTo();
        moveTo.xProperty().bind(this.liste_cercle.get(0).layoutXProperty());
        moveTo.yProperty().bind(this.liste_cercle.get(0).layoutYProperty());

        path.getElements().add(moveTo);
        //this.liste_cercle.add(this.liste_cercle.get(0));
        //this.liste_cercle.remove(0);
        for (int i = 0; i < this.liste_cercle.size() && 2*i < this.liste_cercle_controle.size(); i++) {
            this.liste_courbe.get(i).xProperty().bind(this.liste_cercle.get(i).layoutXProperty());
            this.liste_courbe.get(i).yProperty().bind(this.liste_cercle.get(i).layoutYProperty());

            this.liste_courbe.get(i).controlX1Property().bind(this.liste_cercle_controle.get(2*i+1).layoutXProperty() );
            this.liste_courbe.get(i).controlY1Property().bind(this.liste_cercle_controle.get(2*i+1).layoutYProperty());

            this.liste_courbe.get(i).controlX2Property().bind(this.liste_cercle_controle.get(2*i).layoutXProperty());
            this.liste_courbe.get(i).controlY2Property().bind(this.liste_cercle_controle.get(2*i).layoutYProperty());

            if (i < nb_courbe) {
                this.liste_moveTo.get(i).xProperty().bind(this.liste_cercle.get(i+1).layoutXProperty());
                this.liste_moveTo.get(i).yProperty().bind(this.liste_cercle.get(i+1).layoutYProperty());
                path.getElements().add(this.liste_moveTo.get(i));
            }

            path.getElements().add(this.liste_courbe.get(i));
        }
        path.setFillRule(FillRule.EVEN_ODD);
        path.setFill(Color.GREEN);
       path.getElements().add(new ClosePath());
    }*/