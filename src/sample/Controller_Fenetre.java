package sample;


import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;
import java.util.function.Consumer;

import static sample.Main.*;

public class Controller_Fenetre  {

    // attribut "GLOBAL"
    private final static String DEFAULT_FILE = "./image/congratulations.gif";
    private static final int DECALAGE_PLATEAU_ASSEMBLAGE = 25;
    private static final double MARGE_ASSEMBLAGE = 5.0;
    private Stage stage;
    private Scene scene;
    private static final double min_longueur = Piece.getMinLongueur();
    private static final double min_hauteur = Piece.getMinHauteur();
    private boolean accueil = true;
    private Consumer<String> consumer = e -> System.out.println(e);
    private double oldX;
    private double oldY;

    // page principale
    @FXML private AnchorPane pane_principal;
    @FXML private MenuBar menuBar;
    @FXML private MenuItem open;
    @FXML private MenuItem lancement;
    @FXML private MenuItem quit;
    @FXML private MenuItem save_as;
    @FXML private MenuItem new_puzzle;
    @FXML private MenuItem trier;
    @FXML private Menu zones;
    @FXML private MenuItem z_bordure;
    @FXML private MenuItem z_rouge;
    @FXML private MenuItem z_verte;
    @FXML private MenuItem z_bleue;
    @FXML private VBox box_contour;
    @FXML private Label label_bienvenue;
    @FXML private AnchorPane pane_assemblage;
    //parametrage
    @FXML private Button bouton_image;
    @FXML private Spinner<Integer> spinner_colonne;
    @FXML private Spinner<Integer> spinner_ligne;
    @FXML private Label path_image;
    @FXML private Label nb_colonne;
    @FXML private Label nb_ligne;
    @FXML private Label max_colonne;
    @FXML private Label max_ligne;
    @FXML private Label txt_niveau;
    @FXML private RadioButton radioButton;
    @FXML private RadioButton radioButton2;
    @FXML private RadioButton radioButton3;
    @FXML private RadioButton radioButton4;

    public Controller_Fenetre() {
        this.stage = Main.getPrimary_Stage();
        this.scene = stage.getScene();
    }
    @FXML
    private void fill_lancement() {
        if (est_lancable) lancement();
    }
    //lance un puzzle a partir du plateau etc ...
    private void lancement() {
        hide_titre();
        create_Plateau();
        Rectangle rectangle = create_Rectangle();
        pane_assemblage.getChildren().add(rectangle);
        gestion_affichage_box();
        Plateau p = new Plateau(plateau);
        setPlateau_assemblage(p); // MAJ le plateaud'assemblage == plateau blanc en dessous du puzzle
        set_plateau_on_pane(p);
        set_plateau_on_pane(plateau);
        gestion_evenement_plateau();
        affichage_test(4,plateau.getTab());
        split_piece();
        affichage_test(4,plateau.getTab());
        trier.setDisable(false);
        //init du tableau static de piece
        tab_piece = plateau.tab.clone();
        //consumer.accept("ICI");
        //affichage_test(5, tab_piece);
        //liste(1) == liste principale de piece
        liste_tab_piece.add(plateau.tab.clone());/// indice 0
    }


    @FXML
    private void fill_z_bordure() {
        int indice = 1;
        tab_stage[indice].hide();
        tab_stage[indice].show();
    }
    @FXML
    private void fill_z_rouge() {
        int indice = 2;

        tab_stage[indice].hide();
        tab_stage[indice].show();
    }
    @FXML
    private void fill_z_verte() {
        int indice = 3;
        tab_stage[indice].hide();
        tab_stage[indice].show();
    }
    @FXML
    private void fill_z_bleue() {
        int indice = 4;
        tab_stage[indice].hide();
        tab_stage[indice].show();
    }

    @FXML
    private void fill_trier() {
        if (plateau == null) {
            consumer.accept("plateau est null !! ");
            return ;
        }
        consumer.accept("dans fill trier");
        affichage_test(4,plateau.getTab());
        // creation des diff zones de trie

        Maj_stage_principale();
        consumer.accept("apres maj stage principale");
        affichage_test(4,plateau.getTab());
        create_stage_bordure();
        create_stage_rouge();
        create_stage_verte();
        create_stage_bleue();

        // different tableau de piece pour la differentiationdes piece = TRIE
        // une piece du tableau static
        Piece[][] tab_piece_bordure = new Piece[nombre_ligne][nombre_colonne];
        fill_tab_piece_bordure(tab_piece_bordure);
        Piece[][] tab_piece_red = new Piece[nombre_ligne][nombre_colonne];
        Piece[][] tab_piece_green = new Piece[nombre_ligne][nombre_colonne];
        Piece[][] tab_piece_blue = new Piece[nombre_ligne][nombre_colonne];
        fill_tab_piece_color(tab_piece_blue, tab_piece_green, tab_piece_red, tab_piece_bordure);
        // MAJ la liiste de tableau de piece static
        fill_liste_tab_piece(tab_piece_blue, tab_piece_green, tab_piece_red, tab_piece_bordure);
        //TODO: ajouter les events aux pieces des listes afin de faire du DND vers le panneau principal

        //vider le panneau principale de ses pieces
        unfill_piece_pane();
        affichage_test(4,plateau.getTab());
        // affichage
       /* affichage_test(0, tab_piece_bordure);
        affichage_test(1, tab_piece_red);
        affichage_test(2, tab_piece_green);
        affichage_test(3, tab_piece_blue);
        affichage_test(5, tab_piece);
        */


        //visibilité de la partie du menu corresp. au trie
        zones.setDisable(false);
        z_bordure.setVisible(true);
        z_rouge.setVisible(true);
        z_verte.setVisible(true);
        z_bleue.setVisible(true);

        // remplissage des diff zones de trie
        fill_stage_trie();

    }

