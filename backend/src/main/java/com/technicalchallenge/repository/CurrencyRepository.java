package com.technicalchallenge.repository;

import com.technicalchallenge.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// Repository interface for managing Currency entities in the database
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Optional<Currency> findByCurrency(String currency);
}
