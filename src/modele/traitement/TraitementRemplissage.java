package modele.traitement;

import modele.UtilitaireImage;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

/**
 * ============================================================================
 * CLASSE POUR LE POT DE PEINTURE (FLOOD FILL)
 * ============================================================================
 * 
 * Cette classe implémente l'algorithme de remplissage par diffusion.
 * 
 * PRINCIPE :
 * À partir d'un pixel de départ, on colorie tous les pixels "connectés"
 * qui ont une couleur similaire (selon une tolérance).
 * 
 * CHOIX D'IMPLÉMENTATION :
 * On utilise une FILE (Queue) plutôt que la récursion pour éviter
 * les débordements de pile (StackOverflowError) sur les grandes zones.
 * 
 * @author Équipe 6 - BUT 3 Informatique
 */
public class TraitementRemplissage
{

	/**
	 * Remplit une zone avec une nouvelle couleur (pot de peinture).
	 * 
	 * ALGORITHME (Breadth-First Search) :
	 * 1. On part du pixel de départ
	 * 2. On l'ajoute à une file d'attente
	 * 3. Tant que la file n'est pas vide :
	 * a. On retire un pixel de la file
	 * b. Si sa couleur est proche de la couleur d'origine (selon la tolérance)
	 * c. On le colorie avec la nouvelle couleur
	 * d. On ajoute ses 4 voisins à la file
	 * 
	 * POURQUOI UNE FILE ?
	 * La récursion utilise la pile d'appels, qui est limitée.
	 * Sur une grande zone (ex: 1000x1000 pixels), on risque un StackOverflowError.
	 * La file utilise la mémoire heap, beaucoup plus grande.
	 * 
	 * @param image           L'image à modifier (sera modifiée directement)
	 * @param startX          Position X de départ
	 * @param startY          Position Y de départ
	 * @param nouvelleCouleur La couleur de remplissage (ARGB)
	 * @param tolerance       Distance maximale pour considérer deux couleurs comme
	 *                        similaires
	 * @return L'image modifiée
	 */
	public static BufferedImage remplir(
		BufferedImage image, int startX, int startY,int nouvelleCouleur, double tolerance
	) 
	{
		BufferedImage resultat;
		int           largeur, hauteur, couleurOrigine, pixelsRemplis, x, y, couleurActuelle;
		boolean[][]   visite;
		Queue<int[]>  file;
		int[]         pixel;
		


		resultat = UtilitaireImage.copierImage(image);
		largeur  = resultat.getWidth();
		hauteur  = resultat.getHeight();

		if (startX < 0 || startX >= largeur || startY < 0 || startY >= hauteur) 
		{
			System.err.println("ERREUR : Coordonnées de départ hors de l'image");
			return resultat;
		}

		couleurOrigine = resultat.getRGB(startX, startY);

		if (UtilitaireImage.distanceCouleur(couleurOrigine, nouvelleCouleur) == 0) 
		{
			System.out.println("La zone est déjà de la couleur demandée");
			return resultat;
		}

		visite = new boolean[largeur][hauteur];
		
		file   = new LinkedList<>();
		file.add(new int[] { startX, startY });
		
		visite[startX][startY] = true;

		pixelsRemplis = 0;

		while (!file.isEmpty())
		{
			pixel = file.poll();

			x = pixel[0];
			y = pixel[1];

			couleurActuelle = resultat.getRGB(x, y);

			double distance = UtilitaireImage.distanceCouleur(couleurActuelle, couleurOrigine);

			if (distance <= tolerance)
			{
				resultat.setRGB(x, y, nouvelleCouleur);
				pixelsRemplis++;

				TraitementRemplissage.ajouterVoisin(file, visite, largeur, hauteur, x - 1, y);
				TraitementRemplissage.ajouterVoisin(file, visite, largeur, hauteur, x + 1, y);
				TraitementRemplissage.ajouterVoisin(file, visite, largeur, hauteur, x, y - 1);
				TraitementRemplissage.ajouterVoisin(file, visite, largeur, hauteur, x, y + 1);
			}
		}

		System.out.println("Flood Fill terminé : " + pixelsRemplis + " pixels remplis");
		return resultat;
	}

	/**
	 * Ajoute un voisin à la file s'il est valide et non visité.
	 */
	private static void ajouterVoisin(
		Queue<int[]> file, 
		boolean[][] visite,
		int largeur, int hauteur, 
		int x, int y
	) 
	{
		if (x >= 0 && x < largeur && y >= 0 && y < hauteur) 
		{
			if (!visite[x][y]) 
			{
				visite[x][y] = true;
				file.add(new int[] { x, y });
			}
		}
	}

