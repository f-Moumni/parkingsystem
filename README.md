# Parking System
A command line app for managing the parking system. 
This app uses Java to run and stores the data in Mysql DB.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

- Java 1.8
- Maven 3.8.4
- Mysql 8.0.27

### Installing

A step by step series of examples that tell you how to get a development env running:

1.Install Java:

https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html

2.Install Maven:

https://maven.apache.org/install.html

3.Install MySql:

https://dev.mysql.com/downloads/mysql/

After downloading the mysql 8 installer and installing it, you will be asked to configure the password for the default `root` account.
This code uses the default root account to connect and the password can be set as `rootroot`. 

### Running App

Post installation of MySQL, Java and Maven, you will have to set up the tables and data in the data base.
For this, please run the sql commands present in the `Data.sql` file under the `resources` folder in the code base.

go to the `parkingsystem` folder <br>

And then compile and generate the final jar by running: `mvn package`<br>

Finally, run  the program with <br>
`java -jar target/parking-system-1.0-SNAPSHOT-jar-with-dependencies.jar`



### Testing

To run the tests from maven, go to the folder that contains the pom.xml file and execute the below command.<br>
`mvn site`<br>
You will find all the reports on `project-reports.html` page<br>
Path : `/target/site/project-reports.html`
