# Éditeur d'Images - Architecture MVC

Application de traitement d'images en Java avec architecture Modèle-Vue-Contrôleur.  
**Tout le code est en français** : classes, méthodes, variables et commentaires.

## Structure du Projet

```
TP-Synthese-Multimedia/
├── src/                              # Code source
│   ├── ApplicationPrincipale.java    # Point d'entrée (main)
│   │
│   ├── modele/                       # MODÈLE - Logique métier
│   │   ├── ModeleImage.java          # Données de l'application
│   │   ├── UtilitaireImage.java      # Opérations bas niveau
│   │   └── traitement/               # Algorithmes de traitement
│   │       ├── TraitementFusion.java
│   │       ├── TraitementTransformation.java
│   │       ├── TraitementRemplissage.java
│   │       └── TraitementTexte.java
│   │
│   ├── vue/                          # VUE - Interface graphique
│   │   └── VuePrincipale.java        # Fenêtre principale
│   │
│   └── controleur/                   # CONTRÔLEUR - Logique de contrôle
│       ├── ControleurPrincipal.java  # Contrôleur principal
│       ├── ControleurFichier.java    # Gestion fichiers
│       └── ControleurImage.java      # Opérations images
│
├── bin/                              # Fichiers compilés (.class)
├── compile.sh                        # Script de compilation
└── run.sh                            # Script d'exécution
```

## Compilation et Exécution

### MÉTHODE 1 : Scripts Automatiques (Recommandé)

#### Windows

```cmd
REM Compilation
compile.bat

REM Exécution
run.bat
```

#### Linux/Mac

```bash
# Rendre les scripts exécutables (première fois uniquement)
chmod +x compile.sh run.sh

# Compilation
./compile.sh

# Exécution
./run.sh
```

### MÉTHODE 2 : Ligne de Commande Manuelle

#### Windows (PowerShell)

```powershell
# Compilation
javac -encoding UTF-8 -d bin -sourcepath src src/ApplicationPrincipale.java

# Exécution
java -cp bin ApplicationPrincipale
```

#### Linux/Mac (Bash)

```bash
# Compilation
javac -encoding UTF-8 -d bin -sourcepath src src/ApplicationPrincipale.java

# Exécution
java -cp bin ApplicationPrincipale
```

## Fonctionnalités

### Gestion de Fichiers
- Ouverture d'images PNG (principale et secondaire)
- Sauvegarde au format PNG
- Restauration de l'image originale

### Transformations Géométriques
- Rotation 90° (droite/gauche)
- Rotation 180°

### Transformations Colorimétriques
- Ajustement luminosité
- Ajustement contraste
- Décalage de teinte (HSV)
- Niveaux de gris
- Négatif

### Fusion d'Images
- Superposition simple
- Superposition avec alpha blending
- Superposition avec chroma key (fond vert)
- Fusion pondérée

### Outils de Dessin
- Pot de peinture (flood fill avec tolérance)
- Ajout de texte (simple, avec fond, avec couleur d'image)

## Architecture MVC

### Modèle
Contient les données et la logique métier. Ne dépend ni de la vue ni du contrôleur.

### Vue
Affiche l'interface graphique. Délègue tous les événements au contrôleur.

### Contrôleur
Fait le lien entre le modèle et la vue. Reçoit les événements, modifie le modèle, met à jour la vue.

## Auteurs

**Équipe 6 - BUT 3 Informatique**  
Programmation Multimédia - TP Synthèse

## Licence

Projet académique - BUT Informatique
