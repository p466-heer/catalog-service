
# Polar Bookshop — Catalog Service API Demo

Base URL: http://localhost:9001

This document demonstrates the Catalog Service REST API using curl.

## GET /books — 200 OK

Command:

```bash
curl -i http://localhost:9001/books
```

Response:

```text
HTTP/1.1 200
Content-Type: application/json
Content-Length: 2

[]
```

## POST /books — 201 Created

Command:

```bash
curl -i -X POST http://localhost:9001/books \
  -H "Content-Type: application/json" \
  -d '{"isbn":"1234567891","title":"Northern Lights","author":"Lyra Silverstar","price":9.90}'
```

Response:

```text
HTTP/1.1 201
Content-Type: application/json
Content-Length: 86

{"isbn":"1234567891","title":"Northern Lights","author":"Lyra Silverstar","price":9.9}
```

## GET /books/{isbn} — 200 OK

Command:

```bash
curl -i http://localhost:9001/books/1234567891
```

Response:

```text
HTTP/1.1 200
Content-Type: application/json
Content-Length: 86

{"isbn":"1234567891","title":"Northern Lights","author":"Lyra Silverstar","price":9.9}
```

## POST /books — 400 Bad Request

Command:

```bash
curl -i -X POST http://localhost:9001/books \
  -H "Content-Type: application/json" \
  -d '{"isbn":"123ABC456Z","title":"","author":"Jon Snow","price":9.90}'
```

Previously recorded response:

```text
HTTP/1.1 400
{"isbn":"The ISBN format must be valid.","title":"The book title must be defined."}
```

## GET /books/{isbn} — 404 Not Found

Command:

```bash
curl -i http://localhost:9001/books/9999999999
```

Previously recorded response:

```text
HTTP/1.1 404
The book with ISBN 9999999999 was not found.
```

## POST /books — 422 Unprocessable Entity

Run after creating the book with ISBN 1234567891.

Command:

```bash
curl -i -X POST http://localhost:9001/books \
  -H "Content-Type: application/json" \
  -d '{"isbn":"1234567891","title":"Northern Lights","author":"Lyra Silverstar","price":9.90}'
```

Previously recorded response:

```text
HTTP/1.1 422
A book with ISBN 1234567891 already exists.
```

## PUT /books/{isbn} — 200 OK

Command:

```bash
curl -i -X PUT http://localhost:9001/books/1234567891 \
  -H "Content-Type: application/json" \
  -d '{"isbn":"1234567891","title":"Northern Lights Updated","author":"Lyra Silverstar","price":12.90}'
```

Response:

```text
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked

{"isbn":"1234567891","title":"Northern Lights Updated","author":"Lyra Silverstar","price":12.9}
```

## PUT /books/{isbn} — 201 Created

This demonstrates Exercise E1. PUT creates a book when the ISBN does not already exist.

Command:

```bash
curl -i -X PUT http://localhost:9001/books/4444444444 \
  -H "Content-Type: application/json" \
  -d '{"isbn":"4444444444","title":"New Book","author":"Test Author","price":15.00}'
```

Response:

```text
HTTP/1.1 201
Content-Type: application/json
Transfer-Encoding: chunked

{"isbn":"4444444444","title":"New Book","author":"Test Author","price":15.0}
```

## DELETE /books/{isbn} — 204 No Content

Command:

```bash
curl -i -X DELETE http://localhost:9001/books/1234567891
```

Response:

```text
HTTP/1.1 204
```

The response has no body.

## GET /books/{isbn} — 404 After Deletion

Command:

```bash
curl -i http://localhost:9001/books/1234567891
```

Response:

```text
HTTP/1.1 404
Content-Type: text/plain;charset=UTF-8
Content-Length: 44

The book with ISBN 1234567891 was not found.
```
