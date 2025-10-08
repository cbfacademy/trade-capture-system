# Trading Application Technical Challenge

## Welcome to the Challenge!

This technical challenge is designed to assess your software development skills across multiple domains including backend development, frontend integration, debugging, full-stack feature implementation, and cloud architecture understanding.

**Duration**: This is a comprehensive challenge that may take several days to complete. You are not expected to finish all steps - each step you complete successfully demonstrates valuable skills.

## Challenge Overview

The challenge consists of **7 progressive steps**, each testing different aspects of software development:

1. **Project Setup** - Environment configuration and basic setup
2. **Test Case Fixes** - Debugging and fixing failing tests
3. **Missing Functionality** - Implementing advanced search, validation, and dashboard features
4. **Bug Investigation** - Root cause analysis and bug fixing
5. **Full-Stack Feature** - Settlement instructions implementation
6. **Containerization** - Docker and DevOps (OPTIONAL)
7. **Cloud Architecture** - Azure design documentation (OPTIONAL - MAGNIFICENT ACHIEVEMENT)

**Important**: Each step builds upon the previous ones, but Steps 5-7 are optional stretch objectives. Success is measured by the quality of your work, not the number of steps completed.

---

## Step 1: Project Setup (REQUIRED)

### Objective
Set up the trading application in your local environment and ensure both backend and frontend are running correctly.

### Tasks
1. **Environment Setup**: Install all required prerequisites (Java 17+, Node.js 18+, Maven)
2. **Fork Repository**: Fork the repository to your GitHub account
3. **Clone Repository**: Clone your forked repository to local machine
4. **Backend Setup**: Start the Spring Boot application on port 8080
5. **Frontend Setup**: Start the React application (Vite will assign available port, typically 5173)
6. **Verification**: Ensure both applications communicate correctly

### Success Criteria
- ✅ Backend health verified at http://localhost:8080/actuator/health
- ✅ Frontend running (default ports: **npm uses 5173**, **pnpm uses 3000** - check terminal for actual port)
- ✅ H2 database console accessible at http://localhost:8080/h2-console
- ✅ Swagger UI accessible at http://localhost:8080/swagger-ui/index.html
- ✅ Actuator endpoints accessible at http://localhost:8080/actuator/health
- ✅ Can navigate through the application UI
- ✅ API endpoints respond correctly
- ✅ CORS configuration allows frontend-backend communication (backend pre-configured for ports 3000 and 5173)

### Key Resources
- **Detailed Setup Guide**: See `PROJECT-SETUP-GUIDE.md`
- **Database Console**: JDBC URL: `jdbc:h2:file:./data/tradingdb`, User: `sa`, Password: password

---

## Step 2: Fix Failing Test Cases (REQUIRED)

### Objective
Identify and fix failing test cases in the backend application while documenting your debugging process and understanding of the fixes.

### Background
The application has several failing test cases that need to to be fixed. Your task is to systematically identify, debug, and fix these issues while demonstrating your understanding of the business logic.

### Tasks
1. **Run Tests**: Execute the test suite and identify failing tests
2. **Debug Issues**: Systematically investigate each failing test
3. **Fix Problems**: Implement proper solutions for each issue
4. **Document Work**: Complete the test fixes documentation template
5. **Verify Success**: Ensure all tests pass and no new issues are introduced

### Documentation Requirements
For each test fix, document:
- **Problem Description**: What was failing and why
- **Root Cause Analysis**: The underlying issue causing the test failure
- **Solution Implemented**: How you fixed the issue and why this approach was chosen
- **Verification**: How you confirmed the fix works

### Success Criteria
- ✅ All previously failing tests now pass
- ✅ No new test failures introduced
- ✅ Comprehensive documentation of each fix
- ✅ Understanding of business logic demonstrated
- ✅ Proper Git commit messages following required format

### Templates
- **Git Standards**: `git-commit-standards.md`
- **Self-Assessment**: `test-fix-checklist.md`

---

## Step 3: Implement Missing Functionality (REQUIRED)

### Business Enhancement Request

**Request ID:** TRADE-2025-REQ-003  
**Priority:** High  
**Requested By:** Trading Desk Operations Team  
**Business Sponsor:** Head of Trading Operations  
**Date Requested:** September 15, 2025  

