package com.mainhub.homebanking.controllers;

import com.mainhub.homebanking.DTO.LoanAplicationDTO;
import com.mainhub.homebanking.DTO.LoanDTO;
import com.mainhub.homebanking.exeptions.ClientNotFoundException;
import com.mainhub.homebanking.models.Client;
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

    @GetMapping("/")
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        return new ResponseEntity<>(loanService.getAllLoanDTO(), HttpStatus.OK);
    }

    @GetMapping("/clientLoans")
    public ResponseEntity<List<LoanDTO>> getClientLoans(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        List<LoanDTO> clientLoans = loanService.getLoansByClient(client).stream()
                .map(LoanDTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(clientLoans, HttpStatus.OK);
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