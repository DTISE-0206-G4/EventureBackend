package com.GrAsp.EventureBackend.security.service;

import com.GrAsp.EventureBackend.model.User;
import com.GrAsp.EventureBackend.repository.UserRepository;
import com.GrAsp.EventureBackend.dto.UserAuth;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User existingUser = userRepository.findByEmailContainsIgnoreCase(username).orElseThrow(() -> new RuntimeException("User not found with email: " + username));
//        log.info( "User found with email: " + existingUser.getEmail());
//        log.info( "User found with password: " + existingUser.getPassword());
        UserAuth userAuth = new UserAuth();
        userAuth.setUser(existingUser);
        return userAuth;
    }
}
