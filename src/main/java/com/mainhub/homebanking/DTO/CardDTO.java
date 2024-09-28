package com.mainhub.homebanking.DTO;

import com.mainhub.homebanking.models.Card;
import com.mainhub.homebanking.models.type.CardColor;
import com.mainhub.homebanking.models.type.CardType;

import java.time.LocalDate;

public class CardDTO {

    private Long id;


    private String ClientHolder;

    private String cvv;
    private String number;

    private LocalDate fromDate;
    private LocalDate thruDate;

    private CardType type;
    private CardColor color;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.ClientHolder = card.getCardHolder();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.type = card.getType();
        this.color = card.getColor();
    }

    public Long getId() {
        return id;
    }


    public String getClientHolder() {
        return ClientHolder;
    }

    public String getNumber() {
        return number;
    }

    public String getCvv() {
        return cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }
}
