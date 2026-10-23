package com.example.settlement;

public class InvalidTransactionException extends RuntimeException {
    public InvalidTransactionException(long id, String reason) {
        super("Transaction " + id + ": " + reason);
    }
}
