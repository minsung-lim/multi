package com.account.auth.repository;

import com.account.auth.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    void deleteByLoginIdAndClientId(String loginId, String clientId);
    void deleteByLoginId(String loginId);
}