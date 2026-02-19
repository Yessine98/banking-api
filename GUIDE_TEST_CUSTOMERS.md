# 🧪 GUIDE DE TEST - API Clients

## ✅ CE QUE TU VIENS DE CRÉER

Tu as maintenant une **API REST complète** pour gérer les clients avec :
- ✅ CRUD complet (Create, Read, Update, Delete)
- ✅ Validation des données
- ✅ Gestion d'erreurs professionnelle
- ✅ DTOs pour séparer les données

---

## 🚀 ÉTAPE 1 : Redémarrer l'application

```bash
# Arrête l'app si elle tourne (Ctrl+C)
# Puis relance :
cd banking-api
mvn clean install
mvn spring-boot:run
```

Tu devrais voir :
```
Started BankingApiApplication in X.XXX seconds
```

---

## 📊 ÉTAPE 2 : Accéder à Swagger

Ouvre ton navigateur : `http://localhost:8080/swagger-ui.html`

Tu verras maintenant **customer-controller** avec 5 endpoints :
- `GET /api/customers` - Liste tous les clients
- `GET /api/customers/{id}` - Client par ID
- `POST /api/customers` - Créer un client
- `PUT /api/customers/{id}` - Modifier un client
- `DELETE /api/customers/{id}` - Supprimer un client

---

## 🧪 ÉTAPE 3 : Tester les Endpoints

### ✅ Test 1 : Créer un client

1. Dans Swagger, clique sur **POST /api/customers**
2. Clique sur **Try it out**
3. Entre ce JSON :

```json
{
  "firstName": "Ahmed",
  "lastName": "Ben Ali",
  "email": "ahmed.benali@example.com",
  "phoneNumber": "+216 12 345 678",
  "address": "Avenue Habib Bourguiba, Tunis"
}
```

4. Clique **Execute**

**Résultat attendu :** Code 201 Created
```json
{
  "id": 1,
  "firstName": "Ahmed",
  "lastName": "Ben Ali",
  "email": "ahmed.benali@example.com",
  "phoneNumber": "+216 12 345 678",
  "address": "Avenue Habib Bourguiba, Tunis",
  "createdAt": "2024-02-19T10:30:00",
  "updatedAt": "2024-02-19T10:30:00"
}
```

---

### ✅ Test 2 : Créer un 2e client

```json
{
  "firstName": "Fatma",
  "lastName": "Trabelsi",
  "email": "fatma.trabelsi@example.com",
  "phoneNumber": "+216 98 765 432",
  "address": "Rue de la République, Sfax"
}
```

---

### ✅ Test 3 : Récupérer tous les clients

1. Clique sur **GET /api/customers**
2. **Try it out** → **Execute**

**Résultat :** Liste des 2 clients

---

### ✅ Test 4 : Récupérer un client par ID

1. Clique sur **GET /api/customers/{id}**
2. Entre `id = 1`
3. **Execute**

**Résultat :** Le client Ahmed

---

### ✅ Test 5 : Modifier un client

1. Clique sur **PUT /api/customers/{id}**
2. Entre `id = 1`
3. Modifie le JSON :

```json
{
  "firstName": "Ahmed",
  "lastName": "Ben Ali",
  "email": "ahmed.benali@example.com",
  "phoneNumber": "+216 55 555 555",
  "address": "Nouvelle adresse, Tunis"
}
```

4. **Execute**

**Résultat :** Client mis à jour

---

### ✅ Test 6 : Tester la validation (erreur volontaire)

Essaye de créer un client sans email :

```json
{
  "firstName": "Test",
  "lastName": "Test",
  "email": "",
  "phoneNumber": "12345",
  "address": "Test"
}
```

**Résultat attendu :** Code 400 Bad Request avec message d'erreur

---

### ✅ Test 7 : Tester email en double (erreur volontaire)

Essaye de créer un client avec un email qui existe déjà :

```json
{
  "firstName": "Autre",
  "lastName": "Personne",
  "email": "ahmed.benali@example.com",
  "phoneNumber": "12345",
  "address": "Test"
}
```

**Résultat attendu :** Code 400 avec "Email already exists"

---

### ✅ Test 8 : Vérifier dans H2 Console

1. Va sur `http://localhost:8080/h2-console`
2. Connecte-toi (JDBC: `jdbc:h2:mem:banking_db`, User: `sa`, Password: vide)
3. Exécute :

```sql
SELECT * FROM CUSTOMERS;
```

Tu devrais voir tes 2 clients dans la base !

---

### ✅ Test 9 : Supprimer un client

1. Clique sur **DELETE /api/customers/{id}**
2. Entre `id = 2`
3. **Execute**

**Résultat :** Code 204 No Content

4. Vérifie avec `GET /api/customers` → plus qu'1 client

---

## 🎯 CHECKPOINT : Si tout marche

Tu devrais avoir :
- ✅ Créé 2 clients
- ✅ Récupéré la liste
- ✅ Récupéré 1 client par ID
- ✅ Modifié un client
- ✅ Testé les validations
- ✅ Supprimé un client
- ✅ Vérifié dans H2

**FÉLICITATIONS ! Tu as une API REST fonctionnelle ! 🎉**

---

## 📸 FAIRE DES SCREENSHOTS POUR GITHUB

Prends des screenshots de :
1. Swagger UI montrant les endpoints
2. Un exemple de réponse réussie
3. H2 Console avec les données

Tu les ajouteras au README pour impressionner BIAT !

---

## 🚀 PROCHAINES ÉTAPES

Maintenant qu'on a les **Clients**, on va créer :

### **Demain - Jour 2 : Comptes Bancaires**
- AccountDTO
- AccountService
- AccountController
- Lier les comptes aux clients

### **Jour 3 : Transactions**
- TransactionService (dépôt, retrait, transfert)
- TransactionController
- **C'est le CŒUR du projet !**

### **Jour 4-5 : Finitions**
- Améliorer README
- Ajouter commentaires
- Pusher sur GitHub
- Envoyer à BIAT

---

## 💡 SI TU AS UN PROBLÈME

### Erreur de compilation ?
```bash
mvn clean install -U
```

### L'endpoint n'apparaît pas dans Swagger ?
- Redémarre l'app
- Vérifie que la classe a `@RestController`

### Erreur 500 ?
- Regarde les logs dans le terminal
- Copie l'erreur dans Google

---

## 🎊 BRAVO !

Tu viens de créer ta première API REST Spring Boot fonctionnelle !

**Points clés que tu as appris :**
- ✅ Structure en couches (Controller → Service → Repository)
- ✅ DTOs pour séparer les données
- ✅ Validation avec `@Valid`
- ✅ Gestion d'erreurs avec `@RestControllerAdvice`
- ✅ `@Transactional` pour la cohérence des données
- ✅ Mapping entre entités et DTOs

**Continue comme ça ! Tu vas cartonner chez BIAT ! 💪**
