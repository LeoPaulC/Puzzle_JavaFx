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
import javafx.scene.layout.Pane;
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
        BorderPane root = new BorderPane();
        //test_Piece(root);
        Plateau plateau = new Plateau(10, 15,80,50 );
        Piece tab[][] = new Piece[plateau.getNb_ligne()][plateau.getNb_colonne()];
        System.out.println("colonne : " + plateau.getNb_colonne() + " et ligne : " + plateau.getNb_ligne());
        for (int j = 0; j < plateau.getNb_colonne(); j++) {
            for (int i = 0; i < plateau.getNb_ligne() ; i++) {
                root.getChildren().add(plateau.getTab()[i][j].forme);
            }
        }
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1400, 1275));
        primaryStage.show();
    }
    private void test_Piece(BorderPane root) {
        ArrayList<Forme_Bordure> list = new ArrayList<>();
        //list.add(new Bordure_Plate(0, 20, 20));list.add(new Dents(20,20));list.add(new Dents(20,20));list.add(new Creux(new Dents(20,20)));
        list.add(null);list.add(null);list.add(null);list.add(null);
        //list.add(new Bordure_Plate(0, 220, 220,270,270));list.add(new Bordure_Plate(1, 220, 220,270,27));list.add(new Bordure_Plate(2, 220, 220,270,270));list.add(new Bordure_Plate(3, 220, 220,270,270));
        Piece p = new Piece(list, 220, 220, 200 , 500);
        setAllCircleOnPane(p,root);
        p.forme.setFill(Color.TRANSPARENT);
        p.forme.setStrokeWidth(5);
        p.forme.setStroke(Color.BLACK);
        p.forme.setOnMousePressed(mouseEvent -> {
            oldX = mouseEvent.getX();
            oldY = mouseEvent.getY();
        });
        p.forme.setOnMouseDragged(mouseEvent ->{
            double x = mouseEvent.getSceneX();
            double y = mouseEvent.getSceneY();
            p.forme.setTranslateX(x - oldX);
            p.forme.setTranslateY(y - oldY);
        });
        root.getChildren().add( p.forme);

    }

    private void setAllCircleOnPane(Piece p, BorderPane pane) {
        for (Forme_Bordure forme_bordure : p.tab_bordure) {
            forme_bordure.liste_cercle.forEach(circle -> {
                pane.getChildren().add(circle);
            });
            if (!forme_bordure.getEst_plat()) {
                forme_bordure.liste_cercle_controle.forEach(circle -> {
                    pane.getChildren().add(circle);
                });
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
