package com.example.demo.controller;

import com.example.demo.repositories.TicketRepository;
import com.example.demo.service.TicketManagementService;
import com.example.demo.model.Ticket;
import com.example.demo.model.TicketReservationDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    TicketRepository ticketRepository;
    TicketManagementService ticketManagementService;

    public TicketController(TicketRepository ticketRepository,TicketManagementService ticketManagementService){
        this.ticketRepository = ticketRepository;
        this.ticketManagementService = ticketManagementService;
    }

    @GetMapping()
    public ResponseEntity<List<Ticket>> getAllTickets(){
        return new ResponseEntity<>(ticketRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Ticket>> getAllTicketsByName(@PathVariable String name){
        return new ResponseEntity<>(ticketRepository.getAllByTicketOwner_Name(name), HttpStatus.OK);
    }

    @PostMapping("/reserveTicket")
    public ResponseEntity<Ticket> reserveTicket(@RequestBody TicketReservationDTO ticketReservationDTO) {
        return new ResponseEntity<>(ticketManagementService.reserveTicket(ticketReservationDTO),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable long id, @RequestBody TicketReservationDTO ticketReservationDTO, @RequestParam String username){
        return new ResponseEntity<>(ticketManagementService.updateTicket(id,ticketReservationDTO,username),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Ticket> deleteTicket(@PathVariable long id, @RequestParam String username){
        return new ResponseEntity<>(ticketManagementService.deleteTicket(id,username),HttpStatus.OK);
    }
}
