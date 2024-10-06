package com.mainhub.homebanking.DTO;

import com.mainhub.homebanking.models.Account;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {

    private Long id;
    private String number;
    private double balance;

    private LocalDate creationDate;
    private Set<TransactionDTO> transactions = new HashSet<>();
    private Set<CardDTO> cards = new HashSet<>();

    public AccountDTO() {
    }

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.transactions = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toSet());
        this.cards = account.getCards().stream().map(CardDTO::new).collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public double getBalance() {
        return balance;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }
}
