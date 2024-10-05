package com.mainhub.homebanking.controllers;

import com.mainhub.homebanking.DTO.CardDTO;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.services.CardService;
import com.mainhub.homebanking.services.ClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ClientServices clientService;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<?> createCardForCurrentClient(Authentication authentication, @RequestBody Map<String, String> cardDetails) {
        String type = cardDetails.get("type");
        String color = cardDetails.get("color");
        try {
            cardService.validateCardDetails(type, color);
            Client client = clientService.findByEmail(authentication.getName());
            CardDTO cardDTO = cardService.createCardForCurrentClient(client, type, color);
            return new ResponseEntity<>(cardDTO, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/clients/current/cards")
    public ResponseEntity<?> getClientCards(Authentication authentication){

        try {
            // Obtener las tarjetas del cliente
            Set<CardDTO> cardDtos = cardService.getClientCardsForCurrentClient(authentication.getName());
            return new ResponseEntity<>(cardDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}