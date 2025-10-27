package com.technicalchallenge.service;

import com.technicalchallenge.dto.TradeDTO;
import com.technicalchallenge.dto.TradeLegDTO;
import com.technicalchallenge.dto.ValidationResult;
import com.technicalchallenge.model.*;
import com.technicalchallenge.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TradeValidationService {

    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private CounterpartyRepository counterpartyRepository;
    
    @Autowired
    private TradeRepository tradeRepository;

    public ValidationResult validateTradeCreation(TradeDTO tradeDTO, String userLoginId) {
        ValidationResult result = new ValidationResult();
        
        validateRequiredFields(tradeDTO, result);
        validateDates(tradeDTO, result);
        if (tradeDTO.getTradeLegs() != null) {
            validateTradeLegs(tradeDTO.getTradeLegs(), result);
        }
        
        return result;
    }

    public ValidationResult validateTradeAmendment(TradeDTO tradeDTO, String userLoginId) {
        ValidationResult result = new ValidationResult();
        
        if (tradeDTO.getId() != null) {
            Optional<Trade> trade = tradeRepository.findById(tradeDTO.getId());
            if (trade.isEmpty()) {
                result.addError("Trade not found");
                return result;
            }
            
            if (trade.get().getTradeStatus() != null) {
                Long statusId = trade.get().getTradeStatus().getId();
                if (!statusId.equals(1000L) && !statusId.equals(1001L) && !statusId.equals(1004L)) {
                    result.addError("Trade status does not allow amendments");
                    return result;
                }
            }
        }
        
        validateRequiredFields(tradeDTO, result);
        validateDates(tradeDTO, result);
        if (tradeDTO.getTradeLegs() != null) {
            validateTradeLegs(tradeDTO.getTradeLegs(), result);
        }
        
        return result;
    }

    public ValidationResult validateTradeRead(String userLoginId) {
        ValidationResult result = new ValidationResult();
        // Basic validation - always allow read for simplicity
        return result;
    }

    private void validateRequiredFields(TradeDTO tradeDTO, ValidationResult result) {
        if (tradeDTO.getBookId() == null) {
            result.addError("Book is required");
        } else {
            Optional<Book> book = bookRepository.findById(tradeDTO.getBookId());
            if (book.isEmpty() || !book.get().isActive()) {
                result.addError("Book not found or inactive");
            }
        }
        
        if (tradeDTO.getCounterpartyId() == null) {
            result.addError("Counterparty is required");
        } else {
            Optional<Counterparty> counterparty = counterpartyRepository.findById(tradeDTO.getCounterpartyId());
            if (counterparty.isEmpty() || !counterparty.get().isActive()) {
                result.addError("Counterparty not found or inactive");
            }
        }
        
        if (tradeDTO.getTradeTypeId() == null) {
            result.addError("Trade type is required");
        }
        
        if (tradeDTO.getTradeId() != null) {
            List<Trade> existingTrades = tradeRepository.findByTradeId(tradeDTO.getTradeId());
            boolean isDuplicate = existingTrades.stream()
                .anyMatch(trade -> !trade.getId().equals(tradeDTO.getId()));
            if (isDuplicate) {
                result.addError("Trade ID already exists");
            }
        }
    }

    private void validateDates(TradeDTO tradeDTO, ValidationResult result) {
        if (tradeDTO.getTradeDate() == null) {
            result.addError("Trade date is required");
        }
        
        if (tradeDTO.getTradeStartDate() == null) {
            result.addError("Start date is required");
        }
        
        if (tradeDTO.getTradeMaturityDate() == null) {
            result.addError("Maturity date is required");
        }
        
        if (tradeDTO.getTradeStartDate() != null && tradeDTO.getTradeMaturityDate() != null) {
            if (tradeDTO.getTradeMaturityDate().isBefore(tradeDTO.getTradeStartDate())) {
                result.addError("Maturity date cannot be before start date");
            }
        }
        
        if (tradeDTO.getTradeDate() != null && tradeDTO.getTradeDate().isBefore(LocalDate.now().minusDays(3))) {
            result.addWarning("Trade date is more than 3 days old");
        }
    }

    private void validateTradeLegs(List<TradeLegDTO> legs, ValidationResult result) {
        if (legs.isEmpty()) {
            result.addError("At least one trade leg is required");
            return;
        }
        
        for (int i = 0; i < legs.size(); i++) {
            TradeLegDTO leg = legs.get(i);
            String prefix = "Leg " + (i + 1) + ": ";
            
            if (leg.getNotional() == null || leg.getNotional().compareTo(BigDecimal.ZERO) <= 0) {
                result.addError(prefix + "Notional must be greater than zero");
            }
            
            if (leg.getCurrencyId() == null) {
                result.addError(prefix + "Currency is required");
            }
            
            if (leg.getPayRecId() == null) {
                result.addError(prefix + "Pay/Receive is required");
            }
            
            if (leg.getNotional() != null && leg.getNotional().compareTo(new BigDecimal("100000000")) > 0) {
                result.addWarning(prefix + "Large notional detected");
            }
        }
        
        // Cross-leg validation for multi-leg trades
        if (legs.size() > 1) {
            long payCount = legs.stream().filter(leg -> Long.valueOf(1000L).equals(leg.getPayRecId())).count();
            long receiveCount = legs.stream().filter(leg -> Long.valueOf(1001L).equals(leg.getPayRecId())).count();
            
            if (payCount == 0 || receiveCount == 0) {
                result.addWarning("Trade should have both pay and receive legs")
            }
        }
    }
}