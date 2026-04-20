# Personal Digital Twin for Decision Simulation System

## 🧠 Overview

The **Personal Digital Twin for Decision Simulation System** is an intelligent decision-support platform that allows users to simulate real-life choices and evaluate their outcomes before making decisions.

Unlike traditional systems that provide static recommendations, this system enables **scenario-based simulation** using structured decision models and AI-driven evaluation.

---

## ❗ Problem Statement

Modern users frequently make important decisions (academic, career, health, financial, lifestyle) without tools to analyze long-term consequences.

Existing systems:

* Provide generic recommendations
* Do not support simulation of multiple decision paths
* Lack personalized, data-driven evaluation

👉 This project solves that by enabling **decision simulation using a digital twin model**.

---

## 💡 Key Idea

A **Digital Twin** acts as a virtual representation of a user’s decision scenario.

The system:

* Models decisions as structured scenarios
* Evaluates multiple alternatives
* Uses the **Weighted Sum Model (WSM)** to score and rank options
* Helps users choose the most optimal decision

---

## ⚙️ Features

* 🔐 User Authentication (User/Admin roles)
* 📂 Create & manage decision scenarios
* ➕ Add multiple decision options
* 📊 Define attributes (salary, time, growth, etc.)
* ⚖️ Configure priority weights (importance of criteria)
* 🧠 AI-based simulation using WSM algorithm
* 📈 Ranked results with explanations
* 📄 Downloadable simulation reports
* 🔁 Scenario cloning & history tracking
* 🛠️ Admin dashboard for system control

---

## 🏗️ System Architecture

```
User Input → Scenario Creation → Data Processing → AI Engine (WSM) → Simulation → Ranked Decisions → Feedback
```

---

## 🧩 Core Modules

### 👤 User Module

* Registration, login, profile management
* Scenario creation and simulation

### 🌐 Digital Twin (Decision Scenario)

* Represents a decision problem
* Contains options, attributes, and priorities

### ⚙️ Simulation Engine

* Implements **Weighted Sum Model (WSM)**
* Normalizes data
* Applies weights
* Ranks alternatives

### 📊 Reporting System

* Generates detailed reports
* Provides comparison matrices
* Displays ranked outcomes

---

## 🧱 Object-Oriented Design

### Key Classes

* `User` → authentication & roles
* `DecisionScenario` → core digital twin
* `DecisionOption` → alternatives
* `OptionAttribute` → numerical values
* `Priority` → weighted criteria
* `SimulationRun` → execution record
* `SimulationResult` → scored output
* `SimulationEngine` → decision logic
* `ReportService` → report generation

---

## 📐 UML Diagrams Included

* Use Case Diagram
* Class Diagram
* State Diagram
* Activity Diagram

---

## 🧠 Design Principles

* Single Responsibility Principle (SRP)
* Open/Closed Principle (OCP)
* Dependency Injection (DI)
* Separation of Concerns (SoC)
* DRY Principle

---

## 🧩 Design Patterns Used

* Builder Pattern
* Strategy Pattern (WSM Algorithm)
* Repository Pattern
* Template Method Pattern
* Facade Pattern

---

## 🛠️ Tech Stack

| Layer        | Technology               |
| ------------ | ------------------------ |
| Backend      | Java 21, Spring Boot     |
| Architecture | MVC                      |
| Frontend     | Thymeleaf, HTML, CSS     |
| UI           | Bootstrap 5              |
| Database     | MySQL                    |
| ORM          | Hibernate / JPA          |
| Security     | Spring Security          |
| Build Tool   | Maven                    |
| Algorithm    | Weighted Sum Model (WSM) |

---

## 🎯 Applications

* Career decision planning
* Academic choices
* Financial decision analysis
* Health & lifestyle optimization

---

## 🔥 Highlights

* Combines **AI + Simulation + OOAD concepts**
* Fully structured decision modeling system
* Scalable architecture with design patterns
* Real-world applicable system

---

## 👨‍💻 Team

* Likhith Kumar M
* Kavan Reddy
* Sai Karthik

---

## 📌 Future Enhancements

* Integration with real-time data sources
* Advanced decision algorithms (AHP, TOPSIS)
* AI-based predictive analytics
* Mobile/web dashboard

---

## 📎 How to Run

1. Clone the repository
2. Configure MySQL database
3. Run using Maven:

   ```
   mvn spring-boot:run
   ```
4. Open browser at:

   ```
   http://localhost:8080
   ```

---

## ⭐ Conclusion

This project demonstrates how **Digital Twin technology + AI-based simulation** can transform decision-making into a structured, data-driven process.

It provides users with the ability to explore multiple outcomes before making critical real-life decisions.
