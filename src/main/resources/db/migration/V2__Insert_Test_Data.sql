-- Flyway executa todas as sentenças em uma transação por padrão.

-- --------------------------------------------------------
-- 1. USUÁRIOS (Superclasse: Armazena login e dados comuns)
-- A cláusula OVERRIDING SYSTEM VALUE permite inserir IDs fixos 
-- em colunas BIGSERIAL/SERIAL.
-- --------------------------------------------------------

-- Inserção de todos os 10 usuários (Alunos 1-5, Instrutores 6-10)
INSERT INTO usuarios (id, nome, email, senha, role) OVERRIDING SYSTEM VALUE VALUES 
(1, 'Ana Silva', 'ana.silva@teste.com', 'hashed_senha_1', 'ROLE_ALUNO'),   
(2, 'Bruno Costa', 'bruno.costa@teste.com', 'hashed_senha_2', 'ROLE_ALUNO'),
(3, 'Carla Dias', 'carla.dias@teste.com', 'hashed_senha_3', 'ROLE_ALUNO'), 
(4, 'Daniel Melo', 'daniel.melo@teste.com', 'hashed_senha_4', 'ROLE_ALUNO'), 
(5, 'Erica Nunes', 'erica.nunes@teste.com', 'hashed_senha_5', 'ROLE_ALUNO'), 
(6, 'Prof. João', 'joao.prof@teste.com', 'hashed_instrutor_1', 'ROLE_INSTRUTOR'),
(7, 'Dra. Laura', 'laura.prof@teste.com', 'hashed_instrutor_2', 'ROLE_INSTRUTOR'),
(8, 'Eng. Ricardo', 'ricardo.prof@teste.com', 'hashed_instrutor_3', 'ROLE_INSTRUTOR'),
(9, 'Marta Lima', 'marta.prof@teste.com', 'hashed_instrutor_4', 'ROLE_INSTRUTOR'),
(10, 'Dr. Felipe', 'felipe.prof@teste.com', 'hashed_instrutor_5', 'ROLE_INSTRUTOR');

-- *** Atualiza a sequência 'usuarios_id_seq' para o próximo valor (11) ***
SELECT setval('usuarios_id_seq', (SELECT MAX(id) FROM usuarios) + 1);

-- --------------------------------------------------------
-- 2. ALUNOS (Subclasse: Apenas relaciona o ID com a Superclasse)
-- O ID é a FK para usuarios.
-- --------------------------------------------------------
INSERT INTO alunos (id) VALUES
(1), -- Ana Silva
(2), -- Bruno Costa
(3), -- Carla Dias
(4), -- Daniel Melo
(5); -- Erica Nunes

-- --------------------------------------------------------
-- 3. INSTRUTORES (Subclasse: Apenas relaciona o ID com a Superclasse)
-- --------------------------------------------------------
INSERT INTO instrutores (id) VALUES
(6), -- Prof. João
(7), -- Dra. Laura
(8), -- Eng. Ricardo
(9), -- Marta Lima
(10); -- Dr. Felipe

-- --------------------------------------------------------
-- 4. CURSOS
-- 
INSERT INTO cursos (id, titulo, descricao, carga_horaria) OVERRIDING SYSTEM VALUE VALUES
(1, 'Introdução ao Spring Boot', 'Curso completo de introdução ao desenvolvimento de APIs com Spring Boot e Java.', 40),
(2, 'Desenvolvimento Web com React', 'Aprenda a criar interfaces de usuário modernas e reativas usando a biblioteca React e seus principais hooks.', 60),
(3, 'Banco de Dados PostgreSQL Avançado', 'Domine consultas complexas, otimização e administração de bancos de dados PostgreSQL.', 30),
(4, 'Arquitetura de Microsserviços', 'Projete e implemente sistemas escaláveis usando o padrão de microsserviços.', 50),
(5, 'Design Thinking e UX/UI', 'Entenda o processo de Design Thinking e crie experiências de usuário intuitivas e eficientes.', 20);
SELECT setval('cursos_id_seq', (SELECT MAX(id) FROM cursos) + 1);


