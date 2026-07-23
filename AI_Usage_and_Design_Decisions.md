# AI Usage and Design Decisions

## 1. What did I ask the AI to do, and what did I write or decide myself?

I used ChatGPT as a development assistant throughout this assignment. Instead of asking it to generate the complete application in one prompt, I built the project step by step and used AI mainly to generate boilerplate code, explain Spring Boot concepts, review my implementation, and help debug issues whenever I got stuck.

The overall design and implementation decisions were made by me. Before writing code, I planned the implementation in the following order:

* Designed the REST APIs (`POST /shorten` and `GET /{code}`).
* Designed the request and response DTOs.
* Designed the database entity.
* Identified the repository methods required by the business logic.
* Decided the short-code generation strategy.
* Implemented the service layer.
* Implemented the controller layer.
* Added global exception handling.
* Configured MySQL persistence.
* Integrated Swagger/OpenAPI documentation.
* Added unit tests.
* Prepared the README and project documentation.

Some important design decisions I made during implementation were:

* Started with the API design so that the API contract drove the rest of the implementation.
* Followed a layered architecture (Controller → Service → Repository) to keep responsibilities clearly separated and the code easy to maintain.
* Used separate request and response DTOs instead of exposing database entities directly.
* Kept the `UrlMapping` entity simple with only the fields required for the assignment.
* Added only the repository methods required by the business logic (`findByOriginalUrl`, `findByShortCode`, and `existsByShortCode`) instead of relying only on generic CRUD operations.
* Chose Base62 encoding with a fixed short-code length of **7** because it produces compact, URL-safe identifiers while keeping the implementation simple.
* Decided that shortening the same long URL multiple times should return the existing short URL instead of creating duplicate mappings.
* Supported optional custom aliases while ensuring alias uniqueness.
* Added collision checking before persisting every generated short code.
* Performed request validation using Bean Validation before requests reached the business logic.
* Introduced a common `ErrorResponse` model so every API error follows a consistent response format.
* Used appropriate HTTP status codes (`201 Created`, `301 Moved Permanently`, `400 Bad Request`, `404 Not Found`, and `409 Conflict`).
* Added Swagger/OpenAPI to make API verification easier.
* Kept the implementation simple and aligned with the assignment requirements instead of introducing unnecessary abstractions.

Every AI-generated code snippet was reviewed before becoming part of the project. If something didn't match my understanding or the assignment requirements, I modified it before using it.

---

## 2. Where did I override, correct, or throw away the AI's output — and why?

I didn't accept AI suggestions blindly. I reviewed every generated code snippet and made several changes during implementation.

Some examples include:

* Built the application layer by layer instead of generating everything together.
* Started with API design before implementing the business logic.
* Simplified several implementations to keep the project focused on the assignment requirements.
* Decided that duplicate long URLs should return the existing short URL instead of creating duplicate database records.
* Added collision checking before persisting every generated short code.
* Implemented support for optional custom aliases while ensuring uniqueness.
* Introduced a common `ErrorResponse` model instead of using the default framework error responses.
* Simplified the Swagger configuration and README by removing unnecessary metadata.
* Corrected multiple issues while writing unit tests because of Spring Boot 4 API changes, Mockito argument matching, and framework-specific behaviour.

These changes helped keep the implementation simple, maintainable, and aligned with the assignment requirements.

---

## 3. The two biggest trade-offs I made, and the alternatives I considered

### Trade-off 1: Short-code generation

For this assignment, I deliberately chose a random Base62 string of length **7**. Before saving it, the application checks whether the generated code already exists in the database. This keeps the implementation simple while ensuring correctness. A 7-character Base62 string provides more than 3.5 trillion possible combinations, making collisions extremely unlikely for the expected scale of this project.

**Alternative considered:**

For a production-scale distributed system, I would prefer using a distributed ID generator such as **Twitter Snowflake**. The generated numeric ID can then be encoded into Base62 to produce compact, URL-safe identifiers. This approach guarantees globally unique IDs across multiple application instances, scales much better under high write throughput, and avoids repeated random generation and collision checks.

### Trade-off 2: Handling duplicate URLs

I decided that if the same long URL is shortened multiple times, the application should return the existing short URL instead of creating another database record.

**Alternative considered:**

Always generating a new short URL for every request.

I preferred returning the existing mapping because it avoids duplicate records, keeps the database cleaner, reduces unnecessary storage, and provides deterministic behaviour for repeated requests.

---

## 4. What's missing, or what would I do with another day?

With another day, I would extend the project with features commonly expected in a production-ready URL shortening service, such as:

* Link analytics (click count and access statistics)
* URL expiration support
* Redis caching
* Rate limiting
* Docker support
* User authentication and user-specific URL management
* Custom domain support
* Integration tests using Testcontainers
* Monitoring, metrics, and structured logging

Overall, AI helped me speed up development by generating initial implementations, explaining concepts, and assisting with debugging. However, the project structure, API design, implementation sequence, business rules, and final implementation decisions were made by me. I reviewed every AI-generated suggestion, modified it whenever required, and made sure I understood every part of the implementation so that I can confidently explain or extend the project during a follow-up discussion.
