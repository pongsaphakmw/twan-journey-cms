# Twan Journey CMS

Monorepo for a simple CMS consisting of:
- Backend: Spring Boot 3 (Java 21, Maven)
- Frontend: Next.js 15 + React 19 + TypeScript + Tailwind CSS 4

## Structure

- `cms-backend/` — Spring Boot app (Maven Wrapper included)
- `cms-frontend/` — Next.js app (App Router)

## Prerequisites

- Node.js 20+ and npm (or yarn/pnpm/bun)
- Java 21 (JDK 21)
- Maven not required globally (the project uses `mvnw`)

## Quick start

Open two terminals—one for the frontend and one for the backend.

Frontend (Next.js):

```bash
cd cms-frontend
npm install
npm run dev
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
- `npm run dev` — start dev server
- `npm run build` — production build
- `npm start` — run the built app
- `npm run lint` — lint with ESLint

Backend:
- `./mvnw spring-boot:run` — run the app
- `./mvnw test` — run tests
- `./mvnw clean package` — build JAR in `cms-backend/target/`

## Production builds

Frontend:
```bash
cd cms-frontend
npm install
npm run build
npm start
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

- Port conflicts: change Next.js port via `PORT=3001 npm run dev` or set Spring port with `server.port=8081`.
- Java version: ensure `java -version` shows 21.
- Node version: ensure `node -v` is 20+.

## License

Add a license file if you plan to distribute.