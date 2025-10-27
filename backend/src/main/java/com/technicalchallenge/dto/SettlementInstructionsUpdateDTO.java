package com.technicalchallenge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for updating settlement instructions on trades.
 * Uses validation to ensure settlement instructions meet business requirements.
 */
@Data
public class SettlementInstructionsUpdateDTO {
    
    @NotBlank(message = "Settlement instructions cannot be blank")
    @Size(min = 10, max = 500, message = "Settlement instructions must be between 10 and 500 characters")
    private String settlementInstructions;
}