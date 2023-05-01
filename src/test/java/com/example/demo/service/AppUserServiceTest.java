package com.example.demo.service;

import com.example.demo.model.AppUser;
import com.example.demo.model.Reservation;
import com.example.demo.repositories.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

class AppUserServiceTest {

    @InjectMocks
    private AppUserService appUserService;

    @Mock
    private AppUserRepository appUserRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testUserCreation() {
        String newUserUsername = "Kalin";
        boolean newUserAdminRights = false;

        when(appUserRepository.save(any())).thenReturn(new AppUser(1,newUserUsername,newUserAdminRights));
        when(appUserRepository.findById(getUser().getUserID())).thenReturn(Optional.of(getUser()));

        AppUser appUserNew = appUserService.createUser(newUserUsername,newUserAdminRights);

        assertNotNull(appUserNew);
        assertEquals(newUserUsername, appUserNew.getName());
        assertFalse(appUserNew.isHasAdminRights());

    }

    @Test
    void testUserCreationWithAlreadyExistingName(){
        String newUserUsername = "Kalin";
        boolean newUserAdminRights = false;

        String expectedMessage = "User with username Kalin already exists!";

        when(appUserRepository.existsByNameEquals(getUser().getName())).thenReturn(true);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            AppUser appUserNew = appUserService.createUser(newUserUsername,newUserAdminRights);
        });

        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDeleteByID(){
        AppUser appUser = getUser();
        List<Reservation> reservations = createReservations();
        appUser.setReservations(reservations);

        when(appUserRepository.findByName(appUser.getName())).thenReturn(appUser);
        appUserService.deleteUser("Kalin");
        assertEquals(Collections.emptyList(), appUser.getReservations());

    }

    private List<Reservation> createReservations() {
        Reservation a = new Reservation(1,getUser(),new ArrayList<>());
        Reservation b = new Reservation(2,getUser(),new ArrayList<>());
        return List.of(a,b);
    }

    private AppUser getUser(){
        return new AppUser(1, "Kalin",true);
    }

}