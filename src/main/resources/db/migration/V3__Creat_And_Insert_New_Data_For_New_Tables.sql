-- Tabela: topicos
CREATE TABLE topicos (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    conteudo TEXT NOT NULL,
    data_criacao TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Chaves estrangeiras
    curso_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    
    -- Restrições de Chave Estrangeira
    FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Índices (para otimizar buscas por curso e usuário)
CREATE INDEX idx_topicos_curso_id ON topicos (curso_id);
CREATE INDEX idx_topicos_usuario_id ON topicos (usuario_id);

-- Exemplo de inserção (5 tópicos)
INSERT INTO topicos (titulo, conteudo, curso_id, usuario_id) VALUES
('Dúvida sobre Módulo 1: Introdução', 'Não entendi bem o conceito de polimorfismo na aula 3. Alguém pode dar um exemplo prático?', 1, 2), -- Curso 1, Usuário 2
('Sugestão de projeto final', 'Podemos criar um pequeno sistema de gerenciamento de biblioteca como projeto de final de curso?', 1, 3), -- Curso 1, Usuário 3
('Bug encontrado no quiz', 'O quiz do módulo 2 está marcando a resposta correta como errada para a pergunta 5.', 2, 4), -- Curso 2, Usuário 4
('Melhores práticas de código', 'Quais são as ferramentas de linter que vocês mais utilizam no dia a dia?', 3, 5), -- Curso 3, Usuário 5
('Agradecimento ao instrutor', 'Queria agradecer ao instrutor pelo excelente material! Muito didático.', 1, 2);

-- Tabela: respostas (com auto-referência para comentários aninhados)
CREATE TABLE respostas (
    id BIGSERIAL PRIMARY KEY,
    conteudo TEXT NOT NULL,
    data_criacao TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Colunas Proprietárias/FKs
    topico_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    resposta_pai_id BIGINT, -- ⬅️ Esta é a chave que define a hierarquia

    -- Restrições
    FOREIGN KEY (topico_id) REFERENCES topicos(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (resposta_pai_id) REFERENCES respostas(id) ON DELETE CASCADE -- Auto-referência
);

-- Exemplo de inserção (5 respostas)
INSERT INTO respostas (conteudo, topico_id, usuario_id, resposta_pai_id) VALUES
-- Respostas ao Tópico 1 (ID: 1)
('Geralmente no Service, pois ele lida com a lógica de negócio e transações de banco de dados.', 1, 3, NULL), -- ID Gerado: 1
-- Resposta ao Tópico 3 (ID: 3)
('Você verificou se o método no Controller está anotado corretamente, ex: @PreAuthorize("hasRole(''ALUNO'')")?', 3, 1, NULL), -- ID Gerado: 2
-- Resposta ao Tópico 4 (ID: 4)
('JUnit 5 é o padrão de mercado atual. Integra-se perfeitamente com o Spring Boot.', 4, 2, NULL), -- ID Gerado: 3
-- Resposta ao Comentário 1 (Tópico 1): Aninhamento de 1º Nível
('Obrigado pela dica! Faz sentido concentrar no Service.', 1, 2, 1), 
-- Resposta ao Comentário 3 (Tópico 4): Aninhamento de 1º Nível
('Concordo. O JUnit 5 tem recursos ótimos como o @ParameterizedTest.', 4, 5, 3), 
-- Resposta ao Comentário 4 (Tópico 1): Aninhamento de 2º Nível (exemplo de thread)
('Mas e se eu tiver uma transação em mais de um Service? O que fazer?', 1, 4, 1);

-- Tabela: avaliacoes
CREATE TABLE avaliacoes (
    id BIGSERIAL PRIMARY KEY,
    nota INTEGER NOT NULL CHECK (nota >= 1 AND nota <= 5), -- Nota entre 1 e 5
    comentario TEXT,
    data_avaliacao TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Chaves estrangeiras
    aluno_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,
    
    -- Restrições de Chave Estrangeira
    FOREIGN KEY (aluno_id) REFERENCES alunos(id) ON DELETE CASCADE,
    FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE,

    -- Restrição de Unicidade (Obrigatória para garantir que o aluno só avalie o curso uma vez)
    UNIQUE (aluno_id, curso_id)
);

-- Índices
CREATE INDEX idx_avaliacoes_curso_id ON avaliacoes (curso_id);

-- Exemplo de inserção (5 avaliações)
INSERT INTO avaliacoes (nota, comentario, aluno_id, curso_id) VALUES
(5, 'Conteúdo muito completo e bem explicado. Adorei a parte prática.', 2, 1), -- Aluno 2, Curso 1
(4, 'O instrutor é bom, mas o material de apoio poderia ser mais detalhado.', 3, 1), -- Aluno 3, Curso 1
(5, 'Excelente curso, especialmente o último módulo!', 4, 2), -- Aluno 4, Curso 2
(3, 'Tive alguns problemas com os exercícios, mas o material teórico é sólido.', 5, 3), -- Aluno 5, Curso 3
(5, 'Recomendo a todos que querem aprender Spring Boot.', 2, 3); -- Aluno 2, Curso 3