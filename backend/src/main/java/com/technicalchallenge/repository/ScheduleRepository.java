package com.technicalchallenge.repository;

import com.technicalchallenge.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// Repository interface for managing Schedule entities in the database
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findBySchedule(String schedule);
}
