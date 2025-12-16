package modele.traitement;

import modele.UtilitaireImage;
import java.awt.image.BufferedImage;

/**
 * ============================================================================
 * CLASSE POUR LA FUSION ET SUPERPOSITION D'IMAGES
 * ============================================================================
 * 
 * Cette classe gère :
 * - La superposition d'une image sur une autre à une position donnée
 * - La fusion de deux images avec transparence
 * - La gestion d'une couleur transparente (clé de chrominance)
 * 
 * @author Équipe 6 - BUT 3 Informatique
 */
public class TraitementFusion {

    /**
     * Superpose une image sur une autre à une position donnée.
     * 
     * PRINCIPE :
     * On copie les pixels de l'image source sur l'image de destination,
     * en commençant à la position (posX, posY).
     * 
     * ALGORITHME :
     * 1. Pour chaque pixel de l'image source
     * 2. Calculer sa position sur l'image destination
     * 3. Si la position est valide, copier le pixel
     * 
     * @param destination L'image de fond (sera modifiée)
     * @param source      L'image à superposer
     * @param posX        Position X du coin supérieur gauche
     * @param posY        Position Y du coin supérieur gauche
     * @return L'image résultante
     */
    public static BufferedImage superposer(BufferedImage destination, BufferedImage source,
            int posX, int posY) {
        BufferedImage resultat = UtilitaireImage.copierImage(destination);

        for (int y = 0; y < source.getHeight(); y++) {
            for (int x = 0; x < source.getWidth(); x++) {
                int destX = posX + x;
                int destY = posY + y;

                if (destX >= 0 && destX < resultat.getWidth() &&
                        destY >= 0 && destY < resultat.getHeight()) {

                    int couleurSource = source.getRGB(x, y);

                    resultat.setRGB(destX, destY, couleurSource);
                }
            }
        }

        return resultat;
    }

    /**
     * Superpose une image sur une autre en respectant la transparence.
     * 
     * PRINCIPE DE L'ALPHA BLENDING :
     * La couleur finale est un mélange entre source et destination,
     * pondéré par l'alpha de la source.
     * 
     * FORMULE :
     * couleurFinale = (alpha * couleurSource + (255 - alpha) * couleurDest) / 255
     * 
     * @param destination L'image de fond
     * @param source      L'image à superposer (avec transparence)
     * @param posX        Position X
     * @param posY        Position Y
     * @return L'image résultante avec transparence gérée
     */
    public static BufferedImage superposerAvecAlpha(BufferedImage destination, BufferedImage source,
            int posX, int posY) {
        BufferedImage resultat = UtilitaireImage.copierImage(destination);

        for (int y = 0; y < source.getHeight(); y++) {
            for (int x = 0; x < source.getWidth(); x++) {
                int destX = posX + x;
                int destY = posY + y;

                if (destX >= 0 && destX < resultat.getWidth() &&
                        destY >= 0 && destY < resultat.getHeight()) {

                    int couleurSource = source.getRGB(x, y);
                    int couleurDest = resultat.getRGB(destX, destY);

                    int[] compSource = UtilitaireImage.extraireComposantes(couleurSource);
                    int[] compDest = UtilitaireImage.extraireComposantes(couleurDest);

                    int alphaSource = compSource[0];

                    if (alphaSource == 0) {
                        continue;
                    }

                    if (alphaSource == 255) {
                        resultat.setRGB(destX, destY, couleurSource);
                        continue;
                    }

                    int nouveauRouge = melanger(compSource[1], compDest[1], alphaSource);
                    int nouveauVert = melanger(compSource[2], compDest[2], alphaSource);
                    int nouveauBleu = melanger(compSource[3], compDest[3], alphaSource);

                    int nouveauAlpha = Math.max(alphaSource, compDest[0]);

                    int nouvelleCouleur = UtilitaireImage.combinerComposantes(
                            nouveauAlpha, nouveauRouge, nouveauVert, nouveauBleu);
                    resultat.setRGB(destX, destY, nouvelleCouleur);
                }
            }
        }

        return resultat;
    }

    /**
     * Mélange deux valeurs selon un alpha donné.
     * 
     * @param valeurSource Valeur de la source (0-255)
     * @param valeurDest   Valeur de la destination (0-255)
     * @param alpha        Niveau de mélange (0-255)
     * @return La valeur mélangée
     */
    private static int melanger(int valeurSource, int valeurDest, int alpha) {
        return (alpha * valeurSource + (255 - alpha) * valeurDest) / 255;
    }

