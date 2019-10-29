package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;

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
        this.liste_cercle.get(0).setLayoutX(200);
        this.liste_cercle.get(0).setLayoutY(300);
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
                    tmp = rand.nextInt(this.TAILLE_COTE_PIECE_LONGUEUR); // nb aleatoire en longueur
                    this.liste_cercle.get(i).setLayoutX(this.liste_cercle.get(0).getLayoutX()+tmp); // on decale la position x1 de la piece 1 de ''tmp'' par rapport a x0
                     a = (int)(this.TAILLE_COTE_PIECE_HAUTEUR * 0.3);
                    tmp = rand.nextInt(a); // nb aleatoire en longueur
                    this.liste_cercle.get(i).setLayoutY(this.liste_cercle.get(0).getLayoutY()-tmp);
                    this.liste_cercle.get(i).setFill(Color.ORANGE);
                    break ;
                case 2 :
                    tmp = rand.nextInt(this.TAILLE_COTE_PIECE_LONGUEUR); // nb aleatoire en longueur
                    // pour eviter de placer x2 trop pres du bord droit de la piece
                    while ( tmp > this.TAILLE_COTE_PIECE_LONGUEUR-MIN_TAILLE)
                        tmp = rand.nextInt(this.TAILLE_COTE_PIECE_LONGUEUR);

                    this.liste_cercle.get(i).setLayoutX(this.liste_cercle.get(0).getLayoutX()+ tmp);
                    a = (int)(this.TAILLE_COTE_PIECE_HAUTEUR * 0.3);
                    tmp = rand.nextInt((int) ( a - ( this.liste_cercle.get(i-2).getLayoutY() - this.liste_cercle.get(i-1).getLayoutY() ) )); // nb aleatoire en longueur
                    this.liste_cercle.get(i).setLayoutY(this.liste_cercle.get(i-1).getLayoutY()-tmp);
                    this.liste_cercle.get(i).setFill(Color.RED);
                    break ;
                case 3 :
                    tmp = rand.nextInt(this.TAILLE_COTE_PIECE_LONGUEUR); // nb aleatoire en longueur
                    // pour eviter de placer x2 trop pres du bord droit de la piece
                    int minX = (int) Math.min( liste_cercle.get(1).getLayoutX() , liste_cercle.get(2).getLayoutX() );

                    while ( tmp < this.liste_cercle.get(6).getLayoutX() - minX)
                        tmp = rand.nextInt(this.TAILLE_COTE_PIECE_LONGUEUR);
                    this.liste_cercle.get(i).setLayoutX(this.liste_cercle.get(0).getLayoutX()+ tmp);

                    a = (int)(this.TAILLE_COTE_PIECE_HAUTEUR * 0.3);
                    tmp = rand.nextInt((int) ( a - ( this.liste_cercle.get(i-3).getLayoutY() - this.liste_cercle.get(i-1).getLayoutY() ) )); // nb aleatoire en longueur
                    this.liste_cercle.get(i).setLayoutY(this.liste_cercle.get(i-1).getLayoutY()-tmp);
                    this.liste_cercle.get(i).setFill(Color.PINK);
                    break;
                case 4 :
                    int maxX = (int) Math.max(this.liste_cercle.get(i - 1).getLayoutX(), this.liste_cercle.get(i - 2).getLayoutX());
                    maxX = (int) Math.max(maxX, this.liste_cercle.get(i - 3).getLayoutX());
                    tmp = rand.nextInt((int) (this.liste_cercle.get(6).getLayoutX() - maxX)); // nb aleatoire en longueur
                    while ( tmp >= this.liste_cercle.get(6).getLayoutX() - this.liste_cercle.get(3).getLayoutX()) {
                        tmp = rand.nextInt((int) (this.liste_cercle.get(6).getLayoutX() - maxX)); // nb aleatoire en longueur
                    }
                    // pour eviter de placer x2 trop pres du bord droit de la piece

                    this.liste_cercle.get(i).setLayoutX(maxX + tmp);

                    a = (int)(this.TAILLE_COTE_PIECE_HAUTEUR * 0.3);
                    while ( tmp >= this.liste_cercle.get(6).getLayoutY() - this.liste_cercle.get(3).getLayoutY())
                        tmp = rand.nextInt(this.TAILLE_COTE_PIECE_HAUTEUR-a);
                    //tmp = rand.nextInt((int) ( a - ( this.liste_cercle.get(i-3).getLayoutY() - this.liste_cercle.get(i-1).getLayoutY() ) )); // nb aleatoire en longueur

                    this.liste_cercle.get(i).setLayoutY(this.liste_cercle.get(i-1).getLayoutY()+tmp);
                    this.liste_cercle.get(i).setFill(Color.PURPLE);

                    break ;
                case 5 :
                    int maxX1 = (int) Math.max(this.liste_cercle.get(i - 2).getLayoutX(), this.liste_cercle.get(i - 3).getLayoutX());
                    maxX1 = (int) Math.max(maxX1, this.liste_cercle.get(i - 4).getLayoutX());
                    tmp = rand.nextInt((int) (this.liste_cercle.get(6).getLayoutX() - maxX1)); // nb aleatoire en longueur
                    while ( tmp >= this.liste_cercle.get(6).getLayoutX() - this.liste_cercle.get(3).getLayoutX()) {
                        tmp = rand.nextInt((int) (this.liste_cercle.get(6).getLayoutX() - maxX1)); // nb aleatoire en longueur
                    }
                    // pour eviter de placer x2 trop pres du bord droit de la piece

                    this.liste_cercle.get(i).setLayoutX(maxX1 + tmp);

                    a = (int)(this.TAILLE_COTE_PIECE_HAUTEUR * 0.3);
                    while ( tmp >= this.liste_cercle.get(6).getLayoutY() - this.liste_cercle.get(4).getLayoutY())
                        tmp = rand.nextInt(this.TAILLE_COTE_PIECE_HAUTEUR-a);
                    //tmp = rand.nextInt((int) ( a - ( this.liste_cercle.get(i-3).getLayoutY() - this.liste_cercle.get(i-1).getLayoutY() ) )); // nb aleatoire en longueur

                    this.liste_cercle.get(i).setLayoutY(this.liste_cercle.get(i-1).getLayoutY()+tmp);
                    this.liste_cercle.get(i).setFill(Color.DARKBLUE);

                    break ;
                default:
                    // Inutile donc utile a mettre :)
                    break ;
            }

        }

    }

    Dents(Creux creux){ // Aléatoire complet
        super(false);
    }



}
