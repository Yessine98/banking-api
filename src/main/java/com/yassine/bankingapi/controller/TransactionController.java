package com.yassine.bankingapi.controller;

import com.yassine.bankingapi.dto.TransactionDTO;
import com.yassine.bankingapi.dto.TransactionResponse;
import com.yassine.bankingapi.dto.TransferDTO;
import com.yassine.bankingapi.model.Transaction;
import com.yassine.bankingapi.model.Transaction.TransactionType;
import com.yassine.bankingapi.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions", description = "Banking transaction APIs")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Deposit money into an account
     */
    @PostMapping("/deposit")
    @Operation(summary = "Deposit money", description = "Deposit money into a bank account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Deposit successful"),
            @ApiResponse(responseCode = "400", description = "Invalid amount or account not active"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody TransactionDTO dto) {
        Transaction transaction = transactionService.deposit(dto);
        return new ResponseEntity<>(TransactionResponse.fromTransaction(transaction), HttpStatus.CREATED);
    }

    /**
     * Withdraw money from an account
     */
    @PostMapping("/withdraw")
    @Operation(summary = "Withdraw money", description = "Withdraw money from a bank account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Withdrawal successful"),
            @ApiResponse(responseCode = "400", description = "Insufficient balance, invalid amount, or account not active"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody TransactionDTO dto) {
        Transaction transaction = transactionService.withdraw(dto);
        return new ResponseEntity<>(TransactionResponse.fromTransaction(transaction), HttpStatus.CREATED);
    }

    /**
     * Transfer money between two accounts
     */
    @PostMapping("/transfer")
    @Operation(summary = "Transfer money", description = "Transfer money between two bank accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transfer successful"),
            @ApiResponse(responseCode = "400", description = "Insufficient balance, invalid amount, same account, or accounts not active"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<List<TransactionResponse>> transfer(@Valid @RequestBody TransferDTO dto) {
        List<Transaction> transactions = transactionService.transfer(dto);
        List<TransactionResponse> responses = transactions.stream()
                .map(TransactionResponse::fromTransaction)
                .toList();
        return new ResponseEntity<>(responses, HttpStatus.CREATED);
    }

    /**
     * Get all transactions for an account
     */
    @GetMapping("/account/{accountNumber}")
    @Operation(summary = "Get account transactions", description = "Get all transactions for a specific account")
    @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully")
    public ResponseEntity<List<TransactionResponse>> getAccountTransactions(
            @Parameter(description = "Account number") @PathVariable String accountNumber) {
        List<TransactionResponse> responses = transactionService.getAccountTransactions(accountNumber).stream()
                .map(TransactionResponse::fromTransaction)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Get transactions filtered by date range
     */
    @GetMapping("/account/{accountNumber}/filter")
    @Operation(summary = "Get filtered transactions", description = "Get transactions with optional date range and type filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered transactions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<List<TransactionResponse>> getFilteredTransactions(
            @Parameter(description = "Account number") @PathVariable String accountNumber,
            @Parameter(description = "Transaction type (DEPOSIT, WITHDRAWAL, TRANSFER)") @RequestParam(required = false) TransactionType type,
            @Parameter(description = "Start date (yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<Transaction> transactions;
        
        if (type != null && startDate != null && endDate != null) {
            transactions = transactionService.getAccountTransactionsFiltered(accountNumber, type, startDate, endDate);
        } else if (startDate != null && endDate != null) {
            transactions = transactionService.getAccountTransactionsByDateRange(accountNumber, startDate, endDate);
        } else if (type != null) {
            transactions = transactionService.getAccountTransactionsByType(accountNumber, type);
        } else {
            transactions = transactionService.getAccountTransactions(accountNumber);
        }
        
        List<TransactionResponse> responses = transactions.stream()
                .map(TransactionResponse::fromTransaction)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Get transaction by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID", description = "Retrieve a specific transaction by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction found"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    public ResponseEntity<TransactionResponse> getTransactionById(
            @Parameter(description = "Transaction ID") @PathVariable Long id) {
        return ResponseEntity.ok(TransactionResponse.fromTransaction(transactionService.getTransactionById(id)));
    }
}