### **Business Case:**
The trading desk has identified critical gaps in the current trading application that are impacting daily operations and trader productivity. Traders are struggling with basic operational tasks due to missing search capabilities, insufficient validation systems, and lack of personalized dashboard views.

### **Current Pain Points:**
- **Inefficient Trade Search**: Traders spend 30+ minutes daily manually scrolling through trade lists to find specific trades
- **Validation Gaps**: Invalid trades are being created, causing downstream settlement issues and operational risk
- **No Personalized Views**: Traders cannot easily see their own trades or get summary statistics for decision making
- **Manual Processes**: Lack of dashboard functionality forces traders to use spreadsheets for trade monitoring

### **Business Impact:**
- Reduced trading efficiency and productivity
- Increased operational risk due to validation gaps
- Poor user experience leading to trader frustration
- Manual workarounds creating audit trail issues

## Required Enhancement Implementation

You must implement **ALL THREE** of the following critical enhancements:

### **Enhancement 1: Advanced Trade Search System**

**Business Requirement**: "As a trader, I need to quickly find trades using multiple search criteria so that I can efficiently manage my trading portfolio."

**Current Problem**: The application only supports basic trade retrieval (get all, get by ID) but lacks the advanced search capabilities traders need.

**Required Implementation:**
```java
@GetMapping("/search") // Multi-criteria search
@GetMapping("/filter") // Paginated filtering  
@GetMapping("/rsql")   // RSQL query support for power users
```

**Business Requirements:**
- Search by counterparty, book, trader, status, date ranges
- Support for high-volume result sets with pagination
- Advanced query capabilities for experienced users (RSQL)
- Fast response times (<2 seconds for complex searches)

**RSQL Power User Examples:**
```
// Find trades for specific counterparty
/api/trades/rsql?query=counterparty.name==ABC

// Complex multi-criteria search
/api/trades/rsql?query=(counterparty.name==ABC,counterparty.name==XYZ);tradeStatus.tradeStatus==NEW

// Date range queries for month-end reporting
/api/trades/rsql?query=tradeDate=ge=2025-01-01;tradeDate=le=2025-12-31
```

### **Enhancement 2: Comprehensive Trade Validation Engine**

**Business Requirement**: "As a risk manager, I need comprehensive validation of all trade data to prevent invalid trades from entering our systems and causing operational issues."

**Current Problem**: Basic field validations exist, but comprehensive business rule validation and user privilege enforcement are missing.

**Required Implementation:**
```java
public ValidationResult validateTradeBusinessRules(TradeDTO tradeDTO)
public boolean validateUserPrivileges(String userId, String operation, TradeDTO tradeDTO)
public ValidationResult validateTradeLegConsistency(List<TradeLegDTO> legs)
```

**Critical Business Rules to Implement:**

1. **Date Validation Rules:**
   - Maturity date cannot be before start date or trade date
   - Start date cannot be before trade date
   - Trade date cannot be more than 30 days in the past

2. **User Privilege Enforcement:**
   - **TRADER**: Can create, amend, terminate, cancel trades
   - **SALES**: Can create and amend trades only (no terminate/cancel)
   - **MIDDLE_OFFICE**: Can amend and view trades only
   - **SUPPORT**: Can view trades only

3. **Cross-Leg Business Rules:**
   - Both legs must have identical maturity dates
   - Legs must have opposite pay/receive flags
   - Floating legs must have an index specified
   - Fixed legs must have a valid rate

4. **Entity Status Validation:**
   - User, book, and counterparty must be active in the system
   - All reference data must exist and be valid

### **Enhancement 3: Trader Dashboard and Blotter System**

**Business Requirement**: "As a trader, I need personalized dashboard views and summary statistics so that I can monitor my positions and make informed trading decisions."

**Current Problem**: No personalized views or summary information available for traders to monitor their activity.

**Required Implementation:**
```java
@GetMapping("/my-trades")      // Trader's personal trades
@GetMapping("/book/{id}/trades") // Book-level trade aggregation
@GetMapping("/summary")        // Trade portfolio summaries
@GetMapping("/daily-summary")  // Daily trading statistics
```

