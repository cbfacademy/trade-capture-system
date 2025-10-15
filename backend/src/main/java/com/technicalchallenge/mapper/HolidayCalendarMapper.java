package com.technicalchallenge.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.technicalchallenge.dto.HolidayCalendarDTO;
import com.technicalchallenge.model.HolidayCalendar;

@Component
public class HolidayCalendarMapper {
    @Autowired
    private ModelMapper modelMapper;

    public HolidayCalendarDTO toDto(HolidayCalendar entity) {
        return modelMapper.map(entity, HolidayCalendarDTO.class);
    }

    public HolidayCalendar toEntity(HolidayCalendarDTO dto) {
        return modelMapper.map(dto, HolidayCalendar.class);
    }
}
