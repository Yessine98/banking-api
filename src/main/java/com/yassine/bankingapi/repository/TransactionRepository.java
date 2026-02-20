package com.yassine.bankingapi.repository;

import com.yassine.bankingapi.model.Transaction;
import com.yassine.bankingapi.model.Transaction.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountIdOrderByCreatedAtDesc(Long accountId);
    List<Transaction> findByAccountAccountNumberOrderByCreatedAtDesc(String accountNumber);
    
    // Paginated transactions for an account
    Page<Transaction> findByAccountAccountNumberOrderByCreatedAtDesc(String accountNumber, Pageable pageable);
    
    // Filter by date range
    @Query("SELECT t FROM Transaction t WHERE t.account.accountNumber = :accountNumber AND t.createdAt BETWEEN :startDate AND :endDate ORDER BY t.createdAt DESC")
    List<Transaction> findByAccountNumberAndDateRange(
            @Param("accountNumber") String accountNumber,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    // Filter by type
    List<Transaction> findByAccountAccountNumberAndTypeOrderByCreatedAtDesc(String accountNumber, TransactionType type);
    
    // Filter by date range and type
    @Query("SELECT t FROM Transaction t WHERE t.account.accountNumber = :accountNumber AND t.type = :type AND t.createdAt BETWEEN :startDate AND :endDate ORDER BY t.createdAt DESC")
    List<Transaction> findByAccountNumberAndTypeAndDateRange(
            @Param("accountNumber") String accountNumber,
            @Param("type") TransactionType type,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
