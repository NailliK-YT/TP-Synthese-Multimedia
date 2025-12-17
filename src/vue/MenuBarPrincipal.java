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
public class MenuBarPrincipal extends JMenuBar 
{

	private ControleurFichier controleurFichier;
	private ControleurImage   controleurImage;

	/**
	 * Construit la barre de menu avec les contrôleurs nécessaires.
	 * 
	 * @param controleurFichier Le contrôleur de gestion des fichiers
	 * @param controleurImage   Le contrôleur de traitement d'images
	 */
	public MenuBarPrincipal(ControleurFichier controleurFichier, ControleurImage controleurImage) 
	{
		this.controleurFichier = controleurFichier;
		this.controleurImage   = controleurImage;

		this.creerMenus();
	}

	private void creerMenus() 
	{
		this.add(this.creerMenuFichier());
		this.add(this.creerMenuFusion ());
	}

	/**
	 * Crée le menu Fichier.
	 * 
	 * @return Le menu Fichier
	 */
	private JMenu creerMenuFichier() 
	{
		JMenu     menuFichier;
		JMenuItem itemOuvrir, itemOuvrirSecondaire, itemSauvegarder, itemRestaurer, itemQuitter;

		menuFichier = new JMenu("Fichier");

		itemOuvrir = new JMenuItem("Ouvrir image principale...");
		itemOuvrir.addActionListener(e -> controleurFichier.ouvrirImagePrincipale());

		itemOuvrirSecondaire = new JMenuItem("Ouvrir image secondaire...");
		itemOuvrirSecondaire.addActionListener(e -> controleurFichier.ouvrirImageSecondaire());

		itemSauvegarder = new JMenuItem("Sauvegarder...");
		itemSauvegarder.addActionListener(e -> controleurFichier.sauvegarderImage());

		itemRestaurer = new JMenuItem("Restaurer l'originale");
		itemRestaurer.addActionListener(e -> controleurFichier.restaurerOriginale());

		itemQuitter = new JMenuItem("Quitter");
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
	private JMenu creerMenuFusion() 
	{
		JMenu     menuFusion;
		JMenuItem itemSuperposer, itemSuperposerAlpha, itemChromaKey, itemFusionner;

		menuFusion = new JMenu("Fusion");

		itemSuperposer = new JMenuItem("Superposer image secondaire");
		itemSuperposer.addActionListener(e -> controleurImage.superposerImages());

		itemSuperposerAlpha = new JMenuItem("Superposer avec transparence");
		itemSuperposerAlpha.addActionListener(e -> controleurImage.superposerAvecAlpha());

		itemChromaKey = new JMenuItem("Superposer avec chroma key");
		itemChromaKey.addActionListener(e -> controleurImage.superposerChromaKey());

		itemFusionner = new JMenuItem("Juxtaposer avec fondu");
		itemFusionner.addActionListener(e -> controleurImage.fusionnerImages());

		menuFusion.add(itemSuperposer);
		menuFusion.add(itemSuperposerAlpha);
		menuFusion.add(itemChromaKey);
		menuFusion.add(itemFusionner);
		menuFusion.addSeparator();
		menuFusion.add(this.creerMenuTransformations());

		return menuFusion;
	}

	/**
	 * Crée le sous-menu Transformations.
	 * 
	 * @return Le menu Transformations
	 */
	private JMenu creerMenuTransformations() 
	{
		JMenu menuTransform;
		JMenuItem itemRotDroite, itemRotGauche, itemLuminosite, itemContraste, itemTeinte, itemGris, itemNegatif;

		menuTransform = new JMenu("Transformations");

		itemRotDroite = new JMenuItem("Rotation 90° droite");
		itemRotDroite.addActionListener(e -> controleurImage.appliquerRotation(90));

		itemRotGauche = new JMenuItem("Rotation 90° gauche");
		itemRotGauche.addActionListener(e -> controleurImage.appliquerRotation(-90));

		itemLuminosite = new JMenuItem("Ajuster luminosité...");
		itemLuminosite.addActionListener(e -> controleurImage.ajusterLuminosite());

		itemContraste = new JMenuItem("Ajuster contraste...");
		itemContraste.addActionListener(e -> controleurImage.ajusterContraste());

		itemTeinte = new JMenuItem("Décaler teinte...");
		itemTeinte.addActionListener(e -> controleurImage.decalerTeinte());

		itemGris = new JMenuItem("Niveaux de gris");
		itemGris.addActionListener(e -> controleurImage.convertirEnGris());

		itemNegatif = new JMenuItem("Négatif");
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
