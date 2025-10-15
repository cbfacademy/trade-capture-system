package com.technicalchallenge.tradesubtype;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/tradeSubTypes")
@Tag(name = "Trade Sub-Types", description = "Trade sub-type reference data for detailed trade classification")
public class TradeSubTypeController {
    @Autowired
    private TradeSubTypeRepository tradeSubTypeRepository;

    @GetMapping("/values")
    public List<String> getTradeSubTypeValues() {
        List<TradeSubType> subTypes = tradeSubTypeRepository.findAll();
        return subTypes.stream().map(TradeSubType::getTradeSubType).collect(Collectors.toList());
    }
}

