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
public class AccessToken {
    @Id
    @Column(length = 20)
    private String token;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private String scope;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "login_id", nullable = false)
    private String loginId;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    public AccessToken(String token, String userId, String loginId, String clientId, String scope) {
        this.token = token;
        this.userId = userId;
        this.loginId = loginId;
        this.clientId = clientId;
        this.scope = scope;
        this.issuedAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusHours(1);
    }
} 