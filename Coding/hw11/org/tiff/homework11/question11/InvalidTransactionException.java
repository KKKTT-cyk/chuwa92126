package org.tiff.homework11.question11;

public class InvalidTransactionException extends RuntimeException {
    public InvalidTransactionException(long id, String reason) {
        super("Transaction " + id + ": " + reason);
    }
}
