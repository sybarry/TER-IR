package pilottageColorSensor;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pilottageMQTT.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe Automate
 * Cette classe permet de lire un fichier JSON et de construire un automate à partir de ce fichier.
 * L'automate est représenté par un HashMap dont les clés sont les états de l'automate et les valeurs sont des listes
 * d'objets Action_Etat. Chaque objet Action_Etat contient une action (avancer, reculer, tourner à gauche, tourner à
 * droite, s'arrêter) et un état de destination.
 */
public class Automate {

    /**
     * Pattern pour valider le format de la valeur d'une clé dans le fichier JSON <br>
     * Exemple de format valide : "g(100)-2"
     */
    Pattern pattern = Pattern.compile("([a-zA-Z]+)\\((\\d+)\\)(?:-(\\d+))?");
    private final Set<String> keys;
    private final JSONObject jsonPayload;
    private final Map<String, ArrayList<Action_Etat>> automate;

    /**
     * Constructeur de la classe Automate, qui lit un fichier JSON et construit un automate à partir de ce fichier.
     *
     * @param payload : le contenu du fichier JSON
     * @throws ParseException : exception levée si le fichier JSON n'est pas valide
     */
    public Automate(String payload) throws ParseException {
        JSONParser parser = new JSONParser();
        jsonPayload = (JSONObject) parser.parse(payload);

        // Extraire les valeurs des champs "etats" et "action"
        automate = new HashMap<>();
        keys = jsonPayload.keySet();

        //Conduit L'automate en utilisant un HashMap
        BuildAutomate(keys);
    }


   /**
        * Méthode qui construit l'automate à partir du contenu du fichier JSON.
        *
        * @param keys : les clés du fichier JSON
    */
    public void BuildAutomate(Set<String> keys) {
        for (String key : keys) {
            Object valueObj = jsonPayload.get(key);

            if (valueObj instanceof JSONArray) {
                processJSONArray(key, (JSONArray) valueObj);
            } else if (valueObj instanceof String) {
                processString(key, (String) valueObj);
            }
        }
    }

    /**
     * Méthode qui traite un tableau JSON.
     *
     * @param key : la clé du tableau JSON
     * @param jsonArray : le tableau JSON
     */
    private void processJSONArray(String key, JSONArray jsonArray) {
        for (Object obj : jsonArray) {
            if (obj instanceof String) {
                processString(key, (String) obj);
            }
        }
    }

    /**
     * Méthode qui traite une chaîne de caractères.
     *
     * @param key : la clé de la chaîne de caractères
     * @param value : la chaîne de caractères
     */
    private void processString(String key, String value) {
        Matcher matcher = pattern.matcher(value);
        Action actionElement;

        if (matcher.matches()) {
            String action = matcher.group(1);
            int temps = Integer.parseInt(matcher.group(2));
            String etat_destination = matcher.group(3);

            if (etat_destination != null && etat_destination.startsWith("-")) {
                etat_destination = etat_destination.substring(1);
            }

            actionElement = Action(action);

            Action_Etat element = new Action_Etat(actionElement, temps, etat_destination);

            if (automate.containsKey(key)) {
                ArrayList<Action_Etat> list = automate.get(key);
                list.add(element);
                automate.put(key, list);
            } else {
                ArrayList<Action_Etat> list = new ArrayList<>();
                list.add(element);
                automate.put(key, list);
            }
        } else {
            System.out.println("La chaîne ne correspond pas au format attendu.");
        }
    }

    /**
     * Retourne une action en fonction de la chaîne de caractères passée en paramètre.
     *
     * @param action : la chaîne de caractères
     * @return l'action correspondante
     */
    public Action Action(String action) {
        switch (action) {
            case "g":
                return Action.FORWARD;
            case "b":
                return Action.BACKWARD;
            case "s":
                return Action.STOP;
            case "l":
                return Action.LEFT;
            case "r":
                return Action.RIGHT;

            default:
                return null;
        }
    }


    /**
     * @return l'automate
     */
    public Map<String, ArrayList<Action_Etat>> getAutomate() {
        return this.automate;
    }

}
