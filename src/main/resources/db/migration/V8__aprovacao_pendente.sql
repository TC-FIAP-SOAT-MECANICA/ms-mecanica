UPDATE tb_ordens_servico SET status_aprovacao_rejeicao = 'PENDENTE'
WHERE status = 'AGUARDANDO_APROVACAO' AND status_aprovacao_rejeicao IS NULL;

COMMENT ON COLUMN tb_ordens_servico.status_aprovacao_rejeicao IS
    'Veredito do cliente sobre o orçamento: PENDENTE, APROVADO ou REJEITADO';
COMMENT ON COLUMN tb_ordens_servico.data_aprovacao_rejeicao IS 'Data e hora da decisão do cliente sobre o orçamento';
