package sample;


import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.function.Consumer;

import static sample.Main.*;

public class Controller_Fenetre  {

    // attribut "GLOBAL"

    private static final int DECALAGE_PLATEAU_ASSEMBLAGE = 25;
    private Stage stage;
    private Scene scene;
    private static final int min_longueur = Piece.getMinLongueur();
    private static final int min_hauteur = Piece.getMinHauteur();
    private boolean accueil = true;
    private Consumer<String> consumer = e -> System.out.println(e);
    private double oldX;
    private double oldY;

    // page principale
    @FXML private MenuItem open;
    @FXML private MenuItem lancement;
    @FXML private MenuItem quit;
    @FXML private MenuItem save_as;
    @FXML private MenuItem new_puzzle;
    @FXML private HBox box_contour;
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
    }
    @FXML
    private void fill_lancement() {
        lancement();
    }
    //lance un puzzle a partir du plateau etc ...
    private void lancement() {
        hide_titre();
        create_Plateau();
        set_plateau_on_pane();
        gestion_evenement_plateau();
    }

    //ajoute des evenement aux pieces du plateau
    private void gestion_evenement_plateau() {
        Piece[][] tab = plateau.getTab();
        for (int i = 0; i < tab.length ; i++) {
            for (int j = 0; j <tab[0].length ; j++) {
                ajout_event_piece(i,j);
            }
        }
    }
    // ajoute des event a une piece
    private void ajout_event_piece(int i, int j) {
        //consumer.accept("dans ajout event piece i : "+ i+" j :"+j);
        plateau.getTab()[i][j].forme.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                consumer.accept("dans onCilicked de piece ");
                oldX = mouseEvent.getSceneX();
                oldY = mouseEvent.getSceneY();
                consumer.accept("oldX : "+oldX);
                consumer.accept("oldY : "+oldY);
            }
        });
        plateau.getTab()[i][j].forme.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                consumer.accept("dans on dragged de piece");
                plateau.getTab()[i][j].forme.setTranslateX(mouseEvent.getSceneX() - oldX);
                plateau.getTab()[i][j].forme.setTranslateY(mouseEvent.getSceneY() - oldY);
            }
        });

    }


    private void hide_titre() {
        this.label_bienvenue.setVisible(false);
    }
    //cree notre plateau en fonction des données entrées par l'user
    private void create_Plateau() {
        plateau = new Plateau(
                0,
                0,
                nombre_ligne,
                nombre_colonne,
                calcul_longueur_piece(),
                calcul_hauteur_piece(),
                image,
                niveau
        );
    }
    // renvoie la longueur d'une piece en fonction du nombre de
    // colonne et de la longueur du plateau
    private int calcul_longueur_piece() {
        int res =0;
        res = width_plateau / nombre_colonne;
        return res;
    }
    // renvoie la hauteur d'une piece en fonction du nombre de
    // lignes et de la hauteur du plateau
    private int calcul_hauteur_piece() {
        int res=0;
        res = height_plateau / nombre_ligne;
        return res;
    }


    // met notre plateau dans l'affichage
    private void set_plateau_on_pane() {
        positionnement_plateau_assemblage();
        for (int j = 0; j < plateau.getNb_colonne(); j++) {
            for (int i = 0; i < plateau.getNb_ligne() ; i++) {
                pane_assemblage.getChildren().add(plateau.getTab()[i][j].forme);
            }
        }
    }

    private void positionnement_plateau_assemblage() {
        pane_assemblage.getTransforms().add(new Translate(25,25));
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
        quit.setOnAction(e -> Platform.exit());
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
            // mise a jour des attribut du main
            MAJ_attribut();
            //TODO : retour a la fenetre principale et debut du jeu
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
    //verifie si tout les champs du paarametrage dd'un nouveau puzzle sont bien remplie
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
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, calcul_max_colonne(), 1);
        spinner_colonne.setValueFactory(valueFactory);

        SpinnerValueFactory<Integer> valueFactory2 =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, calcul_max_ligne(), 1);
        spinner_ligne.setValueFactory(valueFactory2);
        nb_ligne.textProperty().bind(StringProperty.stringExpression(spinner_ligne.valueProperty()));
        nb_colonne.textProperty().bind(StringProperty.stringExpression(spinner_colonne.valueProperty()));
    }

    // renvoie le nombre de colonnes que l'utilisateur peut choisir au maximum
    // pour son puzzle
    private int calcul_max_colonne() {
        int res = 0;
        res = Math.min(((int) image.getWidth() / min_longueur), (width_plateau / min_longueur));
        return res;
    }

    // renvoie le nombre de lignes que l'utilisateur peut choisir au maximum
    // pour son puzzle
    private int calcul_max_ligne() {
        int res =0;
        res = Math.min(((int) image.getHeight() / min_hauteur), (height_plateau / min_hauteur));
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
