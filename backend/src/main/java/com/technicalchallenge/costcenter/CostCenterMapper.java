package com.technicalchallenge.costcenter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.technicalchallenge.subdesk.SubDesk;
import com.technicalchallenge.subdesk.SubDeskRepository;

@Component
public class CostCenterMapper {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SubDeskRepository subDeskRepository;

    public CostCenterDTO toDto(CostCenter entity) {
        CostCenterDTO dto = modelMapper.map(entity, CostCenterDTO.class);
        dto.setSubDeskName(entity.getSubDesk() != null ? entity.getSubDesk().getSubdeskName() : null);
        return dto;
    }

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
