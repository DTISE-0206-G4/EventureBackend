package com.GrAsp.EventureBackend.controller;

import com.GrAsp.EventureBackend.repository.RoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class SimpleController {
    private final RoleRepository roleRepository;

    public SimpleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping()
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("hehe");
    }
    @PreAuthorize("hasAuthority('SCOPE_Buyer')")
    @GetMapping("/buyer")
    public ResponseEntity<String> getBuyer() {
        return ResponseEntity.ok("Buyer only endpoint.");
    }
    @PreAuthorize("hasAuthority('SCOPE_Seller')")
    @GetMapping("/seller")
    public ResponseEntity<String> getSeller() {
        return ResponseEntity.ok("Seller only endpoint.");
    }

//    @PreAuthorize("hasAuthority('SCOPE_Seller')")
    @GetMapping("/role")
    public ResponseEntity<String> getRoles() {
        return ResponseEntity.ok(roleRepository.findAll().toString());
    }
}
