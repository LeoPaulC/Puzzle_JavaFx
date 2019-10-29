package sample;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.min;
/**TODO:
 * placement symetrique des controleur blablabla
 * gestion du deplacement en ligne grace aux fonctions affines
 * du coup su axe de symetrie mais les distance au point de repere doivent varier
 * du coup "symetre" uniquement lors du placement initiale puis deplcament lineaire
 */
public class Main extends Application {
    ArrayList<Circle> listC = new ArrayList<Circle>();//liste de cercle
    //liste des indices du premier cercle de chaque courbe
    ArrayList<Integer> listStart = new ArrayList<Integer>();// du cercle start de chaque courbes
    private final static double diametre = 5;
    private final static int NOMBRE_CERCLE = 7;
    private final static int MAX_WIDTH = 600;
    private final static int MAX_HEIGHT = 300;
    //compteur du nombre de cercle utilisé pour dessiner des courbes
    private int cptCircleUsed = 0;
    Scene sc;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Pane root = new Pane();
        primaryStage.setTitle("TP4-2");
        Pane pane = new Pane();

        //remplissage de la liste de cercle
        fillListCircle(listC,NOMBRE_CERCLE);
        //placement des cerlces dans le panneau
        setOnPane(pane,listC);


        //on cree une courbe
        setCurveTo(false,listC,pane);
        setCurveTo(true,listC,pane);
        Circle c1 = listC.get(2);
        Circle c2 = listC.get(4);
        c1.setFill(Color.PINK);
        c2.setFill(Color.BLUE);

        root.getChildren().add(pane);
        primaryStage.setScene(new Scene(root, MAX_WIDTH, MAX_HEIGHT));
        primaryStage.show();
    }




    //ajoute une courbe entre des cercles // collee ou non a une autre
    private void setCurveTo(boolean colle, ArrayList<Circle> list,Pane pane) {
        if (colle) {
            cptCircleUsed--;
        }
        createCurveTo(list,pane);
    }
    //creer une CubicCurvesTo
    private void createCurveTo(ArrayList<Circle> list, Pane pane) {
        MoveTo move = new MoveTo();
        CubicCurveTo curve = new CubicCurveTo();
        //stockage de l'indice du cercle de depart de la courbe
        //listStart.add(cptCircleUsed);
        // point de depart de la courbe
        curve.xProperty().bind(list.get(cptCircleUsed).centerXProperty());
        curve.yProperty().bind(list.get(cptCircleUsed++).centerYProperty());
        //on cree une ligne entre Point de depart et Controleur 1
        setLineOnPane(list,pane);
        //controleur de la courbe
        curve.controlX1Property().bind(list.get(cptCircleUsed).centerXProperty());
        curve.controlY1Property().bind(list.get(cptCircleUsed++).centerYProperty());

        curve.controlX2Property().bind(list.get(cptCircleUsed).centerXProperty());
        curve.controlY2Property().bind(list.get(cptCircleUsed++).centerYProperty());
        //on cree une ligne entre Point d'arrivée et  Controleur 2
        setLineOnPane(list,pane);
        // point d'arrivée de la courbe
        move.xProperty().bind(list.get(cptCircleUsed).centerXProperty());
        move.yProperty().bind(list.get(cptCircleUsed++).centerYProperty());
        Path path = new Path();
        path.getElements().addAll(move,curve);
        pane.getChildren().addAll(path);

    }
    // met une ligne entre deux points dans le panne
    private void setLineOnPane(ArrayList<Circle> list, Pane pane) {
        Line line = createLine(list);
        pane.getChildren().add(line);
    }
    //creer une ligne entre un cercle et son PRECEDENT dans une liste
    private Line createLine(ArrayList<Circle> list) {
        Line line = new Line();
        line.endXProperty().bind(list.get(cptCircleUsed).centerXProperty());
        line.endYProperty().bind(list.get(cptCircleUsed).centerYProperty());
        int moins1 = cptCircleUsed -1 ;
        line.startXProperty().bind(list.get(moins1).centerXProperty());
        line.startYProperty().bind((list.get(moins1).centerYProperty()));
        return line;
    }
    // dispose les cercles dans le panneaux
    private void setOnPane(Pane pane, ArrayList<Circle> list) {
        // place chacun des cercles  de list dans le pane

        for (Circle circle : list) {
            circle.setCenterX(randNumber(MAX_WIDTH));
            circle.setCenterY(randNumber(MAX_HEIGHT));
            pane.getChildren().add(circle);
        }

    }

    //remplie une liste de n cercle differents
    private void fillListCircle(ArrayList<Circle> list, int n) {
        for (int i = 0; i < n; i++) {
            list.add(createCircle(diametre,Color.RED));
        }
    }

    //cree un cercle
    private Circle createCircle(double diametre, Color color) {
        Circle c = new Circle(diametre,color);
        return c;
    }

    //genere un nombre aleatoire entre 0 et max
    private int randNumber(int max) {
        Random rd = new Random();
        int num = rd.nextInt(max);
        return num;
    }


    public static void main(String[] args) {
        launch(args);
    }
}

/**essai de gerer le deplacement d'un cercle dans son panneau sans y sortir mais fail */
/*
    //verifie les bords pour le deplacement
    private void checkBounds(Bounds b, Circle c, double newX, double newY) {
        double minX, minY, maxX, maxY;
        minX = b.getMinX();
        maxX = b.getMaxX();
        minY = b.getMinY();
        maxY = b.getMaxY();
        if (newX < minX) {
            newX = minX;
        } else if (newX > maxX) {
            newX = maxX;
        }
        if (newY < minY) {
            newY = minY;
        } else if (newY > maxY) {
            newY = maxY;
        }
        doMoveCircle(c,newX,newY);
    }

    //effectue le deplacement du cercle
    private void doMoveCircle(Circle c, double newX, double newY) {
        c.setCenterX(newX);
        c.setCenterY(newY);
        // Mémorisation la nouvelle position de la souris
        oldX = newX;
        oldY = newY;
    }

 */
/*IntegerProperty xRef = new SimpleIntegerProperty();
        xRef.bind(cRef.centerXProperty());
        IntegerProperty x1 = new SimpleIntegerProperty();
        x1.bind(c1.centerXProperty());
        IntegerProperty diffX = new SimpleIntegerProperty();
        diffX.bind(Bindings.subtract(xRef,x1));

        IntegerProperty yRef = new SimpleIntegerProperty();
        yRef.bind(cRef.centerYProperty());
        IntegerProperty y1 = new SimpleIntegerProperty();
        y1.bind(c1.centerYProperty());
        IntegerProperty diffY = new SimpleIntegerProperty();
        diffY.bind(Bindings.subtract(yRef,y1));

        IntegerProperty quotient = new SimpleIntegerProperty();
        quotient.bind(Bindings.divide(diffY,diffX));
        // y = ax + b ==> y = quotient*x + 0
        System.out.println(" mon quotient :"+quotient.getValue().toString());

        IntegerProperty x2 = new SimpleIntegerProperty();
        x2.bind(c1.centerXProperty());
        IntegerProperty diffX2 = new SimpleIntegerProperty();
        diffX2.bind(Bindings.subtract(xRef, x2));
        //on calcul y2 du coup
        IntegerProperty y2 = new SimpleIntegerProperty();
        y2.bind(Bindings.multiply(quotient, diffX2));
        //on affecte y2 aux Y de c2
        c2.centerYProperty().bind(Bindings.multiply(-1,y2));*/