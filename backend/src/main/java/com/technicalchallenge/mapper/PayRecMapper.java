package com.technicalchallenge.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.technicalchallenge.dto.PayRecDTO;
import com.technicalchallenge.model.PayRec;

@Component
public class PayRecMapper {
    @Autowired
    private ModelMapper modelMapper;

    public PayRecDTO toDto(PayRec entity) {
        return modelMapper.map(entity, PayRecDTO.class);
    }

    public PayRec toEntity(PayRecDTO dto) {
        return modelMapper.map(dto, PayRec.class);
    }
}
