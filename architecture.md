# 🏛️ Guide d'Architecture & Répartition des Tâches — ChriOnline

> Document de référence pour l'équipe de développement (2 membres)

---

## Vue d'ensemble de l'architecture

ChriOnline suit une architecture **client/serveur stricte** : le client ne touche jamais la base de données directement. Toute la logique passe par le serveur.

```
┌─────────────────────┐         TCP Socket            ┌──────────────────────────────┐
│      CLIENT         │  ──── requête texte ──────►   │         SERVEUR              │
│   (Interface)       │  ◄─── réponse texte ────────  │  (Logique métier + BD)       │
│                     │        port 1234              │                              │
│  Client.java        │                               │  Serveur.java                │
│  Vues Swing/JavaFX  │                               │  GestionnaireClient.java     │
│                     │                               │  DAO + Modèles + BD SQLite   │
└─────────────────────┘                               └──────────────────────────────┘
```

---

## Structure cible complète du projet

Voici l'architecture **finale** à atteindre. Les fichiers ✅ existent déjà, les fichiers 🔲 sont à créer.

```
mini_projet_Chri_Online/
│
├── pom.xml                                                          ✅
├── chri_online.db                                                   ✅
│
└── src/main/java/com/deep_coding15/mini_projet_Chri_Online/
    │
    ├── Main.java                                                    ✅ (à corriger)
    │
    ├── client/
    │   ├── Client.java                 ← Connexion TCP + thread lecture   ✅
    │   └── vue/                        ← Interface graphique              🔲
    │       ├── FenetreConnexion.java   ← Login / Register                 🔲
    │       ├── FenetreProduits.java    ← Catalogue produits               🔲
    │       ├── FenetrePanier.java      ← Panier + checkout                🔲
    │       └── FenetreAdmin.java       ← Espace administrateur            🔲
    │
    └── serveur/
        ├── Serveur.java                ← ServerSocket port 1234           ✅
        ├── GestionnaireClient.java     ← Thread par client + protocole    ✅
        ├── NotificationUDP.java        ← Envoi notifications UDP          🔲
        │
        ├── modeles/
        │   ├── Utilisateur.java        ← id, email, password, role        ✅
        │   ├── Produit.java            ← id, nom, prix, stock, categorie  🔲
        │   ├── Categorie.java          ← id, nom                          🔲
        │   ├── Panier.java             ← lignes, calculerTotal()          🔲
        │   ├── LignePanier.java        ← produit, quantite                🔲
        │   ├── Commande.java           ← id unique, statut, lignes        🔲
        │   ├── LigneCommande.java      ← snapshot produit + quantite      🔲
        │   ├── StatutCommande.java     ← enum EN_ATTENTE/VALIDEE/...      🔲
        │   └── Paiement.java           ← montant, methode, estValide      🔲
        │
        └── dao/
            ├── ConnexionBD.java        ← JDBC vers chri_online.db         ✅
            ├── UtilisateurDAO.java     ← CRUD utilisateurs                ✅
            ├── ProduitDAO.java         ← CRUD produits + stock            🔲
            └── CommandeDAO.java        ← CRUD commandes + statuts         🔲
```

---

## Protocole de communication (à compléter au fil du projet)

Toutes les commandes suivent le format : `COMMANDE:param1:param2`

### Authentification ✅ Implémenté

| Commande | Paramètres | Réponse succès | Réponse échec |
|----------|-----------|----------------|---------------|
| `REGISTER` | `email:password` | `REGISTER_OK:id:CLIENT` | `REGISTER_FAILED:email:message` |
| `LOGIN` | `email:password` | `LOGIN_OK:id:role` | `LOGIN_FAIL:message` |
| `LOGOUT` | — | `LOGOUT_OK` | — |
| `GET_USER` | `email` | `USER_INFO:email:role` | `USER_NOT_FOUND` |

### Produits 🔲 À implémenter

| Commande | Paramètres | Réponse succès | Réponse échec |
|----------|-----------|----------------|---------------|
| `GET_PRODUITS` | — | `PRODUITS_OK:[json]` | `PRODUITS_FAIL:message` |
| `GET_PRODUIT` | `idProduit` | `PRODUIT_OK:id:nom:prix:stock` | `PRODUIT_NOT_FOUND` |
| `ADD_PRODUIT` | `nom:prix:stock:categorieId` | `ADD_PRODUIT_OK:id` | `ADD_PRODUIT_FAIL:message` |
| `UPDATE_PRODUIT` | `id:nom:prix:stock` | `UPDATE_PRODUIT_OK` | `UPDATE_PRODUIT_FAIL` |
| `DELETE_PRODUIT` | `idProduit` | `DELETE_PRODUIT_OK` | `DELETE_PRODUIT_FAIL` |

### Panier 🔲 À implémenter

| Commande | Paramètres | Réponse succès | Réponse échec |
|----------|-----------|----------------|---------------|
| `ADD_CART` | `idProduit:quantite` | `CART_OK:total` | `CART_FAIL:message` |
| `REMOVE_CART` | `idProduit` | `CART_OK:total` | `CART_FAIL:message` |
| `GET_CART` | — | `CART_INFO:[lignes]:total` | — |
| `CLEAR_CART` | — | `CART_CLEARED` | — |

### Commandes & Paiement 🔲 À implémenter

| Commande | Paramètres | Réponse succès | Réponse échec |
|----------|-----------|----------------|---------------|
| `CHECKOUT` | `adresse:methodePaiement` | `COMMANDE_OK:idCommande:total` | `CHECKOUT_FAIL:message` |
| `GET_COMMANDES` | — | `COMMANDES_OK:[liste]` | — |
| `GET_COMMANDE` | `idCommande` | `COMMANDE_INFO:[détails]` | `COMMANDE_NOT_FOUND` |
| `UPDATE_STATUT` | `idCommande:statut` | `STATUT_OK` | `STATUT_FAIL:message` |

---

## Répartition des tâches (2 membres)

> **Règle du cahier des charges** : chaque membre a des tâches **indépendantes**, sans chevauchement. Chacun présente individuellement sa contribution lors de la soutenance.

---

## Conventions de code à respecter

Pour que le projet reste cohérent entre les deux membres :

**Nommage des packages**
```
com.deep_coding15.mini_projet_Chri_Online.serveur          ← tout ce qui est côté serveur
com.deep_coding15.mini_projet_Chri_Online.serveur.dao      ← accès base de données
com.deep_coding15.mini_projet_Chri_Online.serveur.modeles  ← classes métier
com.deep_coding15.mini_projet_Chri_Online.client           ← tout ce qui est côté client
com.deep_coding15.mini_projet_Chri_Online.client.vue       ← interfaces graphiques
```

**Logging**
- Utiliser `LogManager.getLogger(NomDeLaClasse.class)` dans chaque classe
- `logger.info()` pour les événements normaux
- `logger.debug()` pour les détails de débogage
- `logger.error()` pour les erreurs

**Gestion des erreurs**
- Toujours utiliser des `PreparedStatement` (jamais de concaténation SQL directe)
- Envelopper les accès BD dans des blocs `try-with-resources`
- Propager les `SQLException` ou les logger selon le contexte

---

## Commandes Git pour l'équipe

```bash
# Récupérer les modifications de l'autre membre
git pull origin main

# Créer une branche pour sa fonctionnalité
git checkout -b feature/produits-dao      
git checkout -b feature/fenetre-connexion  

# Commiter son travail
git add .
git commit -m "feat: ajout ProduitDAO avec CRUD complet"

# Pousser sa branche
git push origin feature/produits-dao

# Fusionner dans main 
git checkout main
git merge feature/produits-dao
```