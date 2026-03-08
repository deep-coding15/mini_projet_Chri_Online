package com.deep_coding15.mini_projet_Chri_Online.serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.deep_coding15.mini_projet_Chri_Online.serveur.dao.UtilisateurDAO;
import com.deep_coding15.mini_projet_Chri_Online.serveur.modeles.Utilisateur;
// GestionnaireClient.java
public class GestionnaireClient implements Runnable {
    // Initialisation du logger Log4j2
    private static final Logger logger = LogManager.getLogger(GestionnaireClient.class);
    private Socket socket;

    public GestionnaireClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String clientIp = socket.getInetAddress().getHostAddress();
        logger.info("Client connecté : {}!", clientIp);

        try (
                BufferedReader inFomClient = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
                );

                PrintWriter outToClient = new PrintWriter(
                    socket.getOutputStream(), true
                );
        ) {
            String requete;
            //on lit tant que le client envoit des donnés
            while ((requete = inFomClient.readLine()) != null) {
                logger.debug("Requête reçue de {}: {}", clientIp, requete);

                String reponse = traiterRequete(requete);
                outToClient.println(reponse);

                // Gestion du logout
                if("LOGOUT_OK".equalsIgnoreCase(reponse)){
                    logger.info("Déconnexion demandée par le client {}", clientIp);
                    break;
                }
            }
        } catch(IOException e) {
            logger.error("Erreur de communication avec le client {}: {}", clientIp, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            fermerSocket();
        }
    }

    private String traiterRequete(String requete)throws SQLException {
        // C'est ici que tu vas ajouter LOGIN, GET_PRODUITS, etc.
        String[] parts = requete.split(":");
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

        if(requete.startsWith("REGISTER")) {
            String email = parts[1];
            String password = parts[2];
            Utilisateur existant = utilisateurDAO.trouverParEmail(email);

            if(existant != null){
                return "REGISTER_FAILED:" + existant.getEmail() + ":L'utilisateur existe déjà";
            } else {
                Utilisateur newUtilisateur = new Utilisateur(email, password);
                int id = utilisateurDAO.ajouterUtilisateur(newUtilisateur);
                return "REGISTER_OK:" + id + ":CLIENT";
            }
            
        }
        else if (requete.startsWith("LOGIN")) {
            if (parts.length < 3) return "LOGIN_FAIL:Format invalide";
            String email = parts[1];
            String password = parts[2];
            Utilisateur utilisateur = utilisateurDAO.trouverParEmail(email);

            if(utilisateur != null && utilisateur.getPassword().equals(password)){
                logger.info("Authentification réussie pour : {}", email);
                return "LOGIN_OK:" + utilisateur.getId() + utilisateur.getRole();
            } else {
                return "LOGIN_FAIL:Identifiants invalides";
            }
        }
        else if (requete.startsWith("GET_USER")) {
            String email = parts[1];
            Utilisateur utilisateur = utilisateurDAO.trouverParEmail(email);
            if(utilisateur != null){
                return "USER_INFO:" + utilisateur.getEmail() + ":" + utilisateur.getRole();
            } else {
                return "USER_NOT_FOUND";
            }

        } else if(requete.startsWith("HELLO")){
            return "BONJOUR depuis le serveur !";
        } else if (requete.startsWith("LOGOUT")) {
            return "LOGOUT_OK";
        }
        return "ERREUR:Commande inconnue";
    }

    private void fermerSocket() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                logger.info("Connexion fermée proprement.");
            }
        } catch (IOException e) {
            logger.error("Erreur lors de la fermeture du socket: {}", e.getMessage());
        }
    }
}