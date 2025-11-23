-- --------------------------------------------------------
-- 1. ALUNOS
-- --------------------------------------------------------
INSERT INTO alunos (nome, email, senha) VALUES
('Ana Silva', 'ana.silva@teste.com', 'hashed_senha_1'),
('Bruno Costa', 'bruno.costa@teste.com', 'hashed_senha_2'),
('Carla Dias', 'carla.dias@teste.com', 'hashed_senha_3'),
('Daniel Melo', 'daniel.melo@teste.com', 'hashed_senha_4'),
('Erica Nunes', 'erica.nunes@teste.com', 'hashed_senha_5');


-- --------------------------------------------------------
-- 2. INSTRUTORES
-- --------------------------------------------------------
INSERT INTO instrutores (nome, email, senha) VALUES
('Prof. João', 'joao.prof@teste.com', 'hashed_instrutor_1'),
('Dra. Laura', 'laura.prof@teste.com', 'hashed_instrutor_2'),
('Eng. Ricardo', 'ricardo.prof@teste.com', 'hashed_instrutor_3'),
('Marta Lima', 'marta.prof@teste.com', 'hashed_instrutor_4'),
('Dr. Felipe', 'felipe.prof@teste.com', 'hashed_instrutor_5');


-- --------------------------------------------------------
-- 3. CURSOS
-- --------------------------------------------------------
INSERT INTO cursos (titulo, descricao, carga_horaria) VALUES
('Introdução ao Spring Boot', 'Curso completo de introdução ao desenvolvimento de APIs com Spring Boot e Java.', 40),
('Desenvolvimento Web com React', 'Aprenda a criar interfaces de usuário modernas e reativas usando a biblioteca React e seus principais hooks.', 60),
('Banco de Dados PostgreSQL Avançado', 'Domine consultas complexas, otimização e administração de bancos de dados PostgreSQL.', 30),
('Arquitetura de Microsserviços', 'Projete e implemente sistemas escaláveis usando o padrão de microsserviços.', 50),
('Design Thinking e UX/UI', 'Entenda o processo de Design Thinking e crie experiências de usuário intuitivas e eficientes.', 20);

-- --------------------------------------------------------
-- 4. CURSO_INSTRUTOR (Relação N:N)
-- --------------------------------------------------------
INSERT INTO curso_instrutor (curso_id, instrutor_id) VALUES
(1, 1), -- Spring Boot (ID 1) -> Prof. João (ID 1)
(1, 2), -- Spring Boot (ID 1) -> Dra. Laura (ID 2)
(2, 3), -- React (ID 2) -> Eng. Ricardo (ID 3)
(3, 1), -- PostgreSQL (ID 3) -> Prof. João (ID 1)
(4, 4), -- Microsserviços (ID 4) -> Marta Lima (ID 4)
(5, 5); -- Design Thinking (ID 5) -> Dr. Felipe (ID 5)

-- --------------------------------------------------------
-- 5. MÓDULOS (depende de Cursos)
-- --------------------------------------------------------
INSERT INTO modulos (titulo, curso_id, ordem) VALUES
('Fundamentos do Spring Boot', 1, 1), -- Curso 1
('Desenvolvimento com Controllers', 1, 2), -- Curso 1
('Introdução ao React', 2, 1), -- Curso 2
('Otimização de Queries', 3, 1), -- Curso 3
('Padrões de Comunicação', 4, 1); -- Curso 4


-- --------------------------------------------------------
-- 6. AULAS (depende de Módulos)
-- --------------------------------------------------------
INSERT INTO aulas (titulo, url_video, modulo_id, ordem) VALUES
('Configurando o Ambiente', 'http://video.teste/sb/01', 1, 1), -- Módulo 1
('Endpoints REST e Verbos HTTP', 'http://video.teste/sb/02', 1, 2),
('Criando Componentes Funcionais', 'http://video.teste/react/01', 3, 1), -- Módulo 3
('Usando Índices com Eficiência', 'http://video.teste/pg/01', 4, 1), -- Módulo 4
('REST vs. gRPC', 'http://video.teste/ms/01', 5, 1); -- Módulo 5


-- --------------------------------------------------------
-- 7. MATRÍCULAS (depende de Alunos e Cursos)
-- --------------------------------------------------------
-- 'EM_ANDAMENTO', 'CONCLUIDO', 'CANCELADO'
INSERT INTO matriculas (aluno_id, curso_id, data_matricula, progresso_geral, status) VALUES
(1, 1, CURRENT_DATE - INTERVAL '30 days', 50.0, 'EM_ANDAMENTO'), -- Ana no Spring Boot
(2, 2, CURRENT_DATE - INTERVAL '60 days', 100.0, 'CONCLUIDO'), -- Bruno no React
(3, 3, CURRENT_DATE - INTERVAL '15 days', 25.5, 'EM_ANDAMENTO'), -- Carla no PG
(4, 4, CURRENT_DATE, 0.0, 'EM_ANDAMENTO'), -- Daniel no Microsserviços
(5, 5, CURRENT_DATE - INTERVAL '5 days', 10.0, 'EM_ANDAMENTO'); -- Erica no Design


-- --------------------------------------------------------
-- 8. AULAS CONCLUÍDAS (depende de Matrículas e Aulas)
-- --------------------------------------------------------
INSERT INTO aulas_concluidas (matricula_id, aula_id, data_conclusao) VALUES
(1, 1, NOW() - INTERVAL '29 days'), -- Ana concluiu a Aula 1
(1, 2, NOW() - INTERVAL '28 days'), -- Ana concluiu a Aula 2
(2, 3, NOW() - INTERVAL '59 days'), -- Bruno concluiu a Aula 3 (Curso 2)
(3, 4, NOW() - INTERVAL '14 days'); -- Carla concluiu a Aula 4 (Curso 3)