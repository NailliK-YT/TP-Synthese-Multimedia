package controleur;

import modele.ModeleImage;
import modele.UtilitaireImage;
import modele.traitement.*;
import vue.FramePrincipal;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

/**
 * ============================================================================
 * CONTRÔLEUR IMAGE - OPÉRATIONS SUR LES IMAGES
 * ============================================================================
 * 
 * Ce contrôleur gère toutes les opérations de traitement d'images :
 * - Transformations (rotation, luminosité, contraste, teinte)
 * - Fusion d'images
 * - Pot de peinture
 * - Ajout de texte
 * 
 * @author Équipe 6 - BUT 3 Informatique
 */
public class ControleurImage 
{

	private final ModeleImage    modele;
	private final FramePrincipal vue;

	private Color  couleurPeinture   = Color.RED;
	private double tolerancePeinture = 30.0;

	private int positionX = 0;
	private int positionY = 0;

	/**
	 * Constructeur du contrôleur d'image.
	 * 
	 * @param modele Le modèle contenant les images
	 * @param vue    La vue principale de l'application
	 */
	public ControleurImage(ModeleImage modele, FramePrincipal vue) 
	{
		this.modele = modele;
		this.vue    = vue;
	}

	/**
	 * Applique une rotation (en degré).
	 */
	public void appliquerRotation(int rotation) 
	{
		if (!this.verifierImage())
			return;

		BufferedImage resultat;

		resultat = TraitementTransformation.appliquerRotation(
			this.modele.getImagePrincipale(), 
			rotation
		);
		
		this.modele.mettreAJourImagePrincipale(resultat);
	}

	/**
	 * Affiche un dialogue permettant d'ajuster la luminosité de l'image.
	 * L'utilisateur peut choisir une valeur entre -100 (plus sombre) et +100 (plus
	 * clair).
	 */
	public void ajusterLuminosite() 
	{
		if (!this.verifierImage())
			return;

		JSlider curseur;
		int     resultat;

		BufferedImage imageResultat;

		curseur = new JSlider(-100, 100, 0);
		curseur.setMajorTickSpacing(50);
		curseur.setPaintTicks(true);
		curseur.setPaintLabels(true);

		resultat = JOptionPane.showConfirmDialog(
			this.vue, 
			curseur,
			"Ajuster la luminosité", 
			JOptionPane.OK_CANCEL_OPTION
		);

		if (resultat == JOptionPane.OK_OPTION) 
		{
			imageResultat = TraitementTransformation.ajusterLuminosite(
				this.modele.getImagePrincipale(), 
				curseur.getValue()
			);

			this.modele.mettreAJourImagePrincipale(imageResultat);
		}
	}

	/**
	 * Affiche un dialogue permettant d'ajuster le contraste de l'image.
	 * Facteurs typiques : 0.5 (réduit), 1.0 (normal), 2.0 (augmenté).
	 */
	public void ajusterContraste() {
		if (!this.verifierImage())
			return;

		String facteurStr;
		double facteur;

		BufferedImage resultat;

		facteurStr = JOptionPane.showInputDialog(
			this.vue,
			"Facteur de contraste (0.5 = réduit, 1.0 = normal, 2.0 = augmenté) :",
			ConfigurationDefaut.FACTEUR_CONTRASTE_DEFAUT
		);

		if (facteurStr == null) 
			return;

		try
		{
			facteur = Double.parseDouble(facteurStr);

			if (facteur <= 0) 
			{
				this.vue.afficherErreur("Le facteur doit être positif");
				return;
			}

			resultat = TraitementTransformation.ajusterContraste(
				this.modele.getImagePrincipale(), 
				facteur
			);

			this.modele.mettreAJourImagePrincipale(resultat);
		}
		catch (NumberFormatException e) 
		{
			this.vue.afficherErreur("'" + facteurStr + "' n'est pas un nombre valide");
		}
	}

