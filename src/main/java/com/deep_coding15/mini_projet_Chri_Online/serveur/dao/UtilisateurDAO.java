package com.deep_coding15.mini_projet_Chri_Online.serveur.dao;

import com.deep_coding15.mini_projet_Chri_Online.serveur.GestionnaireClient;
import com.deep_coding15.mini_projet_Chri_Online.serveur.modeles.Utilisateur;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class UtilisateurDAO {
    private static final Logger logger = LogManager.getLogger(GestionnaireClient.class);

    public void initialiserTableUtilisateurs() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS utilisateurs (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL," +
                "role TEXT DEFAULT 'CLIENT');";

        try (Connection conn = ConnexionBD.getConnexion(); // Utilise ta classe de connexion
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            logger.info("Table 'utilisateurs' vérifiée/créée.");
        } catch (SQLException e) {
            logger.error("Erreur lors de la création de la table: " + e.getMessage());
        }
    }
    public UtilisateurDAO() throws SQLException {
        initialiserTableUtilisateurs();
    }

    public int ajouterUtilisateur(Utilisateur utilisateur) throws SQLException {
        String sql = "INSERT INTO utilisateurs (email, password, role) VALUES (?, ?, ?)";
        try (Connection conn = ConnexionBD.getConnexion()) {
            String roleUtilisateur = utilisateur.getRole() == null ? "CLIENT" : utilisateur.getRole();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, utilisateur.getEmail());
            preparedStatement.setString(2, utilisateur.getPassword());
            preparedStatement.setString(3, roleUtilisateur);
            return preparedStatement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Utilisateur trouverParEmail(String email) throws SQLException {
        String sql = "SELECT * FROM utilisateurs WHERE email = ?";
        try (Connection conn = ConnexionBD.getConnexion()) {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(resultSet.getInt("id"));
                utilisateur.setEmail(resultSet.getString("email"));
                utilisateur.setPassword(resultSet.getString("password"));
                utilisateur.setRole(resultSet.getString("role"));
                return utilisateur;
            } else {
                return null;
            }
        }catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
