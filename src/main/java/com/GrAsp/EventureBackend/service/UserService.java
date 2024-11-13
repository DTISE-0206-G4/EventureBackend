package com.GrAsp.EventureBackend.service;

import com.GrAsp.EventureBackend.model.Role;
import com.GrAsp.EventureBackend.model.User;
import com.GrAsp.EventureBackend.repository.RoleRepository;
import com.GrAsp.EventureBackend.repository.UserRepository;
import com.GrAsp.EventureBackend.dto.RegisterRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
//    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.roleRepository = roleRepository;
//    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(RegisterRequest req) {
        User newUser = new User();
        newUser.setName(req.getName());
        newUser.setEmail(req.getEmail());
        newUser.setPassword(req.getPassword());
        Set<Role> roles = new HashSet<>();
        newUser.setRoles(roles);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        Optional<Role> defaultRole = roleRepository.findByName("Buyer");
        if (defaultRole.isPresent()) {
            newUser.getRoles().add(defaultRole.get());
        } else {
            throw new RuntimeException("Default role not found");
        }

        try {
            userRepository.save(newUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newUser;
    }
}
