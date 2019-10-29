package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        BorderPane root = new BorderPane();

        Dents d = new Dents();

        d.liste_cercle.forEach( c -> root.getChildren().add(c));





        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1000, 1275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
