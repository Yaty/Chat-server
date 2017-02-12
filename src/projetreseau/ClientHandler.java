package projetreseau;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * DUT Informatique 2A - TD3
 * @author Hugo Da Roit - Benjamin Lévêque
 */
public class ClientHandler implements Runnable {
    private Socket client;
    private String nomClient;
    private Serveur serveur;
    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean away; // Si vrai le client est présent devant le chat, change selon la commande /away
    
    /**
     * Constructeur d'un ClientHandler
     * Une instance de ClientHandler = un client connecté !
     * @param client le client qui s'est connecté
     * @param serveur une référence vers le serveur
     */
    public ClientHandler(Socket client, Serveur serveur) {
        try {
            this.client = client;
            this.serveur = serveur;
            this.away = true;
            // Buffers pour gérer les entrées/sorties
            reader = new BufferedReader(new InputStreamReader(client.getInputStream(), Charset.forName("UTF-8")));
            writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), Charset.forName("UTF-8")));
        } catch (IOException ex) {
            serveur.printMsg("ClientHandler constructeur -> Erreur avec le reader ou le writer de : " + Thread.currentThread().getName() + " : " + ex.getMessage());
        }
    }
    
    /**
     * Permet de récupérer le nom du client
     * @return le nom du client qu'il s'est choisit à la connexion
     */
    public String getNomClient() {
        return nomClient;
    }
    
    /**
     * Permet de récupérer le writer pour écrire au client
     * @return le buffer writer
     */
    public BufferedWriter getWriter() {
        return writer;
    }
    
    /**
     * Méthode appelé lorsqu'on lance le Thread associé à un ClientHandler
     * Cette méthode applique le protocole (bienvenue, demande du pseudo ...)
     * Elle récupère les entrées du clientet l'envoi à la boite aux lettres
     * qui diffuse les messafes à tout le monde.
     * Elle affiche aussi les messages que la boite aux lettres pourrait lui
     * envoyer.
     */
    @Override
    public void run() {
        try {          
            writer.write("Bienvenue sur le serveur HB !");
            writer.newLine();
            writer.write("Veuillez entrez votre pseudo : ");
            writer.flush();
            
            nomClient = reader.readLine();
            if(nomClient != null)
                Thread.currentThread().setName(nomClient);
            serveur.getClients().add(this); // On considère maintenant que l'on peut recevoir des messages, on ajoute donc le client dans la liste
            
            writer.write("Vous etes connectes avec les autres utilisateurs. Vous pouvez commencer a dialoguer.");
            writer.newLine();
            writer.write("Liste des utilisateurs connectes : ");
            for(ClientHandler client : serveur.getClients())
                writer.write(client.getNomClient() + ", ");
            writer.newLine();
            writer.flush();
           
            serveur.getBaL().sendToAll("Bienvenue a " + nomClient + " sur le serveur HB.");
            this.away = false;

            String entreeClient;
            // Boucle qui récupère les entrées du client et envoi le message à la boite au lettre qui l'enverra à tout le monde
            do {   
                // On recupère les entrées
                entreeClient = reader.readLine();
                // On envoi à la BaL
                if(entreeClient != null)
                    serveur.getBaL().send(this, entreeClient);
                else // Quand entreeClient est null c'est que le client s'est déconnecté sans "bye"
                    entreeClient = "bye";
            } while(!entreeClient.equals("bye")); 
        } catch (Exception ex) {
            serveur.printMsg("ClientHandler run -> Erreur : " + ex.getMessage() + ". Le client a du se déconnecter.");
        } finally {
            deconnexion(); // On gère la fin du client au cas ou
        }
    }
    
    /**
     * Méthode qui va gérer la fin de l'objet ClientHandler du client qui se déconnecte
     * On va fermer le socket client, et enlever le client de la liste des clients connectés
     * Le Garbage Collector fera le reste du boulot et supprimera l'objet
     * Méthode synchronized pour que l'on soit sûr de tout éteindre d'un seul coup (pour éviter que l'ordonnanceur fasse autre chose)
     */
    public synchronized void deconnexion() {
        try {
            if(!client.isClosed()) {
                client.close(); // Fermeture du socket client en jeu
                serveur.printMsg("ClientHandler deconnexion -> Deconnexion de l'utilisateur " + nomClient + '.');
                if(serveur.getClients().contains(this))
                    serveur.getClients().remove(this);
                serveur.getBaL().sendToAll(nomClient + " vient de se deconnecter."); // On informe les clients connectés de la déconnexion
            }
        } catch (IOException ex) {
            serveur.printMsg("ClientHandler deconnexion -> Impossible de fermer le socket du client " + nomClient + " : " + ex.getMessage());
        }
    }
    
    /**
     * Retourne la valeur du booléan away
     * @return la valeur de away
     */
    public boolean isAway() {
        return this.away;
    }
    
    /**
     * Affecte une variable à away
     * @param b la valur a affectée à away
     */
    public void setAway(boolean b) {
        this.away = b;
    }
    
}
