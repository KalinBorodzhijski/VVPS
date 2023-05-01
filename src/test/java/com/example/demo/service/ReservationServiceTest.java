package com.example.demo.service;

import com.example.demo.model.AppUser;
import com.example.demo.model.Reservation;
import com.example.demo.model.Ticket;
import com.example.demo.model.Train;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    @Mock
    TicketManagementService ticketManagementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteTickets() {
        Reservation demoReservation = new Reservation(1,getUser(),new ArrayList<>());
        List<Ticket> tickets = createDummyTickets(demoReservation);
        ticketManagementService.deleteTickets(tickets);
        assertEquals(Collections.emptyList(), demoReservation.getTickets());

    }

    private List<Ticket> createDummyTickets(Reservation demoReservation) {
        List<Ticket> result = new ArrayList<>();
        Ticket a = Ticket.builder()
                .train(getTrain())
                .price(5)
                .reservation(demoReservation)
                .ticketOwner(getUser())
                .ticketID(1L)
                .build();

        Ticket b = Ticket.builder()
                .train(getTrain())
                .price(12)
                .reservation(demoReservation)
                .ticketOwner(getUser())
                .ticketID(2L)
                .build();

        result.add(a);
        result.add(b);
        return result;
    }


    private static Train getTrain(){
        return new Train(1, LocalDateTime.of(2023,5,2,19,29,0),"Sofia","Plovdiv",10,150, new ArrayList<>());
    }

    private static AppUser getUser(){
        return new AppUser(1,"Kalin",false);
    }



}