    /**
     * Superpose une image en rendant une couleur spécifique transparente.
     * 
     * PRINCIPE (FOND VERT / CHROMA KEY) :
     * On définit une couleur "clé" qui sera considérée comme transparente.
     * Les pixels de cette couleur ne seront pas copiés.
     * 
     * UTILITÉ :
     * - Supprimer un fond uni (vert, bleu, magenta...)
     * - Intégrer un personnage sur un décor
     * 
     * @param destination         L'image de fond
     * @param source              L'image avec le fond à retirer
     * @param posX                Position X
     * @param posY                Position Y
     * @param couleurTransparente La couleur à rendre transparente (RGB)
     * @param tolerance           Tolérance pour la détection (0 = exact, >0 =
     *                            approximatif)
     * @return L'image résultante
     */
    public static BufferedImage superposerAvecCleTransparence(
            BufferedImage destination, BufferedImage source,
            int posX, int posY,
            int couleurTransparente, double tolerance) {

        BufferedImage resultat = UtilitaireImage.copierImage(destination);

        for (int y = 0; y < source.getHeight(); y++) {
            for (int x = 0; x < source.getWidth(); x++) {
                int destX = posX + x;
                int destY = posY + y;

                if (destX >= 0 && destX < resultat.getWidth() &&
                        destY >= 0 && destY < resultat.getHeight()) {

                    int couleurPixel = source.getRGB(x, y);

                    double distance = UtilitaireImage.distanceCouleur(couleurPixel, couleurTransparente);

                    if (distance > tolerance) {
                        resultat.setRGB(destX, destY, couleurPixel);
                    }
                }
            }
        }

        return resultat;
    }

    /**
     * Juxtapose deux images horizontalement côte à côte avec une zone de fondu.
     * 
     * PRINCIPE :
     * Crée une nouvelle image avec une zone de transition progressive entre les
     * deux images.
     * La zone de fondu mélange progressivement les pixels des deux images.
     * 
     * ALGORITHME :
     * 1. Image1 pure à gauche
     * 2. Zone de fondu au centre (mélange progressif)
     * 3. Image2 pure à droite
     * 
     * @param image1       Première image (à gauche)
     * @param image2       Deuxième image (à droite)
     * @param largeurFondu Largeur de la zone de transition (en pixels)
     * @return L'image résultante avec fondu
     */
    public static BufferedImage juxtaposerHorizontalement(BufferedImage image1, BufferedImage image2,
            int largeurFondu) {
        int largeur1 = image1.getWidth();
        int largeur2 = image2.getWidth();
        int hauteur1 = image1.getHeight();
        int hauteur2 = image2.getHeight();

        int largeurTotale = largeur1 + largeur2 - largeurFondu;
        int hauteurMax = Math.max(hauteur1, hauteur2);

        BufferedImage resultat = UtilitaireImage.creerImageVide(largeurTotale, hauteurMax);

        for (int y = 0; y < hauteurMax; y++) {
            for (int x = 0; x < largeurTotale; x++) {
                int positionDansZoneFondu = x - (largeur1 - largeurFondu);

                if (x < largeur1 - largeurFondu) {
                    if (y < hauteur1) {
                        int couleur = image1.getRGB(x, y);
                        resultat.setRGB(x, y, couleur);
                    }
                } else if (positionDansZoneFondu >= 0 && positionDansZoneFondu < largeurFondu) {
                    double ratio = (double) positionDansZoneFondu / largeurFondu;

                    int x1 = largeur1 - largeurFondu + positionDansZoneFondu;
                    int x2 = positionDansZoneFondu;

                    int couleur1 = (x1 < largeur1 && y < hauteur1) ? image1.getRGB(x1, y) : 0x00000000;
                    int couleur2 = (x2 < largeur2 && y < hauteur2) ? image2.getRGB(x2, y) : 0x00000000;

                    int[] comp1 = UtilitaireImage.extraireComposantes(couleur1);
                    int[] comp2 = UtilitaireImage.extraireComposantes(couleur2);

                    int alpha = (int) ((1 - ratio) * comp1[0] + ratio * comp2[0]);
                    int rouge = (int) ((1 - ratio) * comp1[1] + ratio * comp2[1]);
                    int vert = (int) ((1 - ratio) * comp1[2] + ratio * comp2[2]);
                    int bleu = (int) ((1 - ratio) * comp1[3] + ratio * comp2[3]);

                    int couleurMelangee = UtilitaireImage.combinerComposantes(alpha, rouge, vert, bleu);
                    resultat.setRGB(x, y, couleurMelangee);
                } else {
                    int x2 = x - (largeur1 - largeurFondu);
                    if (x2 < largeur2 && y < hauteur2) {
                        int couleur = image2.getRGB(x2, y);
                        resultat.setRGB(x, y, couleur);
                    }
                }
            }
        }

        return resultat;
    }

    /**
     * Crée une couleur ARGB à partir de valeurs RGB.
     * Utile pour définir la couleur de transparence.
     * 
     * @param rouge Composante rouge (0-255)
     * @param vert  Composante verte (0-255)
     * @param bleu  Composante bleue (0-255)
     * @return La couleur au format ARGB (opaque)
     */
    public static int creerCouleurRGB(int rouge, int vert, int bleu) {
        return UtilitaireImage.combinerComposantes(255, rouge, vert, bleu);
    }

    /**
     * Constantes pour les couleurs de fond courantes (chroma key).
     */
    public static final int VERT_CHROMA = creerCouleurRGB(0, 255, 0);
    public static final int BLEU_CHROMA = creerCouleurRGB(0, 0, 255);
    public static final int MAGENTA_CHROMA = creerCouleurRGB(255, 0, 255);
}
