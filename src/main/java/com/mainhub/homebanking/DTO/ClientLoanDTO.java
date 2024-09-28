// ClientLoanDTO.java
package com.mainhub.homebanking.DTO;

import com.mainhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private Long id;
    private String loanName;
    private double amountRequested;
    private double amountCredited;
    private double totalAmountWithInterest;
    private int payments;
    private String destinationAccountNumber;

    public ClientLoanDTO() {
    }

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanName = clientLoan.getLoan().getName();
        this.amountRequested = clientLoan.getAmount(); // Monto solicitado
        this.amountCredited = clientLoan.getAmount(); // Monto acreditado
        this.totalAmountWithInterest = clientLoan.getTotalAmount(); // Monto total con interés
        this.payments = clientLoan.getPayments();
        this.destinationAccountNumber = clientLoan.getDestinationAccount().getNumber(); // Cuenta de destino
    }

    public String getLoanName() {
        return loanName;
    }

    public double getAmountRequested() {
        return amountRequested;
    }

    public double getAmountCredited() {
        return amountCredited;
    }

    public double getTotalAmountWithInterest() {
        return totalAmountWithInterest;
    }

    public int getPayments() {
        return payments;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }
}
