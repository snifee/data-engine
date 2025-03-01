package com.snifee.data_engine.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "transaction_laundering")
@Data
public class Transaction {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column(name = "source_bank")
        private String sourceBank;

        @Column(name = "dest_bank")
        private String destBank;

        @Column(name = "account")
        private String account;

        @Column(name="receiving_currency")
        private String receivingCurrency;

        @Column(name = "amount")
        private double amount;

        @Column(name="payment_currency")
        private String paymentCurrency;

        @Column(name="payment_format")
        private String paymentFormat;

        @Column(name="is_laundering")
        private boolean isLaundering;
}
