# 📡 EXEMPLES D'APPELS API

## 🔐 AUTHENTIFICATION

### S'inscrire
```bash
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "yassine",
  "email": "yassine@example.com",
  "password": "Password123!"
}
```

### Se connecter
```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "yassine",
  "password": "Password123!"
}

# Réponse:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "yassine",
  "email": "yassine@example.com"
}
```

## 👥 GESTION DES CLIENTS

### Créer un client
```bash
POST http://localhost:8080/api/customers
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "firstName": "Ahmed",
  "lastName": "Ben Ali",
  "email": "ahmed.benali@example.com",
  "phoneNumber": "+216 12 345 678",
  "address": "Avenue Habib Bourguiba, Tunis"
}
```

### Récupérer tous les clients
```bash
GET http://localhost:8080/api/customers
Authorization: Bearer YOUR_JWT_TOKEN
```

### Récupérer un client par ID
```bash
GET http://localhost:8080/api/customers/1
Authorization: Bearer YOUR_JWT_TOKEN
```

### Modifier un client
```bash
PUT http://localhost:8080/api/customers/1
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "firstName": "Ahmed",
  "lastName": "Ben Ali",
  "email": "ahmed.benali@example.com",
  "phoneNumber": "+216 98 765 432",
  "address": "Nouvelle adresse, Tunis"
}
```

### Supprimer un client
```bash
DELETE http://localhost:8080/api/customers/1
Authorization: Bearer YOUR_JWT_TOKEN
```

## 💳 GESTION DES COMPTES

### Créer un compte épargne
```bash
POST http://localhost:8080/api/accounts
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "customerId": 1,
  "accountType": "SAVINGS",
  "initialBalance": 5000.00
}
```

### Créer un compte courant
```bash
POST http://localhost:8080/api/accounts
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "customerId": 1,
  "accountType": "CURRENT",
  "initialBalance": 1000.00
}
```

### Récupérer un compte par numéro
```bash
GET http://localhost:8080/api/accounts/ACC1708534567890
Authorization: Bearer YOUR_JWT_TOKEN
```

### Récupérer les comptes d'un client
```bash
GET http://localhost:8080/api/accounts/customer/1
Authorization: Bearer YOUR_JWT_TOKEN
```

### Consulter le solde
```bash
GET http://localhost:8080/api/accounts/ACC1708534567890/balance
Authorization: Bearer YOUR_JWT_TOKEN

# Réponse:
{
  "accountNumber": "ACC1708534567890",
  "balance": 5000.00,
  "currency": "TND"
}
```

## 💰 OPÉRATIONS BANCAIRES

### Faire un dépôt
```bash
POST http://localhost:8080/api/transactions/deposit
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "accountNumber": "ACC1708534567890",
  "amount": 1500.00,
  "description": "Dépôt mensuel salaire"
}

# Réponse:
{
  "id": 1,
  "transactionReference": "TXN1708534567891",
  "type": "DEPOSIT",
  "amount": 1500.00,
  "balanceAfter": 6500.00,
  "description": "Dépôt mensuel salaire",
  "createdAt": "2024-02-15T10:30:00"
}
```

### Faire un retrait
```bash
POST http://localhost:8080/api/transactions/withdraw
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "accountNumber": "ACC1708534567890",
  "amount": 500.00,
  "description": "Retrait DAB"
}

# Réponse:
{
  "id": 2,
  "transactionReference": "TXN1708534567892",
  "type": "WITHDRAWAL",
  "amount": 500.00,
  "balanceAfter": 6000.00,
  "description": "Retrait DAB",
  "createdAt": "2024-02-15T14:20:00"
}
```

### Faire un transfert
```bash
POST http://localhost:8080/api/transactions/transfer
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "fromAccountNumber": "ACC1708534567890",
  "toAccountNumber": "ACC1708534567891",
  "amount": 300.00,
  "description": "Virement interne"
}

# Réponse:
{
  "sourceTransaction": {
    "id": 3,
    "transactionReference": "TXN1708534567893",
    "type": "TRANSFER",
    "amount": 300.00,
    "balanceAfter": 5700.00,
    "destinationAccountNumber": "ACC1708534567891",
    "description": "Virement interne",
    "createdAt": "2024-02-15T15:45:00"
  },
  "destinationTransaction": {
    "id": 4,
    "transactionReference": "TXN1708534567894",
    "type": "TRANSFER",
    "amount": 300.00,
    "balanceAfter": 1300.00,
    "description": "Virement reçu de ACC1708534567890",
    "createdAt": "2024-02-15T15:45:00"
  }
}
```

