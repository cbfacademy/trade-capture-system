package com.technicalchallenge.repository;

import com.technicalchallenge.model.SubDesk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// Repository interface for managing SubDesk entities in the database
public interface SubDeskRepository extends JpaRepository<SubDesk, Long> {}
