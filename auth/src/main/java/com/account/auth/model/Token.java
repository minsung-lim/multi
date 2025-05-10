package com.account.auth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@NoArgsConstructor
public class Token {
    @Id
    @Column(name = "token", length = 100, nullable = false)
    private String token;

    @Column(name = "client_id", length = 100, nullable = false)
    private String clientId;

    @Column(name = "login_id", length = 100, nullable = false)
    private String loginId;

    @Column(name = "user_id", length = 100, nullable = false)
    private String userId;

    @Column(name = "access_token", length = 500, nullable = false)
    private String accessToken;

    @Column(name = "refresh_token", length = 500)
    private String refreshToken;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Column(name = "scope", length = 100, nullable = false)
    private String scope;

    @Column(name = "create_datetime")
    private LocalDateTime createDatetime;

    @PrePersist
    protected void onCreate() {
        createDatetime = LocalDateTime.now();
        if (issuedAt == null) {
            issuedAt = LocalDateTime.now();
        }
        if (scope == null) {
            scope = "read write"; // 기본 스코프 설정
        }
    }
} 