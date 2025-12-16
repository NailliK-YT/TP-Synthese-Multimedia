package vue;

import controleur.ControleurFichier;
import controleur.ControleurImage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * ============================================================================
 * FRAME PRINCIPAL - INTERFACE GRAPHIQUE
 * ============================================================================
 * 
 * La fenêtre principale de l'application.
 * Assemble les différents composants de l'interface.
 * 
 * RESPONSABILITÉS :
 * - Assembler les composants de l'interface
 * - Coordonner les interactions entre composants
 * - Déléguer les événements aux contrôleurs
 * 
 * ARCHITECTURE :
 * - PanelImage : Affichage de l'image
 * - PanelOutils : Panneau latéral avec boutons
 * - MenuBarPrincipal : Barre de menu
 * - PanelStatut : Barre d'information
 * 
 * @author Équipe 6 - BUT 3 Informatique
 */
public class FramePrincipal extends JFrame {

    private ControleurFichier controleurFichier;
    private ControleurImage controleurImage;

    private PanelImage panelImage;
    private PanelOutils panelOutils;
    private PanelStatut panelStatut;
    private MenuBarPrincipal menuBarPrincipal;

    public FramePrincipal() {
        super("Éditeur d'Images - Architecture MVC - Équipe 6");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        creerInterface();

        setVisible(true);
    }

    private void creerInterface() {
        panelImage = new PanelImage();
        panelOutils = new PanelOutils();
        panelStatut = new PanelStatut();

        JPanel panneauPrincipal = new JPanel(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(panelImage);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Image"));

        panneauPrincipal.add(scrollPane, BorderLayout.CENTER);
        panneauPrincipal.add(panelStatut, BorderLayout.SOUTH);
        panneauPrincipal.add(panelOutils, BorderLayout.EAST);

        add(panneauPrincipal);
    }

    /**
     * Définit le contrôleur de fichier et initialise la barre de menu.
     * 
     * @param controleur Le contrôleur de fichier
     */
    public void definirControleurFichier(ControleurFichier controleur) {
        this.controleurFichier = controleur;

        panelOutils.setControleurFichier(controleur);

        if (controleurImage != null) {
            menuBarPrincipal = new MenuBarPrincipal(controleurFichier, controleurImage);
            setJMenuBar(menuBarPrincipal);
        }
    }

    /**
     * Définit le contrôleur d'image et finalise l'initialisation.
     * 
     * @param controleur Le contrôleur d'image
     */
    public void definirControleurImage(ControleurImage controleur) {
        this.controleurImage = controleur;

        panelImage.setControleurImage(controleur);
        panelOutils.setControleurImage(controleur);
        panelOutils.setFramePrincipal(this);

        panelOutils.initialiser();

        if (controleurFichier != null) {
            menuBarPrincipal = new MenuBarPrincipal(controleurFichier, controleurImage);
            setJMenuBar(menuBarPrincipal);
        }
    }

    /**
     * Affiche l'image dans le panneau d'affichage.
     * 
     * @param image L'image à afficher
     */
    public void afficherImage(BufferedImage image) {
        panelImage.setImage(image);
    }

    /**
     * Met à jour le message de la barre de statut.
     * 
     * @param message Le message à afficher
     */
    public void mettreAJourStatut(String message) {
        panelStatut.mettreAJourStatut(message);
    }

    /**
     * Affiche un message d'erreur dans une boîte de dialogue.
     * 
     * @param message Le message d'erreur
     */
    public void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
