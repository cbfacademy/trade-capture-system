package com.technicalchallenge.controller;

import com.technicalchallenge.dto.BookDTO;
import com.technicalchallenge.mapper.BookMapper;
import com.technicalchallenge.model.Book;
import com.technicalchallenge.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookMapper bookMapper;

    private ObjectMapper objectMapper;
    private BookDTO bookDTO;
    private Book book;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        // Create sample test data
        book = new Book();
        book.setBookName("Test Trading Book");
        book.setId(1L);
        book.setActive(true);
        book.setVersion(1);
        book.setCostCenter(null);

        bookDTO = new BookDTO();
        bookDTO.setBookName("Test Trading Book");
        bookDTO.setId(1L);
        bookDTO.setActive(true);
        bookDTO.setVersion(1);
        bookDTO.setCostCenterName("TRADING_DESK_1");

        // Set up default mappings
        when(bookMapper.toDto(any(Book.class))).thenReturn(bookDTO);
        when(bookMapper.toEntity(any(BookDTO.class))).thenReturn(book);
    }

    @Test
    void testGetAllBooks() throws Exception {
        // Given
        List<BookDTO> books = List.of(bookDTO);
        when(bookService.getAllBooks()).thenReturn(books);

        // When/Then
        mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].bookName", is("Test Trading Book")));

        verify(bookService).getAllBooks();
    }

    @Test
    void testGetBookById() throws Exception {
        // Given
        when(bookService.getBookById(1L)).thenReturn(Optional.of(bookDTO));

        // When/Then
        mockMvc.perform(get("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.bookName", is("Test Trading Book")))
                .andExpect(jsonPath("$.costCenterName", is("TRADING_DESK_1")));

        verify(bookService).getBookById(1L);
    }

    @Test
    void testGetBookByIdNotFound() throws Exception {
        // Given
        when(bookService.getBookById(999L)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/books/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(bookService).getBookById(999L);
    }

    @Test
    void testCreateBook() throws Exception {
        // Given
        BookDTO newBookDTO = new BookDTO();
        newBookDTO.setBookName("New Book");
        newBookDTO.setCostCenterName("TRADING_DESK_2");
        newBookDTO.setActive(true);
        newBookDTO.setVersion(1);

        BookDTO savedBookDTO = new BookDTO();
        savedBookDTO.setId(2L);
        savedBookDTO.setBookName("New Book");
        savedBookDTO.setCostCenterName("TRADING_DESK_2");
        savedBookDTO.setActive(true);
        savedBookDTO.setVersion(1);

        when(bookService.saveBook(any(BookDTO.class))).thenReturn(savedBookDTO);

        // When/Then
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBookDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.bookName", is("New Book")));

        verify(bookService).saveBook(any(BookDTO.class));
    }

    @Test
    void testCreateBookValidationFailure_MissingBookName() throws Exception {
        // Given
        BookDTO invalidDTO = new BookDTO();
        invalidDTO.setCostCenterName("TRADING_DESK_1");
        // Book name is purposely missing

        // When/Then
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Book name is required"));

        verify(bookService, never()).saveBook(any());
    }

    @Test
    void testCreateBookValidationFailure_MissingCostCenter() throws Exception {
        // Given
        BookDTO invalidDTO = new BookDTO();
        invalidDTO.setBookName("Valid Book Name");
        // Cost center is purposely missing

        // When/Then
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cost center is required"));

        verify(bookService, never()).saveBook(any());
    }

    @Test
    void testDeleteBook() throws Exception {
        // Given
        doNothing().when(bookService).deleteBook(1L);

        // When/Then
        mockMvc.perform(delete("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(bookService).deleteBook(1L);
    }

    @Test
    void testGetAllBookNames() throws Exception {
        // Given
        BookDTO book1 = new BookDTO();
        book1.setBookName("Equity Trading");
        BookDTO book2 = new BookDTO();
        book2.setBookName("Fixed Income");

        List<BookDTO> books = List.of(book1, book2);
        when(bookService.getAllBooks()).thenReturn(books);

        // When/Then
        mockMvc.perform(get("/api/books/values")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is("Equity Trading")))
                .andExpect(jsonPath("$[1]", is("Fixed Income")));

        verify(bookService).getAllBooks();
    }
}
