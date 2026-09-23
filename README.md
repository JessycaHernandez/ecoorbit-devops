# EcoOrbit - Cidades ESG Inteligentes

Monitoramento inteligente de florestas por satélite, com IA para detecção de
desmatamento e geração de alertas. Este repositório contém a API REST (Java
Spring Boot + MongoDB) do projeto EcoOrbit, adaptada e ampliada com um
pipeline completo de DevOps (CI/CD, containerização e orquestração) para
simular um ciclo de vida de produção real.

**Integrante:** Jéssyca Hernandez (RM 561908)

**Tema ESG:** Ação Contra a Mudança Global do Clima (ODS 13) e Vida
Terrestre (ODS 15) - monitoramento e fiscalização ambiental de áreas
florestais.

---

## Como executar localmente com Docker

Pré-requisitos: Docker e Docker Compose instalados.

1. Clone o repositório e entre na pasta do projeto:
   ```bash
   git clone <url-do-repositorio>
   cd ecoorbit-devops
   ```

2. Copie o arquivo de variáveis de ambiente de exemplo:
   ```bash
   cp .env.example .env
   ```

3. Suba a aplicação (API + MongoDB) com o compose base, que roda em modo
   local/desenvolvimento:
   ```bash
   docker compose up -d --build
   ```

4. Verifique se a API está no ar:
   ```bash
   curl http://localhost:8080/api/status
   # {"app":"ecoorbit-api","status":"UP","environment":"local"}
   ```

5. Para subir nos perfis de **staging** ou **produção** (usados pelo
   pipeline de CI/CD), use os arquivos de override correspondentes:
   ```bash
   # Staging (porta 8081, banco ecoOrbit_staging)
   docker compose -f docker-compose.yml -f docker-compose.staging.yml up -d --build

   # Produção (porta 8080, banco ecoOrbit_prod)
   docker compose -f docker-compose.yml -f docker-compose.prod.yml up -d --build
   ```

6. Para derrubar os containers e remover os volumes:
   ```bash
   docker compose down -v
   ```

### Endpoints principais

| Recurso                  | Rota                            |
|---------------------------|----------------------------------|
| Status/health             | `GET /api/status`                |
| Áreas monitoradas         | `/api/areas-monitoradas`         |
| Leituras ambientais       | `/api/leituras-ambientais`       |
| Alertas                   | `/api/alertas`                   |
| Usuários                  | `/api/usuarios`                  |
| Ações de fiscalização     | `/api/acoes-fiscalizacao`        |

Cada recurso expõe `GET` (listar / por código), `POST` (criar), `PUT /{codigo}`
(atualizar) e `DELETE /{codigo}` (remover) - CRUD completo sobre as 5
collections do MongoDB.

---

## Pipeline CI/CD

**Ferramenta utilizada:** GitHub Actions (`.github/workflows/ci-cd.yml`).

O pipeline é disparado em push para `main`/`develop`, em pull requests para
`main`, e também pode ser rodado manualmente (`workflow_dispatch`). Ele é
dividido em 4 jobs sequenciais:

1. **`build-and-test`** - compila o projeto com Maven (`mvn clean compile`),
   sobe um serviço MongoDB temporário (container `mongo:7`, fornecido pelo
   próprio GitHub Actions) e executa a suíte de testes automatizados
   (`mvn test`): testes de contexto do Spring e testes de camada web
   (controllers) com MockMvc. O relatório do Surefire e o `.jar` gerado são
   publicados como artefatos do workflow.
2. **`docker-build-push`** - constrói a imagem Docker da API (multi-stage
   build) e publica no GitHub Container Registry (GHCR), com duas tags: o
   SHA curto do commit e `latest`.
3. **`deploy-staging`** - sobe a stack completa (API + MongoDB) com
   `docker compose -f docker-compose.yml -f docker-compose.staging.yml`,
   aguarda a API responder e executa um *smoke test* (`GET /api/status`)
   como evidência do deploy, salvando o resultado como artefato. Usa o
   ambiente do GitHub chamado `staging`.
4. **`deploy-production`** - só roda depois que `deploy-staging` passa.
   Sobe a stack com o override de produção
   (`docker-compose.prod.yml`), faz o mesmo smoke test e guarda a evidência.
   Usa o ambiente do GitHub chamado `production`, que pode ser configurado
   com **aprovação manual obrigatória** (Settings → Environments →
   production → Required reviewers) - simulando um gate de aprovação antes
   de ir para produção, como em um cenário real.

Nesta implementação, os ambientes de staging e produção sobem no próprio
runner do GitHub Actions (para fins de demonstração/avaliação, sem custo de
infraestrutura externa). Em um cenário real de produção, o job de deploy
seria adaptado para publicar a imagem do GHCR em um host remoto (VM, VPS ou
cluster) via SSH (`appleboy/ssh-action`) ou `docker context`, usando os
secrets `STAGING_HOST` / `PRODUCTION_HOST` do repositório - a lógica de
build → push → deploy → smoke test permanece a mesma.

---

## Containerização

### Dockerfile

Build multi-stage para manter a imagem final enxuta e sem as ferramentas de
build:

