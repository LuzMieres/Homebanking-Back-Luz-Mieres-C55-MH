package com.mainhub.homebanking.exeptions;

public class AccountNotBelongToClientException extends RuntimeException {
    public AccountNotBelongToClientException(String message) {
        super(message);
    }
}
