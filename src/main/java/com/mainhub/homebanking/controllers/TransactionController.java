package com.mainhub.homebanking.controllers;

import com.mainhub.homebanking.DTO.TransferDTO;
import com.mainhub.homebanking.exeptions.AccountNotBelongToClientException;
import com.mainhub.homebanking.exeptions.AccountNotFoundException;
import com.mainhub.homebanking.exeptions.InsufficientFundsException;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.services.TransactionsService;
import com.mainhub.homebanking.services.ClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionsService transactionService;

    @Autowired
    private ClientServices clientService;

    @PostMapping("/")
    public ResponseEntity<?> createTransaction(@RequestBody TransferDTO transferDTO, Authentication authentication) {
        try {
            // Imprimir los datos recibidos para debugging
            System.out.println("Amount: " + transferDTO.getAmount());
            System.out.println("Origin Account Number: " + transferDTO.getOriginAccountNumber());
            System.out.println("Destination Account Number: " + transferDTO.getDestinationAccountNumber());

            Client client = clientService.findByEmail(authentication.getName());

            transactionService.createTransaction(transferDTO, client);
            return new ResponseEntity<>("Transaction created successfully", HttpStatus.CREATED);

        } catch (InsufficientFundsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (AccountNotBelongToClientException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while processing the transaction.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
