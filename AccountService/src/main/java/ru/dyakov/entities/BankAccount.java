package ru.dyakov.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "bank_accounts")
@Getter
@Setter
@NoArgsConstructor
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_bank_accounts")
    private Integer id;

    @Column(name = "num_bank_accounts", precision = 20)
    private BigDecimal numBankAccounts;

    @Column(name = "amount")
    private BigDecimal amount;
}
