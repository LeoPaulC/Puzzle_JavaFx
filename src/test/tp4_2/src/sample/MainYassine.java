package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import java.util.Random;

public class MainYassine extends Application {
    private  Circle[]tab={createCircle(),createCircle(),createCircle(),createCircle(),
            createCircle(),createCircle(),createCircle()};
    private double oldY=0.0;
    private double oldX=0.0;

    public Circle createCircle(){
        return new Circle(8, Color.RED);
    }
    public void placeCircles(){
        Random r=new Random();
        for(int i=0;i<tab.length;i++){
            tab[i].setCenterX(r.nextInt(400));
            tab[i].setCenterY(r.nextInt(400));
        }
    }

    public CubicCurve newInstance(Circle start,Circle control1,Circle control2,Circle end){
        CubicCurve cubic = new CubicCurve();
        cubic.startXProperty().bind(start.centerXProperty());
        cubic.startYProperty().bind(start.centerYProperty());
        cubic.controlX1Property().bind(control1.centerXProperty());
        cubic.controlY1Property().bind(control1.centerYProperty());
        cubic.controlX2Property().bind(control2.centerXProperty());
        cubic.controlY2Property().bind(control2.centerYProperty());
        cubic.endXProperty().bind(end.centerXProperty());
        cubic.endYProperty().bind(end.centerYProperty());
        cubic.setFill(null);
        cubic.setStroke(Color.BLACK);
        return cubic;

    }

    public CubicCurve[] createCubicCurve(){
        CubicCurve cubic = newInstance(tab[0],tab[1],tab[2],tab[3]);
        CubicCurve cubic2= newInstance(tab[3],tab[4],tab[5],tab[6]);
        return new CubicCurve[]{cubic,cubic2};
    }


    public Line lineInstance(Circle start,Circle end){
        Line c=new Line();
        c.endXProperty().bind(end.centerXProperty());
        c.endYProperty().bind(end.centerYProperty());
        c.startXProperty().bind(start.centerXProperty());
        c.startYProperty().bind(start.centerYProperty());
        return c;
    }

    public Line[] createLines(){
        Line c1=lineInstance(tab[0],tab[1]);
        Line c2=lineInstance(tab[2],tab[3]);
        Line c3=lineInstance(tab[3],tab[4]);
        Line c4=lineInstance(tab[5],tab[6]);
        return new Line[] {c1,c2,c3,c4};
    }

    public void bindCircles(){
        for(int i=0;i<tab.length;i++){
            int finalI = i;
            tab[i].addEventHandler(MouseEvent.MOUSE_DRAGGED, new Controller(tab,finalI));
        }
    }
    public void setSymetry(){
        tab[4].setCenterY(2*tab[3].getCenterY()-tab[2].getCenterY());
        tab[4].setCenterX(2*tab[3].getCenterX()-tab[2].getCenterX());

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane root =new BorderPane();
        Pane canvas=new Pane();
        placeCircles();
        CubicCurve []cubic=createCubicCurve();
        Line [] lines=createLines();
        canvas.getChildren().addAll(tab);
        canvas.getChildren().addAll(cubic);
        canvas.getChildren().addAll(lines);
        root.setCenter(canvas);
        bindCircles();
        setSymetry();

        /*Rectangle rectangle = new Rectangle(250, 250, 150, 150);
        rectangle.setFill(Color.BLUE);
        Rectangle rectangle1 = new Rectangle(250,250,150,150);
        rectangle1.setTranslateX(300);
        rectangle1.setFill(Color.PINK);
        Shape shape = Shape.union(rectangle, rectangle1);
        shape.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                oldX = e.getX();
                oldY = e.getY();
            }
        });
        shape.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                double x = e.getX();
                double y = e.getY();
                shape.setScaleX(x-oldX);
                shape.setScaleY(y-oldY);
            }
        });
        shape.setFill(Color.LIGHTPINK);
        root.getChildren().addAll(shape);*/
        //root.getChildren().add();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}