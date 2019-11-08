package sample;

import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.awt.*;
import java.lang.reflect.Array;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Random;
/**
 *
 * il
 * nous
 * faut
 * une
 * position
 * de
 * depart !!!!!!
 *
 * */
public class Piece extends Shape{

    // on calcule toutes les coordonnéées d'une piece par rapport au xC0 et yC0 de la bordure du Haut .

    /**
     * 0 -> haut
     * 1 -> droite
     * 2 -> bas
     * 3 -> gauche
     */
    private static double oldX=0;
    private static double oldY=0;
    static final int HAUT = 0;
    static final int DROITE = 1;
    static final int BAS = 2;
    static final int GAUCHE = 3;
    static final int TAB_ANGLE_ROTATION[] = {0, 270, 180, 90};
    static final int TAB_INDICE_PIVOT[] = {0, 0, 0, 0};
    static final int TAB_COEFFICIENT_TRANSLATION_X[] = {0, 1, 1, 0};
    static final int TAB_COEFFICIENT_TRANSLATION_Y[] = {0, 1, 1, 0};
    static final int NOMBRE_COTE = 4;


    ArrayList<Circle> circle = new ArrayList<>();
    ArrayList<Circle> controle = new ArrayList<>();
    ArrayList<CubicCurveTo> cct = new ArrayList<>();
    ArrayList<MoveTo> mt = new ArrayList<>();

    public Path getPath() {
        return path;
    }

    private Path path;

    //utilisé uniquement dans les anciennes methodes morray
    ArrayList<Forme_Bordure> liste_bordure;

    Forme_Bordure[] tab_bordure;
    Rectangle rectangle ;
    Shape forme ;

    //prend en parametre une liste de Forme_Bordure précisant les contraintes de
    //chaque cote de la piece dans un puzzle==Plateau
    // si [ null , null , null, null ] alors aucune contrainte
    //si [ dent d, null , creux , Bordure_Plate b]
    // alors Haut s'adapte a d , Droite libre, Bas s'adapte a c et Gauche a b=ligne droite
    public Piece(ArrayList<Forme_Bordure> liste_forme) {
        path = new Path();
        //liste_bordure = new ArrayList<>();
        tab_bordure = new Forme_Bordure[NOMBRE_COTE];
        for (int i = 0; i < NOMBRE_COTE; i++) {
            if (liste_forme.get(i) == null) {
                //si pas de contrainte alors on met une dent
                // ou un creux mais as de bordure plate ----- ah bon ?
                // Forme_Bordure forme_bordure= creation_Bordure();
                //tab_bordure[i] = forme_bordure;
                tab_bordure[i] =new Dents();
            }else if (liste_forme.get(i).getClass() == Dents.class) {
                tab_bordure[i] =new Dents((Dents) liste_forme.get(i));
            }
            else if (liste_forme.get(i).getClass() == Creux.class) {
                tab_bordure[i] =new Creux((Creux) liste_forme.get(i));
            }
            else if (liste_forme.get(i).getClass() == Bordure_Plate.class) {
                tab_bordure[i] =new Bordure_Plate(true);
                //true pcq la bordure est plate
            }
        }
        Gestion_Placement_Bordure();
        gestion_Forme();
        //gestion_liaison_Bordure();
        //MAJ_Path();
        remplir_liste_piece();
    }

    private void gestion_Forme() {
        forme = Shape.union(tab_bordure[HAUT].notre_path,tab_bordure[DROITE].notre_path);
        forme = Shape.union(forme,tab_bordure[BAS].notre_path);
        forme = Shape.union(forme,tab_bordure[GAUCHE].notre_path);

        forme.setSmooth(true);
        forme.setFill(Color.RED);

        Ajouter_evenement(forme);
    }
    // creer une bordure sans contrainte de bordure precedente // d'ou l'absence de parametre
    private Forme_Bordure creation_Bordure() {
        Forme_Bordure bordure ;
        /*if ( (Math.random()*2) == 0) {
            bordure = new Dents();
        }
        else{
            bordure = new Creux();
        }*/
        bordure = new Dents();
        return bordure;
    }

    // s'occupe de placer les Formes_Bordure sur les cotes de la piece
    private void Gestion_Placement_Bordure() {
        for (int i = 0; i < NOMBRE_COTE; i++) {
            placement_Bordure(i);
        }
    }


    // en fonction du cote on effectue une rotation a partir d'un point de la courbe en position HAUT
    private void placement_Bordure(int position) {
        int angle = recup_Angle_Rotation(position);
        Circle pivot = recup_Indice_Pivot(position);
        Forme_Bordure forme_bordure = this.tab_bordure[position];
        rotation(forme_bordure, angle, pivot);
        int coefficient_x_translation = recup_Coefficient_X_Translation(position);
        int coefficient_y_translation = recup_Coefficient_Y_Translation(position);
        translation( forme_bordure , coefficient_x_translation , coefficient_y_translation );
    }

