package com.account.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Profile {
    private String userId;
    private String loginId;
    private String name;
    private String email;
} 