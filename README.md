# ms-mecanica

API da oficina mecânica (Spring Boot 4 / Java 21), em monólito modular com DDD e Clean Architecture.

## Como executar

Há duas formas de subir com Docker:

| Repositório | Quando usar |
|---|---|
| [**docker**](https://github.com/TC-FIAP-SOAT-MECANICA/docker) | Ambiente da oficina com todos os serviços (hoje: ms-mecanica + PostgreSQL). **Recomendado.** |
| **ms-mecanica** (este) | Só esta API, direto do código deste repositório |

Nos dois casos o script baixa e executa a branch `main`. As duas formas usam as portas 8080 e 5436: pare uma
(`docker compose down` na pasta dela) antes de subir a outra.

### Instalação automática (clone + Docker)

[**⬇ Baixar setup.sh**](https://raw.githubusercontent.com/TC-FIAP-SOAT-MECANICA/ms-mecanica/main/setup.sh)
([ver o arquivo](setup.sh))

O script verifica os pré-requisitos, clona o repositório, compila e sobe tudo no Docker, espera a API ficar saudável
e mostra os endereços. Pré-requisitos: `git` e Docker Desktop em execução. No Windows, rode pelo Git Bash ou WSL.

```bash
curl -fsSL https://raw.githubusercontent.com/TC-FIAP-SOAT-MECANICA/ms-mecanica/main/setup.sh -o setup.sh
bash setup.sh
```

| Opção | Para quê |
|---|---|
| `-d <pasta>` | Pasta do clone (padrão `ms-mecanica`) |

Rodar de novo na mesma pasta atualiza o clone (`git pull`) e reconstrói os containers.

### Com Docker (projeto já clonado)

Pré-requisito: Docker Desktop em execução. Na raiz do projeto:

```bash
docker compose up -d --build
```

Compila o projeto dentro do Docker e sobe a aplicação e o PostgreSQL. Acompanhe até a `app` ficar `healthy` com
`docker compose ps`.

| Serviço | Endereço | Acesso |
|---|---|---|
| API | http://localhost:8080/api/v1 | login em `POST /auth/login` (ADMIN inicial `admin@oficina.com` / `admin12345`) |
| PostgreSQL | `localhost:5436`, banco `oficina` | `oficina` / `oficina` |

| Quero... | Comando |
|---|---|
| Ver o log da aplicação | `docker compose logs -f app` |
| Aplicar uma alteração no código | `docker compose up -d --build app` |
| Parar (mantém os dados) | `docker compose down` |
| Parar e apagar os dados | `docker compose down -v` |

### Pela IDE

Rode `OficinaApplication` ou `mvn spring-boot:run`. Sem perfil informado, a aplicação usa o H2 local (`./data`) e
cria as tabelas pelo Flyway. Para PostgreSQL, use o perfil `postgres` com `DB_URL`, `DB_USERNAME` e `DB_PASSWORD`.
Pare o container `app` antes (`docker compose stop app`), para liberar a porta 8080.

Para testar, importe [`postman/oficina-mecanica.postman_collection.json`](postman/oficina-mecanica.postman_collection.json)
no Postman e use **Run collection**: ela faz login como ADMIN, cria um mecânico e um cliente, e exercita cada endpoint
com o token do perfil certo, incluindo os acessos que devem ser negados.

Para ver só o caminho feliz de uma OS, use
[`postman/oficina-mecanica-fluxo-os.postman_collection.json`](postman/oficina-mecanica-fluxo-os.postman_collection.json):
22 passos numerados, do cadastro de mecânico, cliente, veículo, serviço e peça até a entrega do veículo, passando por
diagnóstico, orçamento, aprovação do cliente e baixa do estoque.

## Testes e cobertura

```bash
mvn verify
```

Roda os testes unitários (domínio e casos de uso) e os de integração (`src/test/java/br/com/oficina/integracao`), que
sobem a aplicação com H2 em memória e exercitam a API via MockMvc: autenticação, permissões por perfil, cadastros,
estoque e o ciclo completo da OS. O JaCoCo gera o relatório em `target/site/jacoco/index.html` e **quebra o build se a
cobertura de linhas ficar abaixo de 80%**.

Para enviar ao SonarQube/SonarCloud, o `pom.xml` já aponta o relatório (`sonar.coverage.jacoco.xmlReportPaths`):

```bash
mvn verify sonar:sonar -Dsonar.host.url=<url-do-sonar> -Dsonar.token=<token> -Dsonar.projectKey=ms-mecanica
```

## Autenticação e perfis

`POST /api/v1/auth/login` devolve um JWT (60 min) que vai no header `Authorization: Bearer <token>`.
Na primeira subida é criado um **ADMIN inicial**: `admin@oficina.com` / `admin12345` (altere com `ADMIN_EMAIL` e
`ADMIN_SENHA`). O ADMIN cria os mecânicos em `POST /usuarios`; o cliente pode se cadastrar sozinho em `POST /clientes`.

| Recurso | ADMIN (gerência/recepção) | MECANICO (execução técnica) | CLIENTE (dono do veículo) |
|---|---|---|---|
| Login e cadastro de cliente | público | público | público |
| Usuários internos | tudo | só `/usuarios/me` | só `/usuarios/me` |
| Clientes | tudo | listar, pesquisar, ver e alterar | ver e alterar **o próprio** |
| Veículos | tudo | tudo, exceto remover | cadastrar, ver e alterar **os próprios** |
| Serviços (catálogo) | tudo | consultar | consultar |
| Peças e estoque | tudo | tudo, exceto remover | — |
| Ordens de serviço | tudo | abrir, itens, diagnóstico, finalizar e entregar | consultar e aprovar/rejeitar o orçamento **das próprias** |

Sem token → `401 NAO_AUTENTICADO`; perfil sem permissão → `403 ACESSO_NEGADO`. Recurso de outro cliente responde `404`,
como se não existisse.

As regras por rota ficam em [`SecurityConfig`](src/main/java/br/com/oficina/infrastructure/dataproviders/config/security/SecurityConfig.java).
A regra "o cliente só vê o que é dele" fica nos casos de uso, que recebem o `Solicitante` (quem fez a requisição).

## Dados sensíveis

CPF/CNPJ e placa são gravados **criptografados** (AES-256-GCM). Para manter unicidade e permitir buscas, cada um tem
um hash HMAC-SHA256 ao lado (`cpf_cnpj_hash`, `placa_hash`). Senhas são guardadas com BCrypt.

| Variável | Para quê |
|---|---|
| `JWT_SECRET` | Segredo de assinatura do JWT (Base64, ≥ 32 bytes) |
| `CRYPTO_KEY` | Chave AES-256 dos campos criptografados (Base64, 32 bytes) |
| `CRYPTO_INDEX_KEY` | Chave do hash de busca (Base64, ≥ 32 bytes) |
| `ADMIN_EMAIL` / `ADMIN_SENHA` | Credenciais do ADMIN inicial |

Os valores padrão em `application.yml` servem só para desenvolvimento. **Em qualquer outro ambiente, defina as
variáveis.** Trocar `CRYPTO_KEY` ou `CRYPTO_INDEX_KEY` torna ilegíveis os dados já gravados. Gere uma chave com:

```bash
python -c "import os,base64;print(base64.b64encode(os.urandom(32)).decode())"
```

## Modelo de dados

As tabelas seguem o diagrama ER com o prefixo `tb_`. Foram criadas por
[`V5__modelo_oficina.sql`](src/main/resources/db/migration/V5__modelo_oficina.sql);
a [`V6`](src/main/resources/db/migration/V6__criptografar_documento_e_placa.sql) prepara as colunas criptografadas e a
[`V7`](src/main/resources/db/migration/V7__funcionarios_e_ciclo_da_os.sql) aplica o prefixo, cria `tb_funcionarios` e
os campos de agendamento, mecânico e aprovação da OS.

| Módulo (contexto) | Tabelas | Papel |
|---|---|---|
| `identidade` (IAM) | `tb_usuarios` | Login, perfis e regras de acesso |
| `cadastro` (Clientes, Funcionários e Frota) | `tb_clientes`, `tb_funcionarios`, `tb_veiculos` | Cliente e Funcionário 1:1 com usuário; frota |
| `catalogo` | `tb_servicos` | Mão de obra com preço base |
| `estoque` | `tb_pecas_insumos` | Peças/insumos, preços e saldo |
| `atendimento` (Oficina, core) | `tb_ordens_servico` e itens | Ciclo da OS, abaixo |

### Ciclo da OS

```
RECEBIDA → EM_DIAGNOSTICO → AGUARDANDO_APROVACAO → EM_EXECUCAO → FINALIZADA → ENTREGUE
                                    └──── orçamento rejeitado ────→ FINALIZADA
```

| Passo | Endpoint | Quem | Status |
|---|---|---|---|
| Abrir (pode agendar o diagnóstico) | `POST /ordens-servico` | ADMIN, MECANICO | → `RECEBIDA` |
| Iniciar diagnóstico (quem chama vira o mecânico da OS) | `POST /ordens-servico/{id}/diagnostico/inicio` | ADMIN, MECANICO | → `EM_DIAGNOSTICO` |
| Registrar laudo e enviar orçamento | `POST /ordens-servico/{id}/diagnostico` | ADMIN, MECANICO | → `AGUARDANDO_APROVACAO` (aprovação `PENDENTE`) |
| Aprovar (pode agendar o serviço) ou rejeitar | `POST /ordens-servico/{id}/aprovacao` | ADMIN, CLIENTE (só as próprias) | → `EM_EXECUCAO` ou `FINALIZADA` |
| Concluir o serviço | `POST /ordens-servico/{id}/finalizacao` | ADMIN, MECANICO | → `FINALIZADA` |
| Entregar o veículo | `POST /ordens-servico/{id}/entrega` | ADMIN, MECANICO | → `ENTREGUE` |

Itens podem ser alterados em `RECEBIDA`, `EM_DIAGNOSTICO` e `EM_EXECUCAO`. As peças só saem do estoque quando o
orçamento é aprovado; em `EM_EXECUCAO`, incluir peça baixa na hora e removê-la devolve ao estoque.

Todo usuário ADMIN ou MECANICO vira um **Funcionário** (ativo): a Identidade publica o evento `UsuarioCadastrado` e o
Cadastro o contrata na mesma transação. Só funcionário ativo pode assumir o diagnóstico de uma OS.

Os nomes no código seguem a linguagem ubíqua do projeto: `OrdemServico`, `OrdemServicoServico`,
`OrdemServicoPecaInsumo`, `statusAprovacaoRejeicao` (`PENDENTE`, `APROVADO`, `REJEITADO`), `cpfCnpj`, `PecaInsumo`...

## Estrutura de pastas

As camadas ficam na raiz de `br.com.oficina`, e cada uma tem uma subpasta por módulo (`identidade`, `cadastro`,
`catalogo`, `estoque`, `atendimento`):

| Pasta | Conteúdo |
|---|---|
| `domain/model/<modulo>` | Agregados, entidades e enums, com as regras de negócio |
| `domain/valueobject`, `domain/exceptions` | Value objects (`Dinheiro`, `Documento`, `Quantidade`) e exceções de domínio |
| `application/usecase/<modulo>` | Interfaces dos casos de uso |
| `application/usecase/implementation/<modulo>` | Implementações (`*UseCaseImpl`) |
| `application/gateways/<modulo>` | Portas: repositórios e integrações com outros módulos |
| `application/dto/<modulo>`, `application/exceptions` | Entradas e saídas dos casos de uso; erros de acesso |
| `entrypoints/controllers/<modulo>` | Controllers REST (implementam as interfaces geradas do contrato) |
| `entrypoints/controllers/presenter/<modulo>` | Conversão entre DTOs do contrato e da aplicação |
| `infrastructure/dataproviders/database` | Entidades JPA, repositórios Spring Data, implementações dos repositórios e criptografia |
| `infrastructure/dataproviders/client/<modulo>` | Implementações dos gateways entre módulos |
| `infrastructure/dataproviders/config` | Segurança, um `*Config` por caso de uso, filtro de log e carga inicial |

O domínio não depende de nenhuma outra camada nem de framework, e a aplicação não depende de `entrypoints` nem de
`infrastructure`. Um módulo só fala com outro pelos casos de uso dele, por meio de um gateway: a Oficina consulta
Cadastro, Catálogo e Estoque, e o Cadastro obtém nome, e-mail e telefone do cliente pela Identidade.

## Monitoramento

| Endpoint da aplicação | Para quê |
|---|---|
| `/api/v1/actuator/health` | Saúde da aplicação (`/liveness` e `/readiness` para orquestradores) |
| `/api/v1/actuator/prometheus` | Métricas no formato do Prometheus |
| `/api/v1/actuator/metrics` | Navegar pelas métricas disponíveis (ADMIN) |

Toda resposta traz o header `X-Request-Id`; nas respostas de erro ele também vem no campo `traceId`. Cada chamada
gera uma linha no log com método, rota, status e tempo (`docker compose logs -f app`).
