# 🛍️ ChriOnline — Application E-Commerce Client/Serveur

> Mini-Projet 1 · Module SSI-AT · UAE-ENSATé 2026  
> Architecture client/serveur native Java avec sockets TCP · SQLite · Maven

---

## 📋 Présentation

**ChriOnline** est une application e-commerce en Java fonctionnant sur une architecture client/serveur basée sur les **sockets TCP**. Le serveur gère toute la logique métier et la persistance des données, tandis que le client se connecte et envoie des requêtes textuelles selon un protocole défini.

---

## ✅ Fonctionnalités implémentées

| Fonctionnalité | Commande client | Réponse serveur |
|----------------|-----------------|-----------------|
| Inscription | `REGISTER:email:password` | `REGISTER_OK:id:CLIENT` ou `REGISTER_FAILED:email:message` |
| Connexion | `LOGIN:email:password` | `LOGIN_OK:id:role` ou `LOGIN_FAIL:message` |
| Déconnexion | `LOGOUT` | `LOGOUT_OK` |
| Info utilisateur | `GET_USER:email` | `USER_INFO:email:role` ou `USER_NOT_FOUND` |
| Test connexion | `HELLO` | `BONJOUR depuis le serveur !` |

---

## 🏗️ Architecture du projet

```
mini_projet_Chri_Online/
│
├── pom.xml                                         # Configuration Maven + dépendances
├── chri_online.db                                  # Base de données SQLite (générée automatiquement)
│
└── src/main/java/com/deep_coding15/mini_projet_Chri_Online/
    │
    ├── Main.java                                   # Point d'entrée principal (à configurer)
    │
    ├── client/
    │   └── Client.java                             # Client TCP console (thread de lecture + envoi)
    │
    └── serveur/
        ├── Serveur.java                            # Serveur TCP — écoute sur le port 1234
        ├── GestionnaireClient.java                 # Thread dédié à chaque client connecté
        │
        ├── modeles/
        │   └── Utilisateur.java                    # Modèle de données utilisateur
        │
        └── dao/
            ├── ConnexionBD.java                    # Connexion JDBC à chri_online.db
            └── UtilisateurDAO.java                 # CRUD utilisateurs + création de la table
```

---

## ⚙️ Prérequis

- **Java** 21+ (le projet est configuré avec le compilateur Java 25)
- **Maven** 3.6+
- Pas besoin d'installer SQLite — le driver JDBC est inclus dans les dépendances Maven

---

## 🚀 Lancer l'application

### 1. Cloner le projet

```bash
git clone <url-du-repo>
cd mini_projet_Chri_Online
```

### 2. Compiler le projet

```bash
mvn compile
```

### 3. Démarrer le serveur

Ouvrir un **premier terminal** et lancer :

```bash
mvn exec:java -Dexec.mainClass="com.deep_coding15.mini_projet_Chri_Online.serveur.Serveur"
```

Vous devriez voir :
```
Serveur démarré sur le port 1234...
```

### 4. Démarrer le client

Ouvrir un **second terminal** et lancer :

```bash
mvn exec:java -Dexec.mainClass="com.deep_coding15.mini_projet_Chri_Online.client.Client"
```

---

## 💬 Utilisation du client console

Une fois le client lancé, vous pouvez taper des commandes directement :

```bash
# Tester la connexion
HELLO

# Créer un compte
REGISTER:alice@mail.com:monMotDePasse

# Se connecter
LOGIN:alice@mail.com:monMotDePasse

# Récupérer les infos d'un utilisateur
GET_USER:alice@mail.com

# Se déconnecter
LOGOUT
```

---

## 🗄️ Base de données

- **Fichier** : `chri_online.db` (à la racine du projet)
- **Driver** : `org.xerial:sqlite-jdbc:3.51.2.0`
- **Création automatique** : la table `utilisateurs` est créée au premier démarrage du serveur

### Structure de la table `utilisateurs`

```sql
CREATE TABLE IF NOT EXISTS utilisateurs (
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    email    TEXT    NOT NULL UNIQUE,
    password TEXT    NOT NULL,
    role     TEXT    DEFAULT 'CLIENT'
);
```

---

## 📦 Dépendances Maven

| Dépendance | Version | Utilisation |
|------------|---------|-------------|
| `log4j-api` | 2.25.3 | Logging (interface) |
| `log4j-core` | 2.25.3 | Logging (implémentation) |
| `sqlite-jdbc` | 3.51.2.0 | Connexion à la base SQLite |

---

## 🔌 Protocole de communication TCP

Le client et le serveur communiquent via des **messages texte** séparés par `:`.  
Chaque message est terminé par un saut de ligne (`\n`).

```
Client  →  Serveur : "COMMANDE:param1:param2"
Serveur →  Client  : "REPONSE:valeur1:valeur2"
```

Le serveur ferme la connexion proprement après réception de `LOGOUT`.

---

## 📝 Logs

Les logs sont gérés par **Log4j2** et configurés dans `src/main/resources/log4j2.xml`.  
Chaque connexion, requête et erreur est tracée dans la console avec le niveau approprié (`INFO`, `DEBUG`, `ERROR`).

---

## 📅 Livraison

Date limite : **vendredi 27 mars 2026** (semaine CC1)