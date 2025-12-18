INSERT INTO courses (id, title, description, workload, image_url, price, old_price, is_best_seller) OVERRIDING SYSTEM VALUE VALUES
(7, 'Desenvolvimento Mobile com Flutter', 'Crie aplicativos nativos para iOS e Android com um único código usando Dart e Flutter.', 55, 'https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?auto=format&fit=crop&q=80&w=400', 145.00, 210.00, TRUE),
(8, 'TypeScript do Zero ao Avançado', 'Eleve o nível do seu JavaScript com tipagem estática, interfaces e Generics.', 35, 'https://images.unsplash.com/photo-1516116216624-53e697fedbea?auto=format&fit=crop&q=80&w=400', 89.90, 89.90, FALSE),
(9, 'Docker e Kubernetes: O Guia Prático', 'Aprenda a containerizar suas aplicações e gerenciar clusters escaláveis na nuvem.', 45, 'https://images.unsplash.com/photo-1605745341112-85968b193ef5?auto=format&fit=crop&q=80&w=400', 179.90, 259.00, TRUE),
(10, 'UI Design com Figma', 'Aprenda a criar interfaces modernas, protótipos interativos e sistemas de design no Figma.', 25, 'https://images.unsplash.com/photo-1586717791821-3f44a563eb4c?auto=format&fit=crop&q=80&w=400', 69.90, 120.00, FALSE);

SELECT setval('courses_id_seq', (SELECT MAX(id) FROM courses));

-- Módulos
INSERT INTO modules (id, title, description, course_id, module_order) OVERRIDING SYSTEM VALUE VALUES
(10, 'Widgets e Layouts', 'Fundamentos de interface no Flutter.', 7, 1),
(11, 'Tipagem e Interfaces', 'Dominando o sistema de tipos do TS.', 8, 1),
(12, 'Conceitos de Containers', 'Introdução ao isolamento de processos com Docker.', 9, 1),
(13, 'Auto-Layout e Componentes', 'Agilidade no design com Figma.', 10, 1);

SELECT setval('modules_id_seq', (SELECT MAX(id) FROM modules));

-- Aulas
INSERT INTO lessons (id, title, content, video_url, module_id, lesson_order) OVERRIDING SYSTEM VALUE VALUES
(10, 'Stateless vs Stateful', 'Diferenças entre tipos de widgets.', 'http://video.teste/flutter/01', 10, 1),
(11, 'Trabalhando com Generics', 'Criando funções e classes flexíveis.', 'http://video.teste/ts/01', 11, 1),
(12, 'Criando sua primeira Dockerfile', 'Passo a passo da construção de imagens.', 'http://video.teste/docker/01', 12, 1),
(13, 'Prototipagem de Alta Fidelidade', 'Conectando telas e criando transições.', 'http://video.teste/figma/01', 13, 1);

SELECT setval('lessons_id_seq', (SELECT MAX(id) FROM lessons));

INSERT INTO course_category (course_id, category_id) VALUES
(7, 7), -- Mobile
(8, 1), -- Dev Web
(9, 1), -- Dev Web (Infra/DevOps)
(10, 3); -- Design

INSERT INTO courses (id, title, description, workload, image_url, price, old_price, is_best_seller) OVERRIDING SYSTEM VALUE VALUES
-- Desenvolvimento & TI
(11, 'Segurança da Informação: Pentest', 'Aprenda técnicas de invasão ética e defesa de sistemas.', 50, 'https://images.unsplash.com/photo-1550751827-4bd374c3f58b?w=400', 199.90, 299.00, TRUE),
(12, 'Angular: Framework Completo', 'Construa aplicações SPA robustas com o framework do Google.', 45, 'https://images.unsplash.com/photo-1509718443690-d8e2fb3af1cc?w=400', 120.00, 120.00, FALSE),
(13, 'AWS Cloud Practitioner', 'Tudo o que você precisa para a primeira certificação Amazon.', 30, 'https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=400', 159.00, 159.00, TRUE),
(14, 'Lógica de Programação com JS', 'O ponto de partida ideal para quem nunca programou.', 20, 'https://images.unsplash.com/photo-1587620962725-abab7fe55159?w=400', 49.90, 89.00, FALSE),
(15, 'Kotlin para Android', 'Saia do Java e domine a linguagem oficial do Android.', 40, 'https://images.unsplash.com/photo-1607252650355-f7fd0460ccdb?w=400', 139.00, 139.00, FALSE),

