package ru.dyakov.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDepositId implements Serializable {
    private Integer customer;
    private Integer deposit;
}