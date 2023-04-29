package com.example.demo.service;

import com.example.demo.repositories.TrainRepository;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Train;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class TrainService {

    TrainRepository trainRepository;

    public TrainService(TrainRepository trainRepository){
        this.trainRepository = trainRepository;
    }

    public Train createTrain(LocalDateTime timeOfDeparture, String departureStation, String arrivalStation, int trainCapacity,int distance) {
        if(trainRepository.existsByArrivalStationAndDepartureStationAndTimeOfDeparture(arrivalStation,departureStation,timeOfDeparture))
        {
            throw new IllegalArgumentException(
                    String.format("Train with parameters (departure time: %s, departure station: %s and arrival station: %s) already exists",
                            timeOfDeparture.toString(),
                            departureStation,
                            arrivalStation));
        }

        Train train = Train.builder()
                .trainCapacity(trainCapacity)
                .departureStation(departureStation)
                .arrivalStation(arrivalStation)
                .timeOfDeparture(timeOfDeparture)
                .distance(distance)
                .tickets(new ArrayList<>()).build();

        return trainRepository.save(train);

    }

    public Train deleteTrain(long id) {

        Train train = trainRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Train not found !"));
        trainRepository.delete(train);
        return train;
    }
}
