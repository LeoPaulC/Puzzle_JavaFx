package sample;

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
        /**
         * on place le 6eme cercle sur la meme ligne que le premier et decalé de "LA largeur d'une piece" .
         */
        this.liste_cercle.get(6).setLayoutX(this.liste_cercle.get(0).getLayoutX());
        this.liste_cercle.get(6).setLayoutY(this.TAILLE_COTE_PIECE_LONGUEUR);

        /**
         * Il nous reste a disposer les 5 points restant , à savoir du 1 à 5 .
         */

        tmp = rand.nextInt(200);
        for (int i = 1; i < NB_CERCLE_PAR_BORDURE-1 ; i++) {
            this.liste_cercle.get(0).setLayoutX();

        }

    }

    Dents(Creux creux){ // Aléatoire complet
        super(false);
    }



}
