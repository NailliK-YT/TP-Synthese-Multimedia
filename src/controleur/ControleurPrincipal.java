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
public class ControleurPrincipal implements ModeleImage.EcouteurModele {

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
    public ControleurPrincipal(ModeleImage modele, FramePrincipal vue) {
        this.modele = modele;
        this.vue = vue;

        this.controleurFichier = new ControleurFichier(modele, vue);
        this.controleurImage = new ControleurImage(modele, vue);
    }

    /**
     * Initialise le contrôleur et lie le modèle à la vue.
     * 
     * PATTERN OBSERVER :
     * Le modèle notifie ce contrôleur quand il change,
     * et le contrôleur met à jour la vue en conséquence.
     */
    public void initialiser() {
        modele.ajouterEcouteur(this);

        vue.definirControleurFichier(controleurFichier);
        vue.definirControleurImage(controleurImage);

        modeleModifie();

        System.out.println("Contrôleur principal initialisé - Prêt !");
    }

    /**
     * Appelé quand le modèle a changé.
     * On met à jour la vue en conséquence.
     */
    @Override
    public void modeleModifie() {
        vue.afficherImage(modele.obtenirImagePrincipale());

        if (modele.possedeImagePrincipale()) {
            int largeur = modele.obtenirImagePrincipale().getWidth();
            int hauteur = modele.obtenirImagePrincipale().getHeight();
            vue.mettreAJourStatut("Image : " + largeur + " x " + hauteur + " pixels");
        } else {
            vue.mettreAJourStatut("Aucune image chargée. Utilisez Fichier > Ouvrir");
        }
    }

    /**
     * @return Le modèle de l'application
     */
    public ModeleImage obtenirModele() {
        return modele;
    }

    /**
     * @return La vue principale
     */
    public FramePrincipal obtenirVue() {
        return vue;
    }

    /**
     * @return Le contrôleur fichier
     */
    public ControleurFichier obtenirControleurFichier() {
        return controleurFichier;
    }

    /**
     * @return Le contrôleur image
     */
    public ControleurImage obtenirControleurImage() {
        return controleurImage;
    }
}
