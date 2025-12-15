#!/bin/bash
# ============================================================================
# SCRIPT DE LANCEMENT - Éditeur d'Images BUT 3
# ============================================================================

# Compilation si nécessaire
if [ ! -d "bin" ] || [ ! -f "bin/MainApp.class" ]; then
    echo "Compilation du projet..."
    ./compile.sh
fi

# Lancement
echo "Lancement de l'application..."
java -cp bin MainApp

