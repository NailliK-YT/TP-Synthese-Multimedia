import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * ============================================================================
 * APPLICATION PRINCIPALE - ÉDITEUR D'IMAGES
 * ============================================================================
 * 
 * Cette classe est le point d'entrée de l'application.
 * Elle crée l'interface graphique Swing et gère les interactions utilisateur.
 * 
 * ARCHITECTURE :
 * - Menu : accès aux fonctionnalités principales
 * - Panneau d'affichage : visualisation de l'image
 * - Panneau de contrôle : paramètres des opérations
 * 
 * @author Équipe 6 - BUT 3 Informatique
 */
public class MainApp extends JFrame {

    // ========================================================================
    // ATTRIBUTS
    // ========================================================================

    // Images de travail
    private BufferedImage imagePrincipale;    // Image principale affichée
    private BufferedImage imageSecondaire;     // Seconde image pour fusion
    private BufferedImage imageOriginale;      // Sauvegarde de l'originale

    // Composants de l'interface
    private PanneauImage panneauImage;         // Zone d'affichage
    private JLabel labelInfo;                  // Informations sur l'image
    
    // Paramètres pour le pot de peinture
    private Color couleurPeinture = Color.RED;
    private double tolerancePeinture = 30.0;
    
    // Mode de clic actif
    private enum ModeClick { AUCUN, FLOOD_FILL, POSITION_IMAGE }
    private ModeClick modeClick = ModeClick.AUCUN;
    
    // Position pour la superposition
    private int positionX = 0;
    private int positionY = 0;

    // ========================================================================
    // CONSTRUCTEUR
    // ========================================================================

    /**
     * Constructeur de l'application.
     * Initialise l'interface graphique.
     */
    public MainApp() {
        super("Éditeur d'Images - Équipe 6 - BUT 3");
        
        // Configuration de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Création de l'interface
        creerMenus();
        creerInterface();
        
        setVisible(true);
        
        System.out.println("===========================================");
        System.out.println("  ÉDITEUR D'IMAGES - BUT 3 INFORMATIQUE");
        System.out.println("  Équipe 6 - Programmation Multimédia");
        System.out.println("===========================================");
    }

    // ========================================================================
    // CRÉATION DES MENUS
    // ========================================================================

