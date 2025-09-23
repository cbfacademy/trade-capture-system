package com.technicalchallenge.service;

import com.technicalchallenge.model.Desk;
import com.technicalchallenge.repository.DeskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class DeskService {
    private static final Logger logger = LoggerFactory.getLogger(DeskService.class);

    @Autowired
    private DeskRepository deskRepository;

    public List<Desk> getAllDesks() {
        logger.info("Retrieving all desks");
        return deskRepository.findAll();
    }

    public Optional<Desk> getDeskById(Long id) {
        logger.debug("Retrieving desk by id: {}", id);
        return deskRepository.findById(id);
    }

    public Desk saveDesk(Desk desk) {
        logger.info("Saving desk: {}", desk);
        return deskRepository.save(desk);
    }

    public void deleteDesk(Long id) {
        logger.warn("Deleting desk with id: {}", id);
        deskRepository.deleteById(id);
    }
}