	/**
	 * Affiche un dialogue permettant de décaler la teinte de l'image.
	 * L'utilisateur peut choisir un décalage entre 0 et 360 degrés sur le cercle
	 * chromatique.
	 */
	public void decalerTeinte() 
	{
		if (!this.verifierImage())
			return;

		JSlider curseur;
		int resultat;

		BufferedImage imageResultat;
		
		curseur= new JSlider(0, 360, 0);
		curseur.setMajorTickSpacing(60);
		curseur.setPaintTicks(true);
		curseur.setPaintLabels(true);

		resultat = JOptionPane.showConfirmDialog(
			this.vue, 
			curseur,
			"Décalage de teinte (degrés)", 
			JOptionPane.OK_CANCEL_OPTION
		);

		if (resultat == JOptionPane.OK_OPTION) 
		{
			imageResultat = TraitementTransformation.decalerTeinte(
				this.modele.getImagePrincipale(), 
				curseur.getValue()
			);
			
			this.modele.mettreAJourImagePrincipale(imageResultat);
		}
	}

	/**
	 * Convertit l'image principale en niveaux de gris.
	 */
	public void convertirEnGris() 
	{
		if (!this.verifierImage())
			return;
		
		BufferedImage resultat;

		resultat = TraitementTransformation.versNiveauxDeGris(
			this.modele.getImagePrincipale()
		);
		
		this.modele.mettreAJourImagePrincipale(resultat);
	}

	/**
	 * Applique un effet négatif (inversion des couleurs) à l'image principale.
	 */
	public void appliquerNegatif() 
	{
		if (!this.verifierImage())
			return;
		
		BufferedImage resultat;

		resultat = TraitementTransformation.inverserCouleurs(
			modele.getImagePrincipale()
		);

		modele.mettreAJourImagePrincipale(resultat);
	}

	/**
	 * Permet de définir la position de l'Image 2 pour la fusion.
	 */
	public void definirPositionImage2() 
	{
		if (!this.verifierDeuxImages())
			return;

		JPanel panel;
		JLabel labelX, labelY;
		JTextField txtX, txtY;

		int resultat;

		panel  = new JPanel(new GridLayout(3, 2, 10, 10));

		labelX = new JLabel("Position X :");
		txtX   = new JTextField(String.valueOf(positionX), 10);

		labelY = new JLabel("Position Y :");
		txtY   = new JTextField(String.valueOf(positionY), 10);

		JLabel labelInfo = new JLabel(
			"<html>Image 1 : " +
			this.modele.getImagePrincipale().getWidth() + "x" +
			this.modele.getImagePrincipale().getHeight() + "<br>Image 2 : " +
			this.modele.getImageSecondaire().getWidth() + "x" +
			this.modele.getImageSecondaire().getHeight() + "</html>"
		);

		panel.add(labelX);
		panel.add(txtX);
		panel.add(labelY);
		panel.add(txtY);
		panel.add(new JLabel("Dimensions :"));
		panel.add(labelInfo);

		resultat = JOptionPane.showConfirmDialog(
			this.vue,
			panel,
			"Définir la position de l'Image 2",
			JOptionPane.OK_CANCEL_OPTION,
			JOptionPane.PLAIN_MESSAGE
		);

		if (resultat == JOptionPane.OK_OPTION) 
		{
			try 
			{
				positionX = Integer.parseInt(txtX.getText());
				positionY = Integer.parseInt(txtY.getText());
				this.vue.mettreAJourStatut("Position Image 2 : (" + positionX + ", " + positionY + ")");

				this.afficherApercu();
			} 
			catch (NumberFormatException e) 
			{
				this.vue.afficherErreur("Veuillez entrer des nombres valides");
			}
		}
	}

	/**
	 * Affiche un aperçu de la superposition.
	 */
	private void afficherApercu() 
	{
		BufferedImage apercu;
		apercu = TraitementFusion.superposerAvecAlpha(
			this.modele.getImagePrincipale(),
			this.modele.getImageSecondaire(),
			positionX, 
			positionY
		);

		this.modele.mettreAJourImagePrincipale(apercu);
		this.vue.mettreAJourStatut("Aperçu affiché - Position : (" + positionX + ", " + positionY + ")");
	}

