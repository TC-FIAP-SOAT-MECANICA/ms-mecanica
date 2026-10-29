# ms-mecanica

API da oficina mecânica (Spring Boot 4 / Java 21), em monólito modular com DDD e Clean Architecture.

## Como executar

### Com Docker (um comando)

Pré-requisito: Docker Desktop em execução. Na raiz do projeto:

```bash
docker compose up -d --build
```

Compila o projeto dentro do Docker e sobe aplicação, PostgreSQL, Prometheus, Loki e Grafana. Acompanhe até a `app`
ficar `healthy` com `docker compose ps`.

| Serviço | Endereço | Acesso |
|---|---|---|
| API | http://localhost:8080/api/v1 | login em `POST /auth/login` (ADMIN inicial `admin@oficina.com` / `admin12345`) |
| Grafana | http://localhost:3000 | `admin` / `admin` |
| Prometheus | http://localhost:9090 | — |
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
| Ordens de serviço | tudo | abrir, itens, diagnóstico, finalizar e entregar | consultar **as próprias** |

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

As tabelas do ER (usuarios, clientes, veiculos, servicos, pecas_insumos, ordens_servico e os itens da OS) são criadas
por [`V5__modelo_oficina.sql`](src/main/resources/db/migration/V5__modelo_oficina.sql); a
[`V6`](src/main/resources/db/migration/V6__criptografar_documento_e_placa.sql) prepara as colunas criptografadas.

| Módulo (contexto) | Tabelas | Papel |
|---|---|---|
| `identidade` (IAM) | `usuarios` | Login, perfis e regras de acesso |
| `cadastro` (Clientes e Veículos) | `clientes`, `veiculos` | Clientes 1:1 com usuário CLIENTE; frota |
| `catalogo` | `servicos` | Mão de obra com preço base |
| `estoque` | `pecas_insumos` | Peças/insumos, preços e saldo |
| `atendimento` (Oficina, core) | `ordens_servico` e itens | OS `ABERTA → EM_ANDAMENTO → CONCLUIDA → ENTREGUE` |

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

## Observabilidade (Prometheus + Loki + Grafana)

| Ferramenta | O que guarda | Porta |
|---|---|---|
| **Prometheus** | Métricas (requisições, latência, memória, banco...) coletadas a cada 15 s | 9090 |
| **Loki** | Logs da aplicação, enviados por ela mesma em ~2 s | 3100 |
| **Grafana** | Nada — mostra métricas e logs em dashboards | 3000 |

As fontes de dados e o dashboard **Oficina Mecânica - Visão Geral** são criados automaticamente no Grafana.

| Endpoint da aplicação | Para quê |
|---|---|
| `/api/v1/actuator/health` | Saúde da aplicação (`/liveness` e `/readiness` para orquestradores) |
| `/api/v1/actuator/prometheus` | Métricas lidas pelo Prometheus |
| `/api/v1/actuator/metrics` | Navegar pelas métricas disponíveis |

### Logs

Cada chamada gera uma linha de log com método, rota, status, tempo e o `X-Request-Id`:

```
2026-10-13T21:59:36 INFO [aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee] ... RequestIdFilter : GET /api/v1/clientes/... -> 404 (12 ms)
```

O log é gravado em três lugares:

- **Console** — onde a aplicação foi iniciada.
- **Arquivo** — `logs/oficina-mecanica.log` (até 10 MB por arquivo, 7 dias de histórico).
- **Loki** — pesquisável no Grafana: painéis *Logs da aplicação* e *Somente erros* do dashboard, ou em *Explore → Loki*.

Para achar uma chamada específica, copie o `X-Request-Id` da resposta (o Postman mostra em *Headers*) e cole no
campo **Buscar no log** do dashboard. Se o Loki estiver desligado, a aplicação funciona normalmente; os logs
continuam no console e no arquivo.

Consultas úteis no *Explore → Loki*:

```logql
{application="oficina-mecanica"}                         # todos os logs
{application="oficina-mecanica", level="ERROR"}          # só erros
{application="oficina-mecanica"} |= "POST /api/v1/clientes"   # chamadas de um endpoint
{application="oficina-mecanica"} |= "-> 4"               # respostas 4xx
```

### Com Docker

Já sobe com `docker compose up -d --build` (veja *Como executar*): o Prometheus lê a aplicação pelo container `app`.

### Sem Docker (Windows)

1. Baixe e extraia:
   - Prometheus (Windows amd64): https://prometheus.io/download/
   - Loki (`loki-windows-amd64.exe.zip`): https://github.com/grafana/loki/releases
   - Grafana (Windows, versão *standalone* `.zip`): https://grafana.com/grafana/download?platform=windows
2. Suba a aplicação (porta 8080).
3. Prometheus — na pasta extraída:
   ```powershell
   .\prometheus.exe --config.file="C:\caminho\do\projeto\observability\prometheus\prometheus.yml"
   ```
   Confira em http://localhost:9090/targets se o alvo `oficina-mecanica` está **UP**.
4. Loki — na pasta extraída:
   ```powershell
   .\loki-windows-amd64.exe --config.file="C:\caminho\do\projeto\observability\loki\loki-config.yaml"
   ```
5. Grafana — na pasta extraída:
   ```powershell
   $env:PROMETHEUS_URL = "http://localhost:9090"
   $env:LOKI_DATASOURCE_URL = "http://localhost:3100"
   $env:GRAFANA_DASHBOARDS_PATH = "C:\caminho\do\projeto\observability\grafana\dashboards"
   $env:GF_PATHS_PROVISIONING = "C:\caminho\do\projeto\observability\grafana\provisioning"
   .\bin\grafana.exe server
   ```
6. Acesse http://localhost:3000 (usuário `admin`, senha `admin`) → *Dashboards* → *Oficina Mecânica*.

### Se a aplicação usar outra porta

Altere o `targets` em `observability/prometheus/prometheus.yml` (ou `prometheus-docker.yml`), por exemplo
`localhost:8081`. Se o Loki estiver em outro endereço, defina a variável `LOKI_URL` ao subir a aplicação
(padrão `http://localhost:3100/loki/api/v1/push`).
