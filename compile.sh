#!/bin/bash
# ============================================================================
# SCRIPT DE COMPILATION - Éditeur d'Images BUT 3
# ============================================================================
# Équipe 6 - Programmation Multimédia

echo "=== Compilation du projet ==="

# Création du dossier de sortie
mkdir -p bin

# Compilation de tous les fichiers Java
echo "Compilation des fichiers Java..."
javac -d bin src/*.java

# Vérification du résultat
if [ $? -eq 0 ]; then
    echo "✓ Compilation réussie !"
    echo ""
    echo "Pour lancer l'application :"
    echo "  cd bin"
    echo "  java MainApp"
    echo ""
    echo "Ou directement :"
    echo "  java -cp bin MainApp"
else
    echo "✗ Erreur de compilation"
    exit 1
fi

