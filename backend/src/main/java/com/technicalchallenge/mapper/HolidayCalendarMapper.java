package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.HolidayCalendarDTO;
import com.technicalchallenge.model.HolidayCalendar;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HolidayCalendarMapper {// Mapper for HolidayCalendar and HolidayCalendarDTO
    @Autowired
    private ModelMapper modelMapper;

    // Convert HolidayCalendar entity to HolidayCalendarDTO
    public HolidayCalendarDTO toDto(HolidayCalendar entity) {
        return modelMapper.map(entity, HolidayCalendarDTO.class);
    }

    // Convert HolidayCalendarDTO to HolidayCalendar entity
    public HolidayCalendar toEntity(HolidayCalendarDTO dto) {
        return modelMapper.map(dto, HolidayCalendar.class);
    }
}
