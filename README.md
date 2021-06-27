
Possible improvements to the current solution:
- Currently, there are some example unit tests that mock methods and services, also there is
 one integration test. But more tests can be added to verify the correct behaviour 
 of every component following the same approach.
 
- In terms of parallel processing, currently there is one thread pushing the events that 
 are ready to be persisted into a BlockingQueue, and another thread actually that persists such events. 
 More threading can be added to improve multiprocessing in the same JVM.

- In order to scale horizontally, instead of using a map in memory to keep ids that have not yet
 found both STARTED and FINISHED events, we could use either files shared between the nodes or
 temporary tables in the DB sharding data based on ID, many nodes could process different chunks
 of the input file in parallel.

Prerequisites:
- Java 8
- Maven

NOTES:

The current application log level is set to INFO and it will be saved in the file "out.log"

To run all tests:
**mvn clean test**

To run the application, make sure to pass the input file as argument:
**mvn spring-boot:run -Dspring-boot.run.arguments="input.log"**