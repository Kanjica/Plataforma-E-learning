-- V2__Insert_All_Data.sql
-- Contém todas as instruções INSERT INTO para popular as tabelas, com IDs reordenados.

-- ========================================================
-- 1. USERS (Superclasse) - INSTRUCTORS (IDs 1-9) e STUDENTS (IDs 10-21)
-- ========================================================
INSERT INTO users (id, name, email, password, role) OVERRIDING SYSTEM VALUE VALUES
-- INSTRUCTORS (Total: 9)
(1, 'Jaozada', 'joao.profss@teste.com', '$2a$10$t0ITe1tnbh.IvsWv/lTGYurOHltkbnaP4aURorcQjuU0PXu38ENsu', 'ROLE_INSTRUCTOR'),
(2, 'Pedro Alvares', 'pedro.alvares@teste.com', '$2a$10$BHOSFfTww2FcVQiexfwYVejEJdQjP84D0wo8kWNLmdJ2RZKef2o3y', 'ROLE_INSTRUCTOR'),
(3, 'Juliana Lima', 'juliana.lima@teste.com', '$2a$10$1qWQTa9nlcNP8LPiayYZD.1uxc.mCFxA9V4QMzhArm3rEZ224WAHe', 'ROLE_INSTRUCTOR'),
(4, 'Fernando Rocha', 'fernando.rocha@teste.com', '$2a$10$xf6TYcQic8DVRJBYSy9kGewSYVZNC7wS.vpx06RRaQVHwW6yfiV32', 'ROLE_INSTRUCTOR'),
(5, 'Diana Gomes', 'diana.gomes@teste.com', '$2a$10$zYTydhzHg8YyfZiMmzasw.HLVQ9TdFX/a.tKIclcKdMrjRZdk/NV.', 'ROLE_INSTRUCTOR'),
(6, 'Henrique Mota', 'henrique.mota@teste.com', '$2a$10$letg5DF3c8bdoxfA46W2MOaWcUddslf/GxQUQID/lP4IIVdtkTdwG', 'ROLE_INSTRUCTOR'),
(7, 'Mariana Campos', 'mariana.campos@teste.com', '$2a$10$diPQ7.7CoEOtX35/qapafeToNov10SazF3vNjo.Pfi2CBHL4SntLi', 'ROLE_INSTRUCTOR'),
(8, 'Guilherme Santos', 'guilherme.santos@teste.com', '$2a$10$VUnR8ho6yDZ9ROTxTIgYIu73k7fPSITkQij.xglYCQcj087eA4RSm', 'ROLE_INSTRUCTOR'),
(9, 'Professor Extra', 'extra.prof@teste.com', '$2a$10$HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH', 'ROLE_INSTRUCTOR'),

-- STUDENTS (Total: 12)
(10, 'Maria Silva', 'maria.silva@teste.com', '$2a$10$XE4TUDAEpw3yGInDVMwU9O5rS6kO73sbJzAPfpuihGpOmNWK2tVui', 'ROLE_STUDENT'),
(11, 'Ana Souza', 'ana.souza@teste.com', '$2a$10$IDbKpmheWzYSLjRCEbCtcuPn.WI5UPBvlS/7FJSkN7r7MWS3yDaZe', 'ROLE_STUDENT'),
(12, 'Carlos Ferreira', 'carlos.ferreira@teste.com', '$2a$10$Ko2jMCyj3AP1h16aDtRj0u3RjSKIWABpv4wqhzW2tNjqHuY9I8e1q', 'ROLE_STUDENT'),
(13, 'Rafael Costa', 'rafael.costa@teste.com', '$2a$10$fYVTmalIUgMWNM8wWRJeSOkvi4Qh/CBU3GG8prg2Rc2YHvLsFj3na', 'ROLE_STUDENT'),
(14, 'Jéssica Santos', 'jessica.santos@teste.com', '$2a$10$.xzyOPWYEIQM6rbled/YF.7dPMnRqQ/fJR0QG0J/3FnaFfhujHfw6', 'ROLE_STUDENT'),
(15, 'Aline Mendes', 'aline.mendes@teste.com', '$2a$10$wlWsgU9o6FrqpoGSMrPVoudbkUkv/URVLIzqXnEBnS3Y/xcPV4rLm', 'ROLE_STUDENT'),
(16, 'Bruno Pires', 'bruno.pires@teste.com', '$2a$10$f8eUto42aIFBdWyp1ZLQJ.R7PikeYPBBvVgGGISMLygIome6V6FWm', 'ROLE_STUDENT'),
(17, 'Eduardo Neves', 'eduardo.neves@teste.com', '$2a$10$3u4shoC.1Vmhhn71Ravk1eDdwyKe.rpj/0hCL9X/JlaJlNU86bnJq', 'ROLE_STUDENT'),
(18, 'Gabriela Alves', 'gabriela.alves@teste.com', '$2a$10$RZTN6c.xVGS6RSdV3usjpOsiJIri5onEImfujWGStvKVzCSI89WRy', 'ROLE_STUDENT'),
(19, 'Isabela Xavier', 'isabela.xavier@teste.com', '$2a$10$G/k/HYqUsMceWmUo7p4kAOh.y5VVQ585iag7myVoip6HPWcGF.72C', 'ROLE_STUDENT'),
(20, 'Lucas Barbosa', 'lucas.barbosa@teste.com', '$2a$10$6.zWtvep0crIhz0hRS5sH.DlQkj/ZVFzYL7IB7gzq62mtAF2E39Ye', 'ROLE_STUDENT'),
(21, 'Thiago Martins', 'thiago.martins@teste.com', '$2a$10$fSfL9tkH4QvkdI3YAl8uu.0Lxl.7keSaC3RzkUYDgRmuu9y2QLZRO', 'ROLE_STUDENT');


SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));

-- ========================================================
-- 2. INSTRUCTORS (Subclasse) - IDs 1 a 9
-- ========================================================
INSERT INTO instructors (id) VALUES
(1), (2), (3), (4), (5), (6), (7), (8), (9); -- 9 Instrutores

-- ========================================================
-- 3. STUDENTS (Subclasse) - IDs 10 a 21
-- ========================================================
INSERT INTO students (id) VALUES
(10), (11), (12), (13), (14), (15), (16), (17), (18), (19), (20), (21); -- 12 Alunos

-- ========================================================
-- 4. COURSES (Cursos)
-- ========================================================
INSERT INTO courses (id, title, description, workload, image_url) OVERRIDING SYSTEM VALUE VALUES
(1, 'Introdução ao Spring Boot', 'Curso completo de introdução ao desenvolvimento de APIs com Spring Boot e Java.', 40, 'http://image.teste/springboot'),
(2, 'Desenvolvimento Web com React', 'Aprenda a criar interfaces de usuário modernas e reativas usando a biblioteca React e seus principais hooks.', 60, 'http://image.teste/react'),
(3, 'Banco de Dados PostgreSQL Avançado', 'Domine consultas complexas, otimização e administração de bancos de dados PostgreSQL.', 30, 'http://image.teste/postgresql'),
(4, 'Arquitetura de Microsserviços', 'Projete e implemente sistemas escaláveis usando o padrão de microsserviços.', 50, 'http://image.teste/microservices'),
(5, 'Design Thinking e UX/UI', 'Entenda o processo de Design Thinking e crie experiências de usuário intuitivas e eficientes.', 20, 'http://image.teste/designthinking'),
(6, 'Introdução ao Python e Ciência de Dados', 'Primeiros passos em Python para análise de dados e machine learning.', 75, 'http://image.teste/python');

SELECT setval('courses_id_seq', (SELECT MAX(id) FROM courses));

-- ========================================================
-- 5. COURSE_INSTRUCTOR (Relação N:N) - Usando novos IDs de Instrutores (1-9)
-- ========================================================
INSERT INTO course_instructor (course_id, instructor_id) VALUES
(1, 1), -- Jaozada
(1, 2), -- Pedro
(2, 3), -- Juliana
(3, 1), -- Jaozada
(4, 4), -- Fernando
(5, 5), -- Diana
(6, 2), -- Pedro
(6, 4); -- Fernando
-- Se precisar de mais instrutores nos cursos, use IDs até 9

-- ========================================================
-- 6. MODULES (Módulos) - Sem alteração
-- ========================================================
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

