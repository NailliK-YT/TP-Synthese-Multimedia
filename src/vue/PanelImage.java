package vue;

import controleur.ControleurImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * ============================================================================
 * PANNEAU D'AFFICHAGE D'IMAGE
 * ============================================================================
 * 
 * Composant responsable de l'affichage de l'image et de la gestion
 * des clics souris pour les outils interactifs (pot de peinture).
 * 
 * RESPONSABILITÉS :
 * - Afficher l'image principale
 * - Gérer les clics souris sur l'image
 * - Adapter la taille du panneau à l'image
 * 
 * @author Équipe 6 - BUT 3 Informatique
 */
public class PanelImage extends JPanel 
{

	private BufferedImage   image;
	private ControleurImage controleurImage;

	public PanelImage() 
	{
		this.setBackground(new Color(200, 200, 200));

		this.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) {
				if (PanelImage.this.controleurImage != null && PanelImage.this.image != null) 
				{
					PanelImage.this.controleurImage.appliquerPotPeinture(e.getX(), e.getY());
				}
			}
		});
	}

	/**
	 * Définit le contrôleur d'image pour déléguer les événements.
	 * 
	 * @param controleur Le contrôleur d'image
	 */
	public void setControleurImage(ControleurImage controleur) 
	{
		this.controleurImage = controleur;
	}

	/**
	 * Définit l'image à afficher.
	 * 
	 * @param image L'image à afficher
	 */
	public void setImage(BufferedImage image) 
	{
		this.image = image;
		if (image != null) 
		{
		   this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		}
		this.revalidate();
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		if (this.image != null) 
		{
			g.drawImage(this.image, 0, 0, null);
		}
	}
}
