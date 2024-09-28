package com.mainhub.homebanking.services.implement;

import com.mainhub.homebanking.DTO.TransferDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.Transaction;
import com.mainhub.homebanking.models.type.TransactionType;
import com.mainhub.homebanking.repositories.AccountRepository;
import com.mainhub.homebanking.repositories.TransactionRepository;
import com.mainhub.homebanking.services.AccountService;
import com.mainhub.homebanking.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private AccountService accountService;

    @Transactional
    public void createTransaction(TransferDTO transferDTO, Client client) throws Exception {
        // Imprimir los datos recibidos para debugging
        System.out.println("Amount: " + transferDTO.getAmount());
        System.out.println("Origin Account: " + transferDTO.getOriginAccountNumber());
        System.out.println("Destination Account: " + transferDTO.getDestinationAccountNumber());

        // Verificar que el monto sea mayor a cero
        if (transferDTO.getAmount() <= 0) {
            throw new IllegalArgumentException("The transaction amount must be greater than zero");
        }

        // Obtener la cuenta origen y verificar que exista
        Account originAccount = accountService.getAccountByNumber(transferDTO.getOriginAccountNumber());
        if (originAccount == null) {
            throw new IllegalArgumentException("Origin account does not exist");
        }

        // Verificar que la cuenta de origen pertenece al cliente autenticado
        if (!originAccount.getClient().equals(client)) {
            throw new IllegalArgumentException("Origin account does not belong to the authenticated client");
        }

        // Verificar que la cuenta de origen tenga suficiente saldo
        if (originAccount.getBalance() < transferDTO.getAmount()) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        // Verificar que la cuenta de origen y destino no sean la misma
        if (originAccount.getNumber().equals(transferDTO.getDestinationAccountNumber())) {
            throw new IllegalArgumentException("Origin and destination accounts cannot be the same");
        }

        // Obtener la cuenta destino y verificar que exista
        Account destinationAccount = accountService.getAccountByNumber(transferDTO.getDestinationAccountNumber());
        if (destinationAccount == null) {
            throw new IllegalArgumentException("Destination account does not exist");
        }

        // Procesar la transacción
        processTransaction(transferDTO.getAmount(), transferDTO.getOriginAccountNumber(), transferDTO.getDestinationAccountNumber(), originAccount, destinationAccount);
    }


    private void processTransaction(double amount, String originAccountNumber, String destinationAccountNumber, Account originAccount, Account destinationAccount) {
        // Crear la transacción de débito para la cuenta de origen
        Transaction debitTransaction = createTransaction(TransactionType.DEBIT, -amount, "Transfer to " + destinationAccountNumber, originAccount);
        transactionRepository.save(debitTransaction);

        // Crear la transacción de crédito para la cuenta de destino
        Transaction creditTransaction = createTransaction(TransactionType.CREDIT, amount, "Transfer from " + originAccountNumber, destinationAccount);
        transactionRepository.save(creditTransaction);

        // Actualizar los balances de las cuentas
        updateAccountBalances(originAccount, destinationAccount, amount);
    }

    private Transaction createTransaction(TransactionType type, double amount, String description, Account account) {
        return new Transaction(type, amount, description, LocalDateTime.now(), account);
    }

    private void updateAccountBalances(Account originAccount, Account destinationAccount, double amount) {
        originAccount.setBalance(originAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);
    }

}