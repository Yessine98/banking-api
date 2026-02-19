# 📖 GUIDE D'IMPLÉMENTATION - Banking API

## 🎯 Plan d'Action (5 Jours)

### **JOUR 1 : Setup & Modèles (2-3h)**

#### 1. Installer les outils nécessaires
```bash
# Vérifier Java
java -version  # Doit être 17+

# Vérifier Maven
mvn -version

# Installer PostgreSQL si nécessaire
# Windows: https://www.postgresql.org/download/windows/
# Mac: brew install postgresql
```

#### 2. Créer le projet
- Option A: Utiliser Spring Initializr (https://start.spring.io/)
  - Project: Maven
  - Language: Java
  - Spring Boot: 3.2.0
  - Java: 17
  - Dependencies: Web, JPA, PostgreSQL, Security, Lombok, Validation

- Option B: Cloner ma structure (recommandé)
```bash
# Utilise les fichiers que je t'ai créés
```

#### 3. Créer la base de données
```sql
-- Dans pgAdmin ou psql
CREATE DATABASE banking_db;
```

#### 4. Tester le démarrage
```bash
mvn spring-boot:run
```

✅ **Checkpoint Jour 1**: L'application démarre sans erreur

---

### **JOUR 2 : Entities & Repositories (3-4h)**

#### 1. Créer les entités (models/)
- ✅ Customer.java (déjà créé)
- ✅ Account.java (déjà créé)
- ✅ Transaction.java (déjà créé)
- ✅ User.java (déjà créé)

#### 2. Créer les repositories
- ✅ CustomerRepository.java
- ✅ AccountRepository.java
- ✅ TransactionRepository.java
- ✅ UserRepository.java

#### 3. Tester avec H2 (base de données en mémoire)
```properties
# Dans application.properties (temporaire)
spring.datasource.url=jdbc:h2:mem:testdb
spring.h2.console.enabled=true
```

✅ **Checkpoint Jour 2**: Tables créées automatiquement par Hibernate

---

### **JOUR 3 : Services & Controllers de Base (4-5h)**

#### 1. Créer CustomerService
```java
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    
    // Méthodes CRUD
    public Customer createCustomer(CustomerDTO dto) { ... }
    public List<Customer> getAllCustomers() { ... }
    public Customer getCustomerById(Long id) { ... }
    public Customer updateCustomer(Long id, CustomerDTO dto) { ... }
    public void deleteCustomer(Long id) { ... }
}
```

#### 2. Créer CustomerController
```java
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;
    
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() { ... }
    
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CustomerDTO dto) { ... }
    
    // Autres endpoints...
}
```

#### 3. Tester avec Postman
```bash
POST http://localhost:8080/api/customers
{
  "firstName": "Ahmed",
  "lastName": "Ben Ali",
  "email": "ahmed@example.com",
  "phoneNumber": "12345678",
  "address": "Tunis"
}
```

✅ **Checkpoint Jour 3**: CRUD clients fonctionne

---

### **JOUR 4 : Comptes & Transactions (4-5h)**

#### 1. AccountService
```java
@Service
public class AccountService {
    @Transactional
    public Account createAccount(AccountDTO dto) {
        // Créer compte
        // Générer numéro de compte unique
        // Associer au client
    }
    
    public BigDecimal getBalance(String accountNumber) { ... }
    public List<Account> getCustomerAccounts(Long customerId) { ... }
}
```

#### 2. TransactionService (IMPORTANT!)
```java
@Service
public class TransactionService {
    
    @Transactional  // TRÈS IMPORTANT pour la cohérence!
    public Transaction deposit(String accountNumber, BigDecimal amount) {
        // 1. Vérifier compte existe
        // 2. Vérifier montant > 0
        // 3. Ajouter au solde
        // 4. Créer transaction
        // 5. Sauvegarder tout
    }
    
    @Transactional
    public Transaction withdraw(String accountNumber, BigDecimal amount) {
        // 1. Vérifier compte existe
        // 2. Vérifier solde suffisant
        // 3. Retirer du solde
        // 4. Créer transaction
    }
    
    @Transactional
    public Transaction transfer(String fromAccount, String toAccount, BigDecimal amount) {
        // 1. Retirer de compte source
        // 2. Ajouter au compte destination
        // 3. Créer 2 transactions
    }
}
```

#### 3. Tester les transactions
```bash
# Dépôt
POST http://localhost:8080/api/transactions/deposit
{
  "accountNumber": "ACC123",
  "amount": 1000,
  "description": "Dépôt initial"
}

# Vérifier solde
GET http://localhost:8080/api/accounts/ACC123
```

✅ **Checkpoint Jour 4**: Dépôt, retrait, transfert fonctionnent

---

### **JOUR 5 : Sécurité JWT & Finitions (4-5h)**

#### 1. JWT Configuration
```java
@Component
public class JwtUtils {
    private String jwtSecret = "YourSecretKey";
    private int jwtExpiration = 86400000; // 24h
    
    public String generateToken(String username) { ... }
    public boolean validateToken(String token) { ... }
    public String getUsernameFromToken(String token) { ... }
}
```

#### 2. Security Config
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
            .csrf().disable()
            .authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
```

#### 3. AuthController
```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO dto) { ... }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) { ... }
}
```

✅ **Checkpoint Jour 5**: Authentification JWT fonctionne

---

## 🚀 DÉPLOIEMENT SUR GITHUB

### 1. Initialiser Git
```bash
cd banking-api
git init
git add .
git commit -m "Initial commit: Banking API with Spring Boot"
```

### 2. Créer repo sur GitHub
- Aller sur github.com
- New Repository → "banking-api"
- **Public** (important pour que BIAT puisse voir)

### 3. Pusher le code
```bash
git remote add origin https://github.com/bryessine/banking-api.git
git branch -M main
git push -u origin main
```

### 4. Améliorer le README
- Ajouter des screenshots Swagger
- Ajouter badge "Build Passing"
- Bien expliquer les fonctionnalités

---

## 📋 CHECKLIST FINALE

Avant d'envoyer le lien à BIAT, vérifie que :

- [ ] Le code compile sans erreur
- [ ] README.md est complet et clair
- [ ] Swagger est accessible et fonctionnel
- [ ] Tous les endpoints principaux marchent
- [ ] Le code est commenté (pas trop, mais aux endroits clés)
- [ ] .gitignore exclut target/, .idea/, etc.
- [ ] LICENSE file ajouté (MIT)
- [ ] Pas de mots de passe en dur dans le code

---

## 💡 CONSEILS IMPORTANTS

### DO ✅
- Commenter les parties complexes (surtout les transactions)
- Utiliser @Transactional pour les opérations critiques
- Valider toutes les entrées avec @Valid
- Gérer les exceptions proprement
- Suivre les conventions de nommage Java
- README professionnel et détaillé

### DON'T ❌
- Ne pas copier-coller du code sans comprendre
- Ne pas mettre de vrais mots de passe dans application.properties
- Ne pas commit le dossier target/
- Ne pas oublier @Transactional sur les opérations bancaires
- Ne pas laisser des System.out.println() partout

---

## 🎓 RESSOURCES D'APPRENTISSAGE

**Tutoriels Spring Boot :**
- Spring Boot Official: https://spring.io/guides
- Baeldung Spring: https://www.baeldung.com/spring-boot
- YouTube: "Spring Boot Full Course" (Amigoscode, Java Brains)

**Spring Data JPA :**
- https://www.baeldung.com/spring-data-jpa-tutorial

**Spring Security & JWT :**
- https://www.bezkoder.com/spring-boot-jwt-authentication/

---

## 📞 BESOIN D'AIDE ?

Si tu bloques sur quelque chose :
1. Cherche l'erreur sur Google (95% des erreurs Spring Boot sont déjà documentées)
2. StackOverflow
3. Documentation officielle Spring
4. Reviens vers moi si vraiment bloqué

**BON COURAGE ! Tu peux le faire ! 💪**
