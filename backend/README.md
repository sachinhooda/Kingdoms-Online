
# Kingdoms Online Backend

Spring Boot 3.x backend for **Kingdoms Online** — a real-time empire building strategy game.

## Features

- REST APIs (Player, Kingdom, Battles, Upgrades)
- JSONB data fields in PostgreSQL
- JWT-based Security
- Redis-compatible queue system
- Swagger UI + Spring Actuator
- GraalVM native support
- Clean DTO + ApiResponse structure
- OpenTelemetry-compatible tracing (optional)

---

## 🔧 Local Dev Setup

### Database Setup

```sql
CREATE DATABASE kingdoms;
CREATE USER kingdoms_user WITH PASSWORD 'kingdoms_pass';
GRANT ALL PRIVILEGES ON DATABASE kingdoms TO kingdoms_user;
```

Configure in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/kingdoms
    username: kingdoms_user
    password: kingdoms_pass
```

---

### Devtools Auto-Restart Setup

1. Make sure this is in `build.gradle`:
```groovy
developmentOnly 'org.springframework.boot:spring-boot-devtools'
```

2. Run using:
```bash
./gradlew bootRun
```

3. Enable auto-build/save in your IDE (e.g., IntelliJ):
- Enable **Build project automatically**
- Enable registry flag `compiler.automake.allow.when.app.running`

---

### Swagger & Actuator

- Swagger: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- Actuator: [http://localhost:8080/actuator](http://localhost:8080/actuator)

---

### Native Compilation

```bash
./gradlew nativeCompile
./build/native/nativeCompile/game-app
```

---

Let's build the empire 🏰

WIP.....................
```Pending common app code TODOS
1. Use Logging framework.
2. Add Tracing and Metrics capabilities.
3. APIs Security etc.
4. Use Minikube.
5. Unit tests.

