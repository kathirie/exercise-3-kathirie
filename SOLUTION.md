SPW4 - Exercise 3
=================

Name: _____________

Effort in hours: __

## 1. Connect Four Web Application and CI/CD Pipeline

### Task 1.a

I added the implementation as well as the tests.

here is the test coverage

![test_coverage](./doc/00.png)


here is the first goal (mvn compile)

![mvn_compile](./doc/01_01.png)


here is the second goal (mvn test)

![mvn_test](./doc/01_02.png)


here is the third goal (mvn package)

![mvn_package](./doc/01_03.png)




### Task 1.b

Most of the programming was done in the lesson. I spent an uncomfortable amount of time on recreating the runners 
and some bug fixing but I managed to make the pipeline do what it was supposed to do.

The pipeline now works as expected

![working_pipeline](./doc/02_01.png)


Deployment can also be stoped

![stop_test](./doc/02_05.png)


Game is reachable

![start_screen](./doc/02_02.png)


Game is also working

![working_game](./doc/02_03.png)


Runners are running (had to make another docker runner, couldn't delete the old one)

![running_runners](./doc/02_04.png)



### Task 1.c

<!--- describe your solution here --->
