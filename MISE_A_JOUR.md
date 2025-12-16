# Mise à Jour - Positionnement des Images

## Nouvelle Fonctionnalité

### Définir Position Image 2

Un nouveau bouton a été ajouté dans la section **FUSION 2 IMAGES** :

**Bouton** : `Definir Position Image 2`

### Comment Utiliser

1. **Charger les images** :
   - Ouvrir Image 1 (image de fond)
   - Ouvrir Image 2 (image à superposer)

2. **Cliquer sur "Definir Position Image 2"**

3. **Dialogue interactif** :
   ```
   ┌─────────────────────────────────┐
   │ Définir la position de l'Image 2│
   ├─────────────────────────────────┤
   │ Position X : [    0    ]        │
   │ Position Y : [    0    ]        │
   │ Dimensions:                     │
   │   Image 1: 1920x1080            │
   │   Image 2: 640x480              │
   │                                 │
   │        [OK]    [Annuler]        │
   └─────────────────────────────────┘
   ```

4. **Saisir les coordonnées** :
   - **X** : position horizontale (0 = bord gauche)
   - **Y** : position verticale (0 = bord haut)

5. **Aperçu automatique** :
   - Dès validation, un aperçu s'affiche
   - Vous voyez exactement où Image 2 sera placée sur Image 1

6. **Ajuster si nécessaire** :
   - Refaire "Definir Position Image 2" pour repositionner
   - Ou utiliser directement les boutons de fusion

### Exemple d'Usage

#### Centrer l'Image 2 sur l'Image 1

```
Image 1 : 1920x1080
Image 2 : 640x480

Pour centrer :
X = (1920 - 640) / 2 = 640
Y = (1080 - 480) / 2 = 300
```

#### Coin supérieur droit

```
Image 1 : 1920x1080
Image 2 : 640x480

Position coin supérieur droit :
X = 1920 - 640 = 1280
Y = 0
```

### Workflow Complet

```
1. Ouvrir Image 1 (fond)
2. Ouvrir Image 2 (à superposer)
3. Definir Position Image 2
   → Saisir X, Y
   → Voir l'aperçu
4. Si OK, utiliser :
   - Superposer
   - Avec Transparence
   - Fond Vert (Chroma)
   - Melanger 50/50
5. Sauvegarder
```

### Avantages

✅ **Visualisation immédiate** : Aperçu avant la fusion finale
✅ **Contrôle précis** : Positionnement au pixel près
✅ **Info dimensions** : Affichage des tailles pour faciliter le calcul
✅ **Barre de statut** : Position actuelle toujours visible

### Barre de Statut

Après avoir défini la position, la barre en bas affiche :
```
Position Image 2 : (640, 300)
```

Et après l'aperçu :
```
Aperçu affiché - Position : (640, 300)
```

---

## Interface Mise à Jour

### Section FUSION 2 IMAGES (nouvelle organisation)

```
┌────────────────────────────┐
│  FUSION 2 IMAGES           │
├────────────────────────────┤
│ Definir Position Image 2   │  ← NOUVEAU !
│ Superposer                 │
│ Avec Transparence          │
│ Fond Vert (Chroma)         │
│ Melanger 50/50             │
└────────────────────────────┘
```

---

## Mise à Jour des Scripts

L'application a été recompilée avec cette nouvelle fonctionnalité.

Pour utiliser :
```powershell
.\run.ps1    # ou run.bat
```

---

**Date de mise à jour** : 16 décembre 2025
