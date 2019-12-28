package sample;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Plateau {
    private final static String DEFAULT_FILE = "./image/roronoa.jpg";
    private Pane panneau;
    private int posX;
    private int posY;
    protected static int nb_ligne;
    protected static int nb_colonne;
    private double longueur;
    private double hauteur;
    private Image image;
    protected Piece tab[][];
    private ArrayList<Shape> liste_piece;
    private double oldX, oldY;
    private static final int DEFAULT_NIVEAU = 1;
    private int niveau = DEFAULT_NIVEAU;
    protected static double tab_longueur[][];
    protected static double tab_hauteur[][];
    private double longueur_plateau;
    private double hauteur_plateau;

    public Plateau(Plateau p) {// pour le plateau transparent
        this.tab = new Piece[p.getTab().length][p.getTab()[0].length];
        copie_piece_plateau(p);
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

    public Plateau(int x, int y, int ligne, int col, double longueur, double hauteur, Image image, int niveau, Pane pane) {
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
        this.panneau = pane;
        this.hauteur_plateau = ligne * hauteur;
        this.longueur_plateau = col * longueur;
        if (niveau == 5) { // niveau 5 = niveau de deformation globale de cadre
            fill_tab_longueur();
            fill_tab_hauteur();
            create_plateau_deform();
        } else {
            create_plateau();
        }
        ajout_piece_liste();
    }

    //rempli le tableau de longueur si on est dans le niveau 5
    private void fill_tab_longueur() {
        tab_longueur = new double[nb_ligne + 1][nb_colonne]; // plus 1 pcq chaque piece a deux cote en longueur et deux en hauteur
        for (int i = 0; i < tab_longueur.length; i++) {
            double longueur_ligne = 0.0;
            for (int j = 0; j < tab_longueur[0].length; j++) {
                // on genere une longueur de piece
                int signe = new Random().nextInt(2);// determine le signe
                double extra = new Random().nextDouble() * (longueur * 0.075); // extra de +- 7.5 % de la longueur d'une piece
                if (signe == 0) {
                    // extra reste positif
                } else if ( signe == 1) { // extra negatif
                    extra = -extra;
                }else{
                    Main.consumer.accept(" ICI ya un probleme chef ");
                }
                if (j == 0) { // premiere colonne
                    tab_longueur[i][j] = longueur + extra;
                    longueur_ligne += tab_longueur[i][j];
                } else if (j == tab_longueur[0].length - 1) { // derniere colonne
                    // pas d'extra car derniere colonne
                    double extra_pred = tab_longueur[i][j - 1] - longueur;
                    tab_longueur[i][j] =  longueur_plateau - longueur_ligne;
                }else{ // piece du milieu
                    double extra_pred = tab_longueur[i][j - 1] - longueur;
                    tab_longueur[i][j] = longueur + extra - extra_pred;
                    longueur_ligne += tab_longueur[i][j];
                }
               // tab_longueur[i][j] = longueur_plateau / nb_colonne;
            }
        }
        //check_longueur_tab_longueur();
    }

    private void fill_tab_hauteur() {
        this.tab_hauteur = new double[nb_ligne][nb_colonne + 1];
        for (int j = 0; j < tab_hauteur[0].length; j++) {
            double hauteur_colonne = 0.0;
            for (int i = 0; i < tab_hauteur.length; i++) {
               /* int signe = new Random().nextInt(2);// determine le signe
                double extra = new Random().nextDouble() * (hauteur * 0.05); // extra de +- 5 % de la hauteur d'une piece
                if (signe == 0) {
                    // extra reste positif
                } else if ( signe == 1) { // extra negatif
                    extra = -extra;
                }else{
                    Main.consumer.accept(" ICI ya un probleme chef  ");
                }

                if (i == 0) { // premiere ligne
                    tab_hauteur[i][j] = hauteur + extra;
                    hauteur_colonne += tab_hauteur[i][j];
                } else if (i == tab_hauteur.length - 1) { // derniere ligne
                    double extra_pred = tab_hauteur[i-1][j] - hauteur;
                    //Main.consumer.accept("hauteur_colonne : "+hauteur_colonne);
                    //Main.consumer.accept("hauteur plateau : "+hauteur_plateau);
                    //Main.consumer.accept("hauteur caculée : "+ (hauteur_plateau - hauteur_colonne));
                    tab_hauteur[i][j] =  hauteur_plateau - hauteur_colonne;
                }else{ // piece du milieu
                    double extra_pred = tab_hauteur[i-1][j] - hauteur;
                    tab_hauteur[i][j] = hauteur + extra - extra_pred;
                    hauteur_colonne += tab_hauteur[i][j];
                }*/
                tab_hauteur[i][j] = hauteur;
            }
        }
        //check_longueur_tab_hauteur();
    }
    private void check_longueur_tab_hauteur() {
        for (int i = 0; i < tab_hauteur.length; i++) {
            double res = 0.0;
            for (int j = 0; j < tab_hauteur[0].length; j++) {
                Main.consumer.accept(i+";"+j+" : "+tab_hauteur[i][j]);
                res += tab_hauteur[i][j];
            }
            Main.consumer.accept("hauteur : "+hauteur);
            Main.consumer.accept("hauteur plateau : "+hauteur_plateau);
            Main.consumer.accept("hauteur de la colonne : "+i+" = "+res);
        }
        for (int j = 0; j < tab_hauteur[0].length; j++) {
            double res = 0.0;
            for (int i = 0; i < tab_hauteur.length; i++) {
                Main.consumer.accept(i+";"+j+" : "+tab_hauteur[i][j]);
                res += tab_hauteur[i][j];
            }
            Main.consumer.accept("hauteur : "+hauteur);
            Main.consumer.accept("hauteur plateau : "+hauteur_plateau);
            Main.consumer.accept("hauteur de la colonne : "+j+" = "+res);
        }
    }
    private void check_longueur_tab_longueur() {
        for (int i = 0; i < tab_longueur.length; i++) {
            double res = 0.0;
            for (int j = 0; j < tab_longueur[0].length; j++) {
                Main.consumer.accept(i+";"+j+" : "+tab_longueur[i][j]);
                res += tab_longueur[i][j];
            }
            Main.consumer.accept("longueur : "+longueur);
            Main.consumer.accept("longueur plateau : "+longueur_plateau);
            Main.consumer.accept("longueur de la ligne : "+i+" = "+res);
        }
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
                // on recupere les bordures de contraintes de la piece courante et on les met dans notre liste locale
                ArrayList<Forme_Bordure> liste = new ArrayList<Forme_Bordure>();
                recup_bordure_contrainte(i, j, liste);
                double h = hauteur;
                double l = longueur;
                Piece piece = new Piece(liste, j * l, i * h, hauteur, longueur, niveau, panneau);
                piece.path.setFill(Color.TRANSPARENT);
                piece.path.setStrokeWidth(1);
                piece.path.setStroke(Color.BLACK);
                gestion_Image(piece);
                tab[i][j] = piece;
            }
        }
    }

    private void create_plateau_deform() {
        Main.consumer.accept(" dans create plateau deform : deformation globale de cadre");
        for (int i = 0; i < nb_ligne; i++) {
            for (int j = 0; j < nb_colonne; j++) {
                // on recupere les bordures de contraintes de la piece courante et on les met dans notre liste locale
                ArrayList<Forme_Bordure> liste = new ArrayList<Forme_Bordure>();
                recup_bordure_contrainte(i, j, liste);
                double hauteur1, hauteur2, longueur1, longueur2;
                longueur1 = tab_longueur[i][j];
                longueur2 = tab_longueur[i + 1][j];
                hauteur1 = tab_hauteur[i][j];
                hauteur2 = tab_hauteur[i][j + 1];
                double x = getPositionX(i, j);
                double y = getPositionY(i, j);
                Main.consumer.accept("pos x :"+x+"pos y :"+y);
                Main.consumer.accept("h1 : "+hauteur1+" h2 :"+hauteur2);
                Main.consumer.accept("l1 : "+longueur1+" l2 :"+longueur2);
                Piece piece = new Piece(liste, x, y, hauteur1, hauteur2, longueur1, longueur2, niveau, panneau, i, j);
                piece.path.setFill(Color.TRANSPARENT);
                piece.path.setStrokeWidth(1);
                piece.path.setStroke(Color.BLACK);
                gestion_Image(piece);
                tab[i][j] = piece;
            }
        }
    }

    //renvoie la position posY d'une piece dans le plateau en fonction des sommes de longueur et hauteur dans le niveau 5
    private double getPositionY(int i, int j) {
        //  i pour les hauteur / j pour les longueurs
        double y = 0.0;
        // recup posY
        for (int k = 0; k < tab_hauteur[0].length; k++) {
            double cumul = 0.0;
            for (int l = 0; l < tab_hauteur.length; l++) {
                if (k == j) {
                    if (l == i) {
                        y = cumul;
                    }
                    cumul += tab_hauteur[l][k];
                }
            }
        }


        return y;
    }
    //renvoie la position posX d'une piece dans le plateau en fonction des sommes de longueur et hauteur dans le niveau 5
    private double getPositionX(int i, int j) {
        //  i pour les hauteur / j pour les longueurs
        double x = 0.0;
        // recup posX
        for (int k = 0; k < tab_longueur.length; k++) {
            double cumul = 0.0;
            for (int l = 0; l < tab_longueur[0].length; l++) {
                if (k == i) {
                    if (l == j) {
                        x = cumul;
                    }
                    cumul += tab_longueur[k][l];
                }
            }
        }
        return x;
    }
    private void gestion_Image(Piece p) {
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
    public void setLongueur_plateau(double longueur_plateau) {
        this.longueur_plateau = longueur_plateau;
    }

    public double getLongueur_plateau() {
        return longueur_plateau;
    }

    public double getHauteur_plateau() {
        return hauteur_plateau;
    }

    public void setHauteur_plateau(double hauteur_plateau) {
        this.hauteur_plateau = hauteur_plateau;
    }


}
