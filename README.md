
RevConnect - Console Based Social Media Application 
📌 Project Overview

RevConnect is a console-based social networking application developed using Java 7, JDBC, and Oracle 10g.
It simulates core features of a modern social media platform such as:

User registration & login

Profile management

Posting content

Likes, comments, shares

Connection (network) management

Personalized feed & discovery

Notification system

Logging using Log4j

The application follows a layered architecture with proper separation of concerns.

Technologies Used

| Technology   | Version             |
| ------------ | ------------------- |
| Java         | Java SE 7           |
| IDE          | Eclipse Indigo      |
| Database     | Oracle 10g          |
| JDBC         | Oracle JDBC Driver  |
| Logging      | Log4j 1.2.17        |
| Testing      | JUnit 4             |
| Architecture | DAO + Service + CLI |


Architecture:
CLI -> Service -> DAO -> Oracle 


How to Run the Project

Open Eclipse Indigo

Import project as Existing Java Project

Add required JARs:

ojdbc14.jar

log4j-1.2.17.jar

junit-4.x.jar

Ensure:

Oracle DB is running

log4j.properties is in classpath

Run:

com.revconnect.cli.RevConnectApp