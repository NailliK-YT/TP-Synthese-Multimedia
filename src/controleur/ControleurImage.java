package controleur;

import modele.ModeleImage;
import modele.UtilitaireImage;
import modele.traitement.*;
import vue.FramePrincipal;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

/**
 * ============================================================================
 * CONTRÔLEUR IMAGE - OPÉRATIONS SUR LES IMAGES
 * ============================================================================
 * 
 * Ce contrôleur gère toutes les opérations de traitement d'images :
 * - Transformations (rotation, luminosité, contraste, teinte)
 * - Fusion d'images
 * - Pot de peinture
 * - Ajout de texte
 * 
 * @author Équipe 6 - BUT 3 Informatique
 */
public class ControleurImage {

    // ========================================================================
    // ATTRIBUTS
    // ========================================================================

    private final ModeleImage modele;
    private final FramePrincipal vue;

    // Paramètres du pot de peinture
    private Color couleurPeinture = Color.RED;
    private double tolerancePeinture = 30.0;

    // Position pour fusion
    private int positionX = 0;
    private int positionY = 0;

    // ========================================================================
    // CONSTRUCTEUR
    // ========================================================================

    public ControleurImage(ModeleImage modele, FramePrincipal vue) {
        this.modele = modele;
        this.vue = vue;
    }

    // ========================================================================
    // TRANSFORMATIONS
    // ========================================================================

    public void appliquerRotation90Droite() {
        if (!verifierImage())
            return;
        BufferedImage resultat = TraitementTransformation.rotation90Horaire(
                modele.obtenirImagePrincipale());
        modele.mettreAJourImagePrincipale(resultat);
    }

    public void appliquerRotation90Gauche() {
        if (!verifierImage())
            return;
        BufferedImage resultat = TraitementTransformation.rotation90AntiHoraire(
                modele.obtenirImagePrincipale());
        modele.mettreAJourImagePrincipale(resultat);
    }

    public void appliquerRotation180() {
        if (!verifierImage())
            return;
        BufferedImage resultat = TraitementTransformation.rotation180(
                modele.obtenirImagePrincipale());
        modele.mettreAJourImagePrincipale(resultat);
    }

    public void ajusterLuminosite() {
        if (!verifierImage())
            return;

        JSlider curseur = new JSlider(-100, 100, 0);
        curseur.setMajorTickSpacing(50);
        curseur.setPaintTicks(true);
        curseur.setPaintLabels(true);

        int resultat = JOptionPane.showConfirmDialog(vue, curseur,
                "Ajuster la luminosité", JOptionPane.OK_CANCEL_OPTION);

        if (resultat == JOptionPane.OK_OPTION) {
            BufferedImage imageResultat = TraitementTransformation.ajusterLuminosite(
                    modele.obtenirImagePrincipale(), curseur.getValue());
            modele.mettreAJourImagePrincipale(imageResultat);
        }
    }

    public void ajusterContraste() {
        if (!verifierImage())
            return;

        String facteurStr = JOptionPane.showInputDialog(vue,
                "Facteur de contraste (0.5 = réduit, 1.0 = normal, 2.0 = augmenté) :", "1.0");
        if (facteurStr == null)
            return;

        double facteur = Double.parseDouble(facteurStr);
        BufferedImage resultat = TraitementTransformation.ajusterContraste(
                modele.obtenirImagePrincipale(), facteur);
        modele.mettreAJourImagePrincipale(resultat);
    }

    public void decalerTeinte() {
        if (!verifierImage())
            return;

        JSlider curseur = new JSlider(0, 360, 0);
        curseur.setMajorTickSpacing(60);
        curseur.setPaintTicks(true);
        curseur.setPaintLabels(true);

        int resultat = JOptionPane.showConfirmDialog(vue, curseur,
                "Décalage de teinte (degrés)", JOptionPane.OK_CANCEL_OPTION);

        if (resultat == JOptionPane.OK_OPTION) {
            BufferedImage imageResultat = TraitementTransformation.decalerTeinte(
                    modele.obtenirImagePrincipale(), curseur.getValue());
            modele.mettreAJourImagePrincipale(imageResultat);
        }
    }

