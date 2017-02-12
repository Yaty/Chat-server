package projetreseau.commandes;

import java.util.Set;
import projetreseau.BoiteAuxLettres;
import projetreseau.ClientHandler;

/**
 * DUT Informatique 2A - TD3
 * @author Hugo Da Roit - Benjamin Lévêque
 */
public class CmdInfo extends Commande {
    
    /**
     * Constructeur par défaut de la commande info
     * @param bal une référence vers la boite aux lettres
     */
    public CmdInfo(BoiteAuxLettres bal) {
        super(bal);
    }

    /**
     * Méthode qui va lister les commandes existantes
     * @param client le client qui execute la commande, ici on ne va pas l'utiliser
     * @param cmd la liste des commandes existantes
     * @return une chaine représentans les commandes existantes
     */
    @Override
    public String executer(ClientHandler client, Set<String> cmd) {
        if(cmd == null) return "Erreur lors de la rechercher des commandes existantes.";
        String reponse = "Liste des commandes : ";
        for (String method : cmd)
            reponse += '/' + method + ", ";
        return reponse;
    }
    
}
