package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        BorderPane root = new BorderPane();

        Dents d = new Dents();

        //d.liste_cercle.forEach( c -> root.getChildren().add(c));
        //d.liste_cercle_controle.forEach( c -> root.getChildren().add(c));
        //root.getChildren().add(d.notre_path);


        // on ajoute un rectangle a une shape pour obtenir une semi-piece
        Rectangle rect = new Rectangle();
        rect.setWidth(Forme_Bordure.TAILLE_COTE_PIECE_LONGUEUR);
        rect.setHeight(Forme_Bordure.TAILLE_COTE_PIECE_HAUTEUR);
        rect.setLayoutX(d.liste_cercle.get(0).getLayoutX());
        rect.setLayoutY(d.liste_cercle.get(0).getLayoutY());


        Shape s =  Shape.union(d.notre_path,rect);
        d.notre_path.setFillRule(FillRule.EVEN_ODD);
        d.notre_path.setFill(Color.RED);



        Shape ss = Path.union(d.notre_path,rect);

        ss.setFill(Color.RED);

        //s.setStroke(Color.BLUE);

        //d.notre_path.fillProperty().bind(s.fillProperty() );


        //d.notre_path.setFill(Color.RED);

        //s.setFill(Color.RED);

        root.getChildren().add(ss);










        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1400, 1275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
