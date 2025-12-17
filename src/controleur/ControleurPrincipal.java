package controleur;

import modele.ModeleImage;
import vue.FramePrincipal;

/**
 * ============================================================================
 * CONTRÔLEUR PRINCIPAL - ARCHITECTURE MVC
 * ============================================================================
 * 
 * Le contrôleur fait le lien entre le modèle (données) et la vue (interface).
 * 
 * RESPONSABILITÉS :
 * - Coordonner les autres contrôleurs
 * - Gérer l'initialisation de l'application
 * - Faire le lien modèle-vue (pattern Observer)
 * 
 * PATTERN MVC :
 * Modèle → notifie → Contrôleur → met à jour → Vue
 * Vue → événements → Contrôleur → modifie → Modèle
 * 
 * @author Équipe 6 - BUT 3 Informatique
 */
public class ControleurPrincipal implements ModeleImage.EcouteurModele 
{

	/** Le modèle contenant les données de l'application */
	private final ModeleImage modele;

	/** La vue (interface graphique) */
	private final FramePrincipal vue;

	/** Sous-contrôleur pour les opérations fichiers */
	private final ControleurFichier controleurFichier;

	/** Sous-contrôleur pour les opérations sur images */
	private final ControleurImage controleurImage;

	/**
	 * Constructeur du contrôleur principal.
	 * 
	 * @param modele Le modèle de données
	 * @param vue    La vue principale
	 */
	public ControleurPrincipal(ModeleImage modele, FramePrincipal vue) 
	{
		this.modele = modele;
		this.vue    = vue;

		this.controleurFichier = new ControleurFichier(this.modele, this.vue);
		this.controleurImage   = new ControleurImage  (this.modele, this.vue);
	}

	/**
	 * Initialise le contrôleur et lie le modèle à la vue.
	 * 
	 * PATTERN OBSERVER :
	 * Le modèle notifie ce contrôleur quand il change,
	 * et le contrôleur met à jour la vue en conséquence.
	 */
	public void initialiser() 
	{
		this.modele.ajouterEcouteur(this);

		this.vue.definirControleurFichier(controleurFichier);
		this.vue.definirControleurImage(controleurImage);

		this.modeleModifie();

		System.out.println("Contrôleur principal initialisé");
	}

	/**
	 * Appelé quand le modèle a changé.
	 * On met à jour la vue en conséquence.
	 */
	@Override
	public void modeleModifie() 
	{
		int largeur, hauteur;

		this.vue.afficherImage(this.modele.getImagePrincipale());

		if (this.modele.possedeImagePrincipale()) 
		{
			largeur = this.modele.getImagePrincipale().getWidth();
			hauteur = this.modele.getImagePrincipale().getHeight();
			this.vue.mettreAJourStatut("Image : " + largeur + " x " + hauteur + " pixels");
		} 
		else 
		{
			this.vue.mettreAJourStatut("Aucune image chargée. Utilisez Fichier > Ouvrir");
		}
	}

	/**
	 * @return Le modèle de l'application
	 */
	public ModeleImage obtenirModele() { return this.modele; }

	/**
	 * @return La vue principale
	 */
	public FramePrincipal obtenirVue() { return this.vue; }

	/**
	 * @return Le contrôleur fichier
	 */
	public ControleurFichier obtenirControleurFichier() { return this.controleurFichier; }

	/**
	 * @return Le contrôleur image
	 */
	public ControleurImage obtenirControleurImage() { return this.controleurImage; }
}
