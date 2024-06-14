# Degrees - APIs
[TOC]

## Swagger UI APIs
[Click here](http://localhost:8080/swagger-ui/)

## Logging in
### login
```
POST /login
{
    "email": "dev",
    "password": "someInsecurePassword"
}

Respond:
200 OK
{
    "didSucceed": true or false
}

POST /login/verify
{
    "verificationCode": "0123",
    "email": ""
}

Respond:
200 OK
{
    "accessToken": "someDumbAccessToken" or null,
    "userEmail": ""
}
```

## Advisors
### Create advisors
```
POST /advisor
{
  "advisorName": "advvisor", 
  "advisorPassword":"1234567",
  "advisorAddress": "Ames,IA,50014",   
  "advisorEmail": "mohd19@iatstate.edu"
}
```

### Get advisor
```http
GET /advisor/{advisorId} HTTP/1.1
```
Gets information about an advisor.
* `advisorid` (string, required): The advisor's ID.

#### Response
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "advisorId": advisorId,
  "isuRegistration": {
    "netId": netId,
    "universityId": universityId
  }
}
```

## Users
### Create users
```
POST /users
{
  "userName": "Dev D", 
  "userPassword":"1234567",
  "userAddress": "Ames,IA,50014",   
  "userEmail": "mohd19@iatstate.edu"
}
```

### Get user
```http
GET /users/{userId} HTTP/1.1
```
Gets information about the currently logged-in user.
* `userId` (long, required): The user's ID.

#### Response
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "userId": userId,
  "isuRegistration": {
    "netId": netId,
    "universityId": universityId
  }
}
```
* `userId` (integer): the user's primary key in the database
* `isuRegistration` (object or null): Information about the
  user's registration to ISU. If `null`, 
  * `netId` (string): the user's Net-ID
  * `universityId` (integer): the user's university ID

## User interface settings

### Get user interface settings
```http
GET /settings/{userId}/userInterface HTTP/1.1
```
Asks the server for a user's UI settings.
* `userId` (integer, required): the ID of the user, that is, the primary key of the user in the database. This is `1234` for testing purposes.

#### Response
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "backgroundColor": backgroundColor,
  "nightMode": nightMode
}
```
* `backgroundColor` (string or null): The preferred background color of the user. This is in hex notation, like `#FFFFFF` for white and `#FF0000` for red. If `null`, then the user doesn't have a preferred background color.
* `nightMode` (string): The night mode setting that the user prefers. This can be one of three values: `followSystem` (the dark/light theme matches the system), `on` (the user wants dark mode), and `off` (the user wants light mode).

### Update user interface settings
```http
PUT /settings/{userId}/userInterface HTTP/1.1
Content-Type: application/json

{
  "backgroundColor": backgroundColor,
  "nightMode": nightMode
}
```
Updates the user interface settings for a user.
* `userId` (integer, required): the ID of the user, that is, the primary key of the user in the database. This is `1234` for testing purposes.
* `backgroundColor` (string or null, required): The preferred background color of the user. This is in hex notation, like `#FFFFFF` for white and `#FF0000` for red. If `null`, then the user doesn't have a preferred background color.
* `nightMode` (string, required): The night mode setting that the user prefers. This can be one of three values: `followSystem` (the dark/light theme matches the system), `on` (the user wants dark mode), and `off` (the user wants light mode).

