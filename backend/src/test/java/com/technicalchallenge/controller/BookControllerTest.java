package com.technicalchallenge.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.technicalchallenge.dto.BookDTO;
import com.technicalchallenge.mapper.BookMapper;
import com.technicalchallenge.model.Book;
import com.technicalchallenge.service.BookService;

@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private BookMapper bookMapper;

    @BeforeEach
    public void setup() {
        Book book = new Book();
        book.setBookName("Book Name");
        book.setId(1L);
        book.setActive(true);
        book.setVersion(1);
        book.setCostCenter(null);


        BookDTO bookDTO = new BookDTO();
        bookDTO.setBookName("Book Name");
        bookDTO.setId(1L);
        bookDTO.setActive(true);
        bookDTO.setVersion(1);

        when(bookService.getAllBooks()).thenReturn(List.of(bookDTO));
        when(bookMapper.toDto(book)).thenReturn(bookDTO);
        when(bookMapper.toEntity(bookDTO)).thenReturn(book);

    }

    @Test
    void shouldReturnAllBooks() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk());
    }
    // Add more tests for POST, PUT, DELETE as needed
}
