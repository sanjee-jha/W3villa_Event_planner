# Event Planner Web Application

A full-featured role-based event management system built with **Spring Boot**, **Thymeleaf**, and **MySQL**. This application allows admins to manage events and users to RSVP as **Going**, **Maybe**, or **Decline**.

---

##  Features

###  User Role
- Register and log in securely
- View all upcoming events
- RSVP to events (Going, Maybe, Decline)
- Modify RSVP responses before the event date
- View all RSVPs with current status

###  Admin Role
- Create, update, and delete events
- View RSVP summaries for each event

###  General
- Role-based access control using Spring Security
- Responsive and mobile-friendly Thymeleaf UI
- Input validation (frontend + backend)
- Prevent duplicate RSVPs

---

##  Tech Stack

 Backend    -->  Spring Boot 3.5.0                      
 Frontend   -->  Thymeleaf + HTML/CSS                  
 Database   -->  MySQL                                
 ORM        -->  Spring Data JPA                      
 Security   -->  Spring Security                      
 Password   -->  BCrypt                               
 Build Tool -->  Maven                                
 Utilities  -->  Lombok                               
 Testing    -->  JUnit, Spring Boot Test              

> **Why Thymeleaf?**  
> Seamless integration with Spring MVC and Security, excellent for server-side rendering of secure content.

---

##  Setup Instructions

###  Prerequisites:
- Java 17+
- MySQL installed and running
- Maven installed

###  Steps to Run:

1. **Clone the repository:**
   git clone https://github.com/sanjee-jha/W3villa_Event_planner.git
   cd W3villa_Event_planner
   
# Create a MySQL database:
   CREATE DATABASE event_planner;
   
# Update application.properties:
   spring.application.name=eventplanner
   spring.datasource.url=jdbc:mysql://localhost:3306/event_planner
   spring.datasource.username=root
   spring.datasource.password=root
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.sql.init.mode=always
   spring.thymeleaf.cache=false
   server.port=8080
   
# Build & Run the Application:
   mvn spring-boot:run

# open your browser and navigate to:
   http://localhost:8080

#  ER Diagram:

User
- id (PK)
- username (unique)
- password
- role (USER / ADMIN)

Event
- id (PK)
- title
- description
- location
- start_time (datetime)
- end_time (datetime)

RSVP
- id (PK)
- status (GOING / MAYBE / DECLINE)
- user_id (FK → User.id)
- event_id (FK → Event.id)


# Tests:
  Basic unit tests with Spring Boot and JUnit
    Tests include:
    User registration and login
    RSVP submission logic
    Event creation and update by admin

# Video:
    https://drive.google.com/file/d/1C2-N2R006DRrwmgYEjzomgY1wdMIEo_b/view?usp=sharing
