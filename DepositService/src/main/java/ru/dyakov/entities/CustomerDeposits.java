package ru.dyakov.entities;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "customer_deposits")
@IdClass(CustomerDepositId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDeposits {

    @Id
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id_customers")
    private Customer customer;

    @Id
    @ManyToOne
    @JoinColumn(name = "deposit_id", referencedColumnName = "id_deposit")
    private Deposit deposit;
}