	/**
	 * Version du pot de peinture qui remplit aussi en diagonale.
	 * 
	 * DIFFÉRENCE :
	 * Au lieu de 4 voisins, on considère 8 voisins (les 4 diagonales en plus).
	 * Le remplissage est plus "complet" mais peut dépasser les bordures fines.
	 * 
	 * @param image           L'image à modifier
	 * @param startX          Position X de départ
	 * @param startY          Position Y de départ
	 * @param nouvelleCouleur La couleur de remplissage
	 * @param tolerance       Distance maximale de couleur
	 * @return L'image modifiée
	 */
	public static BufferedImage remplir8Directions(
		BufferedImage image, 
		int startX, int startY,
		int nouvelleCouleur, 
		double tolerance
	) 
	{
		BufferedImage resultat;
		int           largeur, hauteur, couleurOrigine, x, y, couleurActuelle, nx, ny;
		boolean[][]   visite;
		Queue<int[]>  file;
		int[]         pixel;
		double        distance;

		resultat = UtilitaireImage.copierImage(image);

		largeur = resultat.getWidth();
		hauteur = resultat.getHeight();

		if (startX < 0 || startX >= largeur || startY < 0 || startY >= hauteur) 
		{
			return resultat;
		}

		couleurOrigine = resultat.getRGB(startX, startY);

		if (UtilitaireImage.distanceCouleur(couleurOrigine, nouvelleCouleur) == 0) 
		{
			return resultat;
		}

		visite = new boolean[largeur][hauteur];

		file = new LinkedList<>();
		file.add(new int[] { startX, startY });

		visite[startX][startY] = true;

		int[] dx = { -1, 0, 1, -1, 1, -1, 0, 1 };
		int[] dy = { -1, -1, -1, 0, 0, 1, 1, 1 };

		while (!file.isEmpty()) 
		{
			pixel = file.poll();

			x = pixel[0];
			y = pixel[1];

			couleurActuelle = resultat.getRGB(x, y);
			distance = UtilitaireImage.distanceCouleur(couleurActuelle, couleurOrigine);

			if (distance <= tolerance) 
			{
				resultat.setRGB(x, y, nouvelleCouleur);

				for (int i = 0; i < 8; i++) 
				{
					nx = x + dx[i];
					ny = y + dy[i];
					ajouterVoisin(file, visite, largeur, hauteur, nx, ny);
				}
			}
		}

		return resultat;
	}

	/**
	 * Version récursive du pot de peinture.
	 * 
	 * ATTENTION : Cette version peut provoquer un StackOverflowError
	 * sur les grandes zones. À utiliser uniquement pour des petites images.
	 * 
	 * PRINCIPE DE LA RÉCURSION :
	 * La fonction s'appelle elle-même pour chaque voisin valide.
	 * 
	 * @param image           L'image à modifier
	 * @param x               Position X actuelle
	 * @param y               Position Y actuelle
	 * @param nouvelleCouleur La couleur de remplissage
	 * @param couleurOrigine  La couleur à remplacer
	 * @param tolerance       Distance maximale
	 */
	public static void remplirRecursif(
		BufferedImage image, 
		int x, int y,
		int nouvelleCouleur, int couleurOrigine,
		double tolerance
	) 
	{
		if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) 
			return;

		int couleurActuelle;
		
		couleurActuelle = image.getRGB(x, y);

		if (couleurActuelle == nouvelleCouleur)
			return;

		if (UtilitaireImage.distanceCouleur(couleurActuelle, couleurOrigine) > tolerance)
			return;

		image.setRGB(x, y, nouvelleCouleur);

		TraitementRemplissage.remplirRecursif(image, x - 1, y, nouvelleCouleur, couleurOrigine, tolerance);
		TraitementRemplissage.remplirRecursif(image, x + 1, y, nouvelleCouleur, couleurOrigine, tolerance);
		TraitementRemplissage.remplirRecursif(image, x, y - 1, nouvelleCouleur, couleurOrigine, tolerance);
		TraitementRemplissage.remplirRecursif(image, x, y + 1, nouvelleCouleur, couleurOrigine, tolerance);
	}

	/**
	 * Crée une couleur ARGB à partir de composantes RGB.
	 * 
	 * @param rouge Composante rouge (0-255)
	 * @param vert  Composante verte (0-255)
	 * @param bleu  Composante bleue (0-255)
	 * @return La couleur au format ARGB (opaque)
	 */
	public static int creerCouleur(int rouge, int vert, int bleu) 
	{
		return UtilitaireImage.combinerComposantes(255, rouge, vert, bleu);
	}

	/**
	 * Calcule une tolérance recommandée pour le flood fill.
	 * 
	 * GUIDE :
	 * - 0 : Exactement la même couleur uniquement
	 * - 10-30 : Couleurs très proches (dégradés légers)
	 * - 30-60 : Couleurs similaires (variations de lumière)
	 * - 60-100 : Couleurs différentes mais dans le même ton
	 * - >100 : Très permissif
	 * 
	 * La distance maximale entre deux couleurs est √(255² + 255² + 255²) ≈ 441
	 */
	public static final double TOLERANCE_EXACTE  = 0;
	public static final double TOLERANCE_FAIBLE  = 20;
	public static final double TOLERANCE_MOYENNE = 50;
	public static final double TOLERANCE_FORTE   = 100;
}
