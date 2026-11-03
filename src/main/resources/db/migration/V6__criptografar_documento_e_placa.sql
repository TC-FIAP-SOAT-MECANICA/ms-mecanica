ALTER TABLE clientes ALTER COLUMN cpf_cnpj SET DATA TYPE VARCHAR(255);
ALTER TABLE clientes ADD COLUMN cpf_cnpj_hash VARCHAR(64);
CREATE UNIQUE INDEX uk_clientes_cpf_cnpj_hash ON clientes (cpf_cnpj_hash);

ALTER TABLE veiculos ALTER COLUMN placa SET DATA TYPE VARCHAR(255);
ALTER TABLE veiculos ADD COLUMN placa_hash VARCHAR(64);
CREATE UNIQUE INDEX uk_veiculos_placa_hash ON veiculos (placa_hash);

COMMENT ON COLUMN clientes.cpf_cnpj IS 'Documento fiscal criptografado (AES-256-GCM)';
COMMENT ON COLUMN clientes.cpf_cnpj_hash IS 'HMAC-SHA256 do documento, usado para unicidade e busca (índice cego)';
COMMENT ON COLUMN veiculos.placa IS 'Placa criptografada (AES-256-GCM)';
COMMENT ON COLUMN veiculos.placa_hash IS 'HMAC-SHA256 da placa, usado para unicidade e busca (índice cego)';
