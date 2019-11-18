package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Random;

public class Dents extends Forme_Bordure {

    Shape c ;
    Random rand ;
    int tmp ;
    double posX = DEFAULT_COORD_X;
    double posY = DEFAULT_COORD_Y;
    private final static Boolean est_plat = false;
    public Dents(double x, double y) {
        super(est_plat);
        posX = x;
        posY = y;
        System.out.println("je suis dans dents x y !");
        System.out.println("posX : " + x);
        System.out.println("posY :" + y);
        fill_liste_cercle();
        fill_list_cercle_controle();
    }
    Dents(){ // Aléatoire complet
        super(est_plat);
        rand = new Random();
        System.out.println("posX : " + posX);
        System.out.println("posY : " + posY);
        fill_liste_cercle();
        fill_list_cercle_controle();
        for (int i = 0; i < this.liste_cubicCurveTo.size() ; i++) {

            this.liste_cubicCurveTo.get(i).xProperty().bind(this.liste_cercle.get(i).layoutXProperty());
            this.liste_cubicCurveTo.get(i).yProperty().bind(this.liste_cercle.get(i).layoutYProperty());

            this.liste_cubicCurveTo.get(i).controlX1Property().bind(this.liste_cercle_controle.get(2*i+1).layoutXProperty() );
            this.liste_cubicCurveTo.get(i).controlY1Property().bind(this.liste_cercle_controle.get(2*i+1).layoutYProperty());

            this.liste_cubicCurveTo.get(i).controlX2Property().bind(this.liste_cercle_controle.get(2*i).layoutXProperty());
            this.liste_cubicCurveTo.get(i).controlY2Property().bind(this.liste_cercle_controle.get(2*i).layoutYProperty());

            this.liste_Moveto.get(i).xProperty().bind(this.liste_cercle.get(i+1).layoutXProperty());
            this.liste_Moveto.get(i).yProperty().bind(this.liste_cercle.get(i+1).layoutYProperty());

            notre_path.getElements().add(this.liste_Moveto.get(i));
            notre_path.getElements().add(this.liste_cubicCurveTo.get(i));
        }
    }
    //rempli la liiste de cercle en placatnt le premier point en fonction de posX et posY
    private void fill_liste_cercle() {
        /**
         * pour nos test on va placer le premier point au coordonne 200,300
         */
        this.liste_cercle.get(0).setLayoutX(posX);
        this.liste_cercle.get(0).setLayoutY(posY);
        this.liste_cercle.get(0).setFill(Color.GOLD);
        /**
         * on place le 6eme cercle sur la meme ligne que le premier et decalé de "LA largeur d'une piece" .
         */
        this.liste_cercle.get(6).setLayoutX(this.liste_cercle.get(0).getLayoutX() + Forme_Bordure.getTailleCotePieceLongueur() );
        this.liste_cercle.get(6).setLayoutY(this.liste_cercle.get(0).getLayoutY() );
        this.liste_cercle.get(6).setFill(Color.GOLD);
        /**
         * Il nous reste a disposer les 5 points restant , à savoir du 1 à 5 .
         */
        int a ;
        for (int i = 1; i < Forme_Bordure.getNbCercleBordure()-1 ; i++) {
            switch (i){
                case 1 :
                    // cercle N° 1
                    this.liste_cercle.get(i).setLayoutX(this.liste_cercle.get(0).getLayoutX()+ TAILLE_COTE_PIECE_LONGUEUR*0.4); // on decale la position x1 de la piece 1 de ''tmp'' par rapport a x0
                    this.liste_cercle.get(i).setLayoutY(this.liste_cercle.get(0).getLayoutY()-TAILLE_COTE_PIECE_HAUTEUR*0.08);
                    this.liste_cercle.get(i).setFill(Color.ORANGE);
                    break ;
                case 2 :
                    this.liste_cercle.get(i).setLayoutX(this.liste_cercle.get(0).getLayoutX()+ TAILLE_COTE_PIECE_LONGUEUR * 0.36);
                    this.liste_cercle.get(i).setLayoutY(this.liste_cercle.get(0).getLayoutY() - TAILLE_COTE_PIECE_HAUTEUR * 0.26);
                    this.liste_cercle.get(i).setFill(Color.RED);
                    break ;
                case 3 :
                    // On fixe le x3
                    this.liste_cercle.get(i).setLayoutX( liste_cercle.get(0).getLayoutX() + (  ( liste_cercle.get(6).getLayoutX() - liste_cercle.get(0).getLayoutX() ) /2 ) ) ;
                    this.liste_cercle.get(i).setLayoutY(liste_cercle.get(0).getLayoutY() - TAILLE_COTE_PIECE_HAUTEUR * 0.4);
                    this.liste_cercle.get(i).setFill(Color.PINK);
                    break;
                case 4 :
                    this.liste_cercle.get(i).setLayoutX(liste_cercle.get(6).getLayoutX() - TAILLE_COTE_PIECE_LONGUEUR*0.36);
                    this.liste_cercle.get(i).setLayoutY(this.liste_cercle.get(6).getLayoutY()- TAILLE_COTE_PIECE_HAUTEUR * 0.26);
                    this.liste_cercle.get(i).setFill(Color.PURPLE);
                    break ;
                case 5 :
                    this.liste_cercle.get(i).setLayoutX(liste_cercle.get(6).getLayoutX() - TAILLE_COTE_PIECE_LONGUEUR * 0.4);
                    this.liste_cercle.get(i).setLayoutY(this.liste_cercle.get(6).getLayoutY() - TAILLE_COTE_PIECE_HAUTEUR*0.08);
                    this.liste_cercle.get(i).setFill(Color.DARKBLUE);

                    break ;
                default:
                    // Inutile donc utile a mettre :)
                    break ;
            }

        }

    }
    //rempli la liste de cercle controle en fonction des cercle de la liste de cercle
    private void fill_list_cercle_controle() {
        for (int i = 0; i < this.liste_cercle_controle.size(); i++) { // car les points de controle sont cree 2 à 2
            switch (i) {
                case 0:
                    this.liste_cercle_controle.get(i).setLayoutX(this.liste_cercle.get(i).getLayoutX()+TAILLE_COTE_PIECE_LONGUEUR*0.2);
                    this.liste_cercle_controle.get(i).setLayoutY(this.liste_cercle.get(i).getLayoutY());
                    break;
                case 1:
                    this.liste_cercle_controle.get(i).setLayoutX(this.liste_cercle.get(0).getLayoutX()+TAILLE_COTE_PIECE_LONGUEUR*0.34);
                    this.liste_cercle_controle.get(i).setLayoutY(this.liste_cercle.get(0).getLayoutY());
                    break;
                case 2 :
                    this.liste_cercle_controle.get(i).setLayoutX(this.liste_cercle.get(0).getLayoutX()+TAILLE_COTE_PIECE_LONGUEUR*0.43);
                    this.liste_cercle_controle.get(i).setLayoutY(this.liste_cercle.get(0).getLayoutY() - TAILLE_COTE_PIECE_HAUTEUR * 0.12);
                    break ;
                case 3 :
                    /**
                     * a gauche de controle 2 et en haut de controle 2
                     */
                    this.liste_cercle_controle.get(i).setLayoutX(this.liste_cercle.get(0).getLayoutX() + TAILLE_COTE_PIECE_LONGUEUR*0.36);
                    this.liste_cercle_controle.get(i).setLayoutY(this.liste_cercle.get(0).getLayoutY() - TAILLE_COTE_PIECE_HAUTEUR * 0.14);
                    break ;
                case 4 :
                    this.liste_cercle_controle.get(i).setLayoutX(this.liste_cercle.get(0).getLayoutX()+TAILLE_COTE_PIECE_LONGUEUR*0.36);
                    this.liste_cercle_controle.get(i).setLayoutY(this.liste_cercle.get(0).getLayoutY() - TAILLE_COTE_PIECE_HAUTEUR * 0.34);
                    break ;
                case 5 :
                    this.liste_cercle_controle.get(i).setLayoutX(this.liste_cercle.get(0).getLayoutX() + TAILLE_COTE_PIECE_LONGUEUR * 0.42);
                    this.liste_cercle_controle.get(i).setLayoutY(this.liste_cercle.get(3).getLayoutY() );
                    break ;
                case 6:
                    this.liste_cercle_controle.get(i).setLayoutX(this.liste_cercle.get(0).getLayoutX()+TAILLE_COTE_PIECE_LONGUEUR*0.58);
                    this.liste_cercle_controle.get(i).setLayoutY(this.liste_cercle.get(0).getLayoutY() - TAILLE_COTE_PIECE_HAUTEUR * 0.40);
                    break ;
                case 7 :
                    this.liste_cercle_controle.get(i).setLayoutX(this.liste_cercle.get(6).getLayoutX() - TAILLE_COTE_PIECE_LONGUEUR * 0.36 );
                    this.liste_cercle_controle.get(i).setLayoutY(this.liste_cercle.get(6).getLayoutY() - TAILLE_COTE_PIECE_HAUTEUR * 0.34);
                    break ;
                case 8 :
                    this.liste_cercle_controle.get(i).setLayoutX(this.liste_cercle.get(6).getLayoutX() - TAILLE_COTE_PIECE_LONGUEUR*0.36);
                    this.liste_cercle_controle.get(i).setLayoutY(this.liste_cercle.get(6).getLayoutY() - TAILLE_COTE_PIECE_HAUTEUR * 0.16);
                    break ;
                case 9:
                    this.liste_cercle_controle.get(i).setLayoutX(this.liste_cercle.get(6).getLayoutX() - TAILLE_COTE_PIECE_LONGUEUR * 0.43);
                    this.liste_cercle_controle.get(i).setLayoutY(this.liste_cercle.get(6).getLayoutY() - TAILLE_COTE_PIECE_HAUTEUR * 0.12);
                    break ;
                case 10 :
                    this.liste_cercle_controle.get(i).setLayoutX(this.liste_cercle.get(6).getLayoutX() - TAILLE_COTE_PIECE_LONGUEUR*0.34);
                    this.liste_cercle_controle.get(i).setLayoutY(this.liste_cercle.get(6).getLayoutY() );
                    break ;
                case 11 :
                    this.liste_cercle_controle.get(i).setLayoutX(this.liste_cercle.get(6).getLayoutX()- TAILLE_COTE_PIECE_LONGUEUR*0.2);
                    this.liste_cercle_controle.get(i).setLayoutY(this.liste_cercle.get(6).getLayoutY());
                    break;
                default:
                    System.out.println("i : " + i);
                    this.Place_Point_Symetriquement(liste_cercle_controle.get(i-1),liste_cercle.get(i/2),this.liste_cercle_controle.get(i), i);
                    this.liste_cercle_controle.get(i).setFill(Color.BLACK);
                    break;
            }
        }
    }

