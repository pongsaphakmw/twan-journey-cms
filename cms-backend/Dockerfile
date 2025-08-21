ARG BASE_IMAGE=base-backend

FROM ${BASE_IMAGE} AS jvm-builder
WORKDIR /src
COPY . .
RUN mvn -B -DskipTests package

ARG JAR_FILE=
RUN set -eux; \
  JAR="$(ls -1 target/*.jar | head -n1)"; \
  cp "$JAR" /app.jar

FROM eclipse-temurin:21 AS jvm-runtime
WORKDIR /app
RUN addgroup --system --gid 1001 springboot
RUN adduser --system --uid 1001 springboot

# Copy the built artifact from the builder stage
COPY --from=jvm-builder /app.jar /app/app.jar

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+HeapDumpOnOutOfMemoryError" \
    SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    TZ=Asia/Bangkok
EXPOSE 8080
USER springboot
ENTRYPOINT ["java","-jar","/app/app.jar"]