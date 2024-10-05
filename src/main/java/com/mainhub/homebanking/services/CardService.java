package com.mainhub.homebanking.services;

import com.mainhub.homebanking.DTO.CardDTO;
import com.mainhub.homebanking.models.Card;
import com.mainhub.homebanking.models.Client;

public interface CardService {
    CardDTO createCardForCurrentClient(Client client, String type, String color);
    void validateCardDetails(String type, String color);
    Card findDebitCardByClient(Client client);
}