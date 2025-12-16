package vue;

import controleur.ControleurFichier;
import controleur.ControleurImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * ============================================================================
 * VUE PRINCIPALE - INTERFACE GRAPHIQUE
 * ============================================================================
 * 
 * La fenêtre principale de l'application.
 * 
 * RESPONSABILITÉS :
 * - Afficher l'interface utilisateur
 * - Créer les menus et panneaux
 * - Afficher l'image
 * - Déléguer les événements aux contrôleurs
 * 
 * ATTENTION : Pas de logique métier dans la vue !
 * Tout est délégué aux contrôleurs.
 * 
 * @author Équipe 6 - BUT 3 Informatique
 */
public class VuePrincipale extends JFrame {

    // ========================================================================
    // ATTRIBUTS
    // ========================================================================

    private ControleurFichier controleurFichier;
    private ControleurImage controleurImage;

    private PanneauImage panneauImage;
    private JLabel labelStatut;

    // ========================================================================
    // CONSTRUCTEUR
    // ========================================================================

    public VuePrincipale() {
        super("Éditeur d'Images - Architecture MVC - Équipe 6");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        creerInterface();

        setVisible(true);
    }

    // ========================================================================
    // CRÉATION DE L'INTERFACE
    // ========================================================================

    private void creerInterface() {
        // Panneau principal
        JPanel panneauPrincipal = new JPanel(new BorderLayout());

        // Zone d'affichage de l'image
        panneauImage = new PanneauImage();
        JScrollPane scrollPane = new JScrollPane(panneauImage);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Image"));

        // Barre d'information
        labelStatut = new JLabel("En attente de chargement d'image...");
        labelStatut.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Panneau de boutons rapides
        JPanel panneauBoutons = creerPanneauBoutons();

        panneauPrincipal.add(scrollPane, BorderLayout.CENTER);
        panneauPrincipal.add(labelStatut, BorderLayout.SOUTH);
        panneauPrincipal.add(panneauBoutons, BorderLayout.EAST);

        add(panneauPrincipal);
    }

