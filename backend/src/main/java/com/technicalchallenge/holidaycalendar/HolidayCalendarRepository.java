package com.technicalchallenge.holidaycalendar;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayCalendarRepository extends JpaRepository<HolidayCalendar, Long> {
    Optional<HolidayCalendar> findByHolidayCalendar(String holidayCalendar);
}
