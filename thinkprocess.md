# Think Process
In this file I will try to demonstrate my thought process for creating the application. During development a lot of things cross my mind, but I'll try to list the main ones, but I may have forgotten something :grimacing:

After reading and understanding the requirements and how the 3rd party API works. Define the following goals

#### Definition of the framework to be used:
After understanding the system requirements, I came to the conclusion that the SpringBoot Framework would be a good tool to solve the problem that the system proposes to solve.

### Delimited context definition.

The delimited context I chose is called BookReview, as the requirements say that we should only save the reviews of a certain book, not the information of the book itself (this comes from the third-party api).

### API development

After defining the tool and the delimited context, start working on the development of the application itself, focusing on writing code following principles such as SOLID, TDD. I used docker as an ally when creating project dependencies, such as Postgres and Redis databases.

### System deploy diagram
I tried with the diagram to demonstrate how I would deploy this application, adding Kubernetes for container orchestration and monitoring/logging tools.

![System Diagram](systemdiagram.png "System Diagram")