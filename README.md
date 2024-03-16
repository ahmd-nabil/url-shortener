<hr><h1>URL Shortener</h1>
<h2>Introduction</h2>
<p>URL Shortener is a web application built using Java with Spring Boot framework.
It provides functionality to shorten long URLs into manageable links, making it easier to share and remember.
This project utilizes Spring Boot along with Spring Data Redis for caching and Spring Data JPA for database interaction.</p>

<h2>Features</h2>
<ul>
    <li>Shorten long URLs to compact links.</li>
    <li>Redirect users from shortened links to original URLs.</li>
    <li>Efficient caching using Redis for improved performance.</li>
    <li>MySQL integration for persistent storage of URLs.</li>
    <li>Built with a RESTful API for easy integration with other applications.</li>
    <li>Includes Lombok for reducing boilerplate code.</li>
</ul>

<h2>Requirements</h2>
<ul>
    <li>Java 21 or higher.</li>
    <li>Maven for building the project.</li>
    <li>MySQL database for production deployment.</li>
    <li>Redis for caching (can be run locally or configured with a cloud service).</li>
    <li>Any preferred IDE for development (Eclipse, IntelliJ IDEA, etc.).</li>
</ul>

<h2>Setup</h2>
<ol>
<li> Clone this repository to your local machine.

```
git clone https://github.com/ahmd-nabil/url-shortener.git    
``` 
</li>
<li> Navigate to the project directory.

```
cd {url-shortener-path}
```
</li> 
<li>Ensure you have Java and Maven installed on your system.</li>
<li>Configure MySQL database settings in <code>application.properties</code> under <code>src/main/resources</code>.</li>
<li>Start Redis server either locally or use a cloud service.</li>
<li>Build the project using Maven.

```
mvn clean install
```
</li>
<li>Run the application. 

```
mvn spring-boot:run
```
</li>
</ol>
<h2>Usage</h2>
<ul>
    <li>Access the application through the provided endpoints to shorten URLs and retrieve original ones.</li>
    <li>Use tools like cURL, Postman, or any HTTP client to interact with the RESTful API.</li>
    <li>Monitor Redis cache for caching efficiency and database for storing URLs.</li>
</ul>

<h2>Contributing</h2><p>Contributions are welcome (with no rules)! Feel free to open issues for any bugs, feature requests, or improvements. Pull requests are also appreciated.</p>