**Required New DTOs:**
```java
public class TradeSummaryDTO {
    // Total number of trades by status
    // Total notional amounts by currency
    // Breakdown by trade type and counterparty
    // Risk exposure summaries
}

public class DailySummaryDTO {
    // Today's trade count and notional
    // User-specific performance metrics
    // Book-level activity summaries
    // Comparison to previous trading days
}
```

**Business Requirements:**
- Authenticated user sees only their relevant trades
- Real-time summary calculations
- Support for multiple currency exposures
- Historical comparison capabilities

## Success Criteria

### **Functionality (40%)**
- ✅ All three enhancements fully implemented and working
- ✅ RSQL supports complex trading queries
- ✅ Validation prevents all identified business rule violations
- ✅ Dashboard provides actionable trading insights

### **Technical Implementation (30%)**
- ✅ RESTful API design following existing patterns
- ✅ Proper error handling with meaningful business messages
- ✅ Performance optimized for high-volume trading data
- ✅ Clean separation of concerns (Controller/Service/Repository)

### **Testing Coverage (20%)**
- ✅ Comprehensive unit tests for all business logic
- ✅ Integration tests for API endpoints
- ✅ Edge case testing for validation rules
- ✅ Performance testing for search functionality

### **Business Understanding (10%)**
- ✅ Implementation reflects understanding of trading operations
- ✅ Validation rules align with financial industry practices
- ✅ Dashboard metrics are relevant for trader decision-making
- ✅ Search functionality supports real trading workflows
---

## Step 4: Bug Investigation and Fix (REQUIRED)

### Objective
Investigate and fix a critical bug in the cashflow calculation logic that's affecting production trading operations.

### Bug Report
**Bug ID**: TRD-2025-001  
**Severity**: High  
**Symptoms**: 
- Fixed-leg cashflows showing values ~100x larger than expected
- $10M trade with 3.5% rate generating ~$875,000 quarterly instead of ~$87,500
- Precision issues causing slight variations in calculations

### Your Investigation Tasks

#### Task 1: Bug Identification
Systematically investigate the `calculateCashflowValue` method in `TradeService.java`:
- Review mathematical formulas
- Check data type usage for monetary calculations
- Examine percentage handling
- Look for floating-point precision issues

#### Task 2: Root Cause Analysis
Create a comprehensive report documenting:
- **Executive Summary**: Issue description and business impact
- **Technical Investigation**: Debugging methodology and findings
- **Root Cause Details**: Exact bugs and why they occur
- **Proposed Solution**: Technical fix approach and alternatives

#### Task 3: Bug Fix Implementation
Fix the identified issues:
- **Percentage Formula Bug**: Rate not converted to decimal (3.5% → 0.035)
- **Precision Bug**: Using `double` instead of `BigDecimal` for monetary calculations

#### Task 4: Testing and Validation
Prove your fix works:
- Unit tests for `calculateCashflowValue` method
- Integration tests for cashflow generation
- Verification: $10M at 3.5% quarterly = $87,500 (not $875,000)

### Success Criteria
- ✅ Correct identification of both bugs
- ✅ Professional root cause analysis document
- ✅ Comprehensive testing proving the fix works
- ✅ No regression in existing functionality

---

## Step 5: Full-Stack Feature Implementation (REQUIRED)

### Business Feature Request

**Request ID:** TRADE-2025-REQ-005  
**Priority:** High  
**Requested By:** Trading Operations Team  
**Business Sponsor:** Head of Trade Settlement  
**Date Requested:** September 18, 2025  

### **Business Case:**
The trading desk needs to capture settlement instructions during trade booking to streamline the settlement process and reduce operational risk. Currently, settlement instructions are managed separately in spreadsheets and email communications, leading to delays, errors, and potential settlement failures.

### **Current Operational Challenges:**
- **Manual Settlement Process**: Settlement instructions are managed outside the trading system in spreadsheets
- **Communication Delays**: Instructions are shared via email, causing delays and version control issues  
- **Settlement Errors**: Missing or incorrect settlement instructions lead to failed settlements and client complaints
- **Operational Risk**: Manual processes increase risk of human error and compliance issues
- **Audit Trail Gaps**: No centralized record of settlement instructions changes

### **Business Impact:**
- **$50K+ monthly losses** from settlement delays and failures
- **2-3 hours daily** spent by operations team coordinating settlement instructions
- **Regulatory compliance risk** due to poor audit trails
- **Client dissatisfaction** from settlement processing delays

