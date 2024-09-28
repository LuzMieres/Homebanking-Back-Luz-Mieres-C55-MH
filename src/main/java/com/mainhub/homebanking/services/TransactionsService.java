package com.mainhub.homebanking.services;

import com.mainhub.homebanking.DTO.TransferDTO;
import com.mainhub.homebanking.models.Client;

public interface TransactionsService {
    void createTransaction(TransferDTO transferDTO, Client client) throws Exception;
}
