# Twan Journey CMS

Monorepo for a simple CMS consisting of:
- Backend: Spring Boot 3 (Java 21, Maven)
- Frontend: Next.js 15 + React 19 + TypeScript + Tailwind CSS 4

## Structure

- `cms-backend/` — Spring Boot app (Maven Wrapper included)
- `cms-frontend/` — Next.js app (App Router)

## Prerequisites

- Node.js 20+ and pnpm (Corepack recommended: `corepack enable`)
- Java 21 (JDK 21)
- Maven not required globally (the project uses `mvnw`)

## Quick start

Open two terminals—one for the frontend and one for the backend.

Frontend (Next.js):

```bash
cd cms-frontend
pnpm install
pnpm dev
# Next.js runs at http://localhost:3000
```

Backend (Spring Boot):

```bash
cd cms-backend
./mvnw spring-boot:run
# Starts the Spring Boot app. If you add spring-boot-starter-web, the default port is 8080.
```

## Environment configuration

- Frontend: `cms-frontend/.env.local`
  - Example:
    - `NEXT_PUBLIC_API_BASE_URL=http://localhost:8080`
- Backend: `cms-backend/src/main/resources/application.properties`
  - Example:
    - `server.port=8081`

## Scripts

Frontend:
- `pnpm dev` — start dev server
- `pnpm build` — production build
- `pnpm start` — run the built app
- `pnpm lint` — lint with ESLint

Backend:
- `./mvnw spring-boot:run` — run the app
- `./mvnw test` — run tests
- `./mvnw clean package` — build JAR in `cms-backend/target/`

## Production builds

Frontend:
```bash
cd cms-frontend
pnpm install
pnpm build
pnpm start
```

Backend:
```bash
cd cms-backend
./mvnw clean package
java -jar target/cms-backend-0.0.1-SNAPSHOT.jar
```

## Development notes

- Backend currently has the base Spring Boot setup. To expose REST endpoints, add the dependency below to `cms-backend/pom.xml` and create controllers:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

- Frontend entry is `cms-frontend/src/app/page.tsx`; global styles live in `src/app/globals.css`.

## Troubleshooting

- Port conflicts: change Next.js port via `PORT=3001 pnpm dev` or set Spring port with `server.port=8081`.
- Java version: ensure `java -version` shows 21.
- Node version: ensure `node -v` is 20+.

## Docker (local)

If you want to run the Next.js app in a container locally, build a small base image with dependencies first (pnpm), then build the app image that uses it.

1) Build the Next.js base image (dependencies only) using the provided base Dockerfile:

```bash
docker build -t cms-frontend:base -f cms-frontend/Dockerfile.base cms-frontend
```

The base image installs pnpm and resolves dependencies using the lockfile for better cache reuse.

2) Build the frontend image using the base

```bash
# BASE_IMAGE points to the base we just built; IMAGE is the runtime
DOCKER_BUILDKIT=1 docker build \
  -t cms-frontend:local \
  --build-arg BASE_IMAGE=cms-frontend:base \
  --build-arg IMAGE=node:20.18.3-alpine \
  -f cms-frontend/Dockerfile cms-frontend
```

3) Run the frontend container

```bash
docker run --rm -p 3000:3000 cms-frontend:local
# Open http://localhost:3000
```

Notes:
- The current Dockerfile defines optional lint/test stages; if those scripts aren’t present, build the final image directly by targeting the `runner` stage:

```bash
docker build -t cms-frontend:local \
  --target runner \
  --build-arg BASE_IMAGE=cms-frontend:base \
  --build-arg IMAGE=node:20.18.3-alpine \
  -f cms-frontend/Dockerfile cms-frontend
```

- For full-stack containers, add a backend Dockerfile and a root `docker-compose.yml` to orchestrate both services with networking (e.g., frontend env `NEXT_PUBLIC_API_BASE_URL=http://backend:8080`).

## Docker Compose (dev stack)

Use the provided `docker-compose.dev.yml` to run Postgres, the Spring Boot backend, and the Next.js frontend together.

1) Build the reusable base images (faster subsequent builds):

```bash
docker compose -f docker-compose.dev.yml --profile base build backend-base frontend-base
```

2) Bring the stack up:

```bash
docker compose -f docker-compose.dev.yml up --build
# Ctrl+C to stop, or run in detached mode with -d
```

Services and ports:
- Postgres: localhost:5432 (db: `cmsdb-dev`, user: `postgres`, pass: `postgres`)
- Backend: http://localhost:8080
  - Spring is configured via env vars in compose:
    - `SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/cmsdb-dev`
    - `SPRING_DATASOURCE_USERNAME=postgres`
    - `SPRING_DATASOURCE_PASSWORD=postgres`
- Frontend: http://localhost:3000
  - `NEXT_PUBLIC_API_URL` points to `http://backend:8080` inside the compose network

Notes:
- Base images built by the profile are tagged as:
  - `cms-backend:base-backend` (from `cms-backend/Dockerfile.base`)
  - `cms-frontend:base` (from `cms-frontend/Dockerfile.base`)
- Compose uses those bases to speed up the main images’ builds.
- If you don’t want to build bases, you can skip step 1; builds will still work, just slower on cold caches.
- To actually use Postgres in the backend, add the JDBC driver and JPA starter to `cms-backend/pom.xml`:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <scope>runtime</scope>
</dependency>
```

## License

Add a license file if you plan to distribute.