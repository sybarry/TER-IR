import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import Exception.MessageException;

public class Fenetre extends JFrame {

	private JButton bouton;
	private JScrollPane jsp;
	private JTextArea jta;
	private JPanel conteneur;
	private boolean firstPrint;
	
	public Fenetre() {
		firstPrint=true;
		
		// Modifier le titre de la fenêtre
		setTitle("Race Controller");
		// Modifier la taille
		setSize(500,400);
		// taille non modifiable par l'utilisateur
		setResizable(true);
		// Un click sur croix entraine la fermeture de la fenetre
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Centrer la fenêtre par rapport à l'écran de l'ordinateur
		setLocationRelativeTo(null);
		//initialisation du bouton
		bouton = new JButton("Lancer la course");
		
		// ajout listener sur le bouton
		bouton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					try {
						RaceControllerSwing.Test();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (MessageException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}		
			}
		});
		//initialisation de la zone d'affichage du texte
		jta = new JTextArea();
		jta.setEditable(false);
		//initialisation de la console scrollable avec la zone de texte précédemment initialisée
		jsp = new JScrollPane(jta) {
		      @Override
		      public Dimension getPreferredSize() {
		        return new Dimension(400,300);
		      }
		};
		//initialisation du panel conteneur
		conteneur = new JPanel();
		// ajout bouton sur le conteneur
		conteneur.add(bouton);
		//ajout de la console scrollable sur le conteneur
		conteneur.add(jsp);
		//affichage du panel conteneur sur la frame
		setContentPane(conteneur);
	}
	
	public void write(String text) {
		if(firstPrint) {
			jta.append(text);
			firstPrint=false;
		}else {
			jta.append("\n"+text);
		}
	}
	
	public void DisplayWait() {
		JOptionPane.showMessageDialog(this, "En attente de joueurs : "+ RaceControllerSwing.currentNbPlayer+"/"+RaceControllerSwing.nbPlayerMax);
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
	
}
