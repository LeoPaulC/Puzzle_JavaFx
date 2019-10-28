package sample;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    Slider s1 ,s2 ;
    AnchorPane pann_slider , panel_cercle , panel_couleur ;
    ColorPicker cp1 ,cp2 ;

    javafx.scene.control.Label l1 , label_somme ;
    int v1 , valeur_somme ;


    Circle mon_cercle ;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        BorderPane root = new BorderPane();
        pann_slider = new AnchorPane();
        panel_cercle = new AnchorPane();
        panel_couleur = new AnchorPane();

        cp1 = new ColorPicker();
        cp2 = new ColorPicker();



        mon_cercle = new Circle(20,Color.WHITE);

        mon_cercle.setStyle("-fx-fill: white;\n" +
                "    -fx-stroke: green; -fx-stroke-dash-array: 20 10 8 8;\n" +
                "    -fx-stroke-dash-offset: 20;");
        v1 = 0 ; valeur_somme = 0 ;



        // gere le label binding avec les labels
        s1 = new Slider(0,200,50);
        s2 = new Slider(0,300,50);

        l1 = new Label();
        label_somme = new Label() ;

        IntegerProperty x1 = new SimpleIntegerProperty();
        IntegerProperty x2 = new SimpleIntegerProperty();

        NumberBinding somme =  Bindings.add(x1,x2);

        x1.bind(s1.valueProperty());
        x2.bind(s2.valueProperty());

        l1.textProperty().bind(x1.asString());

        label_somme.textProperty().bind(somme.asString());





        s1.setMinSize(50,480);
        s2.setMinSize(50,480);

        pann_slider.setBorder(new Border(new BorderStroke(Color.BLUE,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        panel_cercle.setBorder(new Border(new BorderStroke(Color.RED,
                BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        panel_couleur.setBorder(new Border(new BorderStroke(Color.GREEN,
                BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        s1.setShowTickMarks(true);
        s1.setShowTickLabels(true);

        s1.setOrientation(Orientation.VERTICAL);
        s2.setOrientation(Orientation.VERTICAL);

        s2.setShowTickMarks(true);
        s2.setShowTickLabels(true);

        AnchorPane.setLeftAnchor(s1, (double) 10);
        AnchorPane.setLeftAnchor(s2, (double) 70);

        AnchorPane.setLeftAnchor(mon_cercle,(double) 50);
        AnchorPane.setTopAnchor(mon_cercle,(double)200);

        AnchorPane.setTopAnchor(cp1,1.0);
        AnchorPane.setTopAnchor(cp2,26.0);

        pann_slider.setMinHeight(500);

        panel_couleur.getChildren().add(cp1);
        panel_couleur.getChildren().add(cp2);

        pann_slider.getChildren().add(s1);
        pann_slider.getChildren().add(s2);

        panel_cercle.getChildren().add(mon_cercle);
        panel_cercle.getChildren().add(l1);
        panel_cercle.getChildren().add(label_somme);

        AnchorPane.setTopAnchor(l1,(double)10);
        AnchorPane.setTopAnchor(label_somme,30.0);
        AnchorPane.setLeftAnchor(l1,(double)10);
        AnchorPane.setLeftAnchor(label_somme,10.0);



        s1.valueProperty().bindBidirectional(s2.valueProperty());
        mon_cercle.radiusProperty().bind(s1.valueProperty());
        mon_cercle.fillProperty().bind(cp1.valueProperty());
        mon_cercle.strokeProperty().bind(cp2.valueProperty());

        root.setLeft(pann_slider);
        root.setCenter(panel_cercle);
        root.setRight(panel_couleur);


        primaryStage.setTitle("TP2 : Slider / BINDING ");
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