	/**
	 * Superpose l'image secondaire sur l'image principale à la position définie.
	 * Utilise une superposition simple sans gestion de la transparence.
	 */
	public void superposerImages() 
	{
		if (!this.verifierDeuxImages())
			return;

		BufferedImage resultat;

		resultat = TraitementFusion.superposer(
			this.modele.getImagePrincipale(),
			this.modele.getImageSecondaire(),
			positionX, 
			positionY
		);

		this.modele.mettreAJourImagePrincipale(resultat);
	}

	/**
	 * Superpose l'image secondaire sur l'image principale avec gestion de la
	 * transparence.
	 * Respecte le canal alpha de l'image secondaire.
	 */
	public void superposerAvecAlpha() 
	{
		if (!this.verifierDeuxImages())
			return;

		BufferedImage resultat;

		resultat = TraitementFusion.superposerAvecAlpha(
			this.modele.getImagePrincipale(),
			this.modele.getImageSecondaire(),
			positionX, 
			positionY
		);

		this.modele.mettreAJourImagePrincipale(resultat);
	}

	/**
	 * Superpose l'image secondaire en utilisant une clé de transparence (chroma
	 * key).
	 * Affiche un dialogue pour choisir la couleur à rendre transparente et la
	 * tolérance.
	 */
	public void superposerChromaKey() 
	{
		if (!this.verifierDeuxImages())
			return;

		Color couleur;
		String tolStr;
		double tolerance;
		int couleurARGB;

		BufferedImage resultat;

		couleur = JColorChooser.showDialog(vue, "Couleur transparente", Color.GREEN);
		if (couleur == null)
			return;

		tolStr = JOptionPane.showInputDialog(vue, "Tolérance (0-100) :", "30");
		if (tolStr == null)
			return;

		tolerance = Double.parseDouble(tolStr);

		couleurARGB = UtilitaireImage.combinerComposantes(
				255, couleur.getRed(), couleur.getGreen(), couleur.getBlue());

		resultat = TraitementFusion.superposerAvecCleTransparence(
			this.modele.getImagePrincipale(),
			this.modele.getImageSecondaire(),
			positionX, 
			positionY,
			couleurARGB, 
			tolerance
		);

		this.modele.mettreAJourImagePrincipale(resultat);
	}

	/**
	 * Juxtapose les deux images horizontalement côte à côte avec une zone de fondu.
	 * Demande à l'utilisateur la largeur de la zone de transition.
	 */
	public void fusionnerImages() 
	{
		if (!this.verifierDeuxImages())
			return;

		String        largeurStr;
		int           largeurFondu, largeur1, largeur2 ,largeurMax;
		BufferedImage resultat;

		largeurStr = JOptionPane.showInputDialog(
			this.vue,
			"Largeur de la zone de fondu (en pixels, 0 = pas de fondu) :",
			String.valueOf(ConfigurationDefaut.LARGEUR_FONDU_DEFAUT)
		);

		if (largeurStr == null)
			return;

		try 
		{
			largeurFondu = Integer.parseInt(largeurStr);

			if (largeurFondu < 0) 
			{
				this.vue.afficherErreur("La largeur doit être positive !");
				return;
			}

			largeur1   = this.modele.getImagePrincipale().getWidth();
			largeur2   = this.modele.getImageSecondaire().getWidth();
			largeurMax = Math.min(largeur1, largeur2);

			if (largeurFondu > largeurMax) 
			{
				this.vue.afficherErreur("Largeur trop grande ! Maximum : " + largeurMax + " pixels");
				return;
			}

			resultat = TraitementFusion.juxtaposerHorizontalement(
				this.modele.getImagePrincipale(),
				this.modele.getImageSecondaire(),
				largeurFondu
			);
			this.modele.mettreAJourImagePrincipale(resultat);
			this.vue.mettreAJourStatut("Images juxtaposées avec fondu de " + largeurFondu + " pixels");
		} 
		catch (NumberFormatException e) 
		{
			this.vue.afficherErreur("Veuillez entrer un nombre valide !");
		}
	}

