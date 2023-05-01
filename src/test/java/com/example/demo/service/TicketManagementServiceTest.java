package com.example.demo.service;

import com.example.demo.model.TicketReservationDTO;
import com.example.demo.model.Train;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TicketManagementServiceTest {

    @InjectMocks
    private TicketManagementService ticketManagementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPriceOfCourseWithoutDiscountsAndInRushHour(){
        Train train = getTrain();
        TicketReservationDTO ticketReservationDTO = new TicketReservationDTO("Ivan",
                "Sofia",
                "Plovdiv",
                LocalDateTime.parse("2023-03-24T19:30"),
                1,
                true,
                false,
                false,
                false);
        double price = ticketManagementService.calculateTicketPrice(train,ticketReservationDTO);
        assertEquals(15,price);

    }

    @Test
    void testGetPriceOfCourseWithElderDiscountAndInRushHour(){
        Train train = getTrain();
        TicketReservationDTO ticketReservationDTO = new TicketReservationDTO("Ivan",
                "Sofia",
                "Plovdiv",
                LocalDateTime.parse("2023-03-24T19:30"),
                1,
                true,
                true,
                false,
                false);
        double price = ticketManagementService.calculateTicketPrice(train,ticketReservationDTO);
        assertEquals(9.9,price,0.1);
    }

    @Test
    void testGetPriceOfCourseWithChildDiscountAndInRushHour(){
        Train train = getTrain();
        TicketReservationDTO ticketReservationDTO = new TicketReservationDTO("Ivan",
                "Sofia",
                "Plovdiv",
                LocalDateTime.parse("2023-03-24T19:30"),
                1,
                true,
                false,
                true,
                false);
        double price = ticketManagementService.calculateTicketPrice(train,ticketReservationDTO);
        assertEquals(13.5,price,0.1);
    }

    @Test
    void testGetPriceOfCourseWithChildAndFamilyCardDiscountAndInRushHour(){
        Train train = getTrain();
        TicketReservationDTO ticketReservationDTO = new TicketReservationDTO("Ivan",
                "Sofia",
                "Plovdiv",
                LocalDateTime.parse("2023-03-24T19:30"),
                1,
                true,
                false,
                true,
                true);
        double price = ticketManagementService.calculateTicketPrice(train,ticketReservationDTO);
        assertEquals(7.5,price,0.1);
    }

    @Test
    void testGetPriceOfCourseWithoutDiscountAndInRushHourTwoWay(){
        Train train = getTrain();
        TicketReservationDTO ticketReservationDTO = new TicketReservationDTO("Ivan",
                "Sofia",
                "Plovdiv",
                LocalDateTime.parse("2023-03-24T19:30"),
                1,
                false,
                false,
                false,
                false);
        double price = ticketManagementService.calculateTicketPrice(train,ticketReservationDTO);
        assertEquals(30,price,0.1);
    }


    @Test
    void testGetPriceOfCourseWithoutDiscountsAndNotInRushHour(){
        Train train = getTrainNotInRushHour();
        TicketReservationDTO ticketReservationDTO = new TicketReservationDTO("Ivan",
                "Sofia",
                "Plovdiv",
                LocalDateTime.parse("2023-03-24T19:30"),
                1,
                true,
                false,
                false,
                false);
        double price = ticketManagementService.calculateTicketPrice(train,ticketReservationDTO);
        assertEquals(14.25,price,0.01);

    }

    @Test
    void testGetPriceOfCourseWithElderDiscountAndNotInRushHour(){
        Train train = getTrainNotInRushHour();
        TicketReservationDTO ticketReservationDTO = new TicketReservationDTO("Ivan",
                "Sofia",
                "Plovdiv",
                LocalDateTime.parse("2023-03-24T19:30"),
                1,
                true,
                true,
                false,
                false);
        double price = ticketManagementService.calculateTicketPrice(train,ticketReservationDTO);
        assertEquals(9.15,price,0.01);
    }

    @Test
    void testGetPriceOfCourseWithChildDiscountAndNotInRushHour(){
        Train train = getTrainNotInRushHour();
        TicketReservationDTO ticketReservationDTO = new TicketReservationDTO("Ivan",
                "Sofia",
                "Plovdiv",
                LocalDateTime.parse("2023-03-24T19:30"),
                1,
                true,
                false,
                true,
                false);
        double price = ticketManagementService.calculateTicketPrice(train,ticketReservationDTO);
        assertEquals(12.75,price,0.01);
    }

    @Test
    void testGetPriceOfCourseWithChildAndFamilyCardDiscountAndNotInRushHour(){
        Train train = getTrainNotInRushHour();
        TicketReservationDTO ticketReservationDTO = new TicketReservationDTO("Ivan",
                "Sofia",
                "Plovdiv",
                LocalDateTime.parse("2023-03-24T19:30"),
                1,
                true,
                false,
                true,
                true);
        double price = ticketManagementService.calculateTicketPrice(train,ticketReservationDTO);
        assertEquals(6.75,price,0.01);
    }

    //Reserve ticket

    //Update ticket

    //Delete ticket


    private static Train getTrain(){
        return new Train(1, LocalDateTime.of(2023,5,2,19,29,0),"Sofia","Plovdiv",10,150, new ArrayList<>());
    }


    private Train getTrainNotInRushHour() {
        return new Train(1, LocalDateTime.of(2023,5,2,19,31,0),"Sofia","Plovdiv",10,150, new ArrayList<>());
    }


}