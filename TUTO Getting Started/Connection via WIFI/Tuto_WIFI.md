# Connexion du boitier au PC via WIFI

Ce tuto aussi suppose que vous avez correctement installé le plugin Lejos EV3 sur Eclipse, et
que vous avez créé un Projet LEJOS et que votre boitier boot sur le LEJOS firmware. Si ça n'est pas le
cas, voir tuto "Installation LEJOS firmware + plugin EClipse".

Dans un premier temps, munissez-vous uniquement de votre boitier Ev3 et de votre PC et veuillez
suivre les étapes suivantes:

ETAPE 1) Brancher l'adaptateur Wifi dans le port USB Host de la brique EV3.

ETAPE 2) Allumer la brique EV3.

ETAPE 3) - dérouler le menu "outils"

- sélectionner "Wifi" ;
- cocher la case "Wifi" ;
- sélectionner "Connections" ;
- sélectionner le point d'accès Wifi dans la liste, puis attendre ;
- saisir la clé WEP/WPA si nécessaire ;
- sélectionner "Connect" ;
- dérouler le menu "outils" ;
- sélectionner "Brick info" ;
- conserver l'adresse IP de la brique EV3 (par exemple 192.168.1.2) pour un usage
ultérieur.

ETAPE 4) Il faut établir un partage de connexion 4G entre notre Android, PC et le contrôleur afin que
ces trois appartiennent au même réseau. Une nouvelle adresse IP s'affichera à l'écran du contrôleur
de la brick Lejos.

ETAPE 5) Définir le contrôleur Lejos comme serveur plutôt que client en déroulant et cliquant sur
l'icône Wifi ensuite sur Access point pt+ et entrez l'adresse IP de votre brick et validez.

ETAPE 6) Dans la ligne de commande de votre PC lancez l'instruction: telnet suivie de la nouvelle adresse IP affichée sur la brick et rentrez comme
login 'root' et sans mot de passe

ETAPE 6bis) Si la commande Telnet n'est pas reconnu:


C:\>telnet google.com 80

'Telnet' is not recognized as an internal or external command, operable program or batch file.

**_Cause:_** Le client Telnet dans un Système d'exploitation Windows est désactivé par défaut.

**_Solution-ligne-de-commande_** : Lancez la commande suivante en disposant d'autorisations de
niveau Administrateur:

- >dism /online /Enable-Feature /FeatureName:TelnetClient

**_Solution-configuration-manuelle:_** commencez par rechercher et cliquer sur 'Turn Windows
features on or off' à partir du domaine de recherche près du Start button. Ensuite, sur la page qui
s'affiche recherchez et sélectionnez Telnet Client et pour finir cliquez sur Ok.

Vous aurez par la suite la page de chargement des changements en titre : ‘Apply Changes’.

Une fois que les changements sont réalisés, vous pouvez enfin cliquer sur 'Close'.

ETAPE 7)root@EV3:~# dropbear

root@EV3:~# hcitool dev

Devices:

hci0 11:22:33:44:55:

ETAPE 8) Lancer la Classe 'TestUSBControl', que vous pouvez retrouver à l'adresse '
https://gitlab.univ-nantes.fr/ter-ir-2020/transfo-protocoles/---
/master/sources/RemoteUSB%20Control/TestUSBcontrol.java ' en Run As Lejos Application.

ETAPE 9) Attendre l'affichage de 'Waiting' sur l'écran du contrôleur qui marque l'attente d'une
connexion.

ETAPE 10 ) Lancer la Classe 'Client WIFI' que vous pouvez retrouver à l'adresse 'https://gitlab.univ-
nantes.fr/ter-ir-2020/transfo-protocoles/---
/master/sources/RemoteUSB%20Control/TestUSBcontrol.java' en Run As Java Application.

ETAPE 11) Pour Ouvrir le portail, entrez 1 sur votre Consol et pour le fermer entrez 2 et afin
terminer la connexion entrez Leave.


