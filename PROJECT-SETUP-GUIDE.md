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

#### 2. Node.js and Package Manager
- **Node.js Version**: Node.js 18.x or higher
- **Download**: [Node.js Official Website](https://nodejs.org/)
- **Verification**: Run `node --version`

**Package Manager Options:**
- **npm** (comes with Node.js): Run `npm --version` to verify
- **pnpm** (recommended for this project): 
  - **Installation**: `npm install -g pnpm`
  - **Verification**: Run `pnpm --version`
  - **Note**: This project is configured to use pnpm and includes `pnpm-lock.yaml`

#### 3. Maven
- **Version**: Maven 3.8 or higher
- **Download**: [Apache Maven](https://maven.apache.org/download.cgi)
- **Verification**: Run `mvn --version`
- **Installation Required**: Candidates must install Maven directly (Maven wrapper not supported for this challenge)

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

### Step 1: Fork and Clone the Repository

#### Fork the Repository
1. **Navigate to the Repository**: Go to the provided repository URL in your web browser
2. **Fork the Repository**: Click the "Fork" button in the top-right corner of the repository page
3. **Create Your Fork**: This creates a copy of the repository under your GitHub account
4. **Note Your Fork URL**: Your forked repository will be at `https://github.com/YOUR_USERNAME/trade-capture-system`

#### Clone Your Forked Repository
```bash
# Clone your forked repository (replace YOUR_USERNAME with your actual GitHub username)
git clone https://github.com/YOUR_USERNAME/trade-capture-system.git
cd trade-capture-system
```

### Step 2: Backend Setup

#### Navigate to Backend Directory
```bash
cd backend
```

#### Install Dependencies and Build
```bash
# Using Maven (required - must be installed)
mvn clean install
```

#### Run Backend Application
```bash
# Using Maven
mvn spring-boot:run

# OR run the built JAR
java -jar target/*.jar
```

#### Verify Backend is Running
- **Application URL (swagger)**: http://localhost:8080/swagger-ui/index.html
- **Health Check**: http://localhost:8080/actuator/health
- **H2 Database Console**: http://localhost:8080/h2-console
  - **JDBC URL**: `jdbc:h2:file:./data/tradingdb`
  - **Username**: `sa`
  - **Password**: (leave empty)

#### Verify API Documentation and Monitoring
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8080/api-docs
- **Actuator Endpoints**: http://localhost:8080/actuator/
- **Application Metrics**: http://localhost:8080/actuator/metrics
- **Application Info**: http://localhost:8080/actuator/info

### Step 3: Frontend Setup

#### Open New Terminal and Navigate to Frontend Directory
```bash
cd frontend
```

#### Install Dependencies

**Option 1: Using pnpm (Recommended - Project is configured for pnpm)**
```bash
# Install pnpm globally if not already installed
npm install -g pnpm

# Install dependencies
pnpm install
```

**Option 2: Using npm (Alternative)**
```bash
# Install dependencies
npm install

# Note: You may need to delete pnpm-lock.yaml first if switching from pnpm
# rm pnpm-lock.yaml  # Linux/macOS
# del pnpm-lock.yaml  # Windows
```

#### Run Frontend Application

**Using pnpm (Recommended)**
```bash
# Development server
pnpm dev

# Alternative commands
pnpm build    # Build for production
pnpm lint     # Run linting
pnpm test     # Run tests
pnpm preview  # Preview production build
```

**Using npm (Alternative)**
```bash
# Development server  
npm run dev

# Alternative commands
npm run build    # Build for production
npm run lint     # Run linting
npm run test     # Run tests
npm run preview  # Preview production build
```

#### Verify Frontend is Running
- **Application URL**: Check terminal for actual port assignment
  - Defaults to http://localhost:5173 
- **Should automatically open in browser**
- **Note**: The frontend uses Vite, which will automatically assign the next available port if the default is busy
- **CORS Configuration**: Backend is pre-configured to accept requests from port `5173`

### Step 4: Verify Full Application

#### Check Both Services are Running
- **Frontend**: http://localhost:5173
- **Backend API**: http://localhost:8080
- **Database Console**: http://localhost:8080/h2-console

#### Test Basic Functionality
1. **Access the Application**: Navigate to http://localhost:5173
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

**Issue**: `pnpm install fails`
**Solution**: 
```bash
# Clear pnpm cache
pnpm store prune

# Delete node_modules and reinstall
rm -rf node_modules pnpm-lock.yaml  # Linux/macOS
del node_modules pnpm-lock.yaml     # Windows (remove directories manually)
pnpm install
```

**Issue**: `Package manager conflicts`
**Solution**: 
```bash
# If switching from npm to pnpm
rm -rf node_modules package-lock.json
pnpm install

# If switching from pnpm to npm
rm -rf node_modules pnpm-lock.yaml
npm install
```

**Issue**: `Port 5173 already in use`
**Solution**: Kill the process using port 5173 or Vite will automatically use the next available port, requiring you to re-configure CORS in the backend:
```bash
# For pnpm  
PORT=5174 pnpm dev  # Linux/macOS
set PORT=5174 && pnpm dev  # Windows

# For npm
PORT=5174 npm run dev  # Linux/macOS
set PORT=5174 && npm run dev  # Windows
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

**Issue**: CORS errors in browser console
**Solutions**:
1. **Default Configuration**: Backend is pre-configured for standard Vite port
   - Supports `http://localhost:5173`
2. **Custom Port Usage**: If using a different port or URL (e.g. from GitHub CodeSpaces), create an override file at `backend/src/main/resources/local.properties` and add your frontend URL
3. **Configuration Location**: 
   ```properties
   # In local.properties
   management.endpoints.web.cors.allowed-origins=https://my-codespace-id-5173.app.github.dev
   ```

## Testing the Setup

### Quick Verification Checklist
- [ ] Backend starts without errors
- [ ] Frontend starts without errors
- [ ] Can access H2 database console at http://localhost:8080/h2-console
- [ ] **Swagger UI accessible at http://localhost:8080/swagger-ui/index.html**
- [ ] **Actuator health endpoint responds at http://localhost:8080/actuator/health**
- [ ] **Actuator metrics available at http://localhost:8080/actuator/metrics**
- [ ] **Actuator info endpoint accessible at http://localhost:8080/actuator/info**
- [ ] Can navigate through the application UI
- [ ] API endpoints respond correctly
- [ ] No console errors in browser developer tools

### Required Verification Steps

#### 1. Backend API Verification
```bash
# Test health endpoint
curl http://localhost:8080/actuator/health

# Test API documentation accessibility
curl http://localhost:8080/api-docs

# Test trades endpoint (may require authentication)
curl http://localhost:8080/api/trades
```

#### 2. Swagger UI Verification
1. Navigate to http://localhost:8080/swagger-ui/index.html
2. Verify all API endpoints are documented
3. Confirm you can expand endpoint sections
4. Test at least one GET endpoint using "Try it out" feature

#### 3. Actuator Monitoring Verification
1. **Health Check**: http://localhost:8080/actuator/health should return status "UP"
2. **Application Info**: http://localhost:8080/actuator/info should show build and environment details
3. **Metrics**: http://localhost:8080/actuator/metrics should list available metrics
4. **All Endpoints**: http://localhost:8080/actuator/ should show all available actuator endpoints
