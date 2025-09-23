# Trading System Functionality Documentation

## Business Overview
This trading system manages financial trades with sophisticated lifecycle management, automated cashflow generation, and role-based access control. It supports interest rate swaps, cross-currency swaps, and spot trades with comprehensive audit trails.

## Core Functionality

### Trade Management

#### Trade Creation
- **Purpose**: Create new trades with 2 legs (Fixed/Floating)
- **Process**:
  1. User inputs trade details (counterparty, book, dates, notional)
  2. System validates business rules
  3. Creates trade with version 1
  4. Generates trade legs
  5. Auto-generates cashflows based on schedule
  6. Sets trade status to "NEW"

#### Trade Amendment
- **Purpose**: Modify existing trades while maintaining history
- **Process**:
  1. User selects existing trade
  2. Modifies trade details
  3. System deactivates old version
  4. Creates new version with incremented version number
  5. Sets status to "AMENDED"
  6. Regenerates cashflows if schedule changed

#### Trade Termination
- **Purpose**: End trade lifecycle normally
- **Process**:
  1. User initiates termination
  2. System validates user permissions
  3. Changes trade status to "TERMINATED"
  4. Updates last touch timestamp
  5. Cashflows remain for historical reference

#### Trade Cancellation
- **Purpose**: Cancel trade due to error or business decision
- **Process**:
  1. User initiates cancellation
  2. System validates user permissions
  3. Changes trade status to "CANCELLED"
  4. Updates last touch timestamp

### Cashflow Generation

#### Algorithm Overview
The system automatically generates cashflows based on:
- **Payment Schedule**: Monthly (1M), Quarterly (3M), Yearly (12M)
- **Trade Maturity**: End date for cashflow generation
- **Leg Type**: Fixed or Floating

#### Examples

**Monthly Schedule (1M) - 1 Year Trade**:
- Start: 2025-01-17, Maturity: 2026-01-17
- Generates: 12 cashflows (every month)
- Dates: 2025-02-17, 2025-03-17, ..., 2026-01-17

**Quarterly Schedule (3M) - 2 Year Trade**:
- Start: 2025-01-17, Maturity: 2027-01-17
- Generates: 8 cashflows (every 3 months)
- Dates: 2025-04-17, 2025-07-17, ..., 2027-01-17

#### Value Calculation
- **Fixed Leg**: (Notional × Rate × Months) ÷ 12
  - Example: $1M × 5% × 3 months ÷ 12 = $12,500
- **Floating Leg**: $0 (placeholder for index fixing)

### User Management

#### User Roles & Capabilities

**TRADER_SALES**:
- Create new trades
- Amend existing trades
- Terminate trades
- Cancel trades
- View all trades they created

**SUPPORT**:
- View all trades (read-only)
- Generate reports
- Assist users with system issues

**MIDDLE_OFFICE (MO)**:
- View all trades
- Amend trades for corrections
- Generate settlement instructions
- Monitor trade lifecycle

**ADMIN**:
- Manage users (create, update, deactivate)
- Assign user roles
- Configure system parameters
- Access audit trails

**SUPERUSER**:
- Full system access
- Database administration
- System configuration
- Override business rules (when necessary)

### Data Validation & Business Rules

#### Date Validations
1. **Trade Date**: Can be in the past (for backdated trades)
2. **Start Date**: Must be >= Trade Date
3. **Maturity Date**: Must be > Start Date
4. **Execution Date**: Typically = Trade Date

#### Trade Structure Validations
1. **Legs**: Must have exactly 2 legs
2. **Notional**: Must be positive
3. **Rate**: Fixed leg must have rate > 0
4. **Currency**: Must be valid currency from reference data

#### User Permission Validations
1. **Book Access**: User must have access to selected book
2. **Action Permissions**: User role must allow the action
3. **Active Entities**: Can only trade with active counterparties/books

#### Schedule Validations
1. **Maturity vs Schedule**: Maturity must allow at least one cashflow
2. **Business Days**: Cashflow dates adjusted for holidays
3. **Calendar**: Must use appropriate holiday calendar

### Reference Data Management

#### Organizational Structure
- **Desk**: Trading desks (FX, Rates, Credit)
- **SubDesk**: Specialized areas within desks
- **CostCenter**: Cost allocation units
- **Book**: Trading books for position management

#### Market Data
- **Currency**: USD, EUR, GBP, etc.
- **Index**: LIBOR, EURIBOR, SOFR, etc.
- **Holiday Calendars**: NY, LON, TOK, etc.
- **Business Day Conventions**: Following, Modified Following, etc.

### Additional Fields (Extensibility)

#### Purpose
Allow custom fields without modifying core trade table structure.

#### Usage
- **Entity Type**: "TRADE", "COUNTERPARTY", "BOOK"
- **Field Name**: Custom field identifier
- **Field Value**: String representation of value
- **Field Type**: "STRING", "NUMBER", "DATE", "BOOLEAN"

#### Examples
```json
{
  "entityType": "TRADE",
  "entityId": 100001,
  "fieldName": "riskLimit",
  "fieldValue": "5000000",
  "fieldType": "NUMBER"
}
```

### Reporting & Analytics

#### Trade Blotter
- Real-time view of all trades
- Filterable by date, counterparty, book, status
- Sortable columns
- Export capabilities

#### Position Reports
- Net position by currency
- Position by book/desk
- Mark-to-market valuations
- Risk metrics

#### Audit Reports
- Trade modification history
- User activity logs
- System access reports
- Compliance reports

### Integration Points

#### External Systems
- **Risk Management**: Position limits, VaR calculations
- **Settlement**: Payment instructions, confirmations
- **Accounting**: P&L calculations, journal entries
- **Regulatory**: Trade reporting, compliance monitoring

#### Data Feeds
- **Market Data**: Real-time rates, curves
- **Reference Data**: Holiday calendars, currency rates
- **Counterparty Data**: Credit ratings, limits

### Error Handling

#### Validation Errors
- Clear error messages for business rule violations
- Field-level validation feedback
- Suggested corrections

#### System Errors
- Graceful degradation
- Error logging for support team
- User-friendly error messages
- Automatic retry for transient failures

### Workflow Examples

#### Typical Trade Workflow
1. **Trader** logs into system
2. Navigates to "New Trade" screen
3. Selects counterparty and book
4. Enters trade details (notional, rate, dates)
5. System validates inputs
6. Trader confirms trade creation
7. System generates cashflows
8. Trade appears in blotter
9. **Middle Office** reviews and confirms
10. **Settlement** team processes payments

#### Trade Amendment Workflow
1. **Trader** searches for existing trade
2. Selects "Amend" option
3. Modifies relevant fields
4. System shows impact of changes
5. Trader confirms amendment
6. System creates new version
7. Old version marked inactive
8. Notification sent to relevant teams

### Performance Characteristics

#### Response Times
- Trade creation: < 2 seconds
- Trade search: < 1 second
- Cashflow generation: < 5 seconds
- Report generation: < 10 seconds

#### Scalability
- Supports 10,000+ trades
- 100+ concurrent users
- 1M+ cashflows
- 24/7 availability

### Security Features

#### Access Control
- Role-based permissions
- Field-level security
- Audit trails for all actions
- Session management

#### Data Protection
- Sensitive data encryption
- Audit logs immutable
- Regular backups
- Disaster recovery procedures
