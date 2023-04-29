package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repositories.AppUserRepository;
import com.example.demo.exception.AccessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppUserService {

    AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository)
    {
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public AppUser deleteUser(String username) {
        AppUser user = appUserRepository.findByName(username);
        if(user == null)
        {
            throw new IllegalArgumentException(String.format("No account with name %s found", username));
        }
        List<Reservation> reservations = user.getReservations();
        for (Reservation r: reservations) {
            List<Ticket> tickets = r.getTickets();
            for (Ticket t:tickets) {
                Train train = t.getTrain();
                train.getTickets().remove(t);
                r.getTickets().remove(t);
            }
            user.getReservations().remove(r);
        }
        appUserRepository.delete(user);
        return user;
    }

    public AppUser createUser(String username, boolean hasAdminRights) {

        if(appUserRepository.existsByNameEquals(username))
        {
            throw new IllegalArgumentException(String.format("User with username %s already exists!",username));
        }

        AppUser user = AppUser.builder().name(username).hasAdminRights(hasAdminRights).reservations(new ArrayList<>()).build();
        return appUserRepository.save(user);
    }

    public List<AppUser> findAll(String username) {
        AppUser user = appUserRepository.findByName(username);
        if(user == null)throw new ResourceNotFoundException("User not found");
        if(user.isHasAdminRights()) return appUserRepository.findAll();
        throw new AccessException("Only Admin users can access this!");
    }

    public AppUser findByName(String username) {
        AppUser user = appUserRepository.findByName(username);
        if(user == null)throw new ResourceNotFoundException("User not found");
        return user;
    }

    public AppUser updateUser(UpdateUserParams updateUserParams, Long accountID) {
        AppUser user = appUserRepository.findByName(updateUserParams.getUsername());
        AppUser loginUser = appUserRepository.findById(accountID).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(user == null)throw new ResourceNotFoundException("User not found");
        if(loginUser.isHasAdminRights())
        {
            user.setName(updateUserParams.getNewUsername());
            return appUserRepository.save(user);
        }
        throw new AccessException("Only Admin users can access this!");
    }
}
