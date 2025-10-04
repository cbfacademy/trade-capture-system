package com.technicalchallenge.repository;

import com.technicalchallenge.model.Counterparty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// Repository interface for managing Counterparty entities in the database
public interface CounterpartyRepository extends JpaRepository<Counterparty, Long> {
    Optional<Counterparty> findByName(String name);
}