    //enleve les pieces presentes dans les zones de trie du panneau d'assemblage
    private void unfill_piece_pane() {
        for (int k = 1; k < NB_ZONE; k++) {
            for (int i = 0; i < liste_tab_piece.get(1).length; i++) {
                for (int j = 0; j < liste_tab_piece.get(1)[0].length; j++) {
                    if (liste_tab_piece.get(k)[i][j] != null) {
                        if (liste_tab_piece.get(0)[i][j] != null) {
                            pane_assemblage.getChildren().remove(liste_tab_piece.get(0)[i][j].path);
                            //liste_tab_piece.get(0)[i][j] = null;
                        }
                    }
                }
            }

        }
    }
    private void fill_liste_tab_piece(Piece[][] tab_piece_blue,Piece[][] tab_piece_green, Piece[][] tab_piece_red, Piece[][] tab_piece_bordure) {
        add_evenement_tab_piece(tab_piece_bordure);
        liste_tab_piece.add(tab_piece_bordure);
        add_evenement_tab_piece(tab_piece_red);
        liste_tab_piece.add(tab_piece_red);
        add_evenement_tab_piece(tab_piece_green);
        liste_tab_piece.add(tab_piece_green);
        add_evenement_tab_piece(tab_piece_blue);
        liste_tab_piece.add(tab_piece_blue);
    }

    //remplie les zone de trie en fonction de leur tableau de pieces
    private void fill_stage_trie() {
        for (int i = 1; i <5 ; i++) {
           // consumer.accept("fill stage trie indice :"+i);
            for (Piece[] pieces : liste_tab_piece.get(i)) {
                for (Piece piece : pieces) {
                    if (piece != null) {
                        tab_pane[i].getChildren().add(piece.path);
                    }
                }
            }
        }
    }

