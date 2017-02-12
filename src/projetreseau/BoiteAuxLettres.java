package projetreseau;

import projetreseau.commandes.CommandeHandler;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DUT Informatique 2A - TD3
 * @author Hugo Da Roit - Benjamin Lévêque
 */
public class BoiteAuxLettres {
    private final Serveur serveur;
    private final SimpleDateFormat dateFormat;
    private final CommandeHandler cmdHandler;

    /**
     * Constructeur de Boite Aux Lettres
     * @param serveur une référence vers le serveur (pour récupérer les clients)
     */
    public BoiteAuxLettres(Serveur serveur) {
        this.serveur = serveur;
        this.dateFormat = new SimpleDateFormat("HH:mm");
        this.cmdHandler = new CommandeHandler(this);
    }
    
    /**
     * Méthode qui envoi un message (data) de la part d'un client (expediteur)
     * à tout les clients sauf celui qui a envoyé le message.
     * Une partie de la méthode est synchronized afin de bien envoyer le
     * message à tout les clients sans possibilité de coupure par l'ordonanceur.
     * Ainsi on est sûr que les clients ont tous reçu le message.
     * La méthode est synchronisé car c'est une partie critique du programme.
     * Il ne faut pas que l'ordonnanceur arrête cette méthode pour faire autre
     * chose au risque que cela soit bloquant. On impose donc que dès qu'il
     * entre dans la méthode alors il la termine avant de faire autre chose.
     * @param expediteur une référence du client qui envoi un message ou une
     * commande
     * @param data le contenu du message a envoyé
     */
    public synchronized void send(ClientHandler expediteur, String data) {
        if(msgValide(expediteur.getNomClient(), data)) {
            if(data.startsWith("/")) { // Alors c'est une commande
                serveur.printMsg("BAL send -> Commande de la part de " + expediteur.getNomClient() + " : " + data);
                String reponse = cmdHandler.handle(expediteur, data); // Réponse selon la commande
                try {
                    if(!reponse.equals("QUIT")) { // Pour éviter d'envoyer un message à un client déconnecté
                        expediteur.getWriter().write('(' + dateFormat.format(new Date()) + ") SERVEUR --> " + reponse); // On envoit la réponse au client concerné
                        expediteur.getWriter().newLine();
                        expediteur.getWriter().flush();
                    }
                } catch (IOException ex) {
                    serveur.printMsg("BAL send -> Erreur lors de la réponse à une commande : " + ex.getMessage());
                }
            } else { // Sinon c'est un message
                for(ClientHandler client : serveur.getClients()) { // Pour chaque client connecté, on pourrait utiliser un objet Stream mais c'est moins compréhensible
                    if(!client.getNomClient().equals(expediteur.getNomClient())) { // Pour éviter de s'auto envoyer le message
                        try {
                            client.getWriter().write('(' + dateFormat.format(new Date()) + ") " + expediteur.getNomClient() + " -> " + data); // On écrit le message sous la forme : Nom -> Message
                            client.getWriter().newLine();
                            client.getWriter().flush();
                        } catch (IOException ex) {
                            serveur.printMsg("BAL send -> Erreur lors de l'envoi d'un message : " + ex.getMessage());
                        }
                    }
                }
            }
        } else {
            serveur.printMsg("BAL send -> Le nom du client ou le contenu de son message est invalide. Nom : " + expediteur.getNomClient() + " -> Message : " + data);
        }
    }
    
    /**
     * Méthode identique à send sauf que l'on envoi le message à TOUT
     * les clients connectés.
     * @param data le message à envoyer, ou la commande à éxecuter
     */
    public synchronized void sendToAll(String data) {
        if(msgValide(data) && !data.startsWith("/")) { // Si message valide et n'est pas une commande
            for(ClientHandler client : serveur.getClients()) { // Envoi à chaque client de la chaine data
                try {
                    client.getWriter().write('(' + dateFormat.format(new Date()) + ") SERVEUR --> " + data);
                    client.getWriter().newLine();
                    client.getWriter().flush();
                } catch (IOException ex) {
                    serveur.printMsg("BAL sendToAll -> Erreur lors de l'envoi d'un message : " + ex.getMessage());
                }
            }
        } else {
            serveur.printMsg("BAL sendToAll -> Vous essayez d'envoyer un message invalide (commandes, caractères interdits, chaines nulle ... : " + data);
        }
    }

    private boolean msgValide(String nomClient, String data) {
        return nomClient != null && !"".equals(nomClient) && data != null && !"".equals(data);
    }
    
    private boolean msgValide(String data) {
        return data != null && !"".equals(data);
    }
    
    /**
     * Retoune une référence au serveur
     * @return le serveur
     */
    public Serveur getServeur() {
        return serveur;
    }
    
    
}
