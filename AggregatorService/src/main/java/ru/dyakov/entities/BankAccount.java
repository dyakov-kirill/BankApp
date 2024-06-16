package ru.dyakov.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class BankAccount {

    private Integer id;

    private BigDecimal numBankAccounts;

    private BigDecimal amount;
}
