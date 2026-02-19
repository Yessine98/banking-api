# 🚀 DÉMARRAGE RAPIDE - Version Simplifiée

## ⚠️ IMPORTANT : Security Désactivée Temporairement

Pour pouvoir développer tranquillement, j'ai **commenté Spring Security** dans le `pom.xml`.

Ça veut dire :
- ✅ Pas de login/password
- ✅ Accès direct à H2 Console
- ✅ Accès direct à Swagger
- ✅ Tous les endpoints accessibles sans token

**On rajoutera la sécurité JWT plus tard !**

---

## 📋 ÉTAPES DE DÉMARRAGE

### 1. Compiler le projet
```bash
cd banking-api
mvn clean install
```

### 2. Lancer l'application
```bash
mvn spring-boot:run
```

Tu devrais voir :
```
Started BankingApiApplication in X.XXX seconds
```

### 3. Accéder aux interfaces

#### H2 Console (Base de données)
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:banking_db`
- Username: `sa`
- Password: *(laisser vide)*

#### Swagger UI (Documentation API)
- URL: `http://localhost:8080/swagger-ui.html`

---

## 🎯 PROCHAINES ÉTAPES (Phase 1 - Sans Security)

### Jour 1-2 : CRUD Clients
1. Créer `CustomerDTO.java`
2. Créer `CustomerService.java`
3. Créer `CustomerController.java`
4. Tester avec Postman/Swagger

### Jour 3-4 : Comptes & Transactions
1. Créer `AccountService.java`
2. Créer `AccountController.java`
3. Créer `TransactionService.java`
4. Créer `TransactionController.java`

### Jour 5 : Finitions
1. Ajouter gestion d'erreurs
2. Améliorer README
3. Pusher sur GitHub

### Phase 2 (Optionnel - Plus tard)
1. Décommenter Spring Security dans `pom.xml`
2. Ajouter JWT
3. Sécuriser les endpoints

---

## 📝 EXEMPLE DE TEST RAPIDE

Une fois l'app lancée, tu peux tester directement dans H2 Console :

```sql
-- Créer un client
INSERT INTO CUSTOMERS (id, first_name, last_name, email, phone_number, address, created_at, updated_at) 
VALUES (1, 'Ahmed', 'Ben Ali', 'ahmed@example.com', '12345678', 'Tunis', NOW(), NOW());

-- Voir tous les clients
SELECT * FROM CUSTOMERS;

-- Créer un compte
INSERT INTO ACCOUNTS (id, account_number, account_type, balance, status, customer_id, created_at, updated_at)
VALUES (1, 'ACC123456', 'SAVINGS', 1000.00, 'ACTIVE', 1, NOW(), NOW());

-- Voir tous les comptes
SELECT * FROM ACCOUNTS;
```

---

## ✅ AVANTAGES DE CETTE APPROCHE

### Pour Développer :
- ✅ Pas de blocage par la sécurité
- ✅ Focus sur la logique métier d'abord
- ✅ Tests plus rapides
- ✅ Moins de complexité au début

### Pour BIAT :
Tu peux leur dire :
*"J'ai commencé par implémenter la logique métier (CRUD, transactions) avant d'ajouter la sécurité. C'est une approche itérative : d'abord faire fonctionner les features, puis sécuriser. En production, j'ajouterais évidemment Spring Security + JWT dès le départ."*

---

## 🔄 QUAND RÉACTIVER LA SÉCURITÉ ?

Plus tard, quand tu seras prêt :

1. Décommenter dans `pom.xml` :
   - Spring Security
   - JWT dependencies

2. Relancer :
```bash
mvn clean install
mvn spring-boot:run
```

3. Tous les fichiers de sécurité que j'ai créés seront activés automatiquement

---

## 🚀 MAINTENANT, TU PEUX :

1. ✅ Accéder à H2 Console sans mot de passe
2. ✅ Voir la structure des tables
3. ✅ Commencer à créer les Services et Controllers
4. ✅ Tester directement sans JWT

**Redémarre ton application maintenant avec `mvn spring-boot:run` ! 🎉**
