package net.engineeringdigest.journalApp.controllers;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepo;
import net.engineeringdigest.journalApp.services.UserService;
import net.engineeringdigest.journalApp.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    UserService userService;

    @Autowired
    UserRepo userRepo;

    @Autowired
    WeatherService weatherService;

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody User user){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User entity = userService.findByUsername(username);
        if(entity != null){
            entity.setUsername(user.getUsername());
            entity.setPassword(user.getPassword());
            userService.saveNewEntry(entity);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new  ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping()
    public ResponseEntity<User> deleteUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userRepo.deleteByUsername(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping()
    public ResponseEntity<?> greeting(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<>("Hi "+ username + " weather feels like " + weatherService.getWeather("bhopal").getCurrent().feelslike, HttpStatus.OK);
    }
}