    /**
     * Crée la barre de menus de l'application.
     */
    private void creerMenus() {
        JMenuBar barreMenu = new JMenuBar();

        // Menu Fichier
        JMenu menuFichier = new JMenu("Fichier");
        
        JMenuItem itemOuvrir = new JMenuItem("Ouvrir image principale...");
        itemOuvrir.addActionListener(e -> ouvrirImagePrincipale());
        
        JMenuItem itemOuvrirSecondaire = new JMenuItem("Ouvrir image secondaire...");
        itemOuvrirSecondaire.addActionListener(e -> ouvrirImageSecondaire());
        
        JMenuItem itemSauvegarder = new JMenuItem("Sauvegarder...");
        itemSauvegarder.addActionListener(e -> sauvegarderImage());
        
        JMenuItem itemRestaurer = new JMenuItem("Restaurer l'originale");
        itemRestaurer.addActionListener(e -> restaurerOriginale());
        
        JMenuItem itemQuitter = new JMenuItem("Quitter");
        itemQuitter.addActionListener(e -> System.exit(0));
        
        menuFichier.add(itemOuvrir);
        menuFichier.add(itemOuvrirSecondaire);
        menuFichier.addSeparator();
        menuFichier.add(itemSauvegarder);
        menuFichier.add(itemRestaurer);
        menuFichier.addSeparator();
        menuFichier.add(itemQuitter);

        // Menu Fusion
        JMenu menuFusion = new JMenu("Fusion");
        
        JMenuItem itemSuperposer = new JMenuItem("Superposer image secondaire");
        itemSuperposer.addActionListener(e -> superposerImages());
        
        JMenuItem itemSuperposerAlpha = new JMenuItem("Superposer avec transparence");
        itemSuperposerAlpha.addActionListener(e -> superposerAvecAlpha());
        
        JMenuItem itemChromaKey = new JMenuItem("Superposer avec clé couleur (Chroma)");
        itemChromaKey.addActionListener(e -> superposerChromaKey());
        
        JMenuItem itemFusionner = new JMenuItem("Fusionner 50/50");
        itemFusionner.addActionListener(e -> fusionnerImages());
        
        JMenuItem itemPositionClic = new JMenuItem("Définir position par clic");
        itemPositionClic.addActionListener(e -> activerModePosition());
        
        menuFusion.add(itemSuperposer);
        menuFusion.add(itemSuperposerAlpha);
        menuFusion.add(itemChromaKey);
        menuFusion.add(itemFusionner);
        menuFusion.addSeparator();
        menuFusion.add(itemPositionClic);

        // Menu Transformations
        JMenu menuTransform = new JMenu("Transformations");
        
        JMenuItem itemRotDroite = new JMenuItem("Rotation 90° droite");
        itemRotDroite.addActionListener(e -> appliquerRotation90Droite());
        
        JMenuItem itemRotGauche = new JMenuItem("Rotation 90° gauche");
        itemRotGauche.addActionListener(e -> appliquerRotation90Gauche());
        
        JMenuItem itemRot180 = new JMenuItem("Rotation 180°");
        itemRot180.addActionListener(e -> appliquerRotation180());
        
        JMenuItem itemLuminosite = new JMenuItem("Ajuster luminosité...");
        itemLuminosite.addActionListener(e -> ajusterLuminosite());
        
        JMenuItem itemContraste = new JMenuItem("Ajuster contraste...");
        itemContraste.addActionListener(e -> ajusterContraste());
        
        JMenuItem itemTeinte = new JMenuItem("Décaler teinte...");
        itemTeinte.addActionListener(e -> decalerTeinte());
        
        JMenuItem itemGris = new JMenuItem("Niveaux de gris");
        itemGris.addActionListener(e -> convertirGris());
        
        JMenuItem itemNegatif = new JMenuItem("Négatif");
        itemNegatif.addActionListener(e -> appliquerNegatif());
        
        menuTransform.add(itemRotDroite);
        menuTransform.add(itemRotGauche);
        menuTransform.add(itemRot180);
        menuTransform.addSeparator();
        menuTransform.add(itemLuminosite);
        menuTransform.add(itemContraste);
        menuTransform.add(itemTeinte);
        menuTransform.addSeparator();
        menuTransform.add(itemGris);
        menuTransform.add(itemNegatif);

        // Menu Outils
        JMenu menuOutils = new JMenu("Outils");
        
        JMenuItem itemFloodFill = new JMenuItem("Pot de peinture (cliquer sur l'image)");
        itemFloodFill.addActionListener(e -> activerFloodFill());
        
        JMenuItem itemCouleurPeinture = new JMenuItem("Choisir couleur du pot...");
        itemCouleurPeinture.addActionListener(e -> choisirCouleurPeinture());
        
        JMenuItem itemTolerance = new JMenuItem("Définir tolérance...");
        itemTolerance.addActionListener(e -> definirTolerance());
        
        menuOutils.add(itemFloodFill);
        menuOutils.add(itemCouleurPeinture);
        menuOutils.add(itemTolerance);

        // Menu Texte
        JMenu menuTexte = new JMenu("Texte");
        
        JMenuItem itemTexteSimple = new JMenuItem("Ajouter texte simple...");
        itemTexteSimple.addActionListener(e -> ajouterTexteSimple());
        
        JMenuItem itemTexteAvecFond = new JMenuItem("Ajouter texte avec fond...");
        itemTexteAvecFond.addActionListener(e -> ajouterTexteAvecFond());
        
        JMenuItem itemTexteCouleurImage = new JMenuItem("Texte avec couleur d'image...");
        itemTexteCouleurImage.addActionListener(e -> ajouterTexteCouleurImage());
        
        menuTexte.add(itemTexteSimple);
        menuTexte.add(itemTexteAvecFond);
        menuTexte.add(itemTexteCouleurImage);

        // Ajout des menus à la barre
        barreMenu.add(menuFichier);
        barreMenu.add(menuFusion);
        barreMenu.add(menuTransform);
        barreMenu.add(menuOutils);
        barreMenu.add(menuTexte);

        setJMenuBar(barreMenu);
    }

