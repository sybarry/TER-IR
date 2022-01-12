Cette télécommande qui utilise la surcouche de communication ne fonctionne pas, à cause de 2 problèmes :

 - Pour l'envoi d'un message au véhicule, les 2 premiers byte du message de la surcouche sont supprimés.
Cela doit être dû au fait que nous cherchons à envoyer un message entre deux appareils différents.
 - Pour la réception d'un message, au lieu que le boitier ev3 envoie le message de la surcouche (_idMessage_%%_typeMessage_%%_ACK_%%_topic_@@_sender_--_recever_&&_message_), il envoie la taille du tableau de byte associé au message.

Ces deux problèmes ne viennent pas de la surcouche, car elle fonctionne très bien lorsqu'on souhaite communiquer avec le serveur MQTT, et cela ne vient pas non plus de la télécommande puisque sans surcouche, la télécommande et le véhicule communique sans problèmes, que ce soit pour un envoyer ou recevoir des messages.