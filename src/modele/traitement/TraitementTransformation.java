package modele.traitement;

import modele.UtilitaireImage;
import java.awt.image.BufferedImage;

/**
 * ============================================================================
 * CLASSE POUR LES TRANSFORMATIONS D'IMAGE
 * ============================================================================
 * 
 * Cette classe regroupe les transformations géométriques et colorimétriques :
 * - Rotation (90°, -90°)
 * - Ajustement de luminosité
 * - Ajustement de contraste
 * - Modification de teinte (Hue)
 * 
 * @author Équipe 6 - BUT 3 Informatique
 */
public class TraitementTransformation 
{

	/**
	 * Effectue une rotation (angle en degré)
	 * 
	 * @param image L'image à tourner
	 * @param angle Angle de rotation (angle > 0 -> horaire, angle < 0 -> anti-horaire)
	 * @return L'image tournée
	 */
	public static BufferedImage appliquerRotation(BufferedImage image, int angle)
	{
		int           largeur, hauteur, couleur, nouveauX, nouveauY;
		BufferedImage resultat;

		largeur  = image.getWidth();
		hauteur  = image.getHeight();
		resultat = UtilitaireImage.creerImageVide(hauteur, largeur);

		for (int y = 0; y < hauteur; y++) 
		{
			for (int x = 0; x < largeur; x++) 
			{
				couleur = image.getRGB(x, y);

				if (angle == 90) 
				{
					nouveauX = hauteur - 1 - y;
					nouveauY = x;
				} 
				else 
				{
					nouveauX = y;
					nouveauY = largeur - 1 - x;
				}

				resultat.setRGB(nouveauX, nouveauY, couleur);
			}
		}
		return resultat;
	}

	/**
	 * Ajuste la luminosité de l'image.
	 * 
	 * PRINCIPE :
	 * On ajoute (ou soustrait) une valeur à chaque composante RGB.
	 * - Valeur positive : éclaircit l'image
	 * - Valeur négative : assombrit l'image
	 * 
	 * FORMULE :
	 * nouvelleValeur = ancienneValeur + facteur
	 * (bornée entre 0 et 255)
	 * 
	 * @param image   L'image à modifier
	 * @param facteur Valeur à ajouter (-255 à +255)
	 * @return L'image avec la luminosité ajustée
	 */
	public static BufferedImage ajusterLuminosite(BufferedImage image, int facteur)
	{
		int           largeur, hauteur;
		BufferedImage resultat;
		int           couleur, alpha, nouveauRouge, nouveauVert, nouveauBleu, nouvelleCouleur;
		int[]         composantes;

		largeur  = image.getWidth();
		hauteur  = image.getHeight();
		resultat = UtilitaireImage.creerImageVide(largeur, hauteur);

		for (int y = 0; y < hauteur; y++) 
		{
			for (int x = 0; x < largeur; x++) 
			{
				couleur     = image.getRGB(x, y);
				composantes = UtilitaireImage.extraireComposantes(couleur);

				alpha        = composantes[0];
				nouveauRouge = TraitementTransformation.borner(composantes[1] + facteur);
				nouveauVert  = TraitementTransformation.borner(composantes[2] + facteur);
				nouveauBleu  = TraitementTransformation.borner(composantes[3] + facteur);

				nouvelleCouleur = UtilitaireImage.combinerComposantes(
					alpha, 
					nouveauRouge, 
					nouveauVert, 
					nouveauBleu
				);

				resultat.setRGB(x, y, nouvelleCouleur);
			}
		}

		return resultat;
	}

