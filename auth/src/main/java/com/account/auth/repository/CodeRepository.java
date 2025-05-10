package com.account.auth.repository;

import com.account.auth.model.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CodeRepository extends JpaRepository<Code, Long> {
    Optional<Code> findByCode(String code);
    
    void deleteByUserId(String userId);

    List<Code> findTop10ByExpiresAtBeforeOrderByExpiresAtAsc(LocalDateTime expiresAt);
} 