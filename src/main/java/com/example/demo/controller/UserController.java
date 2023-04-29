package com.example.demo.controller;

import com.example.demo.model.AppUser;
import com.example.demo.model.UpdateUserParams;
import com.example.demo.repositories.AppUserRepository;
import com.example.demo.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    AppUserRepository appUserRepository;
    AppUserService appUserService;

    public UserController(AppUserRepository appUserRepository, AppUserService appUserService){
        this.appUserRepository = appUserRepository;
        this.appUserService = appUserService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AppUser>> getAllUsers(@RequestParam String username){
        return new ResponseEntity<>(appUserService.findAll(username), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<AppUser> getUserByUsername(@PathVariable String username){
        return new ResponseEntity<>(appUserService.findByName(username),HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<AppUser> createUser(@RequestParam String username, @RequestParam boolean hasAdminRights) {
        return new ResponseEntity<>(appUserService.createUser(username,hasAdminRights),HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    ResponseEntity<AppUser> deleteUser(@PathVariable String username) {
        return new ResponseEntity<>(appUserService.deleteUser(username),HttpStatus.OK);
    }

    @PutMapping()
    ResponseEntity<AppUser> updateUser(@RequestBody UpdateUserParams updateUserParams, @RequestParam Long accountID){
        return new ResponseEntity<>(appUserService.updateUser(updateUserParams,accountID),HttpStatus.OK);

    }

}
