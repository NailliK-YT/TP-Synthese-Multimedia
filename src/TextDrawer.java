import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * ============================================================================
 * CLASSE POUR LE DESSIN DE TEXTE
 * ============================================================================
 * 
 * Cette classe permet de dessiner du texte sur une image avec :
 * - Un fond coloré derrière le texte
 * - La possibilité de définir la couleur du texte depuis une image
 * - Différentes options de style (police, taille, etc.)
 * 
 * On utilise Graphics2D pour le rendu du texte.
 * 
 * @author Équipe 6 - BUT 3 Informatique
 */
public class TextDrawer {

    // ========================================================================
    // DESSIN DE TEXTE SIMPLE
    // ========================================================================

    /**
     * Dessine du texte sur une image à une position donnée.
     * 
     * PRINCIPE :
     * 1. Obtenir le contexte Graphics2D de l'image
     * 2. Configurer la police et la couleur
     * 3. Dessiner le texte avec drawString()
     * 
     * @param image L'image sur laquelle dessiner (sera modifiée)
     * @param texte Le texte à dessiner
     * @param x Position X du texte
     * @param y Position Y du texte (ligne de base)
     * @param couleur Couleur du texte
     * @param taillePolice Taille de la police
     * @return L'image avec le texte
     */
    public static BufferedImage dessinerTexte(BufferedImage image, String texte,
                                              int x, int y, Color couleur, int taillePolice) {
        // Copie de l'image pour ne pas modifier l'originale
        BufferedImage resultat = ImageUtil.copierImage(image);
        
        // Obtention du contexte graphique 2D
        Graphics2D g2d = resultat.createGraphics();
        
        // Activation de l'antialiasing pour un texte plus lisse
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Configuration de la police
        Font police = new Font("SansSerif", Font.BOLD, taillePolice);
        g2d.setFont(police);
        
        // Configuration de la couleur
        g2d.setColor(couleur);
        
        // Dessin du texte
        // ATTENTION : y correspond à la ligne de base du texte, pas au haut
        g2d.drawString(texte, x, y);
        
        // Libération des ressources
        g2d.dispose();
        
        return resultat;
    }

    // ========================================================================
    // DESSIN DE TEXTE AVEC FOND
    // ========================================================================

    /**
     * Dessine du texte avec un rectangle de fond derrière.
     * 
     * ALGORITHME :
     * 1. Calculer les dimensions du texte avec FontMetrics
     * 2. Dessiner un rectangle de fond (avec marge)
     * 3. Dessiner le texte par-dessus
     * 
     * @param image L'image sur laquelle dessiner
     * @param texte Le texte à dessiner
     * @param x Position X
     * @param y Position Y
     * @param couleurTexte Couleur du texte
     * @param couleurFond Couleur du fond
     * @param taillePolice Taille de la police
     * @param marge Marge autour du texte (en pixels)
     * @return L'image avec le texte et son fond
     */
    public static BufferedImage dessinerTexteAvecFond(BufferedImage image, String texte,
                                                      int x, int y,
                                                      Color couleurTexte, Color couleurFond,
                                                      int taillePolice, int marge) {
        BufferedImage resultat = ImageUtil.copierImage(image);
        Graphics2D g2d = resultat.createGraphics();
        
        // Activation de l'antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Configuration de la police
        Font police = new Font("SansSerif", Font.BOLD, taillePolice);
        g2d.setFont(police);
        
        // Obtention des métriques de police pour calculer les dimensions du texte
        FontMetrics metrics = g2d.getFontMetrics();
        
        // Dimensions du texte
        int largeurTexte = metrics.stringWidth(texte);
        int hauteurTexte = metrics.getHeight();
        int ascent = metrics.getAscent(); // Distance de la ligne de base au haut
        
        // Coordonnées du rectangle de fond
        // On ajuste pour que y soit le haut du rectangle, pas la ligne de base
        int rectX = x - marge;
        int rectY = y - ascent - marge;
        int rectLargeur = largeurTexte + 2 * marge;
        int rectHauteur = hauteurTexte + 2 * marge;
        
        // Dessin du rectangle de fond
        g2d.setColor(couleurFond);
        g2d.fillRect(rectX, rectY, rectLargeur, rectHauteur);
        
        // Dessin du texte
        g2d.setColor(couleurTexte);
        g2d.drawString(texte, x, y);
        
        g2d.dispose();
        return resultat;
    }

    // ========================================================================
    // TEXTE AVEC COULEUR ISSUE D'UNE IMAGE
    // ========================================================================