    private void Maj_stage_principale() {
        tab_stage[0] = primary_Stage;
        tab_scene[0] = primary_Stage.getScene();
        tab_pane[0] = pane_principal;
    }
    // cree la zone de trie correspondant aux bordure
    private void create_stage_bordure() {
        Stage ma_stage = new Stage();
        ma_stage.setWidth(stage.getWidth() / 3);
        ma_stage.setHeight(stage.getHeight() - menuBar.getHeight()-TAILLE_HAUTEUR_TITRE_STAGE);
        ma_stage.setX(stage.getX()+ma_stage.getWidth()*2);// fenetre dans le diernier tier de l'ecran
        ma_stage.setY(stage.getY()+menuBar.getHeight()+TAILLE_HAUTEUR_TITRE_STAGE);
        AnchorPane mon_pane = new AnchorPane();
        Scene ma_scene = new Scene(mon_pane);
        ma_stage.setScene(ma_scene);
        ma_stage.setTitle("Pieces Bordures");
        mon_pane.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        ma_stage.hide(); // invisible a la creation
        ma_stage.setResizable(false);
        ma_stage.initStyle(StageStyle.UNDECORATED);
        //MAJ des champs static
        tab_stage[1] = ma_stage;
        tab_scene[1] = ma_scene;
        tab_pane[1] = mon_pane;
    }
    // cree la zone de trie correspondant aux bordure
    private void create_stage_rouge() {
        Stage ma_stage = new Stage();
        ma_stage.setWidth(stage.getWidth() / 3);
        ma_stage.setHeight(stage.getHeight());
        ma_stage.setX(stage.getX()+ma_stage.getWidth()*2);// fenetre dans le diernier tier de l'ecran
        ma_stage.setY(stage.getY());
        AnchorPane mon_pane = new AnchorPane();
        Scene ma_scene = new Scene(mon_pane);
        ma_stage.setScene(ma_scene);
        ma_stage.setTitle("Pieces Rouges");
        mon_pane.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
        ma_stage.hide(); // invisible a la creation
        ma_stage.setResizable(false);
        ma_stage.initStyle(StageStyle.UNDECORATED);
        //MAJ des champs static
        tab_stage[2] = ma_stage;
        tab_scene[2] = ma_scene;
        tab_pane[2] = mon_pane;
    }
    // cree la zone de trie correspondant aux bordure
    private void create_stage_verte() {
        Stage ma_stage = new Stage();
        ma_stage.setWidth(stage.getWidth() / 3);
        ma_stage.setHeight(stage.getHeight());
        ma_stage.setX(stage.getX()+ma_stage.getWidth()*2);// fenetre dans le diernier tier de l'ecran
        ma_stage.setY(stage.getY());
        AnchorPane mon_pane = new AnchorPane();
        Scene ma_scene = new Scene(mon_pane);
        ma_stage.setScene(ma_scene);
        ma_stage.setTitle("Pieces Vertes");
        mon_pane.setBackground(new Background(new BackgroundFill(Color.DARKGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        ma_stage.hide(); // invisible a la creation
        ma_stage.setResizable(false);
        ma_stage.initStyle(StageStyle.UNDECORATED);
        //MAJ des champs static
        tab_stage[3] = ma_stage;
        tab_scene[3] = ma_scene;
        tab_pane[3] = mon_pane;
    }
    // cree la zone de trie correspondant aux bordure
    private void create_stage_bleue() {
        Stage ma_stage = new Stage();
        ma_stage.setWidth(stage.getWidth() / 3);
        ma_stage.setHeight(stage.getHeight());
        ma_stage.setX(stage.getX()+ma_stage.getWidth()*2);// fenetre dans le diernier tier de l'ecran
        ma_stage.setY(stage.getY());
        AnchorPane mon_pane = new AnchorPane();
        Scene ma_scene = new Scene(mon_pane);
        ma_stage.setScene(ma_scene);
        ma_stage.setTitle("Pieces Bleues");
        mon_pane.setBackground(new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        ma_stage.hide(); // invisible a la creation
        ma_stage.setResizable(false);
        ma_stage.initStyle(StageStyle.UNDECORATED);
        //MAJ des champs static
        tab_stage[4] = ma_stage;
        tab_scene[4] = ma_scene;
        tab_pane[4] = mon_pane;
    }

    private void affichage_test(int i, Piece[][] tab) {
        if(i == 0) consumer.accept("tableau de BORD");
        if(i == 1) consumer.accept("tableau de ROUGE");
        if(i == 2) consumer.accept("tableau de VERT");
        if(i == 3) consumer.accept("tableau de BLEUE");
        if(i == 4) consumer.accept("tableau du plateau");
        if(i == 5) consumer.accept("tableau du tab_piece");
        consumer.accept("nb ligne :"+tab.length+", nb colonne l"+tab[0].length);
        for (Piece[] pieces : tab) {
            for (Piece piece : pieces) {
                if (piece != null) {
                    System.out.print("1");
                }else {
                    System.out.print(".");
                }
            }
            consumer.accept("");
        }
        if (i == 4) {
            consumer.accept("piece 0,0 : "+tab[0][0].getLayoutX()+";"+tab[0][0].getLayoutY());
        }
    }
    // remplie les tableau de piece color en fonction de la couleur majoritaire de chaque piece du tab de piece static
    private void fill_tab_piece_color(Piece[][] tab_piece_blue,Piece[][] tab_piece_green, Piece[][] tab_piece_red, Piece[][] tab_piece_bordure) {
        for (int i = 0; i < liste_tab_piece.get(0).length; i++) {
            for (int j = 0; j < liste_tab_piece.get(0)[0].length; j++) {

                ImagePattern pattern = (ImagePattern) liste_tab_piece.get(0)[i][j].path.getFill();
                Image im = pattern.getImage();
                Color c = get_color_majoritaire(liste_tab_piece.get(0)[i][j],im); // renvoie la couleur majoritaire de la piece
                if ( c == Color.RED && tab_piece_bordure[i][j] == null) {
                    Piece p = new Piece(liste_tab_piece.get(0)[i][j],tab_pane[2]);
                    p.path.setFill(liste_tab_piece.get(0)[i][j].path.getFill());
                    p.path.setStroke(liste_tab_piece.get(0)[i][j].path.getStroke());
                    p.path.setLayoutX(rand_coord_X(tab_stage[1],p));
                    p.path.setLayoutY(rand_coord_Y(tab_stage[1],p));
                    tab_piece_red[i][j] = p;
                } else if (c == Color.GREEN && tab_piece_bordure[i][j] == null) {
                    Piece p = new Piece(liste_tab_piece.get(0)[i][j],tab_pane[3]);
                    p.path.setFill(liste_tab_piece.get(0)[i][j].path.getFill());
                    p.path.setStroke(liste_tab_piece.get(0)[i][j].path.getStroke());
                    p.path.setLayoutX(rand_coord_X(tab_stage[1],p));
                    p.path.setLayoutY(rand_coord_Y(tab_stage[1],p));
                    tab_piece_green[i][j] = p;
                }  else if (c == Color.BLUE && tab_piece_bordure[i][j] == null) {
                    Piece p = new Piece(liste_tab_piece.get(0)[i][j],tab_pane[4]);
                    p.path.setFill(liste_tab_piece.get(0)[i][j].path.getFill());
                    p.path.setStroke(liste_tab_piece.get(0)[i][j].path.getStroke());
                    p.path.setLayoutX(rand_coord_X(tab_stage[1],p));
                    p.path.setLayoutY(rand_coord_Y(tab_stage[1],p));
                    tab_piece_blue[i][j] = p;
                } else {
                    //consumer.accept("la piece i:" + i + ", j:" + j + " est deja dans le tableau des bords");
                }
            }
        }
    }

    private Color get_color_majoritaire(Piece p,Image image) {
        int cpt_red = 0;
        int cpt_green = 0;
        int cpt_blue = 0;
        int minX = (int) p.getMinX();
        int maxX = (int) p.getMaxX();
        int minY = (int) p.getMinY();
        int maxY = (int) p.getMaxY();
        PixelReader reader = image.getPixelReader();
        for (int i = minX; i < maxX; i++) {
            for (int j = minX; j < maxY ; j++) {
                Color c = reader.getColor(i, j);
                if (c.getRed() > c.getGreen() && c.getRed() > c.getBlue()) {
                    cpt_red++;
                } else if (c.getGreen() > c.getRed() && c.getGreen() > c.getBlue()) {
                    cpt_green++;
                } else if (c.getBlue() > c.getRed() && c.getBlue() > c.getGreen()) {
                    cpt_blue++;
                }
            }
        }
        if (cpt_blue > cpt_green && cpt_blue > cpt_red) {
            return Color.BLUE;
        } else if (cpt_green > cpt_red && cpt_green > cpt_blue) {
            return Color.GREEN;
        } else {
            return Color.RED;
        }
    }



    // remplie un tableau avec les piece des bord du tableau de piece static
    private void fill_tab_piece_bordure(Piece [][] tab_piece_bordure) {
        for (int i = 0; i < liste_tab_piece.get(0).length; i++) {
            for (int j = 0; j < liste_tab_piece.get(0)[0].length; j++) {

                boolean est_piece_bord = false;
                for (int k = 0; k < liste_tab_piece.get(0)[i][j].tab_bordure.length; k++) {
                   // consumer.accept("K K K K K :" +k);
                    if (liste_tab_piece.get(0)[i][j].tab_bordure[k].getClass() == Bordure_Plate.class) {
                        est_piece_bord = true;
                    }
                }
                if (est_piece_bord) { // alors la piece a au moins un cote qui est un bord
                    //tab_piece_bordure[i][j] = tab_piece[i][j];
                    Piece p = new Piece(liste_tab_piece.get(0)[i][j],tab_pane[1]);
                    p.path.setFill(liste_tab_piece.get(0)[i][j].path.getFill());
                    p.path.setStroke(liste_tab_piece.get(0)[i][j].path.getStroke());
                    p.path.setLayoutX(rand_coord_X(tab_stage[1],p));
                    p.path.setLayoutY(rand_coord_Y(tab_stage[1],p));
                    tab_piece_bordure[i][j] = p;
                    //affichage_test(5, tab_piece_bordure);
                    //consumer.accept("liistte tab cette fois");
                    //affichage_test(5,liste_tab_piece.get(0));
                }

            }
        }
        //liste_tab_piece.add(tab_piece_bordure);
    }

    // renvoie une nouvelle valeur x pour une piece dans une scene
    private double rand_coord_X(Stage stage, Piece p) {
       // consumer.accept("scene width : "+ stage.getWidth());
        //double x = randNumber(0.0, stage.getWidth()/2 - p.longueur);
        double x = randNumber(0.0- p.getMinX(), stage.getWidth()/2 - p.getMinX());
        //consumer.accept(" coord x :"+x);
        return x;
    }

    // renvoie une nouvelle valeur y pour une piece dans une scene
    private double rand_coord_Y(Stage stage, Piece p) {
        //consumer.accept("scene heigth : "+ stage.getHeight());
        //double y = randNumber(20, stage.getHeight()/2);
        double y = randNumber(stage.getHeight()*0.05 - p.getMinY() ,stage.getHeight()*0.7 - p.getMinY());
        //consumer.accept(" coord x :" + y);
        return y;
    }

    // disperse les pieces du puzzle en random dans la scene au lancement du jeu
    private void split_piece() {

        for (Piece[] pieces : plateau.getTab()) {
            for (Piece piece : pieces) {
                // max et min prennent en compte l'appendice
                double minX = box_contour.getLayoutX() + piece.getMinX();
                double maxX = box_contour.getLayoutX() + piece.getMaxX();
                double minY = (box_contour.getLayoutX() / 2) - menuBar.getHeight() * 2 + piece.getMinY();
                double maxY = box_contour.getLayoutY() + piece.getMaxY();
                double x1 = -randNumber(0, minX);// a gauche du 0 relatif au layout de la piece
                double x2 = randNumber(0, scene.getWidth() - maxX);
                double y1 = -randNumber(0, minY);// a gauche du 0 relatif au layout de la piece
                double y2 = randNumber(0, scene.getHeight() - maxY);
                /*consumer.accept("");
                consumer.accept("coordonnees max min piece : (on prend en compte l'appendice)");
                Main.consumer.accept("scene width : " + scene.getWidth());
                Main.consumer.accept("scene heigth : " + scene.getHeight());
                consumer.accept("x : " + minX + " ; " + maxX);
                consumer.accept("y : " + minY + " ; " + maxY);
                consumer.accept("x1:" + x1 + " ; x2:" + x2);
                consumer.accept("y1:" + y1 + " ; y2:" + y2);*/
                piece.path.setLayoutX(rand_choice(x1, x2));
                piece.path.setLayoutY(rand_choice(y1, y2));
                consumer.accept("rand layout :"+piece.path.getLayoutX() +"; "+piece.path.getLayoutY());

            }
        }

    }

    private double rand_choice(double choix1,double choix2) {
        int rand = new Random().nextInt(2);
        double res = 0;
        if (rand == 1) { // alors signe negatif
            res = choix1;
        } else if (rand == 0) { // alors signe positif
            res = choix2;
        }
        //Main.consumer.accept("rand choix :"+rand);
        return res;
    }
    private double randNumber(double min, double max) {
        Random r = new Random();
        double randomValue = min + (max - min) * r.nextDouble();
        return randomValue;
    }
    private void gestion_affichage_box() {
        box_contour.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        box_contour.setMaxHeight(plateau.getNb_ligne() * plateau.getHauteur());
        box_contour.setMaxWidth(plateau.getNb_colonne() * plateau.getLongueur());

    }

    public void affichage_fin() {
        pane_assemblage.getChildren().clear();
        box_contour.setBackground(new Background(new BackgroundImage(new Image("file:" + DEFAULT_FILE),BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,new BackgroundSize(box_contour.getMaxWidth(),box_contour.getMaxHeight(),false,false,false,false))));
    }

    private Rectangle create_Rectangle() {
        Rectangle rectangle = new Rectangle();
        rectangle.setX(0);
        rectangle.setY(0);
        rectangle.setFill(Color.WHITE);
        rectangle.setHeight(plateau.getNb_ligne() * plateau.getHauteur());
        rectangle.setWidth(plateau.getNb_colonne() * plateau.getLongueur());
        rectangle.setStrokeWidth(3.5);
        rectangle.setStroke(Color.BLACK);
        return rectangle;
    }
    //ajoute des evenement aux pieces du plateau
    private void gestion_evenement_plateau() {
        Piece[][] tab = plateau.getTab();
        for (int i = 0; i < tab.length ; i++) {
            for (int j = 0; j <tab[0].length ; j++) {
                ajout_event_piece(plateau.getTab()[i][j]);
            }
        }
    }

    private void add_evenement_tab_piece(Piece[][] tab) {
        for (int i = 0; i < tab.length ; i++) {
            for (int j = 0; j <tab[0].length ; j++) {
                ajout_event_piece(tab[i][j]);
            }
        }
    }
    // informe la piece du panneau qui l'a contient // sert pour la priorite visuel a l'affichage
    private void ajout_event_piece(Piece p) {
        if (p == null) {
            return ;
        }
        p.path.setOnMousePressed((new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                consumer.accept("dans event pressed");
                //gestion event dans le panneau courant de la piece
                double x = p.path.getLayoutX();
                double y = p.path.getLayoutY();
                consumer.accept("lay x:" + x + " lay y:" + y);
                p.panneau.getChildren().remove(p.path);
                p.panneau.getChildren().add(p.path);
                oldX = mouseEvent.getX();
                oldY = mouseEvent.getY();
                //gestion du Drag and Drop
                if (p.panneau == tab_pane[1] || p.panneau == tab_pane[2] || p.panneau == tab_pane[3] || p.panneau == tab_pane[4]) {
                    consumer.accept("piece d'une zone de trie");
                    p.piece_ombre = new Piece(p, tab_pane[0]);
                    p.piece_ombre.path.setStroke(p.path.getStroke());
                    p.piece_ombre.path.setFill(p.path.getFill());
                    p.piece_ombre.path.setLayoutX(p.path.getLayoutX() + p.panneau.getWidth() * 2);
                    p.piece_ombre.path.setLayoutY(p.path.getLayoutY());
                    tab_pane[0].getChildren().add(p.piece_ombre.path);
                }else{
                    consumer.accept("piece du panneau d'assemblage");
                }
            }
        }));
        p.path.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (p.isMovable) { // drag seulement si la piece est deplacable
                    double newX = mouseEvent.getX();
                    double newY = mouseEvent.getY();
                    p.path.setLayoutX(p.path.getLayoutX() + newX - oldX);
                    p.path.setLayoutY(p.path.getLayoutY() + newY - oldY);
                    if (p.piece_ombre != null) {
                        p.piece_ombre.path.setLayoutX((p.piece_ombre.path.getLayoutX() + newX - oldX));
                        p.piece_ombre.path.setLayoutY((p.piece_ombre.path.getLayoutY() + newY - oldY));
                    }
                }
            }
        });
        p.path.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                consumer.accept("dans event released");
                if (p.panneau == tab_pane[0] || p.panneau == pane_assemblage) { // null si normale
                    consumer.accept("piece du panneau principal");
                    consumer.accept("lay x:"+p.path.getLayoutX()+" lay y:"+p.path.getLayoutY());
                    if (p.path.getLayoutX() <= MARGE_ASSEMBLAGE && p.path.getLayoutX() >= -MARGE_ASSEMBLAGE) {
                        if (p.path.getLayoutY() <= MARGE_ASSEMBLAGE && p.path.getLayoutY() >= -MARGE_ASSEMBLAGE) {
                            // si placement approximativement convenable alors placement de la piece dans son espace d'assemblage
                            p.path.setLayoutX(0.0);
                            p.path.setLayoutY(0.0);
                            p.setMovable(false);
                            gestion_fin_jeu();
                        }
                    }
                }else if(p.panneau == tab_pane[1] || p.panneau == tab_pane[2] || p.panneau == tab_pane[3] || p.panneau == tab_pane[4]){ // gestion du drag and drop
                    //consumer.accept("getLayoutSceneX" + mouseEvent.getSceneX());
                    consumer.accept("piece d'une zonne de trie");
                    if (mouseEvent.getSceneX() < 0.0) { // si la piece est relaché a gauche de la zone de trie
                        //alors c'est qu'on a relache la piece dans le panneau principal 'PAS FORCEMENT'
                        // alors on pop la piece de son pane
                        //set_piece_on_pane(p);
                        for (int i = 0; i < liste_tab_piece.get(0).length; i++) {
                            for (int j = 0; j < liste_tab_piece.get(0)[0].length ; j++) {
                                if (liste_tab_piece.get(0)[i][j].path.getFill() == p.path.getFill()) {
                                    // TODO : gerer le decale=age entre ombre et piece principale
                                    tab_pane[0].getChildren().add(liste_tab_piece.get(0)[i][j].path);
                                    tab_pane[0].getChildren().remove(p.piece_ombre.path);
                                    p.panneau.getChildren().remove(p.path);
                                }
                            }
                        }
                    }else{
                        tab_pane[0].getChildren().remove(p.piece_ombre.path);
                        p.piece_ombre = null;
                    }
                }
            }
        });
    }