    // ========================================================================
    // CRÉATION DE L'INTERFACE
    // ========================================================================

    /**
     * Crée l'interface principale (zone d'affichage + contrôles).
     */
    private void creerInterface() {
        // Panneau principal
        JPanel panneauPrincipal = new JPanel(new BorderLayout());
        
        // Zone d'affichage de l'image
        panneauImage = new PanneauImage();
        JScrollPane scrollPane = new JScrollPane(panneauImage);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Image"));
        
        // Barre d'information en bas
        labelInfo = new JLabel("Aucune image chargée. Utilisez Fichier > Ouvrir");
        labelInfo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Panneau de boutons rapides
        JPanel panneauBoutons = creerPanneauBoutons();
        
        panneauPrincipal.add(scrollPane, BorderLayout.CENTER);
        panneauPrincipal.add(labelInfo, BorderLayout.SOUTH);
        panneauPrincipal.add(panneauBoutons, BorderLayout.EAST);
        
        add(panneauPrincipal);
    }

    /**
     * Crée le panneau de boutons rapides sur la droite.
     */
    private JPanel creerPanneauBoutons() {
        JPanel panneau = new JPanel(new GridLayout(0, 1, 5, 5));
        panneau.setBorder(BorderFactory.createTitledBorder("Actions rapides"));
        
        JButton btnOuvrir = new JButton("📂 Ouvrir");
        btnOuvrir.addActionListener(e -> ouvrirImagePrincipale());
        
        JButton btnSauver = new JButton("💾 Sauvegarder");
        btnSauver.addActionListener(e -> sauvegarderImage());
        
        JButton btnRestaurer = new JButton("↩️ Restaurer");
        btnRestaurer.addActionListener(e -> restaurerOriginale());
        
        JButton btnRotDroite = new JButton("↻ Rotation +90°");
        btnRotDroite.addActionListener(e -> appliquerRotation90Droite());
        
        JButton btnRotGauche = new JButton("↺ Rotation -90°");
        btnRotGauche.addActionListener(e -> appliquerRotation90Gauche());
        
        JButton btnGris = new JButton("🔲 Niveaux de gris");
        btnGris.addActionListener(e -> convertirGris());
        
        JButton btnPot = new JButton("🎨 Pot de peinture");
        btnPot.addActionListener(e -> activerFloodFill());
        
        JButton btnTexte = new JButton("📝 Ajouter texte");
        btnTexte.addActionListener(e -> ajouterTexteSimple());
        
        panneau.add(btnOuvrir);
        panneau.add(btnSauver);
        panneau.add(btnRestaurer);
        panneau.add(new JLabel(" "));
        panneau.add(btnRotDroite);
        panneau.add(btnRotGauche);
        panneau.add(btnGris);
        panneau.add(new JLabel(" "));
        panneau.add(btnPot);
        panneau.add(btnTexte);
        
        return panneau;
    }

    // ========================================================================
    // GESTION DES FICHIERS
    // ========================================================================

    /**
     * Ouvre une image principale.
     */
    private void ouvrirImagePrincipale() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Images PNG", "png"));
        
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fichier = chooser.getSelectedFile();
            imagePrincipale = ImageUtil.ouvrirImage(fichier.getAbsolutePath());
            
