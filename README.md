# Éditeur d'Images - BUT 3 Informatique

## Équipe 6 - Programmation Multimédia

### 📋 Description du Projet

Application Java de manipulation d'images développée dans le cadre du TP de synthèse en Programmation Multimédia.

---

## 🚀 Compilation et Exécution

### Méthode 1 : Scripts (Linux/Mac)

```bash
# Rendre les scripts exécutables
chmod +x compile.sh run.sh

# Compiler et lancer
./run.sh
```

### Méthode 2 : Commandes manuelles

```bash
# Compilation
mkdir -p bin
javac -d bin src/*.java

# Exécution
java -cp bin MainApp
```

---

## 📁 Structure du Projet

```
ProjetCode/
├── src/
│   ├── MainApp.java        # Interface graphique + point d'entrée
│   ├── ImageUtil.java      # Utilitaires (ouvrir, sauvegarder, copier)
│   ├── ImageFusion.java    # Fusion et superposition d'images
│   ├── ImageTransform.java # Transformations (rotation, luminosité, etc.)
│   ├── FloodFill.java      # Pot de peinture avec tolérance
│   └── TextDrawer.java     # Dessin de texte avec fond
├── bin/                    # Classes compilées (généré)
├── compile.sh              # Script de compilation
├── run.sh                  # Script de lancement
└── README.md               # Ce fichier
```

---

## ✅ Fonctionnalités Implémentées

### 📂 Gestion des Fichiers
- [x] Ouvrir une image PNG
- [x] Ouvrir une seconde image PNG
- [x] Sauvegarder l'image modifiée
- [x] Restaurer l'image originale

### 🖼️ Fusion d'Images
- [x] Superposition simple (une image sur une autre)
- [x] Superposition avec transparence (canal alpha)
- [x] Superposition avec clé de chrominance (fond vert)
- [x] Fusion pondérée de deux images

### 🎨 Pot de Peinture (Flood Fill)
- [x] Remplissage par diffusion
- [x] Distance de couleur RGB (tolérance)
- [x] Implémentation avec file (évite StackOverflow)
- [x] Choix de la couleur et de la tolérance

### ✏️ Texte
- [x] Texte simple sur l'image
- [x] Texte avec fond coloré
- [x] Texte avec couleur issue d'une image

### 🔄 Transformations
- [x] Rotation 90° (droite et gauche)
- [x] Rotation 180°
- [x] Ajustement de luminosité
- [x] Ajustement de contraste
- [x] Décalage de teinte (Hue shift)
- [x] Conversion en niveaux de gris
- [x] Négatif (inversion des couleurs)

---

## 🎓 Points Pédagogiques Clés

### 1. Représentation d'une Image

Une image est une matrice de pixels. Chaque pixel est codé sur 32 bits (ARGB) :
- **Alpha** (bits 24-31) : transparence
- **Rouge** (bits 16-23) : composante rouge
- **Vert** (bits 8-15) : composante verte
- **Bleu** (bits 0-7) : composante bleue

### 2. Manipulation Pixel par Pixel

```java
// Lecture d'un pixel
int couleur = image.getRGB(x, y);

// Extraction des composantes
int alpha = (couleur >> 24) & 0xFF;
int rouge = (couleur >> 16) & 0xFF;
int vert  = (couleur >> 8)  & 0xFF;
int bleu  = couleur & 0xFF;

// Écriture d'un pixel
int nouvelleCouleur = (alpha << 24) | (rouge << 16) | (vert << 8) | bleu;
image.setRGB(x, y, nouvelleCouleur);
```

### 3. Algorithme Flood Fill

Problème : La récursion peut provoquer un StackOverflow sur les grandes zones.

Solution : Utiliser une file (Queue) pour stocker les pixels à traiter.

```java
Queue<int[]> file = new LinkedList<>();
file.add(new int[] {startX, startY});

while (!file.isEmpty()) {
    int[] pixel = file.poll();
    // Traiter le pixel et ajouter ses voisins
}
```

### 4. Distance entre Couleurs

Pour comparer deux couleurs, on utilise la distance euclidienne RGB :

```
distance = √[(R1-R2)² + (G1-G2)² + (B1-B2)²]
```

Distance max ≈ 441 (noir ↔ blanc)

### 5. Alpha Blending

Pour mélanger deux couleurs avec transparence :

```
résultat = (alpha × source + (255 - alpha) × destination) / 255
```

---

## 👥 Auteurs

- **Équipe 6** - BUT 3 Informatique
- Matière : Programmation Multimédia
- Année : 2024-2025

---

## 📝 Notes pour la Démonstration

1. **Commencer** par charger une image PNG
2. **Montrer** les transformations de base (rotation, luminosité)
3. **Démontrer** le pot de peinture avec différentes tolérances
4. **Illustrer** la fusion de deux images
5. **Terminer** par l'ajout de texte coloré

Chaque fonctionnalité peut être expliquée en montrant le code correspondant dans la classe dédiée.

