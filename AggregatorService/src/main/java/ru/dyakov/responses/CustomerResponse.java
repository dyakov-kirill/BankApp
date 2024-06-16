package ru.dyakov.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.dyakov.entities.Customer;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CustomerResponse {
    private Customer customer;
}
