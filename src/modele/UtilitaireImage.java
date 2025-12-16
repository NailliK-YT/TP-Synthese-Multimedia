package modele;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * ============================================================================
 * CLASSE UTILITAIRE POUR LA GESTION DES IMAGES
 * ============================================================================
 * 
 * Cette classe regroupe les opérations de base sur les images :
 * - Ouverture d'une image depuis un fichier
 * - Sauvegarde d'une image vers un fichier
 * - Copie d'une image (pour ne pas modifier l'originale)
 * 
 * Toutes les méthodes sont statiques : pas besoin d'instancier la classe.
 * 
 * @author Équipe 6 - BUT 3 Informatique
 */
public class UtilitaireImage {

    // ========================================================================
    // OUVERTURE D'IMAGE
    // ========================================================================

    /**
     * Ouvre une image PNG depuis un fichier.
     * 
     * ALGORITHME :
     * 1. Créer un objet File avec le chemin donné
     * 2. Vérifier que le fichier existe
     * 3. Utiliser ImageIO.read() pour charger l'image
     * 4. Retourner le BufferedImage ou null si erreur
     * 
     * @param cheminFichier Le chemin vers le fichier image
     * @return L'image chargée, ou null si erreur
     */
    public static BufferedImage ouvrirImage(String cheminFichier) {
        try {
            // Création de l'objet File représentant le fichier
            File fichier = new File(cheminFichier);

            // Vérification de l'existence du fichier
            if (!fichier.exists()) {
                System.err.println("ERREUR : Le fichier n'existe pas : " + cheminFichier);
                return null;
            }

            // Chargement de l'image avec ImageIO
            // ImageIO.read() gère automatiquement le format PNG
            BufferedImage image = ImageIO.read(fichier);

            if (image == null) {
                System.err.println("ERREUR : Impossible de lire l'image : " + cheminFichier);
                return null;
            }

            System.out.println("Image chargée : " + cheminFichier);
            System.out.println("  Dimensions : " + image.getWidth() + " x " + image.getHeight());

            return image;

        } catch (IOException e) {
            System.err.println("ERREUR lors de l'ouverture : " + e.getMessage());
            return null;
        }
    }

    // ========================================================================
    // SAUVEGARDE D'IMAGE
    // ========================================================================

    /**
     * Sauvegarde une image au format PNG.
     * 
     * ALGORITHME :
     * 1. Créer un objet File avec le chemin de destination
     * 2. Utiliser ImageIO.write() avec le format "PNG"
     * 3. Retourner true si succès, false sinon
     * 
     * @param image         L'image à sauvegarder
     * @param cheminFichier Le chemin de destination
     * @return true si la sauvegarde a réussi
     */
    public static boolean sauvegarderImage(BufferedImage image, String cheminFichier) {
        try {
            // Vérification que l'image n'est pas nulle
            if (image == null) {
                System.err.println("ERREUR : L'image à sauvegarder est nulle");
                return false;
            }

            // Création du fichier de destination
            File fichier = new File(cheminFichier);

            // Sauvegarde au format PNG
            // Le format PNG préserve la transparence (canal alpha)
            boolean succes = ImageIO.write(image, "PNG", fichier);

            if (succes) {
                System.out.println("Image sauvegardée : " + cheminFichier);
            } else {
                System.err.println("ERREUR : Échec de la sauvegarde");
            }

            return succes;

        } catch (IOException e) {
            System.err.println("ERREUR lors de la sauvegarde : " + e.getMessage());
            return false;
        }
    }

    // ========================================================================
    // COPIE D'IMAGE
    // ========================================================================

