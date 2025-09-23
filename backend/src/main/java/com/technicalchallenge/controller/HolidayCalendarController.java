package com.technicalchallenge.controller;

import com.technicalchallenge.dto.HolidayCalendarDTO;
import com.technicalchallenge.mapper.HolidayCalendarMapper;
import com.technicalchallenge.model.HolidayCalendar;
import com.technicalchallenge.service.HolidayCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/holidayCalendars")
public class HolidayCalendarController {
    private static final Logger logger = LoggerFactory.getLogger(HolidayCalendarController.class);

    @Autowired
    private HolidayCalendarService holidayCalendarService;

    @Autowired
    private HolidayCalendarMapper holidayCalendarMapper;

    @GetMapping
    public List<HolidayCalendarDTO> getAll() {
        logger.info("Fetching all holiday calendars");
        return holidayCalendarService.findAll().stream()
                .map(holidayCalendarMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HolidayCalendarDTO> getById(@PathVariable Long id) {
        logger.debug("Fetching holiday calendar by id: {}", id);
        return holidayCalendarService.findById(id)
                .map(holidayCalendarMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public HolidayCalendarDTO create(@RequestBody HolidayCalendarDTO holidayCalendarDTO) {
        logger.info("Creating new holiday calendar: {}", holidayCalendarDTO);
        HolidayCalendar entity = holidayCalendarMapper.toEntity(holidayCalendarDTO);
        return holidayCalendarMapper.toDto(holidayCalendarService.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HolidayCalendarDTO> update(@PathVariable Long id, @RequestBody HolidayCalendarDTO holidayCalendarDTO) {
        return holidayCalendarService.findById(id)
                .map(existing -> {
                    HolidayCalendar entity = holidayCalendarMapper.toEntity(holidayCalendarDTO);
                    entity.setId(id);
                    return ResponseEntity.ok(holidayCalendarMapper.toDto(holidayCalendarService.save(entity)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.warn("Deleting holiday calendar with id: {}", id);
        if (holidayCalendarService.findById(id).isPresent()) {
            holidayCalendarService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/values")
    public List<String> getAllHolidayCalendarValues() {
        logger.info("Fetching all holiday calendar values");
        return holidayCalendarService.findAll().stream()
                .map(HolidayCalendar::getHolidayCalendar)
                .toList();
    }
}
