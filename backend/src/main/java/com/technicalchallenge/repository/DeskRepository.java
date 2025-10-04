package com.technicalchallenge.repository;

import com.technicalchallenge.model.Desk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
// Repository interface for managing Desk entities in the database
public interface DeskRepository extends JpaRepository<Desk, Long> {}
