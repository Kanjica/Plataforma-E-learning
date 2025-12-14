-- V2__Insert_All_Data.sql
-- Contém todas as instruções INSERT INTO para popular as tabelas.

-- --------------------------------------------------------
-- 1. USERS (Superclasse)
-- --------------------------------------------------------
INSERT INTO users (id, name, email, password, role) OVERRIDING SYSTEM VALUE VALUES
-- Alunos (IDs 1-5, 11-12)
(1, 'Ana Silva', 'ana.silva@teste.com', 'hashed_senha_1', 'ROLE_STUDENT'),
(2, 'Bruno Costa', 'bruno.costa@teste.com', 'hashed_senha_2', 'ROLE_STUDENT'),
(3, 'Carla Dias', 'carla.dias@teste.com', 'hashed_senha_3', 'ROLE_STUDENT'),
(4, 'Daniel Melo', 'daniel.melo@teste.com', 'hashed_senha_4', 'ROLE_STUDENT'),
(5, 'Erica Nunes', 'erica.nunes@teste.com', 'hashed_senha_5', 'ROLE_STUDENT'),
(11, 'Fernando Reis', 'fernando.reis@teste.com', 'hashed_senha_11', 'ROLE_STUDENT'),
(12, 'Giovana Alves', 'giovana.alves@teste.com', 'hashed_senha_12', 'ROLE_STUDENT'),
-- Instrutores (IDs 6-10)
(6, 'Prof. João', 'joao.prof@teste.com', 'hashed_instructor_1', 'ROLE_INSTRUCTOR'),
(7, 'Dra. Laura', 'laura.prof@teste.com', 'hashed_instructor_2', 'ROLE_INSTRUCTOR'),
(8, 'Eng. Ricardo', 'ricardo.prof@teste.com', 'hashed_instructor_3', 'ROLE_INSTRUCTOR'),
(9, 'Marta Lima', 'marta.prof@teste.com', 'hashed_instructor_4', 'ROLE_INSTRUCTOR'),
(10, 'Dr. Felipe', 'felipe.prof@teste.com', 'hashed_instructor_5', 'ROLE_INSTRUCTOR');

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));

-- --------------------------------------------------------
-- 2. STUDENTS (Subclasse)
-- --------------------------------------------------------
INSERT INTO students (id) VALUES
(1), (2), (3), (4), (5), (11), (12);

-- --------------------------------------------------------
-- 3. INSTRUCTORS (Subclasse)
-- --------------------------------------------------------
INSERT INTO instructors (id) VALUES
(6), (7), (8), (9), (10);

-- --------------------------------------------------------
-- 4. COURSES (Cursos)
-- --------------------------------------------------------
INSERT INTO courses (id, title, description, workload) OVERRIDING SYSTEM VALUE VALUES
(1, 'Introdução ao Spring Boot', 'Curso completo de introdução ao desenvolvimento de APIs com Spring Boot e Java.', 40),
(2, 'Desenvolvimento Web com React', 'Aprenda a criar interfaces de usuário modernas e reativas usando a biblioteca React e seus principais hooks.', 60),
(3, 'Banco de Dados PostgreSQL Avançado', 'Domine consultas complexas, otimização e administração de bancos de dados PostgreSQL.', 30),
(4, 'Arquitetura de Microsserviços', 'Projete e implemente sistemas escaláveis usando o padrão de microsserviços.', 50),
(5, 'Design Thinking e UX/UI', 'Entenda o processo de Design Thinking e crie experiências de usuário intuitivas e eficientes.', 20),
(6, 'Introdução ao Python e Ciência de Dados', 'Primeiros passos em Python para análise de dados e machine learning.', 75);

SELECT setval('courses_id_seq', (SELECT MAX(id) FROM courses));

-- --------------------------------------------------------
-- 5. COURSE_INSTRUCTOR (Relação N:N)
-- --------------------------------------------------------
INSERT INTO course_instructor (course_id, instructor_id) VALUES
(1, 6), (1, 7), (2, 8), (3, 6), (4, 9), (5, 10), (6, 7), (6, 9);