            if (imagePrincipale != null) {
                // Sauvegarde de l'originale
                imageOriginale = ImageUtil.copierImage(imagePrincipale);
                
                panneauImage.setImage(imagePrincipale);
                mettreAJourInfo();
                modeClick = ModeClick.AUCUN;
            }
        }
    }

    /**
     * Ouvre une image secondaire (pour fusion).
     */
    private void ouvrirImageSecondaire() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Images PNG", "png"));
        
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fichier = chooser.getSelectedFile();
            imageSecondaire = ImageUtil.ouvrirImage(fichier.getAbsolutePath());
            
            if (imageSecondaire != null) {
                labelInfo.setText("Image secondaire chargée : " + 
                    imageSecondaire.getWidth() + "x" + imageSecondaire.getHeight());
            }
        }
    }

    /**
     * Sauvegarde l'image courante.
     */
    private void sauvegarderImage() {
        if (imagePrincipale == null) {
            JOptionPane.showMessageDialog(this, "Aucune image à sauvegarder !");
            return;
        }
        
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Images PNG", "png"));
        
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String chemin = chooser.getSelectedFile().getAbsolutePath();
            if (!chemin.toLowerCase().endsWith(".png")) {
                chemin += ".png";
            }
            
            if (ImageUtil.sauvegarderImage(imagePrincipale, chemin)) {
                JOptionPane.showMessageDialog(this, "Image sauvegardée !");
            }
        }
    }

    /**
     * Restaure l'image originale.
     */
    private void restaurerOriginale() {
        if (imageOriginale != null) {
            imagePrincipale = ImageUtil.copierImage(imageOriginale);
            panneauImage.setImage(imagePrincipale);
            mettreAJourInfo();
        }
    }

    // ========================================================================
    // FUSION D'IMAGES
    // ========================================================================

    /**
     * Superpose l'image secondaire sur la principale.
     */
    private void superposerImages() {
        if (!verifierDeuxImages()) return;
        
        imagePrincipale = ImageFusion.superposer(
            imagePrincipale, imageSecondaire, positionX, positionY
        );
        panneauImage.setImage(imagePrincipale);
        mettreAJourInfo();
    }

    /**
     * Superpose avec gestion de l'alpha.
     */
    private void superposerAvecAlpha() {
        if (!verifierDeuxImages()) return;
        
        imagePrincipale = ImageFusion.superposerAvecAlpha(
            imagePrincipale, imageSecondaire, positionX, positionY
        );
        panneauImage.setImage(imagePrincipale);
        mettreAJourInfo();
    }

    /**
     * Superpose avec clé de chrominance.
     */
    private void superposerChromaKey() {
        if (!verifierDeuxImages()) return;
        
        // Choix de la couleur transparente
        Color couleur = JColorChooser.showDialog(this, "Choisir la couleur transparente", Color.GREEN);
        if (couleur == null) return;
        
        // Choix de la tolérance
        String tolStr = JOptionPane.showInputDialog(this, "Tolérance (0-100) :", "30");
        if (tolStr == null) return;
        
        double tolerance = Double.parseDouble(tolStr);
        int couleurARGB = ImageUtil.combinerComposantes(255, couleur.getRed(), couleur.getGreen(), couleur.getBlue());
        
        imagePrincipale = ImageFusion.superposerAvecCleTransparence(
            imagePrincipale, imageSecondaire, positionX, positionY,
            couleurARGB, tolerance
        );
        panneauImage.setImage(imagePrincipale);
        mettreAJourInfo();
    }

    /**
     * Fusionne les deux images.
     */
    private void fusionnerImages() {
        if (!verifierDeuxImages()) return;
        
        String ratioStr = JOptionPane.showInputDialog(this, 
            "Ratio de l'image principale (0.0 à 1.0) :", "0.5");
        if (ratioStr == null) return;
        
        double ratio = Double.parseDouble(ratioStr);
        
        imagePrincipale = ImageFusion.fusionner(imagePrincipale, imageSecondaire, ratio);
        panneauImage.setImage(imagePrincipale);
        mettreAJourInfo();
    }

    /**
     * Active le mode de sélection de position par clic.
     */
    private void activerModePosition() {
        if (imagePrincipale == null) {
            JOptionPane.showMessageDialog(this, "Chargez d'abord une image !");
            return;
        }
        modeClick = ModeClick.POSITION_IMAGE;
        labelInfo.setText("Mode POSITION : Cliquez sur l'image pour définir la position de superposition");
    }

    // ========================================================================
    // TRANSFORMATIONS
    // ========================================================================

    private void appliquerRotation90Droite() {
        if (!verifierImage()) return;
        imagePrincipale = ImageTransform.rotation90Horaire(imagePrincipale);
        panneauImage.setImage(imagePrincipale);
        mettreAJourInfo();
    }

    private void appliquerRotation90Gauche() {
        if (!verifierImage()) return;
        imagePrincipale = ImageTransform.rotation90AntiHoraire(imagePrincipale);
        panneauImage.setImage(imagePrincipale);
        mettreAJourInfo();
    }

    private void appliquerRotation180() {
        if (!verifierImage()) return;
        imagePrincipale = ImageTransform.rotation180(imagePrincipale);
        panneauImage.setImage(imagePrincipale);
        mettreAJourInfo();
    }

    private void ajusterLuminosite() {
        if (!verifierImage()) return;
        
        JSlider slider = new JSlider(-100, 100, 0);
        slider.setMajorTickSpacing(50);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        
        int result = JOptionPane.showConfirmDialog(this, slider, 
            "Ajuster la luminosité", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            imagePrincipale = ImageTransform.ajusterLuminosite(imagePrincipale, slider.getValue());
            panneauImage.setImage(imagePrincipale);
            mettreAJourInfo();
        }
    }

    private void ajusterContraste() {
        if (!verifierImage()) return;
        
        String facteurStr = JOptionPane.showInputDialog(this, 
            "Facteur de contraste (0.5 = réduit, 1.0 = normal, 2.0 = augmenté) :", "1.0");
        if (facteurStr == null) return;
        
        double facteur = Double.parseDouble(facteurStr);
        imagePrincipale = ImageTransform.ajusterContraste(imagePrincipale, facteur);
        panneauImage.setImage(imagePrincipale);
        mettreAJourInfo();
    }

    private void decalerTeinte() {
        if (!verifierImage()) return;
        
        JSlider slider = new JSlider(0, 360, 0);
        slider.setMajorTickSpacing(60);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        
        int result = JOptionPane.showConfirmDialog(this, slider, 
            "Décalage de teinte (degrés)", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            imagePrincipale = ImageTransform.decalerTeinte(imagePrincipale, slider.getValue());
            panneauImage.setImage(imagePrincipale);
            mettreAJourInfo();
        }
    }

    private void convertirGris() {
        if (!verifierImage()) return;
        imagePrincipale = ImageTransform.versNiveauxDeGris(imagePrincipale);
        panneauImage.setImage(imagePrincipale);
        mettreAJourInfo();
    }

    private void appliquerNegatif() {
        if (!verifierImage()) return;
        imagePrincipale = ImageTransform.inverserCouleurs(imagePrincipale);
        panneauImage.setImage(imagePrincipale);
        mettreAJourInfo();
    }

    // ========================================================================
    // POT DE PEINTURE (FLOOD FILL)
    // ========================================================================

    /**
     * Active le mode pot de peinture.
     */
    private void activerFloodFill() {
        if (!verifierImage()) return;
        modeClick = ModeClick.FLOOD_FILL;
        labelInfo.setText("Mode POT DE PEINTURE : Cliquez sur l'image pour remplir. " +
                         "Couleur : " + couleurToHex(couleurPeinture) + ", Tolérance : " + tolerancePeinture);
    }

    /**
     * Choisit la couleur du pot de peinture.
     */
    private void choisirCouleurPeinture() {
        Color nouvelle = JColorChooser.showDialog(this, "Couleur du pot de peinture", couleurPeinture);
        if (nouvelle != null) {
            couleurPeinture = nouvelle;
            labelInfo.setText("Couleur du pot de peinture : " + couleurToHex(couleurPeinture));
        }
    }

    /**
     * Définit la tolérance du pot de peinture.
     */
    private void definirTolerance() {
        String tolStr = JOptionPane.showInputDialog(this, 
            "Tolérance (0 = exact, 50 = similaire, 100 = très large) :", 
            String.valueOf(tolerancePeinture));
        if (tolStr != null) {
            tolerancePeinture = Double.parseDouble(tolStr);
        }
    }

    /**
     * Applique le pot de peinture à la position donnée.
     */
    private void appliquerFloodFill(int x, int y) {
        int couleurARGB = ImageUtil.combinerComposantes(
            255, couleurPeinture.getRed(), couleurPeinture.getGreen(), couleurPeinture.getBlue()
        );
        
        imagePrincipale = FloodFill.remplir(imagePrincipale, x, y, couleurARGB, tolerancePeinture);
        panneauImage.setImage(imagePrincipale);
        mettreAJourInfo();
    }

    // ========================================================================
    // TEXTE
    // ========================================================================

    /**
     * Ajoute du texte simple sur l'image.
     */
    private void ajouterTexteSimple() {
        if (!verifierImage()) return;
        
        JPanel panel = creerPanneauTexte();
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Ajouter du texte", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            JTextField tfTexte = (JTextField) panel.getComponent(1);
            JSpinner spX = (JSpinner) panel.getComponent(3);
            JSpinner spY = (JSpinner) panel.getComponent(5);
            JSpinner spTaille = (JSpinner) panel.getComponent(7);
            
            String texte = tfTexte.getText();
            int x = (Integer) spX.getValue();
            int y = (Integer) spY.getValue();
            int taille = (Integer) spTaille.getValue();
            
            Color couleur = JColorChooser.showDialog(this, "Couleur du texte", Color.BLACK);
            if (couleur == null) couleur = Color.BLACK;
            
            imagePrincipale = TextDrawer.dessinerTexte(imagePrincipale, texte, x, y, couleur, taille);
            panneauImage.setImage(imagePrincipale);
            mettreAJourInfo();
        }
    }

    /**
     * Ajoute du texte avec fond.
     */
    private void ajouterTexteAvecFond() {
        if (!verifierImage()) return;
        
        JPanel panel = creerPanneauTexte();
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Ajouter du texte avec fond", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            JTextField tfTexte = (JTextField) panel.getComponent(1);
            JSpinner spX = (JSpinner) panel.getComponent(3);
            JSpinner spY = (JSpinner) panel.getComponent(5);
            JSpinner spTaille = (JSpinner) panel.getComponent(7);
            
            String texte = tfTexte.getText();
            int x = (Integer) spX.getValue();
            int y = (Integer) spY.getValue();
            int taille = (Integer) spTaille.getValue();
            
            Color couleurTexte = JColorChooser.showDialog(this, "Couleur du texte", Color.WHITE);
            if (couleurTexte == null) couleurTexte = Color.WHITE;
            
            Color couleurFond = JColorChooser.showDialog(this, "Couleur du fond", Color.BLACK);
            if (couleurFond == null) couleurFond = Color.BLACK;
            
            imagePrincipale = TextDrawer.dessinerTexteAvecFond(
                imagePrincipale, texte, x, y, couleurTexte, couleurFond, taille, 10
            );
            panneauImage.setImage(imagePrincipale);
            mettreAJourInfo();
        }
    }

    /**
     * Ajoute du texte avec couleur issue d'une image.
     */
    private void ajouterTexteCouleurImage() {
        if (!verifierDeuxImages()) return;
        
        JPanel panel = creerPanneauTexte();
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Texte avec couleur de l'image secondaire", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            JTextField tfTexte = (JTextField) panel.getComponent(1);
            JSpinner spX = (JSpinner) panel.getComponent(3);
            JSpinner spY = (JSpinner) panel.getComponent(5);
            JSpinner spTaille = (JSpinner) panel.getComponent(7);
            
            String texte = tfTexte.getText();
            int x = (Integer) spX.getValue();
            int y = (Integer) spY.getValue();
            int taille = (Integer) spTaille.getValue();
            
            // La couleur du texte vient de l'image secondaire
            imagePrincipale = TextDrawer.dessinerTexteAvecCouleurImage(
                imagePrincipale, imageSecondaire, texte, x, y, taille
            );
            panneauImage.setImage(imagePrincipale);
            mettreAJourInfo();
        }
    }

    /**
     * Crée le panneau de saisie pour le texte.
     */
    private JPanel creerPanneauTexte() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        
        panel.add(new JLabel("Texte :"));
        panel.add(new JTextField("Hello World"));
        
        panel.add(new JLabel("Position X :"));
        panel.add(new JSpinner(new SpinnerNumberModel(50, 0, 10000, 10)));
        
        panel.add(new JLabel("Position Y :"));
        panel.add(new JSpinner(new SpinnerNumberModel(50, 0, 10000, 10)));
        
        panel.add(new JLabel("Taille police :"));
        panel.add(new JSpinner(new SpinnerNumberModel(32, 8, 200, 4)));
        
        return panel;
    }

    // ========================================================================
    // UTILITAIRES
    // ========================================================================

    /**
     * Vérifie qu'une image principale est chargée.
     */
    private boolean verifierImage() {
        if (imagePrincipale == null) {
            JOptionPane.showMessageDialog(this, "Veuillez d'abord charger une image !");
            return false;
        }
        return true;
    }

    /**
     * Vérifie que les deux images sont chargées.
     */
    private boolean verifierDeuxImages() {
        if (imagePrincipale == null) {
            JOptionPane.showMessageDialog(this, "Veuillez d'abord charger une image principale !");
            return false;
        }
        if (imageSecondaire == null) {
            JOptionPane.showMessageDialog(this, "Veuillez d'abord charger une image secondaire !");
            return false;
        }
        return true;
    }

    /**
     * Met à jour le label d'information.
     */
    private void mettreAJourInfo() {
        if (imagePrincipale != null) {
            labelInfo.setText("Image : " + imagePrincipale.getWidth() + " x " + 
                imagePrincipale.getHeight() + " pixels");
        }
    }

    /**
     * Convertit une couleur en hexadécimal.
     */
    private String couleurToHex(Color c) {
        return String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
    }

    // ========================================================================
    // CLASSE INTERNE : PANNEAU D'AFFICHAGE DE L'IMAGE
    // ========================================================================

    /**
     * Panneau personnalisé pour afficher l'image et gérer les clics.
     */
    private class PanneauImage extends JPanel {
        private BufferedImage image;

        public PanneauImage() {
            setBackground(new Color(200, 200, 200));
            
            // Gestion des clics
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (image == null) return;
                    
                    int x = e.getX();
                    int y = e.getY();
                    
                    // Vérification que le clic est sur l'image
                    if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) {
                        return;
                    }
                    
                    switch (modeClick) {
                        case FLOOD_FILL:
                            appliquerFloodFill(x, y);
                            break;
                        case POSITION_IMAGE:
                            positionX = x;
                            positionY = y;
                            labelInfo.setText("Position définie : (" + x + ", " + y + ")");
                            modeClick = ModeClick.AUCUN;
                            break;
                        default:
                            // Afficher les infos du pixel cliqué
                            int couleur = image.getRGB(x, y);
                            int[] comp = ImageUtil.extraireComposantes(couleur);
                            labelInfo.setText("Pixel (" + x + ", " + y + ") : " +
                                "R=" + comp[1] + " G=" + comp[2] + " B=" + comp[3] + " A=" + comp[0]);
                            break;
                    }
                }
            });
        }

        public void setImage(BufferedImage img) {
            this.image = img;
            if (img != null) {
                setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
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

    // ========================================================================
    // POINT D'ENTRÉE
    // ========================================================================

    /**
     * Méthode main - Point d'entrée de l'application.
     */
    public static void main(String[] args) {
        // Lancement de l'interface dans le thread de l'EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            new MainApp();
        });
    }
}

