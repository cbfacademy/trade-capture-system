package com.technicalchallenge.service;

import com.technicalchallenge.dto.AdditionalInfoDTO;
import com.technicalchallenge.model.AdditionalInfo;
import com.technicalchallenge.repository.AdditionalInfoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdditionalInfoService {

    @Autowired
    private AdditionalInfoRepository additionalInfoRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<AdditionalInfoDTO> getAdditionalInfoForEntity(String entityType, Long entityId) {
        List<AdditionalInfo> additionalInfoList = additionalInfoRepository.findActiveByEntityTypeAndEntityId(entityType, entityId);
        return additionalInfoList.stream()
                .map(info -> modelMapper.map(info, AdditionalInfoDTO.class))
                .collect(Collectors.toList());
    }

    public AdditionalInfoDTO addAdditionalInfo(AdditionalInfoDTO dto) {
        // Check if field already exists and deactivate old version
        AdditionalInfo existing = additionalInfoRepository.findActiveByEntityTypeAndEntityIdAndFieldName(
                dto.getEntityType(), dto.getEntityId(), dto.getFieldName());

        if (existing != null) {
            existing.setActive(false);
            existing.setDeactivatedDate(LocalDateTime.now());
            additionalInfoRepository.save(existing);
        }

        // Create new version
        AdditionalInfo newInfo = modelMapper.map(dto, AdditionalInfo.class);
        newInfo.setId(null); // Ensure new record
        newInfo.setActive(true);
        newInfo.setCreatedDate(LocalDateTime.now());
        newInfo.setLastModifiedDate(LocalDateTime.now());
        newInfo.setVersion(existing != null ? existing.getVersion() + 1 : 1);

        AdditionalInfo saved = additionalInfoRepository.save(newInfo);
        return modelMapper.map(saved, AdditionalInfoDTO.class);
    }

    public void removeAdditionalInfo(String entityType, Long entityId, String fieldName) {
        AdditionalInfo existing = additionalInfoRepository.findActiveByEntityTypeAndEntityIdAndFieldName(
                entityType, entityId, fieldName);

        if (existing != null) {
            existing.setActive(false);
            existing.setDeactivatedDate(LocalDateTime.now());
            additionalInfoRepository.save(existing);
        }
    }

    public AdditionalInfoDTO updateAdditionalInfo(AdditionalInfoDTO dto) {
        return addAdditionalInfo(dto); // Same logic as add - version control
    }
}
