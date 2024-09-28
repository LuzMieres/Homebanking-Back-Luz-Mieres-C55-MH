package com.mainhub.homebanking.DTO;

public class TransferDTO {
    private double amount;
    private String description;
    private String originAccountNumber;
    private String destinationAccountNumber;
    private String transferType; // Agregado para tipo de transferencia (own, other)

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOriginAccountNumber() {
        return originAccountNumber;
    }

    public void setOriginAccountNumber(String originAccountNumber) {
        this.originAccountNumber = originAccountNumber;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    // Getters y setters

    // Validaciones para todos los campos
    public void validate() throws IllegalArgumentException {
        if (originAccountNumber == null || originAccountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("The origin account number cannot be empty or start with whitespace.");
        }
        if (destinationAccountNumber == null || destinationAccountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("The destination account number cannot be empty or start with whitespace.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("The transaction amount must be greater than zero.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("The description cannot be empty or start with whitespace.");
        }
        if (!transferType.equals("own") && !transferType.equals("other")) {
            throw new IllegalArgumentException("The transfer type must be 'own' or 'other'.");
        }
    }
}
