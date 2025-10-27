package com.technicalchallenge.service;

import com.technicalchallenge.dto.AdditionalInfoDTO;
import com.technicalchallenge.model.AdditionalInfo;
import com.technicalchallenge.repository.AdditionalInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SettlementInstructionsService {

    private static final String ENTITY_TYPE = "TRADE";
    private static final String FIELD_NAME = "SETTLEMENT_INSTRUCTIONS";
    private static final String FIELD_TYPE = "STRING";

    @Autowired
    private AdditionalInfoRepository additionalInfoRepository;

    @Autowired
    private AdditionalInfoService additionalInfoService;

    public String getSettlementInstructions(Long tradeId) {
        AdditionalInfo info = additionalInfoRepository.findActiveByEntityTypeAndEntityIdAndFieldName(
            ENTITY_TYPE, tradeId, FIELD_NAME);
        return info != null ? info.getFieldValue() : null;
    }

    public void updateSettlementInstructions(Long tradeId, String instructions) {
        if (instructions == null || instructions.trim().isEmpty()) {
            removeSettlementInstructions(tradeId);
            return;
        }

        AdditionalInfoDTO dto = new AdditionalInfoDTO();
        dto.setEntityType(ENTITY_TYPE);
        dto.setEntityId(tradeId);
        dto.setFieldName(FIELD_NAME);
        dto.setFieldValue(instructions.trim());
        dto.setFieldType(FIELD_TYPE);

        additionalInfoService.updateAdditionalInfo(dto);
    }

    public void removeSettlementInstructions(Long tradeId) {
        additionalInfoService.removeAdditionalInfo(ENTITY_TYPE, tradeId, FIELD_NAME);
    }

    public List<Long> findTradeIdsBySettlementInstructions(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return List.of();
        }

        List<AdditionalInfo> results = additionalInfoRepository.findByEntityTypeAndFieldNameContaining(
            ENTITY_TYPE, FIELD_NAME, searchText.trim());

        return results.stream()
            .map(AdditionalInfo::getEntityId)
            .collect(Collectors.toList());
    }
}