-- --------------------------------------------------------
-- 6. MODULES (Módulos)
-- --------------------------------------------------------
INSERT INTO modules (id, title, description, course_id, module_order) OVERRIDING SYSTEM VALUE VALUES
(1, 'Fundamentos do Spring Boot', 'Introdução aos conceitos básicos do Spring Boot.', 1, 1),
(2, 'Desenvolvimento com Controllers', 'Criação de endpoints REST com controllers.', 1, 2),
(3, 'Testes de Integração e Unitários', 'Implementação de testes para APIs Spring Boot.', 1, 3),
(4, 'Introdução ao React', 'Primeiros passos com a biblioteca React.', 2, 1),
(5, 'Hooks e Gerenciamento de Estado', 'Uso de hooks e gerenciamento de estado no React.', 2, 2),
(6, 'Otimização de Queries', 'Técnicas para otimizar consultas em PostgreSQL.', 3, 1),
(7, 'Padrões de Comunicação', 'Padrões de comunicação em sistemas distribuídos.', 4, 1),
(8, 'Design System', 'Criação e manutenção de sistemas de design.', 5, 1),
(9, 'Básico de Sintaxe Python', 'Primeiros passos com a linguagem Python.', 6, 1);

SELECT setval('modules_id_seq', (SELECT MAX(id) FROM modules));

-- --------------------------------------------------------
-- 7. LESSONS (Aulas)
-- --------------------------------------------------------
INSERT INTO lessons (id, title, video_url, module_id, lesson_order) OVERRIDING SYSTEM VALUE VALUES
(1, 'Configurando o Ambiente', 'http://video.teste/sb/01', 1, 1),
(2, 'Endpoints REST e Verbos HTTP', 'http://video.teste/sb/02', 1, 2),
(3, 'Injeção de Dependência', 'http://video.teste/sb/03', 1, 3),
(4, 'Criando Componentes Funcionais', 'http://video.teste/react/01', 4, 1),
(5, 'Usando useEffect', 'http://video.teste/react/02', 5, 1),
(6, 'Usando Índices com Eficiência', 'http://video.teste/pg/01', 6, 1),
(7, 'REST vs. gRPC', 'http://video.teste/ms/01', 7, 1),
(8, 'Atomic Design', 'http://video.teste/ux/01', 8, 1),
(9, 'Variáveis e Tipos de Dados', 'http://video.teste/py/01', 9, 1);

SELECT setval('lessons_id_seq', (SELECT MAX(id) FROM lessons));

-- --------------------------------------------------------
-- 8. ENROLLMENTS (Matrículas)
-- --------------------------------------------------------
INSERT INTO enrollments (id, student_id, course_id, enrollment_date, overall_progress, status) OVERRIDING SYSTEM VALUE VALUES
(1, 1, 1, CURRENT_DATE - INTERVAL '30 days', 33.3, 'IN_PROGRESS'),
(2, 2, 2, CURRENT_DATE - INTERVAL '60 days', 100.0, 'COMPLETED'),
(3, 3, 3, CURRENT_DATE - INTERVAL '15 days', 25.5, 'IN_PROGRESS'),
(4, 4, 4, CURRENT_DATE, 0.0, 'IN_PROGRESS'),
(5, 5, 5, CURRENT_DATE - INTERVAL '5 days', 10.0, 'IN_PROGRESS'),
(6, 11, 6, CURRENT_DATE - INTERVAL '10 days', 5.0, 'IN_PROGRESS'),
(7, 12, 1, CURRENT_DATE - INTERVAL '2 days', 0.0, 'IN_PROGRESS'),
(8, 2, 4, CURRENT_DATE - INTERVAL '10 days', 0.0, 'IN_PROGRESS');

SELECT setval('enrollments_id_seq', (SELECT MAX(id) FROM enrollments));

-- --------------------------------------------------------
-- 9. COMPLETED_LESSONS (Aulas Concluídas)
-- --------------------------------------------------------
INSERT INTO completed_lessons (id, enrollment_id, lesson_id, completion_date) OVERRIDING SYSTEM VALUE VALUES
(1, 1, 1, NOW() - INTERVAL '29 days'),
(2, 1, 2, NOW() - INTERVAL '28 days'),
(3, 2, 4, NOW() - INTERVAL '59 days'),
(4, 3, 6, NOW() - INTERVAL '14 days'),
(5, 1, 3, NOW() - INTERVAL '27 days'),
(6, 6, 9, NOW() - INTERVAL '9 days');

