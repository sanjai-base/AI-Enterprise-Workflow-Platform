# 🚀 Nexus: AI-Powered Enterprise Workflow Automation Platform

![Architecture](https://img.shields.io/badge/Architecture-Microservices-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-brightgreen.svg)
![Next.js](https://img.shields.io/badge/Next.js-14.2-black.svg)
![React](https://img.shields.io/badge/React-18-blue.svg)
![LangChain4j](https://img.shields.io/badge/LangChain4j-0.31-orange.svg)
![Terraform](https://img.shields.io/badge/Terraform-AWS-purple.svg)

Nexus is an enterprise-grade, highly scalable Workflow Automation Platform inspired by industry giants like ServiceNow and Salesforce. It empowers organizations to visually design, execute, and orchestrate complex business processes without writing a single line of code.

What sets Nexus apart is its deep integration with **Generative AI (Retrieval-Augmented Generation)**. By utilizing LangChain4j, the platform features an intelligent AI Copilot that can autonomously read workflow attachments, summarize vendor contracts, and guide users through complex enterprise approvals.

---

## ✨ Key Features

- 🧠 **AI-Powered Copilot (RAG)**: Built-in LangChain4j integration allowing users to converse with their workflow data and extract insights from uploaded PDFs in real-time.
- ⚙️ **Spring State Machine Engine**: A mathematically rigorous, event-driven workflow execution engine guaranteeing deterministic state transitions for compliance-heavy processes.
- 🖱️ **Visual Workflow Builder**: A sleek, Next.js Drag-and-Drop canvas allowing non-technical managers to map out Approval Gates, Manual Tasks, and Automated triggers.
- 🏗️ **Distributed Microservices**: 10 independent Spring Boot microservices (API Gateway, Auth, Workflow, AI, Documents, etc.) designed for horizontal scalability.
- 🔐 **Zero-Trust Security**: Robust Spring Security implementation with JWT-based stateless authentication and Role-Based Access Control (RBAC).
- ☁️ **Cloud-Native DevOps**: Infrastructure-as-Code (IaC) via Terraform for automated AWS EKS cluster provisioning, accompanied by Kubernetes manifests and GitHub Actions CI/CD pipelines.

---

## 🏗️ System Architecture

Nexus is built on a modern, decoupled microservices architecture.

### Backend Stack
* **API Gateway**: Spring Cloud Gateway for centralized routing, rate-limiting, and CORS.
* **Authentication Service**: Spring Security & JWT token issuance.
* **Workflow Engine**: Spring State Machine, JPA/Hibernate, and Kafka event publishing.
* **AI Engine**: LangChain4j, OpenAI integrations, and Vector Embeddings.
* **Document Service**: Secure multipart file handling for enterprise assets.

### Frontend Stack
* **Framework**: Next.js 14 (App Router), React 18, TypeScript.
* **State Management**: Redux Toolkit for complex global state.
* **Styling**: Tailwind CSS for highly responsive, glassmorphic enterprise UI.

### Infrastructure & DevOps
* **Containerization**: Docker & Docker Compose.
* **Orchestration**: Kubernetes (Deployment, Service, HPA manifests included).
* **Cloud Provisioning**: Terraform (AWS EKS, RDS, ElastiCache).
* **CI/CD**: GitHub Actions.

---

## 🚀 Getting Started (Local Development)

### Prerequisites
- Java 17+
- Node.js 18+
- Maven 3.8+

### 1. Booting the Microservices Network
Navigate to the `backend` directory and start the core services.
```bash
cd backend

# Start API Gateway (Port 8080)
cd api-gateway
mvn spring-boot:run

# Start Auth Service (Port 8081)
cd ../auth-service
mvn spring-boot:run

# Start Workflow Engine (Port 8083)
cd ../workflow-service
mvn spring-boot:run

# Start AI Engine (Port 8084)
cd ../ai-service
mvn spring-boot:run

# Start Document Service (Port 8085)
cd ../document-service
mvn spring-boot:run
```

### 2. Booting the Next.js Dashboard
Navigate to the `frontend` directory.
```bash
cd frontend
npm install
npm run dev
```
Navigate your browser to `http://localhost:3000`.

---

## 📈 Future Roadmap
- [ ] Implement Apache Kafka listeners for completely asynchronous inter-service event choreography.
- [ ] Deploy local pgvector PostgreSQL database to replace `InMemoryEmbeddingStore`.
- [ ] Complete React Flow visual node-wiring integration.

---

*This project was engineered to demonstrate advanced knowledge of distributed systems, AI integration, and full-stack enterprise architecture.*
