package com.yassine.bankingapi.service;

import com.yassine.bankingapi.dto.AccountDTO;
import com.yassine.bankingapi.exception.BadRequestException;
import com.yassine.bankingapi.exception.ResourceNotFoundException;
import com.yassine.bankingapi.model.Account;
import com.yassine.bankingapi.model.Account.AccountStatus;
import com.yassine.bankingapi.model.Customer;
import com.yassine.bankingapi.repository.AccountRepository;
import com.yassine.bankingapi.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    /**
     * Create a new account for a customer
     */
    @Transactional
    public Account createAccount(AccountDTO dto) {
        // 1. Find the customer
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + dto.getCustomerId()));

        // 2. Create the account
        Account account = new Account();
        account.setAccountType(dto.getAccountType());
        account.setCustomer(customer);
        account.setStatus(AccountStatus.ACTIVE);

        // 3. Set initial deposit if provided
        if (dto.getInitialDeposit() != null && dto.getInitialDeposit().compareTo(BigDecimal.ZERO) > 0) {
            account.setBalance(dto.getInitialDeposit());
        } else {
            account.setBalance(BigDecimal.ZERO);
        }

        // 4. Save and return
        return accountRepository.save(account);
    }

    /**
     * Get account by account number
     */
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountNumber));
    }

    /**
     * Get account by ID
     */
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
    }

    /**
     * Get balance of an account
     */
    public BigDecimal getBalance(String accountNumber) {
        Account account = getAccountByNumber(accountNumber);
        return account.getBalance();
    }

    /**
     * Get all accounts for a customer
     */
    public List<Account> getCustomerAccounts(Long customerId) {
        // Verify customer exists
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found with id: " + customerId);
        }
        return accountRepository.findByCustomerId(customerId);
    }

    /**
     * Get all accounts
     */
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    /**
     * Suspend an account
     */
    @Transactional
    public Account suspendAccount(String accountNumber) {
        Account account = getAccountByNumber(accountNumber);
        if (account.getStatus() == AccountStatus.CLOSED) {
            throw new BadRequestException("Cannot suspend a closed account");
        }
        account.setStatus(AccountStatus.SUSPENDED);
        return accountRepository.save(account);
    }

    /**
     * Activate an account
     */
    @Transactional
    public Account activateAccount(String accountNumber) {
        Account account = getAccountByNumber(accountNumber);
        if (account.getStatus() == AccountStatus.CLOSED) {
            throw new BadRequestException("Cannot activate a closed account");
        }
        account.setStatus(AccountStatus.ACTIVE);
        return accountRepository.save(account);
    }

    /**
     * Close an account
     */
    @Transactional
    public Account closeAccount(String accountNumber) {
        Account account = getAccountByNumber(accountNumber);
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new BadRequestException("Cannot close account with non-zero balance. Current balance: " + account.getBalance());
        }
        account.setStatus(AccountStatus.CLOSED);
        return accountRepository.save(account);
    }
}
