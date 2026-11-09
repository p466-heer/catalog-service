# Configuration Demo

## 1. Command-Line Override

Command:
```bash
java -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar --polar.greeting="Hello from CLI"
```

Result:
```text
Hello from CLI
```

## 2. Environment Variable Override

Command:
```bash
POLAR_GREETING="Hello from Environment" java -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar
```

Result:
```text
Hello from Environment
```

## 3. Configuration Precedence

Command:
```bash
POLAR_GREETING="Hello from Environment" java -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar --polar.greeting="Hello from CLI"
```

Result:
```text
Hello from CLI
```

## Conclusion

Spring Boot supports configuration through application.yml, environment variables, and command-line arguments.

Command-line arguments have higher precedence than environment variables, which have higher precedence than application.yml.
