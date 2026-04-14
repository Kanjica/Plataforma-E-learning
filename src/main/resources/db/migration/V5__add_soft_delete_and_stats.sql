-- 1. Adicionando Soft Delete (padrão Hibernate 6.4+)
ALTER TABLE courses ADD COLUMN deleted BOOLEAN DEFAULT false NOT NULL;
ALTER TABLE modules ADD COLUMN deleted BOOLEAN DEFAULT false NOT NULL;

-- 2. Adicionando campos de estatísticas ao Course
ALTER TABLE courses ADD COLUMN average_rating NUMERIC(3,2) DEFAULT 0.00 NOT NULL;
ALTER TABLE courses ADD COLUMN total_reviews INTEGER DEFAULT 0 NOT NULL;

-- 3. Garantindo que os registros existentes não fiquem inconsistentes
UPDATE courses SET deleted = false, average_rating = 0.00, total_reviews = 0;
UPDATE modules SET deleted = false;