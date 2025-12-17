package vue;

import javax.swing.*;
import java.awt.*;

/**
 * ============================================================================
 * PANNEAU DE STATUT
 * ============================================================================
 * 
 * Composant responsable de l'affichage des messages d'état
 * en bas de la fenêtre principale.
 * 
 * RESPONSABILITÉS :
 * - Afficher les messages d'état
 * - Fournir un retour visuel à l'utilisateur
 * 
 * @author Équipe 6 - BUT 3 Informatique
 */
public class PanelStatut extends JPanel 
{
	private JLabel labelStatut;

	public PanelStatut() 
	{
		this.setLayout(new BorderLayout());

		this.labelStatut = new JLabel("En attente de chargement d'image...");
		this.labelStatut.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		this.add(this.labelStatut, BorderLayout.CENTER);

	}

	/**
	 * Met à jour le message affiché dans la barre de statut.
	 * 
	 * @param message Le message à afficher
	 */
	public void mettreAJourStatut(String message) 
	{
		this.labelStatut.setText(message);
	}
}
