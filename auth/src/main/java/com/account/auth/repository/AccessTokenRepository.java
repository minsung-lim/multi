package com.account.auth.repository;

import com.account.auth.model.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessToken, String> {
    Optional<AccessToken> findByToken(String token);
}