    /**
     * Dessine du texte dont la couleur est extraite d'une image de référence.
     * 
     * PRINCIPE :
     * Pour chaque pixel du texte, on prend la couleur correspondante
     * dans l'image de référence (comme un masque).
     * 
     * ALGORITHME :
     * 1. Créer une image temporaire avec le texte en blanc sur fond transparent
     * 2. Pour chaque pixel blanc, récupérer la couleur de l'image source
     * 3. Appliquer cette couleur au pixel
     * 
     * @param imageDestination L'image sur laquelle dessiner
     * @param imageCouleurs L'image source des couleurs
     * @param texte Le texte à dessiner
     * @param x Position X
     * @param y Position Y
     * @param taillePolice Taille de la police
     * @return L'image avec le texte coloré
     */
    public static BufferedImage dessinerTexteAvecCouleurImage(
            BufferedImage imageDestination, BufferedImage imageCouleurs,
            String texte, int x, int y, int taillePolice) {
        
        BufferedImage resultat = ImageUtil.copierImage(imageDestination);
        
        // Création d'une image temporaire pour le masque du texte
        // On dessine le texte en blanc sur fond transparent
        BufferedImage masque = ImageUtil.creerImageVide(
            resultat.getWidth(), resultat.getHeight()
        );
        
        Graphics2D gMasque = masque.createGraphics();
        gMasque.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                 RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        Font police = new Font("SansSerif", Font.BOLD, taillePolice);
        gMasque.setFont(police);
        gMasque.setColor(Color.WHITE);
        gMasque.drawString(texte, x, y);
        gMasque.dispose();
        
        // Application des couleurs de l'image source au texte
        for (int py = 0; py < masque.getHeight(); py++) {
            for (int px = 0; px < masque.getWidth(); px++) {
                int couleurMasque = masque.getRGB(px, py);
                int[] compMasque = ImageUtil.extraireComposantes(couleurMasque);
                
                // Si le pixel du masque est blanc (texte)
                if (compMasque[1] > 128) { // Rouge > 128 (proche du blanc)
                    // On récupère la couleur de l'image source
                    // On utilise modulo pour répéter l'image si elle est plus petite
                    int srcX = px % imageCouleurs.getWidth();
                    int srcY = py % imageCouleurs.getHeight();
                    int couleurSource = imageCouleurs.getRGB(srcX, srcY);
                    
                    // On applique la couleur avec l'alpha du masque
                    int[] compSource = ImageUtil.extraireComposantes(couleurSource);
                    int nouvelleCouleur = ImageUtil.combinerComposantes(
                        compMasque[0], // Alpha du masque (pour l'antialiasing)
                        compSource[1], compSource[2], compSource[3]
                    );
                    
                    resultat.setRGB(px, py, nouvelleCouleur);
                }
            }
        }
        
        return resultat;
    }

    // ========================================================================
    // TEXTE AVEC FOND ET COULEUR ISSUE D'UNE IMAGE
    // ========================================================================

    /**
     * Dessine du texte avec un fond, la couleur du texte venant d'une image.
     * 
     * Combine les deux fonctionnalités précédentes.
     * 
     * @param imageDestination L'image de destination
     * @param imageCouleurs L'image source des couleurs du texte
     * @param texte Le texte à dessiner
     * @param x Position X
     * @param y Position Y
     * @param couleurFond Couleur du fond
     * @param taillePolice Taille de la police
     * @param marge Marge autour du texte
     * @return L'image avec le texte coloré et son fond
     */
    public static BufferedImage dessinerTexteComplet(
            BufferedImage imageDestination, BufferedImage imageCouleurs,
            String texte, int x, int y,
            Color couleurFond, int taillePolice, int marge) {
        
        BufferedImage resultat = ImageUtil.copierImage(imageDestination);
        Graphics2D g2d = resultat.createGraphics();
        
        // Configuration
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Font police = new Font("SansSerif", Font.BOLD, taillePolice);
        g2d.setFont(police);
        
        // Calcul des dimensions
        FontMetrics metrics = g2d.getFontMetrics();
        int largeurTexte = metrics.stringWidth(texte);
        int hauteurTexte = metrics.getHeight();
        int ascent = metrics.getAscent();
        
        // Dessin du fond
        g2d.setColor(couleurFond);
        g2d.fillRect(x - marge, y - ascent - marge,
                    largeurTexte + 2 * marge, hauteurTexte + 2 * marge);
        
        g2d.dispose();
        
        // Application du texte avec couleur de l'image
        return dessinerTexteAvecCouleurImage(resultat, imageCouleurs, texte, x, y, taillePolice);
    }

    // ========================================================================
    // UTILITAIRES POUR LES COULEURS
    // ========================================================================

    /**
     * Convertit un entier ARGB en objet Color.
     */
    public static Color versColor(int argb) {
        int[] comp = ImageUtil.extraireComposantes(argb);
        return new Color(comp[1], comp[2], comp[3], comp[0]);
    }

    /**
     * Convertit un objet Color en entier ARGB.
     */
    public static int versARGB(Color couleur) {
        return ImageUtil.combinerComposantes(
            couleur.getAlpha(), couleur.getRed(), couleur.getGreen(), couleur.getBlue()
        );
    }

    /**
     * Couleurs prédéfinies pour faciliter l'utilisation.
     */
    public static final Color NOIR = Color.BLACK;
    public static final Color BLANC = Color.WHITE;
    public static final Color ROUGE = Color.RED;
    public static final Color VERT = Color.GREEN;
    public static final Color BLEU = Color.BLUE;
    public static final Color JAUNE = Color.YELLOW;
    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    
    // Fond semi-transparent
    public static final Color FOND_SEMI_TRANSPARENT = new Color(0, 0, 0, 128);
}