    public void place_point(Circle controle1 , Circle point_fixe , Circle controle2 ) { // permet de placer controle 2 par rapport a point_fixe et de controle 1

        double xC1 = controle1.getLayoutX();
        double xRef = point_fixe.getLayoutX();
        double yC1 = controle1.getLayoutY();
        double yRef = point_fixe.getLayoutY();

        double diffX = (xC1 - xRef) ;
        double diffY = (yC1 - yRef) ;

        diffX *= -1 ;
        diffY *= -1 ;

        controle2.setLayoutX(xRef + diffX);
        controle2.setLayoutY(yRef + diffY);

        System.out.println("X : " + controle2.getLayoutX());
        System.out.println("Y : " + controle2.getLayoutY());
    }
    public void Place_Point_Symetriquement(Circle controle1 , Circle point_fixe , Circle controle2 , int i ){ // permet de placer controle 2 par rapport a point_fixe et de controle 1

        double xC1 = controle1.getLayoutX();
        double xRef = point_fixe.getLayoutX();
        double yC1 = controle1.getLayoutY();
        double yRef= point_fixe.getLayoutY();

        double xC2 = controle2.getLayoutX();
        double yC2 = controle2.getLayoutY();
/*
        double diffX = ( xC1 - xRef) ;
        double diffY = ( yC1 - yRef) ;
        double somme1 = Math.sqrt((Math.pow(diffX,2) + Math.pow(diffY,2))) ;
        diffX/= somme1 ;
        diffY/= somme1 ;
        double diffX2 = (xC2 - xRef) ;
        double diffY2 = (yC2 - yRef) ;
        double somme2 = Math.sqrt((Math.pow(diffX2,2) + Math.pow(diffY2,2))) ;
        diffX *= somme2 ;
        diffY *= somme2 ;
        controle2.setLayoutX(point_fixe.getLayoutX() + diffX);
        controle2.setLayoutY(point_fixe.getLayoutY() + diffY);
        liste_cercle_controle.get(i).setLayoutX(point_fixe.getLayoutX() + diffX);
        liste_cercle_controle.get(i).setLayoutY(point_fixe.getLayoutX() + diffY);

 */
        double abx = xRef - xC1;
        double aby = yRef - yC1;
        double dprim = Math.sqrt(Math.pow(xRef - xC2, 2) + Math.pow(yRef - yC2, 2));
        abx /= dprim;
        aby /= dprim;
        double d = Math.sqrt(Math.pow(xRef - xC1, 2) + Math.pow(yRef - yC1, 2));
        abx *= d;
        aby *= d;
        controle2.setLayoutX(xRef + abx);
        controle2.setLayoutY(yRef + aby);
    }

