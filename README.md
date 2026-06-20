# 📚 Plataforma E-Learning — Backend

API REST completa e de alta performance para uma plataforma de ensino online, desenvolvida com **Spring Boot 3** e **Java 21**.

O sistema conta com uma arquitetura robusta para controle de acessos hierárquicos, buscas dinâmicas otimizadas, resiliência com camada de cache distribuído e auditoria automatizada de ações críticas.

---

## 🛠️ Stack Tecnológica

| Tecnologia | Uso / Papel na Arquitetura |
| --- | --- |
| **Java 21 + Spring Boot 3.5** | Core da aplicação e ecossistema principal |
| **Spring Security + JWT (Auth0)** | Autenticação stateless com controle de acessos granulado (`@PreAuthorize`) |
| **Spring Data JPA + PostgreSQL** | Persistência relacional e mapeamento de herança complexa |
| **Redis** | Camada de cache distribuído para otimização de consultas e catálogos |
| **Flyway** | Versionamento evolutivo do banco de dados (Database Migrations) |
| **MapStruct** | Mapeamento de objetos (DTO <-> Entity) de alta performance compilado |
| **OpenPDF** | Geração de certificados de conclusão em PDF |
| **Springdoc OpenAPI 2.8** | Documentação interativa da API (Swagger UI) |
| **Docker + Docker Compose** | Orquestração do ambiente de desenvolvimento (Postgres + Redis) |

---

## 🚀 Como Rodar o Projeto

### Pré-requisitos

* Java 21+
* Docker e Docker Compose

### 1. Subir a Infraestrutura (Postgres + Redis)

```bash
docker compose up -d

```

> Isso inicializará o PostgreSQL na porta `5433`, o pgAdmin na porta `8088` e o Redis na porta `6379`.

### 2. Rodar a Aplicação

```bash
./mvnw clean compile spring-boot:run

```

A API estará disponível em `http://localhost:8080`.

### 3. Acessar a Documentação Interativa

```
http://localhost:8080/swagger-ui.html

```

---

## ⚡ Diferenciais Técnicos & Boas Práticas

* **Buscas Dinâmicas com JPA Specifications:** Filtros avançados no catálogo de cursos utilizando as Specifications do Spring Data JPA de forma tipada (`Type-Safe`), permitindo queries combinadas por título, categorias e instrutores de forma desacoplada.
* **Invalidação Cirúrgica de Cache:** Uso do Spring Cache com Redis. Implementação de chaves dinâmicas baseadas em hierarquia via SpEL (Spring Expression Language) para evitar a limpeza total do cache (`allEntries = true`) quando apenas uma aula ou módulo é alterado.
* **Prevenção contra N+1 Selects:** Uso estratégico de estratégias de fetch, `@BatchSize` e Projections para otimizar o carregamento de grafos de entidades e coleções aninhadas.
* **Auditoria de Ações Baseada em Aspectos (AOP):** Criação de anotações customizadas (`@Auditable`) interceptadas via Spring AOP para registrar logs de modificações de dados sem poluir as regras de negócio dos Services.
* **Segurança Baseada em Contexto:** Uso da meta-anotação customizada `@CurrentUser` para encapsular o `@AuthenticationPrincipal`, isolando o acoplamento com o framework de segurança nos Controllers.
* **Respostas Padronizadas:** Tratamento global de exceções (`@ControllerAdvice`) envelopando retornos com a estrutura genérica de respostas da API.
* **Database Migrations:** Gerenciamento rígido do ciclo de vida do banco com Flyway, isolando a massa de dados de teste (Seeders) das estruturas de tabelas.
* **Herança JPA Avançada:** Implementação de `InheritanceType.JOINED` para gerenciar a especialização da entidade `User` em `Student` e `Instructor`.

---

## 🔐 Níveis de Acesso (Roles)

| Role | Escopo de Permissão |
| --- | --- |
| `ADMIN` | Gestão total da plataforma, controle de categorias, auditoria e exclusão de dados. |
| `INSTRUCTOR` | Criação, edição e reordenação sequencial de cursos, módulos e aulas. |
| `STUDENT` | Consumo de conteúdo, controle de progresso sequencial e emisso de certificados. |

---

## 📡 Endpoints Principais

### Autenticação

* `POST /auth/register` - Cadastrar usuário (Pública)
* `POST /auth/login` - Login e obtenção do token (Pública)

### Cursos & Catálogo (Com Cache e Specifications)

* `GET /courses` - Listar todos os cursos (Paginado - Autenticado)
* `GET /courses/{id}` - Buscar curso por ID (Autenticado)
* `POST /courses` - Criar curso (INSTRUCTOR / ADMIN)
* `PUT /courses/{id}` - Atualizar curso (INSTRUCTOR / ADMIN)
* `DELETE /courses/{id}` - Deletar curso (ADMIN)
* `POST /courses/search` - Filtrar dinamicamente via Specification (Autenticado)
* `GET /courses/{id}/progress` - Ver progresso do aluno logado (STUDENT)

### Matrículas & Progresso

* `POST /enrollments` - Matricular-se em um curso (STUDENT)
* `GET /enrollments/student/{id}` - Listar matrículas de um aluno (ADMIN)
* `GET /enrollments/{id}/certificate` - Baixar certificado em PDF (STUDENT)

### Módulos e Aulas

* `POST /modules` - Criar módulo em um curso (INSTRUCTOR)
* `GET /modules/{id}` - Buscar módulo (Autenticado)
* `POST /lessons` - Criar aula em um módulo (INSTRUCTOR)
* `PATCH /lessons/{id}/complete` - Marcar aula como concluída (STUDENT)

### Fórum de Discussão

* `POST /topics` - Criar tópico (Autenticado)
* `GET /topics/course/{courseId}` - Listar tópicos de um curso (Autenticado)
* `POST /responses` - Responder tópico (Autenticado)
* `POST /responses/{id}/reply` - Resposta aninhada recursiva (Autenticado)

---

## 📋 Estrutura das Respostas de Erro

Todos os erros da plataforma seguem o formato padronizado:

```json
{
  "timestamp": "2026-06-19T21:30:00",
  "status": 409,
  "error": "Conflict Exception",
  "message": "Módulo com o título 'Fundamentos' já existe neste curso."
}

```

Erros de validação de campos (`@Valid`) retornam os detalhes específicos:

```json
{
  "timestamp": "2026-06-19T21:35:00",
  "status": 400,
  "error": "Method Argument Not Valid Exception",
  "message": "Verifique os campos inválidos",
  "fieldErrors": {
    "email": "Deve ser um endereço de e-mail válido",
    "workload": "A carga horária mínima para um curso é de 10 horas"
  }
}

```