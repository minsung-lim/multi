package com.account.metadata.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "applications")
@Getter
@Setter
public class Application {
    @Id
    @Column(name = "app_id")
    private String appId;

    @Column(name = "app_name", nullable = false)
    private String appName;

    @ElementCollection
    @CollectionTable(name = "application_scopes", joinColumns = @JoinColumn(name = "app_id"))
    @Column(name = "scope")
    private Set<String> scopes;

    @Column(name = "secret_key", nullable = false)
    private String secretKey;

    @Column(name = "cipher_key", nullable = false)
    private String cipherKey;

    @Column(name = "redirect_uri")
    private String redirectUri;

    @Column(name = "grant_types")
    @ElementCollection
    @CollectionTable(name = "application_grant_types", joinColumns = @JoinColumn(name = "app_id"))
    private Set<String> grantTypes;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;
} 