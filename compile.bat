@echo off
REM ============================================================================
REM Script de compilation pour Windows
REM ============================================================================

echo ==========================================
echo   COMPILATION - Editeur d'Images MVC
echo ==========================================
echo.

REM Création du dossier bin s'il n'existe pas
if not exist "bin" (
    echo Creation du dossier bin...
    mkdir bin
)

REM Compilation avec encodage UTF-8
echo Compilation en cours...
javac -encoding UTF-8 -d bin -sourcepath src src\ApplicationPrincipale.java

REM Vérification du résultat
if %errorlevel% equ 0 (
    echo.
    echo ==========================================
    echo   COMPILATION REUSSIE !
    echo ==========================================
    echo.
    echo Pour lancer l'application : run.bat
) else (
    echo.
    echo ==========================================
    echo   ERREUR DE COMPILATION
    echo ==========================================
    pause
    exit /b 1
)

pause
