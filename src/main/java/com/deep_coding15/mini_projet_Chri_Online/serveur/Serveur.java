package com.deep_coding15.mini_projet_Chri_Online.serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {
    static final public String SERVER_HOST = "localhost";
    static final public int SERVER_PORT = 1234;

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(Serveur.SERVER_PORT);
        System.out.println("Serveur démarré sur le port " + SERVER_PORT + "...");

        while (true) {
            //Attente de connexion d'un client
            Socket socket = serverSocket.accept();
            new Thread(new GestionnaireClient(socket)).start();
        }

    }
}
