#!/bin/bash
# ============================================================================
# Script de compilation pour Linux/Mac
# ============================================================================

echo "=========================================="
echo "  COMPILATION - Editeur d'Images MVC"
echo "=========================================="
echo ""

# Création du dossier bin s'il n'existe pas
if [ ! -d "bin" ]; then
    echo "Création du dossier bin..."
    mkdir -p bin
fi

# Compilation avec encodage UTF-8
echo "Compilation en cours..."
javac -encoding UTF-8 -d bin -sourcepath src src/ApplicationPrincipale.java

# Vérification du résultat
if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "  COMPILATION REUSSIE !"
    echo "=========================================="
    echo ""
    echo "Pour lancer l'application : ./run.sh"
else
    echo ""
    echo "=========================================="
    echo "  ERREUR DE COMPILATION"
    echo "=========================================="
    exit 1
fi
