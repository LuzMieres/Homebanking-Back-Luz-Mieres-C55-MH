package com.mainhub.homebanking.services;

import com.mainhub.homebanking.DTO.AccountDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AccountService {
    AccountDTO createAccountForCurrentClient(Client client);
    List<AccountDTO> getAllAccounts();
    AccountDTO getAccountById(Long id);
    Client getAuthenticatedClient(Authentication authentication);
    List<AccountDTO> getClientAccounts(Client client);
    Account getAccountByNumber(String accountNumber) throws Exception;
}