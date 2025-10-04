package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.CostCenterDTO;
import com.technicalchallenge.model.CostCenter;
import com.technicalchallenge.repository.SubDeskRepository;
import com.technicalchallenge.model.SubDesk;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CostCenterMapper {// Mapper for CostCenter and CostCenterDTO

    // Inject ModelMapper and SubDeskRepository
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SubDeskRepository subDeskRepository;

    // Convert CostCenter entity to CostCenterDTO
    public CostCenterDTO toDto(CostCenter entity) {
        CostCenterDTO dto = modelMapper.map(entity, CostCenterDTO.class);
        dto.setSubDeskName(entity.getSubDesk() != null ? entity.getSubDesk().getSubdeskName() : null);
        return dto;
    }

    // Convert CostCenterDTO to CostCenter entity
    public CostCenter toEntity(CostCenterDTO dto) {
        CostCenter entity = modelMapper.map(dto, CostCenter.class);
        if (dto.getSubDeskName() != null) {
            SubDesk subDesk = subDeskRepository.findAll().stream()
                .filter(sd -> dto.getSubDeskName().equals(sd.getSubdeskName()))
                .findFirst().orElse(null);
            entity.setSubDesk(subDesk);
        }
        return entity;
    }
}