-- Dados & IA
(16, 'Machine Learning com Scikit-Learn', 'Crie modelos preditivos de regressão e classificação.', 60, 'https://images.unsplash.com/photo-1527474305487-b87b222841cc?w=400', 189.00, 240.00, TRUE),
(17, 'Excel para Negócios', 'Do básico ao avançado com Macros e Tabela Dinâmica.', 25, 'https://images.unsplash.com/photo-1543286386-713bdd548da4?w=400', 79.00, 99.00, FALSE),
(18, 'Power BI: Dashboards Incríveis', 'Transforme dados brutos em decisões estratégicas visuais.', 30, 'https://images.unsplash.com/photo-1551288049-bbbda536339a?w=400', 99.00, 150.00, TRUE),

-- Design & UX
(19, 'Adobe Photoshop para Fotógrafos', 'Edição profissional de imagens e tratamento de cor.', 35, 'https://images.unsplash.com/photo-1572044162444-ad60f128b582?w=400', 110.00, 110.00, FALSE),
(20, 'Motion Design com After Effects', 'Crie animações de interface e vídeos dinâmicos.', 40, 'https://images.unsplash.com/photo-1558655146-d09347e92766?w=400', 160.00, 220.00, FALSE),

-- Marketing & Negócios
(21, 'Google Ads: Do Zero à Conversão', 'Aprenda a criar campanhas que vendem de verdade.', 20, 'https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=400', 85.00, 130.00, TRUE),
(22, 'Gestão Ágil com Scrum e Kanban', 'Aumente a produtividade da sua equipe com métodos ágeis.', 15, 'https://images.unsplash.com/photo-1531403009284-440f080d1e12?w=400', 65.00, 65.00, FALSE),
(23, 'Finanças Pessoais e Investimentos', 'Organize suas contas e aprenda a investir na bolsa.', 10, 'https://images.unsplash.com/photo-1565514020179-026b92b84bb6?w=400', 0.00, 50.00, TRUE),

-- Outros
(24, 'Inglês Instrumental para Devs', 'Leia documentações técnicas sem precisar de tradutor.', 25, 'https://images.unsplash.com/photo-1522202176988-66273c2fd55f?w=400', 95.00, 95.00, FALSE),
(25, 'Fotografia com Smartphone', 'Tire fotos profissionais usando apenas o seu celular.', 12, 'https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=400', 55.00, 89.00, FALSE);

SELECT setval('courses_id_seq', (SELECT MAX(id) FROM courses));

INSERT INTO course_category (course_id, category_id) VALUES
(11, 1), (12, 1), (14, 1), -- Dev Web
(13, 1), -- Cloud/Web
(15, 7), -- Mobile
(16, 2), (18, 2), -- Ciência de Dados
(17, 10), -- Produtividade
(19, 3), (20, 3), -- Design
(21, 4), -- Marketing
(22, 6), (23, 6), -- Negócios
(24, 5), -- Idiomas
(25, 9); -- Arte

INSERT INTO course_instructor (course_id, instructor_id) VALUES
(11, 6), (12, 1), (13, 8), (14, 1), (15, 2),
(16, 4), (17, 8), (18, 5), (19, 7), (20, 7),
(21, 5), (22, 3), (23, 9), (24, 3), (25, 7);

