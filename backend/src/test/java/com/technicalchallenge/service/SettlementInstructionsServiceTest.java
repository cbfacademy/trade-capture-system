package com.technicalchallenge.service;

import com.technicalchallenge.dto.AdditionalInfoDTO;
import com.technicalchallenge.model.AdditionalInfo;
import com.technicalchallenge.repository.AdditionalInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SettlementInstructionsServiceTest {

    @Mock
    private AdditionalInfoRepository additionalInfoRepository;

    @Mock
    private AdditionalInfoService additionalInfoService;

    @InjectMocks
    private SettlementInstructionsService settlementInstructionsService;

    private static final Long TRADE_ID = 12345L;
    private static final String SETTLEMENT_INSTRUCTIONS = "Wire to Bank XYZ, Account 12345";

    @BeforeEach
    void setUp() {
        reset(additionalInfoRepository, additionalInfoService);
    }

    @Test
    void getSettlementInstructions_ShouldReturnInstructions_WhenExists() {
        AdditionalInfo mockInfo = createMockAdditionalInfo(TRADE_ID, SETTLEMENT_INSTRUCTIONS);
        when(additionalInfoRepository.findActiveByEntityTypeAndEntityIdAndFieldName(
                "TRADE", TRADE_ID, "SETTLEMENT_INSTRUCTIONS"))
                .thenReturn(mockInfo);

        String result = settlementInstructionsService.getSettlementInstructions(TRADE_ID);

        assertEquals(SETTLEMENT_INSTRUCTIONS, result);
        verify(additionalInfoRepository).findActiveByEntityTypeAndEntityIdAndFieldName(
                "TRADE", TRADE_ID, "SETTLEMENT_INSTRUCTIONS");
    }

    @Test
    void getSettlementInstructions_ShouldReturnNull_WhenNotExists() {
        when(additionalInfoRepository.findActiveByEntityTypeAndEntityIdAndFieldName(
                "TRADE", TRADE_ID, "SETTLEMENT_INSTRUCTIONS"))
                .thenReturn(null);

        String result = settlementInstructionsService.getSettlementInstructions(TRADE_ID);

        assertNull(result);
        verify(additionalInfoRepository).findActiveByEntityTypeAndEntityIdAndFieldName(
                "TRADE", TRADE_ID, "SETTLEMENT_INSTRUCTIONS");
    }

    @Test
    void updateSettlementInstructions_ShouldCallAdditionalInfoService_WithValidInstructions() {
        String instructions = "New settlement instructions";

        settlementInstructionsService.updateSettlementInstructions(TRADE_ID, instructions);

        verify(additionalInfoService).updateAdditionalInfo(argThat(dto ->
                "TRADE".equals(dto.getEntityType()) &&
                TRADE_ID.equals(dto.getEntityId()) &&
                "SETTLEMENT_INSTRUCTIONS".equals(dto.getFieldName()) &&
                instructions.equals(dto.getFieldValue()) &&
                "STRING".equals(dto.getFieldType())
        ));
    }

    @Test
    void updateSettlementInstructions_ShouldTrimWhitespace_BeforeUpdate() {
        String instructionsWithWhitespace = "  " + SETTLEMENT_INSTRUCTIONS + "  ";

        settlementInstructionsService.updateSettlementInstructions(TRADE_ID, instructionsWithWhitespace);

        verify(additionalInfoService).updateAdditionalInfo(argThat(dto ->
                SETTLEMENT_INSTRUCTIONS.equals(dto.getFieldValue())
        ));
    }

    @Test
    void updateSettlementInstructions_ShouldCallRemove_WhenNullInstructions() {
        settlementInstructionsService.updateSettlementInstructions(TRADE_ID, null);

        verify(additionalInfoService).removeAdditionalInfo("TRADE", TRADE_ID, "SETTLEMENT_INSTRUCTIONS");
        verify(additionalInfoService, never()).updateAdditionalInfo(any());
    }

    @Test
    void updateSettlementInstructions_ShouldCallRemove_WhenEmptyInstructions() {
        settlementInstructionsService.updateSettlementInstructions(TRADE_ID, "   ");

        verify(additionalInfoService).removeAdditionalInfo("TRADE", TRADE_ID, "SETTLEMENT_INSTRUCTIONS");
        verify(additionalInfoService, never()).updateAdditionalInfo(any());
    }

    @Test
    void removeSettlementInstructions_ShouldCallAdditionalInfoService() {
        settlementInstructionsService.removeSettlementInstructions(TRADE_ID);

        verify(additionalInfoService).removeAdditionalInfo("TRADE", TRADE_ID, "SETTLEMENT_INSTRUCTIONS");
    }

    @Test
    void findTradeIdsBySettlementInstructions_ShouldReturnTradeIds_WhenMatchesFound() {
        String searchText = "Bank XYZ";
        AdditionalInfo info1 = createMockAdditionalInfo(123L, "Wire to Bank XYZ, Account 12345");
        AdditionalInfo info2 = createMockAdditionalInfo(456L, "Transfer to Bank XYZ, Swift ABC");
        
        when(additionalInfoRepository.findByEntityTypeAndFieldNameContaining(
                "TRADE", "SETTLEMENT_INSTRUCTIONS", searchText))
                .thenReturn(Arrays.asList(info1, info2));

        List<Long> result = settlementInstructionsService.findTradeIdsBySettlementInstructions(searchText);

        assertEquals(2, result.size());
        assertTrue(result.contains(123L));
        assertTrue(result.contains(456L));
        verify(additionalInfoRepository).findByEntityTypeAndFieldNameContaining(
                "TRADE", "SETTLEMENT_INSTRUCTIONS", searchText);
    }

    @Test
    void findTradeIdsBySettlementInstructions_ShouldReturnEmptyList_WhenNoMatches() {
        String searchText = "NonExistentBank";
        when(additionalInfoRepository.findByEntityTypeAndFieldNameContaining(
                "TRADE", "SETTLEMENT_INSTRUCTIONS", searchText))
                .thenReturn(Arrays.asList());

        List<Long> result = settlementInstructionsService.findTradeIdsBySettlementInstructions(searchText);

        assertTrue(result.isEmpty());
        verify(additionalInfoRepository).findByEntityTypeAndFieldNameContaining(
                "TRADE", "SETTLEMENT_INSTRUCTIONS", searchText);
    }

    @Test
    void findTradeIdsBySettlementInstructions_ShouldReturnEmptyList_WhenNullSearchText() {
        List<Long> result = settlementInstructionsService.findTradeIdsBySettlementInstructions(null);

        assertTrue(result.isEmpty());
        verifyNoInteractions(additionalInfoRepository);
    }

    @Test
    void findTradeIdsBySettlementInstructions_ShouldReturnEmptyList_WhenEmptySearchText() {
        List<Long> result = settlementInstructionsService.findTradeIdsBySettlementInstructions("   ");

        assertTrue(result.isEmpty());
        verifyNoInteractions(additionalInfoRepository);
    }

    @Test
    void findTradeIdsBySettlementInstructions_ShouldTrimSearchText() {
        String searchTextWithWhitespace = "  Bank XYZ  ";
        String trimmedSearchText = "Bank XYZ";
        
        when(additionalInfoRepository.findByEntityTypeAndFieldNameContaining(
                "TRADE", "SETTLEMENT_INSTRUCTIONS", trimmedSearchText))
                .thenReturn(Arrays.asList());

        settlementInstructionsService.findTradeIdsBySettlementInstructions(searchTextWithWhitespace);

        verify(additionalInfoRepository).findByEntityTypeAndFieldNameContaining(
                "TRADE", "SETTLEMENT_INSTRUCTIONS", trimmedSearchText);
    }

    private AdditionalInfo createMockAdditionalInfo(Long entityId, String fieldValue) {
        AdditionalInfo info = new AdditionalInfo();
        info.setEntityType("TRADE");
        info.setEntityId(entityId);
        info.setFieldName("SETTLEMENT_INSTRUCTIONS");
        info.setFieldValue(fieldValue);
        info.setFieldType("STRING");
        info.setActive(true);
        return info;
    }
}