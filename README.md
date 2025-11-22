# 📚 Plataforma E-learning - Backend (Spring Boot/JPA)

Este é o repositório do backend para a Plataforma E-learning, construído com **Spring Boot 3** e **PostgreSQL** via Docker.

## 🚀 1. Requisitos de Desenvolvimento

Para rodar o projeto localmente, você precisa ter instalado:

* **Java 21+** (Versão utilizada no projeto).
* **Maven** (Para build e gerenciamento de dependências).
* **Docker** e **Docker Compose** (Para subir o banco de dados).

---

## ⚙️ 2. Configuração do Ambiente

O banco de dados PostgreSQL e a ferramenta de gerenciamento pgAdmin são inicializados via Docker Compose.

### A. Inicializar o Banco de Dados

Navegue até a raiz do projeto (onde está o arquivo `docker-compose.yml`) e execute o comando:

```bash
docker compose up -d
