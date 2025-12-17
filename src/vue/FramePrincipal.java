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
public class FramePrincipal extends JFrame 
{

	private ControleurFichier controleurFichier;
	private ControleurImage   controleurImage;

	private PanelImage        panelImage;
	private PanelOutils       panelOutils;
	private PanelStatut       panelStatut;

	private MenuBarPrincipal  menuBarPrincipal;

	public FramePrincipal() 
	{
		super("Éditeur d'Images - Architecture MVC - Équipe 6");

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1200, 800);
		this.setLocationRelativeTo(null);

		this.creerInterface();

		this.setVisible(true);
	}

	private void creerInterface() 
	{
		JPanel panelPrincipal;
		JScrollPane scrollPane;
		
		this.panelImage  = new PanelImage();
		this.panelOutils = new PanelOutils();
		this.panelStatut = new PanelStatut();

		panelPrincipal = new JPanel(new BorderLayout());

		scrollPane = new JScrollPane(this.panelImage);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Image"));

		panelPrincipal.add(scrollPane,       BorderLayout.CENTER);
		panelPrincipal.add(this.panelStatut, BorderLayout.SOUTH);
		panelPrincipal.add(this.panelOutils, BorderLayout.EAST);

		this.add(panelPrincipal);
	}

	/**
	 * Définit le contrôleur de fichier et initialise la barre de menu.
	 * 
	 * @param controleur Le contrôleur de fichier
	 */
	public void definirControleurFichier(ControleurFichier controleur) 
	{
		this.controleurFichier = controleur;

		this.panelOutils.setControleurFichier(controleur);

		if (this.controleurImage != null) 
		{
			this.menuBarPrincipal = new MenuBarPrincipal(this.controleurFichier, this.controleurImage);
			this.setJMenuBar(this.menuBarPrincipal);
		}
	}

	/**
	 * Définit le contrôleur d'image et finalise l'initialisation.
	 * 
	 * @param controleur Le contrôleur d'image
	 */
	public void definirControleurImage(ControleurImage controleur) 
	{
		this.controleurImage = controleur;

		this.panelImage.setControleurImage(controleur);
		this.panelOutils.setControleurImage(controleur);
		this.panelOutils.setFramePrincipal(this);

		this.panelOutils.initialiser();

		if (this.controleurFichier != null) 
		{
			this.menuBarPrincipal = new MenuBarPrincipal(this.controleurFichier, this.controleurImage);
			setJMenuBar(this.menuBarPrincipal);
		}
	}

	/**
	 * Affiche l'image dans le panneau d'affichage.
	 * 
	 * @param image L'image à afficher
	 */
	public void afficherImage(BufferedImage image) 
	{
		this.panelImage.setImage(image);
	}

	/**
	 * Met à jour le message de la barre de statut.
	 * 
	 * @param message Le message à afficher
	 */
	public void mettreAJourStatut(String message) 
	{
		this.panelStatut.mettreAJourStatut(message);
	}

	/**
	 * Affiche un message d'erreur dans une boîte de dialogue.
	 * 
	 * @param message Le message d'erreur
	 */
	public void afficherErreur(String message) 
	{
		JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
	}
}
