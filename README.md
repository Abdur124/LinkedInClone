LinkedInClone
A scalable backend system inspired by LinkedIn, built using Java, Spring Boot, Kafka, Microservices architecture, and fully containerized using Docker.

🚀 Key Features
User authentication and profile management
Send & accept connection requests
Asynchronous notification service using Kafka
Event-driven communication between services
Real-time chat using WebSocket (chat-service)
Clean separation of concerns using microservices
Fully containerized services using Docker and Docker Compose
Reproducible local setup with zero manual dependency installation

🧱 Architecture
Auth Service
User Service
Connections Service
Notifications Service
Chat Service (WebSocket-based real-time messaging)
Kafka as message broker
Docker containers for each service
Docker Compose for orchestration

🐳 Dockerized Deployment
Each microservice is independently containerized using Docker. Docker Compose is used to orchestrate all services along with Kafka and PostgreSQL.

Key benefits:
Service isolation
Consistent runtime environment
Easy local setup
Production-like deployment simulation

🔁 Connection Request Flow
User sends connection request
Connections Service validates request
Event published to Kafka
Notifications Service consumes event
Notification generated asynchronously

💬 Chat-Service Flow (Feature Branch: feature/linkedInChanges)
User initiates a direct message (DM) with another user
ConversationService ensures a conversation exists or creates a new one
Participants are automatically added to the conversation
Messages are sent in real-time via WebSocket
All messages are stored in the database and retrievable for history
All chat-service features are fully tested and working end-to-end in the feature/linkedInChanges branch

🛠️ Tech Stack
Java 17
Spring Boot
Apache Kafka
WebSocket (for chat-service)
REST APIs
PostgreSQL
Docker
Docker Compose
Git & GitHub

⚙️ How to Run (Docker - Recommended)

Clone the repository:

git clone https://github.com/Abdur124/LinkedInClone.git

Navigate to project directory:

cd LinkedInClone

Start all services using Docker Compose:

docker-compose up --build

This will start:

User Service
Connections Service
Notifications Service
Chat Service
Kafka
Zookeeper
PostgreSQL

⚙️ How to Run (Manual - Without Docker)

Start Kafka & Zookeeper
Start PostgreSQL
Start each service individually (User, Posts, Connections, Notifications, Chat)
Use REST clients or WebSocket clients to test APIs and messaging

📌 Learning Outcomes
Microservices communication patterns
Kafka producers & consumers
Event-driven system design
Real-time messaging using WebSocket
Docker containerization and orchestration
Service isolation and deployment simulation
Building scalable backend systems

👨‍💻 Author
AbdurRahman S A
Backend Engineer | Java | Spring Boot | Kafka | Docker | Microservices
