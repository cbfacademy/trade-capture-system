package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.TradeLegDTO;
import com.technicalchallenge.model.Schedule;
import com.technicalchallenge.model.TradeLeg;
import com.technicalchallenge.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TradeLegMapper {
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private LegTypeRepository legTypeRepository;
    @Autowired
    private IndexRepository indexRepository;
    @Autowired
    private HolidayCalendarRepository holidayCalendarRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private BusinessDayConventionRepository businessDayConventionRepository;
    @Autowired
    private PayRecRepository payRecRepository;
    @Autowired
    private CashflowMapper cashflowMapper;

    public TradeLegDTO toDto(TradeLeg entity) {
        TradeLegDTO dto = new TradeLegDTO();
        dto.setLegId(entity.getLegId());
        dto.setNotional(entity.getNotional());
        dto.setRate(entity.getRate());
        dto.setCurrency(entity.getCurrency() != null ? entity.getCurrency().getCurrency() : null);
        dto.setLegType(entity.getLegRateType() != null ? entity.getLegRateType().getType() : null);
        dto.setIndexName(entity.getIndex() != null ? entity.getIndex().getIndex() : null);
        dto.setHolidayCalendar(entity.getHolidayCalendar() != null ? entity.getHolidayCalendar().getHolidayCalendar() : null);
        dto.setCalculationPeriodSchedule(entity.getCalculationPeriodSchedule() != null ? entity.getCalculationPeriodSchedule().getSchedule() : null);
        dto.setPaymentBusinessDayConvention(entity.getPaymentBusinessDayConvention() != null ? entity.getPaymentBusinessDayConvention().getBdc() : null);
        dto.setFixingBusinessDayConvention(entity.getFixingBusinessDayConvention() != null ? entity.getFixingBusinessDayConvention().getBdc() : null);
        dto.setPayReceiveFlag(entity.getPayReceiveFlag() != null ? entity.getPayReceiveFlag().getPayRec() : null);
        if (entity.getCashflows() != null) {
            dto.setCashflows(entity.getCashflows().stream().map(cashflowMapper::toDto).collect(Collectors.toList()));
        }
        return dto;
    }

    public TradeLeg toEntity(TradeLegDTO dto) {
        TradeLeg entity = new TradeLeg();
        entity.setLegId(dto.getLegId());
        entity.setNotional(dto.getNotional());
        entity.setRate(dto.getRate());
        if (dto.getCurrency() != null) {
            entity.setCurrency(currencyRepository.findByCurrency(dto.getCurrency()).orElse(null));
        }
        if (dto.getLegType() != null) {
            entity.setLegRateType(legTypeRepository.findByType(dto.getLegType()).orElse(null));
        }
        if (dto.getIndexName() != null) {
            entity.setIndex(indexRepository.findByIndex(dto.getIndexName()).orElse(null));
        }
        if (dto.getHolidayCalendar() != null) {
            entity.setHolidayCalendar(holidayCalendarRepository.findByHolidayCalendar(dto.getHolidayCalendar()).orElse(null));
        }
        if (dto.getCalculationPeriodSchedule() != null) {
            Schedule schedule = scheduleRepository.findBySchedule(dto.getCalculationPeriodSchedule())
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found for: " + dto.getCalculationPeriodSchedule()));
            entity.setCalculationPeriodSchedule(schedule);
        }
        if (dto.getPaymentBusinessDayConvention() != null) {
            entity.setPaymentBusinessDayConvention(businessDayConventionRepository.findByBdc(dto.getPaymentBusinessDayConvention()).orElse(null));
        }
        if (dto.getFixingBusinessDayConvention() != null) {
            entity.setFixingBusinessDayConvention(businessDayConventionRepository.findByBdc(dto.getFixingBusinessDayConvention()).orElse(null));
        }
        if (dto.getPayReceiveFlag() != null) {
            entity.setPayReceiveFlag(payRecRepository.findByPayRec(dto.getPayReceiveFlag()).orElse(null));
        }
        if (dto.getCashflows() != null) {
            entity.setCashflows(dto.getCashflows().stream().map(cashflowMapper::toEntity).collect(Collectors.toList()));
        }
        return entity;
    }
}
