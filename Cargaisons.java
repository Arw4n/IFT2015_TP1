// Nom: Ryan Ramaherison Mac Way Kit - Matricule: 2030 6738

import java.util.Arrays;

public class Cargaisons implements Comparable<Cargaisons> {
    int nbsBoites;
    double[] coords = new double[2]; 
    double distance;

    public Cargaisons(int nbsBoites, double[] coords) {
        this.nbsBoites = nbsBoites;
        this.coords = coords;
        this.distance = 0.0;
    }

    // Comparer les cargaisons en fonction de la distance, latitude, et longitude
    @Override
    public int compareTo(Cargaisons c) {
        // Utilisation de comparaisons avec (<, >) pour éviter les approximations de virgule flottante
        
        // Plus proche distance.
        if (this.distance < c.distance) {
            return -1;
        }
        if (this.distance > c.distance) {
            return 1;
        }

        // Plus petite latitude.
        if (this.coords[0] < c.coords[0]) {
            return -1;
        }
        if (this.coords[0] > c.coords[0]) {
            return 1;
        }

        // Plus petite longitude.
        if (this.coords[1] < c.coords[1]) {
            return -1;
        }
        if (this.coords[1] > c.coords[1]) {
            return 1;
        }

        return 0;
    }
    
    @Override
    public String toString() {
        return "Cargaisons = " + "\n" + "Boites: " + nbsBoites + "\n" + "Coordonnées du point: " + Arrays.toString(coords) + "\n";
    }
}
