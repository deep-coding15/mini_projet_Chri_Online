package com.deep_coding15.mini_projet_Chri_Online.serveur.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionBD {
    private static final String URL = "jdbc:sqlite:chri_online.db";

    public static Connection getConnexion() throws SQLException {
        return DriverManager.getConnection(URL);
    }

}