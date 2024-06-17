package ru.dyakov.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "request_statuses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_request_status")
    private Integer id;

    @Column(name = "request_status_name")
    private String requestStatusName;
}