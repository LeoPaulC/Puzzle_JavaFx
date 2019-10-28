package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.ColorInput;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import javax.swing.border.Border;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        BorderPane root = new BorderPane() ;

        //root.getChildren().add(new Piece_puzzle(0,0,0,0)) ;


        Piece_puzzle ma_piece_test = new Piece_puzzle(0,0,500,500);

        for (Circle c : ma_piece_test.points_du_cadre ) {
            root.getChildren().add(c);
        }
        for (Circle c : ma_piece_test.point_de_controle){
            root.getChildren().add(c);
        }
        Path path = new Path();
        for (int i = 0; i < ma_piece_test.ma_liste_de_move_to.size(); i++) {

            path.getElements().add(ma_piece_test.ma_liste_de_move_to.get(i));
            path.getElements().add(ma_piece_test.ligne.get(i));
        }
        System.out.println("getFill :: " + path.getFill() ) ;


        //path.setFill(Color.RED);
        //path.setStroke(Color.BLUE);
        path.setFillRule(FillRule.NON_ZERO);
        System.out.println("getFill :: " + path.getFill() ) ;


        root.getChildren().add(path);


        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1000, 1000));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
