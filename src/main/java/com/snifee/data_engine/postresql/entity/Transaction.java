package com.snifee.data_engine.postresql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Entity(name = "transaction_laundering")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        @Column(name="id")
        private UUID id;

        @Column(name="datetime")
        private Timestamp datetime;

        @Column(name="source_bank")
        private String sourceBank;

        @Column(name="sender_account")
        private String senderAccount;

        @Column(name="destination_bank")
        private String destinationBank;

        @Column(name="receiver_account")
        private String receiverAccount;

        @Column(name="amount_received")
        private double amountReceived;

        @Column(name="receiving_currency")
        private String receivingCurrency;

        @Column(name="amount_paid")
        private double amountPaid;

        @Column(name="payment_currency")
        private String paymentCurrency;

        @Column(name="payment_format")
        private String paymentFormat;

        @Column(name="is_laundering")
        private boolean laundering;
}
