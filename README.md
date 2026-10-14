# ms-mecanica

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

### Opção 1 — Com Docker

```bash
docker compose --profile observability up -d
```

- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (usuário `admin`, senha `admin`)

A aplicação roda fora do Docker: o Prometheus a alcança por `host.docker.internal:8080` e ela envia os logs para
`localhost:3100`.

### Opção 2 — Sem Docker (Windows)

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
