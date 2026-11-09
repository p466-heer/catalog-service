
# Container Build Observations

## Docker Layer Caching

The initial Docker image build took 6.9 seconds.

Rebuilding without any changes took 0.7 seconds. Docker reused all previously built layers.

After modifying one line in BookController.java and rebuilding the JAR, the Docker image build took 1.1 seconds. The dependencies, Spring Boot loader, and snapshot-dependencies layers were cached, while the application layer was rebuilt.

## Non-Root User

The container runs as the spring user rather than root.

Output:
uid=1001(spring) gid=1001(spring) groups=1001(spring)
