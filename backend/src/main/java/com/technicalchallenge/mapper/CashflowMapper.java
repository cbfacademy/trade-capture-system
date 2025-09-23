package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.CashflowDTO;
import com.technicalchallenge.model.Cashflow;
import com.technicalchallenge.model.PayRec;
import com.technicalchallenge.model.LegType;
import com.technicalchallenge.model.BusinessDayConvention;
import com.technicalchallenge.repository.PayRecRepository;
import com.technicalchallenge.repository.LegTypeRepository;
import com.technicalchallenge.repository.BusinessDayConventionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CashflowMapper {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PayRecRepository payRecRepository;
    @Autowired
    private LegTypeRepository legTypeRepository;
    @Autowired
    private BusinessDayConventionRepository businessDayConventionRepository;

    public CashflowDTO toDto(Cashflow entity) {
        CashflowDTO dto = new CashflowDTO();
        dto.setId(entity.getId());
        dto.setPaymentValue(entity.getPaymentValue());
        dto.setValueDate(entity.getValueDate());
        dto.setRate(entity.getRate());
        dto.setPayRec(entity.getPayRec() != null ? entity.getPayRec().getPayRec() : null);
        dto.setPaymentType(entity.getPaymentType() != null ? entity.getPaymentType().getType() : null);
        dto.setPaymentBusinessDayConvention(entity.getPaymentBusinessDayConvention() != null ? entity.getPaymentBusinessDayConvention().getBdc() : null);
        return dto;
    }

    public Cashflow toEntity(CashflowDTO dto) {
        Cashflow entity = new Cashflow();
        entity.setId(dto.getId());
        entity.setPaymentValue(dto.getPaymentValue());
        entity.setValueDate(dto.getValueDate());
        entity.setRate(dto.getRate());
        if (dto.getPayRec() != null) {
            entity.setPayRec(payRecRepository.findByPayRec(dto.getPayRec()).orElse(null));
        }
        if (dto.getPaymentType() != null) {
            entity.setPaymentType(legTypeRepository.findByType(dto.getPaymentType()).orElse(null));
        }
        if (dto.getPaymentBusinessDayConvention() != null) {
            entity.setPaymentBusinessDayConvention(businessDayConventionRepository.findByBdc(dto.getPaymentBusinessDayConvention()).orElse(null));
        }
        return entity;
    }
}