#### Response
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "backgroundColor": backgroundColor,
  "nightMode": nightMode
}
```
* `backgroundColor` (string or null): The same value that the request has.
* `nightMode` (string): The same value that the request has.

## Departments
### Get department list
```http
GET /departments/ HTTP/1.1
Content-Type: application/json
```
Get a list of all departments.

#### Response
```http
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "departmentId": departmentId,
    "departmentName": departmentName,
    "departmentCode": departmentCode,
    "departmentAddress": departmentAddress
  }
]
```
* An array of Department objects. Each object has the following fields:
  * `departmentId` (long): The department's ID.
  * `departmentName` (string): The department's full name, such as "Computer Science".
  * `departmentCode` (string): The department's abbreviation, such as "COM S".
  * `departmentAddress` (string): The postal address of the department.

## Courses
### Get courses list
```http
GET /courses/?departmentId={departmentId}&departmentCode={departmentCode}&courseNumber={courseNumber} HTTP/1.1
Content-Type: application/json
```

Get a list of courses. For example, `/courses/` would return an array of all courses.

You can optionally get the courses in a specific department by specifying the department's ID *or* department code in a path variable. The ID and department code both individually uniquely identify a department, so you only need to specify one. For example, `/courses/?departmentCode=COM+S` would return an array of courses in the computer science department.

Additionally, you can filter the courses by their course number. For example, `/courses/?departmentCode=COM+S&courseNumber=309` would return an array of length zero or one, with the course COM S 309.
* `departmentId` (long, optional): The ID of the department to filter the courses by, such as "12".
* `departmentCode` (string, optional): The code of the department to filter the courses by, such as "COM S".
* `courseNumber` (string, optional): The course number to filter by.

#### Response
```http
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "courseId": courseId,
    "courseName": courseName,
    "courseDepartment": {
      "departmentId": departmentId,
      "departmentCode": departmentCode
    },
    "courseNumber": courseNumber,
    "courseDescription": courseDescription,
    "courseCreditHours": courseCreditHours
  }
]
```
* An array of Course objects. Each object has the following fields:
  * `courseId` (long): A unique identifier for a course.
  * `courseName` (string): The full name of the course, such as "Software Development Practices".
  * `courseDepartment` (object): A department object.
    * `departmentId` (long): A number uniquely identifying the course's department, such as "12".
    * `departmentCode` (string): The course's department's abbreviation, such as "COM S".
  * `courseNumber` (string): The course's number, such as "309". This is *not* necessarily a valid integer. For example, WISE 201x has the course number "201x".
  * `courseCreditHours` (string): The course's credit hours, such as 3.0. This should ideally be parsed into a `BigDecimal` object instead of a `double` to prevent floating-point errors (such as `0.1 + 0.2 == 3.0004`). It is guaranteed to be a valid decimal number.


### Create courses
```HTTP
/courses/course

{
    "courseName": courseName,
    "courseDepartment": {
      "departmentId": departmentId,
      "departmentCode": departmentCode
    },
    "courseNumber": courseNumber,
    "courseDescription": courseDescription,
    "courseCreditHours": courseCreditHours
  }
```
#### Response
```HTTP
HTTP/1.1 200 OK
```


## Advisor chat

### Websocket /advisor-chat/{username}
``` 
Frontend people in the {username} field please send me the actual username ex: /advisor-chat/Blessing
```
ws://{our server url without Http}/advisor-chat/{username}

#### Response
```
String {username} + " has joined the chat"
```
### For Breakout room
``` 
String /join-room {roomName}
```
#### Response
``` 
String You joined room: + {roomName} 
you will use that respose to create a breakout room with the room name!!
```
### GET /availableBreakoutRooms
```
HTTP request
```
GET /availableBreakoutRooms
* Use this url to get the list of all active rooms currently.
#### Response
```
OK 200
```
### GET /onlineUsers
``` HTTP request
GET /onlineUsers
```
* Use this url to get the list of all active users.
#### Response
``` HTTP
OK 200
```
### GET /chatHistory/{userName}
``` HTTP
GET /chatHistory/{userName}
```

* Use this url to get the list of chat History for a particular user.
#### Response
``` HTTP
OK 200
```

## Course sections
### Create course Section
```http
POST /course-sections HTTP/1.1

Content-Type: application/json

