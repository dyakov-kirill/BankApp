package ru.dyakov.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class WithdrawRequest {
    private BigDecimal value;
}
