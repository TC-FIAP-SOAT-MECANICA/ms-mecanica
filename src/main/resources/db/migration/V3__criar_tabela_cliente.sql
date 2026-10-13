CREATE TABLE cliente (
    id                   UUID         PRIMARY KEY,
    documento            VARCHAR(14)  NOT NULL UNIQUE,
    nome                 VARCHAR(150) NOT NULL,
    email                VARCHAR(254) NOT NULL,
    telefone             VARCHAR(16),
    endereco_logradouro  VARCHAR(150),
    endereco_numero      VARCHAR(10),
    endereco_complemento VARCHAR(60),
    endereco_bairro      VARCHAR(80),
    endereco_cidade      VARCHAR(80),
    endereco_uf          VARCHAR(2),
    endereco_cep         VARCHAR(8),
    criado_em            TIMESTAMP WITH TIME ZONE NOT NULL,
    atualizado_em        TIMESTAMP WITH TIME ZONE NOT NULL
);
