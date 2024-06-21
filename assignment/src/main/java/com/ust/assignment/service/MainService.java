package com.ust.assignment.service;


import com.ust.assignment.model.User;
import com.ust.assignment.repository.UserRepository;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MainService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public User addUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUserById(long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent())
            return user.get();
        else
            return null;
    }

    public User updateUser(User user){
        User x = getUserById(user.getId());
        if(x==null)
            return null;
        x.setUsername(user.getUsername());
        x.setPassword(passwordEncoder.encode(user.getPassword()));
        x.setRole(user.getRole());
        return userRepository.save(x);
    }

    public boolean deleteUser(long id){
        User x = getUserById(id);
        if(x==null)
            return false;

        userRepository.deleteById(id);
        return true;
    }


}
