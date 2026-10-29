package br.com.oficina.infrastructure.dataproviders.config.startup;

import br.com.oficina.application.usecase.identidade.CriarAdminInicialUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
class AdminInicialRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminInicialRunner.class);

    private final CriarAdminInicialUseCase criarAdminInicial;
    private final String nome;
    private final String email;
    private final String telefone;
    private final String senha;

    AdminInicialRunner(CriarAdminInicialUseCase criarAdminInicial,
                       @Value("${app.admin-inicial.nome}") String nome,
                       @Value("${app.admin-inicial.email}") String email,
                       @Value("${app.admin-inicial.telefone}") String telefone,
                       @Value("${app.admin-inicial.senha}") String senha) {
        this.criarAdminInicial = criarAdminInicial;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
    }

    @Override
    public void run(ApplicationArguments args) {
        criarAdminInicial.execute(nome, email, telefone, senha)
                .ifPresent(id -> log.info("Usuário ADMIN inicial criado (id {}, e-mail {})", id, email));
    }
}
