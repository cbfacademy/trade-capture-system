package com.technicalchallenge.service;

import com.technicalchallenge.dto.TradeDTO;
import com.technicalchallenge.dto.TradeLegDTO;
import com.technicalchallenge.dto.ValidationResult;
import com.technicalchallenge.model.Book;
import com.technicalchallenge.model.Counterparty;
import com.technicalchallenge.model.ApplicationUser;
import com.technicalchallenge.repository.BookRepository;
import com.technicalchallenge.repository.CounterpartyRepository;
import com.technicalchallenge.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service that encapsulates business validation rules and user privilege checks.
 *
 * CHANGES:
 * - Use isActive() (boolean) for Book/Counterparty (models define boolean active -> Lombok creates isActive()).
 * - Extract role/user type from ApplicationUser.userProfile.userType (null-safe).
 * - Keeps validation logic small and testable.
 */
@Service
public class TradeValidationService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CounterpartyRepository counterpartyRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    public ValidationResult validateTradeBusinessRules(TradeDTO tradeDTO) {
        ValidationResult result = new ValidationResult();
        LocalDate today = LocalDate.now();

        if (tradeDTO.getTradeDate() == null) {
            result.addError("Trade date is required");
        } else {
            if (tradeDTO.getTradeDate().isBefore(today.minusDays(30))) {
                result.addError("Trade date cannot be more than 30 days in the past");
            }
        }

        if (tradeDTO.getTradeStartDate() != null && tradeDTO.getTradeDate() != null) {
            if (tradeDTO.getTradeStartDate().isBefore(tradeDTO.getTradeDate())) {
                result.addError("Trade start date cannot be before trade date");
            }
        }

        if (tradeDTO.getTradeMaturityDate() != null && tradeDTO.getTradeStartDate() != null) {
            if (tradeDTO.getTradeMaturityDate().isBefore(tradeDTO.getTradeStartDate())) {
                result.addError("Trade maturity date cannot be before start date");
            }
        }

        // Book required check (CHANGED: use isActive() for boolean)
        if (tradeDTO.getBookId() != null) {
            Optional<Book> b = bookRepository.findById(tradeDTO.getBookId());
            if (b.isEmpty()) result.addError("Book not found");
            else if (!b.get().isActive()) result.addError("Book is not active");
        } else if (tradeDTO.getBookName() == null) {
            result.addError("Book is required");
        }

        // Counterparty required check (CHANGED: use isActive())
        if (tradeDTO.getCounterpartyId() != null) {
            Optional<Counterparty> c = counterpartyRepository.findById(tradeDTO.getCounterpartyId());
            if (c.isEmpty()) result.addError("Counterparty not found");
            else if (!c.get().isActive()) result.addError("Counterparty is not active");
        } else if (tradeDTO.getCounterpartyName() == null) {
            result.addError("Counterparty is required");
        }

        if (tradeDTO.getTradeLegs() != null) {
            ValidationResult legsRes = validateTradeLegConsistency(tradeDTO.getTradeLegs());
            if (!legsRes.isValid()) {
                legsRes.getErrors().forEach(result::addError);
            }
        } else {
            result.addError("Trade legs are required");
        }

        return result;
    }

    public ValidationResult validateTradeLegConsistency(List<TradeLegDTO> legs) {
        ValidationResult result = new ValidationResult();
        if (legs == null || legs.size() != 2) {
            result.addError("Exactly 2 legs are required");
            return result;
        }
        TradeLegDTO a = legs.get(0);
        TradeLegDTO b = legs.get(1);

        if (a.getMaturityDate() != null && b.getMaturityDate() != null) {
            if (!a.getMaturityDate().equals(b.getMaturityDate())) {
                result.addError("Both legs must have identical maturity dates");
            }
        }

        if (a.getPayReceiveFlag() != null && b.getPayReceiveFlag() != null) {
            if (a.getPayReceiveFlag().equalsIgnoreCase(b.getPayReceiveFlag())) {
                result.addError("Legs must have opposite pay/receive flags");
            }
        }

        if ("Floating".equalsIgnoreCase(a.getLegType()) && (a.getIndexName() == null && a.getIndexId() == null)) {
            result.addError("Floating leg must have an index specified");
        }
        if ("Floating".equalsIgnoreCase(b.getLegType()) && (b.getIndexName() == null && b.getIndexId() == null)) {
            result.addError("Floating leg must have an index specified");
        }

        if ("Fixed".equalsIgnoreCase(a.getLegType()) && (a.getRate() == null || a.getRate() <= 0.0)) {
            result.addError("Fixed leg must have a valid rate");
        }
        if ("Fixed".equalsIgnoreCase(b.getLegType()) && (b.getRate() == null || b.getRate() <= 0.0)) {
            result.addError("Fixed leg must have a valid rate");
        }

        return result;
    }

    /**
     * Basic user privilege check.
     * CHANGED: ApplicationUser stores userProfile.userType, so read that (null-safe).
     */
    public boolean validateUserPrivileges(String userId, String operation, TradeDTO tradeDTO) {
        if (userId == null) return false;
        Optional<ApplicationUser> opt = applicationUserRepository.findByLoginId(userId);
        if (opt.isEmpty()) return false;
        ApplicationUser user = opt.get();

        // Extract role/user type via userProfile.userType (null-safe)
        String role = "";
        if (user.getUserProfile() != null && user.getUserProfile().getUserType() != null) {
            role = user.getUserProfile().getUserType();
        }

        switch (operation.toLowerCase()) {
            case "create":
                return role.equalsIgnoreCase("TRADER") || role.equalsIgnoreCase("SALES");
            case "amend":
                return role.equalsIgnoreCase("TRADER") || role.equalsIgnoreCase("SALES") || role.equalsIgnoreCase("MIDDLE_OFFICE");
            case "terminate":
            case "cancel":
                return role.equalsIgnoreCase("TRADER");
            case "view":
                return true;
            default:
                return false;
        }
    }
}
