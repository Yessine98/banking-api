package com.yassine.bankingapi.dto;

import com.yassine.bankingapi.model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {

    private Long id;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private String status;
    private Long customerId;
    private String customerName;
    private LocalDateTime createdAt;

    public static AccountResponse fromAccount(Account account) {
        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountType(account.getAccountType().name());
        response.setBalance(account.getBalance());
        response.setStatus(account.getStatus().name());
        response.setCustomerId(account.getCustomer().getId());
        response.setCustomerName(account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName());
        response.setCreatedAt(account.getCreatedAt());
        return response;
    }
}
