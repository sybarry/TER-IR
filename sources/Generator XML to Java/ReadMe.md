# Read Me

Le code Java présenté ici est une première version qui permet de générer automatiquement les classes Client et Server d'un projet Lejos
à partir d'un document XML fourni en entrée. Une DTD est également fournie ci-dessus. Cette dernière n'est pas tout à fait exacte mais permet
surtout d'avoir un aperçu de la structure globale du fichier XML.

La logique de l'automatisation est globalement *linéaire* et surtout sous forme de **composants** : 

![image](/uploads/6e73e35c5e0e44c53fd0d45da6c94fdc/image.png)

La classe Main récupère le XML (qui représente le modèle), puis créée un générateur qui va traiter toutes les classes une par une. Chaque classe traite tout le reste
des informations (comme le nom de la classe, les importations nécessaires, les fonctions, etc) puis donne le code ainsi généré en résultat. La génération du code est progressive 
et se fait à l'aide d'une List<String> qui a (pour l'instant, ceci pourrait être amené à changer) 5 éléments :

    - [0] qui sont les **imports**
    - [1] qui est le nom de la classe
    - [2] qui est la fonction **main**
    - [3] qui sont les fonctions et déclarations de variables
    - [4] qui est l'accolade **}** fermante