package modele;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * MODÈLE - DONNÉES DE L'APPLICATION
 * ============================================================================
 * 
 * Cette classe contient toutes les données de l'application :
 * - Les images chargées (principale, secondaire, originale)
 * - L'état de l'application
 * 
 * PATTERN OBSERVER :
 * Le modèle notifie les écouteurs quand les données changent.
 * Cela permet à la vue de se mettre à jour automatiquement.
 * 
 * @author Équipe 6 - BUT 3 Informatique
 */
public class ModeleImage 
{

	/** Image principale affichée et modifiée */
	private BufferedImage imagePrincipale;

	/** Image secondaire pour les opérations de fusion */
	private BufferedImage imageSecondaire;

	/** Sauvegarde de l'image originale pour restauration */
	private BufferedImage imageOriginale;

	/** Liste des écouteurs à notifier lors des changements */
	private List<EcouteurModele> ecouteurs;

	/**
	 * Constructeur du modèle.
	 * Initialise les structures de données.
	 */
	public ModeleImage() 
	{
		this.ecouteurs = new ArrayList<>();

		this.imagePrincipale = null;
		this.imageSecondaire = null;
		this.imageOriginale  = null;
	}

	/**
	 * Définit une nouvelle image principale.
	 * Sauvegarde automatiquement une copie comme image originale.
	 * 
	 * @param image La nouvelle image principale
	 */
	public void definirImagePrincipale(BufferedImage image) 
	{
		this.imagePrincipale = image;

		if (image != null && this.imageOriginale == null) 
		{
			this.imageOriginale = UtilitaireImage.copierImage(image);
		}

		this.notifierChangement();
	}

	/**
	 * Met à jour l'image principale sans changer l'originale.
	 * Utilisé après des transformations.
	 * 
	 * @param image La nouvelle image principale
	 */
	public void mettreAJourImagePrincipale(BufferedImage image) 
	{
		this.imagePrincipale = image;
		this.notifierChangement();
	}

	/**
	 * Définit l'image secondaire (pour fusion).
	 * 
	 * @param image La nouvelle image secondaire
	 */
	public void definirImageSecondaire(BufferedImage image) 
	{
		this.imageSecondaire = image;
		this.notifierChangement();
	}

	/**
	 * Restaure l'image originale comme image principale.
	 */
	public void restaurerImageOriginale()
	{
		if (this.imageOriginale != null) 
		{
			this.imagePrincipale = UtilitaireImage.copierImage(this.imageOriginale);
			this.notifierChangement();
		}
	}

	/**
	 * Réinitialise toutes les images.
	 */
	public void reinitialiser() 
	{
		this.imagePrincipale = null;
		this.imageSecondaire = null;
		this.imageOriginale  = null;
		this.notifierChangement();
	}

	/**
	 * @return L'image principale (peut être null)
	 */
	public BufferedImage getImagePrincipale() { return this.imagePrincipale; }

	/**
	 * @return L'image secondaire (peut être null)
	 */
	public BufferedImage getImageSecondaire() { return this.imageSecondaire; }

	/**
	 * @return L'image originale (peut être null)
	 */
	public BufferedImage getImageOriginale() { return this.imageOriginale; }

	/**
	 * Vérifie si une image principale est chargée.
	 * 
	 * @return true si une image principale existe
	 */
	public boolean possedeImagePrincipale() 
	{
		return this.imagePrincipale != null;
	}

	/**
	 * Vérifie si une image secondaire est chargée.
	 * 
	 * @return true si une image secondaire existe
	 */
	public boolean possedeImageSecondaire() 
	{
		return this.imageSecondaire != null;
	}

	/**
	 * Vérifie si l'image originale peut être restaurée.
	 * 
	 * @return true si l'image originale existe
	 */
	public boolean peutRestaurer() 
	{
		return this.imageOriginale != null;
	}

	/**
	 * Ajoute un écouteur qui sera notifié des changements.
	 * 
	 * @param ecouteur L'écouteur à ajouter
	 */
	public void ajouterEcouteur(EcouteurModele ecouteur)
	{
		if (!this.ecouteurs.contains(ecouteur)) 
		{
			this.ecouteurs.add(ecouteur);
		}
	}

	/**
	 * Retire un écouteur.
	 * 
	 * @param ecouteur L'écouteur à retirer
	 */
	public void retirerEcouteur(EcouteurModele ecouteur) 
	{
		this.ecouteurs.remove(ecouteur);
	}

	/**
	 * Notifie tous les écouteurs qu'un changement a eu lieu.
	 */
	private void notifierChangement() 
	{
		for (EcouteurModele ecouteur : this.ecouteurs) 
		{
			ecouteur.modeleModifie();
		}
	}

	/**
	 * Interface pour les objets qui veulent être notifiés des changements
	 * du modèle (typiquement la vue et le contrôleur).
	 */
	public interface EcouteurModele 
	{
		/**
		 * Méthode appelée quand le modèle change.
		 */
		void modeleModifie();
	}
}