{
  "course": course,
  "semesterAndYear": semesterAndYear,
  "sectionIdentifier": sectionIdentifier,
  "location": location,
  "startDate": startDate,
  "endDate": endDate,
  "daysOfWeek": daysOfWeek,
  "startTime": startTime,
  "endTime": endTime

}
```
* `course` (Course): The course that this is a section of.
* `semesterAndYear` (string): The semester and year that this section is in, such as "Fall 2023".
* `sectionIdentifier` (string): The section identifier, such as the "1" in section 1. This can also be a letter, like "A" for a section A.
* `location` (string): The location of the course, like "DESIGN 0101".
* `startDate` (string): The date that the course section starts, like the beginning of the semester.
* `endDate` (string): The date that the course section ends, like the end of the semester.
* `daysOfWeek` (string): The days of the week that the course takes place.
* `startTime` (string): The time that the class starts.
* `endtTime` (string): The time that the class ends.

#### Response
```http
HTTP/1.1 200 Created
```
### Get Course Section
```http
GET /course-sections/{id} HTTP/1.1
```
#### Response
```http
HTTP/1.1 200 OK
[
  {
  "id":id,
    "course": course,
    "semesterAndYear": semesterAndYear,
    "sectionIdentifier": sectionIdentifier,
    "location": location,
    "startDate": startDate,
    "endDate": endDate,
    "daysOfWeek": daysOfWeek,
    "startTime": startTime,
    "endTime": endTime

  }
]
```

### Delete Course Section
```http
DELETE /course-sections/{id} HTTP/1.1
```
#### Response
```http
200 OK
```

### Get course section by instroctor how is teaching that section
```HTTP
GET /course-sections/course-sections-by-instructor/{instructorId}
```
#### Response
```HTTP
200 OK

[
    {
        "id": 123456,
        "course": {
            "courseId": 13,
            "courseName": "Introduction to Computer Programming - Python",
            "courseDepartment": {
                "departmentId": 10,
                "departmentName": "Computer Science",
                "departmentAddress": null,
                "departmentCode": "COM S"
            },
            "courseNumber": "127",
            "courseDescription": "Computer programming",
            "courseCreditHours": "0.00",
            "id": 13
        },
        "instructor": {
            "universityId": 51,
            "isuRegistration": {
                "universityId": 51,
                "user": {
                    "userId": 51,
                    "userName": null,
                    "userAddress": null,
                    "userEmail": "Instructor2",
                    "phoneNumber": null
                },
                "netId": "Instructor2",
                "givenName": "Instructor2",
                "middleName": "Instructor2",
                "surname": "Instructor2",
                "student": null,
                "advisor": null,
                "departmentHead": null,
                "fullName": "Instructor2, Instructor2 Instructor2",
                "fullNameFriendly": "Instructor2 Instructor2 Instructor2"
            }
        },
        "semesterAndYear": "Spring 2024",
        "sectionIdentifier": "1",
        "location": "Atanasoff Hall",
        "startDate": "2024-01-01",
        "endDate": "2024-05-20",
        "daysOfWeek": "MWF",
        "startTime": "11:00:00",
        "endTime": "12:00:00"
    },
    {
        "id": 44556677,
        "course": {
            "courseId": 19,
            "courseName": "INTRODUCTION TO COMPUTER ARCHITECTURE AND MACHINE-LEVEL PROGRAMMING\n",
            "courseDepartment": {
                "departmentId": 10,
                "departmentName": "Computer Science",
                "departmentAddress": null,
                "departmentCode": "COM S"
            },
            "courseNumber": "321",
            "courseDescription": "Comupter Science",
            "courseCreditHours": "4.00",
            "id": 19
        },
        "instructor": {
            "universityId": 51,
            "isuRegistration": {
                "universityId": 51,
                "user": {
                    "userId": 51,
                    "userName": null,
                    "userAddress": null,
                    "userEmail": "Instructor2",
                    "phoneNumber": null
                },
                "netId": "Instructor2",
                "givenName": "Instructor2",
                "middleName": "Instructor2",
                "surname": "Instructor2",
                "student": null,
                "advisor": null,
                "departmentHead": null,
                "fullName": "Instructor2, Instructor2 Instructor2",
                "fullNameFriendly": "Instructor2 Instructor2 Instructor2"
            }
        },
        "semesterAndYear": "Spring 2024",
        "sectionIdentifier": "3",
        "location": "Person Hall",
        "startDate": "2024-01-01",
        "endDate": "2024-05-20",
        "daysOfWeek": "TRTH",
        "startTime": "10:00:00",
        "endTime": "11:00:00"
    }
]
```

## Prerequisites

### Create Prerequisite
```http
POST /prerequisites/prerequisites HTTP/1.1
Content-Type: application/json
Body:
{
 "preCourseId": preCourseId,
 "postCourseId": postCourseId,
 "preCourse": preCourse,
 "postCourse": postCourse,
 "description": description,
 "preCourseId": preCourseId,
 "minimumGrade": preCourseId,
}
```
#### Response (Success)
```http
HTTP/1.1 201 Created
```

#### Error Response
```http
HTTP/1.1 400 Bad Request
Content-Type: text/plain

