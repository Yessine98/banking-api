package com.yassine.bankingapi.dto;

import com.yassine.bankingapi.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    private Long id;
    private String transactionReference;
    private String type;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String description;
    private String accountNumber;
    private String destinationAccountNumber;
    private LocalDateTime createdAt;

    public static TransactionResponse fromTransaction(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setTransactionReference(transaction.getTransactionReference());
        response.setType(transaction.getType().name());
        response.setAmount(transaction.getAmount());
        response.setBalanceAfter(transaction.getBalanceAfter());
        response.setDescription(transaction.getDescription());
        response.setAccountNumber(transaction.getAccount().getAccountNumber());
        response.setDestinationAccountNumber(transaction.getDestinationAccountNumber());
        response.setCreatedAt(transaction.getCreatedAt());
        return response;
    }
}
