package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.border.Border;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Main extends Application {
    protected static int TAILLE_HAUTEUR_TITRE_STAGE = 37;
    double oldX,oldY;
    static Stage primary_Stage;
    static int width_plateau = 850;
    static int height_plateau = 550;

    static final int DEFAULT_NIVEAU = 1;
    static int niveau = DEFAULT_NIVEAU;

    static int nombre_ligne;
    static int nombre_colonne;
    protected static Plateau plateau;
    protected static boolean est_lancable = false;


    protected static Plateau plateau_assemblage; //plateau blanc en dessous du puzzle
    static Image image;
    static Consumer<String> consumer = e -> System.out.println(e);

    //tableau static des pieces
    protected static Piece[][] tab_piece;

    // pour le trie et du coup drag and drop

    protected static int NB_ZONE = 5;// primary + bordure + red + green + blue
    protected static Stage[] tab_stage = new Stage[NB_ZONE];
    protected static Scene[] tab_scene = new Scene[NB_ZONE];
    protected static AnchorPane tab_pane[] = new AnchorPane[NB_ZONE];
    protected static ArrayList<Piece[][]> liste_tab_piece = new ArrayList<>();


    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primary_Stage = primaryStage;

        // programme principal
        puzzle_principale();

        // test divers
        //puzzle_test_divers();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                for (Stage stage : tab_stage) {
                    try {
                        stage.hide();
                    } catch (Exception e) {

                    }
                }
                Platform.exit();
                System.exit(0);
            }
        });
    }

    private void puzzle_test_divers() {
        BorderPane root = new BorderPane();
        //test_Piece(root);
        test_piece2(root);
        //test_bordure(root);
        //test_Plateau(root);
        this.primary_Stage.setTitle("Hello World");
        this.primary_Stage.setScene(new Scene(root, 1400, 1275));
        this.primary_Stage.show();
        //new Fenetre();
    }

    private void test_bordure(BorderPane root) {
        //Dents dents = new Dents(0, 100, 100, 200, 100, 1);
        Dents dents = new Dents();

        root.getChildren().addAll(dents.notre_path);
    }



    private void puzzle_principale() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Fenetre.fxml"));
        this.primary_Stage.setTitle("Jeu du Puzzle");
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        this.primary_Stage.setX(bounds.getMinX());
        this.primary_Stage.setY(bounds.getMinY());
        this.primary_Stage.setWidth(bounds.getWidth());
        this.primary_Stage.setHeight(bounds.getHeight());
        Scene scene =new Scene(root);
        this.primary_Stage.setResizable(false);
        this.primary_Stage.setScene(scene);
        this.primary_Stage.show();
    }
    private void test_Plateau(BorderPane root) {
        Plateau plateau = new Plateau(10, 10,100,60 );
        System.out.println("colonne : " + plateau.getNb_colonne() + " et ligne : " + plateau.getNb_ligne());
        for (int j = 0; j < plateau.getNb_colonne(); j++) {
            for (int i = 0; i < plateau.getNb_ligne() ; i++) {
                root.getChildren().add(plateau.getTab()[i][j].forme);
            }
        }
    }

    private void test_piece2(BorderPane root) {
        ArrayList<Forme_Bordure> list1 = new ArrayList<>();
        list1.add(null);list1.add(null);list1.add(null);list1.add(null);
        //list.add(new Bordure_Plate(0, 220, 220,270,270));list.add(new Bordure_Plate(1, 220, 220,270,27));list.add(new Bordure_Plate(2, 220, 220,270,270));list.add(new Bordure_Plate(3, 220, 220,270,270));
        Piece p1 = new Piece(list1, 220, 220, 300 , 400,3);
        ArrayList<Forme_Bordure> list = new ArrayList<>();
        //list.add(null);list.add(new Dents(1,220,220,300,400));list.add(null);list.add(null);
        list.add(null);
        list.add(null);
        list.add(null);
        list.add(p1.getTab_bordure()[1]); // on prend le DROITE de p1 pour faire le gAUCHE de p
        Piece p = new Piece(list, 220+400, 220, 300 , 400,3);
        File file = new File("./image/shingeki.jpg");
        // gestion p
        p.forme.setFill(new ImagePattern(new Image("file:"+file), 220, 220, p.longueur*2, p.hauteur, false));
        p.forme.setStrokeWidth(2);
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
        // gestion p1
        p1.forme.setFill(new ImagePattern(new Image("file:"+file), p1.getPosY(), p1.getPosY(), p1.longueur*2, p1.hauteur, false));
        // p.forme.setFill(Color.TRANSPARENT);
        p1.forme.setStrokeWidth(2);
        p1.forme.setStroke(Color.BLACK);
        p1.forme.setOnMousePressed(mouseEvent -> {
            oldX = mouseEvent.getX();
            oldY = mouseEvent.getY();
        });
        p1.forme.setOnMouseDragged(mouseEvent ->{
            double x = mouseEvent.getSceneX();
            double y = mouseEvent.getSceneY();
            p1.forme.setTranslateX(x - oldX);
            p1.forme.setTranslateY(y - oldY);
        });
        root.getChildren().addAll(p.forme, p1.forme);
        affiche_cerlce(root,p);
    }
    private void affiche_cerlce(BorderPane root, Piece p) {
        consumer.accept("Affiche cercle");
        for (int i = 0; i < p.liste_cercle.size(); i++) {
            consumer.accept("i ="+i);
            Color c = (Color)p.liste_cercle.get(i).getFill();
            consumer.accept("R:"+ c.getRed()+" G:"+c.getGreen()+" B:"+c.getBlue());
        }
    }
    private void test_Piece(BorderPane root) {
        ArrayList<Forme_Bordure> list = new ArrayList<>();
        //list.add(new Bordure_Plate(0, 20, 20));list.add(new Dents(20,20));list.add(new Dents(20,20));list.add(new Creux(new Dents(20,20)));
        list.add(null);list.add(new Dents(1,220,220,300,500,3));list.add(null);list.add(null);
        //list.add(new Bordure_Plate(0, 220, 220,270,270));list.add(new Bordure_Plate(1, 220, 220,270,27));list.add(new Bordure_Plate(2, 220, 220,270,270));list.add(new Bordure_Plate(3, 220, 220,270,270));
        Piece p = new Piece(list, 220, 220, 300 , 500,3);
        //setAllCircleOnPane(p,root);
        File file = new File("./image/shingeki.jpg");
        p.forme.setFill(new ImagePattern(new Image("file:"+file), p.getPosY(), p.getPosY(), p.longueur*2, p.hauteur, false));
       // p.forme.setFill(Color.TRANSPARENT);
        p.forme.setStrokeWidth(2);
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
    public double getOldX() {
        return oldX;
    }

    public void setOldX(double oldX) {
        this.oldX = oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public void setOldY(double oldY) {
        this.oldY = oldY;
    }

    public static Stage getPrimary_Stage() {
        return primary_Stage;
    }

    public static void setPrimary_Stage(Stage primary_Stage) {
        Main.primary_Stage = primary_Stage;
    }

    public static Plateau getPlateau_assemblage() {
        return plateau_assemblage;
    }

    public static void setPlateau_assemblage(Plateau plateau_assemblage) {
        consumer.accept("ajout du plateau dans le panneau");
        Main.plateau_assemblage = plateau_assemblage;
    }

}
