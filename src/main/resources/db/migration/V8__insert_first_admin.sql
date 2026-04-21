INSERT INTO users (name, email, password, role, registered_at)
VALUES (
    'João Admin', 
    'joao@elearning.com', 
    '$2a$12$UeAC7KLWACarOHWrV5Zrfe2otCSCTgszQ6Emx2ObdSin2t.gnE3cS', 
    'ROLE_ADMIN', 
    NOW()
) ON CONFLICT (email) DO NOTHING;