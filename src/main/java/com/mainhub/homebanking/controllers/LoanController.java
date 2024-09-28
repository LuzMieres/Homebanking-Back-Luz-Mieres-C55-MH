package com.mainhub.homebanking.controllers;

import com.mainhub.homebanking.DTO.ClientLoanDTO;
import com.mainhub.homebanking.DTO.LoanAplicationDTO;
import com.mainhub.homebanking.DTO.LoanDTO;
import com.mainhub.homebanking.exeptions.ClientNotFoundException;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.ClientLoan;
import com.mainhub.homebanking.models.Loan;
import com.mainhub.homebanking.services.ClientLoanService;
import com.mainhub.homebanking.services.ClientServices;
import com.mainhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private ClientServices clientService;

    @Autowired
    private ClientLoanService clientLoanService;

    @GetMapping("/")
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        return new ResponseEntity<>(loanService.getAllLoanDTO(), HttpStatus.OK);
    }

    @GetMapping("/clientLoans")
    public ResponseEntity<List<ClientLoanDTO>> getClientLoans(Authentication authentication) {
        // Obtiene el cliente autenticado usando el email
        Client client = clientService.findByEmail(authentication.getName());

        // Obtener los préstamos del cliente
        List<ClientLoanDTO> clientLoans = clientLoanService.getLoansByClient(client);

        return new ResponseEntity<>(clientLoans, HttpStatus.OK);
    }

    @GetMapping("/clientLoans/{loanId}")
    public ResponseEntity<?> getClientLoanByLoanId(Authentication authentication, @PathVariable Long loanId) {
        // Obtener al cliente autenticado
        Client client = clientService.findByEmail(authentication.getName());

        // Obtener el préstamo específico
        Loan loan = loanService.getLoanById(loanId);
        if (loan == null) {
            return new ResponseEntity<>("Loan not found", HttpStatus.NOT_FOUND); // Corregido a String
        }

        // Obtener el préstamo del cliente
        ClientLoanDTO clientLoan = clientLoanService.getClientLoanByClientAndLoan(client, loan);
        return new ResponseEntity<>(clientLoan, HttpStatus.OK);
    }


    @PostMapping("/apply")
    public ResponseEntity<?> applyForLoan(@RequestBody LoanAplicationDTO loanApplicationDTO, Authentication authentication) {
        // Obtener al cliente autenticado
        Client client = clientService.findByEmail(authentication.getName());
        try {
            loanService.applyForLoan(loanApplicationDTO.getLoanName(), loanApplicationDTO.getAmount(),
                    loanApplicationDTO.getPayments(), loanApplicationDTO.getDestinationAccountNumber(), client);
            return new ResponseEntity<>("Loan application successful", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ClientNotFoundException e) {
            return new ResponseEntity<>("Client not found", HttpStatus.FORBIDDEN);
        }
    }
}
