package vue;

import controleur.ControleurFichier;
import controleur.ControleurImage;

import javax.swing.*;

/**
 * ============================================================================
 * BARRE DE MENU
 * ============================================================================
 * 
 * Composant responsable de la barre de menu principal de l'application.
 * 
 * RESPONSABILITÉS :
 * - Créer et organiser les menus
 * - Connecter les actions aux contrôleurs
 * 
 * @author Équipe 6 - BUT 3 Informatique
 */
public class MenuBarPrincipal extends JMenuBar {

    private ControleurFichier controleurFichier;
    private ControleurImage controleurImage;

    /**
     * Construit la barre de menu avec les contrôleurs nécessaires.
     * 
     * @param controleurFichier Le contrôleur de gestion des fichiers
     * @param controleurImage   Le contrôleur de traitement d'images
     */
    public MenuBarPrincipal(ControleurFichier controleurFichier, ControleurImage controleurImage) {
        this.controleurFichier = controleurFichier;
        this.controleurImage = controleurImage;

        creerMenus();
    }

    private void creerMenus() {
        add(creerMenuFichier());
        add(creerMenuFusion());
    }

    /**
     * Crée le menu Fichier.
     * 
     * @return Le menu Fichier
     */
    private JMenu creerMenuFichier() {
        JMenu menuFichier = new JMenu("Fichier");

        JMenuItem itemOuvrir = new JMenuItem("Ouvrir image principale...");
        itemOuvrir.addActionListener(e -> controleurFichier.ouvrirImagePrincipale());

        JMenuItem itemOuvrirSecondaire = new JMenuItem("Ouvrir image secondaire...");
        itemOuvrirSecondaire.addActionListener(e -> controleurFichier.ouvrirImageSecondaire());

        JMenuItem itemSauvegarder = new JMenuItem("Sauvegarder...");
        itemSauvegarder.addActionListener(e -> controleurFichier.sauvegarderImage());

        JMenuItem itemRestaurer = new JMenuItem("Restaurer l'originale");
        itemRestaurer.addActionListener(e -> controleurFichier.restaurerOriginale());

        JMenuItem itemQuitter = new JMenuItem("Quitter");
        itemQuitter.addActionListener(e -> controleurFichier.quitter());

        menuFichier.add(itemOuvrir);
        menuFichier.add(itemOuvrirSecondaire);
        menuFichier.addSeparator();
        menuFichier.add(itemSauvegarder);
        menuFichier.add(itemRestaurer);
        menuFichier.addSeparator();
        menuFichier.add(itemQuitter);

        return menuFichier;
    }

    /**
     * Crée le menu Fusion (avec sous-menu Transformations).
     * 
     * @return Le menu Fusion
     */
    private JMenu creerMenuFusion() {
        JMenu menuFusion = new JMenu("Fusion");

        JMenuItem itemSuperposer = new JMenuItem("Superposer image secondaire");
        itemSuperposer.addActionListener(e -> controleurImage.superposerImages());

        JMenuItem itemSuperposerAlpha = new JMenuItem("Superposer avec transparence");
        itemSuperposerAlpha.addActionListener(e -> controleurImage.superposerAvecAlpha());

        JMenuItem itemChromaKey = new JMenuItem("Superposer avec chroma key");
        itemChromaKey.addActionListener(e -> controleurImage.superposerChromaKey());

        JMenuItem itemFusionner = new JMenuItem("Fusionner 50/50");
        itemFusionner.addActionListener(e -> controleurImage.fusionnerImages());

        menuFusion.add(itemSuperposer);
        menuFusion.add(itemSuperposerAlpha);
        menuFusion.add(itemChromaKey);
        menuFusion.add(itemFusionner);
        menuFusion.addSeparator();
        menuFusion.add(creerMenuTransformations());

        return menuFusion;
    }

    /**
     * Crée le sous-menu Transformations.
     * 
     * @return Le menu Transformations
     */
    private JMenu creerMenuTransformations() {
        JMenu menuTransform = new JMenu("Transformations");

        JMenuItem itemRotDroite = new JMenuItem("Rotation 90° droite");
        itemRotDroite.addActionListener(e -> controleurImage.appliquerRotation90Droite());

        JMenuItem itemRotGauche = new JMenuItem("Rotation 90° gauche");
        itemRotGauche.addActionListener(e -> controleurImage.appliquerRotation90Gauche());

        JMenuItem itemLuminosite = new JMenuItem("Ajuster luminosité...");
        itemLuminosite.addActionListener(e -> controleurImage.ajusterLuminosite());

        JMenuItem itemContraste = new JMenuItem("Ajuster contraste...");
        itemContraste.addActionListener(e -> controleurImage.ajusterContraste());

        JMenuItem itemTeinte = new JMenuItem("Décaler teinte...");
        itemTeinte.addActionListener(e -> controleurImage.decalerTeinte());

        JMenuItem itemGris = new JMenuItem("Niveaux de gris");
        itemGris.addActionListener(e -> controleurImage.convertirEnGris());

        JMenuItem itemNegatif = new JMenuItem("Négatif");
        itemNegatif.addActionListener(e -> controleurImage.appliquerNegatif());

        menuTransform.add(itemRotDroite);
        menuTransform.add(itemRotGauche);
        menuTransform.addSeparator();
        menuTransform.add(itemLuminosite);
        menuTransform.add(itemContraste);
        menuTransform.add(itemTeinte);
        menuTransform.addSeparator();
        menuTransform.add(itemGris);
        menuTransform.add(itemNegatif);

        return menuTransform;
    }
}
