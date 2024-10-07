package com.mainhub.homebanking.DTO;

public class PaymentRequestDTO {
    private Long orderId;
    private double amount;
    private CardDetailsDTO cardDetailsDTO; // Agregamos el objeto CardDetailsDTO

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

    public CardDetailsDTO getCardDetailsDTO() {
        return cardDetailsDTO;
    }

    public void setCardDetailsDTO(CardDetailsDTO cardDetailsDTO) {
        this.cardDetailsDTO = cardDetailsDTO;
    }
}