    public void convertirEnGris() {
        if (!verifierImage())
            return;
        BufferedImage resultat = TraitementTransformation.versNiveauxDeGris(
                modele.obtenirImagePrincipale());
        modele.mettreAJourImagePrincipale(resultat);
    }

    public void appliquerNegatif() {
        if (!verifierImage())
            return;
        BufferedImage resultat = TraitementTransformation.inverserCouleurs(
                modele.obtenirImagePrincipale());
        modele.mettreAJourImagePrincipale(resultat);
    }

    // ========================================================================
    // POSITIONNEMENT
    // ========================================================================

    /**
     * Permet de définir la position de l'Image 2 pour la fusion.
     */
    public void definirPositionImage2() {
        if (!verifierDeuxImages())
            return;

        // Créer un panneau personnalisé avec les champs
        JPanel panneau = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel labelX = new JLabel("Position X :");
        JTextField champX = new JTextField(String.valueOf(positionX), 10);

        JLabel labelY = new JLabel("Position Y :");
        JTextField champY = new JTextField(String.valueOf(positionY), 10);

        JLabel labelInfo = new JLabel("<html>Image 1: " +
                modele.obtenirImagePrincipale().getWidth() + "x" +
                modele.obtenirImagePrincipale().getHeight() + "<br>Image 2: " +
                modele.obtenirImageSecondaire().getWidth() + "x" +
                modele.obtenirImageSecondaire().getHeight() + "</html>");

        panneau.add(labelX);
        panneau.add(champX);
        panneau.add(labelY);
        panneau.add(champY);
        panneau.add(new JLabel("Dimensions:"));
        panneau.add(labelInfo);

        int resultat = JOptionPane.showConfirmDialog(
                vue,
                panneau,
                "Définir la position de l'Image 2",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (resultat == JOptionPane.OK_OPTION) {
            try {
                positionX = Integer.parseInt(champX.getText());
                positionY = Integer.parseInt(champY.getText());
                vue.mettreAJourStatut("Position Image 2 : (" + positionX + ", " + positionY + ")");

                // Afficher un aperçu
                afficherApercu();
            } catch (NumberFormatException e) {
                vue.afficherErreur("Veuillez entrer des nombres valides !");
            }
        }
    }

    /**
     * Affiche un aperçu de la superposition.
     */
    private void afficherApercu() {
        BufferedImage apercu = TraitementFusion.superposerAvecAlpha(
                modele.obtenirImagePrincipale(),
                modele.obtenirImageSecondaire(),
                positionX, positionY);
        modele.mettreAJourImagePrincipale(apercu);
        vue.mettreAJourStatut("Aperçu affiché - Position : (" + positionX + ", " + positionY + ")");
    }

    // ========================================================================
    // FUSION
    // ========================================================================

    public void superposerImages() {
        if (!verifierDeuxImages())
            return;
        BufferedImage resultat = TraitementFusion.superposer(
                modele.obtenirImagePrincipale(),
                modele.obtenirImageSecondaire(),
                positionX, positionY);
        modele.mettreAJourImagePrincipale(resultat);
    }

    public void superposerAvecAlpha() {
        if (!verifierDeuxImages())
            return;
        BufferedImage resultat = TraitementFusion.superposerAvecAlpha(
                modele.obtenirImagePrincipale(),
                modele.obtenirImageSecondaire(),
                positionX, positionY);
        modele.mettreAJourImagePrincipale(resultat);
    }

    public void superposerChromaKey() {
        if (!verifierDeuxImages())
            return;

        Color couleur = JColorChooser.showDialog(vue, "Couleur transparente", Color.GREEN);
        if (couleur == null)
            return;

        String tolStr = JOptionPane.showInputDialog(vue, "Tolérance (0-100) :", "30");
        if (tolStr == null)
            return;

        double tolerance = Double.parseDouble(tolStr);
        int couleurARGB = UtilitaireImage.combinerComposantes(
                255, couleur.getRed(), couleur.getGreen(), couleur.getBlue());

        BufferedImage resultat = TraitementFusion.superposerAvecCleTransparence(
                modele.obtenirImagePrincipale(),
                modele.obtenirImageSecondaire(),
                positionX, positionY,
                couleurARGB, tolerance);
        modele.mettreAJourImagePrincipale(resultat);
    }

    public void fusionnerImages() {
        if (!verifierDeuxImages())
            return;

        String ratioStr = JOptionPane.showInputDialog(vue,
                "Ratio de l'image principale (0.0 à 1.0) :", "0.5");
        if (ratioStr == null)
            return;

        double ratio = Double.parseDouble(ratioStr);
        BufferedImage resultat = TraitementFusion.fusionner(
                modele.obtenirImagePrincipale(),
                modele.obtenirImageSecondaire(),
                ratio);
        modele.mettreAJourImagePrincipale(resultat);
    }

    // ========================================================================
    // POT DE PEINTURE
    // ========================================================================

    public void appliquerPotPeinture(int x, int y) {
        if (!verifierImage())
            return;

        int couleurARGB = UtilitaireImage.combinerComposantes(
                255, couleurPeinture.getRed(), couleurPeinture.getGreen(), couleurPeinture.getBlue());

        BufferedImage resultat = TraitementRemplissage.remplir(
                modele.obtenirImagePrincipale(), x, y, couleurARGB, tolerancePeinture);
        modele.mettreAJourImagePrincipale(resultat);
    }

    public void choisirCouleurPeinture() {
        Color nouvelle = JColorChooser.showDialog(vue, "Couleur du pot", couleurPeinture);
        if (nouvelle != null) {
            couleurPeinture = nouvelle;
            vue.mettreAJourStatut("Couleur du pot : " + String.format("#%02X%02X%02X",
                    nouvelle.getRed(), nouvelle.getGreen(), nouvelle.getBlue()));
        }
    }

    public void definirTolerance() {
        String tolStr = JOptionPane.showInputDialog(vue,
                "Tolérance (0 = exact, 50 = similaire, 100 = large) :",
                String.valueOf(tolerancePeinture));
        if (tolStr != null) {
            tolerancePeinture = Double.parseDouble(tolStr);
        }
    }

    // ========================================================================
    // TEXTE
    // ========================================================================

    public void ajouterTexteSimple() {
        if (!verifierImage())
            return;

        // Panneau avec texte et position
        JPanel panneau = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel labelTexte = new JLabel("Texte :");
        JTextField champTexte = new JTextField("Hello World", 20);

        JLabel labelX = new JLabel("Position X :");
        JTextField champX = new JTextField("50", 10);

        JLabel labelY = new JLabel("Position Y :");
        JTextField champY = new JTextField("50", 10);

        panneau.add(labelTexte);
        panneau.add(champTexte);
        panneau.add(labelX);
        panneau.add(champX);
        panneau.add(labelY);
        panneau.add(champY);

        int resultat = JOptionPane.showConfirmDialog(
                vue,
                panneau,
                "Ajouter du texte",
                JOptionPane.OK_CANCEL_OPTION);

        if (resultat != JOptionPane.OK_OPTION)
            return;

        String texte = champTexte.getText();
        if (texte.isEmpty())
            return;

        int x, y;
        try {
            x = Integer.parseInt(champX.getText());
            y = Integer.parseInt(champY.getText());
        } catch (NumberFormatException e) {
            vue.afficherErreur("Veuillez entrer des nombres valides pour X et Y !");
            return;
        }

        Color couleur = JColorChooser.showDialog(vue, "Couleur du texte", Color.BLACK);
        if (couleur == null)
            couleur = Color.BLACK;

        BufferedImage resultatImage = TraitementTexte.dessinerTexte(
                modele.obtenirImagePrincipale(), texte, x, y, couleur, 32);
        modele.mettreAJourImagePrincipale(resultatImage);
        vue.mettreAJourStatut("Texte ajoute a la position (" + x + ", " + y + ")");
    }

    public void ajouterTexteAvecFond() {
        if (!verifierImage())
            return;

        // Panneau avec texte et position
        JPanel panneau = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel labelTexte = new JLabel("Texte :");
        JTextField champTexte = new JTextField("Hello World", 20);

        JLabel labelX = new JLabel("Position X :");
        JTextField champX = new JTextField("50", 10);

        JLabel labelY = new JLabel("Position Y :");
        JTextField champY = new JTextField("50", 10);

        panneau.add(labelTexte);
        panneau.add(champTexte);
        panneau.add(labelX);
        panneau.add(champX);
        panneau.add(labelY);
        panneau.add(champY);

        int resultat = JOptionPane.showConfirmDialog(
                vue,
                panneau,
                "Ajouter du texte avec fond",
                JOptionPane.OK_CANCEL_OPTION);

        if (resultat != JOptionPane.OK_OPTION)
            return;

        String texte = champTexte.getText();
        if (texte.isEmpty())
            return;

        int x, y;
        try {
            x = Integer.parseInt(champX.getText());
            y = Integer.parseInt(champY.getText());
        } catch (NumberFormatException e) {
            vue.afficherErreur("Veuillez entrer des nombres valides pour X et Y !");
            return;
        }

        Color couleurTexte = JColorChooser.showDialog(vue, "Couleur du texte", Color.WHITE);
        if (couleurTexte == null)
            couleurTexte = Color.WHITE;

        Color couleurFond = JColorChooser.showDialog(vue, "Couleur du fond", Color.BLACK);
        if (couleurFond == null)
            couleurFond = Color.BLACK;

        BufferedImage resultatImage = TraitementTexte.dessinerTexteAvecFond(
                modele.obtenirImagePrincipale(), texte, x, y,
                couleurTexte, couleurFond, 32, 10);
        modele.mettreAJourImagePrincipale(resultatImage);
        vue.mettreAJourStatut("Texte avec fond ajoute a la position (" + x + ", " + y + ")");
    }

    public void ajouterTexteCouleurImage() {
        if (!verifierDeuxImages())
            return;

        // Panneau avec texte et position
        JPanel panneau = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel labelTexte = new JLabel("Texte :");
        JTextField champTexte = new JTextField("Hello World", 20);

        JLabel labelX = new JLabel("Position X :");
        JTextField champX = new JTextField("50", 10);

        JLabel labelY = new JLabel("Position Y :");
        JTextField champY = new JTextField("50", 10);

        panneau.add(labelTexte);
        panneau.add(champTexte);
        panneau.add(labelX);
        panneau.add(champX);
        panneau.add(labelY);
        panneau.add(champY);

        int resultat = JOptionPane.showConfirmDialog(
                vue,
                panneau,
                "Texte avec couleur d'image (Image 2)",
                JOptionPane.OK_CANCEL_OPTION);

        if (resultat != JOptionPane.OK_OPTION)
            return;

        String texte = champTexte.getText();
        if (texte.isEmpty())
            return;

        int x, y;
        try {
            x = Integer.parseInt(champX.getText());
            y = Integer.parseInt(champY.getText());
        } catch (NumberFormatException e) {
            vue.afficherErreur("Veuillez entrer des nombres valides pour X et Y !");
            return;
        }

        BufferedImage resultatImage = TraitementTexte.dessinerTexteAvecCouleurImage(
                modele.obtenirImagePrincipale(),
                modele.obtenirImageSecondaire(),
                texte, x, y, 32);
        modele.mettreAJourImagePrincipale(resultatImage);
        vue.mettreAJourStatut("Texte colore ajoute a la position (" + x + ", " + y + ")");
    }

    // ========================================================================
    // UTILITAIRES
    // ========================================================================

    private boolean verifierImage() {
        if (!modele.possedeImagePrincipale()) {
            vue.afficherErreur("Veuillez d'abord charger une image !");
            return false;
        }
        return true;
    }

    private boolean verifierDeuxImages() {
        if (!modele.possedeImagePrincipale()) {
            vue.afficherErreur("Veuillez d'abord charger une image principale !");
            return false;
        }
        if (!modele.possedeImageSecondaire()) {
            vue.afficherErreur("Veuillez d'abord charger une image secondaire !");
            return false;
        }
        return true;
    }

    // Getters/Setters
    public void definirPosition(int x, int y) {
        this.positionX = x;
        this.positionY = y;
    }
}
