package com.technicalchallenge.service;

import com.technicalchallenge.model.Counterparty;
import com.technicalchallenge.repository.CounterpartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CounterpartyService {
    @Autowired
    private CounterpartyRepository counterpartyRepository;

    public List<Counterparty> getAllCounterparties() {
        return counterpartyRepository.findAll();
    }

    public Optional<Counterparty> getCounterpartyById(Long id) {
        return counterpartyRepository.findById(id);
    }

    public Counterparty saveCounterparty(Counterparty counterparty) {
        return counterpartyRepository.save(counterparty);
    }
    public Counterparty updateCounterparty(Long id, Counterparty updatedData) {
        Optional<Counterparty> existingCounterparty = counterpartyRepository.findById(id);

        if (existingCounterparty.isPresent()) {
            Counterparty counterparty = existingCounterparty.get();
            counterparty.setName(updatedData.getName()); // update fields as needed
            // add more fields here if your Counterparty has more
            return counterpartyRepository.save(counterparty);
        } else {
            throw new RuntimeException("Counterparty not found with id: " + id);
        }
    }

    public void deleteCounterparty(Long id) {
        counterpartyRepository.deleteById(id);
    }
}
