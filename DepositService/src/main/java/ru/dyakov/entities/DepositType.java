package ru.dyakov.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "deposits_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_deposits_types")
    private Integer id;

    @Column(name = "deposits_types_name")
    private String depositsTypesName;
}