package com.yassine.bankingapi.controller;

import com.yassine.bankingapi.dto.AccountDTO;
import com.yassine.bankingapi.dto.AccountResponse;
import com.yassine.bankingapi.model.Account;
import com.yassine.bankingapi.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts", description = "Bank account management APIs")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Create a new account
     */
    @PostMapping
    @Operation(summary = "Create account", description = "Create a new bank account for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountDTO dto) {
        Account account = accountService.createAccount(dto);
        return new ResponseEntity<>(AccountResponse.fromAccount(account), HttpStatus.CREATED);
    }

    /**
     * Get all accounts
     */
    @GetMapping
    @Operation(summary = "Get all accounts", description = "Retrieve all bank accounts")
    @ApiResponse(responseCode = "200", description = "List of accounts retrieved successfully")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<AccountResponse> responses = accountService.getAllAccounts().stream()
                .map(AccountResponse::fromAccount)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Get account by account number
     */
    @GetMapping("/{accountNumber}")
    @Operation(summary = "Get account by number", description = "Retrieve account details by account number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<AccountResponse> getAccountByNumber(
            @Parameter(description = "Account number") @PathVariable String accountNumber) {
        return ResponseEntity.ok(AccountResponse.fromAccount(accountService.getAccountByNumber(accountNumber)));
    }

    /**
     * Get account balance
     */
    @GetMapping("/{accountNumber}/balance")
    @Operation(summary = "Get account balance", description = "Get the current balance of an account")
    @ApiResponse(responseCode = "200", description = "Balance retrieved successfully")
    public ResponseEntity<BigDecimal> getBalance(
            @Parameter(description = "Account number") @PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getBalance(accountNumber));
    }

    /**
     * Get all accounts for a customer
     */
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get customer accounts", description = "Get all accounts belonging to a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<List<AccountResponse>> getCustomerAccounts(
            @Parameter(description = "Customer ID") @PathVariable Long customerId) {
        List<AccountResponse> responses = accountService.getCustomerAccounts(customerId).stream()
                .map(AccountResponse::fromAccount)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Suspend an account (ADMIN only)
     */
    @PutMapping("/{accountNumber}/suspend")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Suspend account", description = "Suspend a bank account (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account suspended successfully"),
            @ApiResponse(responseCode = "400", description = "Cannot suspend closed account"),
            @ApiResponse(responseCode = "403", description = "Access denied - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<AccountResponse> suspendAccount(
            @Parameter(description = "Account number") @PathVariable String accountNumber) {
        return ResponseEntity.ok(AccountResponse.fromAccount(accountService.suspendAccount(accountNumber)));
    }

    /**
     * Activate an account (ADMIN only)
     */
    @PutMapping("/{accountNumber}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activate account", description = "Activate a bank account (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account activated successfully"),
            @ApiResponse(responseCode = "400", description = "Cannot activate closed account"),
            @ApiResponse(responseCode = "403", description = "Access denied - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<AccountResponse> activateAccount(
            @Parameter(description = "Account number") @PathVariable String accountNumber) {
        return ResponseEntity.ok(AccountResponse.fromAccount(accountService.activateAccount(accountNumber)));
    }

    /**
     * Close an account (ADMIN only)
     */
    @PutMapping("/{accountNumber}/close")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Close account", description = "Close a bank account (Admin only, balance must be zero)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account closed successfully"),
            @ApiResponse(responseCode = "400", description = "Cannot close account with non-zero balance"),
            @ApiResponse(responseCode = "403", description = "Access denied - Admin role required"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<AccountResponse> closeAccount(
            @Parameter(description = "Account number") @PathVariable String accountNumber) {
        return ResponseEntity.ok(AccountResponse.fromAccount(accountService.closeAccount(accountNumber)));
    }
}
