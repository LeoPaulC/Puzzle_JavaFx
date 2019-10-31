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

    Dents(){ // Aléatoire complet
        super(false);
        rand = new Random();


        /**
         * pour nos tets on va placer le premier point au coordonne 200,300
         */
        this.liste_cercle.get(0).setLayoutX(TAILLE_COTE_PIECE_LONGUEUR);
        this.liste_cercle.get(0).setLayoutY(TAILLE_COTE_PIECE_HAUTEUR);
        this.liste_cercle.get(0).setFill(Color.GOLD);
        /**
         * on place le 6eme cercle sur la meme ligne que le premier et decalé de "LA largeur d'une piece" .
         */
        this.liste_cercle.get(6).setLayoutX(this.liste_cercle.get(0).getLayoutX() + this.TAILLE_COTE_PIECE_LONGUEUR );
        this.liste_cercle.get(6).setLayoutY(this.liste_cercle.get(0).getLayoutY() );

        this.liste_cercle.get(6).setFill(Color.GOLD);
        /**
         * Il nous reste a disposer les 5 points restant , à savoir du 1 à 5 .
         */
        //tmp = rand.nextInt(200);
        int a ;
        for (int i = 1; i < NB_CERCLE_PAR_BORDURE-1 ; i++) {
            switch (i){
                case 1 :
                    // cercle N° 1
                    //tmp = rand.nextInt(this.TAILLE_COTE_PIECE_LONGUEUR); // nb aleatoire en longueur
                    this.liste_cercle.get(i).setLayoutX(this.liste_cercle.get(0).getLayoutX()+ TAILLE_COTE_PIECE_LONGUEUR*0.4); // on decale la position x1 de la piece 1 de ''tmp'' par rapport a x0
                    // a = (int)(this.TAILLE_COTE_PIECE_HAUTEUR * 0.3);
                    //tmp = rand.nextInt(a); // nb aleatoire en longueur
                    this.liste_cercle.get(i).setLayoutY(this.liste_cercle.get(0).getLayoutY()-TAILLE_COTE_PIECE_HAUTEUR*0.08);
                    this.liste_cercle.get(i).setFill(Color.ORANGE);
                    break ;
                case 2 :
                    /*tmp = rand.nextInt(this.TAILLE_COTE_PIECE_LONGUEUR); // nb aleatoire en longueur
                    // pour eviter de placer x2 trop pres du bord droit de la piece
                    while ( tmp > this.TAILLE_COTE_PIECE_LONGUEUR-MIN_TAILLE)
                        tmp = rand.nextInt(this.TAILLE_COTE_PIECE_LONGUEUR);


                     */
                    this.liste_cercle.get(i).setLayoutX(this.liste_cercle.get(0).getLayoutX()+ TAILLE_COTE_PIECE_LONGUEUR * 0.36);
                    //a = (int)(this.TAILLE_COTE_PIECE_HAUTEUR * 0.3);
                    //a = (int)(this.TAILLE_COTE_PIECE_HAUTEUR);
                    //tmp = rand.nextInt((int) ( a - ( this.liste_cercle.get(i-2).getLayoutY() - this.liste_cercle.get(i-1).getLayoutY() ) )); // nb aleatoire en longueur
                    this.liste_cercle.get(i).setLayoutY(this.liste_cercle.get(0).getLayoutY() - TAILLE_COTE_PIECE_HAUTEUR * 0.26);
                    this.liste_cercle.get(i).setFill(Color.RED);
                    break ;
                case 3 :
                    //tmp = rand.nextInt(this.TAILLE_COTE_PIECE_LONGUEUR); // nb aleatoire en longueur
                    // pour eviter de placer x2 trop pres du bord droit de la piece
                    //int minX = (int) Math.min( liste_cercle.get(1).getLayoutX() , liste_cercle.get(2).getLayoutX() );

                    //while ( tmp < this.liste_cercle.get(6).getLayoutX() - minX)
                      //  tmp = rand.nextInt(this.TAILLE_COTE_PIECE_LONGUEUR);

                    // On fixe le x3
                    this.liste_cercle.get(i).setLayoutX( liste_cercle.get(0).getLayoutX() + (  ( liste_cercle.get(6).getLayoutX() - liste_cercle.get(0).getLayoutX() ) /2 ) ) ;

                    //a = (int)(this.TAILLE_COTE_PIECE_HAUTEUR * 0.3);
                    //a = (int)(this.TAILLE_COTE_PIECE_HAUTEUR);
                    //tmp = rand.nextInt((int) ( a - ( this.liste_cercle.get(i-3).getLayoutY() - this.liste_cercle.get(i-1).getLayoutY() ) )); // nb aleatoire en longueur
                    this.liste_cercle.get(i).setLayoutY(liste_cercle.get(0).getLayoutY() - TAILLE_COTE_PIECE_HAUTEUR * 0.4);
                    this.liste_cercle.get(i).setFill(Color.PINK);
                    break;
                case 4 :
                    /*int maxX = (int) Math.max(this.liste_cercle.get(i - 1).getLayoutX(), this.liste_cercle.get(i - 2).getLayoutX());
                    maxX = (int) Math.max(maxX, this.liste_cercle.get(i - 3).getLayoutX());
                    tmp = rand.nextInt((int) (this.liste_cercle.get(6).getLayoutX() - maxX)); // nb aleatoire en longueur
                    while ( tmp >= this.liste_cercle.get(6).getLayoutX() - this.liste_cercle.get(3).getLayoutX()) {
                        tmp = rand.nextInt((int) (this.liste_cercle.get(6).getLayoutX() - maxX)); // nb aleatoire en longueur
                    }
                    // pour eviter de placer x2 trop pres du bord droit de la piece

                     */

                    this.liste_cercle.get(i).setLayoutX(liste_cercle.get(6).getLayoutX() - TAILLE_COTE_PIECE_LONGUEUR*0.36);

                    //a = (int)(this.TAILLE_COTE_PIECE_HAUTEUR * 0.3);

                    /*
                    a = (int)(this.TAILLE_COTE_PIECE_HAUTEUR);
                    while ( tmp >= this.liste_cercle.get(6).getLayoutY() - this.liste_cercle.get(3).getLayoutY())
                        tmp = rand.nextInt(this.TAILLE_COTE_PIECE_HAUTEUR-a);
                    //tmp = rand.nextInt((int) ( a - ( this.liste_cercle.get(i-3).getLayoutY() - this.liste_cercle.get(i-1).getLayoutY() ) )); // nb aleatoire en longueur


                     */
                    this.liste_cercle.get(i).setLayoutY(this.liste_cercle.get(6).getLayoutY()- TAILLE_COTE_PIECE_HAUTEUR * 0.26);
                    this.liste_cercle.get(i).setFill(Color.PURPLE);

                    break ;
                case 5 :
                    /*int maxX1 = (int) Math.max(this.liste_cercle.get(i - 2).getLayoutX(), this.liste_cercle.get(i - 3).getLayoutX());
                    maxX1 = (int) Math.max(maxX1, this.liste_cercle.get(i - 4).getLayoutX());
                    tmp = rand.nextInt((int) (this.liste_cercle.get(6).getLayoutX() - maxX1)); // nb aleatoire en longueur
                    while ( tmp >= this.liste_cercle.get(6).getLayoutX() - this.liste_cercle.get(3).getLayoutX()) {
                        tmp = rand.nextInt((int) (this.liste_cercle.get(6).getLayoutX() - maxX1)); // nb aleatoire en longueur
                    }
                    // pour eviter de placer x2 trop pres du bord droit de la piece

                     */

                    this.liste_cercle.get(i).setLayoutX(liste_cercle.get(6).getLayoutX() - TAILLE_COTE_PIECE_LONGUEUR * 0.4);

                    /*a = (int)(this.TAILLE_COTE_PIECE_HAUTEUR * 0.3);
                    while ( tmp >= this.liste_cercle.get(6).getLayoutY() - this.liste_cercle.get(4).getLayoutY())
                        tmp = rand.nextInt(this.TAILLE_COTE_PIECE_HAUTEUR-a);
                    //tmp = rand.nextInt((int) ( a - ( this.liste_cercle.get(i-3).getLayoutY() - this.liste_cercle.get(i-1).getLayoutY() ) )); // nb aleatoire en longueur


                     */
                    this.liste_cercle.get(i).setLayoutY(this.liste_cercle.get(6).getLayoutY() - TAILLE_COTE_PIECE_HAUTEUR*0.08);
                    this.liste_cercle.get(i).setFill(Color.DARKBLUE);

                    break ;
                default:
                    // Inutile donc utile a mettre :)
                    break ;
            }

        }

        for (int i = 0; i < this.liste_cercle_controle.size(); i++) { // car le spoints de controle sont cree 2 à 2
            switch (i) {
                case 0:

                    this.liste_cercle_controle.get(i).setLayoutX(this.liste_cercle.get(i).getLayoutX()+TAILLE_COTE_PIECE_LONGUEUR*0.2);
                    this.liste_cercle_controle.get(i).setLayoutY(this.liste_cercle.get(i).getLayoutY());
                    //this.liste_cercle_controle.get(i).setFill(Color.GOLD);
                    break;
                case 1:
                    //tmp = rand.nextInt((int) (this.liste_cercle.get(1).getLayoutX() - this.liste_cercle.get(0).getLayoutX()));
                    this.liste_cercle_controle.get(i).setLayoutX(this.liste_cercle.get(0).getLayoutX()+TAILLE_COTE_PIECE_LONGUEUR*0.34);
                    //tmp = rand.nextInt((int) (this.liste_cercle.get(0).getLayoutY() - this.liste_cercle.get(1).getLayoutY()));
                    this.liste_cercle_controle.get(i).setLayoutY(this.liste_cercle.get(0).getLayoutY());

                    //this.liste_cercle_controle.get(i).setFill(Color.DARKCYAN);
                    break;
                case 2 :
                    this.liste_cercle_controle.get(i).setLayoutX(this.liste_cercle.get(0).getLayoutX()+TAILLE_COTE_PIECE_LONGUEUR*0.43);
                    this.liste_cercle_controle.get(i).setLayoutY(this.liste_cercle.get(0).getLayoutY() - TAILLE_COTE_PIECE_HAUTEUR * 0.12);
                    break ;
                case 3 :
                    /**
                     * a gauche de controle 2 et en haut de controle 2
                     */
//                    tmp = rand.nextInt((int) (this.liste_cercle_controle.get(2).getLayoutX() - this.liste_cercle.get(0).getLayoutX()));
                    this.liste_cercle_controle.get(i).setLayoutX(this.liste_cercle.get(0).getLayoutX() + TAILLE_COTE_PIECE_LONGUEUR*0.36);

                   // tmp = rand.nextInt((int) Math.abs(this.liste_cercle_controle.get(2).getLayoutY() - this.liste_cercle.get(2).getLayoutY()));
                    this.liste_cercle_controle.get(i).setLayoutY(this.liste_cercle.get(0).getLayoutY() - TAILLE_COTE_PIECE_HAUTEUR * 0.14);

                  //  this.liste_cercle_controle.get(i).setFill(liste_cercle.get(2).getFill());

                    break ;

                case 4 :
                    this.liste_cercle_controle.get(i).setLayoutX(this.liste_cercle.get(0).getLayoutX()+TAILLE_COTE_PIECE_LONGUEUR*0.36);
                    this.liste_cercle_controle.get(i).setLayoutY(this.liste_cercle.get(0).getLayoutY() - TAILLE_COTE_PIECE_HAUTEUR * 0.34);
                    break ;

                case 5 :
                    //tmp = rand.nextInt((int) Math.abs(this.liste_cercle_controle.get(3).getLayoutX() - this.liste_cercle.get(2).getLayoutX()));
                    this.liste_cercle_controle.get(i).setLayoutX(this.liste_cercle.get(0).getLayoutX() + TAILLE_COTE_PIECE_LONGUEUR * 0.42);

                    this.liste_cercle_controle.get(i).setLayoutY(this.liste_cercle.get(3).getLayoutY() );

                    break ;

                case 6:
                    this.liste_cercle_controle.get(i).setLayoutX(this.liste_cercle.get(0).getLayoutX()+TAILLE_COTE_PIECE_LONGUEUR*0.58);
                    this.liste_cercle_controle.get(i).setLayoutY(this.liste_cercle.get(0).getLayoutY() - TAILLE_COTE_PIECE_HAUTEUR * 0.40);
                    break ;


                case 7 :
//                    tmp = rand.nextInt((int) Math.abs(this.liste_cercle_controle.get(6).getLayoutX() - this.liste_cercle.get(4).getLayoutX()));
                    this.liste_cercle_controle.get(i).setLayoutX(this.liste_cercle.get(6).getLayoutX() - TAILLE_COTE_PIECE_LONGUEUR * 0.36 );

                   // tmp = rand.nextInt((int) Math.abs(this.liste_cercle_controle.get(4).getLayoutY() - this.liste_cercle.get(3).getLayoutY()));
                    this.liste_cercle_controle.get(i).setLayoutY(this.liste_cercle.get(6).getLayoutY() - TAILLE_COTE_PIECE_HAUTEUR * 0.34);

                    break ;
                case 8 :
                    this.liste_cercle_controle.get(i).setLayoutX(this.liste_cercle.get(6).getLayoutX() - TAILLE_COTE_PIECE_LONGUEUR*0.36);
                    this.liste_cercle_controle.get(i).setLayoutY(this.liste_cercle.get(6).getLayoutY() - TAILLE_COTE_PIECE_HAUTEUR * 0.16);
                    break ;
                case 9:
                    //tmp = rand.nextInt((int) Math.abs(this.liste_cercle_controle.get(3).getLayoutX() - this.liste_cercle.get(5).getLayoutX()));
                    this.liste_cercle_controle.get(i).setLayoutX(this.liste_cercle.get(6).getLayoutX() - TAILLE_COTE_PIECE_LONGUEUR * 0.43);

                    //tmp = rand.nextInt((int) Math.abs(this.liste_cercle_controle.get(5).getLayoutY() - this.liste_cercle.get(4).getLayoutY()));
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

               /* case 2 :

                    this.liste_cercle_controle.get(i).setLayoutX(rand.nextInt(1000));
                    this.liste_cercle_controle.get(i).setLayoutY(rand.nextInt(1000));
                    this.place_point(liste_cercle_controle.get(i-1),liste_cercle.get(i/2),this.liste_cercle_controle.get(i));
                    this.liste_cercle_controle.get(i).setFill(Color.DARKGOLDENROD);
                    break ;*/

                default:
                    /*this.liste_cercle_controle.get(i).setLayoutX(rand.nextInt(500));
                    this.liste_cercle_controle.get(i).setLayoutY(rand.nextInt(500));

                     */
                    System.out.println("i : " + i);
                    this.Place_Point_Symetriquement(liste_cercle_controle.get(i-1),liste_cercle.get(i/2),this.liste_cercle_controle.get(i), i);
                    this.liste_cercle_controle.get(i).setFill(Color.BLACK);
                    break;

            }

        }
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

        /*
        for (int i = 1; i < liste_cercle.size()-1 ; i++) {
            LineTo l = new LineTo();
            l.setX(liste_cercle.get(i).getLayoutX());
            l.setY(liste_cercle.get(i).getLayoutY());

            MoveTo mt = new MoveTo();
            mt.setX(liste_cercle_controle.get(2*i).getLayoutX());
            mt.setY(liste_cercle_controle.get(2*i).getLayoutY());

            notre_path.getElements().add(mt);
            notre_path.getElements().add(l);
        }

         */






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

    Dents(Creux creux){ // Aléatoire complet
        super(false);
    }



}
