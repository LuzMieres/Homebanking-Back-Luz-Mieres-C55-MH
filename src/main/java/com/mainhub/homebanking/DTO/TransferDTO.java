package com.mainhub.homebanking.DTO;

public class TransferDTO {
    private double amount;
    private String description;
    private String originAccountNumber;
    private String destinationAccountNumber;

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getOriginAccountNumber() {
        return originAccountNumber;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setOriginAccountNumber(String originAccountNumber) {
        this.originAccountNumber = originAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }

}