Cyclic dependency detected
```

### Get Prerequisites for a Course
```http
GET /prerequisites/prerequisites/{courseId} HTTP/1.1
Content-Type: application/json
```
#### Response
```http
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
  "preCourseId": preCourseId,
 "postCourseId": postCourseId,
 "preCourse": preCourse,
 "postCourse": postCourse,
 "description": description,
 "preCourseId": preCourseId,
 "minimumGrade": preCourseId,
  }
]
```
## Course registration

### Get student's registered course sections
``` HTTP
/students/{studentId}/registered-course-sections
```
* Request Parameters:
  * studentId (long): University ID of the student.
  * sectionId (long): ID of the course section.

#### Response
```http
200 OK
```

### Register a course section
```HTTP
POST/register-course-section
Content-Type: application/json
{
  "student": {
    "universityId": studentUniversityId
  },
  "coursesection": {
    "id": courseSectionId
  },
  "grade": courseGrade,
  "creditHours": courseCreditHours
}
```

### Drop a course section
```http
DELETE /drop-registered-course-section/{universityId}/{courseId} HTTP/1.1
Content-Type: application/json
```
* Request Parameters:
  * studentId (long): University ID of the student.
  * sectionId (long): ID of the course section.

#### Response (Success)
```http
HTTP/1.1 204 No Content
```

#### Response (Not found)
```http
HTTP/1.1 404 Not Found
```

### GET course registered section by instructor who is teaching that section
```http
GET /course-registration-instructor/{instructorId}
```
#### Response
```HTTP
HTTP/1.1 200
[
    {
        "id": {
            "universityId": 11,
            "sectionId": 112233
        },
        "student": {
            "universityId": 11,
            "isuRegistration": {
                "universityId": 11,
                "user": null,
                "netId": "test",
                "givenName": null,
                "middleName": null,
                "surname": null,
                "instructor": null,
                "advisor": null,
                "departmentHead": null,
                "fullName": null,
                "fullNameFriendly": null
            },
            "studentAdvisor": {
                "advisorId": 60,
                "isuRegistration": {
                    "universityId": 60,
                    "user": {
                        "userId": 60,
                        "userName": null,
                        "userAddress": "2821",
                        "userEmail": "advisor@iastate.edu",
                        "phoneNumber": "515209040"
                    },
                    "netId": "advisor",
                    "givenName": "Advisor",
                    "middleName": "e",
                    "surname": "Advisor",
                    "student": null,
                    "instructor": null,
                    "departmentHead": null,
                    "fullName": "Advisor, Advisor e",
                    "fullNameFriendly": "Advisor e Advisor"
                },
                "advisorDepartment": null
            },
            "majorRegistrations": [],
            "minorRegistrations": [],
            "majors": [],
            "major": null,
            "minors": []
        },
        "section": {
            "id": 112233,
            "course": {
                "courseId": 21,
                "courseName": "SOFTWARE DEVELOPMENT PRACTICES",
                "courseDepartment": {
                    "departmentId": 10,
                    "departmentName": "Computer Science",
                    "departmentAddress": null,
                    "departmentCode": "COM S"
                },
                "courseNumber": "309",
                "courseDescription": "Comupter Science",
                "courseCreditHours": "4.00",
                "id": 21
            },
            "instructor": {
                "universityId": 50,
                "isuRegistration": {
                    "universityId": 50,
                    "user": {
                        "userId": 50,
                        "userName": null,
                        "userAddress": null,
                        "userEmail": "Instructor1",
                        "phoneNumber": null
                    },
                    "netId": "Instructor1",
                    "givenName": "instructor1",
                    "middleName": "instructor1",
                    "surname": "Instructor1",
                    "student": null,
                    "advisor": null,
                    "departmentHead": null,
                    "fullName": "Instructor1, instructor1 instructor1",
                    "fullNameFriendly": "instructor1 instructor1 Instructor1"
                }
            },
            "semesterAndYear": "Spring 2024",
            "sectionIdentifier": "3",
            "location": "Person Hall",
            "startDate": "2024-01-01",
            "endDate": "2024-05-20",
            "daysOfWeek": "TRTH",
            "startTime": "10:00:00",
            "endTime": "11:00:00"
        },
        "grade": null,
        "creditHours": null
    }
]
```
### Update the student grade in course Registration
```HTTP
POST /updateGrade/{universityId}/{sectionId}?newGrade=${value}
 where newGrade is a RequestParameter.
 ```
 #### Response
 ``` HTTP
 200 ok No Content
 ```


## Flowchart
### Get degrees list
```http
GET /degrees/ HTTP/1.1
```
Get a list of possible degrees.
#### Response
```http
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "id": id,
    "name": name,
    "suffix": suffix,
    "department": {
      "departmentId": departmentId,
      "departmentName": departmentName,
      "departmentCode": departmentCode,
      "departmentAddress": departmentAddress
    }
  }
]
```
* An array of Degree objects. Each Degree object has the following fields:
  * `id` (long): The degree's unique ID.
  * `name` (string): The degree's name, like "Computer Science".
  * `suffix` (string): The degree's suffix, like "BS" or "MA".
  * `department` (object): An object with the following fields:
    * `departmentId` (long): The department ID.
    * `departmentName` (string): The department's name.
    * `departmentCode` (string): The department code, like "COM S".
    * `departmentAddress` (string): The department's address.

### Get flowchart for specific degree
```http
GET /degrees/{degreeId}/flowchart?planningSemester={planningSemester} HTTP/1.1
```
Get the flowchart for a specific degree.
* `degreeId` (long, required): The ID for the specific degree.
* `planningSemester` (int, optional): The semester corresponding to the plus button that the user clicked from the four-year planner screen. If the user clicked the plus button at the top, this query parameter is not included.

#### Response
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "degree": {
    "id": id,
    "name": name,
    "suffix": suffix,
    "department": {
      "departmentCode": departmentCode
    }
  },
  "semesters": [
    {
      "semesterNumber": semesterNumber,
      "requirementGroups": [
        {
          "name": name,
          "subtitle": subtitle,
          "totalCreditHours": totalCreditHours,
          "state": state,
          "fulfillments": [
            {
              "minimumGrade": minimumGrade,
              "semesterTaking": semesterTaking,
              "minimumSemester": minimumSemester,
              "course": {
                "courseId": courseId,
                "courseName": courseName,
                "courseDepartment": {
                  "departmentId": departmentId,
                  "departmentCode": departmentCode
                },
                "courseNumber": courseNumber,
                "courseDescription": courseDescription,
                "courseCreditHours": courseCreditHours
              }
            }
          ]
        }
      ]
    }
  ]
}
```
* `degree` (object): The Degree object representing the degree that this flowchart is for. Has the following subfields:
  * `id` (long): The degree's unique ID.
  * `name` (string): The degree's name, like "Computer Science".
  * `suffix` (string): The degree's suffix, like "BS" or "MA".
  * `department` (object): A Department object, with the following field:
    * `departmentCode` (string): The department code, like "COM S".
