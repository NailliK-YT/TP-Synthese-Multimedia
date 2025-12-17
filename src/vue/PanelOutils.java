package vue;

import controleur.ControleurFichier;
import controleur.ControleurImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * ============================================================================
 * PANNEAU D'OUTILS
 * ============================================================================
 * 
 * Composant responsable du panneau latéral contenant tous les boutons
 * d'outils et d'actions.
 * 
 * RESPONSABILITÉS :
 * - Créer et organiser les boutons d'outils
 * - Connecter les actions aux contrôleurs
 * - Gérer les sections visuelles
 * 
 * @author Équipe 6 - BUT 3 Informatique
 */
public class PanelOutils extends JPanel 
{

	private ControleurFichier controleurFichier;
	private ControleurImage   controleurImage;
	private FramePrincipal    framePrincipal;

	public PanelOutils() 
	{
		this.setLayout(new BorderLayout());
	}

	/**
	 * Définit le contrôleur de fichiers.
	 * 
	 * @param controleur Le contrôleur de fichiers
	 */
	public void setControleurFichier(ControleurFichier controleur) 
	{
		this.controleurFichier = controleur;
	}

	/**
	 * Définit le contrôleur d'image.
	 * 
	 * @param controleur Le contrôleur d'image
	 */
	public void setControleurImage(ControleurImage controleur) 
	{
		this.controleurImage = controleur;
	}

	/**
	 * Définit le frame principal pour les mises à jour de statut.
	 * 
	 * @param frame Le frame principal
	 */
	public void setFramePrincipal(FramePrincipal frame) 
	{
		this.framePrincipal = frame;
	}

	/**
	 * Initialise le panneau avec tous les boutons.
	 * Doit être appelé après avoir défini les contrôleurs.
	 */
	public void initialiser() 
	{
		this.removeAll();
		this.add(this.creerPanneauBoutons(), BorderLayout.CENTER);
		this.revalidate();
		this.repaint();
	}

	private JPanel creerPanneauBoutons() 
	{
		JPanel panelPrincipal, sectionFichiers, sectionFusion, sectionTransform, sectionOutils;

		JScrollPane scrollPane;
		JPanel      wrapper;

		panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
		panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		sectionFichiers = creerSection("FICHIERS");
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

		sectionFusion = creerSection("FUSION 2 IMAGES");
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
		ajouterBouton(sectionFusion, "Juxtaposer", e -> {
			if (controleurImage != null)
				controleurImage.fusionnerImages();
		});

		sectionTransform = creerSection("TRANSFORMATIONS");
		ajouterBouton(sectionTransform, "Rotation Droite", e -> {
			if (controleurImage != null)
				controleurImage.appliquerRotation(90);
		});
		ajouterBouton(sectionTransform, "Rotation Gauche", e -> {
			if (controleurImage != null)
				controleurImage.appliquerRotation(-90);
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

		sectionOutils = creerSection("OUTILS DESSIN");
		ajouterBouton(sectionOutils, "Pot de Peinture", e -> {
			if (controleurImage != null) 
			{
				controleurImage.choisirCouleurPeinture();
				controleurImage.definirTolerance();
				if (framePrincipal != null) 
				{
					framePrincipal.mettreAJourStatut("Cliquez sur l'image pour peindre !");
				}
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

		panelPrincipal.add(sectionFichiers);
		panelPrincipal.add(Box.createVerticalStrut(10));
		panelPrincipal.add(sectionFusion);
		panelPrincipal.add(Box.createVerticalStrut(10));
		panelPrincipal.add(sectionTransform);
		panelPrincipal.add(Box.createVerticalStrut(10));
		panelPrincipal.add(sectionOutils);
		panelPrincipal.add(Box.createVerticalGlue());

		scrollPane = new JScrollPane(panelPrincipal);
		scrollPane.setBorder(null);
		scrollPane.setPreferredSize(new Dimension(250, 0));

		wrapper = new JPanel(new BorderLayout());
		wrapper.add(scrollPane, BorderLayout.CENTER);

		return wrapper;
	}

	/**
	 * Crée une section visuelle pour regrouper des boutons.
	 * 
	 * @param titre Le titre de la section
	 * @return Le panneau de section
	 */
	private JPanel creerSection(String titre) 
	{
		JPanel section;

		section= new JPanel();
		section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
		section.setBorder(BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
			titre,
			javax.swing.border.TitledBorder.LEFT,
			javax.swing.border.TitledBorder.TOP,
			new Font("Arial", Font.BOLD, 12),
			new Color(0, 100, 200))
		);

		return section;
	}

	/**
	 * Ajoute un bouton à une section.
	 * 
	 * @param section La section cible
	 * @param texte   Le texte du bouton
	 * @param action  L'action à exécuter lors du clic
	 */
	private void ajouterBouton(JPanel section, String texte, ActionListener action) 
	{
		JButton bouton;

		bouton = new JButton(texte);
		bouton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
		bouton.setAlignmentX(Component.LEFT_ALIGNMENT);
		bouton.addActionListener(action);
		bouton.setFont(new Font("Arial", Font.PLAIN, 11));
		
		section.add(bouton);
		section.add(Box.createVerticalStrut(3));
	}
}