-- --------------------------------------------------------
-- 5. CURSO_INSTRUTOR (Relação N:N)
-- --------------------------------------------------------
INSERT INTO curso_instrutor (curso_id, instrutor_id) VALUES
(1, 6), -- Spring Boot (ID 1) -> Prof. João (ID 6)
(1, 7), -- Spring Boot (ID 1) -> Dra. Laura (ID 7)
(2, 8), -- React (ID 2) -> Eng. Ricardo (ID 8)
(3, 6), -- PostgreSQL (ID 3) -> Prof. João (ID 6)
(4, 9), -- Microsserviços (ID 4) -> Marta Lima (ID 9)
(5, 10); -- Design Thinking (ID 5) -> Dr. Felipe (ID 10)


-- --------------------------------------------------------
-- 6. MÓDULOS (depende de Cursos)
-- --------------------------------------------------------
INSERT INTO modulos (id, titulo, curso_id, ordem) OVERRIDING SYSTEM VALUE VALUES
(1, 'Fundamentos do Spring Boot', 1, 1),
(2, 'Desenvolvimento com Controllers', 1, 2),
(3, 'Introdução ao React', 2, 1),
(4, 'Otimização de Queries', 3, 1),
(5, 'Padrões de Comunicação', 4, 1);
SELECT setval('modulos_id_seq', (SELECT MAX(id) FROM modulos) + 1);


-- --------------------------------------------------------
-- 7. AULAS (depende de Módulos)
-- --------------------------------------------------------
INSERT INTO aulas (id, titulo, url_video, modulo_id, ordem) OVERRIDING SYSTEM VALUE VALUES
(1, 'Configurando o Ambiente', 'http://video.teste/sb/01', 1, 1),
(2, 'Endpoints REST e Verbos HTTP', 'http://video.teste/sb/02', 1, 2),
(3, 'Criando Componentes Funcionais', 'http://video.teste/react/01', 3, 1),
(4, 'Usando Índices com Eficiência', 'http://video.teste/pg/01', 4, 1),
(5, 'REST vs. gRPC', 'http://video.teste/ms/01', 5, 1);
SELECT setval('aulas_id_seq', (SELECT MAX(id) FROM aulas) + 1);


-- --------------------------------------------------------
-- 8. MATRÍCULAS (depende de Alunos e Cursos)
-- --------------------------------------------------------
INSERT INTO matriculas (id, aluno_id, curso_id, data_matricula, progresso_geral, status) OVERRIDING SYSTEM VALUE VALUES
(1, 1, 1, CURRENT_DATE - INTERVAL '30 days', 50.0, 'EM_ANDAMENTO'),
(2, 2, 2, CURRENT_DATE - INTERVAL '60 days', 100.0, 'CONCLUIDO'),
(3, 3, 3, CURRENT_DATE - INTERVAL '15 days', 25.5, 'EM_ANDAMENTO'),
(4, 4, 4, CURRENT_DATE, 0.0, 'EM_ANDAMENTO'),
(5, 5, 5, CURRENT_DATE - INTERVAL '5 days', 10.0, 'EM_ANDAMENTO');
SELECT setval('matriculas_id_seq', (SELECT MAX(id) FROM matriculas) + 1);


-- --------------------------------------------------------
-- 9. AULAS CONCLUÍDAS (depende de Matrículas e Aulas)
-- --------------------------------------------------------
INSERT INTO aulas_concluidas (id, matricula_id, aula_id, data_conclusao) OVERRIDING SYSTEM VALUE VALUES
(1, 1, 1, NOW() - INTERVAL '29 days'),
(2, 1, 2, NOW() - INTERVAL '28 days'),
(3, 2, 3, NOW() - INTERVAL '59 days'),
(4, 3, 4, NOW() - INTERVAL '14 days');
SELECT setval('aulas_concluidas_id_seq', (SELECT MAX(id) FROM aulas_concluidas) + 1);