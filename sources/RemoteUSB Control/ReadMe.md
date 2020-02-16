## Read Me

Brancher le boitier au PC via USB. Vérifier la connexion en pingant le boitier :
- Windows + R
- cmd
- ping 10.0.1.1

Si l'installation est correcte (voir Tutos), vous devriez obtenir une réponse à 0% de perte. 

Upload le fichier TestUSBControl sur l'EV3. Il s'agit du serveur. Lorsque le programme
se lance, il attend la connection d'un client. Le port d'écoute est 5555 dans le fichier.
L'écran du boitier doit indiquer "waiting".

Run ClientUSB sur le PC (dans un projet séparé). Il se connecte au server (IP 10.0.1.1).
Le boitier doit indiquer "Connected client". Cela veut dire que la connecxion est établie entre
l'application PC et le sever boitier.

Entrer 1 pour ouvrir la porte;
Entrer 2 pour fermer la porte;
Entrer leave pour quitter la connexion des deux côtés