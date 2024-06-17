package ru.dyakov.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "types_percent_payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_type_percent_payment")
    private Integer id;

    @Column(name = "type_percent_payment_period")
    private String typePercentPaymentPeriod;
}
