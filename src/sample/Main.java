package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
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
import javafx.stage.Stage;

import javax.swing.border.Border;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class Main extends Application {
    double oldX,oldY;
    static Stage primary_Stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primary_Stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("Fenetre.fxml"));
        primaryStage.setTitle("Jeu du Puzzle");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        /*test_Piece(root);
        test_piece2(root);
        //test_Plateau(root);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1400, 1275));
       // primaryStage.show();
        new Fenetre();*/
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
    private void test_Plateau(BorderPane root) {
        Plateau plateau = new Plateau(10, 10,100,60 );
        System.out.println("colonne : " + plateau.getNb_colonne() + " et ligne : " + plateau.getNb_ligne());
        for (int j = 0; j < plateau.getNb_colonne(); j++) {
            for (int i = 0; i < plateau.getNb_ligne() ; i++) {
                root.getChildren().add(plateau.getTab()[i][j].forme);
            }
        }
       // root.getChildren().addAll(plateau.getListe_piece());
    }

    private void test_piece2(BorderPane root) {
        ArrayList<Forme_Bordure> list = new ArrayList<>();
        list.add(null);list.add(new Dents(1,220,220,300,400));list.add(null);list.add(null);
        Piece p = new Piece(list, 220+400, 220, 300 , 400);
        File file = new File("./image/shingeki.jpg");
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
        root.getChildren().add( p.forme);
    }
    private void test_Piece(BorderPane root) {
        ArrayList<Forme_Bordure> list = new ArrayList<>();
        //list.add(new Bordure_Plate(0, 20, 20));list.add(new Dents(20,20));list.add(new Dents(20,20));list.add(new Creux(new Dents(20,20)));
        list.add(null);list.add(new Dents(1,220,220,300,400));list.add(null);list.add(null);
        //list.add(new Bordure_Plate(0, 220, 220,270,270));list.add(new Bordure_Plate(1, 220, 220,270,27));list.add(new Bordure_Plate(2, 220, 220,270,270));list.add(new Bordure_Plate(3, 220, 220,270,270));
        Piece p = new Piece(list, 220, 220, 300 , 400);
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
}
/*
Pane root = new Pane();
        BorderPane pane = new BorderPane();
        //menuBar
        MenuBar bar = new MenuBar();
        //menu
        Menu menu1 = new Menu("File");
        Menu menu3 = new Menu("View");
        Menu menu4 = new Menu("Naviate");
        Menu menu_test = new Menu("Test");
        MenuItem item5 = new MenuItem("fenetre");
        item5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    new Graphic();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        menu_test.getItems().addAll(item5);
        //itemMenu
        MenuItem item1 = new MenuItem("Open");
        MenuItem item3 = new MenuItem("Save as");
        MenuItem item2 = new MenuItem("Quit");
        item2.setOnAction(e-> Platform.exit());//gere le Quit : crtl-Q
        menu1.getItems().addAll(item1,item2,item3);

        Menu menu2 = new Menu("Edit");
        RadioMenuItem rmi = new RadioMenuItem("Edit");
        MenuItem itemE1 = new MenuItem("Add");
        itemE1.setDisable(true);
        itemE1.disableProperty().bind(rmi.selectedProperty().not());
        menu2.getItems().addAll(rmi,itemE1);

        bar.getMenus().addAll(menu1,menu2,menu3,menu4,menu_test);


        // create a File chooser
        FileChooser fil_chooser = new FileChooser();
        // add filters file's extension
        fil_chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("dot files","*.dot"),
                new FileChooser.ExtensionFilter("pdf files","*.pdf"),
                new FileChooser.ExtensionFilter("text files", "*.txt"),
                new FileChooser.ExtensionFilter("All files", "*.*"));
        // create a Label
        Label label = new Label("no files selected");

        List<String> ma_list = new ArrayList<String>();
        ObservableList<String> list = FXCollections.observableList(ma_list);
        ListView<String> lv = new ListView();
        // Add the CellFactory to the ListView
        lv.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> stringListView) {
                return new TextFieldListCell<String>(new DefaultStringConverter());
            }
        });
        EventHandler<ActionEvent> event2 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fil_chooser2 = new FileChooser();
                File file = fil_chooser2.showSaveDialog(primaryStage);
                fil_chooser2.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Lst files","*.lst"));
                if (file != null) {
                    String s = "";
                    for (String item : lv.getItems()) {
                        s+=item+'\n';
                    }
                    writer(file,s);
                }
            }
        };
        // create an Event Handler
        EventHandler<ActionEvent> event1 =
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        // get the file selected
                        File file = fil_chooser.showOpenDialog(primaryStage);
                        if (file != null) {
                            label.setText(file.getAbsolutePath()
                                    + "  selected");
                        }
                        //read file into stream, try-with-resources
                        Path path = Paths.get(file.getAbsolutePath());
                        //consumer appelé lors de .accept plus tard
                        Consumer<String> consumer = i -> System.out.println(i);
                        try (Stream<String> stream = Files.lines(path)) {
                            stream.forEach(s->{
                                ma_list.add(s);
                                consumer.accept(s.toString());
                            });
                            list.forEach(s->lv.getItems().add(s.toString()));
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }
                };
        item1.setOnAction(event1);
        item3.setOnAction(event2);
        //gestion de l'editabilité des eltss de lv
        lv.editableProperty().bind(rmi.selectedProperty());

        itemE1.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent actionEvent) {
        String s = new String();
        s = " ";
        lv.getItems().add(s);
        }
        });
        pane.setTop(bar);
        pane.setCenter(lv);
        root.getChildren().add(pane);
        primaryStage.setScene(new Scene(root, 600, 275));
        primaryStage.show();
    }
 */