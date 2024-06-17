package ru.dyakov.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_request")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id_customers")
    private Customer customer;

    @Column(name = "request_date")
    private Date requestDate;

    @ManyToOne
    @JoinColumn(name = "deposits_id", referencedColumnName = "id_deposit")
    private Deposit deposit;
}