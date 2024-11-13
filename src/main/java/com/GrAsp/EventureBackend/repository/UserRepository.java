package com.GrAsp.EventureBackend.repository;

import com.GrAsp.EventureBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmailContainsIgnoreCase(String email);
}
