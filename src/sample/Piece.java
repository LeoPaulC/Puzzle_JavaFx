package sample;

import javafx.beans.binding.NumberBinding;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
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
    static final int MIN_LONGUEUR = 30;
    static final int MIN_HAUTEUR = 30;
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
    int hauteur;
    int longueur;

    //constructeur qui permet de placer la piece dans l'espace
    public Piece(ArrayList<Forme_Bordure> liste_bordure, double x, double y, int hauteur, int longueur) {
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
        ///Gestion_Placement_Bordure();
        fill_liste_cercle();
        create_shapes();
        forme = Shape.union(path, new Circle(0));
    }

    //constructeur qui permet de placer la piece dans l'espace
    public Piece(ArrayList<Forme_Bordure> liste_bordure, double x, double y, int hauteur, int longueur, int niveau) {
        Main.consumer.accept("dans piece avec niveau");
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
        //Gestion_Placement_Bordure();
        fill_liste_cercle();
        create_shapes();
        forme = Shape.union(path, new Circle(0));
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
        ///////////////////////////////////////////////////////////////////////////////////fill_tab_bordure(liste_bordure);
        Gestion_Placement_Bordure();
        fill_liste_cercle();
        create_shapes();
        forme = Shape.union(path, new Circle(0));
    }


    /** Gerer la prise en compte des bordure de contraintes !!!! */
    private void fill_tab_bordure(ArrayList<Forme_Bordure> liste_bordure, int hauteur, int longueur) {
        for (int i = 0; i < NOMBRE_COTE; i++) {
            if (liste_bordure.get(i) == null) {
                //si pas de contrainte alors on met une dent
                // ou un creux mais pas de bordure plate --- pcq pas de bord
                tab_bordure[i] = randoms_Bordure(i, hauteur , longueur);
                gestion_placement(tab_bordure[i],i);
            } else if (liste_bordure.get(i).getClass() == Dents.class) {
                Main.consumer.accept("listebordure.get(i).class == Dents");
                ArrayList<Circle> liste1 = liste_bordure.get(i).getListe_cercle();
                ArrayList<Circle> liste2 = liste_bordure.get(i).getListe_cercle_controle();
                if (i == BAS || i == HAUT) {
                    Collections.reverse(liste1);
                    Collections.reverse(liste2);
                }
                tab_bordure[i]= new Creux(liste1,liste2); // pas besoin de la placer normalement

                //tab_bordure[i] = new Creux((Dents)liste_bordure.get(i),i );// passer en parametre le cote pour faire une inversion soit sur les X soit les Y
                System.out.println("tab_bordure i :" + i + " cote : "+i);
                //Dents d = new Dents((Dents) liste_bordure.get(i),niveau);
                //gestion_placement(tab_bordure[i],i);
                //tab_bordure[i] = new Creux(new Dents(i,posX,posY, hauteur, longueur, niveau));
            } else if (liste_bordure.get(i).getClass() == Creux.class) {
                Main.consumer.accept("listebordure.get(i).class == Creux");
                ArrayList<Circle> liste1 = liste_bordure.get(i).getListe_cercle();
                ArrayList<Circle> liste2 = liste_bordure.get(i).getListe_cercle_controle();
                if (i == BAS || i == HAUT) {
                    Collections.reverse(liste1);
                    Collections.reverse(liste2);
                }
                tab_bordure[i]= new Dents(liste1, liste2);
                //tab_bordure[i] = new Dents(i, posX,posY, hauteur, longueur,niveau);// passer en parametre le cote pour faire une inversion soit sur les X soit les Y
                //gestion_placement(tab_bordure[i],i);
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

    private Forme_Bordure randoms_Bordure(int i ,int hauteur , int longueur) {
        Main.consumer.accept("dans randoms_bordure");
        Forme_Bordure bordure;
        Random random = new Random();
        int r =random.nextInt(2+ 1); // random entre
        if ( r%2 == 0) {
            bordure = new Dents(i,posX, posY,hauteur,longueur,niveau);
            //bordure = new Creux(new Dents(i,posX, posY,hauteur,longueur,niveau));
        }
        else{
            bordure = new Creux(new Dents(i,posX, posY,hauteur,longueur,niveau));
        }
        return bordure;
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
    private Point calcul_rotation(Circle m, Circle o, int angle) {
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



    public void Ajouter_evenement(Shape forme) {
        /*forme.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                forme.setLayoutX(mouseEvent.getX());
                forme.setLayoutY(mouseEvent.getY());
            }
        });

         */
        //forme.setFill(new ImagePattern());
       /* forme.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                forme.getTransforms().add(new Rotate(90));
            }
        });*/
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
            System.out.println("coord x : " + c.getLayoutX());
            c.setLayoutY(cct.get(i).getY());
            System.out.println("coord y : " + c.getLayoutY());
            newCircle.add(c);
            cpt++;
            // controle 1 :
            c = new Circle();
            c.setLayoutX(cct.get(i).getControlX1());
            System.out.println("coord x : " + c.getLayoutX());
            c.setLayoutY(cct.get(i).getControlY1());
            System.out.println("coord y : " + c.getLayoutY());
            newCircleControle.add(c);
            cpt++;
            // controle 2 :
            c = new Circle();
            c.setLayoutX(cct.get(i).getControlX2());
            System.out.println("coord x : " + c.getLayoutX());
            c.setLayoutY(cct.get(i).getControlY2());
            System.out.println("coord y : " + c.getLayoutY());
            newCircleControle.add(c);
            cpt++;
        }
        Circle c = new Circle();
        c.setLayoutX(mt.get(mt.size() - 1).getX());
        System.out.println("coord x : " + c.getLayoutX());
        c.setLayoutY(mt.get(mt.size() - 1).getY());
        System.out.println("coord y : " + c.getLayoutY());
        newCircle.add(c);
        cpt++;
        System.out.println("mon cpt : " + cpt);

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

    public static int getMinLongueur() {
        return MIN_LONGUEUR;
    }

    public static int getMinHauteur() {
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