USE gh;

-- Password are hashed in base64
INSERT INTO diretores (nome, email, password) VALUES 
('admin', 'admin', 'YWRtaW4='),
('Diretor', 'dt@mail.com', 'YWRtaW4=');

INSERT INTO alunos (nome, numero, estatuto, email, password) VALUES 
('user', 'A000000', 0, "user", 'dXNlcg=='),
('Aluno', 'A000001', 0, "a000000@mail.com", 'dXNlcg==');