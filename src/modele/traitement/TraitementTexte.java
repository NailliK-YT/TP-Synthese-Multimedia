package modele.traitement;

import modele.UtilitaireImage;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * ============================================================================
 * CLASSE POUR LE DESSIN DE TEXTE
 * ============================================================================
 * 
 * Cette classe permet de dessiner du texte sur une image avec :
 * - Un fond coloré derrière le texte
 * - La possibilité de définir la couleur du texte depuis une image
 * - Différentes options de style (police, taille, etc.)
 * 
 * On utilise Graphics2D pour le rendu du texte.
 * 
 * @author Équipe 6 - BUT 3 Informatique
 */
public class TraitementTexte
{
	/**
	 * Dessine du texte sur une image à une position donnée.
	 * 
	 * PRINCIPE :
	 * 1. Obtenir le contexte Graphics2D de l'image
	 * 2. Configurer la police et la couleur
	 * 3. Dessiner le texte avec drawString()
	 * 
	 * @param image        L'image sur laquelle dessiner (sera modifiée)
	 * @param texte        Le texte à dessiner
	 * @param x            Position X du texte
	 * @param y            Position Y du texte (ligne de base)
	 * @param couleur      Couleur du texte
	 * @param taillePolice Taille de la police
	 * @return L'image avec le texte
	 */
	public static BufferedImage dessinerTexte(
		BufferedImage image, String texte,
		int x, int y, 
		Color couleur, int taillePolice
	) {
		BufferedImage resultat;
		Graphics2D    g2d;
		Font          police;

		resultat = UtilitaireImage.copierImage(image);

		g2d = resultat.createGraphics();

		g2d.setRenderingHint(
			RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON
		);

		police = new Font("SansSerif", Font.BOLD, taillePolice);
		
		g2d.setFont   (police);
		g2d.setColor  (couleur);
		g2d.drawString(texte, x, y);
		g2d.dispose   ();

		return resultat;
	}

	/**
	 * Dessine du texte avec un rectangle de fond derrière.
	 * 
	 * ALGORITHME :
	 * 1. Calculer les dimensions du texte avec FontMetrics
	 * 2. Dessiner un rectangle de fond (avec marge)
	 * 3. Dessiner le texte par-dessus
	 * 
	 * @param image        L'image sur laquelle dessiner
	 * @param texte        Le texte à dessiner
	 * @param x            Position X
	 * @param y            Position Y
	 * @param couleurTexte Couleur du texte
	 * @param couleurFond  Couleur du fond
	 * @param taillePolice Taille de la police
	 * @param marge        Marge autour du texte (en pixels)
	 * @return L'image avec le texte et son fond
	 */
	public static BufferedImage dessinerTexteAvecFond(
		BufferedImage image, String texte,
		int x, int y,
		Color couleurTexte, Color couleurFond,
		int taillePolice, int marge
	) {
		BufferedImage resultat;
		Graphics2D    g2d;
		Font          police;
		FontMetrics   metrics;
		int           largeurTexte, hauteurTexte, ascent;
		int           rectX, rectY, rectLargeur, rectHauteur;

		resultat = UtilitaireImage.copierImage(image);
		
		g2d = resultat.createGraphics();

		g2d.setRenderingHint(
			RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON
		);

		police = new Font("SansSerif", Font.BOLD, taillePolice);
		g2d.setFont(police);

		metrics = g2d.getFontMetrics();

		largeurTexte = metrics.stringWidth(texte);
		hauteurTexte = metrics.getHeight();
		ascent = metrics.getAscent();

		rectX = x - marge;
		rectY = y - ascent - marge;
		rectLargeur = largeurTexte + 2 * marge;
		rectHauteur = hauteurTexte + 2 * marge;

		g2d.setColor(couleurFond);
		g2d.fillRect(rectX, rectY, rectLargeur, rectHauteur);
		g2d.setColor(couleurTexte);
		g2d.drawString(texte, x, y);
		g2d.dispose();

		return resultat;
	}

