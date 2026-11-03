#!/usr/bin/env bash
set -euo pipefail

REPO_URL="${REPO_URL:-https://github.com/TC-FIAP-SOAT-MECANICA/ms-mecanica.git}"
BRANCH="main"
DIRETORIO="${DIRETORIO:-ms-mecanica}"
TIMEOUT_SEGUNDOS="${TIMEOUT_SEGUNDOS:-300}"

uso() {
    cat <<EOF
Uso: ./setup.sh [-d diretorio]

Clona a branch main do ms-mecanica e sobe tudo no Docker (API e PostgreSQL).
Para o ambiente com todos os serviços, use o repositório docker:
https://github.com/TC-FIAP-SOAT-MECANICA/docker

  -d diretorio  Pasta do clone (padrão: ${DIRETORIO})
  -h            Mostra esta ajuda

Também aceita as variáveis REPO_URL, DIRETORIO e TIMEOUT_SEGUNDOS.
EOF
}

info() { printf '\n==> %s\n' "$1"; }
erro() { printf '\nERRO: %s\n' "$1" >&2; exit 1; }

while getopts "d:h" opcao; do
    case "$opcao" in
        d) DIRETORIO="$OPTARG" ;;
        h) uso; exit 0 ;;
        *) uso; exit 1 ;;
    esac
done

info "Verificando pré-requisitos"
command -v git >/dev/null 2>&1 || erro "git não encontrado. Instale em https://git-scm.com/downloads"
command -v docker >/dev/null 2>&1 || erro "docker não encontrado. Instale o Docker Desktop: https://www.docker.com/products/docker-desktop"
if docker compose version >/dev/null 2>&1; then
    COMPOSE=(docker compose)
elif command -v docker-compose >/dev/null 2>&1; then
    COMPOSE=(docker-compose)
else
    erro "Docker Compose não encontrado. Atualize o Docker Desktop ou instale o plugin compose."
fi
docker info >/dev/null 2>&1 || erro "o Docker não está rodando. Abra o Docker Desktop e execute o script de novo."
echo "git, docker e ${COMPOSE[*]} disponíveis"

if [ -d "$DIRETORIO/.git" ]; then
    info "Atualizando o clone existente em $DIRETORIO (branch $BRANCH)"
    git -C "$DIRETORIO" config core.longpaths true
    git -C "$DIRETORIO" fetch origin "$BRANCH"
    git -C "$DIRETORIO" checkout "$BRANCH"
    git -C "$DIRETORIO" pull --ff-only origin "$BRANCH"
elif [ -e "$DIRETORIO" ]; then
    erro "$DIRETORIO já existe e não é um repositório git. Use -d para escolher outra pasta."
else
    info "Clonando $REPO_URL (branch $BRANCH) em $DIRETORIO"
    git clone -c core.longpaths=true --branch "$BRANCH" "$REPO_URL" "$DIRETORIO"
fi

cd "$DIRETORIO"

info "Compilando e subindo os containers (a primeira vez leva alguns minutos)"
"${COMPOSE[@]}" up -d --build --remove-orphans

info "Aguardando a API ficar saudável (até ${TIMEOUT_SEGUNDOS}s)"
APP_ID="$("${COMPOSE[@]}" ps -q app)"
[ -n "$APP_ID" ] || erro "container da aplicação não foi criado. Veja: ${COMPOSE[*]} logs app"
inicio=$SECONDS
while true; do
    estado="$(docker inspect -f '{{if .State.Health}}{{.State.Health.Status}}{{else}}{{.State.Status}}{{end}}' "$APP_ID")"
    case "$estado" in
        healthy) break ;;
        unhealthy|exited|dead) erro "a API ficou '$estado'. Veja o log com: ${COMPOSE[*]} logs app" ;;
    esac
    if [ $((SECONDS - inicio)) -ge "$TIMEOUT_SEGUNDOS" ]; then
        erro "a API não ficou saudável em ${TIMEOUT_SEGUNDOS}s (estado: $estado). Veja: ${COMPOSE[*]} logs app"
    fi
    printf '.'
    sleep 5
done
echo " pronto"

"${COMPOSE[@]}" ps

cat <<EOF

Tudo no ar!

  API         http://localhost:8080/api/v1
              login: POST /auth/login com admin@oficina.com / admin12345
  Contrato    http://localhost:8080/api/v1/oficina-mecanica-contract.yaml
  Métricas    http://localhost:8080/api/v1/actuator/prometheus
  PostgreSQL  localhost:5436, banco oficina (oficina / oficina)

Collection do Postman: $(pwd)/postman/oficina-mecanica.postman_collection.json

Comandos úteis (dentro de $(pwd)):
  ${COMPOSE[*]} logs -f app    log da aplicação
  ${COMPOSE[*]} down           para tudo (mantém os dados)
  ${COMPOSE[*]} down -v        para tudo e apaga os dados
EOF