* `semesters` (array): A sorted array of semesters. Each semester corresponds to a "row" in the flowchart.
  * `semesterNumber` (int): The semester number, like 1, 2, or 3.
  * `requirements` (array): An array of requirement objects. A requirement object corresponds to one of the "boxes" in the flowchart. Each requirement object has the following fields:
    * `name` (string): The big name that
      appears at the top of the "box", such as
      "COM S 309" or "Arts & Humanities".
    * `subtitle` (string): The subtitle of the box, such as "Object-oriented programming - Java" or "(tap to view options)".
    * `totalCreditHours`: (string or null): A string representing the credit hours or range of credit hours. If `null`, then the number of credit hours is not listed and "(required)" should be displayed instead.
    * `state` (string): The box's "state" as described in the screen sketches document. This can be one of the following values: `available`, `selected`, `unavailable`, `unavailableSelected`.
    * `fulfillments` (array): An array representing the set of courses that the user can take to fulfill this requirement. The user only has to take one of them. It consists of one or more objects with the following fields:
      * `minimumGrade` (BigDecimal): The minimum grade in the course required to meet the requirement.
      * `semesterTaking` (int or null): The semester that the user is planning to take this course. If `null`, then the course is not in the user's academic plan. At least one course must be in the academic plan for the requirement to be fulfilled.
      * `minimumSemester` (int or null): The first semester that the user has all the prerequisites for this course fulfilled. If `null`, then the user can take this course as early as they want without issues.
      * `course`: Information about the course itself. This is a Course object, with the following fields:
        * `courseId` (long): A unique identifier for a course.
        * `courseName` (string): The full name of the course, such as "Software Development Practices".
        * `courseDepartment` (object): A department object.
          * `departmentId` (long): A number uniquely identifying the course's department, such as "12".
          * `departmentCode` (string): The course's department's abbreviation, such as "COM S".
        * `courseNumber` (string): The course's number, such as "309". This is *not* necessarily a valid integer. For example, WISE 201x has the course number "201x".
        * `courseCreditHours` (string): The course's credit hours, such as 3.0. It is guaranteed to be a valid decimal number.

