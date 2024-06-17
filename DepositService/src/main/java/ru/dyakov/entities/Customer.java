package ru.dyakov.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_customers")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "bank_account_id", referencedColumnName = "id_bank_accounts")
    private BankAccount bankAccount;

    @Column(name = "phone_number")
    private String phoneNumber;
}