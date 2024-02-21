# SAMIR TEST
## BACKEND JAVA SPRING BOOT
### PACKAGE :
- Spring Web MVC
- Spring Data JPA
- postgresql driver
- Lombok
- Jakarta Validation

# documentation is here :

# Auth API Spec

## Register User
Endpoint : POST /api/auth/register

Request Body  :

```json
{
  "username": "username",
  "password": "password",
  "name": "name",
  "email": "email@example.com"
}
```
Response Body (Success) :
```json
{
  "data" : "OK"
}
```

Response Body (Failed) :
```json
{
  "errors": "Username must not be blank"
}
```

## Login
Endpoint : POST /api/auth/login

Request Body  :

```json
{
  "username": "username",
  "password": "password"
}
```
Response Body (Success) :
```json
{
  "data" : {
    "token": "TOKEN",
    "expiredAt" : 123123123
  }
}
```

Response Body (Failed, 401) :
```json
{
  "errors": "Credentials mis match"
}
```

## Logout User
Endpoint : Delete /api/auth/logout

Request Header :
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
```json
{
  "data": "OK"
}
```

# User API Spec

## Get User
Endpoint : GET /api/users/current

Request Header :
- X-API-TOKEN : Token (Mandatory)


Response Body (Success) :
```json
{
  "data" : {
    "id": "id",
    "username": "username",
    "email" : "email@example.com",
    "name": "name"
  }
}
```

Response Body (Failed, 401) :
```json
{
  "errors": "unnauthorized"
}
```

## Update User
Endpoint : PATCH /api/users

Request Header :
- X-API-TOKEN : Token (Mandatory)

Request Body (Success) :
```json
{
  "data" : {
    "name": "name",
    "email": "email@example.com",
    "password": "new password"
  }
}
```
Response Body (Success) :
```json
{
  "data" : {
    "username": "username",
    "name": "name"
  }
}
```

Response Body (Failed, 401) :
```json
{
  "errors": "unnauthorized"
}
```

## Logout User
Endpoint : Delete /api/auth/logout

Request Header :
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
```json
{
  "data": "OK"
}
```

# Task API Spec

## Get User
Endpoint : GET /api/tasks

Query Param : 
- name
- status (Boolean)
- page
- size

Request Header :
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
```json
{
  "data" : [
      {
        "id": "id",
        "name": "Task 1",
        "note": "Note",
        "status" : "Completed"
      },{
        "id": "id",
        "name": "Task 2",
        "note": "Note",
        "status": "UNCOMPLETED"
      },{
        "id": "id",
        "name": "name",
        "note": "Note",
        "status": "Completed"
      }
    ]
  ,
  "pagingResponse": {
    "currentPage" : 0,
    "totalPage" : 1,
    "size" : 1
    
  }
}
```

Response Body (Failed, 401) :
```json
{
  "errors": "unnauthorized"
}
```

## Get Task By Id
Endpoint : GET /api/tasks/{taskId}

Request Header :
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
```json
{
  "data" : {
    "id" : "id",
    "name": "Task 1",
    "note" : "note",    
    "status" : "email@example.com"    
  }
}
```

Response Body (Failed, 401) :
```json
{
  "errors": "unnauthorized"
}
```

## Create Task
Endpoint : POST /api/tasks

Request Header :
- X-API-TOKEN : Token (Mandatory)

Request Body (Success) :
```json
{
  "data" : {
    "name": "task",
    "note": "note 1",
    "status": false
  }
}
```
Response Body (Success) :
```json
{
  "data" : {
    "id" : "id",
    "name": "task",
    "note": "note 1",
    "status": "completed"
  }
}
```

Response Body (Failed, 401) :
```json
{
  "errors": "unnauthorized"
}
```
