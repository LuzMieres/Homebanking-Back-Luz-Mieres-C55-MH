package com.mainhub.homebanking.services.implement;

import com.mainhub.homebanking.DTO.TransferDTO;
import com.mainhub.homebanking.exeptions.AccountNotBelongToClientException;
import com.mainhub.homebanking.exeptions.AccountNotFoundException;
import com.mainhub.homebanking.exeptions.InsufficientFundsException;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.Transaction;
import com.mainhub.homebanking.models.type.TransactionType;
import com.mainhub.homebanking.repositories.AccountRepository;
import com.mainhub.homebanking.repositories.TransactionRepository;
import com.mainhub.homebanking.services.AccountService;
import com.mainhub.homebanking.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    @Lazy
    private AccountService accountService;

    @Transactional
    public void createTransaction(TransferDTO transferDTO, Client client) throws Exception {
        // Validar los datos recibidos
        transferDTO.validate(); // Método agregado en el DTO

        // Verificar que el monto sea mayor a cero
        if (transferDTO.getAmount() <= 0) {
            throw new IllegalArgumentException("The transaction amount must be greater than zero.");
        }

        // Obtener la cuenta origen y verificar que exista
        Account originAccount = accountService.getAccountByNumber(transferDTO.getOriginAccountNumber());
        if (originAccount == null) {
            throw new AccountNotFoundException("Origin account does not exist.");
        }

        // Verificar que la cuenta de origen pertenece al cliente autenticado
        if (!originAccount.getClient().equals(client)) {
            throw new AccountNotBelongToClientException("Origin account does not belong to the authenticated client.");
        }

        // Verificar que la cuenta de origen tenga suficiente saldo
        if (originAccount.getBalance() < transferDTO.getAmount()) {
            throw new InsufficientFundsException("Insufficient balance.");
        }

        // Validaciones adicionales según el tipo de transferencia
        Account destinationAccount = accountService.getAccountByNumber(transferDTO.getDestinationAccountNumber());

        if (transferDTO.getTransferType().equals("own")) {
            // Verificar que la cuenta de destino pertenezca al cliente autenticado
            if (destinationAccount == null || !destinationAccount.getClient().equals(client)) {
                throw new AccountNotBelongToClientException("The destination account does not belong to the authenticated client.");
            }
        } else if (transferDTO.getTransferType().equals("other")) {
            // Verificar que la cuenta de destino exista en la base de datos pero no pertenezca al cliente autenticado
            if (destinationAccount == null) {
                throw new AccountNotFoundException("Destination account does not exist.");
            }
            if (destinationAccount.getClient().equals(client)) {
                throw new IllegalArgumentException("The destination account must not belong to the authenticated client.");
            }
        }

        // Verificar que la cuenta de origen y destino no sean la misma
        if (originAccount.getNumber().equals(destinationAccount.getNumber())) {
            throw new IllegalArgumentException("Origin and destination accounts cannot be the same.");
        }

        // Crear la transacción de débito
        Transaction debitTransaction = new Transaction(
                TransactionType.DEBIT,
                transferDTO.getAmount(),
                transferDTO.getDescription(),
                LocalDateTime.now(),
                originAccount
        );
        originAccount.addTransaction(debitTransaction); // Añadir transacción de débito a la cuenta de origen
        transactionRepository.save(debitTransaction); // Guardar transacción de débito en la base de datos

        // Crear la transacción de crédito
        Transaction creditTransaction = new Transaction(
                TransactionType.CREDIT,
                transferDTO.getAmount(),
                transferDTO.getDescription(),
                LocalDateTime.now(),
                destinationAccount
        );
        destinationAccount.addTransaction(creditTransaction); // Añadir transacción de crédito a la cuenta de destino
        transactionRepository.save(creditTransaction); // Guardar transacción de crédito en la base de datos

        // Guardar las cuentas actualizadas en la base de datos con los nuevos balances y transacciones
        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);
    }

    public void registerTransaction(Account account, double amount, String description) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setDate(LocalDateTime.now());

        transactionRepository.save(transaction); // Guardar la transacción
    }
}