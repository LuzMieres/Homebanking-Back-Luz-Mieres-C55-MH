package com.mainhub.homebanking.services;

import com.mainhub.homebanking.DTO.CardDTO;
import com.mainhub.homebanking.models.Client;


import java.time.LocalDateTime;
import java.util.List;

public interface CardService {
    CardDTO createCardForCurrentClient(Client client, String type, String color);
    void validateCardDetails(String type, String color);
}