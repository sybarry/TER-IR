## Connexion du boitier au PC via USB

Ce tuto suppose que vous avez correctement installé le plugin Eclipse, que vous avez créer un Projet Eclipse LEJOS
et que votre boitier boot sur le LEJOS firmware. Si ça n'est pas le cas, voir tuto "Installation LEJOS firmware + plugin EClipse". 

Munissez vous de votre boitier Ev3 et du cable USB fourni dans la boite LEGO. BRANCHEZ VOTRE EV3 à votre PC.

Ce tuto suppose également que vous opérez sous Windows 10, si ça n'est pas le cas, dommage !

Tout d'abord, il faut savoir que lorsque vous connectez votre boitier Ev3 à votre PC, Windows 10 semble le reconnaitre et installer des drivers, mais c'est faux...
Complétement faux! Il va vous falloir installer un driver particulier par vous-même : le RNDIS !!. Pour ce faire, lisez d'abord les lignes "A lire" qui suivent puis
allez voir dans le dossier DRIVER de ce git. 

**A lire :** Créez un nouveau dossier "InsertRelevantNameForU" à la racine de votre disque dur. Copiez-y les fichier se trouvant dans le dossier DRIVER du Git. Cliquez droit
sur le fichier d'extension .inf et selectionnez "Installer". Soyez consentant aux éventuelles popup.
Maintenant, tapez trois fois dans vos mains et dîtes à haute voix "abracadabra" ! C'est fait? 
Okay, alors suivez maintenant ceci : 

- Windows + R
- devmgmt.msc
- Etendez "Carte réseau"
    - vous devriez voir quelque chose du genre "USB Ethernet/RNDIS Gadget"
    - Clique droit dessus
    - Propriétés => Avancé 
    - Sélectionnez "Valeur" puis entrez dans le champ "10.0.1.1" et validez moi tout ça
    
Bravo, c'est terminé !
Maintenant il est temps de tester la connexion : 

- Windows + R
- cmd
- ping 10.0.1.1

Vous devriez avoir une réponse avec 0% de pertes.

Si c'est le cas, vous avez correctement connecté votre boitier à votre PC via USB. Reste maintenant à upload un programme sur ce boitier Ev3. 

J'ai suppososé au début de cet article que vous aviez déjà un projet Eclipse correctement Setup en mode Lejos project. Si ça n'est pas le cas, voir le tutoriel 
"Installation LEJOS firmware + plugin EClipse". C'est bon? Créez maintenant une classe en cochant la case "public static void main" . Écrivez quelque chose de simple 
comme *LCD.drawsString("BONJOUR EV3",0,0)* (Importez ce qu'ecplise vous indique, soit *import lejos.hardware.lcd.LCD;* dans ce cas). 
Maintenant nous sommes fin prêt pour importer ce beau programme sur le boitier. Pour ce faire, cliquez droit sur votre classe => Run As ==> lejos EV3 Program.

Votre boitier devrait vous implorer d'attendre 1 seconde. Puis vous devriez voir apparaitre "BONJOUR EV3" sur l'écran du boitier.




