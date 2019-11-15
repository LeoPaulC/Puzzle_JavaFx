package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {
    double oldX,oldY;
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

        //root.getChildren().add(piece_test.forme);
        Dents d = new Dents();
        Creux c = new Creux(d);


        root.getChildren().addAll(d.notre_path, c.notre_path);
        */

        ArrayList<Forme_Bordure> list = new ArrayList<>();
        list.add(null);list.add(null);list.add(null);list.add(null);
        Piece p = new Piece(list);
        //p.path.setFillRule(FillRule.EVEN_ODD);
        //p.path.setFill(new ImagePattern(new Image("file:index.jpeg"), 0,0,100,100,false));
        //p.path.setFill(Color.GREEN);
        setAllCircleOnPane(p,root);
        p.setOnMousePressed(mouseEvent -> {
            oldX = mouseEvent.getX();
            oldY = mouseEvent.getY();
        });
        p.setOnMouseDragged(mouseEvent ->{
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();
            p.setTranslateY(y - oldY);
            p.setTranslateX(x - oldX);
        });

        root.getChildren().add(   p.path);

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
    }

    public static void main(String[] args) {
        launch(args);
    }
}
