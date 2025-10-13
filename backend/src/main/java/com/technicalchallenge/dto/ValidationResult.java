package com.technicalchallenge.dto;

import java.util.ArrayList;
import java.util.List;

// NEW FILE: Validation result returned by validation engine
public class ValidationResult {
    private boolean valid = true;
    private final List<String> errors = new ArrayList<>();

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    public List<String> getErrors() { return errors; }

    public void addError(String e) {
        this.valid = false;
        this.errors.add(e);
    }
}