SELECT setval('completed_lessons_id_seq', (SELECT MAX(id) FROM completed_lessons));

-- --------------------------------------------------------
-- 10. TOPICS (Tópicos do Fórum)
-- Dados do V2 Original + V3:
-- --------------------------------------------------------
INSERT INTO topics (id, title, content, creation_date, course_id, user_id) OVERRIDING SYSTEM VALUE VALUES
-- Dados do V2 Original:
(1, 'Problema ao Rodar o Projeto Base', 'Meu projeto Spring Boot não está subindo após a aula 2. Alguma dica?', NOW() - INTERVAL '5 days', 1, 1),
(2, 'Melhores Práticas para useState', 'Como vocês organizam o código ao usar muitos `useState` em um único componente?', NOW() - INTERVAL '4 days', 2, 8),
(3, 'Como funciona o Join Inheritance?', 'Gostaria de mais detalhes sobre como o `InheritanceType.JOINED` mapeia no banco de dados.', NOW() - INTERVAL '3 days', 1, 3),
-- Dados do V3 (continuando ID a partir de 4):
(4, 'Polymorphism Concept Doubt', 'I did not fully understand the concept of polymorphism in Lesson 3. Can someone give a practical example?', NOW() - INTERVAL '2 days', 1, 2),
(5, 'Suggestion for Final Project', 'Could we create a small Library Management System as the final course project?', NOW() - INTERVAL '1 day', 1, 3),
(6, 'Bug in Quiz Found', 'The Module 2 quiz marks the correct answer as wrong for question 5.', NOW() - INTERVAL '1 day', 2, 4),
(7, 'Best Code Practices: Linters', 'What are the linter tools you use most often?', NOW() - INTERVAL '12 hours', 3, 5),
(8, 'Thanks to the Instructor', 'I wanted to thank the instructor for the excellent material! Very didactic.', NOW() - INTERVAL '10 hours', 1, 2),
(9, 'Error 404 on API Endpoint', 'I followed the steps in Lesson 4, but I keep getting a 404 error when trying to access the /users endpoint. Any ideas?', NOW() - INTERVAL '8 hours', 1, 1),
(10, 'Which Database is Better for Microservices?', 'For a new microservice architecture, is PostgreSQL better than MongoDB for storing user profiles?', NOW() - INTERVAL '5 hours', 4, 11),
(11, 'Need Help with Deployment', 'The React application works locally, but I am facing CORS issues after deploying to Vercel.', NOW() - INTERVAL '2 hours', 2, 12);

SELECT setval('topics_id_seq', (SELECT MAX(id) FROM topics));

-- --------------------------------------------------------
-- 11. RESPONSES (Respostas do Fórum)
-- Dados do V2 Original + V3:
-- --------------------------------------------------------
INSERT INTO responses (id, content, creation_date, topic_id, user_id, response_parent_id) OVERRIDING SYSTEM VALUE VALUES
-- Dados do V2 Original:
(1, 'Verifique se você rodou `mvn clean install` antes de subir o projeto.', NOW() - INTERVAL '4 days 12 hours', 1, 6, NULL),
(2, 'Obrigada, professor! Funcionou.', NOW() - INTERVAL '4 days 10 hours', 1, 1, 1),
(3, 'Para muitos estados, recomendo usar o `useReducer` ou uma biblioteca de estado como Redux/Zustand.', NOW() - INTERVAL '3 days 20 hours', 2, 9, NULL),
(4, 'O `InheritanceType.JOINED` cria uma tabela para a superclasse e uma tabela separada para cada subclasse, usando a PK da subclasse como FK para a superclasse.', NOW() - INTERVAL '2 days', 3, 7, NULL),
-- Dados do V3 (continuando ID a partir de 5, e ajustando o topico_id correspondente):
(5, 'Polymorphism allows you to treat objects of different classes that share a common superclass or interface uniformly. E.g., a "Dog" and a "Cat" can both be treated as an "Animal" when calling a "makeSound" method.', NOW() - INTERVAL '1 day 10 hours', 4, 6, NULL), -- Topic ID 4
(6, 'Thanks for the tip! That makes sense to centralize it in the Service layer.', NOW() - INTERVAL '1 day 8 hours', 4, 2, 5), -- Topic ID 4, Parent ID 5
(7, 'But what if I have a transaction across more than one Service? What should I do?', NOW() - INTERVAL '1 day 6 hours', 4, 4, 5), -- Topic ID 4, Parent ID 5
(8, 'We checked the quiz. The issue was fixed an hour ago. Please try again!', NOW() - INTERVAL '1 day 4 hours', 6, 7, NULL), -- Topic ID 6
(9, 'ESLint for JavaScript and SonarQube/Checkstyle for Java are standard tools in most companies.', NOW() - INTERVAL '10 hours', 7, 8, NULL), -- Topic ID 7
(10, 'I agree. JUnit 5 has great features like @ParameterizedTest for testing.', NOW() - INTERVAL '9 hours', 7, 5, 9), -- Topic ID 7, Parent ID 9
(11, 'Did you remember to add `@RestController` to your Controller class? That is a common mistake.', NOW() - INTERVAL '7 hours', 9, 6, NULL), -- Topic ID 9
(12, 'I also recommend checking the application.properties file to see if the port is configured correctly.', NOW() - INTERVAL '5 hours', 9, 9, 11); -- Topic ID 9, Parent ID 11

