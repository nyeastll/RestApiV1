# RestApiV1
 Barebone RestAPI (Enrollee & Dependent Service)

This is the demo RestAPI implementation, it simulates the enrollment tracking for a family service program.

It has some basic requirements, include:

Enrollee must have id, name, status and birth date
Enrollee may have a phone number
Enrollee may have zero or more dependents
Each Dependents must have an id, name, and birth date

Beside the Enrollee can be accessed by Restful API, each Enrollee should able to add/modify/remove one or more Dependents.

For easy to understand, I choose to make this first prototype to be built as RestAPI only, in the following
project, I will create a more complex microservice, which break the Enrollee service and Dependent service 
to be two services, and add more microservices components, such as config server, service discovery server,
API gateway, distribute tracing, centralized logging (Zipkin server), etc.

This implementation will cover both Enrollee service and Dependent service, as the full set of RestAPI, I also 
create a sample Dockerfile, and give some instruction of how I build the docker image, hopefully, this small 
prototype like project can lead you to understand what is RestAPI, and how to implement the rest call using 
RestTemplate ( enrollee service call dependent service ), so it can give us some basic understanding of how 
to create the rest call across separate service (although it was bundled into one project, there are two set 
of services with its own endpoints).

Here are the instruction of how to test it on your local system (I use Windows 10 here, so it will be slightly different if
your system is Linux or Mac)

1. Two database tables will be created automatically with Spring JPA (change the schema from demo to your schema if you like)

   This is just a reference, you do not have to create those tables manually, make sure the database connection string is correct, plus change the user name / password accordingly.
   
   CREATE TABLE `enrollee` (
   `id` int NOT NULL AUTO_INCREMENT,
   `activationstatus` tinyint(1) NOT NULL,
   `dependentgroupid` int NOT NULL,
   `dob` date NOT NULL,
   `name` varchar(255) NOT NULL,
   `phonenum` varchar(255) DEFAULT NULL,
   PRIMARY KEY (`id`)
   ) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
   
   Note: activationstatus field should be boolean, internally, it is equal to tinyint(1)

   CREATE TABLE `dependent` (
   `id` int NOT NULL AUTO_INCREMENT,
   `dob` date NOT NULL,
   `groupid` int NOT NULL,
   `name` varchar(255) NOT NULL,
   PRIMARY KEY (`id`)
   ) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
   
   Note: just pay attention to two tables, the dependentgroupid / groupid was designed to keep the relationship
between enrollee and dependent, each enrollee will have zero or many dependent, and use one group id to group 
   the dependents together, which means the group of dependents for one enrollee will share the same group id.
   
2. git clone this project, or download zip and unzip it to your local machine (WIndows 10 without modificaition, 
   modify a little bit for Mac or Linux)
   
The code is pretty much self-explained, and the package structure is easy to follow.

3. run the project, choose the method which is more comfortable for you.

method 1:
if load the project in IDE, go to src/main/java/com/nyl, run "Restapiv1Application" as java application 
   this works for most of the IDE (such as Eclipse, Intellij)

method 2: 
go into the project folder, from command line
  > mvnw spring-boot:run

load actuator using http://localhost:8080/actuator, check its metrics (such as info, health, etc)

note: in case something is not work out of box, check carefully about the dependency, usually it is caused by missing
one or two dependencies, or version not match, another common issue is the configuration, minor issue of the mismatch 
can trigger the failure.

4. test the endpoints

check out the Enrollee-Dependent Demo.postman_collection (under root directory of project), upload it 
to Postman, it is the prebuilt collection for endpoints testing
   
The test was broken into three groups, one group is CRUD for Enrollee Service, one group is CRUD for Dependent Service,
the last one is the most important one, it is the test for cross service rest call, such as pull out list of dependents
for one enrollee (only if this enrollee is active, or it will failed), add list of dependents to one enrollee,
remove all the dependents for one enrollee.

5. for more advanced testing, it can be built as docker image, and deploy to docker container for testing.

There are two different approach you can choose to build the docker image for Spring boot project

method 1:
check out the Dockerfile (under project root directory), from terminal, go to project root directory, 
run this from command line to build the docker image. 
(I assume all the build environment was correctly set up, otherwise, need to set up environment first)

  > mvn clean package
  > docker build -t restapidemo:v1 .

note: pay extra attention to the last . for docker command line, this . is NOT an option, is a MUST.

method 2:
customize it in pom.xml, remember this will build image for dockerhub (and you do not have to), without image name,
you still able to build the docker image

<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
    <image>
         <name>docker.io/YOURDOCKERHUBNAME/${project.artifactId}:v1</name>
    </image>
    </configuration>
    <configuration>
		<layers>
		   <enabled>true</enabled>
		</layers>
    </configuration>
</plugin>

from command line, run this will produce some output at terminal, and build the docker image. 
   > mvn spring-boot:build-image
   > docker images

docker images will list all the docker images, you should able to find your new built image.

To test the docker image, assume you have docker container, and ready for deployment, from command line

  > docker run -p 8090:8090 restapidemo

check the REST endpoint in the browser http://localhost:8090

note: if we already run this project from IDE, the default port 8080 should be occupied, that is why we start at port 8090,
in case we have other application run on port 8090, it will failed with the error message, just make sure to find the available port.

After finish the testing on docker container, it is suggested to remove it from container

   > docker ps
   > docker rm -f <container-id>
   
Due to time constraint, I did not test it on AWS, but application can be easily compiled and running on AWS Elasticb Beanstalk.

And more than that, we can creat yaml file for Kurbenetes, but it is doable if we have the correct environment set up,
or if we have chance to deploy to AKS (AWS Kurbenetes Service) or Azure Kurbenetes Service, it can be tested.

This is a barebone version of RestAPI implementation, in next github project, I will demonstrate a more complex 
full scale microservice solution, it will break down the Enrollee service and Dependent service
to be two services, and add more microservices components, such as config server, service discovery server,
API gateway, distribute tracing, centralized logging (Zipkin server), etc. The purpose for that project is to 
demonstrate how to use Spring boot to build a full scale microservice solution. 

Note:

This project only choose the easy to implement resttemplate for cross service call, and it can be implemented using 
webclient, which is reactive approach, and it will be thread-safe, it will fit better for asynchronous, non-blocking, 
using Flux/Mono, and RestTemplate is fit better for synchronous and blocking rest call.

Future enhancement:

Due to time constraint, and I spend some extra time to create another more complex microservice solution, I did miss some of the important features for this project, it will be added in the future, includes:

1. Swagger2 documentation 
2. JUnit test 
3. Mockito test
4. More error handling (I have very basic error handling in current code, but need to be extended)
5. Controller Advisor (it will be nice to have)

Again, this is a barebone RestAPI project, just for demonstration purpose.
