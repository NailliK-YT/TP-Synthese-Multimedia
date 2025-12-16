# Comprendre les Modes de Fusion

## Pourquoi "Avec Transparence" et "Fond Vert" semblent ne rien faire ?

Ces fonctions ont des **cas d'usage spécifiques**. Voici quand les utiliser :

---

## 1. SUPERPOSER (simple)

**Quand utiliser** : Toujours, c'est la base

**Ce qui se passe** :
- Prend Image 2 et la colle sur Image 1
- **Remplace complètement** les pixels de Image 1
- Aucune transparence, aucun mélange

**Exemple** :
```
Image 1 : [Photo de paysage]
Image 2 : [Logo carré 200x200]
Résultat : Logo collé, cache complètement le paysage en dessous
```

---

## 2. AVEC TRANSPARENCE (alpha blending)

**Quand utiliser** : Quand Image 2 a de la **transparence** (canal alpha)

**Ce qui se passe** :
- Lit le canal alpha de Image 2
- Les parties transparentes laissent voir Image 1
- Les parties opaques recouvrent Image 1

**Pour que ça marche** :
✅ Image 2 doit être un PNG **avec transparence**
❌ Si Image 2 est opaque partout → même résultat que "Superposer"

**Exemple qui MARCHE** :
```
Image 1 : [Photo de paysage]
Image 2 : [Logo PNG avec fond transparent - pas de rectangle blanc]
Résultat : Logo flotte sur le paysage, fond transparent
```

**Exemple qui NE MARCHE PAS** :
```
Image 1 : [Photo]
Image 2 : [Logo PNG avec fond blanc opaque]
Résultat : Rectangle blanc visible (pas de transparence)
```

---

## 3. FOND VERT (Chroma Key)

**Quand utiliser** : Quand Image 2 a un **fond uni** (vert, bleu, etc.) à enlever

**Ce qui se passe** :
1. Vous choisissez la couleur à rendre transparente (ex: vert)
2. Vous définissez la tolérance (30 = recommandé)
3. Tous les pixels de cette couleur deviennent transparents

**Pour que ça marche** :
✅ Image 2 doit avoir un fond de couleur UNIE
✅ Cette couleur doit être différente du sujet
❌ Si pas de fond uni → rien ne se passe

**Exemple qui MARCHE** :
```
Image 1 : [Décor de ville]
Image 2 : [Personne sur fond VERT UNI]
Action : Choisir vert, tolérance 30
Résultat : Personne apparaît sur le décor, fond vert disparu
```

**Exemple qui NE MARCHE PAS** :
```
Image 1 : [Décor]
Image 2 : [Personne sur fond BLANC NORMAL]
Action : Choisir blanc
Problème : Le blanc fait partie de l'image (vêtements, etc.)
Résultat : Parties blanches disparaissent partout
```

---

## 4. MELANGER 50/50

**Quand utiliser** : Toujours (effet artistique)

**Ce qui se passe** :
- Mélange les couleurs des 2 images pixel par pixel
- 50% Image 1 + 50% Image 2
- Effet de transparence/superposition

**Exemple** :
```
Image 1 : [Ciel bleu]
Image 2 : [Nuages blancs]
Résultat : Mélange artistique ciel + nuages
```

---

## ✅ TEST : Comment Vérifier

### Test 1 : Transparence
1. Créez une image PNG avec transparence :
   - Dessinez un cercle rouge
   - Le reste doit être **transparent** (pas blanc !)
2. Ouvrir Image 1 (un paysage)
3. Ouvrir Image 2 (votre cercle PNG transparent)
4. "Avec Transparence"
5. **→ Vous devriez voir le cercle sur le paysage, sans rectangle**

### Test 2 : Fond Vert
1. Créez une image avec fond vert uni :
   - Dessinez quelque chose au centre
   - Remplissez le fond en VERT (RGB: 0, 255, 0)
2. Ouvrir Image 1 (un décor)
3. Ouvrir Image 2 (votre image à fond vert)
4. "Fond Vert (Chroma)"
5. Choisir la couleur verte exacte
6. Tolérance : 30
7. **→ Le vert disparaît, seul le dessin reste**

---

## 🔍 Debug : Pourquoi ça ne marche pas ?

### Problème : "Avec Transparence" ne fait rien

**Cause** : Image 2 n'a pas de transparence

**Solution** :
- Vérifiez que Image 2 est PNG (pas JPG)
- Ouvrez Image 2 dans un éditeur (GIMP, Photoshop)
- Vérifiez le canal alpha
- Si fond blanc opaque → utilisez "Fond Vert" à la place

### Problème : "Fond Vert" ne fait rien

**Cause 1** : La couleur choisie n'est pas dans l'image
- Solution : Utilisez la pipette pour choisir la couleur exacte

**Cause 2** : La tolérance est trop faible
- Solution : Augmentez la tolérance à 50-100

**Cause 3** : Il n'y a pas de fond uni
- Solution : Cette fonction ne marchera pas, utilisez "Superposer"

---

## 💡 Recommandation Simple

**Si vous ne savez pas quoi utiliser** :

1. **Essayez d'abord "Superposer"** → Toujours marche
2. Si Image 2 a transparence → Essayez "Avec Transparence"
3. Si Image 2 a fond vert/bleu → Essayez "Fond Vert"
4. Pour effet artistique → Essayez "Melanger 50/50"

**Astuce** : Utilisez "Definir Position Image 2" avant pour voir l'aperçu !

---

## 📸 Exemples d'Images qui Marchent

### Pour "Avec Transparence"
- Logo PNG sans fond
- Icône PNG transparente
- Sprite de jeu vidéo
- Dessin avec fond transparent

### Pour "Fond Vert"
- Photo de studio sur fond vert
- Vidéo chroma key (image extraite)
- Dessin scanné avec fond uni
- Capture d'écran avec fond monochrome

---

## ⚠️ Attention

**Fond blanc ≠ Transparence**
- Un fond blanc est **opaque**
- Il faut utiliser "Fond Vert" et choisir le blanc
- Mais attention, ça supprime TOUT le blanc !

**Solution** : Convertissez le blanc en transparence dans un éditeur d'abord, ou utilisez "Fond Vert" avec précaution.
