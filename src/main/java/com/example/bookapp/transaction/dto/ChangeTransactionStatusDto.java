package com.example.bookapp.transaction.dto;

import javax.validation.constraints.NotNull;

public class ChangeTransactionStatusDto {
    @NotNull(message = "Transaction Id is required")
    public int transactionId;

    @NotNull(message = "Transaction status is required")
    public TransactionStatus transactionStatus;
}
