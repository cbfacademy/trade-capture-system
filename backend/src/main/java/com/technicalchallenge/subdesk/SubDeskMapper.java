package com.technicalchallenge.subdesk;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.technicalchallenge.desk.Desk;
import com.technicalchallenge.desk.DeskRepository;

@Component
public class SubDeskMapper {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DeskRepository deskRepository;

    public SubDeskDTO toDto(SubDesk entity) {
        SubDeskDTO dto = modelMapper.map(entity, SubDeskDTO.class);
        dto.setDeskName(entity.getDesk() != null ? entity.getDesk().getDeskName() : null);
        return dto;
    }

    public SubDesk toEntity(SubDeskDTO dto) {
        SubDesk entity = modelMapper.map(dto, SubDesk.class);
        if (dto.getDeskName() != null) {
            Desk desk = deskRepository.findAll().stream()
                .filter(d -> dto.getDeskName().equals(d.getDeskName()))
                .findFirst().orElse(null);
            entity.setDesk(desk);
        }
        return entity;
    }
}
