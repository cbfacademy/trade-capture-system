package com.technicalchallenge.service;

import com.technicalchallenge.model.Index;
import com.technicalchallenge.repository.IndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class IndexService {// Service class for managing Index entities

    // Logger for logging information and errors
    private static final Logger logger = LoggerFactory.getLogger(IndexService.class);

    // Injected repository
    @Autowired
    private IndexRepository indexRepository;

    // CRUD operations
    public List<Index> findAll() {
        logger.info("Retrieving all indexes");
        return indexRepository.findAll();
    }

    public Optional<Index> findById(Long id) {
        logger.debug("Retrieving index by id: {}", id);
        return indexRepository.findById(id);
    }

    public Index save(Index index) {
        logger.info("Saving index: {}", index);
        return indexRepository.save(index);
    }

    public void deleteById(Long id) {
        logger.warn("Deleting index with id: {}", id);
        indexRepository.deleteById(id);
    }
}
