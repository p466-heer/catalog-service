# Practicum 2 - Bonus Questions

### 1. Why is it a mistake to name feature profiles dev and prod?

Profiles should be named after features instead of environments. If we use dev and prod, it becomes harder to use the same features in other environments. Naming profiles after features makes them easier to reuse.

### 2. You set polar.greeting in application.yml, in an environment variable, and as a JVM property. Which value is used, and why is the environment variable nevertheless the recommended mechanism?

The JVM property is used because it has higher priority than the environment variable and application.yml. Environment variables are still recommended because we can change settings without changing the code or rebuilding the application.

### 3. What exactly makes Catalog Service stateless after Part B, and why does that let you run three instances of it behind a load balancer?

Catalog Service is stateless because the books are now stored in PostgreSQL instead of the application's memory. All three instances can use the same database. This means requests can go to any instance and still get the same book data.

### 4. Why test against PostgreSQL in a container instead of an in-memory H2 database?

We use PostgreSQL because it is the same database used by the application. H2 might handle some queries differently. Testing with PostgreSQL helps us catch problems that might not show up with H2.

### 5. Why must you never modify V1__Initial_schema.sql once it has been applied to a shared database?

Flyway keeps track of migrations that have already been run. If we change V1 after it has been applied, Flyway will notice that the file has changed and may fail validation. We should create a new migration instead.

### 6. Why does BookRepositoryJdbcTests insert its test data with JdbcAggregateTemplate rather than with bookRepository.save()?

We use JdbcAggregateTemplate to add test data without using the repository method we are testing. This helps us test the repository separately and makes it easier to find problems.