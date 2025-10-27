#  Step 4: Cashflow Bug Fix Report

**Bug ID:** TRD-2025-001  
**Date:** October 22, 2025  
**Status:** FIXED & VERIFIED  

---

## **Executive Summary**

A critical issue in the **cashflow calculation logic** was causing fixed-leg cashflows to display values approximately **100x larger than expected**.

For instance, a $10M trade with a 3.5% fixed rate was incorrectly producing **$875,000 per quarter** instead of **$87,500**.

After investigation, two key bugs were identified in the `TradeService` class — an incorrect interest rate formula and loss of precision due to improper data types. Both have now been corrected, tested, and validated through unit and integration tests.

---

## **Problem Description**

### **Symptoms**
- Fixed-leg cashflows displayed inflated values (≈100x larger than correct)
- $10M trade at 3.5% generating ~$875,000 instead of ~$87,500 per quarter
- Slight rounding inconsistencies due to floating-point precision

### **Business Impact**
- Incorrect settlement and interest payments
- Inaccurate P&L and exposure reporting
- Potential regulatory and client trust risks

---

## **Root Cause Analysis**

### **Investigation Method**
A focused review was conducted on the `calculateCashflowValue()` method inside `TradeService.java`, as well as its test coverage in `TradeServiceTest.java`.

The following checks were performed:
- Step-by-step validation of the mathematical formula
- Type analysis for monetary values
- Review of how interest rates were interpreted (percent vs. decimal)
- Precision testing through sample data

### **Findings**

#### 1. Percentage Formula Bug
- The rate (3.5%) was stored as `3.5` but used **without converting to decimal form** (`0.035`)
- This caused all calculations to be multiplied by 100×  
- **Example:**  
  - Buggy: `10,000,000 × 3.5 × (3 ÷ 12) = 875,000`  
  - Correct: `10,000,000 × 0.035 × (3 ÷ 12) = 87,500`

#### 2. Precision Bug
- The system used **`double`** for money, which introduced floating-point rounding errors
- Even when converted to `BigDecimal` later, the loss of precision persisted
- Financial systems require **exact decimal representation** to the cent

---

## **Solution Implemented**

### **File Updated**
`backend/src/main/java/com/technicalchallenge/service/TradeService.java`

### **Method Updated**
`calculateCashflowValue(TradeLeg leg, int monthsInterval)`

---

### **Before (Buggy Code)**
```java
double rate = leg.getRate();  // 3.5 (should be 0.035)
double notional = leg.getNotional().doubleValue();
double result = (notional * rate * months) / 12;
return BigDecimal.valueOf(result);
```

## Verification

**Mathematical Validation**:
```
Formula: (Notional × Rate% ÷ 100 × Months) ÷ 12

Example: $10M at 3.5% quarterly
= ($10,000,000 × 3.5 ÷ 100 × 3) ÷ 12
= ($10,000,000 × 0.035 × 3) ÷ 12
= $1,050,000 ÷ 12
= $87,500 (CORRECT)

Previous (buggy): $10,000,000 × 3.5 × 3 ÷ 12 = $875,000 (100x too large)
```

**Test Results**:
- Quarterly payment: $10M @ 3.5% = $87,500 (correct)
- Monthly payment: $10M @ 3.5% = $29,167 (correct)
- Annual payment: $10M @ 3.5% = $350,000 (correct)
- Fractional rates: $1M @ 4.375% quarterly = $10,937.50 (correct)

**Service Integration Validation**:
- IntelliJ test execution confirmed proper cashflow generation
- Service layer integration working correctly
- Business logic validated through logs

## Technical Details

**Changes Made**:
1. Replaced `double` arithmetic with `BigDecimal` for precision
2. Added percentage-to-decimal conversion (divide by 100)
3. Implemented proper rounding (HALF_UP to 2 decimal places)
4. Eliminated floating-point precision issues

**Files Modified**:
- `TradeService.java`: Fixed `calculateCashflowValue` method
- `TradeServiceTest.java`: Added comprehensive bug fix tests

## Testing Strategy

**Unit Tests**: Created comprehensive test suite covering:
- Bug report scenario (quarterly payment)
- Monthly and annual payment scenarios
- Fractional rate precision testing
- Edge cases (zero rates, floating legs)

**Integration Testing**: Verified through service layer execution logs
**Business Validation**: Mathematical formulas verified against financial standards

## Deployment Safety

- Backward compatible (no API changes)
- No database schema changes required
- Existing functionality preserved
- No performance impact

## Conclusion

Both critical bugs have been fixed:
1. Percentage conversion error eliminated (100x multiplication issue resolved)
2. Floating-point precision issues resolved with BigDecimal implementation

The cashflow calculation now produces mathematically correct results for all trading scenarios, eliminating the risk of settlement errors and regulatory compliance issues.