package sample;

import javafx.animation.FillTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        BorderPane root = new BorderPane();

        //Dents d = new Dents();

        //d.liste_cercle.forEach( c -> root.getChildren().add(c));
        //d.liste_cercle_controle.forEach( c -> root.getChildren().add(c));
        //root.getChildren().add(d.notre_path);


        // on ajoute un rectangle a une shape pour obtenir une semi-piece
        /*Rectangle rect = new Rectangle();
        rect.setWidth(Forme_Bordure.TAILLE_COTE_PIECE_LONGUEUR);
        rect.setHeight(Forme_Bordure.TAILLE_COTE_PIECE_HAUTEUR);
        rect.setLayoutX(d.liste_cercle.get(0).getLayoutX());
        rect.setLayoutY(d.liste_cercle.get(0).getLayoutY());

        Dents d2 = new Dents(d);
        System.out.println(" mon layout X : "+d2.liste_cercle.get(0).getLayoutX());
        Shape s =  Shape.union(d.notre_path,rect);

        //d.notre_path.setFillRule(FillRule.EVEN_ODD);
        //d.notre_path.setFill(Color.RED);
        //root.getChildren().addAll(d2);

        //Shape ss = Path.union(d.notre_path,rect);

        //ss.setFill(Color.RED);

        //s.setStroke(Color.BLUE);

        //d.notre_path.fillProperty().bind(s.fillProperty() );


        //d.notre_path.setFill(Color.RED);

        //s.setFill(Color.RED);
        Dents d3 = new Dents(d);
        Dents d4 = new Dents(d);
        d2.notre_path.getTransforms().add(new Rotate(270, d.getListe_cercle().get(6).getLayoutX(), d.getListe_cercle().get(6).getLayoutY()));
        //d3.notre_path.getTransforms().add(new Rotate(90));
        d3.notre_path.getTransforms().add(new Translate(0,Forme_Bordure.getTailleCotePieceHauteur() ));
        //d3.notre_path.setTranslateY(Forme_Bordure.getTailleCotePieceHauteur());
        d4.notre_path.getTransforms().add(new Rotate(90, d2.getListe_cercle().get(0).getLayoutX(), d.getListe_cercle().get(0).getLayoutY()));



        root.getChildren().addAll(d4.notre_path,d3.notre_path,d2.notre_path,d.notre_path);

         */
/*
        Piece piece_test = new Piece();
        FillTransition fillTransition = new FillTransition(Duration.minutes(2),piece_test.forme,Color.RED,Color.RED);
        fillTransition.setAutoReverse(true);
        fillTransition.setCycleCount(5);
        fillTransition.play();

        //root.getChildren().add(piece_test.forme);
        Dents d = new Dents();
        Creux c = new Creux(d);


        root.getChildren().addAll(d.notre_path, c.notre_path);
        */
        ArrayList<Forme_Bordure> list = new ArrayList<>();
        list.add(null);list.add(null);list.add(null);list.add(null);
        Piece p = new Piece(list);
        //setAllCircleOnPane(p,root);

       // p.MAJ_Path();

        p.getPath().setFillRule(FillRule.NON_ZERO);



        Shape s = p.getPath();

        s.setFill(Color.BLUE);


        for (Circle c : p.circle)
            root.getChildren().add(c);

        for (Circle c : p.controle)
            root.getChildren().add(c);

        //root.getChildren().add(s);
        //root.getChildren().add(gc.getCanvas());
       // root.getChildren().addAll(d.notre_path);
        //setAllCircleOnPane(piece_test,root);








        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1400, 1275));
        primaryStage.show();
    }

    private void setAllCircleOnPane(Piece p, BorderPane pane) {
        for (Forme_Bordure forme_bordure : p.tab_bordure) {
            forme_bordure.liste_cercle.forEach(circle -> {
                pane.getChildren().add(circle);
            });
            forme_bordure.liste_cercle_controle.forEach(circle -> {
                pane.getChildren().add(circle);
            });
        }
        /*p.liste_bordure.forEach(forme_bordure -> {
            forme_bordure.liste_cercle.forEach(circle -> {
                pane.getChildren().add(circle);
            });
            forme_bordure.liste_cercle_controle.forEach(circle -> {
                pane.getChildren().add(circle);
            });
        });

         */
    }

    public static void main(String[] args) {
        launch(args);
    }
}
