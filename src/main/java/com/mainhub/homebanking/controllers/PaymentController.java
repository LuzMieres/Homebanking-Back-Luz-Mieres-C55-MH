package com.mainhub.homebanking.controllers;

import com.mainhub.homebanking.DTO.PaymentRequestDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Card;
import com.mainhub.homebanking.services.AccountService;
import com.mainhub.homebanking.services.CardService;
import com.mainhub.homebanking.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CardService cardService;

    @Autowired
    private TransactionsService transactionsService; // Para registrar transacciones

    @PostMapping("/pay-order")
    public ResponseEntity<?> payOrder(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        try {
            // Obtener la tarjeta de débito a través del número de tarjeta
            Card debitCard = cardService.findByCardNumber(paymentRequestDTO.getCardDetailsDTO().getCardNumber());

            if (debitCard == null) {
                return new ResponseEntity<>("No debit card associated with this card number", HttpStatus.BAD_REQUEST);
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

            // Descontar el monto de la cuenta del cliente
            accountService.debitAccount(account, orderAmount);

            // Acreditar el monto en la cuenta del administrador
            Account adminAccount = accountService.getAccountByNumber("VIN-0001"); // Cuenta del administrador
            accountService.creditAccount(adminAccount, orderAmount);

            // Registrar la transacción en el sistema de homebanking
            transactionsService.registerTransaction(adminAccount, orderAmount, "Payment for order: " + paymentRequestDTO.getOrderId());

            return new ResponseEntity<>("Payment successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
