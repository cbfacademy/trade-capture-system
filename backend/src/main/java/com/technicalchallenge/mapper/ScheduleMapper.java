package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.ScheduleDTO;
import com.technicalchallenge.model.Schedule;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMapper {// Mapper for Schedule and ScheduleDTO

    // Inject ModelMapper
    @Autowired
    private ModelMapper modelMapper;

    // Convert Schedule entity to ScheduleDTO
    public ScheduleDTO toDto(Schedule entity) {
        return modelMapper.map(entity, ScheduleDTO.class);
    }

    // Convert ScheduleDTO to Schedule entity
    public Schedule toEntity(ScheduleDTO dto) {
        return modelMapper.map(dto, Schedule.class);
    }
}