SELECT setval('responses_id_seq', (SELECT MAX(id) FROM responses));

-- --------------------------------------------------------
-- 12. REVIEWS (Avaliações de Curso)
-- Dados do V2 Original + V3:
-- --------------------------------------------------------
INSERT INTO reviews (id, rating, comment, review_date, student_id, course_id) OVERRIDING SYSTEM VALUE VALUES
-- Dados do V2 Original:
(1, 5, 'Curso excelente, o Prof. João explica muito bem. Conteúdo prático e direto.', NOW() - INTERVAL '15 days', 1, 1),
(2, 4, 'Ótimo curso de React, mas senti falta de mais exemplos de projetos práticos no final.', NOW() - INTERVAL '30 days', 2, 2),
(3, 5, 'Material de PostgreSQL muito completo. Valeu o investimento!', NOW() - INTERVAL '5 days', 3, 3),
-- Dados do V3 (continuando ID a partir de 4):
(4, 5, 'Very complete and well-explained content. I loved the practical part.', NOW() - INTERVAL '40 days', 2, 1), -- Bruno (Aluno 2), Course 1
(5, 4, 'The instructor is good, but the support material could be more detailed.', NOW() - INTERVAL '35 days', 3, 1), -- Carla (Aluno 3), Course 1
(6, 5, 'Excellent course, especially the last module!', NOW() - INTERVAL '25 days', 4, 2), -- Daniel (Aluno 4), Course 2
(7, 3, 'I had some problems with the exercises, but the theoretical material is solid.', NOW() - INTERVAL '20 days', 5, 3), -- Erica (Aluno 5), Course 3
(8, 5, 'I recommend it to everyone who wants to learn Spring Boot.', NOW() - INTERVAL '10 days', 2, 3), -- Bruno (Aluno 2), Course 3
(9, 4, 'Good introduction to Design Thinking, although the content was too short.', NOW() - INTERVAL '7 days', 1, 5), -- Ana (Aluno 1), Course 5
(10, 5, 'The best course I have taken on microservices. Highly complex but explained clearly.', NOW() - INTERVAL '3 days', 11, 4), -- Fernando (Aluno 11), Course 4
(11, 2, 'The React course is outdated. It needs an update to modern hooks and context API.', NOW() - INTERVAL '1 day', 12, 2); -- Giovana (Aluno 12), Course 2

SELECT setval('reviews_id_seq', (SELECT MAX(id) FROM reviews));

INSERT INTO categories (id, name) VALUES
(1, 'Desenvolvimento Web'),
(2, 'Ciência de Dados'),
(3, 'Design Gráfico'),
(4, 'Marketing Digital'),
(5, 'Idiomas'),
(6, 'Negócios e Finanças'),
(7, 'Desenvolvimento Mobile'),
(8, 'Saúde e Bem-Estar'),
(9, 'Arte e Música'),
(10, 'Produtividade');

INSERT INTO course_category (course_id, category_id) VALUES
(1, 1),
(2, 1),
(3, 6),
(4, 6),
(5, 3),
(6, 2);