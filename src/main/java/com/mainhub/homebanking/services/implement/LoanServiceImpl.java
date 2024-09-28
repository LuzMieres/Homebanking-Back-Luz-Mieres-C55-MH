package com.mainhub.homebanking.services.implement;

import com.mainhub.homebanking.DTO.LoanDTO;
import com.mainhub.homebanking.models.*;
import com.mainhub.homebanking.models.type.TransactionType;
import com.mainhub.homebanking.repositories.*;
import com.mainhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();  // Obtiene todos los préstamos de la base de datos
    }

    @Override
    public List<LoanDTO> getAllLoanDTO() {
        List<Loan> loans = loanRepository.findAll();  // Obtiene todos los préstamos
        return loans.stream().map(LoanDTO::new).collect(Collectors.toList());  // Convierte cada préstamo en un DTO
    }

    @Override
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElse(null);  // Busca un préstamo por ID
    }

    @Override
    public LoanDTO getLoanDTO(Loan loan) {
        return new LoanDTO(loan);  // Convierte un objeto Loan a LoanDTO
    }

    @Override
    @Transactional
    public void applyForLoan(String loanName, double amount, int payments, String destinationAccountNumber, Client client) {
        // Buscar el préstamo por nombre
        Loan loan = findLoanByName(loanName);

        // Validaciones
        validateLoanApplication(loan, amount, payments, destinationAccountNumber);

        // Verificar la cuenta de destino
        Account destinationAccount = verifyDestinationAccount(destinationAccountNumber, client);

        // Calcular el monto total del préstamo con la tasa de interés
        double totalAmountWithInterest = calculateTotalAmountWithInterest(amount, payments);

        // Aplicar el préstamo al cliente
        applyLoanToClient(loan, amount, payments, destinationAccount, totalAmountWithInterest, client);
    }

    // Método para buscar un préstamo por nombre
    private Loan findLoanByName(String loanName) {
        List<Loan> loans = loanRepository.findByName(loanName);

        if (loans.isEmpty()) {
            throw new IllegalArgumentException("No loan found with name: " + loanName);
        } else if (loans.size() > 1) {
            return loans.get(0); // Retorna el primer préstamo encontrado
        } else {
            return loans.get(0); // Si solo hay un resultado, retorna ese
        }
    }

    // Validaciones de la aplicación del préstamo
    private void validateLoanApplication(Loan loan, double amount, int payments, String destinationAccountNumber) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Please enter a valid amount.");
        }
        if (payments <= 0) {
            throw new IllegalArgumentException("Please enter valid payments.");
        }
        if (destinationAccountNumber.isEmpty()) {
            throw new IllegalArgumentException("Please select a loan destination account.");
        }
        if (amount > loan.getMaxAmount()) {
            throw new IllegalArgumentException("Amount exceeds the maximum allowed for this loan.");
        }
        if (!loan.getPayments().contains(payments)) {
            throw new IllegalArgumentException("Invalid number of payments for this loan.");
        }
    }

    // Verificar la cuenta de destino y su propiedad
    private Account verifyDestinationAccount(String destinationAccountNumber, Client client) {
        List<Account> optionalAccount = accountRepository.findByNumber(destinationAccountNumber);

        // Verificar si la cuenta existe
        if (optionalAccount.isEmpty()) {
            throw new IllegalArgumentException("The account does not exist");
        }

        Account destinationAccount = optionalAccount.get(0);

        // Verificar si la cuenta pertenece al cliente autenticado
        if (!destinationAccount.getClient().equals(client)) {
            throw new IllegalArgumentException("Destination account does not belong to the authenticated client.");
        }

        return destinationAccount;
    }

    // Calcular el monto total con la tasa de interés
    private double calculateTotalAmountWithInterest(double amount, int payments) {
        double interestRate = getInterestRate(payments);
        return amount * (1 + interestRate);
    }

    // Obtener la tasa de interés según los pagos
    private double getInterestRate(int payments) {
        if (payments == 12) {
            return 0.20;  // 20%
        } else if (payments > 12) {
            return 0.25;  // 25%
        } else {
            return 0.15;  // 15%
        }
    }

    @Override
    public List<ClientLoan> getLoansByClient(Client client) {
        // Cambia a findByClient
        return clientLoanRepository.findByClient(client);
    }


    // Método que aplica el préstamo al cliente
    private void applyLoanToClient(Loan loan, double amount, int payments, Account destinationAccount, double totalAmountWithInterest, Client client) {
        // Crear la transacción de crédito solo con el monto solicitado
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, amount,
                "Approved " + loan.getName() + " loan.", LocalDateTime.now(), destinationAccount);
        transactionRepository.save(creditTransaction);

        // Actualizar el balance de la cuenta de destino solo con el monto solicitado
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);
        accountRepository.save(destinationAccount);

        // Crear y guardar la relación entre el cliente y el préstamo (ClientLoan)
        ClientLoan clientLoan = new ClientLoan(amount, payments, client, loan, destinationAccount);
        clientLoan.setTotalAmount(totalAmountWithInterest); // Asegúrate de que el totalAmount esté calculado correctamente con intereses
        clientLoanRepository.save(clientLoan);  // Guardar el ClientLoan

        // Asegurar que el Client y el Loan tengan referencias bidireccionales actualizadas
        client.addClientLoan(clientLoan);
        loan.addClientLoan(clientLoan);  // Añadir clientLoan al préstamo

        // Actualizar y guardar las entidades relacionadas
        clientRepository.save(client);  // Guardar el cliente actualizado
        loanRepository.save(loan);      // Guardar el préstamo actualizado
    }
}
