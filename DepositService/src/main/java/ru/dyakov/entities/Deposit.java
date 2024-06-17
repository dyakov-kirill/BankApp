package ru.dyakov.entities;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "deposits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_deposit")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "deposit_account_id", referencedColumnName = "id_bank_accounts")
    private BankAccount bankAccount;

    @ManyToOne
    @JoinColumn(name = "deposit_type_id", referencedColumnName = "id_deposits_types")
    private DepositType depositType;

    @Column(name = "deposit_refill")
    private Boolean depositRefill;

    @Column(name = "deposits_amount")
    private BigDecimal depositsAmount;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "deposit_rate")
    private BigDecimal depositRate;

    @ManyToOne
    @JoinColumn(name = "type_percent_payment_id", referencedColumnName = "id_type_percent_payment")
    private PaymentType typePercentPayment;

    @ManyToOne
    @JoinColumn(name = "percent_payment_account_id", referencedColumnName = "id_bank_accounts")
    private BankAccount percentPaymentAccount;

    @Column(name = "percent_payment_date")
    private Date percentPaymentDate;

    @Column(name = "capitalization")
    private Boolean capitalization;

    @ManyToOne
    @JoinColumn(name = "deposit_refund_account_id", referencedColumnName = "id_bank_accounts")
    private BankAccount depositRefundAccount;
}

