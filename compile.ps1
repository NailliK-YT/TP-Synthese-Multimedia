# ============================================================================
# Script de compilation PowerShell pour Windows
# ============================================================================

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "  COMPILATION - Editeur d'Images MVC" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# Création du dossier bin s'il n'existe pas
if (-not (Test-Path "bin")) {
    Write-Host "Création du dossier bin..." -ForegroundColor Yellow
    New-Item -ItemType Directory -Path "bin" | Out-Null
}

# Compilation avec encodage UTF-8
Write-Host "Compilation en cours..." -ForegroundColor Yellow
javac -encoding UTF-8 -d bin -sourcepath src src\ApplicationPrincipale.java

# Vérification du résultat
if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "==========================================" -ForegroundColor Green
    Write-Host "  COMPILATION REUSSIE !" -ForegroundColor Green
    Write-Host "==========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Pour lancer l'application : .\run.ps1" -ForegroundColor Cyan
} else {
    Write-Host ""
    Write-Host "==========================================" -ForegroundColor Red
    Write-Host "  ERREUR DE COMPILATION" -ForegroundColor Red
    Write-Host "==========================================" -ForegroundColor Red
    exit 1
}
