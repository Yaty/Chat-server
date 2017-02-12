package projetreseau.commandes;

import java.util.Set;
import projetreseau.BoiteAuxLettres;
import projetreseau.ClientHandler;

/**
 * DUT Informatique 2A - TD3
 * @author Hugo Da Roit - Benjamin Lévêque
 * Classe qui possède un méthode executer.
 * Celle-ci va être rédéfinit par les différentes commandes et appelé
 * par les clients.
 */
public abstract class Commande {

    /**
     * Une référence vers la boite aux lettres.
     */
    protected BoiteAuxLettres bal;
    
    /**
     * Constructeur de base pour un objet Commande
     * @param bal une référence vers la boite aux lettres
     */
    public Commande(BoiteAuxLettres bal) {
        this.bal = bal;
    }
    
    /**
     * Méthode pour lancer une commande
     * @param client le client qui lance la commande
     * @param cmd une liste de commande (utile pour /list)
     * @return la réponse de la commande en string
     */
    public abstract String executer(ClientHandler client, Set<String> cmd);
}
