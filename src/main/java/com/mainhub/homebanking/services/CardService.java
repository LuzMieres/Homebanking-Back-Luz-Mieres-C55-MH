package com.mainhub.homebanking.services;

import com.mainhub.homebanking.DTO.CardDTO;
import com.mainhub.homebanking.models.Card;
import com.mainhub.homebanking.models.Client;

import java.util.Set;

public interface CardService {
    CardDTO createCardForCurrentClient(Client client, String type, String color);
    void validateCardDetails(String type, String color);
    Card findDebitCardByClient(Client client);

    Set<CardDTO> getClientCardsForCurrentClient(String email);

    //---------------------------------------------------------------
    //Metodo que toma un cliente y devuelve un Set<CardDto> que contienen todas las tarjetas del cliente.
    Set<CardDTO> getClientCardDtos(Client client);

    Card findDebitCardByClientAccount(Long accountId);

    Card findByCardNumber(String cardNumber);
}