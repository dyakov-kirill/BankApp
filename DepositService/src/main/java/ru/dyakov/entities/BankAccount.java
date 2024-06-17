package ru.dyakov.entities;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "bank_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_bank_accounts")
    private Integer id;

    @Column(name = "num_bank_accounts")
    private BigDecimal numBankAccounts;

    @Column(name = "amount")
    private BigDecimal amount;
}
