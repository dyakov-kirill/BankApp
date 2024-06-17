package ru.dyakov.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "current_request_status")
@IdClass(CurrentRequestStatusId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentRequestStatus {

    @Id
    @ManyToOne
    @JoinColumn(name = "request_id", referencedColumnName = "id_request")
    private Request request;

    @Id
    @ManyToOne
    @JoinColumn(name = "request_status_id", referencedColumnName = "id_request_status")
    private RequestStatus requestStatus;

    @Column(name = "change_datetime")
    private LocalDate changeDatetime;
}