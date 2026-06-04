import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Tp1 {
    /*
    Pas certain de comprendre en quoi l'énoncé suivant est un problème : 
    Si le nombre de boîtes demandé est plus petit que la somme des boîtes disponibles dans les entrepôts :
    • Sois-vous lancez une exception contenant un message approprié; 
    • Sois-vous ignorez le nombre de boîtes demandé et vous mettez toutes les boîtes disponibles dans le camion. 
    */


    //Fonction de distance.
    public static double DHaversine(double[] CoordCamion, double[] CoordPoint) {
        double lat1 = CoordCamion[0]*Math.PI/180;
        double long1 = CoordCamion[1]*Math.PI/180;
        double lat2 = CoordPoint[0]*Math.PI/180;
        double long2 = CoordPoint[1]*Math.PI/180;

        double r = 6371000; //Rayon de la terre en mètres. 
        double d = 2*r*Math.asin(Math.sqrt(Math.pow(Math.sin((lat2-lat1)/2),2) + Math.cos(lat1)*Math.cos(lat2)*Math.pow(Math.sin((long2-long1)/2),2)));

        return Math.floor(d*100)/100; //Arrondi
    }

    //Tri simple
    public static Cargaisons[] TriParInsertion(Cargaisons[] liste) {
        Cargaisons x;
        for(int i = 1;i<liste.length;i++) {
            x=liste[i];
            int j=i;
            while(j>0 && liste[j-1].compareTo(x) > 0) {
                liste[j] = liste[j-1];
                j--;
            }
            liste[j] = x;
        }
        return liste;
    }

    // Tri efficace
    static Cargaisons[] mergeSort(Cargaisons[] array, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
            mergeSort(array, left, middle);
            mergeSort(array, middle + 1, right);
            merge(array, left, middle, right);
        }
        return array;
    }

    private static void merge(Cargaisons[] array, int left, int middle, int right) {
        Cargaisons[] temp = new Cargaisons[right - left + 1];
        int i = left, j = middle + 1, k = 0;
        while (i <= middle && j <= right) {
            if (array[i].compareTo(array[j]) <= 0) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }
        while (i <= middle) {
            temp[k++] = array[i++];
        }
        while (j <= right) {
            temp[k++] = array[j++];
        }
        for (i = left, k = 0; i <= right; i++, k++) {
            array[i] = temp[k];
        }
    }


    public static void main(String[] args) {
        try{
            BufferedReader data = new BufferedReader(new FileReader(args[0]));
            BufferedWriter sortie = new BufferedWriter(new FileWriter(args[1]));
            
            String Firstline = data.readLine();
            String line;
            String[] tokens = Firstline.trim().split("\\s+");

            double distanceParcourue = 0;
            double[] TruckPosition = new double[2];
            int boitesDemande = Integer.parseInt(tokens[0]);
            int TruckContenance;

            if(tokens.length<2) {TruckContenance = 0;} 
            else { TruckContenance = Integer.parseInt(tokens[1]); }

            if(boitesDemande>TruckContenance) {
                throw new IllegalArgumentException("Trop de demandes par rapport à la contenance du camion.");
            } else {
            Cargaisons[] carg = new Cargaisons[2];
            int cargCount = 0;

            while((line = data.readLine()) != null) {

                if(line.trim().isEmpty()) {
                    continue;
                }

                String[] infos = line.trim().split("\\s+");


                for(int i = 0;i<infos.length-1;i+=2) {

                    if(cargCount == carg.length) {
                        carg = Arrays.copyOf(carg, carg.length*2); 
                    }

                    infos[i+1] = infos[i+1].replaceAll("[()]","");
                    String[] coordsStr = infos[i+1].split(",");

                    int boitesAct = Integer.parseInt(infos[i]);
                    double[] coordsAct = new double[] { Double.parseDouble(coordsStr[0]), Double.parseDouble(coordsStr[1]) };
                    
                    carg[cargCount] = new Cargaisons(boitesAct, coordsAct);
                    cargCount++;
                }
            }
            carg = Arrays.copyOf(carg, cargCount); 
            
            int maxBoites = 0;
            Cargaisons[] left = null;
            Cargaisons[] right = null;
            
            for(int i = 0;i<carg.length;i++) {    
                if(carg[i].nbsBoites > maxBoites) {
                    maxBoites = carg[i].nbsBoites;
                    TruckPosition=carg[i].coords;
                    left = Arrays.copyOfRange(carg,0,i);
                    right = Arrays.copyOfRange(carg,i+1,carg.length);
                } 
            }

            sortie.write("Truck position: ("+TruckPosition[0]+","+TruckPosition[1]+")");
            sortie.newLine();

            Cargaisons[] pos = new Cargaisons[carg.length-1]; 
            System.arraycopy(left,0,pos,0,left.length);
            System.arraycopy(right,0,pos,left.length,right.length);
            
            if(boitesDemande<=maxBoites) {
                sortie.write("Distance:"+(Math.floor(distanceParcourue*10)/10)+"    Number of boxes:"+(maxBoites-boitesDemande)+"   Position:("+TruckPosition[0]+","+TruckPosition[1]+")");
            }
            else {
                sortie.write("Distance:"+(Math.floor(distanceParcourue*10)/10)+"    Number of boxes:0   Position:("+TruckPosition[0]+","+TruckPosition[1]+")");
                sortie.newLine();
                boitesDemande-=maxBoites;

                while(boitesDemande>0 && pos.length > 0) {
                                        
                    for(int i = 0;i<pos.length;i++) {
                        pos[i].distance = DHaversine(TruckPosition, pos[i].coords);
                    }

                    // Choix de la méthode de tri.

                    pos = TriParInsertion(pos);
                    //pos = mergeSort(pos, 0, pos.length - 1);
                    //Arrays.sort(pos);

                    int boitesPointCarg=pos[0].nbsBoites-TruckContenance; 
                    if(boitesPointCarg<0) {boitesPointCarg=0;}
                    
                    distanceParcourue+=pos[0].distance;
                    TruckPosition = pos[0].coords;
                    boitesDemande-=pos[0].nbsBoites;
                    
                    sortie.write("Distance:"+(Math.floor(distanceParcourue*10)/10)+"    Number of boxes:"+boitesPointCarg+"   Position:("+TruckPosition[0]+","+TruckPosition[1]+")");
                    sortie.newLine();
                    
                    pos = Arrays.copyOfRange(pos, 1, pos.length);
                }
            }

            data.close();
            sortie.close();
        }
            } catch(FileNotFoundException e) {
                System.out.println("Erreur (FileNotFoundException) : " + e.getMessage());
            
            } catch(IOException f) {
                System.out.println("Erreur (IOException) : " + f.getMessage());
            } catch(IllegalArgumentException g) {
                System.out.println("Erreur (IllegalArgumentException) : " + g.getMessage());
                throw g;
            }
    }
}


class Cargaisons implements Comparable<Cargaisons> {
    int nbsBoites;
    double[] coords = new double[2]; 
    double distance;

    public Cargaisons(int nbsBoites, double[] coords) {
        this.nbsBoites = nbsBoites;
        this.coords = coords;
        this.distance = 0.0; // CORRECTION 3 : Évite l'auto-affectation inutile
    }

    @Override
    public int compareTo(Cargaisons c) {
        if(this.distance != c.distance) {
            return Double.compare(this.distance, c.distance);
        }
        if(this.coords[0] != c.coords[0]) {
            return Double.compare(this.coords[0], c.coords[0]);
        }
        return Double.compare(this.coords[1], c.coords[1]);
    }
    
    @Override
    public String toString() {
        return "Cargaisons = " + "\n" + "Boites : " + nbsBoites + "\n" + "Coordonnées du point : " + Arrays.toString(coords) + "\n";
    }
}
