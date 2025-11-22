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
````

| Serviço | Porta Local | Credenciais |
| :--- | :--- | :--- |
| **PostgreSQL** | `5433` | Veja `docker-compose.yml` (`POSTGRES_USER`, `POSTGRES_PASSWORD`) |
| **pgAdmin** | `8088` | Veja `docker-compose.yml` (`PGADMIN_DEFAULT_EMAIL`, `PGADMIN_DEFAULT_PASSWORD`) |

### B. Porta do Spring Boot

A aplicação Spring Boot rodará na porta padrão:

  * **API:** `http://localhost:8080`

-----

## 🛠️ 3. Como Rodar a Aplicação

### A. Via IDE (IntelliJ, VS Code, etc.)

1.  Verifique se o Docker está rodando.
2.  Importe o projeto como um projeto Maven.
3.  Execute a classe principal `ElearningApplication.java`.

### B. Via Terminal

1.  Compile o projeto (isso também resolve as dependências):
    ```bash
    ./mvnw clean install
    ```
2.  Execute a aplicação:
    ```bash
    java -jar target/elearning-0.0.1-SNAPSHOT.jar
    ```
    *(A versão do JAR pode variar.)*

-----

## 📦 4. Estrutura do Projeto 

O projeto segue a arquitetura de **Camadas (MVC)** com a separação clara de responsabilidades:

  * **`com.lp3.elearning.entities`**: Classes de Mapeamento JPA (`@Entity`).
  * **`com.lp3.elearning.dtos`**: Classes de Transferência de Dados (Entrada/Saída da API).
  * **`com.lp3.elearning.repositories`**: Interfaces de acesso a dados (`JpaRepository`).
  * **`com.lp3.elearning.services`**: **Lógica de Negócio** central da aplicação.
  * **`com.lp3.elearning.controllers`**: *Endpoints* REST que recebem requisições HTTP.
  * **`src/main/resources/application.properties`**: Configurações da aplicação.
  * **`src/main/resources/db/migration`**: Scripts **Flyway** (Para evoluir o schema do BD).


