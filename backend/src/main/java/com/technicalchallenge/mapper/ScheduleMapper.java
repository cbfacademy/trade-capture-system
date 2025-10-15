package com.technicalchallenge.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.technicalchallenge.dto.ScheduleDTO;
import com.technicalchallenge.model.Schedule;

@Component
public class ScheduleMapper {
    @Autowired
    private ModelMapper modelMapper;

    public ScheduleDTO toDto(Schedule entity) {
        return modelMapper.map(entity, ScheduleDTO.class);
    }

    public Schedule toEntity(ScheduleDTO dto) {
        return modelMapper.map(dto, Schedule.class);
    }
}
