package com.ust.assignment.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import com.ust.assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.ust.assignment.model.User user = userRepository.findByUsername(username);
        if(user==null)
            throw new UsernameNotFoundException(username);
        Collection<GrantedAuthority> roles = new ArrayList<>();
    if (user.getRole().equals("user")) roles.add(new SimpleGrantedAuthority("ROLE_USER"));
    else roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
         User userDetails = new User(user.getUsername(),user.getPassword(),roles);
        return userDetails;
    }
}