    /**
     * Crée une copie d'une image.
     * 
     * POURQUOI COPIER ?
     * Quand on modifie une BufferedImage, on modifie l'originale.
     * Pour garder l'originale intacte, on travaille sur une copie.
     * 
     * ALGORITHME :
     * 1. Créer une nouvelle BufferedImage de même taille et type
     * 2. Copier tous les pixels de l'originale vers la copie
     * 3. Retourner la copie
     * 
     * @param originale L'image à copier
     * @return Une nouvelle image identique
     */
    public static BufferedImage copierImage(BufferedImage originale) {
        if (originale == null) {
            return null;
        }

        // Création d'une nouvelle image de même taille
        // TYPE_INT_ARGB : 4 canaux (Alpha, Rouge, Vert, Bleu) sur 32 bits
        BufferedImage copie = new BufferedImage(
                originale.getWidth(),
                originale.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        // Copie pixel par pixel
        // On parcourt chaque pixel de l'image
        for (int y = 0; y < originale.getHeight(); y++) {
            for (int x = 0; x < originale.getWidth(); x++) {
                // getRGB() retourne la couleur du pixel (ARGB sur 32 bits)
                // setRGB() définit la couleur du pixel
                int couleur = originale.getRGB(x, y);
                copie.setRGB(x, y, couleur);
            }
        }

        return copie;
    }

    // ========================================================================
    // CRÉATION D'IMAGE VIDE
    // ========================================================================

    /**
     * Crée une image vide (transparente) de dimensions données.
     * 
     * UTILITÉ :
     * - Créer un canvas de travail
     * - Créer un fond pour la fusion d'images
     * 
     * @param largeur Largeur en pixels
     * @param hauteur Hauteur en pixels
     * @return Une nouvelle image transparente
     */
    public static BufferedImage creerImageVide(int largeur, int hauteur) {
        // TYPE_INT_ARGB permet d'avoir de la transparence
        BufferedImage image = new BufferedImage(
                largeur,
                hauteur,
                BufferedImage.TYPE_INT_ARGB);

        // L'image est automatiquement remplie de pixels transparents (0x00000000)
        return image;
    }

    // ========================================================================
    // UTILITAIRES DE COULEUR
    // ========================================================================

    /**
     * Extrait les composantes ARGB d'une couleur.
     * 
     * EXPLICATION DU FORMAT ARGB :
     * Une couleur est stockée sur 32 bits (un int) :
     * - Bits 24-31 : Alpha (transparence, 0=transparent, 255=opaque)
     * - Bits 16-23 : Rouge (0-255)
     * - Bits 8-15 : Vert (0-255)
     * - Bits 0-7 : Bleu (0-255)
     * 
     * @param couleurARGB La couleur au format ARGB
     * @return Un tableau [alpha, rouge, vert, bleu]
     */
    public static int[] extraireComposantes(int couleurARGB) {
        // Extraction par décalage de bits et masquage
        int alpha = (couleurARGB >> 24) & 0xFF; // Décalage de 24 bits, masque sur 8 bits
        int rouge = (couleurARGB >> 16) & 0xFF; // Décalage de 16 bits
        int vert = (couleurARGB >> 8) & 0xFF; // Décalage de 8 bits
        int bleu = couleurARGB & 0xFF; // Pas de décalage, juste le masque

        return new int[] { alpha, rouge, vert, bleu };
    }

    /**
     * Combine les composantes ARGB en une seule couleur.
     * 
     * @param alpha Transparence (0-255)
     * @param rouge Composante rouge (0-255)
     * @param vert  Composante verte (0-255)
     * @param bleu  Composante bleue (0-255)
     * @return La couleur combinée au format ARGB
     */
    public static int combinerComposantes(int alpha, int rouge, int vert, int bleu) {
        // On s'assure que les valeurs sont dans [0, 255]
        alpha = Math.max(0, Math.min(255, alpha));
        rouge = Math.max(0, Math.min(255, rouge));
        vert = Math.max(0, Math.min(255, vert));
        bleu = Math.max(0, Math.min(255, bleu));

        // Combinaison par décalage et OU binaire
        return (alpha << 24) | (rouge << 16) | (vert << 8) | bleu;
    }

    /**
     * Calcule la distance euclidienne entre deux couleurs RGB.
     * 
     * FORMULE :
     * distance = √[(R1-R2)² + (G1-G2)² + (B1-B2)²]
     * 
     * UTILITÉ :
     * Permet de savoir si deux couleurs sont "proches" visuellement.
     * Utilisé notamment pour le pot de peinture avec tolérance.
     * 
     * @param couleur1 Première couleur (ARGB)
     * @param couleur2 Deuxième couleur (ARGB)
     * @return La distance entre les deux couleurs (0 = identiques)
     */
    public static double distanceCouleur(int couleur1, int couleur2) {
        // Extraction des composantes RGB (on ignore l'alpha)
        int[] c1 = extraireComposantes(couleur1);
        int[] c2 = extraireComposantes(couleur2);

        // Calcul des différences au carré
        int diffRouge = c1[1] - c2[1];
        int diffVert = c1[2] - c2[2];
        int diffBleu = c1[3] - c2[3];

        // Distance euclidienne
        return Math.sqrt(diffRouge * diffRouge + diffVert * diffVert + diffBleu * diffBleu);
    }
}