INSERT INTO modules (id, title, description, course_id, module_order) OVERRIDING SYSTEM VALUE VALUES
(14, 'Fundamentos de Ethical Hacking', 'Introdução ao mundo do Pentest.', 11, 1),
(15, 'Arquitetura Angular', 'Componentes, Módulos e Services.', 12, 1),
(16, 'Serviços Core da AWS', 'EC2, S3 e RDS na prática.', 13, 1),
(17, 'Algoritmos e Variáveis', 'Aprendendo a pensar como um programador.', 14, 1),
(18, 'Interface com Kotlin', 'Criando layouts modernos no Android.', 15, 1),
(19, 'Regressão Linear', 'Seu primeiro modelo de ML.', 16, 1),
(20, 'Fórmulas e Funções', 'O poder das planilhas no dia a dia.', 17, 1),
(21, 'Modelagem de Dados', 'Conectando fontes de dados no Power BI.', 18, 1),
(22, 'Tratamento de Imagem', 'Layers, Máscaras e Seleções.', 19, 1),
(23, 'Keyframes e Timeline', 'Dando vida às suas artes.', 20, 1),
(24, 'Estrutura de Campanhas', 'Rede de pesquisa e Display.', 21, 1),
(25, 'O Framework Scrum', 'Papéis, Eventos e Artefatos.', 22, 1),
(26, 'Mentalidade Financeira', 'Como poupar e sair das dívidas.', 23, 1),
(27, 'Vocabulário Técnico', 'Termos essenciais para documentação.', 24, 1),
(28, 'Composição e Iluminação', 'O segredo de uma foto atraente.', 25, 1);

SELECT setval('modules_id_seq', (SELECT MAX(id) FROM modules));

INSERT INTO lessons (id, title, content, video_url, module_id, lesson_order) OVERRIDING SYSTEM VALUE VALUES
(14, 'O que é um Hacker?', 'Diferença entre White, Grey e Black Hat.', 'http://video.teste/sec/01', 14, 1),
(15, 'Instalando o Angular CLI', 'Preparando o ambiente de desenvolvimento.', 'http://video.teste/angular/01', 15, 1),
(16, 'Criando sua Conta AWS', 'Passo a passo no Free Tier.', 'http://video.teste/aws/01', 16, 1),
(17, 'Sua primeira linha de JS', 'Usando o console.log.', 'http://video.teste/js/01', 17, 1),
(18, 'Atividades e Ciclo de Vida', 'Como funciona uma App Android.', 'http://video.teste/kotlin/01', 18, 1),
(19, 'O que é Aprendizado Supervisionado?', 'Conceitos teóricos fundamentais.', 'http://video.teste/ml/01', 19, 1),
(20, 'PROCV e ÍNDICE/CORRESP', 'As funções mais pedidas em processos seletivos.', 'http://video.teste/excel/01', 20, 1),
(21, 'Importando arquivos CSV', 'Limpando dados com Power Query.', 'http://video.teste/pbi/01', 21, 1),
(22, 'Ajustes de Níveis e Curvas', 'Melhorando o contraste das fotos.', 'http://video.teste/ps/01', 22, 1),
(23, 'Princípios da Animação', 'Interpolação e curvas de velocidade.', 'http://video.teste/ae/01', 23, 1),
(24, 'Configurando Conversões', 'Instalando a tag de acompanhamento.', 'http://video.teste/gads/01', 24, 1),
(25, 'Daily e Sprint Planning', 'Rituais para agilidade.', 'http://video.teste/scrum/01', 25, 1),
(26, 'Juros Compostos na Prática', 'A matemática do enriquecimento.', 'http://video.teste/fin/01', 26, 1),
(27, 'Reading Documentation', 'Estratégias de leitura rápida.', 'http://video.teste/eng/01', 27, 1),
(28, 'Foco e Exposição', 'Controlando a câmera do celular.', 'http://video.teste/photo/01', 28, 1);

SELECT setval('lessons_id_seq', (SELECT MAX(id) FROM lessons));