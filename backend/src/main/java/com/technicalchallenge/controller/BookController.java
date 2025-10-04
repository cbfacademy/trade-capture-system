package com.technicalchallenge.controller;

import com.technicalchallenge.dto.BookDTO;
import com.technicalchallenge.mapper.BookMapper;
import com.technicalchallenge.model.Book;
import com.technicalchallenge.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/books")
@Validated
public class BookController {//Purpose of this controller - to manage book-related operations such as retrieving, creating, and deleting books.
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired // Injecting BookService to handle business logic
    private BookService bookService;


    @GetMapping // Endpoint to retrieve all books
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        logger.info("Fetching all books");
        return ResponseEntity.ok().body(bookService.getAllBooks());
    }

    @GetMapping("/{id}") // Endpoint to retrieve a book by its ID
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        logger.debug("Fetching book by id: {}", id);
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping // Endpoint to create a new book
    public ResponseEntity<?> createBook(@Valid @RequestBody BookDTO bookDTO) {
        logger.info("Creating new book: {}", bookDTO);
        if (bookDTO.getBookName() == null || bookDTO.getBookName().isBlank()) {
            return ResponseEntity.badRequest().body("Book name is required");
        }
        if (bookDTO.getCostCenterName() == null) {
            return ResponseEntity.badRequest().body("Cost center is required");
        }
        var saved = bookService.saveBook(bookDTO);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}") // Endpoint to delete a book by its ID
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        logger.warn("Deleting book with id: {}", id);
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/values") // Endpoint to retrieve all book names
    public List<String> getAllBookNames() {
        return bookService.getAllBooks().stream()
                .map(BookDTO::getBookName)
                .toList();
    }
}
