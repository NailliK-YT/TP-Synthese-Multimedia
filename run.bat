@echo off
REM ============================================================================
REM Script d'exécution pour Windows
REM ============================================================================

echo ==========================================
echo   LANCEMENT - Editeur d'Images MVC
echo ==========================================
echo.

REM Vérification que le dossier bin existe
if not exist "bin" (
    echo ERREUR: Le dossier bin n'existe pas
    echo Veuillez d'abord compiler avec compile.bat
    pause
    exit /b 1
)

REM Vérification que ApplicationPrincipale.class existe
if not exist "bin\ApplicationPrincipale.class" (
    echo ERREUR: L'application n'est pas compilée
    echo Veuillez d'abord compiler avec compile.bat
    pause
    exit /b 1
)

REM Lancement de l'application
echo Lancement de l'application...
echo.
java -cp bin ApplicationPrincipale
