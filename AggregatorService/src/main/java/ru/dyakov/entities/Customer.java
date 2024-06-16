package ru.dyakov.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class Customer {

    private Integer id;

    private BankAccount bankAccount;

    private String phoneNumber;
}