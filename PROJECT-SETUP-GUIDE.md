# Trading Application - Project Setup Guide

## Overview
This document provides comprehensive setup instructions for the trading application technical challenge. The application consists of a Spring Boot backend and React frontend, designed to simulate a real-world trading system.

## Prerequisites

### System Requirements
- **Operating System**: Windows 10/11, macOS 10.14+, or Linux (Ubuntu 18.04+)
- **RAM**: Minimum 8GB, Recommended 16GB
- **Disk Space**: At least 5GB free space
- **Internet Connection**: Required for downloading dependencies

### Required Software

#### 1. Java Development Kit (JDK)
- **Version**: JDK 17 or higher
- **Download**: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
- **Verification**: Run `java -version` and `javac -version`

#### 2. Node.js and npm
- **Version**: Node.js 18.x or higher
- **Download**: [Node.js Official Website](https://nodejs.org/)
- **Verification**: Run `node --version` and `npm --version`

#### 3. Maven
- **Version**: Maven 3.8 or higher
- **Download**: [Apache Maven](https://maven.apache.org/download.cgi)
- **Verification**: Run `mvn --version`
- **Alternative**: Use Maven wrapper (mvnw) included in the project

#### 4. Git
- **Version**: Latest stable version
- **Download**: [Git Official Website](https://git-scm.com/)
- **Verification**: Run `git --version`

#### 5. IDE/Code Editor (Recommended)
- **Backend**: IntelliJ IDEA, Eclipse, or VS Code with Java extensions
- **Frontend**: VS Code, WebStorm, or any modern editor with React support

#### 6. Database Browser (Optional)
- **H2 Console**: Built into the application (accessible via browser)
- **Alternative**: DB Browser for SQLite, DBeaver, or similar tool

## Project Structure
```
trading-application/
├── backend/                 # Spring Boot application
│   ├── src/main/java/      # Java source code
│   ├── src/test/java/      # Test files
│   ├── src/main/resources/ # Configuration files
│   ├── pom.xml            # Maven configuration
│   └── target/            # Build output
├── frontend/               # React application
│   ├── src/               # React source code
│   ├── public/            # Static assets
│   ├── package.json       # npm configuration
│   └── node_modules/      # Dependencies
├── docs/                  # Documentation
├── data/                  # Database files
└── README.md             # Main project documentation
```

## Setup Instructions

### Step 1: Clone the Repository
```bash
git clone <repository-url>
cd trading-application
```

### Step 2: Backend Setup

#### Navigate to Backend Directory
```bash
cd backend
```

#### Install Dependencies and Build
```bash
# Using Maven (if installed)
mvn clean install

# OR using Maven wrapper (if Maven not installed)
./mvnw clean install    # Linux/macOS
mvnw.cmd clean install  # Windows
```

#### Run Backend Application
```bash
# Using Maven
mvn spring-boot:run

# OR using Maven wrapper
./mvnw spring-boot:run    # Linux/macOS
mvnw.cmd spring-boot:run  # Windows

# OR run the built JAR
java -jar target/*.jar
```

#### Verify Backend is Running
- **Application URL**: http://localhost:8080
- **Health Check**: http://localhost:8080/actuator/health
- **H2 Database Console**: http://localhost:8080/h2-console
  - **JDBC URL**: `jdbc:h2:file:./data/tradingdb`
  - **Username**: `sa`
  - **Password**: (leave empty)

### Step 3: Frontend Setup

#### Open New Terminal and Navigate to Frontend Directory
```bash
cd frontend
```

#### Install Dependencies
```bash
npm install
```

#### Run Frontend Application
```bash
npm start
```

#### Verify Frontend is Running
- **Application URL**: http://localhost:3000
- **Should automatically open in browser**

### Step 4: Verify Full Application

#### Check Both Services are Running
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080
- **Database Console**: http://localhost:8080/h2-console

#### Test Basic Functionality
1. **Access the Application**: Navigate to http://localhost:3000
2. **Login/Register**: Create a user account or use existing credentials
3. **Explore Features**: Navigate through available trading functionalities
4. **API Testing**: Use browser developer tools to verify API calls

## Available Functionalities

### Backend Features
- **REST API Endpoints**: Comprehensive trading operations
- **Trade Management**: Create, read, update, delete trades
- **User Management**: User authentication and authorization
- **Cashflow Generation**: Automatic cashflow calculation for trades
- **Reference Data**: Books, counterparties, currencies, etc.
- **Database Operations**: H2 in-memory database with file persistence

### Frontend Features
- **User Interface**: Modern React-based trading interface
- **Trade Booking**: Interactive forms for creating trades
- **Trade Management**: View and manage existing trades
- **Search and Filter**: Find trades by various criteria
- **User Authentication**: Login and registration functionality
- **Responsive Design**: Works on desktop and mobile devices

### Key Endpoints for Testing

#### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

#### Trades
- `GET /api/trades` - Get all trades
- `POST /api/trades` - Create new trade
- `GET /api/trades/{id}` - Get trade by ID
- `PUT /api/trades/{id}` - Update trade
- `DELETE /api/trades/{id}` - Delete trade

#### Reference Data
- `GET /api/books` - Get all books
- `GET /api/counterparties` - Get all counterparties
- `GET /api/currencies` - Get all currencies
- `GET /api/users` - Get all users

## Database Information

### Default Data
The application comes pre-loaded with sample data:
- **Users**: Sample traders, sales users, and support staff
- **Books**: Trading books for different desks
- **Counterparties**: Sample counterparties for trading
- **Reference Data**: Currencies, trade types, statuses, etc.

### Database Console Access
1. Navigate to http://localhost:8080/h2-console
2. Use connection settings:
   - **Driver Class**: `org.h2.Driver`
   - **JDBC URL**: `jdbc:h2:file:./data/tradingdb`
   - **User Name**: `sa`
   - **Password**: (leave empty)

### Key Tables
- `TRADE` - Main trade information
- `TRADE_LEG` - Trade leg details
- `CASHFLOW` - Generated cashflows
- `APPLICATION_USER` - User accounts
- `BOOK` - Trading books
- `COUNTERPARTY` - Counterparty information

## Troubleshooting

### Common Issues

#### Backend Won't Start
**Issue**: `Port 8080 already in use`
**Solution**: 
```bash
# Find process using port 8080
lsof -i :8080  # macOS/Linux
netstat -ano | findstr :8080  # Windows

# Kill the process or use different port
# Add to application.properties: server.port=8081
```

**Issue**: `Java version not supported`
**Solution**: Ensure JDK 17 or higher is installed and JAVA_HOME is set correctly

#### Frontend Won't Start
**Issue**: `npm install fails`
**Solution**: 
```bash
# Clear npm cache
npm cache clean --force

# Delete node_modules and reinstall
rm -rf node_modules package-lock.json
npm install
```

**Issue**: `Port 3000 already in use`
**Solution**: Kill the process using port 3000 or start on different port:
```bash
PORT=3001 npm start  # Linux/macOS
set PORT=3001 && npm start  # Windows
```

#### Database Issues
**Issue**: `Cannot connect to H2 database`
**Solution**: 
1. Ensure backend is running
2. Check JDBC URL: `jdbc:h2:file:./data/tradingdb`
3. Verify data directory exists and is writable

**Issue**: `Database tables not found`
**Solution**: 
1. Check `data.sql` file in `src/main/resources`
2. Restart backend application
3. Check application logs for initialization errors

### Performance Issues
**Issue**: Application runs slowly
**Solutions**:
1. Increase JVM heap size: `java -Xmx2g -jar target/*.jar`
2. Ensure sufficient RAM available
3. Close unnecessary applications
4. Use SSD storage if available

### Network Issues
**Issue**: Frontend cannot connect to backend
**Solutions**:
1. Verify backend is running on port 8080
2. Check firewall settings
3. Ensure CORS is properly configured
4. Verify API base URL in frontend configuration

## Testing the Setup

### Quick Verification Checklist
- [ ] Backend starts without errors
- [ ] Frontend starts without errors
- [ ] Can access H2 database console
- [ ] Can create and view trades through the UI
- [ ] API endpoints respond correctly
- [ ] No console errors in browser developer tools

### Sample API Test
```bash
# Test health endpoint
curl http://localhost:8080/actuator/health

# Test trades endpoint (may require authentication)
curl http://localhost:8080/api/trades
```

## Development Tips

### Hot Reloading
- **Backend**: Use `mvn spring-boot:run` with Spring Boot DevTools
- **Frontend**: `npm start` automatically reloads on file changes

### Debugging
- **Backend**: Use IDE debugging or add `--debug` flag
- **Frontend**: Use browser developer tools and React DevTools extension

### Code Changes
- **Backend**: Changes require restart unless using DevTools
- **Frontend**: Changes automatically reload in development mode

## Next Steps

Once you have the application running successfully:

1. **Explore the Codebase**: Familiarize yourself with the project structure
2. **Review Documentation**: Read through the technical challenge steps
3. **Test Functionality**: Create trades, explore features, understand the business domain
4. **Prepare for Development**: Set up your preferred IDE and development tools

For the technical challenge, you'll be working with this application to fix issues, implement new features, and demonstrate your software development skills.

## Support

If you encounter issues during setup:
1. Check the troubleshooting section above
2. Review application logs for error messages
3. Ensure all prerequisites are properly installed
4. Verify network connectivity and port availability

The application should be fully functional once properly set up, providing a solid foundation for the technical challenge ahead.
