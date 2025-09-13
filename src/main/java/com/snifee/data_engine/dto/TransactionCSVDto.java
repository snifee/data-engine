package com.snifee.data_engine.dto;


public record TransactionCSVDto(
        String timestamp,
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
