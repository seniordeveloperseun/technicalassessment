package com.banking.app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction")
@SQLDelete(sql = "UPDATE transaction SET is_deleted = TRUE WHERE id = ?")
@Where(clause = "is_deleted = FALSE")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    public enum TransactionStatus {
        PENDING, PROCESSING, SUCCESS, FAILED
    }

    public enum TransactionType {
        CREDIT, DEBIT
    }

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_reference", nullable = false)
    private String paymentReference;

    @Column(name = "debit_account_number", nullable = true)
    private String debitAccountNumber;

    @Column(name = "debit_account_name", nullable = true)
    private String debitAccountName;

    @Column(name = "credit_account_number", nullable = true)
    private String creditAccountNumber;

    @Column(name = "credit_account_name", nullable = true)
    private String creditAccountName;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default 'false'")
    private Boolean isDeleted;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @CurrentTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updateddAt;
}
