# Book-booking

----
## Содержание
- [Build and Run](#Running with Docker)
  - [Running with Docker](#Running with Docker)
  - [Running without Docker](#Running without Docker)



## Running with Docker
Before using the Docker configuration files, ensure that you have the following software installed on your machine:

- Docker: [Install Docker](https://docs.docker.com/get-docker/)
- Docker Compose: [Install Docker Compose](https://docs.docker.com/compose/install/)

### Usage
To use the Docker configuration files, follow the steps below:

1. Clone this repository to your local machine:
    ```
    git clone https://github.com/DmBalaev/Book-booking.git
    ```
2. Navigate to the project directory:
    ```
    cd Book-booking
    ```
3. Build the maven project
   ```
   mvn clean install -DskipTests=true
   ```   
4. Start the containers defined in the docker-compose.yaml file:
   ```
   docker-compose up -d
   ```
    The -d flag runs the containers in the background (detached mode).

## Running without Docker
This section provides instructions for running the Project Name project without Docker. You may choose to 
run the project without Docker if you prefer to set up the environment manually or if Docker is not available on your system.

### Prerequisites
Before running the project without Docker, ensure that you have the following software installed on your machine:

- Java Development Kit (JDK): [Install JDK](https://www.oracle.com/java/technologies/downloads/#java17)
- Maven: [Install Maven](https://maven.apache.org/install.html)
- PostgreSQL: [Install PostgreSQL](https://www.postgresql.org/download/)
- Redis: [Install Redis](https://redis.io/download/)

### Configuration
To configure the project for running without Docker, you will need to update the necessary configuration files.

### Application Configuration
1. Locate the application.yml file in the project's configuration directory.
   ```
   /path/to/Book-booking/src/main/resources/application.yml
   ```
2. Open the application.yml file in a text editor.
3. Update the configuration properties as per your environment, such as the database connection details, API keys, 
or any other relevant settings.
   ```yaml
   spring:
      redis:
        host: localhost
        port: 6379 
      datasource:
         url: jdbc:postgresql://db:5432/booking_db
         username: adm
         password: 123
   ```
   Update the url, username, and password values according to your PostgreSQL configuration.
   
4. Save the changes to the application.yml file.

### Build and Run
1. Open a terminal or command prompt.
2. Navigate to the project directory
3. Build the project using Maven:
   ```
   mvn clean package
   ```
   This command compiles the source code, runs the tests, and packages the application into an executable JAR file.
4. Once the build is successful, you can run the application using the following command:
   ```
   java -jar target/Book-booking-0.0.1-SNAPSHOT.jar
   ```
   The application should start, and you can access it in your web browser at http://localhost:8080

