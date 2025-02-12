package ru.dyakov.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDeposits {

    private Customer customer;
    private Deposit deposit;
}

