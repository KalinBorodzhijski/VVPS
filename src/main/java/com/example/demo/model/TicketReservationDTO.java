package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TicketReservationDTO {

    private String appUserName;
    private String departureStation;
    private String arrivalStation;
    private LocalDateTime departureTime;
    private long reservationID;
    private boolean oneWay;
    private boolean elder;
    private boolean withChild;
    private boolean hasFamilyCard;

}
