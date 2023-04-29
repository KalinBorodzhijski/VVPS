package com.example.demo.controller;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Train;
import com.example.demo.repositories.TrainRepository;
import com.example.demo.service.TrainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/trains")
public class TrainController {


    TrainRepository trainRepository;
    TrainService trainService;

    public TrainController(TrainRepository trainRepository, TrainService trainService) {
        this.trainRepository = trainRepository;
        this.trainService = trainService;
    }

    @GetMapping()
    public ResponseEntity<List<Train>> getAllTrains(){
        return new ResponseEntity<>(trainRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Train> getTrainById(@PathVariable long id){
        return ResponseEntity.of(trainRepository.findById(id));
    }

    @GetMapping("/destination/{destination}")
    public ResponseEntity<List<Train>> getTrainsByDestination(@PathVariable String destination){
        List<Train> trains = trainRepository.findAllByArrivalStation(destination);
        if(trains.isEmpty()) throw new ResourceNotFoundException("No trains with this arrival destination!");
        return new ResponseEntity<>(trains, HttpStatus.OK);
    }

    @GetMapping("/departure/{departureTime}")
    public ResponseEntity<List<Train>> getTrainsByDepartureTime(@PathVariable String departureTime){
        LocalDateTime departTime = LocalDateTime.parse(departureTime);
        List<Train> trains = trainRepository.findAllByTimeOfDeparture(departTime);
        if(trains.isEmpty()) throw new ResourceNotFoundException("No trains with this departure time!");
        return new ResponseEntity<>(trains, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Train> createTrain(@RequestParam LocalDateTime timeOfDeparture,
                                               @RequestParam String departureStation,
                                               @RequestParam String arrivalStation,
                                               @RequestParam int trainCapacity,
                                               @RequestParam int distance) {
        return new ResponseEntity<>(trainService.createTrain(timeOfDeparture,departureStation,arrivalStation,trainCapacity,distance),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Train> deleteTrain(@PathVariable long id){
        return new ResponseEntity<>(trainService.deleteTrain(id),HttpStatus.OK);
    }


}