### **Proposed Solution:**
Integrate settlement instructions directly into the trade capture process, allowing traders to specify settlement details during trade booking for immediate operational use.

## Feature Implementation Requirements

### **Core Feature: Settlement Instructions Integration**

**User Story**: "As a trader, I need to capture settlement instructions during trade booking so that the operations team has immediate access to settlement details without manual coordination."

### **Technical Implementation Options**

#### **Option A: Direct Trade Table Extension (Standard Implementation)**
**Approach**: Extend the existing Trade entity and database schema
```java
// Example: Add new field to Trade entity
// Consider how to modify the existing Trade table structure
// Think about JPA annotations and entity relationships
```

**Pros**: Simple, direct implementation with existing trade data  
**Cons**: Less flexible for future settlement instruction types

#### **Option B: Extensible AdditionalInfo Architecture (Bonus +15 points)**
**Approach**: Use existing AdditionalInfo table for key-value storage
```java
// Use existing AdditionalInfo table for key-value storage
// Key: "SETTLEMENT_INSTRUCTIONS" 
// Value: The settlement instruction text
// Consider the existing AdditionalInfo entity and service layer
```

**Pros**: Extensible design, follows enterprise patterns, leverages existing infrastructure  
**Cons**: More complex implementation requiring additional info service integration

#### **Implementation Guidance**
- **Database Schema**: Since this is an H2 database that recreates on startup, you have options:
  - Modify the existing JPA entity definitions
  - Update the `data.sql` initialization script if needed
  - Consider how the existing database initialization works
- **Entity Design**: Think about JPA relationships, validation annotations, and mapping strategies
- **Service Layer**: Leverage existing patterns in the codebase for data access and business logic

### **Required API Endpoints**
```java
// In TradeController.java - ADD THESE ENDPOINTS:

@GetMapping("/search/settlement-instructions")
public ResponseEntity<List<TradeDTO>> searchBySettlementInstructions(
    @RequestParam String instructions) {
    // Search trades by settlement instruction content
}

@PutMapping("/{id}/settlement-instructions")
public ResponseEntity<?> updateSettlementInstructions(
    @PathVariable Long id, 
    @RequestBody SettlementInstructionsUpdateDTO request) {
    // Update settlement instructions for existing trades
}
```

### **Frontend Integration Requirements**

#### **Trade Booking Form Enhancement**
- Add settlement instructions field to trade booking modal
- Implement proper validation (10-500 characters, no SQL injection)
- Make field optional but prominent in the UI layout
- Provide examples/templates for common settlement types

#### **Trade Management Integration**
- Display settlement instructions in trade detail views
- Add settlement instructions column to trade blotter/grid
- Enable editing during trade amendment process
- Show settlement instruction history for audit purposes

#### **Search and Reporting Enhancement**
- Add settlement instructions to advanced search forms
- Include settlement instructions in quick search capabilities
- Show settlement instructions prominently in search results
- Support partial text matching for operations team queries

### **Business Validation Rules**

#### **Data Validation**
- **Optional Field**: Settlement instructions not required for all trades
- **Length Limits**: Minimum 10 characters if provided, maximum 500 characters
- **Content Validation**: No special characters that could cause security issues
- **Format Standards**: Support for structured settlement instruction formats

#### **Business Logic**
- **Amendment Handling**: Settlement instructions should be editable during trade amendments
- **Audit Trail**: All changes to settlement instructions must be logged with user and timestamp
- **Access Control**: Visible to all user types, editable by TRADER and SALES users only
- **Search Integration**: Case-insensitive partial matching for operational searches

### **Sample Settlement Instructions**
```text
"Settle via JPM New York, Account: 123456789, Further Credit: ABC Corp Trading Account"

"DVP settlement through Euroclear, ISIN confirmation required before settlement"

"Cash settlement only, wire instructions: Federal Reserve Bank routing 123456789"

"Physical delivery to warehouse facility, contact operations team for coordination"
```

### **Integration Touchpoints**

#### **Operations Team Workflow**
- Settlement instructions immediately available after trade booking
- Search capability for finding trades with specific settlement requirements
- Export functionality for settlement processing systems
- Real-time notifications when settlement instructions are updated

