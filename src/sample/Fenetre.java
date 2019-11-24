package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.lang.Thread.sleep;

public class Fenetre {//fenetre principale du jeu
    private final static int rangeMin = 0; // pour RGB
    private final static int rangeMax = 1;// pour RGB
    private static int width_win = 600;
    private static int height_win = 600;
    private Stage ma_stage;
    private Scene ma_scene;
    private Pane root;
    private BorderPane mon_pane;

    //pour le plateau
    private Image mon_image;

    public Fenetre() throws InterruptedException {
        //create_stage(1,rand(), rand(), rand());
        mon_image = null;
        mon_pane = new BorderPane();
        gestion_fenetre();
        root = new Pane();
        root.getChildren().add(mon_pane);
        ma_scene = new Scene(root, width_win, height_win);
        ma_stage = new Stage();
        ma_stage.setTitle("Jeu du Puzzle");
        ma_stage.setScene(ma_scene);
        // Fermeture propre de l'application
        ma_stage.setOnCloseRequest(e -> Platform.exit());
        ma_stage.show();
        
    }

    //
    private void gestion_fenetre() {
        //ajout d'un menu
        mon_pane.setTop(gestion_Menu());
        //ajout d'un pane centrale
        mon_pane.setCenter(middle_Pane());
    }

    private Pane middle_Pane() {
        Pane pane = new Pane();
        Button bouton1 = new Button("lancement");
        gestion_bouton_lancement(bouton1,pane);
        return pane;
    }


    // gere les evenement du bouton de lancement
    private void gestion_bouton_lancement(Button bouton, Pane pane) {
        bouton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mon_image != null) {
                    Plateau plateau = new Plateau(10, 10, 100, 60);
                    setPlateauOnPane(plateau, pane);
                }
            }
        });
    }
    //ajoute toute les piece du plateau au Pane
    private void setPlateauOnPane(Plateau p, Pane pane) {
        for (int i = 0; i < p.getNb_ligne(); i++) {
            for (int j = 0; j < p.getNb_colonne(); j++) {
                pane.getChildren().add(p.getTab()[i][j].forme);
            }
        }
    }
    private MenuBar gestion_Menu() {
        // barre de menu
        MenuBar menuBar = new MenuBar();
        // menu
        Menu menu1 = new Menu("File");
        Menu menu2 = new Menu("Lancement");
        Menu menu3 = new Menu("Navigate");
        fill_menu_file(menu1);
        menuBar.getMenus().addAll(menu1,menu2,menu3);
        return menuBar;
    }
    //gere le menu de lancement
    private void fill_menu_lancement(Menu menu) {
        MenuItem item1 = new MenuItem("new"); // pour creer un nouveau puzzle
        MenuItem item2 = new MenuItem("open");//pour les puzzles en cours de resolution
        /**TODO: gestion du open */
        item1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // affichage d'une fenetre auxiliaire pour les parametre de creation
                // du nouveau puzzle

            }
        });
    }
    private void fill_menu_file(Menu menu) {
        //itemMenu
        MenuItem item1 = new MenuItem("Choose image");
        MenuItem item2 = new MenuItem("Quit");
        MenuItem item3 = new MenuItem("Save as");
        item2.setOnAction(e-> Platform.exit());//gere le Quit : crtl-Q // ajouter a ma stage keypressed ctrl Q pour quit

        event1(item1);
        menu.getItems().addAll(item1,item2,item3);

    }

    private void event1(MenuItem item) {
        FileChooser fil_chooser = new FileChooser();
        // add filters file's extension
        fil_chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("png files", "*.png"),
                new FileChooser.ExtensionFilter("jpg files", "*.jpg"),
                new FileChooser.ExtensionFilter("jpeg files", "*.jpeg"),
                new FileChooser.ExtensionFilter("All files", "*.*"));
        Label label = new Label("no files selected");
        // create an Event Handler
        EventHandler<ActionEvent> event1 =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        // get the file selected
                        File file = fil_chooser.showOpenDialog(ma_stage);
                        if (file != null) {
                            label.setText(file.getAbsolutePath()
                                    + "  selected");
                        }
                        //creation image via le file
                        mon_image = new Image("file:" + file);
                    }
                };
        item.setOnAction(event1);
    }

    /**
     *
     *  gestion de la fenetre de
     *  parametrage d'un nouveau puzzle
     *
     * */

    private void create_stage() {
        Stage stage = new Stage();
        Pane pane = new Pane();
        pane.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        try {
            // Localisation du fichier FXML.
            final URL url = getClass().getResource("parametrage.fxml");
            // Création du loader
            final FXMLLoader fxmlLoader = new FXMLLoader(url);
            // Chargement du FXML
            // Le cast (AnchorPane) n'est pas nécessaire grâce au type
            // générique <T> T load() de la méthode load().
            // Le cast est de toutes façons réalisé à l'exécution
            // par le bytecode.
            final AnchorPane root = (AnchorPane) fxmlLoader.load();
            // Création de la scène
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (Exception exception) {
            System.err.println("Erreur au chargement: " + exception);
        }
        // Titre
        stage.setTitle("Fenetre de parametrage");
        // Affichage
        stage.show();
    }

    private double rand() throws InterruptedException {
        Random r = new Random();
        sleep(100);
        double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        return randomValue;
    }
}
