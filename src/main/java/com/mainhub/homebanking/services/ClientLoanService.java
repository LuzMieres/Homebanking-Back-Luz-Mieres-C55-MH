package com.mainhub.homebanking.services;

import com.mainhub.homebanking.DTO.ClientLoanDTO;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.ClientLoan;
import com.mainhub.homebanking.models.Loan;

import java.util.List;

public interface ClientLoanService {
    List<ClientLoanDTO> getLoansByClient(Client client);
    ClientLoanDTO getClientLoanByClientAndLoan(Client client, Loan loan);
}