-- ========================================================
-- 7. LESSONS (Aulas) - Sem alteração
-- ========================================================
INSERT INTO lessons (id, title, content, video_url, module_id, lesson_order) OVERRIDING SYSTEM VALUE VALUES
(1, 'Configurando o Ambiente', 'Configuração inicial do ambiente de desenvolvimento.', 'http://video.teste/sb/01', 1, 1),
(2, 'Endpoints REST e Verbos HTTP', 'Criação de endpoints REST com verbos HTTP.', 'http://video.teste/sb/02', 1, 2),
(3, 'Injeção de Dependência', 'Injeção de dependência no Spring Boot.', 'http://video.teste/sb/03', 1, 3),
(4, 'Criando Componentes Funcionais', 'Criação de componentes funcionais no React.', 'http://video.teste/react/01', 4, 1),
(5, 'Usando useEffect', 'Uso do hook useEffect no React.', 'http://video.teste/react/02', 5, 1),
(6, 'Usando Índices com Eficiência', 'Técnicas para otimizar consultas em PostgreSQL.', 'http://video.teste/pg/01', 6, 1),
(7, 'REST vs. gRPC', 'Padrões de comunicação em sistemas distribuídos.', 'http://video.teste/ms/01', 7, 1),
(8, 'Atomic Design', 'Criação e manutenção de sistemas de design.', 'http://video.teste/ux/01', 8, 1),
(9, 'Variáveis e Tipos de Dados', 'Primeiros passos com a linguagem Python.', 'http://video.teste/py/01', 9, 1);

SELECT setval('lessons_id_seq', (SELECT MAX(id) FROM lessons));

-- ========================================================
-- 8. ENROLLMENTS (Matrículas) - Usando novos IDs de Alunos (10-21)
-- ========================================================
INSERT INTO enrollments (id, student_id, course_id, enrollment_date, overall_progress, status) OVERRIDING SYSTEM VALUE VALUES
(1, 10, 1, CURRENT_DATE - INTERVAL '30 days', 33.3, 'IN_PROGRESS'), -- Maria Silva (ID 10)
(2, 11, 2, CURRENT_DATE - INTERVAL '60 days', 100.0, 'COMPLETED'), -- Ana Souza (ID 11)
(3, 12, 3, CURRENT_DATE - INTERVAL '15 days', 25.5, 'IN_PROGRESS'), -- Carlos Ferreira (ID 12)
(4, 13, 4, CURRENT_DATE, 0.0, 'IN_PROGRESS'), -- Rafael Costa (ID 13)
(5, 14, 5, CURRENT_DATE - INTERVAL '5 days', 10.0, 'IN_PROGRESS'), -- Jéssica Santos (ID 14)
(6, 15, 6, CURRENT_DATE - INTERVAL '10 days', 5.0, 'IN_PROGRESS'), -- Aline Mendes (ID 15)
(7, 16, 1, CURRENT_DATE - INTERVAL '2 days', 0.0, 'IN_PROGRESS'), -- Bruno Pires (ID 16)
(8, 11, 4, CURRENT_DATE - INTERVAL '10 days', 0.0, 'IN_PROGRESS'); -- Ana Souza (ID 11)

SELECT setval('enrollments_id_seq', (SELECT MAX(id) FROM enrollments));

-- ========================================================
-- 9. COMPLETED_LESSONS (Aulas Concluídas) - Sem alteração
-- ========================================================
-- As IDs de estudante são referenciadas através da tabela ENROLLMENTS
INSERT INTO completed_lessons (id, enrollment_id, lesson_id, completion_date) OVERRIDING SYSTEM VALUE VALUES
(1, 1, 1, NOW() - INTERVAL '29 days'),
(2, 1, 2, NOW() - INTERVAL '28 days'),
(3, 2, 4, NOW() - INTERVAL '59 days'),
(4, 3, 6, NOW() - INTERVAL '14 days'),
(5, 1, 3, NOW() - INTERVAL '27 days'),
(6, 6, 9, NOW() - INTERVAL '9 days');

SELECT setval('completed_lessons_id_seq', (SELECT MAX(id) FROM completed_lessons));

