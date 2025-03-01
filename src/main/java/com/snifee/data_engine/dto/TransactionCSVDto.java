package com.snifee.data_engine.dto;


import java.sql.Timestamp;

public record TransactionCSVDto(
        Timestamp timestamp,
        String fromBank,
        String account1,
        String toBank,
        String account2,
        String amountReceived,
        String receivingCurrency,
        String amountPaid,
        String paymentCurrency,
        String paymentFormat,
        boolean isLaundering
){}
