package modele.traitement;

import modele.UtilitaireImage;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

/**
 * ============================================================================
 * CLASSE POUR LE POT DE PEINTURE (FLOOD FILL)
 * ============================================================================
 * 
 * Cette classe implémente l'algorithme de remplissage par diffusion.
 * 
 * PRINCIPE :
 * À partir d'un pixel de départ, on colorie tous les pixels "connectés"
 * qui ont une couleur similaire (selon une tolérance).
 * 
 * CHOIX D'IMPLÉMENTATION :
 * On utilise une FILE (Queue) plutôt que la récursion pour éviter
 * les débordements de pile (StackOverflowError) sur les grandes zones.
 * 
 * @author Équipe 6 - BUT 3 Informatique
 */
public class TraitementRemplissage {

    // ========================================================================
    // POT DE PEINTURE AVEC FILE (ÉVITE LE STACK OVERFLOW)
    // ========================================================================

    /**
     * Remplit une zone avec une nouvelle couleur (pot de peinture).
     * 
     * ALGORITHME (Breadth-First Search) :
     * 1. On part du pixel de départ
     * 2. On l'ajoute à une file d'attente
     * 3. Tant que la file n'est pas vide :
     * a. On retire un pixel de la file
     * b. Si sa couleur est proche de la couleur d'origine (selon la tolérance)
     * c. On le colorie avec la nouvelle couleur
     * d. On ajoute ses 4 voisins à la file
     * 
     * POURQUOI UNE FILE ?
     * La récursion utilise la pile d'appels, qui est limitée.
     * Sur une grande zone (ex: 1000x1000 pixels), on risque un StackOverflowError.
     * La file utilise la mémoire heap, beaucoup plus grande.
     * 
     * @param image           L'image à modifier (sera modifiée directement)
     * @param startX          Position X de départ
     * @param startY          Position Y de départ
     * @param nouvelleCouleur La couleur de remplissage (ARGB)
     * @param tolerance       Distance maximale pour considérer deux couleurs comme
     *                        similaires
     * @return L'image modifiée
     */
    public static BufferedImage remplir(BufferedImage image, int startX, int startY,
            int nouvelleCouleur, double tolerance) {
        // Copie de l'image pour ne pas modifier l'originale
        BufferedImage resultat = UtilitaireImage.copierImage(image);

        int largeur = resultat.getWidth();
        int hauteur = resultat.getHeight();

        // Vérification des coordonnées de départ
        if (startX < 0 || startX >= largeur || startY < 0 || startY >= hauteur) {
            System.err.println("ERREUR : Coordonnées de départ hors de l'image");
            return resultat;
        }

        // Couleur d'origine (celle qu'on veut remplacer)
        int couleurOrigine = resultat.getRGB(startX, startY);

        // Si la couleur d'origine est déjà la nouvelle couleur, rien à faire
        if (UtilitaireImage.distanceCouleur(couleurOrigine, nouvelleCouleur) == 0) {
            System.out.println("La zone est déjà de la couleur demandée");
            return resultat;
        }

        // Tableau pour marquer les pixels déjà visités
        // Cela évite de traiter plusieurs fois le même pixel
        boolean[][] visite = new boolean[largeur][hauteur];

        // File d'attente pour les pixels à traiter
        // On stocke les coordonnées sous forme de tableau [x, y]
        Queue<int[]> file = new LinkedList<>();

        // On commence par le pixel de départ
        file.add(new int[] { startX, startY });
        visite[startX][startY] = true;

        // Compteur pour affichage (debug)
        int pixelsRemplis = 0;

        // Traitement de la file
        while (!file.isEmpty()) {
            // On retire le premier pixel de la file
            int[] pixel = file.poll();
            int x = pixel[0];
            int y = pixel[1];

            // Récupération de la couleur du pixel actuel
            int couleurActuelle = resultat.getRGB(x, y);

            // Vérification si la couleur est assez proche de l'origine
            double distance = UtilitaireImage.distanceCouleur(couleurActuelle, couleurOrigine);

            if (distance <= tolerance) {
                // On colorie le pixel
                resultat.setRGB(x, y, nouvelleCouleur);
                pixelsRemplis++;

                // On ajoute les 4 voisins (haut, bas, gauche, droite)
                ajouterVoisin(file, visite, largeur, hauteur, x - 1, y); // Gauche
                ajouterVoisin(file, visite, largeur, hauteur, x + 1, y); // Droite
                ajouterVoisin(file, visite, largeur, hauteur, x, y - 1); // Haut
                ajouterVoisin(file, visite, largeur, hauteur, x, y + 1); // Bas
            }
        }

        System.out.println("Flood Fill terminé : " + pixelsRemplis + " pixels remplis");
        return resultat;
    }

    /**
     * Ajoute un voisin à la file s'il est valide et non visité.
     */
    private static void ajouterVoisin(Queue<int[]> file, boolean[][] visite,
            int largeur, int hauteur, int x, int y) {
        // Vérification des limites
        if (x >= 0 && x < largeur && y >= 0 && y < hauteur) {
            // Vérification que le pixel n'a pas été visité
            if (!visite[x][y]) {
                visite[x][y] = true;
                file.add(new int[] { x, y });
            }
        }
    }

    // ========================================================================
    // POT DE PEINTURE AVEC 8 DIRECTIONS (DIAGONALES INCLUSES)
    // ========================================================================

