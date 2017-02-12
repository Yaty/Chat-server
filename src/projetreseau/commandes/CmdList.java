package projetreseau.commandes;

import java.util.Set;
import projetreseau.BoiteAuxLettres;
import projetreseau.ClientHandler;

/**
 * DUT Informatique 2A - TD3
 * @author Hugo Da Roit - Benjamin Lévêque
 */
public class CmdList extends Commande {

    /**
     * Constructeur par défaut de la commande list
     * @param bal une référence vers la boite aux lettres
     */    
    public CmdList(BoiteAuxLettres bal) {
        super(bal);
    }

    /**
     * Méthode qui va chercher et retourner la liste des clients connectés.
     * @param clientDemandeur le client qui execute la commande, ici on ne va pas l'utiliser
     * @param cmd la liste des commandes existantes, ici on ne va pas l'utiliser
     * @return la liste des clients.
     */
    @Override
    public String executer(ClientHandler clientDemandeur, Set<String> cmd) {
        String res = "Liste des clients connectes : ";
        for(ClientHandler client : bal.getServeur().getClients())
            res += client.getNomClient() + ", ";
        return res;
    }
    
}
