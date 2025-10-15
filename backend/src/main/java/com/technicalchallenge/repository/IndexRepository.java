package com.technicalchallenge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technicalchallenge.model.Index;

@Repository
public interface IndexRepository extends JpaRepository<Index, Long> {
    Optional<Index> findByIndex(String index);
}
