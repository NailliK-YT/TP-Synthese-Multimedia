package controleur;

import modele.ModeleImage;
import modele.UtilitaireImage;
import vue.FramePrincipal;

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
public class ControleurFichier 
{

	private final ModeleImage    modele;
	private final FramePrincipal vue;

	public ControleurFichier(ModeleImage modele, FramePrincipal vue) 
	{
		this.modele = modele;
		this.vue    = vue;
	}

	/**
	 * Ouvre une image principale via un dialogue de fichier.
	 */
	public void ouvrirImagePrincipale() 
	{
		JFileChooser  selecteur;
		int           resultat;

		File          fichier;
		BufferedImage image;

		
		selecteur = new JFileChooser();
		selecteur.setDialogTitle("Ouvrir une image principale");
		selecteur.setFileFilter (new FileNameExtensionFilter("Images PNG", "png"));

		resultat = selecteur.showOpenDialog(this.vue);

		if (resultat == JFileChooser.APPROVE_OPTION) 
		{
			fichier = selecteur.getSelectedFile();
			image   = UtilitaireImage.ouvrirImage(fichier.getAbsolutePath());

			if (image != null) 
			{
				this.modele.definirImagePrincipale(image);
				this.vue.mettreAJourStatut("Image chargée : " + fichier.getName());
			} 
			else 
			{
				this.vue.afficherErreur("Impossible de charger l'image !");
			}
		}
	}

	/**
	 * Ouvre une image secondaire pour les opérations de fusion.
	 */
	public void ouvrirImageSecondaire() 
	{
		JFileChooser  selecteur;
		int           resultat;

		File          fichier;
		BufferedImage image;

		selecteur = new JFileChooser();
		selecteur.setDialogTitle("Ouvrir une image secondaire");
		selecteur.setFileFilter(new FileNameExtensionFilter("Images PNG", "png"));

		resultat = selecteur.showOpenDialog(vue);

		if (resultat == JFileChooser.APPROVE_OPTION) 
		{
			fichier = selecteur.getSelectedFile();
			image = UtilitaireImage.ouvrirImage(fichier.getAbsolutePath());

			if (image != null) 
			{
				this.modele.definirImageSecondaire(image);
				this.vue.mettreAJourStatut(
					"Image secondaire chargée : "
					+ image.getWidth() 
					+ "x" 
					+ image.getHeight()
				);
			} 
			else 
			{
				this.vue.afficherErreur("Impossible de charger l'image secondaire");
			}
		}
	}

	/**
	 * Sauvegarde l'image actuelle.
	 */
	public void sauvegarderImage() 
	{
		JFileChooser selecteur;
		int          resultat;
		String       chemin;

		if (!this.modele.possedeImagePrincipale()) 
		{
			this.vue.afficherErreur("Aucune image à sauvegarder");
			return;
		}

		selecteur = new JFileChooser();
		selecteur.setDialogTitle("Sauvegarder l'image");
		selecteur.setFileFilter(new FileNameExtensionFilter("Images PNG", "png"));

		resultat = selecteur.showSaveDialog(vue);

		if (resultat == JFileChooser.APPROVE_OPTION) 
		{
			chemin = selecteur.getSelectedFile().getAbsolutePath();

			if (!chemin.toLowerCase().endsWith(".png")) 
			{
				chemin += ".png";
			}

			if (UtilitaireImage.sauvegarderImage(this.modele.getImagePrincipale(), chemin)) 
			{
				this.vue.mettreAJourStatut("Image sauvegardée : " + chemin);
				JOptionPane.showMessageDialog(this.vue, "Image sauvegardée avec succès");
			}
			else 
			{
				this.vue.afficherErreur("Erreur lors de la sauvegarde");
			}
		}
	}

	/**
	 * Restaure l'image originale.
	 */
	public void restaurerOriginale() 
	{
		if (!this.modele.peutRestaurer()) 
		{
			this.vue.afficherErreur("Aucune image originale à restaurer !");
			return;
		}

		this.modele.restaurerImageOriginale();
		this.vue.mettreAJourStatut("Image originale restaurée");
	}

	/**
	 * Quitte l'application.
	 */
	public void quitter() {
		int choix;

		choix = JOptionPane.showConfirmDialog(
			this.vue,
			"Voulez-vous vraiment quitter ?",
			"Quitter",
			JOptionPane.YES_NO_OPTION
		);

		if (choix == JOptionPane.YES_OPTION)
		{
			System.exit(0);
		}
	}
}
