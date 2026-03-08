package com.deep_coding15.mini_projet_Chri_Online.client;

import com.deep_coding15.mini_projet_Chri_Online.serveur.GestionnaireClient;
import com.deep_coding15.mini_projet_Chri_Online.serveur.Serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Client {
    private static final Logger logger = LogManager.getLogger(GestionnaireClient.class);
    
    public static void main(String[] args) throws IOException {
        try{
            Socket socket = new Socket(Serveur.SERVER_HOST, Serveur.SERVER_PORT);
            
            PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader inFromServer = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            // Thread de lecture : Il tourne en boule en arrière-plan
            // 1. Lancer le thread de lecture (Ecoute le serveur en continu)
            Thread threadLecture = new Thread(() -> {
                try{
                    String reponse;
                    while ((reponse = inFromServer.readLine()) != null) {
                        System.out.println("\n[SERVEUR] : " + reponse);
                        
                        if (reponse.equalsIgnoreCase("LOGOUT_OK")) {
                            logger.info("Déconnexion confirmée par le serveur.");
                            break; // On sort de la boucle de lecture
                        }
                        // Ici, tu mets à jour ton interface Swing :
                        // SwingUtilities.invokeLater(() -> label.setText(reponse));
                    }

                }catch(IOException ex){
                    logger.error("Connexion perdue avec le serveur.");
                }
                finally{
                    try { 
                        socket.close(); 
                    } catch (IOException e) { 
                        e.printStackTrace(); 
                    }
                }
            });

            threadLecture.start();

            // Boucle d'envoi (simule l'interface utilisateur)
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Connecté ! Tapez 'LOGIN', 'HELLO' ou 'LOGOUT' : ");
            
            String texte;
            while(!socket.isClosed()) {
                texte = console.readLine();
                if(texte == null) break;
                outToServer.println(texte); //Envoie du message au serveur

                if ("LOGOUT".equalsIgnoreCase(texte)) break;
            }
        } catch(IOException e) {
            logger.error("Impossible de se connecter au serveur.");
        }

    }
}

