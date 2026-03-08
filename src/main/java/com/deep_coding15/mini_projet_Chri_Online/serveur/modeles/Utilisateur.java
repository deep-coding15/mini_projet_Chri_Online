package com.deep_coding15.mini_projet_Chri_Online.serveur.modeles;

public class Utilisateur {
    private int id;
    private String email;
    private String password;
    private String role;    

    public Utilisateur() {}

    public Utilisateur(int id, String email, String password){
        this.id = id;
        this.email = email;
        this.password = password;
    }
    
    public Utilisateur(String email, String password){
        this.email = email;
        this.password = password;
    }

    public Utilisateur(String email, String password, String role){
        this.role = role;
        this.email = email;
        this.password = password;
        if(role == null){
            this.role = "CLIENT";
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
