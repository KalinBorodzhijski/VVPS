package com.example.demo.controller;


import com.example.demo.repositories.ReservationRepository;
import com.example.demo.service.ReservationService;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Reservation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    ReservationService reservationService;
    ReservationRepository reservationRepository;

    public ReservationController(ReservationService reservationService, ReservationRepository reservationRepository){
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
    }

    @PostMapping("")
    public ResponseEntity<Reservation> createReservation(@RequestParam String username) {
        return new ResponseEntity<>(reservationService.createReservation(username), HttpStatus.OK);
    }


    @GetMapping()
    public ResponseEntity<List<Reservation>> getAll(){
        return new ResponseEntity<>(reservationRepository.findAll(),HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<List<Reservation>> getByUsername(@PathVariable String username){
        return new ResponseEntity<>(reservationService.getAllByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Reservation> getByID(@PathVariable long id){
        return new ResponseEntity<>(reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation does not exists!")), HttpStatus.OK);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Reservation> deleteReservation(@PathVariable long id){
        return new ResponseEntity<>(reservationService.deleteReservation(id), HttpStatus.OK);
    }

}
