package com.technicalchallenge.payrec;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayRecService {
    private static final Logger logger = LoggerFactory.getLogger(PayRecService.class);

    @Autowired
    private PayRecRepository payRecRepository;

    public List<PayRec> findAll() {
        logger.info("Retrieving all pay recs");
        return payRecRepository.findAll();
    }

    public Optional<PayRec> findById(Long id) {
        logger.debug("Retrieving pay rec by id: {}", id);
        return payRecRepository.findById(id);
    }

    public PayRec save(PayRec payRec) {
        logger.info("Saving pay rec: {}", payRec);
        return payRecRepository.save(payRec);
    }

    public void deleteById(Long id) {
        logger.warn("Deleting pay rec with id: {}", id);
        payRecRepository.deleteById(id);
    }
}
