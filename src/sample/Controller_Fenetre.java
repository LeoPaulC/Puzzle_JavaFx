package sample;


import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
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
import javafx.scene.transform.Rotate;
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
import java.util.ArrayList;
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
    private ArrayList<Group> list_group;
    private ArrayList<Piece> list_piece;
    private ArrayList<Piece> list_piece_group;

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
    @FXML private RadioButton radioButton5;

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
        list_piece_group = null ; list_piece = null;list_group = null;
        list_piece = new ArrayList<>();
        list_group = new ArrayList<>();
        list_piece_group = new ArrayList<>();
        hide_titre();
        zones.setDisable(true);
        if (liste_tab_piece != null && liste_tab_piece.size() > 0 && liste_tab_piece.get(0) != null) {
            //liste_tab_piece.remove(0);
            liste_tab_piece.clear();
        }
        consumer.accept("create plateau");
        create_Plateau();
        Rectangle rectangle = create_Rectangle();
        pane_assemblage.getChildren().add(rectangle);
        gestion_affichage_box();
        Plateau p = new Plateau(plateau);
        setPlateau_assemblage(p); // MAJ le plateaud'assemblage == plateau blanc en dessous du puzzle
        set_plateau_on_pane(p);
        set_plateau_on_pane(plateau);
        gestion_evenement_plateau();
        //affichage_test(4,plateau.getTab());
        split_piece();
        //affichage_test(4,plateau.getTab());
        trier.setDisable(false);
        tab_piece = plateau.tab.clone();
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
        //set_pieces_Movable();//
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

        //vider le panneau principale de ses pieces
        unfill_piece_pane();
        affichage_test(4,plateau.getTab());

        //visibilité de la partie du menu corresp. au trie
        zones.setDisable(false);
        z_bordure.setVisible(true);
        z_rouge.setVisible(true);
        z_verte.setVisible(true);
        z_bleue.setVisible(true);
        trier.setDisable(true);
        // remplissage des diff zones de trie
        fill_stage_trie();

    }
    private void set_pieces_Movable() {
        for (Piece[] pieces : plateau.tab) {
            for (Piece piece : pieces) {
                piece.setMovable(true);
                piece.path.setStrokeWidth(1);
                piece.path.setStroke(Color.BLACK);
            }
        }
    }

    //enleve les pieces presentes dans les zones de trie du panneau d'assemblage
    private void unfill_piece_pane() {
        for (int k = 1; k < NB_ZONE; k++) {
            for (int i = 0; i < liste_tab_piece.get(1).length; i++) {
                for (int j = 0; j < liste_tab_piece.get(1)[0].length; j++) {
                    if (liste_tab_piece.get(k)[i][j] != null) {
                        if (liste_tab_piece.get(0)[i][j] != null) {
                            if (liste_tab_piece.get(0)[i][j].isMovable()) {
                                pane_assemblage.getChildren().remove(liste_tab_piece.get(0)[i][j].path);
                            }
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

    private void reset_stage_trie() {
        if (tab_pane != null) {
            for (int i = 0; i < NB_ZONE; i++) {
                if (tab_pane[i] != null) {
                    tab_pane[i].getChildren().clear();
                }
                tab_pane[i] = null;
            }
        }
        if (tab_stage != null) {
            for (int i = 0; i < NB_ZONE; i++) {
                tab_stage[i] = null;
            }
        }
        if (tab_scene != null) {
            for (int i = 0; i < NB_ZONE; i++) {
                tab_scene[i] = null;
            }
        }

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
        ma_stage.setHeight(stage.getHeight() - menuBar.getHeight()-TAILLE_HAUTEUR_TITRE_STAGE);
        ma_stage.setX(stage.getX()+ma_stage.getWidth()*2);// fenetre dans le diernier tier de l'ecran
        ma_stage.setY(stage.getY()+menuBar.getHeight()+TAILLE_HAUTEUR_TITRE_STAGE);
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
        ma_stage.setHeight(stage.getHeight() - menuBar.getHeight()-TAILLE_HAUTEUR_TITRE_STAGE);
        ma_stage.setX(stage.getX()+ma_stage.getWidth()*2);// fenetre dans le diernier tier de l'ecran
        ma_stage.setY(stage.getY()+menuBar.getHeight()+TAILLE_HAUTEUR_TITRE_STAGE);
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
        ma_stage.setHeight(stage.getHeight() - menuBar.getHeight()-TAILLE_HAUTEUR_TITRE_STAGE);
        ma_stage.setX(stage.getX()+ma_stage.getWidth()*2);// fenetre dans le diernier tier de l'ecran
        ma_stage.setY(stage.getY()+menuBar.getHeight()+TAILLE_HAUTEUR_TITRE_STAGE);
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
                if (liste_tab_piece.get(0)[i][j].isMovable() && !isPieceGroup(liste_tab_piece.get(0)[i][j])) { // bouggeable et pas une piece d'un groupe
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
    }

    private boolean isPieceGroup(Piece piece) {
        for (Piece piece1 : list_piece_group) {
            if (piece.i == piece1.i && piece.j == piece1.j) {
                return true;
            }
        }
        return false;
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
            for (int j = minY; j < maxY ; j++) {
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
                    if (liste_tab_piece.get(0)[i][j].tab_bordure[k].getClass() == Bordure_Plate.class) {
                        est_piece_bord = true;
                    }
                }
                if (est_piece_bord && liste_tab_piece.get(0)[i][j].isMovable() && !isPieceGroup(liste_tab_piece.get(0)[i][j])) { // alors la piece a au moins un cote qui est un bord
                    Piece p = new Piece(liste_tab_piece.get(0)[i][j], tab_pane[1]);
                    if (p.isMovable()) {
                        p.path.setFill(liste_tab_piece.get(0)[i][j].path.getFill());
                        p.path.setStroke(liste_tab_piece.get(0)[i][j].path.getStroke());
                        p.path.setLayoutX(rand_coord_X(tab_stage[1],p));
                        p.path.setLayoutY(rand_coord_Y(tab_stage[1],p));
                        tab_piece_bordure[i][j] = p;
                    }
                }

            }
        }
    }

    // renvoie une nouvelle valeur x pour une piece dans une scene
    private double rand_coord_X(Stage stage, Piece p) {
        double x = randNumber(0.0- p.getMinX(), stage.getWidth()/2 - p.getMinX());
        return x;
    }

    // renvoie une nouvelle valeur y pour une piece dans une scene
    private double rand_coord_Y(Stage stage, Piece p) {
        double y = randNumber(stage.getHeight()*0.05 - p.getMinY() ,stage.getHeight()*0.7 - p.getMinY());
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
                piece.path.setLayoutX(rand_choice(x1, x2));
                piece.path.setLayoutY(rand_choice(y1, y2));
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
                ajout_coord_in_plateau(plateau.getTab()[i][j], i, j);
                ajout_event_piece(plateau.getTab()[i][j]);
            }
        }
    }

    private void ajout_coord_in_plateau(Piece p, int i, int j) {
        p.i = i;
        p.j = j;
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
                if (!p.isMovable()) {
                    return ;
                }
                //gestion event dans le panneau courant de la piece
                double x = p.path.getLayoutX();
                double y = p.path.getLayoutY();
                consumer.accept("layout piece clické :"+x+";"+y);
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
        p.path.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!p.isMovable()) {
                    return ;
                }
                if (mouseEvent.getClickCount() == 2) {
                    System.out.println("double clicked");
                    Rotate rotate = new Rotate();
                    rotate.setAngle(90);
                    rotate.setPivotX(mouseEvent.getX());
                    rotate.setPivotY(mouseEvent.getY());
                    //Translate translate = new Translate(200, 0);
                    p.path.getTransforms().add(rotate);
                    //p.path.getTransforms().add(translate);
                }
            }
        });
        p.path.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                consumer.accept("dans event released");
                consumer.accept("Layout de la piece posé : "+p.path.getLayoutX()+";"+p.path.getLayoutY());
                if (p.panneau == tab_pane[0] || p.panneau == pane_assemblage) { // null si normale
                    consumer.accept("piece du panneau principal");
                    // TODO : gestion du collage de piece

                    if (p.path.getLayoutX() <= MARGE_ASSEMBLAGE && p.path.getLayoutX() >= -MARGE_ASSEMBLAGE) {
                        if (p.path.getLayoutY() <= MARGE_ASSEMBLAGE && p.path.getLayoutY() >= -MARGE_ASSEMBLAGE) {
                            // si placement approximativement convenable alors placement de la piece dans son espace d'assemblage
                            p.path.setLayoutX(0.0);
                            p.path.setLayoutY(0.0);
                            p.path.setStrokeWidth(0.0);
                            p.setMovable(false);
                            gestion_fin_jeu();
                        }
                    }
                    if (p.isMovable) { // si la piece n'a pas ete place dans son emplacement
                        //a alorss on regarde si on doit la groupe avec d'autre piece

                        consumer.accept("i "+ p.i +"  j "+p.j);
                        consumer.accept(" verfication du collage externe");
                        Piece p_match = checkMatchPiece(p);
                        if (p_match != null) {
                            consumer.accept("une piece a ete matché ");
                            createGroup(p, p_match);
                        }
                    }
                }else if(p.panneau == tab_pane[1] || p.panneau == tab_pane[2] || p.panneau == tab_pane[3] || p.panneau == tab_pane[4]){ // gestion du drag and drop
                    consumer.accept("piece d'une zonne de trie");
                    if (mouseEvent.getSceneX() < 0.0) { // si la piece est relaché a gauche de la zone de trie
                        //alors c'est qu'on a relache la piece dans le panneau principal 'PAS FORCEMENT'
                        // alors on pop la piece de son pane
                        for (int i = 0; i < plateau.tab.length; i++) {
                            for (int j = 0; j < plateau.tab[0].length ; j++) {
                                if (plateau.tab[i][j].path.getFill() == p.path.getFill()) {
                                    int indice = 0;
                                    for (int k=0; k< tab_pane.length; k++ ) {
                                        if (tab_pane[k] == p.panneau) {
                                            indice = k;
                                        }
                                    }
                                    plateau.tab[i][j].path.setLayoutX(p.path.getLayoutX()  + tab_stage[indice].getWidth()*2 );
                                    plateau.tab[i][j].path.setLayoutY(p.path.getLayoutY() - box_contour.getLayoutY() +menuBar.getHeight()*2+5+TAILLE_HAUTEUR_TITRE_STAGE );
                                    pane_assemblage.getChildren().add(plateau.tab[i][j].path);
                                    tab_pane[0].getChildren().add(plateau.tab[i][j].path);
                                    pane_assemblage.getChildren().remove(p.piece_ombre.path);
                                    tab_pane[0].getChildren().remove(p.piece_ombre.path);
                                    p.panneau.getChildren().remove(p.path);
                                    p.piece_ombre = null;
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

    private void creatGroup(Group g1, Group g2) {
        consumer.accept("creation de groupe par 2 groupes ");
        Group group = new Group();
        g1.setLayoutX(g2.getLayoutY());
        g1.setLayoutY(g2.getLayoutY());
        //remmplissage du group
        consumer.accept("g2hbhbghghgh" + g2.getChildren().size());
        // TODO: faire le parcours de remplissage a l'envers morray
        for (int i = g2.getChildren().size() - 1; i >= 0; i--) {
            group.getChildren().add(g2.getChildren().get(i));
        }
        for (int i = g1.getChildren().size() - 1; i >= 0; i--) {
            group.getChildren().add(g1.getChildren().get(i));
        }
        // positionnement du group
        // g2 et non g1 pcq g1 viens d'etre deplace
        group.setLayoutX(g2.getLayoutX());
        group.setLayoutY(g2.getLayoutY());
        //handler
        addListener_Group(group);
        // suppression des deux groupes pred
        pane_assemblage.getChildren().remove(g1);
        pane_assemblage.getChildren().remove(g2);
        list_group.remove(g1);
        list_group.remove(g2);
        g1.getChildren().clear();
        g2.getChildren().clear();
        g1 = null;
        g2 = null;
        // ajout du nouveau groupe
        pane_assemblage.getChildren().add(group);
        list_group.add(group);
    }
    private void createGroup(Piece r1, Piece r2) {
        // TODO : faire en sorte que deux group puissent s'assemblé en un nouveau groupe
        consumer.accept("creation d'un nouveau group");
        Group group = new Group();
        Piece rect_copy1 = new Piece(r1);// sans les handler du coup
        rect_copy1.path.setFill(r1.path.getFill());
        rect_copy1.i = r1.i;
        rect_copy1.j = r1.j;
        //rect_copy1.path = r1.path;
        Piece rect_copy2 = new Piece(r2);
        //rect_copy2.path = r2.path;
        rect_copy2.path.setFill(r2.path.getFill());
        rect_copy2.i = r2.i;
        rect_copy2.j = r2.j;
        group.getChildren().add(rect_copy1.path);
        group.getChildren().add(rect_copy2.path);
        addListener_Group(group);
        if (r1.path.getLayoutX() == r2.getLayoutX()) {
            if (r1.path.getLayoutY() < r2.path.getLayoutY()) {
                group.setLayoutX(r1.path.getLayoutX());
                group.setLayoutY(r1.path.getLayoutY());
            }else {
                group.setLayoutX(r2.path.getLayoutX());
                group.setLayoutY(r2.path.getLayoutY());
            }
        }else{
            if (r1.path.getLayoutX() < r2.path.getLayoutX()) {
                group.setLayoutX(r1.path.getLayoutX());
                group.setLayoutY(r1.path.getLayoutY());
            }else {
                group.setLayoutX(r2.path.getLayoutX());
                group.setLayoutY(r2.path.getLayoutY());
            }
        }
        consumer.accept("ajout des handler au group");
        //list_piece_group.add(plateau.tab[r1.i][r1.j]);list_piece_group.add(plateau.tab[r2.i][r2.j]);
        list_piece_group.add(rect_copy1);list_piece_group.add(rect_copy2);
        list_piece.remove(r1);list_piece.remove(r2);
        pane_assemblage.getChildren().remove(r1.path);
        pane_assemblage.getChildren().remove(r2.path);
        /*rect_copy1.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                consumer.accept("piece 1 ");
            }
        });
        rect_copy2.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                consumer.accept("piece 2");
            }
        });
         */
        list_group.add(group);
        pane_assemblage.getChildren().add(group);
    }
    private void addListener_Group(Group group) {
        group.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (group.getLayoutX() == 0.0 && group.getLayoutY() == 0.0) {
                    return ;
                }
                pane_assemblage.getChildren().remove(group);
                pane_assemblage.getChildren().add(group);
                oldX = mouseEvent.getX();
                oldY = mouseEvent.getY();
            }
        });
        group.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (group.getLayoutX() == 0.0 && group.getLayoutY() == 0.0) {
                    return ;
                }
                group.setLayoutX(group.getLayoutX() + mouseEvent.getX() - oldX);
                group.setLayoutY(group.getLayoutY() + mouseEvent.getY() - oldY);
            }
        });
        group.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                consumer.accept("released de group");
                consumer.accept("layout group x: "+group.getLayoutX());
                consumer.accept("layout group y: "+group.getLayoutY());
                checkPlacemetnGroup(group);
                checkCollageGroup(group);
                gestion_fin_jeu();
            }
        });
    }
    // gere le placement d'un group dans le plateau d'affichage
    private void checkPlacemetnGroup(Group group) {
        consumer.accept(" dans check placement group");
        if (-10 < group.getLayoutX() && group.getLayoutX() < 10) {
            if (-10 < group.getLayoutY() && group.getLayoutY() < 10) {
                // alors on place le group
                group.setLayoutX(0.0);group.setLayoutY(0.0);
                // on replace les piece du groupe dans le panneau d'assemblage
                gestionStrokeGroup(group);
                dismovablePieceFromGroupe(group);
            }
        }
    }
    //enleve toutes les bordure des pieces du group
    private void gestionStrokeGroup(Group group) {
        for (Node child : group.getChildren()) {
            for (Piece piece : list_piece_group) {
                if (piece.path == child) {
                    piece.path.setStroke(Color.TRANSPARENT);
                    //child = piece.path;
                    break;
                }
            }
        }
    }
    // met les piece du group dans le panneau d'assemblage
    private void putPieceOnPanel(Group group) {

    }
    private void checkCollageGroup(Group group) {
        consumer.accept(" checkCollagegroupe");
        for (Group group1 : list_group) {
            if (group != group1) {
                if (isPlugable(group, group1)) {
                    consumer.accept(" group plugable");
                    // TODO : creation d'un nouveau groupe(de piece) a partir des pieces des deux groupes et supp des deux groupes pred
                    creatGroup(group, group1);
                    break;
                }
            }
        }
    }
    // dis si les deux groupe peuvent etre collé si ils sont mis a coté et qu'ils ont des pieces adjacentes
    private boolean isPlugable(Group g1 , Group g2) {
        consumer.accept("dans is Plugable");
        if (isSameTranslationGroup(g1, g2)) {
            if (isAdjacentGroup(g1, g2)) {
                return true;
            }
        }
        return false;
    }

    //regarde si g2 a la meme translation que g1 (avec marge d'erreue)
    private boolean isSameTranslationGroup(Group g1, Group g2) {
        consumer.accept("dans is same translation group");
        if (g1.getLayoutX() - 10 < g2.getLayoutX() && g2.getLayoutX() < g1.getLayoutX() + 10) {
            if (g1.getLayoutY() - 10 < g2.getLayoutY() && g2.getLayoutY() < g1.getLayoutY() + 10) {
                return true;
            }
        }
        return false;
    }

    //regarde si une piece de g2 est voisine d'une piece de g1
    private boolean isAdjacentGroup(Group g1, Group g2) {
        consumer.accept("dans is adjacent group");
        for (Piece piece : list_piece_group) {
            consumer.accept("unepiece");
            for (int k = 0; k < g2.getChildren().size(); k++) {
                consumer.accept("unchild");
                //if (g2.getChildren().get(k).getClass() == Piece.class) {
                  //  consumer.accept("child=piece");
                    if (piece.path == g2.getChildren().get(k)) {
                        consumer.accept("on a recup une piece du groupe");
                        if (isPieceVoisine(g1, piece)) {
                            consumer.accept(" is piece voisine est true");
                            return true;
                        }
                    }
                    //if (isPieceVoisine(g1, (Piece) g2.getChildren().get(k))) {
                    //   return true;
                    //}
               // }
            }
        }
        return false;
    }

    // regarde si une piece est voisine de l'une des pieces de g1
    private boolean isPieceVoisine(Group g, Piece p) {
        consumer.accept(" dans is piece voisine");
        for (Piece piece : list_piece_group) {
            for (int k = 0; k < g.getChildren().size(); k++) {
                if (g.getChildren().get(k) == piece.path) {
                    int i = piece.i;
                    int j = piece.j;
                    consumer.accept(" i,j piece_g" + i + "," + j);
                    consumer.accept(" i,j piece  p" + p.i + "," + p.j);
                    if ((p.i == i - 1 && p.j == j) || (p.i == i + 1 && p.j == j) || (p.i == i && p.j == j - 1) || (p.i == i && p.j == j + 1)) {
                        //piece du dessus/dessous/gauche/droite
                        consumer.accept(" on a trouvé deux pieces voisines");
                        return true;
                    }
                }
            }
        }
        consumer.accept("on renvoie false");
        return false;
    }


    //regarde si p est place pres d'une de ses 4 piece adjacentes
    private Piece checkMatchPiece(Piece p) {
        int i = p.i;
        int j = p.j;
        // on regarde i-1:j , i:j-1 , i:j+1 , i+1:j

        //piece de gauche
        if (j - 1 >= 0 && plateau.tab[i][j - 1].isMovable() && !list_piece_group.contains(plateau.tab[i][j - 1]) && checkPieceAdjacente(p, i, j - 1)) {
            consumer.accept("piece de gauche");
            Maj_i_j(plateau.tab[i][j - 1], i, j - 1);
            // aimantation
            p.path.setLayoutX(plateau.tab[i][j - 1].path.getLayoutX());
            p.path.setLayoutY(plateau.tab[i][j - 1].path.getLayoutY());
            return plateau.tab[i][j - 1];
        }
        // piece du dessus
        if (i - 1 >= 0 && plateau.tab[i - 1][j].isMovable() && !list_piece_group.contains(plateau.tab[i - 1][j]) && checkPieceAdjacente(p, i - 1, j)) {
            consumer.accept("piece du haut");
            Maj_i_j(plateau.tab[i - 1][j], i - 1, j);
            // aimantation
            p.path.setLayoutX(plateau.tab[i - 1][j].path.getLayoutX());
            p.path.setLayoutY(plateau.tab[i - 1][j].path.getLayoutY());
            return plateau.tab[i - 1][j];
        }
        // piece de droite
        if (j + 1 < plateau.tab[0].length && plateau.tab[i][j + 1].isMovable() && !list_piece_group.contains(plateau.tab[i][j + 1]) && checkPieceAdjacente(p, i, j + 1)) {
            consumer.accept("piece de droite");
            Maj_i_j(plateau.tab[i][j + 1], i, j + 1);
            // aimantation
            p.path.setLayoutX(plateau.tab[i][j + 1].path.getLayoutX());
            p.path.setLayoutY(plateau.tab[i][j + 1].path.getLayoutY());
            return plateau.tab[i][j + 1];
        }
        //piece du bas
        if (i + 1 < plateau.tab.length && plateau.tab[i + 1][j].isMovable() && !list_piece_group.contains(plateau.tab[i + 1][j]) && checkPieceAdjacente(p, i + 1, j)) {
            consumer.accept("piece du bas");
            Maj_i_j(plateau.tab[i + 1][j], i + 1, j);
            // aimantation
            p.path.setLayoutX(plateau.tab[i + 1][j].path.getLayoutX());
            p.path.setLayoutY(plateau.tab[i + 1][j].path.getLayoutY());
            return plateau.tab[i + 1][j];
        }
        return null;
    }

    private void Maj_i_j(Piece p, int i, int j) {
        p.i = i;
        p.j = j;
    }
    private boolean checkPieceAdjacente(Piece p, int i, int j) {
        consumer.accept("i :"+i+" j : "+j);
        consumer.accept("length plateau :"+plateau.tab.length);
        consumer.accept("layout x de la piece"+p.path.getLayoutX());
        consumer.accept("layout y de la piece"+p.path.getLayoutY());
        consumer.accept("layout x de la piece a matcher"+plateau.tab[i][j].path.getLayoutX() );
        consumer.accept("layout y de la piece a matcher"+plateau.tab[i][j].path.getLayoutY() );
        if (p.path.getLayoutX() - 10 < plateau.tab[i][j].path.getLayoutX() && plateau.tab[i][j].path.getLayoutX() < p.path.getLayoutX() + 10 ) {
            if (p.path.getLayoutY() - 10 < plateau.tab[i][j].path.getLayoutY() && plateau.tab[i][j].path.getLayoutY() < p.path.getLayoutY() + 10) {
                // alors p a la meme translation que ma piece de gauche
                return true;
            }
        }
        return false;
    }

    private void gestion_collage(Piece p) {
        int i = p.i;
        int j = p.j;
        if (i - 1 >= 0 && plateau.tab[i - 1][j] != null) { // piece du dessus
            if (p.path.getLayoutX() - 10 < plateau.tab[i - 1][j].path.getLayoutX() && plateau.tab[i - 1][j].path.getLayoutX() < p.path.getLayoutX() + 10) {
                if (p.path.getLayoutY() - 10 < plateau.tab[i - 1][j].path.getLayoutY() && plateau.tab[i - 1][j].path.getLayoutY() < p.path.getLayoutY() + 10) {
                    // alors les deux pieces ont les memes translation donc elles sont cotes a cotes
                    if (p.rotation == plateau.tab[i - 1][j].rotation) {
                        // alors on a le droit de les coller
                        Group group = new Group();
                        group.getChildren().add(p);
                        group.getChildren().add(plateau.tab[i - 1][j]);
                        list_group.add(group);
                    }
                }
            }
        }
    }
    public void gestion_fin_jeu() {
        if (isFinished()) {
            Main.consumer.accept("GAME IS FINISHED");
            affichage_fin();
            trier.setDisable(true);
        }
    }

    private boolean isFinished() {
        consumer.accept("dans is finished");
        if (list_group != null) {
            consumer.accept("list group non null");
            for (Group group : list_group) {
                if (group.getLayoutY() != 0.0 || group.getLayoutX() != 0.0) {
                    consumer.accept("RES = FALSE");
                    return false; // si un groupe n'est pas placé dans sa bonne position dans le panneau d'assemblage
                }
            }
        }
        consumer.accept("tout les group sont place");
        for (Piece[] pieces : plateau.getTab()) {
            for (Piece piece : pieces) {
                if (piece.isMovable()) {
                    consumer.accept("RES = FALSE");
                    return false; // si on peut encore bouger une piece c'est qu'on a pas finis de jouer
                }
            }
        }
        consumer.accept("toutes les pieces sont placees");
        return true;
    }

    private void dismovablePieceFromGroupe(Group group) {
        for (Node child : group.getChildren()) {
            for (Piece piece : list_piece_group) {
                if (child == piece.path) {
                    int i = piece.i;
                    int j = piece.j;
                    plateau.tab[i][j].setMovable(false);
                }
            }
        }
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
        pane_assemblage.getChildren().clear();
    }
    // renvoie la longueur d'une piece en fonction du nombre de
    // colonne et de la longueur du plateau
    private int calcul_longueur_piece() {
        int res =0;
        res = (int)box_contour.getMaxWidth() / nombre_colonne;
        return res;
    }
    // renvoie la hauteur d'une piece en fonction du nombre de
    // lignes et de la hauteur du plateau
    private int  calcul_hauteur_piece() {
        int res=0;
        res = (int) box_contour.getMaxHeight() / nombre_ligne;
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
        // Fermeture propre de l'application
        Platform.exit();
        System.exit(0);
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
        else if(radioButton5.isSelected()) {
            niveau = 5;
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
