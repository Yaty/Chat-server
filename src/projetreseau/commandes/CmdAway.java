package projetreseau.commandes;

import java.util.Set;
import projetreseau.BoiteAuxLettres;
import projetreseau.ClientHandler;

/**
 * DUT Informatique 2A - TD3
 * @author Hugo Da Roit - Benjamin Lévêque
 */
public class CmdAway extends Commande {

    /**
     * Constructeur par défaut de la commande away
     * @param bal une référence vers la boite aux lettres
     */
    public CmdAway(BoiteAuxLettres bal) {
        super(bal);
    }
    
    /**
     * Méthode qui va faire passer l'état du client en Away ou non.
     * @param client le client qui execute la commande
     * @param cmd la liste des commandes existantes, ici on ne va pas l'utiliser
     * @return un message pour dire si le client passe en Away ou pas.
     */
    @Override
    public String executer(ClientHandler client, Set<String> cmd) {
       
        if(client == null) return "Erreur lors de l'éxecution de la commande away.";
        
        if(client.isAway()) {
            client.setAway(false);
            bal.sendToAll(client.getNomClient() + " vient de se reveiller !");
            return "Vous venez de vous reveiller !";
        } else {
            client.setAway(true);
            bal.sendToAll(client.getNomClient() + " s'endort ...");
            return "Vous vous endormez ...";
        }
    }
    
}
