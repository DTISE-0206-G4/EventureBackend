package com.GrAsp.EventureBackend.service;

import com.GrAsp.EventureBackend.model.UserDiscount;
import com.GrAsp.EventureBackend.repository.UserDiscountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserDiscountService {
    private final UserDiscountRepository userDiscountRepository;

    public List<UserDiscount> getUserDiscounts(Integer userId) {
        return userDiscountRepository.findUserDiscountsByUserId(userId);
    }
}
