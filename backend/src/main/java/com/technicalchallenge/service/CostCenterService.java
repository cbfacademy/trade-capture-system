package com.technicalchallenge.service;

import com.technicalchallenge.model.CostCenter;
import com.technicalchallenge.dto.CostCenterDTO;
import com.technicalchallenge.repository.CostCenterRepository;
import com.technicalchallenge.repository.SubDeskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class CostCenterService {
    private static final Logger logger = LoggerFactory.getLogger(CostCenterService.class);

    @Autowired
    private CostCenterRepository costCenterRepository;

    @Autowired
    private SubDeskRepository subDeskRepository;

    public List<CostCenter> getAllCostCenters() {
        logger.info("Retrieving all cost centers");
        return costCenterRepository.findAll();
    }

    public Optional<CostCenter> getCostCenterById(Long id) {
        logger.debug("Retrieving cost center by id: {}", id);
        return costCenterRepository.findById(id);
    }

    public void populateReferenceDataByName(CostCenter costCenter, CostCenterDTO dto) {
        if (dto.getSubDeskName() != null && !dto.getSubDeskName().isBlank()) {
            var subDesk = subDeskRepository.findAll().stream()
                .filter(s -> s.getSubdeskName().equalsIgnoreCase(dto.getSubDeskName()))
                .findFirst().orElse(null);
            if (subDesk == null) throw new IllegalArgumentException("SubDesk '" + dto.getSubDeskName() + "' does not exist");
            costCenter.setSubDesk(subDesk);
        }
        // If subDeskName is null or blank, do not modify the current subDesk
    }

    public CostCenter saveCostCenter(CostCenter costCenter, CostCenterDTO dto) {
        logger.info("Saving cost center: {}", costCenter);
        populateReferenceDataByName(costCenter, dto);
        return costCenterRepository.save(costCenter);
    }

    public void deleteCostCenter(Long id) {
        logger.warn("Deleting cost center with id: {}", id);
        costCenterRepository.deleteById(id);
    }
}