// buggé
    private void set_piece_on_pane(Piece piece) {
        int indice = 0; // ne peut pas etre 0 apres traitement
        //on recupere l'indice du panneau de la piece
        for (int i = 0; i < tab_pane.length; i++) {
            if (tab_pane[i] == piece.panneau) {
                indice = i;
            }
        }
        consumer.accept("indice du panneau de la piece =" + indice);
        int num_ligne = 0;
        int num_colonne = 0;
        // on recupere la case de la piece dans son tableau
        for (int i = 0; i < liste_tab_piece.get(indice).length; i++) {
            for (int j = 0; j < liste_tab_piece.get(indice)[0].length; j++) {
                if (liste_tab_piece.get(indice)[i][j] == piece) {
                    num_ligne = i;
                    num_colonne = j;
                }
            }
        }
        consumer.accept("num_ligne : " + num_ligne + "num_colonne :" + num_colonne);
        // on remet la piece correspondante dans le panneau d'assemblage
        //on place la piece du panneau d'assemblage a celle de la piece ombre que l'on a deplace depuis la zone de trie
        double x_ombre = piece.piece_ombre.path.getLayoutX();
        double y_ombre = piece.piece_ombre.path.getLayoutY();
        consumer.accept("layout x :"+tab_piece[num_ligne][num_colonne].getLayoutX()+" lah=yout y : "+tab_piece[num_ligne][num_colonne].getLayoutY());
        consumer.accept("liste tab piece 0    : layout x :"+liste_tab_piece.get(0)[num_ligne][num_colonne].getLayoutX()+" lah=yout y : "+liste_tab_piece.get(0)[num_ligne][num_colonne].getLayoutY() +menuBar.getHeight()+TAILLE_HAUTEUR_TITRE_STAGE);
        //plateau.getTab()[num_ligne][num_colonne].path.setTranslateX((x_ombre ) - plateau.getTab()[num_ligne][num_colonne].path.getLayoutX());
        //plateau.getTab()[num_ligne][num_colonne].path.setTranslateY(y_ombre - plateau.getTab()[num_ligne][num_colonne].path.getLayoutY());
        //tab_piece[num_ligne][num_colonne].path.setTranslateX((x_ombre ) - tab_piece[num_ligne][num_colonne].path.getLayoutX());
        //tab_piece[num_ligne][num_colonne].path.setTranslateY(y_ombre - tab_piece[num_ligne][num_colonne].path.getLayoutY());
        liste_tab_piece.get(0)[num_ligne][num_colonne].path.setTranslateX(piece.piece_ombre.getLayoutX() + tab_pane[indice].getWidth()*2 - box_contour.getLayoutX() - liste_tab_piece.get(0)[num_ligne][num_colonne].longueur*num_colonne);
        liste_tab_piece.get(0)[num_ligne][num_colonne].path.setTranslateY(piece.piece_ombre.getLayoutX() - box_contour.getLayoutY() - liste_tab_piece.get(0)[num_ligne][num_colonne].hauteur*num_ligne);
        tab_pane[0].getChildren().remove(piece.piece_ombre.path);
        tab_piece[num_ligne][num_colonne].panneau = tab_pane[0];
        liste_tab_piece.get(0)[num_ligne][num_colonne] = tab_piece[num_ligne][num_colonne];
        tab_pane[0].getChildren().add(tab_piece[num_ligne][num_colonne].path);
    }













    // buggé
    private void delete_piece_from_pane(Piece piece) {
        int indice = -1;
        // on recupere l'indice du panneau de la piece
        for (int i = 1; i < tab_pane.length; i++) {//on ne touche aux pieces de basesœ
            if (tab_pane[i] == piece.panneau) {
                indice = i;
            }
        }
        for (int i = 0; i < liste_tab_piece.get(indice).length; i++) {
            for (int j = 0; j < liste_tab_piece.get(indice)[0].length; j++) {
                if (liste_tab_piece.get(indice)[i][j] == piece) {
                    set_piece_ref(indice,i,j,liste_tab_piece.get(indice)[i][j].path.getLayoutX(),liste_tab_piece.get(indice)[i][j].path.getLayoutY());
                   // consumer.accept("voy a remove piece1");
                    tab_pane[indice].getChildren().remove(liste_tab_piece.get(indice)[i][j].path);
                    //consumer.accept("voy a set null piece1");
                    liste_tab_piece.get(indice)[i][j].path = null;
                    liste_tab_piece.get(indice)[i][j] = null;
                    //consumer.accept("c bon frere");
                }
            }
        }
    }

    private void set_piece_ref(int indice,int i, int j,double x, double y) {
        double newX = x + (2 * tab_stage[indice].getWidth()) ;//+ (2 * tab_stage[indice].getWidth()) - box_contour.getLayoutX();
        double newY = y ;//+plateau.getHauteur()/2;
        /*if (liste_tab_piece.get(0)[i][j] == null) {
            consumer.accept("la piece de la liste est nul");
        }
        if (liste_tab_piece.get(0)[i][j].path == null) {
            consumer.accept("le path de la piece de la liste est nul");
        }
         */
        //liste_tab_piece.get(0)[i][j].path;
        liste_tab_piece.get(0)[i][j].path.setLayoutX(newX);
        liste_tab_piece.get(0)[i][j].path.setLayoutY(newY);
        // TODO: refaire les handler avec la forme : EventHandler boatHandler = new EventHandler<javafx.scene.input.MouseEvent>(){
        //TODO: pour ensuite pouvoir les removes avec : canvas.removeEventHandler(MouseEvent.MOUSE_PRESSED, boatHandler);
        //newEvent(liste_tab_piece.get(0)[i][j]);
        consumer.accept("new path piece layout x:"+liste_tab_piece.get(0)[i][j].path.getLayoutX()+" y : "+liste_tab_piece.get(0)[i][j].path.getLayoutY());
        tab_pane[0].getChildren().add(liste_tab_piece.get(0)[i][j].path);
    }
