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
            System.out.println("PaymentRequestDTO: " + paymentRequestDTO);
            System.out.println("Card Number: " + paymentRequestDTO.getCardDetails().getCardNumber());

            // 1. Obtener la tarjeta de débito a través del número de tarjeta enviado en la solicitud
            String cardNumber = paymentRequestDTO.getCardDetails().getCardNumber();
            Card debitCard = cardService.findByCardNumber(cardNumber);

            if (debitCard == null) {
                return new ResponseEntity<>("No debit card associated with this card number", HttpStatus.BAD_REQUEST);
            }

            // 2. Verificar si la tarjeta es de débito
            if (!debitCard.isDebitCard()) {
                return new ResponseEntity<>("The card is not a debit card", HttpStatus.BAD_REQUEST);
            }

            // 3. Obtener la cuenta vinculada a la tarjeta de débito
            Account account = debitCard.getAccount();
            if (account == null) {
                return new ResponseEntity<>("No account associated with this debit card", HttpStatus.BAD_REQUEST);
            }

            // 4. Verificar si la cuenta tiene suficiente saldo
            double orderAmount = paymentRequestDTO.getAmount();
            if (account.getBalance() < orderAmount) {
                return new ResponseEntity<>("Insufficient balance in the account", HttpStatus.BAD_REQUEST);
            }

            // 5. Descontar el monto de la cuenta
            accountService.debitAccount(account, orderAmount);

            // 6. Registrar la transacción en el sistema de homebanking
            transactionsService.registerTransaction(account, orderAmount, "Payment for order: " + paymentRequestDTO.getOrderId());

            return new ResponseEntity<>("Payment successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
