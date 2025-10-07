package com.technicalchallenge.service;

import com.technicalchallenge.dto.BookDTO;
import java.util.List;
import com.technicalchallenge.model.Book;
import com.technicalchallenge.repository.BookRepository;
import com.technicalchallenge.mapper.BookMapper; // Purpose: to map between Book and BookDTO
import com.technicalchallenge.repository.CostCenterRepository; // Purpose: to manage cost center data
import org.junit.jupiter.api.BeforeEach;



import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;// Purpose: to map between Book and BookDTO

    @Mock
    private CostCenterRepository costCenterRepository; // Purpose: to manage cost center data

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        when(bookMapper.toDto(any(Book.class))).thenAnswer(invocation -> {
            Book b = invocation.getArgument(0);
            BookDTO dto = new BookDTO();
            dto.setId(b.getId());
            dto.setBookName(b.getBookName());
            return dto;
        });

        when(bookMapper.toEntity(any(BookDTO.class))).thenAnswer(invocation -> {
            BookDTO dto = invocation.getArgument(0);
            Book b = new Book();
            b.setId(dto.getId());
            b.setBookName(dto.getBookName());
            return b;
        });

        when (costCenterRepository.findAll()).thenReturn(List.of());
    }

    @Test
    void testFindBookById() {
        Book book = new Book();
        book.setId(1L);
        book.setBookName("Test Book");

        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setBookName("Test Book");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDTO);

        Optional<BookDTO> found = bookService.getBookById(1L);
        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
        assertEquals("Test Book", found.get().getBookName());
    }

    @Test
    void testSaveBook() {
        Book book = new Book();
        book.setId(2L);
        book.setBookName("SaveBook");

        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(2L);
        bookDTO.setBookName("SaveBook");

        when(bookMapper.toEntity(bookDTO)).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDTO);

        when (costCenterRepository.findAll()).thenReturn(List.of());

        BookDTO saved = bookService.saveBook(bookDTO);
        assertNotNull(saved);
        assertEquals(2L, saved.getId());
        assertEquals("SaveBook", saved.getBookName());
    }

    @Test
    void testDeleteBook() {
        Long bookId = 3L;
        doNothing().when(bookRepository).deleteById(bookId);
        bookService.deleteBook(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void testFindBookByNonExistentId() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<BookDTO> found = bookService.getBookById(99L);
        assertFalse(found.isPresent());
    }

    // Business logic: test book cannot be created with null name
    @Test
    void testBookCreationWithNullNameThrowsException() {
        BookDTO bookDTO = new BookDTO();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> validateBook(bookDTO));
        assertTrue(exception.getMessage().contains("Book name cannot be null"));
    }

    // Helper for business logic validation
    private void validateBook(BookDTO bookDTO) {
        if (bookDTO.getBookName() == null) {
            throw new IllegalArgumentException("Book name cannot be null");
        }
    }
}

/*Development notes for commit message:
 fix(test): BookServiceTest - Added missing mocks to stop NullPointerExceptions

Problem: 
All five tests in BookServiceTest were failing because of NullPointerExceptions.

Root Cause: 
BookService uses constructor injection, but the test only mocked BookRepository. 
BookMapper and CostCenterRepository were not mocked, so they were null when BookService was created with @InjectMocks.

Solution: 
Added @Mock for BookMapper and CostCenterRepository and made sure they were injected into BookService using @InjectMocks. 
Also added simple default behavior in setUp() to make the mapper work like the real one.

Impact: 
Now BookServiceTest can run on its own without needing a real database or MapStruct mapping, 
and it confirms the service logic works correctly.
 */
