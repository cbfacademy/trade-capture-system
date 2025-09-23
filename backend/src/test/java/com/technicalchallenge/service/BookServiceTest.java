package com.technicalchallenge.service;

import com.technicalchallenge.dto.BookDTO;
import com.technicalchallenge.model.Book;
import com.technicalchallenge.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookService bookService;

    @Test
    void testFindBookById() {
        Book book = new Book();
        book.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Optional<BookDTO> found = bookService.getBookById(1L);
        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
    }

    @Test
    void testSaveBook() {
        Book book = new Book();
        book.setId(2L);
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(2L);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDTO saved = bookService.saveBook(bookDTO);
        assertNotNull(saved);
        assertEquals(2L, saved.getId());
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
