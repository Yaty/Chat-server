package projetreseau.commandes;

import java.util.Set;
import projetreseau.BoiteAuxLettres;
import projetreseau.ClientHandler;

/**
 * DUT Informatique 2A - TD3
 * @author Hugo Da Roit - Benjamin Lévêque
 */
public class CmdMotd extends Commande {

    /**
     * Constructeur par défaut de la commande motd
     * @param bal une référence vers la boite aux lettres
     */
    public CmdMotd(BoiteAuxLettres bal) {
        super(bal);
    }
    
    /**
     * Méthode qui va renvoyer le mot du jour (motd).
     * @param client le client qui execute la commande, ici on ne va pas l'utiliser
     * @param cmd la liste des commandes existantes, ici on ne va pas l'utiliser
     * @return le mot du jour.
     */
    @Override
    public String executer(ClientHandler client, Set<String> cmd) {
        String motd = null;
        for(String line : bal.getServeur().getMotd())
            motd += line;
        return motd == null ? "Le mot du jour n'est pas définit.\n" : "Mot du jour :\n".concat(motd);
    }
    
}
