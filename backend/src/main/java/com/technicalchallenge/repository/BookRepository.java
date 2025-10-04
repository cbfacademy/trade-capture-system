package com.technicalchallenge.repository;

import com.technicalchallenge.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// Repository interface for managing Book entities in the database
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByBookName(String bookName);
}
