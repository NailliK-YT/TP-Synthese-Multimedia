# Éditeur d'Images - Architecture MVC

Application de traitement d'images en Java avec architecture Modèle-Vue-Contrôleur.  
**Tout le code est en français** : classes, méthodes, variables et commentaires.

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-Academic-blue.svg)](LICENSE)

---

## 📋 Table des Matières

- [Structure du Projet](#-structure-du-projet)
- [Installation et Démarrage](#-installation-et-démarrage)
- [Fonctionnalités](#-fonctionnalités)
- [Architecture MVC](#-architecture-mvc)
- [Guide d'Utilisation](#-guide-dutilisation)
- [Documentation Technique](#-documentation-technique)
- [Auteurs](#-auteurs)

---

## 📁 Structure du Projet

```
TP-Synthese-Multimedia/
├── src/                              # Code source
│   ├── ApplicationPrincipale.java    # Point d'entrée (main)
│   │
│   ├── modele/                       # MODÈLE - Logique métier
│   │   ├── ModeleImage.java          # Données de l'application
│   │   ├── UtilitaireImage.java      # Opérations bas niveau (ARGB, HSV)
│   │   └── traitement/               # Algorithmes de traitement
│   │       ├── TraitementFusion.java          # Fusion d'images
│   │       ├── TraitementTransformation.java # Transformations
│   │       ├── TraitementRemplissage.java    # Pot de peinture (flood fill)
│   │       └── TraitementTexte.java          # Ajout de texte
│   │
│   ├── vue/                          # VUE - Interface graphique
│   │   ├── FramePrincipal.java       # Fenêtre principale (JFrame)
│   │   ├── PanelImage.java           # Panneau d'affichage (JPanel)
│   │   ├── PanelOutils.java          # Panneau de boutons latéral (JPanel)
│   │   ├── PanelStatut.java          # Barre d'information (JPanel)
│   │   └── MenuBarPrincipal.java     # Barre de menu (JMenuBar)
│   │
│   └── controleur/                   # CONTRÔLEUR - Logique de contrôle
│       ├── ControleurPrincipal.java  # Contrôleur principal (coordination)
│       ├── ControleurFichier.java    # Gestion fichiers (ouvrir/sauvegarder)
│       └── ControleurImage.java      # Opérations sur images (26 méthodes)
│
├── bin/                              # Fichiers compilés (.class)
├── compile.bat / compile.sh          # Scripts de compilation
└── run.bat / run.sh                  # Scripts d'exécution
```

---

## 🚀 Installation et Démarrage

### Prérequis

- **Java JDK 11+** installé
- Variable d'environnement `JAVA_HOME` configurée

### Compilation et Exécution

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

#### Commande Manuelle

```bash
# Compilation
javac -encoding UTF-8 -d bin -sourcepath src src/ApplicationPrincipale.java

# Exécution
java -cp bin ApplicationPrincipale
```

### Vérification

Après exécution, l'application affiche :

```
==========================================
  ÉDITEUR D'IMAGES - BUT 3 INFORMATIQUE
  Architecture Modèle-Vue-Contrôleur
  Équipe 6 - Programmation Multimédia
==========================================

Modèle créé
Vue créée
Contrôleur créé
Contrôleur principal initialisé - Prêt !
```

---

## ✨ Fonctionnalités

### 📂 Gestion de Fichiers

- Ouverture d'images PNG (principale et secondaire)
- Sauvegarde au format PNG
- Restauration de l'image originale (annuler)

### 🔄 Transformations Géométriques

- **Rotation 90°** droite
- **Rotation 90°** gauche
- **Rotation 180°**

### 🎨 Transformations Colorimétriques

- **Luminosité** : Ajustement de -100 à +100
- **Contraste** : Facteur de 0.5 à 2.0
- **Teinte** : Décalage de 0 à 360°
- **Niveaux de gris** : Conversion en noir et blanc
- **Négatif** : Inversion des couleurs

### 🖼️ Fusion d'Images

- **Superposition simple** : Image 2 sur Image 1
- **Superposition avec alpha** : Gestion de la transparence
- **Chroma Key** : Fond vert (couleur personnalisable)
- **Fusion pondérée** : Mélange selon un ratio (ex: 50/50)

### 🖌️ Outils de Dessin

- **Pot de peinture** :
  - Flood fill avec tolérance ajustable (0-100)
  - Choix de la couleur
  - Clic sur l'image pour peindre

- **Ajout de texte** :
  - Texte simple avec couleur
  - Texte avec fond coloré
  - Texte avec couleur d'image (utilise Image 2)

---

## 🏗️ Architecture MVC

### Modèle (`modele/`)

**Responsabilités** :
- Stockage des images (principale et secondaire)
- Logique métier (algorithmes de traitement)
- Notification des changements (pattern Observer)

**Classes principales** :
- `ModeleImage.java` : Gestion des données d'images
- `UtilitaireImage.java` : Utilitaires bas niveau (ARGB ↔ HSV)
- `TraitementFusion.java` : Superposition, chroma key, fusion
- `TraitementTransformation.java` : Rotations, luminosité, contraste, teinte
- `TraitementRemplissage.java` : Pot de peinture (flood fill)
- `TraitementTexte.java` : Ajout de texte sur images

### Vue (`vue/`)

**Responsabilités** :
- Affichage de l'interface graphique
- Délégation des événements aux contrôleurs
- Mise à jour de l'affichage

**Composants** :
- `FramePrincipal.java` : Fenêtre principale (JFrame)
- `PanelImage.java` : Zone d'affichage de l'image
- `PanelOutils.java` : Panneau latéral avec 4 sections de boutons
- `MenuBarPrincipal.java` : Menus Fichier, Fusion et Transformations
- `PanelStatut.java` : Barre d'information en bas

### Contrôleur (`controleur/`)

**Responsabilités** :
- Coordination entre Modèle et Vue
- Gestion des événements utilisateur
- Mise à jour du Modèle et de la Vue

**Classes** :
- `ControleurPrincipal.java` : Coordination générale, pattern Observer
- `ControleurFichier.java` : Ouvrir, sauvegarder, restaurer, quitter
- `ControleurImage.java` : 26 méthodes de traitement d'images

### Flux de Données MVC

```
Utilisateur → Vue → Contrôleur → Modèle
                                    ↓
                              (notification)
                                    ↓
                     Contrôleur → Vue (mise à jour)
```

---

## 📖 Guide d'Utilisation

### Premier Lancement

1. **Compiler** l'application avec `compile.bat` ou `compile.sh`
2. **Exécuter** avec `run.bat` ou `run.sh`
3. L'interface graphique s'ouvre

### Charger une Image

1. Cliquer sur **Fichier > Ouvrir image principale...**
2. Sélectionner une image PNG
3. L'image s'affiche dans la zone centrale

### Appliquer des Transformations

**Via les boutons** (panneau latéral) :
- Section **TRANSFORMATIONS** : Rotation, Luminosité, Contraste, Teinte, Noir et Blanc
- Cliquer sur le bouton de votre choix

**Via les menus** :
- **Menu Fusion > Transformations** : Toutes les transformations

### Fusion d'Images

1. Charger **Image 1** : Fichier > Ouvrir image principale
2. Charger **Image 2** : Fichier > Ouvrir image secondaire
3. Définir la position : Clic sur **Definir Position Image 2**
4. Choisir le type de fusion :
   - **Superposer** : Simple superposition
   - **Avec Transparence** : Alpha blending
   - **Fond Vert (Chroma)** : Choisir couleur + tolérance
   - **Melanger 50/50** : Fusion pondérée

### Pot de Peinture

1. Cliquer sur **Pot de Peinture** (section OUTILS DESSIN)
2. Choisir la **couleur**
3. Définir la **tolérance** (0 = exact, 50 = similaire, 100 = large)
4. **Cliquer sur l'image** pour peindre

### Ajouter du Texte

1. Choisir le type :
   - **Texte Simple** : Texte avec couleur
   - **Texte avec Fond** : Texte + rectangle de fond
   - **Texte Colore Image** : Texte rempli avec Image 2
2. Entrer le texte et les coordonnées
3. Choisir la/les couleur(s)

### Sauvegarder

1. Cliquer sur **Fichier > Sauvegarder...**
2. Choisir l'emplacement et le nom
3. Le fichier est enregistré au format PNG

---

## 🔧 Documentation Technique

### Conventions de Code

- **Langue** : Français pour tout le code
- **Nommage** :
  - Classes : `PascalCase` + préfixe selon type (ex: `PanelImage`, `FramePrincipal`)
  - Méthodes : `camelCase` avec verbes français (ex: `ajusterLuminosite()`)
  - Variables : `camelCase` descriptif
- **Commentaires** : JavaDoc uniquement (`/** ... */`)

### Format d'Image

- **Format supporté** : PNG uniquement
- **Représentation interne** : `BufferedImage` (TYPE_INT_ARGB)
- **Couleurs** : Format ARGB 32 bits (Alpha-Rouge-Vert-Bleu)

### Algorithmes Clés

#### Pot de Peinture (Flood Fill)

**Fichier** : `TraitementRemplissage.java`  
**Méthode** : Flood fill avec tolérance basée sur distance euclidienne dans l'espace RGB

```java
// Pseudo-code
distance = sqrt((R1-R2)² + (G1-G2)² + (B1-B2)²)
if (distance <= tolerance) → remplir le pixel
```

#### Chroma Key

**Fichier** : `TraitementFusion.java`  
**Principe** : Rendre transparents les pixels proches d'une couleur cible

```java
// Pour chaque pixel de Image 2:
if (distance(pixel, couleur_cible) <= tolerance) {
    pixel devient transparent
}
// Puis superposer sur Image 1
```

#### Conversion HSV

**Fichier** : `UtilitaireImage.java`  
**Utilisation** : Ajustement de teinte, saturation, valeur

```java
ARGB → HSV → (modification) → ARGB
```

### Compilation de la JavaDoc

Pour générer la documentation HTML :

```bash
javadoc -d docs -encoding UTF-8 -sourcepath src -subpackages modele,vue,controleur
```

La documentation sera dans le dossier `docs/`.

---

## 👥 Auteurs

**Équipe 6 - BUT 3 Informatique**  
Programmation Multimédia - TP Synthèse  
IUT - 2024/2025

---

## 📄 Licence

Projet académique - BUT Informatique  
Tous droits réservés - Usage pédagogique uniquement

---

## 🆘 Dépannage

### Erreur : `javac: command not found`

- Vérifier que Java JDK est installé : `java -version`
- Configurer `JAVA_HOME` dans les variables d'environnement

### Erreur : `UnsupportedClassVersionError`

- Votre version de Java est trop ancienne
- Installer Java JDK 11 ou supérieur

### L'image ne s'affiche pas

- Vérifier que le fichier est bien au format **PNG**
- Les autres formats (JPEG, GIF, BMP) ne sont pas supportés

### Le pot de peinture ne fonctionne pas

1. Vérifier qu'une image est chargée
2. Cliquer sur **Pot de Peinture** pour choisir couleur et tolérance
3. Ensuite **cliquer sur l'image** pour peindre

---

## 🎓 Concepts Pédagogiques

Ce projet illustre :

- **Architecture MVC** : Séparation claire Modèle-Vue-Contrôleur
- **Pattern Observer** : Notification des changements du modèle
- **Traitement d'images** : Manipulation de pixels, espaces colorimétriques
- **Algorithmes classiques** : Flood fill, alpha blending, rotations
- **Interface graphique Swing** : JFrame, JPanel, JMenuBar, événements
- **Bonnes pratiques Java** : JavaDoc, nommage, organisation en packages

---

**Bon développement ! 🚀**
