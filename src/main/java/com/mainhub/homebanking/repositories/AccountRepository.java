package com.mainhub.homebanking.repositories;

import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(a.number, 4, LENGTH(a.number)) AS int)), 0) FROM Account a WHERE a.number LIKE ?1%")
    int findMaxAccountNumberByPrefix(String prefix);

    List<Account> findByClient(Client client);

    // Método corregido para buscar una única cuenta por su número y cliente
    Optional<Account> findByNumberAndClient(String sourceAccountNumber, Client client);

    List<Account> findByNumber(String number);
}