package com.ust.assignment.controller;

import com.ust.assignment.model.LoginRequest;
import com.ust.assignment.model.User;
import com.ust.assignment.service.JwtUserDetailsService;
import com.ust.assignment.service.MainService;
import com.ust.assignment.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class MainController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    MainService service;

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JwtUserDetailsService jwtUserDetailsService;
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        User user = service.getUserById(id);
        if(user!=null)
            return ResponseEntity.status(200).body(user);
        return ResponseEntity.status(400).body("no such user");
    }

    @GetMapping("/user")
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.status(200).body(service.getUsers());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> addUser(@RequestBody User user){
        return ResponseEntity.status(201).body(service.addUser(user));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        Boolean success = service.deleteUser(id);
        if(success)
            return ResponseEntity.status(200).body("Delete successful");
        return ResponseEntity.status(404).body("Delete failed");

    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,@RequestBody User user){
        user.setId(id);
        service.updateUser(user);
        return ResponseEntity.status(200).body(user);
    }

    //admin only
    @GetMapping("/admin")
    public ResponseEntity<?> adminRoute(){
        return ResponseEntity.status(200).body("only admin can access");
    }

    //anyone
    @GetMapping("/anyone")
    public ResponseEntity<?> userRoute(){
        return ResponseEntity.status(200).body("anyone can access");
    }

    //login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req){
        //login credentials user provides are used to create a UsernamePasswordAuthenticationToken which
        //is then passed to AuthenticationManager when calling authenticate()
        //Behind the scenes, AuthenticationManager uses the custom UserDetailsService's
        //loadByUsername() to fetch user details from db and check if username and password matches
        // default DaoProvider or sth is the implementation ig.
        //Then we generate a jwt token on successful authentication
        //In the JwtConfig configuration class, we configure the security filter chain which applies
        //some authorization and methods to matching requests
        //and also specify the password encoder and configures authenticationManager to use our
        //UserDetailsService implementation
        try{
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(req.getUsername(),req.getPassword());
        authenticationManager.authenticate(authToken);
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(req.getUsername());
        String jwtToken = jwtUtil.generateToken(userDetails);
        return ResponseEntity.status(200).body(jwtToken);
        }
        catch (AuthenticationException e){
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
