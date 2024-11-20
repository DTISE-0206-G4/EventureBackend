package com.GrAsp.EventureBackend.service;

import com.GrAsp.EventureBackend.dto.*;
import com.GrAsp.EventureBackend.model.Role;
import com.GrAsp.EventureBackend.model.User;
import com.GrAsp.EventureBackend.model.UserDiscount;
import com.GrAsp.EventureBackend.repository.RoleRepository;
import com.GrAsp.EventureBackend.repository.UserDiscountRepository;
import com.GrAsp.EventureBackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
    private final UserDiscountRepository userDiscountRepository;
//    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.roleRepository = roleRepository;
//    }

//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }

    public RegisterResponse createUser(RegisterRequest req) {

        User newUser = new User();
        newUser.setName(req.getName());
        newUser.setEmail(req.getEmail());
        newUser.setPassword(req.getPassword());
        Set<Role> roles = new HashSet<>();
        newUser.setRoles(roles);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        Optional<Role> defaultRole = roleRepository.findByName("ATTENDEE");
        if (defaultRole.isPresent()) {
            newUser.getRoles().add(defaultRole.get());
        } else {
            throw new RuntimeException("Default role not found");
        }

        if (req.getReferralCode() != null) {
            Optional<User> referrer = userRepository.findByReferralCode(req.getReferralCode());
            if (referrer.isPresent()) {
                newUser.setReferrerId(referrer.get().getId());
                try {
                    newUser = userRepository.save(newUser);
                    UserDiscount userDiscount = new UserDiscount();
                    userDiscount.setUserId(newUser.getId());
                    userDiscount.setName("Referral Discount");
                    userDiscount.setDescription("10% discount for using a referral code");
                    userDiscount.setAmount(10);
                    userDiscount.setPercentage(true);
                    userDiscountRepository.save(userDiscount);

                    UserDiscount userDiscountReferrer = new UserDiscount();
                    userDiscountReferrer.setUserId(referrer.get().getId());
                    userDiscountReferrer.setName("Referral Discount");
                    userDiscountReferrer.setDescription("10000 points for referring a user");
                    userDiscountReferrer.setAmount(10000);
                    userDiscountReferrer.setPercentage(false);
                    userDiscountRepository.save(userDiscountReferrer);
                } catch (Exception e) {
                    throw new RuntimeException("Can't save user, " + e.getMessage());
                }
            } else {
                throw new RuntimeException("Referral code not found");
            }
        } else {
            try {
                newUser = userRepository.save(newUser);
            } catch (Exception e) {
                throw new RuntimeException("Can't save user, " + e.getMessage());
            }
        }

        return new RegisterResponse(newUser.getEmail(), newUser.getCreatedAt(), newUser.getUpdatedAt(), newUser.getDeletedAt());
    }

    public boolean changePassword(ChangePasswordRequest req, String email) {
        return userRepository.findByEmailContainsIgnoreCase(email).map(user -> {
            user.setPassword(passwordEncoder.encode(req.getNewPassword()));
            try {
                userRepository.save(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }).orElse(false);
    }

    public UserProfileResponse getProfile(String email) {
        Optional<User> user = userRepository.findByEmailContainsIgnoreCase(email);
        if (user.isEmpty() || user.get().getDeletedAt() != null) {
            return null;
        }
        UserProfileResponse userProfileResponse = new UserProfileResponse();
        userProfileResponse.setId(user.get().getId());
        userProfileResponse.setName(user.get().getName());
        userProfileResponse.setEmail(user.get().getEmail());
        userProfileResponse.setDescription(user.get().getDescription());
        userProfileResponse.setProfileImage(user.get().getProfileImage());
        userProfileResponse.setReferralCode(user.get().getReferralCode());
        userProfileResponse.setRoles(user.get().getRoles());
        userProfileResponse.setCreatedAt(user.get().getCreatedAt());
        userProfileResponse.setUpdatedAt(user.get().getUpdatedAt());
        userProfileResponse.setDeletedAt(user.get().getDeletedAt());
        return userProfileResponse;
    }

    public boolean setProfile(EditUserProfileRequest req, String email) {
        return userRepository.findByEmailContainsIgnoreCase(email).map(user -> {
            user.setName(req.getName() != null ? req.getName() : user.getName());
            user.setDescription(req.getDescription() != null ? req.getDescription() : user.getDescription());
            user.setProfileImage(req.getProfileImage() != null ? req.getProfileImage() : user.getProfileImage());
            try {
                userRepository.save(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }).orElse(false);

    }
}