/*
    private void newEvent(Piece p) {
        p.path.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                p.panneau.getChildren().remove(p.path);
                p.panneau.getChildren().add(p.path);
                oldX = mouseEvent.getX();
                oldY = mouseEvent.getY();
            }
        });
        p.path.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (p.isMovable) { // drag seulement si la piece est deplacable
                    double newX = mouseEvent.getX();
                    double newY = mouseEvent.getY();
                    p.path.setLayoutX(p.path.getLayoutX() + newX - oldX);
                    p.path.setLayoutY(p.path.getLayoutY() + newY - oldY);
                }
            }
        });
        p.path.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (p.path.getLayoutX() <= MARGE_ASSEMBLAGE && p.path.getLayoutX() >= -MARGE_ASSEMBLAGE) {
                    if (p.path.getLayoutY() <= MARGE_ASSEMBLAGE && p.path.getLayoutY() >= -MARGE_ASSEMBLAGE) {
                        // si placement approximativement convenable alors placement de la piece dans son espace d'assemblage
                        p.path.setLayoutX(0.0);
                        p.path.setLayoutY(0.0);
                        p.setMovable(false);
                        gestion_fin_jeu();
                    }
                }
            }
        });
    }*/
    public void gestion_fin_jeu() {
        if (isFinished()) {
            Main.consumer.accept("GAME IS FINISHED");
            affichage_fin();
            trier.setDisable(true);
        }
    }

    private static boolean isFinished() {
        boolean res = true;
        for (Piece[] pieces : plateau.getTab()) {
            for (Piece piece : pieces) {
                if (piece.isMovable()) {
                    res = false; // si on peut encore bouger une piece c'est qu'on a pas finis de jouer
                }
            }
        }
        return res;
    }
    private void hide_titre() {
        this.label_bienvenue.setVisible(false);
    }
    //cree notre plateau en fonction des données entrées par l'user
    private void create_Plateau() {
        plateau = null;
        try {
            clear_pane_plateau();
        } catch (NullPointerException e) {
            consumer.accept("panneau non netoyer");
        }
        plateau = new Plateau(
                0,
                0,
                nombre_ligne,
                nombre_colonne,
                calcul_longueur_piece(),
                calcul_hauteur_piece(),
                image,
                niveau,
                pane_assemblage
        );
    }

    //vide le panneau d'assaemblage de son plateau et de ses autres enfants
    private void clear_pane_plateau() {
        //consumer.accept("dans clear plateau");
        pane_assemblage.getChildren().clear();
    }
    // renvoie la longueur d'une piece en fonction du nombre de
    // colonne et de la longueur du plateau
    private int calcul_longueur_piece() {
        int res =0;
        res = (int)box_contour.getMaxWidth() / nombre_colonne;
        //consumer.accept("calcul longueur piece "+box_contour.getMaxWidth() + " x "+ nombre_colonne + " = "+res);
        return res;
    }
    // renvoie la hauteur d'une piece en fonction du nombre de
    // lignes et de la hauteur du plateau
    private int  calcul_hauteur_piece() {
        int res=0;
        res = (int) box_contour.getMaxHeight() / nombre_ligne;
        //consumer.accept("calcul hauteur piece "+box_contour.getMaxHeight() + " x "+ nombre_ligne + " = "+res);
        return res;
    }


    // met notre plateau dans l'affichage
    private void set_plateau_on_pane(Plateau plateau) {
        //positionnement_plateau_assemblage();
        for (int i = 0; i < plateau.getTab().length; i++) {
            for (int j = 0; j < plateau.getTab()[0].length; j++) {
                pane_assemblage.getChildren().add(plateau.getTab()[i][j].path);
            }
        }
    }

    @FXML
    private void fill_new() throws IOException {
        System.out.println("Dans fill_new");
        final URL url = getClass().getResource("parametrage.fxml");
        // Création du loader
        System.out.println("1");
        final FXMLLoader fxmlLoader = new FXMLLoader(url);
        System.out.println("2");
        // Chargement du FXML
        final AnchorPane root2 = fxmlLoader.load();
        System.out.println("3");
        // Création de la scène
        if (root2 != null) {
            System.out.println("root2 n'est pas null");
            Scene scene2 = new Scene(root2, stage.getWidth(), stage.getHeight());
            this.stage.setScene(scene2);
        }
    }

    @FXML
    private void fill_open() {
        // event1(open);
    }
    @FXML
    private void fill_save_as() {
        // TODO
    }

    @FXML
    private void fill_enonce() throws IOException {
        // TODO : gerer les erreur affiche dans le terminal lors de l'execution de cette fonction
        Desktop d = Desktop.getDesktop();
        d.open(new File("./enonce/puzzle.pdf"));
    }
    @FXML
    private void fill_wiki_puzzle() throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://fr.wikipedia.org/wiki/Puzzle"));
    }
    @FXML
    private void fill_quit() {
        //quit.setOnAction(e -> Platform.exit());
        Platform.exit();
        System.exit(0);
        // Fermeture propre de l'application
        // stage.setOnCloseRequest(e -> Platform.exit());
    }



    // parametrage

    @FXML
    private void on_radio() {
        if (radioButton.isSelected()) {
            niveau = 1;
        } else if (radioButton2.isSelected()) {
            niveau = 2;
        } else if (radioButton3.isSelected()) {
            niveau = 3;
        } else if(radioButton4.isSelected()) {
            niveau = 4;
        }
        txt_niveau.setText(String.valueOf(niveau));
    }

    @FXML
    private void reset_onAction() {
        image = null;
        this.path_image.setText("");
        set_element_hide();
        this.spinner_ligne.setValueFactory(null);
        this.spinner_colonne.setValueFactory(null);
    }

    @FXML
    private void valider_onAction() throws IOException {
        System.out.println("dans valider on action");
        if (check_parametrage()) {
            est_lancable = true;
            // mise a jour des attribut du main
            MAJ_attribut();
            /**retour a la fenetre principale et debut du jeu**/
            final URL url = getClass().getResource("Fenetre.fxml");
            // Création du loader
            System.out.println("1");
            final FXMLLoader fxmlLoader = new FXMLLoader(url);
            System.out.println("2");
            // Chargement du FXML
            final BorderPane root = fxmlLoader.load();
            System.out.println("3");
            // Création de la scène
            if (root != null) {
                System.out.println("root n'est pas null");
                this.stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            }
        }
    }
    //met a jour les attribut de la classe main pour faire notre puzzle
    private void MAJ_attribut() {
        nombre_colonne = Integer.valueOf(nb_colonne.getText().toString());
        nombre_ligne = Integer.valueOf(nb_ligne.getText().toString());
    }
    //verifie si tout les champs du parametrage dd'un nouveau puzzle sont bien remplie
    private boolean check_parametrage() {
        boolean res = false;
        if (check_image()) {
            if (niveau != 0) {
                res = true;
            }
        }
        return  res;
    }

    private boolean check_image() {
        if (image != null) {
            return true;
        }
        return false;
    }

    @FXML
    private void chooseImage() {
        event1();
        if (image != null) {
            /**TODO passer les attribut en double !!!!!  */
            init_spinner();
        }
    }

    private void set_element_hide() {
        this.nb_ligne.setVisible(false);
        this.nb_colonne.setVisible(false);
        this.spinner_colonne.setVisible(false);
        this.spinner_ligne.setVisible(false);
    }
    private void set_element_Visible() {
        this.nb_ligne.setVisible(true);
        this.nb_colonne.setVisible(true);
        this.spinner_colonne.setVisible(true);
        this.spinner_ligne.setVisible(true);
    }
    // init les spinner en fonction de la resolution de l'image choisi
    private void init_spinner() {
        set_element_Visible();
        System.out.println("w: " + image.getWidth() + " min_longueur: " + min_longueur + " == max_col : " + calcul_max_colonne());
        System.out.println("h: " + image.getHeight() + " min_hauteur: " + min_hauteur + " == max_li : " + calcul_max_ligne());
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(2, calcul_max_colonne(), 2);
        spinner_colonne.setValueFactory(valueFactory);

        SpinnerValueFactory<Integer> valueFactory2 =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(2, calcul_max_ligne(), 2);
        spinner_ligne.setValueFactory(valueFactory2);
        nb_ligne.textProperty().bind(StringProperty.stringExpression(spinner_ligne.valueProperty()));
        nb_colonne.textProperty().bind(StringProperty.stringExpression(spinner_colonne.valueProperty()));
    }

    // renvoie le nombre de colonnes que l'utilisateur peut choisir au maximum
    // pour son puzzle
    private int calcul_max_colonne() {
        int res = 0;
        res = Math.min(((int) image.getWidth() / (int) min_longueur), (width_plateau / (int) min_longueur));
        return res;
    }

    // renvoie le nombre de lignes que l'utilisateur peut choisir au maximum
    // pour son puzzle
    private int calcul_max_ligne() {
        int res =0;
        res = Math.min(((int) image.getHeight() / (int) min_hauteur), (height_plateau / (int)  min_hauteur));
        return res;
    }

    private void event1() {
        FileChooser fil_chooser = new FileChooser();
        // add filters file's extension
        fil_chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("jpg files", "*.jpg"),
                new FileChooser.ExtensionFilter("png files", "*.png"),
                new FileChooser.ExtensionFilter("jpeg files", "*.jpeg"),
                new FileChooser.ExtensionFilter("All files", "*.*"));
        Label label = new Label("no files selected");
        File file = fil_chooser.showOpenDialog(stage);
        if (file != null) {
            label.setText(file.getAbsolutePath());
            image = new Image("file:" + file);
        }
        //creation image via le file
        path_image.setText(label.getText());// --> path_image = label
    }


    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

}