    // effectue une rotation de chaque cerlce de la Forme_Bordure en fonction du pivot et de l'angle
    private void rotation(Forme_Bordure forme_bordure, int angle, Circle pivot) {
        for (int i = 0; i < forme_bordure.liste_cercle.size(); i++) {
            Point p = calcul_rotation(forme_bordure.liste_cercle.get(i),pivot,angle);
            forme_bordure.liste_cercle.get(i).setLayoutX(p.x);
            forme_bordure.liste_cercle.get(i).setLayoutY(p.y);
        }
        for (int i = 0; i < forme_bordure.liste_cercle_controle.size(); i++) {
            Point p = calcul_rotation(forme_bordure.liste_cercle_controle.get(i),pivot,angle);
            forme_bordure.liste_cercle_controle.get(i).setLayoutX(p.x);
            forme_bordure.liste_cercle_controle.get(i).setLayoutY(p.y);
        }
    }
    //renvoie les nouvelles coordonné de m apres rotation en fonction de o : origine
    private Point calcul_rotation(Circle m, Circle o, int angle) {
        double xm,ym,x,y;
        double rot = angle * Math.PI / 180;
        xm = m.getLayoutX() - o.getLayoutX();
        ym = m.getLayoutY() - o.getLayoutY();
        x = xm * Math.cos(rot) + ym * Math.sin(rot) + o.getLayoutX();
        y = xm * Math.sin(rot) + ym * Math.cos(rot) + o.getLayoutY();
        return new Point((int)x,(int)y);
    }

    //effectue une translation verticale et/ou horizontale
    private void translation(Forme_Bordure forme_bordure, int coefficient_X, int coefficient_Y) {
        for (int i = 0; i < forme_bordure.liste_cercle.size(); i++) {
            // forme_bordure.liste_cercle.get(i).getTransforms().add(new Translate((coefficient_X * Forme_Bordure.getTailleCotePieceLongueur()), (coefficient_Y * Forme_Bordure.getTailleCotePieceHauteur())));
            Point p = calcul_translation(forme_bordure.liste_cercle.get(i), coefficient_X, coefficient_Y);
            forme_bordure.liste_cercle.get(i).setLayoutX(p.x);
            forme_bordure.liste_cercle.get(i).setLayoutY(p.y);
        }
        for (int i = 0; i < forme_bordure.liste_cercle_controle.size(); i++) {
            //forme_bordure.liste_cercle_controle.get(i).getTransforms().add(new Translate((coefficient_X*Forme_Bordure.getTailleCotePieceLongueur()),(coefficient_Y*Forme_Bordure.getTailleCotePieceHauteur())));
            Point p = calcul_translation(forme_bordure.liste_cercle_controle.get(i), coefficient_X, coefficient_Y);
            forme_bordure.liste_cercle_controle.get(i).setLayoutX(p.x);
            forme_bordure.liste_cercle_controle.get(i).setLayoutY(p.y);
        }
    }

