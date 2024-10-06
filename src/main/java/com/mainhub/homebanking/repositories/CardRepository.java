package com.mainhub.homebanking.repositories;


import com.mainhub.homebanking.models.Card;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.type.CardColor;
import com.mainhub.homebanking.models.type.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByClientAndColorAndType(Client client, CardColor color, CardType type);

    Optional<Card> findByClientAndType(Client client, CardType type);

    Card findByAccountIdAndType(Long accountId, CardType type);
}