	/**
	 * Applique l'outil pot de peinture aux coordonnées spécifiées.
	 * Remplit une zone de couleur similaire avec la couleur sélectionnée.
	 * 
	 * @param x Coordonnée X du point de départ
	 * @param y Coordonnée Y du point de départ
	 */
	public void appliquerPotPeinture(int x, int y) 
	{
		if (!this.verifierImage())
			return;

		int           couleurARGB;
		BufferedImage resultat;
		
		couleurARGB = UtilitaireImage.combinerComposantes(
			255, 
			couleurPeinture.getRed(), 
			couleurPeinture.getGreen(), 
			couleurPeinture.getBlue()
		);

		resultat = TraitementRemplissage.remplir(
			this.modele.getImagePrincipale(), 
			x, 
			y, 
			couleurARGB, 
			tolerancePeinture
		);

		this.modele.mettreAJourImagePrincipale(resultat);
	}

	/**
	 * Affiche un dialogue pour choisir la couleur du pot de peinture.
	 */
	public void choisirCouleurPeinture() 
	{
		Color nouvelle;

		nouvelle = JColorChooser.showDialog(this.vue, "Couleur du pot", couleurPeinture);
		
		if (nouvelle != null) 
		{
			couleurPeinture = nouvelle;

			this.vue.mettreAJourStatut(
				"Couleur du pot : " + String.format("#%02X%02X%02X",
				nouvelle.getRed(), 
				nouvelle.getGreen(), 
				nouvelle.getBlue())
			);
		}
	}

	/**
	 * Affiche un dialogue pour définir la tolérance du pot de peinture.
	 * Plus la tolérance est élevée, plus la zone de remplissage sera large.
	 */
	public void definirTolerance()
	{
		String tolStr;
		double tolerance;

		tolStr = JOptionPane.showInputDialog(
			this.vue,
			"Tolérance (0 = exact, 50 = similaire, 100 = large) :",
			String.valueOf(tolerancePeinture)
		);

		if (tolStr == null)
			return;

		try {
			tolerance = Double.parseDouble(tolStr);

			if (tolerance < 0 || tolerance > 255) 
			{
				this.vue.afficherErreur("La tolérance doit être entre 0 et 255 !");
				return;
			}

			tolerancePeinture = tolerance;
			this.vue.mettreAJourStatut("Tolérance du pot : " + (int) tolerance);
		} 
		catch (NumberFormatException e) 
		{
			this.vue.afficherErreur("Veuillez entrer un nombre valide !");
		}
	}

	/**
	 * Affiche un dialogue pour ajouter du texte simple à l'image.
	 * L'utilisateur peut choisir le texte, la position et la couleur.
	 */
	public void ajouterTexteSimple() 
	{
		if (!this.verifierImage())
			return;

		JPanel     panel;
		JLabel     labelTexte, labelX, labelY;
		JTextField txtTexte, txtX, txtY;

		int    resultat;
		String texte;
		int    x, y, largeurImage, hauteurImage;

		Color         couleur;
		BufferedImage resultatImage;

		panel      = new JPanel(new GridLayout(3, 2, 10, 10));

		labelTexte = new JLabel("Texte :");
		txtTexte   = new JTextField(ConfigurationDefaut.TEXTE_DEFAUT, 20);

		labelX     = new JLabel("Position X :");
		txtX       = new JTextField(ConfigurationDefaut.POSITION_X_DEFAUT, 10);

		labelY     = new JLabel("Position Y :");
		txtY       = new JTextField(ConfigurationDefaut.POSITION_Y_DEFAUT, 10);

		panel.add(labelTexte);
		panel.add(txtTexte);
		panel.add(labelX);
		panel.add(txtX);
		panel.add(labelY);
		panel.add(txtY);

		resultat = JOptionPane.showConfirmDialog(
			this.vue,
			panel,
			"Ajouter du texte",
			JOptionPane.OK_CANCEL_OPTION
		);

		if (resultat != JOptionPane.OK_OPTION)
			return;

		texte = txtTexte.getText();
		if (texte.isEmpty())
			return;

		try
		{
			x = Integer.parseInt(txtX.getText());
			y = Integer.parseInt(txtY.getText());
		} 
		catch (NumberFormatException e) 
		{
			this.vue.afficherErreur("Veuillez entrer des nombres valides pour X et Y !");
			return;
		}

		largeurImage = this.modele.getImagePrincipale().getWidth();
		hauteurImage = this.modele.getImagePrincipale().getHeight();

		if (x < 0 || x >= largeurImage || y < 0 || y >= hauteurImage) 
		{
			this.vue.afficherErreur(
				"Position hors de l'image (0-" + (largeurImage - 1) + ", 0-" +  (hauteurImage - 1) + ")"
			);
			return;
		}

		couleur = JColorChooser.showDialog(this.vue, "Couleur du texte", Color.BLACK);
		
		if (couleur == null)
			couleur = Color.BLACK;

		resultatImage = TraitementTexte.dessinerTexte(
			this.modele.getImagePrincipale(), 
			texte,
			x, 
			y, 
			couleur, 
			ConfigurationDefaut.TAILLE_POLICE_TEXTE
		);
		
		this.modele.mettreAJourImagePrincipale(resultatImage);
		this.vue.mettreAJourStatut("Texte ajoute a la position (" + x + ", " + y + ")");
	}

