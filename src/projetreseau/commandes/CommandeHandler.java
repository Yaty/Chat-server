package projetreseau.commandes;

import java.util.LinkedHashMap;
import projetreseau.BoiteAuxLettres;
import projetreseau.ClientHandler;

/**
 * DUT Informatique 2A - TD3
 * @author Hugo Da Roit - Benjamin Lévêque
 */
public class CommandeHandler {
    private final LinkedHashMap<String, Commande> commandeMap;
    
    /**
     * Constructeur par défaut de ce Handler
     * @param bal une référence vers la boite aux lettres.
     */
    public CommandeHandler(BoiteAuxLettres bal) {
        this.commandeMap = new LinkedHashMap();
        this.commandeMap.put("LIST", new CmdList(bal));
        this.commandeMap.put("AWAY", new CmdAway(bal));
        this.commandeMap.put("INFO", new CmdInfo(bal));
        this.commandeMap.put("MOTD", new CmdMotd(bal));
        this.commandeMap.put("QUIT", new CmdQuit(bal));
    }
    
    /**
     * Méthode pour gérer les commandes.
     * On va executer une instance d'un objet Commande qui est associé
     * dans la commandeMap au String cmd passé en paramètre.
     * On joue avec le polymorphisme et les Map pour éviter un gros switch case.
     * @param client une référence vers le client qui a lancé la commande
     * @param cmd la commande que le client veut éxecuter
     * @return la réponse de la commande
     */
    public String handle(ClientHandler client, String cmd) {
        String nomCmd = cmd.substring(1).trim().toUpperCase();    // La commande
        Commande commande = commandeMap.get(nomCmd);              // On récupère l'objet qui va gérer la commande dans la map, il est associé à un string (via la Map)
        if(commande == null) return "Commande inexistante, tapez /list pour avoir une liste des commandes.";
        return commande.executer(client, commandeMap.keySet());   // On éxecute la commande en passant le client demandeur et la liste des commandes (utile pour CmdInfo)
    }
   
}
