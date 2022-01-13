import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import Exception.MessageException;
import java.awt.Component;
import java.awt.Toolkit;

public class Fenetre extends JFrame {
	private JPanel conteneur;
	private boolean firstPrint;
	private JButton boutonConnection;
	private ActionListener al;
	private JButton boutonCourse;
	private JTextField connectedPlayers;
	private JTextArea jta;
	private JScrollPane jsp;
	private JComboBox nbPlayers;
	private JLabel LconnectedPlayers;
	private JLabel LnbPlayers;
	
	public Fenetre() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\natha\\Documents\\Fac\\Capstone\\transfo-protocoles\\Capstone2021-2022\\code-Java\\RaceController\\resources\\universit\u00E9-de-nantes-squarelogo-1457003476200.png"));
		setResizable(false);
		firstPrint=true;
		
		// Modifier le titre de la fenêtre
		setTitle("Race Controller");
		// Modifier la taille
		setSize(600,400);
		// Un click sur croix entraine la fermeture de la fenetre
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Centrer la fenêtre par rapport à l'écran de l'ordinateur
		setLocationRelativeTo(null);
		//initialisation du panel conteneur
		conteneur = new JPanel();
		conteneur.setLayout(null);
		
		//Ajout d'un listener pour les boutons
		al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object source = e.getSource();
				if(source==boutonConnection) {
					if(nbPlayers.getSelectedItem().equals("Choose")) {
						JOptionPane.showMessageDialog(conteneur, "Please choose a number of players");
					}else {
						int value=0;
						switch((String)nbPlayers.getSelectedItem()) {
						case "Choose":
							value=0;
							break;
						case "1 Player":
							value=1;
							break;
						case "2 Players":
							value=2;
							break;
						case "3 Players":
							value=3;
							break;
						case "4 Players":
							value=4;
							break;
						default:
							break;
						}
						RaceControllerSwing.nbPlayerMax= value;
						nbPlayers.setEnabled(false);
						boutonConnection.setEnabled(false);
						RaceControllerSwing.isLaunched=true;
					}
				}else if(source==boutonCourse) {
					boutonCourse.setEnabled(false);
					new Thread(){
						public void run() {
							try {
								RaceControllerSwing.startRace();
							} catch (IOException | MessageException | InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}.start();
				}
			}
		};
		
		//Initialisation de boutonConnection
		boutonConnection = new JButton("Launch the connection");
		boutonConnection.setToolTipText("Launch the MQTT connection and block le number of players chosen for the race");
		boutonConnection.setBounds(10, 11, 180, 23);
		boutonConnection.addActionListener(al);
		conteneur.add(boutonConnection);
		
		boutonCourse = new JButton("Launch the race");
		boutonCourse.setToolTipText("Launch the race when every vehicle is ready");
		boutonCourse.setEnabled(false);
		boutonCourse.setBounds(206, 54, 150, 23);
		boutonCourse.addActionListener(al);
		conteneur.add(boutonCourse);
		
		
		//Initialisation de jsp
		jsp = new JScrollPane();
		jsp.setBounds(10, 88, 564, 262);
		conteneur.add(jsp);
		
		//Initialisation de jta
		jta=new JTextArea();
		jta.setToolTipText("Text area listing all the events of the race");
		jta.setEditable(false);
		jsp.setViewportView(jta);
		
		//Initialisation de nbPlayers
		nbPlayers = new JComboBox();
		nbPlayers.setModel(new DefaultComboBoxModel(new String[] {"Choose", "1 Player", "2 Players", "3 Players", "4 Players"}));
		nbPlayers.setBounds(476, 11, 98, 22);
		//ajout de nbPlayers au conteneur
		conteneur.add(nbPlayers);
		
		//Initialisation de ConnectedPlayers
		connectedPlayers = new JTextField();
		connectedPlayers.setToolTipText("Number of players currently connected on the max number of players");
		connectedPlayers.setEditable(false);
		connectedPlayers.setBounds(321, 12, 23, 20);
		conteneur.add(connectedPlayers);
		connectedPlayers.setColumns(10);
		
		//Initialisation de LconnectedPlayers
		LconnectedPlayers = new JLabel("Connected Players :");
		LconnectedPlayers.setHorizontalAlignment(SwingConstants.CENTER);
		LconnectedPlayers.setBounds(200, 11, 118, 23);
		conteneur.add(LconnectedPlayers);
		
		//Initialisation de LnbPlayers
		LnbPlayers = new JLabel("Number of players :");
		LnbPlayers.setHorizontalAlignment(SwingConstants.CENTER);
		LnbPlayers.setBounds(353, 15, 118, 14);
		conteneur.add(LnbPlayers);
		
		//affichage du panel conteneur sur la frame
		setContentPane(conteneur);
	}
	
	public void enableRaceLaunch() {
		
		this.boutonCourse.setEnabled(true);
	}
	
	public void write(String text) {
		if(firstPrint) {
			jta.append(text);
			firstPrint=false;
		}else {
			jta.append("\n"+text);
		}
	}
	
	public void displayConnectedPlayers() {
		connectedPlayers.setText(RaceControllerSwing.currentNbPlayer+"/"+RaceControllerSwing.nbPlayerMax);
	}
	
	public JButton getBoutonConnection() {
		return boutonConnection;
	}

	public void setBoutonConnection(JButton bouton) {
		this.boutonConnection = bouton;
	}

	public JPanel getConteneur() {
		return conteneur;
	}

	public void setConteneur(JPanel conteneur) {
		this.conteneur = conteneur;
	}
}
