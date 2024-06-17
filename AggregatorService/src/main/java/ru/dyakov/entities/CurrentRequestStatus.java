package ru.dyakov.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentRequestStatus {

    private Request request;
    private RequestStatus requestStatus;
    private LocalDate changeDatetime;
}