import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;

import Exception.MessageException;

public class Fenetre extends JFrame {

	private JButton bouton = new JButton("Lancer la course");
	private JPanel conteneur = new JPanel();
	private boolean isLaunched = false;
	
	public Fenetre() {
		// Modifier le titre de la fenêtre
		setTitle("Race Controller");
		// Modifier la taille
		setSize(400,300);
		// taille non modifiable par l'utilisateur
		setResizable(true);
		// Un click sur croix entraine la fermeture de la fenetre
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Centrer la fenêtre par rapport à l'écran de l'ordinateur
		setLocationRelativeTo(null);
		// ajout listener sur le bouton
		bouton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					RaceControllerSwing.startRace();
				} catch (IOException | MessageException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		// ajout bouton sur le conteneur
		conteneur.add(bouton);
		setContentPane(conteneur);
	}

	public JButton getBouton() {
		return bouton;
	}

	public void setBouton(JButton bouton) {
		this.bouton = bouton;
	}

	public JPanel getConteneur() {
		return conteneur;
	}

	public void setConteneur(JPanel conteneur) {
		this.conteneur = conteneur;
	}

	public boolean isLaunched() {
		return isLaunched;
	}

	public void setLaunched(boolean isLaunched) {
		this.isLaunched = isLaunched;
	}
	
	
}
