// Nom: Ryan Ramaherison Mac Way Kit - Matricule: 2030 6738
// Nom : Arnaud Mehrabi - Matricule : 20302443

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

// Programme de gestion de cargaisons pour un camion de livraison
public class Tp1 {
    // Fonction de distance de Haversine pour calculer la distance 
    // entre 2 points géographiques par leurs coordonnées latitude/longitude
    
    // COMPLEXITÉ: O(1) - (constant) car toutes les opérations arithmétiques 
    // internes s'exécutent en temps fixe indépendant de n
    public static double DHaversine(double[] CoordCamion, double[] CoordPoint) {
        // Conversion des coordonnées de degrés à radians pour les calculs trigonométriques
        double lat1 = CoordCamion[0]*Math.PI/180; // O(1)
        double long1 = CoordCamion[1]*Math.PI/180; // O(1)
        double lat2 = CoordPoint[0]*Math.PI/180; // O(1)
        double long2 = CoordPoint[1]*Math.PI/180; // O(1)

        double r = 6371000; // rayon de la terre (en mètres)
        double distance = 2*r*Math.asin(
            Math.sqrt(
                Math.pow(
                    Math.sin((lat2-lat1)/2),2
                ) + Math.cos(lat1)*Math.cos(lat2)*Math.pow(
                    Math.sin((long2-long1)/2),2
                )
            )
        ); // O(1) => formule de trigo de Haversine

        // Utiliser la distance brute pour que "compareTo()" trie 
        // avec max précision sans bugs d'arrondis
        return distance;
    }

    // Tri simple: Insertion Sort (option 1)
    // COMPLEXITÉ: O(n²) - (quadratique) 
    public static Cargaisons[] insertionSort(Cargaisons[] liste) {
        Cargaisons temp; // O(1) => tableau d'espace temporaire
        int n = liste.length; // O(1) => taille de la liste
        
        for(int i = 1; i<n; i++) { // O(n) => s'exécute (n - 1) fois
            temp=liste[i]; // O(1) => sélectionner l'élément à insérer
            int j=i; // O(1) => index de comparaison arrière
            
            while(j>0 && liste[j-1].compareTo(temp) > 0) { // O(i) => s'exécute au max i fois
                liste[j] = liste[j-1]; // O(1) => déplacer l'élément le plus grand vers la droite
                j--; // O(1)
            }
            
            liste[j] = temp; // O(1) => insérer l'élément à son emplacement trié
        }
        
        return liste; // O(1)
    }

    // Tri efficace: Merge Sort (option 2)
    // COMPLEXITÉ: O(n log n) - (linéarithmique)
    public static Cargaisons[] mergeSort(Cargaisons[] array, int left, int right) {
        if (left < right) { // O(1) => condition de terminaison de la récursion binaire
            int middle = (left + right) / 2; // O(1) => calcul du pivot du milieu
            
            mergeSort(array, left, middle); // O(log n) => division récursive de la moitié gauche
            mergeSort(array, middle + 1, right); // O(log n) => division récursive de la moitié droite
            
            merge(array, left, middle, right); // O(n) => fusion linéaire des 2 moitiés triées
        }
        
        return array; // O(1)
    }

    // Fusionner les 2 moitiés triées du tableau d'origine
    // COMPLEXITÉ: O(n) - (linéaire) où n = right - left + 1 (nbre d'éléments à fusionner)
    private static void merge(Cargaisons[] array, int left, int middle, int right) {
        Cargaisons[] temp = new Cargaisons[right - left + 1]; // O(n) => tableau d'espace temporaire
        int i = left, j = middle + 1, k = 0; // O(1) => 3 index de parcours
        
        while (i <= middle && j <= right) { // O(n) => s'exécute au max (right - left + 1) fois
            if (array[i].compareTo(array[j]) <= 0) { // O(1) => vérifier l'ordre de tri
                temp[k++] = array[i++]; // O(1) => copier l'élément de la sous-liste gauche
            } else {
                temp[k++] = array[j++]; // O(1) => copier l'élément de la sous-liste droite
            }
        }
        
        while (i <= middle) { // O(n) (pire cas) => copier les éléments restants de la sous-liste gauche
            temp[k++] = array[i++]; // O(1)
        }
        while (j <= right) { // O(n) (pire cas) => copier les éléments restants de la sous-liste droite
            temp[k++] = array[j++]; // O(1)
        }
        
        // Recopier les éléments ordonnés du tableau temporaire vers le tableau d'origine
        for (i = left, k = 0; i <= right; i++, k++) { // O(n) => s'exécute exactement n fois
            array[i] = temp[k]; // O(1)
        }
    }

