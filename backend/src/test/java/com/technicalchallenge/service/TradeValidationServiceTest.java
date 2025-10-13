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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TradeValidationServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CounterpartyRepository counterpartyRepository;

    @Mock
    private ApplicationUserRepository applicationUserRepository;

    private TradeValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new TradeValidationService();
        // inject mocks via reflection (or set package-private fields if in same package)
        // The service uses @Autowired in production; in tests we set fields directly.
        TestUtils.injectField(validationService, "bookRepository", bookRepository);
        TestUtils.injectField(validationService, "counterpartyRepository", counterpartyRepository);
        TestUtils.injectField(validationService, "applicationUserRepository", applicationUserRepository);
    }

    @Test
    void validateTradeBusinessRules_tradeDateMissing() {
        TradeDTO dto = new TradeDTO();
        dto.setTradeLegs(Arrays.asList(leg(1), leg(2)));
        ValidationResult res = validationService.validateTradeBusinessRules(dto);
        assertFalse(res.isValid());
        assertTrue(res.getErrors().stream().anyMatch(s -> s.toLowerCase().contains("trade date")));
    }

    @Test
    void validateTradeBusinessRules_tradeDateTooOld() {
        TradeDTO dto = new TradeDTO();
        dto.setTradeDate(LocalDate.now().minusDays(40));
        dto.setTradeLegs(Arrays.asList(leg(1), leg(2)));
        ValidationResult res = validationService.validateTradeBusinessRules(dto);
        assertFalse(res.isValid());
        assertTrue(res.getErrors().stream().anyMatch(s -> s.toLowerCase().contains("more than 30 days")));
    }

    @Test
    void validateTradeBusinessRules_startBeforeTradeDate() {
        TradeDTO dto = new TradeDTO();
        dto.setTradeDate(LocalDate.now());
        dto.setTradeStartDate(LocalDate.now().minusDays(1));
        dto.setTradeLegs(Arrays.asList(leg(1), leg(2)));
        ValidationResult res = validationService.validateTradeBusinessRules(dto);
        assertFalse(res.isValid());
        assertTrue(res.getErrors().stream().anyMatch(s -> s.toLowerCase().contains("start date")));
    }

    @Test
    void validateTradeBusinessRules_maturityBeforeStart() {
        TradeDTO dto = new TradeDTO();
        dto.setTradeDate(LocalDate.now());
        dto.setTradeStartDate(LocalDate.now().plusDays(1));
        dto.setTradeMaturityDate(LocalDate.now().plusDays(1)); // equals start OK
        // now set maturity before start
        dto.setTradeMaturityDate(LocalDate.now());
        dto.setTradeLegs(Arrays.asList(leg(1), leg(2)));
        ValidationResult res = validationService.validateTradeBusinessRules(dto);
        assertFalse(res.isValid());
        assertTrue(res.getErrors().stream().anyMatch(s -> s.toLowerCase().contains("maturity date")));
    }

    @Test
    void validateTradeLegConsistency_missingLegs() {
        ValidationResult legsRes = validationService.validateTradeLegConsistency(null);
        assertFalse(legsRes.isValid());
        assertTrue(legsRes.getErrors().stream().anyMatch(s -> s.toLowerCase().contains("exactly 2 legs")));
    }

    @Test
    void validateTradeLegConsistency_mismatchMaturity() {
        TradeLegDTO a = leg(1);
        TradeLegDTO b = leg(2);
        a.setMaturityDate(LocalDate.now().plusYears(1));
        b.setMaturityDate(LocalDate.now().plusYears(2));
        ValidationResult res = validationService.validateTradeLegConsistency(Arrays.asList(a, b));
        assertFalse(res.isValid());
        assertTrue(res.getErrors().stream().anyMatch(s -> s.toLowerCase().contains("identical maturity")));
    }

    @Test
    void validateTradeLegConsistency_samePayRec() {
        TradeLegDTO a = leg(1);
        TradeLegDTO b = leg(2);
        a.setPayReceiveFlag("PAY");
        b.setPayReceiveFlag("PAY");
        ValidationResult res = validationService.validateTradeLegConsistency(Arrays.asList(a, b));
        assertFalse(res.isValid());
        assertTrue(res.getErrors().stream().anyMatch(s -> s.toLowerCase().contains("opposite pay/receive")));
    }

    @Test
    void validateTradeLegConsistency_floatingMissingIndex() {
        TradeLegDTO a = leg(1);
        TradeLegDTO b = leg(2);
        a.setLegType("Floating");
        a.setIndexName(null);
        a.setIndexId(null);
        b.setLegType("Fixed");
        b.setRate(0.01);
        ValidationResult res = validationService.validateTradeLegConsistency(Arrays.asList(a, b));
        assertFalse(res.isValid());
        assertTrue(res.getErrors().stream().anyMatch(s -> s.toLowerCase().contains("floating leg")));
    }

    @Test
    void validateTradeLegConsistency_fixedInvalidRate() {
        TradeLegDTO a = leg(1);
        TradeLegDTO b = leg(2);
        a.setLegType("Fixed");
        a.setRate(0.0);
        b.setLegType("Fixed");
        b.setRate(0.01);
        ValidationResult res = validationService.validateTradeLegConsistency(Arrays.asList(a, b));
        assertFalse(res.isValid());
        assertTrue(res.getErrors().stream().anyMatch(s -> s.toLowerCase().contains("fixed leg")));
    }

    private TradeLegDTO leg(int i) {
        TradeLegDTO dto = new TradeLegDTO();
        dto.setNotional(BigDecimal.valueOf(1000000));
        dto.setRate(0.01);
        return dto;
    }
}

