## Rapport de la Réunion du 25/01 à 8h30

### Objectif de la Réunion

* Expliquer le protocole utilisé pour faire communiquer les robots, en mettant l'accent sur le protocole MQTT.
* Réaliser une démonstration pratique du pilotage en groupe et individuel des robots EV3.


### Sujets abordés:

#### Protocole MQTT

* Fonctionnement general du protocole MQTT
* Avantages du protocole MQTT:
    * Leger
    * Simple à utiliser
    * Securisable
* Cooment l'utilisation du protocole MQTT est faite dans le projet

#### Pilotage en Groupe

* Démonstration pratique du pilotage en groupe des robots
* Coordination des robots pour effectuer des tâches synchronisées
* Mise en œuvre du protocole MQTT pour le pilotage en groupe

#### Pilotage Individuel

* Présentation du pilotage individuel d'un robot EV3
* Réception d'instructions via MQTT selon une l'addresse MAC du robot


### A faire:

#### Documentation

* Documenter en détail l'utilisation du protocole MQTT dans le projet
* Inclure des instructions pour le pilotage en groupe et individuel des robots

#### Securité

* Si l'addresse du serveur MQTT est connue, l'acces aux robots devient facile et non-protégé, un authentification est donc nécessaire pour se connecter au serveur.

#### Android
* Utilisation des anciens projets android et des ressources proposés pour controller les robots par bluetooth depuis un appareil Android.
