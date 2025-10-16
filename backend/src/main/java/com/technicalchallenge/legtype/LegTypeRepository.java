package com.technicalchallenge.legtype;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegTypeRepository extends JpaRepository<LegType, Long> {
    Optional<LegType> findByType(String type);
}