#### **Risk Management Integration**
- Settlement instructions visible in risk reporting
- Ability to identify trades with non-standard settlement arrangements
- Integration with existing trade validation workflows

### **Success Criteria**

#### **Functional Requirements (40%)**
- ✅ Settlement instructions captured during trade creation
- ✅ Existing trades searchable by settlement instruction content
- ✅ Settlement instructions editable during trade amendments
- ✅ All CRUD operations work correctly end-to-end

#### **Technical Implementation (30%)**
- ✅ Architectural choice properly justified (direct table vs AdditionalInfo)
- ✅ Clean separation of concerns across all application layers
- ✅ Proper validation and error handling with business-appropriate messages
- ✅ RESTful API design following existing application patterns

#### **User Experience (20%)**
- ✅ Seamless UI integration that feels natural to traders
- ✅ Settlement instructions clearly visible in all relevant trade views
- ✅ Search functionality intuitive for operations team daily use
- ✅ Form validation provides clear, actionable feedback

#### **Code Quality & Testing (10%)**
- ✅ Comprehensive test coverage (unit, integration, end-to-end)
- ✅ Code follows existing patterns and architectural conventions
- ✅ Proper documentation and inline comments for business logic
- ✅ No performance degradation with large datasets

### **Bonus Achievement Opportunities**

#### **Advanced AdditionalInfo Implementation (+15 points)**
- Use extensible AdditionalInfo table architecture
- Demonstrates understanding of enterprise design patterns
- Shows ability to work with existing architectural decisions
- Future-proofs the solution for additional instruction types

#### **Enhanced User Experience (+10 points)**
- **Template System**: Pre-defined settlement instruction templates for common scenarios
- **Validation Intelligence**: Smart validation based on counterparty or trade type
- **Integration Readiness**: API design prepared for future settlement system integration
- **Advanced Search**: Support for complex queries and filtering options

### **Deliverables**

When you complete this feature, you should have:

1. ✅ **Database Schema Implementation** - Chosen approach (direct table or AdditionalInfo)
2. ✅ **Complete Backend API** - All CRUD operations with proper validation
3. ✅ **Frontend Integration** - Settlement instructions in all relevant UI components
4. ✅ **Comprehensive Testing** - Unit, integration, and end-to-end test coverage
5. ✅ **API Documentation** - Updated Swagger/OpenAPI documentation
6. ✅ **User Guide** - Brief documentation for traders and operations team
7. ✅ **Performance Verification** - Confirmed no impact on application performance

### **Expected Business Outcome**
- **Reduced Settlement Delays**: Immediate access to settlement instructions
- **Improved Audit Trail**: Centralized, timestamped record of all settlement details
- **Enhanced Operational Efficiency**: Elimination of manual settlement instruction coordination
- **Better Risk Management**: Visibility into non-standard settlement arrangements
- **Regulatory Compliance**: Complete audit trail for settlement instruction management
---

## Step 6: Application Containerization (OPTIONAL - BONUS)

### Overview
**Congratulations on reaching Step 6!** This is an optional step showcasing DevOps capabilities.

### Implementation Option
You can either:
1. **Complete Full Implementation**: Dockerize both applications with Docker Compose
2. **Documentation Alternative**: Complete the Docker Knowledge Assessment template

### Full Implementation Requirements

#### Backend Containerization
```dockerfile
# Multi-stage build
FROM maven:3.8.6-openjdk-17 AS build
# ... build process
FROM openjdk:17-jdk-slim
# ... runtime configuration
```

#### Frontend Containerization
```dockerfile
# Multi-stage build with nginx
FROM node:18-alpine AS build
# ... build process
FROM nginx:alpine
# ... production serving
```

#### Docker Compose Setup
```yaml
version: '3.8'
services:
  backend:
    build: ./backend
    ports: ["8080:8080"]
  frontend:
    build: ./frontend
    ports: ["3000:80"]
    depends_on: [backend]
```

### Documentation Alternative
If you cannot complete the full implementation, demonstrate your understanding by completing the comprehensive Docker Knowledge Assessment covering:

1. **Docker Fundamentals** (25 points) - Core concepts and ecosystem
2. **Dockerfile Best Practices** (25 points) - Multi-stage builds and optimization
3. **Docker Compose & Orchestration** (25 points) - Service management and networking
4. **Real-World Application** (25 points) - Environment strategies and challenges
5. **Advanced Concepts** (10 bonus points) - Orchestration, security, monitoring

