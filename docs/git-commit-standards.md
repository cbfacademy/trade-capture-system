# Git Commit Message Standards for Test Fixes

## Required Format for Test Fix Commits

```
fix(test): [TestClassName] - Brief description of what was fixed

- Problem: Describe the failing test and error
- Root Cause: What was causing the failure
- Solution: How you fixed it
- Impact: What functionality this enables

Example:
fix(test): TradeControllerTest - Fixed createTrade endpoint validation

- Problem: testCreateTrade() was failing with 400 Bad Request
- Root Cause: Missing @Valid annotation on trade request parameter
- Solution: Added @Valid annotation to TradeController.createTrade() method
- Impact: Enables proper request validation for trade creation
```

## Commit Message Examples

### Backend Test Fixes
```
fix(test): BookServiceTest - Fixed book creation with invalid cost center
fix(test): CashflowServiceTest - Resolved NPE in cashflow calculation
fix(test): UserControllerTest - Fixed authentication token validation
```

### Integration Test Fixes
```
fix(integration): Trade booking flow - Fixed end-to-end trade creation
fix(integration): User authentication - Resolved login redirection issue
```

## Branch Naming Convention
- Use: `fix/test-[test-class-name]` or `fix/tests-batch-[number]`
- Example: `fix/test-trade-controller` or `fix/tests-batch-1`
