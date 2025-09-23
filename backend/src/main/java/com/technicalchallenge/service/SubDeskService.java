package com.technicalchallenge.service;

import com.technicalchallenge.model.SubDesk;
import com.technicalchallenge.dto.SubDeskDTO;
import com.technicalchallenge.repository.DeskRepository;
import com.technicalchallenge.repository.SubDeskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class SubDeskService {
    private static final Logger logger = LoggerFactory.getLogger(SubDeskService.class);

    @Autowired
    private SubDeskRepository subDeskRepository;

    @Autowired
    private DeskRepository deskRepository;

    public List<SubDesk> getAllSubDesks() {
        logger.info("Retrieving all subdesks");
        return subDeskRepository.findAll();
    }

    public Optional<SubDesk> getSubDeskById(Long id) {
        logger.debug("Retrieving subdesk by id: {}", id);
        return subDeskRepository.findById(id);
    }

    public void populateReferenceDataByName(SubDesk subDesk, SubDeskDTO dto) {
        if (dto.getDeskName() != null && !dto.getDeskName().isBlank()) {
            var desk = deskRepository.findAll().stream()
                .filter(d -> d.getDeskName().equalsIgnoreCase(dto.getDeskName()))
                .findFirst().orElse(null);
            if (desk == null) throw new IllegalArgumentException("Desk '" + dto.getDeskName() + "' does not exist");
            subDesk.setDesk(desk);
        }
        // If deskName is null or blank, do not modify the current desk
    }

    public SubDesk saveSubDesk(SubDesk subDesk, SubDeskDTO dto) {
        logger.info("Saving subdesk: {}", subDesk);
        populateReferenceDataByName(subDesk, dto);
        return subDeskRepository.save(subDesk);
    }

    public void deleteSubDesk(Long id) {
        logger.warn("Deleting subdesk with id: {}", id);
        subDeskRepository.deleteById(id);
    }
}
