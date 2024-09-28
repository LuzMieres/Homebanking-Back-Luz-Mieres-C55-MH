package com.mainhub.homebanking.services;

import com.mainhub.homebanking.DTO.LoanDTO;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {
    List<Loan> getAllLoans();  // Método para obtener todos los préstamos
    List<LoanDTO> getAllLoanDTO();  // Método para obtener todos los préstamos como DTO
    Loan getLoanById(Long id);  // Método para obtener un préstamo por ID
    LoanDTO getLoanDTO(Loan loan);  // Método para convertir un préstamo a DTO
    void applyForLoan(String loanName, double amount, int payments, String destinationAccountNumber, Client client);  // Solicitar un préstamo basado en el nombre

    // Asegúrate de que este método esté declarado en la interfaz
    List<Loan> getLoansByClient(Client client);
}