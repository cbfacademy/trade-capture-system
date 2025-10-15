package com.technicalchallenge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technicalchallenge.model.LegType;

@Repository
public interface LegTypeRepository extends JpaRepository<LegType, Long> {
    Optional<LegType> findByType(String type);
}
