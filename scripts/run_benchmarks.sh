#!/bin/bash

# Compilation des classes Java nécessaires
echo "Compiling Java classes..."

javac Cargaisons.java
javac Tp1.java

if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

echo "Compilation succeeded."

# Répertoire d'input contenant les fichiers de test
INPUT_DIR="exemplaires"

# Liste ordonnée des fichiers de test à traiter
fichiers=(
    "100.txt" "150.txt" "200.txt" "250.txt" "300.txt" "350.txt" "400.txt" "450.txt" "500.txt" "550.txt"
    "600.txt" "650.txt" "700.txt" "750.txt" "800.txt" "850.txt" "900.txt" "950.txt" "1000.txt" "1050.txt"
    "1100.txt" "1150.txt" "1200.txt" "1250.txt" "1300.txt" "1350.txt" "1400.txt" "1450.txt" "1500.txt" "1550.txt"
    "1600.txt" "1650.txt" "1700.txt" "1750.txt" "1800.txt" "1850.txt" "1900.txt" "1950.txt" "2000.txt" "2050.txt"
    "2100.txt" "2150.txt" "2200.txt" "2250.txt" "2300.txt" "2350.txt" "2400.txt" "2450.txt" "2500.txt"
)

# Constantes pour le script JS du rapport HTML
n="const n = ["
insertionTime="const insertionTime = ["
mergeTime="const mergeTime = ["
nativeTime="const nativeTime = ["

# Variable pour gérer la virgule dans les tableaux JS
first=true

echo "Running Benchmarks..."
echo "--------------------------------------------------------------------------------------------"

# Exécution de chaque fichier de test
for f in "${fichiers[@]}"; do
    # Chemin complet du fichier de test
    FILE_PATH="$INPUT_DIR/$f"

    # Vérifier si le fichier existe avant de l'exécuter
    if [ ! -f "$FILE_PATH" ]; then
        echo "File: '$FILE_PATH' not found."
        continue
    fi

    # Récupérer la taille sur la 1ère ligne du fichier
    size=$(head -n 1 "$FILE_PATH" | awk '{print $1}')

    # Gérer les virgules de séparation du tableau JS
    if [ "$first" = true ]; then
        n+="$size"
        first=false
    else
        n+=", $size"
    fi

    # Tri par Insertion (Option 1)
    insertionSort=$(java Tp1 "$FILE_PATH" output.txt 1 2>&1 >/dev/null | grep "ANALYSE_EMPIRIQUE" | sed 's/.*Time = \([0-9]*\)ms.*/\1/')
    
    # Tri par Fusion (Option 2)
    mergeSort=$(java Tp1 "$FILE_PATH" output.txt 2 2>&1 >/dev/null | grep "ANALYSE_EMPIRIQUE" | sed 's/.*Time = \([0-9]*\)ms.*/\1/')
    
    # Arrays.sort() (Option 3)
    nativeSort=$(java Tp1 "$FILE_PATH" output.txt 3 2>&1 >/dev/null | grep "ANALYSE_EMPIRIQUE" | sed 's/.*Time = \([0-9]*\)ms.*/\1/')

    # Afficher le suivi de progression dans le terminal
    echo "File: '$f' (size = $size) => Insertion Sort: ${insertionSort}ms | Merge Sort: ${mergeSort}ms | Arrays.sort(): ${nativeSort}ms"

    # Concaténation des résultats dans les tableaux JS respectifs
    if [ "$insertionTime" = "const insertionTime = [" ]; then
        insertionTime+="$insertionSort"
        mergeTime+="$mergeSort"
        nativeTime+="$nativeSort"
    else
        insertionTime+=", $insertionSort"
        mergeTime+=", $mergeSort"
        nativeTime+=", $nativeSort"
    fi
done

# Fermeture des tableaux JS
n+="];"
insertionTime+="];"
mergeTime+="];"
nativeTime+="];"

# Nettoyage du fichier d'output temporaire généré
rm -f output.txt

# Afficher les constantes JS finales pour le rapport
echo "--------------------------------------------------------------------------------------------"
echo "$n"
echo "$insertionTime"
echo "$mergeTime"
echo "$nativeTime"
