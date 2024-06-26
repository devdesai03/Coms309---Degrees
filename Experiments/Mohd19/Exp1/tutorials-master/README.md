# tutorials

ALL tutorials for cs309 will be in this repo.
Each topic will be on a different branch. Example: Mockito, WebSockets etc.
Each platform will be a FOLDER in this repo.
GIT wiki will be used for documentation.
Different levels will be in sub branches such as Mockito-1, Mockito-2 etc.


### Accessing tutorials 
You can access each tutorial by switching to its respective branch. To do so, you can use your terminal/Git bash. 

1. First change the directory to the root of this repository  

```ssh
cd </path/to/tutorials/directory>  
```

2. Switch the desired branch. For example, to checkout to "springboot_unit1_1_helloworld", use the following command. 

```ssh
git checkout springboot_unit1_1_helloworld
```

This should switch the code to the "springboot_unit1_1_helloworld" tutorial. For complete list of topics, check the section "Topics" below.





## Topics 

**NAMING FORMAT:** 
*  \<area/topic\>\_unit<#>\_\<\#\>\_\<name\>    `This should be followed by a short description`
*  Example: springboot_unit1_2_helloworld   `simple helloworld example.`

 <br/>
 

### Springboot  
*UNIT-1*
*  [springboot_unit1_1_helloworld](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit1_1_helloworld) `Getting started with SpringBoot`
*  [springboot_unit1_1_basicrequest](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit1_1_basicrequest) `Example to create simple GET, POST, PUT and DELETE APIs`
*  [springboot_unit1_2_hellopeople](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit1_2_hellopeople) `Sending and Receiving data using GET, POST, PUT and DELETE APIs`
*  [springboot_unit1_3_petclinic_example](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit1_3_petclinic_example) 


*UNIT-2*
*  [springboot_unit2_1_onetoone  (uses H2 database)](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit2_1_onetoone) `Example one-to-one relation, H2 database usage and CRUDL using SpringBoot`
*  [springboot_unit2_1_onetoone_mysql](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit2_1_onetoone_mysql) `MySQL database connectivity using spring boot + one-to-one relation + CRUDL using SpringBoot`
*  [springboot_unit2_2_onetomany](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit2_2_onetomany) `Example one-to-many relations + querying using repository + CRUDL using Spring Boot`
*  [springboot_unit2_3_filerequests](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit2_3_filerequests) `Handling File requests/Multi-Part requests using SpringBoot`
*  [springboot_unit2_4_swagger_ui](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit2_4_swagger_ui) `API documentation using swagger-ui for spring boot applications`
*  [RoundTrip_(with_retrofit2_and_h2)](https://git.las.iastate.edu/cs309/tutorials/-/tree/RoundTrip_(with_retrofit2_and_h2)) `Adds new Trivia questions and Answers by path and body and displays all Trivias. Video link: https://youtu.be/CEiUCDWygD0. To see how to plug into this project into MySQL watch this: https://youtu.be/hyFo1_jryL0`

*UNIT-3*
*  [springboot_unit3_1_manytomany](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit3_1_manytomany) `Example many-to-many relations`
*  [springboot_unit3_2_complex_relations_example](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit3_2_complex_relations_example) `Example many-to-many relations + complex queries using repository + CRUDL using spring boot`
*  [springboot_unit3_3_manytomany_selfrelations](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit3_3_manytomany_selfrelations) `Self Relations among entities in Spring Boot`
*  [testing](https://git.las.iastate.edu/cs309/tutorials/-/tree/testing) `testing examples1`
*  [springboot_unit3_4_mockito_testing](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit3_4_mockito_testing)


*UNIT-4*
* [unit4_1_websockets](https://git.las.iastate.edu/cs309/tutorials/-/tree/unit4_1_websockets)

### Android 
*UNIT-1*
* [android_unit1_1_two_screen_counter](https://git.las.iastate.edu/cs309/tutorials/-/tree/android_unit1_1_two_screen_counter) `simple two screen application for Android kickstart`

*UNIT-2*
* [android_unit2_1_volley](https://git.las.iastate.edu/cs309/tutorials/tree/android_unit2_1_volley) `example for: image / string / json  request calls`
* [android_unit2_2_volley](https://git.las.iastate.edu/cs309/tutorials/tree/android_unit2_2_volley) `more examples for image / json request calls `
* [android_unit2_3_designpattern](https://git.las.iastate.edu/cs309/tutorials/tree/android_unit2_3_designpattern) `MVC, Interfaces`
* [android_unit3_2_service_and_singleton](https://git.las.iastate.edu/cs309/tutorials/tree/android_unit3_2_service_and_singleton) `Example to Create a singleton class for request queue, and a listner for responses`
*  [RoundTrip_(with_retrofit2_and_h2)](https://git.las.iastate.edu/cs309/tutorials/-/tree/RoundTrip_(with_retrofit2_and_h2)) `Uses Retrofit2 to load all the Trivias and posts new trivias by json body and path. Video link: https://youtu.be/eUPFmgp6FKk`


*UNIT-3*
* [testing](https://git.las.iastate.edu/cs309/tutorials/tree/testing) `testing examples`
* [android_unit3_1_mockito_testing](https://git.las.iastate.edu/cs309/tutorials/tree/android_unit3_1_mockito_testing) `Example to test android code with Mockito. UPDATE: Updated java to 11, gradle to 7.0.3, android sdk to 30 and fixed mockito dependencies - should work now`
* [android_unit2_4_Phase 1,2,3,4](https://git.las.iastate.edu/cs309/tutorials/tree/android_unit2_4_phases) `Example to send requests using volley(to simple URL, JSON response, and Image)`


*UNIT-4*
* [unit4_1_websockets](https://git.las.iastate.edu/cs309/tutorials/tree/unit4_1_websockets)

*MISC*
* [android_tutorials_master](https://git.las.iastate.edu/cs309/tutorials/tree/android_tutorials_master) `Docs for Volley, CI/CD, Mockito and websocket`

### Testing: Mockito backend and Frontend

*  [testing](https://git.las.iastate.edu/cs309/tutorials/tree/testing) `Examples to test the backend and the front end using mockito`
*  [springboot_unit3_4_mockito_testing](https://git.las.iastate.edu/cs309/tutorials/tree/springboot_unit3_4_mockito_testing) `Example code to test a springboot application with mockito`
*  [android_unit3_2_service_and_singleton](https://git.las.iastate.edu/cs309/tutorials/tree/android_unit3_2_service_and_singleton) `Example code to test an android application with mockito. UPDATE: updated java to 11, gradle to 7.0.3, junit versions changed, tests fixed to work now with the latest version of Android Studio`

### Websockets 
*  [unit4_1_websockets](https://git.las.iastate.edu/cs309/tutorials/tree/unit4_1_websockets)


### CI/CD Pipeline 
*  [cicd_example](https://git.las.iastate.edu/cs309/tutorials/-/tree/cicd_example)

**DESIGN**
*  godclass
*  designPatterns

