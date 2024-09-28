package com.mainhub.homebanking.repositories;

import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.ClientLoan;
import com.mainhub.homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientLoanRepository extends JpaRepository<ClientLoan, Long> {
    // Buscar préstamos por cliente
    List<ClientLoan> findByClient(Client client);

    // Buscar relación préstamo-cliente específica
    Optional<ClientLoan> findByLoanIdAndClient(Long loanId, Client client);

    Optional<Object> findByClientAndLoan(Client client, Loan loan);
}