## Course rating
### Get course ratings list for a specific course
```http
GET /courseratings/{studentId}/{courseId}/{instructorId} HTTP/1.1
```
### Response
```http
200 OK or 404 Not Found if failed!
```
### Create course rating
```http
POST /courseratings HTTP/1.1
Content-Type: application/json
{
  "student": { "universityId": 1 },
  "course": { "courseId": 2 },
  "instructor": { "instructorId": 3 },
  "dateWritten": "2023-11-01T14:30:00",
  "rating": 5,
  "reviewText": "This course is fantastic!"
}
```
* `student`: The student who rated the course.
* `course`: The course that was rated.
* `instructor`: The instructor of the course.
* `dateWritten`: The date when the review was written (format: "yyyy-MM-dd'T'HH:mm:ss").
* `rating`: The rating given to the course.
* `reviewText` (optional): The text review of the course.

### Response
```http
201 Created
```
## Academic plan
### List courses in user's academic plan(AcademicPlanController)
```http
GET /academic-planner/{studentId}
```
### Respond

```json
{
    "course": {
        "courseId": 1,
        "courseName": "Course A",
        "semesterNumber": 1
    },
    "missingPrerequisites": [
        {
            "preCourseId": 101,
            "preCourseName": "Prerequisite Course 1"
        },
        {
            "preCourseId": 102,
            "preCourseName": "Prerequisite Course 2"
        }
    ]
}

```

### Get a specific course in academic plan
```http
GET /hypothetical-course-registrations/{studentId}/{courseId}
```
### Response
```jason
{
    "courseId": 3,
    "courseName": "Course C",
    "studentId": 12345,
    "semesterNumber": 3
}
```
### Add a new course to academic plan
```http
POST /hypothetical-course-registrations

{
    "id": {
        "course": {
            "courseId": 1
        },
        "student": {
            "universityId": 12345
        }
    },
    "semesterNumber": 2,
}

```
### Update a course in academic plan
```http
PUT /hypothetical-course-registrations/{studentId}/{courseId}

{
    "id": {
        "course": {
            "courseId": 1
        },
        "student": {
            "universityId": 12345
        }
    },
    "semesterNumber": 2,
}

```
### Delete a course in academic plan
```http
DELETE /{courseId}/{studentId}/{semesterNumber}
```
## AI chat
### Connect to server
### Message from client to server
### Message from server to client

## Get degree audit
```http
GET /degreeAudit/{isuRegistrationId} HTTP/1.1
```

### Response
```http
HTTP/1.1 200 OK
Content-Type: text/plain

Degree audit text
Degree audit text
```

## advisor-meeting-scheduling-api

### POST /{studentId}/schedule-appointment

