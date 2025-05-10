package com.account.auth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "codes")
@Getter
@Setter
@NoArgsConstructor
public class Code {
    @Id
    @Column(name = "code", length = 30, nullable = false)
    private String code;

    @Column(name = "user_id", length = 100, nullable = false)
    private String userId;

    @Column(name = "create_datetime")
    private LocalDateTime createDatetime;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @PrePersist
    protected void onCreate() {
        createDatetime = LocalDateTime.now();
    }

    public Code(String code) {
        this.code = code;
        this.createDatetime = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusMinutes(5);
    }
} 