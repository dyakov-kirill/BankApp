package ru.dyakov.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deposit {

    private Integer id;

    private BankAccount bankAccount;

    private DepositType depositType;

    private Boolean depositRefill;

    private BigDecimal depositsAmount;

    private Date startDate;

    private Date endDate;

    private BigDecimal depositRate;

    private PaymentType typePercentPayment;

    private BankAccount percentPaymentAccount;

    private Date percentPaymentDate;

    private Boolean capitalization;

    private BankAccount depositRefundAccount;
}

