package com.mainhub.homebanking.DTO;

public class PaymentRequestDTO {
    private Long orderId;
    private double amount; // El monto que se va a descontar de la cuenta

    // Getters y setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

