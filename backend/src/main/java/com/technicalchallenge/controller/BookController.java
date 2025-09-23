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
public class BookController {
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;


    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        logger.info("Fetching all books");
        return ResponseEntity.ok().body(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        logger.debug("Fetching book by id: {}", id);
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        logger.warn("Deleting book with id: {}", id);
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/values")
    public List<String> getAllBookNames() {
        return bookService.getAllBooks().stream()
                .map(BookDTO::getBookName)
                .toList();
    }
}
