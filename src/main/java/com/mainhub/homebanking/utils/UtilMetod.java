package com.mainhub.homebanking.utils;

import com.mainhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;

@Component
public class UtilMetod {
    @Autowired
    public AccountRepository accountRepository;
    private static final SecureRandom random = new SecureRandom();

    public String generateAccountNumber() {
        String prefix = "VIN";
        int maxAccountNumber = accountRepository.findMaxAccountNumberByPrefix(prefix);

        // Generar el siguiente número en la secuencia
        int nextAccountNumber = maxAccountNumber + 1;
        String formattedNumber = String.format("%03d", nextAccountNumber); // Formato de 3 dígitos con ceros a la izquierda

        return prefix + formattedNumber;
    }

    public String generateCardNumber() {
        return String.format("%04d %04d %04d %04d", generateRandomDigits(4), generateRandomDigits(4), generateRandomDigits(4), generateRandomDigits(4));
    }

    public String generateCvv() {
        return String.format("%03d", generateRandomDigits(3));
    }

    private int generateRandomDigits(int digits) {
        int multiplier = (int) Math.pow(10, digits - 1);
        return random.nextInt(9 * multiplier) + multiplier;
    }

    public LocalDate getThruDate() {
        return LocalDate.now().plusYears(5);
    }
}