### Consulter l'historique des transactions
```bash
GET http://localhost:8080/api/transactions/account/ACC1708534567890
Authorization: Bearer YOUR_JWT_TOKEN

# Réponse:
[
  {
    "id": 1,
    "transactionReference": "TXN1708534567891",
    "type": "DEPOSIT",
    "amount": 1500.00,
    "balanceAfter": 6500.00,
    "description": "Dépôt mensuel salaire",
    "createdAt": "2024-02-15T10:30:00"
  },
  {
    "id": 2,
    "transactionReference": "TXN1708534567892",
    "type": "WITHDRAWAL",
    "amount": 500.00,
    "balanceAfter": 6000.00,
    "description": "Retrait DAB",
    "createdAt": "2024-02-15T14:20:00"
  },
  {
    "id": 3,
    "transactionReference": "TXN1708534567893",
    "type": "TRANSFER",
    "amount": 300.00,
    "balanceAfter": 5700.00,
    "destinationAccountNumber": "ACC1708534567891",
    "description": "Virement interne",
    "createdAt": "2024-02-15T15:45:00"
  }
]
```

## 📊 STATISTIQUES (Bonus)

### Obtenir les statistiques d'un compte
```bash
GET http://localhost:8080/api/accounts/ACC1708534567890/statistics
Authorization: Bearer YOUR_JWT_TOKEN

# Réponse:
{
  "accountNumber": "ACC1708534567890",
  "currentBalance": 5700.00,
  "totalDeposits": 1500.00,
  "totalWithdrawals": 800.00,
  "totalTransfersOut": 300.00,
  "transactionCount": 3,
  "lastTransactionDate": "2024-02-15T15:45:00"
}
```

## ❌ EXEMPLES D'ERREURS

### Solde insuffisant
```bash
POST http://localhost:8080/api/transactions/withdraw
{
  "accountNumber": "ACC1708534567890",
  "amount": 10000.00
}

# Réponse: 400 Bad Request
{
  "timestamp": "2024-02-15T16:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Solde insuffisant. Solde actuel: 5700.00 TND",
  "path": "/api/transactions/withdraw"
}
```

### Compte non trouvé
```bash
GET http://localhost:8080/api/accounts/ACC9999999999

# Réponse: 404 Not Found
{
  "timestamp": "2024-02-15T16:05:00",
  "status": 404,
  "error": "Not Found",
  "message": "Compte non trouvé: ACC9999999999",
  "path": "/api/accounts/ACC9999999999"
}
```

### Token expiré
```bash
GET http://localhost:8080/api/customers
Authorization: Bearer EXPIRED_TOKEN

# Réponse: 401 Unauthorized
{
  "timestamp": "2024-02-15T16:10:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "JWT token has expired",
  "path": "/api/customers"
}
```

## 💡 NOTES IMPORTANTES

1. **Remplacer `YOUR_JWT_TOKEN`** par le token reçu lors du login
2. **Les montants** sont en dinars tunisiens (TND)
3. **Les numéros de compte** sont générés automatiquement au format `ACC + timestamp`
4. **Les références de transaction** sont générées automatiquement au format `TXN + timestamp`
5. **Tous les endpoints** (sauf /auth/*) nécessitent un token JWT valide

## 🔧 IMPORTER DANS POSTMAN

1. Créer une nouvelle collection "Banking API"
2. Créer une variable d'environnement `baseUrl` = `http://localhost:8080`
3. Créer une variable d'environnement `token` = (votre JWT token)
4. Utiliser `{{baseUrl}}` et `{{token}}` dans vos requêtes

Exemple:
```
GET {{baseUrl}}/api/customers
Authorization: Bearer {{token}}
```
