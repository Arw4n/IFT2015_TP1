import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Tp1 {
    /*
    Input : nombre total de boîtes à transporter, capacité maximale d'un camion, positions des 
    bâtiments d'entreposage impliqués dans la cargaison courante, nombre de boîtes disponibles
    à chaque point de cargaison

    Travail : 
    1. Rechercher un point de service possédant le plus grand nombre de boîtes
        Coordonnées du bâtiment deviennent celles du camion
    
    2. Pour chaque cargaison :
        Afficher les positions de bâtiments d'entreposage situés à la distance la plus proche de la position du camion
            Si distances égales, prendre celui avec la latitude la plus basse
            Si latitudes égales, prendre la plus petite longitude
        Afficher le nombre de boîtes restantes au point de service

    Calculs de sitance avec la Formule de havresine : 
    https://fr.wikipedia.org/wiki/Formule_de_haversine

    POUR LES MÉTHODES DE TRI, IL FAUT EN FAIRE 2 :
    1. Simple
    2. Efficace
    3. sort de java

    Output :
    Truck position: (initial)
    Distance:   Number of boxes: (au point de service visité)   Position:(du point de service)
    */


    //Fonction de distance.
    public Double DHaversine(double[] CoordCamion, double[] CoordPoint) {
        double lat1 = CoordCamion[0]*Math.PI/180;
        double long1 = CoordCamion[1]*Math.PI/180;
        double lat2 = CoordPoint[0]*Math.PI/180;
        double long2 = CoordPoint[1]*Math.PI/180;

        double r = 6371000; //Rayon de la terre en mètres. 
        double d = 2*r*Math.asin(Math.sqrt(Math.pow(Math.sin((lat2-lat1)/2),2) + Math.cos(lat1)*Math.cos(lat2)*Math.pow(Math.sin((long2-long1)/2),2)));

        //System.out.println("r = " + r);
        //System.out.println("h = " + d);
        return Math.floor(d*100)/100;
    }



    public void main(String[] args) {
        double[] posCamion = {45.4383,-73.8205};
        double[] posPoint = {45.4977,-73.714};
        double[] posPoint2 = {45.5092,-73.5682};

        Double distance = DHaversine(posCamion,posPoint);
        Double distance2 = DHaversine(posCamion,posPoint2);

        System.out.println(distance2);
        try{
            BufferedReader donnees = new BufferedReader(new FileReader(args[0]));

            String line;
            



            donnees.close();
        } catch(FileNotFoundException e) {
            System.out.println("Erreur (FileNotFoundException) : " + e.getMessage());
        } catch(IOException f) {
            System.out.println("Erreur (IOException) : " + f.getMessage());
        }
        
    }
}