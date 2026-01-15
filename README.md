📚 Système de Gestion des Notes - Architecture SOA


Projet académique - Architecture Orientée Services (SOA) pour la gestion des notes académiques avec génération automatique de relevés de notes en PDF.


🎯 Objectif du Projet
Développer un système de gestion des notes académiques en utilisant une Architecture Orientée Services (SOA) avec :

3 microservices indépendants
Communication REST synchrone
Génération automatique de relevés de notes en PDF
Isolation complète des bases de données


🏗️ Architecture
Diagramme de l'Architecture
┌─────────────────┐
│   Client Web    │
└────────┬────────┘
         │
    ┌────┴─────────────────┬──────────────────┐
    │                      │                  │
┌───▼──────────┐   ┌──────▼───────┐   ┌─────▼──────────┐
│   Student    │◄──┤    Grade     │◄──┤  Certificate   │
│   Service    │   │   Service    │   │    Service     │
│  Port: 8081  │   │  Port: 8082  │   │  Port: 8083    │
└──────┬───────┘   └──────┬───────┘   └────────┬───────┘
       │                  │                     │
   ┌───▼────┐        ┌───▼────┐           ┌───▼────┐
   │MySQL DB│        │MySQL DB│           │MySQL DB│
   │students│        │ grades │           │  certs │
   └────────┘        └────────┘           └────────┘
Services Implémentés
ServicePortResponsabilitéBase de Donnéesstudent-service8081Gestion des dossiers étudiantsstudent_db (MySQL)grade-service8082Saisie et calcul des notes/moyennesgrade_db (MySQL)certificate-service8083Génération de relevés de notes PDFcertificate_db (MySQL)

🚀 Fonctionnalités
Student Service

✅ Créer, modifier, supprimer un étudiant
✅ Lister tous les étudiants
✅ Rechercher par ID ou numéro d'étudiant

Grade Service

✅ Saisir des notes avec coefficients
✅ Calculer automatiquement les moyennes pondérées
✅ Déterminer si l'étudiant est validé (PASSED/FAILED)
✅ Gérer plusieurs semestres

Certificate Service

✅ Générer des relevés de notes en PDF
✅ Communication REST avec Student et Grade Services
✅ Téléchargement des relevés générés
✅ Historique des relevés par étudiant


🛠️ Technologies Utilisées
Backend

Java 17
Spring Boot 3.2.x
Spring Data JPA
RestTemplate (Communication REST)
iText 7 (Génération PDF)

Base de Données

MySQL 8.0
Docker Compose (Orchestration des DBs)

Outils

Maven (Gestion des dépendances)
Git (Versioning)
Postman/cURL (Tests API)


📦 Installation et Démarrage
Prérequis

Java 17 ou supérieur
Maven 3.8+
Docker
Git

Étapes d'Installation
bash# 1. Cloner le projet
git clone https://github.com/VOTRE-USERNAME/gestion-notes-soa.git
cd gestion-notes-soa

# 2. Démarrer les bases de données avec Docker
docker-compose up -d

# 3. Vérifier que les conteneurs tournent
docker ps

# 4. Lancer les services (dans 3 terminaux différents)

# Terminal 1 - Student Service
cd student-service
mvn clean install
mvn spring-boot:run

# Terminal 2 - Grade Service
cd grade-service
mvn clean install
mvn spring-boot:run

# Terminal 3 - Certificate Service
cd certificate-service
mvn clean install
mvn spring-boot:run
Vérification
bash# Vérifier que les services répondent
curl http://localhost:8081/api/students
curl http://localhost:8082/api/grades
curl http://localhost:8083/api/certificates


🧪 Tests

Test Manuel - Scénario Complet

1. Créer un étudiant
bashcurl -X POST http://localhost:8081/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Ahmed",
    "lastName": "Ould Mahmoud",
    "email": "ahmed@example.com",
    "studentNumber": "STU2024001",
    "department": "Informatique"
  }'

3. Ajouter des notes
bash# Mathématiques
curl -X POST http://localhost:8082/api/grades \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 1,
    "subject": "Mathématiques",
    "score": 16.0,
    "coefficient": 3.0,
    "semester": "S1",
    "examType": "Examen"
  }'

# Programmation
curl -X POST http://localhost:8082/api/grades \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 1,
    "subject": "Programmation",
    "score": 18.0,
    "coefficient": 4.0,
    "semester": "S1",
    "examType": "Examen"
  }'
  
3. Calculer la moyenne
bashcurl http://localhost:8082/api/grades/student/1/semester/S1/average
4. Générer le relevé de notes
bashcurl -X POST http://localhost:8083/api/certificates/generate \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 1,
    "semester": "S1"
  }'

6. Télécharger le PDF
bashcurl -o releve.pdf http://localhost:8083/api/certificates/1/download

📡 Documentation API
Endpoints Principaux
Student Service (8081)

POST /api/students - Créer un étudiant
GET /api/students - Liste des étudiants
GET /api/students/{id} - Détails d'un étudiant
PUT /api/students/{id} - Modifier un étudiant
DELETE /api/students/{id} - Supprimer un étudiant

Grade Service (8082)

POST /api/grades - Saisir une note
GET /api/grades/student/{id} - Notes d'un étudiant
GET /api/grades/student/{id}/semester/{sem}/average - Calculer moyenne
PUT /api/grades/{id} - Modifier une note
DELETE /api/grades/{id} - Supprimer une note

Certificate Service (8083)

POST /api/certificates/generate - Générer un relevé
GET /api/certificates/{id}/download - Télécharger le PDF
GET /api/certificates/student/{id} - Relevés d'un étudiant


🔗 Communication Inter-Services
Le Certificate Service communique avec les autres services via REST :
java// Appel à Student Service
StudentDTO student = restTemplate.getForObject(
    "http://localhost:8081/api/students/1", 
    StudentDTO.class
);

// Appel à Grade Service pour les notes
List<GradeDTO> grades = restTemplate.exchange(
    "http://localhost:8082/api/grades/student/1/semester/S1",
    HttpMethod.GET,
    null,
    new ParameterizedTypeReference<List<GradeDTO>>() {}
).getBody();

// Appel à Grade Service pour la moyenne
AverageResultDTO average = restTemplate.getForObject(
    "http://localhost:8082/api/grades/student/1/semester/S1/average",
    AverageResultDTO.class
);

📊 Modèles de Données
Student
json{
  "id": 1,
  "firstName": "Ahmed",
  "lastName": "Ould Mahmoud",
  "email": "ahmed@example.com",
  "studentNumber": "STU2024001",
  "department": "Informatique"
}

Grade
json{
  "id": 1,
  "studentId": 1,
  "subject": "Mathématiques",
  "score": 16.0,
  "coefficient": 3.0,
  "semester": "S1",
  "examType": "Examen"
}

Certificate
json{
  "id": 1,
  "studentId": 1,
  "semester": "S1",
  "generatedDate": "2026-01-10T10:30:00",
  "pdfFileName": "releve_STU2024001_S1_1704885600000.pdf",
  "average": 16.21,
  "status": "PASSED"
}


🐛 Dépannage
Les services ne démarrent pas
bash# Vérifier que les ports sont libres
netstat -an | grep -E "8081|8082|8083|3306|3307|3308"

# Tuer les processus Java si nécessaire
pkill -f java
Base de données non accessible
bash# Redémarrer les conteneurs Docker
docker-compose down
docker-compose up -d

# Vérifier les logs
docker logs student-db
PDF non généré
bash# Créer le dossier certificates
mkdir -p certificates

# Vérifier les permissions
chmod 755 certificates

