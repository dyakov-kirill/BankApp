package ru.dyakov.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    private Integer id;

    private Customer customer;

    private Date requestDate;

    private Deposit deposit;
}