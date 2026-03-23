# 📚 Plataforma E-Learning — Backend

API REST completa para uma plataforma de ensino online, desenvolvida com Spring Boot 3 e Java 21.

O sistema suporta cadastro de alunos e instrutores, catálogo de cursos com módulos e aulas, matrículas com rastreamento de progresso, fórum de discussão com respostas aninhadas, avaliações, e geração de certificado em PDF ao concluir um curso.

---

## 🛠️ Stack

| Tecnologia | Uso |
|---|---|
| Java 21 + Spring Boot 3.5 | Core da aplicação |
| Spring Security + JWT (Auth0) | Autenticação stateless |
| Spring Data JPA + PostgreSQL | Persistência |
| Flyway | Versionamento do schema |
| OpenPDF | Geração de certificados |
| Spring Mail | Envio de e-mails |
| Springdoc OpenAPI 2.8 | Documentação interativa |
| Docker + Docker Compose | Ambiente de desenvolvimento |
| Lombok | Redução de boilerplate |

---

## 🚀 Como rodar

### Pré-requisitos

- Java 21+
- Docker e Docker Compose

### 1. Subir o banco de dados

```bash
docker compose up -d
```

Isso sobe o PostgreSQL na porta `5433` e o pgAdmin na porta `8088`.

| Serviço | URL | Credenciais |
|---|---|---|
| PostgreSQL | `localhost:5433` | `postgres / postgres` |
| pgAdmin | `http://localhost:8088` | `admin@elearning.dev / postgres` |

### 2. Rodar a aplicação

**Via IDE:** execute a classe `ElearningApplication.java`

**Via terminal:**
```bash
./mvnw clean install
java -jar target/elearning-0.0.1-SNAPSHOT.jar
```

A API estará disponível em `http://localhost:8080`.

### 3. Acessar a documentação

```
http://localhost:8080/swagger-ui.html
```

---

## 🔐 Autenticação

A API usa **JWT Bearer Token** com sessão stateless.

### Fluxo

```
POST /auth/login
  → retorna { "token": "eyJ..." }
  → usar nos próximos requests: Authorization: Bearer <token>
```

### Roles disponíveis

| Role | Descrição |
|---|---|
| `ADMIN` | Acesso total à plataforma |
| `INSTRUCTOR` | Gerencia cursos e módulos |
| `STUDENT` | Acessa cursos e acompanha progresso |

---

## 📡 Endpoints principais

### Autenticação
| Método | Rota | Descrição | Auth |
|---|---|---|---|
| `POST` | `/auth/register` | Cadastrar usuário | Pública |
| `POST` | `/auth/login` | Login e obtenção do token | Pública |

### Cursos
| Método | Rota | Descrição | Auth |
|---|---|---|---|
| `GET` | `/courses` | Listar todos os cursos | Autenticado |
| `GET` | `/courses/{id}` | Buscar curso por ID | Autenticado |
| `POST` | `/courses` | Criar curso | INSTRUCTOR / ADMIN |
| `PUT` | `/courses/{id}` | Atualizar curso | INSTRUCTOR / ADMIN |
| `DELETE` | `/courses/{id}` | Deletar curso | ADMIN |
| `POST` | `/courses/search` | Filtrar por título e categorias | Autenticado |
| `GET` | `/courses/{id}/progress` | Ver progresso do aluno logado | STUDENT |

### Matrículas
| Método | Rota | Descrição | Auth |
|---|---|---|---|
| `POST` | `/enrollments` | Matricular-se em um curso | STUDENT |
| `GET` | `/enrollments/student/{id}` | Listar matrículas de um aluno | ADMIN |
| `GET` | `/enrollments/{id}/certificate` | Baixar certificado em PDF | STUDENT |

