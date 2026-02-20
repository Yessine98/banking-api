package com.yassine.bankingapi.service;

import com.yassine.bankingapi.dto.TransactionDTO;
import com.yassine.bankingapi.dto.TransferDTO;
import com.yassine.bankingapi.exception.BadRequestException;
import com.yassine.bankingapi.exception.ResourceNotFoundException;
import com.yassine.bankingapi.model.Account;
import com.yassine.bankingapi.model.Account.AccountStatus;
import com.yassine.bankingapi.model.Transaction;
import com.yassine.bankingapi.model.Transaction.TransactionType;
import com.yassine.bankingapi.repository.AccountRepository;
import com.yassine.bankingapi.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Deposit money into an account
     */
    @Transactional
    public Transaction deposit(TransactionDTO dto) {
        // 1. Find and validate the account
        Account account = findAndValidateAccount(dto.getAccountNumber());

        // 2. Validate amount
        validateAmount(dto.getAmount());

        // 3. Add to balance
        BigDecimal newBalance = account.getBalance().add(dto.getAmount());
        account.setBalance(newBalance);

        // 4. Create transaction record
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(dto.getAmount());
        transaction.setBalanceAfter(newBalance);
        transaction.setDescription(dto.getDescription() != null ? dto.getDescription() : "Deposit");
        transaction.setAccount(account);

        // 5. Save everything
        accountRepository.save(account);
        return transactionRepository.save(transaction);
    }

    /**
     * Withdraw money from an account
     */
    @Transactional
    public Transaction withdraw(TransactionDTO dto) {
        // 1. Find and validate the account
        Account account = findAndValidateAccount(dto.getAccountNumber());

        // 2. Validate amount
        validateAmount(dto.getAmount());

        // 3. Check sufficient balance
        if (account.getBalance().compareTo(dto.getAmount()) < 0) {
            throw new BadRequestException("Insufficient balance. Available: " + account.getBalance());
        }

        // 4. Subtract from balance
        BigDecimal newBalance = account.getBalance().subtract(dto.getAmount());
        account.setBalance(newBalance);

        // 5. Create transaction record
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setAmount(dto.getAmount());
        transaction.setBalanceAfter(newBalance);
        transaction.setDescription(dto.getDescription() != null ? dto.getDescription() : "Withdrawal");
        transaction.setAccount(account);

        // 6. Save everything
        accountRepository.save(account);
        return transactionRepository.save(transaction);
    }

    /**
     * Transfer money between two accounts
     */
    @Transactional
    public List<Transaction> transfer(TransferDTO dto) {
        // 1. Validate source and destination are different
        if (dto.getFromAccountNumber().equals(dto.getToAccountNumber())) {
            throw new BadRequestException("Cannot transfer to the same account");
        }

        // 2. Find and validate both accounts
        Account fromAccount = findAndValidateAccount(dto.getFromAccountNumber());
        Account toAccount = findAndValidateAccount(dto.getToAccountNumber());

        // 3. Validate amount
        validateAmount(dto.getAmount());

        // 4. Check sufficient balance in source account
        if (fromAccount.getBalance().compareTo(dto.getAmount()) < 0) {
            throw new BadRequestException("Insufficient balance. Available: " + fromAccount.getBalance());
        }

        // 5. Perform the transfer
        BigDecimal fromNewBalance = fromAccount.getBalance().subtract(dto.getAmount());
        BigDecimal toNewBalance = toAccount.getBalance().add(dto.getAmount());

        fromAccount.setBalance(fromNewBalance);
        toAccount.setBalance(toNewBalance);

        // 6. Create transaction records for both accounts
        String description = dto.getDescription() != null ? dto.getDescription() : "Transfer";

        // Outgoing transaction (from source account)
        Transaction outgoing = new Transaction();
        outgoing.setType(TransactionType.TRANSFER);
        outgoing.setAmount(dto.getAmount());
        outgoing.setBalanceAfter(fromNewBalance);
        outgoing.setDescription(description + " to " + dto.getToAccountNumber());
        outgoing.setAccount(fromAccount);
        outgoing.setDestinationAccountNumber(dto.getToAccountNumber());

        // Incoming transaction (to destination account)
        Transaction incoming = new Transaction();
        incoming.setType(TransactionType.TRANSFER);
        incoming.setAmount(dto.getAmount());
        incoming.setBalanceAfter(toNewBalance);
        incoming.setDescription(description + " from " + dto.getFromAccountNumber());
        incoming.setAccount(toAccount);
        incoming.setDestinationAccountNumber(dto.getFromAccountNumber());

        // 7. Save everything
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        Transaction savedOutgoing = transactionRepository.save(outgoing);
        Transaction savedIncoming = transactionRepository.save(incoming);

        return List.of(savedOutgoing, savedIncoming);
    }

    /**
     * Get all transactions for an account
     */
    public List<Transaction> getAccountTransactions(String accountNumber) {
        // Verify account exists
        if (!accountRepository.findByAccountNumber(accountNumber).isPresent()) {
            throw new ResourceNotFoundException("Account not found: " + accountNumber);
        }
        return transactionRepository.findByAccountAccountNumberOrderByCreatedAtDesc(accountNumber);
    }

    /**
     * Get transactions with date range filter
     */
    public List<Transaction> getAccountTransactionsByDateRange(String accountNumber, LocalDate startDate, LocalDate endDate) {
        if (!accountRepository.findByAccountNumber(accountNumber).isPresent()) {
            throw new ResourceNotFoundException("Account not found: " + accountNumber);
        }
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);
        return transactionRepository.findByAccountNumberAndDateRange(accountNumber, start, end);
    }

    /**
     * Get transactions by type
     */
    public List<Transaction> getAccountTransactionsByType(String accountNumber, TransactionType type) {
        if (!accountRepository.findByAccountNumber(accountNumber).isPresent()) {
            throw new ResourceNotFoundException("Account not found: " + accountNumber);
        }
        return transactionRepository.findByAccountAccountNumberAndTypeOrderByCreatedAtDesc(accountNumber, type);
    }

    /**
     * Get transactions with date range and type filter
     */
    public List<Transaction> getAccountTransactionsFiltered(String accountNumber, TransactionType type, LocalDate startDate, LocalDate endDate) {
        if (!accountRepository.findByAccountNumber(accountNumber).isPresent()) {
            throw new ResourceNotFoundException("Account not found: " + accountNumber);
        }
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);
        return transactionRepository.findByAccountNumberAndTypeAndDateRange(accountNumber, type, start, end);
    }

    /**
     * Get transaction by ID
     */
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
    }

    /**
     * Helper method to find and validate an account
     */
    private Account findAndValidateAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountNumber));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new BadRequestException("Account is not active. Status: " + account.getStatus());
        }

        return account;
    }

    /**
     * Helper method to validate amount
     */
    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Amount must be greater than 0");
        }
    }
}