	/**
	 * Dessine du texte dont la couleur est extraite d'une image de référence.
	 * 
	 * PRINCIPE :
	 * Pour chaque pixel du texte, on prend la couleur correspondante
	 * dans l'image de référence (comme un masque).
	 * 
	 * ALGORITHME :
	 * 1. Créer une image temporaire avec le texte en blanc sur fond transparent
	 * 2. Pour chaque pixel blanc, récupérer la couleur de l'image source
	 * 3. Appliquer cette couleur au pixel
	 * 
	 * @param imageDestination L'image sur laquelle dessiner
	 * @param imageCouleurs    L'image source des couleurs
	 * @param texte            Le texte à dessiner
	 * @param x                Position X
	 * @param y                Position Y
	 * @param taillePolice     Taille de la police
	 * @return L'image avec le texte coloré
	 */
	public static BufferedImage dessinerTexteAvecCouleurImage(
		BufferedImage imageDestination, BufferedImage imageCouleurs,
		String texte, int x, int y, int taillePolice
	) {
		BufferedImage resultat, masque;
		Graphics2D    gMasque;
		Font          police;

		int   couleurMasque, srcX, srcY, couleurSource, nouvelleCouleur;
		int[] compMasque, compSource;

		resultat = UtilitaireImage.copierImage(imageDestination);
		masque   = UtilitaireImage.creerImageVide(
			resultat.getWidth (), 
			resultat.getHeight()
		);

		gMasque = masque.createGraphics();
		gMasque.setRenderingHint(
			RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON
		);

		police = new Font("SansSerif", Font.BOLD, taillePolice);

		gMasque.setFont(police);
		gMasque.setColor(Color.WHITE);
		gMasque.drawString(texte, x, y);
		gMasque.dispose();

		for (int py = 0; py < masque.getHeight(); py++) 
		{
			for (int px = 0; px < masque.getWidth(); px++) 
			{
				couleurMasque = masque.getRGB(px, py);
				compMasque = UtilitaireImage.extraireComposantes(couleurMasque);

				if (compMasque[1] > 128) 
				{
					srcX = px % imageCouleurs.getWidth();
					srcY = py % imageCouleurs.getHeight();

					couleurSource = imageCouleurs.getRGB(srcX, srcY);

					compSource = UtilitaireImage.extraireComposantes(couleurSource);
					
					nouvelleCouleur = UtilitaireImage.combinerComposantes(
						compMasque[0],
						compSource[1],
						compSource[2], 
						compSource[3]
					);

					resultat.setRGB(px, py, nouvelleCouleur);
				}
			}
		}

		return resultat;
	}

	/**
	 * Dessine du texte avec un fond, la couleur du texte venant d'une image.
	 * 
	 * Combine les deux fonctionnalités précédentes.
	 * 
	 * @param imageDestination L'image de destination
	 * @param imageCouleurs    L'image source des couleurs du texte
	 * @param texte            Le texte à dessiner
	 * @param x                Position X
	 * @param y                Position Y
	 * @param couleurFond      Couleur du fond
	 * @param taillePolice     Taille de la police
	 * @param marge            Marge autour du texte
	 * @return L'image avec le texte coloré et son fond
	 */
	public static BufferedImage dessinerTexteComplet(
		BufferedImage imageDestination, BufferedImage imageCouleurs,
		String texte, int x, int y,
		Color couleurFond, int taillePolice, int marge
	) {

		BufferedImage resultat;
		Graphics2D    g2d;
		Font          police;
		FontMetrics   metrics;
		int           largeurTexte, hauteurTexte, ascent;

		resultat = UtilitaireImage.copierImage(imageDestination);
		g2d = resultat.createGraphics();

		g2d.setRenderingHint(
			RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON
		);

		police = new Font("SansSerif", Font.BOLD, taillePolice);
		g2d.setFont(police);

		metrics      = g2d.getFontMetrics();
		largeurTexte = metrics.stringWidth(texte);
		hauteurTexte = metrics.getHeight();
		ascent       = metrics.getAscent();

		g2d.setColor(couleurFond);
		g2d.fillRect(
			x - marge, 
			y - ascent - marge,
			largeurTexte + 2 * marge, 
			hauteurTexte + 2 * marge
		);
		g2d.dispose();

		return dessinerTexteAvecCouleurImage(resultat, imageCouleurs, texte, x, y, taillePolice);
	}

	/**
	 * Convertit un entier ARGB en objet Color.
	 */
	public static Color versColor(int argb) 
	{
		int[] comp;
		comp = UtilitaireImage.extraireComposantes(argb);
		return new Color(comp[1], comp[2], comp[3], comp[0]);
	}

	/**
	 * Convertit un objet Color en entier ARGB.
	 */
	public static int versARGB(Color couleur) 
	{
		return UtilitaireImage.combinerComposantes(
			couleur.getAlpha(), 
			couleur.getRed  (), 
			couleur.getGreen(), 
			couleur.getBlue ()
		);
	}

	/**
	 * Couleurs prédéfinies pour faciliter l'utilisation.
	 */
	public static final Color NOIR                  = Color.BLACK;
	public static final Color BLANC                 = Color.WHITE;
	public static final Color ROUGE                 = Color.RED;
	public static final Color VERT                  = Color.GREEN;
	public static final Color BLEU                  = Color.BLUE;
	public static final Color JAUNE                 = Color.YELLOW;
	public static final Color TRANSPARENT           = new Color(0, 0, 0, 0);
	public static final Color FOND_SEMI_TRANSPARENT = new Color(0, 0, 0, 128);
}