	/**
	 * Ajuste le contraste de l'image.
	 * 
	 * PRINCIPE :
	 * Le contraste mesure l'écart entre les zones claires et sombres.
	 * On utilise un facteur multiplicatif centré sur 128 (gris moyen).
	 * 
	 * FORMULE :
	 * nouvelleValeur = 128 + (ancienneValeur - 128) * facteur
	 * 
	 * - facteur > 1 : augmente le contraste
	 * - facteur < 1 : diminue le contraste
	 * - facteur = 1 : pas de changement
	 * 
	 * @param image   L'image à modifier
	 * @param facteur Facteur de contraste (0.0 à 3.0 typiquement)
	 * @return L'image avec le contraste ajusté
	 */
	public static BufferedImage ajusterContraste(BufferedImage image, double facteur) 
	{
		int           largeur, hauteur;
		BufferedImage resultat;
		int           couleur, alpha, nouveauRouge, nouveauVert, nouveauBleu, nouvelleCouleur;
		int[]         composantes;

		largeur  = image.getWidth();
		hauteur  = image.getHeight();
		resultat = UtilitaireImage.creerImageVide(largeur, hauteur);

		for (int y = 0; y < hauteur; y++)
		{
			for (int x = 0; x < largeur; x++)
			{
				couleur     = image.getRGB(x, y);
				composantes = UtilitaireImage.extraireComposantes(couleur);

				alpha        = composantes[0];
				nouveauRouge = TraitementTransformation.borner((int) (128 + (composantes[1] - 128) * facteur));
				nouveauVert  = TraitementTransformation.borner((int) (128 + (composantes[2] - 128) * facteur));
				nouveauBleu  = TraitementTransformation.borner((int) (128 + (composantes[3] - 128) * facteur));

				nouvelleCouleur = UtilitaireImage.combinerComposantes(
					alpha,
					nouveauRouge,
					nouveauVert, 
					nouveauBleu
				);

				resultat.setRGB(x, y, nouvelleCouleur);
			}
		}

		return resultat;
	}

	/**
	 * Modifie la teinte de l'image (rotation dans l'espace HSV).
	 * 
	 * PRINCIPE :
	 * La teinte (Hue) est un angle sur le cercle chromatique (0° à 360°).
	 * On convertit RGB → HSV, on décale la teinte, puis HSV → RGB.
	 * 
	 * @param image    L'image à modifier
	 * @param decalage Décalage de teinte en degrés (0 à 360)
	 * @return L'image avec la teinte modifiée
	 */
	public static BufferedImage decalerTeinte(BufferedImage image, int decalage) 
	{
		int           largeur, hauteur;
		BufferedImage resultat;
		int           couleur, alpha, rouge, vert, bleu, nouvelleCouleur;
		int[]         composantes, nouveauRgb;
		float[]       hsv;

		largeur  = image.getWidth();
		hauteur  = image.getHeight();
		resultat = UtilitaireImage.creerImageVide(largeur, hauteur);

		for (int y = 0; y < hauteur; y++) 
		{
			for (int x = 0; x < largeur; x++) 
			{
				couleur     = image.getRGB(x, y);
				composantes = UtilitaireImage.extraireComposantes(couleur);

				alpha = composantes[0];
				rouge = composantes[1];
				vert  = composantes[2];
				bleu  = composantes[3];

				hsv = TraitementTransformation.rgbVersHsv(rouge, vert, bleu);

				hsv[0] = (hsv[0] + decalage) % 360;
				if (hsv[0] < 0)
					hsv[0] += 360;

				nouveauRgb = hsvVersRgb(hsv[0], hsv[1], hsv[2]);

				nouvelleCouleur = UtilitaireImage.combinerComposantes(
					alpha, 
					nouveauRgb[0], 
					nouveauRgb[1], 
					nouveauRgb[2]
				);

				resultat.setRGB(x, y, nouvelleCouleur);
			}
		}

		return resultat;
	}

	/**
	 * Convertit l'image en niveaux de gris.
	 * 
	 * FORMULE STANDARD (luminance perçue) :
	 * gris = 0.299 * R + 0.587 * G + 0.114 * B
	 * 
	 * Ces coefficients correspondent à la sensibilité de l'œil humain.
	 * 
	 * @param image L'image à convertir
	 * @return L'image en niveaux de gris
	 */
	public static BufferedImage versNiveauxDeGris(BufferedImage image) 
	{
		int           largeur, hauteur;
		BufferedImage resultat;
		int           couleur, alpha, gris, nouvelleCouleur;
		int[]         composantes;

		largeur  = image.getWidth();
		hauteur  = image.getHeight();
		resultat = UtilitaireImage.creerImageVide(largeur, hauteur);

		for (int y = 0; y < hauteur; y++)
		{
			for (int x = 0; x < largeur; x++)
			{
				couleur = image.getRGB(x, y);
				composantes = UtilitaireImage.extraireComposantes(couleur);

				alpha = composantes[0];

				gris = (int) (
					0.299 * composantes[1] +
					0.587 * composantes[2] +
					0.114 * composantes[3]
				);

				nouvelleCouleur = UtilitaireImage.combinerComposantes(
					alpha,
					gris, 
					gris, 
					gris
				);

				resultat.setRGB(x, y, nouvelleCouleur);
			}
		}

		return resultat;
	}

