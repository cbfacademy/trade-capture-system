package com.technicalchallenge.service;

import com.technicalchallenge.model.HolidayCalendar;
import com.technicalchallenge.repository.HolidayCalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class HolidayCalendarService {
    private static final Logger logger = LoggerFactory.getLogger(HolidayCalendarService.class);

    @Autowired
    private HolidayCalendarRepository holidayCalendarRepository;

    public List<HolidayCalendar> findAll() {
        logger.info("Retrieving all holiday calendars");
        return holidayCalendarRepository.findAll();
    }

    public Optional<HolidayCalendar> findById(Long id) {
        logger.debug("Retrieving holiday calendar by id: {}", id);
        return holidayCalendarRepository.findById(id);
    }

    public HolidayCalendar save(HolidayCalendar holidayCalendar) {
        logger.info("Saving holiday calendar: {}", holidayCalendar);
        return holidayCalendarRepository.save(holidayCalendar);
    }

    public void deleteById(Long id) {
        logger.warn("Deleting holiday calendar with id: {}", id);
        holidayCalendarRepository.deleteById(id);
    }
}
