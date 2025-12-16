package controleur;

import modele.ModeleImage;
import modele.UtilitaireImage;
import vue.VuePrincipale;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * ============================================================================
 * CONTRÔLEUR FICHIER - GESTION DES FICHIERS
 * ============================================================================
 * 
 * Ce contrôleur gère toutes les opérations liées aux fichiers :
 * - Ouverture d'image principale
 * - Ouverture d'image secondaire
 * - Sauvegarde d'image
 * - Restauration de l'image originale
 * 
 * @author Équipe 6 - BUT 3 Informatique
 */
public class ControleurFichier {

    // ========================================================================
    // ATTRIBUTS
    // ========================================================================

    private final ModeleImage modele;
    private final VuePrincipale vue;

    // ========================================================================
    // CONSTRUCTEUR
    // ========================================================================

    public ControleurFichier(ModeleImage modele, VuePrincipale vue) {
        this.modele = modele;
        this.vue = vue;
    }

    // ========================================================================
    // OPÉRATIONS FICHIER
    // ========================================================================

    /**
     * Ouvre une image principale via un dialogue de fichier.
     */
    public void ouvrirImagePrincipale() {
        JFileChooser selecteur = new JFileChooser();
        selecteur.setDialogTitle("Ouvrir une image principale");
        selecteur.setFileFilter(new FileNameExtensionFilter("Images PNG", "png"));

        int resultat = selecteur.showOpenDialog(vue);

        if (resultat == JFileChooser.APPROVE_OPTION) {
            File fichier = selecteur.getSelectedFile();
            BufferedImage image = UtilitaireImage.ouvrirImage(fichier.getAbsolutePath());

            if (image != null) {
                modele.definirImagePrincipale(image);
                vue.mettreAJourStatut("Image chargée : " + fichier.getName());
            } else {
                vue.afficherErreur("Impossible de charger l'image !");
            }
        }
    }

    /**
     * Ouvre une image secondaire pour les opérations de fusion.
     */
    public void ouvrirImageSecondaire() {
        JFileChooser selecteur = new JFileChooser();
        selecteur.setDialogTitle("Ouvrir une image secondaire");
        selecteur.setFileFilter(new FileNameExtensionFilter("Images PNG", "png"));

        int resultat = selecteur.showOpenDialog(vue);

        if (resultat == JFileChooser.APPROVE_OPTION) {
            File fichier = selecteur.getSelectedFile();
            BufferedImage image = UtilitaireImage.ouvrirImage(fichier.getAbsolutePath());

            if (image != null) {
                modele.definirImageSecondaire(image);
                vue.mettreAJourStatut("Image secondaire chargée : " +
                        image.getWidth() + "x" + image.getHeight());
            } else {
                vue.afficherErreur("Impossible de charger l'image secondaire !");
            }
        }
    }

    /**
     * Sauvegarde l'image actuelle.
     */
    public void sauvegarderImage() {
        if (!modele.possedeImagePrincipale()) {
            vue.afficherErreur("Aucune image à sauvegarder !");
            return;
        }

        JFileChooser selecteur = new JFileChooser();
        selecteur.setDialogTitle("Sauvegarder l'image");
        selecteur.setFileFilter(new FileNameExtensionFilter("Images PNG", "png"));

        int resultat = selecteur.showSaveDialog(vue);

        if (resultat == JFileChooser.APPROVE_OPTION) {
            String chemin = selecteur.getSelectedFile().getAbsolutePath();

            // Ajout de l'extension .png si nécessaire
            if (!chemin.toLowerCase().endsWith(".png")) {
                chemin += ".png";
            }

            if (UtilitaireImage.sauvegarderImage(modele.obtenirImagePrincipale(), chemin)) {
                vue.mettreAJourStatut("Image sauvegardée : " + chemin);
                JOptionPane.showMessageDialog(vue, "Image sauvegardée avec succès !");
            } else {
                vue.afficherErreur("Erreur lors de la sauvegarde !");
            }
        }
    }

    /**
     * Restaure l'image originale.
     */
    public void restaurerOriginale() {
        if (!modele.peutRestaurer()) {
            vue.afficherErreur("Aucune image originale à restaurer !");
            return;
        }

        modele.restaurerImageOriginale();
        vue.mettreAJourStatut("Image originale restaurée");
    }

    /**
     * Quitte l'application.
     */
    public void quitter() {
        int choix = JOptionPane.showConfirmDialog(
                vue,
                "Voulez-vous vraiment quitter ?",
                "Quitter",
                JOptionPane.YES_NO_OPTION);

        if (choix == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
