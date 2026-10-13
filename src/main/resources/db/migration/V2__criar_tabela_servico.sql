CREATE TABLE servico (
    id                     UUID           PRIMARY KEY,
    nome                   VARCHAR(120)   NOT NULL,
    descricao              VARCHAR(500),
    preco                  NUMERIC(11, 2) NOT NULL,
    tempo_estimado_minutos INTEGER        NOT NULL,
    ativo                  BOOLEAN        NOT NULL
);
