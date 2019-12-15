package sample;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Plateau {
    private final static String DEFAULT_FILE = "./image/roronoa.jpg";
    private int posX;
    private int posY;
    private static int nb_ligne;
    private static int nb_colonne;
    private double longueur;
    private double hauteur;
    private Image image;
    protected Piece tab[][];
    private ArrayList<Shape> liste_piece;
    private double oldX, oldY;
    private static final int DEFAULT_NIVEAU = 1;
    private int niveau = DEFAULT_NIVEAU;

    public Plateau(Plateau p) {// pour le plateau transparent
        //this.longueur = p.longueur;
        //this.hauteur = p.hauteur;
        //this.tab = p.tab;
        //Main.consumer.accept("plateau.getTab().length :"+p.getTab().length+"plateau.getTab()[0].length "+p.getTab()[0].length);
        this.tab = new Piece[p.getTab().length][p.getTab()[0].length];
        copie_piece_plateau(p);
        //copie_shape(p);
        //set_default_color();
    }

    private void copie_piece_plateau(Plateau plateau) {
        for (int i = 0; i < plateau.tab.length; i++) {
            for (int j = 0; j < plateau.tab[0].length; j++) {
                Piece p = plateau.tab[i][j];
                Piece ma_piece = new Piece(p);
                ma_piece.path.setFill(Color.TRANSPARENT);
                ma_piece.path.setStrokeWidth(1.5);
                ma_piece.path.setStroke(Color.LIGHTGRAY);
                this.tab[i][j] = ma_piece;
            }
        }
    }

    private void set_default_color() {
        for (int i = 0; i < this.tab.length; i++) {
            for (int j = 0; j < this.tab[0].length; j++) {
                tab[i][j].forme.setFill(Color.WHITE);
                tab[i][j].forme.setFill(Color.WHITE);
                tab[i][j].forme.setStrokeWidth(1);
                tab[i][j].forme.setStroke(Color.BLACK);
            }
        }
    }
    public Plateau(int ligne,int col, double longueur, double hauteur) {
        image = new Image("file:" + DEFAULT_FILE);//met une image par defaut
        nb_ligne = ligne;
        nb_colonne = col;
        this.longueur = longueur;
        this.hauteur = hauteur;
        tab = new Piece[ligne][col];
        create_plateau();
    }
    public Plateau(int x, int y,int ligne,int col, double longueur, double hauteur, Image image) {
        this.posX = x; // a ajouter dans create plateau aux coord des != piece MORRAY
        this.posY = y;
        this.liste_piece = new ArrayList<Shape>();
        this.image = image;
        nb_ligne = ligne;
        nb_colonne = col;
        this.longueur = longueur;
        this.hauteur = hauteur;
        tab = new Piece[ligne][col];
        create_plateau();
        ajout_piece_liste();
    }
    public Plateau(int x, int y,int ligne,int col, double longueur, double hauteur, Image image, int niveau) {
        //Main.consumer.accept(" dans plateau avec niveau");
        this.posX = x; // a ajouter dans create plateau aux coord des != piece MORRAY
        this.posY = y;
        this.liste_piece = new ArrayList<Shape>();
        this.image = image;
        nb_ligne = ligne;
        nb_colonne = col;
        this.longueur = longueur;
        this.hauteur = hauteur;
        tab = new Piece[ligne][col];
        this.niveau = niveau;
        create_plateau();
        ajout_piece_liste();
    }


    private void ajout_piece_liste() {
        for (Piece[] pieces : tab) {
            for (Piece piece : pieces) {
                //this.liste_piece.add(piece.forme);
                this.liste_piece.add(piece.path);
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
                double h = hauteur;
                double l = longueur;
                Piece piece = new Piece(liste, j * l, i * h, hauteur, longueur,niveau);
                piece.path.setFill(Color.TRANSPARENT);
                //piece.forme.setFill(Color.TRANSPARENT);
                piece.path.setStrokeWidth(1);
                //piece.forme.setStrokeWidth(1);
                piece.path.setStroke(Color.BLACK);
                //piece.forme.setStroke(Color.BLACK);
                //add_evenement(piece); //faire l'ajout d'evenment dans le controller
                gestion_Image(piece);
                tab[i][j] = piece;
            }
        }
    }

    private void gestion_Image(Piece p) {
       //p.forme.setFill(new ImagePattern(this.image,0,0,this.longueur*nb_colonne,this.hauteur*nb_ligne,false));
       p.path.setFill(new ImagePattern(this.image,0,0,this.longueur*nb_colonne,this.hauteur*nb_ligne,false));
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
    public ArrayList<Shape> getListe_piece() {
        return liste_piece;
    }

    public void setListe_piece(ArrayList<Shape> liste_piece) {
        this.liste_piece = liste_piece;
    }

    public double getLongueur() {
        return longueur;
    }

    public void setLongueur(int longueur) {
        this.longueur = longueur;
    }

    public static int getNb_ligne() {
        return nb_ligne;
    }

    public void setNb_ligne(int nb_ligne) {
        this.nb_ligne = nb_ligne;
    }

    public static int getNb_colonne() {
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
