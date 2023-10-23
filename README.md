Eva Rusinova Employee Application
================================

**Overview**

The Eva Rusinova Employee Application is a Java Spring Boot application designed to identify pairs of employees who have worked together on common projects for the longest period of time. It also identifies the longest project for the pair and displays all the common projects for the pair in a Datagrid.

**Features**
- Identification of the longest working pair of employees.
- Identification of the longest project for the pair.
- Display of all common projects for the identified pair in a Datagrid.
- The ' Longest Working Pair:' UI field represents the total days that the pair worked together on common projects, excluding weekends and official holidays
- The datagrid table represents the total days that the pair worked together on common projects, without excluding the weekends and official holidays.

**Prerequisites**
Before running the Eva Rusinova Employee Application, make sure you have the following prerequisites installed:
- Java (JDK 21 or higher)



**Dependencies**
The Eva Rusinova Employee Application relies on the following key dependencies:
The following dependencies are managed by the application and don't require manual installation:

- Spring Boot: The application is built on the Spring Boot framework.
- SpringDoc OpenAPI (Swagger): This provides API documentation and an interactive UI.
- Natty Date Parser: Used for parsing dates within the application.
- Apache Commons Lang 3: Provides utility classes for various operations within the application.

**Getting Started**

**Installation**
To install and run the application, follow these steps:
1. Clone the repository to your local machine.
2. Ensure you have the required prerequisites installed.
3. Build the application using Gradle.

**Running the Application**
To run the application, navigate to java/com/example/csv/DemoApplication.java


The application will start, and you can access it by opening a web browser and navigating to http://localhost:8080.

**Usage**
1. Access the application by navigating to http://localhost:8080.
2. Upload a CSV file containing employee data.
3. The application will identify the pair of employees who have worked together for the longest time and display the result, including the longest project for the pair and all common projects in a Datagrid.
   
**API Endpoints**
   The Eva Rusinova Employee Application exposes the following API endpoints:

1. **Upload and Process CSV File**
    - Endpoint: `/handleCsvFile`
    - Method: POST
    - Description: Upload a CSV file containing employee data, and the application will identify the pair of employees who have worked together for the longest time. It will display the result, including the longest project for the pair and all common projects in a Datagrid.
    - Example:
      ```http
      POST http://localhost:8080/handleCsvFile
      ```

2. **Test Endpoint (For Testing Purposes)**
    - Endpoint: `/csv/handleEmployees`
    - Method: POST
    - Description: This endpoint is for testing purposes and is also visible in Swagger. It allows you to handle employees from a predefined CSV file.
    - Example:
      ```http
      POST http://localhost:8080/csv/handleEmployees
      ```

These endpoints provide the functionality of Eva Rusinova Employee Application. You can use them for uploading CSV files, processing employee data, and retrieving relevant information.

**Testing**
The application includes unit tests to ensure its functionality. You can run the tests at java/com/example/csv/CsvReaderTest.java


**Contribution Guidelines**
If you wish to contribute to the Employee Application, please follow these guidelines:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Make your changes and commit them with clear, concise messages.
4. Push your changes to your fork.
5. Submit a pull request with a detailed description of your changes.

**License**
This project can be freely used for the purpose of this demo :) 