### Módulos e Aulas
| Método | Rota | Descrição | Auth |
|---|---|---|---|
| `POST` | `/modules` | Criar módulo em um curso | INSTRUCTOR |
| `GET` | `/modules/{id}` | Buscar módulo | Autenticado |
| `POST` | `/lessons` | Criar aula em um módulo | INSTRUCTOR |
| `PATCH` | `/lessons/{id}/complete` | Marcar aula como concluída | STUDENT |

### Fórum
| Método | Rota | Descrição | Auth |
|---|---|---|---|
| `POST` | `/topics` | Criar tópico | Autenticado |
| `GET` | `/topics/course/{courseId}` | Listar tópicos de um curso | Autenticado |
| `POST` | `/responses` | Responder tópico | Autenticado |
| `POST` | `/responses/{id}/reply` | Resposta aninhada | Autenticado |

### Avaliações e Categorias
| Método | Rota | Descrição | Auth |
|---|---|---|---|
| `POST` | `/reviews` | Avaliar curso (1–5 estrelas) | STUDENT |
| `GET` | `/reviews/course/{id}` | Ver avaliações de um curso | Autenticado |
| `GET` | `/categories` | Listar categorias | Autenticado |
| `POST` | `/categories` | Criar categoria | ADMIN |

---

## 🗄️ Modelo de dados

```
users (id, name, email, password, role)
  ├── students (id → users.id)
  └── instructors (id → users.id)

courses (id, title, description, workload, price, old_price, is_best_seller)
  ├── course_instructor [M:N] → instructors
  ├── course_category  [M:N] → categories
  └── modules (id, title, module_order, course_id)
        └── lessons (id, title, video_url, lesson_order, module_id)

enrollments (student_id, course_id, overall_progress, status)
  └── completed_lessons (enrollment_id, lesson_id)

topics (course_id, user_id, title, content)
  └── responses (topic_id, user_id, response_parent_id)   ← aninhamento recursivo

reviews (student_id, course_id, rating 1–5, comment)
```

---

## 🏗️ Arquitetura

```
src/main/java/com/lp3/elearning/
├── config/          # OpenAPI, CORS, configurações gerais
├── controller/      # Endpoints REST
├── dto/             # Objetos de entrada e saída da API
├── entities/        # Entidades JPA (herança JOINED: User → Student/Instructor)
├── exception/       # GlobalExceptionHandler + exceções customizadas
├── repository/      # Interfaces Spring Data JPA
├── security/        # SecurityFilter, JWT, AuthenticationEntryPoint
└── service/         # Lógica de negócio
```

**Padrões aplicados:**
- Arquitetura em camadas (Controller → Service → Repository)
- DTOs para separação do contrato da API das entidades internas
- Exceções de negócio tipadas (`ConflictException`, `BusinessRuleException`, `ResourceNotFoundException`)
- Respostas de erro padronizadas com `timestamp`, `status`, `error` e `message`
- `@Transactional(readOnly = true)` em operações de leitura
- Herança JPA com `InheritanceType.JOINED` para `User`, `Student` e `Instructor`

---

## 📋 Respostas de erro

Todos os erros seguem o formato:

```json
{
  "timestamp": "2025-01-15T10:30:00",
  "status": 409,
  "error": "Conflito de dados",
  "message": "Já existe um curso com o título: Java para Iniciantes"
}
```

Erros de validação retornam os campos individualmente:

```json
{
  "timestamp": "2025-01-15T10:30:00",
  "status": 400,
  "error": "Erro de validação",
  "message": "Verifique os campos inválidos",
  "fieldErrors": {
    "email": "deve ser um endereço de e-mail válido",
    "workload": "A carga horária mínima para um curso é de 10 horas"
  }
}
```

---

## 📁 Estrutura do banco (Flyway)

As migrations ficam em `src/main/resources/db/migration/`.

| Arquivo | Descrição |
|---|---|
| `V1__Create_All_Schema.sql` | Schema completo inicial |
| `V2__Insert_All_Data.sql` | Dados de seed (usuários, cursos, matrículas, fórum) |
| `V3__Insert_More_Courses.sql` | Cursos adicionais (25 cursos no total) |
---
