# Test Fix Report

## BOOK-SERVICE-STABILIZATION
## 1. Bug Identification
The `BookServiceTest` suite was failing with **three distinct errors** across methods (`testFindBookById`, `testFindBookByNonExistentId`, and `testSaveBook`):

* **Dependency Errors:** Immediate NullPointerExceptions (NPEs), indicating the service object was incomplete.
* **Read Logic Error:** `testFindBookById` returned an Empty Optional, the retrieved entity wasn't being correctly mapped for the test's return.
* **Write Logic Error:** `testSaveBook` resulting in a `PotentialStubbingProblem`),  the save operation was passed a `null` entity.



## 2. Root Cause Analysis
The issues stemmed from an incomplete and undefined test setup for the `BookServiceTest` class:

* **Missing Injection (Caused NPEs):** The `@Mock` declarations for the required dependencies, **`BookMapper`** and **`CostCenterRepository`**, were missing. This prevented the `@InjectMocks` annotation from correctly injecting these non-null dependencies into the `BookService` instance.
* **Undefined Mapper Behavior (Caused Conversion/Stubbing Errors):** The mocked `BookMapper` was not programmed with specific behaviors.
    * **Read Flow:** The `toDto()` method returned `null` by default.
    * **Save Flow:** The `toEntity()` method returned `null` by default, causing `testSaveBook` to call `bookRepository.save(null)`.

## 3. Bug Fix Implementation

The fix involved a two-step approach: **correcting the dependency wiring** and **defining the required behavior** for the data mappers.

* **Dependency Injection Fix:** Added the missing **`@Mock` declarations** for `BookMapper` and `CostCenterRepository`.
* **Read/Conversion Stubbing Fix:** Implemented the Mockito stubbing for the read flow to ensure successful conversion:
    * `when(bookMapper.toDto(any(Book.class))).thenReturn(expectedDto)`.
* **Save/Persistence Stubbing Fix:** Implemented the Mockito stubbing for the write flow to ensure non-null objects are used throughout the save operation:
    * **`when(bookMapper.toEntity(any(BookDTO.class))).thenReturn(book)`** prevents passing `null` to `save`.
    * `when(bookRepository.save(book)).thenReturn(book)` ensures the mock repository returns a valid object.

## 4. Testing and Validation

All tests within the **`BookServiceTest`** class were re-run and now execute successfully.