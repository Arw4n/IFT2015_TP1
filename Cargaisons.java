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

    // COMPLEXITÉ: O(1) => exécute un nbre fixe de constantes, indépendamment de la taille des données d'entrée
    @Override
    public int compareTo(Cargaisons c) {
        // Utilisation de comparaisons directes (<, >) pour éviter les approximations de virgule flottante
        
        // Plus proche distance.
        if (this.distance < c.distance) {
            return -1; // O(1)
        }
        if (this.distance > c.distance) {
            return 1; // O(1)
        }

        // Plus petite latitude.
        if (this.coords[0] < c.coords[0]) {
            return -1; // O(1)
        }
        if (this.coords[0] > c.coords[0]) {
            return 1; // O(1)
        }

        // Plus petite longitude.
        if (this.coords[1] < c.coords[1]) {
            return -1; // O(1)
        }
        if (this.coords[1] > c.coords[1]) {
            return 1; // O(1)
        }

        return 0; // O(1)
    }
    
    @Override
    public String toString() {
        return "Cargaisons = " + "\n" + "Boites: " + nbsBoites + "\n" + "Coordonnées du point: " + Arrays.toString(coords) + "\n";
    }
}
