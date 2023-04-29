package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "train")
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long trainID;
    private LocalDateTime timeOfDeparture;
    private String departureStation;
    private String arrivalStation;
    private int trainCapacity;
    private int distance;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ticket_id")
    @JsonManagedReference//Fixing adding ticket exception
    private List<Ticket> tickets;


}
