
# Catalog Service

[![Commit Stage](https://github.com/p466-heer/catalog-service/actions/workflows/commit-stage.yml/badge.svg)](https://github.com/p466-heer/catalog-service/actions/workflows/commit-stage.yml)

## Description

Catalog Service is a Spring Boot REST API for managing the Polar Bookshop book catalog.

It supports listing, finding, adding, updating, and deleting books.


## Exercise E4 — DELETE on a Missing Book

Returning 204 No Content when deleting a book that does not exist is acceptable because DELETE is an idempotent HTTP method. This means that deleting the same book multiple times should have the same effect on the server as deleting it once. Since the book is absent after the request, returning 204 indicates that the deletion request has been handled successfully.
