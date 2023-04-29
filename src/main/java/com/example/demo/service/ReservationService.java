package com.example.demo.service;

import com.example.demo.repositories.AppUserRepository;
import com.example.demo.repositories.ReservationRepository;
import com.example.demo.repositories.TicketRepository;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.AppUser;
import com.example.demo.model.Reservation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    ReservationRepository reservationRepository;
    AppUserRepository appUserRepository;
    TicketRepository ticketRepository;

    public ReservationService(ReservationRepository reservationRepository, AppUserRepository appUserRepository, TicketRepository ticketRepository){
        this.reservationRepository = reservationRepository;
        this.appUserRepository = appUserRepository;
        this.ticketRepository = ticketRepository;
    }

    public Reservation createReservation(String username) {
        AppUser user = checkIsUserValid(username);
        Reservation reservation = Reservation.builder().user(user).tickets(new ArrayList<>()).build();
        user.getReservations().add(reservation);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllByUsername(String username) {
        return reservationRepository.getAllByUser_Name(username);
    }

    public Reservation deleteReservation(long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found!"));
        ticketRepository.deleteAll(reservation.getTickets());
        reservationRepository.delete(reservation);
        return reservation;
    }

    private AppUser checkIsUserValid(String username) {

        AppUser user = appUserRepository.findByName(username);
        if(user != null) return user;
        throw new IllegalArgumentException(String.format("User with username %s does not exists!",username));
    }

}
