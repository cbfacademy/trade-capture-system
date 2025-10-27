package com.technicalchallenge.model;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    private boolean valid;
    private List<String> errors;
    private List<String> warnings;

    public ValidationResult() {
        this.valid = true;
        this.errors = new ArrayList<>();
        this.warnings = new ArrayList<>();
    }

    public ValidationResult(boolean valid) {
        this();
        this.valid = valid;
    }

    public static ValidationResult success() {
        return new ValidationResult(true);
    }

    public static ValidationResult failure(String error) {
        ValidationResult result = new ValidationResult(false);
        result.addError(error);
        return result;
    }

    public void addError(String error) {
        this.errors.add(error);
        this.valid = false;
    }

    public void addWarning(String warning) {
        this.warnings.add(warning);
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }

    public String getFirstError() {
        return errors.isEmpty() ? null : errors.get(0);
    }
}