    //renvoie les nouvelles coordonnées du point apres translation en x et en y
    private Point calcul_translation(Circle c, int coef_x, int coef_y) {
        double x,y;
        x = c.getLayoutX() + (coef_x * Forme_Bordure.getTailleCotePieceLongueur());
        y = c.getLayoutY() + (coef_y * Forme_Bordure.getTailleCotePieceHauteur());
        return new Point((int)x,(int)y);
    }
    //renvoie la valeur du coef de translation horizontal x du cote POSITION
    private int recup_Coefficient_X_Translation(int position) {
        if (position == DROITE) {
            return TAB_COEFFICIENT_TRANSLATION_X[DROITE];
        } else if (position == BAS) {
            return TAB_COEFFICIENT_TRANSLATION_X[BAS];
        } else if (position == GAUCHE) {
            return TAB_COEFFICIENT_TRANSLATION_X[GAUCHE];
        }
        return TAB_COEFFICIENT_TRANSLATION_X[HAUT];
    }
//renvoie la valeur du coef de translation horizontal y du cote POSITION
    private int recup_Coefficient_Y_Translation(int position) {
        if (position == DROITE) {
            return TAB_COEFFICIENT_TRANSLATION_Y[DROITE];
        } else if (position == BAS) {
            return TAB_COEFFICIENT_TRANSLATION_Y[BAS];
        } else if (position == GAUCHE) {
            return TAB_COEFFICIENT_TRANSLATION_Y[GAUCHE];
        }
        return TAB_COEFFICIENT_TRANSLATION_Y[HAUT];
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

/*
    Piece( ){
        liste_bordure = new ArrayList<>();
        liste_bordure.add( new Dents() );// dents du haut

        liste_bordure.add( new Dents() ); // dents de droite
        liste_bordure.get(DROITE).notre_path.getTransforms().add(new Rotate(270, liste_bordure.get(DROITE).getListe_cercle().get(6).getLayoutX(), liste_bordure.get(DROITE).getListe_cercle().get(6).getLayoutY()));
        Mise_a_jour_point( (Dents) liste_bordure.get(DROITE) ) ;

        liste_bordure.add(new Dents() ); // dents de bas
        liste_bordure.get(BAS).notre_path.getTransforms().add(new Translate(0,Forme_Bordure.getTailleCotePieceHauteur() ));
        Mise_a_jour_point( (Dents) liste_bordure.get(BAS) ) ;

        liste_bordure.add(new Dents() ); // dents de gauche
        liste_bordure.get(GAUCHE).notre_path.getTransforms().add(new Rotate(90, liste_bordure.get(DROITE).getListe_cercle().get(0).getLayoutX(), liste_bordure.get(HAUT).getListe_cercle().get(0).getLayoutY()));
        Mise_a_jour_point( (Dents) liste_bordure.get(BAS) ) ;


        for (int i = 0; i < liste_bordure.size() ; i++) {
            liste_bordure.get(i).notre_path.setFillRule(FillRule.NON_ZERO);
        }

        gestion_liaison_Bordure();
        forme = Shape.union(liste_bordure.get(HAUT).notre_path,liste_bordure.get(DROITE).notre_path);
        forme = Shape.union(forme,liste_bordure.get(BAS).notre_path);
        forme = Shape.union(forme,liste_bordure.get(GAUCHE).notre_path);

        forme.setSmooth(true);
        forme.setFill(Color.RED);

        Ajouter_evenement(forme);
    }
 */
    public void remplir_liste_piece(){

        for (Forme_Bordure forme_bordure : tab_bordure) {

            //circle.addAll(forme_bordure.getListe_cercle());
            for (int j = 0; j < forme_bordure.getListe_cercle().size()-1; j++) {
                if (j % 6 == 0 )j++;
                Circle c = new Circle();
                c.setLayoutX(forme_bordure.getListe_cercle().get(j).getLayoutX());
                c.setLayoutY(forme_bordure.getListe_cercle().get(j).getLayoutY());
                circle.add(c);
            }

            for (Circle circle1 : forme_bordure.getListe_cercle_controle()) {
                controle.add(new Circle(circle1.getRadius(),circle1.getLayoutX(),circle1.getLayoutY()));
            }
        }

        System.out.println(controle.size());
        System.out.println(circle.size());

        for (int i = 0; i < circle.size()-1; i++) {

                System.out.println(circle.get(i));
                MoveTo m = new MoveTo();
                CubicCurveTo cc = new CubicCurveTo();
                cc.xProperty().bind(circle.get(i).layoutXProperty());
                cc.yProperty().bind(circle.get(i).layoutYProperty());

                cc.controlX1Property().bind(controle.get(2 * i).layoutXProperty());
                cc.controlY1Property().bind(controle.get(2 * i).layoutYProperty());
                cc.controlX2Property().bind(controle.get(2 * i + 1).layoutXProperty());
                cc.controlY2Property().bind(controle.get(2 * i + 1).layoutYProperty());
                if (i < circle.size() - 1) {
                    m = new MoveTo();
                    m.xProperty().bind(circle.get(i + 1).layoutXProperty());
                    m.yProperty().bind(circle.get(i + 1).layoutYProperty());
                } else {
                    m = new MoveTo();
                    m.xProperty().bind(circle.get(0).layoutXProperty());
                    m.yProperty().bind(circle.get(0).layoutYProperty());
                }

                path.getElements().add(m);
                path.getElements().add(cc);
            }
    }



    public void MAJ_Path() {
        int i = 0 ;
        for (Forme_Bordure forme_bordure : tab_bordure) {
            for (PathElement element : forme_bordure.notre_path.getElements()) {
                System.out.println(element);
                i++ ;
                path.getElements().add(element);
            }
        }
        System.out.println("!!!!" + path.getElements().get(34) );
        //System.out.println("i : " + (tab_bordure[tab_bordure.length-1].notre_path.getElements().get(0) ) ); // recup le 11 eme element , soit le dernier moveto
        //tab_bordure[tab_bordure.length-1].notre_path.getElements().get(tab_bordure[tab_bordure.length-1].notre_path.getElements().size()-1);
    }

    //colle toute les bordure entre elle
    private void gestion_liaison_Bordure() {
        for (int i = 0; i < tab_bordure.length; i++) {
            //gestion de la derniere bordure
            if (i == tab_bordure.length - 1) {
                //liaison_Bordure(tab_bordure[i],tab_bordure[0]);
                liaison_Bordure_Gauche(tab_bordure[i] , tab_bordure[0] ,tab_bordure[i-1] );
            }else  {
                liaison_Bordure(tab_bordure[i],tab_bordure[i+1]);
            }
        }
    }
    //lie la derniere Courbe d'une Bordure a la premiere de la suivante
    private void liaison_Bordure(Forme_Bordure forme_bordure1, Forme_Bordure forme_bordure2) {
        forme_bordure2.getListe_cercle().set(0,forme_bordure1.getListe_cercle().get(6));
    }
    private void liaison_Bordure_Gauche(Forme_Bordure forme_bordure1, Forme_Bordure forme_bordure2 , Forme_Bordure forme_bordure3) {
        forme_bordure2.getListe_cercle().set(0,forme_bordure1.getListe_cercle().get(0));
        forme_bordure3.getListe_cercle().set(6,forme_bordure2.getListe_cercle().get(6));
    }

    public void Ajouter_evenement(Shape forme){
        forme.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                oldX = mouseEvent.getSceneX();
                oldY = mouseEvent.getSceneY();
            }
        });
        forme.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //forme.setTranslateX((oldX - mouseEvent.getX()));
                //forme.setTranslateY((oldY - mouseEvent.getY()));
                forme.setLayoutX(mouseEvent.getX());
                //(mouseEvent.getSceneX());
                forme.setLayoutY(mouseEvent.getY());
            }
        });
    }





    public void Mise_a_jour_point(Dents d ){
        int i = 0 ;
        ArrayList<MoveTo> newMoveto = new ArrayList<>();
        ArrayList<CubicCurveTo> newControle = new ArrayList<>();
        for (PathElement pe : d.notre_path.getElements()){
            if ( pe.getClass() == MoveTo.class){
                newMoveto.add((MoveTo)pe) ;
            }
            if ( pe.getClass() == CubicCurveTo.class){
                newControle.add((CubicCurveTo)pe) ;
            }

        }
        Recup_elts(d,newMoveto,newControle);
    }

    public void Recup_elts ( Dents d ,ArrayList<MoveTo> mt , ArrayList<CubicCurveTo> cct ){
        //d.liste_Moveto = mt ;
        //d.liste_cubicCurveTo = cct ;

        //d.liste_cercle
        //d.liste_cercle_controle

        ArrayList<Circle> newCircle = new ArrayList<>();
        ArrayList<Circle> newCircleControle = new ArrayList<>();

        int cpt =0;
        for (int i = 0; i < cct.size() ; i++) {
            // start cubiccurveto
            Circle c = new Circle();
            c.setLayoutX(cct.get(i).getX());
            System.out.println("coord x : "+ c.getLayoutX());
            c.setLayoutY(cct.get(i).getY());
            System.out.println("coord y : "+ c.getLayoutY());
            newCircle.add(c);
            cpt++;
            // controle 1 :
            c = new Circle();
            c.setLayoutX(cct.get(i).getControlX1());
            System.out.println("coord x : "+ c.getLayoutX());
            c.setLayoutY(cct.get(i).getControlY1());
            System.out.println("coord y : "+ c.getLayoutY());
            newCircleControle.add(c);
            cpt++;
            // controle 2 :
            c = new Circle();
            c.setLayoutX(cct.get(i).getControlX2());
            System.out.println("coord x : "+ c.getLayoutX());
            c.setLayoutY(cct.get(i).getControlY2());
            System.out.println("coord y : "+ c.getLayoutY());
            newCircleControle.add(c);
            cpt++;
        }
        Circle c = new Circle() ;
        c.setLayoutX(mt.get(mt.size() - 1).getX());
        System.out.println("coord x : "+ c.getLayoutX());
        c.setLayoutY(mt.get(mt.size() - 1).getY());
        System.out.println("coord y : "+ c.getLayoutY());
        newCircle.add(c);
        cpt++;
        System.out.println("mon cpt : " + cpt);

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

    public ArrayList<Forme_Bordure> getListe_bordure() {
        return liste_bordure;
    }

    public void setListe_bordure(ArrayList<Forme_Bordure> liste_bordure) {
        this.liste_bordure = liste_bordure;
    }

    public Forme_Bordure[] getTab_bordure() {
        return tab_bordure;
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


}
