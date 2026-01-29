# LinkedInClone
A scalable backend system inspired by LinkedIn, built using Java, Spring Boot, Kafka, and Microservices architecture.

## 🚀 Key Features
- User authentication and profile management
- Send & accept connection requests
- Asynchronous notification service using Kafka
- Event-driven communication between services
- Clean separation of concerns using microservices

## 🧱 Architecture
- Auth Service
- User Service
- Connections Service
- Notifications Service
- Kafka as message broker

## 🔁 Connection Request Flow
1. User sends connection request
2. Connections Service validates request
3. Event published to Kafka
4. Notifications Service consumes event
5. Notification generated asynchronously

## 🛠️ Tech Stack
- Java 17
- Spring Boot
- Apache Kafka
- REST APIs
- MySQL
- Git & GitHub

## ⚙️ How to Run
1. Start Kafka & Zookeeper
2. Start each service
3. Use REST clients to test APIs

## 📌 Learning Outcomes
- Microservices communication
- Kafka producers & consumers
- Error handling & serialization
- Real-world system design patterns
