package com.technicalchallenge.repository;

import com.technicalchallenge.model.Index;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IndexRepository extends JpaRepository<Index, Long> {
    Optional<Index> findByIndex(String index);
}
