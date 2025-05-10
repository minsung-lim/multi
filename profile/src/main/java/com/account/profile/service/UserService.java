package com.account.profile.service;

import com.account.profile.model.User;
import com.account.profile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int USER_ID_LENGTH = 10;
    private final Random random = new Random();

    private String generateRandomUserId() {
        StringBuilder userId = new StringBuilder(USER_ID_LENGTH);
        for (int i = 0; i < USER_ID_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            userId.append(CHARACTERS.charAt(index));
        }
        return userId.toString();
    }

    private String generateUniqueUserId() {
        String userId;
        do {
            userId = generateRandomUserId();
        } while (userRepository.findByUserId(userId).isPresent());
        return userId;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found with userId: " + userId));
    }

    public User getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("No user exists with the provided loginId: " + loginId));
    }

    public User authenticateUser(String loginId, String password) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("Invalid loginId or password"));
        
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid loginId or password");
        }
        
        return user;
    }

    @Transactional
    public User createUser(User user) {
        user.setUserId(generateUniqueUserId());
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(String userId, User userDetails) {
        User user = getUserByUserId(userId);
        user.setName(userDetails.getName());
        user.setLoginId(userDetails.getLoginId());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        if (userDetails.getPassword() != null && !userDetails.getPassword().trim().isEmpty()) {
            user.setPassword(userDetails.getPassword());
        }
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String userId) {
        User user = getUserByUserId(userId);
        userRepository.delete(user);
    }
} 