	/**
	 * Affiche un dialogue pour ajouter du texte avec un fond coloré à l'image.
	 * L'utilisateur peut choisir le texte, la position, la couleur du texte et la
	 * couleur du fond.
	 */
	public void ajouterTexteAvecFond() 
	{
		if (!this.verifierImage())
			return;

		JPanel     panel;
		JLabel     labelTexte, labelX, labelY;
		JTextField txtTexte, txtX, txtY;

		int    resultat;
		String texte;
		int    x, y, largeurImage, hauteurImage;

		Color         couleurTexte, couleurFond;
		BufferedImage resultatImage;

		panel      = new JPanel(new GridLayout(3, 2, 10, 10));

		labelTexte = new JLabel("Texte :");
		txtTexte   = new JTextField(ConfigurationDefaut.TEXTE_DEFAUT, 20);

		labelX     = new JLabel("Position X :");
		txtX       = new JTextField(ConfigurationDefaut.POSITION_X_DEFAUT, 10);

		labelY     = new JLabel("Position Y :");
		txtY       = new JTextField(ConfigurationDefaut.POSITION_Y_DEFAUT, 10);

		panel.add(labelTexte);
		panel.add(txtTexte);
		panel.add(labelX);
		panel.add(txtX);
		panel.add(labelY);
		panel.add(txtY);

		resultat = JOptionPane.showConfirmDialog(
			this.vue,
			panel,
			"Ajouter du texte avec fond",
			JOptionPane.OK_CANCEL_OPTION
		);

		if (resultat != JOptionPane.OK_OPTION)
			return;

		texte = txtTexte.getText();

		if (texte.isEmpty())
			return;

		try 
		{
			x = Integer.parseInt(txtX.getText());
			y = Integer.parseInt(txtY.getText());
		} 
		catch (NumberFormatException e) 
		{
			this.vue.afficherErreur("Veuillez entrer des nombres valides pour X et Y");
			return;
		}

		largeurImage = this.modele.getImagePrincipale().getWidth();
		hauteurImage = this.modele.getImagePrincipale().getHeight();

		if (x < 0 || x >= largeurImage || y < 0 || y >= hauteurImage) 
		{
			this.vue.afficherErreur(
				"Position hors de l'image (0-" + (largeurImage - 1) + ", 0-" + (hauteurImage - 1) + ")"
			);
			return;
		}

		couleurTexte = JColorChooser.showDialog(this.vue, "Couleur du texte", Color.WHITE);
		if (couleurTexte == null)
			couleurTexte = Color.WHITE;

		couleurFond = JColorChooser.showDialog(this.vue, "Couleur du fond", Color.BLACK);
		if (couleurFond == null)
			couleurFond = Color.BLACK;

		resultatImage = TraitementTexte.dessinerTexteAvecFond(
			this.modele.getImagePrincipale(), 
			texte, 
			x, 
			y,
			couleurTexte, 
			couleurFond, 
			ConfigurationDefaut.TAILLE_POLICE_TEXTE,
			ConfigurationDefaut.MARGE_FOND_TEXTE
		);

		this.modele.mettreAJourImagePrincipale(resultatImage);
		this.vue.mettreAJourStatut("Texte avec fond ajoute a la position (" + x + ", " + y + ")");
	}