``` HTTP request
{
   "advisor":{
       "advisorId":60
   },
   "startTime": "2023-11-28T09:00:00",
    "endTime": "2023-11-28T22:00:00",
    "description": "Webex meeting"
}
```
### Response
``` Http
HTTP/1.1 200 OK
Content-Type: text/plain
```
#### Error 
```
HTTP/1.1 400 bad request
Content-Type: text/plain
```
## Api to list all the appointment for advisor by advisorId and startTime and endTime

### GET /appointments/advisor/${advisorId}/timeRange?startTime=${startTime}&endTime=${endTime}
```Http request
where startTime is a quey parameter
and endTime is a query parameter as well
here is an example of how you will do it:

// Construct URL with query parameters
    String advisorId = "60";
    String startTime = "2023-01-01T10:00:00";
    String endTime = "2023-01-01T11:00:00";

    String urlString = "http://your-api-base-url/appointments/advisor/" + advisorId + "/timeRange"
            + "?startTime=" + startTime + "&endTime=" + endTime;

    URL url = new URL(urlString);
and other cool stuff!
```
### Response

```HTTP
HTTP/1.1 200 OK
Content-Type: list of jason objects

[
  {
    "id": 1,
    "student": {
      "universityId": 19,
      "isuRegistration": {
        "universityId": 19,
      "user": {
        "userId": 7,
        "userName": "monim",
        "userAddress": "2420 Lincoln Way",
        "userEmail": "monim@gmail.com",
        "phoneNumber": "5155555555"
  },

"advisor": {
  "advisorId": 60,
  "isuRegistration": {
      "universityId": 60,
    "user": {
        "userId": 60,
        "userName": null,
        "userAddress": "2821",
        "userEmail": "advisor@iastate.edu",
        "phoneNumber": "515209040"
    },
  "advisorDepartment": null
  },
"startTime": "2023-11-28T09:00:00",
"endTime": "2023-11-28T22:00:00",
"description": "Webex meeting",
}
```

### Error

```HTTP
HTTP/1.1 400 Bad
```

## Api to list all the appointment for advisor by advisorId

### Get /appointments/advisor/{advisorId}

```HTTP request
advisorId:60
```
### Response

``` HTTP
HTTP/1.1 200 OK
Content-Type: list of jason objects

[
  {
    "id": 1,
    "student": {
      "universityId": 19,
      "isuRegistration": {
        "universityId": 19,
      "user": {
        "userId": 7,
        "userName": "monim",
        "userAddress": "2420 Lincoln Way",
        "userEmail": "monim@gmail.com",
        "phoneNumber": "5155555555"
  },

"advisor": {
  "advisorId": 60,
  "isuRegistration": {
      "universityId": 60,
    "user": {
        "userId": 60,
        "userName": null,
        "userAddress": "2821",
        "userEmail": "advisor@iastate.edu",
        "phoneNumber": "515209040"
    },
  "advisorDepartment": null
  },
"startTime": "2023-11-28T09:00:00",
"endTime": "2023-11-28T22:00:00",
"description": "Webex meeting",
}
```

### Error

```HTTP
HTTP/1.1 400 Bad
```


## Api to list all the appointment for student by studentId

### Get /appointments/student/{studentId}

```HTTP request
studentId:19
```
### Response

``` HTTP
HTTP/1.1 200 OK
Content-Type: list of jason objects

[
  {
    "id": 1,
    "student": {
      "universityId": 19,
      "isuRegistration": {
        "universityId": 19,
      "user": {
        "userId": 7,
        "userName": "monim",
        "userAddress": "2420 Lincoln Way",
        "userEmail": "monim@gmail.com",
        "phoneNumber": "5155555555"
  },

"advisor": {
  "advisorId": 60,
  "isuRegistration": {
      "universityId": 60,
    "user": {
        "userId": 60,
        "userName": null,
        "userAddress": "2821",
        "userEmail": "advisor@iastate.edu",
        "phoneNumber": "515209040"
    },
  "advisorDepartment": null
  },
"startTime": "2023-11-28T09:00:00",
"endTime": "2023-11-28T22:00:00",
"description": "Webex meeting",
}
```

### Error

```HTTP
HTTP/1.1 400 Bad
```








