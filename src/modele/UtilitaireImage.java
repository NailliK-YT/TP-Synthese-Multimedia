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
            File fichier = new File(cheminFichier);

            if (!fichier.exists()) {
                System.err.println("ERREUR : Le fichier n'existe pas : " + cheminFichier);
                return null;
            }

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
            if (image == null) {
                System.err.println("ERREUR : L'image à sauvegarder est nulle");
                return false;
            }

            File fichier = new File(cheminFichier);

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

        BufferedImage copie = new BufferedImage(
                originale.getWidth(),
                originale.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < originale.getHeight(); y++) {
            for (int x = 0; x < originale.getWidth(); x++) {
                int couleur = originale.getRGB(x, y);
                copie.setRGB(x, y, couleur);
            }
        }

        return copie;
    }

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
        BufferedImage image = new BufferedImage(
                largeur,
                hauteur,
                BufferedImage.TYPE_INT_ARGB);

        return image;
    }

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
        int alpha = (couleurARGB >> 24) & 0xFF;
        int rouge = (couleurARGB >> 16) & 0xFF;
        int vert = (couleurARGB >> 8) & 0xFF;
        int bleu = couleurARGB & 0xFF;

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
        alpha = Math.max(0, Math.min(255, alpha));
        rouge = Math.max(0, Math.min(255, rouge));
        vert = Math.max(0, Math.min(255, vert));
        bleu = Math.max(0, Math.min(255, bleu));

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
        int[] c1 = extraireComposantes(couleur1);
        int[] c2 = extraireComposantes(couleur2);

        int diffRouge = c1[1] - c2[1];
        int diffVert = c1[2] - c2[2];
        int diffBleu = c1[3] - c2[3];

        return Math.sqrt(diffRouge * diffRouge + diffVert * diffVert + diffBleu * diffBleu);
    }
}