	/**
	 * Affiche un dialogue pour ajouter du texte coloré avec les pixels de l'image
	 * 2.
	 * L'utilisateur peut choisir le texte et la position. La couleur est extraite
	 * de l'image secondaire.
	 */
	public void ajouterTexteCouleurImage() 
	{
		if (!this.verifierDeuxImages())
			return;

		JPanel     panel;
		JLabel     labelTexte, labelX, labelY;
		JTextField txtTexte, txtX, txtY;

		int    resultat;
		String texte;
		int    x, y, largeurImage, hauteurImage;

		BufferedImage resultatImage;

		panel      = new JPanel(new GridLayout(3, 2, 10, 10));

		labelTexte = new JLabel("Texte :");
		txtTexte   = new JTextField(ConfigurationDefaut.TEXTE_DEFAUT, 20);

		labelX     = new JLabel("Position X :");
		txtX       = new JTextField(ConfigurationDefaut.POSITION_X_DEFAUT, 10);

		labelY     = new JLabel("Position Y :");
		txtY       = new JTextField(ConfigurationDefaut.POSITION_Y_DEFAUT, 10);

		panel.add(labelTexte);
		panel.add(txtTexte);
		panel.add(labelX);
		panel.add(txtX);
		panel.add(labelY);
		panel.add(txtY);

		resultat = JOptionPane.showConfirmDialog(
			this.vue,
			panel,
			"Texte avec couleur d'image (Image 2)",
			JOptionPane.OK_CANCEL_OPTION
		);

		if (resultat != JOptionPane.OK_OPTION)
			return;

		texte = txtTexte.getText();

		if (texte.isEmpty())
			return;

		try 
		{
			x = Integer.parseInt(txtX.getText());
			y = Integer.parseInt(txtY.getText());
		} 
		catch (NumberFormatException e) 
		{
			this.vue.afficherErreur("Veuillez entrer des nombres valides pour X et Y");
			return;
		}

		largeurImage = this.modele.getImagePrincipale().getWidth();
		hauteurImage = this.modele.getImagePrincipale().getHeight();

		if (x < 0 || x >= largeurImage || y < 0 || y >= hauteurImage) 
		{
			this.vue.afficherErreur("Position hors de l'image (0-" + (largeurImage - 1) + ", 0-" + (hauteurImage - 1) + ")");
			return;
		}

		resultatImage = TraitementTexte.dessinerTexteAvecCouleurImage(
			this.modele.getImagePrincipale(),
			this.modele.getImageSecondaire(),
			texte,
			x, 
			y,
			ConfigurationDefaut.TAILLE_POLICE_TEXTE
		);

		this.modele.mettreAJourImagePrincipale(resultatImage);
		this.vue.mettreAJourStatut("Texte coloré ajouté à la position (" + x + ", " + y + ")");
	}

	/**
	 * Vérifie qu'une image principale est chargée.
	 * 
	 * @return true si une image est chargée, false sinon
	 */
	private boolean verifierImage() 
	{
		if (!this.modele.possedeImagePrincipale()) 
		{
			this.vue.afficherErreur("Veuillez charger une image");
			return false;
		}

		return true;
	}

	/**
	 * Vérifie que les deux images (principale et secondaire) sont chargées.
	 * 
	 * @return true si les deux images sont chargées, false sinon
	 */
	private boolean verifierDeuxImages() 
	{
		if (!this.modele.possedeImagePrincipale()) 
		{
			this.vue.afficherErreur("Veuillez charger une image principale");
			return false;
		}

		if (!this.modele.possedeImageSecondaire())
		{
			this.vue.afficherErreur("Veuillez charger une image secondaire");
			return false;
		}

		return true;
	}

	/**
	 * Définit la position pour la superposition de l'image secondaire.
	 * 
	 * @param x Coordonnée X de la position
	 * @param y Coordonnée Y de la position
	 */
	public void definirPosition(int x, int y) 
	{
		this.positionX = x;
		this.positionY = y;
	}
}
