## Tuto

Le premier pas consiste à installer le Lejos firmware sur le boitier EV3. Pour cela, munissez vous d'une carte SD
entre 2 et 32 Go. De préférence, elle doit être vide et au format .FAT32.

Suivez ensuite le tutoriel (vraiment, soyez attentif même si vous pensez avoir compris.. ne résinstallez pas Eclipse non plus, ya des limites) sur cette vidéo : 

https://www.youtube.com/watch?v=ycOA4wnvAyM

Vous devriez maintenant avoir le boitier EV3 qui boot sur LEJOS au lieu de mindstorm et le plugin Eclipse installé. Si c'est le cas, bravo, c'est la fin de ce tutoriel, passer directement à la section "FIN".

Sinon, peut-être la suite pourra vous aider:

### Problème pour installer LEJOS sur le boitier

Nous avons aussi eu des problème au début, et nous avons testé avec une autre carte SD, et tout fonctionnait après ça.
Je ne peux que vous conseiller de suivre le tutoriel vidéo à la lettre (si ça ne fonctionne toujours pas, regardez la section suivante sur le PLUGIN ECLIPSE).

### Problème pour installer le plugin LEJOS sur Eclipse

Si vous avez des problèmes avec cette étape, encore une fois, suivez le tutoriel à la lettre, DEPUIS LE DÉBUT. Lorsque vous avez
installé le plugin via le menu Eclipse Help => Install New SoftWare, il faut absolument aller voir dans le menu : 
Window => Preferences => Lejos EV3 

Regardez le champ EV3_HOME. S'il est vide, indiquez le chemin du dossier où vous avez installer LEJOS EV3 (en suivant le tutoriel). Chez moi,
il se trouve dans C:\Program Files. 

### FIN

Normalement, vous devriez maintenant avoir le boitier qui boot sur le firmware LEJOS et le plugin LEJOS sur Eclipse.
Pour créer un projet LEJOS sur Eclipse, faites File => New => Projet => Lejos EV3 => Lejos EV3 Project

Si une erreur du style "Build path problem" arrive, vous n'avez pas correctement lié EV3_HOME comme vu précédemment.

Allez dans Windows => Preference => LEjos EV3 et cochez Connect To names Brick et entrez dans le champ l'addresse 10.0.1.1. Validez. 

