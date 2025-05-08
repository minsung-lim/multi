package com.example.profile.controller;

import com.example.profile.model.User;
import com.example.profile.model.UserAuthRequestDTO;
import com.example.profile.model.UserResponseDTO;
import com.example.profile.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(userId, user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/loginId")
    public ResponseEntity<UserResponseDTO> getUserByLoginId(@RequestBody User user) {
        if (user.getLoginId() == null || user.getLoginId().trim().isEmpty()) {
            throw new IllegalArgumentException("loginId is required");
        }
        User foundUser = userService.getUserByLoginId(user.getLoginId());
        return ResponseEntity.ok(new UserResponseDTO(foundUser.getUserId()));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<User> authenticateUser(@RequestBody UserAuthRequestDTO authRequest) {
        if (authRequest.getLoginId() == null || authRequest.getLoginId().trim().isEmpty() ||
            authRequest.getPassword() == null || authRequest.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("loginId and password are required");
        }
        return ResponseEntity.ok(userService.authenticateUser(authRequest.getLoginId(), authRequest.getPassword()));
    }
} 