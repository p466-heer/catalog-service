
# Polar Bookshop — Catalog Service API Demo

Base URL: http://localhost:9001

## GET /books — 200 OK

Command:
```bash
curl -i http://localhost:9001/books
```

Response:
```text
Paste your actual response here.
```

## GET /books/{isbn} — 200 OK

Command:
```bash
curl -i http://localhost:9001/books/1234567891
```

Response:
```text
Paste your actual response here.
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
Paste your actual response here.
```

## POST /books — 400 Bad Request

Command:
```bash
curl -i -X POST http://localhost:9001/books \
  -H "Content-Type: application/json" \
  -d '{"isbn":"123ABC456Z","title":"","author":"Jon Snow","price":9.90}'
```

Response:
```text
HTTP/1.1 400
{"isbn":"The ISBN format must be valid.","title":"The book title must be defined."}
```

## GET /books/{isbn} — 404 Not Found

Command:
```bash
curl -i http://localhost:9001/books/9999999999
```

Response:
```text
HTTP/1.1 404
The book with ISBN 9999999999 was not found.
```

## POST /books — 422 Unprocessable Entity

Command:
```bash
curl -i -X POST http://localhost:9001/books \
  -H "Content-Type: application/json" \
  -d '{"isbn":"1234567891","title":"Northern Lights","author":"Lyra Silverstar","price":9.90}'
```

Response (when the ISBN already exists):
```text
HTTP/1.1 422
A book with ISBN 1234567891 already exists.
```

## PUT /books/{isbn} — 200 OK

Command (run after creating the book):
```bash
curl -i -X PUT http://localhost:9001/books/1234567891 \
  -H "Content-Type: application/json" \
  -d '{"isbn":"1234567891","title":"Northern Lights Updated","author":"Lyra Silverstar","price":12.90}'
```

Response:
```text
Paste your actual response here.
```

## DELETE /books/{isbn} — 204 No Content

Command:
```bash
curl -i -X DELETE http://localhost:9001/books/1234567891
```

Response:
```text
Paste your actual response here.
```
