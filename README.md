# 🏦 Banking API - RESTful Banking System

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)](https://spring.io/projects/spring-boot)
[![H2](https://img.shields.io/badge/H2-Dev-blue)](https://www.h2database.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Production-blue)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 À Propos

API RESTful complète pour la gestion d'opérations bancaires, développée avec Spring Boot. Ce projet démontre une architecture backend moderne avec sécurité JWT, validation des données, et gestion transactionnelle.

**Auteur:** Yassine Ben Rejeb  
**Stack:** Java 17, Spring Boot 3, H2/PostgreSQL, Spring Security, JWT

## 🎯 Objectif du Projet

Ce projet a été développé dans le cadre de ma montée en compétences sur Spring Boot et pour démontrer ma capacité à :

- Concevoir des API REST sécurisées et scalables
- Implémenter une architecture en couches (Controller, Service, Repository)
- Gérer des transactions bancaires avec intégrité des données
- Mettre en place une authentification JWT
- Appliquer les meilleures pratiques de développement Spring Boot

## ✨ Fonctionnalités

### 🔐 Authentification & Sécurité

- [x] Authentification JWT
- [x] Gestion des rôles (USER, ADMIN)
- [x] Endpoints sécurisés

### 👥 Gestion des Clients

- [x] Créer un client
- [x] Consulter la liste des clients (avec pagination)
- [x] Rechercher des clients par nom/email
- [x] Consulter un client par ID
- [x] Mettre à jour les informations client
- [x] Supprimer un client

### 💳 Gestion des Comptes

- [x] Créer un compte (Épargne / Courant)
- [x] Consulter les comptes d'un client
- [x] Consulter le solde d'un compte
- [x] Changer le statut d'un compte (Actif, Suspendu, Fermé)

### 💰 Opérations Bancaires

- [x] Dépôt d'argent
- [x] Retrait d'argent
- [x] Transfert entre comptes
- [x] Consultation de l'historique des transactions (avec filtres)
- [x] Filtrage par date et type de transaction

### 📊 Autres

- [x] Validation des données
- [x] Gestion des exceptions personnalisées
- [x] Documentation API avec Swagger/OpenAPI
- [x] Annotations Swagger détaillées
- [x] Pagination des résultats
- [x] Contrôle d'accès basé sur les rôles (RBAC)
- [x] Logs détaillés

## 🛠️ Technologies Utilisées

| Technologie     | Version | Usage                           |
| --------------- | ------- | ------------------------------- |
| Java            | 17      | Langage de programmation        |
| Spring Boot     | 3.2.0   | Framework backend               |
| Spring Data JPA | 3.2.0   | Couche d'accès aux données      |
| Spring Security | 3.2.0   | Sécurité et authentification    |
| JWT             | 0.11.5  | Token d'authentification        |
| H2              | 2.x     | Base de données (développement) |
| PostgreSQL      | 15+     | Base de données (production)    |
| Lombok          | Latest  | Réduction du boilerplate code   |
| Swagger/OpenAPI | 2.2.0   | Documentation API               |
| Maven           | 3.8+    | Gestion des dépendances         |

## 📦 Installation & Démarrage

### Prérequis

```bash
- Java 17 ou supérieur
- Maven 3.8+
- Git
# PostgreSQL 15+ (optionnel - pour la production)
```

### 1. Cloner le projet

```bash
git clone https://github.com/bryessine/banking-api.git
cd banking-api
```

### 2. Configurer la base de données

**Option A: H2 (défaut - aucune configuration requise)**

L'application utilise H2 en mémoire par défaut. La console H2 est accessible sur : `http://localhost:8080/h2-console`

**Option B: PostgreSQL (production)**

Créer une base de données PostgreSQL :

```sql
CREATE DATABASE banking_db;
```

Modifier `src/main/resources/application.properties` :

```properties
# Commenter H2 et décommenter PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/banking_db
spring.datasource.username=votre_username
spring.datasource.password=votre_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

### 3. Compiler et lancer l'application

```bash
# Compiler le projet
mvn clean install

# Lancer l'application
mvn spring-boot:run
```

L'API sera accessible sur : `http://localhost:8080`

### 4. Accéder à la documentation Swagger

Ouvrir dans le navigateur : `http://localhost:8080/swagger-ui.html`

## 🔑 Endpoints Principaux

### Authentification

```http
POST /api/auth/register    # S'inscrire
POST /api/auth/login        # Se connecter
```

### Clients

```http
GET    /api/customers           # Liste des clients
GET    /api/customers/{id}      # Client par ID
POST   /api/customers           # Créer un client
PUT    /api/customers/{id}      # Modifier un client
DELETE /api/customers/{id}      # Supprimer un client
```

### Comptes

```http
GET  /api/accounts                    # Tous les comptes
GET  /api/accounts/{accountNumber}    # Compte par numéro
GET  /api/accounts/customer/{id}      # Comptes d'un client
POST /api/accounts                     # Créer un compte
```

### Transactions

```http
POST /api/transactions/deposit        # Faire un dépôt
POST /api/transactions/withdraw       # Faire un retrait
POST /api/transactions/transfer       # Faire un transfert
GET  /api/transactions/account/{accountNumber}  # Historique
```

## 📝 Exemples d'Utilisation

### Créer un client

```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "firstName": "Ahmed",
    "lastName": "Ben Ali",
    "email": "ahmed.benali@example.com",
    "phoneNumber": "+216 12 345 678",
    "address": "Tunis, Tunisia"
  }'
```

### Faire un dépôt

```bash
curl -X POST http://localhost:8080/api/transactions/deposit \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "accountNumber": "ACC1234567890",
    "amount": 1000.00,
    "description": "Dépôt initial"
  }'
```

## 🏗️ Architecture du Projet

```
banking-api/
├── src/
│   ├── main/
│   │   ├── java/com/yassine/bankingapi/
│   │   │   ├── config/           # Configuration (Security, JWT)
│   │   │   ├── controller/       # REST Controllers
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── exception/        # Custom Exceptions
│   │   │   ├── model/            # Entities JPA
│   │   │   ├── repository/       # JPA Repositories
│   │   │   ├── security/         # JWT & Security
│   │   │   ├── service/          # Business Logic
│   │   │   └── BankingApiApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/                     # Tests unitaires
├── pom.xml
└── README.md
```

## 🔒 Sécurité

- **Authentification JWT** : Tous les endpoints (sauf /auth/\*) nécessitent un token JWT valide
- **Validation des données** : Utilisation de `@Valid` et annotations Jakarta Validation
- **Gestion des exceptions** : Handler global pour les erreurs
- **Transactions** : Gestion transactionnelle des opérations bancaires
- **Hashage des mots de passe** : BCrypt pour le stockage sécurisé

## 🧪 Tests

```bash
# Lancer tous les tests
mvn test

# Lancer avec couverture de code
mvn test jacoco:report
```

## 📚 Apprentissages & Compétences Démontrées

À travers ce projet, j'ai mis en pratique :

✅ **Spring Boot & JPA**

- Configuration et structure d'une application Spring Boot
- Mapping objet-relationnel avec JPA/Hibernate
- Relations entre entités (@OneToMany, @ManyToOne)
- Requêtes personnalisées avec Spring Data JPA

✅ **API REST & Bonnes Pratiques**

- Design d'API RESTful
- Gestion des codes HTTP appropriés
- Validation des données entrantes
- Documentation API avec Swagger

✅ **Sécurité**

- Implémentation de Spring Security
- Authentification JWT
- Gestion des rôles et permissions

✅ **Base de Données**

- Modélisation de données bancaires
- Gestion des transactions ACID
- Migrations de schéma

✅ **Gestion d'Erreurs**

- Exceptions personnalisées
- Handler global d'exceptions
- Messages d'erreur clairs

## 🚀 Améliorations Futures

- [ ] Ajout de tests unitaires et d'intégration complets
- [ ] Génération de relevés PDF
- [ ] Système de notifications (email/SMS)
- [ ] API de taux de change
- [ ] Rate limiting
- [ ] Audit logging
- [ ] Déploiement Docker
- [ ] CI/CD avec GitHub Actions

## 👤 Contact

**Yassine Ben Rejeb**

- Email: benrejeb98@gmail.com
- GitHub: [@bryessine](https://github.com/bryessine)
- LinkedIn: [Votre profil LinkedIn]
- Téléphone: +216 54 190 235

## 📄 Licence

Ce projet est sous licence MIT - voir le fichier [LICENSE](LICENSE) pour plus de détails.

---

⭐ Si ce projet vous a été utile, n'hésitez pas à lui donner une étoile !

**Développé avec ❤️ par Yassine Ben Rejeb**