-- ========================================================
-- 10. TOPICS (Tópicos do Fórum) - Usando novos IDs de Usuários (1-9 INSTRUCTOR, 10-21 STUDENT)
-- user_id Original: 1(I), 8(S), 3(I), 2(S), 4(S), 5(S), 11(S), 12(S)
-- user_id Novo: 1(I), 14(S), 3(I), 11(S), 12(S), 13(S), 16(S), 17(S)
-- ========================================================
INSERT INTO topics (id, title, content, creation_date, course_id, user_id) OVERRIDING SYSTEM VALUE VALUES
(1, 'Problema ao Rodar o Projeto Base', 'Meu projeto Spring Boot não está subindo após a aula 2. Alguma dica?', NOW() - INTERVAL '5 days', 1, 10), -- Era ID 1 (Student) -> Agora ID 10 (Student)
(2, 'Melhores Práticas para useState', 'Como vocês organizam o código ao usar muitos `useState` em um único componente?', NOW() - INTERVAL '4 days', 2, 14), -- Era ID 8 (Student) -> Agora ID 14 (Student)
(3, 'Como funciona o Join Inheritance?', 'Gostaria de mais detalhes sobre como o `InheritanceType.JOINED` mapeia no banco de dados.', NOW() - INTERVAL '3 days', 1, 3), -- Era ID 3 (Instructor) -> Agora ID 3 (Instructor)
(4, 'Polymorphism Concept Doubt', 'I did not fully understand the concept of polymorphism in Lesson 3. Can someone give a practical example?', NOW() - INTERVAL '2 days', 1, 11), -- Era ID 2 (Student) -> Agora ID 11 (Student)
(5, 'Suggestion for Final Project', 'Could we create a small Library Management System as the final course project?', NOW() - INTERVAL '1 day', 1, 3), -- Era ID 3 (Instructor) -> Agora ID 3 (Instructor)
(6, 'Bug in Quiz Found', 'The Module 2 quiz marks the correct answer as wrong for question 5.', NOW() - INTERVAL '1 day', 2, 11), -- Era ID 4 (Student) -> Agora ID 11 (Student)
(7, 'Best Code Practices: Linters', 'What are the linter tools you use most often?', NOW() - INTERVAL '12 hours', 3, 12), -- Era ID 5 (Student) -> Agora ID 12 (Student)
(8, 'Thanks to the Instructor', 'I wanted to thank the instructor for the excellent material! Very didactic.', NOW() - INTERVAL '10 hours', 1, 11), -- Era ID 2 (Student) -> Agora ID 11 (Student)
(9, 'Error 404 on API Endpoint', 'I followed the steps in Lesson 4, but I keep getting a 404 error when trying to access the /users endpoint. Any ideas?', NOW() - INTERVAL '8 hours', 1, 10), -- Era ID 1 (Student) -> Agora ID 10 (Student)
(10, 'Which Database is Better for Microservices?', 'For a new microservice architecture, is PostgreSQL better than MongoDB for storing user profiles?', NOW() - INTERVAL '5 hours', 4, 17), -- Era ID 11 (Student) -> Agora ID 17 (Student)
(11, 'Need Help with Deployment', 'The React application works locally, but I am facing CORS issues after deploying to Vercel.', NOW() - INTERVAL '2 hours', 2, 18); -- Era ID 12 (Student) -> Agora ID 18 (Student)

SELECT setval('topics_id_seq', (SELECT MAX(id) FROM topics));

-- ========================================================
-- 11. RESPONSES (Respostas do Fórum) - Usando novos IDs de Usuários (1-9 INSTRUCTOR, 10-21 STUDENT)
-- user_id Original: 6(I), 1(S), 9(I), 7(I), 6(I), 2(S), 4(S), 7(I), 8(S), 5(S), 6(I), 9(I)
-- user_id Novo: 3(I), 10(S), 4(I), 3(I), 3(I), 11(S), 12(S), 3(I), 14(S), 12(S), 3(I), 4(I)
-- ========================================================
INSERT INTO responses (id, content, creation_date, topic_id, user_id, response_parent_id) OVERRIDING SYSTEM VALUE VALUES
(1, 'Verifique se você rodou `mvn clean install` antes de subir o projeto.', NOW() - INTERVAL '4 days 12 hours', 1, 3, NULL), -- Era ID 6 (Instructor) -> Agora ID 3 (Instructor)
(2, 'Obrigada, professor! Funcionou.', NOW() - INTERVAL '4 days 10 hours', 1, 10, 1), -- Era ID 1 (Student) -> Agora ID 10 (Student)
(3, 'Para muitos estados, recomendo usar o `useReducer` ou uma biblioteca de estado como Redux/Zustand.', NOW() - INTERVAL '3 days 20 hours', 2, 4, NULL), -- Era ID 9 (Instructor) -> Agora ID 4 (Instructor)
(4, 'O `InheritanceType.JOINED` cria uma tabela para a superclasse e uma tabela separada para cada subclasse, usando a PK da subclasse como FK para a superclasse.', NOW() - INTERVAL '2 days', 3, 3, NULL), -- Era ID 7 (Instructor) -> Agora ID 3 (Instructor)
(5, 'Polymorphism allows you to treat objects of different classes that share a common superclass or interface uniformly. E.g., a "Dog" and a "Cat" can both be treated as an "Animal" when calling a "makeSound" method.', NOW() - INTERVAL '1 day 10 hours', 4, 3, NULL), -- Era ID 6 (Instructor) -> Agora ID 3 (Instructor)
(6, 'Thanks for the tip! That makes sense to centralize it in the Service layer.', NOW() - INTERVAL '1 day 8 hours', 4, 11, 5), -- Era ID 2 (Student) -> Agora ID 11 (Student)
(7, 'But what if I have a transaction across more than one Service? What should I do?', NOW() - INTERVAL '1 day 6 hours', 4, 12, 5), -- Era ID 4 (Student) -> Agora ID 12 (Student)
(8, 'We checked the quiz. The issue was fixed an hour ago. Please try again!', NOW() - INTERVAL '1 day 4 hours', 6, 3, NULL), -- Era ID 7 (Instructor) -> Agora ID 3 (Instructor)
(9, 'ESLint for JavaScript and SonarQube/Checkstyle for Java are standard tools in most companies.', NOW() - INTERVAL '10 hours', 7, 14, NULL), -- Era ID 8 (Student) -> Agora ID 14 (Student)
(10, 'I agree. JUnit 5 has great features like @ParameterizedTest for testing.', NOW() - INTERVAL '9 hours', 7, 12, 9), -- Era ID 5 (Student) -> Agora ID 12 (Student)
(11, 'Did you remember to add `@RestController` to your Controller class? That is a common mistake.', NOW() - INTERVAL '7 hours', 9, 3, NULL), -- Era ID 6 (Instructor) -> Agora ID 3 (Instructor)
(12, 'I also recommend checking the application.properties file to see if the port is configured correctly.', NOW() - INTERVAL '5 hours', 9, 4, 11); -- Era ID 9 (Instructor) -> Agora ID 4 (Instructor)

