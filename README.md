# Blog-Zone

**Blog-Zone** is a feature-rich blog platform that allows users to create, read, update, and delete blog posts. The platform supports real-time updates, secure authentication, and interactive features to enhance the user experience. It offers functionalities like post liking, commenting, saving, and search filtering. Users can also follow other bloggers for personalized content recommendations.

## Features

- **CRUD Operations**: Create, Read, Update, and Delete blog posts.
- **Real-Time Updates**: AJAX-powered updates to show changes without reloading the page.
- **Secure Authentication**: User login and registration with Spring Security.
- **Post Interaction**: Like, comment, and save posts.
- **Search Filtering**: Filter posts based on categories, keywords, and tags.
- **User Following**: Follow other users and get personalized content.

## Technologies Used

- **Backend**: Spring Boot
- **Security**: Spring Security
- **Database**: MySQL
- **Persistence**: JPA (Java Persistence API)
- **Real-Time Updates**: AJAX
- **Frontend**: HTML, CSS, JavaScript (AJAX)

## Setup and Installation

### Prerequisites

- Java 8 or higher
- MySQL database
- Maven

### Steps

1. **Clone the repository**:

    ```bash
    git clone https://github.com/Ramalingam-N/Blog-Zone.git
    cd Blog-Zone
    ```

2. **Set up the MySQL database**:
    
    - Create a new database in MySQL (e.g., `BLOGZONE`).
    - Update the `application.properties` file with your database credentials
      
      
    

   ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/BLOGZONE
    ```

4. **Build the project**:
    
    ```bash
    mvn clean install
    ```

5. **Run the application**:

    ```bash
    mvn spring-boot:run
    ```

6. **Access the application** at `http://localhost:8080` in your browser.

## Usage

- Register and log in to the platform.
- Create, read, update, or delete blog posts.
- Like, comment on, and save posts.
- Search for posts using categories or keywords.
- Follow other users to receive personalized content.

## Contributing

1. Fork the repository.
2. Create your feature branch: `git checkout -b feature/your-feature`.
3. Commit your changes: `git commit -m 'Add new feature'`.
4. Push to the branch: `git push origin feature/your-feature`.
5. Create a pull request.

## License

This project is licensed under the MIT License.
