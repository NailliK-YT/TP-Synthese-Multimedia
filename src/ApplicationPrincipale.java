import controleur.ControleurPrincipal;
import modele.ModeleImage;
import vue.FramePrincipal;

import javax.swing.SwingUtilities;

/**
 * ============================================================================
 * APPLICATION PRINCIPALE - POINT D'ENTRÉE
 * ============================================================================
 * 
 * Point d'entrée de l'application de traitement d'images.
 * 
 * ARCHITECTURE MVC :
 * - Modèle (modele.ModeleImage) : Données de l'application
 * - Vue (vue.FramePrincipal) : Interface graphique
 * - Contrôleur (controleur.ControleurPrincipal) : Logique de coordination
 * 
 * @author Équipe 6 - BUT 3 Informatique
 */
public class ApplicationPrincipale {

    /**
     * Point d'entrée du programme.
     * 
     * @param args Arguments de la ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        // Utilisation de SwingUtilities.invokeLater pour assurer
        // que l'interface graphique est créée dans le thread EDT
        SwingUtilities.invokeLater(() -> {
            System.out.println("==========================================");
            System.out.println("  ÉDITEUR D'IMAGES - BUT 3 INFORMATIQUE");
            System.out.println("  Architecture Modèle-Vue-Contrôleur");
            System.out.println("  Équipe 6 - Programmation Multimédia");
            System.out.println("==========================================");
            System.out.println();

            // Création du modèle (données)
            ModeleImage modele = new ModeleImage();
            System.out.println("Modèle créé");

            // Création de la vue (interface graphique)
            FramePrincipal vue = new FramePrincipal();
            System.out.println("Vue créée");

            // Création du contrôleur (logique)
            ControleurPrincipal controleur = new ControleurPrincipal(modele, vue);
            System.out.println("Contrôleur créé");

            // Initialisation (liaison modèle-vue-contrôleur)
            controleur.initialiser();
            System.out.println("Application initialisée");
        });
    }
}
