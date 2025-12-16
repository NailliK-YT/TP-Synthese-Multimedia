# 🚀 Démarrage Rapide - Éditeur d'Images MVC

## Windows

### Option 1 : PowerShell (Recommandé - avec couleurs)
```powershell
.\compile.ps1    # Compilation
.\run.ps1        # Exécution
```

### Option 2 : Batch (Compatible tous systèmes Windows)
```cmd
compile.bat      # Compilation
run.bat          # Exécution
```

### Option 3 : Commande manuelle
```powershell
javac -encoding UTF-8 -d bin -sourcepath src src\ApplicationPrincipale.java
java -cp bin ApplicationPrincipale
```

---

## Linux / Mac

### Option 1 : Scripts shell
```bash
chmod +x compile.sh run.sh   # Première fois uniquement
./compile.sh                 # Compilation
./run.sh                     # Exécution
```

### Option 2 : Commande manuelle
```bash
javac -encoding UTF-8 -d bin -sourcepath src src/ApplicationPrincipale.java
java -cp bin ApplicationPrincipale
```

---

## 📁 Fichiers de Scripts

- **Windows PowerShell** : `compile.ps1`, `run.ps1` (avec couleurs)
- **Windows Batch** : `compile.bat`, `run.bat` (compatible partout)
- **Linux/Mac Shell** : `compile.sh`, `run.sh`

## ✅ Vérification

Après compilation, vous devriez voir :
```
==========================================
  COMPILATION REUSSIE !
==========================================
```

Après exécution, l'application s'ouvre et affiche :
```
==========================================
  ÉDITEUR D'IMAGES - BUT 3 INFORMATIQUE
  Architecture Modèle-Vue-Contrôleur
==========================================
✓ Modèle créé
✓ Vue créée
✓ Contrôleur créé
✓ Application initialisée
```

## 📚 Documentation Complète

Voir [README.md](README.md) pour la documentation complète.
