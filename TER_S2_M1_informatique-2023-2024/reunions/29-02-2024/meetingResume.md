## Réunion du 29/02/2024 à 15h30

### Tâches réalisées:

* Équipe 1:
  * Création d'un automate à partir d'un fichier JSON et transfert vers le robot EV3 via le serveur MQTT. 
  * Cet automate détermine les états que doit parcourir le robot pour spécifier les actions à faire.
  
* Équipe 2:
  * Fusion du code source de la communication MQTT et Bluetooth. 
  * Chaque communication est désormais exécutée dans un thread séparé.
  * Le Bluetooth peut être bloqué par des messages MQTT. 
  * Amélioration du pilotage par flèches.
    
### Tâches à venir:

* Équipe 1:
  * Vérification de la validité des états d'un automate construit en JSON (syntaxe incluse). 

* Équipe 2:
  * Amélioration des performances des contrôles de l'application. 
  * Ajout de contrôles plus simples. 
  * Priorisation des communications MQTT over Bluetooth. 

* Préparation de la réunion de l'équipe VELO:
  * Présentation du sujet du TER. 
  * Compte-rendu des progrès atteints.