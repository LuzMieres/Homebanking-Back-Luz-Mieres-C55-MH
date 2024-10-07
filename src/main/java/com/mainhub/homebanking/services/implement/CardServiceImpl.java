package com.mainhub.homebanking.services.implement;

import com.mainhub.homebanking.DTO.CardDTO;
import com.mainhub.homebanking.models.Card;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.type.CardColor;
import com.mainhub.homebanking.models.type.CardType;
import com.mainhub.homebanking.repositories.CardRepository;
import com.mainhub.homebanking.repositories.ClientRepository;
import com.mainhub.homebanking.services.CardService;
import com.mainhub.homebanking.services.ClientServices;
import com.mainhub.homebanking.utils.UtilMetod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private UtilMetod utilMetod;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientServices clientService;

    @Override
    public CardDTO createCardForCurrentClient(Client client, String type, String color) {
        CardType cardType = CardType.valueOf(type.toUpperCase());
        CardColor cardColor = CardColor.valueOf(color.toUpperCase());

        // Verificar si ya existe una tarjeta de este tipo y color
        checkIfCardAlreadyExists(client, cardColor, cardType);

        Card card = new Card(CardType.DEBIT, CardColor.GOLD, LocalDate.now(), LocalDate.now().plusYears(5));
        client.addCard(card);
        card.setType(cardType);
        card.setColor(cardColor);
        card.setNumber(utilMetod.generateCardNumber());
        card.setCvv(utilMetod.generateCvv());
        card.setFromDate(LocalDate.now());
        card.setThruDate(utilMetod.getThruDate());
        card.setCardHolder((client.getFirstName() + " " + client.getLastName()));  // Convertir a mayúsculas
        cardRepository.save(card);

        return new CardDTO(card);
    }


    @Override
    public void validateCardDetails(String type, String color) {
        if (type == null || color == null || type.isBlank() || color.isBlank()) {
            throw new IllegalArgumentException("Type and color must be specified");
        }
    }


    private void checkIfCardAlreadyExists(Client client, CardColor cardColor, CardType cardType) {
        List<Card> clientCards = cardRepository.findByClientAndColorAndType(client, cardColor, cardType);
        if (!clientCards.isEmpty()) {
            throw new IllegalArgumentException("You already have a card of this type and color");
        }
    }

    @Override
    public Card findDebitCardByClient(Client client) {
        return cardRepository.findByClientAndType(client, CardType.DEBIT)
                .orElseThrow(() -> new IllegalArgumentException("No debit card associated with the client"));
    }

    @Override
    public Set<CardDTO> getClientCardsForCurrentClient(String email) {
        Client client = clientService.findByEmail(email);
        return getClientCardDtos(client);
    }

    //---------------------------------------------------------------
    //Metodo que toma un cliente y devuelve un Set<CardDto> que contienen todas las tarjetas del cliente.
    @Override
    public Set<CardDTO> getClientCardDtos(Client client) {
        return client.getCards().stream()
                .map(CardDTO::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Card findDebitCardByClientAccount(Long accountId) {
        // Suponiendo que tienes una relación entre Tarjeta y Cuenta, puedes realizar una consulta en el repositorio
        return cardRepository.findByAccountIdAndType(accountId, CardType.DEBIT);
    }

    @Override
    public Card findByCardNumber(String cardNumber) {
        return cardRepository.findByNumber(cardNumber); // Buscar por número de tarjeta
    }

}