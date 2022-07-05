# Animventory [![Java CI with Gradle](https://github.com/vikhani/Animventory/actions/workflows/gradle.yml/badge.svg?branch=master)](https://github.com/vikhani/Animventory/actions/workflows/gradle.yml)
Test assignment for Art Plan Software.
Implements user authorization and some interactions with db.

## Build

`./gradlew build`

## Run

Ready to use .jar is available in the [Releases](https://github.com/vikhani/Animventory/releases).

`java -jar Animventory.jar`

## API
[Usage examples with Postman](https://www.getpostman.com/collections/b9c229bd07a0388b008b) (copy link and use Postman Import to import Animventory Collection)

**Registration**  
`POST /registration`

*Bodyraw (json)*  
json  
{  
  "username": "...",  
  "password": "..."  
}

**Logout**  
`POST /logout`

**Login**  
`POST /login`

- `AbstractAuthenticationProcessingFilter (org.springframework.security.web.authentication)` checks passed params via `request.getParameter(...)` which reads parameters from the query string or from form data in the body. It does not try to parse JSON body content.
More about it:
[StackOverflow](https://stackoverflow.com/questions/69362349/httpservletrequest-getparameter-of-spring-boot-handlerinterceptor-returns-null)
[Java Doc](https://docs.oracle.com/javaee/7/api/javax/servlet/ServletRequest.html#getParameterMap--)

*Bodyform-data*  
key: username
value: ...
key: password
value: ...

**Add Animal**. 
`POST /animals`

*Bodyraw (json)*  
json  
{  
  "nickname": "...",  
  "birthday": "...",  
  "gender": "...",  
  "species": "..."  
}  

- `birthday` format is "yyyy-MM-dd"
- default values inserted in `Species` table are "Unknown", "Cat", "Dog", "Crocodile"

**Get All Animals**  
`GET /animals`

**Get Animal By Id**  
`GET /animals/{id}`

**Is Animal Nickname Taken**  
`GET /animals/is_nickname_taken`

*Bodyraw (json)*  
json  
{  
  "nickname": "..."  
}  

## Stack
- Java 8
- Spring Boot
- Spring Security
- H2
- Hibernate
- Lombok
