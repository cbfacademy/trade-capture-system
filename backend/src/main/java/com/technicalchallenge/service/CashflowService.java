package com.technicalchallenge.service;

import com.technicalchallenge.dto.CashflowDTO;
import com.technicalchallenge.model.Cashflow;
import com.technicalchallenge.repository.CashflowRepository;
import com.technicalchallenge.repository.BusinessDayConventionRepository;
import com.technicalchallenge.repository.LegTypeRepository;
import com.technicalchallenge.repository.PayRecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class CashflowService {
    private static final Logger logger = LoggerFactory.getLogger(CashflowService.class);
    @Autowired
    private CashflowRepository cashflowRepository;
    @Autowired
    private PayRecRepository payRecRepository;
    @Autowired
    private LegTypeRepository legTypeRepository;
    @Autowired
    private BusinessDayConventionRepository businessDayConventionRepository;

    public List<Cashflow> getAllCashflows() {
        logger.info("Retrieving all cashflows");
        return cashflowRepository.findAll();
    }

    public Optional<Cashflow> getCashflowById(Long id) {
        logger.debug("Retrieving cashflow by id: {}", id);
        return cashflowRepository.findById(id);
    }

    public Cashflow saveCashflow(Cashflow cashflow) {
        logger.info("Saving cashflow: {}", cashflow);
        // Business logic: value must be positive, valueDate required (enforced in controller)
        if (cashflow.getPaymentValue() == null || cashflow.getPaymentValue().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Cashflow value must be greater than 0");
        }
        if (cashflow.getValueDate() == null) {
            throw new IllegalArgumentException("Cashflow valueDate is required");
        }
        // Ensure Cashflow is saved with related entities set, not just IDs
        return cashflowRepository.save(cashflow);
    }

    public void deleteCashflow(Long id) {
        logger.warn("Deleting cashflow with id: {}", id);
        cashflowRepository.deleteById(id);
    }

    public void populateReferenceDataByName(Cashflow cashflow, CashflowDTO dto) {
        if (dto.getPayRec() != null) {
            cashflow.setPayRec(payRecRepository.findAll().stream()
                .filter(p -> p.getPayRec().equalsIgnoreCase(dto.getPayRec()))
                .findFirst().orElse(null));
        }
        if (dto.getPaymentType() != null) {
            cashflow.setPaymentType(legTypeRepository.findAll().stream()
                .filter(l -> l.getType().equalsIgnoreCase(dto.getPaymentType()))
                .findFirst().orElse(null));
        }
        if (dto.getPaymentBusinessDayConvention() != null) {
            cashflow.setPaymentBusinessDayConvention(businessDayConventionRepository.findAll().stream()
                .filter(b -> b.getBdc().equalsIgnoreCase(dto.getPaymentBusinessDayConvention()))
                .findFirst().orElse(null));
        }
    }
}