SELECT setval('responses_id_seq', (SELECT MAX(id) FROM responses));

-- ========================================================
-- 12. REVIEWS (Avaliações de Curso) - Usando novos IDs de Alunos (10-21)
-- student_id Original: 1, 2, 3, 4, 5, 11, 12
-- student_id Novo: 10, 11, 12, 13, 14, 15, 16
-- ========================================================
INSERT INTO reviews (id, rating, comment, review_date, student_id, course_id) OVERRIDING SYSTEM VALUE VALUES
(1, 5, 'Curso excelente, o Prof. João explica muito bem. Conteúdo prático e direto.', NOW() - INTERVAL '15 days', 10, 1), -- Maria (Aluno 10)
(2, 4, 'Ótimo curso de React, mas senti falta de mais exemplos de projetos práticos no final.', NOW() - INTERVAL '30 days', 11, 2), -- Ana (Aluno 11)
(3, 5, 'Material de PostgreSQL muito completo. Valeu o investimento!', NOW() - INTERVAL '5 days', 12, 3), -- Carlos (Aluno 12)
(4, 5, 'Very complete and well-explained content. I loved the practical part.', NOW() - INTERVAL '40 days', 11, 1), -- Ana (Aluno 11), Course 1
(5, 4, 'The instructor is good, but the support material could be more detailed.', NOW() - INTERVAL '35 days', 12, 1), -- Carlos (Aluno 12), Course 1
(6, 5, 'Excellent course, especially the last module!', NOW() - INTERVAL '25 days', 13, 2), -- Rafael (Aluno 13), Course 2
(7, 3, 'I had some problems with the exercises, but the theoretical material is solid.', NOW() - INTERVAL '20 days', 14, 3), -- Jéssica (Aluno 14), Course 3
(8, 5, 'I recommend it to everyone who wants to learn Spring Boot.', NOW() - INTERVAL '10 days', 11, 3), -- Ana (Aluno 11), Course 3
(9, 4, 'Good introduction to Design Thinking, although the content was too short.', NOW() - INTERVAL '7 days', 10, 5), -- Maria (Aluno 10), Course 5
(10, 5, 'The best course I have taken on microservices. Highly complex but explained clearly.', NOW() - INTERVAL '3 days', 15, 4), -- Aline (Aluno 15), Course 4
(11, 2, 'The React course is outdated. It needs an update to modern hooks and context API.', NOW() - INTERVAL '1 day', 16, 2); -- Bruno (Aluno 16), Course 2

SELECT setval('reviews_id_seq', (SELECT MAX(id) FROM reviews));

-- ========================================================
-- 13. CATEGORIES E COURSE_CATEGORY - Sem alteração
-- ========================================================
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