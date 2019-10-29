package sample;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class Controller implements EventHandler<MouseEvent> {
    private Circle[]tab;
    private int finalI;
    public Controller (Circle[]tab,int fini) {
        this.tab=tab;
        finalI=fini;

    }
    @Override
    public void handle(MouseEvent e) {

        double dx = e.getX()-tab[finalI].getCenterX();
        double dy = e.getY()-tab[finalI].getCenterY();



        //definir les nouveau vecteurs tq ab et bc sont colinéaires

        tab[finalI].setCenterX(e.getX());
        tab[finalI].setCenterY(e.getY());
        //coordonées de vecteurs
        /*double abx =tab[2].getCenterX()-tab[3].getCenterX();
        double aby =tab[2].getCenterY()-tab[3].getCenterY();
        double bcx=tab[2].getCenterX()-tab[4].getCenterX();
        double bcy=tab[2].getCenterY()-tab[4].getCenterY();*/


        if(finalI==2){
            double abx=tab[3].getCenterX()-tab[2].getCenterX();
            double aby=tab[3].getCenterY()-tab[2].getCenterY();
            double dprim=Math.sqrt(Math.pow(tab[3].getCenterX()-tab[2].getCenterX(),2)+Math.pow(tab[3].getCenterY()-tab[2].getCenterY(),2));
            abx/=dprim;
            aby/=dprim;
            double d=Math.sqrt(Math.pow(tab[3].getCenterX()-tab[4].getCenterX(),2)+Math.pow(tab[3].getCenterY()-tab[4].getCenterY(),2));
            abx*=d;
            aby*=d;
                    /*
                    tab[4].setCenterY(2*tab[3].getCenterY()-tab[2].getCenterY());
                    tab[4].setCenterX(2*tab[3].getCenterX()-tab[2].getCenterX());*/
            tab[4].setCenterY(tab[3].getCenterY()+aby);
            tab[4].setCenterX(tab[3].getCenterX()+abx);

        }

        if(finalI==4){
            //division par 0 si les cercles se rencontrent
            double abx=tab[3].getCenterX()-tab[4].getCenterX();
            double aby=tab[3].getCenterY()-tab[4].getCenterY();
            double dprim=Math.sqrt(Math.pow(tab[3].getCenterX()-tab[4].getCenterX(),2)+Math.pow(tab[3].getCenterY()-tab[4].getCenterY(),2));
            abx/=dprim;
            aby/=dprim;
            double d=Math.sqrt(Math.pow(tab[3].getCenterX()-tab[2].getCenterX(),2)+Math.pow(tab[3].getCenterY()-tab[2].getCenterY(),2));
            abx*=d;
            aby*=d;
            tab[2].setCenterY(tab[3].getCenterY()+aby);
            tab[2].setCenterX(tab[3].getCenterX()+abx);
            /*
                    tab[2].setCenterY(2*tab[3].getCenterY()-tab[4].getCenterY());
                    tab[2].setCenterX(2*tab[3].getCenterX()-tab[4].getCenterX());

             */
        }

        //Reste inchangé
        if(finalI==3){
            tab[2].setCenterY(tab[2].getCenterY()+dy);
            tab[2].setCenterX(tab[2].getCenterX()+dx);
            tab[4].setCenterY(tab[4].getCenterY()+dy);
            tab[4].setCenterX(tab[4].getCenterX()+dx);
        }


    }
}