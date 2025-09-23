package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.IndexDTO;
import com.technicalchallenge.model.Index;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IndexMapper {
    @Autowired
    private ModelMapper modelMapper;

    public IndexDTO toDto(Index entity) {
        return modelMapper.map(entity, IndexDTO.class);
    }

    public Index toEntity(IndexDTO dto) {
        return modelMapper.map(dto, Index.class);
    }
}
