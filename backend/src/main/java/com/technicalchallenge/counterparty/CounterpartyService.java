package com.technicalchallenge.counterparty;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void deleteCounterparty(Long id) {
        counterpartyRepository.deleteById(id);
    }
}
