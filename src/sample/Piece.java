package sample;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.ArrayList;

public class Piece extends Shape{

    // on calcule toutes les coordonnéées d'une piece par rapport au xC0 et yC0 de la bordure du Haut .

    /**
     * 0 -> haut
     * 1 -> droite
     * 2 -> bas
     * 3 -> gauche
     */
    static final int HAUT = 0 ;
    static final int DROITE = 1 ;
    static final int BAS = 2 ;
    static final int GAUCHE = 3 ;

    ArrayList<Forme_Bordure> liste_bordure ;
    Rectangle rectangle ;
    Shape forme ;


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

        forme = Shape.union(liste_bordure.get(HAUT).notre_path,liste_bordure.get(DROITE).notre_path);
        forme = Shape.union(forme,liste_bordure.get(BAS).notre_path);
        forme = Shape.union(forme,liste_bordure.get(GAUCHE).notre_path);

        forme.setSmooth(true);
        forme.setFill(Color.RED);

        Ajouter_evenement(forme);



    }

    public void Ajouter_evenement(Shape forme){
        forme.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                forme.setLayoutX(mouseEvent.getX());
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
        d.liste_Moveto = mt ;
        d.liste_cubicCurveTo = cct ;

        //d.liste_cercle
        //d.liste_cercle_controle

        ArrayList<Circle> newCircle = new ArrayList<>();
        ArrayList<Circle> newCircleControle = new ArrayList<>();


        for (int i = 0; i < cct.size() ; i++) {
            // start cubiccurveto
            Circle c = new Circle();
            c.setLayoutX(cct.get(i).getX());
            c.setLayoutY(cct.get(i).getY());
            newCircle.add(c);

            // controle 1 :
            c = new Circle();
            c.setLayoutX(cct.get(i).getControlX1());
            c.setLayoutY(cct.get(i).getControlY1());
            newCircleControle.add(c);
            // controle 2 :
            c = new Circle();
            c.setLayoutX(cct.get(i).getControlX2());
            c.setLayoutY(cct.get(i).getControlY2());
            newCircleControle.add(c);

        }
        Circle c = new Circle() ;
        c.setLayoutX(mt.get(mt.size()-1).getX());
        c.setLayoutY(mt.get(mt.size()-1).getY());
        newCircle.add(c);

    }

}
