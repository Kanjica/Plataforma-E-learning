# 🎓 Plataforma de E-learning: E-studo

## 💡 Sobre o Projeto

Plataforma completa de E-learning (LMS) para gerenciamento de cursos online, alunos e controle de progresso. O foco é em uma aplicação robusta, com arquitetura em camadas e uma interface rápida e responsiva.

---

## 🛠️ Stack Tecnológico

| Categoria | Tecnologia | Uso |
| :--- | :--- | :--- |
| **Back-end** | **Java** (Spring Boot) | Desenvolvimento da API RESTful e Lógica de Negócio (Arquitetura MVC/Service Layer). |
| **Front-end** | **Angular** | Interface de usuário (UI) responsiva e interativa. |
| **Banco de Dados** | **H2 Database** | Persistência de dados em memória para desenvolvimento (JPA/Hibernate). |

---

## ✨ Principais Funcionalidades

O sistema suporta dois perfis principais (Aluno e Instrutor) e oferece:

* **Gestão de Conteúdo:** Cadastro de Cursos, Módulos e Aulas (com links de vídeo).
* **Controle de Usuários:** Autenticação e Matrícula de Alunos em Cursos.
* **Acompanhamento de Progresso:** Marcação de aulas concluídas e cálculo automático do **percentual de conclusão**.
* **Certificação:** Emissão de **Certificado de Conclusão** ao finalizar 100% do curso.
* **Interatividade:** Fórum de Dúvidas por curso e Sistema de Avaliação.
* **Painéis Dedicados:** Dashboard do Aluno e Painel de Gerenciamento do Instrutor.

---

## ⚙️ Arquitetura e Padrões

O projeto segue boas práticas de engenharia de software:

1.  **Arquitetura em Camadas:** Separação estrita em **Controller**, **Service** (Lógica de Negócio) e **Repository** (Acesso a Dados).
2.  **API RESTful:** Endpoints padronizados para todas as operações **CRUD** (`/cursos`, `/alunos`, etc.).
3.  **Qualidade:** Validação dupla (Front-end e Back-end - Bean Validation) e tratamento centralizado de erros (Exception Handler).
4.  **Responsividade:** Interface totalmente adaptável a dispositivos mobile.

---

## 🚀 Como Executar

*(**Ajuste estes passos para o seu ambiente exato**)*

### Back-end (Java/Spring Boot)
1.  Clone o repositório.
2.  Navegue até a pasta `backend`.
3.  Execute o projeto via IDE (IntelliJ, VS Code) ou pelo terminal:
    ```bash
    ./mvnw spring-boot:run
    ```

### Front-end (Angular)
1.  Navegue até a pasta `frontend`.
2.  Instale as dependências:
    ```bash
    npm install
    ```
3.  Inicie o servidor de desenvolvimento:
    ```bash
    ng serve --open
    ```

---

<!--## 🧑‍💻 Autor

<!--[Seu Nome / Equipe]
