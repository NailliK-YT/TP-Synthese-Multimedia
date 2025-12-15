import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * ============================================================================
 * PROGRAMME DE TEST ET DÉMONSTRATION
 * ============================================================================
 * 
 * Ce programme permet de :
 * 1. Créer des images de test
 * 2. Tester toutes les fonctionnalités en mode console
 * 3. Servir de démonstration pour l'oral
 * 
 * @author Équipe 6 - BUT 3 Informatique
 */
public class TestDemo {

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  DÉMONSTRATION - Éditeur d'Images");
        System.out.println("  Équipe 6 - BUT 3 Informatique");
        System.out.println("===========================================\n");

        // Création des images de test
        creerImagesTest();

        // Tests des fonctionnalités
        testerTransformations();
        testerFloodFill();
        testerFusion();
        testerTexte();

        System.out.println("\n===========================================");
        System.out.println("  DÉMONSTRATION TERMINÉE");
        System.out.println("  Images générées dans le dossier courant");
        System.out.println("===========================================");
    }

    // ========================================================================
    // CRÉATION D'IMAGES DE TEST
    // ========================================================================

    /**
     * Crée des images de test pour la démonstration.
     */
    private static void creerImagesTest() {
        System.out.println("📷 Création des images de test...\n");

        // Image 1 : Dégradé coloré
        BufferedImage degrade = creerDegrade(400, 300);
        ImageUtil.sauvegarderImage(degrade, "test_degrade.png");

        // Image 2 : Carrés de couleurs
        BufferedImage carres = creerCarres(400, 300);
        ImageUtil.sauvegarderImage(carres, "test_carres.png");

        // Image 3 : Cercle sur fond vert (pour chroma key)
        BufferedImage chromaKey = creerImageChromaKey(200, 200);
        ImageUtil.sauvegarderImage(chromaKey, "test_chroma.png");

        // Image 4 : Image avec transparence
        BufferedImage transparente = creerImageTransparente(200, 200);
        ImageUtil.sauvegarderImage(transparente, "test_transparente.png");

        System.out.println("✓ Images de test créées\n");
    }

    /**
     * Crée une image avec un dégradé de couleurs.
     */
    private static BufferedImage creerDegrade(int largeur, int hauteur) {
        BufferedImage image = ImageUtil.creerImageVide(largeur, hauteur);

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                // Calcul des composantes en fonction de la position
                int rouge = (int) (255.0 * x / largeur);
                int vert = (int) (255.0 * y / hauteur);
                int bleu = (int) (255.0 * (largeur - x) / largeur);

                int couleur = ImageUtil.combinerComposantes(255, rouge, vert, bleu);
                image.setRGB(x, y, couleur);
            }
        }

        return image;
    }

    /**
     * Crée une image avec des carrés de couleurs.
     */
    private static BufferedImage creerCarres(int largeur, int hauteur) {
        BufferedImage image = ImageUtil.creerImageVide(largeur, hauteur);
        Graphics2D g2d = image.createGraphics();

        int tailleCase = 50;
        Color[] couleurs = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
                           Color.CYAN, Color.MAGENTA, Color.ORANGE, Color.PINK};

        int index = 0;
        for (int y = 0; y < hauteur; y += tailleCase) {
            for (int x = 0; x < largeur; x += tailleCase) {
                g2d.setColor(couleurs[index % couleurs.length]);
                g2d.fillRect(x, y, tailleCase, tailleCase);
                index++;
            }
        }

        g2d.dispose();
        return image;
    }

    /**
     * Crée une image avec un cercle sur fond vert (pour test chroma key).
     */
    private static BufferedImage creerImageChromaKey(int largeur, int hauteur) {
        BufferedImage image = ImageUtil.creerImageVide(largeur, hauteur);
        Graphics2D g2d = image.createGraphics();

        // Fond vert
        g2d.setColor(new Color(0, 255, 0));
        g2d.fillRect(0, 0, largeur, hauteur);

        // Cercle rouge
        g2d.setColor(Color.RED);
        int marge = 20;
        g2d.fillOval(marge, marge, largeur - 2 * marge, hauteur - 2 * marge);

        g2d.dispose();
        return image;
    }

    /**
     * Crée une image avec de la transparence.
     */
    private static BufferedImage creerImageTransparente(int largeur, int hauteur) {
        BufferedImage image = ImageUtil.creerImageVide(largeur, hauteur);

        int centreX = largeur / 2;
        int centreY = hauteur / 2;
        int rayonMax = Math.min(largeur, hauteur) / 2;

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                // Calcul de la distance au centre
                double distance = Math.sqrt(Math.pow(x - centreX, 2) + Math.pow(y - centreY, 2));

                // Alpha décroît avec la distance
                int alpha = (int) Math.max(0, 255 * (1 - distance / rayonMax));

                int couleur = ImageUtil.combinerComposantes(alpha, 255, 100, 100);
                image.setRGB(x, y, couleur);
            }
        }

        return image;
    }

    // ========================================================================
    // TEST DES TRANSFORMATIONS
    // ========================================================================

    private static void testerTransformations() {
        System.out.println("🔄 Test des transformations...\n");

        BufferedImage image = ImageUtil.ouvrirImage("test_degrade.png");
        if (image == null) return;

        // Rotation 90°
        BufferedImage rot90 = ImageTransform.rotation90Horaire(image);
        ImageUtil.sauvegarderImage(rot90, "resultat_rotation90.png");
        System.out.println("  ✓ Rotation 90° : resultat_rotation90.png");

        // Luminosité +50
        BufferedImage lum = ImageTransform.ajusterLuminosite(image, 50);
        ImageUtil.sauvegarderImage(lum, "resultat_luminosite.png");
        System.out.println("  ✓ Luminosité +50 : resultat_luminosite.png");

        // Contraste x1.5
        BufferedImage cont = ImageTransform.ajusterContraste(image, 1.5);
        ImageUtil.sauvegarderImage(cont, "resultat_contraste.png");
        System.out.println("  ✓ Contraste x1.5 : resultat_contraste.png");

        // Décalage teinte 120°
        BufferedImage teinte = ImageTransform.decalerTeinte(image, 120);
        ImageUtil.sauvegarderImage(teinte, "resultat_teinte.png");
        System.out.println("  ✓ Teinte +120° : resultat_teinte.png");

        // Niveaux de gris
        BufferedImage gris = ImageTransform.versNiveauxDeGris(image);
        ImageUtil.sauvegarderImage(gris, "resultat_gris.png");
        System.out.println("  ✓ Niveaux de gris : resultat_gris.png");

        // Négatif
        BufferedImage neg = ImageTransform.inverserCouleurs(image);
        ImageUtil.sauvegarderImage(neg, "resultat_negatif.png");
        System.out.println("  ✓ Négatif : resultat_negatif.png");

        System.out.println();
    }

    // ========================================================================
    // TEST DU FLOOD FILL
    // ========================================================================

    private static void testerFloodFill() {
        System.out.println("🎨 Test du Flood Fill (pot de peinture)...\n");

        BufferedImage image = ImageUtil.ouvrirImage("test_carres.png");
        if (image == null) return;

        // Remplissage avec différentes tolérances
        int couleurBleue = FloodFill.creerCouleur(0, 100, 200);

        // Tolérance 0 (exacte)
        BufferedImage fill1 = FloodFill.remplir(image, 25, 25, couleurBleue, 0);
        ImageUtil.sauvegarderImage(fill1, "resultat_floodfill_exact.png");
        System.out.println("  ✓ Flood Fill (tolérance 0) : resultat_floodfill_exact.png");

        // Tolérance 50
        BufferedImage fill2 = FloodFill.remplir(image, 75, 25, couleurBleue, 50);
        ImageUtil.sauvegarderImage(fill2, "resultat_floodfill_tol50.png");
        System.out.println("  ✓ Flood Fill (tolérance 50) : resultat_floodfill_tol50.png");

        System.out.println();
    }

    // ========================================================================
    // TEST DE LA FUSION
    // ========================================================================

    private static void testerFusion() {
        System.out.println("🖼️ Test de la fusion d'images...\n");

        BufferedImage img1 = ImageUtil.ouvrirImage("test_degrade.png");
        BufferedImage img2 = ImageUtil.ouvrirImage("test_carres.png");
        BufferedImage chroma = ImageUtil.ouvrirImage("test_chroma.png");
        BufferedImage transp = ImageUtil.ouvrirImage("test_transparente.png");

        if (img1 == null || img2 == null) return;

        // Superposition simple
        BufferedImage super1 = ImageFusion.superposer(img1, chroma, 100, 50);
        ImageUtil.sauvegarderImage(super1, "resultat_superposition.png");
        System.out.println("  ✓ Superposition simple : resultat_superposition.png");

        // Superposition avec alpha
        BufferedImage super2 = ImageFusion.superposerAvecAlpha(img1, transp, 100, 50);
        ImageUtil.sauvegarderImage(super2, "resultat_superposition_alpha.png");
        System.out.println("  ✓ Superposition avec alpha : resultat_superposition_alpha.png");

        // Chroma key (fond vert)
        int vertChroma = ImageFusion.VERT_CHROMA;
        BufferedImage super3 = ImageFusion.superposerAvecCleTransparence(
            img1, chroma, 100, 50, vertChroma, 30
        );
        ImageUtil.sauvegarderImage(super3, "resultat_chromakey.png");
        System.out.println("  ✓ Chroma key (fond vert) : resultat_chromakey.png");

        // Fusion 50/50
        BufferedImage fusion = ImageFusion.fusionner(img1, img2, 0.5);
        ImageUtil.sauvegarderImage(fusion, "resultat_fusion.png");
        System.out.println("  ✓ Fusion 50/50 : resultat_fusion.png");

        System.out.println();
    }

    // ========================================================================
    // TEST DU TEXTE
    // ========================================================================

    private static void testerTexte() {
        System.out.println("📝 Test du dessin de texte...\n");

        BufferedImage image = ImageUtil.ouvrirImage("test_degrade.png");
        BufferedImage imageCouleur = ImageUtil.ouvrirImage("test_carres.png");

        if (image == null) return;

        // Texte simple
        BufferedImage txt1 = TextDrawer.dessinerTexte(
            image, "Hello World!", 50, 100, Color.WHITE, 36
        );
        ImageUtil.sauvegarderImage(txt1, "resultat_texte_simple.png");
        System.out.println("  ✓ Texte simple : resultat_texte_simple.png");

        // Texte avec fond
        BufferedImage txt2 = TextDrawer.dessinerTexteAvecFond(
            image, "Équipe 6 - BUT 3", 50, 150,
            Color.WHITE, new Color(0, 0, 0, 180), 28, 10
        );
        ImageUtil.sauvegarderImage(txt2, "resultat_texte_fond.png");
        System.out.println("  ✓ Texte avec fond : resultat_texte_fond.png");

        // Texte avec couleur d'image
        if (imageCouleur != null) {
            BufferedImage txt3 = TextDrawer.dessinerTexteAvecCouleurImage(
                image, imageCouleur, "MULTIMÉDIA", 50, 200, 48
            );
            ImageUtil.sauvegarderImage(txt3, "resultat_texte_couleur_image.png");
            System.out.println("  ✓ Texte couleur image : resultat_texte_couleur_image.png");
        }

        System.out.println();
    }
}

