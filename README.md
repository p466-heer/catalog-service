# Catalog Service

[![Commit Stage](https://github.com/p466-heer/catalog-service/actions/workflows/commit-stage.yml/badge.svg)](https://github.com/p466-heer/catalog-service/actions/workflows/commit-stage.yml)

## Description

Catalog Service is a Spring Boot REST API for managing the Polar Bookshop book catalog.

It supports listing, finding, adding, updating, and deleting books.

## Exercise E1 — Adding a Publisher Column

The `publisher` column was added as nullable to maintain backward compatibility with existing book records. Existing books do not have publisher information, so requiring a value immediately could cause migration failures or disrupt the application.

### Two-Release Migration Strategy

**Release 1:** Add the nullable `publisher` column using a Flyway migration. Update the application to support the new field while allowing existing books and API requests without a publisher to continue working.

**Release 2:** After publisher information has been populated for existing books and all clients have been updated, a later migration could add a `NOT NULL` constraint if publisher information becomes mandatory.

This approach allows the database schema and application to evolve without breaking existing functionality.

## Exercise E4 — DELETE on a Missing Book

Returning 204 No Content when deleting a book that does not exist is acceptable because DELETE is an idempotent HTTP method. This means that deleting the same book multiple times should have the same effect on the server as deleting it once. Since the book is absent after the request, returning 204 indicates that the deletion request has been handled successfully.