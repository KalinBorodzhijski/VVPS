package com.example.demo.service;

import com.example.demo.model.AppUser;
import com.example.demo.model.Train;
import com.example.demo.repositories.TrainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TrainServiceTest {


    @Mock
    private TrainRepository trainRepository;

    @InjectMocks
    private TrainService trainService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTrainCreation()
    {
        LocalDateTime timeOfDeparture = LocalDateTime.parse("2023-03-24T19:30");
        String departureStation = "Burgas";
        String arrivalStation = "Plovdiv";


        when(trainRepository.existsByArrivalStationAndDepartureStationAndTimeOfDeparture(arrivalStation,departureStation,timeOfDeparture)).thenReturn(false);
        when(trainRepository.save(any())).thenReturn(new Train(1,timeOfDeparture,departureStation,arrivalStation,1,1,new ArrayList<>()));

        Train newTrain = trainService.createTrain(timeOfDeparture,departureStation,arrivalStation,1,1);

        assertNotNull(newTrain);
        assertEquals(timeOfDeparture, newTrain.getTimeOfDeparture());
        assertEquals(departureStation, newTrain.getDepartureStation());
        assertEquals(arrivalStation, newTrain.getArrivalStation());

    }
    
}