package com.technicalchallenge.mapper;

import com.technicalchallenge.dto.TradeDTO;
import com.technicalchallenge.dto.TradeLegDTO;
import com.technicalchallenge.dto.CashflowDTO;
import com.technicalchallenge.model.Trade;
import com.technicalchallenge.model.TradeLeg;
import com.technicalchallenge.model.Cashflow;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TradeMapper {

    @Autowired
    private ModelMapper modelMapper;

    public TradeDTO toDto(Trade trade) {
        if (trade == null) {
            return null;
        }

        TradeDTO dto = new TradeDTO();
        dto.setId(trade.getId());
        dto.setTradeId(trade.getTradeId());
        dto.setVersion(trade.getVersion());
        dto.setTradeDate(trade.getTradeDate());
        dto.setTradeStartDate(trade.getTradeStartDate());
        dto.setTradeMaturityDate(trade.getTradeMaturityDate());
        dto.setTradeExecutionDate(trade.getTradeExecutionDate());
        dto.setUtiCode(trade.getUtiCode());
        dto.setLastTouchTimestamp(trade.getLastTouchTimestamp());
        dto.setValidityStartDate(trade.getValidityStartDate());
        dto.setValidityEndDate(trade.getValidityEndDate());
        dto.setActive(trade.getActive());
        dto.setCreatedDate(trade.getCreatedDate());


        if (trade.getBook() != null) {
            dto.setBookId(trade.getBook().getId());
            dto.setBookName(trade.getBook().getBookName());
        }

        if (trade.getCounterparty() != null) {
            dto.setCounterpartyId(trade.getCounterparty().getId());
            dto.setCounterpartyName(trade.getCounterparty().getName());
        }

        if (trade.getTraderUser() != null) {
            dto.setTraderUserId(trade.getTraderUser().getId());
            dto.setTraderUserName(trade.getTraderUser().getFirstName() + " " + trade.getTraderUser().getLastName());
        }

        if (trade.getTradeInputterUser() != null) { // Fixed field name
            dto.setTradeInputterUserId(trade.getTradeInputterUser().getId());
            dto.setInputterUserName(trade.getTradeInputterUser().getFirstName() + " " + trade.getTradeInputterUser().getLastName());
        }

        if (trade.getTradeType() != null) {
            dto.setTradeTypeId(trade.getTradeType().getId());
            dto.setTradeType(trade.getTradeType().getTradeType());
        }

        if (trade.getTradeSubType() != null) {
            dto.setTradeSubTypeId(trade.getTradeSubType().getId());
            dto.setTradeSubType(trade.getTradeSubType().getTradeSubType());
        }

        if (trade.getTradeStatus() != null) {
            dto.setTradeStatusId(trade.getTradeStatus().getId());
            dto.setTradeStatus(trade.getTradeStatus().getTradeStatus());
        }

        // Map trade legs
        if (trade.getTradeLegs() != null) {
            List<TradeLegDTO> legDTOs = trade.getTradeLegs().stream()
                    .map(this::tradeLegToDto)
                    .collect(Collectors.toList());
            dto.setTradeLegs(legDTOs);
        }

        return dto;
    }

    public Trade toEntity(TradeDTO dto) {
        if (dto == null) {
            return null;
        }

        Trade trade = new Trade();
        trade.setId(dto.getId());
        trade.setTradeId(dto.getTradeId());
        trade.setVersion(dto.getVersion());
        trade.setTradeDate(dto.getTradeDate());
        trade.setTradeStartDate(dto.getTradeStartDate());
        trade.setTradeMaturityDate(dto.getTradeMaturityDate());
        trade.setTradeExecutionDate(dto.getTradeExecutionDate());
        trade.setUtiCode(dto.getUtiCode());
        trade.setLastTouchTimestamp(dto.getLastTouchTimestamp());
        trade.setValidityStartDate(dto.getValidityStartDate());
        trade.setValidityEndDate(dto.getValidityEndDate());
        trade.setActive(dto.getActive());
        trade.setCreatedDate(dto.getCreatedDate());

        return trade;
    }

    public TradeLegDTO tradeLegToDto(TradeLeg leg) {
        if (leg == null) {
            return null;
        }

        TradeLegDTO dto = new TradeLegDTO();
        dto.setLegId(leg.getLegId());
        dto.setNotional(leg.getNotional());
        dto.setRate(leg.getRate());

        if (leg.getCurrency() != null) {
            dto.setCurrencyId(leg.getCurrency().getId());
            dto.setCurrency(leg.getCurrency().getCurrency());
        }

        if (leg.getLegRateType() != null) {
            dto.setLegTypeId(leg.getLegRateType().getId());
            dto.setLegType(leg.getLegRateType().getType());
        }

        if (leg.getIndex() != null) {
            dto.setIndexId(leg.getIndex().getId());
            dto.setIndexName(leg.getIndex().getIndex()); // Fixed: setIndex() -> setIndexName()
        }

        if (leg.getHolidayCalendar() != null) {
            dto.setHolidayCalendarId(leg.getHolidayCalendar().getId());
            dto.setHolidayCalendar(leg.getHolidayCalendar().getHolidayCalendar());
        }

        if (leg.getCalculationPeriodSchedule() != null) {
            dto.setScheduleId(leg.getCalculationPeriodSchedule().getId());
            dto.setCalculationPeriodSchedule(leg.getCalculationPeriodSchedule().getSchedule());
        }

        if (leg.getPaymentBusinessDayConvention() != null) {
            dto.setPaymentBdcId(leg.getPaymentBusinessDayConvention().getId());
            dto.setPaymentBusinessDayConvention(leg.getPaymentBusinessDayConvention().getBdc());
        }

        if (leg.getFixingBusinessDayConvention() != null) {
            dto.setFixingBdcId(leg.getFixingBusinessDayConvention().getId());
            dto.setFixingBusinessDayConvention(leg.getFixingBusinessDayConvention().getBdc());
        }

        if (leg.getPayReceiveFlag() != null) {
            dto.setPayRecId(leg.getPayReceiveFlag().getId());
            dto.setPayReceiveFlag(leg.getPayReceiveFlag().getPayRec());
        }

        // Map cashflows
        if (leg.getCashflows() != null) {
            List<CashflowDTO> cashflowDTOs = leg.getCashflows().stream()
                    .map(this::cashflowToDto)
                    .collect(Collectors.toList());
            dto.setCashflows(cashflowDTOs);
        }

        return dto;
    }

    public TradeLeg tradeLegToEntity(TradeLegDTO dto) {
        if (dto == null) {
            return null;
        }

        TradeLeg leg = new TradeLeg();
        leg.setLegId(dto.getLegId());
        leg.setNotional(dto.getNotional());
        leg.setRate(dto.getRate());


        return leg;
    }

    public CashflowDTO cashflowToDto(Cashflow cashflow) {
        if (cashflow == null) {
            return null;
        }

        CashflowDTO dto = new CashflowDTO();
        dto.setId(cashflow.getId()); // Fixed field name
        dto.setLegId(cashflow.getTradeLeg() != null ? cashflow.getTradeLeg().getLegId() : null);
        dto.setPaymentValue(cashflow.getPaymentValue());
        dto.setValueDate(cashflow.getValueDate());
        dto.setRate(cashflow.getRate());
        dto.setPayRec(cashflow.getPayRec() != null ? cashflow.getPayRec().getPayRec() : null);
        dto.setPaymentType(cashflow.getPaymentType() != null ? cashflow.getPaymentType().getType() : null);
        dto.setPaymentBusinessDayConvention(cashflow.getPaymentBusinessDayConvention() != null ?
            cashflow.getPaymentBusinessDayConvention().getBdc() : null);
        dto.setCreatedDate(cashflow.getCreatedDate());
        dto.setActive(cashflow.getActive());

        return dto;
    }
}