```dockerfile
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S ecoorbit && adduser -S ecoorbit -G ecoorbit
USER ecoorbit
COPY --from=build /build/target/ecoorbit-api.jar app.jar
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD wget -qO- http://localhost:8080/api/status || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Estratégias adotadas:**

- **Multi-stage build**: a etapa de build (com Maven + JDK completo, ~500MB)
  é descartada da imagem final, que usa apenas `eclipse-temurin:17-jre-alpine`
  - muito menor e com menos riscos de segurança.
- **Cache de dependências**: o `pom.xml` é copiado e as dependências
  resolvidas (`dependency:go-offline`) antes de copiar o código-fonte, para
  aproveitar o cache de camadas do Docker entre builds.
- **Usuário não-root**: a aplicação roda com o usuário `ecoorbit`, não como
  root, seguindo boas práticas de segurança de containers.
- **Healthcheck**: o container expõe `/api/status` e o Docker verifica
  periodicamente se a aplicação está saudável - usado inclusive pelo
  `depends_on: condition: service_healthy` do Mongo no compose.

### Orquestração (Docker Compose)

- `docker-compose.yml` - define a estrutura base: serviço `api` (build a
  partir do Dockerfile) e serviço `mongo` (imagem oficial `mongo:7`), ligados
  pela rede `ecoorbit-net`, com volume nomeado `mongo-data` para persistir os
  dados do banco, e variáveis de ambiente injetadas via `.env`.
- `docker-compose.staging.yml` - *override* que ajusta a porta exposta
  (`8081`), o perfil Spring (`staging`) e o nome do banco
  (`ecoOrbit_staging`), com volume próprio.
- `docker-compose.prod.yml` - *override* de produção: porta `8080`, perfil
  `prod`, banco `ecoOrbit_prod`, política `restart: always` e limites de
  CPU/memória.

Essa separação em arquivos de override (em vez de duplicar o compose inteiro)
segue a prática recomendada do Docker Compose para múltiplos ambientes.

---

## Prints do funcionamento

Execução real e pública: [github.com/JessycaHernandez/ecoorbit-devops/actions](https://github.com/JessycaHernandez/ecoorbit-devops/actions)
(commit `09d38fe`). Resultado: **Success**, 5m 6s no total, 4 artefatos gerados.

| Job | Resultado | Duração |
|---|---|---|
| `build-and-test` | sucesso | 1m 3s |
| `docker-build-push` | sucesso | 1m 6s |
| `deploy-staging` | sucesso | 1m 26s |
| `deploy-production` | sucesso | 1m 18s |

Artefatos gerados pelo workflow: `evidencia-deploy-staging`, `evidencia-deploy-producao`,
`ecoorbit-api-jar`, `relatorio-testes` (aba *Actions* → execução → *Artifacts*).

Staging e produção rodando ao mesmo tempo na máquina local:

```bash
$ curl http://localhost:8081/api/status
{"status":"UP","app":"ecoorbit-api","environment":"staging"}

$ curl http://localhost:8080/api/status
{"app":"ecoorbit-api","environment":"production","status":"UP"}

$ docker ps
NAMES                    STATUS
ecoorbit-api-prod        Up (healthy)
ecoorbit-mongo-prod      Up (healthy)
ecoorbit-api-staging     Up (healthy)
ecoorbit-mongo-staging   Up (healthy)
```

---

## Tecnologias utilizadas

- **Linguagem/Framework:** Java 17, Spring Boot 3 (Spring Web, Spring Data
  MongoDB, Spring Validation, Spring Actuator)
- **Banco de dados:** MongoDB 7 (NoSQL orientado a documentos)
- **Build:** Maven
- **Testes:** JUnit 5, Spring Boot Test, MockMvc, Mockito
- **Containerização:** Docker (multi-stage build)
- **Orquestração:** Docker Compose (com overrides por ambiente)
- **CI/CD:** GitHub Actions
- **Registro de imagens:** GitHub Container Registry (GHCR)

---

## Desafios encontrados e como foram resolvidos

- **Modelagem flexível do MongoDB:** a collection `leituras_ambientais` tem
  documentos com campos diferentes conforme o tipo de sensor (temperatura,
  CO₂, nível do rio, radiação solar etc.). Resolvido modelando a entidade
  Java com todos os campos possíveis como opcionais, respeitando a natureza
  schema-less do MongoDB sem perder a tipagem forte da API.
- **Dois ambientes com a mesma base de código:** em vez de duplicar todo o
  `docker-compose.yml`, foram usados arquivos de *override*
  (`docker-compose.staging.yml` / `docker-compose.prod.yml`), reduzindo
  duplicação e risco de divergência entre staging e produção.
- **Testes sem depender de infraestrutura externa na máquina do
  desenvolvedor:** os testes de controller usam `@WebMvcTest` com o
  repositório mockado (Mockito), rodando sem precisar de um MongoDB local;
  já o teste de contexto completo roda contra o MongoDB de serviço que o
  próprio GitHub Actions sobe no job de testes.
- **Ordem de dependência entre containers:** o serviço `api` só inicia depois
  que o `mongo` reporta `healthy` (`depends_on: condition: service_healthy`),
  evitando falhas de conexão na subida da stack.

---

## Checklist de entrega

| Item                                                          | OK |
|-----------------------------------------------------------------|:--:|
| Projeto compactado em .ZIP com estrutura organizada              | ☑ |
| Dockerfile funcional                                             | ☑ |
| docker-compose.yml ou arquivos Kubernetes                        | ☑ |
| Pipeline com etapas de build, teste e deploy                     | ☑ |
| README.md com instruções e prints                                | ☑ (prints a anexar após rodar o pipeline) |
| Documentação técnica com evidências (PDF ou PPT)                 | ☑ (`docs/EcoOrbit_Documentacao_Tecnica.md`) |
| Deploy realizado nos ambientes staging e produção                | ☑ (jobs `deploy-staging` / `deploy-production`) |
