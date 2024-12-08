package com.GrAsp.EventureBackend.repository;

import com.GrAsp.EventureBackend.model.UserDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserDiscountRepository extends JpaRepository<UserDiscount,Integer> {
    Optional<UserDiscount> findByCode(String code);
    List<UserDiscount> findUserDiscountsByUserId(Integer userId);
    @Query("SELECT ud FROM UserDiscount ud WHERE ud.userId = :userId AND ud.isUsed = false AND (ud.expiredAt IS NULL OR ud.expiredAt > CURRENT_TIMESTAMP)")
    List<UserDiscount> findActiveUserDiscountsByUserId(@Param("userId") Integer userId);
}
