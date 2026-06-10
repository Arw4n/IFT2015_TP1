import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

// Programme de gestion de cargaisons pour un camion de livraison
public class Tp1 {
    // Fonction de distance de Haversine pour calculer la distance 
    // entre 2 points géographiques par leurs coordonnées latitude/longitude
    
    // COMPLEXITÉ GLOBALE: O(1) => toutes les opérations arithmétiques internes 
    // s'exécutent en temps fixe indépendant de n
    public static double DHaversine(double[] CoordCamion, double[] CoordPoint) {
        // Conversion des coordonnées de degrés à radians pour les calculs trigonométriques
        double lat1 = CoordCamion[0]*Math.PI/180; // O(1)
        double long1 = CoordCamion[1]*Math.PI/180; // O(1)
        double lat2 = CoordPoint[0]*Math.PI/180; // O(1)
        double long2 = CoordPoint[1]*Math.PI/180; // O(1)

        double r = 6371000; // Rayon de la terre en mètres
        double distance = 2*r*Math.asin(
            Math.sqrt(
                Math.pow(
                    Math.sin((lat2-lat1)/2),2
                ) + Math.cos(lat1)*Math.cos(lat2)*Math.pow(
                    Math.sin((long2-long1)/2),2
                )
            )
        ); // O(1) => Formule de trigo de Haversine

        // Utiliser la distance brute pour que "compareTo()" trie avec max précision sans bugs d'arrondis
        return distance; // O(1)
    }

    // Tri simple: Insertion Sort (option 1)
    // COMPLEXITÉ (PIRE CAS): O(n²) & MEILLEUR CAS: O(n)
    public static Cargaisons[] insertionSort(Cargaisons[] liste) {
        Cargaisons temp; // O(1) => tableau d'espace temporaire
        int n = liste.length; // O(1) => taille de la liste
        
        for(int i = 1; i<n; i++) { // O(n) => s'exécute (n - 1) fois
            temp=liste[i]; // O(1) => Sélection de l'élément à insérer
            int j=i; // O(1) => Index de comparaison arrière
            
            while(j>0 && liste[j-1].compareTo(temp) > 0) { // O(i) => s'exécute au max i fois
                liste[j] = liste[j-1]; // O(1) => Déplacement de l'élément le plus grand vers la droite
                j--; // O(1)
            }
            
            liste[j] = temp; // O(1) => Insertion de l'élément à son emplacement trié
        }
        
        return liste; // O(1)
    }

    // Tri efficace: Merge Sort (option 2)
    // COMPLEXITÉ (PIRE & MEILLEUR CAS): O(n log n)
    public static Cargaisons[] mergeSort(Cargaisons[] array, int left, int right) {
        if (left < right) { // O(1) => Condition de terminaison de la récursion binaire
            int middle = (left + right) / 2; // O(1) => Calcul du pivot du milieu
            
            mergeSort(array, left, middle); // T(n/2) => Division récursive de la moitié gauche
            mergeSort(array, middle + 1, right); // T(n/2) => Division récursive de la moitié droite
            
            merge(array, left, middle, right); // O(n) => Fusion linéaire des 2 moitiés triées
        }
        
        return array; // O(1)
    }

