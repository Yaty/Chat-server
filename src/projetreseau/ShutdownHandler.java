package projetreseau;

import java.io.IOException;

/**
 * DUT Informatique 2A - TD3
 * @author Hugo Da Roit - Benjamin Lévêque
 */
public class ShutdownHandler implements Runnable {

    private final Serveur serveur;
    
    /**
     * Constucteur par défaut
     * @param serveur une référence au serveur que l'on gère
     */
    public ShutdownHandler(Serveur serveur) {
        this.serveur = serveur;
    }
    
    /*
    * On ferme les sockets, les clients etc ...
    * La JVM est sensé le faire à notre place mais on ne sait jamais.
    */
    @Override
    public void run() {
        try {            
            for(ClientHandler client : serveur.getClients())
                client.deconnexion();
            if(!serveur.getClientSocket().isClosed())
                serveur.getClientSocket().close();
            if(!serveur.getServerSocket().isClosed())
                serveur.getServerSocket().close();
        } catch (IOException ex) {
            serveur.printMsg("Erreur lors l'extinction du serveur : " + ex.getMessage());
        } finally {
            System.exit(0);
        }
    }
    
}
