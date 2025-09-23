package com.technicalchallenge.service;

import com.technicalchallenge.model.LegType;
import com.technicalchallenge.repository.LegTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class LegTypeService {
    private static final Logger logger = LoggerFactory.getLogger(LegTypeService.class);

    @Autowired
    private LegTypeRepository legTypeRepository;

    public List<LegType> findAll() {
        logger.info("Retrieving all leg types");
        return legTypeRepository.findAll();
    }

    public Optional<LegType> findById(Long id) {
        logger.debug("Retrieving leg type by id: {}", id);
        return legTypeRepository.findById(id);
    }

    public LegType save(LegType legType) {
        logger.info("Saving leg type: {}", legType);
        return legTypeRepository.save(legType);
    }

    public void deleteById(Long id) {
        logger.warn("Deleting leg type with id: {}", id);
        legTypeRepository.deleteById(id);
    }
}