    // COMPLEXITÉ: O(n) où n = nbre d'éléments à fusionner (n = right - left + 1)
    private static void merge(Cargaisons[] array, int left, int middle, int right) {
        Cargaisons[] temp = new Cargaisons[right - left + 1]; // O(n) => tableau d'espace temporaire
        int i = left, j = middle + 1, k = 0; // O(1) => 3 index de parcours
        
        while (i <= middle && j <= right) { // O(n) => s'exécute au max (right - left + 1) fois
            if (array[i].compareTo(array[j]) <= 0) { // O(1) => Vérification de l'ordre de tri
                temp[k++] = array[i++]; // O(1) => Copie l'élément de la sous-liste gauche
            } else {
                temp[k++] = array[j++]; // O(1) => Copie l'élément de la sous-liste droite
            }
        }
        
        while (i <= middle) { // O(n) (pire cas) => Copie les éléments restants de la sous-liste gauche
            temp[k++] = array[i++]; // O(1)
        }
        while (j <= right) { // O(n) (pire cas) => Copie les éléments restants de la sous-liste droite
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
        if (args.length < 2) { // O(1) => nbre d'arguments insuffisant pour l'exécution
            System.out.println("Use: java Tp1 input.txt output.txt [n_optional]"); // O(1)
            return; // O(1)
        }

        // Lecture du 3eme argument optionnel n pour choisir l'algo de tri
        // Par défaut, n = 2 (mergeSort): algo de tri le plus efficace du programme
        int triChoix = 2; // O(1)
        
        if (args.length >= 3) { // O(1) => Vérification de la présence de l'argument optionnel
            try {
                int parsedTriChoix = Integer.parseInt(args[2]); // O(1) => Conversion textuelle en entier
                if (parsedTriChoix >= 1 && parsedTriChoix <= 3) { // O(1) => Vérification de la validité du choix
                    triChoix = parsedTriChoix; // O(1)
                }
            } catch (NumberFormatException e) {
                // Si args[2] n'est pas un nombre, on conserve la valeur par défaut
            }
        }

        try {
            BufferedReader data = new BufferedReader(new FileReader(args[0])); // O(1) => pour lire les inputs
            BufferedWriter sortie = new BufferedWriter(new FileWriter(args[1])); // O(1) => pour écrire les outputs
            
            String Firstline = data.readLine(); // O(1) => Lecture de la ligne d'en-tête de configuration
            if (Firstline == null) { // O(1) => si fichier vide
                data.close(); // O(1)
                sortie.close(); // O(1)
                return; // O(1)
            }

            String line; // O(1)
            String[] config = Firstline.trim().split("\\s+"); // O(1) => Extraction des configurations

            double distanceParcourue = 0; // O(1)
            double[] positionCamion = new double[2]; // O(1)
            int boitesDemande = Integer.parseInt(config[0]); // O(1)
            int capaciteCamion; // O(1)

            if(config.length<2) { // O(1) => si capacité du camion non spécifiée dans la ligne 1 de l'input
                capaciteCamion = 0; // O(1) => camion déjà plein (capacité restante = 0)
            } else { 
                capaciteCamion = Integer.parseInt(config[1]); // O(1) => Extraction de la capacité max du camion
            }

            if(boitesDemande > capaciteCamion) { // O(1) => si le nbre de boîtes dépasse la capacité du camion
                boitesDemande = capaciteCamion; // O(1) => remplir le camion à sa capacité max
            }

            Cargaisons[] carg = new Cargaisons[2]; // O(1) => Tableau initial de cargaisons
            int cargCount = 0; // O(1) => Compteur du nbre de bâtiments trouvés

            while((line = data.readLine()) != null) { // O(n) => se répète n fois (nbre de lignes de données)
                if(line.trim().isEmpty()) { // O(1) => Saut des lignes vides du fichier
                    continue; // O(1)
                }

                String[] infos = line.trim().split("\\s+"); // O(1) => Découpage des info de la ligne courante

                for(int i = 0;i<infos.length-1;i+=2) { // Boucle de lecture des paires (boîtes, coordonnées) sur la ligne
                    if(cargCount == carg.length) { // O(1) => Condition d'expansion du tableau si saturé
                        carg = Arrays.copyOf(carg, carg.length*2); // O(n) (pire cas) => lors du redimensionnement
                    }

                    infos[i+1] = infos[i+1].replaceAll("[()]",""); // O(1) => Nettoyage de parenthèses des coordonnées
                    String[] coordsStr = infos[i+1].split(","); // O(1) => Séparation de latitude/longitude

                    int boitesAct = Integer.parseInt(infos[i]); // O(1) => Conversion textuelle du nbre de boîtes actuelles
                    double[] coordsAct = new double[] { 
                        Double.parseDouble(coordsStr[0]), Double.parseDouble(coordsStr[1]) // O(1) => Conversion textuelle de latitude/longitude
                    };
                    
                    carg[cargCount] = new Cargaisons(boitesAct, coordsAct); // O(1)
                    cargCount++; // O(1)
                }
            }
            
            carg = Arrays.copyOf(carg, cargCount); // O(n) => Ajustement final du tableau à la taille exacte des données
            
            if (cargCount == 0) { // O(1) => si aucune cargaison trouvée dans l'input
                data.close(); // O(1)
                sortie.close(); // O(1)
                return; // O(1)
            }

            int maxBoites = 0, maxIndex = 0; // O(1)
            
            // Rechercher l'emplacement de l'origine du camion (avec le plus de boîtes) 
            // pour avoir une position fixe de référence pour le calcul des distances
            for(int i = 0; i < carg.length; i++) { // O(n) => s'exécute exactement n fois
                if(carg[i].nbsBoites > maxBoites) { // O(1) => si le point actuel a plus de boîtes
                    maxBoites = carg[i].nbsBoites; // O(1) => nbre max de boîtes trouvé
                    positionCamion = carg[i].coords; // O(1) => coordonnées initiales du camion
                    maxIndex = i; // O(1) => emplacement de l'origine du camion
                }
            }

            int totalBoitesDisponibles = 0; // O(1)
            
            for (int i = 0; i < carg.length; i++) { // O(n) => s'exécute exactement n fois pour calculer le stock total disponible
                totalBoitesDisponibles += carg[i].nbsBoites; // Somme cumulative des boîtes disponibles dans les bâtiments
            }
            if (boitesDemande < totalBoitesDisponibles) { // O(1) => si la demande de boites < à l'offre disponible
                boitesDemande = totalBoitesDisponibles; // O(1) => collecter toutes les boîtes disponibles
            }
            if (boitesDemande > capaciteCamion) { // O(1) => si la demande de boîtes dépasse la capacité du camion
                boitesDemande = capaciteCamion; // O(1) => ne jamais déborder le camion
            }

            // Calculer toutes les distances des bâtiments par rapport à la position fixe initiale du camion
            for (int i = 0; i < carg.length; i++) { // S'exécute n fois => O(n) => s'exécute exactement n fois
                carg[i].distance = DHaversine(positionCamion, carg[i].coords); // O(1) => distance Haversine
            }

            // Capturer le temps d'exécution pour l'ANALYSE EMPIRIQUE
            long startTime = System.currentTimeMillis(); // temps système initial (en ms)

            // Choix de l'algo de tri à exécuter selon l'argument
            if (triChoix == 1) { // Tri simple (Insertion Sort)
                carg = insertionSort(carg); // COMPLEXITÉ (PIRE CAS): O(n²)
            } else if (triChoix == 2) { // Tri efficace (Merge Sort)
                carg = mergeSort(carg, 0, carg.length - 1); // COMPLEXITÉ (PIRE CAS): O(n log n)
            } else { // Tri de la librairie standard de Java (Arrays.sort)
                Arrays.sort(carg); // COMPLEXITÉ (PIRE CAS): O(n log n)
            }

            long endTime = System.currentTimeMillis(); // temps système final après le tri (en ms)
            long duration = endTime - startTime; // tmeps d'exécution du tri (en ms)
            
            // Log d'analyse empirique pour avoir les données temporelles dans le graphique du rapport
            System.err.println("ANALYSE_EMPIRIQUE => Input: " + args[0] + " | Size = " + cargCount + " | Algo: " + triChoix + " | Time = " + duration + "ms");

            sortie.write("Truck position: ("+positionCamion[0]+","+positionCamion[1]+")"); // O(1)
            sortie.newLine(); // O(1)

            int totalCharge = 0; // O(1)
            
            // Parcourir la liste triée pour charger le camion en respectant la demande de boîtes et la capacité du camion
            for (int i = 0; i < carg.length; i++) { // O(n) => s'exécute exactement n fois pour traiter chaque point de livraison
                Cargaisons cargTriee = carg[i]; // O(1) => Cargaison courante dans la liste triée

                if (totalCharge < boitesDemande || cargTriee.distance == 0) { // O(1) => si le camion non plein OU si point de départ
                    int espaceRestant = boitesDemande - totalCharge; // O(1) => espace restant à charger pour atteindre la demande totale de boîtes
                    int aPrendre = cargTriee.nbsBoites; // O(1) => nbre de boîtes disponibles à prendre à ce point de livraison
                    
                    if (aPrendre > espaceRestant) { // O(1) => si le nbre de boîtes à prendre dépasse l'espace restant dans le camion
                        aPrendre = espaceRestant; // O(1) => prendre que l'espace restant pour ne pas dépasser la demande totale de boîtes
                    }

                    totalCharge += aPrendre; // O(1) => Accumulation du volume pris dans le camion
                    cargTriee.nbsBoites -= aPrendre; // Soustraction des boîtes chargées pour avoir le reste

                    String distanceStr = "0";
                    // Formater la distance avec un nombre de décimales fixe
                    if (cargTriee.distance != 0) { // O(1) => Cas spécial: distance = 0
                        distanceStr = String.format("%.1f", cargTriee.distance); // O(1) => 1 décimale pour la distance
                    }
                    
                    // Écriture formatée standardisée respectant les espacements dans l'énoncé du devoir
                    sortie.write("Distance:" + distanceStr + "\t\tNumber of boxes:" + cargTriee.nbsBoites + "\t\tPosition:(" + cargTriee.coords[0] + "," + cargTriee.coords[1] + ")"); // O(1)
                    sortie.newLine(); // O(1)
                }
            }

            data.close(); // O(1)
            sortie.close(); // O(1)
        } catch(FileNotFoundException e) {
            System.out.println("Erreur (FileNotFoundException): " + e.getMessage());
        } catch(IOException f) {
            System.out.println("Erreur (IOException): " + f.getMessage());
        }
    }
}
