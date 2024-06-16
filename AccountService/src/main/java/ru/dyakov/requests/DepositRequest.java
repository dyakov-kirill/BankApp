package ru.dyakov.requests;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class DepositRequest {
    private BigDecimal value;
}
