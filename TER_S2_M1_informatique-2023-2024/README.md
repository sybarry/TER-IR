# TER-2023-2024

Ceci est le code source des différentes expérimentations réalisées dans le cadre du module Projet de Recherche en M1 ALMA pour l'année 2023/2024.
Le code source est divisé en 2 parties:
- Le code source de [l'application Android](ApplicationLejosEv3)
- Le code source du [robot EV3](code-robots) contenant les différents packages pour les différentes fonctionnalités.

Les fonctionnalites suivantes ont été implementées:
- Controle du robot depuis un serveur MQTT
- Controle autonome du robot sur une surface de couleur spécifique
- Controle du robot à partir d'un automate (etats et transitions)
- Controle du robot depuis une application Android et un serveur MQTT en même temps

Pour cloner le projet, assurez-vous d'abord:
- d'installer le plugin [LeJOS sur Eclipse](https://sourceforge.net/p/lejos/wiki/Installing%20the%20Eclipse%20plugin/) et de le configurer correctement avec votre robot.
- d'utiliser et compiler le projet du robot avec Java 1.7
- Pour cloner un dossier specifique en travaillant sur le même dépot:
```bash
git clone --depth 1 --branch master --no-checkout REPO_REMOTE_URL
cd ROOT_FOLDER/
git sparse-checkout set SUBDIRECTORY_RELATIVE_PATH 
# example pour le projet android: 
# git sparse-checkout set TER_S2_M1_informatique-2023-2024/ApplicationLejosEv3

git checkout
```

Les bibliothèques necessaires pour sont disponibles dans le dossier `lib` du projet, et configurées dans le fichier `.classpath`, donc assurez-vous de le pas l'ignorer.

*NOTE:* L'addresse IP du serveur MQTT configurée dans differents fichiers est à changer pour correspondre à votre propre serveur mosquitto.
Pour plus de details, consultez la [documentation](rapports)

#### Membres:
- Mamadou cire CAMARA
- Mohamed abdallah CHERIF
- Saikou yaya BARRY
- Yassine DERGAOUI
