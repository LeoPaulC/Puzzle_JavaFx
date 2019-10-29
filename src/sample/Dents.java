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
        for (int i = 1; i < NB_CERCLE_PAR_BORDURE-1 ; i++) {
            switch (i){
                case 1 :
                    // cercle N° 1
                    tmp = rand.nextInt(this.TAILLE_COTE_PIECE_LONGUEUR); // nb aleatoire en longueur
                    this.liste_cercle.get(i).setLayoutX(this.liste_cercle.get(0).getLayoutX()+tmp); // on decale la position x1 de la piece 1 de ''tmp'' par rapport a x0
                    tmp = rand.nextInt(this.TAILLE_COTE_PIECE_HAUTEUR); // nb aleatoire en longueur
                    this.liste_cercle.get(i).setLayoutY(this.liste_cercle.get(0).getLayoutY()-tmp);
                    this.liste_cercle.get(i).setFill(Color.PINK);
                    break ;
                case 2 :

                    break ;
                case 3 :
                    break;
                case 4 :
                    break ;
                case 5 :
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
