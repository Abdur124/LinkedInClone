# LinkedInClone

A scalable backend system inspired by LinkedIn, built using Java, Spring Boot, Kafka, and Microservices architecture.

## 🚀 Key Features
- User authentication and profile management  
- Send & accept connection requests  
- Asynchronous notification service using Kafka  
- Event-driven communication between services  
- Real-time chat using WebSocket (chat-service)  
- Clean separation of concerns using microservices  

## 🧱 Architecture
- Auth Service  
- User Service  
- Connections Service  
- Notifications Service  
- Chat Service (WebSocket-based real-time messaging)  
- Kafka as message broker  

## 🔁 Connection Request Flow
1. User sends connection request  
2. Connections Service validates request  
3. Event published to Kafka  
4. Notifications Service consumes event  
5. Notification generated asynchronously  

## 💬 Chat-Service Flow (Feature Branch: `feature/linkedInChanges`)
1. User initiates a direct message (DM) with another user  
2. `ConversationService` ensures a conversation exists or creates a new one  
3. Participants are automatically added to the conversation  
4. Messages are sent in real-time via WebSocket  
5. All messages are stored in the database and retrievable for history  

> ✅ All chat-service features are fully tested and working end-to-end in the `feature/linkedInChanges` branch.

## 🛠️ Tech Stack
- Java 17  
- Spring Boot  
- Apache Kafka  
- WebSocket (for chat-service)  
- REST APIs  
- PostgreSQL  
- Git & GitHub  

## ⚙️ How to Run
1. Start Kafka & Zookeeper  
2. Start each service (User, Posts, Connections, Notifications, Chat)  
3. Use REST clients or WebSocket clients to test APIs and real-time messaging  

## 📌 Learning Outcomes
- Microservices communication patterns  
- Kafka producers & consumers  
- Event-driven system design  
- Real-time messaging using WebSocket  
- Error handling, validations, and serialization  
- Building scalable backend systems
