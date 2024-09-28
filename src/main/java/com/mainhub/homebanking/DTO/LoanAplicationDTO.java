package com.mainhub.homebanking.DTO;

public class LoanAplicationDTO {
    private String loanName;  // El cliente enviará el nombre del préstamo
    private double amount;
    private int payments;
    private String destinationAccountNumber;

    // Constructor
    public LoanAplicationDTO(String loanName, double amount, int payments, String destinationAccountNumber) {
        this.loanName = loanName;
        this.amount = amount;
        this.payments = payments;
        this.destinationAccountNumber = destinationAccountNumber;
    }

    // Getters
    public String getLoanName() {
        return loanName;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }
}