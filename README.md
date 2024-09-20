# Welcome to D&D Photo Contest App!!!



## Overview
The Photo Contest App is a web application that allows users to participate in photo contests. Users can submit their photos, vote on other submissions, and view contest results. The application is built using Java, Spring Boot, and Thymeleaf, with a MySQL database for data storage. It includes both a RESTful API and an MVC architecture.

## Features
- User Authentication and Authorization
- Photo Submission
- Voting System
- Contest Phases Management
- Notifications
- Admin Panel

## Technologies Used
- Java
- Spring Boot
- Thymeleaf
- MySQL
- JavaScript
- HTML/CSS
- Gradle

## Getting Started

### Prerequisites
- Java 11 or higher
- MySQL
- Gradle

### Installation
1. Clone the repository:
    ```sh
    git clone https://github.com/Java-A-60-Forum-System-by-D-D/Photo-Contest_App.git
    ```
2. Navigate to the project directory:
    ```sh
    cd Photo-Contest_App
    ```
3. Configure the MySQL database:
    - Create a database named `photo_contest`.
    - Update the database configuration in `src/main/resources/application.properties`:
        ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/photo_contest
        spring.datasource.username=your_username
        spring.datasource.password=your_password
        ```

4. Build the project:
    ```sh
    ./gradlew build
    ```

5. Run the application:
    ```sh
    ./gradlew bootRun
    ```

6. Open your browser and navigate to `http://localhost:8080`.

## Usage
- Register a new account or log in with an existing account.
- Submit your photos to active contests.
- Vote on other users' submissions.
- View contest results and notifications.

## Scheduled Tasks
The application includes scheduled tasks to manage contest phases. These tasks are defined in the `PhaseScheduler` class and can be configured using cron expressions.

## Contributing
1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Commit your changes (`git commit -m 'Add some feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Open a pull request.

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.

## Contact
For any inquiries or support, please contact us at support@photocontestapp.com.
