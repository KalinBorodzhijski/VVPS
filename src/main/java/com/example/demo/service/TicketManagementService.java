package com.example.demo.service;

import com.example.demo.exception.TicketReservationException;
import com.example.demo.model.*;
import com.example.demo.repositories.AppUserRepository;
import com.example.demo.repositories.ReservationRepository;
import com.example.demo.repositories.TicketRepository;
import com.example.demo.repositories.TrainRepository;
import com.example.demo.exception.AccessException;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class TicketManagementService {

    TicketRepository ticketRepository;
    TrainRepository trainRepository;
    AppUserRepository appUserRepository;
    ReservationRepository reservationRepository;

    public TicketManagementService(TicketRepository ticketRepository, TrainRepository trainRepository, AppUserRepository appUserRepository,ReservationRepository reservationRepository){
        this.ticketRepository = ticketRepository;
        this.trainRepository = trainRepository;
        this.appUserRepository = appUserRepository;
        this.reservationRepository = reservationRepository;
    }

    public Ticket reserveTicket(TicketReservationDTO ticketReservationDTO) {
        AppUser user = checkIsUserValid(ticketReservationDTO.getAppUserName());
        Train train = checkIsTrainValid(ticketReservationDTO);
        Reservation reservation = checkIfReservationValid(ticketReservationDTO);
        checkTrainCapacity(train);


        if(!(reservation.getUser().getName().equals(ticketReservationDTO.getAppUserName())) && (!user.isHasAdminRights())){
            throw new AccessException("You don't have permission to add in this category!");
        }

        double ticketPrice = calculateTicketPrice(train,ticketReservationDTO);

        Ticket ticket = Ticket.builder().ticketOwner(user).train(train).price(ticketPrice).reservation(reservation).build();
        train.getTickets().add(ticket);
        reservation.getTickets().add(ticket);
        return ticketRepository.save(ticket);
    }

    public Ticket updateTicket(long id, TicketReservationDTO ticketReservationDTO, String username) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Ticket with ID %s not found!",id)));
        AppUser user = checkIsUserValid(username);

        if((!user.isHasAdminRights()) && (!ticket.getTicketOwner().getName().equals(ticketReservationDTO.getAppUserName()))){
            throw new AccessException("User %s dont have rights to change the ticket !");
        }

        Train train = checkIsTrainValid(ticketReservationDTO);
        checkTrainCapacity(train);
        double ticketPrice = calculateTicketPrice(train,ticketReservationDTO);
        Reservation reservation = checkIfReservationValid(ticketReservationDTO);

        if(!(reservation.getUser().getName().equals(ticketReservationDTO.getAppUserName())) && (!user.isHasAdminRights())){
            throw new AccessException("You don't have permission to add in this category!");
        }

        ticket.setTrain(train);
        ticket.setPrice(ticketPrice);
        ticket.setReservation(reservation);
        return ticketRepository.save(ticket);
    }

    public Ticket deleteTicket(long id, String username) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Ticket with ID %s not found!",id)));
        AppUser user = checkIsUserValid(username);

        if((!user.isHasAdminRights()) && (!ticket.getTicketOwner().getName().equals(username))){
            throw new AccessException("User %s dont have rights to change the ticket !");
        }

        ticketRepository.delete(ticket);
        return ticket;
    }

    private Reservation checkIfReservationValid(TicketReservationDTO ticketReservationDTO) {
        return reservationRepository.findById(ticketReservationDTO.getReservationID())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found!"));
    }


    private void checkTrainCapacity(Train train) {
        if(train.getTickets().size() >= train.getTrainCapacity())
        {
            throw new TicketReservationException("This train is full and there are no more tickets!");
        }
    }

    private Train checkIsTrainValid(TicketReservationDTO ticketReservationDTO) {
        Train train = trainRepository.findByArrivalStationAndDepartureStationAndTimeOfDeparture(
                    ticketReservationDTO.getArrivalStation(),
                    ticketReservationDTO.getDepartureStation(),
                    ticketReservationDTO.getDepartureTime());

        if(train != null)
        {
            return train;
        }

        throw new IllegalArgumentException(
                String.format("Train with parameters (departure time: %s, departure station: %s and arrival station: %s)does not exist",
                        ticketReservationDTO.getDepartureTime().toString(),
                        ticketReservationDTO.getDepartureStation(),
                        ticketReservationDTO.getArrivalStation()));

    }

    private AppUser checkIsUserValid(String appUserName) {

        AppUser user = appUserRepository.findByName(appUserName);

        if(user != null)
        {
            return user;
        }
        throw new IllegalArgumentException(String.format("User with username %s does not exists!",appUserName));
    }

    private double calculateTicketPrice(Train train, TicketReservationDTO ticketReservationDTO){

        double ticketPrice = 0;
        boolean isInRushHours = false;
        double cardPercentDiscount = 0;

        ticketPrice = calculateDistanceBasedPrice(train);
        isInRushHours = isInRushHours(train.getTimeOfDeparture());
        cardPercentDiscount = getCardDiscounts(ticketReservationDTO);

        if(!isInRushHours){
            cardPercentDiscount += 0.05;
        }

        ticketPrice = ticketPrice * (1 - cardPercentDiscount);

        if (!ticketReservationDTO.isOneWay()) {
            ticketPrice = ticketPrice * 2.0;
        }

        return ticketPrice;

    }

    private double getCardDiscounts(TicketReservationDTO ticketReservationDTO) {

        if(ticketReservationDTO.isElder()){
            return 0.34;
        }

        if(ticketReservationDTO.isWithChild()){
            if(ticketReservationDTO.isHasFamilyCard()){
                return 0.5;
            }
            return 0.1;
        }

        return 0;
    }

    private boolean isInRushHours(LocalDateTime timeOfDeparture) {

        return (timeOfDeparture.isAfter(timeOfDeparture.with(LocalTime.of(7, 30))) &&
                timeOfDeparture.isBefore(timeOfDeparture.with(LocalTime.of(9, 30)))) ||
                (timeOfDeparture.isAfter(timeOfDeparture.with(LocalTime.of(16, 0))) &&
                 timeOfDeparture.isBefore(timeOfDeparture.with(LocalTime.of(19, 30))));
    }

    private double calculateDistanceBasedPrice(Train train) {
        double ticketPrice = 0;

        if (train.getDistance() < 0) {
            throw new IllegalArgumentException("Invalid cities: " + train.getDepartureStation() + " - " + train.getArrivalStation());
        }
        if (train.getDistance() < 100) {
            ticketPrice = 10;
        } else if (train.getDistance() < 500) {
            ticketPrice = 0.1 * train.getDistance();
        } else {
            ticketPrice = 50 + 0.05 * (train.getDistance() - 500);
        }
        return ticketPrice;
    }

}
