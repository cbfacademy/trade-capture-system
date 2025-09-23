package com.technicalchallenge.repository;

import com.technicalchallenge.model.LegType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LegTypeRepository extends JpaRepository<LegType, Long> {
    Optional<LegType> findByType(String type);
}
