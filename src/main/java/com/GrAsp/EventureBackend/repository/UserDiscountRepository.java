package com.GrAsp.EventureBackend.repository;

import com.GrAsp.EventureBackend.model.UserDiscount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDiscountRepository extends JpaRepository<UserDiscount,Integer> {
    Optional<UserDiscount> findByCode(String code);
    List<UserDiscount> findUserDiscountsByUserId(Integer userId);
}
