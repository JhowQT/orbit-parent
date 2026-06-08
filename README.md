# OrbitBook — Plataforma de Reservas para Turismo Espacial

> **FIAP Global Solution 2026/1** — Solução completa de backend para reservas de viagens espaciais, implementada com microserviços Java (Spring Boot) e backend Python (FastAPI), integrada com IA Generativa (Google Gemini).

---

## Sumário

- [Sobre a solução](#sobre-a-solução)
- [Arquitetura](#arquitetura)
- [Tecnologias](#tecnologias)
- [Endpoints principais](#endpoints-principais)
- [Pré-requisitos](#pré-requisitos)
- [Rodando localmente](#rodando-localmente)
- [Rodando com Docker](#rodando-com-docker)
- [Variáveis de ambiente](#variáveis-de-ambiente)
- [Documentação da API](#documentação-da-api)
- [Integrantes](#integrantes)

---

## Sobre a solução

A **OrbitBook** é uma plataforma de reservas para turismo espacial que permite aos usuários descobrir destinos, fazer reservas de missões, avaliar experiências e interagir com **ARIA** — uma assistente de IA especializada em turismo espacial.

### Funcionalidades

- **Autenticação JWT** com controle de roles (ADMIN / VIAJANTE)
- **Gestão de destinos** com filtros, paginação e avaliação média embutida
- **Reservas** com cálculo automático de preço, validação de capacidade e status em tempo real
- **Mensageria assíncrona** via RabbitMQ para notificações de reserva e pagamento
- **ARIA — Chat com IA** multi-turno com Google Gemini 2.5 Flash, com extração automática de destinos recomendados
- **RAG (Retrieval Augmented Generation)** — busca semântica por similaridade coseno sobre os destinos
- **Tooling** — Gemini chama ferramentas autonomamente para buscar dados em tempo real
- **MCP (Model Context Protocol)** — expõe o serviço de IA como servidor MCP para integração com outros agentes

---

## Arquitetura

```
                          ┌─────────────────────────────────────────┐
                          │           Cliente / Frontend             │
                          └────────────────────┬────────────────────┘
                                               │ HTTP :8080
                          ┌────────────────────▼────────────────────┐
                          │           API Gateway :8080              │
                          │   JWT Validation + Header Injection      │
                          │   X-User-Id, X-User-Role downstream      │
                          └───┬──────────┬────────────┬─────────────┘
                              │          │            │
               ┌──────────────▼──┐  ┌───▼────────┐  ┌▼──────────────┐
               │  auth-service   │  │  booking-  │  │  ai-service   │
               │     :8081       │  │  service   │  │    :8083       │
               │                 │  │   :8082    │  │               │
               │ - Cadastro      │  │            │  │ - ARIA Chat   │
               │ - Login (JWT)   │  │ - Destinos │  │ - RAG         │
               │ - Usuários      │  │ - Reservas │  │ - Tooling     │
               └────────┬────────┘  │ - Reviews  │  │ - MCP Server  │
                        │           │ - Passag.  │  └───────┬───────┘
                        │           │ - Pagament.│          │
                        │           └─────┬──────┘          │
                        │                 │                  │
               ┌─────────▼─────────────────▼──────────────────▼──────┐
               │              Oracle DB — rm560077                     │
               │         oracle.fiap.com.br:1521/orcl                  │
               └─────────────────────────────────────────────────────┘
                                          │
               ┌──────────────────────────▼──────────────────────────┐
               │              RabbitMQ — orbitbook.exchange            │
               │    booking.queue (confirmações) · payment.queue       │
               └─────────────────────────────────────────────────────┘

               ┌─────────────────────────────────────────────────────┐
               │         discovery-server (Eureka) :8761              │
               │      Todos os serviços registrados via lb://          │
               └─────────────────────────────────────────────────────┘
```

### Serviços Java

| Serviço | Porta | Responsabilidade |
|---|---|---|
| `discovery-server` | 8761 | Eureka — registro e descoberta de serviços |
| `api-gateway` | 8080 | Roteamento, validação JWT, injeção de headers |
| `auth-service` | 8081 | Cadastro, login, gestão de usuários |
| `booking-service` | 8082 | Destinos, reservas, reviews, passageiros, pagamentos |
| `ai-service` | 8083 | Chat com ARIA (RAG + Tooling + MCP + Gemini) |

### Backend Python

| Serviço | Porta | Responsabilidade |
|---|---|---|
| `orbitbook-api` (FastAPI) | 8000 | Backend alternativo com os mesmos dados Oracle |

---

## Tecnologias

**Java (microserviços principais)**
- Java 21 + Spring Boot 4.0.6
- Spring Cloud 2025.1.1 (Eureka, Gateway WebMVC, OpenFeign)
- Spring AI 2.0.0-M8 (Google Gemini, RAG, Tooling, MCP)
- Spring Security + JJWT 0.12.5
- Spring Data JPA + Oracle JDBC (ojdbc11)
- Spring AMQP + RabbitMQ
- SpringDoc OpenAPI 3.0.2 (Swagger UI)
- Lombok + Gradle

**Python (backend secundário)**
- Python 3.11+ + FastAPI 0.115 + Uvicorn
- SQLAlchemy 2.0 + oracledb 2.4
- Google Gemini via HTTP (httpx)
- JWT via python-jose

**Infraestrutura**
- Oracle Database (FIAP — `oracle.fiap.com.br:1521/orcl`)
- RabbitMQ 3 (mensageria assíncrona)
- Docker + Docker Compose

---

## Endpoints principais

Todos os endpoints protegidos exigem `Authorization: Bearer <token>`.

### Auth (`/auth`, `/users`)

| Método | Endpoint | Auth | Descrição |
|---|---|---|---|
| POST | `/auth/register` | ❌ | Cadastrar usuário |
| POST | `/auth/login` | ❌ | Login — retorna JWT |
| GET | `/users/me` | ✅ | Dados do usuário autenticado |
| GET | `/users/{id}` | ✅ | Buscar usuário por ID |
| GET | `/users` | ✅ | Listar todos os usuários |
| DELETE | `/users/{id}` | ✅ | Remover usuário |

### Destinos (`/destinations`)

| Método | Endpoint | Auth | Descrição |
|---|---|---|---|
| GET | `/destinations` | ✅ | Listar todos (com avaliação média) |
| GET | `/destinations/page` | ✅ | Listar com filtros e paginação |
| GET | `/destinations/{id}` | ✅ | Buscar por ID |
| GET | `/destinations/search?name=` | ✅ | Busca por nome |
| POST | `/destinations` | ✅ | Criar destino |
| PUT | `/destinations/{id}` | ✅ | Atualizar destino |
| DELETE | `/destinations/{id}` | ✅ | Remover destino |

**Query params de `/destinations/page`:** `tipo`, `precoMin`, `precoMax`, `busca`, `page` (default: 1), `limit` (default: 10)

### Reservas (`/bookings`)

| Método | Endpoint | Auth | Descrição |
|---|---|---|---|
| POST | `/bookings` | ✅ | Criar reserva |
| GET | `/bookings` | ✅ | Listar todas as reservas |
| GET | `/bookings/{id}` | ✅ | Buscar reserva por ID |
| GET | `/bookings/user/{userId}` | ✅ | Reservas do usuário |
| PUT | `/bookings/{id}` | ✅ | Atualizar reserva |
| PATCH | `/bookings/{id}/cancel` | ✅ | Cancelar reserva |
| PATCH | `/bookings/{id}/status` | ✅ | Atualizar status (body: `{"statusName": "CONFIRMED"}`) |
| DELETE | `/bookings/{id}` | ✅ | Remover reserva |

### Avaliações (`/reviews`)

| Método | Endpoint | Auth | Descrição |
|---|---|---|---|
| POST | `/reviews` | ✅ | Criar avaliação |
| GET | `/reviews` | ✅ | Listar todas |
| GET | `/reviews/{id}` | ✅ | Buscar por ID |
| GET | `/reviews/booking/{bookingId}` | ✅ | Avaliações de uma reserva |
| GET | `/reviews/destination/{destId}` | ✅ | Avaliações do destino (com nome do usuário) |
| PUT | `/reviews/{id}` | ✅ | Atualizar avaliação |
| DELETE | `/reviews/{id}` | ✅ | Remover avaliação |

### IA (`/ai`)

| Método | Endpoint | Auth | Descrição |
|---|---|---|---|
| POST | `/ai/chat` | ✅ | Chat multi-turno com ARIA |
| POST | `/ai/recommendations` | ✅ | Gerar recomendação (RAG + Tooling) |
| GET | `/ai/recommendations` | ✅ | Listar todas as recomendações |
| GET | `/ai/recommendations/{id}` | ✅ | Buscar recomendação por ID |
| GET | `/ai/recommendations/user/{userId}` | ✅ | Histórico do usuário |

**Exemplo — POST `/ai/chat`:**
```json
{
  "userId": 1,
  "messages": [
    { "role": "user", "content": "Quero ir para a Lua, quanto custa?" }
  ]
}
```

---

## Pré-requisitos

- **Java 21** — [Download](https://adoptium.net/)
- **Docker Desktop** — [Download](https://www.docker.com/products/docker-desktop/)
- **Python 3.11+** (apenas para o backend Python)
- Acesso à rede da **FIAP** ou VPN (para o Oracle Database)

---

## Rodando localmente

### 1. Clonar e configurar

```bash
git clone <repo-url>
cd orbit-parent
```

### 2. Subir o RabbitMQ via Docker

```bash
docker run -d --name rabbitmq \
  -p 5672:5672 -p 15672:15672 \
  rabbitmq:3-management-alpine
```

Painel RabbitMQ disponível em: http://localhost:15672 (guest/guest)

### 3. Iniciar os serviços Java (em ordem)

Abra um terminal para cada serviço:

```bash
# Terminal 1 — Eureka (obrigatório primeiro)
cd discovery-server
./gradlew bootRun

# Terminal 2 — Auth Service
cd auth-service
./gradlew bootRun

# Terminal 3 — Booking Service
cd booking-service
./gradlew bootRun

# Terminal 4 — AI Service
cd ai-service
./gradlew bootRun

# Terminal 5 — API Gateway (por último)
cd api-gateway
./gradlew bootRun
```

> **Windows:** use `gradlew.bat bootRun` no lugar de `./gradlew bootRun`

Aguarde cada serviço registrar-se no Eureka antes de iniciar o próximo.  
Painel Eureka disponível em: http://localhost:8761

### 4. Iniciar o backend Python (opcional)

```bash
cd ../orbitbook-api

# Criar .env com as credenciais
cp .env.example .env   # ou edite manualmente

pip install -r requirements.txt
uvicorn main:app --reload --port 8000
```

Docs Python em: http://localhost:8000/docs

---

## Rodando com Docker

### 1. Criar o arquivo `.env` na raiz do `orbit-parent`

```bash
# orbit-parent/.env
GEMINI_API_KEY=sua_chave_aqui
```

### 2. Build e subir todos os serviços

```bash
cd orbit-parent
docker compose up --build
```

> O primeiro build leva alguns minutos (compila todos os JARs com Gradle dentro dos containers).

### 3. Aguardar a sequência de inicialização

O Docker Compose gerencia a ordem de dependências via `healthcheck`:

```
rabbitmq          → healthy
discovery-server  → healthy (via /actuator/health)
auth-service      → inicia
booking-service   → inicia
ai-service        → inicia
api-gateway       → inicia (por último)
```

### 4. Verificar os serviços

| URL | Descrição |
|---|---|
| http://localhost:8080 | API Gateway (ponto de entrada único) |
| http://localhost:8761 | Eureka Dashboard |
| http://localhost:15672 | RabbitMQ Management (guest/guest) |
| http://localhost:8081/swagger-ui.html | Swagger auth-service |
| http://localhost:8082/swagger-ui.html | Swagger booking-service |
| http://localhost:8083/swagger-ui.html | Swagger ai-service |

### 5. Parar os containers

```bash
docker compose down

# Para remover também os volumes:
docker compose down -v
```

---

## Variáveis de ambiente

### `orbit-parent/.env` (para Docker Compose)

```env
GEMINI_API_KEY=sua_chave_gemini_aqui
```

### Sobreposição via `docker-compose.yml`

O compose sobrepõe automaticamente as configurações de ambiente:

| Variável | Valor no Docker | Serviço |
|---|---|---|
| `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | `http://discovery-server:8761/eureka/` | Todos |
| `SPRING_RABBITMQ_HOST` | `rabbitmq` | booking-service |
| `SPRING_AI_GOOGLE_GENAI_API_KEY` | `${GEMINI_API_KEY}` | ai-service |

### Oracle Database (configurado nos `application.yaml`)

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@oracle.fiap.com.br:1521/orcl
    username: rm560077
    password: 300903
```

> O acesso ao Oracle requer conectividade com a rede FIAP (presencial ou VPN).

---

## Documentação da API

Após subir os serviços, acesse o Swagger de cada serviço diretamente:

- **auth-service:** http://localhost:8081/swagger-ui.html
- **booking-service:** http://localhost:8082/swagger-ui.html  
- **ai-service:** http://localhost:8083/swagger-ui.html
- **Python (FastAPI):** http://localhost:8000/docs

Ou via Gateway (endpoints protegidos por JWT):
- Primeiro faça login em `POST http://localhost:8080/auth/login`
- Use o token retornado como `Bearer` nos demais endpoints

### Exemplo de fluxo completo

```bash
# 1. Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "usuario@email.com", "password": "senha123"}'

# 2. Usar o token retornado
TOKEN="eyJhbGc..."

# 3. Listar destinos com filtro
curl http://localhost:8080/destinations/page?tipo=LUNAR&page=1&limit=5 \
  -H "Authorization: Bearer $TOKEN"

# 4. Chat com ARIA
curl -X POST http://localhost:8080/ai/chat \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "messages": [
      {"role": "user", "content": "Qual destino você recomenda para uma primeira viagem espacial?"}
    ]
  }'
```

---

## Integrantes

| Nome | RM |
|---|---|
| Caio Lucas | RM560601 |
| (Colega Python) | RM560077 |

---

*FIAP — Global Solution 2026/1 — Turismo Espacial com IA*