    public Dents(Dents d) {
        super(est_plat);
        //this.liste_cercle = d.liste_cercle;
        //this.liste_cercle_controle = d.getListe_cercle_controle();
        affichage_coord_liste(d.getListe_cercle());
        copie_Coordonnee(d.liste_cercle,d.liste_cercle_controle);
        affichage_coord_liste(d.getListe_cercle());
        //cercle_Vers_Courbe();

    }
    private void affichage_coord_liste(ArrayList<Circle> liste) {
        int cpt = 0;
        System.out.println("voy a affichar les coord de position morray");
        for (Circle circle : liste) {
            System.out.println("indice : "+ cpt++  +" coord X :" + circle.getCenterX() + " coordY : " + circle.getCenterY());
        }
    }









    //cree des courbes a partir des 2 listes de cercles
    private void cercle_Vers_Courbe() {
        for (int i = 0; i < this.liste_cubicCurveTo.size() ; i++) {

            this.liste_cubicCurveTo.get(i).setX(this.liste_cercle.get(i).getLayoutX());
            this.liste_cubicCurveTo.get(i).setY(this.liste_cercle.get(i).getLayoutY());

            this.liste_cubicCurveTo.get(i).controlX1Property().bind(this.liste_cercle_controle.get(2*i+1).layoutXProperty() );
            this.liste_cubicCurveTo.get(i).controlY1Property().bind(this.liste_cercle_controle.get(2*i+1).layoutYProperty());

            this.liste_cubicCurveTo.get(i).controlX2Property().bind(this.liste_cercle_controle.get(2*i).layoutXProperty());
            this.liste_cubicCurveTo.get(i).controlY2Property().bind(this.liste_cercle_controle.get(2*i).layoutYProperty());

            this.liste_Moveto.get(i).setX(this.liste_cercle.get(i+1).getLayoutX());
            this.liste_Moveto.get(i).setY(this.liste_cercle.get(i+1).getLayoutY());

            notre_path.getElements().add(this.liste_Moveto.get(i));
            notre_path.getElements().add(this.liste_cubicCurveTo.get(i));
        }
    }

    // affecte aux cercles des listes de cercles de this les memes coordonnees que les
    //cercles des listes passées en parametre
    private void copie_Coordonnee(ArrayList<Circle> liste_cercle,ArrayList<Circle> liste_controleurs) {
        for (int i = 0; i < liste_cercle.size(); i++) {
            Circle c = this.liste_cercle.get(i);
            c.setLayoutY(liste_cercle.get(i).getLayoutY());
            c.setLayoutX(liste_cercle.get(i).getLayoutX());
        }
        for (int i = 0; i < liste_cercle_controle.size() ; i++) {
            Circle c = this.liste_cercle_controle.get(i);
            c.setLayoutY(liste_controleurs.get(i).getLayoutY());
            c.setLayoutX(liste_controleurs.get(i).getLayoutX());
        }
    }

}
