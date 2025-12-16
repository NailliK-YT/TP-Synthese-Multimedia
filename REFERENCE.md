# Référence Rapide - Éditeur d'Images

## Lancement de l'Application

### Windows
```cmd
compile.bat    # Compilation
run.bat        # Exécution
```

### PowerShell
```powershell
.\compile.ps1
.\run.ps1
```

---

## Interface Utilisateur

L'application est organisée en **4 sections** dans le panneau latéral droit :

### 1. FICHIERS
- **Ouvrir Image 1** : Charge l'image principale (PNG)
- **Ouvrir Image 2** : Charge l'image secondaire pour fusion/texte (PNG)
- **Sauvegarder** : Enregistre l'image modifiée (PNG)
- **Annuler (Restaurer)** : Annule toutes les modifications

### 2. FUSION 2 IMAGES
*(Nécessite Image 1 ET Image 2)*
- **Superposer** : Place Image 2 sur Image 1
- **Avec Transparence** : Superposition avec alpha blending
- **Fond Vert (Chroma)** : Rend une couleur transparente
- **Melanger 50/50** : Fusionne avec ratio

### 3. TRANSFORMATIONS
- **Rotation Droite** : Rotation 90° horaire
- **Rotation Gauche** : Rotation 90° anti-horaire
- **Luminosite** : Ajuste la luminosité (-100 à +100)
- **Contraste** : Ajuste le contraste (facteur)
- **Teinte (Couleur)** : Décale les couleurs (0-360°)
- **Noir et Blanc** : Conversion en niveaux de gris

### 4. OUTILS DESSIN
- **Pot de Peinture** : Remplissage avec tolérance
  1. Choisir la couleur
  2. Définir la tolérance (distance)
  3. Cliquer sur l'image
  
- **Texte Simple** : Texte avec couleur unie
- **Texte avec Fond** : Texte sur rectangle coloré
- **Texte Colore Image** : Texte rempli avec Image 2

---

## Workflow Typique

### Retouche Simple
```
1. Ouvrir Image 1
2. Luminosite → ajuster
3. Contraste → ajuster
4. Sauvegarder
```

### Fusion d'Images
```
1. Ouvrir Image 1
2. Ouvrir Image 2
3. Choisir mode fusion (Superposer, Transparence, etc.)
4. Sauvegarder
```

### Fond Vert (Chroma Key)
```
1. Ouvrir Image 1 (personne sur fond vert)
2. Ouvrir Image 2 (nouveau décor)
3. Fond Vert (Chroma)
4. Sélectionner la couleur verte
5. Tolérance : 30-50
6. Sauvegarder
```

### Pot de Peinture
```
1. Ouvrir Image 1
2. Pot de Peinture
3. Choisir couleur
4. Définir tolérance :
   - 0 = couleur exacte
   - 30 = similaire (recommandé)
   - 100 = large
5. Cliquer sur zone à remplir
6. Sauvegarder
```

---

## Caractéristiques Techniques

- **Format** : PNG uniquement
- **Architecture** : MVC (Modèle-Vue-Contrôleur)
- **Langage** : Java (100% français)
- **Interface** : Swing
- **Pattern** : Observer pour réactivité

---

## Structure du Code

```
src/
├── ApplicationPrincipale.java
├── modele/
│   ├── ModeleImage.java
│   ├── UtilitaireImage.java
│   └── traitement/
│       ├── TraitementFusion.java
│       ├── TraitementTransformation.java
│       ├── TraitementRemplissage.java
│       └── TraitementTexte.java
├── vue/
│   └── VuePrincipale.java
└── controleur/
    ├── ControleurPrincipal.java
    ├── ControleurFichier.java
    └── ControleurImage.java
```

---

## Dépannage

**"Veuillez d'abord charger une image"**
→ Utilisez "Ouvrir Image 1"

**"Veuillez d'abord charger une image secondaire"**
→ Utilisez "Ouvrir Image 2" (pour fusion/texte coloré)

**Pot de peinture ne remplit pas assez**
→ Augmentez la tolérance (50-100)

**Pot de peinture remplit trop**
→ Réduisez la tolérance (0-20)

**Erreur de compilation**
→ Vérifiez l'encodage UTF-8 : `javac -encoding UTF-8 ...`

---

## Documentation

- **README.md** - Documentation technique complète
- **DEMARRAGE.md** - Guide de démarrage
- **GUIDE_UTILISATEUR.md** - Guide utilisateur détaillé

---

## Contact

Équipe 6 - BUT 3 Informatique
TP Synthèse Multimédia
