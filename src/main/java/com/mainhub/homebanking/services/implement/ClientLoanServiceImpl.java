package com.mainhub.homebanking.services.implement;

import com.mainhub.homebanking.DTO.ClientLoanDTO;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.ClientLoan;
import com.mainhub.homebanking.models.Loan;
import com.mainhub.homebanking.repositories.ClientLoanRepository;
import com.mainhub.homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientLoanServiceImpl implements ClientLoanService {

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Override
    public List<ClientLoanDTO> getLoansByClient(Client client) {
        return clientLoanRepository.findByClient(client).stream()
                .map(ClientLoanDTO::new) // Convertir cada ClientLoan a ClientLoanDTO
                .collect(Collectors.toList());
    }

    @Override
    public ClientLoanDTO getClientLoanByClientAndLoan(Client client, Loan loan) {
        return clientLoanRepository.findByClientAndLoan(client, loan)
                .map(ClientLoanDTO::new) // Convertir ClientLoan a ClientLoanDTO
                .orElseThrow(() -> new IllegalArgumentException("No loan found for client with the specified loan"));
    }
}