    // Fonction principale d'exécution du programme
    public static void main(String[] args) {
        // Vérifier le format des arguments d'entrée 
        if (args.length < 2) { // si nbre d'arguments insuffisant pour l'exécution
            System.out.println("Use: java Tp1 input.txt output.txt [n_optional]");
            return;
        }

        // Lecture du 3eme argument optionnel n pour choisir l'algo de tri
        // Par défaut, n = 2 (mergeSort): algo de tri le plus efficace du programme
        int triChoix = 2;
        
        if (args.length >= 3) { // Vérification de la présence de l'argument optionnel
            try {
                int parsedTriChoix = Integer.parseInt(args[2]); // conversion textuelle en entier
                if (parsedTriChoix >= 1 && parsedTriChoix <= 3) { // vérification de la validité du choix
                    triChoix = parsedTriChoix;
                }
            } catch (NumberFormatException e) {
                // Si args[2] n'est pas un nombre, on conserve la valeur par défaut
            }
        }

        try {
            BufferedReader data = new BufferedReader(new FileReader(args[0])); // pour lire les inputs
            BufferedWriter sortie = new BufferedWriter(new FileWriter(args[1])); // pour écrire les outputs
            
            String Firstline = data.readLine(); // lire la ligne d'en-tête de configuration
            if (Firstline == null) { // si fichier vide
                data.close();
                sortie.close();
                return;
            }

            String line;
            String[] config = Firstline.trim().split("\\s+"); // extraire les données de la ligne d'en-tête

            double distanceParcourue = 0;
            double[] positionCamion = new double[2];
            int boitesDemande = Integer.parseInt(config[0]);
            int capaciteCamion;

            if(config.length<2) { // si capacité du camion non spécifiée dans la ligne 1 de l'input
                capaciteCamion = 0; // camion déjà plein (capacité restante = 0)
            } else { 
                capaciteCamion = Integer.parseInt(config[1]); // extraire la capacité max du camion
            }

            if(boitesDemande > capaciteCamion) { // si le nbre de boîtes dépasse la capacité du camion
                boitesDemande = capaciteCamion; // remplir le camion à sa capacité max
            }

            Cargaisons[] carg = new Cargaisons[2]; //tableau initial de cargaisons
            int cargCount = 0; // compteur du nbre de bâtiments trouvés

            while((line = data.readLine()) != null) { // O(n) => se répète n fois (nbre de lignes de données)
                if(line.trim().isEmpty()) { // saut des lignes vides du fichier
                    continue;
                }

                line = line.replaceAll("\\(\\s*", "(")
                           .replaceAll("\\s*\\)", ")")
                           .replaceAll("\\s*,\\s*", ",");

                String[] infos = line.trim().split("\\s+"); // découpage des info de la ligne courante

                for(int i = 0;i<infos.length-1;i+=2) { // O(n) => s'exécute au max n/2 fois
                    if(cargCount == carg.length) { // si la capacité du tableau est atteinte
                        carg = Arrays.copyOf(carg, carg.length*2); // O(n)
                    }

                    infos[i+1] = infos[i+1].replaceAll("[()]",""); // nettoyage de parenthèses des coordonnées
                    String[] coordsStr = infos[i+1].split(","); // séparation de latitude/longitude

                    int boitesAct = Integer.parseInt(infos[i]); // conversion textuelle du nbre de boîtes actuelles
                    double[] coordsAct = new double[] { 
                        Double.parseDouble(coordsStr[0]), Double.parseDouble(coordsStr[1]) // conversion textuelle de latitude/longitude
                    };
                    
                    carg[cargCount] = new Cargaisons(boitesAct, coordsAct);
                    cargCount++;
                }
            }
            
            carg = Arrays.copyOf(carg, cargCount); // O(n) => ajustement final du tableau à la taille exacte des données
            
            if (cargCount == 0) { // si aucune cargaison trouvée dans l'input
                data.close();
                sortie.close();
                return;
            }

            int maxBoites = 0, maxIndex = 0;
            
            // Rechercher l'emplacement de l'origine du camion (avec le plus de boîtes) 
            // pour avoir une position fixe de référence pour le calcul des distances
            for(int i = 0; i < carg.length; i++) { // O(n) => s'exécute exactement n fois
                if(carg[i].nbsBoites > maxBoites) { // si le point actuel a plus de boîtes
                    maxBoites = carg[i].nbsBoites; // nbre max de boîtes trouvé
                    positionCamion = carg[i].coords; // coordonnées initiales du camion
                    maxIndex = i; // emplacement de l'origine du camion
                }
            }

            int totalBoitesDisponibles = 0;
            
            for (int i = 0; i < carg.length; i++) { // O(n) => s'exécute exactement n fois pour calculer le stock total disponible
                totalBoitesDisponibles += carg[i].nbsBoites; // somme cumulative des boîtes disponibles dans les bâtiments
            }
            if (boitesDemande < totalBoitesDisponibles) { // si la demande de boites < à l'offre disponible
                boitesDemande = totalBoitesDisponibles; // collecter toutes les boîtes disponibles
            }
            if (boitesDemande > capaciteCamion) { // si la demande de boîtes dépasse la capacité du camion
                boitesDemande = capaciteCamion; // ne jamais déborder le camion
            }

            // Calculer toutes les distances des bâtiments par rapport à la position fixe initiale du camion
            for (int i = 0; i < carg.length; i++) { // O(n) => s'exécute exactement n fois pour calculer la distance
                carg[i].distance = DHaversine(positionCamion, carg[i].coords); // O(1) => distance Haversine
            }

            // Capturer le temps d'exécution pour l'ANALYSE EMPIRIQUE
            long startTime = System.currentTimeMillis(); // temps système initial (en ms)

            // Choix de l'algo de tri à exécuter selon l'argument
            if (triChoix == 1) { // Tri simple (Insertion Sort)
                carg = insertionSort(carg); // COMPLEXITÉ: O(n²)
            } else if (triChoix == 2) { // Tri efficace (Merge Sort)
                carg = mergeSort(carg, 0, carg.length - 1); // COMPLEXITÉ: O(n log n)
            } else { // Tri de la librairie standard de Java (Arrays.sort)
                Arrays.sort(carg); // COMPLEXITÉ: O(n log n)
            }

            long endTime = System.currentTimeMillis(); // temps système final après le tri (en ms)
            long duration = endTime - startTime; // temps d'exécution du tri (en ms)
            
            // Log d'analyse empirique pour avoir les données temporelles dans le graphique du rapport
            // System.err.println("ANALYSE_EMPIRIQUE => Input: " + args[0] + " | Size = " + cargCount + " | Algo: " + triChoix + " | Time = " + duration + "ms");

            sortie.write("Truck position: ("+positionCamion[0]+","+positionCamion[1]+")");
            sortie.newLine();

            int totalCharge = 0;
            
            // Parcourir la liste triée pour charger le camion en respectant la demande de boîtes et la capacité du camion
            for (int i = 0; i < carg.length; i++) { // O(n) => s'exécute exactement n fois pour traiter chaque point de livraison
                Cargaisons cargTriee = carg[i]; // cargaison courante dans la liste triée

                if (totalCharge < boitesDemande || cargTriee.distance == 0) { // si le camion non plein OU si point de départ
                    int espaceRestant = boitesDemande - totalCharge; // espace restant à charger pour atteindre la demande totale de boîtes
                    int aPrendre = cargTriee.nbsBoites; // nbre de boîtes disponibles à prendre à ce point de livraison
                    
                    if (aPrendre > espaceRestant) { // si le nbre de boîtes à prendre dépasse l'espace restant dans le camion
                        aPrendre = espaceRestant; // prendre que l'espace restant pour ne pas dépasser la demande totale de boîtes
                    }

                    totalCharge += aPrendre; // accumuler le volume pris dans le camion
                    cargTriee.nbsBoites -= aPrendre; // soustraire les boîtes chargées pour avoir le reste

                    String distanceStr = "0";
                    // Formater la distance avec un nombre de décimales fixe
                    if (cargTriee.distance != 0) { // Cas spécial: distance = 0
                        distanceStr = String.format(Locale.US,"%.1f", cargTriee.distance); // 1 décimale pour la distance
                    }
                    
                    // Écriture formatée standardisée respectant les espacements dans l'énoncé du devoir
                    sortie.write("Distance:" + distanceStr + "\t\tNumber of boxes:" + cargTriee.nbsBoites + "\t\tPosition:(" + cargTriee.coords[0] + "," + cargTriee.coords[1] + ")");
                    sortie.newLine();
                }
            }

            data.close();
            sortie.close();
        } catch(FileNotFoundException e) {
            System.out.println("Erreur (FileNotFoundException): " + e.getMessage());
        } catch(IOException f) {
            System.out.println("Erreur (IOException): " + f.getMessage());
        }
    }
}
