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
    @Column(length = 30)
    private String code;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "create_datetime")
    private LocalDateTime createDatetime;

    public Code(String code) {
        this.code = code;
        this.createDatetime = LocalDateTime.now();
    }
} 