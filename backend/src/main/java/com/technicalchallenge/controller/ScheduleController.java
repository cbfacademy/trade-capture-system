package com.technicalchallenge.controller;

import com.technicalchallenge.dto.ScheduleDTO;
import com.technicalchallenge.mapper.ScheduleMapper;
import com.technicalchallenge.model.Schedule;
import com.technicalchallenge.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {// Purpose of this controller - to manage schedules, including creating, retrieving, updating, and deleting them.
    private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    // Injecting ScheduleService to handle business logic
    @Autowired
    private ScheduleService scheduleService;

    // Mapper to convert between entity and DTO
    @Autowired
    private ScheduleMapper scheduleMapper;

    // Endpoint to retrieve all schedules
    @GetMapping
    public List<ScheduleDTO> getAll() {
        logger.info("Fetching all schedules");
        return scheduleService.findAll().stream()
                .map(scheduleMapper::toDto)
                .toList();
    }

    // Endpoint to retrieve a schedule by its ID
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO> getById(@PathVariable Long id) {
        logger.debug("Fetching schedule by id: {}", id);
        return scheduleService.findById(id)
                .map(scheduleMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to create a new schedule
    @PostMapping
    public ScheduleDTO create(@RequestBody ScheduleDTO scheduleDTO) {
        logger.info("Creating new schedule: {}", scheduleDTO);
        Schedule entity = scheduleMapper.toEntity(scheduleDTO);
        return scheduleMapper.toDto(scheduleService.save(entity));
    }

    // Endpoint to update an existing schedule
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDTO> update(@PathVariable Long id, @RequestBody ScheduleDTO scheduleDTO) {
        return scheduleService.findById(id)
                .map(existing -> {
                    Schedule entity = scheduleMapper.toEntity(scheduleDTO);
                    entity.setId(id);
                    return ResponseEntity.ok(scheduleMapper.toDto(scheduleService.save(entity)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to delete a schedule by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.warn("Deleting schedule with id: {}", id);
        if (scheduleService.findById(id).isPresent()) {
            scheduleService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint to retrieve all schedule values
    @GetMapping("/values")
    public List<String> getAllScheduleValues() {
        logger.info("Fetching all schedule values");
        return scheduleService.findAll().stream()
                .map(Schedule::getSchedule)
                .toList();
    }
}
