package com.mainhub.homebanking.controllers;

import com.mainhub.homebanking.DTO.AccountDTO;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.services.AccountService;
import com.mainhub.homebanking.services.ClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientServices clientService;

    @GetMapping("/clients/current")
    public ResponseEntity<?> getClientAccounts(Authentication authentication) {
        Client client = accountService.getAuthenticatedClient(authentication);
        List<AccountDTO> accounts = accountService.getClientAccounts(client);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<?> createAccountForCurrentClient(Authentication authentication) {
        Client client = accountService.getAuthenticatedClient(authentication);
        try {
            AccountDTO accountDTO = accountService.createAccountForCurrentClient(client);
            return new ResponseEntity<>(accountDTO, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }


    @GetMapping("/")
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        List<AccountDTO> accounts = accountService.getAllAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneAccountById(@PathVariable Long id) {
        try {
            AccountDTO accountDTO = accountService.getAccountById(id);
            return new ResponseEntity<>(accountDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}