# URL Shortener
## Introduction
URL Shortener is a web application built using Java with Spring Boot framework.
It provides functionality to shorten long URLs into manageable links, making it easier to share and remember.
This project utilizes Spring Boot along with Spring Data Redis for caching and Spring Data JPA for database interaction.

## Features
- Shorten long URLs to compact links.
- Redirect users from shortened links to original URLs.
- Efficient caching using Redis for improved performance.
- MySQL integration for persistent storage of URLs.
- Built with a RESTful API for easy integration with other applications.
- OpenAPI(Swagger) for api docs.
- Includes Lombok for reducing boilerplate code.

## Requirements
- Java 21 or higher.
- Maven for building the project.
- MySQL database for production deployment.
- Redis for caching (can be run locally or configured with a cloud service).
- Any preferred IDE for development (Eclipse, IntelliJ IDEA, etc.).

## Setup
1. Clone this repository to your local machine.
    ```
    git clone https://github.com/ahmd-nabil/url-shortener.git    
    ``` 
2. Navigate to the project directory.
    ```
    cd {url-shortener-path}
    ``` 
3. Ensure you have Java and Maven installed on your system.
4. Configure MySQL database settings in `application.properties` under `src/main/resources`.
5. Start Redis server either locally or use a cloud service.
6. Build the project using Maven.
    ```
    mvn clean install
    ```
7. Run the application.
    ```
    mvn spring-boot:run
    ```
## Usage
- Access the application through the provided [endpoints](http://localhost:8080/swagger-ui/index.html) to shorten URLs and retrieve original ones.
- Use tools like cURL, Postman, or any HTTP client to interact with the RESTful API.
- Monitor Redis cache for caching efficiency and database for storing URLs.

## Contributing
Contributions are welcome (with no rules)! Feel free to open issues for any bugs, feature requests, or improvements. Pull requests are also appreciated.