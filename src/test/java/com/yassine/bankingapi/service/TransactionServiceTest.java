package com.yassine.bankingapi.service;

import com.yassine.bankingapi.dto.TransactionDTO;
import com.yassine.bankingapi.dto.TransferDTO;
import com.yassine.bankingapi.exception.BadRequestException;
import com.yassine.bankingapi.exception.ResourceNotFoundException;
import com.yassine.bankingapi.model.Account;
import com.yassine.bankingapi.model.Account.AccountStatus;
import com.yassine.bankingapi.model.Account.AccountType;
import com.yassine.bankingapi.model.Transaction;
import com.yassine.bankingapi.model.Transaction.TransactionType;
import com.yassine.bankingapi.repository.AccountRepository;
import com.yassine.bankingapi.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionService Unit Tests")
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Account testAccount;
    private Account testAccount2;
    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setAccountNumber("ACC001");
        testAccount.setAccountType(AccountType.SAVINGS);
        testAccount.setBalance(new BigDecimal("1000.00"));
        testAccount.setStatus(AccountStatus.ACTIVE);
        testAccount.setCreatedAt(LocalDateTime.now());

        testAccount2 = new Account();
        testAccount2.setId(2L);
        testAccount2.setAccountNumber("ACC002");
        testAccount2.setAccountType(AccountType.CURRENT);
        testAccount2.setBalance(new BigDecimal("500.00"));
        testAccount2.setStatus(AccountStatus.ACTIVE);
        testAccount2.setCreatedAt(LocalDateTime.now());

        testTransaction = new Transaction();
        testTransaction.setId(1L);
        testTransaction.setTransactionReference("TXN001");
        testTransaction.setType(TransactionType.DEPOSIT);
        testTransaction.setAmount(new BigDecimal("100.00"));
        testTransaction.setBalanceAfter(new BigDecimal("1100.00"));
        testTransaction.setAccount(testAccount);
        testTransaction.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should deposit money successfully")
    void deposit_Success() {
        // Arrange
        TransactionDTO dto = new TransactionDTO();
        dto.setAccountNumber("ACC001");
        dto.setAmount(new BigDecimal("100.00"));
        dto.setDescription("Test deposit");

        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);

        // Act
        Transaction result = transactionService.deposit(dto);

        // Assert
        assertNotNull(result);
        assertEquals(TransactionType.DEPOSIT, result.getType());
        verify(accountRepository, times(1)).save(any(Account.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should throw exception for deposit with invalid amount")
    void deposit_InvalidAmount_ThrowsException() {
        // Arrange
        TransactionDTO dto = new TransactionDTO();
        dto.setAccountNumber("ACC001");
        dto.setAmount(new BigDecimal("-50.00"));

        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(Optional.of(testAccount));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> transactionService.deposit(dto));

        assertTrue(exception.getMessage().contains("Amount must be greater than 0"));
    }

    @Test
    @DisplayName("Should throw exception when depositing to non-existent account")
    void deposit_AccountNotFound_ThrowsException() {
        // Arrange
        TransactionDTO dto = new TransactionDTO();
        dto.setAccountNumber("INVALID");
        dto.setAmount(new BigDecimal("100.00"));

        when(accountRepository.findByAccountNumber("INVALID")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> transactionService.deposit(dto));
    }

    @Test
    @DisplayName("Should withdraw money successfully")
    void withdraw_Success() {
        // Arrange
        TransactionDTO dto = new TransactionDTO();
        dto.setAccountNumber("ACC001");
        dto.setAmount(new BigDecimal("100.00"));
        dto.setDescription("Test withdrawal");

        Transaction withdrawTransaction = new Transaction();
        withdrawTransaction.setType(TransactionType.WITHDRAWAL);
        withdrawTransaction.setAmount(new BigDecimal("100.00"));
        withdrawTransaction.setBalanceAfter(new BigDecimal("900.00"));

        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(withdrawTransaction);

        // Act
        Transaction result = transactionService.withdraw(dto);

        // Assert
        assertNotNull(result);
        assertEquals(TransactionType.WITHDRAWAL, result.getType());
    }

    @Test
    @DisplayName("Should throw exception for insufficient balance")
    void withdraw_InsufficientBalance_ThrowsException() {
        // Arrange
        TransactionDTO dto = new TransactionDTO();
        dto.setAccountNumber("ACC001");
        dto.setAmount(new BigDecimal("5000.00")); // More than balance

        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(Optional.of(testAccount));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> transactionService.withdraw(dto));

        assertTrue(exception.getMessage().contains("Insufficient balance"));
    }

    @Test
    @DisplayName("Should transfer money successfully")
    void transfer_Success() {
        // Arrange
        TransferDTO dto = new TransferDTO();
        dto.setFromAccountNumber("ACC001");
        dto.setToAccountNumber("ACC002");
        dto.setAmount(new BigDecimal("200.00"));
        dto.setDescription("Test transfer");

        Transaction outgoingTransaction = new Transaction();
        outgoingTransaction.setType(TransactionType.TRANSFER);
        outgoingTransaction.setAmount(new BigDecimal("200.00"));

        Transaction incomingTransaction = new Transaction();
        incomingTransaction.setType(TransactionType.TRANSFER);
        incomingTransaction.setAmount(new BigDecimal("200.00"));

        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(Optional.of(testAccount));
        when(accountRepository.findByAccountNumber("ACC002")).thenReturn(Optional.of(testAccount2));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArguments()[0]);
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(outgoingTransaction)
                .thenReturn(incomingTransaction);

        // Act
        List<Transaction> result = transactionService.transfer(dto);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should throw exception when transferring to same account")
    void transfer_SameAccount_ThrowsException() {
        // Arrange
        TransferDTO dto = new TransferDTO();
        dto.setFromAccountNumber("ACC001");
        dto.setToAccountNumber("ACC001");
        dto.setAmount(new BigDecimal("100.00"));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> transactionService.transfer(dto));

        assertTrue(exception.getMessage().contains("Cannot transfer to the same account"));
    }

    @Test
    @DisplayName("Should throw exception when account is suspended")
    void deposit_SuspendedAccount_ThrowsException() {
        // Arrange
        testAccount.setStatus(AccountStatus.SUSPENDED);
        TransactionDTO dto = new TransactionDTO();
        dto.setAccountNumber("ACC001");
        dto.setAmount(new BigDecimal("100.00"));

        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(Optional.of(testAccount));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> transactionService.deposit(dto));

        assertTrue(exception.getMessage().contains("Account is not active"));
    }
}
