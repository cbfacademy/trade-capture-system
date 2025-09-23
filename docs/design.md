# Trading System Design Documentation

## Overview
This is a comprehensive financial trading system designed as a technical challenge for programming students. The system implements a full-stack trading platform with sophisticated business logic, role-based access control, and modern technology stack.

## Architecture

### System Architecture
```
Frontend (React 19) ↔ REST API ↔ Backend (Spring Boot) ↔ Database (H2)
```

### Technology Stack

#### Backend
- **Framework**: Spring Boot 3.3.4
- **Database**: H2 (file-based with in-memory performance)
- **ORM**: JPA/Hibernate
- **Build Tool**: Maven
- **Testing**: JUnit 5, Cucumber
- **Documentation**: SpringDoc OpenAPI (Swagger)
- **Query**: RSQL JPA Specification

#### Frontend
- **Framework**: React 19 with TypeScript
- **Build Tool**: Vite
- **Package Manager**: pnpm
- **State Management**: MobX
- **Styling**: Tailwind CSS
- **Data Grid**: AG Grid React
- **Testing**: Vitest, Playwright

## Data Model

### Core Entities

#### Organizational Hierarchy
```
Desk → SubDesk → CostCenter → Book
```

#### Trading Entities
- **Trade**: Main trading entity with versioning and lifecycle management
- **TradeLeg**: Each trade has exactly 2 legs (Fixed/Floating)
- **Cashflow**: Generated automatically based on schedules
- **Counterparty**: External trading parties

#### Reference Data
- Currency, TradeType, TradeSubType, TradeStatus
- Index, Schedule, BusinessDayConvention
- HolidayCalendar, LegType, PayRec

#### User Management
- **ApplicationUser**: System users with roles
- **UserProfile**: TRADER_SALES, SUPPORT, ADMIN, MO, SUPERUSER
- **Privilege**: Fine-grained permissions
- **UserPrivilege**: User-privilege mapping

#### Extensibility
- **AdditionalInfo**: Key-value store for custom fields (bonus feature)

## Business Logic

### Trade Lifecycle
1. **NEW**: Initial trade creation
2. **AMENDED**: Modified version (version control)
3. **TERMINATED**: End of trade life
4. **CANCELLED**: Trade cancellation
5. **LIVE/DEAD**: Operational status

### Cashflow Generation Algorithm
Cashflows are generated based on:
- **Schedule**: 1M, 3M, 12M intervals
- **Maturity Date**: End date for generation
- **Leg Type**: 
  - Fixed: Simple interest calculation
  - Floating: Zero value placeholder

### Business Validations
- Date hierarchy: trade_date ≤ start_date ≤ maturity_date
- User privilege validation for operations
- Active entity requirements
- Schedule vs maturity validation

### Version Control
All entities support versioning:
- New records: version = 1, active = true
- Updates: deactivate old (active = false), create new version
- Audit trail with timestamps

## Security & Access Control

### User Roles & Permissions
- **TRADER**: Create, amend, terminate, cancel trades
- **SALES**: Book and amend trades only
- **MIDDLE_OFFICE**: Amend and view trades
- **SUPPORT**: View trades only
- **ADMIN**: User management
- **SUPERUSER**: Full system access

### Authentication
- Username/password based
- Session management
- Role-based route protection

## API Design

### RESTful Endpoints
```
GET    /api/trades              - List all trades
GET    /api/trades/{id}         - Get trade by ID
POST   /api/trades              - Create new trade
PUT    /api/trades/{id}         - Amend existing trade
DELETE /api/trades/{id}         - Cancel trade
POST   /api/trades/{id}/terminate - Terminate trade

GET    /api/users               - List users (admin only)
POST   /api/users               - Create user (admin only)
PUT    /api/users/{id}          - Update user (admin only)

POST   /api/login/{username}    - Authenticate user
```

### RSQL Query Support
Dynamic filtering using RSQL syntax:
```
/api/trades?filter=tradeDate>=2025-01-01;counterparty.name==BigBank
```

## Database Design

### Key Features
- **File Persistence**: H2 database with file storage
- **Automatic Schema**: DDL auto-generation
- **Data Initialization**: Reference data loaded on startup
- **Audit Trails**: Created/modified timestamps on all entities

### Performance Considerations
- Indexed foreign keys
- Lazy loading for relationships
- Connection pooling
- Query optimization

## Frontend Architecture

### Component Structure
```
App
├── AppRouter (React Router)
├── Layout
│   ├── Navbar
│   └── Sidebar
├── Pages
│   ├── SignIn/SignUp
│   ├── TraderSales
│   ├── MiddleOffice
│   ├── Support
│   └── Admin
└── Components
    ├── AGGridTable
    ├── Modals
    └── Forms
```

### State Management
- **MobX Stores**: User, Trade, UI state
- **React Query**: Server state management
- **Local State**: Component-specific state

### Design System
- **Colors**: Primary green, secondary red
- **Components**: Rounded, shadowed, hover effects
- **Notifications**: 5-second snackbars
- **Modals**: Sliding overlays

## Development Setup

### Prerequisites
- Java 21
- Node.js 18+
- Maven 3.8+
- pnpm 8+

### Local Development
```bash
# Backend
cd backend
mvn spring-boot:run

# Frontend
cd frontend
pnpm install
pnpm dev
```

### Testing Strategy
- **Unit Tests**: JUnit 5 for services
- **Integration Tests**: Cucumber for e2e
- **Frontend Tests**: Vitest for components, Playwright for e2e

## Deployment

### Docker Support
```dockerfile
# Multi-stage build
FROM openjdk:21-jdk-slim AS backend
# ... backend build

FROM node:18-alpine AS frontend
# ... frontend build

FROM nginx:alpine
# ... final image
```

### Cloud Deployment
- **Azure**: Container instances or App Service
- **Kubernetes**: Helm charts provided
- **Database**: Persistent volumes for H2 file storage

## Monitoring & Observability

### Logging
- Structured logging with SLF4J
- Different log levels per package
- Request/response logging

### Health Checks
- Spring Boot Actuator endpoints
- Database connectivity checks
- Custom business logic health indicators

### Metrics
- JVM metrics
- Business metrics (trades per minute, etc.)
- Custom dashboards ready

## Security Considerations

### Input Validation
- DTO validation annotations
- Business rule validation
- SQL injection prevention (JPA)

### CORS Configuration
- Restricted origins for production
- Credential support for authentication

### Data Protection
- Password hashing (in production)
- Audit trails for sensitive operations
- Role-based data access
