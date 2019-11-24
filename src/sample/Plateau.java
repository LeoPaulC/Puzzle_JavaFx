package sample;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Shape;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Plateau {
    private final static String DEFAULT_FILE = "./image/shingeki.jpg";
    private int posX;
    private int posY;
    private int nb_ligne;
    private int nb_colonne;
    private int longueur;
    private int hauteur;
    private Image image;
    private Piece tab[][];
    private ArrayList<Shape> liste_piece;
    private double oldX, oldY;

    public Plateau(int ligne,int col, int longueur, int hauteur) {
        image = new Image("file:" + DEFAULT_FILE);//met une image par defaut
        nb_ligne = ligne;
        nb_colonne = col;
        this.longueur = longueur;
        this.hauteur = hauteur;
        tab = new Piece[ligne][col];
        create_plateau();
    }
    public Plateau(int x, int y,int ligne,int col, int longueur, int hauteur, File file) {
        this.posX = x; // a ajouter dans create plateau aux coord des != piece MORRAY
        this.posY = y;
        this.liste_piece = new ArrayList<Shape>();
        image = new Image("file:"+file);
        nb_ligne = ligne;
        nb_colonne = col;
        this.longueur = longueur;
        this.hauteur = hauteur;
        tab = new Piece[ligne][col];
        create_plateau();
        ajout_piece_liste();
    }

    public ArrayList<Shape> getListe_piece() {
        return liste_piece;
    }

    public void setListe_piece(ArrayList<Shape> liste_piece) {
        this.liste_piece = liste_piece;
    }

    private void ajout_piece_liste() {
        for (Piece[] pieces : tab) {
            for (Piece piece : pieces) {
                this.liste_piece.add(piece.forme);
            }
        }
    }
    // rempli le tableau de piece
    private void create_plateau() {
        System.out.println("hauteur : "+hauteur+" longueur : "+longueur);
        for (int i = 0; i < nb_ligne; i++) {
            for (int j = 0; j < nb_colonne; j++) {
                ArrayList<Forme_Bordure> liste = new ArrayList<Forme_Bordure>();
                recup_bordure_contrainte(i, j, liste);
                Piece piece = new Piece(liste, j * longueur, i * hauteur, hauteur, longueur);
                piece.forme.setFill(Color.TRANSPARENT);
                piece.forme.setStrokeWidth(2);
                piece.forme.setStroke(Color.BLACK);
                add_evenement(piece);
                gestion_Image(piece);
                tab[i][j] = piece;
            }
        }
    }

    private void gestion_Image(Piece p) {
       p.forme.setFill(new ImagePattern(this.image,0,0,this.longueur*nb_colonne,this.hauteur*nb_ligne,false));
    }
    private void add_evenement(Piece p) {
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
    }

    //recupere les bordures des pieces entourant notre piece
    //afin de remplir la liste de contraintes de bordure de notre piece
    private void recup_bordure_contrainte(int i, int j, ArrayList<Forme_Bordure> liste ) {
        liste.add(gestion_bordure_HAUT(i,j));
        liste.add(gestion_bordure_DROITE(i,j));
        liste.add(gestion_bordure_BAS(i,j));
        liste.add(gestion_bordure_GAUCHE(i,j));
    }


    //renvoie la bordure de contrainte DROITE de notre piece == bordure plate ou GAUCHE de la piece en j+1
    private Forme_Bordure gestion_bordure_DROITE(int i, int j) {
        if (j == nb_colonne -1) {
            return new Bordure_Plate(Piece.DROITE, i * hauteur, j * longueur, hauteur, longueur);
        } else {
            return null ; // choix libre pcq piece a droite vide
        }
    }
    //renvoie la bordure de contrainte GAUCHE de notre piece == bordure plate ou DROITE de la piece en j-1
    private Forme_Bordure gestion_bordure_GAUCHE(int i, int j) {
        if (j == 0) {
            return new Bordure_Plate(Piece.GAUCHE, i * hauteur, j * longueur, hauteur, longueur);
        } else {
            return tab[i][j - 1].getBordure(Piece.DROITE);
        }
    }

    //renvoie la bordure de contrainte BAS de notre piece ==  bordure plate ou HAUT de la piece au dessus i+1
    private Forme_Bordure gestion_bordure_BAS(int i, int j) {
        if (i == nb_ligne -1) {
            return new Bordure_Plate(Piece.BAS, i * hauteur, j * longueur, hauteur, longueur);
        } else { // on ne la pas encore cree donc random mec !
            return null;
        }
    }
    //renvoie la bordure de contrainte HAUT de notre piece ==  bordure plate ou BAs de la piece au dessus i-1
    private Forme_Bordure gestion_bordure_HAUT(int i, int j) {
        if (i == 0) {
            return new Bordure_Plate(Piece.HAUT, i * hauteur, j * longueur, hauteur, longueur);
        } else {
            return tab[i - 1][j].getBordure(Piece.BAS);
        }
    }
    public int getLongueur() {
        return longueur;
    }

    public void setLongueur(int longueur) {
        this.longueur = longueur;
    }

    public int getNb_ligne() {
        return nb_ligne;
    }

    public void setNb_ligne(int nb_ligne) {
        this.nb_ligne = nb_ligne;
    }

    public int getNb_colonne() {
        return nb_colonne;
    }

    public void setNb_colonne(int nb_colonne) {
        this.nb_colonne = nb_colonne;
    }

    public Piece[][] getTab() {
        return tab;
    }

    public void setTab(Piece[][] tab) {
        this.tab = tab;
    }

    public double getLargeur() {
        return longueur;
    }

    public void setLargeur(int longueur) {
        this.longueur = longueur;
    }

    public double getHauteur() {
        return hauteur;
    }

    public void setHauteur(int hauteur) {
        this.hauteur = hauteur;
    }
    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

}
