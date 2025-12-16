# ============================================================================
# Script d'exécution PowerShell pour Windows
# ============================================================================

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "  LANCEMENT - Editeur d'Images MVC" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# Vérification que le dossier bin existe
if (-not (Test-Path "bin")) {
    Write-Host "ERREUR: Le dossier bin n'existe pas" -ForegroundColor Red
    Write-Host "Veuillez d'abord compiler avec .\compile.ps1" -ForegroundColor Yellow
    exit 1
}

# Vérification que ApplicationPrincipale.class existe
if (-not (Test-Path "bin\ApplicationPrincipale.class")) {
    Write-Host "ERREUR: L'application n'est pas compilée" -ForegroundColor Red
    Write-Host "Veuillez d'abord compiler avec .\compile.ps1" -ForegroundColor Yellow
    exit 1
}

# Lancement de l'application
Write-Host "Lancement de l'application..." -ForegroundColor Green
Write-Host ""
java -cp bin ApplicationPrincipale

# Si l'application se termine, afficher un message
Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "  Application terminée" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
