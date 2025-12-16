-- V1__Create_All_Schema.sql
-- Contém todas as instruções CREATE TABLE para a aplicação Elearning.

-- --------------------------------------------------------
-- Tabela de Usuários (Superclasse para Autenticação)
-- --------------------------------------------------------
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- --------------------------------------------------------
-- Tabela de Alunos (Subclasse)
-- --------------------------------------------------------
CREATE TABLE students (
    id BIGINT PRIMARY KEY,
    CONSTRAINT fk_student_user FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Tabela de Instrutores
-- --------------------------------------------------------
CREATE TABLE instructors (
    id BIGINT PRIMARY KEY,
    CONSTRAINT fk_instructor_user FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Tabela de Cursos
-- --------------------------------------------------------
CREATE TABLE courses (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    workload INTEGER NOT NULL,
    image_url TEXT NOT NULL,
    
    price NUMERIC(10, 2) NOT NULL,
    old_price NUMERIC(10, 2) NOT NULL,
    is_best_seller BOOLEAN NOT NULL,
    
    CONSTRAINT chk_courses_price_positive CHECK (price >= 0.00),
    CONSTRAINT chk_courses_old_price_positive CHECK (old_price >= 0.00)
);
-- --------------------------------------------------------
-- Tabela de Módulos
-- --------------------------------------------------------
CREATE TABLE modules (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    module_order INTEGER NOT NULL,
    course_id BIGINT NOT NULL,
    CONSTRAINT fk_module_course FOREIGN KEY (course_id) REFERENCES courses(id)
);

-- --------------------------------------------------------
-- Tabela de Aulas
-- --------------------------------------------------------
CREATE TABLE lessons (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    content TEXT NOT NULL,
    video_url VARCHAR(255) NOT NULL,
    lesson_order INTEGER NOT NULL,
    module_id BIGINT NOT NULL,
    CONSTRAINT fk_lesson_module FOREIGN KEY (module_id) REFERENCES modules(id)
);

-- --------------------------------------------------------
-- Tabela de Matrículas (Aluno-Curso)
-- --------------------------------------------------------
CREATE TABLE enrollments (
    id BIGSERIAL PRIMARY KEY,
    enrollment_date DATE NOT NULL,
    overall_progress DOUBLE PRECISION NOT NULL,
    status VARCHAR(50) NOT NULL,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    CONSTRAINT fk_enrollment_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_enrollment_course FOREIGN KEY (course_id) REFERENCES courses(id),
    CONSTRAINT unq_student_course UNIQUE (student_id, course_id)
);

-- --------------------------------------------------------
-- Tabela de Aulas Concluídas
-- --------------------------------------------------------
CREATE TABLE completed_lessons (
    id BIGSERIAL PRIMARY KEY,
    completion_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    enrollment_id BIGINT NOT NULL,
    lesson_id BIGINT NOT NULL,
    CONSTRAINT fk_completed_enrollment FOREIGN KEY (enrollment_id) REFERENCES enrollments(id),
    CONSTRAINT fk_completed_lesson FOREIGN KEY (lesson_id) REFERENCES lessons(id),
    CONSTRAINT unq_enrollment_lesson UNIQUE (enrollment_id, lesson_id)
);

-- --------------------------------------------------------
-- Tabela de Junção Many-to-Many: Course-Instructor
-- --------------------------------------------------------
CREATE TABLE course_instructor (
    course_id BIGINT NOT NULL,
    instructor_id BIGINT NOT NULL,
    PRIMARY KEY (course_id, instructor_id),
    CONSTRAINT fk_ci_course FOREIGN KEY (course_id) REFERENCES courses(id),
    CONSTRAINT fk_ci_instructor FOREIGN KEY (instructor_id) REFERENCES instructors(id)
);

-- --------------------------------------------------------
-- Tabela de Tópicos do Fórum
-- --------------------------------------------------------
CREATE TABLE topics (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    course_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE INDEX idx_topics_course_id ON topics (course_id);
CREATE INDEX idx_topics_user_id ON topics (user_id);

-- --------------------------------------------------------
-- Tabela de Respostas do Fórum
-- --------------------------------------------------------
CREATE TABLE responses (
    id BIGSERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    topic_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    response_parent_id BIGINT,
    FOREIGN KEY (topic_id) REFERENCES topics(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (response_parent_id) REFERENCES responses(id) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Tabela de Avaliações de Curso
-- --------------------------------------------------------
CREATE TABLE reviews (
    id BIGSERIAL PRIMARY KEY,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    review_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    UNIQUE (student_id, course_id)
);
CREATE INDEX idx_reviews_course_id ON reviews (course_id);

CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);
-- --------------------------------------------------------
-- Tabela de Junção Many-to-Many: Course-Category
-- --------------------------------------------------------
CREATE TABLE course_category (
    course_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (course_id, category_id),
    CONSTRAINT fk_cc_course FOREIGN KEY (course_id) REFERENCES courses(id),
    CONSTRAINT fk_cc_category FOREIGN KEY (category_id) REFERENCES categories(id)
);