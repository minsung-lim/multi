package com.account.auth.repository;

import com.account.auth.model.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, String> {
    Optional<AccessToken> findByToken(String token);
    
    void deleteByUserIdAndClientId(String userId, String clientId);
    
    void deleteByUserId(String userId);
}