### Success Criteria
- ✅ Applications run correctly in containers OR comprehensive documentation
- ✅ Proper multi-stage builds and optimization
- ✅ Docker Compose orchestration setup
- ✅ Environment configuration management
- ✅ Health checks and monitoring capabilities

---

## Step 7: Azure Cloud Architecture Design (OPTIONAL - MAGNIFICENT ACHIEVEMENT)

### Overview
**Reaching Step 7 represents a magnificent achievement!** This step focuses entirely on architectural design and documentation (no actual Azure deployment required).

### Assessment Approach
Create comprehensive Azure cloud architecture documentation without deploying actual resources.

### Required Documentation

#### Section 1: Azure Fundamentals (20 points)
- Core Azure services for trading applications
- Compute options comparison (App Service, ACI, AKS)
- Resource organization and management strategies

#### Section 2: Architecture Design (25 points)
- Complete system architecture with all Azure services
- Database architecture decisions (Azure SQL vs PostgreSQL vs Cosmos DB)
- Network and security architecture design

#### Section 3: Deployment Strategy (25 points)
- Container strategy and Azure Container Registry
- CI/CD pipeline design with Azure DevOps
- Scaling and performance optimization approaches

#### Section 4: Operations and Monitoring (20 points)
- Monitoring strategy with Application Insights
- Operational procedures and incident response
- Disaster recovery and business continuity planning

#### Section 5: Cost and Governance (10 points)
- Cost management and optimization strategies
- Governance framework and compliance considerations

### Business Context
Design architecture supporting:
- 24/7 global trading operations
- Auto-scaling for varying trading volumes
- High availability and disaster recovery
- Financial services compliance
- Integration with existing Azure enterprise systems

### Deliverables
1. **Complete architectural diagrams** (can be hand-drawn)
2. **Detailed service selection justifications**
3. **Implementation planning** (theoretical)
4. **Cost analysis and optimization recommendations**
5. **Security and compliance framework**
6. **Operational procedures documentation**

### Success Criteria
- ✅ Comprehensive understanding of Azure services
- ✅ Well-reasoned architectural decisions
- ✅ Enterprise-grade security and compliance planning
- ✅ Practical operational and cost considerations
- ✅ Professional documentation suitable for stakeholders

---

## Assessment Guidelines

### Overall Approach
- **Quality over Quantity**: Focus on doing fewer steps well rather than rushing through many
- **Documentation is Key**: Document your thought process, decisions, and learnings
- **Business Understanding**: Show comprehension of trading domain concepts
- **Code Quality**: Write maintainable, well-tested code following best practices

### Time Management
- **Step 1**: 2-4 hours (setup and familiarization)
- **Step 2**: 4-8 hours (debugging and documentation)
- **Step 3**: 8-16 hours (comprehensive feature implementation)
- **Step 4**: 4-8 hours (investigation and fix)
- **Step 5**: 12-24 hours (full-stack development)
- **Step 6**: 6-12 hours (containerization or documentation)
- **Step 7**: 4-8 hours (architecture documentation)

### Success Pathways
- **Solid Developer**: Complete Steps 1-4 thoroughly
- **Senior Developer**: Complete Steps 1-4 + attempt Step 5
- **Full-Stack Expert**: Complete Steps 1-5 + containerization knowledge
- **Solution Architect**: Complete all steps including cloud architecture

## Getting Started

1. **Read the Project Setup Guide**: `PROJECT-SETUP-GUIDE.md`
2. **Set up your environment** following the detailed instructions
3. **Explore the application** to understand the business domain
4. **Begin with Step 1** and progress systematically
5. **Document everything** as you go - this is crucial for assessment

## Resources and Templates

### Documentation Templates
- `git-commit-standards.md` - Git commit message format
- `test-fix-checklist.md` - Self-assessment checklist

### Assessment Materials
- Project setup verification checklist
- Code quality standards and best practices
- Business domain glossary and concepts
- API documentation and examples

Remember: This challenge is designed to showcase your skills and learning ability. Focus on demonstrating your problem-solving approach, code quality, and ability to work with complex systems. Good luck!
