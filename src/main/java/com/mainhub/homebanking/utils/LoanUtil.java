package com.mainhub.homebanking.utils;

import org.springframework.stereotype.Component;

@Component
public class LoanUtil {
    public static double calculateTotalAmountWithInterest(double amount, int payments) {
        double interestRate = getInterestRate(payments);
        return amount * (1 + interestRate);
    }

    private static double getInterestRate(int payments) {
        if (payments == 12) {
            return 0.20;  // 20%
        } else if (payments > 12) {
            return 0.25;  // 25%
        } else {
            return 0.15;  // 15%
        }
    }
}

