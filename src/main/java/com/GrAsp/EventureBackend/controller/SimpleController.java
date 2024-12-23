package com.GrAsp.EventureBackend.controller;

import com.GrAsp.EventureBackend.repository.RoleRepository;
import com.GrAsp.EventureBackend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class SimpleController {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public SimpleController(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @GetMapping()
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("hehe");
    }
    @PreAuthorize("hasAuthority('SCOPE_ATTENDEE')")
    @GetMapping("/buyer")
    public ResponseEntity<String> getBuyer() {
        return ResponseEntity.ok("Buyer only endpoint.");
    }

    @PreAuthorize("hasAuthority('SCOPE_ORGANIZER')")
    @GetMapping("/seller")
    public ResponseEntity<String> getSeller() {
        return ResponseEntity.ok("Seller only endpoint.");
    }

//    @PreAuthorize("hasAuthority('SCOPE_Seller')")
    @GetMapping("/role")
    public ResponseEntity<String> getRoles() {
        return ResponseEntity.ok(roleRepository.findAll().toString());
    }

    @GetMapping("/users")
    public ResponseEntity<String> getUsers() {
        return ResponseEntity.ok(userRepository.findAll().toString());
    }
}
