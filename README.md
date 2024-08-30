# D\&D Photo Contest

## Overview
This project is a photography contest application built using Java, Spring Boot, and Gradle. It allows users to create, participate in, and review photo contests. The application supports various functionalities such as user authentication, contest management, photo submissions, and reviews.

## Features
- User authentication and authorization
- Contest creation and management
- Photo submission and review
- Filtering and sorting of photo submissions
- Contest summary with top 3 places

## Technologies Used
- Java
- Spring Boot
- Gradle
- MySQL (for database operations)
- IntelliJ IDEA (IDE)

## Getting Started

### Prerequisites
- Java 11 or higher
- Gradle
- MySQL
- An IDE like IntelliJ IDEA

### Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/yourusername/your-repo-name.git
   ```
2. Navigate to the project directory:
   ```sh
   cd your-repo-name
   ```
3. Build the project using Gradle:
   ```sh
   gradle build
   ```

### Running the Application
1. Ensure MySQL is running and create a database for the project.
2. Update the `application.properties` file with your MySQL database credentials.
3. Run the application using Gradle:
   ```sh
   gradle bootRun
   ```
4. The application will start on `http://localhost:8080`.

## API Endpoints

### Photo Submissions
- `GET /api/photo-submissions` - Retrieve all photo submissions with optional filtering by title, username, sortBy, and sortDirection.
- `GET /api/photo-submissions/{id}/reviews` - Retrieve reviews for a specific photo submission.
- `POST /api/photo-submissions/{id}/reviews` - Create a review for a specific photo submission.

### Contests
- `GET /api/contests` - Retrieve all contests.
- `POST /api/contests` - Create a new contest.
- `GET /api/contests/{id}` - Retrieve a specific contest by ID.
- `PUT /api/contests/{id}` - Update a specific contest by ID.
- `DELETE /api/contests/{id}` - Delete a specific contest by ID.

## Project Structure
- `src/main/java/com/example/demo` - Main source code directory
  - `controllers` - REST controllers
  - `models` - Entity and DTO classes
  - `services` - Service layer
  - `repositories` - Repository layer
  - `mappers` - Mapper classes for converting between entities and DTOs
- `src/main/resources` - Configuration files and static resources

## Contributing
1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Commit your changes (`git commit -m 'Add some feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Open a pull request.

## License
This project is licensed under the MIT License. See the `LICENSE` file for more details.

## Contact
For any questions or feedback, please contact [your-email@example.com].
