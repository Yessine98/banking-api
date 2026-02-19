# ⚡ QUICK START - 30 MINUTES POUR ÊTRE OPÉRATIONNEL

## 🎯 OBJECTIF
Avoir un projet Spring Boot fonctionnel sur GitHub en 30 minutes pour impressionner BIAT.

---

## ✅ CHECKLIST RAPIDE

### ÉTAPE 1 : SETUP (5 min)
- [ ] Java 17 installé (`java -version`)
- [ ] Maven installé (`mvn -version`)
- [ ] PostgreSQL installé et démarré
- [ ] IDE (IntelliJ IDEA Community / VS Code)
- [ ] Compte GitHub créé

### ÉTAPE 2 : CRÉER LA BASE DE DONNÉES (2 min)
```sql
-- Dans pgAdmin ou psql
CREATE DATABASE banking_db;
```

### ÉTAPE 3 : IMPORTER LE PROJET (3 min)
1. Extraire le fichier `banking-api-springboot-project.zip`
2. Ouvrir dans ton IDE
3. Modifier `application.properties` si besoin :
   ```properties
   spring.datasource.username=ton_user
   spring.datasource.password=ton_password
   ```

### ÉTAPE 4 : TESTER LOCALEMENT (5 min)
```bash
# Dans le terminal du projet
mvn clean install
mvn spring-boot:run
```

Vérifier : `http://localhost:8080/swagger-ui.html`

- [ ] L'application démarre sans erreur
- [ ] Swagger s'affiche correctement
- [ ] Tables créées dans la base de données

### ÉTAPE 5 : PUSHER SUR GITHUB (10 min)

#### 1. Créer le repo sur GitHub
- Aller sur github.com
- **New Repository**
- Nom : `banking-api`
- Description : "RESTful Banking API with Spring Boot - Portfolio Project"
- **Public** ✅
- **Ne pas** initialiser avec README (on a déjà le nôtre)

#### 2. Pusher le code
```bash
cd banking-api

# Initialiser Git
git init
git add .
git commit -m "Initial commit: Banking API with Spring Boot

- REST API for banking operations (deposit, withdraw, transfer)
- JWT authentication and security
- PostgreSQL database with JPA
- Swagger API documentation
- Complete README and implementation guide"

# Connecter à GitHub
git remote add origin https://github.com/bryessine/banking-api.git
git branch -M main
git push -u origin main
```

#### 3. Vérifier sur GitHub
- [ ] README.md s'affiche bien
- [ ] Structure du projet visible
- [ ] Pas de dossier `target/` committé
- [ ] .gitignore fonctionne

### ÉTAPE 6 : AMÉLIORER LE README (5 min)

Ajouter en haut du README.md (sur GitHub directement ou localement) :

```markdown
## 🎥 Demo
![API Demo](https://via.placeholder.com/800x400?text=Banking+API+Demo)

## ⚡ Quick Start
\`\`\`bash
# Clone
git clone https://github.com/bryessine/banking-api.git
cd banking-api

# Configure PostgreSQL
createdb banking_db

# Run
mvn spring-boot:run
\`\`\`

Access Swagger: http://localhost:8080/swagger-ui.html
```

---

## 📧 PRÉPARER L'EMAIL POUR BIAT

### Option A : Email Court (Recommandé)
```
Objet: Candidature Développeur - Projet Spring Boot Banking API

Bonjour,

Suite à notre échange, voici le lien vers mon projet Spring Boot :
https://github.com/bryessine/banking-api

Il s'agit d'une API bancaire complète avec :
- Gestion de comptes et transactions
- Authentification JWT
- Architecture Spring Boot professionnelle
- Documentation complète

Le README contient toutes les instructions pour le tester.

Bien cordialement,
Yassine Ben Rejeb
```

### Option B : Email Avec Contexte
```
Objet: Candidature Développeur - Projet Spring Boot Banking API

Bonjour,

Suite à notre échange concernant le poste de développeur, j'ai développé 
un projet Spring Boot pour démontrer ma montée en compétences sur cette 
technologie.

🔗 GitHub: https://github.com/bryessine/banking-api

Le projet est une API RESTful complète pour des opérations bancaires :
✅ Spring Boot 3 + JPA + PostgreSQL
✅ Sécurité JWT
✅ Gestion transactionnelle (dépôt, retrait, transfert)
✅ Documentation Swagger
✅ Code structuré et commenté

Bien que mon expérience professionnelle soit principalement sur Node.js/React, 
ce projet démontre ma capacité à apprendre rapidement de nouvelles technologies 
et à produire du code de qualité professionnelle.

Je reste à votre disposition pour toute question.

Cordialement,
Yassine Ben Rejeb
+216 54 190 235
```

---

## 🎯 APRÈS L'ENVOI

### Jour 1-2 :
- Continue à améliorer le projet
- Ajoute des commentaires dans le code
- Crée 2-3 commits supplémentaires (montre que c'est actif)

### Exemples de commits à faire :
```bash
git commit -m "docs: improve README with better examples"
git commit -m "refactor: add more comments in TransactionService"
git commit -m "feat: add account statistics endpoint"
```

### Jour 3-5 :
- Si pas de réponse → relance polie
- Prépare-toi pour l'entretien technique

---

## 💡 QUESTIONS QU'ILS POURRAIENT POSER

### Sur le projet :
1. **"Pourquoi Spring Boot plutôt que Node.js ?"**
   → "Je voulais élargir mes compétences vers les technologies enterprise Java, très utilisées dans le secteur bancaire."

2. **"Comment gérez-vous les transactions ?"**
   → "@Transactional assure l'atomicité. Si une opération échoue, tout est rollback automatiquement."

3. **"Comment sécurisez-vous l'API ?"**
   → "JWT pour l'authentification, Spring Security pour l'autorisation, BCrypt pour les mots de passe."

4. **"Avez-vous fait des tests ?"**
   → "Le projet a des tests manuels via Swagger. Les tests unitaires JUnit sont une amélioration future prévue."

### Sur ta motivation :
1. **"Pourquoi BIAT ?"**
   → "Leader bancaire, transformation digitale, opportunité de travailler sur des systèmes critiques."

2. **"Pourquoi passer de Sanofi à une banque ?"**
   → "Intérêt pour les challenges du secteur financier, technologies enterprise, impact business direct."

---

## ⚠️ DERNIERS CHECKS AVANT D'ENVOYER

- [ ] Le projet compile et démarre sans erreur
- [ ] README.md est complet et clair
- [ ] Pas de mots de passe en dur dans le code
- [ ] .gitignore exclut target/ et .idea/
- [ ] GitHub repo est **Public**
- [ ] Commits ont des messages clairs
- [ ] Ton email/téléphone sont dans le README

---

## 🚀 C'EST PARTI !

Tu as maintenant tout ce qu'il faut pour :
1. ✅ Montrer à BIAT que tu apprends vite
2. ✅ Démontrer tes compétences backend
3. ✅ Te démarquer des autres candidats
4. ✅ Avoir un sujet de discussion technique en entretien

**BON COURAGE ! TU VAS ASSURER ! 💪**

---

## 📞 BESOIN D'AIDE RAPIDE ?

**Problème de compilation ?**
→ `mvn clean install -U` (force le téléchargement des dépendances)

**Problème de base de données ?**
→ Vérifie que PostgreSQL tourne : `pg_isready`

**Port 8080 déjà utilisé ?**
→ Change dans application.properties : `server.port=8081`

**Erreur Git push ?**
→ Configure ton identité : 
```bash
git config --global user.email "benrejeb98@gmail.com"
git config --global user.name "Yassine Ben Rejeb"
```
