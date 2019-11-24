package sample;


import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Controller_Fenetre  {
    private static int width_win = 600;
    private static int height_win = 600;
    private static final int DEFAULT_NIVEAU = 0;
    private Stage stage;
    private Scene scene;
    private Image image;
    private static final int min_longueur = Piece.MIN_LONGUEUR;
    private static final int min_hauteur = Piece.MIN_HAUTEUR;
    private int niveau=DEFAULT_NIVEAU;
    @FXML private MenuItem open;
    @FXML private MenuItem quit;
    @FXML private MenuItem save_as;
    @FXML private MenuItem new_puzzle;

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
            Scene scene2 = new Scene(root2, 800, 600);
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
    private void fill_quit() {
        quit.setOnAction(e -> Platform.exit());
        // Fermeture propre de l'application
        // stage.setOnCloseRequest(e -> Platform.exit());
    }



    // parametrage

    @FXML
    private void on_radio() {
        if (radioButton.isSelected()) {
            this.niveau = 1;
        } else if (radioButton2.isSelected()) {
            this.niveau = 2;
        } else if (radioButton3.isSelected()) {
            this.niveau = 3;
        } else if(radioButton4.isSelected()) {
            this.niveau = 4;
        }
        txt_niveau.setText(String.valueOf(niveau));
    }

    @FXML
    private void reset_onAction() {

    }

    @FXML
    private void valider_onAction() {

    }
    /*
    //verifie si tout les champs du paarametrage dd'un nouveau puzzle sont bien remplie
    private boolean check_parametrage() {
        boolean res = false;
        if (check_image()) {
            if (niveau != 0) {

            }
        }
        return  res;
    }

    private boolean check_image() {
        if (this.image != null) {
            return true;
        }
        return false;
    }
*/
    @FXML
    private void chooseImage() {
        event1();
        if (image != null) {
            /**TODO passer les attribut en double !!!!!  */
            init_spinner();
        }
    }
    // init les spinner en fonction de la resolution de l'image choisi
    private void init_spinner() {
        int h = (int) image.getHeight();
        int max_li = (int) (h / min_hauteur);// pas besoin des max ??????????
        int w = (int) image.getWidth();
        int max_col = (int) (w / min_longueur);
        System.out.println("w: "+w+" min_longueur: "+min_longueur+" == max_col : "+max_col);
        System.out.println("h: "+h+" min_hauteur: "+min_hauteur+" == max_li : "+max_li);
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(5, max_col, 5);
        spinner_colonne.setValueFactory(valueFactory);

        SpinnerValueFactory<Integer> valueFactory2 =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(5, max_li, 5);
        spinner_ligne.setValueFactory(valueFactory2);
        nb_ligne.textProperty().bind(StringProperty.stringExpression(spinner_ligne.valueProperty()));
        nb_colonne.textProperty().bind(StringProperty.stringExpression(spinner_colonne.valueProperty()));
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

