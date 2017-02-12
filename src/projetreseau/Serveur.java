package projetreseau;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * DUT Informatique 2A - TD3
 * @author Hugo Da Roit - Benjamin Lévêque
 */
public class Serveur {
    private ServerSocket serveur;
    private Socket client;
    private long runtime;
    private LinkedList<ClientHandler> clients;
    private BoiteAuxLettres bal;
    private List<String> motd;
    
    /**
     * Création du serveur
     * Lancement du serveur
     */
    public Serveur() {
        try {
            this.runtime = System.currentTimeMillis();
            this.motd = Files.readAllLines(Paths.get("motd.txt"), Charset.forName("UTF-8"));
            this.serveur = new ServerSocket(2016);
            this.client = null;
            this.clients = new LinkedList<>();
            this.bal = new BoiteAuxLettres(this);
            this.attachShutDownHook(); // Gestion de l'exctinction du serveur
            this.lancerServeur();
        } catch (IOException ex) {
            printMsg("Serveur constructeur -> (port occupé, motd.txd inexistant ...) :\n" + ex.getMessage());
        } finally {
            System.exit(-1);
        }
    }
    
    /**
     * Méthode principale du serveur, c'est une boucle interminable
     * Attend des clients pui créer un ClientHandler par client qui se connecte
     */
    private void lancerServeur() {
        printMsg("Serveur lancerServeur -> Lancement du serveur.");
        do {
            try {
                printMsg("Serveur lancerServeur -> Attente de la connexion d'un client ...");
                client = serveur.accept(); // Dès qu'un client se connecte on l'associe au socket client
                printMsg("Serveur lancerServeur -> Client connecté -> " + client.getInetAddress() + ':' + client.getPort());
                
                // On associe un ClientHandler au client pour gérer les messages
                ClientHandler clientHandler = new ClientHandler(client, this);
                Thread threadClient = new Thread(clientHandler);
                threadClient.start();
                
            } catch (IOException ex) {
                printMsg("Serveur lancerServeur -> Erreur lors de l'accept : " + ex.getMessage());
            }
        } while(true); // Le serveur ne doit jamais s'arrêter
    }
    
    /**
     * Affiche un message passé en paramètre avec une certaine forme :
     * (durée depuis le lancement du programme en MS) : message
     * @param msg Le message à écrire dans la console
     */
    public void printMsg(String msg) {
        System.out.println(getRunTime() + "ms : " + msg);
    }
    
    /**
     * Permet de récupèrer le runtime du serveur
     * @return le temps depuis lequel le serveur est lancé
     */
    private long getRunTime() {
        return System.currentTimeMillis() - runtime;
    }
    
    /**
     * Méthode qui ajoute un ShutdowHook pour gérer la fin du programme proprement
     */
    private void attachShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHandler(this)));
    }

    /**
     * Permet de récupérer le socket du dernier client connecté
     * @return un socket d'un client
     */
    public Socket getClientSocket() {
        return client;
    }

    /**
     * Permet de récupérer le Server Socket de notre serveur
     * @return le ServerSocket de notre Serveur
     */
    public ServerSocket getServerSocket() {
        return serveur;
    }
    
    /**
     * Permet de récupérer la Boite Aux Lettres, elle gère les inputs des clients
     * @return la Boite Aux Lettres
     */
    public BoiteAuxLettres getBaL() {
        return bal;
    }
    
    /**
     * Permet de récupérer une liste de clients connectés au serveur
     * @return une liste de clients connectés
     */
    public LinkedList<ClientHandler> getClients(){
        return clients;
    }

    /**
     * Permet de récupérer le mot du jour (motd)
     * @return le motd
     */
    public List<String> getMotd() {
        return this.motd;
    }
}