	/**
	 * Inverse les couleurs de l'image (négatif).
	 * 
	 * FORMULE :
	 * nouvelleValeur = 255 - ancienneValeur
	 * 
	 * @param image L'image à inverser
	 * @return L'image en négatif
	 */
	public static BufferedImage inverserCouleurs(BufferedImage image) 
	{
		int           largeur, hauteur;
		BufferedImage resultat;
		int           couleur, alpha, nouveauRouge, nouveauVert, nouveauBleu, nouvelleCouleur;
		int[]         composantes;

		largeur  = image.getWidth();
		hauteur  = image.getHeight();
		resultat = UtilitaireImage.creerImageVide(largeur, hauteur);

		for (int y = 0; y < hauteur; y++) 
		{
			for (int x = 0; x < largeur; x++) 
			{
				couleur = image.getRGB(x, y);
				composantes = UtilitaireImage.extraireComposantes(couleur);

				alpha        = composantes[0];
				nouveauRouge = 255 - composantes[1];
				nouveauVert  = 255 - composantes[2];
				nouveauBleu  = 255 - composantes[3];

				nouvelleCouleur = UtilitaireImage.combinerComposantes(
					alpha, 
					nouveauRouge, 
					nouveauVert, 
					nouveauBleu
				);

				resultat.setRGB(x, y, nouvelleCouleur);
			}
		}

		return resultat;
	}

	/**
	 * Borne une valeur entre 0 et 255.
	 */
	private static int borner(int valeur) 
	{
		return Math.max(0, Math.min(255, valeur));
	}

	/**
	 * Convertit RGB vers HSV.
	 * 
	 * @param r Rouge (0-255)
	 * @param g Vert (0-255)
	 * @param b Bleu (0-255)
	 * @return Un tableau [H, S, V] avec H en degrés (0-360), S et V en % (0-100)
	 */
	private static float[] rgbVersHsv(int r, int g, int b)
	{
		float rNorm, gNorm, bNorm;
		float max, min, delta;
		float h, s, v;
		
		rNorm = r / 255f;
		gNorm = g / 255f;
		bNorm = b / 255f;

		max   = Math.max(rNorm, Math.max(gNorm, bNorm));
		min   = Math.min(rNorm, Math.min(gNorm, bNorm));
		delta = max - min;

		h = 0;

		if (delta == 0) 
			h = 0;
		else if (max == rNorm)
			h = 60 * (((gNorm - bNorm) / delta) % 6);
		else if (max == gNorm)
			h = 60 * (((bNorm - rNorm) / delta) + 2);
		else
			h = 60 * (((rNorm - gNorm) / delta) + 4);

		if (h < 0)
			h += 360;

		s = (max == 0) ? 0 : (delta / max) * 100;

		v = max * 100;

		return new float[] { h, s, v };
	}

	/**
	 * Convertit HSV vers RGB.
	 * 
	 * @param h Teinte en degrés (0-360)
	 * @param s Saturation en % (0-100)
	 * @param v Valeur en % (0-100)
	 * @return Un tableau [R, G, B] avec des valeurs 0-255
	 */
	private static int[] hsvVersRgb(float h, float s, float v)
	{
		float c, x, m, rPrime, gPrime, bPrime;
		int   r, g, b;

		s = s / 100f;
		v = v / 100f;

		c = v * s;
		x = c * (1 - Math.abs((h / 60) % 2 - 1));
		m = v - c;

		rPrime = 0; 
		gPrime = 0; 
		bPrime = 0;

		if (h < 60) 
		{
			rPrime = c;
			gPrime = x;
			bPrime = 0;
		} else if (h < 120) 
		{
			rPrime = x;
			gPrime = c;
			bPrime = 0;
		} else if (h < 180) 
		{
			rPrime = 0;
			gPrime = c;
			bPrime = x;
		} else if (h < 240) 
		{
			rPrime = 0;
			gPrime = x;
			bPrime = c;
		} else if (h < 300) 
		{
			rPrime = x;
			gPrime = 0;
			bPrime = c;
		} else 
		{
			rPrime = c;
			gPrime = 0;
			bPrime = x;
		}

		r = Math.round((rPrime + m) * 255);
		g = Math.round((gPrime + m) * 255);
		b = Math.round((bPrime + m) * 255);

		return new int[] 
		{ 
			TraitementTransformation.borner(r), 
			TraitementTransformation.borner(g), 
			TraitementTransformation.borner(b) 
		};
	}
}
