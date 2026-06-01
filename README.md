# 🏨 BookMyStay

<div align="center">

<img src="https://capsule-render.vercel.app/api?type=waving&color=0:0f172a,50:1e293b,100:334155&height=220&section=header&text=BookMyStay&fontSize=48&fontColor=ffffff&animation=fadeIn&fontAlignY=38&desc=Modern%20Property%20Booking%20Platform%20Backend&descAlignY=58&descSize=18" />

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-brightgreen?style=for-the-badge&logo=springboot" />
  <img src="https://img.shields.io/badge/PostgreSQL-Database-blue?style=for-the-badge&logo=postgresql" />
  <img src="https://img.shields.io/badge/JWT-Authentication-red?style=for-the-badge&logo=jsonwebtokens" />
  <img src="https://img.shields.io/badge/Maven-Build Tool-purple?style=for-the-badge&logo=apachemaven" />
</p>

<h3>🚀 A scalable Hotel Room booking backend built using Spring Boot</h3>

</div>

---

# 📖 Overview

BookMyStay is a modern Hotel Room booking backend system developed using Spring Boot.

This project provides secure authentication, property management, booking functionality, reviews, and RESTful APIs designed with industry-level backend architecture.

The goal of this project is to learn and implement:

* REST APIs
* Spring Security
* JWT Authentication
* PostgreSQL Integration
* JPA & Hibernate
* Role-Based Authorization
* Backend Architecture

---

# ✨ Features

## 🔐 Authentication & Authorization

* User Registration
* User Login
* JWT Token Authentication
* BCrypt Password Encryption
* Role-Based Access Control

## 🏠 Property Management

* Add Property
* Update Property
* Delete Property
* View All Properties
* Property Details API

## 📅 Booking System

* Book Property
* Prevent Overlapping Bookings
* View User Bookings

## ⭐ Reviews & Ratings

* Add Reviews
* Property Ratings
* View Reviews by Property

## 🔍 Search Functionality

* Search Properties by Location
* Pagination Support

## ⚙️ Backend Features

* DTO Mapping
* Global Exception Handling
* Request Validation
* Clean Architecture
* RESTful API Design

---

# 🛠️ Tech Stack

| Technology      | Usage                          |
| --------------- | ------------------------------ |
| Java 17         | Core Language                  |
| Spring Boot     | Backend Framework              |
| Spring Security | Authentication & Authorization |
| JWT             | Secure Token Authentication    |
| Spring Data JPA | Database Operations            |
| Hibernate       | ORM Framework                  |
| PostgreSQL      | Database                       |
| Maven           | Dependency Management          |
| Lombok          | Boilerplate Reduction          |
| Postman         | API Testing                    |

---

# 📂 Project Structure

```bash
src/main/java/com/bookmystay
│
├── controller
├── service
├── repository
├── entity
├── dto
├── security
├── config
├── exception
└── util
```

---

# 🚀 API Endpoints

## 🔐 Authentication APIs

| Method | Endpoint       | Description   |
| ------ | -------------- | ------------- |
| POST   | /auth/register | Register User |
| POST   | /auth/login    | Login User    |

---

## 🏠 Property APIs

| Method | Endpoint         |
| ------ | ---------------- |
| GET    | /properties      |
| GET    | /properties/{id} |
| POST   | /properties      |
| PUT    | /properties/{id} |
| DELETE | /properties/{id} |

---

## 📅 Booking APIs

| Method | Endpoint              |
| ------ | --------------------- |
| POST   | /bookings             |
| GET    | /bookings/my-bookings |

---

## ⭐ Review APIs

| Method | Endpoint               |
| ------ | ---------------------- |
| POST   | /reviews               |
| GET    | /reviews/property/{id} |

---

# ⚡ Getting Started

## 1️⃣ Clone Repository

```bash
git clone https://github.com/Priyanshu8709/BookMyStay.git
```

---

## 2️⃣ Open Project

Open the project in:

* IntelliJ IDEA

---

## 3️⃣ Configure PostgreSQL

Create a database named:

```sql
bookmystay
```

---

## 4️⃣ Configure application.properties

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bookmystay
spring.datasource.username=postgres
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8080
```

---

## 5️⃣ Run Application

```bash
mvn spring-boot:run
```

---

# 🔒 Security Features

* JWT Authentication
* Stateless Authentication
* Password Encryption using BCrypt
* Protected APIs
* Role-Based Authorization

---

# 📌 Future Improvements

* Cloudinary Image Upload
* Payment Gateway Integration
* Email Notifications
* Docker Support
* Redis Caching
* Microservices Architecture
* Admin Dashboard

---

# 📸 Sample Workflow

```text
User Register → Login → Receive JWT Token → Access Protected APIs → Book Properties
```

---

# 🧠 Learning Outcomes

This project helped in understanding:

* Backend Development
* API Design
* Authentication Systems
* Database Relationships
* Spring Security
* Clean Code Practices
* Real-world Project Architecture

---

# 🤝 Contributing

Contributions are welcome.

If you'd like to improve this project:

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

---

# 👨‍💻 Developer

### Priyanshu Raj

📌 Full Stack Developer | Spring Boot Enthusiast | Java Backend Developer

---

# ⭐ Support

If you liked this project, consider giving it a ⭐ on GitHub.

---

<div align="center">

## 🚀 Building Real-World Backend Systems with Spring Boot

</div>
