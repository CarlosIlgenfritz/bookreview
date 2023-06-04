
# Book Qualifier

Book Qualifier is an application whose function is to carry out book reviews, search for book data based on the name and some endpoints that allow you to have a more analytical view of some books (perform a book ranking, etc.)

The application was built using Java,Maven, Spring Boot, Spring Data, Object Oriented Programming, JUnit, Mockito, Redis, Docker, etc.

### What do you need to run the project
To run the project you need to have installed:
- Java 17
- Maven
- Docker

### How to Install and Run the Project

- Import the project into your favorite IDE.
- Download the project dependencies using maven
- Start the Database dependencies using the docker-compose file. This file will start the database and Redis.
- On the terminal run the following command to run the application:
  `mvn spring-boot:run -Dspring-boot.run.profiles=local`



I suggest you use the Intellij IDE, as it performs many of the necessary configurations for you to run the project.


### How to use the API
The project has the following APIs at your disposal:


| HTTP Verb | API URL |What the API Do| Parameters | 
|-----------|--| -- |--|
| Get       | `/books/search?name=` | Find information for the desired book using its name | Name of the desired Book(string)  |
| Post      | `/books/save` | Save a book review |`JSON following this structure: { "bookId": Integer, "rating": Integer (1-5),review:"A good book"(String)}`
| Get       |`/books/detailed?id=`| Retrieve all available information about that book, such as title, all reviews, and get an average rating of the book | BookId(Integer)|
| Get       |`/books/average?limit=`|Returns the top N books based on their average rating, and limit being the number of books to retrieve| Limit(Integer)
| Get       | `/books/average/month?bookId=`|Given a book id, returns its average rating per month.| BookId (Integer)

The application has flyway migrations, so you don't have to worry about populating the application so you can use it.

### How to deploy

To deploy the application, we need to run the docker file, it will run all the tests and build the application.
After the dockerfile finishes, it will try to start the application, but it will fail. Due to a Docker inability, It's impossible inside the dockerfile, define a network so that it can connect with the database and with Redis. 
Then, after the Dockerfile process is finished, you can run this command in the terminal: `docker run --network=bookqualifier_network <container_id>` , which the application will run using data from the production properties file.

In a real-world scenario, the application's database could be on a cloud, such as AWS, so instead of needing to connect one container to another, the container containing the application will connect using a database URL hosted on the cloud. That removes the problem of having to connect two containers on the same machine




