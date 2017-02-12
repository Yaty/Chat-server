/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetreseau.commandes;

import java.util.Set;
import projetreseau.BoiteAuxLettres;
import projetreseau.ClientHandler;

/**
 *
 * @author Hugo Da Roit - contact@hdaroit.fr
 */
public class CmdQuit extends Commande {
    
    /**
     * Constructeur par défaut de la commande quit
     * @param bal une référence vers la boite aux lettres
     */
    public CmdQuit(BoiteAuxLettres bal) {
        super(bal);
    }

    /**
     * Déconnecte le client, et retourne un string pour annoncer la déconnexion d'un client
     * @param client le client qui veut se déconnecter
     * @param cmd la liste des commandes, ici on ne va pas l'utiliser
     * @return un string "QUIT" pour annoncer à la BAL qu'il va se déconnecter
     */
    @Override
    public String executer(ClientHandler client, Set<String> cmd) {
        client.deconnexion();
        return "QUIT"; // Le client ne verrat pas ce message, on envoi un string QUIT pour informer la BAL.
    }
    
}
