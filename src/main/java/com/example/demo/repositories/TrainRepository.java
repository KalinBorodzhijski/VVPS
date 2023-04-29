package com.example.demo.repositories;

import com.example.demo.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {
    List<Train> findAllByArrivalStation(String arrivalStation);
    List<Train> findAllByTimeOfDeparture(LocalDateTime timeOfDeparture);
    Train findByArrivalStationAndDepartureStationAndTimeOfDeparture(String arrivalStation, String departureStation, LocalDateTime timeOfDeparture);
    boolean existsByArrivalStationAndDepartureStationAndTimeOfDeparture(String arrivalStation, String departureStation, LocalDateTime timeOfDeparture);
}
