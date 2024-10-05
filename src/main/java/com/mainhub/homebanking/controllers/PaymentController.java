package com.mainhub.homebanking.controllers;

import com.mainhub.homebanking.DTO.PaymentRequestDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Card;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.services.AccountService;
import com.mainhub.homebanking.services.CardService;
import com.mainhub.homebanking.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CardService cardService;

    @Autowired
    private TransactionsService transactionsService; // Para registrar transacciones

    @PostMapping("/pay-order")
    public ResponseEntity<?> payOrder(Authentication authentication, @RequestBody PaymentRequestDTO paymentRequestDTO) {
        try {
            // Obtener el cliente autenticado
            Client client = accountService.getAuthenticatedClient(authentication);

            // Verificar que la tarjeta de débito esté vinculada al cliente
            Card debitCard = cardService.findDebitCardByClient(client);
            if (debitCard == null) {
                return new ResponseEntity<>("No debit card associated with the client", HttpStatus.BAD_REQUEST);
            }

            // Obtener la cuenta vinculada a la tarjeta de débito
            Account account = debitCard.getAccount();
            if (account == null) {
                return new ResponseEntity<>("No account associated with this debit card", HttpStatus.BAD_REQUEST);
            }

            // Verificar si la cuenta tiene suficiente saldo
            double orderAmount = paymentRequestDTO.getAmount();
            if (account.getBalance() < orderAmount) {
                return new ResponseEntity<>("Insufficient balance in the account", HttpStatus.BAD_REQUEST);
            }

            // Descontar el monto de la cuenta
            accountService.debitAccount(account, orderAmount);

            // Registrar la transacción en el sistema de homebanking
            transactionsService.registerTransaction(account, orderAmount, "Payment for order: " + paymentRequestDTO.getOrderId());

            return new ResponseEntity<>("Payment successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

