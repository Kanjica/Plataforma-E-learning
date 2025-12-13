-- --------------------------------------------------------
-- Tabela de Usuários (Superclasse para Autenticação)
-- --------------------------------------------------------
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL -- Campo para armazenar ROLE_ALUNO, ROLE_INSTRUTOR, etc.
    -- Outros campos de segurança, como 'ativo', 'data_criacao'
);
-- --------------------------------------------------------
-- Tabela de Alunos
-- Mapeada de: com.lp3.elearning.entities.Aluno
-- --------------------------------------------------------
-- --------------------------------------------------------
-- Tabela de Alunos (Subclasse)
-- Não contém colunas de login/senha, usa a PK como FK
-- --------------------------------------------------------
CREATE TABLE alunos (
    id BIGINT PRIMARY KEY, -- PK e FK para usuarios(id)
    
    CONSTRAINT fk_aluno_usuario FOREIGN KEY (id) REFERENCES usuarios(id) ON DELETE CASCADE
);
-- --------------------------------------------------------
-- Tabela de Instrutores
-- Mapeada de: com.lp3.elearning.entities.Instrutor
-- --------------------------------------------------------
CREATE TABLE instrutores (
    id BIGINT PRIMARY KEY, -- PK e FK para usuarios(id)
    -- Adicione campos específicos do Instrutor, se houver

    CONSTRAINT fk_instrutor_usuario FOREIGN KEY (id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Tabela de Cursos
-- Mapeada de: com.lp3.elearning.entities.Curso
-- --------------------------------------------------------
CREATE TABLE cursos (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    descricao TEXT NOT NULL,
    carga_horaria INTEGER NOT NULL
);

-- --------------------------------------------------------
-- Tabela de Módulos
-- Mapeada de: com.lp3.elearning.entities.Modulo
-- --------------------------------------------------------
CREATE TABLE modulos (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    ordem INTEGER NOT NULL,
    curso_id BIGINT NOT NULL,

    CONSTRAINT fk_modulo_curso FOREIGN KEY (curso_id) REFERENCES cursos(id)
);

-- --------------------------------------------------------
-- Tabela de Aulas
-- Mapeada de: com.lp3.elearning.entities.Aula
-- --------------------------------------------------------
CREATE TABLE aulas (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    url_video VARCHAR(255) NOT NULL,
    ordem INTEGER NOT NULL,
    modulo_id BIGINT NOT NULL,

    CONSTRAINT fk_aula_modulo FOREIGN KEY (modulo_id) REFERENCES modulos(id)
);

-- --------------------------------------------------------
-- Tabela de Matrículas (Tabela de Relacionamento Aluno-Curso)
-- Mapeada de: com.lp3.elearning.entities.Matricula
-- --------------------------------------------------------
CREATE TABLE matriculas (
    id BIGSERIAL PRIMARY KEY,
    data_matricula DATE NOT NULL,
    progresso_geral DOUBLE PRECISION NOT NULL,
    status VARCHAR(50) NOT NULL,
    aluno_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,

    CONSTRAINT fk_matricula_aluno FOREIGN KEY (aluno_id) REFERENCES alunos(id),
    CONSTRAINT fk_matricula_curso FOREIGN KEY (curso_id) REFERENCES cursos(id),

    CONSTRAINT unq_aluno_curso UNIQUE (aluno_id, curso_id)
);

-- --------------------------------------------------------
-- Tabela de Aulas Concluídas
-- Mapeada de: com.lp3.elearning.entities.AulaConcluida
-- --------------------------------------------------------
CREATE TABLE aulas_concluidas (
    id BIGSERIAL PRIMARY KEY,
    data_conclusao TIMESTAMP WITHOUT TIME ZONE NOT NULL, -- Mapeado de LocalDateTime
    matricula_id BIGINT NOT NULL,
    aula_id BIGINT NOT NULL,

    CONSTRAINT fk_concluida_matricula FOREIGN KEY (matricula_id) REFERENCES matriculas(id),
    CONSTRAINT fk_concluida_aula FOREIGN KEY (aula_id) REFERENCES aulas(id),

    CONSTRAINT unq_matricula_aula UNIQUE (matricula_id, aula_id)
);

-- --------------------------------------------------------
-- Tabela de Junção Many-to-Many: Curso-Instrutor
-- Mapeada de: curso_instrutor
-- --------------------------------------------------------
CREATE TABLE curso_instrutor (
    curso_id BIGINT NOT NULL,
    instrutor_id BIGINT NOT NULL,

    PRIMARY KEY (curso_id, instrutor_id),
    CONSTRAINT fk_ci_curso FOREIGN KEY (curso_id) REFERENCES cursos(id),
    CONSTRAINT fk_ci_instrutor FOREIGN KEY (instrutor_id) REFERENCES instrutores(id)
);