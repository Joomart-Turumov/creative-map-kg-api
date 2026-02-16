#!/bin/sh

# Convert Render's DATABASE_URL (postgres://user:pass@host:port/db) to JDBC format
if [ -n "$DATABASE_URL" ] && [ -z "$DATABASE_HOST" ]; then
  JDBC_URL=$(echo "$DATABASE_URL" | sed 's|^postgres://\([^:]*\):\([^@]*\)@\(.*\)|jdbc:postgresql://\3|')
  DB_USER=$(echo "$DATABASE_URL" | sed 's|^postgres://\([^:]*\):.*|\1|')
  DB_PASS=$(echo "$DATABASE_URL" | sed 's|^postgres://[^:]*:\([^@]*\)@.*|\1|')

  export SPRING_DATASOURCE_URL="$JDBC_URL"
  export SPRING_DATASOURCE_USERNAME="$DB_USER"
  export SPRING_DATASOURCE_PASSWORD="$DB_PASS"
fi

exec java -jar app.jar --spring.profiles.active=prod
