package com.example.profile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(length = 10)
    private String userId;
    
    @Column(unique = true)
    private String loginId;
    
    private String name;
    private String phoneNumber;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonIgnore
    public boolean isEmpty() {
        return userId == null || userId.trim().isEmpty();
    }
} 