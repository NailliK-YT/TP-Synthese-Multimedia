#!/bin/bash
# ============================================================================
# Script d'exécution pour Linux/Mac
# ============================================================================

echo "=========================================="
echo "  LANCEMENT - Editeur d'Images MVC"
echo "=========================================="
echo ""

# Vérification que le dossier bin existe
if [ ! -d "bin" ]; then
    echo "ERREUR: Le dossier bin n'existe pas"
    echo "Veuillez d'abord compiler avec ./compile.sh"
    exit 1
fi

# Vérification que ApplicationPrincipale.class existe
if [ ! -f "bin/ApplicationPrincipale.class" ]; then
    echo "ERREUR: L'application n'est pas compilée"
    echo "Veuillez d'abord compiler avec ./compile.sh"
    exit 1
fi

# Lancement de l'application
echo "Lancement de l'application..."
echo ""
java -cp bin ApplicationPrincipale

# Si l'application se termine, afficher un message
echo ""
echo "=========================================="
echo "  Application terminée"
echo "=========================================="