    /**
     * Version du pot de peinture qui remplit aussi en diagonale.
     * 
     * DIFFÉRENCE :
     * Au lieu de 4 voisins, on considère 8 voisins (les 4 diagonales en plus).
     * Le remplissage est plus "complet" mais peut dépasser les bordures fines.
     * 
     * @param image           L'image à modifier
     * @param startX          Position X de départ
     * @param startY          Position Y de départ
     * @param nouvelleCouleur La couleur de remplissage
     * @param tolerance       Distance maximale de couleur
     * @return L'image modifiée
     */
    public static BufferedImage remplir8Directions(BufferedImage image, int startX, int startY,
            int nouvelleCouleur, double tolerance) {
        BufferedImage resultat = UtilitaireImage.copierImage(image);

        int largeur = resultat.getWidth();
        int hauteur = resultat.getHeight();

        if (startX < 0 || startX >= largeur || startY < 0 || startY >= hauteur) {
            return resultat;
        }

        int couleurOrigine = resultat.getRGB(startX, startY);

        if (UtilitaireImage.distanceCouleur(couleurOrigine, nouvelleCouleur) == 0) {
            return resultat;
        }

        boolean[][] visite = new boolean[largeur][hauteur];
        Queue<int[]> file = new LinkedList<>();

        file.add(new int[] { startX, startY });
        visite[startX][startY] = true;

        // Les 8 directions : haut, bas, gauche, droite + 4 diagonales
        int[] dx = { -1, 0, 1, -1, 1, -1, 0, 1 };
        int[] dy = { -1, -1, -1, 0, 0, 1, 1, 1 };

        while (!file.isEmpty()) {
            int[] pixel = file.poll();
            int x = pixel[0];
            int y = pixel[1];

            int couleurActuelle = resultat.getRGB(x, y);
            double distance = UtilitaireImage.distanceCouleur(couleurActuelle, couleurOrigine);

            if (distance <= tolerance) {
                resultat.setRGB(x, y, nouvelleCouleur);

                // Ajout des 8 voisins
                for (int i = 0; i < 8; i++) {
                    int nx = x + dx[i];
                    int ny = y + dy[i];
                    ajouterVoisin(file, visite, largeur, hauteur, nx, ny);
                }
            }
        }

        return resultat;
    }

    // ========================================================================
    // VERSION RÉCURSIVE (POUR PETITES ZONES UNIQUEMENT)
    // ========================================================================

    /**
     * Version récursive du pot de peinture.
     * 
     * ⚠️ ATTENTION : Cette version peut provoquer un StackOverflowError
     * sur les grandes zones. À utiliser uniquement pour des petites images
     * ou des démonstrations pédagogiques.
     * 
     * PRINCIPE DE LA RÉCURSION :
     * La fonction s'appelle elle-même pour chaque voisin valide.
     * C'est plus simple à comprendre mais moins robuste.
     * 
     * @param image           L'image à modifier
     * @param x               Position X actuelle
     * @param y               Position Y actuelle
     * @param nouvelleCouleur La couleur de remplissage
     * @param couleurOrigine  La couleur à remplacer
     * @param tolerance       Distance maximale
     */
    public static void remplirRecursif(BufferedImage image, int x, int y,
            int nouvelleCouleur, int couleurOrigine,
            double tolerance) {
        // Conditions d'arrêt
        if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) {
            return; // Hors limites
        }

        int couleurActuelle = image.getRGB(x, y);

        // Si le pixel a déjà la nouvelle couleur, on s'arrête
        if (couleurActuelle == nouvelleCouleur) {
            return;
        }

        // Si la couleur est trop différente de l'origine, on s'arrête
        if (UtilitaireImage.distanceCouleur(couleurActuelle, couleurOrigine) > tolerance) {
            return;
        }

        // On colorie le pixel
        image.setRGB(x, y, nouvelleCouleur);

        // Appels récursifs sur les 4 voisins
        remplirRecursif(image, x - 1, y, nouvelleCouleur, couleurOrigine, tolerance);
        remplirRecursif(image, x + 1, y, nouvelleCouleur, couleurOrigine, tolerance);
        remplirRecursif(image, x, y - 1, nouvelleCouleur, couleurOrigine, tolerance);
        remplirRecursif(image, x, y + 1, nouvelleCouleur, couleurOrigine, tolerance);
    }

    // ========================================================================
    // UTILITAIRES
    // ========================================================================

    /**
     * Crée une couleur ARGB à partir de composantes RGB.
     * 
     * @param rouge Composante rouge (0-255)
     * @param vert  Composante verte (0-255)
     * @param bleu  Composante bleue (0-255)
     * @return La couleur au format ARGB (opaque)
     */
    public static int creerCouleur(int rouge, int vert, int bleu) {
        return UtilitaireImage.combinerComposantes(255, rouge, vert, bleu);
    }

    /**
     * Calcule une tolérance recommandée pour le flood fill.
     * 
     * GUIDE :
     * - 0 : Exactement la même couleur uniquement
     * - 10-30 : Couleurs très proches (dégradés légers)
     * - 30-60 : Couleurs similaires (variations de lumière)
     * - 60-100 : Couleurs différentes mais dans le même ton
     * - >100 : Très permissif
     * 
     * La distance maximale entre deux couleurs est √(255² + 255² + 255²) ≈ 441
     */
    public static final double TOLERANCE_EXACTE = 0;
    public static final double TOLERANCE_FAIBLE = 20;
    public static final double TOLERANCE_MOYENNE = 50;
    public static final double TOLERANCE_FORTE = 100;
}