    private JPanel creerPanneauBoutons() {
        JPanel panneauPrincipal = new JPanel();
        panneauPrincipal.setLayout(new BoxLayout(panneauPrincipal, BoxLayout.Y_AXIS));
        panneauPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // SECTION 1 : FICHIERS
        JPanel sectionFichiers = creerSection("FICHIERS");
        ajouterBouton(sectionFichiers, "Ouvrir Image 1", e -> {
            if (controleurFichier != null)
                controleurFichier.ouvrirImagePrincipale();
        });
        ajouterBouton(sectionFichiers, "Ouvrir Image 2", e -> {
            if (controleurFichier != null)
                controleurFichier.ouvrirImageSecondaire();
        });
        ajouterBouton(sectionFichiers, "Sauvegarder", e -> {
            if (controleurFichier != null)
                controleurFichier.sauvegarderImage();
        });
        ajouterBouton(sectionFichiers, "Annuler (Restaurer)", e -> {
            if (controleurFichier != null)
                controleurFichier.restaurerOriginale();
        });

        // SECTION 2 : FUSION D'IMAGES
        JPanel sectionFusion = creerSection("FUSION 2 IMAGES");
        ajouterBouton(sectionFusion, "Definir Position Image 2", e -> {
            if (controleurImage != null)
                controleurImage.definirPositionImage2();
        });
        ajouterBouton(sectionFusion, "Superposer", e -> {
            if (controleurImage != null)
                controleurImage.superposerImages();
        });
        ajouterBouton(sectionFusion, "Avec Transparence", e -> {
            if (controleurImage != null)
                controleurImage.superposerAvecAlpha();
        });
        ajouterBouton(sectionFusion, "Fond Vert (Chroma)", e -> {
            if (controleurImage != null)
                controleurImage.superposerChromaKey();
        });
        ajouterBouton(sectionFusion, "Melanger 50/50", e -> {
            if (controleurImage != null)
                controleurImage.fusionnerImages();
        });

        // SECTION 3 : TRANSFORMATIONS
        JPanel sectionTransform = creerSection("TRANSFORMATIONS");
        ajouterBouton(sectionTransform, "Rotation Droite", e -> {
            if (controleurImage != null)
                controleurImage.appliquerRotation90Droite();
        });
        ajouterBouton(sectionTransform, "Rotation Gauche", e -> {
            if (controleurImage != null)
                controleurImage.appliquerRotation90Gauche();
        });
        ajouterBouton(sectionTransform, "Luminosite", e -> {
            if (controleurImage != null)
                controleurImage.ajusterLuminosite();
        });
        ajouterBouton(sectionTransform, "Contraste", e -> {
            if (controleurImage != null)
                controleurImage.ajusterContraste();
        });
        ajouterBouton(sectionTransform, "Teinte (Couleur)", e -> {
            if (controleurImage != null)
                controleurImage.decalerTeinte();
        });
        ajouterBouton(sectionTransform, "Noir et Blanc", e -> {
            if (controleurImage != null)
                controleurImage.convertirEnGris();
        });

        // SECTION 4 : OUTILS DE DESSIN
        JPanel sectionOutils = creerSection("OUTILS DESSIN");
        ajouterBouton(sectionOutils, "Pot de Peinture", e -> {
            if (controleurImage != null) {
                controleurImage.choisirCouleurPeinture();
                controleurImage.definirTolerance();
                mettreAJourStatut("Cliquez sur l'image pour peindre !");
            }
        });
        ajouterBouton(sectionOutils, "Texte Simple", e -> {
            if (controleurImage != null)
                controleurImage.ajouterTexteSimple();
        });
        ajouterBouton(sectionOutils, "Texte avec Fond", e -> {
            if (controleurImage != null)
                controleurImage.ajouterTexteAvecFond();
        });
        ajouterBouton(sectionOutils, "Texte Colore Image", e -> {
            if (controleurImage != null)
                controleurImage.ajouterTexteCouleurImage();
        });

        panneauPrincipal.add(sectionFichiers);
        panneauPrincipal.add(Box.createVerticalStrut(10));
        panneauPrincipal.add(sectionFusion);
        panneauPrincipal.add(Box.createVerticalStrut(10));
        panneauPrincipal.add(sectionTransform);
        panneauPrincipal.add(Box.createVerticalStrut(10));
        panneauPrincipal.add(sectionOutils);
        panneauPrincipal.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(panneauPrincipal);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(250, 0));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scrollPane, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel creerSection(String titre) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
                titre,
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                new Color(0, 100, 200)));
        return section;
    }

    private void ajouterBouton(JPanel section, String texte, java.awt.event.ActionListener action) {
        JButton bouton = new JButton(texte);
        bouton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        bouton.setAlignmentX(Component.LEFT_ALIGNMENT);
        bouton.addActionListener(action);
        bouton.setFont(new Font("Arial", Font.PLAIN, 11));
        section.add(bouton);
        section.add(Box.createVerticalStrut(3));
    }

    // ========================================================================
    // DÉFINITION DES CONTRÔLEURS
    // ========================================================================

    public void definirControleurFichier(ControleurFichier controleur) {
        this.controleurFichier = controleur;
        creerMenus();
    }

    public void definirControleurImage(ControleurImage controleur) {
        this.controleurImage = controleur;
    }

    private void creerMenus() {
        JMenuBar barreMenu = new JMenuBar();

        // Menu Fichier
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

        // Menu Transformations
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

        // Menu Fusion
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

        barreMenu.add(menuFichier);
        menuFusion.add(menuTransform);
        barreMenu.add(menuFusion);

        setJMenuBar(barreMenu);
    }

    // ========================================================================
    // AFFICHAGE
    // ========================================================================

    public void afficherImage(BufferedImage image) {
        panneauImage.setImage(image);
    }

    public void mettreAJourStatut(String message) {
        labelStatut.setText(message);
    }

    public void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    // ========================================================================
    // CLASSE INTERNE : PANNEAU D'AFFICHAGE
    // ========================================================================

    private class PanneauImage extends JPanel {
        private BufferedImage image;

        public PanneauImage() {
            setBackground(new Color(200, 200, 200));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (controleurImage != null && image != null) {
                        controleurImage.appliquerPotPeinture(e.getX(), e.getY());
                    }
                }
            });
        }

        public void setImage(BufferedImage image) {
            this.image = image;
            if (image != null) {
                setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
            }
            revalidate();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, null);
            }
        }
    }
}
