package com.technicalchallenge.controller;

import com.technicalchallenge.model.TradeSubType;
import com.technicalchallenge.repository.TradeSubTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tradeSubTypes")
public class TradeSubTypeController {// Purpose of this controller - to manage trade sub-types, including retrieving them.

    // Injecting TradeSubTypeRepository to access trade sub-type data
    @Autowired
    private TradeSubTypeRepository tradeSubTypeRepository;

    // Endpoint to retrieve all trade sub-type values

    @GetMapping("/values")
    public List<String> getTradeSubTypeValues() {
        List<TradeSubType> subTypes = tradeSubTypeRepository.findAll();
        return subTypes.stream().map(TradeSubType::getTradeSubType).collect(Collectors.toList());
    }
}

