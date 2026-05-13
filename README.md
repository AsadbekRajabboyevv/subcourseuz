# Subcourse

This app was created with Bootify.io - tips on working with the code [can be found here](https://bootify.io/next-steps/).

## Development

Update your local database connection in `application.yml` or create your own `application-local.yml` file to override settings for development. 

During development it is recommended to use the profile `local`. In IntelliJ `-Dspring.profiles.active=local` can be added in the VM options of the Run Configuration after enabling this property in "Modify options".

Lombok must be supported by your IDE. For IntelliJ install the Lombok plugin and enable annotation processing - [learn more](https://bootify.io/next-steps/spring-boot-with-lombok.html).

In addition to the Spring Boot application, the development server must also be started - for this [Node.js](https://nodejs.org/) version 24 is required. Angular CLI and required dependencies must be installed once:

# 🚀 Subcourse - Online Academy Platform

Subcourse is a comprehensive online learning solution. The project features a robust **Spring Boot** backend integrated with a modern **Angular** frontend, compiled into a single executable artifact for seamless deployment.

## 🛠 Tech Stack

### Backend
- **Java 17 & Spring Boot 3.x**
- **Spring Security & JWT** (Authentication & Authorization)
- **Spring Data JPA** (Hibernate ORM)
- **Liquibase** (Database Migrations)
- **PostgreSQL** (Primary Database)
- **SpringDoc / Swagger** (API Documentation)
- **HikariCP** (Connection Pooling)

### Frontend
- **Angular 17+**
- **Tailwind CSS** (Utility-first styling)
- **FontAwesome** (Iconography)
- **Angular Signals** (Reactive State Management)

## ⚙️ Environment Variables

To run this project, you must configure the following environment variables in your system or `.env` file:

### 🗄 Database & Core Settings
| Variable | Description | Example |
| :--- | :--- | :--- |
| `JDBC_DATABASE_URL` | DB Connection URL | `jdbc:postgresql://localhost:5432/subcourse` |
| `JDBC_DATABASE_USERNAME` | DB Username | `postgres` |
| `JDBC_DATABASE_PASSWORD` | DB Password | `your_password` |
| `DDL_AUTO` | Hibernate DDL Mode | `validate` or `update` |
| `OPEN_IN_VIEW` | Spring JPA OSIV | `false` |

### 🔐 Security & JWT
| Variable | Description | Example |
| :--- | :--- | :--- |
| `JWT_SECRET_KEY` | Secret for token signing | `64-character-min-secure-key...` |
| `JWT_EXPIRATION_IN_MS` | Token validity (ms) | `86400000` (24h) |
| `SECURITY_USER_NAME` | Default Admin Username | `admin` |
| `SECURITY_USER_PASSWORD` | Default Admin Password | `admin_pass` |

### 📂 File Storage System
| Variable | Description | Example |
| :--- | :--- | :--- |
| `FILE_STORAGE_ROOT` | Directory for uploads | `./uploads` |
| `MAX_FILE_SIZE` | Max per file size | `100MB` |
| `MAX_REQUEST_SIZE` | Max total request size | `110MB` |

### 📧 Mail (SMTP)
| Variable | Description | Example |
| :--- | :--- | :--- |
| `MAIL_HOST` | SMTP Server | `smtp.gmail.com` |
| `MAIL_PORT` | SMTP Port | `587` |
| `MAIL_USERNAME` | Email Address | `your-app@gmail.com` |
| `MAIL_PASSWORD` | App Password | `xxxx xxxx xxxx xxxx` |

## 🏗 Build & Deployment

Since the frontend and backend are unified, you can build the entire project using Maven:

1. **Clone the repository:**
 ```
   git clone https://github.com/AsadbekRajabboyevv/subcourseuz.git
```
```
   cd subcourseuz
```

Build the Project:
    Bash
    ./mvnw clean install

This command compiles the Angular frontend, moves the build artifacts to src/main/resources/static, and packages everything into a single JAR.

Run the Application:
    Bash

    java -jar target/subcourse-0.0.1-SNAPSHOT.jar

Access the Platform:

Web UI: http://localhost:4200

API Swagger Docs: http://localhost:8080/swagger-ui/index.html

📂 Project Structure
Plaintext
```
├── src/
│   ├── main/
│   │   ├── java/         # Spring Boot Source Code
│   │   ├── resources/
│   │   │   ├── db/       # Liquibase changelogs & master.xml
│   │   │   └── static/   # Angular production build (automatically generated)
│   └── frontend/         # Angular Project Directory
├── pom.xml               # Unified Maven Configuration
└── README.md
```
🤝 Contributing

[Contributions, issues and feature requests are welcome! Feel free to check the issues page.](https://github.com/AsadbekRajabboyevv/subcourseuz/issues/4)

