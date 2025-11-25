<h1 align="center"> SnapQR — Modern QR Code Platform</h1>

<p align="center">
  QR Code Generator & Management system built with <strong>Angular 19</strong>, <strong>Spring Boot</strong> and <strong>PostgreSQL 16</strong>, fully containerized with <strong>Docker Compose</strong>.
  <br/>
  Secure authentication, clean dashboard, and cloud-ready CI/CD & deployments.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Angular-19-red?style=for-the-badge" alt="Angular"/>
  <img src="https://img.shields.io/badge/Spring_Boot-4-brightgreen?style=for-the-badge" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/PostgreSQL-16-blue?style=for-the-badge" alt="Postgres"/>
  <img src="https://img.shields.io/badge/Docker-Docker%20Compose-purple?style=for-the-badge" alt="Docker"/>
</p>

---

## ✨ Overview

SnapQR is a production-oriented full-stack QR Code platform that demonstrates strong engineering practices: modular frontend, secure backend, persistent database, multi-stage Docker builds, and CI/CD readiness.  
Designed for portfolio presentation, technical interviews, and real-world deployment.

**Highlights**
- JWT authentication + Google OAuth ready  
- Generate, store, download QR codes  
- Angular (NGINX multi-stage) + Spring Boot + PostgreSQL  
- Docker Compose orchestration + healthchecks  
- Ready for GitHub Actions → AWS ECR → EC2 deployments

---

## 🔑 Key Features

- **Authentication & Security** — JWT, BCrypt, role-based access  
- **QR Management** — Generate, save, download, and list user QR codes  
- **Responsive UI** — Angular + TailwindCSS with SPA routing  
- **Persistence** — PostgreSQL with Docker volume for data durability  
- **DevOps** — Docker Compose, image health checks, CI/CD-ready structure

---

## 🧰 Tech Stack

| Layer     | Technologies |
|-----------|--------------|
| Frontend  | Angular 19, TailwindCSS, TypeScript, NGINX |
| Backend   | Spring Boot, Spring Security, JPA/Hibernate, Java (JDK 21) |
| Database  | PostgreSQL 16 |
| DevOps    | Docker, Docker Compose, GitHub Actions, AWS (ECR/EC2) |

---

## ▶ Quick start (Docker - recommended)

> Prerequisites: Docker & Docker Compose

```bash
# clone
git clone https://github.com/jaykant01/SnapQR.git
cd SnapQR

# create backend .env ( put all your secrets over there )
# then start
docker compose up --build
# stop and remove containers
docker compose down
# show logs
docker compose logs -f


