package com.mainhub.homebanking.repositories;

import com.mainhub.homebanking.DTO.ClientLoanDTO;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.ClientLoan;
import com.mainhub.homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientLoanRepository extends JpaRepository<ClientLoan, Long> {
    // Buscar todos los préstamos de un cliente
    List<ClientLoan> findByClient(Client client);

    // Buscar un préstamo específico de un cliente
    Optional<ClientLoan> findByClientAndLoan(Client client, Loan loan);
}

