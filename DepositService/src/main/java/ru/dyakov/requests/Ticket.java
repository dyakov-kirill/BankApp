package ru.dyakov.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    private int depositType;
    private int depositTerm;
    private BigDecimal amount;
    private int interestPayment;
    private boolean capitalization;
    private BigDecimal withdrawalAccount;
    private BigDecimal interestPaymentAccount;
    private BigDecimal returnAccount;
    private int requestId;
}
